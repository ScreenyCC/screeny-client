package screeny.client.process;

import io.netty.channel.Channel;
import lombok.Getter;
import screeny.client.ScreenyClient;
import screeny.client.audio.AudioPlayer;
import screeny.client.config.ConnectionConfig;
import screeny.client.protocol.NettyClientConnector;
import screeny.client.protocol.NettyClientConnectorHandler;
import screeny.client.shots.ScreenShotTaker;
import screeny.client.trayicon.TrayIconManager;
import screeny.protocol.listener.PacketHandler;
import screeny.protocol.listener.PacketListener;
import screeny.protocol.netty.NettyException;
import screeny.protocol.netty.client.NettyClient;
import screeny.protocol.packet.packets.client.ClientPacketUploadScreen;
import screeny.protocol.packet.packets.server.ServerPacketLoginDataResult;
import screeny.protocol.packet.packets.server.ServerPacketScreenUploaded;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TakeScreenShotProcess implements NettyClientConnectorHandler, PacketListener {
    @Getter
    private static TakeScreenShotProcess currentTakeScreenshotProcess;
    private ScreenShotTaker screenShotTaker;
    private File imageFile;
    private NettyClientConnector nettyClientConnector;

    public TakeScreenShotProcess( ScreenShotTaker screenShotTaker ) {
        this.screenShotTaker = screenShotTaker;
        this.nettyClientConnector = new NettyClientConnector( ScreenyClient.getClientSettingsStore().getServerAddress(), ConnectionConfig.getPORT(), this );
    }

    public void start() {
        synchronized ( TakeScreenShotProcess.class ) {
            currentTakeScreenshotProcess = this;
        }
        ScreenyClient.getExecutor().execute( () -> {
            screenShotTaker.startAndWait();
            imageFile = screenShotTaker.getImgageFile();
            connect();
        } );
    }

    public void connect() {
        this.nettyClientConnector.setHost( ScreenyClient.getClientSettingsStore().getServerAddress() );
        this.nettyClientConnector.connect();
        this.nettyClientConnector.getNettyClient().addPacketListener( this );
    }

    public void upload( NettyClient nettyClient ) {
        // Upload
        try {
            nettyClient.sendPacket( new ClientPacketUploadScreen( Files.readAllBytes( Paths.get( imageFile.toURI() ) ) ) );
        } catch ( NettyException e ) {
            // NOP
        } catch ( IOException e ) {
            // NOP
        }
        synchronized ( TakeScreenShotProcess.class ) {
            if ( currentTakeScreenshotProcess != null && currentTakeScreenshotProcess.equals( this ) ) {
                currentTakeScreenshotProcess = null;
            }
        }
    }

    @Override
    public void clientConnected( NettyClient nettyClient ) {
        // Login
        LoginDataChecker loginDataChecker = new LoginDataChecker( ScreenyClient.getClientSettingsStore().getSessionID(), ScreenyClient.getClientSettingsStore().getUsername(), "" );
        try {
            ServerPacketLoginDataResult serverPacketLoginDataResult = loginDataChecker.check( nettyClient );
            if ( serverPacketLoginDataResult.getSessionID().isEmpty() ) {
                // Login again
                TrayIconManager.displayMessage( "Fehler", "Sitzung ist nicht mehr gültig, bitte neu einloggen!" );
                StartupProcess.startLoginProcess();
            } else {
                // Start upload
                upload( nettyClient );
            }
        } catch ( Exception e ) {
            handleException( e );
        }
    }

    @Override
    public void handleException( Throwable throwable ) {
        TrayIconManager.displayMessage( "Fehler", "Fehler beim Upload, versuche erneut in 2 Sekunden!" );
        try {
            Thread.sleep( 2000 );
            connect();
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive( Channel channel ) {

    }

    @Override
    public void channelDisconnect( Channel channel ) {

    }

    @PacketHandler
    public void onServerPacketScreenUploaded( ServerPacketScreenUploaded serverPacketScreenUploaded ) {
        this.nettyClientConnector.disconnect();
        this.imageFile.delete();
        TrayIconManager.displayMessage( "screeny!", serverPacketScreenUploaded.getUrl() );

        if ( ScreenyClient.getClientSettingsStore().isCopyLinkToClipboard() ) {
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents( new StringSelection( serverPacketScreenUploaded.getUrl() ), null );
        }

        if ( ScreenyClient.getClientSettingsStore().isPlayNotificationSoundOnScreen() ) {
            new AudioPlayer( TakeScreenShotProcess.class.getResourceAsStream( "/success.wav" ) ).start();
        }

        if ( ScreenyClient.getClientSettingsStore().isOpenScreenshotInBrowser() ) {
            try {
                Desktop.getDesktop().browse( new URI( serverPacketScreenUploaded.getUrl() ) );
            } catch ( IOException e ) {
                e.printStackTrace();
            } catch ( URISyntaxException e ) {
                e.printStackTrace();
            }
        }
    }
}

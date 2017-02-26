package screeny.client.process;

import screeny.client.ScreenyClient;
import screeny.protocol.netty.NettyAnswerPacketTimeout;
import screeny.protocol.netty.NettyException;
import screeny.protocol.netty.client.NettyClient;
import screeny.protocol.packet.packets.client.ClientPacketCheckLoginData;
import screeny.protocol.packet.packets.server.ServerPacketLoginDataResult;

public class LoginDataChecker {
    private String sessionID;
    private String username;
    private String password;

    public LoginDataChecker( String sessionID, String username, String password ) {
        this.sessionID = sessionID;
        this.username = username;
        this.password = password;
    }

    public ServerPacketLoginDataResult check( NettyClient nettyClient ) throws NettyAnswerPacketTimeout, NettyException, LoginDataCheckerException {
        ClientPacketCheckLoginData clientPacketCheckLoginData = new ClientPacketCheckLoginData();
        clientPacketCheckLoginData.setUsername( this.username );
        if ( !this.sessionID.isEmpty() ) {
            clientPacketCheckLoginData.setSessionID( this.sessionID );
        } else {
            clientPacketCheckLoginData.setPassword( this.password );
        }
        ServerPacketLoginDataResult serverPacketLoginDataResult = ( ServerPacketLoginDataResult ) nettyClient.sendAndGetResponse( nettyClient.getChannel(), clientPacketCheckLoginData );
        if ( serverPacketLoginDataResult == null ) {
            throw new NettyException( "Received answer packet is null" );
        }
        if ( serverPacketLoginDataResult.getSessionID().isEmpty() ) {
            throw new LoginDataCheckerException( "Username, password or sessionid is invalid!" );
        }
        ScreenyClient.getClientSettingsStore().setUsername( this.username );
        ScreenyClient.getClientSettingsStore().setSessionID( serverPacketLoginDataResult.getSessionID() );
        ScreenyClient.getClientSettingsStore().save();
        return serverPacketLoginDataResult;
    }

    public class LoginDataCheckerException extends Exception {
        public LoginDataCheckerException( String message ) {
            super( message );
        }
    }
}

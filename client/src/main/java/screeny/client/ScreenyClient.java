package screeny.client;

import lombok.Getter;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import screeny.client.key.KeyListener;
import screeny.client.key.ScreenKeyManager;
import screeny.client.process.StartupProcess;
import screeny.client.store.UserSettingsStore;
import screeny.client.trayicon.TrayIconManager;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenyClient {
    @Getter
    private static final UserSettingsStore clientSettingsStore = new UserSettingsStore( "screenerData" );
    @Getter
    private static final ExecutorService executor = Executors.newFixedThreadPool( 100 );

    public static void main( String[] args ) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        try {
            GlobalScreen.registerNativeHook();
        } catch ( NativeHookException e ) {
            e.printStackTrace();
        }

        KeyListener.disableNativeKeyListenerLogger();

        clientSettingsStore.load();
        ScreenKeyManager.setupKeyListeners();
        StartupProcess.start();

        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            getClientSettingsStore().save();
            TrayIconManager.disable();
        } ) );
    }
}

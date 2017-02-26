package screeny.client.process;

import lombok.Setter;
import screeny.client.ScreenyClient;
import screeny.client.gui.LoginGui;
import screeny.client.trayicon.TrayIconManager;

import java.awt.*;

public class StartupProcess {
    @Setter
    private static LoginGui loginGui;

    public static void start() {
        if ( !ScreenyClient.getClientSettingsStore().getSessionID().isEmpty() ) {
            LoginDataCheckerStandalone loginDataCheckerStandalone = new LoginDataCheckerStandalone( ScreenyClient.getClientSettingsStore().getSessionID(), ScreenyClient.getClientSettingsStore().getUsername(), "", new LoginDataCheckerStandalone.LoginDataCheckerResponse() {
                @Override
                public void success() {
                    // Startup normal
                    startupNormal();
                }

                @Override
                public void exception( Throwable throwable ) {
                    // Start login window
                    throwable.printStackTrace();
                    startLoginProcess();
                }
            } );
            loginDataCheckerStandalone.check();
        } else {
            startLoginProcess();
        }
    }

    public static void startupNormal() {
        try {
            if ( !TrayIconManager.isSetup() ) {
                TrayIconManager.setup();
            }
        } catch ( AWTException e ) {
            // NOP
        }
        TrayIconManager.displayMessage( "Info", "screeny wurde erfolgreich gestartet!" );
    }

    public static synchronized void startLoginProcess() {
        if ( loginGui == null ) {
            loginGui = new LoginGui();
            loginGui.start();
        }
    }
}

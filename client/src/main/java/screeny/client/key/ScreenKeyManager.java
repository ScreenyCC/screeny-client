package screeny.client.key;

import lombok.Getter;
import screeny.client.ScreenyClient;
import screeny.client.process.TakeScreenShotProcess;
import screeny.client.shots.AreaCaptureShot;
import screeny.client.store.UserSettingsStore;

public class ScreenKeyManager {
    @Getter
    private static KeyListener captureAreaKeyListener;

    public static synchronized void setupKeyListeners() {
        UserSettingsStore userSettingsStore = ScreenyClient.getClientSettingsStore();
        if ( userSettingsStore.getKeyBindingCaptureArea().isEmpty() && captureAreaKeyListener != null ) {
            captureAreaKeyListener.unregister();
            captureAreaKeyListener = null;
        } else if ( !userSettingsStore.getKeyBindingCaptureArea().isEmpty() && captureAreaKeyListener == null ) {
            captureAreaKeyListener = new KeyListener( userSettingsStore.getKeyBindingCaptureArea(), ( keyListener, pressedKey ) -> {
                synchronized ( TakeScreenShotProcess.class ) {
                    if ( TakeScreenShotProcess.getCurrentTakeScreenshotProcess() == null ) {
                        new TakeScreenShotProcess( new AreaCaptureShot() ).start();
                    }
                }
            } );
            captureAreaKeyListener.register();
        } else if ( !userSettingsStore.getKeyBindingCaptureArea().isEmpty() && captureAreaKeyListener != null ) {
            captureAreaKeyListener.setListenKey( userSettingsStore.getKeyBindingCaptureArea() );
            System.out.println( "Set key to " + userSettingsStore.getKeyBindingCaptureArea() );
        }
    }
}

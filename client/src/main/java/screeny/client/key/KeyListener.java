package screeny.client.key;

import lombok.Setter;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class KeyListener implements NativeKeyListener {
    private static List<KeyListener> registeredListeners = new ArrayList<>();
    @Setter
    private String listenKey;
    private KeyListenerCallback keyListenerCallback;

    public KeyListener( String listenKey, KeyListenerCallback keyListenerCallback ) {
        this.listenKey = listenKey;
        this.keyListenerCallback = keyListenerCallback;
    }

    public static void disableNativeKeyListenerLogger() {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger( GlobalScreen.class.getPackage().getName() );
        logger.setLevel( Level.OFF );
    }

    @Override
    public void nativeKeyTyped( NativeKeyEvent nativeKeyEvent ) {

    }

    @Override
    public void nativeKeyPressed( NativeKeyEvent nativeKeyEvent ) {
        synchronized ( this ) {
            synchronized ( registeredListeners ) {
                if ( !registeredListeners.contains( this ) ) {
                    return;
                }
            }
            String pressedKey = NativeKeyEvent.getKeyText( nativeKeyEvent.getKeyCode() );
            if ( listenKey.isEmpty() || listenKey.toUpperCase().equals( pressedKey ) ) {
                keyListenerCallback.keyPressed( this, pressedKey );
            }
        }
    }

    @Override
    public void nativeKeyReleased( NativeKeyEvent nativeKeyEvent ) {

    }

    public void register() {
        synchronized ( registeredListeners ) {
            if ( registeredListeners.size() == 0 ) {
                try {
                    GlobalScreen.registerNativeHook();
                } catch ( NativeHookException e ) {
                    e.printStackTrace();
                }
            }
            registeredListeners.add( this );
        }
        GlobalScreen.addNativeKeyListener( this );
    }

    public void unregister() {
        GlobalScreen.removeNativeKeyListener( this );

        synchronized ( registeredListeners ) {
            registeredListeners.remove( this );
            if ( registeredListeners.size() == 0 ) {
                try {
                    GlobalScreen.unregisterNativeHook();
                } catch ( NativeHookException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface KeyListenerCallback {
        void keyPressed( KeyListener keyListener, String pressedKey );
    }
}

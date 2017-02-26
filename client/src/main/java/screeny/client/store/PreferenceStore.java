package screeny.client.store;

import java.util.prefs.Preferences;

public class PreferenceStore {
    public Preferences preferences;

    public PreferenceStore( String storeName ) {
        this.preferences = Preferences.userNodeForPackage( getClass() ).node( storeName );
    }

    public void storeString( String key, String value ) {
        this.preferences.put( key, value );
    }

    public String readString( String key, String defaultValue ) {
        return this.preferences.get( key, defaultValue );
    }

    public void storeBoolean( String key, boolean value ) {
        this.preferences.putBoolean( key, value );
    }

    public boolean readBoolean( String key, boolean defaultValue ) {
        return this.preferences.getBoolean( key, defaultValue );
    }
}

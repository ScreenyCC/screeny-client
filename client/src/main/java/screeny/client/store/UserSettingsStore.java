package screeny.client.store;

import lombok.Getter;
import lombok.Setter;
import screeny.client.config.ConnectionConfig;
import screeny.client.key.ScreenKeyManager;

@Getter
@Setter
public class UserSettingsStore extends PreferenceStore {
    private String sessionID;
    private String username;
    private String serverAddress;
    private boolean playNotificationSoundOnScreen;
    private boolean openScreenshotInBrowser;
    private boolean copyLinkToClipboard;
    private boolean saveLocalCopyOfScreen;
    private String localCopyLocation;
    private String keyBindingCaptureArea;


    public UserSettingsStore( String storeName ) {
        super( storeName );
    }

    public void load() {
        this.sessionID = readString( "sessionID", "" );
        this.username = readString( "username", "" );
        this.serverAddress = readString( "serverAddress", ConnectionConfig.getHOST() );
        this.playNotificationSoundOnScreen = readBoolean( "playNotificationSoundOnScreen", true );
        this.openScreenshotInBrowser = readBoolean( "openScreenshotInBrowser", false );
        this.copyLinkToClipboard = readBoolean( "copyLinkToClipboard", true );
        this.saveLocalCopyOfScreen = readBoolean( "saveLocalCopyOfScreen", false );
        this.localCopyLocation = readString( "localCopyLocation", "" );
        this.keyBindingCaptureArea = readString( "keyBindingCaptureArea", "" );
    }

    public void save() {
        storeString( "sessionID", this.sessionID );
        storeString( "username", this.username );
        storeString( "serverAddress", this.serverAddress );
        storeBoolean( "playNotificationSoundOnScreen", playNotificationSoundOnScreen );
        storeBoolean( "openScreenshotInBrowser", openScreenshotInBrowser );
        storeBoolean( "copyLinkToClipboard", copyLinkToClipboard );
        storeBoolean( "saveLocalCopyOfScreen", saveLocalCopyOfScreen );
        storeString( "localCopyLocation", localCopyLocation );
        storeString( "keyBindingCaptureArea", keyBindingCaptureArea );
    }

    public void setKeyBindingCaptureArea( String keyBindingCaptureArea ) {
        this.keyBindingCaptureArea = keyBindingCaptureArea;
        ScreenKeyManager.setupKeyListeners();
    }

}

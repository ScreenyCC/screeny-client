package screeny.client.gui;

import lombok.Getter;
import lombok.Setter;

public class GuiManager {
    @Getter
    @Setter
    private static UserSettings userSettings;

    public static void openUserSettings() {
        if ( userSettings == null ) {
            userSettings = new UserSettings();
            userSettings.start();
        } else {
            userSettings.toFront();
        }
    }
}

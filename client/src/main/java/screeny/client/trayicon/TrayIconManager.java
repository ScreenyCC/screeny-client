package screeny.client.trayicon;

import screeny.client.ScreenyClient;
import screeny.client.gui.GuiManager;
import screeny.client.process.TakeScreenShotProcess;
import screeny.client.shots.AreaCaptureShot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;

public class TrayIconManager {
    private static TrayIcon trayIcon;
    private static PopupMenu popupMenu;
    private static HashMap<MenuItemType, MenuItem> menuItems = new HashMap<>();

    public static void setup() throws AWTException {
        trayIcon = new TrayIcon( getIcon() );
        trayIcon.setImageAutoSize( true );
        trayIcon.setToolTip( "Screeny v0.1" );
        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.add( trayIcon );

        // Menu
        popupMenu = new PopupMenu();

        // Screen of selected area
        addMenuItem( MenuItemType.CAPTURE_AREA, "Bereich auswählen" );

        addActionListener( MenuItemType.CAPTURE_AREA, new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                // Start TakeScreenShotProcess
                setMenuEnabled( MenuItemType.CAPTURE_AREA, false );
                ScreenyClient.getExecutor().execute( new Runnable() {
                    @Override
                    public void run() {
                        synchronized ( TakeScreenShotProcess.class ) {
                            if ( TakeScreenShotProcess.getCurrentTakeScreenshotProcess() == null ) {
                                new TakeScreenShotProcess( new AreaCaptureShot() ).start();
                            }
                        }
                    }
                } );
            }
        } );

        // Seperator
        addSeperator();

        // Settings
        addMenuItem( MenuItemType.SETTINGS, "Einstellungen" );

        addActionListener( MenuItemType.SETTINGS, new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                GuiManager.openUserSettings();
            }
        } );

        // Seperator
        addSeperator();

        // Exit
        addMenuItem( MenuItemType.EXIT, "Beenden" );

        addActionListener( MenuItemType.EXIT, new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit( 0 );
            }
        } );

        trayIcon.setPopupMenu( popupMenu );

    }

    public static boolean isSetup() {
        return trayIcon != null;
    }

    private static void addMenuItem( MenuItemType menuItemType, String text ) {
        MenuItem menuItem = new MenuItem( text );
        menuItems.put( menuItemType, menuItem );
        popupMenu.add( menuItem );
    }

    private static void addSeperator() {
        popupMenu.addSeparator();
    }

    private static void addActionListener( MenuItemType menuItemType, ActionListener actionListener ) {
        menuItems.get( menuItemType ).addActionListener( actionListener );
    }

    private static Image getIcon() {
        URL imageURL = TrayIconManager.class.getResource( "/icon_16x16.png" );
        return ( new ImageIcon( imageURL ) ).getImage();
    }

    public static void setMenuEnabled( MenuItemType menuItemType, boolean enabled ) {
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                menuItems.get( menuItemType ).setEnabled( enabled );
            }
        } );
    }

    public static void displayMessage( String title, String message ) {
        trayIcon.displayMessage( title, message, TrayIcon.MessageType.NONE );
    }

    public static void disable() {
        if ( trayIcon != null ) {
            SystemTray.getSystemTray().remove( trayIcon );
            trayIcon = null;
            popupMenu = null;
            menuItems.clear();
        }
    }

    public enum MenuItemType {
        CAPTURE_AREA, SETTINGS, EXIT
    }
}

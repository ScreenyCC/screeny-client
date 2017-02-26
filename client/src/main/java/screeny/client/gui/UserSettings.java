package screeny.client.gui;

import screeny.client.ScreenyClient;
import screeny.client.key.KeyListener;
import screeny.client.process.StartupProcess;
import screeny.client.store.UserSettingsStore;
import screeny.client.trayicon.TrayIconManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserSettings extends javax.swing.JFrame {

    // Variables declaration - do not modify
    private javax.swing.JButton jBKeybindingSelectArea;
    private javax.swing.JButton jBLogout;
    private javax.swing.JButton jBSelectLocalCopyDirectory;
    private javax.swing.JCheckBox jCBAutostart;
    private javax.swing.JCheckBox jCBCopyLinkToClipboard;
    private javax.swing.JCheckBox jCBOpenScreenInBrowser;
    private javax.swing.JCheckBox jCBPlayNotificationSound;
    private javax.swing.JCheckBox jCBSaveLocalCopy;
    private javax.swing.JLabel jLKeybindingSelectArea;
    private javax.swing.JLabel jLLoggedInDescriptionn;
    private javax.swing.JLabel jLServerSettingsAddress;
    private javax.swing.JLabel jLSessionKey;
    private javax.swing.JLabel jLSessionKeyDescription;
    private javax.swing.JLabel jLSessionUsername;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanelAccountInformation;
    private javax.swing.JPanel jPanelAccountSection;
    private javax.swing.JPanel jPanelGeneralInGeneral;
    private javax.swing.JPanel jPanelGeneralSection;
    private javax.swing.JPanel jPanelKeybindings;
    private javax.swing.JPanel jPanelKeybindingsSection;
    private javax.swing.JPanel jPanelServerSection;
    private javax.swing.JPanel jPanelServerSettings;
    private javax.swing.JPanel jPanelSuccessfullScreenshotActionInGeneral;
    private javax.swing.JTextField jTFSaveCopyLocalDirectory;
    private javax.swing.JTextField jTFServerAddressField;
    private javax.swing.JTabbedPane jTabbedPaneSettings;
    /**
     * Creates new form UserSettings
     */
    public UserSettings() {
        initComponents();

        jTFServerAddressField.getDocument().addDocumentListener( new DocumentListener() {
            @Override
            public void insertUpdate( DocumentEvent e ) {
                jTFServerAddressFieldActionPerformed( null );
            }

            @Override
            public void removeUpdate( DocumentEvent e ) {
                jTFServerAddressFieldActionPerformed( null );
            }

            @Override
            public void changedUpdate( DocumentEvent e ) {
                jTFServerAddressFieldActionPerformed( null );
            }
        } );

        setTitle( "screeny.cc - Einstellungen" );
        setIconImage( new ImageIcon( LoginGui.class.getResource( "/icon_48x48.png" ) ).getImage() );
        setLocationRelativeTo( null );

        setSettingsFromStore();

        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed( WindowEvent e ) {
                GuiManager.setUserSettings( null );
            }
        } );
    }

    private void setSettingsFromStore() {
        UserSettingsStore userSettingsStore = ScreenyClient.getClientSettingsStore();

        this.jCBPlayNotificationSound.setSelected( userSettingsStore.isPlayNotificationSoundOnScreen() );

        this.jCBOpenScreenInBrowser.setSelected( userSettingsStore.isOpenScreenshotInBrowser() );

        this.jCBCopyLinkToClipboard.setSelected( userSettingsStore.isCopyLinkToClipboard() );

        this.jCBSaveLocalCopy.setSelected( userSettingsStore.isSaveLocalCopyOfScreen() );

        this.jTFSaveCopyLocalDirectory.setText( userSettingsStore.getLocalCopyLocation() );

        this.jBKeybindingSelectArea.setText( ( userSettingsStore.getKeyBindingCaptureArea().isEmpty() ) ? "Nicht festgelegt" : userSettingsStore.getKeyBindingCaptureArea() );

        this.jTFServerAddressField.setText( userSettingsStore.getServerAddress() );

        this.jLSessionUsername.setText( userSettingsStore.getUsername() );

        this.jLSessionKey.setText( userSettingsStore.getSessionID() );

        saveLocalCopyCheck();

    }

    private void saveLocalCopyCheck() {
        if ( !jCBSaveLocalCopy.isSelected() ) {
            jTFSaveCopyLocalDirectory.setEnabled( false );
            jBSelectLocalCopyDirectory.setEnabled( false );
        } else {
            jTFSaveCopyLocalDirectory.setEnabled( true );
            jBSelectLocalCopyDirectory.setEnabled( true );
        }
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jTabbedPaneSettings = new javax.swing.JTabbedPane();
        jPanelGeneralSection = new javax.swing.JPanel();
        jPanelGeneralInGeneral = new javax.swing.JPanel();
        jCBAutostart = new javax.swing.JCheckBox();
        jPanelSuccessfullScreenshotActionInGeneral = new javax.swing.JPanel();
        jCBPlayNotificationSound = new javax.swing.JCheckBox();
        jCBOpenScreenInBrowser = new javax.swing.JCheckBox();
        jCBSaveLocalCopy = new javax.swing.JCheckBox();
        jCBCopyLinkToClipboard = new javax.swing.JCheckBox();
        jTFSaveCopyLocalDirectory = new javax.swing.JTextField();
        jBSelectLocalCopyDirectory = new javax.swing.JButton();
        jPanelKeybindingsSection = new javax.swing.JPanel();
        jPanelKeybindings = new javax.swing.JPanel();
        jLKeybindingSelectArea = new javax.swing.JLabel();
        jBKeybindingSelectArea = new javax.swing.JButton();
        jPanelServerSection = new javax.swing.JPanel();
        jPanelServerSettings = new javax.swing.JPanel();
        jLServerSettingsAddress = new javax.swing.JLabel();
        jTFServerAddressField = new javax.swing.JTextField();
        jPanelAccountSection = new javax.swing.JPanel();
        jPanelAccountInformation = new javax.swing.JPanel();
        jLLoggedInDescriptionn = new javax.swing.JLabel();
        jLSessionKeyDescription = new javax.swing.JLabel();
        jLSessionUsername = new javax.swing.JLabel();
        jLSessionKey = new javax.swing.JLabel();
        jBLogout = new javax.swing.JButton();

        jMenuItem1.setText( "jMenuItem1" );

        jMenu3.setText( "File" );
        jMenuBar2.add( jMenu3 );

        jMenu4.setText( "Edit" );
        jMenuBar2.add( jMenu4 );

        jMenu5.setText( "File" );
        jMenuBar3.add( jMenu5 );

        jMenu6.setText( "Edit" );
        jMenuBar3.add( jMenu6 );

        jMenu7.setText( "jMenu7" );

        setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
        setTitle( "screeny - Einstellungen" );
        setResizable( false );

        jPanelGeneralInGeneral.setBorder( javax.swing.BorderFactory.createTitledBorder( "Allgemeine Einstellungen" ) );
        jPanelGeneralInGeneral.setToolTipText( "Test" );
        jPanelGeneralInGeneral.setName( "Test" ); // NOI18N

        jCBAutostart.setText( "screeny bei Systemstart starten - soon" );
        jCBAutostart.setEnabled( false );

        javax.swing.GroupLayout jPanelGeneralInGeneralLayout = new javax.swing.GroupLayout( jPanelGeneralInGeneral );
        jPanelGeneralInGeneral.setLayout( jPanelGeneralInGeneralLayout );
        jPanelGeneralInGeneralLayout.setHorizontalGroup(
                jPanelGeneralInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelGeneralInGeneralLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jCBAutostart )
                                .addContainerGap( 130, Short.MAX_VALUE ) )
        );
        jPanelGeneralInGeneralLayout.setVerticalGroup(
                jPanelGeneralInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addComponent( jCBAutostart )
        );

        jPanelSuccessfullScreenshotActionInGeneral.setBorder( javax.swing.BorderFactory.createTitledBorder( "Erfolgreicher Screenshot" ) );

        jCBPlayNotificationSound.setText( "Benachrichtigungs Sound abspielen" );
        jCBPlayNotificationSound.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jCBPlayNotificationSoundActionPerformed( evt );
            }
        } );

        jCBOpenScreenInBrowser.setText( "Screenshot im Browser öffnen" );
        jCBOpenScreenInBrowser.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jCBOpenScreenInBrowserActionPerformed( evt );
            }
        } );

        jCBSaveLocalCopy.setText( "Lokale Kopie speichern" );
        jCBSaveLocalCopy.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jCBSaveLocalCopyActionPerformed( evt );
            }
        } );

        jCBCopyLinkToClipboard.setText( "Link in die Zwischenablage kopieren" );
        jCBCopyLinkToClipboard.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jCBCopyLinkToClipboardActionPerformed( evt );
            }
        } );

        jTFSaveCopyLocalDirectory.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jTFSaveCopyLocalDirectoryActionPerformed( evt );
            }
        } );

        jBSelectLocalCopyDirectory.setText( ".." );
        jBSelectLocalCopyDirectory.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jBSelectLocalCopyDirectoryActionPerformed( evt );
            }
        } );

        javax.swing.GroupLayout jPanelSuccessfullScreenshotActionInGeneralLayout = new javax.swing.GroupLayout( jPanelSuccessfullScreenshotActionInGeneral );
        jPanelSuccessfullScreenshotActionInGeneral.setLayout( jPanelSuccessfullScreenshotActionInGeneralLayout );
        jPanelSuccessfullScreenshotActionInGeneralLayout.setHorizontalGroup(
                jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                        .addComponent( jCBCopyLinkToClipboard )
                                        .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createSequentialGroup()
                                                .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                                        .addComponent( jCBPlayNotificationSound )
                                                        .addComponent( jCBOpenScreenInBrowser ) )
                                                .addGap( 10, 10, 10 )
                                                .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                                        .addComponent( jCBSaveLocalCopy )
                                                        .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createSequentialGroup()
                                                                .addGap( 4, 4, 4 )
                                                                .addComponent( jTFSaveCopyLocalDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE )
                                                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                                                .addComponent( jBSelectLocalCopyDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE ) ) ) ) )
                                .addContainerGap( javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) )
        );
        jPanelSuccessfullScreenshotActionInGeneralLayout.setVerticalGroup(
                jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createSequentialGroup()
                                .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.BASELINE )
                                        .addComponent( jCBPlayNotificationSound )
                                        .addComponent( jCBSaveLocalCopy ) )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addGroup( jPanelSuccessfullScreenshotActionInGeneralLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.BASELINE )
                                        .addComponent( jCBOpenScreenInBrowser )
                                        .addComponent( jTFSaveCopyLocalDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE )
                                        .addComponent( jBSelectLocalCopyDirectory ) )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addComponent( jCBCopyLinkToClipboard ) )
        );

        javax.swing.GroupLayout jPanelGeneralSectionLayout = new javax.swing.GroupLayout( jPanelGeneralSection );
        jPanelGeneralSection.setLayout( jPanelGeneralSectionLayout );
        jPanelGeneralSectionLayout.setHorizontalGroup(
                jPanelGeneralSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelGeneralSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( jPanelGeneralSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                        .addComponent( jPanelGeneralInGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                        .addComponent( jPanelSuccessfullScreenshotActionInGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE ) )
                                .addContainerGap( 13, Short.MAX_VALUE ) )
        );
        jPanelGeneralSectionLayout.setVerticalGroup(
                jPanelGeneralSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelGeneralSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelGeneralInGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addComponent( jPanelSuccessfullScreenshotActionInGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) )
        );

        jTabbedPaneSettings.addTab( "Allgemein", jPanelGeneralSection );

        jPanelKeybindings.setBorder( javax.swing.BorderFactory.createTitledBorder( "Tasten Kombinationen" ) );

        jLKeybindingSelectArea.setText( "Bereich auswählen:" );

        jBKeybindingSelectArea.setText( "-" );
        jBKeybindingSelectArea.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jBKeybindingSelectAreaActionPerformed( evt );
            }
        } );

        javax.swing.GroupLayout jPanelKeybindingsLayout = new javax.swing.GroupLayout( jPanelKeybindings );
        jPanelKeybindings.setLayout( jPanelKeybindingsLayout );
        jPanelKeybindingsLayout.setHorizontalGroup(
                jPanelKeybindingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelKeybindingsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( jPanelKeybindingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                        .addComponent( jLKeybindingSelectArea )
                                        .addComponent( jBKeybindingSelectArea ) )
                                .addContainerGap( 249, Short.MAX_VALUE ) )
        );
        jPanelKeybindingsLayout.setVerticalGroup(
                jPanelKeybindingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelKeybindingsLayout.createSequentialGroup()
                                .addComponent( jLKeybindingSelectArea )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addComponent( jBKeybindingSelectArea ) )
        );

        javax.swing.GroupLayout jPanelKeybindingsSectionLayout = new javax.swing.GroupLayout( jPanelKeybindingsSection );
        jPanelKeybindingsSection.setLayout( jPanelKeybindingsSectionLayout );
        jPanelKeybindingsSectionLayout.setHorizontalGroup(
                jPanelKeybindingsSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelKeybindingsSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelKeybindings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE )
                                .addContainerGap() )
        );
        jPanelKeybindingsSectionLayout.setVerticalGroup(
                jPanelKeybindingsSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelKeybindingsSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelKeybindings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( 90, Short.MAX_VALUE ) )
        );

        jTabbedPaneSettings.addTab( "Tastenkombinationen", jPanelKeybindingsSection );

        jPanelServerSettings.setBorder( javax.swing.BorderFactory.createTitledBorder( "Server Einstellungen" ) );

        jLServerSettingsAddress.setText( "Server Adresse:" );

        jTFServerAddressField.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jTFServerAddressFieldActionPerformed( evt );
            }
        } );

        javax.swing.GroupLayout jPanelServerSettingsLayout = new javax.swing.GroupLayout( jPanelServerSettings );
        jPanelServerSettings.setLayout( jPanelServerSettingsLayout );
        jPanelServerSettingsLayout.setHorizontalGroup(
                jPanelServerSettingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelServerSettingsLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jLServerSettingsAddress )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addComponent( jTFServerAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( 120, Short.MAX_VALUE ) )
        );
        jPanelServerSettingsLayout.setVerticalGroup(
                jPanelServerSettingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelServerSettingsLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.BASELINE )
                                .addComponent( jLServerSettingsAddress )
                                .addComponent( jTFServerAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE ) )
        );

        javax.swing.GroupLayout jPanelServerSectionLayout = new javax.swing.GroupLayout( jPanelServerSection );
        jPanelServerSection.setLayout( jPanelServerSectionLayout );
        jPanelServerSectionLayout.setHorizontalGroup(
                jPanelServerSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelServerSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelServerSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE )
                                .addContainerGap() )
        );
        jPanelServerSectionLayout.setVerticalGroup(
                jPanelServerSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelServerSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelServerSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( 113, Short.MAX_VALUE ) )
        );

        jTabbedPaneSettings.addTab( "Server", jPanelServerSection );

        jPanelAccountInformation.setBorder( javax.swing.BorderFactory.createTitledBorder( "Benutzerkonto Informationen" ) );

        jLLoggedInDescriptionn.setText( "Angemeldet als:" );

        jLSessionKeyDescription.setText( "Session Key:" );

        jLSessionUsername.setText( "session.username" );
        jLSessionUsername.setAutoscrolls( true );

        jLSessionKey.setText( "session.key" );

        jBLogout.setText( "Abmelden" );
        jBLogout.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed( java.awt.event.ActionEvent evt ) {
                jBLogoutActionPerformed( evt );
            }
        } );

        javax.swing.GroupLayout jPanelAccountInformationLayout = new javax.swing.GroupLayout( jPanelAccountInformation );
        jPanelAccountInformation.setLayout( jPanelAccountInformationLayout );
        jPanelAccountInformationLayout.setHorizontalGroup(
                jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelAccountInformationLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.TRAILING )
                                        .addComponent( jLLoggedInDescriptionn )
                                        .addComponent( jLSessionKeyDescription ) )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addGroup( jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                                        .addGroup( jPanelAccountInformationLayout.createSequentialGroup()
                                                .addGap( 10, 10, 10 )
                                                .addComponent( jBLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE ) )
                                        .addComponent( jLSessionKey )
                                        .addComponent( jLSessionUsername ) )
                                .addContainerGap( 80, Short.MAX_VALUE ) )
        );
        jPanelAccountInformationLayout.setVerticalGroup(
                jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelAccountInformationLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup( jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.BASELINE )
                                        .addComponent( jLLoggedInDescriptionn )
                                        .addComponent( jLSessionUsername ) )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addGroup( jPanelAccountInformationLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.BASELINE )
                                        .addComponent( jLSessionKeyDescription )
                                        .addComponent( jLSessionKey ) )
                                .addPreferredGap( javax.swing.LayoutStyle.ComponentPlacement.RELATED )
                                .addComponent( jBLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE ) )
        );

        javax.swing.GroupLayout jPanelAccountSectionLayout = new javax.swing.GroupLayout( jPanelAccountSection );
        jPanelAccountSection.setLayout( jPanelAccountSectionLayout );
        jPanelAccountSectionLayout.setHorizontalGroup(
                jPanelAccountSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelAccountSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelAccountInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE )
                                .addContainerGap() )
        );
        jPanelAccountSectionLayout.setVerticalGroup(
                jPanelAccountSectionLayout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( jPanelAccountSectionLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jPanelAccountInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( 39, Short.MAX_VALUE ) )
        );

        jTabbedPaneSettings.addTab( "Account", jPanelAccountSection );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout( getContentPane() );
        getContentPane().setLayout( layout );
        layout.setHorizontalGroup(
                layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jTabbedPaneSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) )
        );
        layout.setVerticalGroup(
                layout.createParallelGroup( javax.swing.GroupLayout.Alignment.LEADING )
                        .addGroup( layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent( jTabbedPaneSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE )
                                .addContainerGap( javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE ) )
        );

        pack();
    }// </editor-fold>

    private void jBLogoutActionPerformed( java.awt.event.ActionEvent evt ) {
        dispose();
        TrayIconManager.disable();
        ScreenyClient.getClientSettingsStore().setSessionID( "" );
        ScreenyClient.getClientSettingsStore().save();
        StartupProcess.startLoginProcess();
    }

    private void jBKeybindingSelectAreaActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setKeyBindingCaptureArea( "" );
        this.jBKeybindingSelectArea.setText( "Press key..." );
        this.jBKeybindingSelectArea.setEnabled( false );
        new KeyListener( "", ( keyListener, pressedKey ) -> {
            keyListener.unregister();
            SwingUtilities.invokeLater( () -> {
                jBKeybindingSelectArea.setText( pressedKey );
                jBKeybindingSelectArea.setEnabled( true );
                ScreenyClient.getClientSettingsStore().setKeyBindingCaptureArea( pressedKey );
            } );
        } ).register();
    }

    private void jTFServerAddressFieldActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setServerAddress( this.jTFServerAddressField.getText() );
    }

    private void jBSelectLocalCopyDirectoryActionPerformed( java.awt.event.ActionEvent evt ) {
        JFileChooser jFileChooser = new JFileChooser( this.jTFSaveCopyLocalDirectory.getText() );
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        int retVal = jFileChooser.showOpenDialog( this );
        if ( retVal == JFileChooser.APPROVE_OPTION ) {
            this.jTFSaveCopyLocalDirectory.setText( jFileChooser.getSelectedFile().getAbsolutePath() );
        }
    }

    private void jCBPlayNotificationSoundActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setPlayNotificationSoundOnScreen( this.jCBPlayNotificationSound.isSelected() );
    }

    private void jCBOpenScreenInBrowserActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setOpenScreenshotInBrowser( this.jCBOpenScreenInBrowser.isSelected() );
    }

    private void jCBCopyLinkToClipboardActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setCopyLinkToClipboard( this.jCBPlayNotificationSound.isSelected() );
    }

    private void jCBSaveLocalCopyActionPerformed( java.awt.event.ActionEvent evt ) {
        saveLocalCopyCheck();
    }

    private void jTFSaveCopyLocalDirectoryActionPerformed( java.awt.event.ActionEvent evt ) {
        ScreenyClient.getClientSettingsStore().setLocalCopyLocation( this.jTFSaveCopyLocalDirectory.getText() );
    }

    public void start() {
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                setVisible( true );
            }
        } );
    }
    // End of variables declaration
}
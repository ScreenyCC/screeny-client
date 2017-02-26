package screeny.client.shots;

import screeny.client.trayicon.TrayIconManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AreaCaptureShot extends ScreenShotTaker {


    public JFrame jFrame;
    private File imageFile;

    public static Rectangle getVirtualBounds() {

        Rectangle bounds = new Rectangle( 0, 0, 0, 0 );

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        for ( GraphicsDevice gd : lstGDs ) {

            bounds.add( gd.getDefaultConfiguration().getBounds() );

        }

        return bounds;

    }

    @Override
    public void start() {
        enable();
    }

    @Override
    public File getImgageFile() {
        return this.imageFile;
    }

    public void enable() {
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
                } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex ) {
                }

                jFrame = new JFrame();
                jFrame.setUndecorated( true );
                jFrame.setBackground( new Color( 0, 0, 0, 0 ) );
                jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                jFrame.setLayout( new BorderLayout() );
                jFrame.add( new SnipItPane() );
                jFrame.setBounds( getVirtualBounds() );
                jFrame.setVisible( true );
            }
        } );
    }

    public class SnipItPane extends JPanel {

        private Point mouseAnchor;
        private Point dragPoint;
        private SelectionPane selectionPane;

        public SnipItPane() {
            setOpaque( false );
            setLayout( null );
            selectionPane = new SelectionPane();
            add( selectionPane );
            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mousePressed( MouseEvent e ) {
                    mouseAnchor = e.getPoint();
                    dragPoint = null;
                    selectionPane.setLocation( mouseAnchor );
                    selectionPane.setSize( 0, 0 );
                }

                @Override
                public void mouseReleased( MouseEvent e ) {
                    Point pos = selectionPane.getLocationOnScreen();
                    Rectangle rectangle = selectionPane.getBounds();
                    SwingUtilities.getWindowAncestor( selectionPane ).dispose();

                    BufferedImage image = null;
                    try {

                        rectangle.x = pos.x;
                        rectangle.y = pos.y;
                        rectangle.x -= 1;
                        rectangle.y -= 1;
                        rectangle.width += 2;
                        rectangle.height += 2;

                        image = new Robot().createScreenCapture( rectangle );
                    } catch ( AWTException e1 ) {
                        e1.printStackTrace();
                    }

                    try {
                        imageFile = File.createTempFile( "screeny", "screeny" );
                        imageFile.deleteOnExit();
                        ImageIO.write( image, "png", imageFile );
                        TrayIconManager.setMenuEnabled( TrayIconManager.MenuItemType.CAPTURE_AREA, true );
                        done();
                    } catch ( IOException e1 ) {
                        e1.printStackTrace();
                    }

                }

                @Override
                public void mouseDragged( MouseEvent e ) {
                    dragPoint = e.getPoint();
                    int width = dragPoint.x - mouseAnchor.x;
                    int height = dragPoint.y - mouseAnchor.y;

                    int x = mouseAnchor.x;
                    int y = mouseAnchor.y;

                    if ( width < 0 ) {
                        x = dragPoint.x;
                        width *= -1;
                    }
                    if ( height < 0 ) {
                        y = dragPoint.y;
                        height *= -1;
                    }
                    selectionPane.setBounds( x, y, width, height );
                    selectionPane.revalidate();
                    repaint();
                }
            };
            addMouseListener( adapter );
            addMouseMotionListener( adapter );
        }

        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent( g );

            Graphics2D g2d = ( Graphics2D ) g.create();

            Rectangle bounds = new Rectangle( 0, 0, getWidth(), getHeight() );
            Area area = new Area( bounds );
            area.subtract( new Area( selectionPane.getBounds() ) );

            g2d.setColor( new Color( 192, 192, 192, 64 ) );
            g2d.fill( area );

        }
    }

    public class SelectionPane extends JPanel {

        private JLabel label;

        public SelectionPane() {
            setOpaque( false );
            label = new JLabel( "Maus loslassen um Screen hochzuladen" );
            label.setOpaque( true );
            label.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );
            label.setBackground( Color.GRAY );
            label.setForeground( Color.WHITE );
            setLayout( new GridBagLayout() );

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            add( label, gbc );

            gbc.gridy++;

        }

        @Override
        protected void paintComponent( Graphics g ) {
            super.paintComponent( g );
            Graphics2D g2d = ( Graphics2D ) g.create();
            // I've chosen NOT to fill this selection rectangle, so that
            // it now appears as if you're "cutting" away the selection
//            g2d.setColor(new Color(128, 128, 128, 64));
//            g2d.fillRect(0, 0, getWidth(), getHeight());

            float dash1[] = { 10.0f };
            BasicStroke dashed =
                    new BasicStroke( 3.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f );
            g2d.setColor( Color.BLACK );
            g2d.setStroke( dashed );
            g2d.drawRect( 0, 0, getWidth() - 3, getHeight() - 3 );
            g2d.dispose();
        }
    }
}

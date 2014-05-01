package quickkar.gui.reusable.effect;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vendetta7
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class TransparentBackground extends JComponent
        implements ComponentListener, WindowFocusListener, Runnable {

    // constants ---------------------------------------------------------------
    // instance ----------------------------------------------------------------
    private JFrame frame;
    private BufferedImage background;
    private long lastUpdate = 0;
    private boolean refreshRequested = true;
    private Robot robot;
    private Rectangle screenRect;
    private BoxBlurFilter imageFilter;
//    private ConvolveOp _blurOp;

    // constructor -------------------------------------------------------------
    public TransparentBackground(JFrame frame) {
        this.frame = frame;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        screenRect = new Rectangle(dim.width, dim.height);
        imageFilter = new BoxBlurFilter();
//        float[] my_kernel = {
//            0.10f, 0.10f, 0.10f,
//            0.10f, 0.20f, 0.10f,
//            0.10f, 0.10f, 0.10f};
//        _blurOp = new ConvolveOp(new Kernel(3, 3, my_kernel));
        updateBackground();
        this.frame.addComponentListener(this);
        this.frame.addWindowFocusListener(this);
        new Thread(this).start();
    }
    // protected ---------------------------------------------------------------

    protected void updateBackground() {
        background = robot.createScreenCapture(screenRect);
        background = imageFilter.filter(background, background);
    }
    // public ------------------------------------------------------------------

    public void refresh() {
        if (frame.isVisible() && this.isVisible()) {
            repaint();
            refreshRequested = true;
            lastUpdate = System.currentTimeMillis();
        }
    }
    // JComponent --------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//        Point pos = this.getLocationOnScreen();
//        BufferedImage buf = new BufferedImage(getWidth(), getHeight(), BufferedImage.OPAQUE);
//        buf.getGraphics().drawImage(_background, -pos.x, -pos.y, null);
//        Image img = _blurOp.filter(buf, null);
//        g2.drawImage(_background, 0, 0, null);
//        g2.setColor(new Color(0, 0, 0, 100));
//        g2.fillRect(0, 0, getWidth(), getHeight());

        Point pos = this.getLocationOnScreen();
        Point offset = new Point(-pos.x, -pos.y);
        g.drawImage(background, offset.x, offset.y, null);
//        g.setColor(new Color(173, 230, 196, 100));
//        g.setColor(new Color(62, 180, 137, 100));
//        g.setColor(new Color(0, 159, 107, 100));
//        g.setColor(new Color(34, 139, 34, 100));
        g.setColor(new Color(59, 122, 87, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    // ComponentListener -------------------------------------------------------

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        repaint();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        repaint();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        repaint();
    }
    // WindowFocusListener -----------------------------------------------------

    @Override
    public void windowGainedFocus(WindowEvent e) {
//        refresh();
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
//        refresh();
    }
    // Runnable ----------------------------------------------------------------

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1);
                long now = System.currentTimeMillis();
                if (refreshRequested && ((now - lastUpdate) > 1)) {
                    if (frame.isVisible()) {
                        Point location = frame.getLocation();
                        frame.setLocation(-frame.getWidth(), -frame.getHeight());
                        updateBackground();
                        frame.setLocation(location);
                        refresh();
                    }
                    lastUpdate = now;
                    refreshRequested = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Transparent Window");
//        TransparentBackground bg = new TransparentBackground(frame);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(bg);
//        frame.pack();
//        frame.setSize(200, 200);
//        frame.setLocation(500, 500);
//        frame.setVisible(true);
//    }
}

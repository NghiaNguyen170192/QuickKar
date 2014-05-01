/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.effect;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import quickkar.gui.theme.common.CommonInterface;

/**
 *
 * @author Vendetta7
 */
public class LoadingPane extends JComponent implements CommonInterface {

    public LoadingPane() {
        setLayout(new BorderLayout());
        add(new JLabel(new ImageIcon("images/Theme 1/Loading3.gif")));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, DEFAULT_INVISIBLE));
        g2d.setColor(Color.black);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        g2d.fillRect(0, 0, (int) screen.getWidth(), (int) screen.getHeight());
//        g2d.setComposite(AlphaComposite.getInstance(
//            AlphaComposite.SRC_OVER, DEFAULT_INVISIBLE));
//        g2d.fillRect((int)screen.getWidth() - 50, (int)screen.getHeight() - 50, 50, 50);
    }
}

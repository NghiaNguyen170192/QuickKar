/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.effect;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import quickkar.gui.theme.common.CommonInterface;

/**
 *
 * @author Vendetta7
 */
public class ContentPane extends JPanel implements CommonInterface {

    private float opa = INVISIBLE;
    private boolean round;
    private Color color;
    private int corner;

    public ContentPane() {
        this(INVISIBLE);
    }

    public ContentPane(float opa) {
        this.opa = opa;
        this.color = Color.black;
        setOpaque(false);
    }

    public ContentPane(float opa, boolean round, Color color) {
        this(opa);
        this.round = round;
        this.color = color;
        this.corner = 10;
    }

    public ContentPane(float opa, int corner, Color color) {
        this(opa, true, color);
        this.corner = corner;
    }

    @Override
    protected void paintComponent(Graphics g) {

//        // Allow super to paint
//        super.paintComponent(g);
//
//        // Apply our own painting effect
//        Graphics2D g2d = (Graphics2D) g.create();
//        // 0% transparent Alpha
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opa));
//
//        g2d.setColor(getBackground());
////        g2d.fill(getBounds());
//        g2d.fillRect(0, 0, getWidth(), getHeight());
//
//        g2d.dispose();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, opa));
        g2d.setColor(color);
        if (!round) {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), corner, corner);
        }
    }
}

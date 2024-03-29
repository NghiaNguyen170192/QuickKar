/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import quickkar.gui.theme.common.CommonInterface;

/**
 * Construct a rounded-corner panel.
 * P/S: One of the most vital elements of the GUI.
 * @author Vinh
 */
public class RoundedPanel extends JPanel implements CommonInterface {

    /** Stroke size. it is recommended to set it to 1 for better view */
    protected int strokeSize = 0;
    /** Color of shadow */
    protected Color shadowColor = Color.black;
    /** Sets if it drops shadow */
    protected boolean shady = false;
    /** Sets if it has an High Quality view */
    protected boolean highQuality = true;
    /** Double values for Horizontal and Vertical radius of corner arcs */
    protected Dimension arcs = new Dimension(10, 10);
    /** Distance between shadow border and opaque panel border */
    protected int shadowGap = 0;
    /** The offset of shadow.  */
    protected int shadowOffset = 4;
    /** The transparency value of shadow. ( 0 - 255) */
    protected int shadowAlpha = 150;
    /** The transparency value of the panel itself. ( 0.0f - 1.0f ) */
    protected float opa = DEFAULT_INVISIBLE;

    //FOLLOWING CODES GOES HERE
    public RoundedPanel() {
        super();
        setOpaque(false);
    }

    public RoundedPanel(LayoutManager e) {
        super(e);
        setOpaque(false);
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void isShady(boolean shady) {
        this.shady = shady;
    }

    public void isHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    public void setArcs(int width, int height) {
        this.arcs = new Dimension(width, height);
    }

    public void setShadowGap(int shadowGap) {
        this.shadowGap = shadowGap;
    }

    public void setShadowOffset(int shadowOffset) {
        this.shadowOffset = shadowOffset;
    }

    public void setShadowAlpha(int shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
    }

    public void setOpa(float opa) {
        this.opa = opa;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        //int shadowGap = this.shadowGap;
//        Color shadowColorA = new Color(shadowColor.getRed(),
//	shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //Draws shadow borders if any.
//        if (shady) {
//            graphics.setColor(shadowColorA);
//            graphics.fillRoundRect(
//                    shadowOffset,// X position
//                    shadowOffset,// Y position
//                    width - strokeSize - shadowOffset, // width
//                    height - strokeSize - shadowOffset, // height
//                    arcs.width, arcs.height);// arc Dimension
//        } else {
//            //shadowGap = 1;
//        }
        graphics.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, opa));

        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap,
                height - shadowGap, arcs.width, arcs.height);

        //graphics.setColor(getBackground());
//        graphics.setStroke(new BasicStroke(strokeSize));
//        graphics.drawRoundRect(0, 0, width - shadowGap,
//		height - shadowGap, arcs.width, arcs.height);

        //Sets strokes to default, is better.
//        graphics.setStroke(new BasicStroke());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.theme.common.CommonInterface;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.model.DatabaseFacade;

/**
 * Construct Next and Forward paging panels.
 * @author Vinh
 */
public class SwitcherPanel extends RoundedPanel implements CommonInterface {

    private boolean left;
    private int type;
    private ImageIcon iconLeft, iconRight;
    private JLabel arrowLeft, arrowRight;

    public SwitcherPanel(int currentUser, DatabaseFacade model, boolean left, int type) {
        this.type = type;
        this.left = left;
        if (model.getCurrentTheme() == THEME1) {
            setLayout(new MigLayout());
        } else {
            setLayout(new MigLayout("", "", "0[]0"));
        }
        setBackground(model.getSWITCHER_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            setArcs(15, 15);
        } else {
            setArcs(0, 0);
        }

        if (type != SWITCH) {
            iconLeft = ICONFIRST_3;
            iconRight = ICONLAST_3;
        } else if (model.getCurrentTheme() == THEME1) {
            iconLeft = ICONLEFT_1;
            iconRight = ICONRIGHT_1;
        } else if (model.getCurrentTheme() == THEME2 && currentUser == CUSTOMER) {
            iconLeft = ICONLEFT_2;
            iconRight = ICONRIGHT_2;
        } else {
            iconLeft = ICONLEFT_3;
            iconRight = ICONRIGHT_3;
        }
        arrowLeft = new JLabel(iconLeft);
        arrowRight = new JLabel(iconRight);

        if (left) {
            add(arrowLeft, "dock center");
        } else {
            add(arrowRight, "dock center");
        }
    }
}

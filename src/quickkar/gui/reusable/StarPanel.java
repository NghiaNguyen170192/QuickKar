/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import javax.swing.JLabel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 * Construct individual star panel
 * @author Vinh
 */
public class StarPanel extends JLabel implements CommonInterface {

    private int star;
    private DatabaseFacade model;

    public StarPanel(DatabaseFacade model, int star) {
        this.model = model;
        this.star = star;

        setState(EMPTY);
    }

    public int getStar() {
        return star;
    }

    public void setState(int state) {
        if (model.getCurrentTheme() == THEME1) {
            if (state == EMPTY) {
                setIcon(STAR_EMPTY);
            } else if (state == HALF) {
                setIcon(STAR_HALF);
            } else if (state == FULL) {
                setIcon(STAR_FULL);
            } else if (state == EMPTY_VOTE) {
                setIcon(STAR_EMPTY_GLOW);
            } else if (state == HALF_VOTE) {
                setIcon(STAR_HALF_GLOW);
            } else if (state == FULL_VOTE) {
                setIcon(STAR_FULL_GLOW);
            }
        } else {
            if (state == EMPTY) {
                setIcon(STAR_EMPTY_2);
            } else if (state == HALF) {
                setIcon(STAR_HALF_2);
            } else if (state == FULL) {
                setIcon(STAR_FULL_2);
            } else if (state == EMPTY_VOTE) {
                setIcon(STAR_EMPTY_GLOW_2);
            } else if (state == HALF_VOTE) {
                setIcon(STAR_HALF_GLOW_2);
            } else if (state == FULL_VOTE) {
                setIcon(STAR_FULL_GLOW_2);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.theme.common.CommonInterface;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.model.DatabaseFacade;

/**
 * Wrap and manage Top Rated and New Added panels
 * @author Vinh
 */
public class WrapperFeedPanel extends RoundedPanel implements CommonInterface {

    private JPanel feedTop, feedNew;

    public WrapperFeedPanel(int currentUser, DatabaseFacade model) {
        super.setOpa(INVISIBLE);
        setLayout(new MigLayout("wrap 1", "0[]0", "0[]3[]0"));
        setBackground(model.getWRAPPER_COLOR());

        feedTop = new FeedPanel(currentUser, model, TOP_RATED);
        feedNew = new FeedPanel(currentUser, model, JUST_ADDED);

        add(feedTop, "w :100%:, h :50%:");
        add(feedNew, "w :100%:, h :50%:");
    }
}

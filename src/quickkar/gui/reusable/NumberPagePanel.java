/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.theme.common.CommonInterface;
import net.miginfocom.swing.MigLayout;
import quickkar.model.DatabaseFacade;

/**
 * Construct individual page number panel for pagination
 * @author Vinh
 */
public class NumberPagePanel extends RoundedPanel implements CommonInterface {

    private int pageNumber;

    public NumberPagePanel(int pageNumber, DatabaseFacade model) {
        super.setOpa(DEFAULT_VISIBLE);
        setLayout(new MigLayout("", "", "5[]5"));
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}

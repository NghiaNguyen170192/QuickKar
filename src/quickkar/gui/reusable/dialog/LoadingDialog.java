/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class LoadingDialog extends JDialog implements CommonInterface {

    public LoadingDialog(DatabaseFacade model, Window owner, boolean fill) throws InterruptedException {
        setUndecorated(true);

        if (fill) {
            setBackground(new Color(0, 0, 0, 128));
            setLayout(new MigLayout());
            setSize(owner.getWidth(), owner.getHeight());
        } else {
            setBackground(new Color(0, 0, 0, 0));
            setLayout(new MigLayout());
        }

        if (model == null || model.getCurrentTheme() == THEME1) {
            add(new JLabel(LOADING_1), "push, align center");
        } else {
            add(new JLabel(LOADING_2), "push, align center");
        }

        if (!fill) {
            pack();
            setLocationRelativeTo(null);
        } else {
            setLocation(owner.getLocation());
        }
        setVisible(true);
//        Thread.sleep(5000);
    }

    public LoadingDialog(Window owner, boolean fill) throws InterruptedException {
        this(null, owner, fill);
    }
}

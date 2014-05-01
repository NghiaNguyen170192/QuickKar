/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.LoginPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class PopupLoginDialog extends JDialog implements CommonInterface {

    private JPanel loginPanel;

    public PopupLoginDialog(final DatabaseFacade model, final Window owner) {
        super(owner, "", ModalityType.DOCUMENT_MODAL);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        loginPanel = new LoginPanel(model) {

            @Override
            public void setAction() {
                dispose();
            }

            @Override
            public void setError() {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("loginErr").toUpperCase(), (JFrame) owner, "ERROR", ModalityType.DOCUMENT_MODAL);
            }
        };

        add(loginPanel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }
}

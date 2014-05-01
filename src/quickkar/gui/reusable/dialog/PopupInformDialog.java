/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import quickkar.gui.reusable.effect.ContentPane;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class PopupInformDialog extends JDialog implements CommonInterface {

    private JLabel themeLabel;
    private JButton ok;
    private RoundedPanel panel;
    private DatabaseFacade model;

    public PopupInformDialog(DatabaseFacade model, String message, Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
        this.model = model;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("", "30[]30", "30[]20[]30"));
        ((RoundedPanel) panel).setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        themeLabel = new JLabel(message);
        themeLabel.setFont(new Font("Calibri", Font.BOLD, 17));
        themeLabel.setForeground(Color.white);
        ok = new JButton(model.getLanguage().getString("ok").toUpperCase());
        ActivateListener activate = new ActivateListener();

        panel.add(themeLabel, "span, align center");
        panel.add(ok, "h :36:, w :60:, push, align center");

        ok.addActionListener(activate);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    class ActivateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }
}

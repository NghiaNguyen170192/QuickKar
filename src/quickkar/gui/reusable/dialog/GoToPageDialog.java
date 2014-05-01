/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import quickkar.gui.reusable.effect.ContentPane;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class GoToPageDialog extends JDialog implements CommonInterface {

    private RoundedPanel panel;
    private JLabel label;
    private JTextField blank;
    private JButton go, cancel;
    private DatabaseFacade model;
    private Window owner;

    public GoToPageDialog(DatabaseFacade model, Window owner) {
        super(owner, "GO TO PAGE", ModalityType.DOCUMENT_MODAL);
        this.owner = owner;
        this.model = model;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("", "20[]10[]20", "20[]10[]20"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getWRAPPER_POST_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }
        label = new JLabel(model.getLanguage().getString("goToPage").toUpperCase() + " ");
        label.setFont(BOLD);
        label.setForeground(model.getBOLD_COLOR());
        blank = new JTextField(5);
        go = new JButton(model.getLanguage().getString("go"));
        cancel = new JButton(model.getLanguage().getString("cancel"));

        panel.add(label);
        panel.add(blank, "wrap");
        panel.add(go, "span, split 2, push, align right");
        panel.add(cancel, "align right");

        ActionListener jump = new GoToPageListener();
        blank.addActionListener(jump);
        go.addActionListener(jump);
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    class GoToPageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int page = 1;
            boolean invalid = false;
            try {
                page = Integer.parseInt(blank.getText());
            } catch (NumberFormatException ex) {
                invalid = true;
            }
            if (invalid) {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("inputErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            } else if (page > 0 && page <= model.getTotalPages()) {
                dispose();
                model.setCurrentPage(page);
            } else {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("rangeErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            }
        }
    }
}

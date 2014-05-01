/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

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
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class AdvancedSearchDialog extends JDialog implements CommonInterface {

    private JLabel exactLabel, anyLabel, noneLabel, topLabel;
    private JTextField exactField, anyField, noneField;
    private JButton search, cancel;
    private RoundedPanel panel;
    private DatabaseFacade model;
    private Window owner;

    public AdvancedSearchDialog(DatabaseFacade model, Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        this.model = model;
        this.owner = owner;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("wrap 2", "30[]12[]30", "20[]15[]15[]15[]15[]20"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        (topLabel = new JLabel(model.getLanguage().getString("advancedSearch").toUpperCase())).setFont(TYPE_BOLD);
        (exactLabel = new JLabel(model.getLanguage().getString("exact"))).setFont(TYPE);
        (anyLabel = new JLabel(model.getLanguage().getString("any"))).setFont(TYPE);
        (noneLabel = new JLabel(model.getLanguage().getString("none"))).setFont(TYPE);
        exactField = new JTextField(18);
        anyField = new JTextField(18);
        noneField = new JTextField(18);
        search = new JButton(model.getLanguage().getString("search"));
        cancel = new JButton(model.getLanguage().getString("cancel"));

        panel.add(topLabel, "span, push, align center, wrap");
        panel.add(exactLabel);
        panel.add(exactField, "h :20:");
        panel.add(anyLabel);
        panel.add(anyField, "h :20:");
        panel.add(noneLabel);
        panel.add(noneField, "h :20:");
        panel.add(search, "span, split 2, push, align right");
        panel.add(cancel);

        ActionListener searchListener = new SearchListener();
        exactField.addActionListener(searchListener);
        anyField.addActionListener(searchListener);
        noneField.addActionListener(searchListener);
        search.addActionListener(searchListener);
        cancel.addActionListener(searchListener);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public String getExact() {
        return exactField.getText();
    }

    public String getAny() {
        return anyField.getText();
    }

    public String getNone() {
        return noneField.getText();
    }

    public void action() {
    }

    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != cancel) {
                action();
            }
            dispose();
        }
    }
}

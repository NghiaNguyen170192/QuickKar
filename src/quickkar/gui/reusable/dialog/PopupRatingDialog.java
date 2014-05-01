/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class PopupRatingDialog extends JDialog implements CommonInterface {

    private JLabel phoneLabel, emailLabel, topLabel;
    private JTextField phoneField, emailField;
    private JButton yes, no;
    private RoundedPanel panel;
    private Window owner;
    private DatabaseFacade model;

    public PopupRatingDialog(DatabaseFacade model, Window owner) {
        super(owner, "Rate", ModalityType.DOCUMENT_MODAL);
        this.owner = owner;
        this.model = model;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("", "30[]10[]30", "20[]20[]10[]20[]20"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        topLabel = new JLabel(model.getLanguage().getString("rate").toUpperCase());
        phoneLabel = new JLabel(model.getLanguage().getString("phone").toUpperCase());
        emailLabel = new JLabel(model.getLanguage().getString("email").toUpperCase());
        phoneField = new JTextField();
        emailField = new JTextField();
        yes = new JButton(model.getLanguage().getString("ok"));
        no = new JButton(model.getLanguage().getString("cancel"));

        topLabel.setFont(new Font("Calibri", Font.BOLD, 24));
        topLabel.setForeground(Color.white);
        phoneLabel.setFont(BOLD);
        phoneLabel.setForeground(Color.white);
        emailLabel.setFont(BOLD);
        emailLabel.setForeground(Color.white);

        panel.add(topLabel, "span, align center");
        panel.add(phoneLabel);
        panel.add(phoneField, "w :150:, wrap");
        panel.add(emailLabel);
        panel.add(emailField, "w :150:, wrap");
        panel.add(yes, "push, align right, span, split 2");
        panel.add(no);

        no.addActionListener(new ActivateListener());
        yes.addActionListener(new ActivateListener());
        phoneField.addActionListener(new ActivateListener());
        emailField.addActionListener(new ActivateListener());

        no.addKeyListener(new MoveSelection());
        yes.addKeyListener(new MoveSelection());

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public JTextField getPhone() {
        return phoneField;
    }

    public JTextField getEmail() {
        return emailField;
    }

    public void setAction() {
        dispose();
    }

    class ActivateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == no) {
                dispose();
            } else if (e.getSource() == yes || e.getSource() == phoneField || e.getSource() == emailField) {
                setAction();
            }
        }
    }

    class MoveSelection extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                yes.setFocusable(false);
                no.setFocusable(true);
                no.requestFocus();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                no.setFocusable(false);
                yes.setFocusable(true);
                yes.requestFocus();
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import quickkar.gui.reusable.effect.ContentPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RegularExpression;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.Account;
import quickkar.model.DatabaseFacade;
import quickkar.model.Staff;

/**
 *
 * @author Vendetta7
 */
public class EditAccountDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private Account account;
    private JLabel topTitle, usernameLabel, passwordLabel, fullnameLabel, emailLabel, phoneLabel, addressLabel, descriptionLabel;
    private JTextField usernameField, fullnameField, emailField, phoneField, addressField;
    private JTextArea descriptionField;
    private JScrollPane descriptionScroll;
    private JPasswordField passwordField;
    private JButton update, cancel;
    private JPanel panel;
    private Window owner;
    private String info;

    public EditAccountDialog(String info, DatabaseFacade model, Account account, Window owner, String name, ModalityType modalityType) {
        super(owner, name, modalityType);
        this.info = info;
        this.owner = owner;
        this.model = model;
        this.account = account;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setLayout(new MigLayout());
        setSize(owner.getWidth(), owner.getHeight());

        if (info.equals("Add")) {
            topTitle = new JLabel(model.getLanguage().getString("newAcc").toUpperCase());
        } else {
            topTitle = new JLabel(model.getLanguage().getString("editAcc").toUpperCase());
        }
        usernameLabel = new JLabel(model.getLanguage().getString("username").toUpperCase());
        passwordLabel = new JLabel(model.getLanguage().getString("password").toUpperCase());
        fullnameLabel = new JLabel(model.getLanguage().getString("fullname").toUpperCase());
        emailLabel = new JLabel(model.getLanguage().getString("email").toUpperCase());
        phoneLabel = new JLabel(model.getLanguage().getString("phone").toUpperCase());
        addressLabel = new JLabel(model.getLanguage().getString("address").toUpperCase());
        descriptionLabel = new JLabel(model.getLanguage().getString("description").toUpperCase());

        topTitle.setFont(new Font("Calibri", Font.BOLD, 24));
        usernameLabel.setFont(BOLD);
        passwordLabel.setFont(BOLD);
        fullnameLabel.setFont(BOLD);
        emailLabel.setFont(BOLD);
        phoneLabel.setFont(BOLD);
        addressLabel.setFont(BOLD);
        descriptionLabel.setFont(BOLD);

        topTitle.setForeground(model.getBOLD_COLOR());
        usernameLabel.setForeground(model.getBOLD_COLOR());
        passwordLabel.setForeground(model.getBOLD_COLOR());
        fullnameLabel.setForeground(model.getBOLD_COLOR());
        emailLabel.setForeground(model.getBOLD_COLOR());
        phoneLabel.setForeground(model.getBOLD_COLOR());
        addressLabel.setForeground(model.getBOLD_COLOR());
        descriptionLabel.setForeground(model.getBOLD_COLOR());

        usernameField = new JTextField(this.account.getUsername());
        passwordField = new JPasswordField(this.account.getPassword());
        fullnameField = new JTextField(this.account.getName());
        emailField = new JTextField(this.account.getEmail());
        phoneField = new JTextField(this.account.getPhone());
        addressField = new JTextField(this.account.getAddress());
        descriptionField = new JTextArea(this.account.getDescription());
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        descriptionScroll = new JScrollPane(descriptionField);
        if (this.account.getUsername().equals("admin")) {
            usernameField.setEnabled(false);
        }

        if (info.equals("Add")) {
            update = new JButton(model.getLanguage().getString("add"));
        } else {
            update = new JButton(model.getLanguage().getString("update"));
        }
        cancel = new JButton(model.getLanguage().getString("cancel"));

        panel = new RoundedPanel(new MigLayout("", "12%[]12%", "30[]20[][]10[][]10[][]10[][]10[][]10[][]10[][]20[]30"));
        ((RoundedPanel) panel).setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getNORMAL_COLOR());
        panel.setSize(EDIT_ACCOUNT_WIDTH, EDIT_ACCOUNT_HEIGHT);
        if (model.getCurrentTheme() == THEME2) {
            ((RoundedPanel) panel).setArcs(0, 0);
        }

        panel.add(topTitle, "align center, wrap");
        panel.add(fullnameLabel, "wrap");
        panel.add(fullnameField, "w :400:, h :25:, wrap");
        panel.add(usernameLabel, "wrap");
        panel.add(usernameField, "w :400:, h :25:, wrap");
        panel.add(passwordLabel, "wrap");
        panel.add(passwordField, "w :400:, h :25:, wrap");
        panel.add(emailLabel, "wrap");
        panel.add(emailField, "w :400:, h :25:, wrap");
        panel.add(phoneLabel, "wrap");
        panel.add(phoneField, "w :400:, h :25:, wrap");
        panel.add(addressLabel, "wrap");
        panel.add(addressField, "w :400:, h :25:, wrap");
        panel.add(descriptionLabel, "wrap");
        panel.add(descriptionScroll, "w :400:, h :150:, wrap");
        panel.add(update, "w :60:, h :36:, split 2");
        panel.add(cancel, "w :60:, h :36:, gapleft 270");

        ActionListener validation = new ValidationListener();
        fullnameField.addActionListener(validation);
        usernameField.addActionListener(validation);
        passwordField.addActionListener(validation);
        emailField.addActionListener(validation);
        phoneField.addActionListener(validation);
        addressField.addActionListener(validation);
        update.addActionListener(validation);
        cancel.addActionListener(validation);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public void editProcessing() {
        String fullname = fullnameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String description = descriptionField.getText();
        RegularExpression check = new RegularExpression(model);
        String errorMessage = check.inforCheck(username, password, fullname, email, phone, address, description);
        if (errorMessage == null) {
            if (info.equals("Add")) {
                account = new Staff(fullname, username, password, email, phone, address, description);
                if (model.addAccount(account)) {
                    dispose();
                    JDialog success = new PopupInformDialog(model, model.getLanguage().getString("addAccMes").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    JDialog conflict = new PopupInformDialog(model, model.getLanguage().getString("usernameErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
                }
            } else if (model.editAccount(account, fullname, username, password, email, phone, address, description)) {
                dispose();
                JDialog success = new PopupInformDialog(model, model.getLanguage().getString("updateMes").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            } else {
                JDialog conflict = new PopupInformDialog(model, model.getLanguage().getString("usernameErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            }
        } else {
            JDialog error = new PopupInformDialog(model, errorMessage, owner, "", ModalityType.DOCUMENT_MODAL);
        }
    }

    class ValidationListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancel) {
                dispose();
            } else {
                editProcessing();
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.theme.common.CommonInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import quickkar.model.Account;
import quickkar.model.DatabaseFacade;

/**
 * Construct login interface as well as functionality
 * @author Vinh
 */
public class LoginPanel extends RoundedPanel implements CommonInterface {

    private JLabel userLabel, passLabel;
    private JTextField userField;
    private JPasswordField passField;
    private JButton login, cancel;
    private DatabaseFacade model;

    public LoginPanel(DatabaseFacade model) {
        super.setOpa(INVISIBLE);
        this.model = model;
        setLayout(new MigLayout("wrap 2", "30[]12[]30", "20[]15[]15[]20"));
        ActivateListener activate = new ActivateListener();
        setContent();
    }

    /**
     * Populate the interface for the login panel
     */
    public void setContent() {
        super.setOpa(DEFAULT_VISIBLE);
        setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME2) {
            setArcs(0, 0);
        }

        userLabel = new JLabel(model.getLanguage().getString("username"));
        passLabel = new JLabel(model.getLanguage().getString("password"));
        userField = new JTextField(18);
        passField = new JPasswordField(18);
        login = new JButton(model.getLanguage().getString("login"));
        cancel = new JButton(model.getLanguage().getString("cancel"));
        ActivateListener activate = new ActivateListener();

        //login.setUI(new RoundedCornerButtonUI());
        userLabel.setFont(TYPE);
        passLabel.setFont(TYPE);

        add(userLabel);
        add(userField, "h :20:");
        add(passLabel);
        add(passField, "h :20:");
        add(login, "span, split 2, align right");
        add(cancel);

        cancel.addActionListener(activate);
        login.addActionListener(activate);
        userField.addActionListener(activate);
        passField.addActionListener(activate);
    }

    /**
     * To be overridden. Other classes can manipulate the behavior of this function
     */
    public void setAction() {
    }

    /**
     * To be overridden. Other classes can manipulate the behavior of this function
     */
    public void setError() {
    }

    /**
     * Actions to be executed when user interacts with the panel
     */
    class ActivateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login || e.getSource() == userField || e.getSource() == passField) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                ArrayList<Account> accountList = model.getAccountList();
                String login_password = null;
                Account account = null;
                for (int i = 0; i < accountList.size(); i++) {
                    if (accountList.get(i).getUsername().equals(username)) {
                        login_password = accountList.get(i).getPassword();
                        account = accountList.get(i);
                        break;
                    }
                }

                if (username.equals("admin") && password.equals(login_password)) {
                    model.setCurrentAccount(account);
                    model.setCurrentUser(ADMIN);
                } else if (username.equals(username) && password.equals(login_password)) {
                    model.setCurrentAccount(account);
                    model.setSession(account.getUsername());
                    model.setCurrentUser(STAFF);
                } else {
                    setError();
                }
            } else if (e.getSource() == cancel) {
                setAction();
            }
        }
    }
}

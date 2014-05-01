/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
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
public class ProxySettingDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private Window owner;
    private JLabel hostLabel, portLabel, userLabel, passLabel, topLabel;
    private JTextField hostField, portField, userField;
    private JPasswordField passField;
    private JButton accept, cancel;
    private RoundedPanel panel;
    private boolean destroy = false;
    private JDialog currentParent;
    private JLabel message, messageTop;
    private JButton proxy, ok;

    public ProxySettingDialog(DatabaseFacade model, Window owner, int error) {
        super(owner, "PROXY", ModalityType.DOCUMENT_MODAL);
        this.owner = owner;
        this.model = model;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        if (error == CONNECTION_ERROR) {
            errorMessage();
        } else {
            proxySetting(error);
        }

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public void errorMessage() {
        panel = new RoundedPanel(new MigLayout("wrap", "30[]30", "20[]15[]15[]20"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        (messageTop = new JLabel(model.getLanguage().getString("connectError"))).setFont(TYPE_BOLD);
        message = new JLabel(model.getLanguage().getString("proxyErr"));
        message.setFont(TYPE);

        (proxy = new JButton(model.getLanguage().getString("proxySet"))).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                proxySetting(CONNECTION_ERROR);
            }
        });
        proxy.addKeyListener(new MoveSelection());

        (ok = new JButton(model.getLanguage().getString("ok"))).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        ok.addKeyListener(new MoveSelection());

        panel.add(messageTop, "push, align center");
        panel.add(message);
        panel.add(proxy, "split 2, push, align right");
        panel.add(ok, "align right");
    }

    public void proxySetting(int error) {

        if (error == CONNECTION_ERROR) {
            panel.removeAll();
        } else if (error == ANTHENTICATION_ERROR) {
            panel = new RoundedPanel();
        }

        panel.setLayout(new MigLayout("wrap 2", "30[]12[]30", "20[]15[]15[]20"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        (hostLabel = new JLabel(model.getLanguage().getString("host"))).setFont(TYPE);
        (portLabel = new JLabel(model.getLanguage().getString("port"))).setFont(TYPE);
        (topLabel = new JLabel(model.getLanguage().getString("proxySet").toUpperCase())).setFont(BOLD);
        (userLabel = new JLabel(model.getLanguage().getString("username"))).setFont(TYPE);
        (passLabel = new JLabel(model.getLanguage().getString("password"))).setFont(TYPE);
        (hostField = new JTextField(18)).setText(model.getProxyHost());
        portField = new JTextField(18);
        if (model.getProxyPort() != 0) {
            portField.setText(String.format("%d", model.getProxyPort()));
        }
        userField = new JTextField(18);
        passField = new JPasswordField(18);
        accept = new JButton(model.getLanguage().getString("accept"));
        cancel = new JButton(model.getLanguage().getString("cancel"));

        panel.add(topLabel, "span, align center, wrap");
        panel.add(hostLabel);
        panel.add(hostField, "h :20:");
        panel.add(portLabel);
        panel.add(portField, "h :20:");
        panel.add(userLabel);
        panel.add(userField, "h :20:");
        panel.add(passLabel);
        panel.add(passField, "h :20:");
        panel.add(accept, "span, split 2, push, align right");
        panel.add(cancel);

        hostField.setFocusable(true);
        hostField.requestFocus();

        ActionListener proxyAction = new ProxyListener();
        hostField.addActionListener(proxyAction);
        portField.addActionListener(proxyAction);
        userField.addActionListener(proxyAction);
        passField.addActionListener(proxyAction);
        accept.addActionListener(proxyAction);
        cancel.addActionListener(proxyAction);

        panel.updateUI();
    }

    public boolean isDestroyed() {
        return destroy;
    }

    private JDialog getThis() {
        return this;
    }

    private class ProxyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() != cancel) {
                    String host = hostField.getText();
                    int port = Integer.parseInt(portField.getText());
                    String username = userField.getText();
                    String password = new String(passField.getPassword());
                    if (host.equals("") || username.equals("") || password.equals("")) {
                        currentParent = model.getCurrentParent();
                        model.setCurrentParent(getThis());
                        JDialog error = new PopupInformDialog(model, model.getLanguage().getString("emptyErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
                        model.setCurrentParent(currentParent);
                    } else {
                        model.setProxy(host, port, username, password);
                        dispose();
                    }
                } else {
                    destroy = true;
                    dispose();
                }
            } catch (NumberFormatException ex) {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("portErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            }
        }
    }

    private class MoveSelection extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                proxy.setFocusable(false);
                ok.setFocusable(true);
                ok.requestFocus();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                ok.setFocusable(false);
                proxy.setFocusable(true);
                proxy.requestFocus();
            }
        }
    }
}

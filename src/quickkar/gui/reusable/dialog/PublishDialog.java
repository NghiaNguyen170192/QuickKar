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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Freedom
 */
public class PublishDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private RoundedPanel panel;
    private JLabel messageLabel, topLabel;
    private JTextArea messageArea;
    private JScrollPane scroll;
    private JButton send, cancel;
    private String link;
    private Window owner;

    public PublishDialog(DatabaseFacade model, String link) {
        super(model.getTopContainer(), ModalityType.DOCUMENT_MODAL);
        this.model = model;
        this.link = link;
        this.owner = model.getTopContainer();

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setLayout(new MigLayout());
        setSize(owner.getWidth(), owner.getHeight());

        panel = new RoundedPanel(new MigLayout("", "30[]30", "30[]20[][]20[]30"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        (topLabel = new JLabel("Publish to Facebook")).setFont(TYPE_BOLD);
        (messageLabel = new JLabel("Message")).setFont(TYPE);
        messageArea = new JTextArea("I've just found a great song! Come join me at QuickKar!");
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        scroll = new JScrollPane(messageArea);
        send = new JButton("Publish");
        cancel = new JButton("Cancel");

        ActionListener publish = new PublishListener();
        send.addActionListener(publish);
        cancel.addActionListener(publish);

        panel.add(topLabel, "push, align center, wrap");
        panel.add(messageLabel, "wrap");
        panel.add(scroll, "w :400:, h :150:, wrap");
        panel.add(send, "split 2, push, align right");
        panel.add(cancel);

        setLocation(owner.getLocation());
        add(panel, "push, align center");

        setVisible(true);
    }

    private class PublishListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() != cancel) {
                model.publishToFacebook(messageArea.getText(), link, model.getAccessToken());
                JDialog success = new PopupInformDialog(model, "PUBLISHED SUCCESSFULLY!!!", owner, "", ModalityType.DOCUMENT_MODAL);
            }
            dispose();
        }
    }
}

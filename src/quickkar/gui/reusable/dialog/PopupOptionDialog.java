/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import quickkar.gui.reusable.effect.ContentPane;
import java.awt.event.KeyEvent;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class PopupOptionDialog extends JDialog implements CommonInterface {

    private JLabel themeLabel;
    private JButton yes, no;
    private RoundedPanel panel;

    public PopupOptionDialog(DatabaseFacade model, String message, Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setLayout(new MigLayout());
        setSize(owner.getWidth(), owner.getHeight());

        panel = new RoundedPanel(new MigLayout("", "30[]30", "30[]20[]30"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        themeLabel = new JLabel(message);
        themeLabel.setFont(new Font("Calibri", Font.BOLD, 17));
        themeLabel.setForeground(Color.white);
        yes = new JButton(model.getLanguage().getString("yes"));
        no = new JButton(model.getLanguage().getString("no"));
        ActivateListener activate = new ActivateListener();

        panel.add(themeLabel, "span, align center");
        panel.add(yes, "h :36:, w :60:, gapleft 20");
        panel.add(no, "h :36:, w :60:, gapright 20, align right");

        no.addActionListener(activate);
        yes.addActionListener(activate);

        no.addKeyListener(new MoveSelection());
        yes.addKeyListener(new MoveSelection());

        setLocation(owner.getLocation());
        add(panel, "push, align center");

        setVisible(true);
    }

    public void setAction() {
        dispose();
    }

    class ActivateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == no) {
                dispose();
            } else if (e.getSource() == yes) {
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

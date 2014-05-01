/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;

/**
 *
 * @author Vendetta7
 */
public class About extends JDialog implements CommonInterface {

    private JLabel imageContainer;
    private ImageIcon about;

    public About(final Window owner) {
        super(owner, "", ModalityType.DOCUMENT_MODAL);

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        about = ABOUT;
        imageContainer = new JLabel(about);
        add(imageContainer, "push, align center");

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
            }
        });

        setLocation(owner.getLocation());
        setVisible(true);
    }
}

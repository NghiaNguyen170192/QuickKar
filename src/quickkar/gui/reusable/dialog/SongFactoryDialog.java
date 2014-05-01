/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class SongFactoryDialog extends JDialog implements CommonInterface, Observer {

    private DatabaseFacade model;
    private JPanel[] songs;
    private RoundedPanel panel;
    private JLabel close;
    private JLabel title;
    private ImageIcon cross;
    private ImageIcon crossHover;
    private int type;

    public SongFactoryDialog(final DatabaseFacade model, int type, final Window owner, JPanel[] songs) {
        super(owner, "", ModalityType.DOCUMENT_MODAL);
        model.setCurrentParent(this);
        ((Database) model).addObserver(this);
        this.type = type;
        this.songs = songs;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        if (type == TOP_RATED) {
            (title = new JLabel(model.getLanguage().getString("topRated").toUpperCase())).setFont(BOLD);
        } else if (type == JUST_ADDED) {
            (title = new JLabel(model.getLanguage().getString("justAdded").toUpperCase())).setFont(BOLD);
        }

        panel = new RoundedPanel(new MigLayout("wrap"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setLayout(new MigLayout("wrap", "0[]0", "0[]1"));
            panel.setArcs(0, 0);
        }

        cross = CROSS;
        crossHover = CROSS_HOVER;

        (close = new JLabel(cross)).addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                model.setCurrentParent(null);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    close.setIcon(CROSS_HOVER);
                } else {
                    close.setIcon(CROSS_HOVER_2);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    close.setIcon(CROSS);
                } else {
                    close.setIcon(CROSS_2);
                }
            }
        });

        panel.add(close, "push, align right");
        if (type != NONE) {
            panel.add(title, "push, align center");
        }

        for (int i = 0; i < songs.length; i++) {
            panel.add(songs[i], String.format("w :%d:", WRAPPER_POST_WIDTH));
        }

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public void setSongs(JPanel[] songs) {
        this.songs = songs;
    }

    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (type != NONE) {
            for (int i = 0; i < songs.length; i++) {
                panel.remove(songs[i]);
            }
        }
    }
}

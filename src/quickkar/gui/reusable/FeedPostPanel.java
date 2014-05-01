/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import quickkar.gui.theme.common.CommonInterface;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.dialog.SongFactoryDialog;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 * Construct each individual song posts for Top Rated and Just Added features
 * @author Vinh
 */
public class FeedPostPanel extends RoundedPanel implements CommonInterface {

    private JLabel code, name;
    private DatabaseFacade model;
    private int currentUser;
    private Song song;
    private int index;

    public FeedPostPanel(final int index, final int currentUser, final DatabaseFacade model, final Song song) {
        super.setOpa(DEFAULT_VISIBLE);
        this.index = index;
        this.currentUser = currentUser;
        this.model = model;
        this.song = song;

        setLayout(new MigLayout());
        setBackground(model.getPOST_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            setArcs(15, 15);
        } else {
            setArcs(0, 0);
        }

        this.code = new JLabel(song.getCode());
        this.name = new JLabel(song.getTitle().substring(0, 1).toUpperCase() + song.getTitle().substring(1).toLowerCase());

        add(this.code, "dock center");
        add(this.name);

        /**
         * Provide expanded version of a song post that facilitates all fundamentals
         * interactions to user.
         */
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                final JPanel songPanel = new IndividualPostPanel(index, currentUser, model, song);
                songPanel.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        songPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        songPanel.setBackground(model.getPOST_COLOR());
                    }
                });
                JDialog songDialog = new SongFactoryDialog(model, NONE, (JFrame) getTopLevelAncestor(), new JPanel[]{songPanel});
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(model.getPOST_COLOR_BRIGHT());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(model.getPOST_COLOR());
            }
        });
    }
}

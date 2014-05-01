/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.event.MouseEvent;
import java.util.Observable;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observer;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.dialog.SongFactoryDialog;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 * Responsible for 'Top Rated' and 'Just Added' features
 * @author Vinh
 */
public class FeedPanel extends RoundedPanel implements CommonInterface, Observer {

    private DatabaseFacade model;
    private int currentUser;
    private ContentPane feedPanel;
    private JLabel feedLabel;
    private JLabel expand;
    private Font feedFont;
    private JPanel[] feedPosts;
    private JPanel[] songPanels;
    private int type;
    private MouseListener hover;

    public FeedPanel(int currentUser, DatabaseFacade model, int type) {
        ((Database) model).addObserver(this);
        this.currentUser = currentUser;
        this.model = model;
        this.type = type;
        if (model.getCurrentTheme() == THEME1) {
            setLayout(new MigLayout("wrap", "2[]2", "2[]3"));
        } else {
            setLayout(new MigLayout("wrap", "0[]0", "0[]1"));
        }
        setBackground(model.getFEED_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            setArcs(15, 15);
        } else {
            setArcs(0, 0);
        }

        if (type == TOP_RATED) {
            topRatedContent();
        } else {
            justAddedContent();
        }

        hover = new HoverListener();
    }

    /**
     * Populate Top Rated features
     */
    private void topRatedContent() {
        if (model.getCurrentTheme() == THEME1) {
            (feedPanel = new ContentPane()).setLayout(new MigLayout("", "", "4[]2"));
        } else {
            (feedPanel = new ContentPane()).setLayout(new MigLayout("", "", "6[]5"));
        }
        feedPanel.add(feedLabel = new JLabel(model.getLanguage().getString("topRated").toUpperCase()));
        if (model.getCurrentTheme() == 1) {
            feedPanel.add(expand = new JLabel(EXPAND), "push, align right");
        } else {
            feedPanel.add(expand = new JLabel(EXPAND_2), "push, align right");
        }

        feedFont = new Font("Calibri", Font.BOLD, 18);
        feedLabel.setFont(feedFont);
//        feedLabel.setForeground(Color.white);

        add(feedPanel, "w :100%:");

        ArrayList<Song> topRatedSong = model.getTopRated();
        feedPosts = new FeedPostPanel[topRatedSong.size()];
        int length = 0;
        if (model.getCurrentTheme() == THEME1) {
            length = topRatedSong.size() < 10 ? topRatedSong.size() : 9;
        } else {
            length = topRatedSong.size() < 10 ? topRatedSong.size() : 10;
        }
        for (int i = 0; i < length; i++) {
            add(feedPosts[i] = new FeedPostPanel(i, currentUser, model, topRatedSong.get(i)), "w :100%:");
        }

        expand.setToolTipText(model.getLanguage().getString("expand"));

        /**
         * Expanded version of Top Rated. Provide all fundamentals interactions of a song to user
         */
        expand.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    expand.setIcon(EXPAND_HOVER);
                } else {
                    expand.setIcon(EXPAND_HOVER_2);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    expand.setIcon(EXPAND);
                } else {
                    expand.setIcon(EXPAND_2);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                songPanels = new RoundedPanel[model.getTopRated().size()];
                for (int i = 0; i < model.getTopRated().size(); i++) {
                    songPanels[i] = new IndividualPostPanel(i, currentUser, model, model.getTopRated().get(i));
                    songPanels[i].addMouseListener(hover);
                }
                JDialog songDialog = new SongFactoryDialog(model, type, model.getTopContainer(), songPanels) {

                    @Override
                    public void update(Observable o, Object arg) {
                        super.update(o, arg);
                        songPanels = new RoundedPanel[model.getTopRated().size()];
                        for (int i = 0; i < model.getTopRated().size(); i++) {
                            songPanels[i] = new IndividualPostPanel(i, currentUser, model, model.getTopRated().get(i));
                            songPanels[i].addMouseListener(hover);
                            getPanel().add(songPanels[i], String.format("w :%d:", WRAPPER_POST_WIDTH));
                        }
                        getPanel().updateUI();
                        setSongs(songPanels);
                    }
                };
            }
        });
    }

    /**
     * Populate Just Added features
     */
    private void justAddedContent() {
        if (model.getCurrentTheme() == THEME1) {
            (feedPanel = new ContentPane()).setLayout(new MigLayout("", "", "4[]2"));
        } else {
            (feedPanel = new ContentPane()).setLayout(new MigLayout("", "", "6[]5"));
        }
        feedPanel.add(feedLabel = new JLabel(model.getLanguage().getString("justAdded").toUpperCase()));
        if (model.getCurrentTheme() == 1) {
            feedPanel.add(expand = new JLabel(EXPAND), "push, align right");
        } else {
            feedPanel.add(expand = new JLabel(EXPAND_2), "push, align right");
        }

        feedFont = new Font("Calibri", Font.BOLD, 18);
        feedLabel.setFont(feedFont);
//        feedLabel.setForeground(Color.white);

        add(feedPanel, "w :100%:");

        ArrayList<Song> newAddedSong = model.getNewAdded();
        feedPosts = new FeedPostPanel[newAddedSong.size()];
        int length = 0;
        if (model.getCurrentTheme() == THEME1) {
            length = newAddedSong.size() < 10 ? newAddedSong.size() : 9;
        } else {
            length = newAddedSong.size() < 10 ? newAddedSong.size() : 10;
        }
        for (int i = 0; i < length; i++) {
            add(feedPosts[i] = new FeedPostPanel(i, currentUser, model, newAddedSong.get(i)), "w :100%:");
        }

        expand.setToolTipText(model.getLanguage().getString("expand"));

        /**
         * Expanded version of Just Added. Provide all fundamentals interactions of a song to user
         */
        expand.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    expand.setIcon(EXPAND_HOVER);
                } else {
                    expand.setIcon(EXPAND_HOVER_2);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (model.getCurrentTheme() == THEME1) {
                    expand.setIcon(EXPAND);
                } else {
                    expand.setIcon(EXPAND_2);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                songPanels = new RoundedPanel[model.getNewAdded().size()];
                for (int i = 0; i < model.getNewAdded().size(); i++) {
                    songPanels[i] = new IndividualPostPanel(i, currentUser, model, model.getNewAdded().get(i));
                    songPanels[i].addMouseListener(hover);
                }
                JDialog songDialog = new SongFactoryDialog(model, type, model.getTopContainer(), songPanels);
            }
        });
    }

    /**
     * Update Top Rated songs after user rates a song
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        expand.setToolTipText(model.getLanguage().getString("expand"));
        if (type == TOP_RATED) {
            feedLabel.setText(((DatabaseFacade) o).getLanguage().getString("topRated").toUpperCase());
            for (int i = 0; i < feedPosts.length; i++) {
                if (feedPosts[i] != null) {
                    remove(feedPosts[i]);
                }
            }
            ArrayList<Song> topRatedSong = model.getTopRated();
            feedPosts = new FeedPostPanel[topRatedSong.size()];
            int length = 0;
            if (model.getCurrentTheme() == THEME1) {
                length = topRatedSong.size() < 10 ? topRatedSong.size() : 9;
            } else {
                length = topRatedSong.size() < 10 ? topRatedSong.size() : 10;
            }
            for (int i = 0; i < length; i++) {
                add(feedPosts[i] = new FeedPostPanel(i, currentUser, model, topRatedSong.get(i)), "w :100%:");
            }
            updateUI();
        } else {
            feedLabel.setText(((DatabaseFacade) o).getLanguage().getString("justAdded").toUpperCase());
            updateUI();
        }
    }

    private class HoverListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            ((IndividualPostPanel) e.getSource()).setBackground(model.getPOST_COLOR_BRIGHT());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((IndividualPostPanel) e.getSource()).setBackground(model.getPOST_COLOR());
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.HandleKiller;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Vendetta7
 */
public class KaraokeKillerDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private ArrayList<ArrayList<String>> searchResult;
    private SongPanel song[];
    private RoundedPanel panel;
    private ImageIcon cross;
    private ImageIcon cross_hover;
    private JLabel close;
    private Window owner;

    public KaraokeKillerDialog(DatabaseFacade model, Window owner, ArrayList<ArrayList<String>> searchResult) {
        super(owner, "", ModalityType.DOCUMENT_MODAL);
        this.model = model;
        this.owner = owner;
        this.searchResult = searchResult;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("wrap 1"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        cross = CROSS;
        cross_hover = CROSS_HOVER;
        close = new JLabel(cross);
        panel.add(close, "push, align right");

        MouseListener action = new AnonymousListener();

        song = new SongPanel[searchResult.size()];
        for (int i = 0; i < song.length; i++) {
            panel.add(song[i] = new SongPanel(model, searchResult.get(i)), String.format("w :%d:", POST_WIDTH));
            song[i].addMouseListener(action);
        }

        close.addMouseListener(action);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    private class SongPanel extends RoundedPanel implements CommonInterface {

        private DatabaseFacade model;
        private ArrayList<String> song;
        private JLabel title, singer, share;

        public SongPanel(final DatabaseFacade model, final ArrayList<String> song) {
            setOpa(DEFAULT_VISIBLE);
            this.model = model;
            this.song = song;

            setLayout(new MigLayout());
            setBackground(model.getPOST_COLOR());
            if (model.getCurrentTheme() == THEME1) {
                setArcs(15, 15);
            } else {
                setArcs(0, 0);
            }

            title = new JLabel(song.get(TITLE_SONG).toUpperCase());
            singer = new JLabel(song.get(SINGER));
            (share = new JLabel(SHARE)).setToolTipText(model.getLanguage().getString("share"));

            add(title);
            add(singer, "push, align right");
            add(share);

            addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        model.letSing(song.get(URL));
                    } catch (IOException ex) {
                        System.out.println("sing karaoke function got problems!");
                    }
                }
            });

            share.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (model.getCurrentTheme() == THEME1) {
                        share.setIcon(SHARE_HOVER);
                    } else {
                        share.setIcon(SHARE_HOVER_2);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (model.getCurrentTheme() == THEME1) {
                        share.setIcon(SHARE);
                    } else {
                        share.setIcon(SHARE_2);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    HandleKiller killer = new HandleKiller(model, song.get(URL), PLAY_SHARE);
                }
            });
        }

        private String getURL() {
            return song.get(URL);
        }
    }

    private class AnonymousListener extends MouseAdapter {

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == close) {
                close.setIcon(cross);
            } else {
                ((SongPanel) e.getSource()).setBackground(model.getPOST_COLOR());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == close) {
                close.setIcon(cross_hover);
            } else {
                ((SongPanel) e.getSource()).setBackground(model.getPOST_COLOR_BRIGHT());
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() != close) {
                try {
                    model.letSing(((SongPanel) e.getSource()).getURL());
                } catch (IOException ex) {
                    System.out.println("sing karaoke function got problems!");
                }
            } else {
                dispose();
            }
        }
    }
}

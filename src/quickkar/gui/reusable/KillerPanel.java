/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 *
 * @author Freedom
 */
public class KillerPanel extends ContentPane implements CommonInterface {

    private JLabel play, karaoke;
    private DatabaseFacade model;
    private Song song;

    public KillerPanel(DatabaseFacade model, Song song) {
        this.model = model;
        this.song = song;

        setLayout(new MigLayout("", "0[][]0", "1[]1"));
        if (model.getCurrentTheme() == THEME1) {
            add(play = new JLabel(LISTEN));
            add(karaoke = new JLabel(KARAOKE));
        } else {
            add(play = new JLabel(LISTEN_2));
            add(karaoke = new JLabel(KARAOKE_2));
        }
        play.setToolTipText(model.getLanguage().getString("play"));
        karaoke.setToolTipText(model.getLanguage().getString("karaoke"));

        MouseListener killer = new Killer();
        play.addMouseListener(killer);
        karaoke.addMouseListener(killer);
    }

    private class Killer extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == play) {
                    play.setIcon(LISTEN_HOVER);
                } else if (e.getSource() == karaoke) {
                    karaoke.setIcon(KARAOKE_HOVER);
                }
            } else {
                if (e.getSource() == play) {
                    play.setIcon(LISTEN_HOVER_2);
                } else if (e.getSource() == karaoke) {
                    karaoke.setIcon(KARAOKE_HOVER_2);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == play) {
                    play.setIcon(LISTEN);
                } else if (e.getSource() == karaoke) {
                    karaoke.setIcon(KARAOKE);
                }
            } else {
                if (e.getSource() == play) {
                    play.setIcon(LISTEN_2);
                } else if (e.getSource() == karaoke) {
                    karaoke.setIcon(KARAOKE_2);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == play) {
                HandleKiller handle = new HandleKiller(model, song, PLAY_SONG_VIDEO);
            } else if (e.getSource() == karaoke) {
                HandleKiller handle = new HandleKiller(model, song, PLAY_KARAOKE);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.DecimalFormat;
import javax.swing.JDialog;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.StarPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 *
 * @author Vendetta7
 */
public class RatingHoverDialog extends JDialog implements CommonInterface {

    private StarPanel[] stars;
    private JLabel label;
    private ContentPane starPanel;
    private RoundedPanel panel;
    private DatabaseFacade model;
    private JLabel parent;
    private Song song;

    public RatingHoverDialog(DatabaseFacade model, Song song, Window owner, JLabel parent) {
        super(owner, "", ModalityType.MODELESS);
        this.model = model;
        this.song = song;
        this.parent = parent;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        if (model.getCurrentTheme() == THEME1) {
            setContentPane(new ContentPane(DEFAULT_INVISIBLE, true, Color.white));
        } else {
            setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        }
        getContentPane().setBackground(Color.black);
        setLayout(new MigLayout("", "3[]3", "3[]3"));

        panel = new RoundedPanel(new MigLayout("", "[][]0[]0[]0[]0[]", "2[]2"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(Color.white);
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(10, 10);
        } else {
            panel.setArcs(0, 0);
        }

        starPanel = new ContentPane();
        starPanel.setLayout(new MigLayout("", "0[]0", "0[]0"));

        //Format 0.0;
        DecimalFormat df = new DecimalFormat("0.00");
        String output = df.format(song.getAverage());
        stars = new StarPanel[5];
        label = new JLabel(output);

        int i, star, state;
        double average = song.getAverage();
        int starNumber = (int) average;
        average = average - starNumber;
        if (average <= 0.25) {
            star = starNumber;
            state = FULL;
        } else if (average < 0.75) {
            star = starNumber + 1;
            state = HALF;
        } else {
            star = starNumber + 1;
            state = FULL;
        }
        for (i = 0; i < 5; i++) {
            starPanel.add(stars[i] = new StarPanel(model, i + 1));
            if (i < star - 1) {
                stars[i].setState(FULL);
            } else if (i == star - 1) {
                stars[i].setState(state);
            } else {
                stars[i].setState(EMPTY);
            }
        }
        panel.add(label);
        panel.add(starPanel, "wrap");

        panel.add(new JLabel("------------------------------------"), "span, wrap");

        for (i = 1; i < 6; i++) {
            label = new JLabel(song.getNumOfVote(i) + " " + model.getLanguage().getString("rated"));
            starPanel = new ContentPane();
            starPanel.setLayout(new MigLayout("", "0[]0", "0[]0"));

            for (int j = 0; j < 5; j++) {
                starPanel.add(stars[j] = new StarPanel(model, j + 1));
                if (j < i) {
                    stars[j].setState(FULL);
                } else {
                    stars[j].setState(EMPTY);
                }
            }
            panel.add(label, "dock center");
            panel.add(starPanel, "wrap");
        }

        if (model.getSession() != null && song.isCurrentlyVoted(model.getSession())) {
            label = new JLabel(model.getLanguage().getString("yourRating"));
            starPanel = new ContentPane();
            starPanel.setLayout(new MigLayout("", "0[]0", "0[]0"));

            star = song.getCurrentlyVoted(model.getSession());
            for (i = 0; i < 5; i++) {
                starPanel.add(stars[i] = new StarPanel(model, i + 1));
                if (i < star) {
                    stars[i].setState(FULL_VOTE);
                } else {
                    stars[i].setState(EMPTY_VOTE);
                }
            }
            panel.add(new JLabel("------------------------------------"), "span, wrap");
            panel.add(label, "dock center");
            panel.add(starPanel, "wrap");
        }

        add(panel);
        pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screen = toolkit.getScreenSize();
        int xCor = (int) parent.getLocationOnScreen().getX() + parent.getWidth() - getWidth();
        int yCor = (int) parent.getLocationOnScreen().getY() + parent.getHeight();
        if (yCor + getHeight() > screen.getHeight()) {
            yCor = (int) parent.getLocationOnScreen().getY() - getHeight();
        }
        setLocation(xCor, yCor);
        setVisible(true);
    }

    public void destroy() {
        dispose();
    }
}

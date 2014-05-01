/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.dialog.RatingHoverDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import quickkar.gui.reusable.dialog.PopupRatingDialog;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 * Construct and manage five star panels.
 * @author Vinh
 */
public class StarRatingPanel extends ContentPane implements Observer, CommonInterface {

    private StarPanel[] stars;
    private JLabel expand;
    private DatabaseFacade model;
    private int currentUser;
    private Song song;
    private int star = -1;
    private int state = EMPTY;

    public StarRatingPanel(int currentUser, DatabaseFacade model, Song song) {
        this.currentUser = currentUser;
        this.model = model;
        this.song = song;
        ((Database) model).addObserver(this);
        setLayout(new MigLayout("", "0[]0[]0[]0[]0[][]0", "1[]1"));
        stars = new StarPanel[5];
        if (model.getCurrentTheme() == THEME1) {
            expand = new JLabel(EXPAND);
        } else {
            expand = new JLabel(EXPAND_2);
        }

        MouseListener rating = new RatingListener();
        MouseListener popup = new PopupListener();

        if (song.getAverage() != 0) {
            double average = song.getAverage();
            int starNumber = (int) average;
            average = average - starNumber;
            if (model.getSession() != null && song.isCurrentlyVoted(model.getSession())) {
                if (average <= 0.25) {
                    star = starNumber;
                    state = FULL_VOTE;
                } else if (average < 0.75) {
                    star = starNumber + 1;
                    state = HALF_VOTE;
                } else {
                    star = starNumber + 1;
                    state = FULL_VOTE;
                }
            } else {
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
            }
        }
        for (int i = 0; i < 5; i++) {
            add(stars[i] = new StarPanel(model, i + 1));
            stars[i].addMouseListener(rating);
            if (model.getSession() != null && song.isCurrentlyVoted(model.getSession())) {
                if (i < star - 1) {
                    stars[i].setState(FULL_VOTE);
                } else if (i == star - 1) {
                    stars[i].setState(state);
                } else {
                    stars[i].setState(EMPTY_VOTE);
                }
            } else {
                if (i < star - 1) {
                    stars[i].setState(FULL);
                } else if (i == star - 1) {
                    stars[i].setState(state);
                } else {
                    stars[i].setState(EMPTY);
                }
            }
        }
        add(expand);
        expand.addMouseListener(popup);
    }

    public void reset() {
        if (!model.getSongList().isEmpty()) {
            int i = 0;
            if (model.getSession() != null && song.isCurrentlyVoted(model.getSession())) {
                if (star != -1) {
                    for (i = 0; i < 5; i++) {
                        stars[i].setState(FULL_VOTE);
                        if (i == star - 1) {
                            stars[i].setState(state);
                            break;
                        }
                    }
                }
                for (int j = i + 1; j < 5; j++) {
                    stars[j].setState(EMPTY_VOTE);
                }
            } else {
                if (star != -1) {
                    for (i = 0; i < 5; i++) {
                        if (stars != null) {
                            if (stars[i] != null) {
                                stars[i].setState(FULL);
                                if (i == star - 1) {
                                    stars[i].setState(state);
                                    break;
                                }
                            }
                        }
                    }
                }
                for (int j = i + 1; j < 5; j++) {
                    stars[j].setState(EMPTY);
                }
            }
        }
    }

    private JLabel getExpanded() {
        return expand;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (((Database) o).getSong(song) != null) {
            double average = ((Database) o).getSong(song).getAverage();
            int starNumber = (int) average;
            average = average - starNumber;
            if (((Database) o).getSession() != null && song.isCurrentlyVoted(((Database) o).getSession())) {
                if (average <= 0.25) {
                    star = starNumber;
                    state = FULL_VOTE;
                } else if (star < 0.75) {
                    star = starNumber + 1;
                    state = HALF_VOTE;
                } else {
                    star = starNumber + 1;
                    state = FULL_VOTE;
                }
            } else {
                if (average <= 0.25) {
                    star = starNumber;
                    state = FULL;
                } else if (star < 0.75) {
                    star = starNumber + 1;
                    state = HALF;
                } else {
                    star = starNumber + 1;
                    state = FULL;
                }
            }
            reset();
        }
    }

    class PopupListener extends MouseAdapter {

        private JFrame frame = (JFrame) getTopLevelAncestor();
        private JDialog popup;

        @Override
        public void mouseEntered(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == expand) {
                    expand.setIcon(EXPAND_HOVER);
                }
            } else {
                if (e.getSource() == expand) {
                    expand.setIcon(EXPAND_HOVER_2);
                }
            }
            if (e.getSource() == expand) {
                popup = new RatingHoverDialog(model, song, frame, getExpanded());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == expand) {
                    expand.setIcon(EXPAND);
                }
            } else {
                if (e.getSource() == expand) {
                    expand.setIcon(EXPAND_2);
                }
            }
            if (e.getSource() == expand) {
                ((RatingHoverDialog) popup).destroy();
            }
        }
    }

    class RatingListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            for (int i = 0; i < 5; i++) {
                stars[i].setState(FULL);
                if (e.getSource() == stars[i]) {
                    final int starChosen = i + 1;
                    stars[i].setState(FULL);
                    final JFrame frame = model.getTopContainer();
                    if (currentUser == CUSTOMER) {
                        PopupRatingDialog rating = new PopupRatingDialog(model, frame) {

                            @Override
                            public void setAction() {
                                String phone = this.getPhone().getText();
                                String email = this.getEmail().getText();
                                RegularExpression regex = new RegularExpression(model);
                                String errorMessage = regex.voteCheck(phone, email);
                                if (errorMessage != null) {
                                    JDialog currentParent = null;
                                    if (model.getCurrentParent() != null) {
                                        currentParent = model.getCurrentParent();
                                    }
                                    model.setCurrentParent(this);
                                    PopupInformDialog error = new PopupInformDialog(model, errorMessage, frame, "", ModalityType.DOCUMENT_MODAL);
                                    if (currentParent != null) {
                                        model.setCurrentParent(currentParent);
                                    } else {
                                        model.setCurrentParent(null);
                                    }
                                } else if (!model.rate(song, phone, email, starChosen)) {
                                    JDialog currentParent = null;
                                    if (model.getCurrentParent() != null) {
                                        currentParent = model.getCurrentParent();
                                    }
                                    model.setCurrentParent(this);
                                    PopupInformDialog error = new PopupInformDialog(model, model.getLanguage().getString("matchErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                                    if (currentParent != null) {
                                        model.setCurrentParent(currentParent);
                                    } else {
                                        model.setCurrentParent(null);
                                    }
                                } else {
                                    model.setSession(phone + ":" + email);
                                    super.setAction();
//                                    PopupInformDialog success = new PopupInformDialog(model, "VOTED SUCCESSFULLY!!!", frame, "SUCCESS", ModalityType.DOCUMENT_MODAL);
                                }
                            }
                        };
                    } else if (currentUser == STAFF) {
                        model.rate(song, model.getCurrentAccount().getUsername(), "", starChosen);
//                        PopupInformDialog success = new PopupInformDialog(model, "VOTED SUCCESSFULLY!!!", frame, "SUCCESS", ModalityType.DOCUMENT_MODAL);
                    }
                    break;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            int i;
            for (i = 0; i < 5; i++) {
                stars[i].setState(FULL);
                if (e.getSource() == stars[i]) {
                    stars[i].setState(FULL);
                    break;
                }
            }
            i++;
            for (int j = i; j < 5; j++) {
                stars[j].setState(EMPTY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            for (int i = 0; i < 5; i++) {
                stars[i].setState(EMPTY);
            }
            reset();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.reusable.dialog.PopupOptionDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.Dialog.ModalityType;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import quickkar.model.Account;
import quickkar.model.Song;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;

/**
 * Wraps and manages song posts
 * @author s3312310
 */
public class WrapperPostPanel extends RoundedPanel implements CommonInterface, Observer {

    private IndividualPostPanel[] postArray = new IndividualPostPanel[11];
    private Object element;
    private static final int FIRST_PAGE = 1;
    private DatabaseFacade model;
    private int currentUser;
    private IndividualPostPanel previousPost = null;
    private int currentPage;
    private boolean shift = false;
    private JPanel sortPanel;

    public WrapperPostPanel(int currentUser, DatabaseFacade model) {
        this.currentUser = currentUser;
        this.model = model;
        ((Database) this.model).addObserver(this);
        this.currentPage = this.model.getCurrentPage();
        if (model.getCurrentTheme() == THEME1) {
            setLayout(new MigLayout("wrap 1", "2[]2", "2[]2"));
            setArcs(15, 15);
        } else {
            setLayout(new MigLayout("wrap 1", "0[]0", "0[]1"));
            setArcs(0, 0);
        }
        setBackground(model.getWRAPPER_POST_COLOR());

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, true), "deleteSelected");
        this.getActionMap().put("deleteSelected", new MultipleAction());

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK | Event.SHIFT_MASK), "selecteAll");
        this.getActionMap().put("selecteAll", new SelectAllAction());

        sortPanel = new SortingPanel(model);

        prepareData(FIRST_PAGE);
    }

    public void prepareData(int page) {
        page -= 1;
        ArrayList<?> data = null;
        if (currentUser != ADMIN) {
            data = model.getSongList();
        } else {
            data = model.getAccountList();
        }
        int totalPages = model.getTotalPages();
        int length = data.size();
        if (page < 0 || page > totalPages) {
            System.out.println("Error! Array out of bound!");
            return;
        }
        removeAll();
        if (currentUser != ADMIN) {
            add(sortPanel, "w :100%:");
        }
        for (int index = 0, j = page * 11; j < page * 11 + 11 && j < length; index++, j++) {
            element = data.get(j);
            if (element == null) {
                break;
            }
            postArray[index] = new IndividualPostPanel(
                    index,
                    this.currentUser,
                    this.model,
                    element);
            postArray[index].addMouseListener(new MultipleListener());
            add(postArray[index], "w :100%:");
        }
        updateUI();
    }

    @Override
    public void update(Observable o, Object arg) {
        prepareData(((DatabaseFacade) o).getCurrentPage());
        if (currentPage != ((DatabaseFacade) o).getCurrentPage()) {
            if (currentUser != ADMIN) {
                model.deselectAllSong();
            } else {
                model.deselectAllAccount();
            }
            for (IndividualPostPanel post : postArray) {
                if (post != null) {
                    if (currentUser != ADMIN && ((Song) post.getElement()).isSelected()) {
                        post.setBackground(model.getPOST_COLOR_BRIGHT());
                    } else if (currentUser == ADMIN && post != null && ((Account) post.getElement()).isSelected()) {
                        post.setBackground(model.getPOST_COLOR_BRIGHT());
                    } else if (post != null) {
                        post.setBackground(model.getPOST_COLOR());
                    }
                }
            }
            currentPage = ((Database) o).getCurrentPage();
        }
    }

    class MultipleListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (currentUser != CUSTOMER) {
                IndividualPostPanel current = (IndividualPostPanel) e.getSource();
                if (e.getClickCount() == 2) {
                    current.editAction();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            IndividualPostPanel current = (IndividualPostPanel) e.getSource();
            Song song = null;
            Account account = null;
            if (currentUser != ADMIN) {
                song = (Song) current.getElement();
            } else {
                account = (Account) current.getElement();
            }
            if (e.isShiftDown()) {
                if (currentUser != ADMIN) {
                    if (previousPost != null && ((Song) previousPost.getElement()).isSelected()) {
                        shift = true;
                    }
                    if (!shift) {
                        if (!song.isSelected()) {
                            song.setSelected(true);
                            current.setBackground(model.getPOST_COLOR_BRIGHT());
                            previousPost = current;
                        } else {
                            previousPost = current;
                        }
                        shift = true;
                    } else {
                        if (current != previousPost) {
                            if (current.getIndex() > previousPost.getIndex()) {
                                model.deselectAllSong();
                                for (IndividualPostPanel post : postArray) {
                                    post.setBackground(model.getPOST_COLOR());
                                }
                                ((Song) previousPost.getElement()).setSelected(true);
                                previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                                for (int i = previousPost.getIndex(); i <= current.getIndex(); i++) {
                                    ((Song) postArray[i].getElement()).setSelected(true);
                                    postArray[i].setBackground(model.getPOST_COLOR_BRIGHT());
                                }
                            } else {
                                model.deselectAllSong();
                                for (IndividualPostPanel post : postArray) {
                                    post.setBackground(model.getPOST_COLOR());
                                }
                                ((Song) previousPost.getElement()).setSelected(true);
                                previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                                for (int i = current.getIndex(); i <= previousPost.getIndex(); i++) {
                                    ((Song) postArray[i].getElement()).setSelected(true);
                                    postArray[i].setBackground(model.getPOST_COLOR_BRIGHT());
                                }
                            }
                        } else {
                            model.deselectAllSong();
                            for (IndividualPostPanel post : postArray) {
                                post.setBackground(model.getPOST_COLOR());
                            }
                            ((Song) previousPost.getElement()).setSelected(true);
                            previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                        }
                    }
                } else {
                    if (previousPost != null && ((Account) previousPost.getElement()).isSelected()) {
                        shift = true;
                    }
                    if (!shift) {
                        if (!account.isSelected()) {
                            account.setSelected(true);
                            current.setBackground(model.getPOST_COLOR_BRIGHT());
                            previousPost = current;
                        } else {
                            previousPost = current;
                        }
                        shift = true;
                    } else {
                        if (current != previousPost) {
                            if (current.getIndex() > previousPost.getIndex()) {
                                model.deselectAllAccount();
                                for (IndividualPostPanel post : postArray) {
                                    if (post != null) {
                                        post.setBackground(model.getPOST_COLOR());
                                    }
                                }
                                ((Account) previousPost.getElement()).setSelected(true);
                                previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                                for (int i = previousPost.getIndex(); i <= current.getIndex(); i++) {
                                    ((Account) postArray[i].getElement()).setSelected(true);
                                    postArray[i].setBackground(model.getPOST_COLOR_BRIGHT());
                                }
                            } else {
                                model.deselectAllAccount();
                                for (IndividualPostPanel post : postArray) {
                                    if (post != null) {
                                        post.setBackground(model.getPOST_COLOR());
                                    }
                                }
                                ((Account) previousPost.getElement()).setSelected(true);
                                previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                                for (int i = current.getIndex(); i <= previousPost.getIndex(); i++) {
                                    ((Account) postArray[i].getElement()).setSelected(true);
                                    postArray[i].setBackground(model.getPOST_COLOR_BRIGHT());
                                }
                            }
                        } else {
                            model.deselectAllAccount();
                            for (IndividualPostPanel post : postArray) {
                                if (post != null) {
                                    post.setBackground(model.getPOST_COLOR());
                                }
                            }
                            ((Account) previousPost.getElement()).setSelected(true);
                            previousPost.setBackground(model.getPOST_COLOR_BRIGHT());
                        }
                    }
                }
                shift = true;
            } else if (e.isControlDown()) {
                shift = false;
                if (currentUser != ADMIN) {
                    if (!song.isSelected()) {
                        song.setSelected(true);
                        current.setBackground(model.getPOST_COLOR_BRIGHT());
                    } else {
                        song.setSelected(false);
                        current.setBackground(model.getPOST_COLOR());
                    }
                } else {
                    if (!account.isSelected()) {
                        account.setSelected(true);
                        current.setBackground(model.getPOST_COLOR_BRIGHT());
                    } else {
                        account.setSelected(false);
                        current.setBackground(model.getPOST_COLOR());
                        current.setContent();
                    }
                }

            } else {
                shift = false;
                if (currentUser != ADMIN) {
                    model.deselectAllSong();
                    for (IndividualPostPanel post : postArray) {
                        if (post != null && post != current) {
                            post.setBackground(model.getPOST_COLOR());
                        }
                    }
                    if (current == previousPost) {
                        previousPost = null;
                    } else {
                        current.setBackground(model.getPOST_COLOR_BRIGHT());
                        song.setSelected(true);
                        previousPost = current;
                    }
                } else {
                    model.deselectAllAccount();
                    for (IndividualPostPanel post : postArray) {
                        if (post != null && post != current) {
                            post.setBackground(model.getPOST_COLOR());
                        }
                    }
                    if (current == previousPost) {
                        previousPost = null;
                        current.setContent();
                    } else {
                        current.setBackground(model.getPOST_COLOR_BRIGHT());
                        account.setSelected(true);
                        current.resetContent();
                        if (previousPost != null) {
                            previousPost.setContent();
                        }
                        previousPost = current;
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            IndividualPostPanel current = (IndividualPostPanel) e.getSource();
            current.setBackground(model.getPOST_COLOR_BRIGHT());
        }

        @Override
        public void mouseExited(MouseEvent e) {
            IndividualPostPanel current = (IndividualPostPanel) e.getSource();
            if (currentUser != ADMIN && previousPost != current && !((Song) current.getElement()).isSelected()) {
                current.setBackground(model.getPOST_COLOR());
            } else if (currentUser == ADMIN && previousPost != current && !((Account) current.getElement()).isSelected()) {
                current.setBackground(model.getPOST_COLOR());
            }
        }
    }

    class MultipleAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            final JFrame frame = (JFrame) getTopLevelAncestor();
            if (model.hasSongSelected() && currentUser == STAFF) {
                PopupOptionDialog deleteSong = new PopupOptionDialog(model, model.getLanguage().getString("deleteMess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

                    @Override
                    public void setAction() {
                        super.setAction();
                        model.deleteAllSelectedSong();
                        JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                    }
                };
            } else if (!model.hasSongSelected() && currentUser == STAFF) {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("editMenuErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
            } else if (model.hasAccountSelected() && currentUser == ADMIN) {
                PopupOptionDialog deleteAccount = new PopupOptionDialog(model, model.getLanguage().getString("deleteAccMess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

                    @Override
                    public void setAction() {
                        super.setAction();
                        if (model.deleteAllSelectedAccount()) {
                            JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        } else {
                            JDialog failure = new PopupInformDialog(model, model.getLanguage().getString("deleteAdminErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        }
                    }
                };
            } else if (!model.hasAccountSelected() && currentUser == ADMIN) {
                JDialog error = new PopupInformDialog(model, model.getLanguage().getString("deleteMenuAccErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
            }
        }
    }

    class SelectAllAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            for (IndividualPostPanel post : postArray) {
                if (currentUser == ADMIN) {
                    ((Account) post.getElement()).setSelected(true);
                } else {
                    ((Song) post.getElement()).setSelected(true);
                }
                post.setBackground(model.getPOST_COLOR_BRIGHT());
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.reusable.dialog.PopupOptionDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.Dialog.ModalityType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.dialog.EditAccountDialog;
import quickkar.gui.reusable.dialog.EditSongDialog;
import quickkar.model.Account;
import quickkar.model.Song;
import quickkar.model.DatabaseFacade;

/**
 * Construct individual song/account post. Provide all fundamentals interactions of a song post.
 * @author Vinh
 */
public class IndividualPostPanel extends RoundedPanel implements CommonInterface {

    private JLabel code, title, composer, lyric, username, fullname, email, address, phone;
    private JTextArea description;
    private ImageIcon editIcon, deleteIcon;
    private JLabel edit, delete;
    private JPanel starRating;
    private JPanel killer;
    private DatabaseFacade model;
    private Object element;
    private int index;
    private int currentUser;

    public IndividualPostPanel(int index, int currentUser, DatabaseFacade model, Object object) {
        super.setOpa(DEFAULT_VISIBLE);
        this.currentUser = currentUser;
        this.index = index;
        this.model = model;

        // Determine whether to display song or account object
        if (currentUser != ADMIN) {
            this.element = (Song) object;
        } else {
            this.element = (Account) object;
        }
        setLayout(new MigLayout("", "10[]10", ""));
        setBackground(model.getPOST_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            setArcs(15, 15);
        } else {
            setArcs(0, 0);
        }

        // Basic features for customer
        if (currentUser == CUSTOMER) {
            this.code = new JLabel(((Song) this.element).getCode());
            this.title = new JLabel(((Song) this.element).getTitle());
            this.composer = new JLabel(((Song) this.element).getComposer());
            this.lyric = new JLabel(((Song) this.element).getLyric());
            this.starRating = new StarRatingPanel(currentUser, model, (Song) this.element);
            this.killer = new KillerPanel(model, (Song) this.element);

            add(this.code);
            add(this.title, "dock center");
            add(this.composer, "wrap");
            add(this.killer);
            add(this.lyric, "span 2, split 2, dock center");
            add(this.starRating);
            addMouseListener(new InteractListener());
        } else if (currentUser == STAFF) { // Basic features for staff
            setLayout(new MigLayout("", "10[]10", "6[]5[]5"));
            this.code = new JLabel(((Song) this.element).getCode());
            this.title = new JLabel(((Song) this.element).getTitle());
            this.composer = new JLabel(((Song) this.element).getComposer());
            this.lyric = new JLabel(((Song) this.element).getLyric());
            this.starRating = new StarRatingPanel(currentUser, model, (Song) this.element);

            edit = new JLabel();
            delete = new JLabel();
            edit.setLayout(new MigLayout("", "0[]0", "0[]0"));
            delete.setLayout(new MigLayout("", "0[]0", "0[]0"));
            edit.setBackground(model.getPAGE_COLOR_NORMAL());
            delete.setBackground(model.getPAGE_COLOR_NORMAL());

            if (model.getCurrentTheme() == THEME1) {
                editIcon = EDITICON_1;
                deleteIcon = DELETEICON_1;
            } else {
                setLayout(new MigLayout("", "6[]6", "6[]4[]6"));
                editIcon = WRENCH_2;
                deleteIcon = CROSS_2;
            }
            edit.setIcon(editIcon);
            delete.setIcon(deleteIcon);

            add(this.code);
            add(this.title, "dock center");
            add(this.composer);
            if (currentUser != ADMIN) {
                add(this.edit, "split 2");
                add(this.delete, "wrap");
            } else {
                if (((Account) element).getUsername().equals("admin")) {
                    add(this.edit, "push, align right, wrap");
                } else {
                    add(this.edit, "split 2");
                    add(this.delete, "wrap");
                }
            }
            add(this.lyric, "span, cell 1 1, split 2, dock center");
            add(this.starRating);

            EditListener editListener = new EditListener();
            edit.addMouseListener(editListener);
            delete.addMouseListener(editListener);
        } else if (currentUser == ADMIN) { // Basic feature for admin
            setContent();
        }
    }

    public Object getElement() {
        return element;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Default interface of an account post
     */
    public void setContent() {
        removeAll();

        setLayout(new MigLayout("", "[20%][30%][42%][]", ""));

        this.username = new JLabel(((Account) element).getUsername());
        this.fullname = new JLabel(((Account) element).getName());
        this.email = new JLabel(((Account) element).getEmail());
        this.phone = new JLabel(((Account) element).getPhone());
        this.address = new JLabel(((Account) element).getAddress());

        edit = new JLabel();
        delete = new JLabel();
        edit.setLayout(new MigLayout("", "0[]0", "0[]0"));
        delete.setLayout(new MigLayout("", "0[]0", "0[]0"));
        edit.setBackground(model.getPAGE_COLOR_NORMAL());
        delete.setBackground(model.getPAGE_COLOR_NORMAL());

        if (model.getCurrentTheme() == THEME1) {
            editIcon = EDITICON_1;
            deleteIcon = DELETEICON_1;
        } else {
            setLayout(new MigLayout("", "6[20%][30%][42%][]6", "6[]4[]6"));
            editIcon = WRENCH_2;
            deleteIcon = CROSS_2;
        }

        edit.setIcon(editIcon);
        delete.setIcon(deleteIcon);

        add(this.username);
        add(this.fullname);
        add(this.email);
        if (currentUser != ADMIN) {
            add(this.edit, "split 2");
            add(this.delete, "wrap");
        } else {
            if (((Account) element).getUsername().equals("admin")) {
                add(this.edit, "push, align right, wrap");
            } else {
                add(this.edit, "split 2");
                add(this.delete, "wrap");
            }
        }
        add(this.phone, "cell 1 1");
        add(this.address);

        EditListener editListener = new EditListener();
        edit.addMouseListener(editListener);
        delete.addMouseListener(editListener);

        updateUI();
    }

    /**
     * Expanded interface of an account post
     */
    public void resetContent() {
        removeAll();
        setLayout(new MigLayout());

        this.username = new JLabel(((Account) element).getUsername());
        this.fullname = new JLabel(((Account) element).getName());
        this.email = new JLabel(((Account) element).getEmail());
        this.phone = new JLabel(((Account) element).getPhone());
        this.address = new JLabel(((Account) element).getAddress());

        edit = new JLabel();
        delete = new JLabel();
        edit.setLayout(new MigLayout("", "0[]0", "0[]0"));
        delete.setLayout(new MigLayout("", "0[]0", "0[]0"));
        edit.setBackground(model.getPAGE_COLOR_NORMAL());
        delete.setBackground(model.getPAGE_COLOR_NORMAL());

        if (model.getCurrentTheme() == THEME1) {
            editIcon = EDITICON_1;
            deleteIcon = DELETEICON_1;
        } else {
            editIcon = WRENCH_2;
            deleteIcon = CROSS_2;
        }

        edit.setIcon(editIcon);
        delete.setIcon(deleteIcon);

        add(new JLabel(("Username: ").toUpperCase()), "w :60:");
        add(this.username);
        if (currentUser != ADMIN) {
            add(this.edit, "split 2");
            add(this.delete, "wrap");
        } else {
            if (((Account) element).getUsername().equals("admin")) {
                add(this.edit, "right align, wrap");
            } else {
                add(this.edit, "push, align right, split 2");
                add(this.delete, "wrap");
            }
        }
        add(new JLabel(("Fullname: ").toUpperCase()), "w :60:");
        add(this.fullname, "wrap");
        add(new JLabel(("Email: ").toUpperCase()), "w :60:");
        add(this.email, "wrap");
        add(new JLabel(("Phone: ").toUpperCase()), "w :60:");
        add(this.phone, "wrap");
        add(new JLabel(("Address: ").toUpperCase()), "w :60:");
        add(this.address, "wrap");
        add(new JLabel(("Description: ").toUpperCase()), "w :60:");
        description = new JTextArea(((Account) element).getDescription());
        if (model.getCurrentTheme() == THEME2) {
            description.setBackground(model.getPOST_COLOR_BRIGHT());
        }
        description.setFont(PLAIN);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEnabled(false);
        description.setOpaque(false);
        add(description, "w :100%:, span");

        EditListener editListener = new EditListener();
        edit.addMouseListener(editListener);
        delete.addMouseListener(editListener);

        updateUI();
    }

    /**
     * Edit individual song/account
     */
    public void editAction() {
        JFrame frame = (JFrame) getTopLevelAncestor();
        if (currentUser != ADMIN) {
            JDialog editSong = new EditSongDialog("Update", model, (Song) element, frame, model.getLanguage().getString("editSong").toUpperCase(), ModalityType.DOCUMENT_MODAL);
        } else {
            JDialog editAccount = new EditAccountDialog("Update", model, (Account) element, frame, model.getLanguage().getString("editAcc").toUpperCase(), ModalityType.DOCUMENT_MODAL);
        }
    }

    /**
     * Interactions each individual post provides
     */
    class EditListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == edit) {
                JFrame frame = (JFrame) getTopLevelAncestor();
                if (currentUser != ADMIN) {
                    JDialog editSong = new EditSongDialog("Update", model, (Song) element, frame, model.getLanguage().getString("editSong").toUpperCase(), ModalityType.DOCUMENT_MODAL);
                } else {
                    JDialog editAccount = new EditAccountDialog("Update", model, (Account) element, frame, model.getLanguage().getString("editAcc").toUpperCase(), ModalityType.DOCUMENT_MODAL);
                }
            } else if (e.getSource() == delete) {
                final JFrame frame = (JFrame) getTopLevelAncestor();
                if (currentUser != ADMIN) {
                    PopupOptionDialog deleteSong = new PopupOptionDialog(model, model.getLanguage().getString("deleteMes").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

                        @Override
                        public void setAction() {
                            super.setAction();
                            model.deleteSong((Song) element);
                            JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        }
                    };
                } else {
                    PopupOptionDialog deleteAccount = new PopupOptionDialog(model, model.getLanguage().getString("deleteAccMes").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

                        @Override
                        public void setAction() {
                            super.setAction();
                            model.deleteAccount((Account) element);
                            JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        }
                    };
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == edit) {
                    edit.setIcon(EDITICON_HOVER_1);
                } else if (e.getSource() == delete) {
                    delete.setIcon(DELETEICON_HOVER_1);
                }
            } else {
                if (e.getSource() == edit) {
                    edit.setIcon(WRENCH_HOVER_2);
                } else if (e.getSource() == delete) {
                    delete.setIcon(CROSS_HOVER_2);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (model.getCurrentTheme() == THEME1) {
                if (e.getSource() == edit) {
                    edit.setIcon(EDITICON_1);
                } else if (e.getSource() == delete) {
                    delete.setIcon(DELETEICON_1);
                }
            } else {
                if (e.getSource() == edit) {
                    edit.setIcon(WRENCH_2);
                } else if (e.getSource() == delete) {
                    delete.setIcon(CROSS_2);
                }
            }
        }
    }

    /**
     * Execute KILLER FEATURE when user double clicks on a SONG post.
     */
    private class InteractListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                HandleKiller handle = new HandleKiller(model, (Song) ((IndividualPostPanel) e.getSource()).getElement(), PLAY_KARAOKE);
            }
        }
    }
}

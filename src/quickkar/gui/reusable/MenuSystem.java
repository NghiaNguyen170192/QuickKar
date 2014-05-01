/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.util.Observable;
import quickkar.gui.reusable.dialog.About;
import quickkar.gui.reusable.dialog.PopupOptionDialog;
import quickkar.gui.reusable.dialog.PopupImportDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import quickkar.gui.reusable.dialog.GoToPageDialog;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import quickkar.gui.reusable.dialog.EditAccountDialog;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.gui.reusable.dialog.EditSongDialog;
import quickkar.gui.reusable.dialog.PopupLoginDialog;
import quickkar.gui.reusable.effect.TransparentBackground;
import quickkar.model.Account;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;
import quickkar.model.Staff;

/**
 * Construct menu bar for the application
 * @author Vinh Dao Nhan
 */
public class MenuSystem extends JMenuBar implements CommonInterface, Observer {

    private DatabaseFacade model;
    private int currentUser;
    private JMenu fileMenu;
    private JMenu helpMenu;
    private JMenu editMenu;
    private JMenu viewMenu;
    private JMenuItem newItem;
    private JMenuItem importCSV;
    private JMenuItem export;
    private JMenuItem login;
    private JMenuItem refresh;
    private JMenuItem switchTheme;
    private JMenu language;
    private JMenuItem eng;
    private JMenuItem vie;
    private JMenuItem logout;
    private JMenuItem exit;
    private JMenuItem editItem;
    private JMenuItem deleteItem;
    private JMenuItem next;
    private JMenuItem previous;
    private JMenuItem first;
    private JMenuItem last;
    private JMenuItem jump;
    private JFileChooser csv;
    private FileFilter filter;
    private JMenuItem userGuide;
    private JMenuItem about;
    private ActionListener menuListener;

    public MenuSystem(int currentUser, DatabaseFacade model) {
        this.model = model;
        this.currentUser = currentUser;
        ((Database) model).addObserver(this);

        fileMenu = new JMenu(model.getLanguage().getString("file"));
        helpMenu = new JMenu(model.getLanguage().getString("help"));
        editMenu = new JMenu(model.getLanguage().getString("edit"));
        viewMenu = new JMenu(model.getLanguage().getString("view"));

        fileMenu.setMnemonic(KeyEvent.VK_F);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        editMenu.setMnemonic(KeyEvent.VK_E);
        viewMenu.setMnemonic(KeyEvent.VK_V);

        newItem = new JMenuItem(model.getLanguage().getString("new"), KeyEvent.VK_N);
        importCSV = new JMenuItem(model.getLanguage().getString("importCSV"), KeyEvent.VK_I);
        export = new JMenuItem(model.getLanguage().getString("export"), KeyEvent.VK_P);
        login = new JMenuItem(model.getLanguage().getString("login"), KeyEvent.VK_L);
        refresh = new JMenuItem(model.getLanguage().getString("refresh"), KeyEvent.VK_F5);
        switchTheme = new JMenuItem(model.getLanguage().getString("switch"), KeyEvent.VK_S);
        language = new JMenu(model.getLanguage().getString("language"));
        eng = new JMenuItem(model.getLanguage().getString("eng"), KeyEvent.VK_1);
        vie = new JMenuItem(model.getLanguage().getString("vie"), KeyEvent.VK_2);
        logout = new JMenuItem(model.getLanguage().getString("logout"), KeyEvent.VK_O);
        exit = new JMenuItem(model.getLanguage().getString("exit"), KeyEvent.VK_X);

        editItem = new JMenuItem(model.getLanguage().getString("edit"), KeyEvent.VK_T);
        deleteItem = new JMenuItem(model.getLanguage().getString("delete"), KeyEvent.VK_D);

        next = new JMenuItem(model.getLanguage().getString("next"), KeyEvent.VK_RIGHT);
        previous = new JMenuItem(model.getLanguage().getString("previous"), KeyEvent.VK_LEFT);
        first = new JMenuItem(model.getLanguage().getString("first"), KeyEvent.VK_DOWN);
        last = new JMenuItem(model.getLanguage().getString("last"), KeyEvent.VK_UP);
        jump = new JMenuItem(model.getLanguage().getString("goTo"), KeyEvent.VK_G);

        userGuide = new JMenuItem(model.getLanguage().getString("userManual"), KeyEvent.VK_F1);
        about = new JMenuItem(model.getLanguage().getString("about"), 'H');

        csv = new JFileChooser(new File("."));
        filter = new FileNameExtensionFilter("CSV file", "csv");
        csv.setFileFilter(filter);
        csv.setMultiSelectionEnabled(true);
        csv.setAcceptAllFileFilterUsed(false);

        menuListener = new MenuListener();

        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        importCSV.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        login.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));
        switchTheme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        eng.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        vie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
        logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));

        first.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
        last.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
        previous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
        next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
        jump.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));

        userGuide.setAccelerator(KeyStroke.getKeyStroke("F1"));
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));

        if (currentUser != CUSTOMER) {
            fileMenu.add(newItem);
            if (currentUser != ADMIN) {
                fileMenu.addSeparator();
                fileMenu.add(importCSV);
                fileMenu.add(export);
            }

            fileMenu.add(logout);
        } else {
            fileMenu.add(login);
        }
        fileMenu.addSeparator();
        fileMenu.add(language);
        language.add(eng);
        language.add(vie);
        fileMenu.add(switchTheme);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        helpMenu.add(userGuide);
        helpMenu.addSeparator();
        helpMenu.add(about);

        editMenu.add(editItem);
        editMenu.add(deleteItem);

        if (currentUser == CUSTOMER) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }

        viewMenu.add(refresh);
        viewMenu.addSeparator();
        viewMenu.add(jump);
        viewMenu.addSeparator();
        viewMenu.add(next);
        viewMenu.add(previous);
        viewMenu.addSeparator();
        viewMenu.add(first);
        viewMenu.add(last);

        add(fileMenu);
        add(editMenu);
        add(viewMenu);
        add(helpMenu);

        newItem.addActionListener(menuListener);
        importCSV.addActionListener(menuListener);
        export.addActionListener(menuListener);
        login.addActionListener(menuListener);
        refresh.addActionListener(menuListener);
        switchTheme.addActionListener(menuListener);
        eng.addActionListener(menuListener);
        vie.addActionListener(menuListener);
        logout.addActionListener(menuListener);
        exit.addActionListener(menuListener);

        editItem.addActionListener(menuListener);
        deleteItem.addActionListener(menuListener);

        next.addActionListener(menuListener);
        previous.addActionListener(menuListener);
        first.addActionListener(menuListener);
        last.addActionListener(menuListener);
        jump.addActionListener(menuListener);

        userGuide.addActionListener(menuListener);
        about.addActionListener(menuListener);

        csv.addActionListener(menuListener);
    }

    /**
     * Import CSV feature
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private ArrayList<Object> open() throws FileNotFoundException, IOException {
        if (csv.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return model.readFromCSV(csv.getSelectedFiles());
        }
        return null;
    }

    /**
     * Alter the colors when user switch the theme
     */
    private void theme() {
        if (model.getCurrentTheme() == THEME1) {
            model.setCurrentTheme(THEME2);
            model.setWRAPPER_COLOR(new Color(255, 113, 0));
            model.setWRAPPER_POST_COLOR(new Color(255, 188, 0));
            model.setPOST_COLOR(new Color(255, 255, 255));
            model.setPOST_COLOR_BRIGHT(new Color(255, 255, 140));
            model.setRelatedColors();
            model.setCurrentUser(currentUser);
        } else if (model.getCurrentTheme() == THEME2) {
            model.setCurrentTheme(THEME1);
            model.setWRAPPER_COLOR(new Color(153, 153, 255));
            model.setWRAPPER_POST_COLOR(new Color(153, 204, 255));
            model.setPOST_COLOR(new Color(153, 255, 255));
            model.setPOST_COLOR_BRIGHT(Color.white);
            model.setRelatedColors();
            model.setCurrentUser(currentUser);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ResourceBundle lang = ((DatabaseFacade) o).getLanguage();
        fileMenu.setText(lang.getString("file"));
        helpMenu.setText(lang.getString("help"));
        editMenu.setText(lang.getString("edit"));
        viewMenu.setText(lang.getString("view"));
        if (currentUser != CUSTOMER) {
            newItem.setText(lang.getString("new"));
            if (currentUser != ADMIN) {
                importCSV.setText(lang.getString("importCSV"));
                export.setText(lang.getString("exportCSV"));
            }
            logout.setText(lang.getString("logout"));
        } else {
            login.setText(lang.getString("login"));
        }
        language.setText(lang.getString("language"));
        eng.setText(lang.getString("eng"));
        vie.setText(lang.getString("vie"));
        switchTheme.setText(lang.getString("switch"));
        exit.setText(lang.getString("exit"));

        userGuide.setText(lang.getString("userManual"));
        about.setText(lang.getString("about"));

        editItem.setText(lang.getString("edit"));
        deleteItem.setText(lang.getString("delete"));

        refresh.setText(lang.getString("refresh"));
        jump.setText(lang.getString("goTo"));
        next.setText(lang.getString("next"));
        previous.setText(lang.getString("previous"));
        first.setText(lang.getString("first"));
        last.setText(lang.getString("last"));
    }

    /**
     * Actions for each menu items
     */
    class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == newItem) { // Create new item
                JFrame frame = (JFrame) getTopLevelAncestor();
                if (currentUser != ADMIN) {
                    Song newSong = new Song("", "", "", "", 0);
                    JDialog editSong = new EditSongDialog("Add", model, newSong, frame, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    Account newAccount = new Staff("", "", "", "", "", "", "");
                    JDialog editAccount = new EditAccountDialog("Add", model, newAccount, frame, "", ModalityType.DOCUMENT_MODAL);
                }
            } else if (e.getSource() == editItem) { // Edit a particular item
                JFrame frame = (JFrame) getTopLevelAncestor();
                if (currentUser == ADMIN) {
                    Account account = model.getSelectedAccount();
                    if (account != null) {
                        JDialog editAccount = new EditAccountDialog("Edit", model, account, frame, "", ModalityType.DOCUMENT_MODAL);
                    } else {
                        JDialog error = new PopupInformDialog(model, model.getLanguage().getString("editMenuAccErr").toUpperCase(), frame, "ERROR", ModalityType.DOCUMENT_MODAL);
                    }
                } else {
                    Song song = model.getSelectedSong();
                    if (song != null) {
                        JDialog editSong = new EditSongDialog("Edit", model, song, frame, "", ModalityType.DOCUMENT_MODAL);
                    } else {
                        JDialog error = new PopupInformDialog(model, model.getLanguage().getString("editMenuErr").toUpperCase(), frame, "ERROR", ModalityType.DOCUMENT_MODAL);
                    }
                }
            } else if (e.getSource() == deleteItem) { // Delete item(s)
                final JFrame frame = (JFrame) getTopLevelAncestor();
                if (model.hasSongSelected() && currentUser == STAFF) {
                    PopupOptionDialog deleteSong = new PopupOptionDialog(model, model.getLanguage().getString("deleteMess").toUpperCase(), frame, "ATTENTION", ModalityType.DOCUMENT_MODAL) {

                        @Override
                        public void setAction() {
                            super.setAction();
                            model.deleteAllSelectedSong();
                            JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        }
                    };
                } else if (!model.hasSongSelected() && currentUser == STAFF) {
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("deleteErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                } else if (model.hasAccountSelected() && currentUser == ADMIN) {
                    boolean isAdmin = false;
                    for (Account account : model.getAccountList()) {
                        if (account.isSelected() && account.getUsername().equals("admin")) {
                            isAdmin = true;
                        }
                    }
                    if (isAdmin) {
                        JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteAdminErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                    } else {
                        PopupOptionDialog deleteAccount = new PopupOptionDialog(model, model.getLanguage().getString("deleteAccMess").toUpperCase(), frame, "ATTENTION", ModalityType.DOCUMENT_MODAL) {

                            @Override
                            public void setAction() {
                                super.setAction();
                                model.deleteAllSelectedAccount();
                                JDialog success = new PopupInformDialog(model, model.getLanguage().getString("deleteSuccess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                            }
                        };
                    }
                } else if (!model.hasAccountSelected() && currentUser == ADMIN) {
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("deleteAccErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                }
            } else if (e.getSource() == importCSV) { // Import CSV file(s)
                JFrame top = (JFrame) getTopLevelAncestor();

                ArrayList<Object> return_array = null;
                try {
                    return_array = open();
                } catch (FileNotFoundException ex) {
                    JFrame frame = (JFrame) getTopLevelAncestor();
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("fileNotFound").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                    System.out.println("file not found!");
                } catch (IOException ex) {
                    System.out.println("cannot access file!");
                }

                if (return_array == null) {
                    return;
                }

                if (((ArrayList<String>) return_array.get(0)).isEmpty()
                        && ((ArrayList<String[]>) return_array.get(1)).isEmpty()
                        && ((ArrayList<Song>) return_array.get(2)).isEmpty()) {
                    JFrame frame = (JFrame) getTopLevelAncestor();
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("importSuc").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                } else if (((ArrayList<String>) return_array.get(0)).isEmpty()) {
                    JFrame frame = (JFrame) getTopLevelAncestor();
                    JDialog error = new PopupImportDialog(model, (ArrayList<String[]>) return_array.get(1), (ArrayList<Song>) return_array.get(2), frame, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    JFrame frame = (JFrame) getTopLevelAncestor();
                    JDialog error = new PopupInformDialog(model, ((ArrayList<String>) return_array.get(0)).get(0), frame, "", ModalityType.DOCUMENT_MODAL);
                }
            } else if (e.getSource() == export) {
                FileFilter fi;
                fi = new FileNameExtensionFilter("CSV file", "csv");
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(fi);
                fc.setAcceptAllFileFilterUsed(false);
                int respone = fc.showSaveDialog(null);
                if (respone == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String dir = file.getAbsolutePath();
                    dir = dir.replaceAll("(.csv)+", ".csv");
                    if (!dir.contains(".csv")) {
                        dir = file.getAbsolutePath().concat(".csv");
                    }

                    model.writeToCSV(model.getSongDatabase(), dir);
                }
            } else if (e.getSource() == login) { // Sign in the system
                JDialog session = new PopupLoginDialog(model, (JFrame) getTopLevelAncestor());
            } else if (e.getSource() == refresh) { // Refresh the background image
                ((TransparentBackground) ((JFrame) getTopLevelAncestor()).getContentPane()).refresh();
            } else if (e.getSource() == switchTheme) { // Switch between theme
                JFrame topContainer = (JFrame) (getTopLevelAncestor());
                JDialog switchTheme = new PopupOptionDialog(model, model.getLanguage().getString("themeMes").toUpperCase(), topContainer, "", ModalityType.DOCUMENT_MODAL) {

                    @Override
                    public void setAction() {
                        super.setAction();
                        theme();
                    }
                };
            } else if (e.getSource() == eng) { // Change the system's display language
                model.setLanguageEN();
            } else if (e.getSource() == vie) {
                model.setLanguageVI();
            } else if (e.getSource() == logout) { // Sign out
                JFrame frame = (JFrame) getTopLevelAncestor();
                JDialog option = new PopupOptionDialog(model, model.getLanguage().getString("logoutMes").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

                    @Override
                    public void setAction() {
                        model.setCurrentAccount(null);
                        model.setSession(null);
                        model.setCurrentUser(CUSTOMER);
                    }
                };
            } else if (e.getSource() == exit) { // Quit the application
                final JFrame topContainer = (JFrame) (getTopLevelAncestor());
                JDialog exit = new PopupOptionDialog(model, model.getLanguage().getString("exitMes").toUpperCase(), topContainer, "", ModalityType.DOCUMENT_MODAL) {

                    @Override
                    public void setAction() {
                        super.setAction();
                        topContainer.dispose();
                        WindowEvent windowClosing = new WindowEvent(topContainer, WindowEvent.WINDOW_CLOSING);
                        topContainer.dispatchEvent(windowClosing);
                    }
                };
            } else if (e.getSource() == userGuide) { // Display user guide
                try {
                    Desktop.getDesktop().open(new File("user_manual/UserGuideHome.html"));
                } catch (IOException ex) {
                    System.out.println("cannot access file!");
                }
            } else if (e.getSource() == about) {
                JFrame frame = (JFrame) (getTopLevelAncestor());
                JDialog about = new About(frame);
            } else if (e.getSource() == next) {
                if (model.getCurrentPage() < model.getTotalPages()) {
                    model.setCurrentPage(model.getCurrentPage() + 1);
                }
            } else if (e.getSource() == previous) {
                if (model.getCurrentPage() > 1) {
                    model.setCurrentPage(model.getCurrentPage() - 1);
                }
            } else if (e.getSource() == first) {
                model.setCurrentPage(1);
            } else if (e.getSource() == last) {
                model.setCurrentPage(model.getTotalPages());
            } else if (e.getSource() == jump) {
                JFrame frame = (JFrame) getTopLevelAncestor();
                JDialog goToPage = new GoToPageDialog(model, frame);
            }
        }
    }
}

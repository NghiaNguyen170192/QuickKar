/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.util.Observable;
import quickkar.gui.reusable.dialog.PopupOptionDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import java.awt.Color;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.event.MouseEvent;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.reusable.dialog.EditAccountDialog;
import quickkar.gui.reusable.dialog.EditSongDialog;
import quickkar.gui.reusable.dialog.PopupImportDialog;
import quickkar.gui.reusable.dialog.PopupLoginDialog;
import quickkar.model.Account;
import quickkar.model.Database;
import quickkar.model.Song;
import quickkar.model.DatabaseFacade;
import quickkar.model.Staff;

/**
 * Construct the north part of the application GUI
 * @author Vinh
 */
public class TopPanel extends ContentPane implements CommonInterface, Observer {

    private ContentPane toolbarPanel, searchPanel, statisticsPanel;
    private JButton switchTheme, logout, login, importCSV, export;
    private JPanel topPanel, bottomPanel, add, delete, addDes, deleteDes, addWrap, deleteWrap;
    private JLabel addType, deleteType, user;
    private Interaction interaction;
    private DatabaseFacade model;
    private int currentUser;
    private JFileChooser csv;
    private FileFilter filter;
    private JPanel loginPanel, switchPanel, logoutPanel, importPanel, exportPanel;
    private JLabel loginLabel, switchLabel, logoutLabel, importLabel, exportLabel;

    public TopPanel(int currentUser, final DatabaseFacade model) {
        this.currentUser = currentUser;
        this.model = model;
        ((Database) model).addObserver(this);

        setLayout(new MigLayout("", "5[]5", "5[]5"));

        toolbarPanel = new ContentPane();
        if (model.getCurrentTheme() == THEME1) {
            toolbarPanel.setLayout(new MigLayout("", "0[][]0", "0[]0"));
        } else if (model.getCurrentUser() == STAFF) {
            toolbarPanel.setLayout(new MigLayout("", "0[][][][]0", "0[]0"));
        } else {
            toolbarPanel.setLayout(new MigLayout("", "0[][][]0", "0[]0"));
        }
        searchPanel = new SearchPanel(model);
        statisticsPanel = new StatisticsPanel(model);

        if (model.getCurrentTheme() == THEME1) {
            switchTheme = new JButton(model.getLanguage().getString("theme"));
            login = new JButton(model.getLanguage().getString("login"));
            switchTheme.setFocusable(false);
            login.setFocusable(false);

            toolbarPanel.add(login);
            toolbarPanel.add(switchTheme, "push, align right");
            switchTheme.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSwitchTheme();
                }
            });
            login.addActionListener(new SessionListener());
        } else if (model.getCurrentTheme() == THEME2) {
            (switchPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
            (loginPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
            switchPanel.add(switchLabel = new JLabel(SWITCH_2));
            loginPanel.add(loginLabel = new JLabel(LOGIN_2));
            switchLabel.setToolTipText(model.getLanguage().getString("switch"));
            loginLabel.setToolTipText(model.getLanguage().getString("login"));
            switchLabel.addMouseListener(new Action());
            loginLabel.addMouseListener(new Action());

            toolbarPanel.add(loginPanel);
            toolbarPanel.add(switchPanel);
            toolbarPanel.add(searchPanel, "w :42%:, gapleft 8%");
            toolbarPanel.add(statisticsPanel, "w :26%:, push, align right");
        }

        if (currentUser == CUSTOMER) {
            if (model.getCurrentTheme() == THEME1) {
                add(toolbarPanel, "w :100%:, span, wrap");
                add(statisticsPanel, "w :24%:");
                add(searchPanel, "w :42%:, gapleft 16%");
            } else if (model.getCurrentTheme() == THEME2) {
                add(toolbarPanel, "w :100%:");
            }
        } else {
            csv = new JFileChooser(new File("."));
            filter = new FileNameExtensionFilter("CSV file", "csv");
            csv.setFileFilter(filter);
            csv.setMultiSelectionEnabled(true);
            csv.setAcceptAllFileFilterUsed(false);

            topPanel = new ContentPane();
            if (currentUser == STAFF) {
                topPanel.setLayout(new MigLayout("", "0[]10[]10[][][]0", "0[]0"));
            } else {
                topPanel.setLayout(new MigLayout("", "0[]10[]10[]0", "0[]0"));
            }

            if (model.getCurrentTheme() == THEME1) {
                logout = new JButton(model.getLanguage().getString("logout"));
                logout.addActionListener(new SessionListener());
                topPanel.add(logout);
                logout.setFocusable(false);

                if (model.getCurrentAccount() != null) {
                    user = new JLabel(model.getLanguage().getString("welcome") + ", " + model.getCurrentAccount().getUsername());
                    topPanel.add(user);
                }

                if (currentUser == STAFF) {
                    importCSV = new JButton(model.getLanguage().getString("import"));
                    importCSV.setFocusable(false);
                    export = new JButton(model.getLanguage().getString("export"));
                    export.setFocusable(false);
                    topPanel.add(importCSV, "push, align right");
                    topPanel.add(export);
                    topPanel.add(switchTheme);
                    importCSV.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            importAction();
                        }
                    });
                    export.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            exportAction();
                        }
                    });

                    (bottomPanel = new ContentPane()).setLayout(new MigLayout("", "0[]10[]10[]0", "0[]0"));
                } else {
                    topPanel.add(switchTheme, "push, align right");

                    (bottomPanel = new ContentPane()).setLayout(new MigLayout("", "0[]10[]0", "0[]0"));
                }
            } else if (model.getCurrentTheme() == THEME2) {
                if (model.getCurrentUser() == STAFF) {
                    topPanel.setLayout(new MigLayout("", "0[][][][][]0", "0[]0"));
                } else {
                    topPanel.setLayout(new MigLayout("", "0[][][]0", "0[]0"));
                }
                (logoutPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
                logoutPanel.add(logoutLabel = new JLabel(LOGOUT_2));
                logoutLabel.setToolTipText(model.getLanguage().getString("logout"));
                logoutLabel.addMouseListener(new Action());

                if (model.getCurrentAccount() != null) {
                    user = new JLabel(model.getLanguage().getString("welcome") + model.getCurrentAccount().getUsername());
                }

                if (currentUser != CUSTOMER) {
                    (importPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
                    importPanel.add(importLabel = new JLabel(IMPORT_2));
                    importLabel.setToolTipText(model.getLanguage().getString("importCSV"));
                    importLabel.addMouseListener(new Action());
                    (exportPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
                    exportPanel.add(exportLabel = new JLabel(EXPORT_2));
                    exportLabel.setToolTipText(model.getLanguage().getString("exportCSV"));
                    exportLabel.addMouseListener(new Action());

                    topPanel.add(switchPanel);
                    if (model.getCurrentUser() == STAFF) {
                        topPanel.add(exportPanel);
                        topPanel.add(importPanel);
                    }
                    topPanel.add(user, "push, align right");
                    topPanel.add(logoutPanel, "align right");

                    if (model.getCurrentUser() == STAFF) {
                        (bottomPanel = new ContentPane()).setLayout(new MigLayout("", "0[]10[]10[]0", "0[]0"));
                    } else {
                        (bottomPanel = new ContentPane()).setLayout(new MigLayout("", "0[][]0", "0[]0"));
                    }
                } else {
                    topPanel.add(switchPanel);
                    topPanel.add(user, "push, align right");
                    topPanel.add(loginPanel, "align right");

                    (bottomPanel = new ContentPane()).setLayout(new MigLayout("", "0[]10[]0", "0[]0"));
                }
            }

            addWrap = new RoundedPanel(new MigLayout("", "0[]0", "0[]0"));
            ((RoundedPanel) addWrap).setOpa(INVISIBLE);
            deleteWrap = new RoundedPanel(new MigLayout("", "0[]0", "0[]0"));
            ((RoundedPanel) deleteWrap).setOpa(INVISIBLE);
            add = new RoundedPanel();
            ((RoundedPanel) add).setOpa(DEFAULT_VISIBLE);
            add.add(new JLabel(PLUS));
            add.setBackground(model.getWRAPPER_POST_COLOR());
            delete = new RoundedPanel();
            ((RoundedPanel) delete).setOpa(DEFAULT_VISIBLE);
            delete.add(new JLabel(MINUS));
            delete.setBackground(model.getWRAPPER_POST_COLOR());

            if (currentUser == STAFF) {
                addType = new JLabel(model.getLanguage().getString("addSong").toUpperCase());
            } else if (currentUser == ADMIN) {
                addType = new JLabel(model.getLanguage().getString("addAccount").toUpperCase());
            }
            addType.setFont(BOLD);
            addType.setForeground(model.getPOST_COLOR_BRIGHT());
            deleteType = new JLabel(model.getLanguage().getString("deleteSelected").toUpperCase());
            deleteType.setFont(BOLD);
            deleteType.setForeground(model.getPOST_COLOR_BRIGHT());
            addDes = new RoundedPanel(new MigLayout("", "", "5[]3"));
            ((RoundedPanel) addDes).setOpa(VISIBLE);
            addDes.add(addType);
            addDes.setFont(BOLD);
            addDes.setBackground(model.getWRAPPER_POST_COLOR());
            addDes.setForeground(model.getPOST_COLOR_BRIGHT());
            addDes.setVisible(false);
            deleteDes = new RoundedPanel(new MigLayout("", "", "5[]3"));
            ((RoundedPanel) deleteDes).setOpa(VISIBLE);
            deleteDes.add(deleteType);
            deleteDes.setFont(BOLD);
            deleteDes.setBackground(model.getWRAPPER_POST_COLOR());
            deleteDes.setForeground(model.getPOST_COLOR_BRIGHT());
            deleteDes.setVisible(false);

            addWrap.add(addDes);
            addWrap.add(add);
            deleteWrap.add(delete);
            deleteWrap.add(deleteDes);

            interaction = new Interaction();
            add.addMouseListener(interaction);
            delete.addMouseListener(interaction);

            bottomPanel.add(deleteWrap);
            if (currentUser == STAFF) {
                bottomPanel.add(searchPanel, "w :50%:, push, align center");
                bottomPanel.add(addWrap, "align right");
            } else {
                bottomPanel.add(addWrap, "push, align right");
            }

            add(topPanel, "w :100%:, wrap");
            add(bottomPanel, "w :100%:");
        }
    }

    private void importAction() {
        JFrame top = (JFrame) getTopLevelAncestor();

        ArrayList<Object> return_array = null;
        try {
            return_array = open();
        } catch (FileNotFoundException ex) {
            JFrame frame = (JFrame) getTopLevelAncestor();
            JDialog error = new PopupInformDialog(model, model.getLanguage().getString("fileNotFound").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
            System.out.println("language file not found!");
        } catch (IOException ex) {
            System.out.println("cannot access language file!");
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
    }

    private void exportAction() {
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
    }

    private ArrayList<Object> open() throws FileNotFoundException, IOException {
        if (csv.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return model.readFromCSV(csv.getSelectedFiles());
        }
        return null;
    }

    public void handleSwitchTheme() {
        JFrame topContainer = (JFrame) (getTopLevelAncestor());
        JDialog switchDialog = new PopupOptionDialog(model, model.getLanguage().getString("themeMes").toUpperCase(), topContainer, "", ModalityType.DOCUMENT_MODAL) {

            @Override
            public void setAction() {
                super.setAction();
                switchTheme();
            }
        };
    }

    public void switchTheme() {
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

    private void logoutAction() {
        JDialog option = new PopupOptionDialog(model, model.getLanguage().getString("logoutMes").toUpperCase(), model.getTopContainer(), "LOGOUT", ModalityType.DOCUMENT_MODAL) {

            @Override
            public void setAction() {
                model.setCurrentAccount(null);
                model.setSession(null);
                model.setCurrentUser(CUSTOMER);
            }
        };
    }

    @Override
    public void update(Observable o, Object arg) {
        ResourceBundle lang = ((DatabaseFacade) o).getLanguage();
        if (model.getCurrentTheme() == THEME1) {
            if (currentUser == CUSTOMER) {
                if (((DatabaseFacade) o).getSearchMode() == SIMPLE && searchPanel != null) {
                    remove(searchPanel);
                    add(searchPanel, "w :42%:, gapleft 16%");
                } else if (((DatabaseFacade) o).getSearchMode() == ADVANCED && searchPanel != null) {
                    remove(searchPanel);
                    add(searchPanel, "w :75%:, push, align right");
                }
                if (login != null) {
                    login.setText(lang.getString("login"));
                }
            } else {
                if (currentUser == STAFF) {
                    if (((DatabaseFacade) o).getSearchMode() == SIMPLE && searchPanel != null) {
                        bottomPanel.remove(addWrap);
                        bottomPanel.remove(searchPanel);
                        bottomPanel.add(searchPanel, "w :50%:, push, align center");
                        bottomPanel.add(addWrap, "align right");
                        addType.setText(lang.getString("addAccount").toUpperCase());
                        deleteType.setText(lang.getString("deleteSelected").toUpperCase());
                    } else if (((DatabaseFacade) o).getSearchMode() == ADVANCED && searchPanel != null) {
                        bottomPanel.remove(addWrap);
                        bottomPanel.remove(searchPanel);
                        bottomPanel.add(searchPanel, "w :70%:, push, align 42%");
                        bottomPanel.add(addWrap, "align right");
                        addType.setText(lang.getString("add").toUpperCase());
                        deleteType.setText(lang.getString("delete").toUpperCase());
                    }
                    if (importCSV != null) {
                        importCSV.setText(lang.getString("import"));
                    }
                    if (export != null) {
                        export.setText(lang.getString("export"));
                    }
                }
                if (logout != null) {
                    logout.setText(lang.getString("logout"));
                }
            }
            if (switchTheme != null) {
                switchTheme.setText(lang.getString("theme"));
            }
        } else {
            if (currentUser == CUSTOMER) {
                if (((DatabaseFacade) o).getSearchMode() == SIMPLE && searchPanel != null) {
                    toolbarPanel.remove(searchPanel);
                    toolbarPanel.remove(statisticsPanel);
                    toolbarPanel.add(searchPanel, "w :42%:, gapleft 8%");
                    toolbarPanel.add(statisticsPanel, "w :26%:, push, align right");
                    toolbarPanel.updateUI();
                } else if (((DatabaseFacade) o).getSearchMode() == ADVANCED && searchPanel != null) {
                    toolbarPanel.remove(searchPanel);
                    toolbarPanel.remove(statisticsPanel);
                    toolbarPanel.add(searchPanel, "w :65%:");
                    toolbarPanel.add(statisticsPanel, "w :26%:, push, align right");
                    toolbarPanel.updateUI();
                }
                if (loginLabel != null) {
                    loginLabel.setToolTipText(lang.getString("login"));
                }
            } else {
                if (currentUser == STAFF) {
                    if (((DatabaseFacade) o).getSearchMode() == SIMPLE && searchPanel != null) {
                        bottomPanel.remove(addWrap);
                        bottomPanel.remove(searchPanel);
                        bottomPanel.add(searchPanel, "w :50%:, push, align center");
                        bottomPanel.add(addWrap, "align right");
                        addType.setText(lang.getString("addAccount").toUpperCase());
                        deleteType.setText(lang.getString("deleteSelected").toUpperCase());
                    } else if (((DatabaseFacade) o).getSearchMode() == ADVANCED && searchPanel != null) {
                        bottomPanel.remove(addWrap);
                        bottomPanel.remove(searchPanel);
                        bottomPanel.add(searchPanel, "w :70%:, push, align 42%");
                        bottomPanel.add(addWrap, "align right");
                        addType.setText(lang.getString("add").toUpperCase());
                        deleteType.setText(lang.getString("delete").toUpperCase());
                    }
                    if (importLabel != null) {
                        importLabel.setToolTipText(lang.getString("importCSV"));
                    }
                    if (exportLabel != null) {
                        exportLabel.setToolTipText(lang.getString("exportCSV"));
                    }
                }
                if (logoutLabel != null) {
                    logoutLabel.setToolTipText(lang.getString("logout"));
                }
            }
            if (switchLabel != null) {
                switchLabel.setToolTipText(lang.getString("switch"));
            }
        }
        if (currentUser != CUSTOMER) {
//            if(addType != null){
//                if (currentUser == STAFF) {
//                    addType.setText(lang.getString("addSong").toUpperCase());
//                } else if (currentUser == ADMIN) {
//                    addType.setText(lang.getString("addAccount").toUpperCase());
//                }
//            }
//            if(deleteType != null){
//                deleteType.setText(lang.getString("deleteSelected").toUpperCase());
//            }
            if (model.getCurrentAccount() != null && user != null) {
                user.setText(lang.getString("welcome") + ", " + model.getCurrentAccount().getUsername());
            }
        }
    }

    class Interaction extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == add) {
                JFrame frame = (JFrame) getTopLevelAncestor();
                if (currentUser != ADMIN) {
                    Song newSong = new Song("", "", "", "", 0);
                    JDialog editSong = new EditSongDialog("Add", model, newSong, frame, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    Account newAccount = new Staff("", "", "", "", "", "", "");
                    JDialog editAccount = new EditAccountDialog("Add", model, newAccount, frame, "", ModalityType.DOCUMENT_MODAL);
                }
            } else if (e.getSource() == delete) {
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
                        PopupOptionDialog deleteAccount = new PopupOptionDialog(model, model.getLanguage().getString("deleteAccMess").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL) {

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
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == add) {
                add.setBackground(model.getWRAPPER_COLOR());
                addWrap.setBackground(model.getWRAPPER_POST_COLOR());
                ((RoundedPanel) addWrap).setOpa(VISIBLE);
                addDes.setVisible(true);
            } else if (e.getSource() == delete) {
                delete.setBackground(model.getWRAPPER_COLOR());
                deleteWrap.setBackground(model.getWRAPPER_POST_COLOR());
                ((RoundedPanel) deleteWrap).setOpa(VISIBLE);
                deleteDes.setVisible(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == add) {
                add.setBackground(model.getWRAPPER_POST_COLOR());
                addWrap.setBackground(getBackground());
                ((RoundedPanel) addWrap).setOpa(INVISIBLE);
                addDes.setVisible(false);
            } else if (e.getSource() == delete) {
                delete.setBackground(model.getWRAPPER_POST_COLOR());
                deleteWrap.setBackground(getBackground());
                ((RoundedPanel) deleteWrap).setOpa(INVISIBLE);
                deleteDes.setVisible(false);
            }
        }
    }

    class SessionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = (JFrame) getTopLevelAncestor();
            if (e.getSource() == logout) {
                logoutAction();
            } else if (e.getSource() == login) {
                JDialog session = new PopupLoginDialog(model, frame);
            }
        }
    }

    private class Action extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == loginLabel) {
                loginLabel.setIcon(LOGIN_HOVER_2);
            } else if (e.getSource() == switchLabel) {
                switchLabel.setIcon(SWITCH_HOVER_2);
            } else if (e.getSource() == logoutLabel) {
                logoutLabel.setIcon(LOGOUT_HOVER_2);
            } else if (e.getSource() == importLabel) {
                importLabel.setIcon(IMPORT_HOVER_2);
            } else if (e.getSource() == exportLabel) {
                exportLabel.setIcon(EXPORT_HOVER_2);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == loginLabel) {
                loginLabel.setIcon(LOGIN_2);
            } else if (e.getSource() == switchLabel) {
                switchLabel.setIcon(SWITCH_2);
            } else if (e.getSource() == logoutLabel) {
                logoutLabel.setIcon(LOGOUT_2);
            } else if (e.getSource() == importLabel) {
                importLabel.setIcon(IMPORT_2);
            } else if (e.getSource() == exportLabel) {
                exportLabel.setIcon(EXPORT_2);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == loginLabel) {
                JDialog session = new PopupLoginDialog(model, model.getTopContainer());
            } else if (e.getSource() == switchLabel) {
                handleSwitchTheme();
            } else if (e.getSource() == logoutLabel) {
                logoutAction();
            } else if (e.getSource() == importLabel) {
                importAction();
            } else if (e.getSource() == exportLabel) {
                exportAction();
            }
        }
    }
}

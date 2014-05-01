/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import quickkar.gui.reusable.effect.ContentPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 *
 * @author Vendetta7
 */
public class PopupImportDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private ArrayList<String[]> errors;
    private ArrayList<Song> duplicate;
    private JPanel panel;
    private JLabel error_title;
    private JLabel error_lines;
    private JLabel duplicated_songs;
    private JTextArea error_area;
    private JTextArea duplicate_area;
    private JScrollPane error_scroll;
    private JScrollPane duplicate_scroll;
    private JButton ok;

    public PopupImportDialog(DatabaseFacade model, ArrayList<String[]> errors, ArrayList<Song> duplicate, Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
        this.model = model;
        this.errors = errors;
        this.duplicate = duplicate;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel();
        ((RoundedPanel) panel).setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getWRAPPER_POST_COLOR());
        if (model.getCurrentTheme() == THEME2) {
            ((RoundedPanel) panel).setArcs(0, 0);
        }

        error_title = new JLabel(model.getLanguage().getString("error").toUpperCase());
        error_title.setFont(new Font("Calibri", Font.BOLD, 24));
        error_title.setForeground(model.getBOLD_COLOR());
        ok = new JButton(model.getLanguage().getString("ok").toUpperCase());
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (!errors.isEmpty() && !duplicate.isEmpty()) {
            panel.setSize(IMPORT_ERROR_WIDTH, IMPORT_ERROR_HEIGHT);
            panel.setLayout(new MigLayout("", "12%[]12%", "20[]20[][]20[][]20[]20"));
            panel.add(error_title, "align center, wrap");
            error_lines = new JLabel(model.getLanguage().getString("csvErr").toUpperCase());
            duplicated_songs = new JLabel(model.getLanguage().getString("songDupErr").toUpperCase());
            error_lines.setFont(BOLD);
            duplicated_songs.setFont(BOLD);
            error_lines.setForeground(model.getBOLD_COLOR());
            duplicated_songs.setForeground(model.getBOLD_COLOR());
            error_area = new JTextArea();
            duplicate_area = new JTextArea();
            error_area.setEditable(false);
            duplicate_area.setEditable(false);
            error_scroll = new JScrollPane(error_area);
            duplicate_scroll = new JScrollPane(duplicate_area);
            String error_string = new String();
            String duplicate_string = new String();
            int count = 0;
            for (String[] line : errors) {
                for (String string : line) {
                    error_string += string + " ";
                }
                error_string += "\n";
                count++;
            }
            error_string += count + model.getLanguage().getString("csvFoundErr").toUpperCase();
            count = 0;
            for (Song song : duplicate) {
                duplicate_string += song.getCode() + " " + song.getTitle() + "\n";
                count++;
            }
            duplicate_string += count + model.getLanguage().getString("songDupFoundErr").toUpperCase();
            error_area.setText(error_string);
            duplicate_area.setText(duplicate_string);
            panel.add(error_lines, "wrap");
            panel.add(error_scroll, "w :600:, h :400:, wrap");
            panel.add(duplicated_songs, "wrap");
            panel.add(duplicate_scroll, "w :600:, h :400:, wrap");
        } else if (!errors.isEmpty()) {
            panel.setSize(IMPORT_ERROR_WIDTH, IMPORT_ERROR_SMALL_HEIGHT);
            panel.setLayout(new MigLayout("", "12%[]12%", "20[]20[][]20[]20"));
            panel.add(error_title, "align center, wrap");
            error_lines = new JLabel(model.getLanguage().getString("songErr").toUpperCase());
            error_lines.setFont(BOLD);
            error_lines.setForeground(model.getBOLD_COLOR());
            error_area = new JTextArea();
            error_area.setEditable(false);
            error_scroll = new JScrollPane(error_area);
            String error_string = new String();
            int count = 0;
            for (String[] line : errors) {
                for (String string : line) {
                    error_string += string + " ";
                }
                error_string += "\n";
                count++;
            }
            error_string += count + model.getLanguage().getString("csvFoundErr").toUpperCase();
            error_area.setText(error_string);
            panel.add(error_lines, "wrap");
            panel.add(error_scroll, "w :600:, h :400:, wrap");
        } else if (!duplicate.isEmpty()) {
            panel.setSize(IMPORT_ERROR_WIDTH, IMPORT_ERROR_SMALL_HEIGHT);
            panel.setLayout(new MigLayout("", "12%[]12%", "20[]20[][]20[][]20[]20"));
            panel.add(error_title, "align center, wrap");
            duplicated_songs = new JLabel(model.getLanguage().getString("songDupErr").toUpperCase());
            duplicated_songs.setFont(BOLD);
            duplicated_songs.setForeground(model.getBOLD_COLOR());
            duplicate_area = new JTextArea();
            duplicate_area.setEditable(false);
            duplicate_scroll = new JScrollPane(duplicate_area);
            String duplicate_string = new String();
            int count = 0;
            for (Song song : duplicate) {
                duplicate_string += song.getCode() + " " + song.getTitle() + "\n";
                count++;
            }
            duplicate_string += count + model.getLanguage().getString("songDupFoundErr").toUpperCase();
            duplicate_area.setText(duplicate_string);
            panel.add(duplicated_songs, "wrap");
            panel.add(duplicate_scroll, "w :600:, h :400:, wrap");
        }
        panel.add(ok, "w :100:, h :50:, align center");

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }
}

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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.Database;
import quickkar.model.Song;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class EditSongDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private Song song;
    private JLabel topTitle, codeLabel, titleLabel, composerLabel, lyricLabel;
    private JTextField codeField, titleField, composerField;
    private JTextArea lyricField;
    private JButton update, cancel;
    private JPanel panel;
    private Window owner;
    private JScrollPane scroll;
    private String info;

    public EditSongDialog(String info, DatabaseFacade model, Song song, Window owner, String name, ModalityType modalityType) {
        super(owner, name, modalityType);
        this.info = info;
        this.owner = owner;
        this.model = model;
        this.song = song;

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setLayout(new MigLayout());
        setSize(owner.getWidth(), owner.getHeight());

        if (info.equals("Add")) {
            topTitle = new JLabel(model.getLanguage().getString("newSong").toUpperCase());
        } else {
            topTitle = new JLabel(model.getLanguage().getString("editSong").toUpperCase());
        }
        codeLabel = new JLabel(model.getLanguage().getString("code").toUpperCase());
        titleLabel = new JLabel(model.getLanguage().getString("title").toUpperCase());
        composerLabel = new JLabel(model.getLanguage().getString("composer").toUpperCase());
        lyricLabel = new JLabel(model.getLanguage().getString("lyric").toUpperCase());

        topTitle.setFont(new Font("Calibri", Font.BOLD, 24));
        codeLabel.setFont(BOLD);
        titleLabel.setFont(BOLD);
        composerLabel.setFont(BOLD);
        lyricLabel.setFont(BOLD);

        topTitle.setForeground(model.getBOLD_COLOR());
        codeLabel.setForeground(model.getBOLD_COLOR());
        titleLabel.setForeground(model.getBOLD_COLOR());
        composerLabel.setForeground(model.getBOLD_COLOR());
        lyricLabel.setForeground(model.getBOLD_COLOR());

        model.removeTags(this.song);
        codeField = new JTextField(this.song.getCode());
        titleField = new JTextField(this.song.getTitle());
        composerField = new JTextField(this.song.getComposer());
        lyricField = new JTextArea(this.song.getLyric());

        scroll = new JScrollPane(lyricField);

        lyricField.setLineWrap(true);
        lyricField.setWrapStyleWord(true);

        if (info.equals("Add")) {
            update = new JButton(model.getLanguage().getString("add"));
        } else {
            update = new JButton(model.getLanguage().getString("update"));
        }
        cancel = new JButton(model.getLanguage().getString("cancel"));

        panel = new RoundedPanel(new MigLayout("", "12%[]12%", "30[]20[][]20[][]20[][]20[][]20[]30"));
        ((RoundedPanel) panel).setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getNORMAL_COLOR());
        panel.setSize(EDIT_WIDTH, EDIT_HEIGHT);
        if (model.getCurrentTheme() == THEME2) {
            ((RoundedPanel) panel).setArcs(0, 0);
        }

        panel.add(topTitle, "align center, wrap");
        panel.add(codeLabel, "wrap");
        panel.add(codeField, "w :400:, h :25:, wrap");
        panel.add(titleLabel, "wrap");
        panel.add(titleField, "w :400:, h :25:, wrap");
        panel.add(composerLabel, "wrap");
        panel.add(composerField, "w :400:, h :25:, wrap");
        panel.add(lyricLabel, "wrap");
        panel.add(scroll, "w :400:, h :150:, wrap");
        panel.add(update, "w :60:, h :36:, split 2");
        panel.add(cancel, "w :60:, h :36:, gapleft 270");

        ActionListener validation = new ValidationListener();
        codeField.addActionListener(validation);
        titleField.addActionListener(validation);
        composerField.addActionListener(validation);
        update.addActionListener(validation);
        cancel.addActionListener(validation);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    public void editProcessing() {
        String code = codeField.getText();
        String title = titleField.getText().toUpperCase();
        String composer = composerField.getText();
        String lyric = lyricField.getText();
        if (code.equals("") || title.equals("") || composer.equals("") || lyric.equals("")) {
            JDialog error = new PopupInformDialog(model, model.getLanguage().getString("emptyErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
        } else if (info.equals("Add")) {
            song = new Song(code, title, lyric, composer, Database.latestID + 1);
            if (model.addSong(song)) {
                Database.latestID++;
                dispose();
                JDialog success = new PopupInformDialog(model, model.getLanguage().getString("addMes").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            } else {
                JDialog conflict = new PopupInformDialog(model, model.getLanguage().getString("codeErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
            }
        } else if (model.editSong(song, code, title, lyric, composer)) {
            dispose();
            JDialog success = new PopupInformDialog(model, model.getLanguage().getString("updateMes").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
        } else {
            JDialog conflict = new PopupInformDialog(model, model.getLanguage().getString("codeErr").toUpperCase(), owner, "", ModalityType.DOCUMENT_MODAL);
        }
    }

    class ValidationListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancel) {
                dispose();
            } else {
                editProcessing();
            }
        }
    }
}

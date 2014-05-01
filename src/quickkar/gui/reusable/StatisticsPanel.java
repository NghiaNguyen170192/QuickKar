/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 * Provide additional information when search or browse songs.
 * @author Vinh
 */
public class StatisticsPanel extends ContentPane implements Observer {

    private DatabaseFacade model;
    private JLabel display, item;
    private String outOf, result, second;

    public StatisticsPanel(DatabaseFacade model) {
//        super(0.2f);
        ((Database) model).addObserver(this);
        setLayout(new MigLayout("", "0[]0", "3[]0[]0"));
        add(display = new JLabel(" "), "wrap");
        add(item = new JLabel());
        outOf = model.getLanguage().getString("outOf");
        if (model.getSongDatabase().size() > 11) {
            item.setText("1 - 11 " + outOf + " " + model.getSongDatabase().size());
            item.setFont(new Font("Calibri", Font.PLAIN, 13));
        } else if (!model.getSongDatabase().isEmpty()) {
            item.setText("1 - " + model.getSongDatabase().size() + " " + outOf + " " + model.getSongDatabase().size());
            item.setFont(new Font("Calibri", Font.PLAIN, 13));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Song song = null;
        outOf = ((DatabaseFacade) o).getLanguage().getString("outOf");
        result = ((DatabaseFacade) o).getLanguage().getString("result");
        second = ((DatabaseFacade) o).getLanguage().getString("second");
        if (!((DatabaseFacade) o).getSongList().isEmpty()) {
            song = ((DatabaseFacade) o).getSongList().get(0);

            String search = "<html>";
            if (song.getCode().contains(search)
                    || song.getTitle().contains(search)
                    || song.getComposer().contains(search)
                    || song.getLyric().contains(search)) {
                int searchResults = ((DatabaseFacade) o).getSearchResults();
                float searchTime = ((DatabaseFacade) o).getSearchTime();
                display.setText(searchResults + " " + result + " (" + searchTime + " " + second + ")");
                display.setFont(new Font("Calibri", Font.PLAIN, 13));
                display.setVisible(true);
            } else {
                display.setVisible(false);
            }
        } else {
            display.setVisible(false);
        }

        ArrayList<Song> songList = ((DatabaseFacade) o).getSongList();
        int currentPage = ((DatabaseFacade) o).getCurrentPage();
        int totalPages = ((DatabaseFacade) o).getTotalPages();

        if (songList.size() > 11) {
            if (currentPage == 1) {
                item.setText("1 - 11 " + outOf + " " + songList.size());
            } else if (currentPage == totalPages) {
                item.setText(((currentPage - 1) * 11 + 1) + " - " + songList.size() + " " + outOf + " " + songList.size());
            } else {
                item.setText(((currentPage - 1) * 11 + 1) + " - " + currentPage * 11 + " " + outOf + " " + songList.size());
            }
            item.setVisible(true);
        } else if (!songList.isEmpty()) {
            item.setText("1 - " + songList.size() + " " + outOf + " " + songList.size());
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
    }
}

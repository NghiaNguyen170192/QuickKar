/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Freedom
 */
public class SortingPanel extends RoundedPanel implements CommonInterface, Observer {

    private JPanel codePanel, titlePanel, lyricPanel, composerPanel, ratingPanel;
    private JLabel code, title, lyric, composer, rate, codeOrder, titleOrder, lyricOrder, composerOrder, rateOrder;
    private DatabaseFacade model;

    public SortingPanel(DatabaseFacade model) {
        this.model = model;
        ((Database) model).addObserver(this);

        if (model.getCurrentTheme() == THEME1) {
            setLayout(new MigLayout("", "0[]2[]2[]2[]2[]0", "0[]0"));
        } else {
            setLayout(new MigLayout("", "0[]1[]1[]1[]1[]0", "0[]0"));
        }
        setArcs(15, 15);
        setOpa(INVISIBLE);
        setBackground(model.getWRAPPER_POST_COLOR());

        ((RoundedPanel) (codePanel = new RoundedPanel(new MigLayout("", "", "3[]3")))).setOpa(DEFAULT_VISIBLE);
        ((RoundedPanel) (titlePanel = new RoundedPanel(new MigLayout("", "", "3[]3")))).setOpa(DEFAULT_VISIBLE);
        ((RoundedPanel) (lyricPanel = new RoundedPanel(new MigLayout("", "", "3[]3")))).setOpa(DEFAULT_VISIBLE);
        ((RoundedPanel) (composerPanel = new RoundedPanel(new MigLayout("", "", "3[]3")))).setOpa(DEFAULT_VISIBLE);
        ((RoundedPanel) (ratingPanel = new RoundedPanel(new MigLayout("", "", "3[]3")))).setOpa(DEFAULT_VISIBLE);

        if (model.getCurrentTheme() == THEME2) {
            ((RoundedPanel) codePanel).setArcs(0, 0);
            ((RoundedPanel) titlePanel).setArcs(0, 0);
            ((RoundedPanel) lyricPanel).setArcs(0, 0);
            ((RoundedPanel) composerPanel).setArcs(0, 0);
            ((RoundedPanel) ratingPanel).setArcs(0, 0);
        }

        codePanel.setBackground(model.getPOST_COLOR());
        titlePanel.setBackground(model.getPOST_COLOR());
        lyricPanel.setBackground(model.getPOST_COLOR());
        composerPanel.setBackground(model.getPOST_COLOR());
        ratingPanel.setBackground(model.getPOST_COLOR());

        (code = new JLabel(model.getLanguage().getString("code"))).setFont(PLAIN);
        (title = new JLabel(model.getLanguage().getString("title"))).setFont(PLAIN);
        (lyric = new JLabel(model.getLanguage().getString("lyric"))).setFont(PLAIN);
        (composer = new JLabel(model.getLanguage().getString("composer"))).setFont(PLAIN);
        (rate = new JLabel(model.getLanguage().getString("rate"))).setFont(PLAIN);

        codePanel.add(code);
        codePanel.add((codeOrder = new JLabel(DEFAULT_EMPTY)), "push, align right");
        titlePanel.add(title);
        titlePanel.add((titleOrder = new JLabel(DEFAULT_EMPTY)), "push, align right");
        lyricPanel.add(lyric);
        lyricPanel.add((lyricOrder = new JLabel(DEFAULT_EMPTY)), "push, align right");
        composerPanel.add(composer);
        composerPanel.add((composerOrder = new JLabel(DEFAULT_EMPTY)), "push, align right");
        ratingPanel.add(rate);
        ratingPanel.add((rateOrder = new JLabel(DEFAULT_EMPTY)), "push, align right");
        add(codePanel, "w :20%:");
        add(titlePanel, "w :20%:");
        add(lyricPanel, "w :20%:");
        add(composerPanel, "w :20%:");
        add(ratingPanel, "w :20%:");

        MouseListener sort = new Sort();
        codePanel.addMouseListener(sort);
        titlePanel.addMouseListener(sort);
        lyricPanel.addMouseListener(sort);
        composerPanel.addMouseListener(sort);
        ratingPanel.addMouseListener(sort);
    }

    @Override
    public void update(Observable o, Object arg) {
        ResourceBundle lang = ((DatabaseFacade) o).getLanguage();
        code.setText(lang.getString("code"));
        title.setText(lang.getString("title"));
        lyric.setText(lang.getString("lyric"));
        composer.setText(lang.getString("composer"));
        rate.setText(lang.getString("rate"));
        int type = ((DatabaseFacade) o).getSortType();
        switch (type) {
            case CODE:
                codeOrder.setIcon(ASCENDING);
                break;
            case CODE_REVERSE:
                codeOrder.setIcon(DESCENDING);
                break;
            case TITLE:
                titleOrder.setIcon(ASCENDING);
                break;
            case TITLE_REVERSE:
                titleOrder.setIcon(DESCENDING);
                break;
            case LYRIC:
                lyricOrder.setIcon(ASCENDING);
                break;
            case LYRIC_REVERSE:
                lyricOrder.setIcon(DESCENDING);
                break;
            case COMPOSER:
                composerOrder.setIcon(ASCENDING);
                break;
            case COMPOSER_REVERSE:
                composerOrder.setIcon(DESCENDING);
                break;
            case RATING:
                rateOrder.setIcon(ASCENDING);
                break;
            case RATING_REVERSE:
                rateOrder.setIcon(DESCENDING);
                break;
            default:
                codeOrder.setIcon(DEFAULT_EMPTY);
                titleOrder.setIcon(DEFAULT_EMPTY);
                lyricOrder.setIcon(DEFAULT_EMPTY);
                composerOrder.setIcon(DEFAULT_EMPTY);
                rateOrder.setIcon(DEFAULT_EMPTY);
                codePanel.setBackground(model.getPOST_COLOR());
                titlePanel.setBackground(model.getPOST_COLOR());
                lyricPanel.setBackground(model.getPOST_COLOR());
                composerPanel.setBackground(model.getPOST_COLOR());
                ratingPanel.setBackground(model.getPOST_COLOR());
                code.setFont(PLAIN);
                title.setFont(PLAIN);
                lyric.setFont(PLAIN);
                composer.setFont(PLAIN);
                rate.setFont(PLAIN);
        }
    }

    private class Sort extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == codePanel) {
                code.setFont(TYPE);
                codePanel.setBackground(model.getPOST_COLOR_BRIGHT());
            } else if (e.getSource() == titlePanel) {
                title.setFont(TYPE);
                titlePanel.setBackground(model.getPOST_COLOR_BRIGHT());
            } else if (e.getSource() == lyricPanel) {
                lyric.setFont(TYPE);
                lyricPanel.setBackground(model.getPOST_COLOR_BRIGHT());
            } else if (e.getSource() == composerPanel) {
                composer.setFont(TYPE);
                composerPanel.setBackground(model.getPOST_COLOR_BRIGHT());
            } else if (e.getSource() == ratingPanel) {
                rate.setFont(TYPE);
                ratingPanel.setBackground(model.getPOST_COLOR_BRIGHT());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == codePanel && model.getSortType() != CODE && model.getSortType() != CODE_REVERSE) {
                code.setFont(PLAIN);
                codePanel.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == titlePanel && model.getSortType() != TITLE && model.getSortType() != TITLE_REVERSE) {
                title.setFont(PLAIN);
                titlePanel.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == lyricPanel && model.getSortType() != LYRIC && model.getSortType() != LYRIC_REVERSE) {
                lyric.setFont(PLAIN);
                lyricPanel.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == composerPanel && model.getSortType() != COMPOSER && model.getSortType() != COMPOSER_REVERSE) {
                composer.setFont(PLAIN);
                composerPanel.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == ratingPanel && model.getSortType() != RATING && model.getSortType() != RATING_REVERSE) {
                rate.setFont(PLAIN);
                ratingPanel.setBackground(model.getPOST_COLOR());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            code.setFont(PLAIN);
            title.setFont(PLAIN);
            lyric.setFont(PLAIN);
            composer.setFont(PLAIN);
            rate.setFont(PLAIN);
            codeOrder.setIcon(DEFAULT_EMPTY);
            titleOrder.setIcon(DEFAULT_EMPTY);
            lyricOrder.setIcon(DEFAULT_EMPTY);
            composerOrder.setIcon(DEFAULT_EMPTY);
            rateOrder.setIcon(DEFAULT_EMPTY);
            codePanel.setBackground(model.getPOST_COLOR());
            titlePanel.setBackground(model.getPOST_COLOR());
            lyricPanel.setBackground(model.getPOST_COLOR());
            composerPanel.setBackground(model.getPOST_COLOR());
            ratingPanel.setBackground(model.getPOST_COLOR());
            if (e.getSource() == codePanel) {
                code.setFont(TYPE);
                codePanel.setBackground(model.getPOST_COLOR_BRIGHT());
                if (model.getSortType() == CODE) {
                    model.sort(CODE, true);
                    model.setSortType(CODE_REVERSE);
                } else if (model.getSortType() == CODE_REVERSE) {
                    model.sort(CODE, false);
                    model.setSortType(DEFAULT);
                } else {
                    model.sort(CODE, false);
                    model.setSortType(CODE);
                }
            } else if (e.getSource() == titlePanel) {
                title.setFont(TYPE);
                titlePanel.setBackground(model.getPOST_COLOR_BRIGHT());
                if (model.getSortType() == TITLE) {
                    model.sort(TITLE, true);
                    model.setSortType(TITLE_REVERSE);
                } else if (model.getSortType() == TITLE_REVERSE) {
                    model.sort(DEFAULT, false);
                    model.setSortType(DEFAULT);
                } else {
                    model.sort(TITLE, false);
                    model.setSortType(TITLE);
                }
            } else if (e.getSource() == lyricPanel) {
                lyric.setFont(TYPE);
                lyricPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                if (model.getSortType() == LYRIC) {
                    model.sort(LYRIC, true);
                    model.setSortType(LYRIC_REVERSE);
                } else if (model.getSortType() == LYRIC_REVERSE) {
                    model.sort(DEFAULT, false);
                    model.setSortType(DEFAULT);
                } else {
                    model.sort(LYRIC, false);
                    model.setSortType(LYRIC);
                }
            } else if (e.getSource() == composerPanel) {
                composer.setFont(TYPE);
                composerPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                if (model.getSortType() == COMPOSER) {
                    model.sort(COMPOSER, true);
                    model.setSortType(COMPOSER_REVERSE);
                } else if (model.getSortType() == COMPOSER_REVERSE) {
                    model.sort(DEFAULT, false);
                    model.setSortType(DEFAULT);
                } else {
                    model.sort(COMPOSER, false);
                    model.setSortType(COMPOSER);
                }
            } else if (e.getSource() == ratingPanel) {
                rate.setFont(TYPE);
                ratingPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                if (model.getSortType() == RATING) {
                    model.sort(RATING, true);
                    model.setSortType(RATING_REVERSE);
                } else if (model.getSortType() == RATING_REVERSE) {
                    model.sort(DEFAULT, false);
                    model.setSortType(DEFAULT);
                } else {
                    model.sort(RATING, false);
                    model.setSortType(RATING);
                }
            }
            model.setCurrentPage(FIRST);
        }
    }
}

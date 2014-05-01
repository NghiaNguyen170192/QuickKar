/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.gui.theme.common.CommonInterface;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;

/**
 * Construct pagination for navigation purpose
 * @author Vinh
 */
public class PaginatorPanel extends RoundedPanel implements CommonInterface, Observer {

    private ImageIcon iconFirst, iconLast;
    private JPanel pages, first, last;
    private JPanel[] pageArray;
    private final int DEFAULT = 5, FIRST_PAGE = 1;
    private DatabaseFacade model;

    public PaginatorPanel(int currentUser, DatabaseFacade model) {
        this.model = model;
        ((Database) this.model).addObserver(this);
        setLayout(new MigLayout("", "2[]2", "2[]2"));
        if (model.getCurrentTheme() == THEME1) {
            setArcs(15, 15);
        } else {
            setArcs(0, 0);
        }
        setBackground(model.getSWITCHER_COLOR());

        if (model.getCurrentTheme() == THEME1) {
            iconFirst = ICONFIRST_1;
            iconLast = ICONLAST_1;
        } else {
            iconFirst = ICONFIRST_2;
            iconLast = ICONLAST_2;
        }

        if (model.getCurrentTheme() == THEME2 && currentUser == CUSTOMER) {
            pages = new RoundedPanel(new MigLayout("wrap", "0[]0", ""));
        } else {
            pages = new RoundedPanel(new MigLayout("", "", "0[]0"));
        }
        ((RoundedPanel) pages).setOpa(INVISIBLE);
        pages.setBackground(model.getWRAPPER_POST_COLOR());
        pageArray = new NumberPagePanel[DEFAULT];

        first = new RoundedPanel(new MigLayout("", "5[]5", "4[]4"));
        ((RoundedPanel) first).setOpa(DEFAULT_VISIBLE);
        first.setBackground(model.getPAGE_COLOR_BRIGHT());
        first.add(new JLabel(iconFirst));

        last = new RoundedPanel(new MigLayout("", "5[]5", "4[]4"));
        ((RoundedPanel) last).setOpa(DEFAULT_VISIBLE);
        last.setBackground(model.getPAGE_COLOR_NORMAL());
        last.add(new JLabel(iconLast));

        if (model.getCurrentTheme() == THEME2 && currentUser == CUSTOMER) {
            JPanel top = new ContentPane();
            top.add(first, "push, align center");
            add(top, "dock north");
            add(pages, "push, align center");
            JPanel bottom = new ContentPane();
            bottom.add(last, "push, align center");
            add(bottom, "dock south");
        } else if (model.getCurrentTheme() == THEME2) {
            add(pages, "push, align center");
        } else {
            add(first);
            add(pages, "push, align center");
            add(last);
        }

        first.addMouseListener(new PagingListener());
        last.addMouseListener(new PagingListener());

        displayPages(FIRST_PAGE);
    }

    /**
     * Determine how would number pagination is displayed through current page number
     * @param currentPage   Current selected page by user
     */
    public void displayPages(int currentPage) {
        int totalPages = model.getTotalPages();
        int requirePage = currentPage;
        pages.removeAll();
        if (totalPages <= 5) {
            for (int i = 0; i < totalPages; i++) {
                pageArray[i] = new NumberPagePanel(i + 1, model);
                pageArray[i].add(new JLabel("" + (i + 1)));
                pageArray[i].addMouseListener(new PagingListener());
                if (i + 1 == currentPage) {
                    pageArray[i].setBackground(model.getPAGE_COLOR_BRIGHT());
                } else {
                    pageArray[i].setBackground(model.getPOST_COLOR());
                }
                pages.add(pageArray[i]);
            }
        } else {
            if (currentPage < 3) {
                requirePage = 3;
            } else if (currentPage > totalPages - 2) {
                requirePage = totalPages - 2;
            }
            for (int i = 0, j = requirePage - 2; j < requirePage + 3; i++, j++) {
                pageArray[i] = new NumberPagePanel(j, model);
                pageArray[i].add(new JLabel("" + j));
                pageArray[i].addMouseListener(new PagingListener());
                if (j == currentPage) {
                    pageArray[i].setBackground(model.getPAGE_COLOR_BRIGHT());
                } else {
                    pageArray[i].setBackground(model.getPOST_COLOR());
                }
                pages.add(pageArray[i]);
            }
        }
        pages.updateUI();
    }

    /**
     * Obtain and display pages from user interaction
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        int currentPage = ((Database) o).getCurrentPage();
        int totalPages = ((Database) o).getTotalPages();
        displayPages(currentPage);
        renderColor(currentPage, totalPages);
    }

    public void renderColor(int currentPage, int totalPages) {
        if (currentPage != 1) {
            first.setBackground(model.getPAGE_COLOR_NORMAL());
        } else {
            first.setBackground(model.getPAGE_COLOR_BRIGHT());
        }

        if (currentPage != totalPages) {
            last.setBackground(model.getPAGE_COLOR_NORMAL());
        } else {
            last.setBackground(model.getPAGE_COLOR_BRIGHT());
        }
    }

    /**
     * Actions for user interactions
     */
    class PagingListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JPanel current = (RoundedPanel) e.getSource();
            for (int i = 0; i < 5; i++) {
                if (current == pageArray[i]) {
                    model.setCurrentPage(((NumberPagePanel) pageArray[i]).getPageNumber());
                    break;
                }
            }

            if (e.getSource() == first) {
                model.setCurrentPage(1);
            } else if (e.getSource() == last) {
                model.setCurrentPage(model.getTotalPages());
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {

            JPanel current = (RoundedPanel) e.getSource();
            for (int i = 0; i < 5; i++) {
                if (current == pageArray[i]) {
                    pageArray[i].setBackground(model.getPAGE_COLOR_BRIGHT());
                }
            }

            if (e.getSource() == first) {
                first.setBackground(model.getPAGE_COLOR_BRIGHT());
            } else if (e.getSource() == last) {
                last.setBackground(model.getPAGE_COLOR_BRIGHT());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

            JPanel current = (RoundedPanel) e.getSource();
            int currentPage = model.getCurrentPage();
            for (int i = 0; i < 5; i++) {
                if (current == pageArray[i] && currentPage != ((NumberPagePanel) pageArray[i]).getPageNumber()) {
                    pageArray[i].setBackground(model.getPAGE_COLOR_NORMAL());
                }
            }

            if (e.getSource() == first && model.getCurrentPage() != 1) {
                first.setBackground(model.getPAGE_COLOR_NORMAL());
            } else if (e.getSource() == last && model.getCurrentPage() != model.getTotalPages()) {
                last.setBackground(model.getPAGE_COLOR_NORMAL());
            }
        }
    }
}

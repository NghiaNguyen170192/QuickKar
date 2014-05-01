/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import quickkar.model.DatabaseFacade;

/**
 * Contains the south part of the application GUI
 * @author Vinh
 */
public class WrapperPanel extends RoundedPanel implements CommonInterface {

    private JPanel switchLeft, switchRight, paginator, wrapperPost, feed, switchFirst, switchLast;
    private DatabaseFacade model;
    private PagingListener paging;
    private JScrollPane wrapperScroll;

    public WrapperPanel(int currentUser, DatabaseFacade model) {
        super.setOpa(DEFAULT_INVISIBLE);
        this.model = model;
        setLayout(new MigLayout("", "3[]3", "3[]3"));
        if (model.getCurrentTheme() == THEME1) {
            setBackground(model.getWRAPPER_COLOR());
        } else {
            setBackground(model.getWRAPPER_COLOR());
            setArcs(0, 0);
        }

        paging = new PagingListener();
        switchLeft = new SwitcherPanel(currentUser, model, LEFT, SWITCH);
        switchRight = new SwitcherPanel(currentUser, model, RIGHT, SWITCH);
        paginator = new PaginatorPanel(currentUser, model);
        wrapperPost = new WrapperPostPanel(currentUser, model);


        if (model.getCurrentTheme() == THEME1) {
            feed = new WrapperFeedPanel(currentUser, model);
            if (currentUser == CUSTOMER) {
                add(feed, "w :24%:, h :100%:, span 1 2");
                add(switchLeft, "w :8%:, h :100%:, span 1 2");
                add(paginator, "w :60%:");
                add(switchRight, "w :8%:, h :100%, span 1 2, wrap");
                add(wrapperPost, "w :60%:, h :100%:");
            } else {
                add(switchLeft, "w :15%:, h :100%:, span 1 2");
                add(paginator, "w :70%:");
                add(switchRight, "w :15%:, h :100%, span 1 2, wrap");
                add(wrapperPost, "w :70%:, h :100%:");
            }
        } else if (model.getCurrentTheme() == THEME2) {
            (switchFirst = new SwitcherPanel(currentUser, model, LEFT, FIRST)).addMouseListener(paging);
            (switchLast = new SwitcherPanel(currentUser, model, RIGHT, LAST)).addMouseListener(paging);
            feed = new WrapperFeedPanel(currentUser, model);
            if (currentUser == CUSTOMER) {
                add(switchLeft, "w :80%:, h :0%:, span 2");
                add(feed, "w :30%:, h :100%:, span 1 3, wrap");
                add(wrapperPost, "w :70%:, h :93%:");
                add(paginator, "w :7%:, h :93%:, wrap");
                add(switchRight, "w :80%:, h :0%:, span 2");
            } else {
                add(switchFirst, "w :8%:, h :100%:, span 1 2");
                add(switchLeft, "w :8%:, h :100%:, span 1 2");
                add(paginator, "w :70%:");
                add(switchRight, "w :8%:, h :100%, span 1 2");
                add(switchLast, "w :8%:, h :100%:, span 1 2, wrap");
                add(wrapperPost, "w :70%:, h :100%:");
            }
        }

        switchLeft.addMouseListener(paging);
        switchRight.addMouseListener(paging);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "pagingFirst");
        this.getActionMap().put("pagingFirst", new FirstPaging());

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "pagingLast");
        this.getActionMap().put("pagingLast", new LastPaging());

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "pagingLeft");
        this.getActionMap().put("pagingLeft", new LeftPaging());

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "pagingRight");
        this.getActionMap().put("pagingRight", new RightPaging());
    }

    class PagingListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int totalPages = model.getTotalPages();
            int currentPage = model.getCurrentPage();
            if (e.getSource() == switchLeft && currentPage > 1) {
                model.setCurrentPage(--currentPage);
            } else if (e.getSource() == switchRight && currentPage < totalPages) {
                model.setCurrentPage(++currentPage);
            } else if (e.getSource() == switchFirst) {
                model.setCurrentPage(1);
            } else if (e.getSource() == switchLast) {
                model.setCurrentPage(model.getTotalPages());
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == switchLeft) {
                switchLeft.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == switchRight) {
                switchRight.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == switchFirst) {
                switchFirst.setBackground(model.getPOST_COLOR());
            } else if (e.getSource() == switchLast) {
                switchLast.setBackground(model.getPOST_COLOR());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == switchLeft) {
                switchLeft.setBackground(model.getSWITCHER_COLOR());
            } else if (e.getSource() == switchRight) {
                switchRight.setBackground(model.getSWITCHER_COLOR());
            } else if (e.getSource() == switchFirst) {
                switchFirst.setBackground(model.getSWITCHER_COLOR());
            } else if (e.getSource() == switchLast) {
                switchLast.setBackground(model.getSWITCHER_COLOR());
            }
        }
    }

    class RightPaging extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentPage() < model.getTotalPages()) {
                model.setCurrentPage(model.getCurrentPage() + 1);
            }
        }
    }

    class LeftPaging extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getCurrentPage() > 1) {
                model.setCurrentPage(model.getCurrentPage() - 1);
            }
        }
    }

    class FirstPaging extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setCurrentPage(1);
        }
    }

    class LastPaging extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setCurrentPage(model.getTotalPages());
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import com.gargoylesoftware.htmlunit.javascript.host.Event;
import java.awt.AWTException;
import java.awt.Font;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import quickkar.gui.theme.common.CommonInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.model.Database;
import quickkar.model.Song;
import quickkar.model.DatabaseFacade;

/**
 * Construct search panel as well as searching functionality.
 * P/S: Vital class since it provides the search functions.
 * @author Nhan Dao Vinh
 */
public class SearchPanel extends ContentPane implements CommonInterface, KeyListener, FocusListener, Observer {

    private DatabaseFacade model;
    private ContentPane searchPanel;
    private ContentPane advancedPanel;
    private JLabel searchLabel;
    private JLabel advancedLabel;
    private JLabel exactLabel;
    private JLabel anyLabel;
    private JLabel noneLabel;
    private JTextField searchField;
    private JTextField exactField;
    private JTextField anyField;
    private JTextField noneField;
    private int time;
    private long startTime, endTime;
    private boolean simpleFlag, advancedFlag;
    private static final int THREADS = 4;
    private Timer t = new Timer(700, new HandleSimpleSearch());
    private long startTimeForSearch = 0, endTimeForSearch = 0;
    private int timeEnter = 1;
    private HandleSimpleSearch1 simple;
    private HandleAdvancedSearch advanced;
    private Search search;

    public SearchPanel(final DatabaseFacade model) {
        this.model = model;
        simpleFlag = true;
        ((Database) model).addObserver(this);

        initializeSearchContent();
    }

    private void initializeSearchContent() {
        model.setSearchMode(DEFAULT);
        if (model.getCurrentTheme() == THEME1) {
            (searchPanel = new ContentPane(DEFAULT_VISIBLE, 33, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "20[][]0", "0[]0"));
            (advancedPanel = new ContentPane(DEFAULT_VISIBLE, 33, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
        } else {
            (searchPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "[][]0", "0[]0"));
            (advancedPanel = new ContentPane(DEFAULT_VISIBLE, 20, model.getSEARCH_COLOR())).setLayout(new MigLayout("", "0[]0", "0[]0"));
        }

        setLayout(new MigLayout("", "0[][]0", "0[]0"));

        searchField = new JTextField();
        if (model.getCurrentTheme() == THEME1) {
            (searchLabel = new JLabel(SEARCH)).setToolTipText(model.getLanguage().getString("search"));
            (advancedLabel = new JLabel(ADVANCED_SEARCH)).setToolTipText(model.getLanguage().getString("advancedSearch"));
            searchPanel.add(searchField, "w :100%:");
        } else if (model.getCurrentTheme() == THEME2) {
            (searchLabel = new JLabel(SEARCH_2)).setToolTipText(model.getLanguage().getString("search"));
            (advancedLabel = new JLabel(ADVANCED_SEARCH_2)).setToolTipText(model.getLanguage().getString("advancedSearch"));
            searchPanel.add(searchField, "w :100%:, h :55%:");
        }

        simple = new HandleSimpleSearch1();
        advanced = new HandleAdvancedSearch();
        search = new Search();

        searchField.addKeyListener(this);
        searchField.addActionListener(simple);
        searchField.setFocusable(true);
        searchField.requestFocus();
        searchField.addFocusListener(this);
        searchPanel.add(searchLabel);

        advancedPanel.add(advancedLabel);
        searchLabel.addMouseListener(search);
        advancedLabel.addMouseListener(search);

        exactField = new JTextField();
        anyField = new JTextField();
        noneField = new JTextField();

        exactField.addActionListener(advanced);
        anyField.addActionListener(advanced);
        noneField.addActionListener(advanced);

        add(searchPanel, "w :100%:");
        add(advancedPanel);
    }

    private void simpleSearchContent() {
        if (model.getSearchMode() == ADVANCED) {
            exactField.setFocusable(false);
            anyField.setFocusable(false);
            noneField.setFocusable(false);

            searchPanel.removeAll();
            searchPanel.setLayout(new MigLayout("", "20[][]0", "0[]0"));
            advancedLabel.setToolTipText(model.getLanguage().getString("advancedSearch"));

//            searchField = new JTextField();
            if (model.getCurrentTheme() == THEME1) {
                searchPanel.add(searchField, "w :100%:");
            } else if (model.getCurrentTheme() == THEME2) {
                searchPanel.add(searchField, "w :100%:, h :55%:");
            }
        }

//        searchField.addKeyListener(this);
//        searchField.addActionListener(simple);

        searchPanel.add(searchLabel);
        searchPanel.updateUI();

//        searchField.setRequestFocusEnabled(true);
        searchField.setFocusable(true);
//        searchField.requestFocus();

        model.setSearchMode(SIMPLE);
        model.setSongList(model.getSongDatabase());

    }

    private void advancedSearchContent() {
        searchPanel.removeAll();
        searchPanel.setLayout(new MigLayout("", "20[][][][]0", "0[]-5[]0"));
        advancedLabel.setToolTipText(model.getLanguage().getString("simpleSearch"));

        exactLabel = new JLabel(model.getLanguage().getString("exact") + " ");
        anyLabel = new JLabel(model.getLanguage().getString("any") + " ");
        noneLabel = new JLabel(model.getLanguage().getString("none") + " ");
        exactLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
        anyLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
        noneLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
        
        exactField = new JTextField();
        anyField = new JTextField();
        noneField = new JTextField();

        exactField.addActionListener(advanced);
        anyField.addActionListener(advanced);
        noneField.addActionListener(advanced);

        searchPanel.add(exactLabel);
        searchPanel.add(anyLabel);
        searchPanel.add(noneLabel);
        searchPanel.add(searchLabel, "span 1 2, wrap");
        searchPanel.add(exactField, "w :100%:");
        searchPanel.add(anyField, "w :100%:");
        searchPanel.add(noneField, "w :100%:");

        if (model.getCurrentTheme() == THEME1) {
            advancedLabel.setIcon(SIMPLE_SEARCH);
        } else {
            advancedLabel.setIcon(SIMPLE_SEARCH_2);
        }
        searchPanel.updateUI();

        exactField.setFocusable(true);
        exactField.requestFocus();
//        exactField.requestFocusInWindow();

        model.setSearchMode(ADVANCED);
    }
    boolean shouldFocus = false;
    String whoLost = "";

    @Override
    public void focusGained(FocusEvent e) {
        System.out.print("FC Gained \n");
//        if (shouldFocus) {
//            System.out.println("I get Focus");
//            searchField.setFocusable(true);
//            searchField.requestFocusInWindow();
//        }
//
//        if (e.getOppositeComponent() != null) {
//            whoLost = e.getOppositeComponent().getClass().toString();
//            System.out.println("who Lost " + whoLost);
//        } else {
//            System.out.println("Noone lost focus");
//        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        System.out.print("FC Lost and ");

//        if (e.getOppositeComponent() != null) {
//            System.out.println(e.getOppositeComponent().getClass().getName() + " get focus");
//        } else {
//            System.out.println("Noone get focus");
//        }

        if (!searchField.getText().equalsIgnoreCase("")) {
            searchField.setFocusable(true);
            searchField.requestFocusInWindow();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();
        if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0)) {
            System.out.println("im running");
//            searchField.setFocusable(false);
//            searchField.setFocusable(true);
        } else if (keyCode != KeyEvent.VK_ENTER
                && keyCode != KeyEvent.VK_SHIFT
                && keyCode != KeyEvent.VK_ALT
                && keyCode != KeyEvent.VK_DELETE
                && keyCode != KeyEvent.VK_LEFT
                && keyCode != KeyEvent.VK_RIGHT
                && keyCode != KeyEvent.VK_UP
                && keyCode != KeyEvent.VK_DOWN
                && keyCode != KeyEvent.VK_CONTROL) {
            t.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void callFocusEvent() {
        System.out.println("call FC event");
        FocusEvent fcE = new FocusEvent(searchField, FocusEvent.FOCUS_FIRST);
        FocusListener listener = searchField.getFocusListeners()[0];
        if (listener != null) {
//            searchField.dispatchEvent(fcE);
            listener.focusGained(fcE);
        }
        System.out.println("seach field " + searchField.hasFocus());

    }

    public void click() throws AWTException {
        Point p = searchField.getLocationOnScreen();
        Robot r = new Robot();
        r.mouseMove(p.x + searchField.getWidth() / 2, p.y + searchField.getHeight() / 2);
        r.mousePress(InputEvent.BUTTON1_MASK);
        try {
            Thread.sleep(1);
        } catch (Exception e) {
        }
        r.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public boolean isNumeric(String input) {
        boolean succeed = true;

        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            succeed = false;
        }

        return succeed;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (searchLabel != null && advancedLabel != null) {
            searchLabel.setToolTipText(((DatabaseFacade) o).getLanguage().getString("search"));
            if (model.getSearchMode() != ADVANCED) {
                advancedLabel.setToolTipText(((DatabaseFacade) o).getLanguage().getString("advancedSearch"));
            } else {
                advancedLabel.setToolTipText(((DatabaseFacade) o).getLanguage().getString("simpleSearch"));
            }
        }
        if (model.getSearchMode() == ADVANCED) {
            ResourceBundle lang = ((DatabaseFacade) o).getLanguage();
            if (exactLabel != null && anyLabel != null && noneLabel != null) {
                exactLabel.setText(lang.getString("exact") + " ");
                anyLabel.setText(lang.getString("any") + " ");
                noneLabel.setText(lang.getString("none") + " ");
            }
        }
    }

    private class Search extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == searchLabel) {
                if (model.getCurrentTheme() == THEME1) {
                    searchLabel.setIcon(SEARCH_HOVER);
                } else {
                    searchLabel.setIcon(SEARCH_HOVER_2);
                }
            } else if (model.getSearchMode() != ADVANCED) {
                if (model.getCurrentTheme() == THEME1) {
                    advancedLabel.setIcon(ADVANCED_SEARCH_HOVER);
                } else {
                    advancedLabel.setIcon(ADVANCED_SEARCH_HOVER_2);
                }
            } else {

                if (model.getCurrentTheme() == THEME1) {
                    advancedLabel.setIcon(SIMPLE_SEARCH_HOVER);
                } else {
                    advancedLabel.setIcon(SIMPLE_SEARCH_HOVER_2);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == searchLabel) {
                if (model.getCurrentTheme() == THEME1) {
                    searchLabel.setIcon(SEARCH);
                } else {
                    searchLabel.setIcon(SEARCH_2);
                }
            } else if (model.getSearchMode() != ADVANCED) {
                if (model.getCurrentTheme() == THEME1) {
                    advancedLabel.setIcon(ADVANCED_SEARCH);
                } else {
                    advancedLabel.setIcon(ADVANCED_SEARCH_2);
                }
            } else {
                if (model.getCurrentTheme() == THEME1) {
                    advancedLabel.setIcon(SIMPLE_SEARCH);
                } else {
                    advancedLabel.setIcon(SIMPLE_SEARCH_2);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == searchLabel) {
                if (model.getSearchMode() == ADVANCED) {
                    advanced.advancedSearch();
                }
            } else {
                if (model.getSearchMode() != ADVANCED) {
                    advancedSearchContent();
                } else {
                    simpleSearchContent();
                }
            }
        }
    }

    private class HandleSimpleSearch implements ActionListener {

        private ExecutorService executor;

        @Override
        public void actionPerformed(ActionEvent e) {
            String input = searchField.getText().trim();
            startTime = System.currentTimeMillis();

            if (!(input.equals("") || model.getSongDatabase().isEmpty())) {
                //split input to quotes and other(number + input)
                Set<String> quotesTemp = new LinkedHashSet<String>();
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(input);
                while (m.find()) {
                    if (!m.group(1).equals("")) {
                        quotesTemp.add(m.group(1).trim());
                    }
                    input = input.replaceAll(m.group(0), "");
                }
                ArrayList<String> quotes = new ArrayList<String>(quotesTemp);

                //remove redundant spaces
                input = input.replaceAll("\\s+", " ").trim();

                // Create a thread pool with 4 threads
                executor = Executors.newFixedThreadPool(THREADS);
                executor.execute(new searchThread1(input, quotes, "", "", "", true));
                executor.execute(new searchThread2(input, quotes, "", "", "", true));
                executor.execute(new searchThread3(input, quotes, "", "", "", true));
                executor.execute(new searchThread4(input, quotes, "", "", "", true));
                executor.shutdown();

            } else {
                model.setSongList(model.getSongDatabase());
                model.resetPage();
                model.setTotalPages(model.getSongDatabase());
            }
            if (t.isRunning()) {
                t.stop();
            }
            model.setSortType(DEFAULT);
        }
    }

    private class HandleSimpleSearch1 implements ActionListener {

        private ExecutorService executor;

        @Override
        public void actionPerformed(ActionEvent e) {

            if (timeEnter == 1) {
                startTimeForSearch = System.currentTimeMillis();
                timeEnter = 2;
            } else if (timeEnter == 2) {
                endTimeForSearch = System.currentTimeMillis();
                timeEnter = 1;
            }

            double interval = Math.abs((endTimeForSearch - startTimeForSearch) / 1000.0);

            if (interval > 0.5) {
                String input = searchField.getText().trim();
                startTime = System.currentTimeMillis();

                if (!(input.equals("") || model.getSongDatabase().isEmpty())) {
                    //split input to quotes and other(number + input)
                    Set<String> quotesTemp = new LinkedHashSet<String>();
                    Pattern p = Pattern.compile("\"([^\"]*)\"");
                    Matcher m = p.matcher(input);
                    while (m.find()) {
                        if (!m.group(1).equals("")) {
                            quotesTemp.add(m.group(1).trim());
                        }
                        input = input.replaceAll(m.group(0), "");
                    }
                    ArrayList<String> quotes = new ArrayList<String>(quotesTemp);

                    //remove redundant spaces
                    input = input.replaceAll("\\s+", " ").trim();

                    // Create a thread pool with 4 threads
                    executor = Executors.newFixedThreadPool(THREADS);
                    executor.execute(new searchThread1(input, quotes, "", "", "", true));
                    executor.execute(new searchThread2(input, quotes, "", "", "", true));
                    executor.execute(new searchThread3(input, quotes, "", "", "", true));
                    executor.execute(new searchThread4(input, quotes, "", "", "", true));
                    executor.shutdown();

                } else {
                    model.setSongList(model.getSongDatabase());
                    model.resetPage();
                    model.setTotalPages(model.getSongDatabase());
                }

                searchField.setFocusable(true);
                searchField.requestFocus();

                if (t.isRunning()) {
                    t.stop();
                }
                model.setSortType(DEFAULT);
            }
        }
    }

    private class HandleAdvancedSearch implements ActionListener {

        private ExecutorService executor;

        public void advancedSearch() {
            ArrayList<String> empty = new ArrayList<String>();
            String inputMatch = exactField.getText().trim();
            String inputAny = anyField.getText().replaceAll("\\s+", " ").trim();
            String inputNone = noneField.getText().replaceAll("\\s+", " ").trim();

            startTime = System.currentTimeMillis();

            if (!(inputMatch.equals("") && inputAny.equals("") && inputNone.equals("")) && !model.getSongDatabase().isEmpty()) {
                // Create a thread pool with 4 threads
                executor = Executors.newFixedThreadPool(THREADS);
                executor.execute(new searchThread1("", empty, inputMatch, inputAny, inputNone, false));
                executor.execute(new searchThread2("", empty, inputMatch, inputAny, inputNone, false));
                executor.execute(new searchThread3("", empty, inputMatch, inputAny, inputNone, false));
                executor.execute(new searchThread4("", empty, inputMatch, inputAny, inputNone, false));
                executor.shutdown();
            } else {
                model.setSongList(model.getSongDatabase());
                model.resetPage();
                model.setTotalPages(model.getSongDatabase());
            }
//            searchField.setFocusable(true);
//            searchField.requestFocus();
            if (t.isRunning()) {
                t.stop();
            }
            model.setSortType(DEFAULT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exactField || e.getSource() == noneField || e.getSource() == anyField) {
                advancedSearch();
            }
        }
    }

    private void resetFlags() {
        simpleFlag = false;
        advancedFlag = false;
    }

    private ArrayList<Song> combineResults(ArrayList<Song> input, ArrayList<Song> quotes,
            String _input, ArrayList<String> _quotes, int start, int end) {
        ArrayList<Song> inputCombine = new ArrayList<Song>();
        ArrayList<Song> quotesCombine = new ArrayList<Song>();
        ArrayList<Song> mixedResult = new ArrayList<Song>();

        if (_input.equals("")) {
            if (_quotes.isEmpty()) {
                //empty all
                return inputCombine;
            } else {
                //only quotes not empty
                quotesCombine.addAll(quotes);
                return model.bold(quotesCombine, searchField.getText().trim(), false);
            }
        } else {
            if (_quotes.isEmpty()) {
                //only input not empty
                inputCombine.addAll(input);
                Set<Song> set = new LinkedHashSet<Song>(inputCombine);
                ArrayList<Song> finalResult = new ArrayList<Song>(set);

                return model.bold(finalResult, searchField.getText().trim(), false);
            } else {
                //nothing is empty
                inputCombine.addAll(input);
                quotesCombine.addAll(quotes);
                for (int i = 0; i < inputCombine.size(); i++) {
                    for (int j = 0; j < quotesCombine.size(); j++) {
                        if (inputCombine.get(i).getCode().equals(quotesCombine.get(j).getCode())) {
                            mixedResult.add(inputCombine.get(i));
                        }
                    }
                }
                Set<Song> set = new LinkedHashSet<Song>(mixedResult);
                ArrayList<Song> finalResult = new ArrayList<Song>(set);

                return model.bold(finalResult, searchField.getText().trim(), false);
            }
        }
    }

    private ArrayList<Song> combineAdvancedResults(ArrayList<Song> match, ArrayList<Song> any, ArrayList<Song> none,
            String _match, String _any, String _none) {
        ArrayList<Song> mixedResults = new ArrayList<Song>();

        if (_match.equals("")) {
            if (_any.equals("")) {
                if (_none.equals("")) {
                    //empty all
                    return mixedResults;
                } else {
                    // only none case
                    return none;
                }
            } else {
                if (_none.equals("")) {
                    //only any case
                    return model.bold(any, _any, false);
                } else {
                    //any & none case
                    mixedResults = any;
                    mixedResults.retainAll(none);
                    return model.bold(mixedResults, _any, false);
                }
            }
        } else {
            if (_any.equals("")) {
                if (_none.equals("")) {
                    //only match case
                    return model.bold(match, _match, true);
                } else {
                    //match & none case
                    mixedResults = match;
                    mixedResults.retainAll(none);
                    return model.bold(mixedResults, _match, true);
                }
            } else {
                if (_none.equals("")) {
                    //match & any case
                    mixedResults = match;
                    mixedResults.retainAll(any);
                    return model.bold(mixedResults, _match + " " + _any, false);
                } else {
                    //match & any & none case
                    mixedResults = match;
                    mixedResults.retainAll(any);
                    mixedResults.retainAll(none);
                    return model.bold(mixedResults, _match + " " + _any, false);
                }
            }
        }
    }

    private class searchThread1 implements Runnable {

        String input, inputMatch, inputAny, inputNone;
        ArrayList<String> quotes;
        int start, end;
        ArrayList<Song> resultCombine;
        ArrayList<Song> result = new ArrayList<Song>();
        ArrayList<Song> resultQuote = new ArrayList<Song>();
        ArrayList<Song> resultMatch = new ArrayList<Song>();
        ArrayList<Song> resultAny = new ArrayList<Song>();
        ArrayList<Song> resultNone = new ArrayList<Song>();
        boolean isSimple;

        public searchThread1(String input, ArrayList<String> quotes, String inputMatch, String inputAny, String inputNone, boolean isSimple) {
            this.input = input;
            this.quotes = quotes;
            this.isSimple = isSimple;
            this.inputMatch = inputMatch;
            this.inputAny = inputAny;
            this.inputNone = inputNone;
            start = 0;
            end = (model.getSongDatabase().size() - 1) / THREADS;
        }

        @Override
        public void run() {
            if (isSimple) {
                if (!input.trim().equals("")) {
                    result = model.searchSongByPlainText(input.trim(), start, end, false);
                }

                if (!quotes.isEmpty()) {
                    resultQuote = model.searchSongByQuote(quotes, start, end);
                }

                resultCombine = combineResults(result, resultQuote, input, quotes, start, end);

            } else {
                if (!inputMatch.trim().equals("")) {
                    ArrayList<String> temp_match = new ArrayList<String>();
                    temp_match.add(inputMatch);
                    resultMatch = model.searchSongByQuote(temp_match, start, end);
                }

                if (!inputAny.trim().equals("")) {
                    resultAny = model.searchSongByPlainText(inputAny, start, end, true);
                }

                if (!inputNone.trim().equals("")) {
                    resultNone = model.searchSongByPlainText(inputNone, start, end, true);

                    ArrayList<Song> excludedResult = new ArrayList<Song>();
                    for (int i = start; i <= end; i++) {
                        boolean good = true;
                        for (int j = 0; j < resultNone.size(); j++) {
                            if (model.getSongDatabase().get(i).getCode().equals(resultNone.get(j).getCode())) {
                                good = false;
                                break;
                            }
                        }
                        if (good) {
                            excludedResult.add(model.getSongDatabase().get(i));
                        }
                    }
                    resultNone = excludedResult;
                }
                resultCombine = combineAdvancedResults(resultMatch, resultAny, resultNone, inputMatch, inputAny, inputNone);
            }

            model.setSongListWithThread1(resultCombine);
        }
    }

    private class searchThread2 implements Runnable {

        String input, inputMatch, inputAny, inputNone;
        ArrayList<String> quotes;
        int start, end;
        ArrayList<Song> resultCombine;
        ArrayList<Song> result = new ArrayList<Song>();
        ArrayList<Song> resultQuote = new ArrayList<Song>();
        ArrayList<Song> resultMatch = new ArrayList<Song>();
        ArrayList<Song> resultAny = new ArrayList<Song>();
        ArrayList<Song> resultNone = new ArrayList<Song>();
        boolean isSimple;

        public searchThread2(String input, ArrayList<String> quotes, String inputMatch, String inputAny, String inputNone, boolean isSimple) {
            this.input = input;
            this.quotes = quotes;
            this.isSimple = isSimple;
            this.inputMatch = inputMatch;
            this.inputAny = inputAny;
            this.inputNone = inputNone;
            start = ((model.getSongDatabase().size() - 1) / THREADS) + 1;
            end = (model.getSongDatabase().size() - 1) * 2 / THREADS;
        }

        @Override
        public void run() {
            if (isSimple) {
                if (!input.trim().equals("")) {
                    result = model.searchSongByPlainText(input.trim(), start, end, false);
                }

                if (!quotes.isEmpty()) {
                    resultQuote = model.searchSongByQuote(quotes, start, end);
                }

                resultCombine = combineResults(result, resultQuote, input, quotes, start, end);

            } else {
                if (!inputMatch.trim().equals("")) {
                    ArrayList<String> temp_match = new ArrayList<String>();
                    temp_match.add(inputMatch);
                    resultMatch = model.searchSongByQuote(temp_match, start, end);
                }

                if (!inputAny.trim().equals("")) {
                    resultAny = model.searchSongByPlainText(inputAny, start, end, true);
                }

                if (!inputNone.trim().equals("")) {
                    resultNone = model.searchSongByPlainText(inputNone, start, end, true);

                    ArrayList<Song> excludedResult = new ArrayList<Song>();
                    for (int i = start; i <= end; i++) {
                        boolean good = true;
                        for (int j = 0; j < resultNone.size(); j++) {
                            if (model.getSongDatabase().get(i).getCode().equals(resultNone.get(j).getCode())) {
                                good = false;
                                break;
                            }
                        }
                        if (good) {
                            excludedResult.add(model.getSongDatabase().get(i));
                        }
                    }
                    resultNone = excludedResult;
                }
                resultCombine = combineAdvancedResults(resultMatch, resultAny, resultNone, inputMatch, inputAny, inputNone);
            }

            model.setSongListWithThread2(resultCombine);
        }
    }

    private class searchThread3 implements Runnable {

        String input, inputMatch, inputAny, inputNone;
        ArrayList<String> quotes;
        int start, end;
        ArrayList<Song> resultCombine;
        ArrayList<Song> result = new ArrayList<Song>();
        ArrayList<Song> resultQuote = new ArrayList<Song>();
        ArrayList<Song> resultMatch = new ArrayList<Song>();
        ArrayList<Song> resultAny = new ArrayList<Song>();
        ArrayList<Song> resultNone = new ArrayList<Song>();
        boolean isSimple;

        public searchThread3(String input, ArrayList<String> quotes, String inputMatch, String inputAny, String inputNone, boolean isSimple) {
            this.input = input;
            this.quotes = quotes;
            this.isSimple = isSimple;
            this.inputMatch = inputMatch;
            this.inputAny = inputAny;
            this.inputNone = inputNone;
            start = ((model.getSongDatabase().size() - 1) * 2 / THREADS) + 1;
            end = (model.getSongDatabase().size() - 1) * 3 / THREADS;
        }

        @Override
        public void run() {
            if (isSimple) {
                if (!input.trim().equals("")) {
                    result = model.searchSongByPlainText(input.trim(), start, end, false);
                }

                if (!quotes.isEmpty()) {
                    resultQuote = model.searchSongByQuote(quotes, start, end);
                }

                resultCombine = combineResults(result, resultQuote, input, quotes, start, end);

            } else {
                if (!inputMatch.trim().equals("")) {
                    ArrayList<String> temp_match = new ArrayList<String>();
                    temp_match.add(inputMatch);
                    resultMatch = model.searchSongByQuote(temp_match, start, end);
                }

                if (!inputAny.trim().equals("")) {
                    resultAny = model.searchSongByPlainText(inputAny, start, end, true);
                }

                if (!inputNone.trim().equals("")) {
                    resultNone = model.searchSongByPlainText(inputNone, start, end, true);

                    ArrayList<Song> excludedResult = new ArrayList<Song>();
                    for (int i = start; i <= end; i++) {
                        boolean good = true;
                        for (int j = 0; j < resultNone.size(); j++) {
                            if (model.getSongDatabase().get(i).getCode().equals(resultNone.get(j).getCode())) {
                                good = false;
                                break;
                            }
                        }
                        if (good) {
                            excludedResult.add(model.getSongDatabase().get(i));
                        }
                    }
                    resultNone = excludedResult;
                }
                resultCombine = combineAdvancedResults(resultMatch, resultAny, resultNone, inputMatch, inputAny, inputNone);
            }

            model.setSongListWithThread3(resultCombine);
        }
    }

    private class searchThread4 implements Runnable {

        String input, inputMatch, inputAny, inputNone;
        ArrayList<String> quotes;
        int start, end;
        ArrayList<Song> resultCombine;
        ArrayList<Song> result = new ArrayList<Song>();
        ArrayList<Song> resultQuote = new ArrayList<Song>();
        ArrayList<Song> resultMatch = new ArrayList<Song>();
        ArrayList<Song> resultAny = new ArrayList<Song>();
        ArrayList<Song> resultNone = new ArrayList<Song>();
        boolean isSimple;

        public searchThread4(String input, ArrayList<String> quotes, String inputMatch, String inputAny, String inputNone, boolean isSimple) {
            this.input = input;
            this.quotes = quotes;
            this.isSimple = isSimple;
            this.inputMatch = inputMatch;
            this.inputAny = inputAny;
            this.inputNone = inputNone;
            start = ((model.getSongDatabase().size() - 1) * 3 / THREADS) + 1;
            end = (model.getSongDatabase().size() - 1);
        }

        @Override
        public void run() {
            if (isSimple) {
                if (!input.trim().equals("")) {
                    result = model.searchSongByPlainText(input.trim(), start, end, false);
                }

                if (!quotes.isEmpty()) {
                    resultQuote = model.searchSongByQuote(quotes, start, end);
                }

                resultCombine = combineResults(result, resultQuote, input, quotes, start, end);

            } else {
                if (!inputMatch.trim().equals("")) {
                    ArrayList<String> temp_match = new ArrayList<String>();
                    temp_match.add(inputMatch);
                    resultMatch = model.searchSongByQuote(temp_match, start, end);
                }

                if (!inputAny.trim().equals("")) {
                    resultAny = model.searchSongByPlainText(inputAny, start, end, true);
                }

                if (!inputNone.trim().equals("")) {
                    resultNone = model.searchSongByPlainText(inputNone, start, end, true);

                    ArrayList<Song> excludedResult = new ArrayList<Song>();
                    for (int i = start; i <= end; i++) {
                        boolean good = true;
                        for (int j = 0; j < resultNone.size(); j++) {
                            if (model.getSongDatabase().get(i).getCode().equals(resultNone.get(j).getCode())) {
                                good = false;
                                break;
                            }
                        }
                        if (good) {
                            excludedResult.add(model.getSongDatabase().get(i));
                        }
                    }
                    resultNone = excludedResult;
                }
                resultCombine = combineAdvancedResults(resultMatch, resultAny, resultNone, inputMatch, inputAny, inputNone);
            }

            model.setSongListWithThread4(resultCombine);
            model.setSearchTime((float) (System.currentTimeMillis() - startTime) / 1000);
            model.setSearchResults(model.getSongList().size());
            model.resetPage();
            model.setTotalPages(model.getSongList());
        }
    }
}

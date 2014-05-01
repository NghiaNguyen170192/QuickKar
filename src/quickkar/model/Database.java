package quickkar.model;

import au.com.bytecode.opencsv.CSVReader;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Collator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author Le Chinh Nhan and Nguyen Ba Dao
 */
public class Database extends Observable implements DatabaseFacade {

    //Common Properties
    public static int latestID;
    private int currentPage;
    private int totalPages;
    private final int CUSTOMER = 1;
    private final int STAFF = 2;
    private final int ADMIN = 3;
    private int currentUser = 0;
    private int currentTheme = 1;
    private boolean focus = false;
    private String session = null;
    private JFrame topContainer;
    private JDialog currentParent = null;
    private float searchTime;
    private int searchResults;
    private ResourceBundle language;
    private final String VI = "Language_vi";
    private final String EN = "Language_en";
    private final int DEFAULT = 0;
    private final int CODE = 1;
    private final int TITLE = 2;
    private final int LYRIC = 3;
    private final int COMPOSER = 4;
    private final int RATING = 5;
    private int sortType = DEFAULT;
    private int searchMode = DEFAULT;
    private String access_token = "";
    //Network
    private String proxyHost, username, password;
    private int proxyPort;
    private boolean setProxy = false;
    //Color
    private Color WRAPPER_POST_COLOR = new Color(153, 204, 255);
    private Color POST_COLOR = new Color(153, 255, 255);
    private Color WRAPPER_COLOR = new Color(153, 153, 255);
    private Color SWITCHER_COLOR = WRAPPER_POST_COLOR;
    private Color FEED_COLOR = WRAPPER_POST_COLOR;
    private Color TOP_COLOR = WRAPPER_POST_COLOR;
    private Color LOGIN_COLOR = WRAPPER_POST_COLOR;
    private Color SEARCH_COLOR = WRAPPER_POST_COLOR;
    private Color FEED_POST_COLOR = POST_COLOR;
    private Color PAGE_COLOR_NORMAL = POST_COLOR;
    private Color POST_COLOR_BRIGHT = Color.white;
    private Color PAGE_COLOR_BRIGHT = POST_COLOR_BRIGHT;
    private Color BACKGROUND = WRAPPER_COLOR;
    private Color BOLD_COLOR = POST_COLOR_BRIGHT;
    private Color BRIGHT_COLOR = POST_COLOR;
    private Color NORMAL_COLOR = WRAPPER_POST_COLOR;
    //Song Properties
    protected static final String SONGDATA = "song.dat";
    private ArrayList<Song> songList;
    private ArrayList<Song> songDatabase;
    private ArrayList<Song> duplicatedSong;
    //Account Properties
    private ArrayList<Account> accountList;
    protected static final String ACCOUNTDATA = "account.dat";
    private Account currentAccount;
    //lock for threads
    private static Lock lock = new ReentrantLock();
    private int finish = 0;
    private static Condition previousDone;
    public static ProxySelector DEFAULT_PROXYSELECTOR;
    //constructor

    public Database() {
        accountList = new ArrayList<Account>();
        songList = new ArrayList<Song>();
        songDatabase = new ArrayList<Song>();
        previousDone = lock.newCondition();
        language = ResourceBundle.getBundle(EN);
        try {
            readAccount();
        } catch (Exception ex) {
            System.out.println("\"account.dat\" not found!");
        }
        if (accountList.isEmpty()) {
            accountList.add(new Admin("", "admin", "admin", "", "", "", ""));
            writeAccount(accountList);
        }

        try {
            readSong();
        } catch (Exception ex) {
            System.out.println("\"song.dat\" not found!");
        }
        totalPages = (int) Math.ceil(songDatabase.size() / 11.0);
        currentPage = 1;
        setDEFAULT_PROXYSELECTOR();
    }

    /*****************************************************************************
     * Song Functions                                                            *
     *****************************************************************************/
    //accessors and mutators
    @Override
    public int getCurrentTheme() {
        return currentTheme;
    }

    @Override
    public void setCurrentTheme(int currentTheme) {
        this.currentTheme = currentTheme;
        setChanged();
        notifyObservers();
    }

    @Override
    public int getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
        if (currentUser != 0) {
            if (currentUser == ADMIN) {
                resetPage();
                setTotalPages(accountList);
            } else {
                resetPage();
                setTotalPages(songDatabase);
            }
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public JFrame getTopContainer() {
        return topContainer;
    }

    @Override
    public void setTopContainer(JFrame topContainer) {
        this.topContainer = topContainer;
    }

    @Override
    public JDialog getCurrentParent() {
        return currentParent;
    }

    @Override
    public void setCurrentParent(JDialog currentParent) {
        this.currentParent = currentParent;
    }

    @Override
    public ResourceBundle getLanguage() {
        return language;
    }

    @Override
    public void setLanguageVI() {
        language = ResourceBundle.getBundle(VI);
        setChanged();
        notifyObservers();
    }

    @Override
    public void setLanguageEN() {
        language = ResourceBundle.getBundle(EN);
        setChanged();
        notifyObservers();
    }

    @Override
    public int getSearchMode() {
        return searchMode;
    }

    @Override
    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
        if (searchMode != DEFAULT) {
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public String getAccessToken() {
        return access_token;
    }

    @Override
    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public float getSearchTime() {
        return searchTime;
    }

    @Override
    public void setSearchTime(float searchTime) {
        this.searchTime = searchTime;
    }

    @Override
    public int getSearchResults() {
        return searchResults;
    }

    @Override
    public void setSearchResults(int searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public ArrayList<Song> getSongList() {
        return songList;
    }

    @Override
    public void setSongList(ArrayList<Song> songList) {
        this.songList = (ArrayList<Song>) songList.clone();
        setChanged();
    }

    @Override
    public ArrayList<Song> getSongDatabase() {
        return songDatabase;
    }

    @Override
    public void setSongDatabase(ArrayList<Song> songDatabase) {
        this.songDatabase = songDatabase;
        setChanged();
    }

    @Override
    public ArrayList<Song> getDuplicatedSong() {
        return duplicatedSong;
    }

    @Override
    public void setDuplicatedSong(ArrayList<Song> duplicatedSong) {
        this.duplicatedSong = duplicatedSong;
    }

    @Override
    public void resetPage() {
        currentPage = 1;
    }

    @Override
    public int getFinish() {
        return finish;
    }

    @Override
    public void setFinish(int finish) {
        this.finish = finish;
    }

    @Override
    public String getSession() {
        return session;
    }

    @Override
    public void setSession(String session) {
        this.session = session;
        setChanged();
        notifyObservers();
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        setChanged();
        notifyObservers(currentPage);
    }

    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public void setTotalPages(ArrayList list) {
        if (!list.isEmpty()) {
            totalPages = (int) Math.ceil(list.size() / 11.0);
        } else {
            totalPages = 0;
            currentPage = 1;
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void deselectAllSong() {
        for (Song song : songList) {
            song.setSelected(false);
        }
    }

    @Override
    public int getTotalSelectedSong() {
        int count = 0;
        for (Song song : songList) {
            if (song.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Song getSelectedSong() {
        int count = 0;
        Song currentSong = null;
        for (Song song : songList) {
            if (song.isSelected()) {
                currentSong = song;
                count++;
            }
        }
        if (count != 1) {
            return null;
        } else {
            return currentSong;
        }
    }

    @Override
    public ArrayList<Song> getTopRated() {
        if (!songDatabase.isEmpty()) {
            ArrayList<Song> temp = (ArrayList<Song>) songDatabase.clone();
            Collections.sort(temp, new CompareVote());
            Collections.reverse(temp);
            if (temp.size() < 10) {
                int count = 0;
                for (Song song : temp) {
                    if (song.getAverage() == 0) {
                        break;
                    }
                    count++;
                }
                return new ArrayList<Song>(temp.subList(0, count));
            } else {
                int count = 0;
                for (int i = 0; i < 10; i++) {
                    if (temp.get(i).getAverage() == 0) {
                        break;
                    }
                    count++;
                }
                return new ArrayList<Song>(temp.subList(0, count));
            }
        } else {
            return new ArrayList<Song>();
        }
    }

    @Override
    public ArrayList<Song> getNewAdded() {
        if (!songDatabase.isEmpty()) {
            ArrayList<Song> temp = (ArrayList<Song>) songDatabase.clone();
            Collections.sort(temp, new CompareID());
            Collections.reverse(temp);
            if (temp.size() < 10) {
                return temp;
            } else {
                return new ArrayList<Song>(temp.subList(0, 10));
            }
        } else {
            return new ArrayList<Song>();
        }
    }

    @Override
    public void sort(int type, boolean reverse) {
        if (!reverse) {
            if (type == CODE || type == DEFAULT) {
                Collections.sort(songList, new CompareCode());
            } else if (type == RATING) {
                Collections.sort(songList, Collections.reverseOrder(new CompareVote()));
            } else {
                Collections.sort(songList, new CompareString(type));
            }
        } else {
            if (type == CODE) {
                Collections.sort(songList, Collections.reverseOrder(new CompareCode()));
            } else if (type == RATING) {
                Collections.sort(songList, new CompareVote());
            } else {
                Collections.sort(songList, Collections.reverseOrder(new CompareString(type)));
            }
        }
    }

    @Override
    public int getSortType() {
        return sortType;
    }

    @Override
    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    @Override
    public boolean hasSongSelected() {
        for (Song song : songList) {
            if (song.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAllSelectedSong() {
        ArrayList<Song> deleteSongs = new ArrayList<Song>();
        for (Song song : songList) {
            if (song.isSelected()) {
                removeTags(song);
                deleteSongs.add(song);
            }
        }

        for (int i = 0; i < deleteSongs.size(); i++) {
            for (int j = 0; j < songDatabase.size(); j++) {
                if (songDatabase.get(j).getCode().equals(deleteSongs.get(i).getCode())) {
                    songDatabase.remove(songDatabase.get(j));
                }
            }
        }

//        setSongList(songDatabase);
//        resetPage();
        songList.removeAll(deleteSongs);
        writeSong(songDatabase);
//        setTotalPages(songDatabase);
        setTotalPages(songList);
        if (currentPage > totalPages) {
            setCurrentPage(totalPages);
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public void setSongListWithThread1(ArrayList<Song> songList) {
        lock.lock();
        try {
            this.songList = songList;
            finish = 1;
            previousDone.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setSongListWithThread2(ArrayList<Song> songList) {
        lock.lock();
        try {
            while (finish != 1) {
                previousDone.await();
            }
            this.songList.addAll(songList);
            finish = 2;
            previousDone.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setSongListWithThread3(ArrayList<Song> songList) {
        lock.lock();
        try {
            while (finish != 2) {
                previousDone.await();
            }
            this.songList.addAll(songList);
            finish = 3;
            previousDone.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setSongListWithThread4(ArrayList<Song> songList) {
        lock.lock();
        try {
            while (finish != 3) {
                previousDone.await();
            }
            this.songList.addAll(songList);
            finish = 4;
            setChanged();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //add a new song to the system
    @Override
    public boolean addSong(Song newSong) {
        boolean succeed = true;

        String regex = "[0-9]{5}";
        if (newSong.getCode().matches(regex)) {
            for (Song existedSong : songDatabase) {
                if (newSong.getCode().equals(existedSong.getCode())) {
                    succeed = false;
                    break;
                }
            }
        } else {
            succeed = false;
        }

        if (succeed) {
            songDatabase.add(newSong);
            Collections.sort((List) songDatabase, new CompareCode());
            setSongList(songDatabase);
            //write to binary file
            writeSong(songDatabase);
        }

        setTotalPages(songDatabase);
        setChanged();
        notifyObservers();

        return succeed;
    }

    //edit a song from the system
    @Override
    public boolean editSong(Song song, String code, String title, String lyric, String composer) {
        String oldCode = song.getCode();
        boolean succeed = true;

        String regex = "[0-9]{5}";
        if (code.matches(regex)) {
            for (Song existedSong : songDatabase) {
                if (code.equals(existedSong.getCode()) && !code.equals(oldCode)) {
                    succeed = false;
                    break;
                }
            }
        } else {
            succeed = false;
        }

        if (succeed) {
            for (Song existedSong : songDatabase) {
                if (song.getCode().equals(existedSong.getCode())) {
                    existedSong.setCode(code);
                    existedSong.setTitle(title);
                    existedSong.setComposer(composer);
                    existedSong.setLyric(lyric);
                    break;
                }
            }

            if (!code.equals(oldCode)) {
                Collections.sort((List) songDatabase, new CompareCode());
            }
            setSongList(songDatabase);
            //write to binary file
            writeSong(songDatabase);

            setChanged();
            notifyObservers();
        }
        return succeed;
    }

    //delete a song from the system
    @Override
    public boolean deleteSong(Song song) {
        boolean succeed = false;
        removeTags(song);

        for (Song existedSong : songDatabase) {
            if (song.getCode().equals(existedSong.getCode())) {
                songDatabase.remove(existedSong);
                songList.remove(song);
                succeed = true;
                //write to binary file
                writeSong(songDatabase);
                break;
            }
        }

        setTotalPages(songList);
        if (currentPage > totalPages) {
            setCurrentPage(totalPages);
        }
        setChanged();
        notifyObservers();

        return succeed;
    }

    @Override
    public boolean rate(Song song, String phone, String email, int star) {
        String search = "<html>";
        if (song.getCode().contains(search)
                || song.getTitle().contains(search)
                || song.getComposer().contains(search)
                || song.getLyric().contains(search)) {
            Song realSong = getSong(song);
            realSong.rate(phone, email, star);
        }
        if (song.rate(phone, email, star)) {
            setChanged();
            notifyObservers();
            writeSong(songDatabase);
            return true;
        }
        return false;
    }

    @Override
    public Song getSong(Song song) {
        try {
            Song cloneSong = (Song) song.clone();
            removeTags(cloneSong);
            for (Song existedSong : songDatabase) {
                if (cloneSong.getCode().equals(existedSong.getCode())) {
                    song.setVoters((TreeMap<String, Integer>) existedSong.getVoters().clone());
                    return existedSong;
                }
            }
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int occurenceCount(String[] source, String pattern) {
        int occ = 0;

        for (int i = 0; i < source.length; i++) {
            if (source[i].contains(pattern)) {
                occ++;
            }
        }

        return occ;
    }

    public boolean compareContent(String content, String input) {
        boolean succeed = true;
        ArrayList<Integer> occurence_content = new ArrayList<Integer>();
        ArrayList<Integer> occerence_input = new ArrayList<Integer>();

        String title_split[] = content.split(" ");
        String input_split[] = input.split(" ");

        for (int i = 0; i < input_split.length; i++) {
            int occInput = occurenceCount(input_split, input_split[i]);
            int occTitle = occurenceCount(title_split, input_split[i]);
            occerence_input.add(occInput);
            occurence_content.add(occTitle);
        }

        for (int i = 0; i < occerence_input.size(); i++) {
            if (occerence_input.get(i) > occurence_content.get(i)) {
                succeed = false;
                break;
            }
        }

        if (succeed) {
            for (int i = 0; i < input_split.length; i++) {

                if (!content.contains(input_split[i])) {
                    succeed = false;
                    break;
                }
            }
        }

        return succeed;
    }

    public boolean compareConsecutiveContent(String content, String input) {
        boolean succeed = true;
        int length = 0, remain_length = 0, match_index = 0;
        content = content.toUpperCase();
        input = input.toUpperCase();
        String content_split[] = content.split(" ");
        String input_split[] = input.split(" ");

        //take the shorter string length to use in loop
        for (int i = 0; i < content_split.length; i++) {
            if (content_split[i].equalsIgnoreCase(input_split[0])) {
                match_index = i;
                remain_length = content_split.length - i;
                if (remain_length < input_split.length) {
                    succeed = false;
                } else {
                    length = input_split.length;
                }
                break;
            }
        }

        if (length == 0) {
            succeed = false;
        } else {
            for (int i = 0, j = match_index; i < length; i++, j++) {
                if (!(content_split[j].equalsIgnoreCase(input_split[i]))) {
                    succeed = false;
                    break;
                }
            }
        }

        return succeed;
    }

    public boolean compareAnyContent(String content, String input) {
        boolean succeed = false;
        String content_split[] = content.split(" ");
        String input_split[] = input.split(" ");

        for (int i = 0; i < content_split.length; i++) {
            for (int j = 0; j < input_split.length; j++) {
                if (content_split[i].contains(input_split[j])) {
                    succeed = true;
                    break;
                }
            }
        }
        return succeed;
    }

    //search songs by song title
    @Override
    public ArrayList<Song> searchSongByPlainText(String input, int start, int end, boolean any) {
        ArrayList<Song> searchResult = new ArrayList<Song>();
        input = input.toUpperCase();
        String words_without_accent = Normalizer.normalize(input, Normalizer.Form.NFD);
        words_without_accent = words_without_accent.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        int check_accent = input.compareTo(words_without_accent);

        // Implement search

        // Case there is only one character
        // --> search songs which start with this character
        if (input.length() == 1) {
            if (check_accent > 0) {
                for (int i = start; i <= end; i++) {
                    String temp_title = songDatabase.get(i).getTitle().replaceAll("Đ", "D");
                    String temp_lyric = songDatabase.get(i).getLyric().toUpperCase().replaceAll("Đ", "D");
                    String temp_composer = songDatabase.get(i).getComposer().toUpperCase().replaceAll("Đ", "D");

                    String temp_input = input.intern().replaceAll("Đ", "D");

                    if (temp_title.startsWith(temp_input)
                            || temp_lyric.startsWith(temp_input)
                            || temp_composer.startsWith(temp_input)) {
                        searchResult.add(songDatabase.get(i));
                    }
                }
            } else {
                for (int i = start; i <= end; i++) {
                    String temp_title = Normalizer.normalize(songDatabase.get(i).getTitle(), Form.NFD);
                    temp_title = temp_title.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    temp_title = temp_title.replaceAll("Đ", "D");

                    String temp_lyric = Normalizer.normalize(songDatabase.get(i).getLyric().toUpperCase(), Form.NFD);
                    temp_lyric = temp_lyric.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    temp_lyric = temp_lyric.replaceAll("Đ", "D");

                    String temp_composer = Normalizer.normalize(songDatabase.get(i).getComposer().toUpperCase(), Form.NFD);
                    temp_composer = temp_composer.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    temp_composer = temp_composer.replaceAll("Đ", "D");

                    String temp_input = words_without_accent.intern().replaceAll("Đ", "D");

                    if (temp_title.startsWith(temp_input)
                            || temp_lyric.startsWith(temp_input)
                            || temp_composer.startsWith(temp_input)) {
                        searchResult.add(songDatabase.get(i));
                    }
                }
            }
        } else {
            // Case there are many characters (String)
            // --> search songs with the whole string
            if (check_accent > 0) {
                for (int i = start; i <= end; i++) {
                    String temp_data = songDatabase.get(i).toString().replaceAll("Đ", "D");
                    String temp_input = input.intern().replaceAll("Đ", "D");

                    if (!any) {
                        if (compareContent(temp_data, temp_input)) {
                            searchResult.add(songDatabase.get(i));
                        }
                    } else {
                        if (compareAnyContent(temp_data, temp_input)) {
                            searchResult.add(songDatabase.get(i));
                        }
                    }
                }
            } else {
                for (int i = start; i <= end; i++) {
                    String temp_data = Normalizer.normalize(songDatabase.get(i).toString(), Form.NFD);
                    temp_data = temp_data.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    temp_data = temp_data.replaceAll("Đ", "D");
                    String temp_input = words_without_accent.intern().replaceAll("Đ", "D");
                    if (!any) {
                        if (compareContent(temp_data, temp_input)) {
                            searchResult.add(songDatabase.get(i));
                        }
                    } else {
                        if (compareAnyContent(temp_data, temp_input)) {
                            searchResult.add(songDatabase.get(i));
                        }
                    }
                }
            }
        }
        return searchResult;
    }

    @Override
    public ArrayList<Song> searchSongByQuote(ArrayList<String> quotes, int start, int end) {
        ArrayList<Song> searchResult = new ArrayList<Song>();
        boolean fine;

        if (quotes.size() == 1) {
            for (int i = start; i <= end; i++) {
                String code = songDatabase.get(i).getCode();
                String title = songDatabase.get(i).getTitle();
                String lyric = songDatabase.get(i).getLyric().replaceAll("\\.\\.\\.", "");
                String composer = songDatabase.get(i).getComposer();

                if (compareConsecutiveContent(title, quotes.get(0))) {
                    searchResult.add(songDatabase.get(i));
                } else if (compareConsecutiveContent(lyric, quotes.get(0))) {
                    searchResult.add(songDatabase.get(i));
                } else if (compareConsecutiveContent(composer, quotes.get(0))) {
                    searchResult.add(songDatabase.get(i));
                } else if (compareConsecutiveContent(code, quotes.get(0))) {
                    searchResult.add(songDatabase.get(i));
                }
            }
        } else {
            for (int i = start; i <= end; i++) {
                fine = true;
                String code = songDatabase.get(i).getCode();
                String title = songDatabase.get(i).getTitle();
                String lyric = songDatabase.get(i).getLyric().replaceAll("\\.\\.\\.", "");
                String composer = songDatabase.get(i).getComposer();

                for (int j = 0; j < quotes.size(); j++) {
                    if (!(compareConsecutiveContent(code, quotes.get(j))
                            || compareConsecutiveContent(title, quotes.get(j))
                            || compareConsecutiveContent(lyric, quotes.get(j))
                            || compareConsecutiveContent(composer, quotes.get(j)))) {
                        fine = false;
                        break;
                    }
                }
                if (fine) {
                    searchResult.add(songDatabase.get(i));
                }
            }
        }

        return searchResult;
    }

    @Override
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
    public ArrayList<Song> bold(ArrayList<Song> songList, String input, boolean match) {
        ArrayList<Song> result = new ArrayList<Song>();
        String temp_input = input.toUpperCase();
        temp_input = temp_input.replaceAll("\"", "");
        temp_input = temp_input.replaceAll("Đ", "D");
        temp_input = temp_input.replaceAll("\\s+", " ");
        temp_input = Normalizer.normalize(temp_input, Normalizer.Form.NFD);
        temp_input = temp_input.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String[] input_split = temp_input.split(" ");

        Set<String> codesTemp = new LinkedHashSet<String>();
        for (int i = 0; i < input_split.length; i++) {
            if (isNumeric(input_split[i])) {
                codesTemp.add(input_split[i]);
            }
        }
        ArrayList<String> codes = new ArrayList<String>(codesTemp);

        for (int i = 0; i < songList.size(); i++) {
            Song clonedSong = null;
            try {
                clonedSong = (Song) songList.get(i).clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
            String songCode = clonedSong.getCode();
            String songTitle = clonedSong.getTitle();
            String songLyric = clonedSong.getLyric();
            String songComposer = clonedSong.getComposer();

            String temp_title = Normalizer.normalize(songTitle, Form.NFD);
            temp_title = temp_title.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            temp_title = temp_title.replaceAll("Đ", "D");

            String temp_lyric = Normalizer.normalize(songLyric.toUpperCase(), Form.NFD);
            temp_lyric = temp_lyric.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            temp_lyric = temp_lyric.replaceAll("Đ", "D");

            String temp_composer = Normalizer.normalize(songComposer.toUpperCase(), Form.NFD);
            temp_composer = temp_composer.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            temp_composer = temp_composer.replaceAll("Đ", "D");

            String[] song_title_split = songTitle.split(" ");
            String[] temp_song_title_split = temp_title.split(" ");

            String[] song_lyric_split = songLyric.split(" ");
            String[] temp_song_lyric_split = temp_lyric.split(" ");

            String[] song_composer_split = songComposer.split(" ");
            String[] temp_song_composer_split = temp_composer.split(" ");

            for (int j = 0; j < input_split.length; j++) {
                if (!match) {
                    for (int k = 0; k < codes.size(); k++) {
                        if (songCode.contains(codes.get(k))) {
                            songCode = songCode.replaceAll(codes.get(k), "<b>" + codes.get(k) + "</b>");
                        }
                    }

                    for (int k = 0; k < song_title_split.length; k++) {
                        if (temp_song_title_split[k].contains(input_split[j])) {
                            song_title_split[k] = "<b>" + song_title_split[k] + "</b>";
                        }
                    }

                    for (int k = 0; k < song_lyric_split.length; k++) {
                        if (temp_song_lyric_split[k].contains(input_split[j])) {
                            song_lyric_split[k] = "<b>" + song_lyric_split[k] + "</b>";
                        }
                    }

                    for (int k = 0; k < song_composer_split.length; k++) {
                        if (temp_song_composer_split[k].contains(input_split[j])) {
                            song_composer_split[k] = "<b>" + song_composer_split[k] + "</b>";
                        }
                    }
                } else {
                    for (int k = 0; k < codes.size(); k++) {
                        if (songCode.contains(codes.get(k))) {
                            songCode = songCode.replaceAll(codes.get(k), "<b>" + codes.get(k) + "</b>");
                        }
                    }

                    for (int k = 0; k < song_title_split.length; k++) {
                        if (temp_song_title_split[k].equalsIgnoreCase(input_split[j])) {
                            song_title_split[k] = "<b>" + song_title_split[k] + "</b>";
                        }
                    }

                    for (int k = 0; k < song_lyric_split.length; k++) {

                        if (temp_song_lyric_split[k].replaceAll("\\.\\.\\.", "").equalsIgnoreCase(input_split[j])) {
                            song_lyric_split[k] = "<b>" + song_lyric_split[k] + "</b>";
                        }
                    }

                    for (int k = 0; k < song_composer_split.length; k++) {
                        if (temp_song_composer_split[k].equalsIgnoreCase(input_split[j])) {
                            song_composer_split[k] = "<b>" + song_composer_split[k] + "</b>";
                        }
                    }
                }
            }

            songCode = "<html>" + songCode + "</html>";

            songTitle = "";
            for (int j = 0; j < song_title_split.length; j++) {
                songTitle = songTitle + song_title_split[j] + " ";
            }
            songTitle = "<html>" + songTitle + "</html>";

            songLyric = "";
            for (int j = 0; j < song_lyric_split.length; j++) {
                songLyric = songLyric + song_lyric_split[j] + " ";
            }
            songLyric = "<html>" + songLyric + "</html>";

            songComposer = "";
            for (int j = 0; j < song_composer_split.length; j++) {
                songComposer = songComposer + song_composer_split[j] + " ";
            }
            songComposer = "<html>" + songComposer + "</html>";

            clonedSong.setCode(songCode);
            clonedSong.setTitle(songTitle);
            clonedSong.setLyric(songLyric);
            clonedSong.setComposer(songComposer);
            result.add(clonedSong);
        }
        return result;
    }

    @Override
    public ArrayList<ArrayList<String>> getLinksOnline(String keyword) {
        List<WebElement> allSuggestions;
        ArrayList<String> links = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> singers = new ArrayList<String>();
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        WebDriver driver;

        if (setProxy) {
            driver = createProxyHtmlUnitWebDriver(proxyHost, proxyPort, username, password);
        } else {
            driver = createHtmlUnitWebDriver();
        }

        keyword = Normalizer.normalize(keyword.toLowerCase().replaceAll("đ", "d"), Form.NFD);
        keyword = keyword.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String[] keyword_split = keyword.split(" ");
        String pages = "";
        String keys = "";
        int num = 1;

        for (int i = 0; i < keyword_split.length; i++) {
            if (i < keyword_split.length - 1) {
                pages += keyword_split[i] + "-";
                keys += keyword_split[i] + "+";
            } else {
                pages += keyword_split[i];
                keys += keyword_split[i];
            }
        }

        for (int i = 1; i < 4; i++) {
            num = i;
            String songURL = "http://star.zing.vn/star/search/" + pages + "." + num + ".html?q=" + keys;
            driver.get(songURL);
            allSuggestions = driver.findElements(By.xpath("//a[@class='font_12 bold']"));

            for (WebElement suggestion : allSuggestions) {
                links.add(suggestion.getAttribute("href"));
                String content = suggestion.getAttribute("onMouseOver").replaceAll("(ddrivetip\\(\')|(/)|(<br />Nhạc sĩ: )|( class=\\\\'user\\\\')|(\'\\))", "");
                content = content.replaceAll("<b>", "\"");
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(content);
                for (int j = 0; m.find(); j++) {
                    if (j == 0) {
                        titles.add(m.group(1).trim());
                    } else if (j == 1) {
                        singers.add(m.group(1).trim());
                    }
                }
            }
        }

        for (int i = 0; i < titles.size(); i++) {
            String temp = Normalizer.normalize(titles.get(i).toLowerCase().replaceAll("đ", "d"), Form.NFD);
            temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            titles.set(i, temp);
        }

        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).equalsIgnoreCase(keyword)) {
                ArrayList<String> tempResult = new ArrayList<String>();
                tempResult.add(titles.get(i));
                tempResult.add(singers.get(i));
                tempResult.add(links.get(i));
                result.add(tempResult);
            }
        }

        if (result.isEmpty()) {
            for (int i = 0; i < titles.size(); i++) {
                if (keyword_split.length == 1) {
                    if (titles.get(i).contains(keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(titles.get(i));
                        tempResult.add(singers.get(i));
                        tempResult.add(links.get(i));
                        result.add(tempResult);
                    }
                } else {
                    if (compareConsecutiveContent(titles.get(i), keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(titles.get(i));
                        tempResult.add(singers.get(i));
                        tempResult.add(links.get(i));
                        result.add(tempResult);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<ArrayList<String>>> getLinksNCT(String keyword) {
        keyword = Normalizer.normalize(keyword.toLowerCase().replaceAll("đ", "d"), Form.NFD);
        keyword = keyword.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String[] keyword_split = keyword.split(" ");

        List<WebElement> songs, videos;
        List<WebElement> videoLines;
        ArrayList<String> songLink = new ArrayList<String>();
        ArrayList<String> songTitle = new ArrayList<String>();
        ArrayList<String> songInfo = new ArrayList<String>();
        ArrayList<String> videoLink = new ArrayList<String>();
        ArrayList<String> videoInfo = new ArrayList<String>();
        ArrayList<String> videoImage = new ArrayList<String>();
        ArrayList<ArrayList<String>> songResult = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> videoResult = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> finalResult = new ArrayList<ArrayList<ArrayList<String>>>();

        String tempKeyword = keyword.replaceAll(" ", "+");
        String searchSongLink = "http://www.nhaccuatui.com/tim-kiem/bai-hat?q=" + tempKeyword;
        String searchVideoLink = "http://www.nhaccuatui.com/tim-kiem/mv?q=" + tempKeyword;
        WebDriver driver;

        if (setProxy) {
            driver = createProxyHtmlUnitWebDriver(proxyHost, proxyPort, username, password);
        } else {
            driver = createHtmlUnitWebDriver();
        }

        driver.get(searchSongLink);
        songs = driver.findElement(By.className("list_song")).findElements(By.tagName("li"));

        for (WebElement song : songs) {
            try {
                songLink.add(song.findElement(By.className("imusic")).getAttribute("href"));
                songTitle.add(song.findElement(By.tagName("h3")).getText());
                songInfo.add(song.findElement(By.className("info-song")).getText());
            } catch (Exception ex) {
                System.out.println("invalid elenment");
            }
        }

        driver.get(searchVideoLink);
        videoLines = driver.findElements(By.className("list_clip"));

        for (WebElement videoLine : videoLines) {
            videos = videoLine.findElements(By.tagName("li"));
            for (WebElement video : videos) {
                try {
                    videoLink.add(video.findElement(By.tagName("a")).getAttribute("href"));
                    videoInfo.add(video.findElement(By.tagName("a")).getAttribute("title")
                            + " | " + video.getText().substring(video.getText().lastIndexOf("\n")).trim());
                    videoImage.add(video.findElement(By.tagName("img")).getAttribute("src"));
                } catch (Exception ex) {
                    System.out.println("invalid elenment");
                }
            }
        }

        //filter song results
        ArrayList<String> tempSongInfo = new ArrayList<String>();
        for (int i = 0; i < songTitle.size(); i++) {
            String temp = Normalizer.normalize(songTitle.get(i).toLowerCase().replaceAll("đ", "d"), Form.NFD);
            temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            tempSongInfo.add(temp);
        }

        for (int i = 0; i < tempSongInfo.size(); i++) {
            if (tempSongInfo.get(i).equalsIgnoreCase(keyword)) {
                ArrayList<String> tempResult = new ArrayList<String>();
                tempResult.add(songLink.get(i));
                tempResult.add(songTitle.get(i));
                tempResult.add(songInfo.get(i));
                songResult.add(tempResult);
            }
        }

        if (songResult.isEmpty()) {
            for (int i = 0; i < tempSongInfo.size(); i++) {
                if (keyword_split.length == 1) {
                    if (tempSongInfo.get(i).contains(keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(songLink.get(i));
                        tempResult.add(songTitle.get(i));
                        tempResult.add(songInfo.get(i));
                        songResult.add(tempResult);
                    }
                } else {
                    if (compareConsecutiveContent(tempSongInfo.get(i), keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(songLink.get(i));
                        tempResult.add(songTitle.get(i));
                        tempResult.add(songInfo.get(i));
                        songResult.add(tempResult);
                    }
                }
            }
        }

        //filter video resutls
        ArrayList<String> tempVideoInfo = new ArrayList<String>();
        for (int i = 0; i < videoInfo.size(); i++) {
            String temp = Normalizer.normalize(videoInfo.get(i).toLowerCase().replaceAll("đ", "d"), Form.NFD);
            temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            tempVideoInfo.add(temp);
        }

        for (int i = 0; i < tempVideoInfo.size(); i++) {
            String[] temp_split = videoInfo.get(i).split("-");
            for (int j = 0; j < temp_split.length; j++) {
                if (temp_split[j].trim().equalsIgnoreCase(keyword)) {
                    ArrayList<String> tempResult = new ArrayList<String>();
                    tempResult.add(videoLink.get(i));
                    tempResult.add(videoInfo.get(i));
                    tempResult.add(videoImage.get(i));
                    videoResult.add(tempResult);
                    break;
                }
            }
        }

        if (videoResult.isEmpty()) {
            for (int i = 0; i < tempVideoInfo.size(); i++) {
                if (keyword_split.length == 1) {
                    if (tempVideoInfo.get(i).contains(keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(videoLink.get(i));
                        tempResult.add(videoInfo.get(i));
                        tempResult.add(videoImage.get(i));
                        videoResult.add(tempResult);
                    }
                } else {
                    if (compareConsecutiveContent(tempVideoInfo.get(i), keyword)) {
                        ArrayList<String> tempResult = new ArrayList<String>();
                        tempResult.add(videoLink.get(i));
                        tempResult.add(videoInfo.get(i));
                        tempResult.add(videoImage.get(i));
                        videoResult.add(tempResult);
                    }
                }
            }
        }

        finalResult.add(songResult);
        finalResult.add(videoResult);
        return finalResult;
    }

    @Override
    public void letSing(String flashURL) throws IOException {
        WebDriver driver;

        if (setProxy) {
            driver = createProxyHtmlUnitWebDriver(proxyHost, proxyPort, username, password);
        } else {
            driver = createHtmlUnitWebDriver();
        }

        driver.get(flashURL);
        String htmlSource = driver.getPageSource();
        InputStream is = new ByteArrayInputStream(htmlSource.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line, flashvar = "";
        String flashLink = "";
        String[] info = new String[8];
        while ((line = reader.readLine().trim()) != null) {
            if (line.contains("var flashvars")) {
                for (int i = 0; i < info.length; i++) {
                    flashvar = flashvar.concat(line + "\n");
                    info[i] = line;
                    line = reader.readLine().trim();
                }
                break;
            }
        }

        String song_id_line = info[3];
        String song_id = "";
        String urlDemo = "http://image.star.zing.vn/flash/";
        String domain = "http://star.zing.vn";

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(song_id_line);
        while (m.find()) {
            song_id = m.group(1).trim();
        }

        flashLink = "http://star.zing.vn/flash/zingStarPlayer.swf?username=&status=karaoke&song_id="
                + song_id + "&recorder_id=&urlDemo=" + urlDemo + "&domain=" + domain + "&filetype=.swf";
        try {
            Runtime.getRuntime().exec("cmd /c start /MAX wmplayer /open \"" + flashLink + "\"");
        } catch (IOException ex) {
            System.out.println("There is no Window Media!");
            try {
                Desktop.getDesktop().browse(new URI(flashLink));
            } catch (URISyntaxException ex1) {
                System.out.println("open web function got problems!");
            }
        }
        reader.close();
    }

    @Override
    public void playSongOrVideo(String flashURL) throws IOException {
        WebDriver driver;

        if (setProxy) {
            driver = createProxyHtmlUnitWebDriver(proxyHost, proxyPort, username, password);
        } else {
            driver = createHtmlUnitWebDriver();
        }
        driver.get(flashURL);
        String flashLink = driver.findElement(By.xpath("//link[@rel='video_src']")).getAttribute("href");

        try {
            Runtime.getRuntime().exec("cmd /c start /MAX wmplayer /open \"" + flashLink + "\"");
        } catch (IOException ex) {
            System.out.println("There is no Window Media!");
            try {
                Desktop.getDesktop().browse(new URI(flashLink));
            } catch (URISyntaxException ex1) {
                System.out.println("open web function got problems!");
            }
        }
    }

    @Override
    public void publishToFacebook(String message, String link, String accessToken) {
        FacebookClient fbClient = new DefaultFacebookClient(accessToken);
        if (message.equals("")) {
            FacebookType publish = fbClient.publish("me/feed", FacebookType.class,
                    Parameter.with("link", link));
        } else {
            FacebookType publish = fbClient.publish("me/feed", FacebookType.class,
                    Parameter.with("message", message), Parameter.with("link", link));
        }
    }

    private WebDriver createHtmlUnitWebDriver() {

        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver() {

            @Override
            protected WebClient modifyWebClient(WebClient client) {
                client.setCssEnabled(false);
                client.setJavaScriptEnabled(false);
                client.setThrowExceptionOnScriptError(false);
                client.setThrowExceptionOnFailingStatusCode(false);
                client.setTimeout(10000);
                return client;
            }
        };

        return htmlUnitDriver;
    }

    private WebDriver createProxyHtmlUnitWebDriver(final String proxyHost, final int proxyPort, final String username, final String password) {

        HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver() {

            @Override
            protected WebClient modifyWebClient(WebClient client) {
                client.setCssEnabled(false);
                client.setJavaScriptEnabled(false);
                client.setThrowExceptionOnScriptError(false);
                client.setThrowExceptionOnFailingStatusCode(false);
                client.setTimeout(10000);
                client.setProxyConfig(new ProxyConfig(proxyHost, proxyPort));

                DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
                credentialsProvider.addCredentials(username, password);
                credentialsProvider.addProxyCredentials(username, password, proxyHost, proxyPort);
                client.setCredentialsProvider(credentialsProvider);

                return client;
            }
        };

        htmlUnitDriver.setProxy(proxyHost, proxyPort);

        return htmlUnitDriver;
    }

    public static ProxySelector getDEFAULT_PROXYSELECTOR() {
        return DEFAULT_PROXYSELECTOR;
    }

    public static void setDEFAULT_PROXYSELECTOR() {
        System.setProperty("java.net.useSystemProxies", "true");
        Database.DEFAULT_PROXYSELECTOR = ProxySelector.getDefault();
    }

    @Override
    public String getProxyHostAndPort() {
        String proxyHostandPort = "";
        try {
            Proxy proxy = (Proxy) getDEFAULT_PROXYSELECTOR().select(new URI("http://www.google.com.vn")).iterator().next();
            InetSocketAddress add = (InetSocketAddress) proxy.address();

            if (add == null) {
                proxyHostandPort = "";
            } else {
                proxyHostandPort = add.getHostName() + ":" + add.getPort();
            }
        } catch (URISyntaxException ex) {
            System.out.println("get Proxy host and Port method has problems");
        }
        ProxySelector.setDefault(null);
        return proxyHostandPort;
    }

    @Override
    public boolean checkProxy() throws MalformedURLException, IOException {
        URL url = new URL("http://www.google.com.vn");
        HttpURLConnection con;

        try {
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.getResponseCode();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public void setProxy(String proxyHost, int proxyPort, String username, String password) {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", proxyHost);
        System.getProperties().put("proxyPort", proxyPort);
        System.getProperties().put("proxyUser", username);
        System.getProperties().put("proxyPassword", password);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.username = username;
        this.password = password;
        setProxy = true;
    }

    @Override
    public boolean testProxyAuthentication() throws IOException {
        int statusCode = 0;
        String url = "http://www.google.com.vn";
        WebClient webClient = new WebClient();
        webClient.setJavaScriptEnabled(false);
        webClient.setPrintContentOnFailingStatusCode(false);
        webClient.setThrowExceptionOnFailingStatusCode(false);
        webClient.setProxyConfig(new ProxyConfig(proxyHost, proxyPort));

        DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
        credentialsProvider.addCredentials(username, password);
        credentialsProvider.addProxyCredentials(username, password, proxyHost, proxyPort);
        webClient.setCredentialsProvider(credentialsProvider);
        try {
            statusCode = webClient.getPage(url).getWebResponse().getStatusCode();
        } catch (Exception ex) {
            System.out.println("Wrong Proxy Information!");
        }
        if (statusCode == 200) {
            setProxy = true;
            return true;
        } else {
            System.getProperties().put("proxySet", "false");
            System.getProperties().put("proxyHost", "");
            System.getProperties().put("proxyPort", 0);
            setProxy = false;
            return false;
        }
    }

    @Override
    public String getProxyHost() {
        return proxyHost;
    }

    @Override
    public int getProxyPort() {
        return proxyPort;
    }

    @Override
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    @Override
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isSetProxy() {
        return setProxy;
    }

    @Override
    public void removeTags(Song song) {
        song.setCode(song.getCode().replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim());

        song.setTitle(song.getTitle().replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim());

        song.setLyric(song.getLyric().replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim());

        song.setComposer(song.getComposer().replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim());
    }

    private class CompareCode implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            return o1.getCode().compareTo(o2.getCode());
        }
    }

    private class CompareString implements Comparator<Song> {

        private int flag = 0;
        Collator coll = null;

        public CompareString(int flag) {
            this.flag = flag;
            coll = Collator.getInstance(new Locale("vi"));
            coll.setStrength(Collator.CANONICAL_DECOMPOSITION);
        }

        @Override
        public int compare(Song o1, Song o2) {
            if (flag == TITLE) {
//                return o1.getStripContent(o1.getTitle()).compareTo(o2.getStripContent(o2.getTitle()));
                return coll.compare(o1.getTitle(), o2.getTitle());
            } else if (flag == LYRIC) {
//                return o1.getStripContent(o1.getLyric()).compareTo(o2.getStripContent(o2.getLyric()));
                return coll.compare(o1.getLyric(), o2.getLyric());
            } else if (flag == COMPOSER) {
//                return o1.getStripContent(o1.getComposer()).compareTo(o2.getStripContent(o2.getComposer()));
                return coll.compare(o1.getComposer(), o2.getComposer());
            }
            return 0;
        }
    }

    private class CompareVote implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            return Double.compare(o1.getAverage(), o2.getAverage());
        }
    }

    private class CompareID implements Comparator<Song> {

        @Override
        public int compare(Song o1, Song o2) {
            return Integer.compare(o1.getSongID(), o2.getSongID());
        }
    }

    /*****************************************************************************
     * Account Functions                                                         *
     *****************************************************************************/
    @Override
    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    @Override
    public void setAccountList(ArrayList<Account> accountList) {
        this.accountList = accountList;
        setChanged();
        notifyObservers(accountList);
    }

    @Override
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
//        setChanged();
//        notifyObservers();
    }

    @Override
    public Account getCurrentAccount() {
        return currentAccount;
    }

    @Override
    public void deselectAllAccount() {
        for (Account account : accountList) {
            account.setSelected(false);
        }
    }

    @Override
    public int getTotalSelectedAccount() {
        int count = 0;
        for (Account account : accountList) {
            if (account.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Account getSelectedAccount() {
        int count = 0;
        Account currentAccount = null;
        for (Account account : accountList) {
            if (account.isSelected()) {
                currentAccount = account;
                count++;
            }
        }
        if (count != 1) {
            return null;
        } else {
            return currentAccount;
        }
    }

    @Override
    public boolean hasAccountSelected() {
        for (Account account : accountList) {
            if (account.isSelected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAllSelectedAccount() {
        ArrayList<Account> deleteAccounts = new ArrayList<Account>();
        for (Account account : accountList) {
            if (account.isSelected()) {
                if (account.getUsername().equals("admin")) {
                    return false;
                }
            }
        }
        for (Account account : accountList) {
            if (account.isSelected()) {
                deleteAccounts.add(account);
            }
        }
        accountList.removeAll(deleteAccounts);
        writeAccount(accountList);
        setTotalPages(accountList);
        if (currentPage > totalPages) {
            setCurrentPage(totalPages);
        }
        setChanged();
        notifyObservers();
        return true;
    }

    //add a new account to the system
    @Override
    public boolean addAccount(Account newAccount) {
        boolean succeed = true;

        String regexPassLength = "^.{5,}$";
        String regexPhone1 = "^((012[0-9])|(016[2-9])|(09[0-9])|(0188)|(0199))[0-9]{7}$";
        String regexPhone2 = "^08[0-9]{8}$";
        String regexEmail = "^([\\w-\\.])+@([\\w])+\\.(\\w){2,6}(\\.([\\w]){2,4})*$";

        if (newAccount.getPassword().matches(regexPassLength) && newAccount.getEmail().matches(regexEmail)
                && (newAccount.getPhone().matches(regexPhone1) || newAccount.getPhone().matches(regexPhone2))) {
            for (Account existedAccount : accountList) {
                if (newAccount.getUsername().equals(existedAccount.getUsername())) {
                    succeed = false;
                    break;
                }
            }
        } else {
            succeed = false;
        }

        if (succeed) {
            accountList.add(newAccount);

            setTotalPages(accountList);
            setChanged();
            notifyObservers();
            //write to binary file
            writeAccount(accountList);
        }

        return succeed;
    }

    //edit a account from the system
    @Override
    public boolean editAccount(Account account, String name, String username, String password, String email, String phone, String address, String description) {
        boolean succeed = true;

        String regexPassLength = "^.{5,}$";
        String regexPhone1 = "^((012[0-9])|(016[2-9])|(09[0-9])|(0188)|(0199))[0-9]{7}$";
        String regexPhone2 = "^08[0-9]{8}$";
        String regexEmail = "^([\\w-\\.])+@([\\w])+\\.(\\w){2,6}(\\.([\\w]){2,4})*$";

        if (password.matches(regexPassLength) && email.matches(regexEmail)
                && (phone.matches(regexPhone1) || phone.matches(regexPhone2))) {
            for (Account existedAccount : accountList) {
                if (username.equals(existedAccount.getUsername()) && !account.getUsername().equals(username)) {
                    succeed = false;
                    break;
                }
            }
        } else {
            succeed = false;
        }

        if (succeed) {
            account.setName(name);
            account.setUsername(username);
            account.setPassword(password);
            account.setEmail(email);
            account.setPhone(phone);
            account.setAddress(address);
            account.setDescription(description);

            setChanged();
            notifyObservers();
            //write to binary file
            writeAccount(accountList);
        }

        return succeed;
    }

    //delete a account from the system
    @Override
    public boolean deleteAccount(Account account) {

        if (account.getUsername().equals("admin")) {
            return false;
        } else {
            accountList.remove(account);
        }

        setTotalPages(accountList);
        if (currentPage > totalPages) {
            setCurrentPage(totalPages);
        }
        setChanged();
        notifyObservers();
        //write to binary file
        writeAccount(accountList);

        return true;
    }

    /*****************************************************************************
     * File Functions                                                            *
     *****************************************************************************/
    //import CSV data to the system and return duplicated songs if any
    @Override
    public ArrayList<Object> readFromCSV(File[] file) throws FileNotFoundException, IOException {
        String[] nextLine, songInfo;
        ArrayList<Object> return_array = new ArrayList<Object>();
        ArrayList<Song> duplicated_songs = new ArrayList<Song>();
        ArrayList<String[]> error_lines = new ArrayList<String[]>();
        ArrayList<String> error_messages = new ArrayList<String>();
        // For storing new song added
        ArrayList<Song> newSong = new ArrayList<Song>();

        return_array.add(error_messages);
        return_array.add(error_lines);
        return_array.add(duplicated_songs);
        return_array.add(newSong);
        for (int k = 0; k < file.length; k++) {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file[k]), "UTF-8"));
            //skip the headers line
            String[] header = reader.readNext();

            if (header[0].equals("MÃ SỐ")
                    && header[1].equals("TÊN BÀI HÁT")
                    && header[2].equals("TÊN TÁC GIẢ")) {
                boolean duplication;
                while ((nextLine = reader.readNext()) != null) {
                    //nextLine[] is an array of values from the line
                    if (nextLine.length == 3) {
                        duplication = false;
                        if (!songDatabase.isEmpty()) {
                            for (int i = 0; i < songDatabase.size(); i++) {
                                if (songDatabase.get(i).getCode().equals(nextLine[0])) {
                                    duplication = true;
                                    break;
                                }
                            }
                        }

                        songInfo = nextLine[1].split("\n");
                        if (nextLine[2].contains("\n")) {
                            nextLine[2] = nextLine[2].replaceFirst("\n", "  ♪  ");
                        }
                        if (duplication) {
                            duplicated_songs.add(new Song(nextLine[0], songInfo[0], songInfo[1], nextLine[2], 0));
                        } else {
                            songDatabase.add(new Song(nextLine[0].trim(), songInfo[0].trim(), songInfo[1].trim(), nextLine[2].trim(), ++latestID));
                            newSong.add(new Song(nextLine[0].trim(), songInfo[0].trim(), songInfo[1].trim(), nextLine[2].trim(), latestID));
                        }
                    } else {
                        error_lines.add(nextLine);
                    }
                }

            } else {
                //wrong csv
                error_messages.add("INVALID CSV FORMAT! IMPORT CSV ABORTED!!!");

            }
        }
        Collections.sort((List) songDatabase, new CompareCode());
        setTotalPages(songDatabase);

        songList = (ArrayList<Song>) songDatabase.clone();
        writeSong(songDatabase);

        setChanged();
        notifyObservers();

        return return_array;
    }

    @Override
    public boolean writeToCSV(ArrayList<Song> songDatabase, String dir) {
        PrintWriter pw = null;
        try {
//            pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(dir))));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir), "UTF-8")));
        } catch (IOException ex) {
            System.out.println("Invalid dir!");
        }

        pw.println("MÃ SỐ,TÊN BÀI HÁT,TÊN TÁC GIẢ");
        for (int i = 0; i < songDatabase.size(); i++) {
            String code = songDatabase.get(i).getCode().trim();
            String title = songDatabase.get(i).getTitle().toUpperCase().trim();
            String lyric = songDatabase.get(i).getLyric().trim();
            String composer = songDatabase.get(i).getComposer().trim();
            pw.println(code + ",\"" + title + "\n" + lyric + "\"," + composer);
        }

        pw.close();
        return true;
    }

    //write song data to binary file
    @Override
    public void writeSong(ArrayList<Song> songList) {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(SONGDATA)));
            oos.writeObject(songList);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //read song data from binary file
    @Override
    public void readSong() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(SONGDATA)));
        Object obj = null;

        try {
            while ((obj = ois.readObject()) != null) {
                songDatabase = (ArrayList<Song>) obj;
                songList = (ArrayList<Song>) songDatabase.clone();
                for (Song song : songDatabase) {
                    if (song.getSongID() > latestID) {
                        latestID = song.getSongID();
                    }
                }
            }
        } catch (EOFException ex) {
            System.out.println("Songs are loaded.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectInputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //write account data to binary file
    @Override
    public void writeAccount(ArrayList<Account> accountList) {
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(ACCOUNTDATA)));
            oos.writeObject(accountList);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //read account data from binary file
    @Override
    public void readAccount() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(ACCOUNTDATA)));
        Object obj = null;

        try {
            while ((obj = ois.readObject()) != null) {
                accountList = (ArrayList<Account>) obj;
            }
        } catch (EOFException ex) {
            System.out.println("Accounts are loaded.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectInputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //accessor and mutator for colors
    @Override
    public void setPOST_COLOR(Color POST_COLOR) {
        this.POST_COLOR = POST_COLOR;
    }

    @Override
    public void setWRAPPER_COLOR(Color WRAPPER_COLOR) {
        this.WRAPPER_COLOR = WRAPPER_COLOR;
    }

    @Override
    public void setWRAPPER_POST_COLOR(Color WRAPPER_POST_COLOR) {
        this.WRAPPER_POST_COLOR = WRAPPER_POST_COLOR;
    }

    @Override
    public void setPOST_COLOR_BRIGHT(Color POST_COLOR_BRIGHT) {
        this.POST_COLOR_BRIGHT = POST_COLOR_BRIGHT;
    }

    @Override
    public void setRelatedColors() {
        SWITCHER_COLOR = WRAPPER_POST_COLOR;
        FEED_COLOR = WRAPPER_POST_COLOR;
        TOP_COLOR = WRAPPER_POST_COLOR;
        LOGIN_COLOR = WRAPPER_POST_COLOR;
        SEARCH_COLOR = WRAPPER_POST_COLOR;
        FEED_POST_COLOR = POST_COLOR;
        PAGE_COLOR_NORMAL = POST_COLOR;
        PAGE_COLOR_BRIGHT = POST_COLOR_BRIGHT;
        BACKGROUND = WRAPPER_COLOR;
        BOLD_COLOR = POST_COLOR_BRIGHT;
        BRIGHT_COLOR = POST_COLOR;
        NORMAL_COLOR = WRAPPER_POST_COLOR;
    }

    @Override
    public Color getBACKGROUND() {
        return BACKGROUND;
    }

    @Override
    public Color getBOLD_COLOR() {
        return BOLD_COLOR;
    }

    @Override
    public Color getBRIGHT_COLOR() {
        return BRIGHT_COLOR;
    }

    @Override
    public Color getFEED_COLOR() {
        return FEED_COLOR;
    }

    @Override
    public Color getFEED_POST_COLOR() {
        return FEED_POST_COLOR;
    }

    @Override
    public Color getLOGIN_COLOR() {
        return LOGIN_COLOR;
    }

    @Override
    public Color getNORMAL_COLOR() {
        return NORMAL_COLOR;
    }

    @Override
    public Color getPAGE_COLOR_BRIGHT() {
        return PAGE_COLOR_BRIGHT;
    }

    @Override
    public Color getPAGE_COLOR_NORMAL() {
        return PAGE_COLOR_NORMAL;
    }

    @Override
    public Color getPOST_COLOR() {
        return POST_COLOR;
    }

    @Override
    public Color getPOST_COLOR_BRIGHT() {
        return POST_COLOR_BRIGHT;
    }

    @Override
    public Color getSEARCH_COLOR() {
        return SEARCH_COLOR;
    }

    @Override
    public Color getSWITCHER_COLOR() {
        return SWITCHER_COLOR;
    }

    @Override
    public Color getTOP_COLOR() {
        return TOP_COLOR;
    }

    @Override
    public Color getWRAPPER_COLOR() {
        return WRAPPER_COLOR;
    }

    @Override
    public Color getWRAPPER_POST_COLOR() {
        return WRAPPER_POST_COLOR;
    }
}

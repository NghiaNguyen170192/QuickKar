/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.model;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Nguyen Ba Dao
 */
public interface DatabaseFacade {

    public int getCurrentTheme();

    public void setCurrentTheme(int currentTheme);

    public int getCurrentUser();

    public void setCurrentUser(int currentUser);

    public JFrame getTopContainer();

    public void setTopContainer(JFrame topContainer);

    public JDialog getCurrentParent();

    public void setCurrentParent(JDialog currentParent);

    public ResourceBundle getLanguage();

    public void setLanguageVI();

    public void setLanguageEN();

    public int getSearchMode();

    public void setSearchMode(int searchMode);

    public String getAccessToken();

    public void setAccessToken(String access_token);

    public float getSearchTime();

    public void setSearchTime(float searchTime);

    public int getSearchResults();

    public void setSearchResults(int searchResults);

    public ArrayList<Song> getSongList();

    public void setSongList(ArrayList<Song> songList);

    public ArrayList<Song> getSongDatabase();

    public void setSongDatabase(ArrayList<Song> songDatabase);

    public ArrayList<Song> getDuplicatedSong();

    public void setDuplicatedSong(ArrayList<Song> duplicatedSong);

    public void resetPage();

    public int getFinish();

    public void setFinish(int finish);

    public String getSession();

    public void setSession(String session);

    public int getCurrentPage();

    public void setCurrentPage(int currentPage);

    public int getTotalPages();

    public void setTotalPages(ArrayList list);

    public void deselectAllSong();

    public int getTotalSelectedSong();

    public Song getSelectedSong();

    public boolean hasSongSelected();

    public void deleteAllSelectedSong();

    public ArrayList<Song> getTopRated();

    public ArrayList<Song> getNewAdded();

    public void sort(int type, boolean reverse);

    public int getSortType();

    public void setSortType(int sortType);

    public boolean rate(Song song, String phone, String email, int star);

    public Song getSong(Song song);

    /**
     * set the search result of the first 1/4 database to songList
     * @param songList temporary ArrayList for manipulating the result
     */
    public void setSongListWithThread1(ArrayList<Song> songList);

    /**
     * set the search result of the second 1/4 database to songList
     * @param songList temporary ArrayList for manipulating the result
     */
    public void setSongListWithThread2(ArrayList<Song> songList);

    /**
     * set the search result of the third 1/4 database to songList
     * @param songList temporary ArrayList for manipulating the result
     */
    public void setSongListWithThread3(ArrayList<Song> songList);

    /**
     * set the search result of the final 1/4 database to songList
     * @param songList temporary ArrayList for manipulating the result
     */
    public void setSongListWithThread4(ArrayList<Song> songList);

    /**
     * Add a new song to the database
     * @param newSong the song will be added
     * @return true if success, false otherwise
     */
    public boolean addSong(Song newSong);

    /**
     * Edit current song in the database
     * @param song the song to be edited
     * @param code new code
     * @param title new title
     * @param lyric new lyric
     * @param composer new composer
     * @return true if success, false otherwise 
     */
    public boolean editSong(Song song, String code, String title, String lyric, String composer);

    /**
     * Delete an existing song in the database
     * @param song the song to be deleted
     * @return true if success, false otherwise
     */
    public boolean deleteSong(Song song);

    /**
     * Search song without double quotes
     * @param input the input that users enter
     * @param start the starting index of song database for searching with thread
     * @param end the ending index of song database for searching with thread
     * @param any simple search = false or advanced search with any mode = true
     * @return an array list of result
     */
    public ArrayList<Song> searchSongByPlainText(String input, int start, int end, boolean any);

    /**
     * Search song with double quotes
     * @param quotes array of quotes that users enter
     * @param start start the starting index of song database for searching with thread
     * @param end end the ending index of song database for searching with thread
     * @return an array list of result
     */
    public ArrayList<Song> searchSongByQuote(ArrayList<String> quotes, int start, int end);

    public boolean isNumeric(String input);

    /**
     * Bold the array result
     * @param songList the array containing search result
     * @param input the input that users enter
     * @param match bold with quotes = true or without quotes = false
     * @return array list of bold result
     */
    public ArrayList<Song> bold(ArrayList<Song> songList, String input, boolean match);

    /**
     * Get link from star.zing.vn
     * @param keyword the keyword to get song
     * @return an array of song information with match keyword
     */
    public ArrayList<ArrayList<String>> getLinksOnline(String keyword);

    /**
     * Get link from nhaccuatui.com
     * @param keyword the keyword to get song & video
     * @return an array of song and video information with match keyword
     */
    public ArrayList<ArrayList<ArrayList<String>>> getLinksNCT(String keyword);

    /**
     * Open the link with Windwos Media Player
     * @param flashURL the flash link of song or video
     */
    public void playSongOrVideo(String flashURL) throws IOException;

    /**
     * Open the link with Windows Media Player
     * @param flashURL the flash link of song
     */
    public void letSing(String flashURL) throws IOException;

    /**
     * publish information to Facebook
     * @param message message
     * @param link the multimedia link
     * @param accessToken accessToken of user whose want to publish
     */
    public void publishToFacebook(String message, String link, String accessToken);

    /**
     * Check if the Internet connection is established
     * @return true if connection is setup, false otherwise
     */
    public boolean checkProxy() throws MalformedURLException, IOException, URISyntaxException;

    /**
     * Set the proxy for the application
     * @param proxyHost the proxy host
     * @param proxyPort the proxy port
     * @param username the username for authentication
     * @param password the password for authentication
     */
    public void setProxy(String proxyHost, int proxyPort, String username, String password);

    /**
     * Test if the user is authenticated
     * @return true if yes, false otherwise
     */
    public boolean testProxyAuthentication() throws IOException;

    public String getProxyHost();

    public int getProxyPort();

    public void setProxyHost(String proxyHost);

    public void setProxyPort(int proxyPort);

    public String getUsername();

    public String getPassword();

    public boolean isSetProxy();

    /**
     * Remove bold
     * @param song the song whose information will be removed HTML tags
     */
    public void removeTags(Song song);

    public ArrayList<Account> getAccountList();

    public void setAccountList(ArrayList<Account> accountList);

    public void setCurrentAccount(Account account);

    public Account getCurrentAccount();

    public void deselectAllAccount();

    public int getTotalSelectedAccount();

    public Account getSelectedAccount();

    public boolean hasAccountSelected();

    public boolean deleteAllSelectedAccount();

    /**
     * Add new account to the database
     * @param newAccount account to be added
     * @return true if success,false otherwise
     */
    public boolean addAccount(Account newAccount);

    /**
     * Edit an existing account in the database
     * @param account the account to be edited
     * @param name new name
     * @param username new username
     * @param password new password
     * @param email new email
     * @param phone new phone 
     * @param address new address
     * @param description new description
     * @return true if success, false otherwise
     */
    public boolean editAccount(Account account, String name, String username, String password, String email, String phone, String address, String description);

    /**
     * Delete an account in the database
     * @param account the account to be deleted
     * @return true if success, false otherwise
     */
    public boolean deleteAccount(Account account);

    /**
     * Import song data from CSV files to the system
     * @param file array of CSV files
     * @return an array of object of errors if any
     */
    public ArrayList<Object> readFromCSV(File[] file) throws FileNotFoundException, IOException;

    /**
     * Export song data from the system to CSV file
     * @param songDatabase all songs from the system
     * @param dir directory to be exported to
     * @return success or failure condition
     */
    public boolean writeToCSV(ArrayList<Song> songDatabase, String dir);

    /**
     * Write song database to binary
     * @param songList the song to be written 
     */
    public void writeSong(ArrayList<Song> songList);

    /**
     * Read song database into the running system
     */
    public void readSong() throws IOException, ClassNotFoundException;

    /**
     * Write account database to binary
     * @param accountList
     */
    public void writeAccount(ArrayList<Account> accountList);

    /**
     * Read account database into the running system
     */
    public void readAccount() throws IOException, ClassNotFoundException;

    //accessor and mutator for colors
    public void setPOST_COLOR(Color POST_COLOR);

    public void setWRAPPER_COLOR(Color WRAPPER_COLOR);

    public void setWRAPPER_POST_COLOR(Color WRAPPER_POST_COLOR);

    public void setPOST_COLOR_BRIGHT(Color POST_COLOR_BRIGHT);

    public void setRelatedColors();

    public Color getBACKGROUND();

    public Color getBOLD_COLOR();

    public Color getBRIGHT_COLOR();

    public Color getFEED_COLOR();

    public Color getFEED_POST_COLOR();

    public Color getLOGIN_COLOR();

    public Color getNORMAL_COLOR();

    public Color getPAGE_COLOR_BRIGHT();

    public Color getPAGE_COLOR_NORMAL();

    public Color getPOST_COLOR();

    public Color getPOST_COLOR_BRIGHT();

    public Color getSEARCH_COLOR();

    public Color getSWITCHER_COLOR();

    public Color getTOP_COLOR();

    public Color getWRAPPER_COLOR();

    public Color getWRAPPER_POST_COLOR();

    public String getProxyHostAndPort();
}

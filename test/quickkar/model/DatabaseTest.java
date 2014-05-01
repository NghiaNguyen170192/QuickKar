/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.model;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nguyen Ba Dao
 */
public class DatabaseTest implements Serializable {

    public DatabaseTest() {
    }

    @Before
    public void setUp() {
        Database db = new Database();
        File[] files = new File[1];
        files[0] = new File("List for Test.csv");
        try {
            db.readFromCSV(files);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DatabaseTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DatabaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        db.writeSong(db.getSongDatabase());
    }

    @After
    public void tearDown() {
        try {
            File songData = new File("song.dat");
            File accountData = new File("account.dat");
            songData.delete();
            accountData.delete();
            System.out.println("Data deleted.");
        } catch (Exception ex) {
            System.out.println("Deletion failed.");
        }
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_1() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "admin", "12345", "admin@yahoo.com",
                "0991234567", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_2() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "nguyenvana", "1234", "admin@yahoo.com",
                "0991234567", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_3() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "nguyenvana", "123456", "admin@yahoo.com",
                "123", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_4() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "nguyenvana", "123456", "admin@yahoo",
                "0991234567", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_5() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "newUser", "123456abc", "admin@yahoo.com",
                "0991234567", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount_6() {
        System.out.println("Test Add Account");
        Account newAccount = new Account("Nguyen Van A", "nguyenvana", "123456", "admin@yahoo.com",
                "0831234567", "123 Thanh Thai", "I am a staff") {
        };
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.addAccount(newAccount);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_1() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "admin";
        String password = "12345";
        String email = "admin@yahoo.com";
        String phone = "0991234567";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = false;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_2() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "nguyenvana";
        String password = "1234";
        String email = "admin@yahoo.com";
        String phone = "0991234567";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = false;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_3() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "nguyenvana";
        String password = "123456";
        String email = "admin@yahoo.com";
        String phone = "123";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = false;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_4() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "nguyenvana";
        String password = "123456";
        String email = "admin@yahoo";
        String phone = "0991234567";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = false;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_5() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "newUser";
        String password = "123456abc";
        String email = "admin@yahoo.com";
        String phone = "0991234567";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = true;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of editAccount method, of class Database.
     */
    @Test
    public void testEditAccount_6() {
        System.out.println("Test Edit Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);
        String name = "Nguyen Van A";
        String username = "nguyenvana";
        String password = "123456";
        String email = "admin@yahoo.com";
        String phone = "0831234567";
        String address = " 123 Thanh Thai";
        String description = "I am a staff";

        boolean expResult = true;
        boolean result = instance.editAccount(account, name, username, password, email, phone, address, description);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteAccount method, of class Database.
     */
    @Test
    public void testDeleteAccount() {
        System.out.println("Test Delete Account");
        Database instance = new Database();
        instance.getAccountList().add(new Account("staff", "staff", "staff", "staff@yahoo.com", "0983439700", "8 Paster", "super staff") {
        });
        Account account = instance.getAccountList().get(1);

        boolean expResult = true;
        boolean result = instance.deleteAccount(account);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_1() {
        System.out.println("Test Add Song");
        Song newSong = new Song("1234", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_2() {
        System.out.println("Test Add Song");
        Song newSong = new Song("12345", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_3() {
        System.out.println("Test Add Song");
        Song newSong = new Song("123456", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_4() {
        System.out.println("Test Add Song");
        Song newSong = new Song("abcde", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_5() {
        System.out.println("Test Add Song");
        Song newSong = new Song("55502", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of addSong method, of class Database.
     */
    @Test
    public void testAddSong_6() {
        System.out.println("Test Add Song");
        Song newSong = new Song("99999", "Any", "Any", "Any", 0);
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.addSong(newSong);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_1() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "1234";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = false;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_2() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "12345";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = true;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_3() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "123456";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = false;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_4() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "abcde";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = false;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_5() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "55502";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = false;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of editSong method, of class Database.
     */
    @Test
    public void testEditSong_6() {
        System.out.println("Test Edit Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);
        String code = "99999";
        String title = "Any";
        String lyric = "Any";
        String composer = "Any";

        boolean expResult = true;
        boolean result = instance.editSong(song, code, title, lyric, composer);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteSong method, of class Database.
     */
    @Test
    public void testDeleteSong_1() {
        System.out.println("Test Delete Song");
        Database instance = new Database();
        Song song = instance.getSongDatabase().get(0);

        boolean expResult = true;
        boolean result = instance.deleteSong(song);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteSong method, of class Database.
     */
    @Test
    public void testDeleteSong_2() {
        System.out.println("Test Delete Song");
        Database instance = new Database();
        Song song = new Song("55555", "Any", "Any", "Any", 0);

        boolean expResult = false;
        boolean result = instance.deleteSong(song);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_1() {
        System.out.println("Test Compare Content");
        String content = "anh";
        String input = "anh anh anh";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_2() {
        System.out.println("Test Compare Content");
        String content = "anh anh anh";
        String input = "anh";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_3() {
        System.out.println("Test Compare Content");
        String content = "anh";
        String input = "anh";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_4() {
        System.out.println("Test Compare Content");
        String content = "anh can em";
        String input = "chi";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_5() {
        System.out.println("Test Compare Content");
        String content = "anh can em";
        String input = "anh can";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_6() {
        System.out.println("Test Compare Content");
        String content = "anh can em";
        String input = "em can anh";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareContent method, of class Database.
     */
    @Test
    public void testCompareContent_7() {
        System.out.println("Test Compare Content");
        String content = "anh";
        String input = "anh can em";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareConsecutiveContent method, of class Database.
     */
    @Test
    public void testCompareConsecutiveContent_1() {
        System.out.println("Test Compare Consecutive Content");
        String content = "anh can em";
        String input = "can em";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareConsecutiveContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareConsecutiveContent method, of class Database.
     */
    @Test
    public void testCompareConsecutiveContent_2() {
        System.out.println("Test Compare Consecutive Content");
        String content = "anh can em";
        String input = "em can";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareConsecutiveContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareConsecutiveContent method, of class Database.
     */
    @Test
    public void testCompareConsecutiveContent_3() {
        System.out.println("Test Compare Consecutive Content");
        String content = "anh can em";
        String input = "an can em";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareConsecutiveContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareAnyContent method, of class Database.
     */
    @Test
    public void testCompareAnyContent_1() {
        System.out.println("Test Compare Any Content");
        String content = "anh can em";
        String input = "anh chao em";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.compareAnyContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareAnyContent method, of class Database.
     */
    @Test
    public void testCompareAnyContent_2() {
        System.out.println("Test Compare Any Content");
        String content = "anh can em";
        String input = "le dem";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.compareAnyContent(content, input);
        assertEquals(expResult, result);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_1() {
        System.out.println("Search Song By Plain Text");
        String input = "yeu";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55481");
        expResult.add("55506");
        expResult.add("55514");
        expResult.add("55576");
        expResult.add("55584");
        expResult.add("55621");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_2() {
        System.out.println("Search Song By Plain Text");
        String input = "yeu 81";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55481");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_3() {
        System.out.println("Search Song By Plain Text");
        String input = "999";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("50931");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_4() {
        System.out.println("Search Song By Plain Text");
        String input = "ao da";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55506");
        expResult.add("55584");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_5() {
        System.out.println("Search Song By Plain Text");
        String input = "ao da mai khoi";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55506");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_6() {
        System.out.println("Search Song By Plain Text");
        String input = "ao da yeu thuong";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55584");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_7() {
        System.out.println("Search Song By Plain Text");
        String input = "sáng";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55506");
        expResult.add("55576");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_8() {
        System.out.println("Search Song By Plain Text");
        String input = "sáng tùng";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55576");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_9() {
        System.out.println("Search Song By Plain Text");
        String input = "chợt sáng tùng";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55576");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_10() {
        System.out.println("Search Song By Plain Text");
        String input = "555";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55502");
        expResult.add("55506");
        expResult.add("55514");
        expResult.add("55567");
        expResult.add("55576");
        expResult.add("55584");
        expResult.add("55590");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByPlainText method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByPlainText_11() {
        System.out.println("Search Song By Plain Text");
        String input = "55514";
        boolean any = false;
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55514");
        ArrayList<Song> result = instance.searchSongByPlainText(input, 0, instance.getSongDatabase().size() - 1, any);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_1() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("đóa");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("50931");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_2() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("đóa");
        quotes.add("chuyện xưa");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("50931");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_3() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("tình yêu");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55506");
        expResult.add("55576");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_4() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("tình yêu");
        quotes.add("đinh tùng");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55576");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_5() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("55");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_6() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("55502");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55502");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_7() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("yêu");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55481");
        expResult.add("55506");
        expResult.add("55514");
        expResult.add("55576");
        expResult.add("55584");
        expResult.add("55621");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_8() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("yêu");
        quotes.add("mai khôi");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55506");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of searchSongByQuote method, of class Database.
     * Test with a small test CSV file
     */
    @Test
    public void testSearchSongByQuote_9() {
        System.out.println("Test Search Song By Quote");
        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("yêu");
        quotes.add("hình");
        quotes.add("quang trung");
        Database instance = new Database();
        ArrayList expResult = new ArrayList();
        expResult.add("55481");
        ArrayList<Song> result = instance.searchSongByQuote(quotes, 0, instance.getSongDatabase().size() - 1);
        ArrayList actResult = new ArrayList();
        for (int i = 0; i < result.size(); i++) {
            actResult.add(result.get(i).getCode());
        }
        assertEquals(expResult, actResult);
    }

    /**
     * Test of isNumeric method, of class Database.
     */
    @Test
    public void testIsNumeric_1() {
        System.out.println("Test Is Numeric");
        String input = "55555";
        Database instance = new Database();
        boolean expResult = true;
        boolean result = instance.isNumeric(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of isNumeric method, of class Database.
     */
    @Test
    public void testIsNumeric_2() {
        System.out.println("Test Is Numeric");
        String input = "string";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.isNumeric(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of isNumeric method, of class Database.
     */
    @Test
    public void testIsNumeric_3() {
        System.out.println("Test Is Numeric");
        String input = "string55";
        Database instance = new Database();
        boolean expResult = false;
        boolean result = instance.isNumeric(input);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLinksOnline method, of class Database.
     * Sometimes, test can be failed because of Internet connection under proxy.
     * Regression test will give the correct result.
     */
    @Test
    public void testGetLinksOnline_1() throws MalformedURLException, IOException {
        System.out.println("Test Get Links Online");
        String keyword = "because i love you";
        Database instance = new Database();
        int expResult = 3;
        if (!instance.checkProxy()) {
            instance.setProxy("proxy.rmit.edu.vn", 8080, "s3296796", "l0velyW3ndy");
        }
        ArrayList result = instance.getLinksOnline(keyword);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getLinksOnline method, of class Database.
     * Sometimes, test can be failed because of Internet connection under proxy.
     * Regression test will give the correct result.
     */
    @Test
    public void testGetLinksOnline_2() throws MalformedURLException, IOException {
        System.out.println("Test Get Links Online");
        String keyword = "le da";
        Database instance = new Database();
        int expResult = 1;
        if (!instance.checkProxy()) {
            instance.setProxy("proxy.rmit.edu.vn", 8080, "s3296796", "l0velyW3ndy");
        }
        ArrayList result = instance.getLinksOnline(keyword);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getLinksOnline method, of class Database.
     * Sometimes, test can be failed because of Internet connection under proxy.
     * Regression test will give the correct result.
     */
    @Test
    public void testGetLinksOnline_3() throws MalformedURLException, IOException {
        System.out.println("Test Get Links Online");
        String keyword = "mot con vit";
        Database instance = new Database();
        int expResult = 0;
        if (!instance.checkProxy()) {
            instance.setProxy("proxy.rmit.edu.vn", 8080, "s3296796", "l0velyW3ndy");
        }
        ArrayList result = instance.getLinksOnline(keyword);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of removeTags method, of class Database.
     */
    @Test
    public void testRemoveTags() {
        System.out.println("Test Remove Tags");
        Song song = new Song("<html><b>55</b>511</html>", "<html><b>Anh</b> bên <b>em</b></html>",
                "<html><b>Anh</b> muốn bên <b>em</b></html>", "<html><b>Anonymous</b></html>", 0);
        Database instance = new Database();
        String expResult[] = new String[4];
        expResult[0] = "55511";
        expResult[1] = "Anh bên em";
        expResult[2] = "Anh muốn bên em";
        expResult[3] = "Anonymous";

        instance.removeTags(song);

        String actResult[] = new String[4];
        actResult[0] = song.getCode();
        actResult[1] = song.getTitle();
        actResult[2] = song.getLyric();
        actResult[3] = song.getComposer();
        assertArrayEquals(expResult, actResult);
    }
}

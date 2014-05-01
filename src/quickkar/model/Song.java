/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.model;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.TreeMap;

/**
 *
 * @author Le Chinh Nhan and Nguyen Ba Dao
 */
public class Song implements Serializable, Cloneable {

    private String code, lyric, title, composer;
    private boolean selected = false;
    private double average = 0;
    private int numOfVote = 0;
    private TreeMap<String, Integer> voters;
    private int songID;

    public Song(String code, String title, String lyric, String composer, int songID) {
        this.code = code;
        this.lyric = lyric;
        this.title = title;
        this.composer = composer;
        this.songID = songID;
        voters = new TreeMap<String, Integer>();
    }

    public Song() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getTitle() {
        return title;
    }

    public String getStripContent(String input) {
        String input_without_accent = Normalizer.normalize(input.toUpperCase(), Normalizer.Form.NFD);
        input_without_accent = input_without_accent.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        input_without_accent = input_without_accent.replaceAll("Đ", "D");
        return input_without_accent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getNumOfVote(int star) {
        int count = 0;
        for (int value : voters.values()) {
            if (star == value) {
                count++;
            }
        }
        return count;
    }

    public TreeMap getVoters() {
        return voters;
    }

    public void setVoters(TreeMap voters) {
        this.voters = voters;
    }

    public boolean isCurrentlyVoted(String voter) {
        return voters.containsKey(voter);
    }

    public int getCurrentlyVoted(String voter) {
        return voters.get(voter);
    }

    /**
     * Rate a song
     * @param phone user phone number
     * @param email user email
     * @param star number of star user vote for a song
     * @return true if success, false otherwise
     */
    public boolean rate(String phone, String email, int star) {
        if (email.equals("")) { // In case the voter is staff, then the 'phone' contains the username
            if (!voters.isEmpty()) {
                for (String string : voters.keySet()) {
                    if (string.equals(phone)) {
                        average = (average * numOfVote - voters.get(phone) + star) / (numOfVote);
                        voters.put(phone, star);
                        return true;
                    }
                }
            }
            voters.put(phone, star);
            average = (average * numOfVote + star) / (++numOfVote);
            return true;
        } else { // In case the voter is a customer
            if (!voters.isEmpty()) {
                for (String string : voters.keySet()) {
                    String voter[] = new String[2];
                    voter = string.split(":");
                    if (voter[0].equals(phone) && voter[1].equals(email)) {
                        average = (average * numOfVote - voters.get(phone + ":" + email) + star) / (numOfVote);
                        voters.put(phone + ":" + email, star);
                        return true;
                    } else if (voter[0].equals(phone)) {
                        return false;
                    }
                }
            }
            voters.put(phone + ":" + email, star);
            average = (average * numOfVote + star) / (++numOfVote);
            return true;
        }
    }

    @Override
    public String toString() {
        return (code + " " + title + " " + lyric + " " + composer).toUpperCase();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Song) super.clone();
    }
}

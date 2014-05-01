/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import quickkar.gui.reusable.dialog.LoadingDialog;
import quickkar.gui.reusable.dialog.PopupInformDialog;
import quickkar.gui.reusable.dialog.ProxySettingDialog;
import quickkar.gui.reusable.dialog.KaraokeKillerDialog;
import quickkar.gui.reusable.dialog.PlayKillerDialog;
import quickkar.gui.reusable.dialog.ShareDialog;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;
import quickkar.model.Song;

/**
 *
 * @author Freedom
 */
public class HandleKiller implements CommonInterface {

    private DatabaseFacade model;
    private Song song;
    private int type;
    private String link;

    public HandleKiller(DatabaseFacade model, Song song, int type) {
        this.model = model;
        this.song = song;
        this.type = type;
        init();
    }

    public HandleKiller(DatabaseFacade model, String link, int type) {
        this.model = model;
        this.link = link;
        this.type = type;
        init();
    }

    private void init() {
        try {
            JFrame frame = model.getTopContainer();
            String temp = model.getProxyHostAndPort();
            if (!model.checkProxy()) {
                if (!model.isSetProxy()) {
                    if (!temp.equalsIgnoreCase("")) {
                        String[] temp_split = temp.split(":");
                        model.setProxyHost(temp_split[0]);
                        model.setProxyPort(Integer.parseInt(temp_split[1]));
                    }
                    JDialog proxySetting = new ProxySettingDialog(model, frame, CONNECTION_ERROR);
                }
                if (model.isSetProxy()) {
                    while (!model.testProxyAuthentication()) {
                        JDialog error = new PopupInformDialog(model, model.getLanguage().getString("authenErr").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                        JDialog proxySetting = new ProxySettingDialog(model, frame, ANTHENTICATION_ERROR);
                        if (((ProxySettingDialog) proxySetting).isDestroyed()) {
                            return;
                        }
                    }
                    Handle handleLoading = new Handle(frame, model, song);
                    Thread t = new Thread(handleLoading);
                    t.start();
                }
            } else {
                Handle handleLoading = new Handle(frame, model, song);
                Thread t = new Thread(handleLoading);
                t.start();
            }
        } catch (Exception ex) {
            System.out.println("network initialization function got problems!");
        }
    }

    private class Handle implements Runnable, CommonInterface {

        private JFrame frame;
        private LoadingDialog loading;
        private boolean done;
        private Song song;
        private DatabaseFacade model;

        public Handle(JFrame frame, DatabaseFacade model, Song song) {
            this.model = model;
            this.frame = frame;
            this.song = song;
        }

        private void karaoke() {
            try {
                loading = new LoadingDialog(model, frame, true);
                String temp = song.getTitle();
                temp = temp.replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim();
                ArrayList<ArrayList<String>> searchResult = model.getLinksOnline(temp);
                if (searchResult.isEmpty()) {
                    loading.dispose();
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("404Err").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    loading.dispose();
                    JDialog killer = new KaraokeKillerDialog(model, frame, searchResult);
                }
            } catch (InterruptedException ex) {
                System.out.println("karaoke function got problems!");
            }
        }

        private void play() {
            try {
                loading = new LoadingDialog(model, frame, true);
                String temp = song.getTitle();
                temp = temp.replaceAll("(<b>)|(</b>)|(<html>)|(</html>)", "").trim();
                ArrayList<ArrayList<ArrayList<String>>> searchResult = model.getLinksNCT(temp);
                if (searchResult.isEmpty()) {
                    loading.dispose();
                    JDialog error = new PopupInformDialog(model, model.getLanguage().getString("404Err").toUpperCase(), frame, "", ModalityType.DOCUMENT_MODAL);
                } else {
                    loading.dispose();
                    JDialog killer = new PlayKillerDialog(model, searchResult);
                }
            } catch (Exception ex) {
                System.out.println("play function got problems!");
            }
        }

        private void share(String link) {
            try {
                JDialog share = new ShareDialog(model, link);
            } catch (Exception ex) {
                System.out.println("share function got problems!");
            }
        }

        @Override
        public void run() {
            if (type == PLAY_KARAOKE) {
                karaoke();
            } else if (type == PLAY_SONG_VIDEO) {
                play();
            } else if (type == PLAY_SHARE) {
                share(link);
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import java.awt.Color;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.apache.xerces.impl.dv.util.Base64;
import quickkar.gui.reusable.HandleKiller;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.SwitcherPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Freedom
 */
public class PlayKillerDialog extends JDialog implements CommonInterface {

    private final int DEFAULT_PAGE = 0;
    private final int FIRST_PAGE = 1;
    private final int LAST_PAGE = 2;
    private final int SONG = 1;
    private final int VIDEO = 2;
    private DatabaseFacade model;
    private ArrayList<ArrayList<ArrayList<String>>> searchResult;
    private SongPanel song[];
    private VideoPanel clip[];
    private RoundedPanel panel, wrapper;
    private RoundedPanel pageLeft, pageRight, musicPanel, videoPanel;
    private JPanel tabPanel;
    private ImageIcon cross;
    private ImageIcon cross_hover;
    private JLabel close, music, video;
    private Window owner;
    private int page = DEFAULT_PAGE;
    private int state = SONG;

    public PlayKillerDialog(DatabaseFacade model, ArrayList<ArrayList<ArrayList<String>>> searchResult) throws IOException {
        super(model.getTopContainer(), ModalityType.DOCUMENT_MODAL);
        this.model = model;
        this.searchResult = searchResult;
        this.owner = model.getTopContainer();

        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, false, Color.black));
        setSize(owner.getWidth(), owner.getHeight());
        setLayout(new MigLayout());

        panel = new RoundedPanel(new MigLayout("", "2[]2[]2[]2", "2[]2[]2"));
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getWRAPPER_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        wrapper = new RoundedPanel(new MigLayout("wrap", "2[]2", "2[]2"));
        wrapper.setOpa(DEFAULT_VISIBLE);
        wrapper.setBackground(model.getWRAPPER_POST_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            wrapper.setArcs(15, 15);
        } else {
            wrapper.setArcs(0, 0);
        }

        if (model.getCurrentTheme() == THEME1) {
            cross = CROSS;
            cross_hover = CROSS_HOVER;
        } else {
            cross = CROSS_2;
            cross_hover = CROSS_HOVER_2;
        }
        close = new JLabel(cross);
        pageLeft = new SwitcherPanel(STAFF, model, LEFT, SWITCH);
        pageLeft.setOpa(DEFAULT_VISIBLE);
        pageRight = new SwitcherPanel(STAFF, model, RIGHT, SWITCH);
        pageRight.setOpa(DEFAULT_VISIBLE);

        (tabPanel = new ContentPane()).setLayout(new MigLayout("", "0[]2[]0", "0[]0"));
        (musicPanel = new RoundedPanel(new MigLayout())).setOpa(DEFAULT_VISIBLE);
        if (model.getCurrentTheme() == THEME1) {
            musicPanel.setArcs(15, 15);
        } else {
            musicPanel.setArcs(0, 0);
        }
        musicPanel.setBackground(model.getPOST_COLOR_BRIGHT());
        musicPanel.add((music = new JLabel(model.getLanguage().getString("music"))), "push, align center");
        music.setFont(TYPE);
        (videoPanel = new RoundedPanel(new MigLayout())).setOpa(DEFAULT_VISIBLE);
        if (model.getCurrentTheme() == THEME1) {
            videoPanel.setArcs(15, 15);
        } else {
            videoPanel.setArcs(0, 0);
        }
        videoPanel.setBackground(model.getPOST_COLOR());
        videoPanel.add((video = new JLabel(model.getLanguage().getString("video"))), "push, align center");
        if (searchResult.get(PLAY_VIDEO).isEmpty()) {
            tabPanel.setLayout(new MigLayout("", "0[]0", "0[]0"));
            tabPanel.add(musicPanel, "w :100%:");
        } else {
            tabPanel.add(musicPanel, "w :50%:");
            tabPanel.add(videoPanel, "w :50%:");
        }
        wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));

        song = new SongPanel[searchResult.get(PLAY_SONG).size()];
        if (song.length < 10) {
            for (int i = 0; i < song.length; i++) {
                wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i)), String.format("w :%d:", POST_WIDTH));
            }
        } else {
            this.page = FIRST_PAGE;
            for (int i = 0; i < 10; i++) {
                wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i)), String.format("w :%d:", POST_WIDTH));
            }
        }

        panel.add(close, "span, push, align right, wrap");
        panel.add(pageLeft);
        panel.add(wrapper);
        panel.add(pageRight);

        MouseListener external = new ExternalAction();
        close.addMouseListener(external);
        pageLeft.addMouseListener(external);
        pageRight.addMouseListener(external);
        musicPanel.addMouseListener(external);
        videoPanel.addMouseListener(external);

        add(panel, "push, align center");
        setLocation(owner.getLocation());
        setVisible(true);
    }

    private class SongPanel extends RoundedPanel implements CommonInterface {

        private DatabaseFacade model;
        private ArrayList<String> song;
        private JLabel title, info, share;

        public SongPanel(final DatabaseFacade model, final ArrayList<String> song) throws IOException {
            setOpa(DEFAULT_VISIBLE);
            this.model = model;
            this.song = song;

            setLayout(new MigLayout("", "", ""));
            setBackground(model.getPOST_COLOR());
            if (model.getCurrentTheme() == THEME1) {
                setArcs(15, 15);
            } else {
                setArcs(0, 0);
            }

            title = new JLabel(song.get(PLAY_SONG_TITLE).toUpperCase());
            info = new JLabel(song.get(PLAY_SONG_INFO));
            (share = new JLabel(SHARE)).setToolTipText(model.getLanguage().getString("share"));

            add(title);
            add(share, "push, align right, wrap");
            add(info, "span, push, align right");

            MouseListener killer = new PlayKiller();
            share.addMouseListener(killer);
            addMouseListener(killer);
        }

        private class PlayKiller extends MouseAdapter {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(model.getPOST_COLOR_BRIGHT());
                if (e.getSource() == share) {
                    share.setIcon(SHARE_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(model.getPOST_COLOR());
                if (e.getSource() == share) {
                    share.setIcon(SHARE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getSource() == share) {
                    HandleKiller killer = new HandleKiller(model, song.get(PLAY_SONG_URL), PLAY_SHARE);
                } else {
                    try {
                        model.playSongOrVideo(song.get(PLAY_SONG_URL));
                    } catch (IOException ex) {
                        System.out.println("play function got problems!");
                    }
                }
            }
        }
    }

    private class VideoPanel extends RoundedPanel implements CommonInterface {

        private DatabaseFacade model;
        private ArrayList<String> video;
        private JLabel image, info, share;

        public VideoPanel(final DatabaseFacade model, final ArrayList<String> video) throws IOException {
            setOpa(DEFAULT_VISIBLE);
            this.model = model;
            this.video = video;

            setLayout(new MigLayout("", "", ""));
            setBackground(model.getPOST_COLOR());
            if (model.getCurrentTheme() == THEME1) {
                setArcs(15, 15);
            } else {
                setArcs(0, 0);
            }

            Image image_url = null;
            URL url = new URL(video.get(PLAY_VIDEO_IMAGE));
            if (model.isSetProxy()) {
                try {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(model.getProxyHost(), model.getProxyPort()));
                    HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
                    String s = model.getUsername() + ":" + model.getPassword();
                    String encoded = Base64.encode(s.getBytes());
                    uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
                    uc.connect();

                    image_url = ImageIO.read(uc.getInputStream());
                } catch (IOException ex) {
                    System.out.println("Unable to retrieve image!");
                }
            } else {
                image_url = ImageIO.read(url);
            }
            image = new JLabel();
            try {
                image.setIcon(new ImageIcon(image_url.getScaledInstance(100, 56, Image.SCALE_SMOOTH)));
            } catch (Exception ex) {
                System.out.println("Image link is died!");
            }
            info = new JLabel(video.get(PLAY_VIDEO_INFO));
            (share = new JLabel(SHARE)).setToolTipText(model.getLanguage().getString("share"));

            add(image, "span 1 2");
            add(share, "push, align right, wrap");
            add(info, "push, align right");

            MouseListener killer = new PlayKiller();
            share.addMouseListener(killer);
            addMouseListener(killer);
        }

        private class PlayKiller extends MouseAdapter {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(model.getPOST_COLOR_BRIGHT());
                if (e.getSource() == share) {
                    share.setIcon(SHARE_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(model.getPOST_COLOR());
                if (e.getSource() == share) {
                    share.setIcon(SHARE);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getSource() == share) {
                    HandleKiller killer = new HandleKiller(model, video.get(PLAY_VIDEO_URL), PLAY_SHARE);
                } else {
                    try {
                        model.playSongOrVideo(video.get(PLAY_VIDEO_URL));
                    } catch (IOException ex) {
                        System.out.println("play function got problems!");
                    }
                }
            }
        }
    }

    private class ExternalAction extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == close) {
                close.setIcon(cross_hover);
            } else if (e.getSource() == pageLeft) {
                pageLeft.setBackground(model.getBRIGHT_COLOR());
            } else if (e.getSource() == pageRight) {
                pageRight.setBackground(model.getBRIGHT_COLOR());
            } else if (e.getSource() == musicPanel) {
                musicPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                music.setFont(TYPE);
            } else if (e.getSource() == videoPanel) {
                videoPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                video.setFont(TYPE);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == close) {
                close.setIcon(cross);
            } else if (e.getSource() == pageLeft) {
                pageLeft.setBackground(model.getSWITCHER_COLOR());
            } else if (e.getSource() == pageRight) {
                pageRight.setBackground(model.getSWITCHER_COLOR());
            } else if (e.getSource() == musicPanel && state != SONG) {
                musicPanel.setBackground(model.getPOST_COLOR());
                music.setFont(PLAIN);
            } else if (e.getSource() == videoPanel && state != VIDEO) {
                videoPanel.setBackground(model.getPOST_COLOR());
                video.setFont(PLAIN);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == close) {
                dispose();
            } else if (e.getSource() == pageLeft && page == LAST_PAGE && state == SONG) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                song = new SongPanel[10];
                for (int i = 0; i < 10; i++) {
                    try {
                        wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i)), String.format("w :%d:", POST_WIDTH));
                    } catch (IOException ex) {
                        System.out.println("access search results got problems!");
                    }
                }
                page = FIRST_PAGE;
                wrapper.updateUI();
            } else if (e.getSource() == pageRight && page == FIRST_PAGE && state == SONG) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                song = new SongPanel[searchResult.get(PLAY_SONG).size() - 10];
                for (int i = 0; i < song.length; i++) {
                    try {
                        wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i + 10)), String.format("w :%d:", POST_WIDTH));
                    } catch (IOException ex) {
                        System.out.println("access search results got problems!");
                    }
                }
                page = LAST_PAGE;
                wrapper.updateUI();
            } else if (e.getSource() == pageLeft && page == LAST_PAGE && state == VIDEO) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                clip = new VideoPanel[10];
                for (int i = 0; i < 10; i++) {
                    try {
                        wrapper.add(clip[i] = new VideoPanel(model, searchResult.get(PLAY_VIDEO).get(i)), String.format("w :%d:", POST_WIDTH));
                    } catch (IOException ex) {
                        System.out.println("access search results got problems!");
                    }
                }
                page = FIRST_PAGE;
                wrapper.updateUI();
            } else if (e.getSource() == pageRight && page == FIRST_PAGE && state == VIDEO) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                clip = new VideoPanel[searchResult.get(PLAY_VIDEO).size() - 10];
                for (int i = 0; i < clip.length; i++) {
                    try {
                        wrapper.add(clip[i] = new VideoPanel(model, searchResult.get(PLAY_VIDEO).get(i + 10)), String.format("w :%d:", POST_WIDTH));
                    } catch (IOException ex) {
                        System.out.println("access search results got problems!");
                    }
                }
                page = LAST_PAGE;
                wrapper.updateUI();
            } else if (e.getSource() == musicPanel && state == VIDEO) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                song = new SongPanel[searchResult.get(PLAY_SONG).size()];
                if (song.length < 10) {
                    page = DEFAULT;
                    for (int i = 0; i < song.length; i++) {
                        try {
                            wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i)), String.format("w :%d:", POST_WIDTH));
                        } catch (IOException ex) {
                            System.out.println("access search results got problems!");
                        }
                    }
                } else {
                    page = FIRST_PAGE;
                    for (int i = 0; i < 10; i++) {
                        try {
                            wrapper.add(song[i] = new SongPanel(model, searchResult.get(PLAY_SONG).get(i)), String.format("w :%d:", POST_WIDTH));
                        } catch (IOException ex) {
                            System.out.println("access search results got problems!");
                        }
                    }
                }
                state = SONG;
                musicPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                music.setFont(TYPE);
                videoPanel.setBackground(model.getPOST_COLOR());
                video.setFont(PLAIN);
            } else if (e.getSource() == videoPanel && state == SONG) {
                wrapper.removeAll();
                wrapper.add(tabPanel, String.format("w :%d:", POST_WIDTH));
                clip = new VideoPanel[searchResult.get(PLAY_VIDEO).size()];
                if (clip.length < 10) {
                    page = DEFAULT;
                    for (int i = 0; i < clip.length; i++) {
                        try {
                            wrapper.add(clip[i] = new VideoPanel(model, searchResult.get(PLAY_VIDEO).get(i)), String.format("w :%d:", POST_WIDTH));
                        } catch (IOException ex) {
                            System.out.println("access search results got problems!");
                        }
                    }
                } else {
                    page = FIRST_PAGE;
                    for (int i = 0; i < 10; i++) {
                        try {
                            wrapper.add(clip[i] = new VideoPanel(model, searchResult.get(PLAY_VIDEO).get(i)), String.format("w :%d:", POST_WIDTH));
                        } catch (IOException ex) {
                            System.out.println("access search results got problems!");
                        }
                    }
                }
                state = VIDEO;
                videoPanel.setBackground(model.getPOST_COLOR_BRIGHT());
                video.setFont(TYPE);
                musicPanel.setBackground(model.getPOST_COLOR());
                music.setFont(PLAIN);
            }
        }
    }
}

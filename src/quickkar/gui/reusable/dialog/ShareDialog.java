/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable.dialog;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.RoundedPanel;
import quickkar.gui.reusable.effect.ContentPane;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author Freedom
 */
public class ShareDialog extends JDialog implements CommonInterface {

    private DatabaseFacade model;
    private RoundedPanel panel;
    private ImageIcon cross;
    private ImageIcon cross_hover;
    private JLabel close;
    private String access_token = "";
    private String link;
    private final String URL_SHARE = "https://www.facebook.com/dialog/oauth?client_id=147714508702415"
            + "&redirect_uri=https://www.facebook.com/connect/login_success.html"
            + "&scope=publish_stream,share_item,user_about_me"
            + "&response_type=token";

    public ShareDialog(DatabaseFacade model, String link) throws MalformedURLException, IOException, URISyntaxException {
        super(model.getTopContainer(), ModalityType.DOCUMENT_MODAL);
        this.model = model;
        this.link = link;
        if (model.isSetProxy()) {
            JDialog failure = new PopupInformDialog(model, model.getLanguage().getString("facebookErr").toUpperCase(), model.getTopContainer(), "", ModalityType.DOCUMENT_MODAL);
        } else if (model.getAccessToken().equals("")) {
            UIUtils.setPreferredLookAndFeel();
            NativeInterface.initialize();
            NativeInterface.open();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    init();
                }
            });
            NativeInterface.runEventPump();
        } else {
            JDialog publish = new PublishDialog(model, link);
        }
    }

    private void init() {
        setUndecorated(true);
        setBackground(model.getLOGIN_COLOR());
        setContentPane(new ContentPane(DEFAULT_INVISIBLE, 15, model.getLOGIN_COLOR()));
        setLayout(new MigLayout("", "0[]0", "0[]0"));
        setSize(WRAPPER_WIDTH, WRAPPER_HEIGHT);

        panel = new RoundedPanel(new MigLayout());
        panel.setOpa(DEFAULT_VISIBLE);
        panel.setBackground(model.getLOGIN_COLOR());
        if (model.getCurrentTheme() == THEME1) {
            panel.setArcs(15, 15);
        } else {
            panel.setArcs(0, 0);
        }

        if (model.getCurrentTheme() == THEME1) {
            cross = CROSS;
            cross_hover = CROSS_HOVER;
        } else {
            cross = CROSS_2;
            cross_hover = CROSS_HOVER_2;
        }
        (close = new JLabel(cross)).addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setIcon(cross_hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setIcon(cross);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                dispose();
            }
        });

        final JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.navigate(URL_SHARE);
        panel.add(close, "push, align right, wrap");
        panel.add(webBrowser, "w :100%:, h :100%:");
        webBrowser.setBarsVisible(false);
        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {

            @Override
            public void locationChanged(WebBrowserNavigationEvent e) {
                final String newResourceLocation = e.getNewResourceLocation();
                if (newResourceLocation.contains("access_token")) {
                    access_token = newResourceLocation.substring(newResourceLocation.indexOf("=") + 1, newResourceLocation.indexOf("&"));
                    dispose();
                    model.setAccessToken(access_token);
                    JDialog publish = new PublishDialog(model, link);
                }
            }
        });

        setLocationRelativeTo(null);
        add(panel, "w :100%:, h :100%:, push, align center");

        setVisible(true);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.theme.staff;

import quickkar.gui.reusable.SystemUI;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.MenuSystem;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.gui.reusable.TopPanel;
import quickkar.gui.reusable.WrapperPanel;
import quickkar.gui.reusable.effect.TransparentBackground;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class StaffFrameView extends JFrame implements CommonInterface {

    private final SystemUI systemUI = new SystemUI();
    private WrapperPanel wrapper;
    private TopPanel topPanel;
    private MenuSystem menu;

    public StaffFrameView(DatabaseFacade model) {
        model.setSongList(model.getSongDatabase());

        TransparentBackground bg = new TransparentBackground(this);
        setContentPane(bg);
        systemUI.prepareUI();
        setLayout(new MigLayout("wrap 1", "0[]0", "0[]0"));

        wrapper = new WrapperPanel(STAFF, model);
        topPanel = new TopPanel(STAFF, model);
        menu = new MenuSystem(STAFF, model);

        add(topPanel, "w :100%:");
        add(wrapper, "w :100%:, h :100%:, dock south");
        setJMenuBar(menu);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("QUICKKAR");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}

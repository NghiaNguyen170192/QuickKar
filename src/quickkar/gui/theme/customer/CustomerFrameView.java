/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.theme.customer;

import quickkar.gui.reusable.TopPanel;
import quickkar.gui.reusable.WrapperPanel;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.gui.reusable.SystemUI;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import quickkar.gui.reusable.MenuSystem;
import quickkar.gui.reusable.effect.TransparentBackground;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class CustomerFrameView extends JFrame implements CommonInterface {

    private WrapperPanel wrapper;
    private TopPanel topPanel;
    private MenuSystem menu;
    private final SystemUI systemUI = new SystemUI();

    public CustomerFrameView(DatabaseFacade model) throws FileNotFoundException, IOException, InterruptedException {
        model.setSongList(model.getSongDatabase());
        model.setTopContainer(this);

        TransparentBackground bg = new TransparentBackground(this);
        setContentPane(bg);
        setLayout(new MigLayout("wrap 1", "0[]0", "0[]0"));
        systemUI.prepareUI();

        wrapper = new WrapperPanel(CUSTOMER, model);
        topPanel = new TopPanel(CUSTOMER, model);
        menu = new MenuSystem(CUSTOMER, model);

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

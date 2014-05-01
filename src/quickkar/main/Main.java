/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import quickkar.gui.reusable.dialog.LoadingDialog;
import quickkar.gui.reusable.effect.TransparentBackground;
import quickkar.gui.theme.admin.AdminFrameView;
import quickkar.gui.theme.common.CommonInterface;
import quickkar.gui.theme.customer.CustomerFrameView;
import quickkar.gui.theme.staff.StaffFrameView;
import quickkar.model.Database;
import quickkar.model.DatabaseFacade;

/**
 *
 * @author s3312310
 */
public class Main implements CommonInterface, Observer {

    private DatabaseFacade model;
    private CustomerFrameView customer;
    private StaffFrameView staff;
    private AdminFrameView admin;

    public Main() throws FileNotFoundException, IOException, InterruptedException {
        LoadingDialog loading = new LoadingDialog(null, false);
        model = new Database();
        customer = new CustomerFrameView(model);
        ((Database) model).addObserver(this);
        loading.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (((Database) o).getCurrentUser() == CUSTOMER) {
            if (staff != null) {
                staff.dispose();
            }
            if (admin != null) {
                admin.dispose();
            }
            if (customer != null) {
                customer.dispose();
            }
            try {
                customer = new CustomerFrameView(model);
                ((TransparentBackground) customer.getContentPane()).refresh();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            model.setCurrentUser(0);
        } else if (((Database) o).getCurrentUser() == STAFF) {
            if (staff != null) {
                staff.dispose();
            }
            if (admin != null) {
                admin.dispose();
            }
            if (customer != null) {
                customer.dispose();
            }
            staff = new StaffFrameView(model);
            ((TransparentBackground) staff.getContentPane()).refresh();
            model.setCurrentUser(0);
        } else if (((Database) o).getCurrentUser() == ADMIN) {
            if (staff != null) {
                staff.dispose();
            }
            if (admin != null) {
                admin.dispose();
            }
            if (customer != null) {
                customer.dispose();
            }
            admin = new AdminFrameView(model);
            ((TransparentBackground) admin.getContentPane()).refresh();
            model.setCurrentUser(0);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        new Main();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.gui.reusable;

import quickkar.model.DatabaseFacade;

/**
 * Construct Regex
 * @author Nhan Dao Vinh Nghia
 */
public class RegularExpression {

    private DatabaseFacade model;

    public RegularExpression(DatabaseFacade model) {
        this.model = model;
    }

    /**
     * Validates inputs when an account is registered
     * @param username
     * @param password
     * @param fullname
     * @param email
     * @param phone
     * @param address
     * @param description
     * @return message error or null if no error
     */
    public String inforCheck(String username, String password, String fullname, String email, String phone, String address, String description) {
        boolean check = true;
        String error = "<html>";
        // Check empty fields
        if (username.equals("") || password.equals("")
                || fullname.equals("") || email.equals("") || phone.equals("") || address.equals("") || description.equals("")) {
            return model.getLanguage().getString("emptyErr").toUpperCase();
        } else {
            //Check password
            if (!password.matches(".{5,}")) {
                error += model.getLanguage().getString("passErr").toUpperCase() + "<br />";
                check = false;
            }
            //Check phone
            if (!phone.matches("^((012[0-9])|(016[2-9])|(09[0-9])|(0188)|(0199))[0-9]{7}$") && !phone.matches("^08[0-9]{8}$")) {
                error += model.getLanguage().getString("phoneErr").toUpperCase() + "<br />";
                check = false;
            }
            //Check email
            if (!email.matches("^([\\w-\\.])+@([\\w])+\\.(\\w){2,6}(\\.([\\w]){2,4})*$")) {
                error += model.getLanguage().getString("emailErr").toUpperCase() + "<br />";
                check = false;
            }
            //Check address
            if (!address.matches("[a-zA-Z0-9 ,./]+")) {
                error += model.getLanguage().getString("addressErr").toUpperCase();
                check = false;
            }
        }
        if (!check) {
            error += "</html>";
            return error;
        }
        return null;
    }

    /**
     * Validates phone and email provided by user when rating a song
     * @param phone
     * @param email
     * @return message error or null if no error
     */
    public String voteCheck(String phone, String email) {
        boolean check = true;
        String error = "<html>";
        // Check empty fields
        if (phone.equals("") || email.equals("")) {
            return model.getLanguage().getString("emptyErr").toUpperCase();
        } else {
            // Check phone
            if (!phone.matches("^((012[0-9])|(016[2-9])|(09[0-9])|(0188)|(0199))[0-9]{7}$") && !phone.matches("^08[0-9]{8}$")) {
                error += model.getLanguage().getString("phoneErr").toUpperCase() + "<br />";
                check = false;
            }
            // Check email
            if (!email.matches("^([\\w-\\.])+@([\\w])+\\.(\\w){2,6}(\\.([\\w]){2,4})*$")) {
                error += model.getLanguage().getString("emailErr").toUpperCase() + "<br />";
                check = false;
            }
        }
        if (!check) {
            error += "</html>";
            return error;
        }
        return null;
    }

    /**
     * Validate inputs when adding a new song
     * @param songCode
     * @param songTitle
     * @param lyric
     * @param author
     * @return message error or null if no error
     */
    public String songCheck(String songCode, String songTitle, String lyric, String author) {
        boolean check = true;
        String error = "<html>";
        // Check empty fields
        if (songCode.equals("") || songTitle.equals("")
                || lyric.equals("") || author.equals("")) {
            return model.getLanguage().getString("emptyErr").toUpperCase();
        } else {
            //Check song code
            if (!songCode.matches("(\\d{5})")) {
                error += model.getLanguage().getString("codeLenErr").toUpperCase();
                check = false;
            }
        }
        if (!check) {
            error += "</html>";
            return error;
        }
        return null;
    }
}

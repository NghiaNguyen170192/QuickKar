/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package quickkar.model;

import java.io.Serializable;

/**
 *
 * @author Le Chinh Nhan and Nguyen Ba Dao
 */
public abstract class Account implements Serializable, Cloneable {

    private String name, username, password, email, phone, address, description;
    private boolean select = false;

    public Account(String name, String username, String password, String email, String phone, String address, String description) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
    }

    public Account() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return select;
    }

    public void setSelected(boolean select) {
        this.select = select;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Account) super.clone();
    }
}

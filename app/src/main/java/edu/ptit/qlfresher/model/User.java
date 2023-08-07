package edu.ptit.qlfresher.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email, password, fullname, dob;

    public User() {
    }

    public User(String email, String password, String fullname, String dob) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.dob = dob;
    }

    public User(int id, String email, String password, String fullname, String dob) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.dob = dob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
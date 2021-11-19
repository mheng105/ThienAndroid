package com.example.ad_btl.Characters;

import java.io.Serializable;
public class Accounts implements Serializable {
    public Accounts() {
    }

    public String susername, spassword;
    public Accounts(String username, String password) {
        this.susername = username;
        this.spassword = password;
    }


    public String getUsername() {
        return susername;
    }

    public void setUsername(String username) {
        this.susername = username;
    }

    public String getPassword() {
        return spassword;
    }

    public void setPassword(String password) {
        this.spassword = password;
    }



}
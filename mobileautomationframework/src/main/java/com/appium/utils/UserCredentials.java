package com.appium.utils;

public class UserCredentials {

    String userName;
    String passWord;

    public UserCredentials(String username, String password) {
        this.userName = username;
        this.passWord = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }


}

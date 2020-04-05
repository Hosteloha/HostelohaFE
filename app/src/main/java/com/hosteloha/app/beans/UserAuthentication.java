package com.hosteloha.app.beans;

import com.google.gson.annotations.SerializedName;

public class UserAuthentication {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    private String userAuthenticationToken;
    private String jwt;

    public UserAuthentication(String username, String userPass) {
        this.username = username;
        this.password = userPass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAuthenticationToken() {
        return userAuthenticationToken;
    }

    public void setUserAuthenticationToken(String userAuthenticationToken) {
        this.userAuthenticationToken = userAuthenticationToken;
    }
}

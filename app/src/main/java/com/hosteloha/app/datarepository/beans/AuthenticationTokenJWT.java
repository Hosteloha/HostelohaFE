package com.hosteloha.app.datarepository.beans;

import com.google.gson.annotations.SerializedName;

public class AuthenticationTokenJWT {
    @SerializedName("jwt")
    private String jwt;
    @SerializedName("id")
    private int userId;
    @SerializedName("expirationDate")
    private String tokenExpiryDate;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(String tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public AuthenticationTokenJWT(String jwt, int userId, String tokenExpiryDate) {

        this.jwt = jwt;
        this.userId = userId;
        this.tokenExpiryDate = tokenExpiryDate;
    }
}

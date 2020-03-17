package com.hosteloha.app.beans;

public class AuthenticationTokenJWT {
    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public AuthenticationTokenJWT(String jwt) {
        this.jwt = jwt;
    }
}

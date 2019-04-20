package com.one.vcanteen_admin;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    private String token;

    @SerializedName("email")
    private String email;

    public Token(String email, String token) {
        this.email = email;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.zeustech.excursions.http.requests;

import androidx.annotation.NonNull;

public class LoginRequest {

    private final String user, pass;

    public LoginRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginRequest{" +
                "user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}

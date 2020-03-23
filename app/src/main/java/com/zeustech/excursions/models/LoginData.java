package com.zeustech.excursions.models;

import androidx.annotation.NonNull;

public class LoginData extends ExLoginModel {

    private final String username, password;

    public LoginData(@NonNull ExLoginModel login, String username, String password) {
        super(login.getSeller(), login.getCompany(), login.getCustomer());
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", seller='" + seller + '\'' +
                ", company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}

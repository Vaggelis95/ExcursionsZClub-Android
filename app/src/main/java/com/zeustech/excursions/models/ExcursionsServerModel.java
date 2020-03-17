package com.zeustech.excursions.models;

import androidx.annotation.NonNull;

public class ExcursionsServerModel {

    private final String distribution_host;
    private final String development_host;
    private final String username;
    private final String password;

    public ExcursionsServerModel(String distribution_host, String development_host, String username, String password) {
        this.distribution_host = distribution_host;
        this.development_host = development_host;
        this.username = username;
        this.password = password;
    }

    public String getDistributionHost() {
        return distribution_host;
    }

    public String getDevelopmentHost() {
        return development_host;
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
        return "ExcursionsServerModel{" +
                "distribution_host='" + distribution_host + '\'' +
                ", development_host='" + development_host + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

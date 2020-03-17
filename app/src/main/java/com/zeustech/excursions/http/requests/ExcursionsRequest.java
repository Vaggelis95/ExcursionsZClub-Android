package com.zeustech.excursions.http.requests;

import androidx.annotation.NonNull;

public class ExcursionsRequest {

    private final String hotel, language, customer;

    public ExcursionsRequest(String hotel, String language, String customer) {
        this.hotel = hotel;
        this.language = language;
        this.customer = customer;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExcursionsRequest{" +
                "hotel='" + hotel + '\'' +
                ", language='" + language + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}

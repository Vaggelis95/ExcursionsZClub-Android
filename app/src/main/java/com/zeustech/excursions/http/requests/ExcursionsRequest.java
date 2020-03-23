package com.zeustech.excursions.http.requests;

import androidx.annotation.NonNull;

public class ExcursionsRequest {

    private final String hotel, language, customer;

    public ExcursionsRequest(String hotel, String customer, String language) {
        this.hotel = hotel;
        this.customer = customer;
        this.language = language;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExcursionsRequest{" +
                "hotel='" + hotel + '\'' +
                ", customer='" + customer + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}

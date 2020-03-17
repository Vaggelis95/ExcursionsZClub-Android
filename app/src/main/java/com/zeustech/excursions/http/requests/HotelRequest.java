package com.zeustech.excursions.http.requests;

import androidx.annotation.NonNull;

public class HotelRequest {

    private final String areaCode;

    public HotelRequest(String areaCode) {
        this.areaCode = areaCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "HotelRequest{" +
                "areaCode='" + areaCode + '\'' +
                '}';
    }
}

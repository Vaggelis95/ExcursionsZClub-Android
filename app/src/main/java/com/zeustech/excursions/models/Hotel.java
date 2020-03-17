package com.zeustech.excursions.models;

import com.zeustech.excursions.customViews.autoComplete.AutoCompleteData;

import androidx.annotation.NonNull;

public class Hotel implements AutoCompleteData {

    private final String hotelCode, hotelName;

    public Hotel(String hotelCode, String hotelName) {
        this.hotelCode = hotelCode;
        this.hotelName = hotelName;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    @NonNull
    @Override
    public String getText() {
        return hotelName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hotel{" +
                "hotelCode='" + hotelCode + '\'' +
                ", hotelName='" + hotelName + '\'' +
                '}';
    }
}

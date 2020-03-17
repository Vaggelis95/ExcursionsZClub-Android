package com.zeustech.excursions.models;

import com.zeustech.excursions.customViews.autoComplete.AutoCompleteData;

import androidx.annotation.NonNull;

public class Area implements AutoCompleteData {

    private final String areaCode, description;

    public Area(String areaCode, String description) {
        this.areaCode = areaCode;
        this.description = description;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    @Override
    public String getText() {
        return description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Area{" +
                "areaCode='" + areaCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

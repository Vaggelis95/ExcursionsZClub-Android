package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExLanguageModel implements Parcelable {

    private String languageCode, description;

    public ExLanguageModel(String languageCode, String description) {
        this.languageCode = languageCode;
        this.description = description;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public static String findLanCode(String lanDescr, List<ExLanguageModel> languages) {
        for (ExLanguageModel language : languages) {
            if (language.getDescription().equals(lanDescr)) {
                return language.getLanguageCode();
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExLanguageModel{" +
                "languageCode='" + languageCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    protected ExLanguageModel(Parcel in) {
        languageCode = in.readString();
        description = in.readString();
    }

    public static final Creator<ExLanguageModel> CREATOR = new Creator<ExLanguageModel>() {
        @Override
        public ExLanguageModel createFromParcel(Parcel in) {
            return new ExLanguageModel(in);
        }

        @Override
        public ExLanguageModel[] newArray(int size) {
            return new ExLanguageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(languageCode);
        parcel.writeString(description);
    }

}

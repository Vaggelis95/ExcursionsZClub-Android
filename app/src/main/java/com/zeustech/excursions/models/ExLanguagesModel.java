package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import androidx.annotation.NonNull;

public class ExLanguagesModel implements Parcelable {

    private ExLanguageModel[] languages;

    public ExLanguageModel[] getLanguages() {
        return languages;
    }

    public String getPrefixFor(String language) {
        for (ExLanguageModel model : languages) {
            if (model.getDescription().equals(language)) {
                return model.getLanguageCode();
            }
        }
        return null;
    }

    public static final Creator<ExLanguagesModel> CREATOR = new Creator<ExLanguagesModel>() {
        @Override
        public ExLanguagesModel createFromParcel(Parcel in) {
            return new ExLanguagesModel(in);
        }

        @Override
        public ExLanguagesModel[] newArray(int size) {
            return new ExLanguagesModel[size];
        }
    };

    protected ExLanguagesModel(Parcel in) {
        languages = in.createTypedArray(ExLanguageModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(languages, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExLanguagesModel{" +
                "languages=" + Arrays.toString(languages) +
                '}';
    }
}

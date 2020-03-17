package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.annotation.NonNull;

public class ExDaysModel implements Parcelable {

    private final List<String> excDays;

    public ExDaysModel(List<String> days) {
        this.excDays = days;
    }

    public List<String> getDays() {
        return excDays;
    }

    public static final Creator<ExDaysModel> CREATOR = new Creator<ExDaysModel>() {
        @Override
        public ExDaysModel createFromParcel(Parcel in) {
            return new ExDaysModel(in);
        }

        @Override
        public ExDaysModel[] newArray(int size) {
            return new ExDaysModel[size];
        }
    };

    protected ExDaysModel(Parcel in) {
        excDays = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(excDays);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExDaysModel{" +
                "excDays=" + excDays +
                '}';
    }
}

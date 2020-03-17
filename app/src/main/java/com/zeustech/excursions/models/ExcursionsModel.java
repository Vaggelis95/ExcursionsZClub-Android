package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.zeustech.excursions.customViews.autoComplete.AutoCompleteData;

import androidx.annotation.NonNull;

public class ExcursionsModel implements AutoCompleteData, Parcelable {

    private String excCode, excDescr, picPath;

    public ExcursionsModel(String excCode, String excDescr, String picPath) {
        this.excCode = excCode;
        this.excDescr = excDescr;
        this.picPath = picPath;
    }

    public boolean isImageAvailable() {
        return picPath != null && !picPath.isEmpty();
    }

    public String getCode() {
        return excCode;
    }

    public String getDescription() {
        return excDescr;
    }

    public String getPicPath() {
        return picPath;
    }

    @NonNull
    @Override
    public String getText() {
        return excDescr;
    }

    public static final Creator<ExcursionsModel> CREATOR = new Creator<ExcursionsModel>() {
        @Override
        public ExcursionsModel createFromParcel(Parcel in) {
            return new ExcursionsModel(in);
        }

        @Override
        public ExcursionsModel[] newArray(int size) {
            return new ExcursionsModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(excCode);
        dest.writeString(excDescr);
        dest.writeString(picPath);
    }

    protected ExcursionsModel(Parcel in) {
        excCode = in.readString();
        excDescr = in.readString();
        picPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

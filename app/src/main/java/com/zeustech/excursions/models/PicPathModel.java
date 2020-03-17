package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PicPathModel implements Parcelable {

    private String picPath;

    public String getPicPath() {
        return picPath;
    }

    public static final Creator<PicPathModel> CREATOR = new Creator<PicPathModel>() {
        @Override
        public PicPathModel createFromParcel(Parcel in) {
            return new PicPathModel(in);
        }

        @Override
        public PicPathModel[] newArray(int size) {
            return new PicPathModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picPath);
    }

    protected PicPathModel(Parcel in) {
        picPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "PicPathModel{" +
                "picPath='" + picPath + '\'' +
                '}';
    }
}

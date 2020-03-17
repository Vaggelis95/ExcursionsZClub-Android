package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import androidx.annotation.NonNull;

public class ExcursionModel implements Parcelable {

    private String excCode, excDescr, mainPicPath;

    private PicPathModel[] picGallery;

    public String getCode() {
        return excCode;
    }

    public String getDescription() {
        return excDescr;
    }

    public String getMainPicPath() {
        return mainPicPath;
    }

    public PicPathModel[] getPicGallery() {
        return picGallery;
    }

    public static final Creator<ExcursionModel> CREATOR = new Creator<ExcursionModel>() {
        @Override
        public ExcursionModel createFromParcel(Parcel in) {
            return new ExcursionModel(in);
        }

        @Override
        public ExcursionModel[] newArray(int size) {
            return new ExcursionModel[size];
        }
    };

    protected ExcursionModel(Parcel in) {
        excCode = in.readString();
        excDescr = in.readString();
        mainPicPath = in.readString();
        picGallery = in.createTypedArray(PicPathModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(excCode);
        dest.writeString(excDescr);
        dest.writeString(mainPicPath);
        dest.writeTypedArray(picGallery, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExcursionModel{" +
                "excCode='" + excCode + '\'' +
                ", excDescr='" + excDescr + '\'' +
                ", mainPicPath='" + mainPicPath + '\'' +
                ", picGallery=" + Arrays.toString(picGallery) +
                '}';
    }
}

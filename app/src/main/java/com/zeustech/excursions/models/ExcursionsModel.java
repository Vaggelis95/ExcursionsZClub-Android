package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.zeustech.excursions.customViews.autoComplete.AutoCompleteData;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ExcursionsModel implements AutoCompleteData, Parcelable {

    private String excCode, excDescr, picPath;

    public ExcursionsModel(String excCode, String excDescr, String picPath) {
        this.excCode = excCode;
        this.excDescr = excDescr;
        this.picPath = picPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExcursionsModel that = (ExcursionsModel) o;
        return Objects.equals(excCode, that.excCode) &&
                Objects.equals(excDescr, that.excDescr) &&
                Objects.equals(picPath, that.picPath);
    }

    public boolean areItemsTheSame(@NonNull ExcursionsModel that) {
        return Objects.equals(excCode, that.excCode);
    }

    public boolean areContentsTheSame(@NonNull ExcursionsModel that) {
        return Objects.equals(excDescr, that.excDescr) &&
                Objects.equals(picPath, that.picPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excCode, excDescr, picPath);
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

package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExBookingData implements Parcelable {

    private String exc_code;
    private String exc_name;
    private String main_pick_path;

    public ExBookingData(String exc_code, String exc_name, String main_pick_path) {
        this.exc_code = exc_code;
        this.exc_name = exc_name;
        this.main_pick_path = main_pick_path;
    }

    public String getCode() {
        return exc_code;
    }

    public void setCode(String exc_code) {
        this.exc_code = exc_code;
    }

    public String getName() {
        return exc_name;
    }

    public void setName(String exc_name) {
        this.exc_name = exc_name;
    }

    public String getMainPickPath() {
        return main_pick_path;
    }

    public void setMainPickPath(String main_pick_path) {
        this.main_pick_path = main_pick_path;
    }

    protected ExBookingData(Parcel in) {
        exc_code = in.readString();
        exc_name = in.readString();
        main_pick_path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exc_code);
        dest.writeString(exc_name);
        dest.writeString(main_pick_path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExBookingData> CREATOR = new Creator<ExBookingData>() {
        @Override
        public ExBookingData createFromParcel(Parcel in) {
            return new ExBookingData(in);
        }

        @Override
        public ExBookingData[] newArray(int size) {
            return new ExBookingData[size];
        }
    };

}

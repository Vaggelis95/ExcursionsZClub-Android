package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ExCustomerInfo implements Parcelable {

    private String name, roomCode, remarks;

    public ExCustomerInfo(String name, String roomCode, String remarks) {
        this.name = name;
        this.roomCode = roomCode;
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public static final Creator<ExCustomerInfo> CREATOR = new Creator<ExCustomerInfo>() {
        @Override
        public ExCustomerInfo createFromParcel(Parcel in) {
            return new ExCustomerInfo(in);
        }

        @Override
        public ExCustomerInfo[] newArray(int size) {
            return new ExCustomerInfo[size];
        }
    };

    protected ExCustomerInfo(Parcel in) {
        name = in.readString();
        roomCode = in.readString();
        remarks = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(roomCode);
        dest.writeString(remarks);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExCustomerInfo{" +
                "name='" + name + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

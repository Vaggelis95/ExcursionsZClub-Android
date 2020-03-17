package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ExTicketId implements Parcelable {

    private int year, intno;

    public int getYear() {
        return year;
    }

    public int getIntNo() {
        return intno;
    }

    public boolean isValid() {
        return (year != 0 && intno != 0);
    }

    public static final Creator<ExTicketId> CREATOR = new Creator<ExTicketId>() {
        @Override
        public ExTicketId createFromParcel(Parcel in) {
            return new ExTicketId(in);
        }

        @Override
        public ExTicketId[] newArray(int size) {
            return new ExTicketId[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(intno);
    }

    protected ExTicketId(Parcel in) {
        year = in.readInt();
        intno = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExTicketId{" +
                "year=" + year +
                ", intno=" + intno +
                '}';
    }

}

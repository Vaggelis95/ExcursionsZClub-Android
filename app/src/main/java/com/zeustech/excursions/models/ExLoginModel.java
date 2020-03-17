package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ExLoginModel implements Parcelable {

    private String seller, company, customer;

    public String getSeller() {
        return seller;
    }

    public String getCompany() {
        return company;
    }

    public String getCustomer() {
        return customer;
    }

    public static final Creator<ExLoginModel> CREATOR = new Creator<ExLoginModel>() {
        @Override
        public ExLoginModel createFromParcel(Parcel in) {
            return new ExLoginModel(in);
        }

        @Override
        public ExLoginModel[] newArray(int size) {
            return new ExLoginModel[size];
        }
    };

    protected ExLoginModel(Parcel in) {
        seller = in.readString();
        company = in.readString();
        customer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seller);
        dest.writeString(company);
        dest.writeString(customer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExLoginModel{" +
                "seller='" + seller + '\'' +
                ", company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                '}';
    }
}

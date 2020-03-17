package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ExCardDetails implements Parcelable {

    private String cardHolder, ccNumber, ccv, expYear, expMonth, email;

    public ExCardDetails(String cardHolder, String ccNumber, String ccv, String expYear, String expMonth, String email) {
        this.cardHolder = cardHolder;
        this.ccNumber = ccNumber;
        this.ccv = ccv;
        this.expYear = expYear;
        this.expMonth = expMonth;
        this.email = email;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCCNumber() {
        return ccNumber;
    }

    public String getCCV() {
        return ccv;
    }

    public String getExpYear() {
        return expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public String getEmail() {
        return email;
    }

    public static final Creator<ExCardDetails> CREATOR = new Creator<ExCardDetails>() {
        @Override
        public ExCardDetails createFromParcel(Parcel in) {
            return new ExCardDetails(in);
        }

        @Override
        public ExCardDetails[] newArray(int size) {
            return new ExCardDetails[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardHolder);
        dest.writeString(ccNumber);
        dest.writeString(ccv);
        dest.writeString(expYear);
        dest.writeString(expMonth);
        dest.writeString(email);
    }

    protected ExCardDetails(Parcel in) {
        cardHolder = in.readString();
        ccNumber = in.readString();
        ccv = in.readString();
        expYear = in.readString();
        expMonth = in.readString();
        email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExCardDetails{" +
                "cardHolder='" + cardHolder + '\'' +
                ", ccNumber='" + ccNumber + '\'' +
                ", ccv='" + ccv + '\'' +
                ", expYear='" + expYear + '\'' +
                ", expMonth='" + expMonth + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

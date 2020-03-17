package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExBasketModel implements Parcelable {

    private ExPriceModel priceModel;
    private String pickPath;

    public ExBasketModel(ExPriceModel priceModel, String pickPath) {
        this.priceModel = priceModel;
        this.pickPath = pickPath;
    }

    public ExPriceModel getPriceModel() {
        return priceModel;
    }

    public String getPickPath() {
        return pickPath;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ExBasketModel)) { return false; }
        ExBasketModel model = (ExBasketModel) obj;
        return priceModel.getCode().equals(model.getPriceModel().getCode());
    }

    public static final Creator<ExBasketModel> CREATOR = new Creator<ExBasketModel>() {
        @Override
        public ExBasketModel createFromParcel(Parcel in) {
            return new ExBasketModel(in);
        }

        @Override
        public ExBasketModel[] newArray(int size) {
            return new ExBasketModel[size];
        }
    };

    protected ExBasketModel(Parcel in) {
        priceModel = in.readParcelable(ExPriceModel.class.getClassLoader());
        pickPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(priceModel, flags);
        dest.writeString(pickPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExBasketModel{" +
                "priceModel=" + priceModel +
                ", pickPath='" + pickPath + '\'' +
                '}';
    }
}

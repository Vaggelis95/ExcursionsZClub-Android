package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;

public class ExPriceModel implements Parcelable {

    private int adults, children, infants;
    private String company, customer, excCode, excDate, hotel;
    private String language, pickUpPointCode, pickUpTime;
    private Float price;

    private String excDescr, pickUpPoint;
    private List<String> included;

    public ExPriceModel(String excDate, String excCode, String excDescr, String language, String company, String customer, String hotel, int adults, int children, int infants, Float price, String pickUpPointCode, String pickUpPoint, String pickUpTime) {
        this.excDate = excDate;
        this.excCode = excCode;
        this.excDescr = excDescr;
        this.language = language;
        this.company = company;
        this.customer = customer;
        this.hotel = hotel;
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.price = price;
        this.pickUpPointCode = pickUpPointCode;
        this.pickUpPoint = pickUpPoint;
        this.pickUpTime = pickUpTime;
    }

    public String getDate() {
        return excDate;
    }

    public String getCode() {
        return excCode;
    }

    public String getDescription() {
        return excDescr;
    }

    public String getLanguage() {
        return language;
    }

    public String getCompany() {
        return company;
    }

    public String getCustomer() {
        return customer;
    }

    public String getHotel() {
        return hotel;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getInfants() {
        return infants;
    }

    public List<String> getIncluded() {
        return included;
    }

    public String getFormattedPrice() {
        try {
            return String.format(Locale.US, "%.2f", price);
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        }
        return String.valueOf(price);
    }

    public Float getPrice() {
        return this.price;
    }

    public String getPickUpPointCode() {
        return pickUpPointCode;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ExPriceModel)) { return false; }
        ExPriceModel model = (ExPriceModel) obj;
        return model.excDate.equals(excDate) && model.excCode.equals(excCode) && model.excDescr.equals(excDescr)
                && model.language.equals(language) && model.company.equals(company) && model.customer.equals(customer)
                && model.hotel.equals(hotel) && model.adults == adults && model.children == children && model.infants == infants
                && model.price.equals(price) && model.pickUpPointCode.equals(pickUpPointCode) && model.pickUpPoint.equals(pickUpPoint)
                && model.pickUpTime.equals(pickUpTime);
    }

    protected ExPriceModel(Parcel in) {
        excDate = in.readString();
        excCode = in.readString();
        excDescr = in.readString();
        language = in.readString();
        company = in.readString();
        customer = in.readString();
        hotel = in.readString();
        adults = in.readInt();
        children = in.readInt();
        infants = in.readInt();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        pickUpPointCode = in.readString();
        pickUpPoint = in.readString();
        pickUpTime = in.readString();
        included = in.createStringArrayList();
    }

    public static final Creator<ExPriceModel> CREATOR = new Creator<ExPriceModel>() {
        @Override
        public ExPriceModel createFromParcel(Parcel in) {
            return new ExPriceModel(in);
        }

        @Override
        public ExPriceModel[] newArray(int size) {
            return new ExPriceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(excDate);
        parcel.writeString(excCode);
        parcel.writeString(excDescr);
        parcel.writeString(language);
        parcel.writeString(company);
        parcel.writeString(customer);
        parcel.writeString(hotel);
        parcel.writeInt(adults);
        parcel.writeInt(children);
        parcel.writeInt(infants);
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(price);
        }
        parcel.writeString(pickUpPointCode);
        parcel.writeString(pickUpPoint);
        parcel.writeString(pickUpTime);
        parcel.writeStringList(included);
    }
}

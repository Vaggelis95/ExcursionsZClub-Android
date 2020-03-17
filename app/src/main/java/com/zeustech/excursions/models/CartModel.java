package com.zeustech.excursions.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class CartModel implements Parcelable {

    private String excDate, excCode, excDescr, language, company, customer, hotel;
    private int adult, child, infand;
    private Float price;
    private String pickUpPointCode, pickUpPoint, pickUpTime;
    private String pickPath;

    public CartModel(ExPriceModel priceModel, String pickPath) {
        excDate = priceModel.getDate();
        excCode = priceModel.getCode();
        excDescr = priceModel.getDescription();
        language = priceModel.getLanguage();
        company = priceModel.getCompany();
        customer = priceModel.getCustomer();
        hotel = priceModel.getHotel();
        adult = priceModel.getAdults();
        child = priceModel.getChildren();
        infand = priceModel.getInfants();
        price = priceModel.getPrice();
        pickUpPointCode = priceModel.getPickUpPointCode();
        pickUpPoint = priceModel.getPickUpPoint();
        pickUpTime = priceModel.getPickUpTime();
        this.pickPath = pickPath;
    }

    public ExPriceModel getPriceModel() {
        return new ExPriceModel(excDate, excCode, excDescr, language, company,
                customer, hotel, adult, child, infand, price, pickUpPointCode, pickUpPoint, pickUpTime);
    }

    public boolean isValid() {
        return (excCode != null && excDescr != null && company != null
                && customer != null && hotel != null && price != null);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof CartModel)) { return false; }
        CartModel model = (CartModel) obj;
        return getCode().equals(model.getPriceModel().getCode());
    }

    public String getDate() { return excDate; }

    public void setDate(String date) { this.excDate = date; }

    public String getCode() { return excCode; }

    public void setCode(String code) { this.excCode = code; }

    public String getDescription() { return excDescr; }

    public void setDescription(String description) { this.excDescr = description; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public String getCustomer() { return customer; }

    public void setCustomer(String customer) { this.customer = customer; }

    public String getHotel() { return hotel; }

    public void setHotel(String hotel) { this.hotel = hotel; }

    public int getAdult() { return adult; }

    public void setAdult(int adult) { this.adult = adult; }

    public int getChild() { return child; }

    public void setChild(int child) { this.child = child; }

    public int getInfants() { return infand; }

    public void setInfants(int infants) { this.infand = infants; }

    public Float getPrice() { return price; }

    public void setPrice(Float price) { this.price = price; }

    public String getPickUpPointCode() { return pickUpPointCode; }

    public void setPickUpPointCode(String pickUpPointCode) { this.pickUpPointCode = pickUpPointCode; }

    public String getPickUpPoint() { return pickUpPoint; }

    public void setPickUpPoint(String pickUpPoint) { this.pickUpPoint = pickUpPoint; }

    public String getPickUpTime() { return pickUpTime; }

    public void setPickUpTime(String pickUpTime) { this.pickUpTime = pickUpTime; }

    public String getPickPath() { return pickPath; }

    public void setPickPath(String pickPath) { this.pickPath = pickPath; }

    protected CartModel(Parcel in) {
        excDate = in.readString();
        excCode = in.readString();
        excDescr = in.readString();
        language = in.readString();
        company = in.readString();
        customer = in.readString();
        hotel = in.readString();
        adult = in.readInt();
        child = in.readInt();
        infand = in.readInt();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        pickUpPointCode = in.readString();
        pickUpPoint = in.readString();
        pickUpTime = in.readString();
        pickPath = in.readString();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(excDate);
        dest.writeString(excCode);
        dest.writeString(excDescr);
        dest.writeString(language);
        dest.writeString(company);
        dest.writeString(customer);
        dest.writeString(hotel);
        dest.writeInt(adult);
        dest.writeInt(child);
        dest.writeInt(infand);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        dest.writeString(pickUpPointCode);
        dest.writeString(pickUpPoint);
        dest.writeString(pickUpTime);
        dest.writeString(pickPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

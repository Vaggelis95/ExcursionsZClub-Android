package com.zeustech.excursions.http.requests;

import com.zeustech.excursions.customViews.DateManager;
import com.zeustech.excursions.models.ExCustomerInfo;
import com.zeustech.excursions.models.ExPriceModel;

import androidx.annotation.NonNull;

public class BookingCart {

    private final int adults, children, infants;
    private final String company, customer, excCode, excDate, hotel;
    private final String language, pickUpPointCode, pickUpTime;
    private final Float price;
    private final String seller, name, roomCode, remarks;

    private BookingCart(int adults, int children, int infants, String company, String customer, String excCode, String excDate, String hotel, String language, String pickUpPointCode, String pickUpTime, Float price, String seller, String name, String roomCode, String remarks) {
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.company = company;
        this.customer = customer;
        this.excCode = excCode;
        this.excDate = excDate;
        this.hotel = hotel;
        this.language = language;
        this.pickUpPointCode = pickUpPointCode;
        this.pickUpTime = pickUpTime;
        this.price = price;
        this.seller = seller;
        this.name = name;
        this.roomCode = roomCode;
        this.remarks = remarks;
    }

    static BookingCart Create(@NonNull ExPriceModel model, @NonNull String seller, @NonNull ExCustomerInfo customer) {
        return new BookingCart(model.getAdults(), model.getChildren(), model.getInfants(), model.getCompany(),
                model.getCustomer(), model.getCode(), DateManager.convertDate(model.getDate(), "dd/MM/yyyy", "yyyyMMdd"),
                model.getHotel(), model.getLanguage(), model.getPickUpPointCode(), model.getPickUpTime(), model.getPrice(), seller,
                customer.getName(), customer.getRoomCode(), customer.getRemarks());
    }

    @NonNull
    @Override
    public String toString() {
        return "BookingCart{" +
                "adults=" + adults +
                ", children=" + children +
                ", infants=" + infants +
                ", company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                ", excCode='" + excCode + '\'' +
                ", excDate='" + excDate + '\'' +
                ", hotel='" + hotel + '\'' +
                ", language='" + language + '\'' +
                ", pickUpPointCode='" + pickUpPointCode + '\'' +
                ", pickUpTime='" + pickUpTime + '\'' +
                ", price=" + price +
                ", seller='" + seller + '\'' +
                ", name='" + name + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

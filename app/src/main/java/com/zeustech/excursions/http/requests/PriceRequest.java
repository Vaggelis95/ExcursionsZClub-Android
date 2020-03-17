package com.zeustech.excursions.http.requests;

import com.zeustech.excursions.models.ExLoginModel;

import androidx.annotation.NonNull;

public class PriceRequest {

    private final String excDate, excCode, language, company, customer, hotel;
    private final String adults, children, infants;

    private PriceRequest(String excDate, String excCode, String language, String company, String customer, String hotel, String adults, String children, String infants) {
        this.excDate = excDate;
        this.excCode = excCode;
        this.language = language;
        this.company = company;
        this.customer = customer;
        this.hotel = hotel;
        this.adults = adults;
        this.children = children;
        this.infants = infants;
    }

    public static PriceRequest Create(String excDate, String excCode, String language, @NonNull ExLoginModel login,
                                      String hotel, String adults, String children, String infants) {
        return new PriceRequest(excDate, excCode, language, login.getCompany(), login.getCustomer(),
                hotel, adults, children, infants);
    }

    @NonNull
    @Override
    public String toString() {
        return "PriceRequest{" +
                "excDate='" + excDate + '\'' +
                ", excCode='" + excCode + '\'' +
                ", language='" + language + '\'' +
                ", company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                ", hotel='" + hotel + '\'' +
                ", adults='" + adults + '\'' +
                ", children='" + children + '\'' +
                ", infants='" + infants + '\'' +
                '}';
    }
}

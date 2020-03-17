package com.zeustech.excursions.http.requests;

import com.zeustech.excursions.models.ExLoginModel;

import androidx.annotation.NonNull;

public class LanguagesRequest {

    private final String company, customer, seller, excCode;

    LanguagesRequest(String company, String customer, String seller, String excCode) {
        this.company = company;
        this.customer = customer;
        this.seller = seller;
        this.excCode = excCode;
    }

    public static LanguagesRequest Create(@NonNull ExLoginModel login, String excCode) {
        return new LanguagesRequest(login.getCompany(), login.getCustomer(), login.getSeller(), excCode);
    }

    @NonNull
    @Override
    public String toString() {
        return "LanguagesRequest{" +
                "company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                ", seller='" + seller + '\'' +
                ", excCode='" + excCode + '\'' +
                '}';
    }
}

package com.zeustech.excursions.http.requests;

import com.zeustech.excursions.models.ExLoginModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

public class ExcursionProgramRequest {

    private final String company, customer, seller, fromDate;
    private final String toDate, language, excCode;

    private ExcursionProgramRequest(String company, String customer, String seller, String fromDate, String toDate, String language, String excCode) {
        this.company = company;
        this.customer = customer;
        this.seller = seller;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.language = language;
        this.excCode = excCode;
    }

    public static ExcursionProgramRequest Create(@NonNull String excCode, @NonNull String firstDate, @NonNull String lastDate,
                                                 @NonNull String language, @NonNull ExLoginModel login) {

        return new ExcursionProgramRequest(login.getCompany(), login.getCustomer(), login.getSeller(),
                firstDate, lastDate, language, excCode);
    }

    @NonNull
    @Override
    public String toString() {
        return "ExcursionProgramRequest{" +
                "company='" + company + '\'' +
                ", customer='" + customer + '\'' +
                ", seller='" + seller + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", language='" + language + '\'' +
                ", excCode='" + excCode + '\'' +
                '}';
    }
}

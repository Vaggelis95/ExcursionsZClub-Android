package com.zeustech.excursions.http.requests;

import com.zeustech.excursions.models.ExCardDetails;
import com.zeustech.excursions.models.ExCustomerInfo;
import com.zeustech.excursions.models.ExLoginModel;
import com.zeustech.excursions.models.ExPriceModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class BookRequest {

    private final List<BookingCart> cart;
    private final ExCardDetails payment;

    private BookRequest(List<BookingCart> cart, ExCardDetails payment) {
        this.cart = cart;
        this.payment = payment;
    }

    public static BookRequest Create(@NonNull ExLoginModel login, @NonNull List<ExPriceModel> basket,
                                     @NonNull ExCustomerInfo customer, @NonNull ExCardDetails details) {
        List<BookingCart> cart = new ArrayList<>();
        for (ExPriceModel item : basket) {
            cart.add(BookingCart.Create(item, login.getSeller(), customer));
        }
        return new BookRequest(cart, details);
    }

    @NonNull
    @Override
    public String toString() {
        return "BookRequest{" +
                "cart=" + cart +
                ", payment=" + payment +
                '}';
    }
}

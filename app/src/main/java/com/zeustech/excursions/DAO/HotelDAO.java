package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.Expression;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.Hotel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HotelDAO extends CouchbaseDAO<Hotel> {

    private final static String HOTEL_CODE = "hotelCode";

    public HotelDAO(@NonNull Context context) {
        super(context, Hotel.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull Hotel model) {
        return Expression.property(HOTEL_CODE).equalTo(Expression.string(model.getHotelCode()));
    }
}

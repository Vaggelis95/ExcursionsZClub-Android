package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.Expression;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.Area;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AreaDAO extends CouchbaseDAO<Area> {

    private final static String AREA_CODE = "areaCode";

    public AreaDAO(@NonNull Context context) {
        super(context, Area.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull Area model) {
        return Expression.property(AREA_CODE).equalTo(Expression.string(model.getAreaCode()));
    }
}

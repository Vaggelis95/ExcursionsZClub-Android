package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.Expression;

import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.ExcursionsModel;

import androidx.annotation.NonNull;

public class ExcursionsDAO extends CouchbaseDAO<ExcursionsModel> {

    private final static String EXC_CODE = "excCode";

    public ExcursionsDAO(@NonNull Context context) {
        super(context, ExcursionsModel.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull ExcursionsModel model) {
        return Expression.property(EXC_CODE).equalTo(Expression.string(model.getCode()));
    }
}

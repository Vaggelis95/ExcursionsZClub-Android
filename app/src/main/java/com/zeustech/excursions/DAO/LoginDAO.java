package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.Expression;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.ExLoginModel;

import androidx.annotation.NonNull;

import static com.zeustech.excursions.database.DatabaseManager.TYPE;

public class LoginDAO extends CouchbaseDAO<ExLoginModel> {

    public LoginDAO(@NonNull Context context) {
        super(context, ExLoginModel.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull ExLoginModel model) {
        return Expression.property(TYPE).equalTo(Expression.string(documentType));
    }
}

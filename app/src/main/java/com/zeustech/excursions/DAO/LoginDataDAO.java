package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Expression;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.LoginData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static com.zeustech.excursions.database.DatabaseManager.TYPE;

public class LoginDataDAO extends CouchbaseDAO<LoginData> {

    public LoginDataDAO(@NonNull Context context) {
        super(context, LoginData.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull LoginData model) {
        return Expression.property(TYPE).equalTo(Expression.string(documentType));
    }

    @Nullable
    public LoginData getLoginData() {
        try {
            List<LoginData> data = getAllDocuments();
            if (data.size() == 1) return data.get(0);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.zeustech.excursions.DAO;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;
import com.zeustech.excursions.database.CouchbaseDAO;
import com.zeustech.excursions.models.CartModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.zeustech.excursions.database.DatabaseManager.ID;
import static com.zeustech.excursions.database.DatabaseManager.TYPE;

public class CartDAO extends CouchbaseDAO<CartModel> {

    private final static String EXC_CODE = "excCode";

    public CartDAO(@NonNull Context context) {
        super(context, CartModel.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull CartModel model) {
        return Expression.property(EXC_CODE).equalTo(Expression.string(model.getCode()));
    }

}

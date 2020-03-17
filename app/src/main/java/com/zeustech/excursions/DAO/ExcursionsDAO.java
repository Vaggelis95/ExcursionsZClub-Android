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
import com.zeustech.excursions.models.ExcursionsModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.zeustech.excursions.database.DatabaseManager.ID;
import static com.zeustech.excursions.database.DatabaseManager.TYPE;

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

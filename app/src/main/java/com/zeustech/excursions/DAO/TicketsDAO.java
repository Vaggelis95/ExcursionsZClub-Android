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
import com.zeustech.excursions.models.ExTicketId;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.zeustech.excursions.database.DatabaseManager.ID;
import static com.zeustech.excursions.database.DatabaseManager.TYPE;

public class TicketsDAO extends CouchbaseDAO<ExTicketId> {

    private final static String INT_NO = "intno";
    private final static String YEAR = "year";

    public TicketsDAO(@NonNull Context context) {
        super(context, ExTicketId.class);
    }

    @NonNull
    @Override
    protected Expression getExpressionId(@NonNull ExTicketId model) {
        return Expression.property(YEAR).equalTo(Expression.intValue(model.getYear()))
                .and(Expression.property(INT_NO).equalTo(Expression.intValue(model.getIntNo())));
    }
}

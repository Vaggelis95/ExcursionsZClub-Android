package com.zeustech.excursions.database;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseManager {

    private final String TAG = getClass().getSimpleName();
    private final String DB_NAME = "excursions_db";
    public final static String TYPE = "type";
    public final static String ID = "id";

    private static volatile DatabaseManager INSTANCE = null;
    private final Database database; // Database Instance

    public synchronized static DatabaseManager getInstance(Context context) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new DatabaseManager(context.getApplicationContext());
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    private DatabaseManager(Context context) throws CouchbaseLiteException {
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        database = new Database(DB_NAME, config);
        printAllDocuments();
    }

    private void printAllDocuments() throws CouchbaseLiteException {
        Query query = QueryBuilder
                .select(SelectResult.expression(Meta.id),
                        SelectResult.all())
                .from(DataSource.database(database));
        for (Result doc : query.execute().allResults()) {
            Log.i(TAG, "printAllDocuments: " + doc.toMap().toString());
        }
    }

    public Database getDatabase() {
        return database;
    }

    @Nullable
    public Document getDocumentWith(String id) {
        return database.getDocument(id);
    }

    public void saveDocument(@NonNull MutableDocument doc) throws CouchbaseLiteException {
        database.save(doc);
    }

    void deleteDocument(@NonNull String docId) throws CouchbaseLiteException {
        Document doc = getDocumentWith(docId);
        if (doc == null) return;
        database.delete(doc);
    }

    public void eraseDatabaseExcept(@NonNull List<String> types) throws CouchbaseLiteException {
        Query query = QueryBuilder
                .select(SelectResult.expression(Meta.id))
                .from(DataSource.database(database));
        database.inBatch(() -> {
            try {
                for (Result doc : query.execute().allResults()) {
                    if (doc.contains(ID)) {
                        Document document = database.getDocument(doc.getString(ID));
                        String type = document.getString(TYPE);
                        if (type != null && types.contains(type)) continue;
                        database.delete(document);
                    }
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        });
    }

    public void eraseDatabase() throws CouchbaseLiteException {
        eraseDatabaseExcept(new ArrayList<>());
    }
}

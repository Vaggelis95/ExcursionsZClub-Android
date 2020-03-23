package com.zeustech.excursions.database;

import android.content.Context;
import android.os.AsyncTask;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.SelectResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.zeustech.excursions.database.DatabaseManager.ID;
import static com.zeustech.excursions.database.DatabaseManager.TYPE;

public abstract class CouchbaseDAO<T> {

    private final String TAG = getClass().getSimpleName();

    public final DatabaseManager dbManager;
    private final Query getAllDocsQuery;
    private ListenerToken getAllDocsToken;
    private final Gson gson;

    public final String documentType;
    private final Class<T> mClass;

    public interface ChangeListener<T> {
        void onChange(@NonNull List<T> change);
    }

    public enum Operation {
        SAVE,
        UPDATE,
        DELETE,
        REPLACE
    }

    public CouchbaseDAO(@NonNull Context context, @NonNull Class<T> mClass) {
        dbManager = DatabaseManager.getInstance(context);
        this.mClass = mClass;
        this.documentType = mClass.getSimpleName();
        gson = new Gson();
        // ExecutorService executorService = Executors.newSingleThreadExecutor();
        if (dbManager.getDatabase() == null) {
            getAllDocsQuery = null;
        } else {
            getAllDocsQuery = QueryBuilder.select(SelectResult.expression(Meta.id), SelectResult.all())
                    .from(DataSource.database(dbManager.getDatabase()))
                    .where(Expression.property(TYPE).equalTo(Expression.string(documentType)));
        }
    }

    /**Listen about changes of Documents of the provided Type*/
    public void addChangeListener(ChangeListener<T> changeListener) {
        if (getAllDocsToken != null) return;
        // SERIAL_EXECUTOR : An Executor that executes tasks one at a time in serial order. This serialization is global to a particular process.
        // THREAD_POOL_EXECUTOR : An Executor that can be used to execute tasks in parallel.
        getAllDocsToken = getAllDocsQuery.addChangeListener(AsyncTask.SERIAL_EXECUTOR, change ->
                changeListener.onChange(resultToModels(change.getResults().allResults())));
    }

    /**Stop Listen about changes of Documents of the provided Type*/
    public void removeChangeListener() {
        if (getAllDocsToken != null && getAllDocsQuery != null) {
            getAllDocsQuery.removeChangeListener(getAllDocsToken);
        }
        getAllDocsToken = null;
    }

    /**Get all documents of type which is mentioned inside the constructor*/
    @NonNull
    public List<T> getAllDocuments() throws CouchbaseLiteException {
        List<T> repo = new ArrayList<>();
        if (getAllDocsQuery != null) {
            repo.addAll(resultToModels(getAllDocsQuery.execute().allResults()));
        }
        return repo;
    }

    /**Saves Document inside database, this function converts Objects to CouchbaseLite Documents and
     * then checks if the specified document exists via getDocumentID function. If it is not,
     * a simple save takes place, otherwise the old document replaced.*/
    public void updateDocument(@NonNull T model) throws CouchbaseLiteException {
        String docId = getDocumentId(model);
        if (docId == null) {
            saveDocument(model);
        } else {
            Document document = dbManager.getDocumentWith(docId);
            if (document == null) return;
            MutableDocument mutableDoc = document.toMutable();
            Map<String, Object> map = modelToMap(model);
            if (map == null) return;
            dbManager.saveDocument(mutableDoc.setData(map));
        }
    }

    @NonNull
    protected abstract Expression getExpressionId(@NonNull T model);

    @Nullable
    private String getDocumentId(@NonNull T model) throws CouchbaseLiteException {
        Database db = dbManager.getDatabase();
        if (db != null) {
            Query query = QueryBuilder
                    .select(SelectResult.expression(Meta.id))
                    .from(DataSource.database(db))
                    .where(Expression.property(TYPE).equalTo(Expression.string(documentType))
                            .and(getExpressionId(model)));
            for (Result doc : query.execute().allResults()) {
                if (doc.contains(ID)) return doc.getString(ID);
            }
        }
        return null;
    }

    /*    public void updateDocumets2(@NonNull List<T> list) throws CouchbaseLiteException {
        for(T model : list) {
            model.get
        }
        Expression[] values = new Expression[] {
                Expression.property("first"),
                Expression.property("last"),
                Expression.property("username")
        };
        Database db = dbManager.getDatabase();
        if (db != null) {
            Query query = QueryBuilder
                    .select(SelectResult.expression(Meta.id))
                    .from(DataSource.database(db))
                    .where(Expression.property(TYPE).equalTo(Expression.string(documentType))
                            .and(Expression.string("categoryCode").in(values)));
            for (Result doc : query.execute().allResults()) {
                if (doc.contains(ID)) return doc.getString(ID);
            }
        }
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database))
                .where(Expression.string("Armani").in(values));
    }*/

    public void updateDocuments(@NonNull List<T> list) throws CouchbaseLiteException {
        for (T model : list) {
            updateDocument(model);
        }
    }

    public void saveDocument(@NonNull T model) throws CouchbaseLiteException {
        MutableDocument mutableDoc = modelToDocument(model);
        if (mutableDoc == null) return;
        dbManager.saveDocument(mutableDoc);
    }

    private void saveDocuments(@NonNull List<T> list) throws CouchbaseLiteException {
        for (T model : list) {
            saveDocument(model);
        }
    }

    public void deleteDocument(@NonNull T model) throws CouchbaseLiteException {
        String docId = getDocumentId(model);
        if (docId == null) return;
        dbManager.deleteDocument(docId);
    }

    public void deleteDocuments(@NonNull List<T> list) throws CouchbaseLiteException {
        for (T model : list) {
            deleteDocument(model);
        }
    }

    public void deleteAllDocs() throws CouchbaseLiteException {
        List<Result> results = getAllDocsQuery.execute().allResults();
        for (Result result : results) { // #1 Delete All
            String id = result.getString(ID);
            if (id == null) continue;
            Document doc = dbManager.getDatabase().getDocument(id);
            if (doc == null) continue;
            dbManager.getDatabase().delete(doc);
        }
    }

    public void batchOperation(@NonNull Operation operation, @NonNull List<T> list) throws CouchbaseLiteException {
        dbManager.getDatabase().inBatch(() -> {
            try {
                switch (operation) {
                    case SAVE : {
                        saveDocuments(list);
                        break;
                    }
                    case UPDATE : {
                        updateDocuments(list);
                        break;
                    }
                    case DELETE : {
                        deleteDocuments(list);
                        break;
                    }
                    case REPLACE : {
                        deleteAllDocs();
                        updateDocuments(list);
                        break;
                    }
                    default : break;
                }
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        });
    }

    public void replaceAll(@NonNull T model) throws CouchbaseLiteException {
        deleteAllDocs();
        saveDocument(model);
    }

    public void replaceAll(@NonNull List<T> list) throws CouchbaseLiteException {
        deleteAllDocs();
        dbManager.getDatabase().inBatch(() -> {
            try {
                saveDocuments(list);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        });
    }

    /*Tools Start*/

    @NonNull
    public List<T> resultToModels(@NonNull List<Result> results) {
        List<T> repo = new ArrayList<>();
        Database db = dbManager.getDatabase();
        if (db != null) {
            for (Result doc : results) {
                if (doc.getString(ID) == null) continue;
                Document document = db.getDocument(doc.getString(ID));
                if (document == null) continue;
                T model = documentToModel(document);
                if (model != null) {
                    repo.add(model);
                }
            }
        }
        return repo;
    }

    @Nullable
    public MutableDocument modelToDocument(@NonNull T model) {
        Map<String, Object> map = modelToMap(model);
        if (map != null) {
            return new MutableDocument().setData(map);
        }
        return null;
    }

    @Nullable
    public Map<String, Object> modelToMap(@NonNull T model) {
        try {
            String json = gson.toJson(model);
            Map<String, Object> map = gson.fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
            map.put(TYPE, documentType);
            return map;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public T documentToModel(@NonNull Document document) {
        try {
            String json = gson.toJson(document.toMap());
            return gson.fromJson(json, mClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*Tools End*/
}
package com.example.notepad.bullsandcows.data.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.data.databases.DBConnector;
import com.example.notepad.bullsandcows.data.databases.DBOperations;
import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import java.util.Arrays;
import java.util.HashSet;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class RecordsContentProvider extends ContentProvider {

    private static final String AUTHORITY_PATH = "com.example.notepad.bullsandcows.data.providers";
    private static final String BASE_PATH = "records";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_PATH + "/" + BASE_PATH);
//    public static final String BASE_DIRECT_CONTENT_PATH =
//            ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
//    public static final String ITEM_CONTENT_PATH = ContentResolver.CURSOR_ITEM_BASE_TYPE;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int RECORDS = 1;

    public static final int SINGL_RECORD = 2;

    static {
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH, RECORDS);
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH + "/#", SINGL_RECORD);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: ContentProvider");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "Start query for getting of cursor");
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumnProjection(projection);

        if (sortOrder == null) {
            sortOrder = UserRecordsDB.ID + Tables.ASC;
        }

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                queryBuilder.setTables(UserRecordsDB.TABLE);
                Log.d(TAG, "Records case");
                DBOperations dbOperations = new DBOperations();

                Cursor cursor = dbOperations.query(UserRecordsDB.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case SINGL_RECORD:
                queryBuilder.setTables(UserRecordsDB.TABLE);
                queryBuilder.appendWhere(UserRecordsDB.ID + " = " + uri.getLastPathSegment());
                SQLiteDatabase db = DBConnector.getInstance().getReadableDatabase();

                return queryBuilder.query(db, projection, selection, selectionArgs, null,
                        null, sortOrder);
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }
    }

    private void checkColumnProjection(String[] projection) {
        String[] available = UserRecordsDB.AVAILABLE_COLUMNS;

        if (projection != null) {
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Not equals column in projection");
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        DBOperations dbOperations = new DBOperations();
        long id;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                id = dbOperations.insert(UserRecordsDB.TABLE, values);
                Log.d(TAG, "insert: record with id " + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = DBConnector.getInstance().getReadableDatabase();
        Log.d(TAG, "bulkInsert in content provider start");
        DBOperations dbOperations = new DBOperations();
        boolean isNew = false;
        int successAdd = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                successAdd = dbOperations.bulkInsert(UserRecordsDB.TABLE, values);
                if (successAdd > 0) {
                    isNew = true;
                }
                break;
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }

        if (isNew) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return successAdd;
    }
}

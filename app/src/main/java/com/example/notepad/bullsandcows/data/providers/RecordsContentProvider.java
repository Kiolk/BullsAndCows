package com.example.notepad.bullsandcows.data.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import java.util.Arrays;
import java.util.HashSet;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

public class RecordsContentProvider extends ContentProvider {

    private static final String AUTHORITY_PATH = "com.example.notepad.bullsandcows.data.providers";
    private static final String BASE_PATH = "records";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_PATH + "/" + BASE_PATH);
    public static final String BASE_DIRECT_CONTENT_PATH =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
    public static final String ITEM_CONTENT_PATH = ContentResolver.CURSOR_ITEM_BASE_TYPE;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int RECORDS = 1;

    public static final int SINGL_RECORD = 2;

    static {
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH, RECORDS);
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH + "/#", SINGL_RECORD);
    }

    private DBConnector dataBase;

    @Override
    public boolean onCreate() {
        dataBase = DBConnector.getInstance();
        Log.d(TAG, "onCreate: ContentProvider");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "Start query for getting of cursor");
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumnProjection(projection);

        queryBuilder.setTables(UserRecordsDB.TABLE);

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                Log.d(TAG, "Records case");
                break;
            case SINGL_RECORD:
                queryBuilder.appendWhere(UserRecordsDB.ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }

        SQLiteDatabase db = DBConnector.getInstance().getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, UserRecordsDB.ID + Tables.ASC);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        Log.d(TAG, "Cursor: " + cursor);
        return cursor;
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
        Log.d(TAG, "insert: " + values.toString());
        SQLiteDatabase db = dataBase.getReadableDatabase();
        db.beginTransaction();
        int uriType = URI_MATCHER.match(uri);
        long id = 0;
        try {
            switch (uriType) {
                case RECORDS:
                    id = db.insert(UserRecordsDB.TABLE, null, values);
                    Log.d(TAG, "insert: record with id " + id);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Uri: " + uri);
            }
            db.setTransactionSuccessful();
        } catch (Exception pE) {
            pE.getStackTrace();
        } finally {
            db.endTransaction();
            db.close();
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
        Log.d(TAG, "bulkInsert: start");
        boolean isNew = false;
        int successAdd = 0;
        for (ContentValues contentValues : values) {
            db.beginTransaction();

            try {
                long id = db.insert(UserRecordsDB.TABLE, null, contentValues);
                Log.d(TAG, "bulkInsert: id" + id);
                if(id != -1){
                    isNew = true;
                }
                db.setTransactionSuccessful();
                ++successAdd;
                Log.d(TAG, "bulkInsert: success update");
            } catch (Exception pE) {
                pE.getStackTrace();
                Log.d(TAG, "bulkInsert: " + pE.getMessage());
            } finally {
                db.endTransaction();
            }
        }
        db.close();
        if(isNew) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "Update notifyChange for cursors");
        Log.d(TAG, "bulkInsert, success add:" + successAdd);
        return successAdd;
    }
}

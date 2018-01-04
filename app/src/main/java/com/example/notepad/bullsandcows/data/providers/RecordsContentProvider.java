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
import com.example.notepad.bullsandcows.data.databases.DBOperationsSingleTone;
import com.example.notepad.bullsandcows.data.databases.Tables;
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import java.util.Arrays;
import java.util.HashSet;

import static com.example.notepad.bullsandcows.utils.Constants.TAG;

//TODO split logic i
public class RecordsContentProvider extends ContentProvider {

    private static final String AUTHORITY_PATH = "com.example.notepad.bullsandcows.data.providers";
    private static final String BASE_PATH = "records";
    private static final String USERS_PATH = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY_PATH + "/" + BASE_PATH);
    public static final Uri CONTENT_USERS_URI = Uri.parse("content://" + AUTHORITY_PATH + "/" + USERS_PATH);
//    public static final String BASE_DIRECT_CONTENT_PATH =
//            ContentResolver.CURSOR_DIR_BASE_TYPE + "/records";
//    public static final String ITEM_CONTENT_PATH = ContentResolver.CURSOR_ITEM_BASE_TYPE;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int RECORDS = 1;

    public static final int SINGL_RECORD = 2;

    public static final int USERS = 3;

    public static final int SINGLE_USER = 4;

    static {
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH, RECORDS);
        URI_MATCHER.addURI(AUTHORITY_PATH, BASE_PATH + "/#", SINGL_RECORD);
        URI_MATCHER.addURI(AUTHORITY_PATH, USERS_PATH, USERS);
        URI_MATCHER.addURI(AUTHORITY_PATH, USERS_PATH + "/#", SINGLE_USER);
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
//                DBOperations dbOperations;
                queryBuilder.setTables(UserRecordsDB.TABLE);
                Log.d(TAG, "Records case");
//                dbOperations = new DBOperations();

//                Cursor cursor = dbOperations.query(UserRecordsDB.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                //TODO create one
                Cursor cursor = DBOperationsSingleTone.getInstance().query(UserRecordsDB.TABLE, projection, selection, selectionArgs, null, null, sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case SINGL_RECORD:
                queryBuilder.setTables(UserRecordsDB.TABLE);
                queryBuilder.appendWhere(UserRecordsDB.ID + " = " + uri.getLastPathSegment());
                SQLiteDatabase db = DBConnector.getInstance().getReadableDatabase();

                return queryBuilder.query(db, projection, selection, selectionArgs, null,
                        null, sortOrder);
            case USERS:
//                DBOperations dbOperation;
//                dbOperation = new DBOperations();
//                return dbOperation.query(UserInfoDB.TABLE, null, selection, selectionArgs, null, null, null);
                return DBOperationsSingleTone.getInstance().query(UserInfoDB.TABLE, null, selection, selectionArgs, null, null, null);
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
//        DBOperations dbOperations = new DBOperations();
        long id;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
//                id = dbOperations.insert(UserRecordsDB.TABLE, values);
                id = DBOperationsSingleTone.getInstance().insert(UserRecordsDB.TABLE, values);
                Log.d(TAG, "insert: record with id " + id);
                break;
            case USERS:
//                id = dbOperations.insert(UserInfoDB.TABLE, values);
                id = DBOperationsSingleTone.getInstance().insert(UserInfoDB.TABLE, values);
                Log.d(TAG, "insert: user info with id" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(uri.getLastPathSegment() + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
//        DBOperations dbOperations = new DBOperations();
        int result = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
//                result = dbOperations.update(UserRecordsDB.TABLE, values);
                result = DBOperationsSingleTone.getInstance().update(UserRecordsDB.TABLE, values);
                break;
            case USERS:
//                result = dbOperations.update(UserInfoDB.TABLE, values);
                result = DBOperationsSingleTone.getInstance().update(UserInfoDB.TABLE, values);
                break;
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        Log.d(TAG, "bulkInsert in content provider start");
//        DBOperations dbOperations = new DBOperations();
        boolean isNew = false;
        int successAdd = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                successAdd = DBOperationsSingleTone.getInstance().bulkInsert(UserRecordsDB.TABLE, values);
//                successAdd = dbOperations.bulkInsert(UserRecordsDB.TABLE, values);
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

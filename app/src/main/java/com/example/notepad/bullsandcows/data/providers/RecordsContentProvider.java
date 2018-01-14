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
import com.example.notepad.bullsandcows.data.databases.models.UserInfoDB;
import com.example.notepad.bullsandcows.data.databases.models.UserRecordsDB;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.example.notepad.bullsandcows.utils.Constants.DBConstants.ASC;
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
    public Cursor query(@NonNull final Uri uri, @Nullable final String[] projection, @Nullable final String selection, @Nullable final String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "Start query for getting of cursor");
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumnProjection(projection);

        if (sortOrder == null) {
            sortOrder = UserRecordsDB.ID + ASC;
        }

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {

            case RECORDS:
//                DBOperations dbOperations;
                queryBuilder.setTables(UserRecordsDB.TABLE);
                Log.d(TAG, "Records case");
//                dbOperations = new DBOperations();

//                Cursor cursor = dbOperations.query(UserRecordsDB.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                //TODO create one
                final Cursor cursor = DBOperations.getInstance().query(UserRecordsDB.TABLE, projection, selection, selectionArgs, null, null, sortOrder);

                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case SINGL_RECORD:
                queryBuilder.setTables(UserRecordsDB.TABLE);
                queryBuilder.appendWhere(UserRecordsDB.ID + " = " + uri.getLastPathSegment());
                final SQLiteDatabase db = DBConnector.getInstance().getReadableDatabase();

                return queryBuilder.query(db, projection, selection, selectionArgs, null,
                        null, sortOrder);
            case USERS:
//                DBOperations dbOperation;
//                dbOperation = new DBOperations();
//                return dbOperation.query(UserInfoDB.TABLE, null, selection, selectionArgs, null, null, null);
                return DBOperations.getInstance().query(UserInfoDB.TABLE, null, selection, selectionArgs, null, null, null);
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }
    }

    private void checkColumnProjection(final String[] projection) {
        final String[] available = UserRecordsDB.AVAILABLE_COLUMNS;

        if (projection != null) {
            final Collection<String> availableColumns = new HashSet<>(Arrays.asList(available));
            final Collection<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Not equals column in projection");
            }
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull final Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull final Uri uri, @Nullable final ContentValues values) {
//        DBOperations dbOperations = new DBOperations();
        final long id;

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
//                id = dbOperations.insert(UserRecordsDB.TABLE, values);
                id = DBOperations.getInstance().insert(UserRecordsDB.TABLE, values);
                Log.d(TAG, "insert: record with id " + id);
                break;
            case USERS:
//                id = dbOperations.insert(UserInfoDB.TABLE, values);
                id = DBOperations.getInstance().insert(UserInfoDB.TABLE, values);
                Log.d(TAG, "insert: user info with id" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(uri.getLastPathSegment() + "/" + id);
    }

    @Override
    public int delete(@NonNull final Uri uri, @Nullable final String selection, @Nullable final String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull final Uri uri, @Nullable final ContentValues values, @Nullable final String selection, @Nullable final String[] selectionArgs) {
//        DBOperations dbOperations = new DBOperations();
        int result = 0;

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
//                result = dbOperations.update(UserRecordsDB.TABLE, values);
                result = DBOperations.getInstance().update(UserRecordsDB.TABLE, values);
                break;
            case USERS:
//                result = dbOperations.update(UserInfoDB.TABLE, values);
                result = DBOperations.getInstance().update(UserInfoDB.TABLE, values);
                break;
            default:
                throw new IllegalArgumentException("Not correct Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int bulkInsert(@NonNull final Uri uri, @NonNull final ContentValues[] values) {
        Log.d(TAG, "bulkInsert in content provider start");
//        DBOperations dbOperations = new DBOperations();
        boolean isNew = false;
        int successAdd = 0;

        final int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case RECORDS:
                successAdd = DBOperations.getInstance().bulkInsert(UserRecordsDB.TABLE, values);
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

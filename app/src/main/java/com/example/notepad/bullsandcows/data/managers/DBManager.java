package com.example.notepad.bullsandcows.data.managers;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
@Deprecated
public class DBManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private static DBManager mDBManager;

    private CursorDBCallbacks mCallback;

    private LoaderManager mLoaderManager;

    private DBManager() {

    }

    public DBManager getInstance() {
        if (mDBManager == null) {
            mDBManager = new DBManager();
        }
        return mDBManager;
    }

    public void getCursorFromDB(LoaderManager pLoaderManager, CursorDBCallbacks pCallback) {
        mLoaderManager = pLoaderManager;
        mCallback = pCallback;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        mCallback.getCursorCallback(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

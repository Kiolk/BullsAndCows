package com.example.notepad.bullsandcows.data.Loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.example.notepad.bullsandcows.data.databases.DBOperations;

public class CursorDBLoader extends CursorLoader {

    private DBOperations mDBOperations;

    public CursorDBLoader(Context pContext) {
        super(pContext);
        mDBOperations = new DBOperations();
    }

    @Override
    public Cursor loadInBackground() {
        return mDBOperations.query();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
}

package com.example.notepad.bullsandcows.data.Loaders;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.example.notepad.bullsandcows.data.databases.DBOperations;

public class CursorDBLoader extends CursorLoader {

    private DBOperations mDBOperations;

    private String[] mRequest;

    public CursorDBLoader(Context pContext) {
        super(pContext);
        mDBOperations = new DBOperations();
    }

    public CursorDBLoader(Context pContext, String[] pRequest) {
        super(pContext);
        mDBOperations = new DBOperations();
        mRequest = pRequest;

    }


    @Override
    public Cursor loadInBackground() {
        if (mRequest != null) {
            return mDBOperations.queryForSortRecords(mRequest);
        } else {
            return mDBOperations.query();
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
}

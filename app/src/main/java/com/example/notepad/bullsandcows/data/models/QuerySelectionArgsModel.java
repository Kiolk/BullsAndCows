package com.example.notepad.bullsandcows.data.models;

public class QuerySelectionArgsModel {

    private final String mSelection;

    private final String[] mSelectionArgs;

    public QuerySelectionArgsModel(final String pSelection, final String[] pSelectionArgs) {
        mSelection = pSelection;
        mSelectionArgs = pSelectionArgs;
    }

    public String getSelection() {
        return mSelection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }
}

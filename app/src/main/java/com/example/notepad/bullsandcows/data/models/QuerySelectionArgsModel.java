package com.example.notepad.bullsandcows.data.models;

public class QuerySelectionArgsModel {

    private String mSelection;

    private String mSelectionArgs[];

    public QuerySelectionArgsModel(String mSelection, String[] mSelectionArgs) {
        this.mSelection = mSelection;
        this.mSelectionArgs = mSelectionArgs;
    }

    public String getSelection() {
        return mSelection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }
}

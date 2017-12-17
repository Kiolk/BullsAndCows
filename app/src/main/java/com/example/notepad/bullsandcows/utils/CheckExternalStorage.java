package com.example.notepad.bullsandcows.utils;

import android.os.Environment;

public class CheckExternalStorage {

    public boolean isExternalStoragePresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}

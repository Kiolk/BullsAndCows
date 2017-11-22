package com.example.notepad.bullsandcows.Utils;

import android.os.Environment;

public class CheckExternalStorage {

    public boolean isExternalStoragePresent() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

}

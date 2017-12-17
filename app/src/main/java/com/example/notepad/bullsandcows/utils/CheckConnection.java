package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckConnection {

    public static boolean checkConnection(Context pContext) {
        ConnectivityManager check = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return check != null && check.getActiveNetworkInfo() != null && check.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

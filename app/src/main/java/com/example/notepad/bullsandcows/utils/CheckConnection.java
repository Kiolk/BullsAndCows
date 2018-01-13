package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class CheckConnection {

    public static boolean checkConnection(final Context pContext) {
        final ConnectivityManager check = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return check != null && check.getActiveNetworkInfo() != null && check.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtils implements CheckInternetConnection {

    @Override
    public boolean checkConnection(Context pContext) {
        ConnectivityManager check = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean result = check.getActiveNetworkInfo() != null && check.getActiveNetworkInfo().isConnectedOrConnecting();
        return result;
    }

    @Override
    public void connect() {

    }
}

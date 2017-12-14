package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;


import com.example.notepad.bullsandcows.R;

public class CheckConnection  {

    public static boolean checkConnection(Context pContext) {
        ConnectivityManager check = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return check != null && check.getActiveNetworkInfo() != null && check.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void showToastAboutDisconnection(Context pContext){
        String massage = pContext.getResources().getString(R.string.NOT_CONNECT_TO_INTERNET);
        Toast.makeText(pContext, massage, Toast.LENGTH_SHORT).show();
    }
}

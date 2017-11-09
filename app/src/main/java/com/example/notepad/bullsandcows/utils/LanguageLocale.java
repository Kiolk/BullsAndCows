package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageLocale {

    public static void setLocale(String pLanguage, Context pContext) {
        if(pLanguage.equalsIgnoreCase("")){
            return;
        }
        Locale mLocale = new Locale(pLanguage);
//        saveLocale(pLanguage);
        Locale.setDefault(mLocale);
        Configuration configuration = new Configuration();
        configuration.setLocale(mLocale);
        pContext.getResources().updateConfiguration(configuration, pContext.getResources().getDisplayMetrics());
//        updateText();
    }
}

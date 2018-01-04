package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.graphics.Typeface;

//TODO google provides solution for custom fonts
public class CustomFonts {

    public static final int ANNABELLE = 0;
    public static final int AASSUANBRK = 1;
    public static final int BLACKGROTESKC = 2;
    public static final int DIGITAL_FONT = 3;

    private static final int NUM_OF_CUSTOM_FONTS = 4;

    private static boolean fontLoadead = false;

    private static Typeface[] fonts = new Typeface[NUM_OF_CUSTOM_FONTS];

    private static String[] fontPath = {
            "fonts/annabelle.ttf",
            "fonts/aassuanbrk.ttf",
            "fonts/BlackGroteskC.otf",
            "fonts/digital-7.ttf"
    };

    public static Typeface getTypeFace(Context pContext, int fontIdentifier) {
        if (!fontLoadead) {
            loadFonts(pContext);
        }
        return fonts[fontIdentifier];
    }

    private static void loadFonts(Context pContext) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; ++i) {
            fonts[i] = Typeface.createFromAsset(pContext.getAssets(), fontPath[i]);
        }
        fontLoadead = true;
    }
}

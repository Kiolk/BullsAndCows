package com.example.notepad.bullsandcows.Utils;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFonts {

    public static final int ANNABELLE = 0;
    public static final int AASSUANBRK = 1;
    public static final int BLACKGROTESKC = 2;

    private static final int NUM_OF_CUSTOM_FONTS = 3;

    private static boolean fontLoadead = false;

    private static Typeface[] fonts = new Typeface[3];

    private static String[] fontPath = {
            "fonts/annabelle.ttf",
            "fonts/aassuanbrk.ttf",
            "fonts/BlackGroteskC.otf"
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

package com.example.notepad.bullsandcows.utils.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.notepad.bullsandcows.R;

public final class SlideAnimationUtil {

    public static void slideInFromLeft(final Context pContext, final View pView){
        runSimpleAnimation(pContext, pView, R.anim.slide_from_left);
    }

    public static void slideOutToLeft(final Context pContext, final View pView){
        runSimpleAnimation(pContext, pView, R.anim.slide_to_left);
    }

    public static void slideInFromRignt(final Context pContext, final View pView){
        runSimpleAnimation(pContext, pView, R.anim.slide_from_right);
    }

    public static void slideOutToRight(final Context pContext, final View pView){
        runSimpleAnimation(pContext, pView, R.anim.slide_to_right);
    }

    private static void runSimpleAnimation(final Context pContext, final View pView, final int pAnimResources) {
        pView.startAnimation(AnimationUtils.loadAnimation(pContext, pAnimResources));
    }
}

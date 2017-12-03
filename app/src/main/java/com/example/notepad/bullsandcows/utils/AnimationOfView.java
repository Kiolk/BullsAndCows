package com.example.notepad.bullsandcows.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewAnimationUtils;

public class AnimationOfView {

    @TargetApi(21)
    public void enteredView(View pView) {
        int cx = (pView.getLeft() + pView.getRight()) / 2;
        int cy = (pView.getBottom() + pView.getTop()) / 2;
        int finalRadius = Math.max(pView.getWidth(), pView.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(pView, cx, cy, 0, finalRadius);
        anim.setDuration(2000);
        anim.start();
    }

}

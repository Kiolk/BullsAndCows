package com.example.notepad.bullsandcows.utils.animation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.notepad.bullsandcows.R;

public final class SlideAnimationUtil {

    public interface SlideAnimationListener {

        void animationEnd();
    }

    public static final long FASTER = 500;

    public static final long SLOWLY = 1500;

    public static final long NORMAL = 1000;

    public static void slideInFromLeft(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_left, pSlideAnimationListener, pDuration);
    }

    public static void slideOutToLeft(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_left, pSlideAnimationListener, pDuration);
    }

    public static void slideInFromRight(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_right, pSlideAnimationListener, pDuration);
    }

    public static void slideOutToRight(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_right, pSlideAnimationListener, pDuration);
    }

    public static void slideInToTop(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_to_top, pSlideAnimationListener, pDuration);
    }

    public static void slideOutToTop(final Context pContext, final View pView, @Nullable final SlideAnimationListener pSlideAnimationListener, final long pDuration) {
        runSimpleAnimation(pContext, pView, R.anim.slide_from_top, pSlideAnimationListener, pDuration);
    }

    private static void runSimpleAnimation(final Context pContext, final View pView,
                                           final int pAnimResources,
                                           @Nullable final SlideAnimationListener pListener,
                                           final long pDuration) {
        final Animation animation = AnimationUtils.loadAnimation(pContext, pAnimResources);

        if (pListener != null) {
            final Animation.AnimationListener animationListener = new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(final Animation pAnimation) {
                }

                @Override
                public void onAnimationEnd(final Animation pAnimation) {
                    pListener.animationEnd();
                }

                @Override
                public void onAnimationRepeat(final Animation pAnimation) {
                }
            };
            animation.setAnimationListener(animationListener);
        }

        animation.setDuration(pDuration);
        pView.startAnimation(animation);
    }
}

package kiolk.com.github.pen.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

public class PlaceHolderUtil {

    private static PlaceHolderUtil mPlaceHolder;

    private Drawable mDefaultDrawable;

    private Drawable mErrorDrawable;

    private PlaceHolderUtil() {
    }

    public static PlaceHolderUtil getInstance() {
        if (mPlaceHolder == null) {
            mPlaceHolder = new PlaceHolderUtil();
        }

        return mPlaceHolder;
    }

    public Drawable getDefaultDrawable() {
        return mDefaultDrawable;
    }

    public void setDefaultDrawable(Drawable mDefaultDrawable) {
        this.mDefaultDrawable = mDefaultDrawable;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public void setErrorDrawable(Drawable mErrorDrawable) {
        this.mErrorDrawable = mErrorDrawable;
    }
}

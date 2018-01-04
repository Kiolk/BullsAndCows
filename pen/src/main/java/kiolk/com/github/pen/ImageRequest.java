package kiolk.com.github.pen;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class ImageRequest {

    private String mUrl;
    private WeakReference<ImageView> mTarget;
    private int mWidth;
    private int mHeight;
    private GetBitmapCallback mBitmapCallback;

    ImageRequest(String mUrl, WeakReference<ImageView> mTarget) {
        this.mUrl = mUrl;
        this.mTarget = mTarget;
    }

    ImageRequest(String mUrl, GetBitmapCallback mBitmapCallback) {
        this.mUrl = mUrl;
        this.mBitmapCallback = mBitmapCallback;
    }


    //TODO rename all methods 'get"m"'
    String getmUrl() {
        return mUrl;
    }

    void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    WeakReference<ImageView> getmTarget() {
        return mTarget;
    }

    void setTarget(WeakReference<ImageView> mTarget) {
        this.mTarget = mTarget;
    }

    int getmWidth() {
        return mWidth;
    }

    void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    int getmHeight() {
        return mHeight;
    }

    void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    GetBitmapCallback getmBitmapCallback() {
        return mBitmapCallback;
    }

    public void setmBitmapCallback(GetBitmapCallback mBitmapCallback) {
        this.mBitmapCallback = mBitmapCallback;
    }
}
package kiolk.com.github.pen;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class ImageRequest {

    private String mUrl;
    private WeakReference<ImageView> mTarget;
    private int mWidth;
    private int mHeight;
    private GetBitmapCallback mBitmapCallback;

    public ImageRequest(String mUrl, WeakReference<ImageView> mTarget) {
        this.mUrl = mUrl;
        this.mTarget = mTarget;
    }

    public ImageRequest(String mUrl, GetBitmapCallback mBitmapCallback) {
        this.mUrl = mUrl;
        this.mBitmapCallback = mBitmapCallback;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public WeakReference<ImageView> getmTarget() {
        return mTarget;
    }

    public void setmTarget(WeakReference<ImageView> mTarget) {
        this.mTarget = mTarget;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public GetBitmapCallback getmBitmapCallback() {
        return mBitmapCallback;
    }

    public void setmBitmapCallback(GetBitmapCallback mBitmapCallback) {
        this.mBitmapCallback = mBitmapCallback;
    }
}
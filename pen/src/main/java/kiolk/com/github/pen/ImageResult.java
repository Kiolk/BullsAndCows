package kiolk.com.github.pen;

import android.graphics.Bitmap;

public class ImageResult {

    private ImageRequest mRequest;
    private Bitmap mBitmap;
    private Exception mException;

    ImageResult(ImageRequest request) {
        this.mRequest = request;
    }

    ImageRequest getmRequest() {
        return mRequest;
    }

    public void setRequest(ImageRequest mRequest) {
        this.mRequest = mRequest;
    }

    Bitmap getmBitmap() {
        return mBitmap;
    }

    void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    Exception getmException() {
        return mException;
    }

    void setmException(Exception mException) {
        this.mException = mException;
    }
}

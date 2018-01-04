package kiolk.com.github.pen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import kiolk.com.github.pen.utils.ConstantsUtil;
import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;
import kiolk.com.github.pen.utils.PlaceHolderUtil;

public class Pen {

    //TODO move to enums
    public static final int WITHOUT_CACHE = 0;
    public static final int MEMORY_CACHE = 1;
    public static final int INNER_FILE_CACHE = 2;
    public static final int SAVE_SCALING_IMAGE_STRATEGY = 0;
    public static final int SAVE_FULL_IMAGE_STRATEGY = 1;
    public static final int MIN_COMPRESSION = 0;
    public static final int MAX_COMPRESSION = 100;
    public static final int DEFAULT_COMPRESSION = 80;

    //TODO What style are used for static fields?
    static File CACHE_DIR;
    static int QUALITY_OF_COMPRESSION_BMP;
    //TODO rename, why it's static?
    private static int mTypeOfMemoryCache;

    private static Pen instance = null;

    private BlockingDeque<ImageRequest> mQueue;
    private ExecutorService mExecutor;
    private LruCache<String, Bitmap> mBitmapLruCache;

    private int mStrategySaveImage;
    private Builder mBuilder;

    final Object mLock;


    private Pen() {
        mQueue = new LinkedBlockingDeque<>();
        mExecutor = Executors.newFixedThreadPool(3);
        mLock = new Object();
        mBuilder = new Builder();
        mTypeOfMemoryCache = WITHOUT_CACHE;
        mStrategySaveImage = SAVE_SCALING_IMAGE_STRATEGY;

        initialisationLruCache();
        //TODO need to be later, do not require to be static
        DiskCache.getInstance();
        LogUtil.msg("Create object of Pen");
    }

    private void initialisationLruCache() {
        //TODO move calculations to separate methods
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / ConstantsUtil.KILOBYTE_SIZE);

        //TODO should be clear that size in KB
        final int cacheSize = maxMemory / ConstantsUtil.PART_OF_MEMORY_CACHE;

        //TODO move Runtime.getRuntime().maxMemory() to var
        LogUtil.msg("maxMemory = " + maxMemory + ". MaxMemory from Runtime: "
                + Runtime.getRuntime().maxMemory() + ". CacheSize: " + cacheSize);

        mBitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                //TODO wrong calculation
                return value.getByteCount() / ConstantsUtil.KILOBYTE_SIZE;
            }
        };
    }

    public static Pen getInstance() {
        if (instance == null) {
            instance = new Pen();
        }

        return instance;
    }

    int getTypeOfMemoryCache() {
        return mTypeOfMemoryCache;
    }

    int getStrategySaveImage() {
        return mStrategySaveImage;
    }

    public Builder getImageFromUrl(String url) {
        return mBuilder.getBitmapFromUrl(url);
    }

    private void enqueue(ImageRequest imageRequest) {

        ImageView imageView = imageRequest.getmTarget().get();

        //TODO What is best practice: check through if or covered it try-catch? How get resources in module?
        //TODO put to ImageRequest model
        if (PlaceHolderUtil.getInstance().getDefaultDrawable() != null) {
            imageView.setImageDrawable(PlaceHolderUtil.getInstance().getDefaultDrawable());
        }

        if (imageRequest.getmUrl() == null) {
            return;
        }


        if (imageView == null) {
            LogUtil.msg("Target image view not exist");

            return;
        }

        if (imageHasSize(imageRequest)) {
            String tag = MD5Util.getHashString(imageRequest.getmUrl());
            imageView.setTag(tag);
            LogUtil.msg(" get image " + tag + " " + imageRequest.getmUrl());
            mQueue.addFirst(imageRequest);
            LogUtil.msg("Image view" + imageRequest.getmTarget().get().toString() + " start setup");
            try {
                //TODO to use ExecutorService / executeOnExecutor
                new ImageLoadingAsyncTask().execute(mQueue.takeFirst());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            waiterImageViewShow(imageRequest);
        }
    }

    public Builder setLoaderSettings() {
        return mBuilder;
    }

    private boolean imageHasSize(ImageRequest request) {

        if (request.getmHeight() > 0 && request.getmWidth() > 0) {
            return true;
        }

        ImageView view = request.getmTarget().get();

        if (view != null && view.getHeight() > 0 && view.getWidth() > 0) {
            int viewHeight = view.getHeight();
            int viewWidth = view.getWidth();

            request.setmHeight(viewHeight);
            request.setmWidth(viewWidth);

            return true;
        }

        return false;
    }

    private void waiterImageViewShow(final ImageRequest pRequest) {
        LogUtil.msg("Image view" + pRequest.getmTarget().get().toString() + " wait for draw");

        ImageView viewWaitDraw = pRequest.getmTarget().get();

        viewWaitDraw.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                ImageView v = pRequest.getmTarget().get();

                if (v == null) {
                    return true;
                }

//                v.getViewTreeObserver().removeOnPreDrawListener(this);
                if (v.getWidth() > 0 && v.getHeight() > 0) {
                    LogUtil.msg("Image view" + pRequest.getmTarget().get().toString() + " start draw");

                    pRequest.setmWidth(v.getWidth());
                    pRequest.setmHeight(v.getHeight());
                    enqueue(pRequest);
                    //TODO remove comments correct variant for remove OnPreDrawListener
                    //TODO memory leak! should be removed in any case
                    v.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                return true;
            }
        });
    }

    //add bitmap for LruCache
    void addBitmapForLruCache(String key, Bitmap bitmap) {
        if (getBitmapFromLruCache(key) == null) {
            mBitmapLruCache.put(key, bitmap);
            LogUtil.msg("Add bitmap by key: " + key);
        }
    }

    //get bitmap from LruCache
    Bitmap getBitmapFromLruCache(String key) {
        LogUtil.msg("Try bitmap by key " + key);

        return mBitmapLruCache.get(key);
    }


    public class Builder {

        private Builder() {
        }

        private String mUrl;

        private void setUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        private Builder getBitmapFromUrl(String url) {
            setUrl(url);

            return mBuilder;
        }

        public Builder setTypeOfCache(int pTypeOfCache) {
            if (pTypeOfCache >= WITHOUT_CACHE && pTypeOfCache <= INNER_FILE_CACHE) {
                mTypeOfMemoryCache = pTypeOfCache;
            }

            return mBuilder;
        }

        public Builder setSavingStrategy(int pTypeStrategy) {
            if (pTypeStrategy >= SAVE_SCALING_IMAGE_STRATEGY && pTypeStrategy <= SAVE_FULL_IMAGE_STRATEGY) {
                mStrategySaveImage = pTypeStrategy;
            }

            return mBuilder;
        }

        public void inputTo(ImageView pView) {
            WeakReference<ImageView> weakReference = new WeakReference<>(pView);
            ImageRequest imageRequest = new ImageRequest(mBuilder.mUrl, weakReference);
            //TODO we already inside Pen?
            Pen.getInstance().enqueue(imageRequest);
        }

        public Builder setSizeInnerFileCache(Long pSizeMB) {
            DiskCache.getInstance().setUserCacheSize(pSizeMB);

            return mBuilder;
        }

        public Builder setContext(Context pContext) {
            CACHE_DIR = pContext.getCacheDir();

            return mBuilder;
        }

        public Builder setDefaultDrawable(Drawable pDefaultDrawable) {
            PlaceHolderUtil.getInstance().setDefaultDrawable(pDefaultDrawable);

            return mBuilder;
        }

        public Builder setErrorDrawable(Drawable pErorDrawable) {

            PlaceHolderUtil.getInstance().setErrorDrawable(pErorDrawable);
            return mBuilder;
        }

        public Builder setQualityImageCompression(int pCompression) {
            if (pCompression > MIN_COMPRESSION && pCompression <= MAX_COMPRESSION) {
                QUALITY_OF_COMPRESSION_BMP = pCompression;
            }

            return mBuilder;
        }

        public void getBitmapDirect(GetBitmapCallback pCallBack) {

            ImageRequest request = new ImageRequest(mBuilder.mUrl, pCallBack);
            new ImageLoadingAsyncTask().execute(request);
        }

        public void setUp() {
            if (QUALITY_OF_COMPRESSION_BMP == MIN_COMPRESSION) {
                QUALITY_OF_COMPRESSION_BMP = DEFAULT_COMPRESSION;
            }
        }
    }
}
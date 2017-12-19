package kiolk.com.github.pen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;

import static kiolk.com.github.pen.utils.ConstantsUtil.KILOBYTE_SIZE;
import static kiolk.com.github.pen.utils.ConstantsUtil.LOG;

class ImageFactory {

    static ImageResult creteBitmapFromUrl(ImageResult pResult) {
        String url = pResult.getmRequest().getmUrl();

        switch (Pen.getInstance().getTypeOfMemoryCache()) {
            case Pen.MEMORY_CACHE:
                synchronized (Pen.getInstance().mLock) {

                    if (Pen.getInstance().getBitmapFromLruCache(url) != null) {
                        pResult.setmBitmap(Pen.getInstance().getBitmapFromLruCache(url));
                        LogUtil.msg("Set bitmap from LruCache");

                        return pResult;
                    }
                }
                break;
            case Pen.INNER_FILE_CACHE:
                synchronized (DiskCache.getInstance().mLock) {
                    String name = getName(pResult);
                    Bitmap bitmap = DiskCache.getInstance().loadBitmapFromDiskCache(name);

                    if (bitmap != null) {
                        pResult.setmBitmap(bitmap);
                        LogUtil.msg("Set bitmap from DiskCache");

                        return pResult;
                    }
                }
                break;
            default:
                break;
        }

        pResult = creteBitmap(pResult);

        switch (Pen.getInstance().getTypeOfMemoryCache()) {
            case Pen.MEMORY_CACHE:

                synchronized (Pen.getInstance().mLock) {
                    Pen.getInstance().addBitmapForLruCache(pResult.getmRequest().getmUrl(), pResult.getmBitmap());
                }

                break;
            case Pen.INNER_FILE_CACHE:

                synchronized (DiskCache.getInstance().mLock) {
                    boolean resultOfSave;

                    String name = getName(pResult);
                    Bitmap bitmap = pResult.getmBitmap();
                    resultOfSave = DiskCache.getInstance().saveBitmapInDiskCache(bitmap, name);
                    if (resultOfSave) {
                        LogUtil.msg("Save " + name + " to DiskCache");
                    }
                }

                break;
            default:
                break;
        }

        return pResult;
    }

    private static ImageResult creteBitmap(ImageResult pResult) {
        String url = pResult.getmRequest().getmUrl();
        int reqHeight = pResult.getmRequest().getmHeight();
        int reqWidth = pResult.getmRequest().getmWidth();

        try {
            int bytesRead;
            InputStream stream = new URL(url).openStream();
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream(stream.available());
            byte[] arrayByte = new byte[KILOBYTE_SIZE];

            while ((bytesRead = stream.read(arrayByte)) > 0) {
                byteArrayInputStream.write(arrayByte, 0, bytesRead);
            }

            byte[] bytes = byteArrayInputStream.toByteArray();

            if (Pen.getInstance().getStrategySaveImage() == Pen.SAVE_SCALING_IMAGE_STRATEGY) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                options.inJustDecodeBounds = false;
                options.inSampleSize = ImageFactory.calculateInSimpleSize(options, reqHeight, reqWidth);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

//                int size = bmp.getByteCount();
                pResult.setmBitmap(bmp);
            } else {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                LogUtil.msg("Size non scaled file = " + bmp.getByteCount());
                pResult.setmBitmap(bmp);
            }

            return pResult;
        } catch (IOException e) {
            e.printStackTrace();
            pResult.setmException(e);

            return pResult;
        }
    }

    private static int calculateInSimpleSize(BitmapFactory.Options pOptions, int pHeight, int pWidth) {
        int outHeight = pOptions.outHeight;
        int outWidth = pOptions.outWidth;
        int inSimpleSize = 1;

        if (outHeight > pHeight || outWidth > pWidth) {
            outWidth = outWidth / 2;
            outHeight = outWidth / 2;

            while ((outWidth / inSimpleSize) >= pWidth && (outWidth / inSimpleSize) >= pHeight) {
                outHeight = outWidth / 2;
                outWidth = outWidth / 2;
                inSimpleSize *= 2;
            }
            Log.d(LOG, "inSimpleSize: " + inSimpleSize);
        }

        return inSimpleSize;
    }

    private static String getName(ImageResult pResult) {
        return MD5Util.getHashString(pResult.getmRequest().getmUrl());
    }
}

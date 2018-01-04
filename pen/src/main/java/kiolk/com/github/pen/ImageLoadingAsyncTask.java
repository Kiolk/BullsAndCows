package kiolk.com.github.pen;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import kiolk.com.github.pen.utils.LogUtil;
import kiolk.com.github.pen.utils.MD5Util;
import kiolk.com.github.pen.utils.PlaceHolderUtil;

public class ImageLoadingAsyncTask extends AsyncTask<ImageRequest, Void, ImageResult> {

    @Override
    protected ImageResult doInBackground(ImageRequest... pImageRequests) {
        LogUtil.msg("Started thread :" + Thread.currentThread().getName());

        ImageRequest request = pImageRequests[0];
        ImageResult result = new ImageResult(request);

        return ImageFactory.creteBitmapFromUrl(result);
    }

    @Override
    protected void onPostExecute(ImageResult pImageResult) {
        super.onPostExecute(pImageResult);

        //TODO pImageResult.getmBitmap() to var and others
        if (pImageResult.getmBitmap() != null
                && pImageResult.getmRequest().getmBitmapCallback() == null
                && pImageResult.getmRequest().getmTarget() != null) {
            ImageView imageView = pImageResult.getmRequest().getmTarget().get();
            String tag = MD5Util.getHashString(pImageResult.getmRequest().getmUrl());
//            LogUtil.msg("Compare between " + tag + " and " + imageView.getTag());

            if (imageView != null && imageView.getTag().equals(tag)) {
                imageView.setImageBitmap(pImageResult.getmBitmap());
                LogUtil.msg("set bmp from cache" + pImageResult.getmRequest().getmUrl() + MD5Util.getHashString(pImageResult.getmRequest().getmUrl()));
            }

        } else if (pImageResult.getmBitmap() != null && pImageResult.getmRequest().getmBitmapCallback() != null) {
            pImageResult.getmRequest().getmBitmapCallback().getBitmap(pImageResult.getmBitmap());

        } else if (pImageResult.getmBitmap() == null && pImageResult.getmRequest().getmTarget().get() != null) {
            //Not very good idea show toast from AsyncTask only for example
            //TODO implement possibility setup different drawable for different exceptions
            if (PlaceHolderUtil.getInstance().getErrorDrawable() != null) {
                ImageView imageView = pImageResult.getmRequest().getmTarget().get();
                imageView.setImageDrawable(PlaceHolderUtil.getInstance().getErrorDrawable());
                imageView.setContentDescription(pImageResult.getmException().getMessage());
                Toast.makeText(pImageResult.getmRequest().getmTarget().get().getContext(),
                        pImageResult.getmException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
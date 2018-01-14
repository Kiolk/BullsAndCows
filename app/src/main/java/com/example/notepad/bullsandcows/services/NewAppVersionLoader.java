package com.example.notepad.bullsandcows.services;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.notepad.bullsandcows.data.models.RequestUpdateModel;
import com.example.notepad.bullsandcows.utils.CheckExternalStorage;
import com.example.notepad.bullsandcows.utils.Constants;
import com.example.notepad.bullsandcows.utils.IOCloseUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//TODO split logic to: HttpRequest, FileCache, Parser -> join them in one operation.
public class NewAppVersionLoader extends AsyncTask<RequestUpdateModel, Void, RequestUpdateModel> {

    private static final String APK_FILE_RESOLUTION = ".apk";

    @Override
    protected void onPostExecute(final RequestUpdateModel requestUpdateModel) {
        final UploadNewVersionAppCallback callback = requestUpdateModel.getCallback();
        callback.sendUploadResultsCallback(requestUpdateModel);
    }

    @Override
    protected RequestUpdateModel doInBackground(final RequestUpdateModel... requestUpdateModels) {
        final RequestUpdateModel request = requestUpdateModels[0];
        final String downloadUrl = request.getVersionApp().getUrlNewVersionOfApp();
        File apkStorage = null;
        final File outputFile;
        final String downloadFile = request.getVersionApp().getNameOfApp() + APK_FILE_RESOLUTION;
        FileOutputStream fOS = null;
        InputStream in = null;

        try {

            final URL mUrl = new URL(downloadUrl);
            final HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(Constants.TAG, "Get not correct response");
            }

            if (new CheckExternalStorage().isExternalStoragePresent()) {

                apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + Constants.FOLDER_DESTINATION_IN_SD);
            } else {
                Log.d(Constants.TAG, "External storage not present");
            }

            if (apkStorage != null && !apkStorage.exists()) {
                if (apkStorage.mkdir()) {
                    Log.d(Constants.TAG, "Folder create");
                }
            }

            outputFile = new File(apkStorage, downloadFile);

            if (!outputFile.exists()) {
                if (outputFile.createNewFile()) {
                    Log.d(Constants.TAG, "File create");
                }
            }

            fOS = new FileOutputStream(outputFile);
            in = connection.getInputStream();
            final byte[] buffer = new byte[1024];
            int lin1;

            while ((lin1 = in.read(buffer)) != -1) {
                fOS.write(buffer, 0, lin1);
            }

        } catch (final Exception pE) {
            pE.printStackTrace();
            Log.d(Constants.TAG, "Message of error: " + pE.getMessage());
            request.setException(pE);
        } finally {
            new IOCloseUtils().addForClose(fOS).addForClose(in).close();
//            try {
//                if (fOS != null) {
//                    fOS.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
        }

        return request;
    }
}

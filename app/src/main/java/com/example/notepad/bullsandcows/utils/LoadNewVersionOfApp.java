package com.example.notepad.bullsandcows.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.example.notepad.bullsandcows.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadNewVersionOfApp {

    private Context mContext;
    private Button mButton;
    private String mDownloadUrl = "";
    private String mDownloadFile = "";

    public LoadNewVersionOfApp(Context pContext, Button pButton, String pDownloadUrl, String pDownloadFile) {
        mContext = pContext;
        mButton = pButton;
        mDownloadUrl = pDownloadUrl;
        mDownloadFile = pDownloadFile + ".apk" ;
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage;
        File outputFile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mButton.setEnabled(false);
            mButton.setText(R.string.START_DOWNLOAD_NEW_VERSION);
        }

        @Override
        protected void onPostExecute(Void pVoid) {
            try{
                if(outputFile != null){
                    mButton.setText(R.string.FINISH_DOWNLOAD_NEW_VERSION);
                    mButton.setText(R.string.VISIT_SITE);
                    Log.d(Constants.TAG, "Finish download new version");
                }else{
                    mButton.setText(R.string.FAILED_DOWNLOAD_NEW_VERSION);
                    Log.d(Constants.TAG, "Failed download new version");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mButton.setEnabled(true);
                            mButton.setText(R.string.TRY_AGAIN);
                        }
                    }, 3000);
                }

            }catch (Exception pE){
                mButton.setText(R.string.FAILED_DOWNLOAD_NEW_VERSION);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mButton.setEnabled(true);
                        mButton.setText(R.string.TRY_AGAIN);
                    }
                }, 3000);
                Log.d(Constants.TAG, "Failed download. Error: " + pE.getLocalizedMessage());
            }

            super.onPostExecute(pVoid);
        }

        @Override
        protected Void doInBackground(Void... pVoids) {
            try {
                URL mUrl = new URL(mDownloadUrl);
                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.d(Constants.TAG, "Get not correct response");
                }
                if(new CheckExternalStorage().isExternalStoragePresent()){
                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + Constants.FOLDER_DESTINATION_IN_SD);
                }else {
                    Log.d(Constants.TAG, "External storage not present");
                }
                if(!apkStorage.exists()){
                    apkStorage.mkdir();
                    Log.d(Constants.TAG, "Folder create");
                }
                outputFile = new File(apkStorage, mDownloadFile);
                if(!outputFile.exists()){
                    outputFile.createNewFile();
                    Log.d(Constants.TAG, "File create");
                }
                FileOutputStream fOS = new FileOutputStream(outputFile);
                InputStream in = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int lin1 = 0;
                while ((lin1 = in.read(buffer)) != -1){
                    fOS.write(buffer, 0, lin1);
                }
                fOS.close();
                in.close();
            } catch (Exception pE) {
                pE.printStackTrace();
                outputFile = null;
                Log.d(Constants.TAG, "Message of error: " + pE.getMessage());
            }
            return null;
        }
    }
}

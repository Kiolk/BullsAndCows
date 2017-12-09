package com.example.notepad.bullsandcows.data.managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.example.notepad.bullsandcows.data.factories.RecordJsonFactory;
import com.example.notepad.bullsandcows.data.models.RequestRecordModel;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;


public class RecordsManager implements RecordsCallbacks {

    private static final String URL_RECORDS_BACKEND = "https://onlinerecordbulsandcows.appspot.com/_ah/api/";
    private RecordsToNetApi myApiService;
    private Thread mListRecordThread;
    private Handler mListHandler;
    private String mCursor;


     protected RecordsManager() {

        initHandler();
        mListRecordThread = new Thread(mListRecordRunnable);
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mListHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ResponseRecordModel response = (ResponseRecordModel) msg.obj;
                response = new RecordJsonFactory().getRecordsFromBackend(response);
                getResponseBackendCallback(response);
            }
        };
    }

    private Runnable mListRecordRunnable = new Runnable() {
        @Override
        public void run() {

            if (myApiService == null) {
                RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl(URL_RECORDS_BACKEND);

                myApiService = builder.build();
            }

            Message msg = new Message();
            ResponseRecordModel responseRecord = new ResponseRecordModel();

            try {
                // String cursor = "CjoSNGoZZ35vbmxpbmVyZWNvcmRidWxzYW5kY293c3IXCxIMUmVjb3Jkc1RvTmV0GIGk8JX5KwwYACAA";
                String json = myApiService.list().setCursor(mCursor).execute().toString();
                responseRecord.setmJsonFromBackend(json);
                msg.obj = responseRecord;
                mListHandler.sendMessage(msg);
            } catch (IOException pE) {
                pE.printStackTrace();
                responseRecord.setmException(pE);
                msg.obj = responseRecord;
                mListHandler.sendMessage(msg);
            }
        }
    };

    public void getRecordSBackend(RequestRecordModel pRequest) {
        mCursor = pRequest.getCursor();
        mListRecordThread.start();
    }


    @Override
    public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
        return pResponse;
    }
}

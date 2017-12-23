package com.example.notepad.bullsandcows.data.managers;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.example.notepad.bullsandcows.data.factories.RecordJsonFactory;
import com.example.notepad.bullsandcows.data.httpclient.BackendEndpointClient;
import com.example.notepad.bullsandcows.data.models.RequestRecordModel;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.utils.converters.Converters;

import java.io.IOException;

import javax.annotation.Nullable;


public class RecordsManager implements RecordsCallbacks {

    private Thread mListRecordThread;
    private Handler mListHandler;
    private String mCursor;


    public RecordsManager() {
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

//            if (myApiService == null) {
//                RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                        .setRootUrl(URL_RECORDS_BACKEND);
//
//                myApiService = builder.build();
//            }

            Message msg = new Message();
            ResponseRecordModel responseRecord = new ResponseRecordModel();

            try {
                // String cursor = "CjoSNGoZZ35vbmxpbmVyZWNvcmRidWxzYW5kY293c3IXCxIMUmVjb3Jkc1RvTmV0GIGk8JX5KwwYACAA";
//                String json = myApiService.list().setCursor(mCursor).execute().toString();
                String json = BackendEndpointClient.getRecordToNetApi().list().setCursor(mCursor).execute().toString();
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
    @Deprecated
    public ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse) {
//        mListRecordThread = null;
        return pResponse;
    }

    @Override
    public void getRecordsBackendCallback(ResponseRecordModel pResponse) {

    }

    public void getRecordsFromBackend(@Nullable final Long pAllRecordsOnDate, final RecordsCallbacks pCallback) {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseRecordModel response = new ResponseRecordModel();

                try {

                    do {
                        if (response.getmCursor() == null) {
                            response.setmJsonFromBackend(BackendEndpointClient.getRecordToNetApi().list().setCursor(null).execute().toString());
                        } else {
                            response.setmJsonFromBackend(BackendEndpointClient.getRecordToNetApi().list().setCursor(response.getmCursor()).execute().toString());
                        }
                        response = new RecordJsonFactory().getRecordsFromBackend(response);
                    } while (pAllRecordsOnDate != null
                            && Converters.convertToBackendTime(pAllRecordsOnDate) > response.getmRecordsArray().get(response.getmRecordsArray().size() - 1).getDate());

                } catch (IOException pE) {
                    pE.printStackTrace();
                    response.setmException(pE);
                }
                final ResponseRecordModel readyResponse = response;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pCallback.getRecordsBackendCallback(readyResponse);
                    }
                });
            }
        });
        thread.start();
    }
}

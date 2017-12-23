package com.example.notepad.bullsandcows.data.managers;

import android.os.Handler;

import com.example.notepad.bullsandcows.data.factories.RecordJsonFactory;
import com.example.notepad.bullsandcows.data.httpclient.BackendEndpointClient;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.ui.activity.listeners.PostRecordSuccessListener;
import com.example.notepad.bullsandcows.utils.converters.Converters;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.io.IOException;

import javax.annotation.Nullable;


public class RecordsManager{

    public void postRecordOnBackend(final RecordsToNet pRecord, @Nullable final PostRecordSuccessListener pCallback){
        final Handler handler = new Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               RecordsToNet setupRecord = null;
                try {
                   setupRecord = BackendEndpointClient.getRecordToNetApi().insert(pRecord).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final RecordsToNet responseRecord = setupRecord;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(pCallback != null && responseRecord != null ){
                            pCallback.successSetResultListener(pRecord);
                        }else if (pCallback != null){
                            pCallback.successSetResultListener(null);
                        }
                    }
                });
            }
        });

        thread.start();
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

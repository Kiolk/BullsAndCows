package com.example.notepad.bullsandcows.data.managers;

import android.os.Handler;

import com.example.notepad.bullsandcows.data.parsers.RecordJsonParser;
import com.example.notepad.bullsandcows.data.httpclient.BackendEndpointClient;
import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;
import com.example.notepad.bullsandcows.utils.converters.TimeConvertersUtil;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

import java.io.IOException;

import javax.annotation.Nullable;


//TODO threads management in one class - operations in another
//generic Callback<Result> onSuccess(Result result) and onException(Exception ex)
public class RecordsManager{

    public void postRecordOnBackend(final RecordsToNet pRecord, @Nullable final OnResultCallback<RecordsToNet> pCallback){
        final Handler handler = new Handler();

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
               RecordsToNet setupRecord = null;
                Exception gettingException = null;
                try {
                   setupRecord = BackendEndpointClient.getRecordToNetApi().insert(pRecord).execute();
                } catch (final IOException pException) {
                    gettingException = pException;
                }
                final RecordsToNet responseRecord = setupRecord;
                final Exception exception = gettingException;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(pCallback != null && responseRecord != null ){
                            pCallback.onSuccess(pRecord);
                        }else if (pCallback != null){
                            pCallback.onError(exception);
                        }
                    }
                });
            }
        });

        thread.start();
    }

    public void getRecordsFromBackend(@Nullable final Long pAllRecordsOnDate, final OnResultCallback<ResponseRecordModel> pCallback) {
        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseRecordModel response = new ResponseRecordModel();

                try {

                    do {
                        if (response.getCursor() == null) {
                            response.setJsonFromBackend(BackendEndpointClient.getRecordToNetApi().list().setCursor(null).execute().toString());
                        } else {
                            response.setJsonFromBackend(BackendEndpointClient.getRecordToNetApi().list().setCursor(response.getCursor()).execute().toString());
                        }
                        response = new RecordJsonParser().getRecordsFromBackend(response);
                    } while (pAllRecordsOnDate != null
                            && TimeConvertersUtil.convertToBackendTime(pAllRecordsOnDate) > response.getRecordsArray().get(response.getRecordsArray().size() - 1).getDate());

                } catch (IOException pE) {
                    pE.printStackTrace();
                    response.setException(pE);
                }
                final ResponseRecordModel readyResponse = response;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pCallback.onSuccess(readyResponse);
                    }
                });
            }
        });
        thread.start();
    }
}

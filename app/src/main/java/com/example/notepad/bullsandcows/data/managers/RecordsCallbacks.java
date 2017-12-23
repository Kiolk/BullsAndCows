package com.example.notepad.bullsandcows.data.managers;

import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;

public interface RecordsCallbacks {
    @Deprecated
    ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse);

    void getRecordsBackendCallback(ResponseRecordModel pResponse);

}

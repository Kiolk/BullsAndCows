package com.example.notepad.bullsandcows.data.managers;

import com.example.notepad.bullsandcows.data.models.ResponseRecordModel;

/**
 * Created by yauhen on 2.12.17.
 */

public interface RecordsCallbacks {

    ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse);
}

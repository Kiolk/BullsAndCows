package com.example.notepad.bullsandcows.Data.Managers;

import com.example.notepad.bullsandcows.Data.Models.ResponseRecordModel;

/**
 * Created by yauhen on 2.12.17.
 */

public interface RecordsCallbacks {

    ResponseRecordModel getResponseBackendCallback(ResponseRecordModel pResponse);
}

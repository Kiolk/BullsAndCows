package com.example.notepad.bullsandcows.ui.activity.listeners;

import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

public interface PostRecordSuccessListener {
    @Deprecated
    RecordsToNet setResult(RecordsToNet pRecord);

    void successSetResultListener(RecordsToNet pRecord);
}

package com.example.notepad.bullsandcows.data.executable;

import com.example.notepad.bullsandcows.data.httpclient.BackendEndpointClient;
import com.example.notepad.bullsandcows.data.managers.IODataOperation;
import com.example.notepad.myapplication.backend.recordsToNetApi.model.RecordsToNet;

public class PostUserRecord implements IODataOperation<RecordsToNet> {

    private final RecordsToNet mRecord;

    PostUserRecord (final RecordsToNet pRecord){
        mRecord = pRecord;
    }

    @Override
    public RecordsToNet perform() throws Exception {

        return  BackendEndpointClient.getRecordToNetApi().insert(mRecord).execute();
    }
}

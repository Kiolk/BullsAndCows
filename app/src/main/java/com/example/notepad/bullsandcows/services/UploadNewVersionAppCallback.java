package com.example.notepad.bullsandcows.services;

import com.example.notepad.bullsandcows.data.models.RequestUpdateModel;

public interface UploadNewVersionAppCallback {

    //TODO onUploadRes
    void sendUploadResultsCallback(RequestUpdateModel pRequest);

}

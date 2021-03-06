package com.example.notepad.bullsandcows.data.httpclient;

import com.example.notepad.bullsandcows.BuildConfig;
import com.example.notepad.myapplication.backend.recordsToNetApi.RecordsToNetApi;
import com.example.notepad.myapplication.backend.userDataBaseApi.UserDataBaseApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

public class BackendEndpointClient {

    private static final String URL_RECORDS_BACKEND = BuildConfig.BACKEND_USER_INFO;
    private static final String USER_BACKEND_URL = BuildConfig.BACKEND_USER_INFO;

    public static RecordsToNetApi getRecordToNetApi() {
        RecordsToNetApi myApiService;

        RecordsToNetApi.Builder builder = new RecordsToNetApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(URL_RECORDS_BACKEND);

        myApiService = builder.build();

        return myApiService;
    }

    public static UserDataBaseApi getUserDataBaseApi(){
        UserDataBaseApi.Builder builder = new UserDataBaseApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl(USER_BACKEND_URL);

       return builder.build();
    }
}

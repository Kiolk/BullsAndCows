package com.example.notepad.bullsandcows.data.httpclient.models;

import javax.annotation.Nonnull;

public class HttpRequest {

    private String mUrl;

    private String mParam;

    public HttpRequest(@Nonnull String mUrl, @Nonnull String mParam) {
        this.mUrl = mUrl;
        this.mParam = mParam;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getParam() {
        return mParam;
    }

    public void setParam(String mParam) {
        this.mParam = mParam;
    }
}

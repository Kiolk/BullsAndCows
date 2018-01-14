package com.example.notepad.bullsandcows.data.httpclient.models;

import javax.annotation.Nonnull;

public class HttpRequest {

    private String mUrl;

    private String mParam;

    public HttpRequest(@Nonnull final String mUrl, @Nonnull final String mParam) {
        this.mUrl = mUrl;
        this.mParam = mParam;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    public String getParam() {
        return mParam;
    }

    public void setParam(final String mParam) {
        this.mParam = mParam;
    }
}

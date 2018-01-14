package com.example.notepad.bullsandcows.data.httpclient.models;

public class HttpResponse {

    private HttpRequest mTargetRequest;

    private String mResponse;

    private Exception mHttpError;

    private boolean isHttpOk;

    public HttpRequest getTargetRequest() {
        return mTargetRequest;
    }

    public void setTargetRequest(final HttpRequest mTargetRequest) {
        this.mTargetRequest = mTargetRequest;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(final String mGettingResponse) {
        this.mResponse = mGettingResponse;
    }

    public Exception getHttpError() {
        return mHttpError;
    }

    public void setHttpError(final Exception mHttpError) {
        this.mHttpError = mHttpError;
    }

    public boolean isHttpOk() {
        return isHttpOk;
    }

    public void setHttpOk(final boolean httpOk) {
        isHttpOk = httpOk;
    }
}

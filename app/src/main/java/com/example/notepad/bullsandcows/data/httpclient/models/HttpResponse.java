package com.example.notepad.bullsandcows.data.httpclient.models;

public class HttpResponse {

    private HttpRequest mTargetRequest;

    private String mResponse;

    private Exception mHttpError;

    private boolean isHttpOk;

    public HttpRequest getTargetRequest() {
        return mTargetRequest;
    }

    public void setTargetRequest(HttpRequest mTargetRequest) {
        this.mTargetRequest = mTargetRequest;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String mGettingResponse) {
        this.mResponse = mGettingResponse;
    }

    public Exception getHttpError() {
        return mHttpError;
    }

    public void setHttpError(Exception mHttpError) {
        this.mHttpError = mHttpError;
    }

    public boolean isHttpOk() {
        return isHttpOk;
    }

    public void setHttpOk(boolean httpOk) {
        isHttpOk = httpOk;
    }
}

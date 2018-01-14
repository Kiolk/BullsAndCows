package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.bullsandcows.data.managers.OnResultCallback;
@Deprecated
public class UniversalRequestParams<T> {

    private T mFirstParam;

    private OnResultCallback mCallback;

    public UniversalRequestParams(final T pFirstParam, final OnResultCallback pCallback){
        mFirstParam = pFirstParam;
        mCallback = pCallback;
    }

    public T getFirstParam() {
        return mFirstParam;
    }

    public OnResultCallback getCallback() {
        return mCallback;
    }
}

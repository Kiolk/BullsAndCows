package com.example.notepad.bullsandcows.data.models;

import com.example.notepad.bullsandcows.data.managers.UniversalThreadCallback;

public class UniversalRequestParams<T> {

    private T mFirstParam;

    private UniversalThreadCallback mCallback;

    public UniversalRequestParams(final T pFirstParam, final UniversalThreadCallback pCallback){
        mFirstParam = pFirstParam;
        mCallback = pCallback;
    }

    public T getFirstParam() {
        return mFirstParam;
    }

    public UniversalThreadCallback getCallback() {
        return mCallback;
    }
}

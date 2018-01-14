package com.example.notepad.bullsandcows.data.managers;

public interface OnResultCallback<T> {

    void onSuccess(T pResult);

    void onError(Exception pException);

}

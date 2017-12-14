package com.example.notepad.bullsandcows.data.managers;

import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public interface UserInfoCallback {

    void nikFreeCallback();

    UserDataBase nikPasswordCorrectCallback(UserDataBase pUserInfo);

    void nikCorrectPasswordWrongCallback();

    UserDataBase getFullUserInfoCallback(UserDataBase pUserData);

    boolean patchNewUserInfoCallback(Boolean isSuccessFull);

}

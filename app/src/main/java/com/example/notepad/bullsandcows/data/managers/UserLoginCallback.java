package com.example.notepad.bullsandcows.data.managers;

import com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase;

public interface UserLoginCallback {

    void getUserInfoCallback(UserDataBase pUserInfo);
}

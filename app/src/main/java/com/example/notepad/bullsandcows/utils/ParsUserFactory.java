package com.example.notepad.bullsandcows.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ParsUserFactory {

//    public UserDataBase parseForUserData (String pS){
//        UserDataBase oneUser = new UserDataBase();
//
//        try {
//            JSONObject userJson = new JSONObject(pS);
//            oneUser.setUserName(userJson.getString("userName"));
//            oneUser.setPassword(userJson.getString("password"));
//            oneUser.setCountry(userJson.getString("country"));
//            oneUser.setEmail(userJson.getString("email"));
//            return oneUser;
//        } catch (JSONException pE) {
//            pE.printStackTrace();
//        }
//
//        return null;
//    }

//    remove com.example.notepad.myapplication.backend.userDataBaseApi.model. and just import UserDataBase
    public com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase parseForUserDataForModel (String pS){
        com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase oneUser = new com.example.notepad.myapplication.backend.userDataBaseApi.model.UserDataBase();

        try {
            JSONObject userJson = new JSONObject(pS);

//            move to constant
            oneUser.setUserName(userJson.getString("userName"));
            oneUser.setPassword(userJson.getString("password"));
            oneUser.setCountry(userJson.getString("country"));
            oneUser.setEmail(userJson.getString("email"));
            return oneUser;
        } catch (JSONException pE) {
            pE.printStackTrace();
        }

        return null;
    }

}

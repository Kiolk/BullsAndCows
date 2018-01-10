package com.example.notepad.bullsandcows.data.managers;

public interface IODataOperation<Result> {

    Result perform() throws Exception;

}

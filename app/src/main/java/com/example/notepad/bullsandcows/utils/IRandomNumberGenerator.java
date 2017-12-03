package com.example.notepad.bullsandcows.utils;

public interface IRandomNumberGenerator {

    String generateRandomNumber(int pNumberOfDigits);

    int checkNumberOfBulls(String pCodedNumber, String pEnteredNumber);

    int checkNumberOfCows(String pCodedNumber, String pEnteredNumber);

    boolean checkNumberForCorrectInput(String pEnteredNumber, int pNumberOfDigits);
}

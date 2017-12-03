package com.example.notepad.bullsandcows.utils;

import java.util.Random;

/*
This class generate random number for game. Checking input number.
*/

public class RandomNumberGenerator implements IRandomNumberGenerator{

    public String generateRandomNumber(int pNumberOfDigits) {
        int[] randomNumber = new int[10];
        String result = "";
        for (int i = 0, j = 0; i < pNumberOfDigits; ++i) {
            Random r = new Random();
            int k = r.nextInt(9);
            if (i == 0) {
                randomNumber[i] = k;
            } else {
                for (j = 0; j < i; ++j) {
                    if (randomNumber[j] == k) {
                        --i;
                        break;
                    } else {
                        randomNumber[i] = k;
                    }
                }
            }

        }
        for (int i = 0; i < pNumberOfDigits; ++i) {
            result = result + randomNumber[i];
        }

        return result;
    }

    public int checkNumberOfBulls(String pCodedNumber, String pEnteredNumber) {
        int numberOfBulls = 0;
        char[] codedNumberArray = pCodedNumber.toCharArray();
        char[] enteredNumberArray = pEnteredNumber.toCharArray();
        for (int i = 0; i < pCodedNumber.length(); ++i) {
            if (codedNumberArray[i] == enteredNumberArray[i]) {
                ++numberOfBulls;
            }
        }
        return numberOfBulls;
    }

    public int checkNumberOfCows(String pCodedNumber, String pEnteredNumber) {
        int numberOfCows = 0;
        char[] codedNumberArray = pCodedNumber.toCharArray();
        char[] enteredNumberArray = pEnteredNumber.toCharArray();
        int lengthOfNumber = codedNumberArray.length;
        for (int i = 0; i < lengthOfNumber; ++i) {
            for (int j = 0; j < lengthOfNumber; ++j) {
                if (codedNumberArray[i] == enteredNumberArray[j] && i != j) {
                    ++numberOfCows;
                }
            }
        }
        return numberOfCows;
    }

    public boolean checkNumberForCorrectInput(String pEnteredNumber, int pNumberOfDigits) {
        if (pEnteredNumber.length() == pNumberOfDigits) {
            for (int i = 0; i < pNumberOfDigits; ++i) {
                if (pEnteredNumber.charAt(i) < '0' || pEnteredNumber.charAt(i) > '9') {
                    return false;
                }
            }
            int numberForChecking = Integer.parseInt(pEnteredNumber);
            int k = 1;
            for (int i = 0; i < pNumberOfDigits - 1; ++i) {
                k = k * 10;
            }
            if (numberForChecking >= k) {
                int[] numberArray;
                numberArray = new int[pNumberOfDigits];
                for (int i = pNumberOfDigits - 1; i >= 0; --i) {
                    numberArray[i] = numberForChecking % 10;
                    numberForChecking = numberForChecking / 10;
                }
                for (int i = 0, cnt = 0; i < pNumberOfDigits; ++i) {
                    for (int j = 0; j < pNumberOfDigits; ++j) {
                        if (numberArray[i] == numberArray[j]) {
                            ++cnt;
                        }
                        if (cnt > pNumberOfDigits) {
                            return false;
                        }
                    }
                }
                return true;
            } else if (pEnteredNumber.charAt(0) == '0' && numberForChecking >= k / 10) {
                int[] numberArray;
                numberArray = new int[pNumberOfDigits];
                for (int i = pNumberOfDigits - 1; i >= 1; --i) {
                    numberArray[i] = numberForChecking % 10;
                    numberForChecking = numberForChecking / 10;
                }
                for (int i = 0, cnt = 0; i < pNumberOfDigits; ++i) {
                    for (int j = 0; j < pNumberOfDigits; ++j) {
                        if (numberArray[i] == numberArray[j]) {
                            ++cnt;
                        }
                        if (cnt > pNumberOfDigits) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
        return false;
    }
}

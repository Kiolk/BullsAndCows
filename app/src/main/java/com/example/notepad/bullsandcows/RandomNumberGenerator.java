package com.example.notepad.bullsandcows;

import java.util.Random;

public class RandomNumberGenerator {

    public String generateRandomNumber(int pNumberOfDigits){
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
}

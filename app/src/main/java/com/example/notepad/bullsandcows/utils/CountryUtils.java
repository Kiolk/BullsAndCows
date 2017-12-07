package com.example.notepad.bullsandcows.utils;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.models.ItemCountryData;

import java.util.ArrayList;

public class CountryUtils {

    public static ArrayList<ItemCountryData> getCountryList(){
        ArrayList<ItemCountryData> listOfCountry = new ArrayList<>();

        listOfCountry.add(new ItemCountryData("Belarus", R.drawable.ic_belarus));
        listOfCountry.add(new ItemCountryData("USA", R.drawable.ic_us));
        listOfCountry.add(new ItemCountryData("Grate Britany", R.drawable.ic_gb_eng));

        return listOfCountry;
    }


    public static int getCountryResources(String pCountry){
        ArrayList<ItemCountryData> listOfCountry = getCountryList();
        int i = 0;
        while(!pCountry.equals(listOfCountry.get(i).getCountry())){
            ++i;
        }

        return listOfCountry.get(i).getResource();
    }

    public static String getCountry (int pIndex){
        ArrayList<ItemCountryData> listOfCountry = getCountryList();
        return listOfCountry.get(pIndex).getCountry();
    }
}

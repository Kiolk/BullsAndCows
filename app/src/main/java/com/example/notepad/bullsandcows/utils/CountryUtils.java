package com.example.notepad.bullsandcows.utils;

import com.example.notepad.bullsandcows.R;
import com.example.notepad.bullsandcows.data.models.ItemCountryData;

import java.util.ArrayList;

public class CountryUtils {

    public static ArrayList<ItemCountryData> getCountryList(){
        ArrayList<ItemCountryData> listOfCountry = new ArrayList<>();
        //TODO not very good idea show countries and it flahs like this
        //TODO move to resource or on your backend
        listOfCountry.add(new ItemCountryData("Belarus", R.drawable.ic_belarus));
        listOfCountry.add(new ItemCountryData("USA", R.drawable.ic_us));
        listOfCountry.add(new ItemCountryData("Grate Britany", R.drawable.ic_gb_eng));
        listOfCountry.add(new ItemCountryData("Poland", R.drawable.ic_pol));
        listOfCountry.add(new ItemCountryData("Ukraine", R.drawable.ic_ukr));

        return listOfCountry;
    }


    public static int getCountryResources(String pCountry){
        //TODO use interface List instead of and for others
        ArrayList<ItemCountryData> listOfCountry = getCountryList();
        int i = 0;
        while(!pCountry.equals(listOfCountry.get(i).getCountry())){
            ++i;
        }
        return listOfCountry.get(i).getResource();
    }

    public static int getCountryId(String pCountry){
        ArrayList<ItemCountryData> listOfCountry = getCountryList();
        int i = 0;
        while(!pCountry.equals(listOfCountry.get(i).getCountry())){
            ++i;
        }
        return i;
    }

    public static String getCountry (int pIndex){
        ArrayList<ItemCountryData> listOfCountry = getCountryList();
        return listOfCountry.get(pIndex).getCountry();
    }
}

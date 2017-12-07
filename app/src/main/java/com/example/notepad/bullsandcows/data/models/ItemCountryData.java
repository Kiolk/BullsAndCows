package com.example.notepad.bullsandcows.data.models;

public class ItemCountryData {

    private String mCountry;

    private int mResource;

    public ItemCountryData(String mCountry, int mResource) {
        this.mCountry = mCountry;
        this.mResource = mResource;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public int getResource() {
        return mResource;
    }

    public void setResource(int mResource) {
        this.mResource = mResource;
    }
}

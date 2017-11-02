package com.example.NotePad.myapplication.backend;

public class VersionOfApp {

    private String mNameOfApp;
    private String mVersionOfApp;
    private Long mDateOfRelease;
    private String mPowered;
    private String mFeatures;
    private String mUrlNewVersionOfApp;

    public VersionOfApp(String pNameOfApp, String pVersionOfApp, Long pDateOfRelease, String pPowered, String pFeatures, String pUrlNewVersionOfApp) {
        mNameOfApp = pNameOfApp;
        mVersionOfApp = pVersionOfApp;
        mDateOfRelease = pDateOfRelease;
        mPowered = pPowered;
        mFeatures = pFeatures;
        mUrlNewVersionOfApp = pUrlNewVersionOfApp;
    }

    public String getUrlNewVersionOfApp() {
        return mUrlNewVersionOfApp;
    }

    public void setUrlNewVersionOfApp(String pUrlNewVersionOfApp) {
        mUrlNewVersionOfApp = pUrlNewVersionOfApp;
    }


    public String getNameOfApp() {
        return mNameOfApp;
    }

    public void setNameOfApp(String pNameOfApp) {
        mNameOfApp = pNameOfApp;
    }

    public String getVersionOfApp() {
        return mVersionOfApp;
    }

    public void setVersionOfApp(String pVersionOfApp) {
        mVersionOfApp = pVersionOfApp;
    }

    public Long getDateOfRelease() {
        return mDateOfRelease;
    }

    public void setDateOfRelease(Long pDateOfRelease) {
        mDateOfRelease = pDateOfRelease;
    }

    public String getPowered() {
        return mPowered;
    }

    public void setPowered(String pPowered) {
        mPowered = pPowered;
    }

    public String getFeatures() {
        return mFeatures;
    }

    public void setFeatures(String pFeatures) {
        mFeatures = pFeatures;
    }
}
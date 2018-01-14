package com.example.notepad.myapplication.backend;

public class VersionOfApp {

    private String mNameOfApp;
    private String mVersionOfApp;
    private Long mDateOfRelease;
    private String mPowered;
    private String mFeatures;
    private String mUrlNewVersionOfApp;
    private String[] mNewVersionFeatures;
    private String mUserRecordBackend;

    VersionOfApp(final String pNameOfApp, final String pVersionOfApp, final Long pDateOfRelease,
                 final String pPowered, final String pFeatures, final String pUrlNewVersionOfApp,
                 final String pUserRecordBackend, final String[] pNewVersionFeatures) {
        mNameOfApp = pNameOfApp;
        mVersionOfApp = pVersionOfApp;
        mDateOfRelease = pDateOfRelease;
        mPowered = pPowered;
        mFeatures = pFeatures;
        mUrlNewVersionOfApp = pUrlNewVersionOfApp;
        mNewVersionFeatures = pNewVersionFeatures;
        mUserRecordBackend = pUserRecordBackend;
    }

    public VersionOfApp() {
    }

    public String getUrlNewVersionOfApp() {
        return mUrlNewVersionOfApp;
    }

    public void setUrlNewVersionOfApp(final String pUrlNewVersionOfApp) {
        mUrlNewVersionOfApp = pUrlNewVersionOfApp;
    }

    public String getNameOfApp() {
        return mNameOfApp;
    }

    public void setNameOfApp(final String pNameOfApp) {
        mNameOfApp = pNameOfApp;
    }

    public String getVersionOfApp() {
        return mVersionOfApp;
    }

    public void setVersionOfApp(final String pVersionOfApp) {
        mVersionOfApp = pVersionOfApp;
    }

    public Long getDateOfRelease() {
        return mDateOfRelease;
    }

    public void setDateOfRelease(final Long pDateOfRelease) {
        mDateOfRelease = pDateOfRelease;
    }

    public String getPowered() {
        return mPowered;
    }

    public void setPowered(final String pPowered) {
        mPowered = pPowered;
    }

    public String getFeatures() {
        return mFeatures;
    }

    public void setFeatures(final String pFeatures) {
        mFeatures = pFeatures;
    }

    public String[] getmNewVersionFeatures() {
        return mNewVersionFeatures;
    }

    public void setNewVersionFeatures(final String[] mNewVersionFeatures) {
        this.mNewVersionFeatures = mNewVersionFeatures;
    }

    public String getUserRecordBackend() {
        return mUserRecordBackend;
    }

    public void setUserRecordBackend(final String mUserRecordBackend) {
        this.mUserRecordBackend = mUserRecordBackend;
    }
}

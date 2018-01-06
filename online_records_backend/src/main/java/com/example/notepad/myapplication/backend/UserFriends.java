package com.example.notepad.myapplication.backend;

public class UserFriends {

    private String mUserFriendName;

    private long mLastFriendVisit;

    private String mFriendImageUrl;

    public String getUserFriendName() {
        return mUserFriendName;
    }

    public void setUserFriendName(String mUserFriendName) {
        this.mUserFriendName = mUserFriendName;
    }

    public long getLastFriendVisit() {
        return mLastFriendVisit;
    }

    public void setLastFriendVisit(long mLastFriendVisit) {
        this.mLastFriendVisit = mLastFriendVisit;
    }

    public String getFriendImageUrl() {
        return mFriendImageUrl;
    }

    public void setFriendImageUrl(String mFriendImageUrl) {
        this.mFriendImageUrl = mFriendImageUrl;
    }
}

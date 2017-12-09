package com.example.NotePad.myapplication.backend;

public class UserMessages {

    private String mMessageFrom;

    private String mMessageTo;

    private String mMessage;

    private boolean isRead;

    private long mSendTime;

    public String getMessageFrom() {
        return mMessageFrom;
    }

    public void setMessageFrom(String mMessageFrom) {
        this.mMessageFrom = mMessageFrom;
    }

    public String getMessageTo() {
        return mMessageTo;
    }

    public void setMessageTo(String mMessageTo) {
        this.mMessageTo = mMessageTo;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getSendTime() {
        return mSendTime;
    }

    public void setSendTime(long mSendTime) {
        this.mSendTime = mSendTime;
    }
}
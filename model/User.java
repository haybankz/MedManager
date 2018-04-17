package com.haybankz.medmanager.model;

public class User {

    private int mId;
    private String mProfileId;
    private String mGivenName;
    private String mFamilyName;
    private String mDisplayName;
    private String mEmail;
    private String mPhotoUrl;
    private String mPassword;

    public User(String mProfileId, String mGivenName, String mFamilyName, String mDisplayName, String mEmail, String mPassword, String mPhotoUrl) {
        this.mProfileId = mProfileId;
        this.mGivenName = mGivenName;
        this.mFamilyName = mFamilyName;
        this.mDisplayName = mDisplayName;
        this.mEmail = mEmail;
        this.mPhotoUrl = mPhotoUrl;
        this.mPassword = mPassword;
    }

    public User(int mId, String mProfileId, String mGivenName, String mFamilyName, String mDisplayName, String mEmail, String mPassword, String mPhotoUrl) {
        this.mId = mId;
        this.mProfileId = mProfileId;
        this.mGivenName = mGivenName;
        this.mFamilyName = mFamilyName;
        this.mDisplayName = mDisplayName;
        this.mEmail = mEmail;
        this.mPhotoUrl = mPhotoUrl;
        this.mPassword = mPassword;
    }

    public int getId() {
        return mId;
    }

    public String getProfileId() {
        return mProfileId;
    }

    public String getGivenName() {
        return mGivenName;
    }

    public String getFamilyName() {
        return mFamilyName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public String getPassword() {
        return mPassword;
    }
}

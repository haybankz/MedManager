package com.haybankz.medmanager.model;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class Medication {

    private long mId;
    private String mName;
    private String mDescription;
    private int mFrequency;
    private long mStartDateTime;
    private long mEndDateTime;

    public Medication() {
    }

    public Medication(String mName, String mDescription, int mFrequency, long mStartDateTime, long mEndDateTime) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mFrequency = mFrequency;
        this.mStartDateTime = mStartDateTime;
        this.mEndDateTime = mEndDateTime;
    }

    public Medication(long mId, String mName, String mDescription, int mFrequency, long mStartDateTime, long mEndDateTime) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mFrequency = mFrequency;
        this.mStartDateTime = mStartDateTime;
        this.mEndDateTime = mEndDateTime;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }

    public long getStartDateTime() {
        return mStartDateTime;
    }

    public void setStartDateTime(long mStartDateTime) {
        this.mStartDateTime = mStartDateTime;
    }

    public long getEndDateTime() {
        return mEndDateTime;
    }

    public void setEndDateTime(long mEndDateTime) {
        this.mEndDateTime = mEndDateTime;
    }

    public String toString(){
        return "Medication = id: " +mId +"\n name: "+ mName +"\n description: "
                +mDescription + "\n frequency: " + mFrequency +"\n startdate: "
                +mStartDateTime +"\n enddate: "+ mEndDateTime;
    }
}

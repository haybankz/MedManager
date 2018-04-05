package com.haybankz.medmanager.model;

public class Reminder {

    private long mId;
    private long mMedicationId;
    private long mReminderDateTime;
    private boolean mTaken;

    public Reminder(long mId, long mMedicationId, long mReminderDateTime, boolean mTaken) {
        this.mId = mId;
        this.mMedicationId = mMedicationId;
        this.mReminderDateTime = mReminderDateTime;
        this.mTaken = mTaken;
    }

    public Reminder(long mMedicationId, long mReminderDateTime, boolean mTaken) {
        this.mMedicationId = mMedicationId;
        this.mReminderDateTime = mReminderDateTime;
        this.mTaken = mTaken;
    }

    public long getId() {
        return mId;
    }

//    public void setId(long mId) {
//        this.mId = mId;
//    }

    public long getMedicationId() {
        return mMedicationId;
    }

//    public void setMedicationId(long mMedicationId) {
//        this.mMedicationId = mMedicationId;
//    }

    public long getReminderDateTime() {
        return mReminderDateTime;
    }

//    public void setReminderDateTime(long mReminderDateTime) {
//        this.mReminderDateTime = mReminderDateTime;
//    }

    public boolean isTaken() {
        return mTaken;
    }

//    public void setTaken(boolean mTaken) {
//        this.mTaken = mTaken;
//    }

    @Override
    public String toString(){
        return "Reminder:: id: "+ mId +
                "\nmedicationId: " + mMedicationId +
                "\nreminderDateTime: " + mReminderDateTime +
                "\nisTaken: " + mTaken;
    }
}

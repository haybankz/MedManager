package com.haybankz.medmanager.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.haybankz.medmanager.receiver.AlarmReceiver;
import com.haybankz.medmanager.data.reminder.ReminderContract.ReminderEntry;
import com.haybankz.medmanager.model.Reminder;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderDbUtils {


    public static ArrayList<Reminder> getRemindersByMedication(Context context, long medicationId){
        ArrayList<Reminder> reminders = new ArrayList<>();

        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_MEDICATION_ID,
                ReminderEntry.COLUMN_REMINDER_DATE_TIME,
                ReminderEntry.COLUMN_REMINDER_TAKEN
        };

        String selection = ReminderEntry.COLUMN_MEDICATION_ID + "=?";
        String[] selectionArgs = {String.valueOf(medicationId)};
        String sortOrder = ReminderEntry.COLUMN_REMINDER_DATE_TIME +" ASC";


        Cursor c =  context.getContentResolver().query(ReminderEntry.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);

        if(c != null){

            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndexOrThrow(ReminderEntry._ID));
                long medId = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_MEDICATION_ID));
                long date = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_DATE_TIME));
                boolean taken = c.getInt(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_TAKEN)) == 1;
                Reminder reminder = new Reminder(id, medId, date, taken);
                reminders.add(reminder);
            }
            c.close();

        }else{
            return null;
        }

        return reminders;
    }

    private static int updateReminder(Context context, long id, ContentValues values){

        Uri uri = ContentUris.withAppendedId(ReminderEntry.CONTENT_URI, id);

        return context.getContentResolver().update(uri, values, null, null);


    }

    public static int ReminderTaken(Context context, long id){
        ContentValues values = new ContentValues();
        values.put(ReminderEntry.COLUMN_REMINDER_TAKEN, 1);

        return updateReminder(context, id, values);
    }

    public static int ReminderRefuse(Context context, long id){
        ContentValues values = new ContentValues();
        values.put(ReminderEntry.COLUMN_REMINDER_TAKEN, 0);

        return updateReminder(context, id, values);
    }

    public static int deleteReminder(Context context, long id){

        Uri uri = ContentUris.withAppendedId(ReminderEntry.CONTENT_URI, id);

        return context.getContentResolver().delete(uri, null, null);
    }


    public static int deleteRemindersOfMedication(Context context, long medicationId){

        String selection = ReminderEntry.COLUMN_MEDICATION_ID + "=?";
        String[] selectionArgs = {String.valueOf(medicationId)};


        return context.getContentResolver().delete(ReminderEntry.CONTENT_URI, selection, selectionArgs);
    }



    public static Uri insertReminder(Context context, ContentValues values){

        Uri uri = context.getContentResolver().insert(ReminderEntry.CONTENT_URI, values);
        Log.e("ReminderDbUtils", "insertReminder: " + uri );

        AlarmReceiver alarmReceiver = new AlarmReceiver();
        if(uri != null){
            long startTimeDateInMillis = values.getAsLong(ReminderEntry.COLUMN_REMINDER_DATE_TIME);
            if(startTimeDateInMillis > Calendar.getInstance().getTimeInMillis()) {
                int id = (int) ContentUris.parseId(uri);
                alarmReceiver.setAlarm(context, startTimeDateInMillis, id);
            }
        }


        return uri;
    }


    public static Reminder getReminder(Context context, long reminderId){
        Reminder reminder = null;
        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_MEDICATION_ID,
                ReminderEntry.COLUMN_REMINDER_DATE_TIME,
                ReminderEntry.COLUMN_REMINDER_TAKEN
        };

        Uri uri = ContentUris.withAppendedId(ReminderEntry.CONTENT_URI, reminderId);

        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        if( c != null ){
            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndexOrThrow(ReminderEntry._ID));
                long medicationId = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_MEDICATION_ID));
                long reminderDateTime = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_DATE_TIME));
                boolean taken = c.getInt(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_TAKEN)) == 1;
                reminder = new Reminder(id, medicationId, reminderDateTime, taken);

            }
            c.close();
        }

        return reminder;
    }

    public static ArrayList<Reminder> getAllRemindersToRingAfterCurrentTime(Context context){
        ArrayList<Reminder> reminders = new ArrayList<>();
        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_MEDICATION_ID,
                ReminderEntry.COLUMN_REMINDER_DATE_TIME,
                ReminderEntry.COLUMN_REMINDER_TAKEN
        };

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        String selection = ReminderEntry.COLUMN_REMINDER_DATE_TIME + "> ?";
        String[] selectionArgs = {String.valueOf(currentTimeInMillis)};



        Cursor c = context.getContentResolver().query(ReminderEntry.CONTENT_URI, projection, selection,
                selectionArgs, null);

        if( c != null ){

            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndexOrThrow(ReminderEntry._ID));
                long medicationId = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_MEDICATION_ID));
                long reminderDateTime = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_DATE_TIME));
                boolean taken = c.getInt(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_TAKEN)) == 1;
                Reminder reminder = new Reminder(id, medicationId, reminderDateTime, taken);

                reminders.add(reminder);

            }
            c.close();

            return reminders;
        }

        return null;
    }


    public static ArrayList<Reminder> getAllRemindersThatRangBeforeCurrentTime(Context context){
        ArrayList<Reminder> reminders = new ArrayList<>();
        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_MEDICATION_ID,
                ReminderEntry.COLUMN_REMINDER_DATE_TIME,
                ReminderEntry.COLUMN_REMINDER_TAKEN
        };

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        String selection = ReminderEntry.COLUMN_REMINDER_DATE_TIME + "< ?";
        String[] selectionArgs = {String.valueOf(currentTimeInMillis)};



        Cursor c = context.getContentResolver().query(ReminderEntry.CONTENT_URI, projection, selection,
                selectionArgs, null);

        if( c != null ){

            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndexOrThrow(ReminderEntry._ID));
                long medicationId = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_MEDICATION_ID));
                long reminderDateTime = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_DATE_TIME));
                boolean taken = c.getInt(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_TAKEN)) == 1;
                Reminder reminder = new Reminder(id, medicationId, reminderDateTime, taken);

                reminders.add(reminder);

            }
            c.close();

            return reminders;
        }

        return null;
    }


    public static ArrayList<Reminder> getAllReminders(Context context){
        ArrayList<Reminder> reminders = new ArrayList<>();
        String[] projection = {
                ReminderEntry._ID,
                ReminderEntry.COLUMN_MEDICATION_ID,
                ReminderEntry.COLUMN_REMINDER_DATE_TIME,
                ReminderEntry.COLUMN_REMINDER_TAKEN
        };



        String sortOrder = ReminderEntry.COLUMN_REMINDER_DATE_TIME + " ASC";

        Cursor c = context.getContentResolver().query(ReminderEntry.CONTENT_URI, projection, null,
                null, sortOrder);

        if( c != null ){


            while(c.moveToNext()){
                long id = c.getLong(c.getColumnIndexOrThrow(ReminderEntry._ID));
                long medicationId = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_MEDICATION_ID));
                long reminderDateTime = c.getLong(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_DATE_TIME));
                boolean taken = c.getInt(c.getColumnIndexOrThrow(ReminderEntry.COLUMN_REMINDER_TAKEN)) == 1;
                Reminder reminder = new Reminder(id, medicationId, reminderDateTime, taken);

                reminders.add(reminder);

            }
            c.close();


        }

        if(reminders.size() == 0){
            reminders = null;
        }

        return reminders;
    }

}

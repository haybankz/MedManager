package com.haybankz.medmanager.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.data.reminder.ReminderContract;
import com.haybankz.medmanager.data.reminder.ReminderContract.ReminderEntry;
import com.haybankz.medmanager.model.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class MedicationDbUtils {

    public static Uri insertMedication(Context context, ContentValues values){

        Uri uri =  context.getContentResolver().insert(MedicationEntry.CONTENT_URI, values);

        if(uri != null){
            long medicationId = ContentUris.parseId(uri);
            long startDateTime =  values.getAsLong(MedicationEntry.COLUMN_MEDICATION_START_DATE);
            long endDateTime = values.getAsLong(MedicationEntry.COLUMN_MEDICATION_END_DATE);
            int frequency = values.getAsInteger(MedicationEntry.COLUMN_MEDICATION_FREQUENCY);
            long frequencyInMillis = DateTimeUtils.getFrequencyInMilliseconds(frequency);

            ContentValues reminderValues = new ContentValues();
            reminderValues.put(ReminderEntry.COLUMN_MEDICATION_ID, medicationId);



            while(startDateTime < endDateTime ){
                reminderValues.put(ReminderEntry.COLUMN_REMINDER_DATE_TIME, startDateTime);
                ReminderDbUtils.insertReminder(context, reminderValues);

                startDateTime += frequencyInMillis;
            }



        }

        return uri;
    }

    public static int addBulkMedication(Context context, ContentValues[] values){

        return context.getContentResolver().bulkInsert(MedicationEntry.CONTENT_URI, values);
    }

    public static ArrayList<Medication> getMedicationsByName(Context context, String s){

       ArrayList<Medication> medications = new ArrayList<>();

        String[] projection = {
                MedicationEntry._ID,
                MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationEntry.COLUMN_MEDICATION_DOSAGE,

                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE,
                MedicationEntry.COLUMN_MEDICATION_ACTIVE
        };

        String selection = MedicationEntry.COLUMN_MEDICATION_NAME + " like ?";
        String[] selectionArgs = new String[]{"%" + s + "%"};
        String sortOrder = MedicationEntry.COLUMN_MEDICATION_NAME + " ASC";



        Cursor c = context.getContentResolver().query(MedicationEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                String dosage = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DOSAGE));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));
                boolean active =  c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_ACTIVE)) == 1;

                Medication medication = new Medication(id, name, description, dosage, frequency, startDate, endDate, active);

                medications.add(medication);
            }
            c.close();
        }

        if(medications.size() == 0) return null;

        return medications;
    }

    public static Medication getMedicationById(Context context, long medId){

        Medication medication = null;

        Uri uri = ContentUris.withAppendedId( MedicationEntry.CONTENT_URI, medId );

        String[] projection = {MedicationEntry._ID,
                MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationEntry.COLUMN_MEDICATION_DOSAGE,

                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE,
                MedicationEntry.COLUMN_MEDICATION_ACTIVE
        };


        Cursor c = context.getContentResolver().query(uri, projection, null, null, null );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                String dosage = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DOSAGE));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));
                boolean active =  c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_ACTIVE)) == 1;

                medication = new Medication(id, name, description, dosage, frequency, startDate, endDate, active);


            }
            c.close();
        }



        return medication;
    }

    public static ArrayList<Medication> getAllMedications(Context context){

        ArrayList<Medication> medications = new ArrayList<>();

        String[] projection = {MedicationEntry._ID,
                MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationEntry.COLUMN_MEDICATION_DOSAGE,
                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE,
                MedicationEntry.COLUMN_MEDICATION_ACTIVE
        };

        String sortOrder = MedicationEntry._ID + " DESC";


        Cursor c = context.getContentResolver().query(MedicationEntry.CONTENT_URI, projection, null, null, sortOrder );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                String dosage = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DOSAGE));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));
                boolean active =  c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_ACTIVE)) == 1 ;


                Medication medication = new Medication(id, name, description, dosage, frequency, startDate, endDate, active);

                medications.add(medication);
            }

            c.close();
        }



        if(medications.size() == 0) {
            medications = null;
        }

        return medications;

    }

    public static int updateMedication(Context context, long id, ContentValues values){

//        String selection = MedicationEntry._ID + "=?";
//        String[] selectionArgs = {String.valueOf(id)};

        Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, id);

        return context.getContentResolver().update(uri, values, null, null);

    }

    public static int deactivateMedication(Context context, long id){
        ContentValues values = new ContentValues();
        values.put(MedicationEntry.COLUMN_MEDICATION_ACTIVE, 0);

        Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, id);

        return context.getContentResolver().update(uri, values, null, null);
    }

    public static int activateMedication(Context context, long id){
        ContentValues values = new ContentValues();
        values.put(MedicationEntry.COLUMN_MEDICATION_ACTIVE, 1);

        Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, id);

        return context.getContentResolver().update(uri, values, null, null);
    }

    public static int deleteMedication(Context context, long id){

        Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, id);


        int deleted =  context.getContentResolver().delete(uri, null, null);

        if(deleted > 0){
            ReminderDbUtils.deleteRemindersOfMedication(context, id);
        }

        return deleted;
    }

}

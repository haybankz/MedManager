package com.haybankz.medmanager.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.model.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class MedicationDbUtils {

    public static Uri saveMedication(Context context, ContentValues values){

        return context.getContentResolver().insert(MedicationEntry.CONTENT_URI, values);
    }

    public static int addBulkMedication(Context context, ContentValues[] values){

        return context.getContentResolver().bulkInsert(MedicationEntry.CONTENT_URI, values);
    }

    public static ArrayList<Medication> getMedicationsByName(Context context, String s){

       ArrayList<Medication> medications = new ArrayList<>();

        String[] projection = {MedicationEntry._ID,
                MedicationEntry.COLUMN_MEDICATION_NAME,
                MedicationEntry.COLUMN_MEDICATION_DESCRIPTION,
                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE
        };

        String selection = MedicationEntry.COLUMN_MEDICATION_NAME + " like ?";
        String[] selectionArgs = new String[]{s +"%"};
        String sortOrder = MedicationEntry.COLUMN_MEDICATION_NAME + " ASC";



        Cursor c = context.getContentResolver().query(MedicationEntry.CONTENT_URI, projection, selection, selectionArgs, sortOrder );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));

                Medication medication = new Medication(id, name, description, frequency, startDate, endDate);

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
                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE
        };


        Cursor c = context.getContentResolver().query(uri, projection, null, null, null );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));

                medication = new Medication(id, name, description, frequency, startDate, endDate);


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
                MedicationEntry.COLUMN_MEDICATION_FREQUENCY,
                MedicationEntry.COLUMN_MEDICATION_START_DATE,
                MedicationEntry.COLUMN_MEDICATION_END_DATE
        };

        String sortOrder = MedicationEntry._ID + " DESC";


        Cursor c = context.getContentResolver().query(MedicationEntry.CONTENT_URI, projection, null, null, sortOrder );

        if(c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MedicationEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_NAME));
                String description = c.getString(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
                int frequency = c.getInt(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
                long startDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_START_DATE));
                long endDate = c.getLong(c.getColumnIndexOrThrow(MedicationEntry.COLUMN_MEDICATION_END_DATE));

                Medication medication = new Medication(id, name, description, frequency, startDate, endDate);

                medications.add(medication);
            }

            c.close();
        }



        if(medications.size() == 0) {
            medications = null;
        }

        return medications;

    }

}

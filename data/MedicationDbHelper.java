package com.haybankz.medmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.data.reminder.ReminderContract;
import com.haybankz.medmanager.data.reminder.ReminderContract.ReminderEntry;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class MedicationDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "med_manager.db";


    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_MEDICATION_ENTRIES = "CREATE TABLE "+ MedicationEntry.TABLE_NAME + " ( " +
            MedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MedicationEntry.COLUMN_MEDICATION_NAME + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_DESCRIPTION + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_FREQUENCY + " INTEGER" + COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_START_DATE + " INTEGER"  + COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_END_DATE + " INTEGER" + COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_ACTIVE + " INTEGER NOT NULL DEFAULT 1 " + COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_MONTH + " INTEGER" +

            " )";

    private static final String SQL_CREATE_REMINDER_ENTRIES = "CREATE TABLE "+ ReminderEntry.TABLE_NAME +" ( " +
            ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            ReminderEntry.COLUMN_MEDICATION_ID + " INTEGER NOT NULL" + COMMA_SEP +
            ReminderEntry.COLUMN_REMINDER_TAKEN + " INTEGER NOT NULL DEFAULT 0" + COMMA_SEP +
            ReminderEntry.COLUMN_REMINDER_DATE_TIME +" INTEGER NOT NULL" +

            " )";


    private static final String SQL_DELETE_MEDICATION_ENTRIES = "DROP TABLE IF EXISTS "+ MedicationEntry.TABLE_NAME;
    private static final String SQL_DELETE_REMINDER_ENTRIES = "DROP TABLE IF EXISTS "+ ReminderEntry.TABLE_NAME;


    public MedicationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MEDICATION_ENTRIES);
        db.execSQL(SQL_CREATE_REMINDER_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_MEDICATION_ENTRIES);
        db.execSQL(SQL_DELETE_REMINDER_ENTRIES);

        onCreate(db);
    }



}

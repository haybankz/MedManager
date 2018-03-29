package com.haybankz.medmanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class MedicationDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "med_manager.db";


    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_MEDICATION_ENTRIES = "CREATE TABLE "+ MedicationEntry.TABLE_NAME + " ( " +
            MedicationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MedicationEntry.COLUMN_MEDICATION_NAME + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_DESCRIPTION + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
            MedicationEntry.COLUMN_MEDICATION_FREQUENCY + " INTEGER," +
            MedicationEntry.COLUMN_MEDICATION_START_DATE + " INTEGER,"  +
            MedicationEntry.COLUMN_MEDICATION_END_DATE + " INTEGER," +
            MedicationEntry.COLUMN_MEDICATION_MONTH + " INTEGER" +

            " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST "+ MedicationEntry.TABLE_NAME;


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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);

        onCreate(db);
    }



}

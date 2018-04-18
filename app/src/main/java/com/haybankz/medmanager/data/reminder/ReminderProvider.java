package com.haybankz.medmanager.data.reminder;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.haybankz.medmanager.data.MedicationDbHelper;
import com.haybankz.medmanager.model.Medication;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class ReminderProvider extends ContentProvider{

    private MedicationDbHelper medicationDbHelper;
    private static final int REMINDERS = 2000;
    private static  final int REMINDERS_ID = 2001;
    private static final String REMINDERS_AUTHORITY = "com.haybankz.medmanager.reminder";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        sUriMatcher.addURI(REMINDERS_AUTHORITY, ReminderContract.PATH_REMINDERS, REMINDERS);


        sUriMatcher.addURI(REMINDERS_AUTHORITY, ReminderContract.PATH_REMINDERS + "/#", REMINDERS_ID);



    }

    @Override
    public boolean onCreate() {
        medicationDbHelper = new MedicationDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = medicationDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch(match){

            case REMINDERS:
                cursor = database.query(ReminderContract.ReminderEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case REMINDERS_ID:
                selection = ReminderContract.ReminderEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ReminderContract.ReminderEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown uri: "+uri);


        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {

        int match = sUriMatcher.match(uri);
        switch(match){
            case REMINDERS:
                getContext().getContentResolver().notifyChange(uri, null);
                return insertMedication(uri, values);

            default:
                throw  new IllegalArgumentException("Insertion is not supported for: "+uri);
        }

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = medicationDbHelper.getWritableDatabase();

        int result;
        switch (match){
            case REMINDERS:
                result = database.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case REMINDERS_ID:
                selection = ReminderContract.ReminderEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = database.delete(ReminderContract.ReminderEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            default:
                throw new IllegalArgumentException("Deleting is not supported for "+uri);
        }


    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        SQLiteDatabase database = medicationDbHelper.getWritableDatabase();
        int result;

        switch(match){
            case REMINDERS:
                result = database.update(ReminderContract.ReminderEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case REMINDERS_ID:
                selection = ReminderContract.ReminderEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                result = database.update(ReminderContract.ReminderEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            default:
                throw new IllegalArgumentException("Editing is not supported for :"+uri);

        }

    }

    private Uri insertMedication(Uri uri, ContentValues values){

        SQLiteDatabase database = medicationDbHelper.getWritableDatabase();
        long newRowId = database.insert(ReminderContract.ReminderEntry.TABLE_NAME, null, values);
        if(newRowId == -1){
            Toast.makeText(getContext(), "Failed to insert row for: " + uri, Toast.LENGTH_LONG).show();
            return null;
        }

        String id = String.valueOf(newRowId);

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(uri, id);
    }



}

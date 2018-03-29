package com.haybankz.medmanager.data.medication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.haybankz.medmanager.data.MedicationDbHelper;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class MedicationProvider extends ContentProvider{

    private MedicationDbHelper medicationDbHelper;
    private static final int MEDICATIONS = 1000;
    private static  final int MEDICATIONS_ID = 1001;
    private static final String MEDICATIONS_AUTHORITY = "com.haybankz.medmanager";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{

        sUriMatcher.addURI(MEDICATIONS_AUTHORITY, MedicationContract.PATH_MEDICATIONS, MEDICATIONS);


        sUriMatcher.addURI(MEDICATIONS_AUTHORITY, MedicationContract.PATH_MEDICATIONS + "/#", MEDICATIONS_ID);



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

            case MEDICATIONS:
                cursor = database.query(MedicationContract.MedicationEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;

            case MEDICATIONS_ID:
                selection = MedicationContract.MedicationEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MedicationContract.MedicationEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

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
            case MEDICATIONS:
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
            case MEDICATIONS:
                result = database.delete(MedicationContract.MedicationEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case MEDICATIONS_ID:
                selection = MedicationContract.MedicationEntry._ID +"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = database.delete(MedicationContract.MedicationEntry.TABLE_NAME, selection, selectionArgs);
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
            case MEDICATIONS:
                result = database.update(MedicationContract.MedicationEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            case MEDICATIONS_ID:
                selection = MedicationContract.MedicationEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                result = database.update(MedicationContract.MedicationEntry.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return result;

            default:
                throw new IllegalArgumentException("Editing is not supported for :"+uri);

        }

    }

    private Uri insertMedication(Uri uri, ContentValues values){

        SQLiteDatabase database = medicationDbHelper.getWritableDatabase();
        long newRowId = database.insert(MedicationContract.MedicationEntry.TABLE_NAME, null, values);
        if(newRowId == -1){
            Toast.makeText(getContext(), "Failed to insert row for: " + uri, Toast.LENGTH_LONG).show();
            return null;
        }

        String id = String.valueOf(newRowId);

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(uri, id);
    }



}

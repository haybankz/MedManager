package com.haybankz.medmanager.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.haybankz.medmanager.data.user.UserContract.UserEntry;
import com.haybankz.medmanager.model.User;

public class UserDbUtils {


    public static int signUp(Context context, ContentValues values){

        Uri uri = context.getContentResolver().insert(UserEntry.CONTENT_URI, values);

        return (int) ContentUris.parseId(uri);
    }

    public static User login(Context context, String userNameOrEmail, String password){

        User user = null;

        String[] projection = {
                UserEntry.COLUMN_USER_ID,
            UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_GIVEN_NAME,
                UserEntry.COLUMN_USER_FAMILY_NAME,
                UserEntry.COLUMN_USER_DISPLAY_NAME,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_PASSWORD,
                UserEntry.COLUMN_USER_PHOTO_URL
        };
        String selection = UserEntry.COLUMN_USER_EMAIL + "=? and " + UserEntry.COLUMN_USER_PASSWORD + "=?" ;
        String[] selectionArgs = {userNameOrEmail, password};

        Cursor c = context.getContentResolver().query(UserEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        if(c != null){

            while(c.moveToNext()){
                int id = c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_ID));
                String profileId = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PROFILE_ID));
                String givenName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_GIVEN_NAME));
                String familyName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_FAMILY_NAME));
                String email = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_EMAIL));
                String photoUrl = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PHOTO_URL));
                String displayName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_DISPLAY_NAME));
                String pass = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PASSWORD));

                user = new User(id, profileId, givenName, familyName, displayName, email, pass, photoUrl);
            }

            c.close();
        }

        return user;
    }

    public static User loginUser(Context context, String userNameOrEmail){

        User user = null;

        String[] projection = {
                UserEntry.COLUMN_USER_ID,
                UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_GIVEN_NAME,
                UserEntry.COLUMN_USER_FAMILY_NAME,
                UserEntry.COLUMN_USER_DISPLAY_NAME,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_PASSWORD,
                UserEntry.COLUMN_USER_PHOTO_URL
        };
        String selection = UserEntry.COLUMN_USER_EMAIL + "=?" ;
        String[] selectionArgs = {userNameOrEmail};

        Cursor c = context.getContentResolver().query(UserEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        if(c != null){

            while(c.moveToNext()){
                int id = c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_ID));
                String profileId = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PROFILE_ID));
                String givenName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_GIVEN_NAME));
                String familyName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_FAMILY_NAME));
                String email = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_EMAIL));
                String photoUrl = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PHOTO_URL));
                String displayName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_DISPLAY_NAME));
                String password = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PASSWORD));

                user = new User(id, profileId, givenName, familyName, displayName, email, password, photoUrl);
            }

            c.close();
        }

        return user;
    }

    public static User getUserById(Context context, long userId){

        User user = null;

        String[] projection = {
                UserEntry.COLUMN_USER_ID,
                UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_PROFILE_ID,
                UserEntry.COLUMN_USER_GIVEN_NAME,
                UserEntry.COLUMN_USER_FAMILY_NAME,
                UserEntry.COLUMN_USER_DISPLAY_NAME,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_PASSWORD,
                UserEntry.COLUMN_USER_PHOTO_URL
        };

        Uri uri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, userId);

        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);

        if(c != null){

            while(c.moveToNext()){
                int id = c.getInt(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_ID));
                String profileId = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PROFILE_ID));
                String givenName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_GIVEN_NAME));
                String familyName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_FAMILY_NAME));
                String email = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_EMAIL));
                String photoUrl = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PHOTO_URL));
                String displayName = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_DISPLAY_NAME));
                String password = c.getString(c.getColumnIndexOrThrow(UserEntry.COLUMN_USER_PASSWORD));

                user = new User(id, profileId, givenName, familyName, displayName, email, password, photoUrl);
            }

            c.close();
        }

        return user;
    }

    public static int updateUser(Context context, int id, ContentValues values){

        Uri uri = ContentUris.withAppendedId(UserEntry.CONTENT_URI, id);

        return context.getContentResolver().update(uri, values, null, null);
    }


}

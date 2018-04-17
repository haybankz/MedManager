package com.haybankz.medmanager.data.user;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class UserContract {

    private UserContract(){}

    public static final String PATH_USERS = "users";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.haybankz.medmanager.user/");

    public static final class UserEntry implements BaseColumns {

        public final static String TABLE_NAME = "users";


        public static final String COLUMN_USER_ID = BaseColumns._ID;
        public static final String COLUMN_USER_PROFILE_ID = "profile_id";
        public static final String COLUMN_USER_DISPLAY_NAME = "display_name";
        public static final String COLUMN_USER_GIVEN_NAME = "given_name";
        public static final String COLUMN_USER_FAMILY_NAME = "family_name";
        public static final String COLUMN_USER_PHOTO_URL = "photo_url";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_PASSWORD = "password";



        public final static String COLUMN_NAME_NULLABLE = null;

        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);






    }

}

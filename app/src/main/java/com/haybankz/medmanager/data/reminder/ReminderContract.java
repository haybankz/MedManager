package com.haybankz.medmanager.data.reminder;


import android.net.Uri;
import android.provider.BaseColumns;

import com.haybankz.medmanager.data.medication.MedicationContract;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class ReminderContract {

    private ReminderContract(){}

    public static final String PATH_REMINDERS = "reminders";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.haybankz.medmanager.reminder/");

    public static final class ReminderEntry implements BaseColumns {

        public final static String TABLE_NAME = "reminders";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MEDICATION_ID = "medication_id";
        public final static String COLUMN_REMINDER_TAKEN = "taken";
        public final static String COLUMN_REMINDER_DATE_TIME = "reminder_date_time";


        public final static String COLUMN_NAME_NULLABLE = null;

        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REMINDERS);






    }

}

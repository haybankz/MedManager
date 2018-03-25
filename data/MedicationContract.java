package com.haybankz.medmanager.data;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by LENOVO on 3/21/2018.
 */

public class MedicationContract {

    private MedicationContract(){}

    public static final String PATH_MEDICATIONS = "medications";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.haybankz.medmanager/");

    public static final class MedicationEntry implements BaseColumns {

        public final static String TABLE_NAME = "medications";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MEDICATION_NAME = "name";
        public final static String COLUMN_MEDICATION_DESCRIPTION = "description";
        public final static String COLUMN_MEDICATION_FREQUENCY = "frequency";
        public final static String COLUMN_MEDICATION_START_DATE = "start_date";
        public final static String COLUMN_MEDICATION_END_DATE = "end_date";
        public final static String COLUMN_MEDICATION_MONTH = "month";


        public final static String COLUMN_MEDICATION_CATEGORY= "category";

        public final static String COLUMN_NAME_NULLABLE = null;



        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEDICATIONS);





    }

}

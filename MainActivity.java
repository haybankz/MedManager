package com.haybankz.medmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.haybankz.medmanager.data.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.util.DateTimeUtils;

import java.util.Calendar;



public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    EditText mNameEditText;
    EditText mDescriptionEditText;

    Spinner mFrequencySpinner;

    TextView mStartDateTextView;
    TextView mEndDateTextView;

    FloatingActionButton mStartDateFab;
    FloatingActionButton mEndDateFab;

    FloatingActionButton saveFab;



    private Context mContext = this;
    private Activity mActivity = this;



    private Calendar mCalendar;

    private DatePickerDialog mStartDatePickerDialog;
    private TimePickerDialog mStartTimePickerDialog;

    private DatePickerDialog mEndDatePickerDialog;
    private TimePickerDialog mEndTimePickerDialog;
    private TimePicker mStartTimePicker;
    private TimePicker mEndTimePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameEditText = findViewById(R.id.et_med_name);
        mDescriptionEditText =  findViewById(R.id.et_med_descr);

        mFrequencySpinner =  findViewById(R.id.spn_freq);

        mStartDateTextView =  findViewById(R.id.tv_start_date);
        mEndDateTextView =  findViewById(R.id.tv_end_date);

        mStartDateFab =  findViewById(R.id.fabtn_start_date);
        mEndDateFab =  findViewById(R.id.fabtn_end_date);

        saveFab = findViewById(R.id.fbtn_save);


        // to hide keyboard at the start  of activity, keyboard shows up cos first view in the activity is an editText
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mFrequencySpinner = findViewById(R.id.spn_freq);
        ArrayAdapter<CharSequence> aAdpt = ArrayAdapter.createFromResource(this, R.array.frequency_array, android.R.layout.simple_spinner_item);
        aAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFrequencySpinner.setAdapter(aAdpt);


        mCalendar = Calendar.getInstance();
        Long dateInMillis = mCalendar.getTimeInMillis();

        mStartDateTextView.setText(DateTimeUtils.getDateTimeString(dateInMillis));



    setUpStartDateAndTimePickerDialog();
    setUpEndDateAndTimePickerDialog();



        mStartDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mStartDatePickerDialog.show();
            }
        });





        mEndDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mEndDatePickerDialog.show();
            }
        });




        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMedication();
            }
        });



    }


    public void addMedication(){

        DatePicker startDatePicker = mStartDatePickerDialog.getDatePicker();
        DatePicker endDatePicker = mEndDatePickerDialog.getDatePicker();
        int sYear = startDatePicker.getYear(), sMonth = startDatePicker.getMonth(),
                sDay = startDatePicker.getDayOfMonth(), sHour = 0, sMinute = 0;

        int eYear = endDatePicker.getYear(), eMonth = endDatePicker.getMonth(),
                eDay = endDatePicker.getDayOfMonth(), eHour = 0, eMinute = 0;

        if(mStartTimePicker != null ){
            sHour = mStartTimePicker.getCurrentHour();
            sMinute = mStartTimePicker.getCurrentMinute();
        }

        if(mEndTimePicker != null){
            eHour = mEndTimePicker.getCurrentHour();
            eMinute = mEndTimePicker.getCurrentMinute();
        }

        String name = mNameEditText.getText().toString().trim();
        String description = mDescriptionEditText.getText().toString().trim();

        long startDateTimeInMillis = DateTimeUtils.getDateTimeInMilliseconds(sYear, sMonth, sDay, sHour, sMinute);
        long endDateTimeInMillis = DateTimeUtils.getDateTimeInMilliseconds(eYear, eMonth, eDay, eHour, eMinute);

        ContentValues contentValues = new ContentValues();

        if(!(TextUtils.isEmpty(name) || TextUtils.isEmpty(description))) {

            contentValues.put(MedicationEntry.COLUMN_MEDICATION_NAME, name);
            contentValues.put(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION, description);
            contentValues.put(MedicationEntry.COLUMN_MEDICATION_FREQUENCY, mFrequencySpinner.getSelectedItemPosition() + 1);
            contentValues.put(MedicationEntry.COLUMN_MEDICATION_START_DATE, startDateTimeInMillis );
            contentValues.put(MedicationEntry.COLUMN_MEDICATION_END_DATE, endDateTimeInMillis);

            Log.e(TAG, "addMedication: " + contentValues.toString());
            Uri medicationUri = getContentResolver().insert(MedicationEntry.CONTENT_URI, contentValues);

            if(medicationUri != null){
                Toast.makeText(mContext, "Medication created :" + medicationUri.getPath(), Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(mContext, "Please fill details properly", Toast.LENGTH_SHORT).show();
        }


    }

    public void setUpStartDateAndTimePickerDialog(){

        final int cYear, cMonth, cDay;
        Log.d(TAG, "onClick: stardate fab clicked!!!!!");
        final Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);

        mStartDatePickerDialog = new DatePickerDialog(mContext,new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {


                int cHour, cMinute;
                mCalendar = Calendar.getInstance();
                cHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cMinute = mCalendar.get(Calendar.MINUTE);

                mStartTimePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mStartTimePicker =  view;
                        long dateTimeInMilliseconds = DateTimeUtils.getDateTimeInMilliseconds(year,
                                month, dayOfMonth, hourOfDay, minute);

                        mStartDateTextView.setText(DateTimeUtils.getDateTimeString(dateTimeInMilliseconds));

                    }
                }, cHour, cMinute, false);




                mStartTimePickerDialog.show();
            }
        }, cYear, cMonth, cDay);


    }

    private void setUpEndDateAndTimePickerDialog(){
        final int cYear, cMonth, cDay;
        Log.d(TAG, "onClick: stardate fab clicked!!!!!");
        final Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);

        mEndDatePickerDialog = new DatePickerDialog(mContext,new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {


                int cHour, cMinute;
                mCalendar = Calendar.getInstance();
                cHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cMinute = mCalendar.get(Calendar.MINUTE);

                mEndTimePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mEndTimePicker = view;

                        long dateTimeInMilliseconds = DateTimeUtils.getDateTimeInMilliseconds(year,
                                month, dayOfMonth, hourOfDay, minute);

                        mEndDateTextView.setText(DateTimeUtils.getDateTimeString(dateTimeInMilliseconds));

                    }
                }, cHour, cMinute, false);


                mEndTimePickerDialog.show();
            }
        }, cYear, cMonth, cDay);

    }







}

package com.haybankz.medmanager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.haybankz.medmanager.utils.DateTimeUtils;

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
//        mFrequencySpinner.setSelection(3, true);


        mCalendar = Calendar.getInstance();
        Long dateInMillis = mCalendar.getTimeInMillis();

        mStartDateTextView.setText(DateTimeUtils.getDateTimeString(dateInMillis));



        mStartDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                                long dateTimeInMilliseconds = DateTimeUtils.getDateTimeInMilliseconds(year,
                                        month, dayOfMonth, hourOfDay, minute);

                                mStartDateTextView.setText(DateTimeUtils.getDateTimeString(dateTimeInMilliseconds));

                            }
                        }, cHour, cMinute, false);


                        mStartTimePickerDialog.show();
                    }
                }, cYear, cMonth, cDay);

                mStartDatePickerDialog.show();
            }
        });



        mEndDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                                long dateTimeInMilliseconds = DateTimeUtils.getDateTimeInMilliseconds(year,
                                        month, dayOfMonth, hourOfDay, minute);

                                mEndDateTextView.setText(DateTimeUtils.getDateTimeString(dateTimeInMilliseconds));

                            }
                        }, cHour, cMinute, false);


                        mEndTimePickerDialog.show();
                    }
                }, cYear, cMonth, cDay);

                mEndDatePickerDialog.show();
            }
        });



        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }






}

package com.haybankz.medmanager.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.data.medication.MedicationContract;
import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;

import java.util.Calendar;


public class EditMedicationActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    EditText mNameEditText;
    EditText mDescriptionEditText;

    Spinner mFrequencySpinner;

    TextView mStartDateTextView;
    TextView mEndDateTextView;

    FloatingActionButton mStartDateFab;
    FloatingActionButton mEndDateFab;


    private Context mContext = this;
    private Activity mActivity = this;



    private Calendar mCalendar;

    private DatePickerDialog mStartDatePickerDialog;
    private TimePickerDialog mStartTimePickerDialog;

    private DatePickerDialog mEndDatePickerDialog;
    private TimePickerDialog mEndTimePickerDialog;

    private long mStartDateTimeInMillis = 0l;
    private long mEndDateTimeInMillis = 0l;

    private long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_medication);



        mNameEditText = findViewById(R.id.et_med_name);
        mDescriptionEditText =  findViewById(R.id.et_med_descr);

        mFrequencySpinner =  findViewById(R.id.spn_freq);

        mStartDateTextView =  findViewById(R.id.tv_start_date);
        mEndDateTextView =  findViewById(R.id.tv_end_date);

        mStartDateFab =  findViewById(R.id.fabtn_start_date);
        mEndDateFab =  findViewById(R.id.fabtn_end_date);

        FloatingActionButton fab = findViewById(R.id.fbtn_save);
        fab.setVisibility(View.GONE);


        // to hide keyboard at the start  of activity, keyboard shows up cos first view in the activity is an editText
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mFrequencySpinner = findViewById(R.id.spn_freq);
        ArrayAdapter<CharSequence> aAdpt = ArrayAdapter.createFromResource(this, R.array.frequency_array, android.R.layout.simple_spinner_item);
        aAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFrequencySpinner.setAdapter(aAdpt);





        mStartDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setUpStartDateAndTimePickerDialog();
                mStartDatePickerDialog.show();
            }
        });





        mEndDateFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setUpEndDateAndTimePickerDialog();
                mEndDatePickerDialog.show();
            }
        });


        Intent intent = getIntent();
        Medication medication = null;
        if(intent != null){
            Uri uri = intent.getData();
            id = ContentUris.parseId(uri);
            medication = MedicationDbUtils.getMedicationById(this, id);
        }

        if(medication != null){
            mNameEditText.setText(medication.getName());
            mDescriptionEditText.setText(medication.getDescription());
            mFrequencySpinner.setSelection(medication.getFrequency() - 1);
            mStartDateTextView.setText(DateTimeUtils.getDateTimeString(medication.getStartDateTime()));
            mEndDateTextView.setText(DateTimeUtils.getDateTimeString(medication.getEndDateTime()));

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(medication.getStartDateTime());
//            mStartDatePickerDialog.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) );
//            mStartTimePickerDialog.updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

            c.setTimeInMillis(medication.getEndDateTime());
//            mEndDatePickerDialog.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) );
//            mEndTimePickerDialog.updateTime(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_medication, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_save:

                updateMedication();


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpStartDateAndTimePickerDialog(){

        final int cYear, cMonth, cDay;
        Log.d(TAG, "onClick: start date fab clicked!!!!!");
        final Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);

        mStartDatePickerDialog = new DatePickerDialog(mContext,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {


                int cHour, cMinute;
                mCalendar = Calendar.getInstance();
                cHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cMinute = mCalendar.get(Calendar.MINUTE);

                mStartTimePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//                        mStartTimePicker =  view;
                        mStartDateTimeInMillis = DateTimeUtils.getDateTimeInMilliseconds(year,
                                month, dayOfMonth, hourOfDay, minute);

                        mStartDateTextView.setText(DateTimeUtils.getDateTimeString(mStartDateTimeInMillis));

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

        mEndDatePickerDialog = new DatePickerDialog(mContext,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {


                int cHour, cMinute;
                mCalendar = Calendar.getInstance();
                cHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                cMinute = mCalendar.get(Calendar.MINUTE);

                mEndTimePickerDialog = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//                        mEndTimePicker = view;

                        mEndDateTimeInMillis = DateTimeUtils.getDateTimeInMilliseconds(year,
                                month, dayOfMonth, hourOfDay, minute);

                        mEndDateTextView.setText(DateTimeUtils.getDateTimeString(mEndDateTimeInMillis));


                    }
                }, cHour, cMinute, false);


                mEndTimePickerDialog.show();
            }
        }, cYear, cMonth, cDay);

    }

    private void updateMedication(){
        String name = mNameEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        int frequency = mFrequencySpinner.getSelectedItemPosition() + 1;

        long startDate = DateTimeUtils.DateTimeStringToMillis(mStartDateTextView.getText().toString().trim());
        long endDate = DateTimeUtils.DateTimeStringToMillis(mEndDateTextView.getText().toString().trim());

        if(!(name.isEmpty() || description.isEmpty() || startDate == 0 || endDate == 0)) {

            ContentValues values = new ContentValues();
            values.put(MedicationEntry.COLUMN_MEDICATION_NAME, name);
            values.put(MedicationEntry.COLUMN_MEDICATION_DESCRIPTION, description);
            values.put(MedicationEntry.COLUMN_MEDICATION_FREQUENCY, frequency);
            values.put(MedicationEntry.COLUMN_MEDICATION_START_DATE, startDate);
            values.put(MedicationEntry.COLUMN_MEDICATION_END_DATE, endDate);


            int result = MedicationDbUtils.updateMedication(mContext, id, values);

            if (result > 0) {
                Toast.makeText(mContext, "Medication edited successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(mContext, "Unable to edit medication", Toast.LENGTH_SHORT).show();
            }
        }


    }

}

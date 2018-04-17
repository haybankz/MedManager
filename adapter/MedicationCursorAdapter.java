package com.haybankz.medmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.data.medication.MedicationContract;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;

public class MedicationCursorAdapter extends CursorAdapter {

    public MedicationCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.medication_card_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView mNameTextView = view.findViewById(R.id.tv_med_name);
        TextView mDescriptionTextView = view.findViewById(R.id.tv_med_description);
        TextView mDosageTextView = view.findViewById(R.id.tv_med_dosage);
        TextView mIntervalTextView = view.findViewById(R.id.tv_med_interval);
        TextView mStartDateTextView = view.findViewById(R.id.tv_med_start_date);
        TextView mEndDateTextView = view.findViewById(R.id.tv_med_end_date);
        ImageView mMedTypeImageView =  view.findViewById(R.id.img_med_type);
        final ImageView mMedActiveImageView = view.findViewById(R.id.img_med_active);

        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry._ID));
        final String name = cursor.getString(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_NAME));
        final String description = cursor.getString(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_DESCRIPTION));
        final String dosage = cursor.getString(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_DOSAGE));
        final int frequency = cursor.getInt(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_FREQUENCY));
        final long startDate = cursor.getLong(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_START_DATE));
        final long endDate = cursor.getLong(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_END_DATE));
        final boolean active =  cursor.getInt(cursor.getColumnIndexOrThrow(MedicationContract.MedicationEntry.COLUMN_MEDICATION_ACTIVE)) == 1 ;

        mNameTextView.setText(name);
        mDescriptionTextView.setText(description);

        String interval = "";
        switch (frequency){
            case Constant.INT_ONCE_A_DAY:
                interval = Constant.TEXT_ONCE_A_DAY;
                break;

            case Constant.INT_TWICE_A_DAY:
                interval = Constant.TEXT_TWICE_A_DAY;
                break;

            case Constant.INT_THRICE_A_DAY:
                interval = Constant.TEXT_THRICE_A_DAY;
                break;

            case Constant.INT_FOUR_TIMES_A_DAY:
                interval = Constant.TEXT_FOUR_TIMES_A_DAY;
                break;

            default:
                break;
        }

        mIntervalTextView.setText(interval);
        mStartDateTextView.setText(DateTimeUtils.getDateTimeString(startDate));
        mEndDateTextView.setText(DateTimeUtils.getDateTimeString(endDate));
        mDosageTextView.setText(dosage);



        if(active){
            mMedActiveImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

        }else{
            mMedActiveImageView.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
        }


        mMedActiveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                if(active){
                    MedicationDbUtils.deactivateMedication(context, id);
                    mMedActiveImageView.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));

                }else{

                    MedicationDbUtils.activateMedication(context, id);
                    mMedActiveImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                }
            }
        });






    }

}

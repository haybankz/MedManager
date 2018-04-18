package com.haybankz.medmanager.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.data.medication.MedicationContract.MedicationEntry;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;

public class ViewMedicationActivity extends AppCompatActivity {

    ImageView mEditImageView;
    ImageView mDeleteImageView;

    TextView mNameTextView;
    TextView mDescriptionTextView;
    TextView mDosageTextView;
    TextView mFrequency;
    TextView mStartDate;
    TextView mEndDate;

    Button mActivateButton;

    Medication medication;

    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medication);


        mUri = getIntent().getData();

        if (mUri != null) {

            long id = ContentUris.parseId(mUri);
            medication = MedicationDbUtils.getMedicationById(this, id);


            mEditImageView = findViewById(R.id.img_view_edit);
            mDeleteImageView = findViewById(R.id.img_view_delete);

            mNameTextView = findViewById(R.id.tv_view_med_name);
            mDescriptionTextView = findViewById(R.id.tv_view_med_description);
            mDosageTextView = findViewById(R.id.tv_view_med_dosage);
            mFrequency = findViewById(R.id.tv_view_med_frequency);
            mStartDate = findViewById(R.id.tv_view_med_start_date);
            mEndDate = findViewById(R.id.tv_view_med_end_date);


            mActivateButton = findViewById(R.id.btn_view_med_activate);
            if(medication.isActive()){
                mActivateButton.setText("Deactivate");
            }else{
                mActivateButton.setText("Activate");
            }

            mActivateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mActivateButton.getText().toString().equals("Activate")){
                        MedicationDbUtils.activateMedication(ViewMedicationActivity.this, medication.getId());
                        mActivateButton.setText("Deactivate");
                    }else{
                        MedicationDbUtils.deactivateMedication(ViewMedicationActivity.this, medication.getId());
                        mActivateButton.setText("Activate");
                    }


                }
            });

            mEditImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri uri = ContentUris.withAppendedId(MedicationEntry.CONTENT_URI, medication.getId());
                    Intent intent = new Intent(ViewMedicationActivity.this, EditMedicationActivity.class);
                    intent.setData(uri);

                    startActivity(intent);

                }
            });

            mDeleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ViewMedicationActivity.this);
                    builder.setTitle("\tDelete Medication??")
                            .setMessage("\t\t\tMedication and its reminder will be deleted from the database");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            long medicationId = ContentUris.parseId(mUri);
                            int deleted = MedicationDbUtils.deleteMedication(ViewMedicationActivity.this, medicationId);

                            getContentResolver().notifyChange(MedicationEntry.CONTENT_URI, null, false);

                            if (deleted > 0) {
                                Toast.makeText(ViewMedicationActivity.this, "Medication Deleted", Toast.LENGTH_SHORT).show();
                                ViewMedicationActivity.this.finish();

                                Intent intent = new Intent(ViewMedicationActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(ViewMedicationActivity.this, "Cant delete medication", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            //Do nothing
                        }
                    });

                    builder.setIcon(R.drawable.ic_notifications_black_24dp);

                    builder.create().show();


                }
            });

            mNameTextView.setText(medication.getName());
            mDescriptionTextView.setText(medication.getDescription());
            mDosageTextView.setText("Dosage: ");
            mFrequency.setText("Rings: " + medication.getFrequency() + "time(s)");
            mStartDate.setText(DateTimeUtils.getDateTimeString(medication.getStartDateTime()));
            mEndDate.setText(DateTimeUtils.getDateTimeString(medication.getEndDateTime()));


        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mUri = getIntent().getData();

        if (mUri != null) {

            long id = ContentUris.parseId(mUri);
            medication = MedicationDbUtils.getMedicationById(this, id);
            mNameTextView.setText(medication.getName());
            mDescriptionTextView.setText(medication.getDescription());
            mDosageTextView.setText("Dosage: ");
            mFrequency.setText("Rings: " + medication.getFrequency() + "time(s)");
            mStartDate.setText(DateTimeUtils.getDateTimeString(medication.getStartDateTime()));
        }
    }
}

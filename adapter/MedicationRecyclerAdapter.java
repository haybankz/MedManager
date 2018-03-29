package com.haybankz.medmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class MedicationRecyclerAdapter extends RecyclerView.Adapter<MedicationRecyclerAdapter.MedicationViewHolder>{


    Context mContext;
    List<Medication> mMedications;

    public MedicationRecyclerAdapter(Context context, List<Medication> medications){
        mContext = context;
        mMedications = medications;
    }


    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_card_item, parent, false);

        return new MedicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {

        Medication medication = mMedications.get(position);

        holder.bindMedication(mContext, medication);


    }

    @Override
    public int getItemCount() {
        return mMedications.size();
    }

    public Medication getItem(int position){
        return mMedications.get(position);
    }

    public void clear(){
        mMedications.clear();
    }


    public void addAll(ArrayList<Medication> medications){
        mMedications.clear();
        mMedications.addAll(medications);
        notifyDataSetChanged();
    }



    @Override
    @NonNull
    public void onAttachedToRecyclerView(@NonNull  RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    static class MedicationViewHolder extends RecyclerView.ViewHolder {

        TextView mNameTextView;
        TextView mDescriptionTextView;
        TextView mIntervalTextView;
        ImageView mMedTypeImageView;
        TextView mStartDateTextView;
        TextView mEndDateTextView;
        ImageView mMedActiveImageView;

//        View mView;

        MedicationViewHolder(View itemView){
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.tv_med_name);
            mDescriptionTextView = itemView.findViewById(R.id.tv_med_description);
            mIntervalTextView = itemView.findViewById(R.id.tv_med_interval);
            mStartDateTextView = itemView.findViewById(R.id.tv_med_start_date);
            mEndDateTextView = itemView.findViewById(R.id.tv_med_end_date);
            mMedTypeImageView = itemView.findViewById(R.id.img_med);
            mMedActiveImageView = itemView.findViewById(R.id.img_med_active);

        }


        private void bindMedication(Context context, @NonNull Medication medication) {

            mNameTextView.setText(medication.getName());
            mDescriptionTextView.setText(medication.getDescription());

            String interval = "";
            switch (medication.getFrequency()){
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

            String startDate = DateTimeUtils.getDateTimeString(medication.getStartDateTime());

            String endDate = DateTimeUtils.getDateTimeString(medication.getEndDateTime());

            mStartDateTextView.setText(startDate);
            mEndDateTextView.setText(endDate);

            mMedActiveImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

        }



    }
}

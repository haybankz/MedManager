package com.haybankz.medmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ReminderViewHolder>{


    private Context mContext;
    private List<Reminder> mReminders;

    public ReminderRecyclerAdapter(Context context, List<Reminder> reminders){
        mContext = context;
        mReminders = reminders;
    }


    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card_item, parent, false);

        return new ReminderViewHolder(mContext, v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        Reminder reminder = mReminders.get(position);

        holder.bindReminder(mContext, reminder);


    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public Reminder getItem(int position){
        return mReminders.get(position);
    }

    public void clear(){
        mReminders.clear();
    }


    public void addAll(ArrayList<Reminder> reminders){
        mReminders.clear();
        mReminders.addAll(reminders);
        notifyDataSetChanged();
    }



    @Override
    @NonNull
    public void onAttachedToRecyclerView(@NonNull  RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


    static class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mMedicationNameTextView;
        TextView mDosageTextView;
        TextView mReminderTimeTextView;
        ImageView mRescheduleImageView;
        ImageView mTakeMedicationImageView;
        ImageView mRefuseMedicationImageView;
        ImageView mRemImageView;

        Context mContext;



        ReminderViewHolder(Context context, View itemView){
            super(itemView);

            mContext = context;

            mMedicationNameTextView = itemView.findViewById(R.id.tv_rem_med_name);
            mDosageTextView = itemView.findViewById(R.id.tv_rem_med_dosage);
            mReminderTimeTextView = itemView.findViewById(R.id.tv_rem_time);
            mRescheduleImageView = itemView.findViewById(R.id.img_rem_reschedule);
            mTakeMedicationImageView = itemView.findViewById(R.id.img_rem_take_med);
            mRefuseMedicationImageView =  itemView.findViewById(R.id.img_rem_refuse_medic);
            mRemImageView = itemView.findViewById(R.id.img_rem);



            mRefuseMedicationImageView.setOnClickListener(this);
            mRescheduleImageView.setOnClickListener(this);
            mTakeMedicationImageView.setOnClickListener(this);

        }


        private void bindReminder(Context context, @NonNull Reminder reminder) {

            Medication medication = MedicationDbUtils.getMedicationById(context, reminder.getMedicationId());

            mMedicationNameTextView.setText(medication.getName());
            mDosageTextView.setText("Pick two");
            mReminderTimeTextView.setText(DateTimeUtils.getDateTimeString(reminder.getReminderDateTime()));

//            String interval = "";
//            switch (medication.getFrequency()){
//                case Constant.INT_ONCE_A_DAY:
//                    interval = Constant.TEXT_ONCE_A_DAY;
//                    break;
//
//                case Constant.INT_TWICE_A_DAY:
//                    interval = Constant.TEXT_TWICE_A_DAY;
//                    break;
//
//                case Constant.INT_THRICE_A_DAY:
//                    interval = Constant.TEXT_THRICE_A_DAY;
//                    break;
//
//                case Constant.INT_FOUR_TIMES_A_DAY:
//                    interval = Constant.TEXT_FOUR_TIMES_A_DAY;
//                    break;
//
//                default:
//                    break;
//            }


            Picasso.with(context)
                    .load("file:///android_asset/flags/USD.png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .transform(new PicassoCircleTransformation())
                    .fit()
                    .noFade()
                    .into(mRemImageView);


            if(reminder.isTaken()) {
                mTakeMedicationImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
//                mTakeMedicationImageView.setEnabled(false);
            }else{
//                mRefuseMedicationImageView.setEnabled(false);
            }

        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.img_rem_reschedule:
                    Toast.makeText(mContext, "Reschedule clicked", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.img_rem_refuse_medic:
                    Toast.makeText(mContext, "Refuse clicked", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.img_rem_take_med:
                    Toast.makeText(mContext, "take clicked", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(mContext, "whole item clicked clicked", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

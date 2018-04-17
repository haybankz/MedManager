package com.haybankz.medmanager.adapter;

import android.content.Context;
import android.graphics.ColorFilter;
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
import com.haybankz.medmanager.receiver.AlarmReceiver;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;
import com.haybankz.medmanager.util.ReminderDbUtils;
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
    public void onBindViewHolder(@NonNull final ReminderViewHolder holder, int position) {

        final Reminder reminder = mReminders.get(position);

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
//        notifyDataSetChanged();
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
        Reminder  mReminder;



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
            itemView.setOnClickListener(this);

        }


        private void bindReminder(Context context, @NonNull Reminder reminder) {



            Medication medication = MedicationDbUtils.getMedicationById(context, reminder.getMedicationId());

            mReminder = reminder;

            mMedicationNameTextView.setText(medication.getName());
            mDosageTextView.setText(medication.getDosage());
            mReminderTimeTextView.setText(DateTimeUtils.getDateTimeString(reminder.getReminderDateTime()));


            Picasso.with(context)
                    .load("file:///android_asset/flags/USD.png")
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .transform(new PicassoCircleTransformation())
                    .fit()
                    .noFade()
                    .into(mRemImageView);

            if(reminder.isTaken()) {
                mTakeMedicationImageView.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                mTakeMedicationImageView.setEnabled(false);
            }else{
                mTakeMedicationImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                mRefuseMedicationImageView.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));
                mRefuseMedicationImageView.setEnabled(true);
            }

            mRescheduleImageView.setColorFilter(ContextCompat.getColor(context, R.color.darker_gray));



        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.img_rem_reschedule:

                    break;

                case R.id.img_rem_refuse_medic:
                    ReminderDbUtils.ReminderRefuse(mContext, mReminder.getId());
                    mRefuseMedicationImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
                    mTakeMedicationImageView.setEnabled(true);
                    mRefuseMedicationImageView.setEnabled(false);
                    mTakeMedicationImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.darker_gray));
                    Toast.makeText(mContext, "Refuse clicked", Toast.LENGTH_SHORT).show();
                    break;


                case R.id.img_rem_take_med:
                    if(!mReminder.isTaken()) {
                        ReminderDbUtils.ReminderTaken(mContext, mReminder.getId());
                        mTakeMedicationImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary));
                        mTakeMedicationImageView.setEnabled(false);
                        mRefuseMedicationImageView.setEnabled(true);
                        mRefuseMedicationImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.darker_gray));
                        Toast.makeText(mContext, "Take", Toast.LENGTH_SHORT).show();

                    }


                    break;

                default:
                    Toast.makeText(mContext, "whole item clicked clicked", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

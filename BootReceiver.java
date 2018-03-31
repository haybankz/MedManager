package com.haybankz.medmanager;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.MedicationDbUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class BootReceiver extends BroadcastReceiver {

    int id;

    private AlarmReceiver mAlarmReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            mAlarmReceiver = new AlarmReceiver();
            List<Medication> medications = MedicationDbUtils.getAllMedications(context);

            if(medications != null) {
                for (Medication med : medications) {

                    id = (int) med.getId();
                    String mName = med.getName();
                    String mDescription = med.getDescription();
                    int mFrequency = med.getFrequency();
                    long mStartDate = med.getStartDateTime();
                    long mEndDate = med.getEndDateTime();
                    boolean active = med.getActive();

                    long repeatTime;

                    //Cancel existing notification of the alarm reminder by using its Id
                    mAlarmReceiver.cancelAlarm(context, id);

                    switch(mFrequency){
                        case Constant.INT_ONCE_A_DAY:
                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_ONCE_A_DAY;
                            break;

                        case Constant.INT_TWICE_A_DAY:
                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_TWICE_A_DAY;
                            break;

                        case Constant.INT_THRICE_A_DAY:
                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_THRICE_A_DAY;
                            break;

                        case Constant.INT_FOUR_TIMES_A_DAY:
                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_FOUR_TIMES_A_DAY;
                            break;

                        default:
                            repeatTime = 0;
                            break;


                    }

                    if(active || mEndDate < Calendar.getInstance().getTimeInMillis())
                    mAlarmReceiver.setRepeatAlarm(context, mStartDate, id, repeatTime);
                    else{
                        mAlarmReceiver.cancelAlarm(context, id);
                    }

                }
            }



        }

    }
}

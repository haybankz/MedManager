package com.haybankz.medmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.receiver.AlarmReceiver;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.MedicationDbUtils;
import com.haybankz.medmanager.util.ReminderDbUtils;

import java.util.ArrayList;
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
            ArrayList<Reminder> reminders = ReminderDbUtils.getAllRemindersToRingAfterCurrentTime(context);

            if(reminders != null) {
                for (Reminder reminder : reminders) {

                   int id = (int) reminder.getId();
                   long medicationId = reminder.getMedicationId();
                   long mStartDate = reminder.getReminderDateTime();



                    long repeatTime;

//                    //Cancel existing notification of the alarm reminder by using its Id
//                    mAlarmReceiver.cancelAlarm(context, id);
//
//                    switch(mFrequency){
//                        case Constant.INT_ONCE_A_DAY:
//                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_ONCE_A_DAY;
//                            break;
//
//                        case Constant.INT_TWICE_A_DAY:
//                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_TWICE_A_DAY;
//                            break;
//
//                        case Constant.INT_THRICE_A_DAY:
//                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_THRICE_A_DAY;
//                            break;
//
//                        case Constant.INT_FOUR_TIMES_A_DAY:
//                            repeatTime = Constant.DAY_IN_MILLIS / Constant.INT_FOUR_TIMES_A_DAY;
//                            break;
//
//                        default:
//                            repeatTime = 0;
//                            break;
//
//
//                    }
//
//                    if(active || mEndDate < Calendar.getInstance().getTimeInMillis())
//                    mAlarmReceiver.setRepeatAlarm(context, mStartDate, id, repeatTime);
//                    else{
//                        mAlarmReceiver.cancelAlarm(context, id);
//                    }

                    mAlarmReceiver.setAlarm(context, mStartDate, id);
                }
            }



        }

    }
}

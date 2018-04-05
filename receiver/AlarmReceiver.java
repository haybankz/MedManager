package com.haybankz.medmanager.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.ui.AddMedicationActivity;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.DateTimeUtils;
import com.haybankz.medmanager.util.MedicationDbUtils;
import com.haybankz.medmanager.util.ReminderDbUtils;

import java.util.Calendar;

/**
 * Created by LENOVO on 3/25/2018.
 */

public class AlarmReceiver extends BroadcastReceiver{

    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {


//        Log.e("ALarm reciever", "onReceive: notification running........\n\n\n\n\n\n" );

        int receiverId = Integer.parseInt(intent.getStringExtra(Constant.REMINDER_ID));

        Reminder reminder = ReminderDbUtils.getReminder(context, receiverId);

        long medicationId= reminder.getMedicationId();

        Medication medication = MedicationDbUtils.getMedicationById(context, medicationId);

        String title = medication.getName();

        Log.e("ALarm reciever", "onReceive: notification running........"+ medication.toString() );

        // creating intent to open mainactivity when notification is clicked
        Intent addIntent = new Intent(context, AddMedicationActivity.class);
        addIntent.putExtra(Constant.REMINDER_ID,  String.valueOf(receiverId));
        PendingIntent mReminderPendingIntent = PendingIntent.getActivity(context, receiverId,
                addIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context, Constant.REMINDER_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setSmallIcon(R.drawable.ic_action_calendar)
                .setContentTitle((context.getResources().getString(R.string.app_name)))
                .setContentText(title)
                .setTicker(title)
                .setContentIntent(mReminderPendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

//        Notification mNotification = mNotificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null)
            notificationManager.notify(receiverId, mNotificationBuilder.build());

    }

    public void setAlarm(Context context, long startDateTimeInMillis, int id){

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constant.REMINDER_ID, "" +id);
        mPendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long diffTime =  startDateTimeInMillis - Calendar.getInstance().getTimeInMillis();

        mAlarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + diffTime, mPendingIntent);


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

    }

    public void setRepeatAlarm(Context context, long startDateTimeInMillis, int id, long frequency){

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constant.REMINDER_ID, "" + id);
        mPendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long diffTime =  startDateTimeInMillis - Calendar.getInstance().getTimeInMillis();

        Log.e("Alarm receiver", "setRepeatAlarm: ALARM ADDED "+ DateTimeUtils.getDateTimeString(startDateTimeInMillis) +" to ring in next" +
                "" + frequency / Constant.MINUTE_IN_MILLIS + " min = " + DateTimeUtils.getDateTimeString(startDateTimeInMillis + frequency)  );


        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startDateTimeInMillis);

        //if start time is after current time
        if(startCalendar.after(Calendar.getInstance())) {


            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startDateTimeInMillis, frequency, mPendingIntent);

        }else{

            while(Calendar.getInstance().getTimeInMillis() > startDateTimeInMillis ){

                startDateTimeInMillis += frequency;
            }

            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startDateTimeInMillis, frequency, mPendingIntent);
        }



        Toast.makeText(context, "ALARM ADDED "+ DateTimeUtils.getDateTimeString(startDateTimeInMillis) +" to ring in next" +
                "" + frequency / Constant.MINUTE_IN_MILLIS + " mins = " +
                DateTimeUtils.getDateTimeString(startDateTimeInMillis + frequency), Toast.LENGTH_LONG).show();


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


    }


    public void cancelAlarm(Context context, int id){

        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Intent intent = new Intent(context, AlarmReceiver.class);

        mPendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(String.valueOf(id)), intent, 0);
        mAlarmManager.cancel(mPendingIntent);



        //disable alarm
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }


}

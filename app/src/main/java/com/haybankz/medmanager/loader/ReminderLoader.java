package com.haybankz.medmanager.loader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.model.Reminder;
import com.haybankz.medmanager.util.ReminderDbUtils;

import java.util.ArrayList;

public class ReminderLoader extends AsyncTaskLoader<ArrayList<Reminder>> {

    private Context mContext;

    public ReminderLoader(@NonNull Context context) {
        super(context);

        mContext = context;
    }

    @Nullable
    @Override
    public ArrayList<Reminder> loadInBackground() {
        return ReminderDbUtils.getAllReminders(mContext);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

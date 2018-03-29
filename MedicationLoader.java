package com.haybankz.medmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.haybankz.medmanager.model.Medication;
import com.haybankz.medmanager.util.MedicationDbUtils;

import java.util.ArrayList;

public class MedicationLoader extends AsyncTaskLoader<ArrayList<Medication>> {

    Context mContext;
    int mId;

    public MedicationLoader(@NonNull Context context) {
        super(context);
        mContext = context;

    }

    @Nullable
    @Override
    public ArrayList<Medication> loadInBackground() {

        return MedicationDbUtils.getAllMedications(mContext);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


}

package com.haybankz.medmanager.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haybankz.medmanager.ui.MedicationListFragment;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.ui.ReminderListFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new ReminderListFragment();
        }else{
            return new MedicationListFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        switch(position){
            case 0:
                return mContext.getString(R.string.app_name);

            case 1:
                return mContext.getString(R.string.title_home);

            default:
                return null;

        }

    }


}

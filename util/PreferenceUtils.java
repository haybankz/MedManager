package com.haybankz.medmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    synchronized public static void setLoggedInUser(Context context, long id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Constant.KEY_ACCT_ID, id);
        editor.apply();
    }

    synchronized public static long  getLoggedInUser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getLong(Constant.KEY_ACCT_ID, 0);
    }
    synchronized public static void setLoggedOut(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Constant.KEY_ACCT_ID, 0);
        editor.apply();
    }

}
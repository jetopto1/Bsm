package com.jetopto.bsm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jetopto.bsm.MainActivity;


public class PreferenceManager {

    private static final String TAG = PreferenceManager.class.getSimpleName();
    private static final String pref = MainActivity.class.getPackage().getName();
    public static final String DEMO_MODE = "demo_mode";

    public static void editPreference(Context context, String key, boolean value) {
        Log.i(TAG, "pref: " + pref);
        SharedPreferences preferences = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defValue) {
        Log.i(TAG, "pref: " + pref);
        SharedPreferences preferences = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defValue);
    }
}

package com.jetopto.bsm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jetopto.bsm.MainActivity;


public class PreferencesManager {


    public static final String KEY_DEMO_MODE = "demo_mode";
    public static final String KEY_BSM_MAC = "bsm_mac";
    public static final String KEY_NAVIGATION_HIDDEN = "hide_nav_bar";
    public static final String KEY_INVERSE_LAYOUT = "inverse_layout";
    private static final String TAG = PreferencesManager.class.getSimpleName();
    private static final String PREF_NAME = MainActivity.class.getPackage().getName();

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void put(Context context, String key, boolean value) {
        getPreference(context).edit().putBoolean(key, value).apply();
    }

    public static void put(Context context, String key, String value) {
        Log.i(TAG, "putString: key " + key + ", v: " + value);
        getPreference(context).edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPreference(context).getBoolean(key, defValue);
    }

    public static String getString(Context context, String key, String defValue) {
        return getPreference(context).getString(key, defValue);
    }

    public static boolean remove(Context context, String key) {
        return getPreference(context).edit().remove(key).commit();
    }
}

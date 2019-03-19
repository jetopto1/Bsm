package com.jetopto.bsm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.jetopto.bsm.utils.PreferencesManager;

public class BsmApplication extends Application {

    private static Context context;
    public static boolean isReverse = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        isReverse = PreferencesManager.getBoolean(context, PreferencesManager.KEY_INVERSE_LAYOUT, false);
    }

    public static Context getAppContext() {
        return context;
    }

    public static void onSharedPreferenceChanged(SharedPreferences preference, String key) {
        if (key.equals(PreferencesManager.KEY_INVERSE_LAYOUT)) {
            isReverse = preference.getBoolean(key, false);
        }
    }
}

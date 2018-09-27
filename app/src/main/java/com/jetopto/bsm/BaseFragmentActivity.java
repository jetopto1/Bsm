package com.jetopto.bsm;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.common.util.ArrayUtils;
import com.jetopto.bsm.utils.Utils;

import java.util.ArrayList;

public abstract class BaseFragmentActivity extends FragmentActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = BaseFragmentActivity.class.getSimpleName();

    protected final int REQUEST_PERMISSION_LOCATION = 98;
    protected final int REQUEST_PERMISSION_BLUETOOTH = 99;
    protected final int REQUEST_PERMISSION_CONTACT = 100;
    protected final int REQUEST_PERMISSION_PHONE_CALL = 101;
    protected final int REQUEST_PERMISSION_ALL = 1206;

    protected final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected final String[] BLUETOOTH_PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH_ADMIN};

    protected final String[] CONTACTS_PERMISSIONS = new String[]{Manifest.permission.READ_CONTACTS};

    protected final String[] CALL_PHONE_PERMISSIONS = new String[]{Manifest.permission.CALL_PHONE};

    protected void requestAllPermissions() {
        String[] array = ArrayUtils.concat(LOCATION_PERMISSIONS, BLUETOOTH_PERMISSIONS,
                CONTACTS_PERMISSIONS, CALL_PHONE_PERMISSIONS);
        if (isFirstTimeAskForPermission()) {
            requestRunTimePermission(this, array, REQUEST_PERMISSION_ALL);
        } else {
            ArrayList<String> deniedList = new ArrayList<>();
            for (String permission : array) {
                if (!checkPermission(permission)) {
                    deniedList.add(permission);
                }
            }

            if (!deniedList.isEmpty()) {
                //TODO
                String[] deniedPermissions = new String[deniedList.size()];
                deniedList.toArray(deniedPermissions);
                requestRunTimePermission(this, deniedPermissions, REQUEST_PERMISSION_ALL);
            }
        }
    }

    protected void requestRunTimePermission(final Activity activity, final String[] permissions,
                                            final int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    protected boolean checkPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(getApplicationContext(), permission);
    }

    protected boolean isFirstTimeAskForPermission() {
        String prefPermission = "permissions_ask";
        String requestAll = "request_all";
        SharedPreferences preferences = getSharedPreferences(prefPermission, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(requestAll, true);
        if (isFirstTime) {
            preferences.edit().putBoolean(requestAll, false).apply();
        }
        return isFirstTime;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    private void hideNavigationBar() {
        if (Utils.shouldHideNavBar(getResources())) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}

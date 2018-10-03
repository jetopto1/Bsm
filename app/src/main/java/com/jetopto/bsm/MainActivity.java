package com.jetopto.bsm;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jetopto.bsm.custom.view.CustomViewPager;
import com.jetopto.bsm.custom.view.SensorLevelView;
import com.jetopto.bsm.fragment.BaseFragment;
import com.jetopto.bsm.fragment.CategoryFragment;
import com.jetopto.bsm.fragment.ContactsFragment;
import com.jetopto.bsm.fragment.DashBoardFragment;
import com.jetopto.bsm.fragment.MapFragment;
import com.jetopto.bsm.fragment.SettingFragment;
import com.jetopto.bsm.fragment.VideoFragment;
import com.jetopto.bsm.presenter.MainPresenterImpl;
import com.jetopto.bsm.presenter.interfaces.MainMvpView;
import com.jetopto.bsm.utils.Constant;
import com.jetopto.bsm.utils.PreferencesManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements MainMvpView,
        CategoryFragment.OnCategoryClick, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    SensorLevelView mLeftView;
    SensorLevelView mRightView;
    SlidePageAdapter mPagerAdapter;
    CustomViewPager mViewPager;
    MainPresenterImpl mPresenter;
    Fragment mMapFragment;
    Fragment mCategoryFragment;
    Fragment mDashFragment;
    Fragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPresenter = new MainPresenterImpl();
        mPresenter.attachView(getApplicationContext(), this);
        requestAllPermissions();

        if (!getPackageManager().hasSystemFeature(getPackageManager().FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getBaseContext(), "BLE not support", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "BLE supported", Toast.LENGTH_SHORT).show();

        }

//        beaconScanIntent = new Intent(this, SensorMonitorService.class);
//        this.startService(beaconScanIntent);
    }


    @Override
    public void onBackPressed() {
        int curPosition = mViewPager.getCurrentItem();
        if (curPosition == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mCategoryFragment), false);
        }
    }

    @Override
    public void onSensorStateChanged(final Bundle bundle) {
        //TODO resolve sensor level
        if (null != bundle) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRightView.updateSensorLevel(bundle.getString(Constant.RIGHT_SENSOR_LEVEL));
                    mLeftView.updateSensorLevel(bundle.getString(Constant.LEFT_SENSOR_LEVEL));
                }
            });
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(int id) {
        switch (id) {
            case R.drawable.map_selector:
                if (PreferencesManager.getBoolean(this,
                        PreferencesManager.KEY_DEMO_MODE, false)) {
                    showDemoVideo(Constant.PLAY_NAVIGATION_FILE);
                } else {
                    if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        requestRunTimePermission(this, LOCATION_PERMISSIONS, REQUEST_PERMISSION_LOCATION);
                    } else {
                        mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mMapFragment), false);
                    }
                }
                break;
            case R.drawable.dashboard_selector:
                mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mDashFragment), false);
                break;
            case R.drawable.contact_selector:
                if (checkPermission(Manifest.permission.READ_CONTACTS)) {
                    showContactsFragment();
                } else {
                    requestRunTimePermission(this, CONTACTS_PERMISSIONS, REQUEST_PERMISSION_CONTACT);
                }
                break;
            case R.drawable.setting_selector:
                mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mSettingFragment), false);
                break;
            case R.drawable.dvr_selector:
                showDemoVideo(Constant.PLAY_DVR_FILE);
                break;
            case R.drawable.bsm_selector:
                //TODO TBD function
                break;
        }
    }

    private void showDemoVideo(int video) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().
                findFragmentByTag(VideoFragment.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = VideoFragment.newInstance(video);
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        newFragment.show(ft, VideoFragment.class.getSimpleName());
    }

    private void showContactsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().
                findFragmentByTag(ContactsFragment.class.getSimpleName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = ContactsFragment.newInstance();
        newFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        newFragment.show(ft, ContactsFragment.class.getSimpleName());
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager_category);
        mPagerAdapter = new SlidePageAdapter(getSupportFragmentManager());

        mCategoryFragment = new CategoryFragment();
        mMapFragment = new MapFragment();
        mDashFragment = new DashBoardFragment();
        mSettingFragment = new SettingFragment();
        mPagerAdapter.addFragment(mCategoryFragment);
        mPagerAdapter.addFragment(mMapFragment);
        mPagerAdapter.addFragment(mDashFragment);
        mPagerAdapter.addFragment(mSettingFragment);
        mViewPager.setAdapter(mPagerAdapter);
        mLeftView = findViewById(R.id.sensor_bar_left);
        mRightView = findViewById(R.id.sensor_bar_right);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return true;
        }

        mViewPager.getCurrentItem();
        Fragment f = mPagerAdapter.getItem(mViewPager.getCurrentItem());
        if (f instanceof BaseFragment) {
            ((BaseFragment) f).handleKeyEvent(keyCode);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode && event.getRepeatCount() == 15) {
            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.bindBsmService();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        sendProjectorIntent(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        mPresenter.unbindBsmService();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        sendProjectorIntent(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (null != mPresenter) {
            mPresenter.detachView();
            mPresenter = null;
        }
//        stopService(beaconScanIntent);
    }

    private void sendProjectorIntent(final boolean enable) {

        final String command1 = "/system/bin/sh /system/bin/projector_on.sh";
        final String command2 = "/system/bin/sh /system/bin/projector_off.sh";
        Log.i(TAG, "sendProjectorIntent " + enable);
//
        if (Build.PRODUCT.equals("rk312x")) {
            if (enable) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, "command1" + command1);
                            Process process = Runtime.getRuntime().exec(command1);
                            InputStreamReader reader = new InputStreamReader(process.getInputStream());
                            BufferedReader bufferedReader = new BufferedReader(reader);
                            int numRead;
                            char[] buffer = new char[5000];
                            StringBuffer commandOutput = new StringBuffer();
                            while ((numRead = bufferedReader.read(buffer)) > 0) {
                                commandOutput.append(buffer, 0, numRead);
                            }
                            bufferedReader.close();
                            process.waitFor();
                            Log.d(TAG, "Set USER_ROTATION, 1");
                            Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                            Settings.System.putInt(getContentResolver(), Settings.System.USER_ROTATION, 1);
                            Log.d(TAG, "commandOutput.toString(): " + commandOutput.toString());
                            //				return commandOutput.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(TAG, "e.fillInStackTrace(): " + e.fillInStackTrace());
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d(TAG, "e.fillInStackTrace(): " + e.fillInStackTrace());
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            } else {
                Intent intent = new Intent("tmj.setting.projector.onoff");
                intent.putExtra("projector_onoff", enable);
                sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getBaseContext(), R.string.location_permission_get, Toast.LENGTH_SHORT).show();
                    mMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case REQUEST_PERMISSION_BLUETOOTH:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.bindBsmService();
                }
                break;
            case REQUEST_PERMISSION_CONTACT:
                break;
            case REQUEST_PERMISSION_ALL:
                Log.i(TAG, "request permission all");
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesManager.KEY_BSM_MAC)) {
            mPresenter.bindBsmService();
        }
    }

    class SlidePageAdapter extends FragmentStatePagerAdapter {

        private FragmentManager mFragmentManager;

        private List<Fragment> mFragmentList;

        public SlidePageAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
            mFragmentList = new ArrayList<>();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        private int getItemIndex(@NonNull Object object) {
            for (int i = 0; i < mFragmentList.size(); i++) {
                if (object.equals(mFragmentList.get(i))) {
                    return i;
                }
            }
            return super.getItemPosition(object);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mFragmentManager.beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            mFragmentManager.beginTransaction().hide(mFragmentList.get(position)).commit();
        }
    }
}

package com.jetopto.bsm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jetopto.bsm.core.SensorMonitorService;
import com.jetopto.bsm.custom.view.SensorLevelView;
import com.jetopto.bsm.fragment.CategoryFragment;
import com.jetopto.bsm.fragment.MapFragment;
import com.jetopto.bsm.presenter.MainPresenterImpl;
import com.jetopto.bsm.presenter.interfaces.IBasePresenter;
import com.jetopto.bsm.presenter.interfaces.MainMvpView;
import com.jetopto.bsm.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MainMvpView {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent beaconScanIntent;
    public static final int BSM_PERMISSIONN_REQUEST_LOCATION = 99;
    public static final int BSM_PERMISSIONN_REQUEST_BLUETOOTH = 98;

    SensorLevelView mLeftView;
    SensorLevelView mRightView;
    SlidePageAdapter mPagerAdapter;
    ViewPager mViewPager;
    IBasePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPresenter = new MainPresenterImpl();
        mPresenter.attachView(getApplicationContext(),this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    BSM_PERMISSIONN_REQUEST_LOCATION
            );
        } else {
            Toast.makeText(getBaseContext(), R.string.location_permission_get, Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) !=
                PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getBaseContext(),R.string.bt_admin_permission_failed, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    BSM_PERMISSIONN_REQUEST_BLUETOOTH
            );
        } else {
            Toast.makeText(getBaseContext(),R.string.bt_admin_permission_get, Toast.LENGTH_LONG).show();
        }


        if (!getPackageManager().hasSystemFeature(getPackageManager().FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getBaseContext(), "BLE not support", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(),"BLE supported", Toast.LENGTH_SHORT).show();

        }

//        beaconScanIntent = new Intent(this, SensorMonitorService.class);
//        this.startService(beaconScanIntent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
//        stopService(beaconScanIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onSensorStateChanged(Bundle bundle) {
        //TODO resolve sensor level
        Log.d(TAG, "bundle" + bundle);
        if (null != bundle) {
            mRightView.updateSensorLevel(bundle.getString(Constant.RIGHT_SENSOR_LEVEL));
            mLeftView.updateSensorLevel(bundle.getString(Constant.LEFT_SENSOR_LEVEL));
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager_category);
        mPagerAdapter = new SlidePageAdapter(getSupportFragmentManager());
        Fragment category = new CategoryFragment();
        mPagerAdapter.addFragment(category);
        Fragment map = new MapFragment();
        mPagerAdapter.addFragment(map);
        mViewPager.setAdapter(mPagerAdapter);
        mLeftView = findViewById(R.id.sensor_bar_left);
        mRightView = findViewById(R.id.sensor_bar_right);

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
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case BSM_PERMISSIONN_REQUEST_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),R.string.location_permission_get, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(),R.string.location_permission_failed, Toast.LENGTH_SHORT).show();
                }
            }
            case BSM_PERMISSIONN_REQUEST_BLUETOOTH: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(),R.string.bt_admin_permission_get, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(),R.string.bt_admin_permission_failed,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
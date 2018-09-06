package com.jetopto.bsm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jetopto.bsm.custom.view.CustomViewPager;
import com.jetopto.bsm.custom.view.SensorLevelView;
import com.jetopto.bsm.fragment.CategoryFragment;
import com.jetopto.bsm.fragment.ContactsFragment;
import com.jetopto.bsm.fragment.DashBoardFragment;
import com.jetopto.bsm.fragment.MapFragment;
import com.jetopto.bsm.presenter.MainPresenterImpl;
import com.jetopto.bsm.presenter.interfaces.IBasePresenter;
import com.jetopto.bsm.presenter.interfaces.MainMvpView;
import com.jetopto.bsm.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements MainMvpView, CategoryFragment.OnCategoryClick {

    private static final String TAG = MainActivity.class.getSimpleName();

    SensorLevelView mLeftView;
    SensorLevelView mRightView;
    SlidePageAdapter mPagerAdapter;
    CustomViewPager mViewPager;
    IBasePresenter mPresenter;
    Fragment mMapFragment;
    Fragment mCategoryFragment;
    Fragment mDashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mPresenter = new MainPresenterImpl();
        mPresenter.attachView(getApplicationContext(), this);
        requestAllPermissions();
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestRunTimePermission(this, LOCATION_PERMISSIONS, REQUEST_PERMISSION_LOCATION);
        } else {
            Toast.makeText(getBaseContext(), R.string.location_permission_get, Toast.LENGTH_SHORT).show();
        }

        if (!checkPermission(Manifest.permission.BLUETOOTH_ADMIN)) {

            Toast.makeText(getBaseContext(), R.string.bt_admin_permission_failed, Toast.LENGTH_LONG).show();
            requestRunTimePermission(this, BLUETOOTH_PERMISSIONS, REQUEST_PERMISSION_BLUETOOTH);
        } else {
            Toast.makeText(getBaseContext(), R.string.bt_admin_permission_get, Toast.LENGTH_LONG).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestRunTimePermission(this, CONTACTS_PERMISSIONS, REQUEST_PERMISSION_CONTACT);
        } else {
            Toast.makeText(getBaseContext(), R.string.bt_admin_permission_get, Toast.LENGTH_LONG).show();
        }

        if (!getPackageManager().hasSystemFeature(getPackageManager().FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getBaseContext(), "BLE not support", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "BLE supported", Toast.LENGTH_SHORT).show();

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
        int curPosition = mViewPager.getCurrentItem();
        Log.i(TAG, "back: " + curPosition);
        if (curPosition == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mCategoryFragment), false);
        }
    }


    @Override
    public void onSensorStateChanged(Bundle bundle) {
        //TODO resolve sensor level
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

    @Override
    public void onClick(int id) {
        switch (id) {
            case R.drawable.map_selector:
                mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mMapFragment), false);
                break;
            case R.drawable.dashboard_selector:
                mViewPager.setCurrentItem(mPagerAdapter.getItemIndex(mDashFragment), false);
                break;
            case R.drawable.contact_selector:
                showContactsFragment();
                break;
            case R.drawable.setting_selector:
                break;
        }
    }

    private void showContactsFragment() {
        ContactsFragment fragment = new ContactsFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        fragment.show(getSupportFragmentManager(), "contacts");
    }


    private void initView() {
        mViewPager = findViewById(R.id.view_pager_category);
        mPagerAdapter = new SlidePageAdapter(getSupportFragmentManager());

        mCategoryFragment = new CategoryFragment();
        mMapFragment = new MapFragment();
        mDashFragment = new DashBoardFragment();
        mPagerAdapter.addFragment(mCategoryFragment);
        mPagerAdapter.addFragment(mMapFragment);
        mPagerAdapter.addFragment(mDashFragment);
        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.setPageTransformer(true, new DepthPageTransformer());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), R.string.location_permission_get, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.location_permission_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_BLUETOOTH:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), R.string.bt_admin_permission_get, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.bt_admin_permission_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_CONTACT:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), R.string.bt_admin_permission_get, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.bt_admin_permission_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_ALL:
                Log.i(TAG, "request permission all");
                break;
        }
    }


}

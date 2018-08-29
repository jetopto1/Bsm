package com.jetopto.bsm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.jetopto.bsm.custom.view.SensorLevelView;
import com.jetopto.bsm.fragment.CategoryFragment;
import com.jetopto.bsm.fragment.MapFragment;
import com.jetopto.bsm.presenter.MainPresenterImpl;
import com.jetopto.bsm.presenter.interfaces.IBasePresenter;
import com.jetopto.bsm.presenter.interfaces.MainMvpView;
import com.jetopto.bsm.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MainMvpView, CategoryFragment.OnCategoryClick {

    private static final String TAG = MainActivity.class.getSimpleName();

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        Log.d(TAG, "resource id: " + id);
        switch (id) {
            case R.drawable.ic_map:
            break;
            case R.drawable.ic_dashboard:
            break;
            case R.drawable.ic_contact:
            break;
            case R.drawable.ic_setting:
            break;
        }
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
}
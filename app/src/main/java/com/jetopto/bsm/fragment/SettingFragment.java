package com.jetopto.bsm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jetopto.bsm.R;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = SettingFragment.class.getSimpleName();
    private final String TAG_BLUETOOTH = "Bluetooth";
    private final String TAG_BRIGHTNESS = "Brightness";
    private final String TAG_VOLUME = "Volume";
    private final String TAG_INFORMATION = "Information";
    private final String TAG_DEMO = "Demo";
    private FragmentTabHost mTabHost;
    private TextView mTitle;
    private ImageView mBrightness;
    private ImageView mBluetooth;
    private ImageView mVolume;
    private ImageView mInformation;
    private ImageView mDemo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mTabHost = view.findViewById(R.id.tap_layout);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_DEMO).setIndicator(TAG_DEMO),
                DemoFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_BRIGHTNESS).setIndicator(TAG_BRIGHTNESS),
                BrightnessFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_BLUETOOTH).setIndicator(TAG_BLUETOOTH),
                BluetoothFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_VOLUME).setIndicator(TAG_VOLUME),
                VolumeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_INFORMATION).setIndicator(TAG_INFORMATION),
                InformationFragment.class, null);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mTitle.setText(tabId);
                updateTabHost(tabId);
            }
        });
        mTitle = view.findViewById(R.id.title_view);
        mBrightness = view.findViewById(R.id.brightness);
        mBluetooth = view.findViewById(R.id.bluetooth);
        mVolume = view.findViewById(R.id.volume);
        mInformation = view.findViewById(R.id.information);
        mDemo = view.findViewById(R.id.demo);

        mBrightness.setOnClickListener(this);
        mBluetooth.setOnClickListener(this);
        mVolume.setOnClickListener(this);
        mInformation.setOnClickListener(this);
        mDemo.setOnClickListener(this);

    }

    private void updateTabHost(String tab) {
        int colorSelected = getResources().getColor(R.color.colorSettingSelectedBg);
        int colorDark = getResources().getColor(android.R.color.background_dark);
        //TODO Currently the selector of TabHost doesn't work, check it and remove ugly code of below
        mBluetooth.setBackgroundColor(colorDark);
        mBrightness.setBackgroundColor(colorDark);
        mVolume.setBackgroundColor(colorDark);
        mInformation.setBackgroundColor(colorDark);
        mDemo.setBackgroundColor(colorDark);
        switch (tab) {
            case TAG_DEMO:
                mDemo.setBackgroundColor(colorSelected);
                break;
            case TAG_BLUETOOTH:
                mBluetooth.setBackgroundColor(colorSelected);
                break;
            case TAG_BRIGHTNESS:
                mBrightness.setBackgroundColor(colorSelected);
                break;
            case TAG_VOLUME:
                mVolume.setBackgroundColor(colorSelected);
                break;
            case TAG_INFORMATION:
                mInformation.setBackgroundColor(colorSelected);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth:
                mTabHost.setCurrentTabByTag(TAG_BLUETOOTH);
                break;
            case R.id.brightness:
                mTabHost.setCurrentTabByTag(TAG_BRIGHTNESS);
                break;
            case R.id.volume:
                mTabHost.setCurrentTabByTag(TAG_VOLUME);
                break;
            case R.id.information:
                mTabHost.setCurrentTabByTag(TAG_INFORMATION);
                break;
            case R.id.demo:
                mTabHost.setCurrentTabByTag(TAG_DEMO);
                break;
        }
    }

    @Override
    public void handleKeyEvent(int keyCode) {
        int max = mTabHost.getTabWidget().getTabCount();
        int cur = mTabHost.getCurrentTab();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (cur > 0) {
                    mTabHost.setCurrentTab(--cur);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (cur < max) {
                    mTabHost.setCurrentTab(++cur);
                }
                break;
        }
        String tag = mTabHost.getCurrentTabTag();
        Fragment frag = getChildFragmentManager().findFragmentByTag(tag);
        if (frag instanceof BaseFragment) {
            ((BaseFragment) frag).handleKeyEvent(keyCode);
        }
    }
}

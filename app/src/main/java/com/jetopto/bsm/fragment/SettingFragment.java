package com.jetopto.bsm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetopto.bsm.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = SettingFragment.class.getSimpleName();

    private final String TAG_BLUETOOTH = BluetoothFragment.class.getSimpleName();
    private final String TAG_BRIGHTNESS = BrightnessFragment.class.getSimpleName();
    private final String TAG_VOLUME = VolumeFragment.class.getSimpleName();
    private final String TAG_INFORMATION = InformationFragment.class.getSimpleName();
    private final String TAG_DEMO = DemoFragment.class.getSimpleName();

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
        mTabHost.addTab(mTabHost.newTabSpec(TAG_BRIGHTNESS).setIndicator(TAG_BRIGHTNESS),
                BrightnessFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_BLUETOOTH).setIndicator(TAG_BLUETOOTH),
                BluetoothFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_VOLUME).setIndicator(TAG_VOLUME),
                VolumeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_INFORMATION).setIndicator(TAG_INFORMATION),
                InformationFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_DEMO).setIndicator(TAG_DEMO),
                DemoFragment.class, null);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth:
                mTabHost.setCurrentTabByTag(TAG_BLUETOOTH);
                mTitle.setText("Bluetooth");
                mBluetooth.setBackgroundColor(getResources().getColor(R.color.colorSettingSelectedBg));
                mBrightness.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mVolume.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mInformation.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mDemo.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case R.id.brightness:
                mTabHost.setCurrentTabByTag(TAG_BRIGHTNESS);
                mTitle.setText("Brightness");
                mBluetooth.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mBrightness.setBackgroundColor(getResources().getColor(R.color.colorSettingSelectedBg));
                mVolume.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mInformation.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mDemo.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case R.id.volume:
                mTabHost.setCurrentTabByTag(TAG_VOLUME);
                mTitle.setText("Volume");
                mBluetooth.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mBrightness.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mVolume.setBackgroundColor(getResources().getColor(R.color.colorSettingSelectedBg));
                mInformation.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mDemo.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case R.id.information:
                mTabHost.setCurrentTabByTag(TAG_INFORMATION);
                mTitle.setText("Information");
                mBluetooth.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mBrightness.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mVolume.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mInformation.setBackgroundColor(getResources().getColor(R.color.colorSettingSelectedBg));
                mDemo.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case R.id.demo:
                mTabHost.setCurrentTabByTag(TAG_DEMO);
                mTitle.setText("Demo");
                mBluetooth.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mBrightness.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mVolume.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mInformation.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                mDemo.setBackgroundColor(getResources().getColor(R.color.colorSettingSelectedBg));

                break;
        }
    }
}

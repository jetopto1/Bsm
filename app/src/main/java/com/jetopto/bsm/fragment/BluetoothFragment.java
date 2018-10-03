package com.jetopto.bsm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.PreferencesManager;
import com.jetopto.bsm.utils.Utils;

public class BluetoothFragment extends BaseFragment implements
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = BluetoothFragment.class.getSimpleName();

    private final String BSM_MAC_ONE = "D6:44:A1:7A:BB:83";
    private final String BSM_MAC_TWO = "F9:EA:43:D4:EF:1A";

    private ToggleButton mDeviceOneToggle;
    private ToggleButton mDeviceTwoToggle;
    private View mFocusedView;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initView(mMainView);
        return mMainView;
    }

    private void initView(View view) {
        String macAddress = PreferencesManager.getString(getActivity(),
                PreferencesManager.KEY_BSM_MAC, null);

        mDeviceOneToggle = view.findViewById(R.id.switch_one);
        mDeviceTwoToggle = view.findViewById(R.id.switch_two);
        mDeviceOneToggle.setOnCheckedChangeListener(this);
        mDeviceTwoToggle.setOnCheckedChangeListener(this);
        if (null != macAddress) {
            switch (macAddress) {
                case BSM_MAC_ONE:
                    mDeviceOneToggle.setChecked(true);
                    break;
                case BSM_MAC_TWO:
                    mDeviceTwoToggle.setChecked(true);
                    break;
            }
        }
        mFocusedView = mDeviceOneToggle;
    }


    @Override
    public void handleKeyEvent(int keyCode) {
        if (!mFocusedView.isFocused()) {
            mFocusedView.requestFocus();
            return;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mFocusedView.clearFocus();
                mFocusedView.setSelected(!mFocusedView.isSelected());
                mFocusedView = mMainView.findViewById(mFocusedView.getNextFocusRightId());
                mFocusedView.setSelected(!mFocusedView.isSelected());
                mFocusedView.requestFocus();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mFocusedView.clearFocus();
                mFocusedView.setSelected(!mFocusedView.isSelected());
                mFocusedView = mMainView.findViewById(mFocusedView.getNextFocusLeftId());
                mFocusedView.setSelected(!mFocusedView.isSelected());
                mFocusedView.requestFocus();
                break;
            case KeyEvent.KEYCODE_ENTER:
                ToggleButton btn = ((ToggleButton) mFocusedView);
                btn.setChecked(!btn.isChecked());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.switch_one:
                    mDeviceTwoToggle.setChecked(!isChecked);
                    break;
                case R.id.switch_two:
                    mDeviceOneToggle.setChecked(!isChecked);
                    break;
            }
        }
        handleBindingDevice(buttonView.getId());
    }

    private void handleBindingDevice(int viewId) {
        if (viewId == R.id.switch_one && mDeviceOneToggle.isChecked()) {
            PreferencesManager.put(getActivity(), PreferencesManager.KEY_BSM_MAC,
                    BSM_MAC_ONE);
        } else if (viewId == R.id.switch_two && mDeviceTwoToggle.isChecked()) {
            PreferencesManager.put(getActivity(), PreferencesManager.KEY_BSM_MAC,
                    BSM_MAC_TWO);
        } else {
            Utils.showToast(getActivity(), getString(R.string.msg_bsm_not_bind));
            PreferencesManager.remove(getActivity(), PreferencesManager.KEY_BSM_MAC);
        }
    }
}

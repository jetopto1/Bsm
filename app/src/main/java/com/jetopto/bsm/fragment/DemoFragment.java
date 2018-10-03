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

public class DemoFragment extends BaseFragment {
    private static final String TAG = DemoFragment.class.getSimpleName();

    private ToggleButton mDemoButton;
    private ToggleButton mNavButton;
    private View mFocusedView;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_demo, container, false);
        initView(mMainView);
        return mMainView;
    }

    private void initView(View view) {
        mDemoButton = view.findViewById(R.id.demo_switch);
        boolean demoMode = PreferencesManager.getBoolean(getContext(),
                PreferencesManager.KEY_DEMO_MODE, false);
        mDemoButton.setChecked(demoMode);
        mDemoButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(getContext(), PreferencesManager.KEY_DEMO_MODE, isChecked);
            }
        });

        mNavButton = view.findViewById(R.id.hide_nav_switch);
        boolean hidden = PreferencesManager.getBoolean(getContext(),
                PreferencesManager.KEY_NAVIGATION_HIDDEN, false);
        mNavButton.setChecked(hidden);
        mNavButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(getContext(), PreferencesManager.KEY_NAVIGATION_HIDDEN, isChecked);
            }
        });
        mFocusedView = mDemoButton;
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
}

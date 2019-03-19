package com.jetopto.bsm.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.jetopto.bsm.BsmApplication;
import com.jetopto.bsm.MainActivity;
import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.PreferencesManager;

public class DemoFragment extends BaseFragment {
    private static final String TAG = DemoFragment.class.getSimpleName();

    private ToggleButton mDemoButton;
    private ToggleButton mNavButton;
    private ToggleButton mInvButton;
    private View mFocusedView;
    private View mMainView;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_demo, container, false);
        mContext = BsmApplication.getAppContext().getApplicationContext();
        initView(mMainView);
        return mMainView;
    }

    private void initView(View view) {
        mDemoButton = view.findViewById(R.id.demo_switch);
        boolean demoMode = PreferencesManager.getBoolean(mContext,
                PreferencesManager.KEY_DEMO_MODE, false);
        mDemoButton.setChecked(demoMode);
        mDemoButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(mContext, PreferencesManager.KEY_DEMO_MODE, isChecked);
            }
        });

        mNavButton = view.findViewById(R.id.hide_nav_switch);
        boolean hidden = PreferencesManager.getBoolean(mContext,
                PreferencesManager.KEY_NAVIGATION_HIDDEN, false);
        mNavButton.setChecked(hidden);
        mNavButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(mContext, PreferencesManager.KEY_NAVIGATION_HIDDEN, isChecked);
            }
        });

        mInvButton = view.findViewById(R.id.upside_down_switch);
        boolean inverse = PreferencesManager.getBoolean(mContext, PreferencesManager.KEY_INVERSE_LAYOUT, false);
        mInvButton.setChecked(inverse);
        mInvButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(mContext, PreferencesManager.KEY_INVERSE_LAYOUT, isChecked);
                Intent i = new Intent(mContext, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
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

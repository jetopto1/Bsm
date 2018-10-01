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

    ToggleButton toggleButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        toggleButton = view.findViewById(R.id.demo_toggle);
        boolean demoMode = PreferencesManager.getBoolean(getContext(),
                PreferencesManager.KEY_DEMO_MODE, false);
        toggleButton.setChecked(demoMode);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesManager.put(getContext(), PreferencesManager.KEY_DEMO_MODE, isChecked);
            }
        });
    }

    @Override
    public void handleKeyEvent(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                toggleButton.setChecked(!toggleButton.isChecked());
                break;
        }
    }
}

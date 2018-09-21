package com.jetopto.bsm.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.PreferenceManager;

public class DemoFragment extends Fragment {
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
        boolean demoMode = PreferenceManager.getPreference(getContext(),
                "demoMode", false);
        toggleButton.setChecked(demoMode);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "isChecked: " + isChecked);
                PreferenceManager.editPreference(getContext(),"demoMode", isChecked);
            }
        });
    }
}

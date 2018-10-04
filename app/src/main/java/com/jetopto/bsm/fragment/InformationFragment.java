package com.jetopto.bsm.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jetopto.bsm.R;

public class InformationFragment extends Fragment {

    private static final String TAG = InformationFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView appTextView = view.findViewById(R.id.app_ver);
        TextView bsmTextView = view.findViewById(R.id.bsm_ver);
        appTextView.setText(getString(R.string.label_app_version, getDisplayVersion(getContext())));
        bsmTextView.setText(getString(R.string.label_bsm_version, getDisplayVersion(getContext())));
    }

    private String getDisplayVersion(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(getVersionName(context));
        sb.append(" - ");
        sb.append(getVersionCode(context));
        return sb.toString();
    }

    private String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}

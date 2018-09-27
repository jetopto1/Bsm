package com.jetopto.bsm.fragment;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.Utils;

public abstract class BaseDialogFragment extends DialogFragment {

    private static final String TAG = BaseDialogFragment.class.getSimpleName();

    protected void hideNavigationBar() {
        if (Utils.shouldHideNavBar(getResources())) {
            View decorView = getDialog().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    protected void registerOnKeyListener() {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getRepeatCount() == 15) {
                    getDialog().dismiss();
                }
                return false;
            }
        });
    }

    protected void setupSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / 1.5);
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = width;
        wlp.x = (int) (getResources().getDimension(R.dimen.sensor_margin_start) + 0.5);
        Log.i(TAG, "window width: " + width);
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }
}

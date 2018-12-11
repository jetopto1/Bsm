package com.jetopto.bsm.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.jetopto.bsm.R;
import com.jetopto.bsm.presenter.ContentObservePresenterImpl;
import com.jetopto.bsm.presenter.interfaces.ContentObserveMvpView;

public class BrightnessFragment extends BaseFragment implements ContentObserveMvpView {

    private static final String TAG = BrightnessFragment.class.getSimpleName();
    private AppCompatSeekBar mSeekBar;
    private AppCompatCheckBox mCheckBox;
    private ContentObservePresenterImpl mPresenter;
    private View mFocusedView;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_brightness, container, false);
        initView(mMainView);
        mPresenter = new ContentObservePresenterImpl(new Handler());
        mPresenter.attachView(getActivity(), this);
        return mMainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    private void initView(View view) {
        mCheckBox = view.findViewById(R.id.check_box);
        mSeekBar = view.findViewById(R.id.seek_bar);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(getBrightness());
        mSeekBar.setFocusable(true);

        mCheckBox.setChecked(isAutomaticMode());
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBrightnessMode(isChecked ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC :
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Brightness range 0 - 255
                int brightness = (int) (progress * 2.55 + 0.5);
                setBrightness(brightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mFocusedView = mCheckBox;
//        mFocusedView.requestFocus();
    }

    private void startManageWriteSetting() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private int getBrightnessMode() {
        int mode = -1;
        try {
            mode = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, 0);

            return mode;
        } catch (Exception ex) {
            Log.e(TAG, "Err: " + ex.getMessage(), ex);
        }
        return mode;
    }

    private void setBrightnessMode(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(getActivity())) {
            startManageWriteSetting();
        } else {
            try {
                Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
                getActivity().getContentResolver().notifyChange(uri, null);
            } catch (Exception ex) {
                Log.e(TAG, "Err: " + ex.getMessage(), ex);
            }
        }
    }

    private boolean isAutomaticMode() {
        try {
            return getBrightnessMode() == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Exception ex) {
            Log.e(TAG, "Err: " + ex.getMessage(), ex);
        }
        return false;
    }

    private int getBrightness() {
        try {
            int val = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, 0);
            val /= 2.55;
            return val;
        } catch (Exception ex) {
            Log.e(TAG, "Err: " + ex.getMessage(), ex);
        }
        return 0;
    }

    private void setBrightness(int brightness) {
        if ( null == getActivity()) {
            Log.e(TAG,"Activity " + getActivity());
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(getActivity())) {
            startManageWriteSetting();
        } else {
            try {
                Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, brightness);
                getActivity().getContentResolver().notifyChange(uri, null);
            } catch (Exception ex) {
                Log.e(TAG, "Err: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void handleKeyEvent(int keyCode) {
        if (!mFocusedView.isFocused()) {
            mFocusedView.requestFocus();
            return;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!mFocusedView.isSelected()) {
                    mFocusedView.clearFocus();
                    mFocusedView = mMainView.findViewById(mFocusedView.getNextFocusRightId());
                    mFocusedView.requestFocus();
                } else {
                    if (mFocusedView instanceof AppCompatSeekBar) {
                        AppCompatSeekBar seek = ((AppCompatSeekBar) mFocusedView);
                        int pro = seek.getProgress();
                        if (pro < seek.getMax()) {
                            pro += 10;
                            seek.setProgress(pro);
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!mFocusedView.isSelected()) {
                    mFocusedView.clearFocus();
                    mFocusedView = mMainView.findViewById(mFocusedView.getNextFocusLeftId());
                    mFocusedView.requestFocus();
                } else {
                    if (mFocusedView instanceof AppCompatSeekBar) {
                        AppCompatSeekBar seek = ((AppCompatSeekBar) mFocusedView);
                        int progress = seek.getProgress();
                        if (progress > 0) {
                            progress -= 10;
                            seek.setProgress(progress);
                        }
                    }
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                if (mFocusedView instanceof AppCompatCheckBox) {
                    mCheckBox.setChecked(!mCheckBox.isChecked());
                } else {
                    mFocusedView.setSelected(!mFocusedView.isSelected());
                }
                break;
        }
    }

    @Override
    public void onChanged(String type) {
        switch (type) {
            case TYPE_BRIGHTNESS_LEVEL:
                int cur = getBrightness();
                int pre = mSeekBar.getProgress();
                if (cur != pre) mSeekBar.setProgress(cur);
                break;
            case TYPE_BRIGHTNESS_MODE:
                mCheckBox.setChecked(isAutomaticMode());
                break;
        }
    }
}

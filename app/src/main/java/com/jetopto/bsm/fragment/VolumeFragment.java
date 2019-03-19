package com.jetopto.bsm.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.jetopto.bsm.BsmApplication;
import com.jetopto.bsm.R;
import com.jetopto.bsm.presenter.ContentObservePresenterImpl;
import com.jetopto.bsm.presenter.interfaces.ContentObserveMvpView;

public class VolumeFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, ContentObserveMvpView {

    private static final String TAG = VolumeFragment.class.getSimpleName();

    private AppCompatSeekBar mAlarmSeekBar;
    private AppCompatSeekBar mMusicSeekBar;
    private AppCompatSeekBar mRingSeekBar;
    private AudioManager mAudioManager;
    private ContentObservePresenterImpl mPresenter;
    private View mFocusedView;
    private View mMainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        mMainView = inflater.inflate(R.layout.fragment_volume, container, false);
        initView(mMainView);
        mPresenter = new ContentObservePresenterImpl(new Handler());
        mPresenter.attachView(getActivity(), this);
        return mMainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged");
    }

    private void initView(View view) {
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        mAlarmSeekBar = view.findViewById(R.id.alarm_seek_bar);
        mMusicSeekBar = view.findViewById(R.id.music_seek_bar);
        mRingSeekBar = view.findViewById(R.id.ring_seek_bar);

        mAlarmSeekBar.setMax(getMaxVolume(mAudioManager, AudioManager.STREAM_ALARM));
        mMusicSeekBar.setMax(getMaxVolume(mAudioManager, AudioManager.STREAM_MUSIC));
        mRingSeekBar.setMax(getMaxVolume(mAudioManager, AudioManager.STREAM_RING));

        mAlarmSeekBar.setProgress(getCurrentVolume(mAudioManager, AudioManager.STREAM_ALARM));
        mMusicSeekBar.setProgress(getCurrentVolume(mAudioManager, AudioManager.STREAM_MUSIC));
        mRingSeekBar.setProgress(getCurrentVolume(mAudioManager, AudioManager.STREAM_RING));

        mAlarmSeekBar.setOnSeekBarChangeListener(this);
        mMusicSeekBar.setOnSeekBarChangeListener(this);
        mRingSeekBar.setOnSeekBarChangeListener(this);
        mFocusedView = mRingSeekBar;
    }

    private int getMaxVolume(AudioManager manager, int type) {
        return manager.getStreamMaxVolume(type);
    }

    private int getCurrentVolume(AudioManager manager, int type) {
        return manager.getStreamVolume(type);
    }

    private void setVolume(AudioManager manager, int type, int volume) {
        manager.setStreamVolume(type, volume, 0);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.alarm_seek_bar:
                setVolume(mAudioManager, AudioManager.STREAM_ALARM, progress);
                break;
            case R.id.music_seek_bar:
                setVolume(mAudioManager, AudioManager.STREAM_MUSIC, progress);
                break;
            case R.id.ring_seek_bar:
                NotificationManager notificationManager = (NotificationManager) BsmApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        && !notificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    startActivity(intent);
                    return;
                }
                setVolume(mAudioManager, AudioManager.STREAM_RING, progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                    AppCompatSeekBar seek = ((AppCompatSeekBar) mFocusedView);
                    int progress = seek.getProgress();
                    if (progress < seek.getMax()) {
                        seek.setProgress(++progress);
                    }
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!mFocusedView.isSelected()) {
                    mFocusedView.clearFocus();
                    mFocusedView = mMainView.findViewById(mFocusedView.getNextFocusLeftId());
                    mFocusedView.requestFocus();
                } else {
                    AppCompatSeekBar seek = ((AppCompatSeekBar) mFocusedView);
                    int progress = seek.getProgress();
                    if (progress > 0) {
                        seek.setProgress(--progress);
                    }
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                mFocusedView.setSelected(!mFocusedView.isSelected());
                break;
        }
    }

    @Override
    public void onChanged(String type) {
        Log.d(TAG, "onChanged: " + type);
        int curVolume;
        int preVolume;
        switch (type) {
            case TYPE_ALARM:
                curVolume = getCurrentVolume(mAudioManager, AudioManager.STREAM_ALARM);
                preVolume = mAlarmSeekBar.getProgress();
                if (curVolume != preVolume) mAlarmSeekBar.setProgress(curVolume);
                break;
            case TYPE_MUSIC:
                curVolume = getCurrentVolume(mAudioManager, AudioManager.STREAM_MUSIC);
                preVolume = mMusicSeekBar.getProgress();
                if (curVolume != preVolume) mMusicSeekBar.setProgress(curVolume);
                break;
            case TYPE_RING:
                curVolume = getCurrentVolume(mAudioManager, AudioManager.STREAM_RING);
                preVolume = mRingSeekBar.getProgress();
                if (curVolume != preVolume) mRingSeekBar.setProgress(curVolume);
                break;
        }
    }
}

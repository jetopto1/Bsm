package com.jetopto.bsm.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.Constant;

public class VideoFragment extends BaseDialogFragment {

    private static final String TAG = VideoFragment.class.getSimpleName();

    private int mVideo;

    public static VideoFragment newInstance(int video) {
        Log.i(TAG, "video: " + video);
        VideoFragment f = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.VIDEO_FILE, video);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideo = getArguments().getInt(Constant.VIDEO_FILE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        setupSize();
        registerOnKeyListener();
        hideNavigationBar();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int videoFile;
        switch (mVideo) {
            case Constant.PLAY_NAVIGATION_FILE:
                videoFile = R.raw.test;
                break;
            case Constant.PLAY_DVR_FILE:
            default:
                videoFile = R.raw.dvr_demo;
                break;

        }
        VideoView videoView = getView().findViewById(R.id.video_view);
        Uri video = Uri.parse("android.resource://" +
                getActivity().getPackageName() + "/" + videoFile);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    @Override
    protected void setupSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels -
                (getResources().getDimension(R.dimen.sensor_status_width) * 3));
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = width;
        wlp.x = (int) (getResources().getDimension(R.dimen.sensor_margin_start) + 0.5);
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }
}

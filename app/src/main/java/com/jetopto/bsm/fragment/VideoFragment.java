package com.jetopto.bsm.fragment;

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

public class VideoFragment extends BaseDialogFragment {

    private static final String TAG = VideoFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        setupSize();
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VideoView videoView = getView().findViewById(R.id.video_view);
        Uri video = Uri.parse("android.resource://" +
                getActivity().getPackageName() + "/" + R.raw.test);
        Log.i(TAG, "uri " + video.getPath());
        Log.i(TAG, "uri " + video.toString());
        videoView.setVideoURI(video);
        videoView.start();
    }

    @Override
    protected void setupSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / 1.5);
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }
}

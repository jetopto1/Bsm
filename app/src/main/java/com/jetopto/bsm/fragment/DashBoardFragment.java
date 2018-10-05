package com.jetopto.bsm.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jetopto.bsm.R;

public class DashBoardFragment extends Fragment {

    private static final String TAG = DashBoardFragment.class.getSimpleName();
    private static final int MAX_SPEED = 250;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private TextView mHundredView;
    private TextView mTenView;
    private TextView mUnitView;
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i <= MAX_SPEED; i++) {
                try {
                    updateView(i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.i(TAG, "interrupted, stop it.");
                    return;
                } catch (NullPointerException ne) {
                    Log.e(TAG, ne.getMessage(), ne);
                    return;
                }
            }
            afterSpeedUp();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /* TODO move data fetching function to presenter
         *  TODO this phase is for demo
         */
        if (!isHidden()) {
            testData();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            onPause();
        } else {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTestingThread();
    }

    private void initView(View view) {
        mHundredView = view.findViewById(R.id.hundred);
        mTenView = view.findViewById(R.id.ten);
        mUnitView = view.findViewById(R.id.unit);
        Typeface ty = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DS-DIGI.TTF");
        mHundredView.setTypeface(ty);
        mTenView.setTypeface(ty);
        mUnitView.setTypeface(ty);
    }

    private void testData() {
        if (null != mHandlerThread) {
            stopTestingThread();
        }
        mHandlerThread = new HandlerThread("Sheldon");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(mRunnable);
    }

    private void stopTestingThread() {
        if (null != mHandlerThread) {
            mHandlerThread.interrupt();
            mHandler.removeCallbacks(mRunnable);
            mHandlerThread.quit();
            mHandlerThread = null;
            mHandler = null;
        }
    }

    private void afterSpeedUp() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateView((int) (Math.random() *
                        (MAX_SPEED - (MAX_SPEED - 10) + 1)) + (MAX_SPEED - 10));
                mHandler.postDelayed(this, 500);
            }
        }, 500);
    }

    private void updateView(final int speed) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHundredView.setText(String.valueOf((speed / 100 % 10)));
                mTenView.setText(String.valueOf(speed / 10 % 10));
                mUnitView.setText(String.valueOf(speed % 10));
            }
        });
    }
}

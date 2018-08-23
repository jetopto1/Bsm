package com.jetopto.bsm.core;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jetopto.bsm.core.listener.ISensorStateListener;
import com.jetopto.bsm.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorMonitorService extends Service {

    private static final String TAG = SensorMonitorService.class.getSimpleName();


    private List<ISensorStateListener> mListenerList = new ArrayList<>();
    private SensorMonitorBinder mBinder = new SensorMonitorBinder();
    private Handler mHandler = new Handler();

    public SensorMonitorService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void registerListener(ISensorStateListener listener) {
        mListenerList.add(listener);
    }

    public void unRegisterListener(ISensorStateListener listener) {
        if (!mListenerList.isEmpty() && mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }


    public void monitorSensor() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //TODO remove these shit, do the right thing
                Random random = new Random();
                int level = random.nextInt(3) + 1;
                String rState = "safety";
                String lState = "safety";
                switch (level) {
                    case 1:
                        lState = "safety";
                        rState = "safety";
                        break;
                    case 2:
                        lState = "urgent";
                        rState = "urgent";
                        break;
                    case 3:
                        lState = "emergent";
                        rState = "emergent";
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.LEFT_SENSOR_LEVEL, lState);
                bundle.putString(Constant.RIGHT_SENSOR_LEVEL, rState);
                updateListener(bundle);
                mHandler.postDelayed(this, 2000);
            }
        }, 1000);
    }

    private void updateListener(Bundle bundle) {
        for (ISensorStateListener listener : mListenerList) {
            listener.onStateChanged(bundle);
        }
    }

    public class SensorMonitorBinder extends Binder {
        public SensorMonitorBinder() {
            super();
        }

        public SensorMonitorService getService() {
            return SensorMonitorService.this;
        }
    }
}

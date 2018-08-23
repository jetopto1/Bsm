package com.jetopto.bsm.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.jetopto.bsm.core.SensorMonitorService;
import com.jetopto.bsm.core.listener.ISensorStateListener;
import com.jetopto.bsm.presenter.interfaces.IBasePresenter;
import com.jetopto.bsm.presenter.interfaces.MainMvpView;

public class MainPresenterImpl implements IBasePresenter<MainMvpView>, ISensorStateListener, ServiceConnection {

    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private Context mContext;
    private MainMvpView mMvpView;
    private SensorMonitorService mService;

    public MainPresenterImpl() {
        super();
    }

    @Override
    public void attachView(Context context, MainMvpView view) {
        mContext = context;
        mMvpView = view;
        Intent intent = new Intent(context, SensorMonitorService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void detachView() {
        mContext.unbindService(this);
    }

    @Override
    public void onStateChanged(Bundle bundle) {
        mMvpView.onSensorStateChanged(bundle);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((SensorMonitorService.SensorMonitorBinder) service).getService();
        mService.registerListener(this);
        mService.monitorSensor();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService.unRegisterListener(this);
    }
}

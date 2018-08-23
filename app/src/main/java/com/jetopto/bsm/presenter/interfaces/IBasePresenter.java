package com.jetopto.bsm.presenter.interfaces;

import android.content.Context;

public interface IBasePresenter<V> {
    void attachView(Context context, V view);

    void detachView();
}

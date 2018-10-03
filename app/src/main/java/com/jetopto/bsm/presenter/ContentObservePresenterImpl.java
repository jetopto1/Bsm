package com.jetopto.bsm.presenter;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.jetopto.bsm.presenter.interfaces.ContentObserveMvpView;
import com.jetopto.bsm.presenter.interfaces.IBasePresenter;

public class ContentObservePresenterImpl extends ContentObserver implements IBasePresenter<ContentObserveMvpView> {

    private static final String TAG = ContentObservePresenterImpl.class.getSimpleName();

    private Context mContext;
    private ContentObserveMvpView mMvpView;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ContentObservePresenterImpl(Handler handler) {
        super(handler);
    }


    @Override
    public void attachView(Context context, ContentObserveMvpView view) {
        mContext = context;
        mMvpView = view;
        context.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI, true, this);
    }

    @Override
    public void detachView() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String path = uri.getPath().replaceAll("/system/", "");
        mMvpView.onChanged(path);
    }
}

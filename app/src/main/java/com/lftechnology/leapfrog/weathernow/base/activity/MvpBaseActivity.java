package com.lftechnology.leapfrog.weathernow.base.activity;

import android.os.Bundle;

import com.lftechnology.leapfrog.weathernow.base.presenter.BasePresenter;
import com.lftechnology.leapfrog.weathernow.base.view.BaseView;

import javax.inject.Inject;

/**
 * Should be implemented by activity which makes use of {@link BasePresenter} and {@link BaseView}
 *
 * @param <T> {@link BasePresenter}
 */
public abstract class MvpBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    @Inject
    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.deAttachView();
        }
    }
}

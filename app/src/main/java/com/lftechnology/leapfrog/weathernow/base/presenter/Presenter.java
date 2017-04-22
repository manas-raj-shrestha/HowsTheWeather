package com.lftechnology.leapfrog.weathernow.base.presenter;


import com.lftechnology.leapfrog.weathernow.base.view.BaseView;

import rx.Subscription;

public interface Presenter<T extends BaseView> {

    void attachView(T t);

    void deAttachView();

    boolean isViewAttached();

    T getView();

    void onResume();

    void addSubscription(Subscription subscription);

}

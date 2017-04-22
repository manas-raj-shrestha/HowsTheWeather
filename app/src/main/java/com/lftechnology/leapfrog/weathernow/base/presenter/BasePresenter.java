package com.lftechnology.leapfrog.weathernow.base.presenter;


import com.lftechnology.leapfrog.weathernow.base.view.BaseView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Needs to extends by all presenter
 *
 * @param <T> {@link BaseView}
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private T t;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void attachView(T t) {
        this.t = t;
    }

    @Override
    public void deAttachView() {
        this.t = null;
    }

    @Override
    public boolean isViewAttached() {
        return t != null;
    }

    @Override
    public T getView() {
        return t;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void addSubscription(Subscription subscription) {
        this.compositeSubscription.add(compositeSubscription);
    }

}

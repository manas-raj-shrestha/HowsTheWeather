package com.lftechnology.leapfrog.weathernow.base.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lftechnology.leapfrog.weathernow.base.presenter.BasePresenter;
import com.lftechnology.leapfrog.weathernow.base.view.BaseView;
import com.lftechnology.leapfrog.weathernow.injection.DaggerWeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.WeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.modules.NetworkModule;
import com.lftechnology.leapfrog.weathernow.injection.modules.PresenterModule;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Injects dagger and butter knife to make code reusable
 */
public abstract class MvpBaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    @Inject
    protected T presenter;

    private Unbinder unbinder;
    protected ProgressDialog progressDialog;
    private WeatherNowComponent natureCollectionsComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDagger(providesPresenterComponents());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeProgressDialogue();
        return view;
    }

    private void initializeProgressDialogue() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Just A Moment");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeProgressDialogue();
    }

    protected abstract int getLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.deAttachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Provides instance of PresenterComponent{@link WeatherNowComponent}
     *
     * @return instance of {@link WeatherNowComponent}
     */
    public WeatherNowComponent providesPresenterComponents() {
        if (natureCollectionsComponent == null) {
            natureCollectionsComponent = DaggerWeatherNowComponent.builder()
                    .networkModule(new NetworkModule())
                    .presenterModule(new PresenterModule())
                    .build();
        }
        return natureCollectionsComponent;
    }

    protected abstract void injectDagger(WeatherNowComponent natureCollectionsComponent);

    /**
     * Shows toast with message for short length
     *
     * @param context {@link Context}
     * @param message message to be displayed
     */
    public void showToast(Context context, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }
}

package com.lftechnology.leapfrog.weathernow.base.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lftechnology.leapfrog.weathernow.injection.DaggerWeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.WeatherNowComponent;
import com.lftechnology.leapfrog.weathernow.injection.modules.NetworkModule;
import com.lftechnology.leapfrog.weathernow.injection.modules.PresenterModule;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    private WeatherNowComponent weatherNowComponent;
    protected ProgressDialog progressDialog;

    /**
     * abstract method which returns id of layout in the form of R.layout.layout_name.
     *
     * @return id of layout in the form of R.layout.layout_name
     */
    protected abstract int getLayout();

    protected boolean includeActivityTransition() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());

        unbinder = ButterKnife.bind(this);

        injectDagger(providesPresenterComponents());
        initializeProgressDialogue();

    }

    /**
     * Provides instance of PresenterComponent{@link WeatherNowComponent}
     *
     * @return instance of {@link WeatherNowComponent}
     */
    public WeatherNowComponent providesPresenterComponents() {
        if (weatherNowComponent == null) {
            weatherNowComponent = DaggerWeatherNowComponent.builder()
                    .networkModule(new NetworkModule())
                    .presenterModule(new PresenterModule())
                    .build();
        }
        return weatherNowComponent;
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private void initializeProgressDialogue() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Just A Moment");
    }

    /**
     * Intercept touch event for hiding keyboard while pressing outside of edit text
     *
     * @param event {@link MotionEvent}
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard(this);
        return true;
    }

    protected abstract void injectDagger(WeatherNowComponent weatherNowComponent);

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

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}

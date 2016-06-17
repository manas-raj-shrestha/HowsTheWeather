package com.droid.manasshrestha.rxandroid.splashscreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.data.PrefUtils;
import com.droid.manasshrestha.rxandroid.update.UpdateService;
import com.droid.manasshrestha.rxandroid.weathercards.WeatherCardsActivity;
import com.droid.manasshrestha.rxandroid.widget.BroadCastText;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Splash screen with tutorial
 */
public class SplashActivity extends AppCompatActivity {

    private static final int ANIMATION_DELAY = 500;
    private static final int IMG_WIDTH = 250;
    private static final int IMG_HEIGHT = 250;

    @Bind(R.id.iv_bg)
    ImageView ivBg;

    @Bind(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);

//        if (!PrefUtils.getFirstRun()) {
//            startActivity(new Intent(this, WeatherCardsActivity.class));
//            finish();
//            return;
//        }

        createAlarm(this);
        Log.e("check","log");
        Glide.with(this).load(R.drawable.clearsky_bg).override(IMG_WIDTH, IMG_HEIGHT).into(ivBg);

        viewPager.setAdapter(new TutorialScreenPagerAdapter(getSupportFragmentManager()));

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                new Handler().postDelayed(() -> ((PageChangeListener) getActiveFragment(viewPager, position)).onPageChangeListener(position), ANIMATION_DELAY);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPager.addOnPageChangeListener(onPageChangeListener);
        inkPageIndicator.setViewPager(viewPager);
        onPageChangeListener.onPageSelected(0);
    }

    /**
     * returns fragment at given position
     *
     * @param container {@link ViewPager} instance
     * @param position  position
     * @return {@link Fragment}
     */
    private Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return getSupportFragmentManager().findFragmentByTag(name);
    }

    /**
     * constructs the fragment name
     *
     * @param viewId viewpager id
     * @param index  fragment position
     * @return fragment name
     */
    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    @OnClick({R.id.btn_skip, R.id.iv_next_arrow})
    public void setOnClicks(View view) {
        switch (view.getId()) {
            case R.id.btn_skip:
                System.exit(0);
                break;
            case R.id.iv_next_arrow:
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(this, WeatherCardsActivity.class));
                    finish();
                }
                break;
        }
    }

    private void createAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,  UpdateService.class);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.DATE, 1);

//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 6, 0, 0);
//        Log.e("Calendar before", String.valueOf(calendar.getTime()));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
    }
}

package com.lftechnology.leapfrog.weathernow.splashscreen;

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
import com.droid.manasshrestha.weathernow.R;
import com.lftechnology.leapfrog.weathernow.data.PrefUtils;
import com.lftechnology.leapfrog.weathernow.weathercards.WeatherCardsActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

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

        Log.e("check","log "+getResources().getDisplayMetrics().density);

        if (!PrefUtils.getFirstRun()) {
            startActivity(new Intent(this, WeatherCardsActivity.class));
            finish();
            return;
        }

        Glide.with(this).load(R.drawable.clearsky_bg).override(IMG_WIDTH, IMG_HEIGHT).into(ivBg);

        viewPager.setAdapter(new TutorialScreenPagerAdapter(getSupportFragmentManager()));

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //no action required on page scrolled
            }

            @Override
            public void onPageSelected(int position) {
                new Handler().postDelayed(() -> ((PageChangeListener) getActiveFragment(viewPager, position)).onPageChangeListener(position), ANIMATION_DELAY);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //no action required on scroll state changed
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

}

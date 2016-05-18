package com.droid.manasshrestha.rxandroid.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathercards.WeatherCardsActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Splash screen with tutorial
 */
public class SplashActivity extends AppCompatActivity {

    private static final int ANIMATION_DELAY = 200;
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

        Glide.with(this).load(R.drawable.clearsky_bg).override(IMG_WIDTH, IMG_HEIGHT).into(ivBg);

        viewPager.setAdapter(new SplashScreenPagerAdapter(getSupportFragmentManager()));

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

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return getSupportFragmentManager().findFragmentByTag(name);
    }

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

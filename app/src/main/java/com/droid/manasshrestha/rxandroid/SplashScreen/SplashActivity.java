package com.droid.manasshrestha.rxandroid.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.weathercards.WeatherCardsActivity;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Manas on 5/11/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.iv_bg)
    ImageView ivBg;

    @Bind(R.id.indicator)
    InkPageIndicator inkPageIndicator;

    @Bind(R.id.btn_skip)
    Button btnSkip;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.iv_next_arrow)
    ImageView ivNextArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.clearsky_bg).override(500, 500).into(ivBg);

        viewPager.setAdapter(new SplashScreenPagerAdapter(getSupportFragmentManager()));

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                new Handler().postDelayed(() -> ((PageChangeListener) getActiveFragment(viewPager, position)).onPageChangeListener(position), 200);

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
                startActivity(new Intent(this, WeatherCardsActivity.class));
                finish();
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

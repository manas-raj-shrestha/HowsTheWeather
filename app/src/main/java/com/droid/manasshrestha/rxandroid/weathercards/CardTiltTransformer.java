package com.droid.manasshrestha.rxandroid.weathercards;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.droid.manasshrestha.rxandroid.R;
import com.droid.manasshrestha.rxandroid.WeatherApplication;

/**
 * Card tilt view pager animation
 */
public class CardTiltTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
            view.setRotation((float) (0));

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
            if (position == 0) {
                view.setRotation(0);
            } else if (position == -1) {
                view.setRotation(0);
                view.setTranslationX(0);
            } else {
                view.setTranslationX(position * WeatherApplication.getContext().getResources().getDimension(R.dimen.card_x_translation));
                view.setRotation((position) * 25f);
            }
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setRotation(0);
            view.setAlpha(1 - position);
            view.setRotation((float) (view.getRotation() + position));
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setRotation(0);

        } else { // (1,+Infinity]
            Log.e("position is ", "else");
            // This page is way off-screen to the right.
            view.setRotation(0);
            view.setAlpha(0);
        }
    }
}
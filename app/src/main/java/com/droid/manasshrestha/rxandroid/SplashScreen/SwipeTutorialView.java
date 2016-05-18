package com.droid.manasshrestha.rxandroid.splashscreen;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Manas on 5/17/2016.
 */
public interface SwipeTutorialView {

    View getFrontCardView();

    View getBackCardView();

    void animateFrontCard(int rotation, float translation);

    void animateBackCardSize(RelativeLayout.LayoutParams layoutParams);

    void animateBackCardAlpha(int alpha);

    interface PresenterActions {
        void startAnimation();

        void onPageSelected(int position, int currentPagePosition);
    }
}

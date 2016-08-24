package com.lftechnology.leapfrog.weathernow.splashscreen;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * contract for swipe tutorial
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

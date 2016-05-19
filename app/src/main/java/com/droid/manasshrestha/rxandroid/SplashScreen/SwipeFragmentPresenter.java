package com.droid.manasshrestha.rxandroid.splashscreen;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.droid.manasshrestha.rxandroid.GeneralUtils;

/**
 * handles animation logic for swipe fragment
 */
public class SwipeFragmentPresenter implements SwipeTutorialView.PresenterActions {

    private static final int ANIMATION_DURATION = 1500;
    private static final int ANIMATION_DELAY = 1000;
    private static final int MAX_ROTATION = -30;
    private static final int TRANSLATION_INCREMENT = -16;
    private static final int MAX_MARGIN = 20;
    private static final int MIN_MARGIN = 0;

    private final SwipeTutorialView swipeTutorialView;

    private ValueAnimator alphaAnimator;
    private ValueAnimator marginAnimator;
    private ValueAnimator rotationAnimator;


    public SwipeFragmentPresenter(SwipeTutorialView swipeTutorialView) {
        this.swipeTutorialView = swipeTutorialView;
    }

    @Override
    public void startAnimation() {
        animateFrontCard(swipeTutorialView.getFrontCardView());
        animateBackCardAlpha();
        animateBackCardSize();
    }

    private void animateFrontCard(View view) {
        rotationAnimator = ValueAnimator.ofInt((int) view.getRotation(), MAX_ROTATION);
        rotationAnimator.setDuration(ANIMATION_DURATION);
        rotationAnimator.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();

            swipeTutorialView.animateFrontCard(value, view.getTranslationX() - TRANSLATION_INCREMENT);
        });
    }

    private void animateBackCardAlpha() {
        alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.setDuration(ANIMATION_DURATION);
        alphaAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();

            swipeTutorialView.animateBackCardAlpha(value);
        });
    }

    private void animateBackCardSize() {
        marginAnimator = ValueAnimator.ofInt((int) GeneralUtils.convertDpToPixel(MAX_MARGIN), MIN_MARGIN);
        marginAnimator.setDuration(ANIMATION_DURATION);
        marginAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            rlParams.setMargins(value, value, value, value);

            swipeTutorialView.animateBackCardSize(rlParams);
        });
    }

    @Override
    public void onPageSelected(int position, int currentPagePosition) {
        if (currentPagePosition == position) {

            rotationAnimator.cancel();
            marginAnimator.cancel();
            alphaAnimator.cancel();

            swipeTutorialView.getFrontCardView().setRotation(0);
            swipeTutorialView.getFrontCardView().setTranslationX(0);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            int value = (int) GeneralUtils.convertDpToPixel(MAX_MARGIN);
            rlParams.setMargins(value, value, value, value);
            swipeTutorialView.getBackCardView().setLayoutParams(rlParams);

            new Handler().postDelayed(() -> {
                swipeTutorialView.getBackCardView().setVisibility(View.VISIBLE);
                rotationAnimator.start();
                marginAnimator.start();
                alphaAnimator.start();
            }, ANIMATION_DELAY);

        }
    }
}

package com.droid.manasshrestha.rxandroid.splashscreen;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.droid.manasshrestha.rxandroid.GeneralUtils;

/**
 * Created by Manas on 5/17/2016.
 */
public class SwipeFragmentPresenter implements SwipeTutorialView.PresenterActions {

    private static final int ANIMATION_DURATION = 1500;
    private static final int ANIMATION_DELAY = 1000;
    private ValueAnimator alphaAnimator;
    private ValueAnimator marginAnimator;
    private ValueAnimator rotationAnimator;
    private float initialTranslationX;
    private SwipeTutorialView swipeTutorialView;

    public SwipeFragmentPresenter(SwipeTutorialView swipeTutorialView) {
        this.swipeTutorialView = swipeTutorialView;
    }

    @Override
    public void startAnimation() {
        animateFrontCard(swipeTutorialView.getFrontCardView());
        animateBackCardAlpha();
        animateBackCardSize();
    }

    public void animateFrontCard(View view) {
        initialTranslationX = view.getTranslationX();
        rotationAnimator = ValueAnimator.ofInt((int) view.getRotation(), -30);
        rotationAnimator.setDuration(ANIMATION_DURATION);
        rotationAnimator.addUpdateListener(animation -> {
            Integer value = (Integer) animation.getAnimatedValue();

            swipeTutorialView.animateFrontCard(value, view.getTranslationX() - 16);
        });
    }

    public void animateBackCardAlpha() {
        alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.setDuration(ANIMATION_DURATION);
        alphaAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();

            swipeTutorialView.animateBackCardAlpha(value);
//            ivDummyCard2.setImageAlpha(value);
        });
    }

    public void animateBackCardSize() {
        marginAnimator = ValueAnimator.ofInt((int) GeneralUtils.convertDpToPixel(20), 0);
        marginAnimator.setDuration(ANIMATION_DURATION);
        marginAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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
            int value = (int) GeneralUtils.convertDpToPixel(20);
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

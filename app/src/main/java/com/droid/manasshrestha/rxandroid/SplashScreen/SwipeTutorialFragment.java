package com.droid.manasshrestha.rxandroid.splashscreen;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Manas on 5/11/2016.
 */
public class SwipeTutorialFragment extends Fragment implements PageChangeListener {

    private static final String KEY_POSITION = "position";
    private int position;

    @Bind(R.id.iv_dummy_card)
    ImageView ivDummyCard;

    @Bind(R.id.iv_dummy_card_2)
    ImageView ivDummyCard2;

    ValueAnimator alphaAnimator;
    ValueAnimator marginAnimator;
    ValueAnimator rotationAnimator;
    float initialTranslationX;

    public static SwipeTutorialFragment getInstance(int position) {
        SwipeTutorialFragment swipeTutorialFragment = new SwipeTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        swipeTutorialFragment.setArguments(bundle);

        return swipeTutorialFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.first_splash_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(this).load(R.drawable.snow_front).override((int) GeneralUtils.convertDpToPixel(300), (int) GeneralUtils.convertDpToPixel(300)).into(ivDummyCard);
        Glide.with(getActivity()).load(R.drawable.clear_weather).override((int) GeneralUtils.convertDpToPixel(300), (int) GeneralUtils.convertDpToPixel(300)).into(ivDummyCard2);

        this.position = getArguments().getInt(KEY_POSITION);

        initialTranslationX = ivDummyCard.getTranslationX();
        rotationAnimator = ValueAnimator.ofInt((int) ivDummyCard.getRotation(), -30);
        rotationAnimator.setDuration(1500);
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                ivDummyCard.setRotation(value);
                ivDummyCard.setTranslationX(ivDummyCard.getTranslationX() - 16);
            }
        });

        marginAnimator = ValueAnimator.ofInt((int) GeneralUtils.convertDpToPixel(20), 0);
        marginAnimator.setDuration(1500);
        marginAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            rlParams.setMargins(value, value, value, value);
            ivDummyCard2.setLayoutParams(rlParams);
        });

        alphaAnimator = ValueAnimator.ofInt(0, 255);
        alphaAnimator.setDuration(1500);
        alphaAnimator.addUpdateListener((animation) -> {
            int value = (int) animation.getAnimatedValue();
            ivDummyCard2.setImageAlpha(value);
        });


    }

    @Override
    public void onPageChangeListener(int position) {

        if (this.position == position) {

            rotationAnimator.cancel();
            marginAnimator.cancel();
            alphaAnimator.cancel();

            ivDummyCard.setRotation(0);
            ivDummyCard.setTranslationX(0);
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            int value = (int) GeneralUtils.convertDpToPixel(20);
            rlParams.setMargins(value, value, value, value);
            ivDummyCard2.setLayoutParams(rlParams);

            new Handler().postDelayed(() -> {
                ivDummyCard2.setVisibility(View.VISIBLE);
                rotationAnimator.start();
                marginAnimator.start();
                alphaAnimator.start();
            }, 1000);

        }
    }

}

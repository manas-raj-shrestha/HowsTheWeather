package com.lftechnology.leapfrog.weathernow.splash;

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
import com.lftechnology.leapfrog.weathernow.utils.GeneralUtils;
import com.droid.manasshrestha.weathernow.R;
import com.lftechnology.leapfrog.weathernow.weathercards.FlipAnimation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * tap tutorial fragment
 */
public class TapTutorialFragment extends Fragment implements PageChangeListener {

    private static final String KEY_POSITION = "position";
    private static final int IMG_HEIGHT = 300;
    private static final int IMG_WIDTH = 300;
    private static final int ANIMATION_DELAY = 1000;

    @Bind(R.id.iv_dummy_back)
    ImageView ivDummyBack;

    @Bind(R.id.iv_dummy_front)
    ImageView ivDummyFront;

    @Bind(R.id.rl_card_container)
    RelativeLayout relativeLayout;

    public static TapTutorialFragment getInstance(int position) {
        TapTutorialFragment firstScreenFragment = new TapTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        firstScreenFragment.setArguments(bundle);

        return firstScreenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tap_tutorial_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(this).load(R.drawable.snow_front).override((int) GeneralUtils.convertDpToPixel(IMG_WIDTH),
                (int) GeneralUtils.convertDpToPixel(IMG_HEIGHT)).into(ivDummyFront);
        Glide.with(getActivity()).load(R.drawable.snow_back).override((int) GeneralUtils.convertDpToPixel(IMG_WIDTH),
                (int) GeneralUtils.convertDpToPixel(IMG_HEIGHT)).into(ivDummyBack);

    }

    @Override
    public void onPageChangeListener(int position) {

        new Handler().postDelayed(() -> {
            FlipAnimation flipAnimation = new FlipAnimation(ivDummyFront, ivDummyBack);

            if (ivDummyFront.getVisibility() == View.GONE) {
                flipAnimation.reverse();
            }

            relativeLayout.startAnimation(flipAnimation);
        }, ANIMATION_DELAY);

    }

}

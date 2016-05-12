package com.droid.manasshrestha.rxandroid.splashscreen;

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
import com.droid.manasshrestha.rxandroid.weathercards.FlipAnimation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Manas on 5/12/2016.
 */
public class TapTutorialFragment extends Fragment implements PageChangeListener {

    private static final String KEY_POSITION = "position";
    private int position;

    @Bind(R.id.iv_dummy_back)
    ImageView ivDummyBack;

    @Bind(R.id.iv_dummy_front)
    ImageView ivDummyFront;

    @Bind(R.id.rl_card_container)
    RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_screen_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Glide.with(this).load(R.drawable.snow_front).override((int) GeneralUtils.convertDpToPixel(300), (int) GeneralUtils.convertDpToPixel(300)).into(ivDummyFront);
        Glide.with(getActivity()).load(R.drawable.snow_back).override((int) GeneralUtils.convertDpToPixel(300), (int) GeneralUtils.convertDpToPixel(300)).into(ivDummyBack);

        this.position = getArguments().getInt(KEY_POSITION);
    }

    public static TapTutorialFragment getInstance(int position) {
        TapTutorialFragment firstScreenFragment = new TapTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        firstScreenFragment.setArguments(bundle);

        return firstScreenFragment;
    }


    @Override
    public void onPageChangeListener(int position) {

        new Handler().postDelayed(() -> {
            FlipAnimation flipAnimation = new FlipAnimation(ivDummyFront, ivDummyBack);

            if (ivDummyFront.getVisibility() == View.GONE) {
                flipAnimation.reverse();
            }

            relativeLayout.startAnimation(flipAnimation);
        }, 1000);

    }
}

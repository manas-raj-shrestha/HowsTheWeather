package com.lftechnology.leapfrog.weathernow.splashscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lftechnology.leapfrog.weathernow.GeneralUtils;
import com.droid.manasshrestha.weathernow.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * fragment with swipe animation tutorial
 */
public class SwipeTutorialFragment extends Fragment implements PageChangeListener, SwipeTutorialView {

    private static final String KEY_POSITION = "position";
    private int position;
    private static final int IMG_HEIGHT = 300;
    private static final int IMG_WIDTH = 300;

    @Bind(R.id.iv_dummy_card)
    ImageView ivDummyCard;

    @Bind(R.id.iv_dummy_card_2)
    ImageView ivDummyCard2;

    private SwipeFragmentPresenter swipeFragmentPresenter;

    public static SwipeTutorialFragment getInstance(int position) {
        SwipeTutorialFragment swipeTutorialFragment = new SwipeTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        swipeTutorialFragment.setArguments(bundle);

        return swipeTutorialFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.from(getContext()).inflate(R.layout.swipe_tutorial_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        swipeFragmentPresenter = new SwipeFragmentPresenter(this);

        Glide.with(this).load(R.drawable.snow_front).override((int) GeneralUtils.convertDpToPixel(IMG_WIDTH),
                (int) GeneralUtils.convertDpToPixel(IMG_HEIGHT)).into(ivDummyCard);
        Glide.with(getActivity()).load(R.drawable.clear_weather).override((int) GeneralUtils.convertDpToPixel(IMG_WIDTH),
                (int) GeneralUtils.convertDpToPixel(IMG_HEIGHT)).into(ivDummyCard2);

        this.position = getArguments().getInt(KEY_POSITION);

        swipeFragmentPresenter.startAnimation();
    }

    @Override
    public void onPageChangeListener(int currentPosition) {
        swipeFragmentPresenter.onPageSelected(currentPosition, position);
    }


    @Override
    public View getFrontCardView() {
        return ivDummyCard;
    }

    @Override
    public View getBackCardView() {
        return ivDummyCard2;
    }

    @Override
    public void animateFrontCard(int rotation, float translation) {
        getFrontCardView().setRotation(rotation);
        getFrontCardView().setTranslationX(translation);
    }

    @Override
    public void animateBackCardSize(RelativeLayout.LayoutParams layoutParams) {
        ivDummyCard2.setLayoutParams(layoutParams);
    }

    @Override
    public void animateBackCardAlpha(int alpha) {
        ivDummyCard2.setImageAlpha(alpha);
    }
}

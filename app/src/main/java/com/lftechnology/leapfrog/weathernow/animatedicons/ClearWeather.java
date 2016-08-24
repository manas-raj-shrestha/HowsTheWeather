package com.lftechnology.leapfrog.weathernow.animatedicons;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.droid.manasshrestha.weathernow.R;

/**
 * Animated sunny weather icon
 */
public class ClearWeather extends RelativeLayout {

    private static final int ANIM_DURATION = 20000;
    private static final float START_ANGLE = 0.0f;
    private static final float END_ANGLE = 360.0f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final int REPEAT_COUNT = -1;

    private ImageView ivClearWeather;
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivClearWeather.setImageBitmap(bitmap);
        }
    };

    public ClearWeather(Context context) {
        this(context, null, 0);
    }

    public ClearWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageView();
    }

    /**
     * creates an image view and adds it to this view group
     * animates the image view by rotate animation
     */
    private void setImageView() {

        ivClearWeather = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivClearWeather.setLayoutParams(layoutParams);
        addView(ivClearWeather);
        Glide.with(getContext()).load(R.drawable.sunny).asBitmap().into(target);

        Animation animation = new RotateAnimation(START_ANGLE, END_ANGLE,
                Animation.RELATIVE_TO_SELF, PIVOT_VALUE, Animation.RELATIVE_TO_SELF,
                PIVOT_VALUE);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(ANIM_DURATION);

        animation.setRepeatCount(REPEAT_COUNT);
        ivClearWeather.startAnimation(animation);

    }

}

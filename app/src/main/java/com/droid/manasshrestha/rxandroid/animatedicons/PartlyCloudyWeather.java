package com.droid.manasshrestha.rxandroid.animatedicons;

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
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated partly cloudy weather icon
 */
public class PartlyCloudyWeather extends RelativeLayout {

    private static final int ANIM_DURATION = 20000;
    private static final float START_ANGLE = 0.0f;
    private static final float END_ANGLE = 360.0f;
    private static final float PIVOT_VALUE = 0.5f;
    private static final float IMAGE_HEIGHT = 140;
    private static final float IMAGE_WIDTH = 140;
    private static final int REPEAT_COUNT = -1;

    private final SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            ivSun.setImageBitmap(bitmap);
        }
    };

    private ImageView ivSun;

    public PartlyCloudyWeather(Context context) {
        this(context, null, 0);
    }

    public PartlyCloudyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PartlyCloudyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageView();
    }

    /**
     * creates an image view and adds it to this view group
     * animates the image view by rotate animation
     */
    private void setImageView(){
        ivSun = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) GeneralUtils.convertDpToPixel(IMAGE_WIDTH), (int) GeneralUtils.convertDpToPixel(IMAGE_HEIGHT));
        ivSun.setLayoutParams(layoutParams);
        this.addView(ivSun);
        Glide.with(getContext()).load(R.drawable.sunny).asBitmap().into(target);

        ImageView ivClouds = new ImageView(getContext());
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivClouds.setPadding(0,(int)GeneralUtils.convertDpToPixel(30),0,0);
        ivClouds.setLayoutParams(layoutParams);
        this.addView(ivClouds);
        Glide.with(getContext()).load(R.drawable.clouds).into(ivClouds);

        Animation animation = new RotateAnimation(START_ANGLE, END_ANGLE,
                Animation.RELATIVE_TO_SELF, PIVOT_VALUE, Animation.RELATIVE_TO_SELF,
                PIVOT_VALUE);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(ANIM_DURATION);

        animation.setRepeatCount(REPEAT_COUNT);
        ivSun.startAnimation(animation);
    }

}

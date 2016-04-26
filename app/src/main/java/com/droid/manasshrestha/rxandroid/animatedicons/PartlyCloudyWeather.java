package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated foggy weather icon
 */
public class PartlyCloudyWeather extends RelativeLayout {

    private ImageView imageView;
    private static int check = 0;

    public PartlyCloudyWeather(Context context) {
        this(context, null, 0);
    }

    public PartlyCloudyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PartlyCloudyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) GeneralUtils.convertDpToPixel(140), (int) GeneralUtils.convertDpToPixel(140));
        imageView.setLayoutParams(layoutParams);
        this.addView(imageView);
        Glide.with(getContext()).load(R.drawable.sunny).into(imageView);

        ImageView imageView2 = new ImageView(getContext());
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView2.setLayoutParams(layoutParams2);
        this.addView(imageView2);
        Glide.with(getContext()).load(R.drawable.clouds).into(imageView2);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        Animation animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(20000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e("asdasdasd", "start partly cloudy " + check);
                check = check + 1;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation.setRepeatCount(-1);
        imageView.startAnimation(animation);


    }
}

package com.howstheweather.animatedicons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.howstheweather.GeneralUtils;
import com.howstheweather.R;

/**
 * Animated foggy weather icon
 */
public class CloudyWeather extends RelativeLayout {

    private static final int MSG_INVALIDATE_VIEW = 0;
    private static final int INVALIDATE_DELAY = 100;
    private static final int DP_INCREMENT = 1;

    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_WIDTH = 100;

    private RectF rectF = new RectF();
    private Paint paint = new Paint();
    private Bitmap cloudBitmap;

    private int rectStartX = 20;
    private int rectStopX = 20;
    private int rectStartY = 20;
    private int rectStopY = 20;

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            cloudBitmap = bitmap;
            setWillNotDraw(false);
        }
    };

    public CloudyWeather(Context context) {
        this(context, null, 0);
    }

    public CloudyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint.setStyle(Paint.Style.STROKE);

        CloudsAnimationThread cloudsAnimationThread = new CloudsAnimationThread();
        cloudsAnimationThread.start();

        Glide.with(getContext()).load(R.drawable.clouds).asBitmap().into(target);
    }

    Handler handler = new Handler((message) -> {
        invalidate();
        return true;
    });

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(GeneralUtils.convertDpToPixel(rectStartX),
                GeneralUtils.convertDpToPixel(rectStartY),
                getWidth() - GeneralUtils.convertDpToPixel(rectStopX),
                getHeight() - GeneralUtils.convertDpToPixel(rectStopY));

        canvas.drawBitmap(cloudBitmap, null, rectF, paint);

    }

    /**
     * Handles the animation logic for cloud animation
     */
    private class CloudsAnimationThread extends Thread {

        boolean reverseDirection = false;

        @Override
        public void run() {
            super.run();

            while (true) {
                if (!reverseDirection) {
                    rectStartX = rectStartX + DP_INCREMENT;
                    rectStopX = rectStopX - DP_INCREMENT;
                } else {
                    rectStartX = rectStartX - DP_INCREMENT;
                    rectStopX = rectStopX + DP_INCREMENT;
                }

                if (rectStartX < 0 || (getWidth() - GeneralUtils.convertDpToPixel(rectStopX)) >= getWidth()) {
                    reverseDirection = !reverseDirection;
                }

                try {
                    Thread.sleep(INVALIDATE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE_VIEW);
            }

        }
    }

}

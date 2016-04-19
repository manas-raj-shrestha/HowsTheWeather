package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated snow weather icon
 */
public class SnowyWeather extends RelativeLayout {

    private static final int POST_DELAY_TIME = 50;
    private static final int MSG_INVALIDATE_VIEW = 0;

    private static final int SNOWFLAKE_DP_INCREMENT = 1;

    private static final int LINE_STROKE_WIDTH = 4;

    private ImageView imageView;
    private Paint paint = new Paint();

    private int flakeOneStartX = 30;
    private int flakeOneStopX = 40;
    private int flakeOneStartY = 40;
    private int flakeOneStopY = 30;

    private int flakeTwoStartX = 90;
    private int flakeTwoStopX = 100;
    private int flakeTwoStartY = 60;
    private int flakeTwoStopY = 50;

    int flakeThreeStartX = 150;
    int flakeThreeStopX = 160;
    int flakeThreeStartY = 70;
    int flakeThreeStopY = 60;

    private Bitmap bitmap;
    private RectF bitmapRect = new RectF();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return false;
        }
    });

    public SnowyWeather(Context context) {
        this(context, null, 0);
    }

    public SnowyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setImageView();
    }

    /**
     * creates an image view
     * image view holds the main animation component
     */
    private void setImageView() {
        imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        this.addView(imageView);
        Glide.with(getContext()).load(R.drawable.clouds).into(imageView);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_card_front, this, false);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(LINE_STROKE_WIDTH));

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snowflake);

        AnimateSnowThread animateRainThread = new AnimateSnowThread();
        animateRainThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bitmapRect.set(imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeOneStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeOneStartY),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeOneStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeOneStopY));
        canvas.drawBitmap(bitmap, null, bitmapRect, paint);

        bitmapRect.set(imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeTwoStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeTwoStartY),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeTwoStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeTwoStopY));
        canvas.drawBitmap(bitmap, null, bitmapRect, paint);

        bitmapRect.set(imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeThreeStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeThreeStartY),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(flakeThreeStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeThreeStopY));
        canvas.drawBitmap(bitmap, null, bitmapRect, paint);

    }

    /**
     * Thread to implement logic for snow flakes
     */
    private class AnimateSnowThread extends Thread {
        private Boolean stopThread = false;

        @Override
        public void run() {
            super.run();

            while (!stopThread) {

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeOneStartY) < imageView.getBottom()) {
                    flakeOneStartY = flakeOneStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeOneStopY = flakeOneStopY - SNOWFLAKE_DP_INCREMENT;
                } else {
                    flakeOneStartY = 40;
                    flakeOneStopY = 30;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeTwoStartY) < imageView.getBottom()) {
                    flakeTwoStartY = flakeTwoStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeTwoStopY = flakeTwoStopY - SNOWFLAKE_DP_INCREMENT;
                } else {
                    flakeTwoStartY = 60;
                    flakeTwoStopY = 50;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeThreeStartY) < imageView.getBottom()) {
                    flakeThreeStartY = flakeThreeStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeThreeStopY = flakeThreeStopY - SNOWFLAKE_DP_INCREMENT;
                } else {
                    flakeThreeStartY = 60;
                    flakeThreeStopY = 50;
                }
                try {
                    Thread.sleep(POST_DELAY_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE_VIEW);
            }
        }
    }

}

package com.howstheweather.animatedicons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.howstheweather.GeneralUtils;
import com.howstheweather.R;

/**
 * Animated rainy weather icon
 */
public class RainyWeather extends RelativeLayout {

    private static final int POST_DELAY_TIME = 50;
    private static final int MSG_INVALIDATE_VIEW = 0;

    private static final int LINE_ONE_DP_INCREMENT = 2;
    private static final int LINE_TWO_DP_INCREMENT = 3;
    private static final int LINE_THREE_DP_INCREMENT = 2;

    private static final int LINE_STROKE_WIDTH = 4;

    private ImageView imageView;
    private Paint paint = new Paint();

    private int lineOneStartX = 30;
    private int lineOneStopX = 15;
    private int lineOneStartY = 40;
    private int lineOneStopY = 20;

    private int lineTwoStartX = 125;
    private int lineTwoStopX = 110;
    private int lineTwoStartY = 80;
    private int lineTwoStopY = 60;

    private int lineThreeStartX = 170;
    private int lineThreeStopX = 155;
    private int lineThreeStartY = 80;
    private int lineThreeStopY = 60;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return false;
        }
    });

    public RainyWeather(Context context) {
        this(context, null, 0);
    }

    public RainyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
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
        paint.setStrokeCap(Paint.Cap.ROUND);

        AnimateRainThread animateRainThread = new AnimateRainThread();
        animateRainThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(imageView.getX() + GeneralUtils.convertDpToPixel(lineOneStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineOneStartY),
                imageView.getX() + GeneralUtils.convertDpToPixel(lineOneStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineOneStopY), paint);

        canvas.drawLine(imageView.getX() + GeneralUtils.convertDpToPixel(lineTwoStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineTwoStartY),
                imageView.getX() + GeneralUtils.convertDpToPixel(lineTwoStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineTwoStopY), paint);

        canvas.drawLine(imageView.getX() + GeneralUtils.convertDpToPixel(lineThreeStartX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeStartY),
                imageView.getX() + GeneralUtils.convertDpToPixel(lineThreeStopX),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeStopY), paint);

    }

    /**
     * Thread to implement logic for rain drops
     */
    private class AnimateRainThread extends Thread {
        private Boolean stopThread = false;

        @Override
        public void run() {
            super.run();

            while (!stopThread) {
                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(lineOneStartY) < imageView.getBottom()) {
                    lineOneStartX = lineOneStartX - LINE_ONE_DP_INCREMENT;
                    lineOneStopX = lineOneStopX - LINE_ONE_DP_INCREMENT;
                    lineOneStartY = lineOneStartY - LINE_ONE_DP_INCREMENT;
                    lineOneStopY = lineOneStopY - LINE_ONE_DP_INCREMENT;
                } else {
                    lineOneStartX = 30;
                    lineOneStopX = 15;
                    lineOneStartY = 40;
                    lineOneStopY = 20;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(lineTwoStartY) < imageView.getBottom()) {
                    lineTwoStartX = lineTwoStartX - LINE_TWO_DP_INCREMENT;
                    lineTwoStopX = lineTwoStopX - LINE_TWO_DP_INCREMENT;
                    lineTwoStartY = lineTwoStartY - LINE_TWO_DP_INCREMENT;
                    lineTwoStopY = lineTwoStopY - LINE_TWO_DP_INCREMENT;
                } else {
                    lineTwoStartX = 125;
                    lineTwoStopX = 110;
                    lineTwoStartY = 80;
                    lineTwoStopY = 60;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeStartY) < imageView.getBottom()) {
                    lineThreeStartX = lineThreeStartX - LINE_THREE_DP_INCREMENT;
                    lineThreeStopX = lineThreeStopX - LINE_THREE_DP_INCREMENT;
                    lineThreeStartY = lineThreeStartY - LINE_THREE_DP_INCREMENT;
                    lineThreeStopY = lineThreeStopY - LINE_THREE_DP_INCREMENT;
                } else {
                    lineThreeStartX = 170;
                    lineThreeStopX = 155;
                    lineThreeStartY = 80;
                    lineThreeStopY = 60;
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

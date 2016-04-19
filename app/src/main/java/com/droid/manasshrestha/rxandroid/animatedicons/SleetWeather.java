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
public class SleetWeather extends RelativeLayout {

    private static final int POST_DELAY_TIME = 50;
    private static final int MSG_INVALIDATE_VIEW = 0;

    private static final int SNOWFLAKE_DP_INCREMENT = 1;

    private static final int LINE_ONE_DP_INCREMENT = 2;
    private static final int LINE_TWO_DP_INCREMENT = 3;
    private static final int LINE_THREE_DP_INCREMENT = 2;

    private static final int LINE_STROKE_WIDTH = 2;

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

    private int flakeThreeStartX = 150;
    private int flakeThreeStopX = 160;
    private int flakeThreeStartY = 70;
    private int flakeThreeStopY = 60;

    private int flakeOneXBoundaryLeft = 15;
    private int flakeOneXBoundaryRight = 45;
    private int flakeTwoXBoundaryLeft = 75;
    private int flakeTwoXBoundaryRight = 105;
    private int flakeThreeXBoundaryLeft = 135;
    private int flakeThreeXBoundaryRight = 165;

    private int lineOneStartX = 30;
    private int lineOneStopX = 15;
    private int lineOneStartY = 40;
    private int lineOneStopY = 20;

    private int lineTwoStartX = 135;
    private int lineTwoStopX = 120;
    private int lineTwoStartY = 80;
    private int lineTwoStopY = 60;

    private int lineThreeStartX = 200;
    private int lineThreeStopX = 185;
    private int lineThreeStartY = 80;
    private int lineThreeStopY = 60;

    private Bitmap bitmap;
    private RectF bitmapRect = new RectF();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return false;
        }
    });

    public SleetWeather(Context context) {
        this(context, null, 0);
    }

    public SleetWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleetWeather(Context context, AttributeSet attrs, int defStyleAttr) {
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
     * Thread to implement logic for snow flakes
     */
    private class AnimateSnowThread extends Thread {
        private Boolean stopThread = false;
        private Boolean reverseDirectionFLakeOne = false;
        private Boolean reverseDirectionFLakeTwo = false;
        private Boolean reverseDirectionFLakeThree = false;

        @Override
        public void run() {
            super.run();

            while (!stopThread) {

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeOneStartY) < imageView.getBottom()) {
                    flakeOneStartY = flakeOneStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeOneStopY = flakeOneStopY - SNOWFLAKE_DP_INCREMENT;

                    if (!reverseDirectionFLakeOne) {
                        if (flakeOneStartX >= flakeOneXBoundaryLeft) {
                            flakeOneStartX = flakeOneStartX - SNOWFLAKE_DP_INCREMENT;
                            flakeOneStopX = flakeOneStopX - SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeOne = true;
                        }
                    } else {
                        if (flakeOneStartX <= flakeOneXBoundaryRight) {
                            flakeOneStartX = flakeOneStartX + SNOWFLAKE_DP_INCREMENT;
                            flakeOneStopX = flakeOneStopX + SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeOne = false;
                        }
                    }
                } else {
                    flakeOneStartY = 40;
                    flakeOneStopY = 30;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeTwoStartY) < imageView.getBottom()) {
                    flakeTwoStartY = flakeTwoStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeTwoStopY = flakeTwoStopY - SNOWFLAKE_DP_INCREMENT;

                    if (!reverseDirectionFLakeTwo) {
                        if (flakeTwoStartX >= flakeTwoXBoundaryLeft) {
                            flakeTwoStartX = flakeTwoStartX - SNOWFLAKE_DP_INCREMENT;
                            flakeTwoStopX = flakeTwoStopX - SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeTwo = true;
                        }
                    } else {
                        if (flakeTwoStartX <= flakeTwoXBoundaryRight) {
                            flakeTwoStartX = flakeTwoStartX + SNOWFLAKE_DP_INCREMENT;
                            flakeTwoStopX = flakeTwoStopX + SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeTwo = false;
                        }
                    }
                } else {
                    flakeTwoStartY = 60;
                    flakeTwoStopY = 50;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(flakeThreeStartY) < imageView.getBottom()) {
                    flakeThreeStartY = flakeThreeStartY - SNOWFLAKE_DP_INCREMENT;
                    flakeThreeStopY = flakeThreeStopY - SNOWFLAKE_DP_INCREMENT;

                    if (!reverseDirectionFLakeThree) {
                        if (flakeThreeStartX >= flakeThreeXBoundaryLeft) {
                            flakeThreeStartX = flakeThreeStartX - SNOWFLAKE_DP_INCREMENT;
                            flakeThreeStopX = flakeThreeStopX - SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeThree = true;
                        }
                    } else {
                        if (flakeThreeStartX <= flakeThreeXBoundaryRight) {
                            flakeThreeStartX = flakeThreeStartX + SNOWFLAKE_DP_INCREMENT;
                            flakeThreeStopX = flakeThreeStopX + SNOWFLAKE_DP_INCREMENT;
                        } else {
                            reverseDirectionFLakeThree = false;
                        }
                    }

                } else {
                    flakeThreeStartY = 70;
                    flakeThreeStopY = 60;
                }

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
                    lineTwoStartX = 135;
                    lineTwoStopX = 120;
                    lineTwoStartY = 80;
                    lineTwoStopY = 60;
                }

                if (imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeStartY) < imageView.getBottom()) {
                    lineThreeStartX = lineThreeStartX - LINE_THREE_DP_INCREMENT;
                    lineThreeStopX = lineThreeStopX - LINE_THREE_DP_INCREMENT;
                    lineThreeStartY = lineThreeStartY - LINE_THREE_DP_INCREMENT;
                    lineThreeStopY = lineThreeStopY - LINE_THREE_DP_INCREMENT;
                } else {
                    lineThreeStartX = 200;
                    lineThreeStopX = 185;
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

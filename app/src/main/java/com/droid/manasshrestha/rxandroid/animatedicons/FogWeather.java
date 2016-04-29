package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated foggy weather view
 */
public class FogWeather extends RelativeLayout {

    private static final int POST_INVALIDATE_DELAY = 100;
    private static final int MSG_INVALIDATE_VIEW = 0;
    private static final int DELTA = 1;
    private static final int LINE_MARGIN = 10;
    private static final int STROKE_WIDTH = 2;

    private ImageView imageView;
    private Paint paint;

    private int lineOneX1 = 10;
    private int lineOneX2 = 115;
    private int lineOneY1 = 25;
    private int lineOneY2 = 25;

    private int lineTwoX1 = 20;
    private int lineTwoX2 = 125;
    private int lineTwoY1 = 18;
    private int lineTwoY2 = 18;

    private int lineThreeX1 = 10;
    private int lineThreeX2 = 115;
    private int lineThreeY1 = 11;
    private int lineThreeY2 = 11;

    public FogWeather(Context context) {
        this(context, null, 0);
    }

    public FogWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FogWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageView();
        setWillNotDraw(false);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return true;
        }
    });

    /**
     * creates an image view and add it to the view group
     * initializes paint objects for drawing
     */
    private void setImageView() {

        imageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        this.addView(imageView);
        Glide.with(getContext()).load(R.drawable.clouds).into(imageView);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(STROKE_WIDTH));
        paint.setStrokeCap(Paint.Cap.ROUND);

        FogAnimationThread fogAnimationThread = new FogAnimationThread();
        fogAnimationThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX1),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineOneY1),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX2),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineOneY2), paint);

        canvas.drawLine(imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX1),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineTwoY1),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX2),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineTwoY2), paint);

        canvas.drawLine(imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX1),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeY1),
                imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX2),
                imageView.getBottom() - GeneralUtils.convertDpToPixel(lineThreeY2), paint);
    }

    /**
     * Animation logic for fog lines
     */
    private class FogAnimationThread extends Thread {

        private boolean stopThread = false;

        private boolean reverseLineOne = false;
        private boolean reverseLineTwo = true;
        private boolean reverseLineThree = false;

        @Override
        public void run() {
            super.run();
            while (!stopThread) {
                if (!reverseLineOne) {
                    lineOneX1 = lineOneX1 + DELTA;
                    lineOneX2 = lineOneX2 + DELTA;
                } else {
                    lineOneX1 = lineOneX1 - DELTA;
                    lineOneX2 = lineOneX2 - DELTA;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX1) <= GeneralUtils.convertDpToPixel(LINE_MARGIN)) {
                    reverseLineOne = !reverseLineOne;
                }

                if (!reverseLineTwo) {
                    lineTwoX1 = lineTwoX1 + DELTA;
                    lineTwoX2 = lineTwoX2 + DELTA;
                } else {
                    lineTwoX1 = lineTwoX1 - DELTA;
                    lineTwoX2 = lineTwoX2 - DELTA;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX1) <= GeneralUtils.convertDpToPixel(LINE_MARGIN)) {
                    reverseLineTwo = !reverseLineTwo;
                }

                if (!reverseLineThree) {
                    lineThreeX1 = lineThreeX1 + DELTA;
                    lineThreeX2 = lineThreeX2 + DELTA;
                } else {
                    lineThreeX1 = lineThreeX1 - DELTA;
                    lineThreeX2 = lineThreeX2 - DELTA;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX1) <= GeneralUtils.convertDpToPixel(LINE_MARGIN)) {
                    reverseLineThree = !reverseLineThree;
                }

                try {
                    Thread.sleep(POST_INVALIDATE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE_VIEW);
            }
        }
    }

}

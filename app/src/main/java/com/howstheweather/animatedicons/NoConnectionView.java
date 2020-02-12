package com.howstheweather.animatedicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.howstheweather.GeneralUtils;
import com.howstheweather.R;

/**
 * Animated no connection icon
 */
public class NoConnectionView extends View {

    private final static int MSG_INVALIDATE = 0;
    private final static int POST_DELAY_INTERVAL = 10;
    private final static int STROKE_WIDTH = 3;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;
    private static final int CROSS_SIZE = 25;
    private static final int PADDING = 10;
    private static final int LENGTH_INCREMENT = 1;

    private RectF rectBitmap = new RectF();
    private RectF rectCross = new RectF();
    private Bitmap networkBitmap;
    private Paint paint = new Paint();
    private int layout_width;
    private int layout_height;

    private float lineOneEndX;
    private float lineOneEndY;
    private double angle = 45 * Math.PI / 180;
    private int lengthX = 1;

    private float lineTwoEndX;
    private float lineTwoEndY;
    private double angleLineTwo = -(45 * Math.PI / 180);
    private int lengthY = 1;
    private boolean animateLineTwo = false;

    Handler handler = new Handler((message -> {
        invalidate();
        return true;
    }));

    public NoConnectionView(Context context) {
        this(context, null, 0);
    }

    public NoConnectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoConnectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        layout_width = ta.getDimensionPixelSize(2, (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH));
        layout_height = ta.getDimensionPixelSize(3, (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        setPadding((int) GeneralUtils.convertDpToPixel(PADDING),
                (int) GeneralUtils.convertDpToPixel(PADDING),
                (int) GeneralUtils.convertDpToPixel(PADDING),
                (int) GeneralUtils.convertDpToPixel(PADDING));

        networkBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.wifi,
                (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH), (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        rectBitmap.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(),
                layout_height - getPaddingBottom());

        rectCross.set(rectBitmap.right - GeneralUtils.convertDpToPixel(CROSS_SIZE),
                rectBitmap.bottom - GeneralUtils.convertDpToPixel(CROSS_SIZE),
                rectBitmap.right, rectBitmap.bottom);

        paint.setColor(ContextCompat.getColor(context, R.color.colorRed));
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(STROKE_WIDTH));
        paint.setStrokeCap(Paint.Cap.ROUND);

        lineTwoEndX = rectCross.right + LENGTH_INCREMENT;
        lineTwoEndY = rectCross.top + LENGTH_INCREMENT;

        new CrossAnimationThread().start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(networkBitmap, null, rectBitmap, paint);
        canvas.drawLine(rectCross.left, rectCross.top, lineOneEndX, lineOneEndY, paint);

        if (animateLineTwo) {
            canvas.drawLine(rectCross.right, rectCross.top, lineTwoEndX, lineTwoEndY, paint);
        }

    }

    /**
     * Logic for cross animation
     * First draws the first diagonal cross then the second
     */
    private class CrossAnimationThread extends Thread {

        private boolean animate = true;

        @Override
        public void run() {
            super.run();
            while (animate) {
                if (!animateLineTwo) {
                    lineOneEndX = (float) (rectCross.left + GeneralUtils.convertDpToPixel(lengthX) * Math.sin(angle));
                    lineOneEndY = (float) (rectCross.top + GeneralUtils.convertDpToPixel(lengthX) * Math.cos(angle));
                    lengthX = lengthX + LENGTH_INCREMENT;
                } else {
                    lineTwoEndX = (float) (rectCross.right + GeneralUtils.convertDpToPixel(lengthY) * Math.sin(angleLineTwo));
                    lineTwoEndY = (float) (rectCross.top + GeneralUtils.convertDpToPixel(lengthY) * Math.cos(angleLineTwo));
                    lengthY = lengthY + LENGTH_INCREMENT;
                }

                if (lineOneEndX >= rectCross.right) {
                    animateLineTwo = true;
                }

                //stop once both lines are drawn
                if (lineTwoEndX < rectCross.left) {
                    animate = false;
                }

                try {
                    Thread.sleep(POST_DELAY_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE);
            }
        }
    }

}

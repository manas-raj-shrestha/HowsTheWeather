package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated loading icon
 */
public class AnimatedNoConnection extends View {

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    private RectF rectF = new RectF();
    private RectF rectCross = new RectF();
    private Bitmap cloudBitmap;
    private Paint paint = new Paint();
    private int layout_width;
    private int layout_height;

    float lineOneEndX;
    float lineOneEndY;
    double angle = 45 * Math.PI / 180;
    int lengthX = 0;

    float lineTwoEndX;
    float lineTwoEndY;
    double angleLineTwo = -(45 * Math.PI / 180);
    int lengthY = 1;

    Handler handler = new Handler((message -> {
        invalidate();
        return true;
    }));

    public AnimatedNoConnection(Context context) {
        this(context, null, 0);
    }

    public AnimatedNoConnection(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedNoConnection(Context context, AttributeSet attrs, int defStyleAttr) {
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

        setPadding((int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10));

        cloudBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.wifi,
                (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH), (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        rectF.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(), layout_height - getPaddingBottom());
        rectCross.set(rectF.right - GeneralUtils.convertDpToPixel(25)
                , rectF.bottom - GeneralUtils.convertDpToPixel(25)
                , rectF.right, rectF.bottom);

        paint.setColor(Color.parseColor("#F44336"));
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);

        lineTwoEndX = rectCross.right + 1;
        lineTwoEndY = rectCross.top + 1;

        new CrossAnimationThread().start();
    }

    boolean animateLineTwo = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(cloudBitmap, null, rectF, paint);
        canvas.drawLine(rectCross.left, rectCross.top, lineOneEndX, lineOneEndY, paint);

        if (animateLineTwo) {
            canvas.drawLine(rectCross.right, rectCross.top, lineTwoEndX, lineTwoEndY, paint);
        }

    }

    /**
     * Logic for cross animation
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
                    lengthX = lengthX + 1;
                } else {
                    lineTwoEndX = (float) (rectCross.right + GeneralUtils.convertDpToPixel(lengthY) * Math.sin(angleLineTwo));
                    lineTwoEndY = (float) (rectCross.top + GeneralUtils.convertDpToPixel(lengthY) * Math.cos(angleLineTwo));
                    lengthY = lengthY + 1;
                }

                if (lineOneEndX >= rectCross.right) {
                    animateLineTwo = true;
                }

                //stop once both lines are drawn
                if (lineTwoEndX < rectCross.left) {
                    animate = false;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(0);
            }
        }
    }

}

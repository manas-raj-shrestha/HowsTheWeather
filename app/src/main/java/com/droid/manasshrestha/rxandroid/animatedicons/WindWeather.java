package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated wind weather icon
 */
public class WindWeather extends RelativeLayout {

    private static final int POST_DELAY_TIME = 40;
    private static final int MSG_INVALIDATE_VIEW = 0;
    private static final int ANGLE_INCREMENT = 10;
    private static final int FAKE_Y_ORIGIN = 150;

    private static final int LINE_STROKE_WIDTH = 2;

    private Paint paint = new Paint();
    private RectF rectF = new RectF();
    private Bitmap mapleBitmap;
    private Bitmap rotatedBitmap;
    private int rotation = 0;
    private Bitmap scaledBitmap;
    private Path path;

    private float plottingX;
    private float plottingX2;
    private float plottingX3;
    private float x1 = -180;
    private float x2 = -90;
    private float x3 = 0;

    private float y1 = GeneralUtils.convertDpToPixel(150);
    private float y2 = 400;
    private float y3 = 400;

    //these are the controlled points for cubic curve
    private float cx1a;
    private float cy1a;
    private float cx1b;
    private float cy1b;
    private float cx2a;
    private float cy2a;
    private float cx2b;
    private float cy2b;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return true;
        }
    });

    public WindWeather(Context context) {
        this(context, null, 0);
    }

    public WindWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        setImageView();
    }

    /**
     * creates an image view
     * image view holds the main animation component
     */
    private void setImageView() {

        RelativeLayout rootLayout = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rootLayout.setLayoutParams(layoutParams);
        addView(rootLayout);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(LINE_STROKE_WIDTH));
        paint.setAntiAlias(true);

        mapleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maple_leaf);
        scaledBitmap = Bitmap.createScaledBitmap(mapleBitmap, mapleBitmap.getWidth(), mapleBitmap.getHeight(), true);

        WindAnimationThread windAnimationThread = new WindAnimationThread();
        windAnimationThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(rotatedBitmap, null, rectF, paint);
        canvas.drawPath(path, paint);

    }

    /**
     * create curve path
     *
     * @return {@link Path}
     */
    private Path drawCurve() {

        x3 = x3 + ANGLE_INCREMENT;
        x1 = x1 + ANGLE_INCREMENT;
        x2 = x2 + ANGLE_INCREMENT;

        //sin wave equation y = A*sin(x) A is amplitude and x must be in radians
        y1 = (((float) Math.sin(Math.toRadians(x1))) * 100) / 4 + GeneralUtils.convertDpToPixel(FAKE_Y_ORIGIN);
        y2 = (((float) Math.sin(Math.toRadians(x2))) * 100) / 4 + GeneralUtils.convertDpToPixel(FAKE_Y_ORIGIN);
        y3 = (((float) Math.sin(Math.toRadians(x3))) * 100) / 4 + GeneralUtils.convertDpToPixel(FAKE_Y_ORIGIN);

        plottingX = x1 * 1f;
        plottingX2 = x2 * 1f;
        plottingX3 = x3 * 1f;

        cx1a = plottingX + (plottingX2 - plottingX) / 3;
        cy1a = y1 + (y2 - y1) / 3;
        cx1b = plottingX2 - (plottingX3 - plottingX) / 3;
        cy1b = y2 - (y3 - y1) / 3;
        cx2a = plottingX2 + (plottingX3 - plottingX) / 3;
        cy2a = y2 + (y3 - y1) / 3;
        cx2b = plottingX3 - (plottingX3 - plottingX2) / 3;
        cy2b = y3 - (y3 - y2) / 3;

        Path myPath = new Path();
        myPath.moveTo(plottingX, y1);
        myPath.cubicTo(cx1a, cy1a, cx1b, cy1b, plottingX2, y2);
        myPath.cubicTo(cx2a, cy2a, cx2b, cy2b, plottingX3, y3);

        return myPath;

    }

    private class WindAnimationThread extends Thread {
        private boolean stopThread = false;

        @Override
        public void run() {
            super.run();
            while (!stopThread) {

                if (x1 <= getWidth()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotation);
                    rotation = rotation - 2;

                    rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                    rectF.set(plottingX3 + GeneralUtils.convertDpToPixel(10), y3 - GeneralUtils.convertDpToPixel(15), plottingX3 + GeneralUtils.convertDpToPixel(30), y3 + GeneralUtils.convertDpToPixel(5));

                    path = drawCurve();
                } else {
                    x1 = -180;
                    x2 = -90;
                    x3 = 0;

                    y1 = GeneralUtils.convertDpToPixel(150);
                    y2 = 400;
                    y3 = 400;
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

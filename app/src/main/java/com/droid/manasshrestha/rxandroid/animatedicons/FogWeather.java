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
 * Animated foggy weather icon
 */
public class FogWeather extends RelativeLayout {
    private ImageView imageView;
    private Paint paint;

    private int lineOneX1 = 10;
    private int lineOneX2 = 165;
    private int lineOneY1 = 30;
    private int lineOneY2 = 30;

    private int lineTwoX1 = 20;
    private int lineTwoX2 = 175;
    private int lineTwoY1 = 20;
    private int lineTwoY2 = 20;

    private int lineThreeX1 = 10;
    private int lineThreeX2 = 165;
    private int lineThreeY1 = 10;
    private int lineThreeY2 = 10;

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
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(3));
        paint.setStrokeCap(Paint.Cap.BUTT);

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

        boolean reverseLineOne = false;
        boolean reverseLineTwo = true;
        boolean reverseLineThree = false;

        @Override
        public void run() {
            super.run();
            while (true) {
                if (!reverseLineOne) {
                    lineOneX1 = lineOneX1 + 1;
                    lineOneX2 = lineOneX2 + 1;
                } else {
                    lineOneX1 = lineOneX1 - 1;
                    lineOneX2 = lineOneX2 - 1;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineOneX1) <= GeneralUtils.convertDpToPixel(10)) {
                    reverseLineOne = !reverseLineOne;
                }

                if (!reverseLineTwo) {
                    lineTwoX1 = lineTwoX1 + 1;
                    lineTwoX2 = lineTwoX2 + 1;
                } else {
                    lineTwoX1 = lineTwoX1 - 1;
                    lineTwoX2 = lineTwoX2 - 1;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineTwoX1) <= GeneralUtils.convertDpToPixel(10)) {
                    reverseLineTwo = !reverseLineTwo;
                }

                if (!reverseLineThree) {
                    lineThreeX1 = lineThreeX1 + 1;
                    lineThreeX2 = lineThreeX2 + 1;
                } else {
                    lineThreeX1 = lineThreeX1 - 1;
                    lineThreeX2 = lineThreeX2 - 1;
                }

                if ((imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX2) >= getWidth()) ||
                        imageView.getLeft() + GeneralUtils.convertDpToPixel(lineThreeX1) <= GeneralUtils.convertDpToPixel(10)) {
                    reverseLineThree = !reverseLineThree;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(0);
            }
        }
    }
}

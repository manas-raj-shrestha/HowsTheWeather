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
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated rainy weather icon
 */
public class WindWeather extends RelativeLayout {

    private static final int POST_DELAY_TIME = 50;
    private static final int MSG_INVALIDATE_VIEW = 0;

    private static final int LINE_ONE_DP_INCREMENT = 2;
    private static final int LINE_TWO_DP_INCREMENT = 3;
    private static final int LINE_THREE_DP_INCREMENT = 2;

    private static final int LINE_STROKE_WIDTH = 2;

    private ImageView imageView;
    private Paint paint = new Paint();

    private RectF rectF = new RectF();
    private int startX = -(int) GeneralUtils.convertDpToPixel(300);
    private int stopX = 0;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return false;
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
        this.addView(rootLayout);


        imageView = new ImageView(getContext());
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        rootLayout.addView(imageView);
        imageView.setScaleX(-1);
        Glide.with(getContext()).load(R.drawable.wind_rooster).into(imageView);
        imageView.setPadding((int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10),
                (int) GeneralUtils.convertDpToPixel(10));

        imageView.setVisibility(INVISIBLE);

//        ImageView imageView2 = new ImageView(getContext());
//        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        imageView2.setLayoutParams(layoutParams2);
//        rootLayout.addView(imageView2);
//        Glide.with(getContext()).load(R.drawable.wind_rooster).into(imageView2);
//        imageView2.setVisibility(GONE);
//        imageView2.setPadding((int) GeneralUtils.convertDpToPixel(10),
//                (int) GeneralUtils.convertDpToPixel(10),
//                (int) GeneralUtils.convertDpToPixel(10),
//                (int) GeneralUtils.convertDpToPixel(10));
//
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(GeneralUtils.convertDpToPixel(LINE_STROKE_WIDTH));
//
//        FlipAnimation flipAnimation = new FlipAnimation(imageView, imageView2);
//        flipAnimation.setDuration(2000);
//        if (imageView.getVisibility() == View.GONE) {
//            flipAnimation.reverse();
//        }
//        rootLayout.startAnimation(flipAnimation);
        mapleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maple_leaf);
        scaledBitmap = Bitmap.createScaledBitmap(mapleBitmap, mapleBitmap.getWidth(), mapleBitmap.getHeight(), true);
    }

    Bitmap mapleBitmap;
    //    RectF rectF = new RectF();
    Bitmap rotatedBitmap;
    int rotation = 0;
    Bitmap scaledBitmap;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (x1 <= getWidth()) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotation);
            rotation= rotation+3;

            rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            rectF.set(plottingX3 + GeneralUtils.convertDpToPixel(10), y3 - GeneralUtils.convertDpToPixel(15), plottingX3 + GeneralUtils.convertDpToPixel(30), y3 + GeneralUtils.convertDpToPixel(5));
//            canvas.drawRect(rectF, paint);
            canvas.drawBitmap(rotatedBitmap, null, rectF, paint);


            Path myPath1 = new Path();
            paint.setAntiAlias(true);

            myPath1 = drawCurve();
            canvas.drawPath(myPath1, paint);

//            canvas.drawCircle(x1, y1, 30, paint);
//            canvas.drawCircle(x2, y2, 30, paint);
//            canvas.drawCircle(x3, y3, 30, paint);
        } else {
            x1 = -180;
            x2 = -90;
            x3 = 0;

            y1 = GeneralUtils.convertDpToPixel(150);
            y2 = 400;
            y3 = 400;
        }
        postInvalidateDelayed(60);

    }

    float plottingX;
    float plottingX2;
    float plottingX3;
    float x1 = -180;
    float x2 = -90;
    float x3 = 0;

    float y1 = GeneralUtils.convertDpToPixel(150);
    float y2 = 400;
    float y3 = 400;

    private Path drawCurve() {

        x3 = x3 + 10;
        x1 = x1 + 10;
        x2 = x2 + 10;

        //sin wave equation y = A*sin(x) A is amplitude and x must be in radians
        y1 = (((float) Math.sin(Math.toRadians(x1))) * 100) / 3 + GeneralUtils.convertDpToPixel(150);
        y2 = (((float) Math.sin(Math.toRadians(x2))) * 100) / 3 + GeneralUtils.convertDpToPixel(150);
        y3 = (((float) Math.sin(Math.toRadians(x3))) * 100) / 3 + GeneralUtils.convertDpToPixel(150);

        Log.e("value", String.valueOf(((float) Math.sin(Math.toRadians(180)))) + " dsa");

        plottingX = x1 * 1f;
        plottingX2 = x2 * 1f;
        plottingX3 = x3 * 1f;

        float cx1a = plottingX + (plottingX2 - plottingX) / 3;
        float cy1a = y1 + (y2 - y1) / 3;
        float cx1b = plottingX2 - (plottingX3 - plottingX) / 3;
        float cy1b = y2 - (y3 - y1) / 3;
        float cx2a = plottingX2 + (plottingX3 - plottingX) / 3;
        float cy2a = y2 + (y3 - y1) / 3;
        float cx2b = plottingX3 - (plottingX3 - plottingX2) / 3;
        float cy2b = y3 - (y3 - y2) / 3;

        Path myPath = new Path();
        myPath.moveTo(plottingX, y1);
        myPath.cubicTo(cx1a, cy1a, cx1b, cy1b, plottingX2, y2);
        myPath.cubicTo(cx2a, cy2a, cx2b, cy2b, plottingX3, y3);


        return myPath;

    }


}

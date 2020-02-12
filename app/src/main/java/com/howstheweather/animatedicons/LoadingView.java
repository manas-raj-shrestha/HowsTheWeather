package com.howstheweather.animatedicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.howstheweather.GeneralUtils;
import com.howstheweather.R;

/**
 * Animated loading icon
 */
public class LoadingView extends View {

    private static final float ANGLE_INCREMENT = 2f;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;
    private static final int MAX_ANGLE = 380;
    private static final int MIN_ANGLE = 0;
    private static final int DELAY_TIME = 10;

    private RectF rectF = new RectF();
    private Bitmap cloudBitmap;
    private Paint paint = new Paint();
    private int layout_width;
    private int layout_height;
    private Path path = new Path();

    private float sweepAngle = 0;
    private boolean reverse = false;

    public LoadingView(Context context) {
        this(context, null, 0);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("ResourceType")
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };

        //get defined dimens. if not available assign default values
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        layout_width = ta.getDimensionPixelSize(2, (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH));
        layout_height = ta.getDimensionPixelSize(3, (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        cloudBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.cloud_loading,
                (int) GeneralUtils.convertDpToPixel(DEFAULT_WIDTH), (int) GeneralUtils.convertDpToPixel(DEFAULT_HEIGHT));

        rectF.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(), layout_height - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        path.moveTo(layout_width / 2, layout_height / 2);
        path.addArc(rectF, 0, sweepAngle);
        path.lineTo(rectF.centerX(), rectF.centerY());
        path.close();

        canvas.clipPath(path);
        canvas.drawBitmap(cloudBitmap, null, rectF, paint);
        if (!reverse) {
            sweepAngle = sweepAngle + ANGLE_INCREMENT;
        } else {
            sweepAngle = sweepAngle - ANGLE_INCREMENT;
        }

        if (sweepAngle >= MAX_ANGLE || sweepAngle <= MIN_ANGLE) {
            reverse = !reverse;
        }

        postInvalidateDelayed(DELAY_TIME);

    }
}

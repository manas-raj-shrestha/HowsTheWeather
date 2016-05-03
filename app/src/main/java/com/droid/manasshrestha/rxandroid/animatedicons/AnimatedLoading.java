package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Created by Manas on 5/3/2016.
 */
public class AnimatedLoading extends View {

    RectF rectF = new RectF();
    Bitmap cloudBitmap;
    Paint paint = new Paint();
    int layout_width;
    int layout_height;
    Path path = new Path();

    public AnimatedLoading(Context context) {
        this(context, null, 0);
    }

    public AnimatedLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int[] attrsArray = new int[]{
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };

        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        layout_width = ta.getDimensionPixelSize(2, (int) GeneralUtils.convertDpToPixel(200));
        layout_height = ta.getDimensionPixelSize(3, (int) GeneralUtils.convertDpToPixel(200));

        cloudBitmap = GeneralUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.cloud_loading, 200, 200);

//        rectF.set(0, 0, layout_width, layout_height);
        rectF.set(getPaddingLeft(), getPaddingTop(), layout_width - getPaddingRight(), layout_height - getPaddingBottom());
    }

    float sweepAngle = 0;
    boolean reverse = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(layout_width / 2, layout_height / 2);
        path.addArc(rectF, 0, sweepAngle);
        path.lineTo(rectF.centerX(), rectF.centerY());
        path.close();

        canvas.clipPath(path);
//        canvas.drawBitmap(cloudBitmap, null, rectF, paint);
        canvas.drawBitmap(cloudBitmap, null, rectF, paint);
        if (!reverse) {
            Log.e("not reverse", "not reverse");
            sweepAngle = sweepAngle + 2f;
//            canvas.drawPath(path,paint);
        } else {
            Log.e("reverse", "reverse");
            sweepAngle = sweepAngle - 2f;

        }

        if (sweepAngle >= 360) {
//            sweepAngle = 0f;
            reverse = !reverse;
            path.reset();
        } else if (sweepAngle <= 0) {
            reverse = !reverse;
            path.reset();
        }

        postInvalidateDelayed(10);

    }
}

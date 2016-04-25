package com.droid.manasshrestha.rxandroid.animatedicons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.droid.manasshrestha.rxandroid.GeneralUtils;
import com.droid.manasshrestha.rxandroid.R;

/**
 * Animated foggy weather icon
 */
public class CloudyWeather extends RelativeLayout {

    private static final int MSG_INVALIDATE_VIEW = 0;
    private static final int INVALIDATE_DELAY = 100;
    private static final int DP_INCREAMENT = 1;

    private static final int IMAGE_HEIGHT = 100;
    private static final int IMAGE_WIDTH = 100;

    private RectF rectF = new RectF();
    private Paint paint = new Paint();
    private Bitmap cloudBitmap;

    private int rectStartX = 20;
    private int rectStopX = 20;
    private int rectStartY = 20;
    private int rectStopY = 20;

    public CloudyWeather(Context context) {
        this(context, null, 0);
    }

    public CloudyWeather(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloudyWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        paint.setStyle(Paint.Style.STROKE);
        cloudBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.clouds, IMAGE_WIDTH, IMAGE_HEIGHT);

        CloudsAnimationThread cloudsAnimationThread = new CloudsAnimationThread();
        cloudsAnimationThread.start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            invalidate();
            return true;
        }
    });

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.set(GeneralUtils.convertDpToPixel(rectStartX),
                GeneralUtils.convertDpToPixel(rectStartY),
                getWidth() - GeneralUtils.convertDpToPixel(rectStopX),
                getHeight() - GeneralUtils.convertDpToPixel(rectStopY));

        canvas.drawBitmap(cloudBitmap, null, rectF, paint);

    }

    /**
     * Handles the animation logic for cloud animation
     */
    private class CloudsAnimationThread extends Thread {

        boolean reverseDirection = false;

        @Override
        public void run() {
            super.run();

            while (true) {
                if (!reverseDirection) {
                    rectStartX = rectStartX + DP_INCREAMENT;
                    rectStopX = rectStopX - DP_INCREAMENT;
                } else {
                    rectStartX = rectStartX - DP_INCREAMENT;
                    rectStopX = rectStopX + DP_INCREAMENT;
                }

                if (rectStartX < 0 || (getWidth() - GeneralUtils.convertDpToPixel(rectStopX)) >= getWidth()) {
                    reverseDirection = !reverseDirection;
                }

                try {
                    Thread.sleep(INVALIDATE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.sendEmptyMessage(MSG_INVALIDATE_VIEW);
            }

        }
    }

    /**
     * Load bitmap effectively using bitmap options
     *
     * @param res
     * @param resId     drawable id of target
     * @param reqWidth  required bitmap width
     * @param reqHeight required bitmap height
     * @return sampled bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Tells the decoder to subsample the image, loading a smaller version into memory.
     * <p/>
     * an image with resolution 2048x1536 that is decoded with an inSampleSize of 4 produces a bitmap of approximately 512x384.
     * Loading this into memory uses 0.75MB rather than 12MB for the full image
     *
     * @param options   {@link android.graphics.BitmapFactory.Options}
     * @param reqWidth  required bitmap width
     * @param reqHeight required bitmap height
     * @return sample size
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}

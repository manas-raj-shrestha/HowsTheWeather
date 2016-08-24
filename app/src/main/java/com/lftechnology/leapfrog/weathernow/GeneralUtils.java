package com.lftechnology.leapfrog.weathernow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import com.droid.manasshrestha.weathernow.R;
import com.lftechnology.leapfrog.weathernow.data.Constants;

import java.util.Date;

/**
 * general functionality used throughout application
 */
public class GeneralUtils {

    private static final long MS_CONSTANT = 1000;

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp) {
        Resources resources = WeatherApplication.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px) {
        Resources resources = WeatherApplication.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
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
     * <p>
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

    /**
     * Check if device is connected to internet
     *
     * @param context {@link Context}
     * @return <li>true - if online</li>
     * <li>false - if not online</li>
     */
    public static boolean isNetworkOnline(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }

    /**
     * parse date according to date format provided
     *
     * @param dateFormat date format
     * @param timeStamp  timestamp to be converted
     * @return formated date
     */
    public static String parseDate(String dateFormat, long timeStamp) {
        String longV = String.valueOf(timeStamp * MS_CONSTANT);
        long millisecond = Long.parseLong(longV);
        String dayString = DateFormat.format(dateFormat, new Date(millisecond)).toString();
        return dayString;
    }

    /**
     * provides the widget icon according to the weather
     *
     * @param iconName weather icon key
     * @return resource id of respective icon
     */
    public static int getWidgetIcons(String iconName) {
        int drawableId = R.drawable.clear_weather;

        switch (iconName) {
            case Constants.KEY_CLEAR_DAY:
            case Constants.KEY_CLEAR_NIGHT:
                drawableId = R.drawable.widget_sun;
                break;
            case Constants.KEY_RAIN:
                drawableId = R.drawable.widget_rain;
                break;
            case Constants.KEY_SNOW:
                drawableId = R.drawable.widget_snow;
                break;
            case Constants.KEY_SLEET:
                drawableId = R.drawable.widget_sleet;
                break;
            case Constants.KEY_WIND:
                drawableId = R.drawable.widget_wind;
                break;
            case Constants.KEY_FOG:
                drawableId = R.drawable.widget_fog;
                break;
            case Constants.KEY_CLOUDY:
                drawableId = R.drawable.widget_clouds;
                break;
            case Constants.KEY_PARTLY_CLOUDY_DAY:
            case Constants.KEY_PARTLY_CLOUDY_NIGHT:
                drawableId = R.drawable.widget_sun_cloud;
                break;
        }

        return drawableId;
    }

    public static String getNotificationTicker(String iconName) {
        String tickerText = "";
        Context context = WeatherApplication.getContext();
        switch (iconName) {
            case Constants.KEY_CLEAR_DAY:
            case Constants.KEY_CLEAR_NIGHT:
                tickerText = context.getString(R.string.txt_notification_sunny);
                break;
            case Constants.KEY_RAIN:
                tickerText = context.getString(R.string.txt_notification_rain);
                break;
            case Constants.KEY_SNOW:
                tickerText = context.getString(R.string.txt_notification_snow);
                break;
            case Constants.KEY_SLEET:
                tickerText = context.getString(R.string.txt_notification_sleet);
                break;
            case Constants.KEY_WIND:
                tickerText = context.getString(R.string.txt_notification_wind);
                break;
            case Constants.KEY_FOG:
                tickerText = context.getString(R.string.txt_notification_fog);
                break;
            case Constants.KEY_CLOUDY:
                tickerText = context.getString(R.string.txt_notification_cloudy);
                break;
            case Constants.KEY_PARTLY_CLOUDY_DAY:
            case Constants.KEY_PARTLY_CLOUDY_NIGHT:
                tickerText = context.getString(R.string.txt_notification_partly_cloudy);
                break;
        }

        return tickerText;
    }

}

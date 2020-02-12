package com.howstheweather.locationhandlers;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Location Fetch.
 * <p/>
 * Use of the Location API to retrieve the last known location for device.
 * Uses Google Play services (GoogleApiClient) to fetch the location.
 */
public class LocationCatcher implements LocationInterface, LocationListener, com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = LocationCatcher.class.getSimpleName();
    private LocationCallBack locationCallBack;
    private Context context;
    private boolean locationCallbackEnabled = true;

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient locationClient;

    /*
     * CustomerConstants for location update parameters
     */
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    private static final int FAST_CEILING_IN_SECONDS = 5;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    private long locationUpdateTimeInterval;

    public void cancelLocationCallback() {
        locationCallbackEnabled = false;
    }

    public interface LocationCallBack {

        void onLocationFound(GpsInfo location);

        void onLocationNotFound();
    }

    public LocationCatcher(Context context) {
        this.context = context;

        createLocationRequest();
    }

    public void setLocationUpdateTimeInterval(long updateTimeInterval) {
        this.locationUpdateTimeInterval = updateTimeInterval;
    }

    private long getUpdateIntervalInMilliseconds() {
        return locationUpdateTimeInterval;
    }

    /**
     * Builds a GoogleApiClient. Uses the {@link LocationCatcher#createLocationRequest()} method to request the LocationServices API.
     */
    public void getLocation(LocationCallBack locationCallback) {
        locationClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        locationClient.connect();
        this.locationCallBack = locationCallback;
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            /**only for Lollipop and older versions
             * as permission check does not work for them
             */
            showSettingsAlert();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle arg0) {

        if (locationClient.isConnected()) {

            Location mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(locationClient);

            if (mLastLocation == null) {
                if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    /**only for Lollipop and older versions
                     * as permission check does not work for them
                     */
//                    showSettingsAlert();
                }
                Log.e("null", "null mLastLocation Null");
                locationCallBack.onLocationNotFound();
            } else {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                Log.e(TAG, String.format(
                        "Location from play service, Latitude: %s Longitude: %s  ",
                        latitude, longitude));
                locationCallBack.onLocationFound(new GpsInfo(latitude, longitude));
                startLocationUpdates();
            }
        } else {
            Log.e("null", "null location client not connected");
            locationCallBack.onLocationNotFound();
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                /**only for Lollipop and older versions
                 * as permission check does not work for them
                 */
                showSettingsAlert();
            }
        }

    }

    /**
     * Show {@link AlertDialog} if {@link android.os.Build.VERSION} is less than Marshmallow
     */
    public void showSettingsAlert() {
        Log.e("show alert", "show alert");
        if (!((Activity) context).isFinishing()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("Enable GPS");

            // Setting Dialog Message
            alertDialog.setMessage("Please enable gps");

            // Setting Icon to Dialog
            // alertDialog.setIcon(R.drawable.delete);

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    (dialog, which) -> {
                        dialog.cancel();
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);

                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    (dialog, which) -> {
                        System.exit(0);
                        dialog.cancel();
                    });

            // Showing Alert Message
            alertDialog.show();
        }else {
            Log.e("activity","finishing");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Log.e("location", "location changed");

        if (locationCallbackEnabled) {
            locationCallBack.onLocationFound(new GpsInfo(location.getLatitude(), location.getLongitude()));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * Start {@link Location} update
     */
    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                locationClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        if (locationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, this);
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval {@link LocationCatcher#UPDATE_INTERVAL_IN_MILLISECONDS}, the Fused Location Provider API returns location updates that are
     * accurate to within few meters
     * <p/>
     */
    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        locationUpdateTimeInterval = getUpdateIntervalInMilliseconds() == 0 ? UPDATE_INTERVAL_IN_MILLISECONDS : locationUpdateTimeInterval;
        mLocationRequest.setInterval(locationUpdateTimeInterval);
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }
}
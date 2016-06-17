package com.droid.manasshrestha.rxandroid.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ManasShrestha on 6/10/16.
 */
public class BroadCastText extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("asd", "onReceive: received" );
    }
}

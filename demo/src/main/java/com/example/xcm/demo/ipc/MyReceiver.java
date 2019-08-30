package com.example.xcm.demo.ipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.xcm.demo.utils.NotificationUtils;

/**
 * Created by xcm on 17-7-5.
 */

public class MyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("test")) {
            Log.d("MainActivity", "ddd");
            NotificationUtils.showNotification1(context);
        }
    }
}
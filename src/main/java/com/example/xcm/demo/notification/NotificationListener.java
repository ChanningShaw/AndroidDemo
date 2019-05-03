package com.example.xcm.demo.notification;

import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.xcm.demo.base.Config;

/**
 * Created by xcm on 18-3-30.
 */

public class NotificationListener extends NotificationListenerService {

    private static NotificationListener mNotificationListener;

    public static NotificationListener getInstance() {
        return mNotificationListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationListener = this;
    }

    @Override
    public void onDestroy() {
        mNotificationListener = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(Config.TAG, "notification post:" + sbn.getPackageName());
        Log.d(Config.TAG, "processName = " + Process.myPid());
        StatusBarNotification[] activeNotifications = getActiveNotifications();
        Log.d("xcm", "all active notification:");
        for (StatusBarNotification notification : activeNotifications) {
            Log.d("xcm", notification.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(Config.TAG, "notification remove:" + sbn.getPackageName());
    }
}

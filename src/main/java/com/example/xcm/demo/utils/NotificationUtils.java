package com.example.xcm.demo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.xcm.demo.MainActivity;
import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class NotificationUtils {

    public static final int DEFAULT_LED_COLOR = 0x00ff00;
    public static final int DEFAULT_CHANNEL_COLOR = 0x00ff00;
    public static final int DEFAULT_VALUE = Notification.DEFAULT_ALL;

    public static void showNotification1(Context context) {
        showNotification1(context, DEFAULT_LED_COLOR, DEFAULT_CHANNEL_COLOR, DEFAULT_VALUE);
    }

    public static void showNotification1(Context context, int ledColor, int channelColor, int defaultValue) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("xcm", "xcm", NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(true);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0, 100, 0});
            channel.setSound(null,null);
            channel.setLightColor(ledColor);
            manager.createNotificationChannel(channel);

            Toast.makeText(context, "111", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 1; i++) {
                Notification n = new NotificationCompat.Builder(context, "xcm")
                        .setSmallIcon(R.mipmap.ic_launcher)             //一定要设置
                        .setContentTitle("您有一条新的" + i)
                        .setContentText("这是一条")
                        .setAutoCancel(true)
                        .setColor(0x00000001)
                        .setChannelId("xcm")
                        .setVibrate(new long[]{0, 100, 100, 200, 100, 300})
                        .setLights(channelColor, 1000, 1000)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setDefaults(defaultValue).build();
                n.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
                manager.notify(i, n);
            }
        } else {

            Notification.Builder builder = new Notification.Builder(context);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("444");
            builder.setContentText("Make this service run in the foreground.");
            builder.setVisibility(Notification.VISIBILITY_PRIVATE);
            builder.setCategory(Notification.CATEGORY_MESSAGE);
            Notification public_notification = builder.build();
            for (int i = 0; i < 2; i++) {
                Notification notification = new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)             //一定要设置
                        .setContentTitle("您有一条新" + i)
                        .setContentText("这是一条")
                        .setAutoCancel(true)
                        .setColor(0x00000001)
                        .setVibrate(new long[]{0, 100, 100, 200})
                        .setLights(0x0001ff, 1000, 1000)
                        .setDefaults(Notification.DEFAULT_LIGHTS)
                        .setPublicVersion(public_notification)
                        .build();
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                manager.notify(i, notification);
            }
        }
        Log.e(Config.TAG, "importance:" + manager.getImportance());
    }


    public static void clearNotification1(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(1);
    }

    public static void showNotification2(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 1,
                new Intent(), 0);
//        builder.setContentIntent(contentIntent);
        PendingIntent contentIntent2 = PendingIntent.getBroadcast(context, 2,
                new Intent(), 0);
        builder.setDeleteIntent(contentIntent2);
        PendingIntent contentIntent3 = PendingIntent.getBroadcast(context, 3,
                new Intent(), 0);
        PendingIntent contentIntent4 = PendingIntent.getBroadcast(context, 4,
                new Intent(), 0);
        PendingIntent contentIntent5 = PendingIntent.getBroadcast(context, 5,
                new Intent(), 0);
        PendingIntent contentIntent6 = PendingIntent.getBroadcast(context, 6,
                new Intent(), 0);
        PendingIntent contentIntent7 = PendingIntent.getBroadcast(context, 7,
                new Intent(), 0);
        PendingIntent contentIntent8 = PendingIntent.getBroadcast(context, 8,
                new Intent(), 0);
        PendingIntent contentIntent9 = PendingIntent.getBroadcast(context, 9,
                new Intent(), 0);
        PendingIntent contentIntent10 = PendingIntent.getBroadcast(context, 10,
                new Intent(), 0);

//        Notification.Action[] actions = new Notification.Action[]{
//                new Notification.Action(R.mipmap.ic_launcher, "dddd", contentIntent4),
//                new Notification.Action(R.mipmap.ic_launcher, "dddd", contentIntent5),
//                new Notification.Action(R.mipmap.ic_launcher, "dddd", contentIntent6),
//                new Notification.Action(R.mipmap.ic_launcher, "dddd", contentIntent7),
//                new Notification.Action(R.mipmap.ic_launcher, "dddd", contentIntent8)};
//        builder.setActions(actions);

        builder.setFullScreenIntent(contentIntent9, false);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("222");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();

        long[] vibrates = {0, 200, 300, 200};
        notification.vibrate = vibrates;

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, "222", Toast.LENGTH_SHORT).show();
        manager.notify(2, notification);
        manager.notify(3, notification);
        manager.notify(4, notification);
        manager.notify(5, notification);
        manager.notify(6, notification);
        manager.notify(7, notification);
    }

    public static void showNotification3(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("333");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.sound = uri;

        notification.ledARGB = Color.YELLOW;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
        notification.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
        notification.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
        notification.flags = Notification.FLAG_SHOW_LIGHTS;// 指定通知的一些行为，其中就包括显示
        // LED 灯这一选项

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, "333", Toast.LENGTH_SHORT).show();
        manager.notify(8, notification);
    }

    public static void showNotification4(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("444");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(context, "444", Toast.LENGTH_SHORT).show();
        manager.notify(9, notification);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

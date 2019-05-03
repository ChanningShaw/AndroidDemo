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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xcm on 17-9-28.
 */

public class Utils {
    private static final String TAG = "xcm";

    public static void writeToExternalStorage(Context context) {
        for (int i = 0; i < 5; i++) {
            File logFile = new File(context.getFilesDir(), "log" + i + ".txt");
            if (!logFile.exists()) {
                boolean result; // 文件是否创建成功
                try {
                    result = logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                if (!result) {
                    return;
                }
            }
            try {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(logFile, true));
                bfw.write("6666\n");
                bfw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "writeToExternalStorage: " + i);
        }
    }

    public static void accessNetwork() {
        try {
            URL url = new URL("https://www.baidu.com");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.e(TAG, "" + con.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "" + e.toString());
        }
    }
}

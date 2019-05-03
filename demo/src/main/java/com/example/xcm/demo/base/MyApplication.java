package com.example.xcm.demo.base;

import android.app.Application;

import com.example.xcm.demo.crash.CrashHandler;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setCustomCrashHandler(getApplicationContext());
    }
}

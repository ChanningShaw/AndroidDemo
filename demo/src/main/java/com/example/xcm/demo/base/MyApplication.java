package com.example.xcm.demo.base;

import android.app.Activity;
import android.app.Application;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.xcm.demo.crash.CrashHandler;

public class MyApplication extends Application {

    public static final String Tag = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setCustomCrashHandler(getApplicationContext());
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallback());
    }


    class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}


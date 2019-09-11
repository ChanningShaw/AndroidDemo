package com.example.xcm.demo.app;

import android.app.Activity;
import android.app.Application;
import android.nfc.Tag;
import android.os.Bundle;

import com.example.xcm.demo.MainActivity;
import com.example.xcm.demo.crash.CrashHandler;
import com.example.xcm.demo.utils.NotificationUtils;
import com.example.xcm.demo.utils.Utils;

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


        private int activityStartCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityStartCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity instanceof MainActivity) {
                if (Utils.checkRoot()) {
                    NotificationUtils.showToast(MyApplication.this,"您的手机已经root");
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityStartCount--;
            if (activityStartCount == 0){
                NotificationUtils.showToast(MyApplication.this, "应用进入后台");
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}


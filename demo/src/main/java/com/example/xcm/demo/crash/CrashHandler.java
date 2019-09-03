package com.example.xcm.demo.crash;

import android.content.Context;
import android.os.Process;

import com.example.xcm.demo.network.NetworkUtils;
import com.example.xcm.demo.utils.NotificationUtils;

public class CrashHandler implements Thread.UncaughtExceptionHandler{

    private static CrashHandler instance = new CrashHandler();
    private Context mContext;

    private CrashHandler() {}

    public static CrashHandler getInstance() {
        return instance;
    }

    public void setCustomCrashHandler(Context context) {
        mContext = context;
        //崩溃时将catch住异常
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        //使用Toast进行提示
        NotificationUtils.showToast(mContext, "很抱歉，程序异常即将退出！");
        //延时退出
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Process.killProcess(Process.myPid());
    }
}

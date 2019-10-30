package com.example.xcm.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

public class NightModeUtils {

    private static final String NIGHT_MODE = "Night_Mode";

    public static void setNightMode(Context context, int mode) {
        SharedPreferences preferences = context.getSharedPreferences(NIGHT_MODE, context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = preferences.edit();
        mEditor.putInt(NIGHT_MODE, mode);
        mEditor.commit();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static int getNightMode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NIGHT_MODE, context.MODE_PRIVATE);
        return preferences.getInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}

package com.example.xcm.demo.ams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("xcm", intent.getAction());
    }
}

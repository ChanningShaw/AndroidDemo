package com.example.xcm.demo.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class TestWindowService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        installFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    void installFloatingWindow(){
        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        final Button btn = new Button(this.getBaseContext());
        btn.setText("click me to dismiss");
        WindowManager.LayoutParams lp = createLayoutParams();
        wm.addView(btn,lp);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                wm.removeView(btn);
            }
        });
    }

    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        return lp;
    }
}

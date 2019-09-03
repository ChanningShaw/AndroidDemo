package com.example.xcm.demo.wms;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TestWmsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageView view = new ImageView(this);
        view.setBackgroundColor(Color.GREEN);

        getWindowManager().addView(view, getWindowParams());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    view.setVisibility(View.GONE);
                    getWindowManager().removeView(view);
                } catch (IllegalArgumentException e) {
                    // the window maybe already deattach from wms
                    e.printStackTrace();
                }

            }
        }, 10000);
    }

    private WindowManager.LayoutParams getWindowParams(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;// 支持透明
        //params.format = PixelFormat.RGBA_8888;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        params.width = 490;//窗口的宽和高
        params.height = 160;
        params.x = 500;//窗口位置的偏移量
        params.y = 0;
        //params.alpha = 0.1f;//窗口的透明度
        return params;
    }
}

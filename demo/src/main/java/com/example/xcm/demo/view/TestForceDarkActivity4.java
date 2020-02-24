package com.example.xcm.demo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestForceDarkActivity4 extends AppCompatActivity {

    private static final String TAG = Config.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_force_dark4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            shotActivityAndJudge(this);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据指定的Activity截图（带空白的状态栏）
     *
     * @param context 要截图的Activity
     * @return Bitmap
     */
    public boolean shotActivityAndJudge(Activity context) {
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.white_black);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        final int xStep = Math.max(bitmap.getWidth() / 10, 1);
        final int yStep = Math.max(bitmap.getHeight() / 10, 1);
        float[] hsv = new float[3];
        int blackCount = 0;
        int totalCount = 0;
        boolean adaptDarkMode;
        for (int i =0; i < bitmap.getWidth(); i += xStep) {
            for (int j = 0; j < bitmap.getHeight(); j += yStep) {
                int pixel = bitmap.getPixel(i, j);
                int r = pixel >> 16 & 0xff;
                int g = pixel >> 8 & 0xff;
                int b = pixel & 0xff;
                Color.RGBToHSV(r, g, b, hsv);
                if (hsv[2] < 0.5) {
                    blackCount++;
                }
                totalCount++;
                Log.e(TAG, "rgb:" + r + "," + g + "," + b + ", hsv: " + hsv[0] + "," + hsv[1] + "," + hsv[2]);
            }
        }
        Log.e(TAG, "shotActivityAndJudge: blackCount = " + blackCount);

        adaptDarkMode = blackCount >= totalCount / 2;

        return adaptDarkMode;
    }
}

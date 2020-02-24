package com.example.xcm.demo.grahpic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xcm.demo.R;

public class TestShotViewActivity extends AppCompatActivity {
    private static final String TAG = "TestShotViewActivity";

    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_shot_view);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long before = SystemClock.uptimeMillis();
                boolean adaptDarkMode = shotActivityAndJudge(TestShotViewActivity.this);
                if (adaptDarkMode) {
                    textView.setText("适配了darkMode");
                } else {
                    textView.setText("没有适配darkMode");
                }
                Log.d(TAG, "time cost:" + (SystemClock.uptimeMillis() - before));
            }
        });
        imageView = findViewById(R.id.iv);
        textView = findViewById(R.id.tv);
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

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
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
//                Log.e(TAG, "shotActivityAndJudge hsv: " + hsv[0] + "," + hsv[1] + "," + hsv[2]);
            }
        }
        Log.e(TAG, "shotActivityAndJudge: blackCount = " + blackCount);

        adaptDarkMode = blackCount >= totalCount / 2;

        imageView.setImageBitmap(bitmap);
        return adaptDarkMode;
    }

}

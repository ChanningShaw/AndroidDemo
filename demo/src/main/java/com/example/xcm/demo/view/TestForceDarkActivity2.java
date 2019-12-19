package com.example.xcm.demo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestForceDarkActivity2 extends AppCompatActivity {

    private static final String TAG = Config.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_force_dark2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            getWindow().getDecorView().invalidate();
        }
        return super.onTouchEvent(event);
    }
}

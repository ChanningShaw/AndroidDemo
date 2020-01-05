package com.example.xcm.demo.wms;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestWmsActivity extends AppCompatActivity {

    private Button view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_window);
        view = new MyButton(this);

        getWindowManager().addView(view, getWindowParams());

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    view.setVisibility(View.GONE);
                    getWindowManager().removeView(view);
                } catch (IllegalArgumentException e) {
                    // the window maybe already deattach from wms
                    e.printStackTrace();
                }
            }
        });
    }

    private WindowManager.LayoutParams getWindowParams(){
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.format = PixelFormat.TRANSLUCENT;// 支持透明
        //params.flags |= WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;// 接受窗口外事件
        //params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = 490;//窗口的宽和高
        params.height = 160;
        params.x = 500;//窗口位置的偏移量
        params.y = 0;
        params.setTitle("MyDialog");
        return params;
    }

    public void dismiss(){
        getWindowManager().removeView(view);
    }

    class MyButton extends AppCompatButton {

        private TestWmsActivity activity;
        public MyButton(TestWmsActivity context) {
            super(context);
            activity = context;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.d(Config.TAG, "onTouchEvent: " + event.getAction());
            activity.dismiss();
            return true;

        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.d(Config.TAG, "onKeyDown: " + keyCode);
            activity.dismiss();
            return true;
        }
    }
}

package com.example.xcm.demo.view;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.xcm.demo.R;

public class TestShowViewOnChildThreadActivity extends AppCompatActivity {

    static Toast sToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_show_view_on_child_thread);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        if (sToast == null) {
                            sToast = Toast.makeText(TestShowViewOnChildThreadActivity.this,
                                    "子线程弹出Toast", Toast.LENGTH_SHORT);
                        }
                        sToast.setText("子线程弹出Toast");
                        sToast.show();
                        Looper.loop();
                    }
                }).start();
            }
        });

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sToast == null) {
                    sToast = Toast.makeText(TestShowViewOnChildThreadActivity.this,
                            "主线程弹出Toast", Toast.LENGTH_SHORT);
                    sToast.setText("主线程弹出Toast");
                }
                sToast.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

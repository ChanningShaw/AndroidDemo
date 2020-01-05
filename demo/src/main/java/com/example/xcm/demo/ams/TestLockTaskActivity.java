package com.example.xcm.demo.ams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.xcm.demo.R;

public class TestLockTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mLockTask;

    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_lock_task);
        mButton = findViewById(R.id.bt);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mLockTask = !mLockTask;
        if (mLockTask) {
            startLockTask();
            mButton.setText("点我解锁屏幕");
        } else {
            stopLockTask();
            mButton.setText("点我锁定屏幕");
        }
    }
}

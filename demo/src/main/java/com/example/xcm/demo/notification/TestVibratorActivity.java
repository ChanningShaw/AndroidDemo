package com.example.xcm.demo.notification;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xcm.demo.R;

/**
 * Created by xcm on 18-4-13.
 */

public class TestVibratorActivity extends AppCompatActivity implements View.OnClickListener {

    private Vibrator vibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_vibrator);
        initView();
        initVibrator();
    }

    private void initVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initView() {
        findViewById(R.id.vibrator_strong).setOnClickListener(this);
        findViewById(R.id.vibrator_medium).setOnClickListener(this);
        findViewById(R.id.vibrator_weak).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (vibrator == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.vibrator_weak:
                vibrator.vibrate(new long[]{9, 11}, -1);
                break;
            case R.id.vibrator_medium:
                vibrator.vibrate(new long[]{5, 20}, -1);
                break;
            case R.id.vibrator_strong:
                vibrator.vibrate(new long[]{0, 30}, -1);
                break;
        }
    }
}

package com.example.xcm.demo.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.xcm.demo.MainActivity;
import com.example.xcm.demo.R;

import java.util.Timer;
import java.util.TimerTask;

public class TestBackgroundActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_background_activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(TestBackgroundActivity.this, MainActivity.class));
            }
        };
        timer.schedule(task, 5000);
    }
}

package com.example.xcm.demo.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.xcm.demo.R;
import com.example.xcm.demo.utils.BitmapUtils;

public class TestWindowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_window);
        Intent intent = new Intent(this, TestWindowService.class);
        startService(intent);
    }
}

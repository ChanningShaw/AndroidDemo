package com.example.xcm.demo.ams;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xcm.demo.R;

public class LaunchHomeActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ams);
        findViewById(R.id.bt_start_ams).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage("com.miui.home"); // "com.miui.home"
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}

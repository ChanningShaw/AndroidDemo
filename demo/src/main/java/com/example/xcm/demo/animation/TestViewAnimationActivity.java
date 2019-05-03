package com.example.xcm.demo.animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.example.xcm.demo.R;

public class TestViewAnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_animation);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0, 200, 0, 200);
                animation.setDuration(2000);
                v.startAnimation(animation);
            }
        });
    }
}

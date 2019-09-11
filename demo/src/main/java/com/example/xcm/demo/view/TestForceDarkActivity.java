package com.example.xcm.demo.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestForceDarkActivity extends AppCompatActivity {

    private static final String TAG = Config.TAG;

    View decorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_force_dark);
        decorView = getWindow().getDecorView();
        decorView.setForceDarkAllowed(false);
        final Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decorView.setForceDarkAllowed(!decorView.isForceDarkAllowed());
                decorView.invalidate();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.d(TAG, "Night mode is not active");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.d(TAG, "Night mode is active");
                break;
        }
    }
}

package com.example.xcm.demo.view;

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
        final Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allowed = !decorView.isForceDarkAllowed();
                decorView.setForceDarkAllowed(allowed);
                Log.d(TAG, "isForceDarkAllowed = " + allowed);
                decorView.invalidate();
            }
        });
    }
}

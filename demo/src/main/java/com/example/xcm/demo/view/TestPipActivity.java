package com.example.xcm.demo.view;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.widget.Toast;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestPipActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = Config.TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pip);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                enterPip();
                break;
        }
    }

    private void enterPip() {
        PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
        Rational actions = new Rational(3,4);
        builder.setAspectRatio(actions);
        enterPictureInPictureMode(builder.build());
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d(TAG, "onUserLeaveHint: ");
        enterPip();
    }
}

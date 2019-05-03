package com.example.xcm.demo.view;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Rational;
import android.view.View;
import android.widget.Toast;

import com.example.xcm.demo.R;

public class TestPipActivity extends AppCompatActivity implements View.OnClickListener {
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
            case R.id.button2:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void enterPip() {
        PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
        Rational actions = new Rational(3,4);
        builder.setAspectRatio(actions);
        enterPictureInPictureMode(builder.build());
    }
}

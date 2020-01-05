package com.example.xcm.demo.grahpic.opengl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TestGLActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGLView.destroy();
    }
}

package com.example.xcm.demo.grahpic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;
import com.example.xcm.demo.utils.BitmapUtils;

public class TestMediaProjectionActivity extends AppCompatActivity implements ScreenCapturer.OnImageCaptureScreenListener {

    private static final int REQUEST_CODE = 100;
    MediaProjectionManager mProjectionManager;
    MediaProjection sMediaProjection;
    ScreenCapturer screenCapturer;
    ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        setContentView(R.layout.test_media_projection);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            }
        });
        mImageView = findViewById(R.id.iv);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && REQUEST_CODE == requestCode) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            if (sMediaProjection != null) {
                Log.d(Config.TAG, "Start capturing...");
                screenCapturer = new ScreenCapturer(this, sMediaProjection, "");
                screenCapturer.setOnImageCaptureListener(this);
                screenCapturer.startProjection();
            }
        }
    }

    @Override
    public void onImageCaptured(byte[] image) {
        final Bitmap bitmap = BitmapUtils.byteToBitmap(image);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bitmap);
            }
        });
    }
}

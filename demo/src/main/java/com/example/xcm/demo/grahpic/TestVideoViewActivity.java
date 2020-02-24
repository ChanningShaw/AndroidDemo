package com.example.xcm.demo.grahpic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;
import com.example.xcm.demo.utils.NotificationUtils;

public class TestVideoViewActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_video_view);
        VideoView videoView = findViewById(R.id.vv);
        View startBt = findViewById(R.id.bt_start);
        View stopBt = findViewById(R.id.bt_stop);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(this);

        videoView.start();

        Log.d(Config.TAG, "TestVideoViewActivity onCreate: ");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        NotificationUtils.showToast(this, "视频播放完了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Config.TAG, "TestVideoViewActivity onDestroy: ");
    }
}

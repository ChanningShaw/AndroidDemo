package com.example.xcm.demo.view;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xcm.demo.R;

public class TestAnimationDrawableActivity extends AppCompatActivity {

    private Button btStart;
    private Button btEnd;
    private ImageView iv_frame;
    private AnimationDrawable frameAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_animation_drawable);
        btStart = findViewById(R.id.bt_start);
        btEnd = findViewById(R.id.bt_end);
        btStart.setOnClickListener(click);
        btEnd.setOnClickListener(click);

        iv_frame = findViewById(R.id.iv);
        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        frameAnim = (AnimationDrawable) getResources().getDrawable(R.drawable.animation_drawable);
        // 把AnimationDrawable设置为ImageView的背景
        iv_frame.setBackgroundDrawable(frameAnim);
    }

    private View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_start:
                    start();
                    break;
                case R.id.bt_end:
                    stop();
                    break;
                default:
                    break;
            }
        }
    };

    protected void start() {
        if (frameAnim != null && !frameAnim.isRunning()) {
            frameAnim.start();
            Toast.makeText(TestAnimationDrawableActivity.this, "开始播放", Toast.LENGTH_LONG).show();
            Log.i("main", "index 为5的帧持续时间为："+frameAnim.getDuration(5)+"毫秒");
            Log.i("main", "当前AnimationDrawable一共有"+frameAnim.getNumberOfFrames()+"帧");
        }
    }

    protected void stop() {
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
            Toast.makeText(TestAnimationDrawableActivity.this, "停止播放", Toast.LENGTH_LONG).show();
        }
    }
}
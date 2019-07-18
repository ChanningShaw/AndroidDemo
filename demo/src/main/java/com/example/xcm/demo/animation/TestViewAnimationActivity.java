package com.example.xcm.demo.animation;

import android.animation.PropertyValuesHolder;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.example.xcm.demo.R;

public class TestViewAnimationActivity extends AppCompatActivity {

    private Choreographer mChoreographer = Choreographer.getInstance();

    private Button bt2;

    final Transformation t = new Transformation();
    long mAnimationStartTime;

    private Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            float fraction = (frameTimeNanos / 1000f / 1000f - mAnimationStartTime) / 2000f;
            float tx = 200 * fraction;
            Matrix m = new Matrix();
            m.reset();
            float[] tmp = new float[9];
            m.postConcat(bt2.getMatrix());
            m.getValues(tmp);
            Log.d("xcm", "tx=" + tx);
            bt2.setTranslationX(tx);
            bt2.requestLayout();
            if (fraction < 1.0f) {
                mChoreographer.postFrameCallback(this);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_animation);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0, 200, 0, 200);
                animation.setDuration(2000);
                v.startAnimation(animation);
            }
        });


        bt2 = findViewById(R.id.button2);

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mChoreographer.postFrameCallback(mFrameCallback);
                mAnimationStartTime = SystemClock.uptimeMillis();
            }
        });
    }
}

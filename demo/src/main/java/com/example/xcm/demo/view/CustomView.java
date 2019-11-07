package com.example.xcm.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.xcm.demo.base.Config;

public class CustomView extends View {

    Paint mPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(Config.TAG, "onDraw: width=" + canvas.getWidth() + ", height=" + canvas.getHeight());
        canvas.drawRect(new Rect(10, 10, 100, 100), mPaint);
    }
}

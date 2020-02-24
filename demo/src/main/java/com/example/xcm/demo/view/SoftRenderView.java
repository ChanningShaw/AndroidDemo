package com.example.xcm.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class SoftRenderView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    public SoftRenderView(Context context) {
        this(context, null);
    }

    public SoftRenderView(Context context, @Nullable  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftRenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //初始化画笔
        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextSize(50);

        mBitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mCanvas.drawText("你是来搞笑的么！！！", 100, 100, mPaint);
        canvas.drawBitmap(mBitmap, 100, 100, mPaint);
    }
}

package com.example.xcm.demo.grahpic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.xcm.demo.base.Config;

import java.io.IOException;
import java.io.InputStream;

public class MySurfaceView extends SurfaceView implements Callback {

    private Paint paint;
    private SurfaceHolder holder;
    private Activity mActivity;

    public MySurfaceView(Context context) {
        super(context);
        mActivity = (Activity) context;
        paint = new Paint();
        holder = this.getHolder();
        holder.addCallback(this);
        paint.setColor(Color.RED);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setFixedSize(800, 800);
        mDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void mDraw() {
        Log.d(Config.TAG, "mDraw: ");
        Canvas canvas = holder.lockCanvas();
        Rect rect = new Rect(0, 0, 1080, 1920);

        Bitmap bitmap;
        try {
            InputStream ims = this.getContext().getAssets().open("P3-4.jpg");
            BitmapDrawable bd = (BitmapDrawable) Drawable.createFromStream(ims, null);
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = bd.getBitmap();
            ims.close();
        } catch (IOException ex) {
            Log.d(Config.TAG, "fail to load photo: ");
            return;
        }
        canvas.drawBitmap(bitmap, null, rect, new Paint());
        holder.unlockCanvasAndPost(canvas);
    }
}

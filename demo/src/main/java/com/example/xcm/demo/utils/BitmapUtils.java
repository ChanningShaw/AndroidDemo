package com.example.xcm.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.example.xcm.demo.base.Config;

import java.io.InputStream;

public class BitmapUtils {
    public static Bitmap scaleBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float) screenWidth / w;
        float scale2 = (float) screenHeight / h;
        // scale = scale < scale2 ? scale : scale2;
        matrix.postScale(scale, scale);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public static Bitmap ReadBitmapById(Context context, int drawableId,
                                        int screenWidth, int screenHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return scaleBitmap(bitmap, screenWidth, screenHeight);
    }


    public static Bitmap decodeBitmapFitScreen(Context context, int drawableId) {
        Bitmap image = null;
        try {
            // 获取原图宽度
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeResource(context.getResources(), drawableId, options);

            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

            int size = 1;
            int scaleW = (options.outWidth / screenWidth);
            int scaleH = (options.outHeight / screenHeight);
            size = scaleW > scaleH ? scaleW : scaleH;

            // 计算缩放比例
            if (size <= 0) {
                size = 1;
            }
            Log.d(Config.TAG, "old-w:" + options.outWidth + ", size:" + size);
            // 缩放
            options.inJustDecodeBounds = false;
            options.inSampleSize = size;
            image = BitmapFactory.decodeResource(context.getResources(), drawableId, options);
            image = scaleBitmap(image, screenWidth, screenHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

}

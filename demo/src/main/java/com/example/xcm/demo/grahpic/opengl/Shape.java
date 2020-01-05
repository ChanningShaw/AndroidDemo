package com.example.xcm.demo.grahpic.opengl;

import android.content.Context;

public abstract class Shape {
    private Context mContext;

    public Shape(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void destroy();
}

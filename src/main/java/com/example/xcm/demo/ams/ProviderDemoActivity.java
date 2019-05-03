package com.example.xcm.demo.ams;

import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xcm.demo.R;

public class ProviderDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button queryButton;
    private TextView tvResult;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_provider);
        queryButton = findViewById(R.id.bt_query);
        tvResult = findViewById(R.id.tv_result);
        queryButton.setOnClickListener(this);
        HandlerThread t = new HandlerThread("demo:provider");
        t.start();
        mHandler = new H(t.getLooper());
    }

    private class H extends Handler {

        public H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvResult.setText("query result:" + msg.what);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1100; i++) {
                    ContentProviderClient p = getContentResolver()
                            .acquireContentProviderClient("com.xcm.demo.provider");
                    Uri uri_user = Uri.parse("content://com.xcm.demo.provider/user");
                    Cursor cursor = null;
                    try {
                        cursor = p.query(uri_user, null, null, null,
                                null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("xcm", "activity thread=" + Process.myTid());
                    Log.d("xcm", "result =" + cursor.getInt(0));
                    mHandler.obtainMessage(cursor.getInt(0)).sendToTarget();
                    cursor.close();
                }
            }
        });
    }
}

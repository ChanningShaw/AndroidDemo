package com.example.xcm.demo.ipc;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.xcm.demo.IMemoryAidlInterface;
import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

import java.io.FileDescriptor;
import java.io.FileInputStream;

public class TestMemoryFileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_memory_file);
        final Intent intent = new Intent(TestMemoryFileActivity.this, MemoryFetchService.class);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Config.TAG, "bindService");
                bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                        byte[] content = new byte[10];
                        IMemoryAidlInterface iMemoryAidlInterface
                                = IMemoryAidlInterface.Stub.asInterface(service);
                        try {
                            ParcelFileDescriptor parcelFileDescriptor = iMemoryAidlInterface.getParcelFileDescriptor();
                            FileDescriptor descriptor = parcelFileDescriptor.getFileDescriptor();
                            FileInputStream fileInputStream = new FileInputStream(descriptor);
                            int read = fileInputStream.read(content);

                            StringBuilder sb = new StringBuilder();
                            for (byte b : content) {
                                sb.append(b);
                            }
                            Log.d(Config.TAG, sb.toString());

                        } catch (Exception e) {
                            Log.d(Config.TAG, e.getMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Service.BIND_AUTO_CREATE);
            }
        });
    }
}

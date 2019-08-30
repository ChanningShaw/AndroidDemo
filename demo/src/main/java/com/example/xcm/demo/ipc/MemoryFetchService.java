package com.example.xcm.demo.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.xcm.demo.IMemoryAidlInterface;
import com.example.xcm.demo.base.Config;

import java.io.FileDescriptor;
import java.lang.reflect.Method;

public class MemoryFetchService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Config.TAG, "onBind");
        return new MemoryFetchStub();
    }

    static class MemoryFetchStub extends IMemoryAidlInterface.Stub {
        @Override
        public ParcelFileDescriptor getParcelFileDescriptor() throws RemoteException {
            Log.d(Config.TAG, "bn getParcelFileDescriptor");
            MemoryFile memoryFile = null;
            try {
                memoryFile = new MemoryFile("test_memory", 1024);
                memoryFile.getOutputStream().write(new byte[]{1, 2, 3, 4, 5});
                Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                FileDescriptor des = (FileDescriptor) method.invoke(memoryFile);
                return ParcelFileDescriptor.dup(des);
            } catch (Exception e) {
            }
            return null;
        }
    }
}

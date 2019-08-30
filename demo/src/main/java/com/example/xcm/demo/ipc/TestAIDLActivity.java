package com.example.xcm.demo.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.xcm.demo.R;
import com.yichuang.mylibrary.Book;
import com.yichuang.mylibrary.BookManager;
import com.yichuang.mylibrary.IBookHolder;

import java.util.List;

public class TestAIDLActivity extends AppCompatActivity {

    private static final String TAG = "xcm";

    TelephonyManager manager;

    //由AIDL文件生成的Java类
    private BookManager mBookManager;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private static boolean mBound = false;

    //包含Book对象的list
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //获取电话服务
        manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        // 手动注册对PhoneStateListener中的listen_call_state状态进行监听
        manager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void doAddBook() {
        Thread.dumpStack();
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < 10; i++) {
            addBook(i);
        }
        System.gc();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "service connected");
            mBookManager = BookManager.Stub.asInterface(service);
            mBound = true;

            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBooks();
                    if (mBooks == null) {
                        Log.e(TAG, "null");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "service disconnected");
            mBound = false;
        }
    };

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.example.hooligan.aidl");
        intent.setPackage("com.example.xcm.test");
        Log.d(TAG, "try connect server");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 按钮的点击事件，点击之后调用服务端的addBookIn方法
     */
    public void addBook(int price) {
        if (mBookManager == null) return;

        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(price);
        try {
            Thread.sleep(2000);
            mBookManager.addBook(new BookHolder(book));
            Log.e(TAG, book.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class BookHolder extends IBookHolder.Stub {

        private Book mValue;

        public BookHolder(Book book) {
            mValue = book;
        }

        @Override
        public Book get() throws RemoteException {
            Book book = mValue;
            mValue = null;
            Log.d(TAG, "BookHolder.get()");
            return book;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            Log.d(TAG, "bookholder is finalize");
        }
    }


    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "phone state is idle: ");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TAG, "phone state is ringing: ");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TAG, "phone state is offhook");
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }
}

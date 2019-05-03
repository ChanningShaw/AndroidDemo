package com.example.xcm.demo.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xcm.demo.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by xcm on 18-3-28.
 */

public class TestHandlerActivity extends AppCompatActivity {

    public static final String TAG = "xcm";

    private Button mTitle;
    private Button mBtn;
    private Button mCrashButton;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    Future<?> mTask;
    boolean mSendMsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_handler);
        initFindView();
        setListener();
        myHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "#######receive the msg?? what = " + msg.what);
                int num = msg.what;
                switch (num) {
                    case 1:
                        mTitle.setText("######Yeah, we receive the first msg 1");
                        break;
                    case 2:
                        mTitle.setText("######Yeah, we receive the second msg 2");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initFindView() {
        mTitle = findViewById(R.id.btn1);
        mBtn = findViewById(R.id.btn2);
        mCrashButton = findViewById(R.id.btn3);
    }

    Handler myHandler;

    private void setListener() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestCallBack testCallBack = new TestCallBack();
                testCallBack.loadToHandler();
            }
        });
        mCrashButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException("5s crash");
                    }
                }, 5000);
            }
        });
    }

    private class TestCallBack {
        public TestCallBack() {
            Log.d(TAG, "#####TestCallBack===Constructor");
        }

        public void loadToHandler() {
            testExecutorHandler(myHandler);
        }
    }

    public void testExecutorHandler(final Handler handler) {
        Log.d(TAG, "########testExecutorHandler, mTask = " + mTask);
        if (mTask != null) {
            // 通过取消mTask,来实现之前排队但未运行的submit的task的目的,通过标志位不让其发msg给UI主线程更新.
            mTask.cancel(false);
            Log.d(TAG, "########mTask.isCannel? === " + mTask.isCancelled());
            mSendMsg = false;
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                mSendMsg = true;
                try {
                    Log.d(TAG, "###step 1####start to sleep 6s.");
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg;
                Log.d(TAG, "#######1111 mSendMsg === " + mSendMsg);
                if (mSendMsg) {
                    msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    return;
                }
                Log.d(TAG, "####step 2####start to sleep 4s.");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 若没有重新obtainMessage的话,就会出现以下错误,因为已经被回收, 所以报错. 需要重新 obtainMessage().
//                E/AndroidRuntime( 1606): java.lang.IllegalStateException: The specified message queue synchronization  barrier token has not been posted or has already been removed.
                Log.d(TAG, "#######22222 mSendMsg === " + mSendMsg);
                if (mSendMsg) {
//                    msg = handler.obtainMessage();

                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    return;
                }
            }
        };
        mTask = mExecutor.submit(r);
    }
}

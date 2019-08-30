package com.example.xcm.demo.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yichuang.mylibrary.Book;
import com.yichuang.mylibrary.BookManager;
import com.yichuang.mylibrary.IBookHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcm on 17-10-10.
 */

public class AIDLService extends Service {

    public final String TAG = "xcm";

    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    private final BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                return mBooks;
            }
        }

        @Override
        public void addBook(IBookHolder bookHolder) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                Book book;
                if (bookHolder != null) {
                    try {
                        book = bookHolder.get();
                    } catch (Exception e) {
                        Log.w(TAG, "addBook: Error receiving book", e);
                        return;
                    }

                    if (book != null) {
                        //尝试修改book的参数，主要是为了观察其到客户端的反馈
                        if (!mBooks.contains(book)) {
                            mBooks.add(book);
                        }
                        //打印mBooks列表，观察客户端传过来的值
                        Log.e(TAG, "invoking addBook() method , now the list is : "
                                + mBooks.toString() + ",thread=" + Thread.currentThread());
                    } else {
                        Log.e(TAG, "book is null!");
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.currentThread().setName("AIDLService");
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "service is destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "service onbind,thread = " + Thread.currentThread());
        return mBookManager;
    }
}

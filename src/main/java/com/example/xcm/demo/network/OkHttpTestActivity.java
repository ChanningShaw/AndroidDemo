package com.example.xcm.demo.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.xcm.demo.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpTestActivity extends AppCompatActivity implements View.OnClickListener {
    OkHttpClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_network);
        Button button = findViewById(R.id.bt);
        button.setOnClickListener(this);

        File httpCacheDirectory = new File(this.getCacheDir(), "okhttpCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(netCacheInterceptor)
                .addInterceptor(offlineCacheInterceptor)
                .cache(cache)
                .build();
    }

    @Override
    public void onClick(View v) {
        Request request = new Request.Builder()
                .url("https://www.jianshu.com/c/V2CqjW?utm_medium=index-collections&utm_source=desktop")
                .get()
                .build();
        client.newCall(request);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("xcm", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("xcm", "onResponse: " + response.body().string());
            }
        });
    }

    final Interceptor netCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            int onlineCacheTime = 30;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + onlineCacheTime)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    /**
     * 没有网时候的缓存
     */
    final Interceptor offlineCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isNetworkConnect(OkHttpTestActivity.this)) {
                request = request.newBuilder().cacheControl(new CacheControl
                                .Builder()
                                .maxStale(60, TimeUnit.SECONDS)
                                .onlyIfCached()
                                .build()
                        ).build();
            }
            return chain.proceed(request);
        }
    };

}

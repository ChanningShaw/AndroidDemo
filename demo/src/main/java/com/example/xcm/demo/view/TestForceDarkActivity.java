package com.example.xcm.demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.xcm.demo.R;
import com.example.xcm.demo.base.Config;

public class TestForceDarkActivity extends AppCompatActivity {

    private static final String TAG = Config.TAG;

    View decorView;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            boolean showOnKeyGuard = intent.getBooleanExtra("showOnKeyGuard", false);
            Log.d(TAG, "showOnKeyGuard=" + showOnKeyGuard);
        }
        decorView = getWindow().getDecorView();
        setContentView(R.layout.test_force_dark);
        final Button bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allowed = !decorView.isForceDarkAllowed();
                decorView.setForceDarkAllowed(allowed);
                Log.d(TAG, "isForceDarkAllowed = " + allowed);
                decorView.invalidate();
            }
        });

        webView = findViewById(R.id.wv);
        webView.loadUrl("http://www.baidu.com/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
}

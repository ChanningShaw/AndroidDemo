package com.yichuang.anotherapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button queryButton;
    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryButton = findViewById(R.id.bt_query);
        tvResult = findViewById(R.id.tv_result);
        queryButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Uri uri_user = Uri.parse("content://com.xcm.demo.provider/user");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri_user, null, null, null,
                null);
        if (cursor.moveToNext()) {
            tvResult.setText("query result:" + cursor.getInt(0) + " " + cursor.getString(0));
        }
        cursor.close();
    }
}

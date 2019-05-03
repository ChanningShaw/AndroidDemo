package com.example.xcm.demo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xcm.demo.R;

import java.util.HashMap;

public class ContentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String[] from = {"name"};
    int[] to = {R.id.tv};

    private CategoryContainer mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mContainer = intent.getExtras().getParcelable("container");

        HashMap map = new HashMap<String, String>();
        setContentView(R.layout.content_layout);
        ListView lv = findViewById(R.id.menu);
        CategoryListViewAdapter adapter = new CategoryListViewAdapter(mContainer.getChildren(), ContentActivity.this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CategoryContainer child = mContainer.getChild(position);
        if (child instanceof Category) {
            Category c = (Category) child;
            startActivity(c.getTargetClazz());
        } else {
            Intent intent = new Intent(ContentActivity.this, ContentActivity.class);
            intent.putExtra("container", child);
            startActivity(intent);
        }
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}

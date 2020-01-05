package com.example.xcm.demo.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xcm.demo.R;

import java.util.List;

public class CategoryListViewAdapter extends BaseAdapter {

    List<CategoryContainer> mCategoryItems;
    Context mContext;

    public CategoryListViewAdapter(List mCategoryItems, Context mContext) {
        this.mCategoryItems = mCategoryItems;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCategoryItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null, false);
            holder = new ViewHolder();
            holder.childCout = convertView.findViewById(R.id.tv_child_count);
            holder.name = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.childCout.setText("demo:" + mCategoryItems.get(position).getChildCountRecur());
        holder.name.setText(mCategoryItems.get(position).mName);
        return convertView;
    }
}

class ViewHolder {
    TextView childCout;
    TextView name;
}

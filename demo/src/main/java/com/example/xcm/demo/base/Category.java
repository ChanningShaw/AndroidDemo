package com.example.xcm.demo.base;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

public class Category extends CategoryContainer<Category> {
    public Class clazz;

    public Category(Class<? extends Activity> clazz) {
        super(clazz.getSimpleName());
        this.clazz = clazz;
    }

    public Class getTargetClazz() {
        return clazz;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(clazz);
        super.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<CategoryContainer> CREATOR = new Creator<CategoryContainer>() {

        @Override
        public CategoryContainer[] newArray(int size) {
            return new Category[size];
        }

        @Override
        public CategoryContainer createFromParcel(Parcel source) {
            Class clazz = (Class) source.readSerializable();
            String name = source.readString();
            Category container = new Category(clazz) {
            };
            source.readList(container.mChildren, null);
            return container;
        }
    };
}
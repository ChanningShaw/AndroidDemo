package com.example.xcm.demo.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public abstract class CategoryContainer<E extends CategoryContainer> implements Parcelable {
    ArrayList<E> mChildren = new ArrayList<E>();
    String mName;

    public CategoryContainer(String name) {
        this.mName = name;
    }

    public E getChild(int index) {
        return mChildren.get(index);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeList(mChildren);
    }

    public static final Parcelable.Creator<CategoryContainer> CREATOR = new Creator<CategoryContainer>() {

        @Override
        public CategoryContainer[] newArray(int size) {
            return new Category[size];
        }

        @Override
        public CategoryContainer createFromParcel(Parcel source) {
            String name = source.readString();
            CategoryContainer container = new CategoryContainer(name) {
            };
            source.readList(container.mChildren, getClass().getClassLoader());
            return container;
        }
    };

    public ArrayList<E> getChildren() {
        return mChildren;
    }
}

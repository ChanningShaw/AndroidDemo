package com.example.xcm.demo.base;

public class FirstLevelCategory<E extends SecondLevelCategory> extends CategoryContainer<E> {

    public FirstLevelCategory(String name) {
        super(name);
    }
}

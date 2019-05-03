package com.example.xcm.demo.base;

public class SecondLevelCategory<E extends Category> extends CategoryContainer<E> {

    public SecondLevelCategory(String name) {
        super(name);
    }
}

package com.grabapp.myapplication.util;

/**
 * Created by dy on 18/8/16.
 */
public interface RequestListener {
    void onSuccess(Object object);
    void onError(Object object);
}

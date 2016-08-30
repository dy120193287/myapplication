package com.grabapp.myapplication.util;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.ivankocijan.magicviews.MagicViews;

import io.fabric.sdk.android.Fabric;

/**
 * Created by dy on 16/8/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("sadasd","sadsda");
        MagicViews.setFontFolderPath(this, "fonts");

    }

}
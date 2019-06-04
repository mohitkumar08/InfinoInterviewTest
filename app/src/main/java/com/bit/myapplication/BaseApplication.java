package com.bit.myapplication;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}

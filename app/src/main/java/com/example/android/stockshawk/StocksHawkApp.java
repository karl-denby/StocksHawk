package com.example.android.stockshawk;

import android.app.Application;
import android.util.Log;

import timber.log.BuildConfig;
import timber.log.Timber;

public class StocksHawkApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v("DEBUG", "StocksHawkApp.onCreate()");
        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
    }

}

package com.example.android.stockshawk;

import android.app.Application;
import android.content.Context;

import timber.log.BuildConfig;
import timber.log.Timber;

public class StocksHawkApp extends Application {

    public static Context contextOfApplication;
    
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());
        }
        contextOfApplication = getApplicationContext();
    }

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

}

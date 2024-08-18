package com.skilrock.lms.ui;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.skilrock.config.Config;
import com.skilrock.config.MyExceptionHandler;
import com.skilrock.lms.tracker.Analytics;

import java.util.Locale;

public class AfricaApplication extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        Analytics.setEnable(!Config.isDebug);
        String languageToLoad = "en_US"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        if (Config.isWithoutCrashApp)
            Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, MainScreen.class));

//        ActiveAndroid.initialize(getApplicationContext());
        // configure Flurry
//        FlurryAgent.setLogEvents(true);
//        // init Flurry
//        FlurryAgent.init(this, "TCQTTF3S6524Y277ZZSJ");
//        analytics = GoogleAnalytics.getInstance(this);
//        analytics.setLocalDispatchPeriod(1800);
//
//        tracker = analytics.newTracker("UA-55594103-6");
//        //tracker.enableExceptionReporting(true);
//        tracker.enableAdvertisingIdCollection(true);
//        tracker.enableAutoActivityTracking(true);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
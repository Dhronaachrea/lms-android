package com.skilrock.lms.tracker;

import android.content.Context;
import android.os.Build;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.skilrock.lms.ui.BuildConfig;

import java.util.HashMap;
import java.util.Map;

public class Analytics {

    private static boolean enable;
    private Tracker tracker;

    public static void setEnable(boolean enabled) {
        enable = enabled;
        FlurryAgent.setLogEvents(enabled);
    }

    public void startAnalytics(Context context) {
        if (!enable) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            startSession(context);
        else
            initFlurry(context);


        tracker = GoogleAnalytics.getInstance(context).newTracker("UA-55594103-6");
        //tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    private void initFlurry(Context context) {
        if (BuildConfig.FLAVOR.contains("zim"))
            FlurryAgent.init(context, "TCQTTF3S6524Y277ZZSJ");
        else if (BuildConfig.FLAVOR.contains("ghana"))
            FlurryAgent.init(context, "VG93TTD88K799GF4XN3X");
        else if (BuildConfig.FLAVOR.contains("lagos"))
            FlurryAgent.init(context, "NPT8K58K3YKZVBG5CR6M");
    }

    private void startSession(Context context) {
        if (BuildConfig.FLAVOR.contains("zim"))
            FlurryAgent.onStartSession(context, "TCQTTF3S6524Y277ZZSJ");
        else if (BuildConfig.FLAVOR.contains("ghana"))
            FlurryAgent.onStartSession(context, "VG93TTD88K799GF4XN3X");
        else if (BuildConfig.FLAVOR.contains("lagos"))
            FlurryAgent.onStartSession(context, "NPT8K58K3YKZVBG5CR6M");
    }

    public void endAnalytics(Context context) {
        if (enable && Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            FlurryAgent.onEndSession(context);
        }
    }

    public void setScreenName(String screenName) {
        if (!enable) return;
        tracker.setScreenName(screenName);
        FlurryAgent.logEvent(screenName);
    }

    public void sendAll(String category, String action, String label) {
        if (!enable) return;
        if (category == null || action == null || label == null)
            throw new NullPointerException("either category, action and lable must not null");
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
        Map<String, String> map = new HashMap<>();
        map.put("Action", action);
        map.put("Label", label);
        FlurryAgent.logEvent(category, map);
    }

    public void sendAction(String category, String action) {
        if (!enable) return;
        if (category == null || action == null)
            throw new NullPointerException("either category, action must not null");
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
        Map<String, String> map = new HashMap<>();
        map.put("Action", action);
        FlurryAgent.logEvent(category, map);
    }

    public void sendLabel(String category, String label) {
        if (!enable) return;
        if (category == null || label == null)
            throw new NullPointerException("either category, label must not null");
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setLabel(label).build());
        Map<String, String> map = new HashMap<>();
        map.put("Label", label);
        FlurryAgent.logEvent(category, map);
    }
}

package com.skilrock.customui;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnClickListener implements View.OnClickListener {

    private final long minimumInterval;
    private final String KEY = "CLICKED";
    private Map<String, Long> lastClickMap;
    private Map<View, Long> lastClickMap2;
    private boolean canClick = true;

    /**
     * Implement this in your subclass instead of onClick
     *
     * @param v The view that was clicked
     */
    public abstract void onDebouncedClick(View v);


    /**
     * The one and only constructor
     *
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<String, Long>();
        this.lastClickMap2 = new HashMap<>();
    }


    @Override
    public void onClick(View clickedView) {
        Long previousClickTimestamp = lastClickMap.get(KEY);
        Long previousClickTimestamp2 = lastClickMap2.get(clickedView);
        long currentTimestamp = SystemClock.uptimeMillis();

//        lastClickMap2.put(clickedView, currentTimestamp);
//        if ((currentTimestamp - previousClickTimestamp2 <= minimumInterval))
//            return;
        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp > minimumInterval)) {
            lastClickMap.put(KEY, currentTimestamp);
            onDebouncedClick(clickedView);
        }
    }

//    @Override
//    public boolean onTouch(View clickedView, MotionEvent event) {
//        Long previousClickTimestamp = lastClickMap.get(KEY);
//        long currentTimestamp = SystemClock.uptimeMillis();
//
//        lastClickMap.put(KEY, currentTimestamp);
//        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
//            onDebouncedClick(clickedView);
//        }
//        return false;
//    }
}
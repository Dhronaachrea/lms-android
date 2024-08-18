
package com.skilrock.customui;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnTouchListener implements View.OnTouchListener {

    private final long minimumInterval;
    private final String KEY = "CLICKED";
    private Map<String, Long> lastClickMap;
    private boolean canClick = true;

    /**
     * Implement this in your subclass instead of onClick
     *
     * @param v The view that was clicked
     */
    public abstract void onDebouncedClick(View v, MotionEvent event);


    /**
     * The one and only constructor
     *
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnTouchListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<String, Long>();
    }


//    @Override
//    public void onClick(View clickedView) {
//        Long previousClickTimestamp = lastClickMap.get(KEY);
//        long currentTimestamp = SystemClock.uptimeMillis();
//
//        lastClickMap.put(KEY, currentTimestamp);
//        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
//            onDebouncedClick(clickedView);
//        }
//    }

    @Override
    public boolean onTouch(View clickedView, MotionEvent event) {
        Long previousClickTimestamp = lastClickMap.get(KEY);
        long currentTimestamp = SystemClock.uptimeMillis();

        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
            lastClickMap.put(KEY, currentTimestamp);
            onDebouncedClick(clickedView, event);
        }
        return false;
    }
}
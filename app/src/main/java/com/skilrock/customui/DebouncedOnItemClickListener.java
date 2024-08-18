
package com.skilrock.customui;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnItemClickListener implements AdapterView.OnItemClickListener {

    private final long minimumInterval;
    private final String KEY = "CLICKED";
    private Map<String, Long> lastClickMap;
    private boolean canClick = true;

    /**
     * Implement this in your subclass instead of onClick
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    public abstract void onDebouncedItemClick(AdapterView<?> parent, View view, int position, long id);


    /**
     * The one and only constructor
     *
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnItemClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<String, Long>();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long previousClickTimestamp = lastClickMap.get(KEY);
        long currentTimestamp = SystemClock.uptimeMillis();

        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
            lastClickMap.put(KEY, currentTimestamp);
            onDebouncedItemClick(parent, view, position, id);
        }
    }
}
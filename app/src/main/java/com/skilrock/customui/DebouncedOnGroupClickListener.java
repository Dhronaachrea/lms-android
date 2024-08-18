
package com.skilrock.customui;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebouncedOnGroupClickListener implements ExpandableListView.OnGroupClickListener {

    private final long minimumInterval;
    private final String KEY = "CLICKED";
    private Map<String, Long> lastClickMap;
    private boolean canClick = true;

    /**
     * Implement this in your subclass instead of onClick
     *
     * @param parent        The ExpandableListConnector where the click happened
     * @param v             The view within the expandable list/ListView that was clicked
     * @param groupPosition The group position that was clicked
     * @param id            The row id of the group that was clicked
     * @return True if the click was handled
     */
    public abstract void onDebouncedGroupClick(ExpandableListView parent, View v, int groupPosition, long id);


    /**
     * The one and only constructor
     *
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
     */
    public DebouncedOnGroupClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<String, Long>();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Long previousClickTimestamp = lastClickMap.get(KEY);
        long currentTimestamp = SystemClock.uptimeMillis();

        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
            lastClickMap.put(KEY, currentTimestamp);
            onDebouncedGroupClick(parent, v, groupPosition, id);
        }
        return true;
    }
}
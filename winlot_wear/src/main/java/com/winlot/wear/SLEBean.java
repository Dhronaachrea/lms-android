
package com.winlot.wear;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stpl on 9/24/2015.
 */
public class SLEBean {
    private static SLEBean instance = new SLEBean();
    private int[] selected = new int[4];

    private SLEBean() {
    }

    public static SLEBean getInstance() {
        return instance;
    }

    public void setSelected(int fragmentPosition, int selectedPosition) {
        selected[fragmentPosition] = selectedPosition;
    }

    public int[] getSelected() {
        return selected;
    }


    public void clear() {
        for (int i = 0; i < selected.length; i++)
            selected[i] = 0;
    }
}
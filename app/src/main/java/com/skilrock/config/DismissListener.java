package com.skilrock.config;

import com.skilrock.bean.DrawData;

import java.util.ArrayList;

public interface DismissListener {
    void onDismissCustomDiloag(ArrayList<DrawData> newObj, boolean isDrawFreeze);
    void onCancelCustomDiloag();
}

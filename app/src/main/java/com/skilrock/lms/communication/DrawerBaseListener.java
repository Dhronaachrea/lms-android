package com.skilrock.lms.communication;

import android.app.Dialog;

public interface DrawerBaseListener {
    void onListen(String methodType, Object resultData, Dialog dialog);
}

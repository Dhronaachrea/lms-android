package com.skilrock.customui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by stpl on 7/16/2015.
 */
public class CustomProgressDiloag extends ProgressDialog {
    public CustomProgressDiloag(Context context) {
        super(context);
    }

    public CustomProgressDiloag(Context context, int theme) {
        super(context, theme);
    }
}

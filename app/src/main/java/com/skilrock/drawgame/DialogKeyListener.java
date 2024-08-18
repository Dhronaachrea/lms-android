package com.skilrock.drawgame;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by abhishekd on 5/6/2015.
 */
public class DialogKeyListener implements DialogInterface.OnKeyListener {
    private Dialog mDialog;

    public DialogKeyListener(Dialog dialog) {
        this.mDialog = dialog;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            return true;
        }
        return false;
    }
}

package com.skilrock.network;

import android.app.Dialog;
import android.content.Context;

import com.skilrock.escratch.customui.LoadingDialog;

/**
 * Created by stpl on 2/16/2016.
 */
public class DialogRequest extends Request {
    private Dialog dialog;

    public DialogRequest(Context context, String url) {
        super(url);
        dialog = new LoadingDialog(context);
    }

    public void setMessage(String message) {
        ((LoadingDialog)dialog).setMessage(message);
    }

    public Dialog getDialog() {
        return dialog;
    }
}

package com.skilrock.lms.communication;

import android.app.Dialog;

public interface WebServicesListener {
    public void onResult(String methodType, Object resultData, Dialog dialog);
}

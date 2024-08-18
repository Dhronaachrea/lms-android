package com.skilrock.config;

import android.content.Context;
import android.content.Intent;

import com.skilrock.lms.ui.BuildConfig;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by stpl on 2/17/2016.
 */
public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    private final Class<?> myActivityClass;
    private String packageName;

    public MyExceptionHandler(Context context, Class<?> c) {
        myContext = context;
        myActivityClass = c;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        Utils.logPrint(stackTrace + "");// You can use LogCat too
        packageName = BuildConfig.APPLICATION_ID;


        Intent launchIntent = myContext.getPackageManager().getLaunchIntentForPackage(packageName);
        myContext.startActivity(launchIntent);
        //for restarting the Activity
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

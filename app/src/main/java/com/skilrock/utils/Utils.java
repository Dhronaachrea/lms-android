package com.skilrock.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.skilrock.config.Config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    private static Toast toast;

    // for Logs in Android
    public static void logPrint(String msg) {
        if (Config.isDebug)
            Log.d("LOTTO", msg);
    }

    // for Printing with System.out.println();
    public static void consolePrint(String msg) {
        if (Config.isDebug)
            System.out.println("LOTTO:" + msg);
    }

    public static void Toast(Context context, String msg) {
        if (toast == null)
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        logPrint("Toast Starts");
        toast.setText(msg);
        toast.show();
        logPrint("Toast Ends");
    }

    public static void ToastFargi(Context context, String msg) {
        if (Config.isDebug) {
            if (toast != null)
                toast.cancel();
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


}
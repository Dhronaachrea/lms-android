package com.skilrock.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;

import com.skilrock.config.Config;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;

import org.json.JSONArray;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class GlobalVariables {
    public static Double amountWithDrawal;
    public static final String PROJECT_ID = "639180047311";//change zim to lagos
    public String PROJECT_ID_COUNTRY = "";
    //project ids
    // 582408046818//ghana
    // 639180047311//zim
    // 261702618783//lagos

    //flurry anlaytics
    //for zim
//    API Key	TCQTTF3S6524Y277ZZSJ
//    Project ID	704949

    //for ghana
//    API Key	VG93TTD88K799GF4XN3X
//    Project ID	824478

    //for lagos
//    API Key	NPT8K58K3YKZVBG5CR6M
//    Project ID	824486


    private String apiTestGcmKey = "AIzaSyC3yzh9zzoYFJ0-bxvGjyo2TMcZxC5UZKE";
    public static boolean isLive = false;
    public static int startAmin = R.anim.abc_fade_in;
    public static int endAmin = R.anim.abc_fade_out;
    public static String BASE_URL = "http://192.168.132.105:8080/PMS/";
    public static String deviceName = "android";
    public static int selectedPosition = -1;
    public static String deviceType = "phone";
    public static String[] junk = new String[]{"Draw Result",
            "Scratch Games", "Inbox", "Second Chance Of Winning",
            "Change Password", "Locate Rtailer", "SAbout"};
    public static int topHeight;
    public static boolean loadDummyData = false;
    private GlobalPref globalPref;

    public static class GamesData {
        public static String[] subGamesDG = new String[]{"5/90",
                "Lotto Bonus", "Fast Lotto"};
        public static String[] subGamesSL = new String[]{"Super Plus", "Bon",
                "Lucky", "Fast Winlot"};
        public static String[] subGamesES = new String[]{"Lotto", "Bonanzaa",
                "Lucky Lotto", "Fast Number"};
        public static String[] subGamesSC = new String[]{"Plus",
                "Lucky Bonanza", "Fast Lotto"};
        public static String[] subGamesID = new String[]{"Super Winlot",
                "Bon Plus", "Lucky Item", "Fast", "Money"};
        public static String[] gamesDisplayName;
        public static String[] gameBannerURL;
        public static String[] winningnumber;

        public static JSONArray gamesData;
        public static String currentTime;
        public static LinkedHashMap<String, String> gamenameMap;
        public static LinkedHashMap<String, String> gameCodeMap;
        public static LinkedHashMap<String, String> gameDataMap;
        public static String[] gamesDevsArr;
    }

    public String getProjectId(Context context) {
        try {
            if (context == null)
                return PROJECT_ID_COUNTRY;
            globalPref = GlobalPref.getInstance(context);
            if (globalPref.getCountry().equalsIgnoreCase("GHANA")) {
                PROJECT_ID_COUNTRY = "582408046818";
            } else if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                PROJECT_ID_COUNTRY = "261702618783";
            } else if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
                PROJECT_ID_COUNTRY = "639180047311";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PROJECT_ID_COUNTRY;
    }

    public static float dpToPx(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static void showFragment(Fragment fragment, int position,
                                    int layout, FragmentManager fManager) {
        Bundle data = new Bundle();
        data.putInt("position", position);
        fragment.setArguments(data);
        FragmentTransaction ft = fManager.beginTransaction();
        ft.addToBackStack(position + "");
        ft.replace(layout, fragment, position + "");
        ft.commit();
    }

    public static void showFragmentWihoutTag(Fragment fragment, int position, int layout, FragmentManager fManager, int id) {
        Bundle data = new Bundle();
        data.putInt("id", id);
        fragment.setArguments(data);
        FragmentTransaction ft = fManager.beginTransaction();
        ft.addToBackStack(position + "");
        ft.replace(layout, fragment/* , position + "" */);
        ft.commit();

    }

    // public static void replaceFragment(FragmentManager fManager,
    // Fragment fragment, String tag) {
    // FragmentTransaction ft;
    // // if (fManager.findFragmentByTag(tag) == null) {
    // ft = fManager.beginTransaction();
    // ft.addToBackStack(tag);
    // ft.replace(R.id.content_frame, fragment);
    // ft.commit();
    // // } else {
    // // // if (!(fManager.findFragmentByTag(tag).isVisible())) {
    // // ft = fManager.beginTransaction();
    // // ft.addToBackStack(tag);
    // // ft.replace(R.id.content_frame, fragment);
    // // ft.commit();
    // // // }
    // // }
    // }

    // public static void replaceFragmentIfNot(FragmentManager fManager,
    // Fragment fragment, String tag) {
    // FragmentTransaction ft = fManager.beginTransaction();
    // ft.addToBackStack(tag);
    // ft.replace(R.id.content_frame, fragment);
    // ft.commit();
    // }

    public static void fakeLoading(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 0);

    }

    public static void fakeLoadingThis(final Context context,
                                       final Class<?> android) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.dismiss();
                ((Activity) context)
                        .startActivity(new Intent(context, android));
            }
        }, 0);

    }

    public static int getPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int getPx(double dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static boolean onTablet(Context context) {
        int intScreenSize = context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) // LARGE
                || (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1); // Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

    public static int getDensity(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return (new SimpleDateFormat("E, dd MMM yyyy").format(cal.getTime()))
                .toUpperCase(Locale.ENGLISH);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateFromCal(Context context, String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (new SimpleDateFormat("E, dd MMM yyyy", Locale.US).format(calendar.getTime()))
                .toUpperCase(Locale.ENGLISH);
    }

    @SuppressLint("SimpleDateFormat")
    public static String fastFormatDateFromCal(Context context, Calendar cal) {
        return (new SimpleDateFormat("E, dd MMM").format(cal.getTime()))
                .toUpperCase(Locale.ENGLISH);
    }

    public static void showDataAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(context.getResources()
                .getString(R.string.no_ntk));
        alertDialog.setPositiveButton(
                context.getResources().getString(R.string.action_settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).startActivity(new Intent(
                                Settings.ACTION_SETTINGS));
                    }
                });
        alertDialog.setNegativeButton(
                context.getResources().getString(R.string.ok), null);
        alertDialog.show();
    }


    public static void showServerErr(Context context) {
        new DownloadDialogBox(context, context.getResources().getString(
                R.string.net_error), "", false, true, null, null).show();
    }

    public static boolean connectivityExists(Context context) {
        boolean isConnectivity = false;
        ConnectivityManager con_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            loadDummyData = false;
            isConnectivity = true;
        } else {
            if (!Config.isStatic) {
                isConnectivity = false;
                loadDummyData = false;
            } else {
                loadDummyData = true;
                isConnectivity = true;
            }

        }
        return isConnectivity;
    }

    public static float pxToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static double getVersion(Context context) {
        double currentVersion = 1.0;
        try {
            String version = context.getPackageManager().getPackageInfo(
                    context.getApplicationInfo().packageName, 0).versionName;
            currentVersion = Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentVersion;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase(Locale.ENGLISH) : sAddr.substring(0, delim).toUpperCase(Locale.ENGLISH);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }
}
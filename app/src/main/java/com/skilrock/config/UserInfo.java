package com.skilrock.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo {
    private static String USER_PREF = "user_state_pref";
    private static String IS_LOGIN = "is_login";
    private static SharedPreferences preferences;
    private static Editor editor;

    public static void setLogin(Context context) {
        preferences = context.getSharedPreferences(USER_PREF,
                Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public static void setLogout(Context context) {
        preferences = context.getSharedPreferences(USER_PREF,
                Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();

        VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_BAL, "");

        if (Config.isWearer) {
            VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.LOGIN_DATA, "");
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.SESSION_ID, "");
        }

    }

    public static boolean isLogin(Context context) {
        preferences = context.getSharedPreferences(USER_PREF,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(IS_LOGIN, false);
    }
}

package com.skilrock.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

/**
 * Created by stpl on 3/2/2016.
 */
public class UserPref {

    private static UserPref instance;

    private String PREF_NAME = "global_pref";
    private SharedPreferences preferences;


    private UserPref(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static UserPref getInstance(Context context) {
        if (instance == null)
            instance = new UserPref(context);
        return instance;
    }

    private void setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean isAdminLogin() {
        return preferences.getBoolean(Keys.ADMIN.name(), false);
    }

    public void loginAdmin() {
        setBoolean(Keys.ADMIN.name(), true);
    }

    public void logoutAdmin() {
        setBoolean(Keys.ADMIN.name(), false);
    }

    public boolean isUserLogin() {
        return preferences.getBoolean(Keys.USER.name(), false);
    }

    public void loginUser() {
        setBoolean(Keys.USER.name(), true);
    }

    public void logoutUser() {
        setBoolean(Keys.USER.name(), false);
    }

    public void setUserName(String userName) {
        setString(Keys.USER_NAME.name(), userName);
    }

    public String getUserName() {
        return preferences.getString(Keys.USER_NAME.name(), "");
    }

    public void setSessionId(String sessionId) {
        setString(Keys.SESSION_ID.name(), sessionId);
    }

    public String getSessionId() {
        return preferences.getString(Keys.SESSION_ID.name(), "");
    }

    public String getUserNameEntered() {
        return preferences.getString(Keys.ENTER_USER_NAME.name(), "");
    }

    public void setUserBalance(String userBalance) {
        setString(Keys.USER_BAL.name(), userBalance);
    }

    public String getUserBalance() {
        return preferences.getString(Keys.USER_BAL.name(), "");
    }

    public void setUserNameEntered(String userName) {
        setString(Keys.ENTER_USER_NAME.name(), userName);
    }

    public void setPlayerId(String playerId) {
        setString(Keys.PLAYER_ID.name(), playerId);
    }

    public String getPlayerId() {
        return preferences.getString(Keys.PLAYER_ID.name(), "");
    }

    private enum Keys {
        USER_NAME, USER, ADMIN, SESSION_ID, USER_BAL, ENTER_USER_NAME, PLAYER_ID
    }
}

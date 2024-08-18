package com.skilrock.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.skilrock.config.VariableStorage;

/**
 * Created by stpl on 4/14/2016.
 */
public class GlobalPref {

    private static GlobalPref instance;

    private String PREF_NAME = "global_pref";
    private SharedPreferences preferences;


    private GlobalPref(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static GlobalPref getInstance(Context context) {
        if (instance == null)
            instance = new GlobalPref(context);
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

    private enum Keys {
        COUNTRY, BASEURL, isBASEURLFIRST
    }

    public String getCountry() {
        return preferences.getString(Keys.COUNTRY.name(), "");
    }

    public void setCountry(String country) {
        setString(Keys.COUNTRY.name(), country);
    }

    public String getBaseUrl() {
        return preferences.getString(Keys.BASEURL.name(), "");
    }

    public void setBaseUrl(String baseUrl) {
        setString(Keys.BASEURL.name(), baseUrl);
    }

    public boolean getIsBaseUrlFirst() {
        return preferences.getBoolean(Keys.isBASEURLFIRST.name(), false);
    }

    public void setIsBaseUrlFirst(boolean isFirst) {
        setBoolean(Keys.isBASEURLFIRST.name(), isFirst);
    }
}

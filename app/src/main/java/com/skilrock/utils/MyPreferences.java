package com.skilrock.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

	private static final String LOGIN_BANNER = "login_banner";
	private static final String NON_LOGIN_BANNER = "non_login_banner";
	private static final String USER_LATITUDE = "latitude";
	private static final String USER_LONGITUDE = "longitude";
	SharedPreferences sharedPreferences;

	public MyPreferences(Activity context) {
		this.sharedPreferences = context.getPreferences(Context.MODE_PRIVATE);

	}

	public void setLoginBannerData(List<String> data) {

		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(LOGIN_BANNER + "size", data.size());
		for (int i = 0; i < data.size(); i++) {
			editor.putString(LOGIN_BANNER + i, data.get(i));
		}
		editor.commit();

	}

	public List<String> getLoginBannerData() {

		List<String> data = new ArrayList<String>();
		int bannerLength = sharedPreferences.getInt(LOGIN_BANNER + "size", 0);
		for (int i = 0; i < bannerLength; i++) {
			data.add(sharedPreferences.getString(LOGIN_BANNER + i, null));
		}

		return data;
	}

	public void setNonLoginBannerData(List<String> data) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(NON_LOGIN_BANNER + "size", data.size());
		for (int i = 0; i < data.size(); i++) {
			editor.putString(NON_LOGIN_BANNER + i, data.get(i));
		}
		editor.commit();
	}

	public List<String> getNonLoginBannerData() {
		List<String> data = new ArrayList<String>();
		int bannerLength = sharedPreferences.getInt(NON_LOGIN_BANNER + "size",
				0);
		for (int i = 0; i < bannerLength; i++) {
			data.add(sharedPreferences.getString(NON_LOGIN_BANNER + i, null));
		}
		return data;
	}

	public boolean eraseLoginBannerData() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(LOGIN_BANNER + "size", 0);
		editor.commit();
		return true;
	}

	public boolean eraseNonLoginBannerData() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(NON_LOGIN_BANNER + "size", 0);
		editor.commit();
		return true;
	}

	public void setUserLatitude(Double data) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(USER_LATITUDE, data + "");
		editor.commit();

	}

	public Double getUserLatitude() {
		return Double.parseDouble(sharedPreferences.getString(USER_LATITUDE,
				0.0 + ""));
	}

	public void setUserLongitude(Double data) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(USER_LONGITUDE, data + "");
		editor.commit();

	}

	public Double getUserLongitude() {
		return Double.parseDouble(sharedPreferences.getString(USER_LONGITUDE,
				0.0 + ""));
	}

}

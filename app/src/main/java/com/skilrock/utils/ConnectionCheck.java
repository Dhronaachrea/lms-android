package com.skilrock.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionCheck {

	public static boolean isServerConnectionOK(Context context, String url) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL urlData = new URL(url); // for www test.
				HttpURLConnection urlc = (HttpURLConnection) urlData
						.openConnection();
				urlc.setConnectTimeout(5 * 1000); // 5 s.
				urlc.connect();
				// 200 = "OK" code (http connection is fine).
				if (urlc.getResponseCode() == 200) {
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException e1) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}
}

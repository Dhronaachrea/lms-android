package com.skilrock.banner;

import java.io.InputStream;

import org.json.JSONObject;

public class JSONParser {
	public static JSONObject parse(InputStream inStream) {
		JSONObject jsonObj = null;
		String json = null;
		try {
			int ch = -1;
			StringBuffer sb = new StringBuffer();
			while ((ch = inStream.read()) != -1) {
				sb.append((char) ch);
			}
			json = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jsonObj = new JSONObject(json);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return jsonObj;
	}

	public static JSONObject parse(String string) {
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(string);
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
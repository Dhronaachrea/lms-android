package com.skilrock.bean;

import java.io.Serializable;

import org.json.JSONObject;

public class JSONParcelable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;
	private JSONObject jsonParcelable;

	public JSONObject getJsonParcelable() {
		return jsonParcelable;
	}

	public JSONParcelable(JSONObject jsonParcelable) {
		super();
		this.jsonParcelable = jsonParcelable;
	}
}

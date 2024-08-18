package com.skilrock.banner;

import java.io.Serializable;

public class BannerDataBean implements Serializable {

	/**
	 * Abhishek Dubey
	 */
	private static final long serialVersionUID = 1L;

	private String isUpdate;
	private String urlVal;

	public BannerDataBean(String isUpdate, String urlVal) {
		super();
		this.isUpdate = isUpdate;
		this.urlVal = urlVal;
	}

	public String isUpdate() {
		return isUpdate;
	}

	public void setUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getUrlVal() {
		return urlVal;
	}

	public void setUrlVal(String urlVal) {
		this.urlVal = urlVal;
	}

}

package com.skilrock.bean;

public class MPowerBean {

	private int icon;
	private String mode;
	private String desc;

	public MPowerBean(int icon, String mode, String desc) {
		super();
		this.icon = icon;
		this.mode = mode;
		this.desc = desc;
	}

	public int getIcon() {
		return icon;
	}

	public String getMode() {
		return mode;
	}

	public String getDesc() {
		return desc;
	}
}

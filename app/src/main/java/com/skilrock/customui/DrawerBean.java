package com.skilrock.customui;

public class DrawerBean {
	private String name;
	private int icon;
	private int id;

	public String getName() {
		return name;
	}

	public int getIcon() {
		return icon;
	}

	public int getId() {
		return id;
	}

	public DrawerBean(String name, int icon, int id) {
		super();
		this.name = name;
		this.icon = icon;
		this.id = id;
	}
}

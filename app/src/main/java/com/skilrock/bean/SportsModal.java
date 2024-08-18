package com.skilrock.bean;

public class SportsModal {
	private int eventId;
	private String eventName;
	private boolean homeSelected, drawSelected, awaySelected;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public boolean isHomeSelected() {
		return homeSelected;
	}

	public void setHomeSelected(boolean homeSelected) {
		this.homeSelected = homeSelected;
	}

	public boolean isDrawSelected() {
		return drawSelected;
	}

	public void setDrawSelected(boolean drawSelected) {
		this.drawSelected = drawSelected;
	}

	public boolean isAwaySelected() {
		return awaySelected;
	}

	public void setAwaySelected(boolean awaySelected) {
		this.awaySelected = awaySelected;
	}
}

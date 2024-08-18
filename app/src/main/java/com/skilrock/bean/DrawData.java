package com.skilrock.bean;

public class DrawData {
	private String drawId, drawName, drawFreezeTime, drawDateTime;
	private boolean selected;
	private int position;
	public int getPosition() {
		return position;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public DrawData(String drawId, String drawName, String drawDateTime, int position) {
		super();
		this.position = position;
		this.drawId = drawId;
		this.drawName = drawName;
		this.drawDateTime = drawDateTime;
	}
	public DrawData(String drawId, String drawName, String drawFreezeTime, String drawDateTime, int position) {
		super();
		this.position = position;
		this.drawId = drawId;
		this.drawName = drawName;
		this.drawFreezeTime = drawFreezeTime;
		this.drawDateTime = drawDateTime;
	}
	public String getDrawId() {
		return drawId;
	}
	public String getDrawName() {
		return drawName;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public String getDrawDateTime() {
		return drawDateTime;
	}
	@Override
	public String toString() {
		return "DrawData [drawId=" + drawId + ", drawName=" + drawName+ ", drawFreezeTime=" + drawFreezeTime + ", drawDateTime="+ drawDateTime + ", selected=" + selected + "]";
	}
}

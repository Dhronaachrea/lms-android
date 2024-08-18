package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SLEWinnerResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameDevName;
	private String gameDispName;
	private boolean isSuccess;
	private ArrayList<WinningResult> winningResult;

	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	public String getGameDispName() {
		return gameDispName;
	}

	public void setGameDispName(String gameDispName) {
		this.gameDispName = gameDispName;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ArrayList<WinningResult> getWinningResult() {
		return winningResult;
	}

	public void setWinningResult(ArrayList<WinningResult> winningResult) {
		this.winningResult = winningResult;
	}

	public static class WinningResult implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String drawDate;
		private String drawName;
		private String drawTime;
		private ArrayList<EventData> eventData;

		public String getDrawDate() {
			return drawDate;
		}

		public void setDrawDate(String drawDate) {
			this.drawDate = drawDate;
		}

		public String getDrawName() {
			return drawName;
		}

		public void setDrawName(String drawName) {
			this.drawName = drawName;
		}

		public String getDrawTime() {
			return drawTime;
		}

		public void setDrawTime(String drawTime) {
			this.drawTime = drawTime;
		}

		public ArrayList<EventData> getEventData() {
			return eventData;
		}

		public void setEventData(ArrayList<EventData> eventData) {
			this.eventData = eventData;
		}

	}

	public static class EventData implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String eventDisplayAway;
		private String eventDisplayHome;
		private String winningEvent;

		public String getEventDisplayAway() {
			return eventDisplayAway;
		}

		public void setEventDisplayAway(String eventDisplayAway) {
			this.eventDisplayAway = eventDisplayAway;
		}

		public String getEventDisplayHome() {
			return eventDisplayHome;
		}

		public void setEventDisplayHome(String eventDisplayHome) {
			this.eventDisplayHome = eventDisplayHome;
		}

		public String getWinningEvent() {
			return winningEvent;
		}

		public void setWinningEvent(String winningEvent) {
			this.winningEvent = winningEvent;
		}

	}
}

package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SportsBean implements Serializable {
	/**
	 *
	 */

	public String responseMsg;
	public int responseCode;
	public SportsLotteryData sleData;

	private static final long serialVersionUID = 11;

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public SportsLotteryData getSleData() {
		return sleData;
	}

	public void setSleData(SportsLotteryData sleData) {
		this.sleData = sleData;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public static class EventData implements Serializable {
		private boolean homeSelected, drawSelected, awaySelected;

		/*#################--------- New Added --------##############*/

		private boolean minusTwoSelected, minusOneSelected, plusOneSelected, plusTwoSelected;

		/*###########################################################*/

		private int minusTwoId, minusOneId, plusOneId, plusTwoId;

		private int homeId, drawId, awayId;

		public int getHomeId() {
			return homeId;
		}

		public void setHomeId(int homeId) {
			this.homeId = homeId;
		}

		public int getDrawId() {
			return drawId;
		}

		public void setDrawId(int drawId) {
			this.drawId = drawId;
		}

		public int getAwayId() {
			return awayId;
		}

		public void setAwayId(int awayId) {
			this.awayId = awayId;
		}

	/*#################--------- New Added --------##############*/

		public void setMinusTwoId(int minusTwoId) {
			this.minusTwoId = minusTwoId;
		}

		public int getMinusTwoId() {
			return minusTwoId;
		}

		public void setMinusOneId(int minusOneId) {
			this.minusOneId = minusOneId;
		}

		public int getMinusOneId() {
			return minusOneId;
		}

		public void setPlusOneId(int plusOneId) {
			this.plusOneId = plusOneId;
		}

		public int getPlusOneId() {
			return plusOneId;
		}

		public void setPlusTwoId(int plusTwoId) {
			this.plusTwoId = plusTwoId;
		}

		public int getPlusTwoId() {
			return plusTwoId;
		}

		public void setMinusTwoSelected(boolean minusTwoSelected) {
			this.minusTwoSelected = minusTwoSelected;
		}

		public boolean isMinusTwoSelected() {
			return minusTwoSelected;
		}

		public void setMinusOneSelected(boolean minusOneSelected) {
			this.minusOneSelected = minusOneSelected;
		}

		public boolean isMinusOneSelected() {
			return minusOneSelected;
		}

		public void setPlusOneSelected(boolean plusOneSelected) {
			this.plusOneSelected = plusOneSelected;
		}

		public boolean isPlusOneSelected() {
			return plusOneSelected;
		}

		public void setPlusTwoSelected(boolean plusTwoSelected) {
			this.plusTwoSelected = plusTwoSelected;
		}

		public boolean isPlusTwoSelected() {
			return plusTwoSelected;
		}

		/*###########################################################*/

		/**
		 *
		 */
		private static final long serialVersionUID = 12;
		public String eventDisplayHome;
		public String eventDisplayAway;
		public String eventLeague;
		public String eventVenue;
		public String eventDate;
		public int eventId;
		public String favTeam;
		public String homeTeamOdds;
		public String drawOdds;
		public String awayTeamOdds;

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



		public String getEventDisplayHome() {
			return eventDisplayHome;
		}

		public void setEventDisplayHome(String eventDisplayHome) {
			this.eventDisplayHome = eventDisplayHome;
		}

		public String getEventDisplayAway() {
			return eventDisplayAway;
		}

		public void setEventDisplayAway(String eventDisplayAway) {
			this.eventDisplayAway = eventDisplayAway;
		}

		public String getEventLeague() {
			return eventLeague;
		}

		public void setEventLeague(String eventLeague) {
			this.eventLeague = eventLeague;
		}

		public String getEventVenue() {
			return eventVenue;
		}

		public void setEventVenue(String eventVenue) {
			this.eventVenue = eventVenue;
		}

		public String getEventDate() {
			return eventDate;
		}

		public void setEventDate(String eventDate) {
			this.eventDate = eventDate;
		}

		public int getEventId() {
			return eventId;
		}

		public void setEventId(int eventId) {
			this.eventId = eventId;
		}

		public String getFavTeam() {
			return favTeam;
		}

		public void setFavTeam(String favTeam) {
			this.favTeam = favTeam;
		}

		public String getHomeTeamOdds() {
			return homeTeamOdds;
		}

		public void setHomeTeamOdds(String homeTeamOdds) {
			this.homeTeamOdds = homeTeamOdds;
		}

		public String getDrawOdds() {
			return drawOdds;
		}

		public void setDrawOdds(String drawOdds) {
			this.drawOdds = drawOdds;
		}

		public String getAwayTeamOdds() {
			return awayTeamOdds;
		}

		public void setAwayTeamOdds(String awayTeamOdds) {
			this.awayTeamOdds = awayTeamOdds;
		}
	}

	public class DrawData implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 14;

		public int getDrawId() {
			return drawId;
		}

		public void setDrawId(int drawId) {
			this.drawId = drawId;
		}

		public List<EventData> getEventData() {
			return eventData;
		}

		public void setEventData(List<EventData> eventData) {
			this.eventData = eventData;
		}

		public String getDrawDisplayString() {
			return drawDisplayString;
		}

		public void setDrawDisplayString(String drawDisplayString) {
			this.drawDisplayString = drawDisplayString;
		}

		public String getDrawDateTime() {
			return drawDateTime;
		}

		public void setDrawDateTime(String drawDateTime) {
			this.drawDateTime = drawDateTime;
		}

		public int getDrawNumber() {
			return drawNumber;
		}

		public void setDrawNumber(int drawNumber) {
			this.drawNumber = drawNumber;
		}

		public String getFtg() {
			return ftg;
		}

		public void setFtg(String ftg) {
			this.ftg = ftg;
		}

		public int drawId;
		public List<EventData> eventData;
		public String drawDisplayString;
		public String drawDateTime;
		public int drawNumber;
		public String ftg;
	}

	public class GameTypeData implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 15;

		public String getGameTypeDevName() {
			return gameTypeDevName;
		}

		public void setGameTypeDevName(String gameTypeDevName) {
			this.gameTypeDevName = gameTypeDevName;
		}

		public int getGameTypeId() {
			return gameTypeId;
		}

		public void setGameTypeId(int gameTypeId) {
			this.gameTypeId = gameTypeId;
		}

		public List<DrawData> getDrawData() {
			return drawData;
		}

		public void setDrawData(List<DrawData> drawData) {
			this.drawData = drawData;
		}

		public String getEventType() {
			return eventType;
		}

		public void setEventType(String eventType) {
			this.eventType = eventType;
		}

		public String getGameTypeDisplayName() {
			return gameTypeDisplayName;
		}

		public void setGameTypeDisplayName(String gameTypeDisplayName) {
			this.gameTypeDisplayName = gameTypeDisplayName;
		}

		public double getGameTypeMaxBetAmtMultiple() {
			return gameTypeMaxBetAmtMultiple;
		}

		public void setGameTypeMaxBetAmtMultiple(
				double gameTypeMaxBetAmtMultiple) {
			this.gameTypeMaxBetAmtMultiple = gameTypeMaxBetAmtMultiple;
		}

		public double getGameTypeUnitPrice() {
			return gameTypeUnitPrice;
		}

		public void setGameTypeUnitPrice(double gameTypeUnitPrice) {
			this.gameTypeUnitPrice = gameTypeUnitPrice;
		}

		public void setEventSelectionType(String eventSelectionType) {
			this.eventSelectionType = eventSelectionType;
		}

		public String getUpcomingDrawStartTime() {
			return upcomingDrawStartTime;
		}

		public void setUpcomingDrawStartTime(String upcomingDrawStartTime) {
			this.upcomingDrawStartTime = upcomingDrawStartTime;
		}

		public boolean isAreEventsMappedForUpcomingDraw() {
			return areEventsMappedForUpcomingDraw;
		}

		public void setAreEventsMappedForUpcomingDraw(boolean areEventsMappedForUpcomingDraw) {
			this.areEventsMappedForUpcomingDraw = areEventsMappedForUpcomingDraw;
		}

		public String getEventSelectionType() {
			return eventSelectionType;
		}

		public String gameTypeDevName;
		public int gameTypeId;
		public List<DrawData> drawData;
		public String eventSelectionType;
		public String eventType;
		public String upcomingDrawStartTime;
		public boolean areEventsMappedForUpcomingDraw;
		public String gameTypeDisplayName;
		public double gameTypeMaxBetAmtMultiple;
		public double gameTypeUnitPrice;
	}

	public class GameData implements Serializable {
		/**
		 *
		 */
		private static final long serialVersionUID = 16;

		public int getMaxEventCount() {
			return maxEventCount;
		}

		public void setMaxEventCount(int maxEventCount) {
			this.maxEventCount = maxEventCount;
		}

		public String getGameDisplayName() {
			return gameDisplayName;
		}

		public void setGameDisplayName(String gameDisplayName) {
			this.gameDisplayName = gameDisplayName;
		}

		public double getTktMaxAmt() {
			return tktMaxAmt;
		}

		public void setTktMaxAmt(double tktMaxAmt) {
			this.tktMaxAmt = tktMaxAmt;
		}

		public int getGameId() {
			return gameId;
		}

		public void setGameId(int gameId) {
			this.gameId = gameId;
		}

		public int getMinEventCount() {
			return minEventCount;
		}

		public void setMinEventCount(int minEventCount) {
			this.minEventCount = minEventCount;
		}

		public String getGameDevname() {
			return gameDevname;
		}

		public void setGameDevname(String gameDevname) {
			this.gameDevname = gameDevname;
		}

		public int getTktThresholdAmt() {
			return tktThresholdAmt;
		}

		public void setTktThresholdAmt(int tktThresholdAmt) {
			this.tktThresholdAmt = tktThresholdAmt;
		}

		public List<GameTypeData> getGameTypeData() {
			return gameTypeData;
		}

		public void setGameTypeData(List<GameTypeData> gameTypeData) {
			this.gameTypeData = gameTypeData;
		}

		public int maxEventCount;
		public String gameDisplayName;
		public double tktMaxAmt;
		public int gameId;
		public int minEventCount;
		public String gameDevname;
		public int tktThresholdAmt;
		public List<GameTypeData> gameTypeData;
	}

	public class SportsLotteryData implements Serializable {
		/**
		 *
		 */
		public ArrayList<GameData> gameData;
		private static final long serialVersionUID = 17;

		public ArrayList<GameData> getGameData() {
			return gameData;
		}

		public void setGameData(ArrayList<GameData> gameData) {
			this.gameData = gameData;
		}

	}
}
package com.skilrock.sportslottery;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stpl on 7/16/2015.
 */
public class SportsLotteryCheckResultBean implements Serializable {

    private int responseCode;
    private String responseMsg;
    private DrawResultData drawResultData;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public DrawResultData getDrawResultData() {
        return drawResultData;
    }

    public void setDrawResultData(DrawResultData drawResultData) {
        this.drawResultData = drawResultData;
    }

    public static class DrawResultData implements Serializable {
        List<GameData> gameData;

        public List<GameData> getGameData() {
            return gameData;
        }

        public void setGameData(List<GameData> gameData) {
            this.gameData = gameData;
        }
    }

    public static class GameData implements Serializable {

        private String gameDisplayName;
        private int gameId;
        private String gameDevname;
        private List<GameTypeData> gameTypeData;

        public String getGameDisplayName() {
            return gameDisplayName;
        }

        public void setGameDisplayName(String gameDisplayName) {
            this.gameDisplayName = gameDisplayName;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameDevname() {
            return gameDevname;
        }

        public void setGameDevname(String gameDevname) {
            this.gameDevname = gameDevname;
        }

        public List<GameTypeData> getGameTypeData() {
            return gameTypeData;
        }

        public void setGameTypeData(List<GameTypeData> gameTypeData) {
            this.gameTypeData = gameTypeData;
        }
    }

    public static class GameTypeData implements Serializable {

        private String gameTypeDevName;
        private String eventType;
        private String gameTypeDisplayName;
        private int gameTypeId;
        private List<DrawData> drawData;

        public String getGameTypeDevName() {
            return gameTypeDevName;
        }

        public void setGameTypeDevName(String gameTypeDevName) {
            this.gameTypeDevName = gameTypeDevName;
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
    }

    public static class DrawData implements Serializable {

        private int drawId;
        private String drawDisplayString;
        private String drawDateTime;
        private int drawNumber;
        private List<WinningData> winningData;
        private List<EventData> eventData;

        public int getDrawId() {
            return drawId;
        }

        public void setDrawId(int drawId) {
            this.drawId = drawId;
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

        public List<WinningData> getWinningData() {
            return winningData;
        }

        public void setWinningData(List<WinningData> winningData) {
            this.winningData = winningData;
        }

        public List<EventData> getEventData() {
            return eventData;
        }

        public void setEventData(List<EventData> eventData) {
            this.eventData = eventData;
        }
    }

    public static class WinningData implements Serializable {
        private int noOfWinners;
        private double prizeAmount;
        private int noOfMatches;

        public int getNoOfWinners() {
            return noOfWinners;
        }

        public void setNoOfWinners(int noOfWinners) {
            this.noOfWinners = noOfWinners;
        }

        public double getPrizeAmount() {
            return prizeAmount;
        }

        public void setPrizeAmount(double prizeAmount) {
            this.prizeAmount = prizeAmount;
        }

        public int getNoOfMatches() {
            return noOfMatches;
        }

        public void setNoOfMatches(int noOfMatches) {
            this.noOfMatches = noOfMatches;
        }
    }

    public static class EventData implements Serializable {

        private boolean isMinusTwo;
        private boolean isMinusOne;
        private boolean isHome;
        private boolean isDraw;
        private boolean isAway;
        private boolean isPlusOne;
        private boolean isPlusTwo;
        private boolean isCancled;

        private String startTime;
        private String eventLeague;
        private String eventVenue;
        private String eventDiscription;
        private String eventCodeAway;
        private String eventDisplayHome;
        private String eventDisplay;
        private String endTime;
        private String winningOption;
        private String eventDisplayAway;
        private String eventCodeHome;
        private int eventId;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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

        public String getEventDiscription() {
            return eventDiscription;
        }

        public void setEventDiscription(String eventDiscription) {
            this.eventDiscription = eventDiscription;
        }

        public String getEventCodeAway() {
            return eventCodeAway;
        }

        public void setEventCodeAway(String eventCodeAway) {
            this.eventCodeAway = eventCodeAway;
        }

        public String getEventDisplayHome() {
            return eventDisplayHome;
        }

        public void setEventDisplayHome(String eventDisplayHome) {
            this.eventDisplayHome = eventDisplayHome;
        }

        public String getEventDisplay() {
            return eventDisplay;
        }

        public void setEventDisplay(String eventDisplay) {
            this.eventDisplay = eventDisplay;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getWinningOption() {
            return winningOption;
        }

        public void setWinningOption(String winningOption) {
            this.winningOption = winningOption;
        }

        public String getEventDisplayAway() {
            return eventDisplayAway;
        }

        public void setEventDisplayAway(String eventDisplayAway) {
            this.eventDisplayAway = eventDisplayAway;
        }

        public String getEventCodeHome() {
            return eventCodeHome;
        }

        public void setEventCodeHome(String eventCodeHome) {
            this.eventCodeHome = eventCodeHome;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }


        public boolean isMinusTwo() {
            return isMinusTwo;
        }

        public void setIsMinusTwo(boolean isMinusTwo) {
            this.isMinusTwo = isMinusTwo;
        }

        public boolean isMinusOne() {
            return isMinusOne;
        }

        public void setIsMinusOne(boolean isMinusOne) {
            this.isMinusOne = isMinusOne;
        }

        public boolean isHome() {
            return isHome;
        }

        public void setIsHome(boolean isHome) {
            this.isHome = isHome;
        }

        public boolean isDraw() {
            return isDraw;
        }

        public void setIsDraw(boolean isDraw) {
            this.isDraw = isDraw;
        }

        public boolean isAway() {
            return isAway;
        }

        public void setIsAway(boolean isAway) {
            this.isAway = isAway;
        }

        public boolean isPlusOne() {
            return isPlusOne;
        }

        public void setIsPlusOne(boolean isPlusOne) {
            this.isPlusOne = isPlusOne;
        }

        public boolean isPlusTwo() {
            return isPlusTwo;
        }

        public void setIsPlusTwo(boolean isPlusTwo) {
            this.isPlusTwo = isPlusTwo;
        }

        public boolean isCancled() {
            return isCancled;
        }

        public void setIsCancled(boolean isCancled) {
            this.isCancled = isCancled;
        }
    }
}
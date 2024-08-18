package com.skilrock.sportslottery;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stpl on 7/16/2015.
 */
public class SportsLotteryMatchBean implements Serializable{

    private int responseCode;
    private String responseMsg;
    private MatchListData matchListData;

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

    public MatchListData getMatchListData() {
        return matchListData;
    }

    public void setMatchListData(MatchListData matchListData) {
        this.matchListData = matchListData;
    }

    public static class MatchListData implements Serializable{

        private List<GameData> gameData;

        public List<GameData> getGameData() {
            return gameData;
        }

        public void setGameData(List<GameData> gameData) {
            this.gameData = gameData;
        }
    }

    public static class GameData implements Serializable{

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

    public static class GameTypeData implements Serializable{

        private String gameTypeDevName;
        private int gameTypeId;
        private String eventType;
        private String gameTypeDisplayName;
        private List<DrawData> drawData;

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

        public List<DrawData> getDrawData() {
            return drawData;
        }

        public void setDrawData(List<DrawData> drawData) {
            this.drawData = drawData;
        }
    }

    public static class DrawData implements Serializable{

        private int drawId;
        private String drawDisplayString;
        private String drawDateTime;
        private int drawNumber;
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

        public List<EventData> getEventData() {
            return eventData;
        }

        public void setEventData(List<EventData> eventData) {
            this.eventData = eventData;
        }
    }

    public static class EventData implements Serializable{

        private String startTime;
        private String eventLeague;
        private String eventVenue;
        private String eventDiscription;
        private String awayTeamOdds;
        private String eventDisplayHome;
        private String drawOdds;
        private String favTeam;
        private int eventId;
        private String eventDisplay;
        private String endTime;
        private String eventDisplayAway;
        private String homeTeamOdds;

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

        public String getEventDisplayHome() {
            return eventDisplayHome;
        }

        public void setEventDisplayHome(String eventDisplayHome) {
            this.eventDisplayHome = eventDisplayHome;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
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

        public String getEventDisplayAway() {
            return eventDisplayAway;
        }

        public void setEventDisplayAway(String eventDisplayAway) {
            this.eventDisplayAway = eventDisplayAway;
        }

        public String getAwayTeamOdds() {
            return awayTeamOdds;
        }

        public void setAwayTeamOdds(String awayTeamOdds) {
            this.awayTeamOdds = awayTeamOdds;
        }

        public String getDrawOdds() {
            return drawOdds;
        }

        public void setDrawOdds(String drawOdds) {
            this.drawOdds = drawOdds;
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
    }
}

package com.skilrock.sportslottery;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stpl on 7/21/2015.
 */
public class SportsLotteryPurchaseBean {

    private int gameId;
    private int gameTypeId;
    private int noOfBoard;
    private String playerName;
    private String sessionId;
    private String merchantCode;
    private List<DrawInfo> drawInfo;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameTypeId() {
        return gameTypeId;
    }

    public void setGameTypeId(int gameTypeId) {
        this.gameTypeId = gameTypeId;
    }

    public int getNoOfBoard() {
        return noOfBoard;
    }

    public void setNoOfBoard(int noOfBoard) {
        this.noOfBoard = noOfBoard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public List<DrawInfo> getDrawInfo() {
        return drawInfo;
    }

    public void setDrawInfo(List<DrawInfo> drawInfo) {
        this.drawInfo = drawInfo;
    }

    public static class DrawInfo implements Serializable{
        private int drawId;
        private int betAmtMul;
        private List<EventData> eventData;

        public int getDrawId() {
            return drawId;
        }

        public void setDrawId(int drawId) {
            this.drawId = drawId;
        }

        public int getBetAmtMul() {
            return betAmtMul;
        }

        public void setBetAmtMul(int betAmtMul) {
            this.betAmtMul = betAmtMul;
        }

        public List<EventData> getEventData() {
            return eventData;
        }

        public void setEventData(List<EventData> eventData) {
            this.eventData = eventData;
        }
    }

    public static class EventData implements Serializable {
        private int eventId;
        private String eventSelected;

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public String getEventSelected() {
            return eventSelected;
        }

        public void setEventSelected(String eventSelected) {
            this.eventSelected = eventSelected;
        }
    }

}

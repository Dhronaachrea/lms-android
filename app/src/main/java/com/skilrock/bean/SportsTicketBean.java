package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SportsTicketBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 11;

    public String responseMsg;
    public int responseCode;
    public boolean areEventsMappedForUpcomingDraw;
    public String upcomingDrawStartTime;
    public TicketData tktData;

    public TicketData getTktData() {
        return tktData;
    }

    public void setTktData(TicketData tktData) {
        this.tktData = tktData;
    }


    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isAreEventsMappedForUpcomingDraw() {
        return areEventsMappedForUpcomingDraw;
    }

    public void setAreEventsMappedForUpcomingDraw(boolean areEventsMappedForUpcomingDraw) {
        this.areEventsMappedForUpcomingDraw = areEventsMappedForUpcomingDraw;
    }

    public String getUpcomingDrawStartTime() {
        return upcomingDrawStartTime;
    }

    public void setUpcomingDrawStartTime(String upcomingDrawStartTime) {
        this.upcomingDrawStartTime = upcomingDrawStartTime;
    }

    public static class EventData implements Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String eventLeague;
        private String eventVenue;
        private String eventCodeHome;
        private String eventDisplayHome;
        private String eventDate;
        private String eventCodeAway;
        private String eventDisplayAway;
        private String selection;

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

        public void setEventCodeHome(String eventCodeHome) {
            this.eventCodeHome = eventCodeHome;
        }

        public String getEventCodeHome() {
            return eventCodeHome;
        }

        public String getEventDisplayHome() {
            return eventDisplayHome;
        }

        public void setEventDisplayHome(String eventDisplayHome) {
            this.eventDisplayHome = eventDisplayHome;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public void setEventCodeAway(String eventCodeAway) {
            this.eventCodeAway = eventCodeAway;
        }

        public String getEventCodeAway() {
            return eventCodeAway;
        }

        public String getEventDisplayAway() {
            return eventDisplayAway;
        }

        public void setEventDisplayAway(String eventDisplayAway) {
            this.eventDisplayAway = eventDisplayAway;
        }

        public String getSelection() {
            return selection;
        }

        public void setSelection(String selection) {
            this.selection = selection;
        }

    }

    public class TicketData implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        /**
         *
         */
        public String brCd;
        public String balance;
        public int gameTypeId;
        public long ticketNo;
        public String purchaseDate;
        public int gameId;
        public String eventType;
        public String gameTypeName;
        public ArrayList<BoardData> boardData;
        public String trxId;
        public String ticketAmt;
        public String gameName;
        public String purchaseTime;

        public String getBrCd() {
            return brCd;
        }

        public void setBrCd(String brCd) {
            this.brCd = brCd;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
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

        public long getTicketNo() {
            return ticketNo;
        }

        public void setTicketNo(long ticketNo) {
            this.ticketNo = ticketNo;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameTypeName() {
            return gameTypeName;
        }

        public void setGameTypeName(String gameTypeName) {
            this.gameTypeName = gameTypeName;
        }

        public ArrayList<BoardData> getBoardData() {
            return boardData;
        }

        public void setBoardData(ArrayList<BoardData> boardData) {
            this.boardData = boardData;
        }

        public String getTrxId() {
            return trxId;
        }

        public void setTrxId(String trxId) {
            this.trxId = trxId;
        }

        public String getTicketAmt() {
            return ticketAmt;
        }

        public void setTicketAmt(String ticketAmt) {
            this.ticketAmt = ticketAmt;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getPurchaseTime() {
            return purchaseTime;
        }

        public void setPurchaseTime(String purchaseTime) {
            this.purchaseTime = purchaseTime;
        }

    }

    public class BoardData implements Serializable {


        public int drawId;
        public String drawTime;
        public String drawName;
        public float unitPrice;
        public int noOfLines;
        public double winAmt;
        public String winStatus;
        public ArrayList<EventData> eventData;
        public float boardPrice;
        public String drawDate;

        public int getDrawId() {
            return drawId;
        }

        public void setDrawId(int drawId) {
            this.drawId = drawId;
        }

        public String getDrawTime() {
            return drawTime;
        }

        public void setDrawTime(String drawTime) {
            this.drawTime = drawTime;
        }

        public String getDrawName() {
            return drawName;
        }

        public void setDrawName(String drawName) {
            this.drawName = drawName;
        }

        public float getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(float unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getNoOfLines() {
            return noOfLines;
        }

        public void setNoOfLines(int noOfLines) {
            this.noOfLines = noOfLines;
        }

        public double getWinAmt() {
            return winAmt;
        }

        public void setWinAmt(double winAmt) {
            this.winAmt = winAmt;
        }

        public String getWinStatus() {
            return winStatus;
        }

        public void setWinStatus(String winStatus) {
            this.winStatus = winStatus;
        }

        public ArrayList<EventData> getEventData() {
            return eventData;
        }

        public void setEventData(ArrayList<EventData> eventData) {
            this.eventData = eventData;
        }

        public float getBoardPrice() {
            return boardPrice;
        }

        public void setBoardPrice(float boardPrice) {
            this.boardPrice = boardPrice;
        }

        public String getDrawDate() {
            return drawDate;
        }

        public void setDrawDate(String drawDate) {
            this.drawDate = drawDate;
        }

    }
}
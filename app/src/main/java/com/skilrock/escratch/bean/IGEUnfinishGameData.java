package com.skilrock.escratch.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stpl on 7/30/2015.
 */
public class IGEUnfinishGameData {

    private int errorCode;
    private boolean isUnfinishGames;
    private List<UnfinishedGameList> unfinishedGameList;
    private String errorMsg;


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isUnfinishGames() {
        return isUnfinishGames;
    }

    public void setIsUnfinishGames(boolean isUnfinishGames) {
        this.isUnfinishGames = isUnfinishGames;
    }

    public List<UnfinishedGameList> getUnfinishedGameList() {
        return unfinishedGameList;
    }

    public void setUnfinishedGameList(List<UnfinishedGameList> unfinishedGameList) {
        this.unfinishedGameList = unfinishedGameList;
    }

    public static class UnfinishedGameList implements Serializable {
        private int transactionId;
        private double winningAmt;
        private long ticketNbr;
        private String date;
        private GameMaster gameMaster;


        public UnfinishedGameList(int transactionId, double winningAmt, long ticketNbr, String date, GameMaster gameMaster) {
            this.transactionId = transactionId;
            this.winningAmt = winningAmt;
            this.ticketNbr = ticketNbr;
            this.date = date;
            this.gameMaster = gameMaster;
        }

        public UnfinishedGameList(long ticketNbr, String date, GameMaster gameMaster) {
            this.ticketNbr = ticketNbr;
            this.date = date;
            this.gameMaster = gameMaster;
        }

        public int getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(int transactionId) {
            this.transactionId = transactionId;
        }

        public double getWinningAmt() {
            return winningAmt;
        }

        public void setWinningAmt(double winningAmt) {
            this.winningAmt = winningAmt;
        }

        public long getTicketNbr() {
            return ticketNbr;
        }

        public void setTicketNbr(long ticketNbr) {
            this.ticketNbr = ticketNbr;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public GameMaster getGameMaster() {
            return gameMaster;
        }

        public void setGameMaster(GameMaster gameMaster) {
            this.gameMaster = gameMaster;
        }
    }

    public static class GameMaster implements Serializable {

        private int gameNum;
        private int swfHeight;
        private int gameId;
        private String gameImageName;
        private String gameDesc;
        private double gamePrice;
        private String gameName;
        private int swfWidth;

        public GameMaster(int gameNum, int swfHeight, int gameId, String gameImageName, String gameDesc, double gamePrice, String gameName, int swfWidth) {
            this.gameNum = gameNum;
            this.swfHeight = swfHeight;
            this.gameId = gameId;
            this.gameImageName = gameImageName;
            this.gameDesc = gameDesc;
            this.gamePrice = gamePrice;
            this.gameName = gameName;
            this.swfWidth = swfWidth;
        }

        public GameMaster(int gameNum, int gameId, String gameName) {
            this.gameNum = gameNum;
            this.gameId = gameId;
            this.gameName = gameName;
        }


        public double getGamePrice() {
            return gamePrice;
        }

        public void setGamePrice(double gamePrice) {
            this.gamePrice = gamePrice;
        }

        public int getGameNum() {
            return gameNum;
        }

        public void setGameNum(int gameNum) {
            this.gameNum = gameNum;
        }

        public int getSwfHeight() {
            return swfHeight;
        }

        public void setSwfHeight(int swfHeight) {
            this.swfHeight = swfHeight;
        }

        public int getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameImageName() {
            return gameImageName;
        }

        public void setGameImageName(String gameImageName) {
            this.gameImageName = gameImageName;
        }

        public String getGameDesc() {
            return gameDesc;
        }

        public void setGameDesc(String gameDesc) {
            this.gameDesc = gameDesc;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public int getSwfWidth() {
            return swfWidth;
        }

        public void setSwfWidth(int swfWidth) {
            this.swfWidth = swfWidth;
        }
    }
}

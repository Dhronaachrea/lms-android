package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ScratchGameBean implements Serializable {
    public ArrayList<ScratchGameData> getScratchGameData() {
        return scratchGameData;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    private boolean isSuccess;

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String errorMsg;

    public void setScratchGameData(ArrayList<ScratchGameData> scratchGameData) {
        this.scratchGameData = scratchGameData;
    }

    private ArrayList<ScratchGameData> scratchGameData;

    public class ScratchGameData implements Serializable {
        private String gameName;
        private String gameDescription;
        private String gameImagePath;
        private String fullGameImagePath;
        private double ticketPrice;

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public void setGameDescription(String gameDescription) {
            this.gameDescription = gameDescription;
        }

        public void setGameImagePath(String gameImagePath) {
            this.gameImagePath = gameImagePath;
        }

        public void setFullGameImagePath(String fullGameImagePath) {
            this.fullGameImagePath = fullGameImagePath;
        }

        public void setTicketPrice(double ticketPrice) {
            this.ticketPrice = ticketPrice;
        }


        public double getTicketPrice() {
            return ticketPrice;
        }

        public String getGameName() {
            return gameName;
        }

        public String getGameDescription() {
            return gameDescription;
        }

        public String getGameImagePath() {
            return gameImagePath;
        }

        public String getFullGameImagePath() {
            return fullGameImagePath;
        }
    }
}
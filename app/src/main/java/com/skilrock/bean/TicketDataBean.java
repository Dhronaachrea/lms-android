package com.skilrock.bean;

import java.util.ArrayList;
import java.util.List;

public class TicketDataBean {
    private String ticketNumber;
    private double purchaseAmt;
    private ArrayList<DrawData> drawData;
    private ArrayList<PanelData> panelData;
    private String gameName;
    private String purchaseTime;
    private String[] ulNos;
    private String[] blNos;

    public String[] getUlNos() {
        return ulNos;
    }

    public void setUlNos(String[] ulNos) {
        this.ulNos = ulNos;
    }

    public String[] getBlNos() {
        return blNos;
    }

    public void setBlNos(String[] blNos) {
        this.blNos = blNos;
    }

    public TicketDataBean(String ticketNumber, double purchaseAmt, ArrayList<DrawData> drawData, ArrayList<PanelData> panelData, String gameName, String purchaseTime) {
        this.ticketNumber = ticketNumber;
        this.purchaseAmt = purchaseAmt;
        this.drawData = drawData;
        this.panelData = panelData;
        this.gameName = gameName;
        this.purchaseTime = purchaseTime;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public double getPurchaseAmt() {
        return purchaseAmt;
    }

    public void setPurchaseAmt(int purchaseAmt) {
        this.purchaseAmt = purchaseAmt;
    }

    public ArrayList<DrawData> getDrawData() {
        return drawData;
    }

    public void setDrawData(ArrayList<DrawData> drawData) {
        this.drawData = drawData;
    }

    public List<PanelData> getPanelData() {
        return panelData;
    }

    public void setPanelData(ArrayList<PanelData> panelData) {
        this.panelData = panelData;
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

    public class DrawData {
        private String drawId;
        private String drawTime;
        private String drawName;
        private String drawDate;
        private String drawStatus;
        private String drawResult;
        private String ticketWinStatus;
        private String winningAmt;

        public DrawData(String drawId, String drawTime, String drawName, String drawDate) {
            this.drawId = drawId;
            this.drawTime = drawTime;
            this.drawName = drawName;
            this.drawDate = drawDate;
        }

        public DrawData(String drawId, String drawTime, String drawName, String drawDate, String drawStatus, String drawResult, String ticketWinStatus, String winningAmt) {
            this.drawId = drawId;
            this.drawTime = drawTime;
            this.drawName = drawName;
            this.drawDate = drawDate;
            this.drawStatus = drawStatus;
            this.drawResult = drawResult;
            this.ticketWinStatus = ticketWinStatus;
            this.winningAmt = winningAmt;
        }

        public String getDrawId() {
            return drawId;
        }

        public void setDrawId(String drawId) {
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

        public String getDrawDate() {
            return drawDate;
        }

        public void setDrawDate(String drawDate) {
            this.drawDate = drawDate;
        }


    }

    public class PanelData {
        private int betAmtMul;
        private String numberPicked;
        private String pickedNumbers;
        private String betName;
        private String betDispName;
        private double unitPrice;
        private String noPicked;

        public String getBetDispName() {
            return betDispName == null ? betName != null ? betName : "" : betDispName;
        }

        public void setBetDispName(String betDispName) {
            this.betDispName = betDispName;
        }

        public int getBetAmtMul() {
            return betAmtMul;
        }

        public void setBetAmtMul(int betAmtMul) {
            this.betAmtMul = betAmtMul;
        }

        public String getNumberPicked() {
            return numberPicked;
        }

        public void setNumberPicked(String numberPicked) {
            this.numberPicked = numberPicked;
        }

        public String getPickedNumbers() {
            return pickedNumbers;
        }

        public void setPickedNumbers(String pickedNumbers) {
            this.pickedNumbers = pickedNumbers;
        }

        public String getBetName() {
            return betName == null ? "" : betName;
        }

        public void setBetName(String betName) {
            this.betName = betName;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getPanelPrice() {
            return panelPrice;
        }

        public void setPanelPrice(int panelPrice) {
            this.panelPrice = panelPrice;
        }

        public int getNoOfLines() {
            return noOfLines;
        }

        public void setNoOfLines(int noOfLines) {
            this.noOfLines = noOfLines;
        }

        public boolean isQP() {
            return isQP;
        }

        public void setIsQP(boolean isQP) {
            this.isQP = isQP;
        }

        private double panelPrice;
        private int noOfLines;
        private boolean isQP;
    }
}
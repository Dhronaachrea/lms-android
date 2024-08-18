package com.skilrock.bean;

import java.io.Serializable;

/**
 * Created by stpl on 7/22/2015.
 */
public class PurchaseTicketBean implements Serializable {

    private String gameDevName;
    private String gameDispName;
    private String serviceCode;
    private String txnTime;
    private double winAmount;
    private double txnAmount;
    private String txnId;
    private String txnPwt;
    private String ticketNumber;

    public PurchaseTicketBean(String gameDevName, String gameDispName, String serviceCode, String txnTime, double winAmount, double txnAmount, String txnId, String txnPwt, String ticketNumber) {
        this.gameDevName = gameDevName;
        this.gameDispName = gameDispName;
        this.serviceCode = serviceCode;
        this.txnTime = txnTime;
        this.winAmount = winAmount;
        this.ticketNumber = ticketNumber;
        this.txnAmount = txnAmount;
        this.txnId = txnId;
        this.txnPwt = txnPwt;
    }

    public String getGameDevName() {
        return gameDevName;
    }

    public String getGameDispName() {
        return gameDispName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public double getWinAmount() {
        return winAmount;
    }

    public double getTxnAmount() {
        return txnAmount;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setGameDevName(String gameDevName) {
        this.gameDevName = gameDevName;
    }

    public void setGameDispName(String gameDispName) {
        this.gameDispName = gameDispName;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public void setWinAmount(double winAmount) {
        this.winAmount = winAmount;
    }

    public void setTxnAmount(double txnAmount) {
        this.txnAmount = txnAmount;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getTxnPwt() {
        return txnPwt;
    }

    public void setTxnPwt(String txnPwt) {
        this.txnPwt = txnPwt;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}

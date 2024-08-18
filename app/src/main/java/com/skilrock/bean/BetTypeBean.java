
package com.skilrock.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class BetTypeBean {
    private double unitPrice;
    private double minValue;
    private int maxBetAmtMul;
    private String betDisplayName;
    private int betCode;
    private int minNo, maxNo;
    private String[] currentNos;
    private String[] ulNos;
    private String[] blNos;
    private String[] favNos;
    private String[] lastPicked;
    private double maxBetAmtMulUnitPrice;
    private double lowerUnitPrice;

    private String[] ulNosLastPick;
    private String[] blNosLastPick;
    private int minBetAmountMultipleForDc;
    private String betName;

    public double getMaxBetAmtMulUnitPrice() {
        return maxBetAmtMulUnitPrice;
    }

    public double getLowerUnitPrice() {
        return lowerUnitPrice;
    }

    public void setLowerUnitPrice(double lowerUnitPrice) {
        this.lowerUnitPrice = lowerUnitPrice;
    }

    public void setMaxBetAmtAndUnitPrice(double maxBetAmtMulUnitPrice, double lowerUnitPrice) {
        this.maxBetAmtMulUnitPrice = maxBetAmtMulUnitPrice;
        this.lowerUnitPrice = lowerUnitPrice;
    }

    public double getMinValue(String country) {
        if (country.equalsIgnoreCase("ghana"))
            return 0.1;
        else
            return unitPrice;
    }

    public String[] getUlNosLastPick() {
        return ulNosLastPick;
    }

    public void setUlNosLastPick(String[] ulNosLastPick) {
        this.ulNosLastPick = ulNosLastPick;
    }

    public String[] getBlNosLastPick() {
        return blNosLastPick;
    }

    public void setBlNosLastPick(String[] blNosLastPick) {
        this.blNosLastPick = blNosLastPick;
    }

    private HashMap<String, ArrayList<String>> banker;
    private ArrayList<String> bankerNoSelect;

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

    public BetTypeBean(double unitPrice, int maxBetAmtMul,
                       String betDisplayName, int betCode, int minNo, int maxNo,
                       String[] currentNos, String[] favNos, String[] lastPicked) {
        super();
        this.unitPrice = unitPrice;
        this.maxBetAmtMul = maxBetAmtMul;
        this.betDisplayName = betDisplayName;
        this.betCode = betCode;
        this.minNo = minNo;
        this.maxNo = maxNo;
        setCurrentNos(currentNos);
        this.favNos = favNos;
        this.lastPicked = lastPicked;
        ulNos = new String[]{};
        blNos = new String[]{};
    }

    public String getBetDisplayName() {
        return betDisplayName;
    }

    public void setMinNo(int minNo) {
        this.minNo = minNo;
    }

    public void setMaxNo(int maxNo) {
        this.maxNo = maxNo;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getMaxBetAmtMul() {
        return maxBetAmtMul;
    }

    public int getBetCode() {
        return betCode;
    }

    public int getMaxNo() {
        return maxNo;
    }

    public int getMinNo() {
        return minNo;
    }

    public void setMinAndMaxNo(int minNo, int maxNo) {
        this.minNo = minNo;
        this.maxNo = maxNo;
    }

    public synchronized String[] getCurrentNos() {
        return currentNos;
    }

    public String[] getFavNos() {
        return favNos;
    }

    public String[] getLastPicked() {
        return lastPicked;
    }

    public synchronized void setCurrentNos(String[] pickedNo) {
        currentNos = pickedNo;
    }

    public ArrayList<String> getBankerNoSelect() {
        return bankerNoSelect;
    }

    public void setLastPicked(String[] lastPicked) {
        this.lastPicked = lastPicked;
    }

    public void setBankerNoSelect(ArrayList<String> bankerNoSelect) {
        this.bankerNoSelect = bankerNoSelect;

    }

    public HashMap<String, ArrayList<String>> getBanker() {
        return banker;
    }

    public void setBanker(HashMap<String, ArrayList<String>> banker) {
        this.banker = banker;
    }

    public void setDcMinAmt(int minBetAmountMultipleForDc) {
        this.minBetAmountMultipleForDc = minBetAmountMultipleForDc;
    }

    public int getDcMinAmt() {
        return minBetAmountMultipleForDc;
    }

    //for new changes lagos

    public String getBetName() {
        return betName != null ? betName : "";
    }

    public void setBetName(String betName) {
        this.betName = betName;
    }
}
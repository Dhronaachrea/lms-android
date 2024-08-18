package com.skilrock.bean;

import java.io.Serializable;

public class GamePlay implements Serializable {

    private static final long serialVersionUID = 1L;
    private int minValue;
    private int maxValue;
    private int betAmountMultiple;
    private int betName;
    private String betNameBanker;


    public GamePlay(int minValue, int maxValue, int betAmountMultiple,
                    int betName, String betNameBanker) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.betAmountMultiple = betAmountMultiple;
        this.betName = betName;
        this.betNameBanker = betNameBanker;
    }

    public String getBetNameLagos() {
        return betNameBanker;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getBetAmountMultiple() {
        return betAmountMultiple;
    }

    public void setBetAmountMultiple(int betAmountMultiple) {
        this.betAmountMultiple = betAmountMultiple;
    }

    public int getBetName() {
        return betName;
    }

    public void setBetName(int betName) {
        this.betName = betName;
    }
}
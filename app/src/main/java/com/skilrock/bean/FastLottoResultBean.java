package com.skilrock.bean;

import java.io.Serializable;

public class FastLottoResultBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3L;

    private String drawName;
    private String[] ballInfo;
    private String[] winner;
    private String[] hour;

    private String[] time;

    public FastLottoResultBean(String drawName, String[] ballInfo,
                               String[] winner, String[] hour, String[] time) {
        super();
        this.drawName = drawName;
        this.ballInfo = ballInfo;
        this.winner = winner;
        this.hour = hour;
        this.time = time;
    }

    public String getDrawName() {
        return drawName;
    }


    public String[] getBallInfo() {
        return ballInfo;
    }

    public String[] getWinner() {
        return winner;
    }


    public String[] getHour() {
        return hour;
    }

    public String[] getTime() {
        return time;
    }

}

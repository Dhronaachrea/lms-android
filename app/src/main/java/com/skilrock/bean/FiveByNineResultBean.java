package com.skilrock.bean;

import java.io.Serializable;

public class FiveByNineResultBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private String drawName;
    private String drawDate;
    private String drawTime;
    private String[] winningNo;
    private String[] machineNo;
    private String[] match;
    private String[] winner;
    private String[] amount;

    public String getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(String drawTime) {
        this.drawTime = drawTime;
    }


    public FiveByNineResultBean() {
        // TODO Auto-generated constructor stub
    }

    public FiveByNineResultBean(String drawname, String drawDate, String drawTime,
                                String[] winningNo, String[] machineNo, String[] match, String[] winner,
                                String[] amount) {
        super();
        this.drawDate = drawDate;
        this.drawTime = drawTime;
        this.drawName = drawname;
        this.winningNo = winningNo;
        this.machineNo = machineNo;
        this.match = match;
        this.winner = winner;
        this.amount = amount;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public String getDrawName() {
        return drawName;
    }

    public String[] getWinningNo() {
        return winningNo;
    }

    public String[] getMachineNo() {
        return machineNo;
    }

    public String[] getMatch() {
        return match;
    }

    public String[] getWinner() {
        return winner;
    }

    public String[] getAmount() {
        return amount;
    }
}

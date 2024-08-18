package com.skilrock.bean;

import java.io.Serializable;

public class BonusLottoResultBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private String drawName;
    private String drawDate;
    private String drawTime;
    private String[] winningNo;
    private String[] match;
    private String[] winner;
    private String[] amount;

    public String getDrawDate() {
        return drawDate;
    }

    public String getDrawTime() {
        return drawTime;
    }

    public String getBonusNo() {
        return bonusNo;
    }

    public String[] getMachineNo() {
        return machineNo;
    }

    private String bonusNo;
    private String[] machineNo;

    public BonusLottoResultBean(String drawname, String drawDate, String drawTime, String[] winningNo, String bonusNo, String[] machineNo,
                                String match[], String winner[], String amount[]) {
        super();
        this.drawDate = drawDate;
        this.drawTime = drawTime;
        this.drawName = drawname;
        this.winningNo = winningNo;
        this.match = match;
        this.winner = winner;
        this.amount = amount;
        this.bonusNo = bonusNo;
        this.machineNo = machineNo;
    }

    public String getDrawName() {
        return drawName;
    }

    public String[] getWinningNo() {
        return winningNo;
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

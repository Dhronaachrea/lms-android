package com.skilrock.bean;

import android.graphics.Typeface;

/**
 * Created by stpl on 7/9/2015.
 */
public class SPLWinningBean {

    private String match;
    private String winning;
    private String amount;
    private boolean isHeader;

    public SPLWinningBean(String match, String winning, String amount, boolean isHeader) {
        this.match = match;
        this.winning = winning;
        this.amount = amount;
        this.isHeader = isHeader;
    }

    public String getMatch() {
        return match;
    }

    public String getWinning() {
        return winning;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isHeader() {
        return isHeader;
    }
}

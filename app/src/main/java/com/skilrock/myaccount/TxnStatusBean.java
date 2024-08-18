package com.skilrock.myaccount;

import java.util.List;

/**
 * Created by stpl on 7/24/2015.
 */
public class TxnStatusBean {

    private List<String> merchantTxnIdList;
    private String playerId;
    private String playerName;
    private String url;
    private String sessionId;

    private String merchantCode;
    public List<String> getMerchantTxnIdList() {
        return merchantTxnIdList;
    }

    public void setMerchantTxnIdList(List<String> merchantTxnIdList) {
        this.merchantTxnIdList = merchantTxnIdList;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }
}

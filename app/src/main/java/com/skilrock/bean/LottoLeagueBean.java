package com.skilrock.bean;

/**
 * Created by stpl on 3/3/2016.
 */
public class LottoLeagueBean {

    private ResponseData[] responseData;

    private String responseCode;

    private String responseMsg;

    public ResponseData[] getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData[] responseData) {
        this.responseData = responseData;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public class ResponseData {
        private String rank;

        private String playerId;

        private String points;

        private String pseudoName;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getPseudoName() {
            return pseudoName;
        }

        public void setPseudoName(String pseudoName) {
            this.pseudoName = pseudoName;
        }

    }


}

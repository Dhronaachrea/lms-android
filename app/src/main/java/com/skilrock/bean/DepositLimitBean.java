package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stpl on 7/28/2015.
 */
public class DepositLimitBean implements Serializable {

    private int errorCode;
    private String errorMsg;
    private boolean isSuccess;
    private ArrayList<PgRange> pgRange;

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setPgRanges(ArrayList<PgRange> pgRange) {
        this.pgRange = pgRange;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<PgRange> getPgRanges() {
        return pgRange;
    }

    public static class PgRange implements Serializable {

        private double max;
        private double min;
        private String pgCode;
        private double surchargeAmt;

        public double getSurchargeAmt() {
            return surchargeAmt;
        }

        public void setSurchargeAmt(double surchargeAmt) {
            this.surchargeAmt = surchargeAmt;
        }

        public void setMax(double max) {
            this.max = max;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public void setPgCode(String pgCode) {
            this.pgCode = pgCode;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }

        public String getPgCode() {
            return pgCode;
        }
    }

}

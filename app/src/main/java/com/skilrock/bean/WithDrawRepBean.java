package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stpl on 8/14/2015.
 */
public class WithDrawRepBean implements Serializable {


    private boolean isSuccess;
    private ArrayList<WithdrawlRequestData> withdrawlRequestData;

    //new for lagos deposite report
    private ArrayList<DepositData> depositData;
    private String errorMsg;

    //new code lagos
    public ArrayList<DepositData> getDepositData() {
        return depositData;
    }

    public void setDepositData(ArrayList<DepositData> depositData) {
        this.depositData = depositData;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


    public ArrayList<WithdrawlRequestData> getWithdrawlRequestData() {
        return withdrawlRequestData;
    }

    public void setWithdrawlRequestData(ArrayList<WithdrawlRequestData> withdrawlRequestData) {
        this.withdrawlRequestData = withdrawlRequestData;
    }


    public class WithdrawlRequestData implements Serializable {
        private String expiryDate;
        private double payableAmount;
        private String transactionStatus;
        private String transactionTime;
        private String verificationCode;
        private double withdrawalAmount;
        private double withdrawalCharges;
        private String Withdrawlchannel;
        private String paidDate;
        private String cancelDate;
        private String expiredDate;

        public String getExpiredDate() {
            return expiredDate;
        }

        public void setExpiredDate(String expiredDate) {
            this.expiredDate = expiredDate;
        }

        public String getCancelDate() {
            return cancelDate;
        }

        public void setCancelDate(String cancelDate) {
            this.cancelDate = cancelDate;
        }

        public String getPaidDate() {
            return paidDate;
        }

        public void setPaidDate(String paidDate) {
            this.paidDate = paidDate;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public double getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(int payableAmount) {
            this.payableAmount = payableAmount;
        }

        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public String getTransactionTime() {
            return transactionTime;
        }

        public void setTransactionTime(String transactionTime) {
            this.transactionTime = transactionTime;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }

        public double getWithdrawalAmount() {
            return withdrawalAmount;
        }

        public void setWithdrawalAmount(int withdrawalAmount) {
            this.withdrawalAmount = withdrawalAmount;
        }

        public double getWithdrawalCharges() {
            return withdrawalCharges;
        }

        public void setWithdrawalCharges(int withdrawalCharges) {
            this.withdrawalCharges = withdrawalCharges;
        }

        public String getWithdrawlchannel() {
            return Withdrawlchannel;
        }

        public void setWithdrawlchannel(String withdrawlchannel) {
            Withdrawlchannel = withdrawlchannel;
        }
    }

    public class DepositData implements Serializable {
        private String depositAmount;
        private String tellerNbr;
        private String status;
        private String requestId;
        private String bankName;
        private String requestDate;
        private String paymentOption;

        public void setDepositAmount(String depositAmount) {
            this.depositAmount = depositAmount;
        }

        public void setTellerNbr(String tellerNbr) {
            this.tellerNbr = tellerNbr;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public void setPaymentOption(String paymentOption) {
            this.paymentOption = paymentOption;
        }

        public String getDepositAmount() {
            return depositAmount;
        }

        public String getTellerNbr() {
            return tellerNbr;
        }

        public String getStatus() {
            return status;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getBankName() {
            return bankName;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public String getPaymentOption() {
            return paymentOption;
        }
    }
}




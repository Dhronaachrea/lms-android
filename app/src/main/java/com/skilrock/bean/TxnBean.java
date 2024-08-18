package com.skilrock.bean;

import com.skilrock.config.Config;

import java.io.Serializable;
import java.util.ArrayList;

public class TxnBean implements Serializable {
    private double bonusBal;
    private double withdrawlBal;
    private double openingBal;
    private double depositBal;
    private double winningBal;
    private double closingBal;

    public ArrayList<TxnList> getTxnList() {
        return txnList;
    }

    public void setTxnList(ArrayList<TxnList> txnList) {
        this.txnList = txnList;
    }

    public double getBonusBal() {
        return bonusBal;
    }

    public void setBonusBal(double bonusBal) {
        this.bonusBal = bonusBal;
    }

    public double getWithdrawlBal() {
        return withdrawlBal;
    }

    public void setWithdrawlBal(double withdrawlBal) {
        this.withdrawlBal = withdrawlBal;
    }

    public double getDepositBal() {
        return depositBal;
    }

    public void setDepositBal(double depositBal) {
        this.depositBal = depositBal;
    }

    public double getWinningBal() {
        return winningBal;
    }

    public void setWinningBal(double winningBal) {
        this.winningBal = winningBal;
    }

    private boolean isSuccess;
    private String errorMsg;
    private int errorCode;
    private ArrayList<TransactionData> transactionData;
    private ArrayList<TxnList> txnList;

    private String respMsg;

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getErrorMsg() {
        if (Config.isWearer)
            errorMsg = respMsg;
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;

    }

    public double getOpeningBal() {
        return openingBal;
    }

    public double getClosingBal() {
        return closingBal;
    }

    public boolean isSuccess() {
        if (errorCode == 0 && Config.isWearer)
            isSuccess = true;
        return isSuccess;
    }

    public ArrayList<TransactionData> getTransactionData() {
        return transactionData;
    }

    public void setOpeningBal(double openingBal) {
        this.openingBal = openingBal;
    }

    public void setClosingBal(double closingBal) {
        this.closingBal = closingBal;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setTransactionData(ArrayList<TransactionData> transactionData) {
        this.transactionData = transactionData;
    }

    public static class TransactionData implements Serializable {

        private int drawId;
        private String playerBalance;
        private String transactionAmount;
        private String transactionMode;
        private String transactionTime;
        private String transactionType;
        private String transCategory;
        private String txnStatus;

        public TransactionData() {
        }

        public TransactionData(String playerBalance, String transactionAmount, String transactionTime, String transCategory, String transactionType, String openingBalance) {
            this.playerBalance = playerBalance;
            this.transactionAmount = transactionAmount;
            this.transactionTime = transactionTime;
            this.transCategory = transCategory == null ? "" : transCategory;
            this.transactionMode = Double.parseDouble(playerBalance) - Double.parseDouble(openingBalance) > 0 ? "CREDIT" : "DEBIT";
            this.transactionType = transactionType;
        }

        public String getTxnStatus() {
            return txnStatus;
        }

        public void setTxnStatus(String txnStatus) {
            this.txnStatus = txnStatus;
        }

        public int getDrawId() {
            return drawId;
        }

        public String getPlayerBalance() {
            return playerBalance;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public String getTransactionMode() {
            return transactionMode;
        }

        public String getTransactionTime() {
            return transactionTime;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public String getTransCategory() {
            return transCategory;
        }

        public void setDrawId(int drawId) {
            this.drawId = drawId;
        }

        public void setPlayerBalance(String playerBalance) {
            this.playerBalance = playerBalance;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public void setTransactionMode(String transactionMode) {
            this.transactionMode = transactionMode;
        }

        public void setTransactionTime(String transactionTime) {
            this.transactionTime = transactionTime;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public void setTransCategory(String transCategory) {
            this.transCategory = transCategory;
        }
    }

    public class TxnList implements Serializable {
        private String txnType;
        private String balance;
        private int transactionId;
        private int currencyId;
        private String transactionDate;
        private String creditAmount;
        private String txnAmount;
        private String particular;
        private String openingBalance;
        private String subwalletTxn;
        private double withdrawableBalance;
        private String gameGroup;
        private String currency;


        public String getTxnType() {
            return txnType;
        }

        public void setTxnType(String txnType) {
            this.txnType = txnType;
        }

        public int getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(int transactionId) {
            this.transactionId = transactionId;
        }

        public int getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(int currencyId) {
            this.currencyId = currencyId;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCreditAmount() {
            return creditAmount;
        }

        public void setCreditAmount(String creditAmount) {
            this.creditAmount = creditAmount;
        }

        public String getTxnAmount() {
            return txnAmount;
        }

        public void setTxnAmount(String txnAmount) {
            this.txnAmount = txnAmount;
        }

        public void setOpeningBalance(String openingBalance) {
            this.openingBalance = openingBalance;
        }

        public String getParticular() {
            return particular;
        }

        public void setParticular(String particular) {
            this.particular = particular;
        }

        public String getOpeningBalance() {
            return openingBalance;
        }

        public String getSubwalletTxn() {
            return subwalletTxn;
        }

        public void setSubwalletTxn(String subwalletTxn) {
            this.subwalletTxn = subwalletTxn;
        }

        public double getWithdrawableBalance() {
            return withdrawableBalance;
        }

        public void setWithdrawableBalance(double withdrawableBalance) {
            this.withdrawableBalance = withdrawableBalance;
        }

        public String getGameGroup() {
            return gameGroup;
        }

        public void setGameGroup(String gameGroup) {
            this.gameGroup = gameGroup;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

    }
}

package com.skilrock.bean;

/**
 * Created by stpl on 11/30/2015.
 */

public class BankDetailsBean {

    private String bankName;
    private String bankInfo;
    private String bankId;
    private String accountNo;

    public BankDetailsBean(String bankName, String bankInfo, String bankId,
                           String accountNo) {
        super();
        this.bankName = bankName;
        this.bankInfo = bankInfo;
        this.bankId = bankId;
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }


}

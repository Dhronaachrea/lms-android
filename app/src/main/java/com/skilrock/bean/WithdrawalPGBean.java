package com.skilrock.bean;

public class WithdrawalPGBean {
    private String action;
    private String bankName;
    private String pgKey;

    public WithdrawalPGBean(String action, String bankName, String pgKey) {
        super();
        this.action = action;
        this.bankName = bankName;
        this.pgKey = pgKey;
    }

    public String getAction() {
        return action;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPgKey() {
        return pgKey;
    }

}

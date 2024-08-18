package com.skilrock.bean;

/**
 * Created by stpl on 2/15/2016.
 */
public class BankDetailsWithdraw {

    private BankDetails[] bankDetails;

    private String isSuccess;


    //new For BankDetailRegistraion
    private String accNbr;

    private String branchId;

    private Boolean isReg;

    private String accName;

    private String branchName;

    private String bankName;

    private String bankId;

    public String getAccNbr() {
        return accNbr;
    }

    public void setAccNbr(String accNbr) {
        this.accNbr = accNbr;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Boolean getIsReg() {
        return isReg;
    }

    public void setIsReg(Boolean isReg) {
        this.isReg = isReg;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    //

    public BankDetails[] getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails[] bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }


    public class BankDetails {
        private String bankInfo;

        private String actNo;

        private String bankName;

        private String bankId;

        public String getBankInfo() {
            return bankInfo;
        }

        public void setBankInfo(String bankInfo) {
            this.bankInfo = bankInfo;
        }

        public String getActNo() {
            return actNo;
        }

        public void setActNo(String actNo) {
            this.actNo = actNo;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

    }


}

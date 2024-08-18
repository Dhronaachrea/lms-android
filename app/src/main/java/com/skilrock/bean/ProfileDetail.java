package com.skilrock.bean;

import java.io.Serializable;

/**
 * Created by stpl on 7/6/2015.
 */
public class ProfileDetail {

    private boolean isSuccess;
    private PersonalInfo personalInfo;
    private WalletInfo walletInfo;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    private int responseCode;
    private String responseMsg;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    private String errorCode;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    private String errorMsg;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public WalletInfo getWalletInfo() {
        return walletInfo;
    }

    public void setWalletInfo(WalletInfo walletInfo) {
        this.walletInfo = walletInfo;
    }

    public static class PersonalInfo implements Serializable {
        private String lastName;
        private String emailId;
        private String mobileNum;
        private String firstName;
        private String address;
        private String state;
        private String stateCode;
        private String city;
        private String cityCode;
        private String dob;
        private String gender;
        private String profilePhoto;

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getMobileNum() {
            return mobileNum;
        }

        public void setMobileNum(String mobileNum) {
            this.mobileNum = mobileNum;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }
    }

    public static class WalletInfo implements Serializable {
        private String bonusBal;
        private String depositBal;
        private String withdrawlBal;
        private String winningBal;
        private String availableBal;

        public String getBonusBal() {
            return bonusBal;
        }

        public void setBonusBal(String bonusBal) {
            this.bonusBal = bonusBal;
        }

        public String getDepositBal() {
            return depositBal;
        }

        public void setDepositBal(String depositBal) {
            this.depositBal = depositBal;
        }

        public String getWithdrawlBal() {
            return withdrawlBal;
        }

        public void setWithdrawlBal(String withdrawlBal) {
            this.withdrawlBal = withdrawlBal;
        }

        public String getWinningBal() {
            return winningBal;
        }

        public void setWinningBal(String winningBal) {
            this.winningBal = winningBal;
        }

        public String getAvailableBal() {
            return availableBal;
        }

        public void setAvailableBal(String availableBal) {
            this.availableBal = availableBal;
        }
    }

}

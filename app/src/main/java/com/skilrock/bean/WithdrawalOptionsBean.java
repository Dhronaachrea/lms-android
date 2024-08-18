package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by stpl on 10/21/2015.
 */
public class WithdrawalOptionsBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean isSuccess;
    private String errorMsg;
    private int errorCode;
    private ArrayList<ModeList> modeList;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ArrayList<ModeList> getModeList() {
        return modeList;
    }

    public void setModeList(ArrayList<ModeList> modeList) {
        this.modeList = modeList;
    }

    public class ModeList implements Serializable {

        private static final long serialVersionUID = 1L;
        private String modeName;
        private String modeKey;
        private ArrayList<ModeData> modeData;

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }

        public String getModeKey() {
            return modeKey;
        }

        public void setModeKey(String modeKey) {
            this.modeKey = modeKey;
        }

        public ArrayList<ModeData> getModeData() {
            return modeData;
        }

        public void setModeData(ArrayList<ModeData> modeData) {
            this.modeData = modeData;
        }

    }

    public class ModeData implements Serializable {

        private static final long serialVersionUID = 1L;
        private String modeKey;
        private String modeName;
        private ArrayList<ProvidersList> providersList;

        public String getModeKey() {
            return modeKey;
        }

        public void setModeKey(String modeKey) {
            this.modeKey = modeKey;
        }

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }

        public ArrayList<ProvidersList> getProvidersList() {
            return providersList;
        }

        public void setProvidersList(ArrayList<ProvidersList> providersList) {
            this.providersList = providersList;
        }

    }

    public class ProvidersList implements Serializable {

        private static final long serialVersionUID = 1L;
        private String providerCode;
        private String providerName;

        public String getProviderCode() {
            return providerCode;
        }

        public void setProviderCode(String providerCode) {
            this.providerCode = providerCode;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

    }
}

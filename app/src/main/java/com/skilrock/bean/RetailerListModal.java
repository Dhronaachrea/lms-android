package com.skilrock.bean;

import java.io.Serializable;
import java.util.List;

public class RetailerListModal implements Serializable {
    public List<RetailerList> outletList;

    //new values
     public List<String> orgTypes;
     public List<String> orgServices;

    //

    public String errorMsg;
    public boolean isSuccess;
    public String lat, lng;


    //new methods

    public List<String> getOrgServices() {
        return orgServices;
    }


    public void setOrgServices(List<String> orgServices) {
        this.orgServices = orgServices;
    }

    public List<String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(List<String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    //



    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<RetailerList> getRetailerList() {
        return outletList;
    }

    public void setRetailerList(List<RetailerList> retailerList) {
        this.outletList = retailerList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public class RetailerList implements Serializable {
        public String addr_1;
        public String phoneNbr;
        public String lastName;
        public String mobileNbr;
        public String addr_2;
        public String longitude;
        public String latitude;
        public String firstName;
        public String email_id;
        public String type;
        public List<String> service;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getService() {
            return service;
        }

        public void setService(List<String> service) {
            this.service = service;
        }

        public String getAddr_1() {
            return addr_1;
        }

        public void setAddr_1(String addr_1) {
            this.addr_1 = addr_1;
        }

        public String getPhoneNbr() {
            return phoneNbr;
        }

        public void setPhoneNbr(String phoneNbr) {
            this.phoneNbr = phoneNbr;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMobileNbr() {
            return mobileNbr;
        }

        public void setMobileNbr(String mobileNbr) {
            this.mobileNbr = mobileNbr;
        }

        public String getAddr_2() {
            return addr_2;
        }

        public void setAddr_2(String addr_2) {
            this.addr_2 = addr_2;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }
    }
}
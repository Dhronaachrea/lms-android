package com.skilrock.bean;

import java.util.ArrayList;

/**
 * Created by stpl on 9/16/2016.
 */
public class MyRetailerBean {

    private int responseCode;
    private String responseMsg;
    private ArrayList<TicketData> ticketData;

    public String getResponseMsg() {
        return responseMsg;
    }

    public MyRetailerBean(ArrayList<TicketData> ticketData) {
        this.ticketData = ticketData;
    }

    public ArrayList<TicketData> getTicketData() {
        return ticketData;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public class TicketData {
        private String serviceCode;
        private String ticketNumber;


        public String getServiceCode() {
            return serviceCode;
        }

        public String getTicketNumber() {
            return ticketNumber;
        }
    }
}


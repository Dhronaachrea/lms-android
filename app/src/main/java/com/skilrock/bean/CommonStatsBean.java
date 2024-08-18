package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class CommonStatsBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 4L;

    private String statsType;
    private ArrayList<SubData> subDatas;

    public CommonStatsBean() {
        super();

    }

    public CommonStatsBean(String statsType, ArrayList<SubData> subDatas) {
        super();
        this.statsType = statsType;
        this.subDatas = subDatas;
    }

    public ArrayList<SubData> getSubDatas() {
        return subDatas;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getStatsType() {
        return statsType;
    }

    public class SubData implements Serializable {
        private int frequency;
        private long lastSeenSec;
        private int ball;

        public String getLastSeenDisplay() {
            return lastSeenDisplay;
        }

        private String lastSeenDisplay;

        public SubData(int ball, int frequency, long lastSeenSec, String lastSeenDisplay) {
            super();
            this.ball = ball;
            this.frequency = frequency;
            this.lastSeenSec = lastSeenSec;
            this.lastSeenDisplay = lastSeenDisplay;
        }

        public int getBall() {
            return ball;
        }

        public int getFrequency() {
            return frequency;
        }

        public long getLastSeenSec() {
            return lastSeenSec;
        }

    }

}

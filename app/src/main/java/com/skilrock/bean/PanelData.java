package com.skilrock.bean;

public class PanelData {
    private boolean isQp;
    private String noPicked;
    // private int noOfLines;
    private String playType;
    private double betAmtMul;
    private String pickedNumbers;
    private double panelamount;
    private double setSinglePanelAmt;
    private String playTypeName;

    public PanelData() {
    }

    public PanelData(boolean isQp, String noPicked, double panelamount,
                     String playType, double betAmtMul, String pickedNumbers) {
        this.isQp = isQp;
        this.noPicked = noPicked;
        this.panelamount = panelamount;
        this.playType = playType;
        this.betAmtMul = betAmtMul;
        this.pickedNumbers = pickedNumbers;
        // System.out.println(isQp + ", " + noPicked + ", " + playType + ", " +
        // betAmtMul + ", " + pickedNumbers);
    }

    public boolean isQp() {
        return isQp;
    }

    public void setQp(boolean isQp) {
        this.isQp = isQp;
    }

    public String getNoPicked() {
        return noPicked;
    }

    public void setNoPicked(String noPicked) {
        this.noPicked = noPicked;
    }

    // public int getNoOfLines() {
    // return noOfLines;
    // }
    //
    // public void setNoOLines(int noOfLines) {
    // this.noOfLines = noOfLines;
    // }

    public double getPanelamount() {
        return panelamount;
    }

    public void setPanelamount(double panelamount) {
        this.panelamount = panelamount;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }


    public String getPlayTypeName() {
        return playTypeName;
    }

    public void setPlayTypeName(String playTypeName) {
        this.playTypeName = playTypeName;
    }

    public double getBetAmtMul() {
        return betAmtMul;
    }

    public void setBetAmtMul(int betAmtMul) {
        this.betAmtMul = betAmtMul;
    }

    public String getPickedNumbers() {
        return pickedNumbers;
    }

    public void setPickedNumbers(String pickedNumbers) {
        this.pickedNumbers = pickedNumbers;
    }


    public double getSetSinglePanelAmt() {
        return setSinglePanelAmt;
    }

    public void setSetSinglePanelAmt(double setSinglePanelAmt) {
        this.setSinglePanelAmt = setSinglePanelAmt;
    }
}
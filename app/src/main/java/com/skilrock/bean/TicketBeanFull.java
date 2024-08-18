package com.skilrock.bean;

import java.util.ArrayList;

public class TicketBeanFull {
	private CommonSaleData commonSaleData;
	private ArrayList<BetTypeData> betTypeData;

	public CommonSaleData getCommonSaleData() {
		return commonSaleData;
	}

	public void setCommonSaleData(CommonSaleData commonSaleData) {
		this.commonSaleData = commonSaleData;
	}

	public ArrayList<BetTypeData> getBetTypeData() {
		return betTypeData;
	}

	public void setBetTypeData(ArrayList<BetTypeData> betTypeData) {
		this.betTypeData = betTypeData;
	}

	public class DrawData {
		private String drawId;
		private String drawTime;
		private String drawDate;
		private String drawName;
		private String drawStatus;

		public String getDrawId() {
			return drawId;
		}

		public void setDrawId(String drawId) {
			this.drawId = drawId;
		}

		public String getDrawTime() {
			return drawTime;
		}

		public void setDrawTime(String drawTime) {
			this.drawTime = drawTime;
		}

		public String getDrawDate() {
			return drawDate;
		}

		public void setDrawDate(String drawDate) {
			this.drawDate = drawDate;
		}

		public String getDrawName() {
			return drawName;
		}

		public void setDrawName(String drawName) {
			this.drawName = drawName;
		}

		public String getDrawStatus() {
			return drawStatus;
		}

		public void setDrawStatus(String drawStatus) {
			this.drawStatus = drawStatus;
		}
	}

	public class CommonSaleData {
		private String ticketNumber;
		private ArrayList<DrawData> drawData;
		private double purchaseAmt;
		private String gameName;
		private String purchaseTime;
		private String purchaseDate;

		public String getTicketNumber() {
			return ticketNumber;
		}

		public void setTicketNumber(String ticketNumber) {
			this.ticketNumber = ticketNumber;
		}

		public ArrayList<DrawData> getDrawData() {
			return drawData;
		}

		public void setDrawData(ArrayList<DrawData> drawData) {
			this.drawData = drawData;
		}

		public double getPurchaseAmt() {
			return purchaseAmt;
		}

		public void setPurchaseAmt(double purchaseAmt) {
			this.purchaseAmt = purchaseAmt;
		}

		public String getGameName() {
			return gameName;
		}

		public void setGameName(String gameName) {
			this.gameName = gameName;
		}

		public String getPurchaseTime() {
			return purchaseTime;
		}

		public void setPurchaseTime(String purchaseTime) {
			this.purchaseTime = purchaseTime;
		}

		public String getPurchaseDate() {
			return purchaseDate;
		}

		public void setPurchaseDate(String purchaseDate) {
			this.purchaseDate = purchaseDate;
		}
	}

	public class BetTypeData {
		private int betAmtMul;
		private String pickedNumbers;
		private String numberPicked;
		private String betName;
		private double unitPrice;
		private double panelPrice;
		private int noOfLines;
		private boolean isQp;

		public int getBetAmtMul() {
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

		public String getNumberPicked() {
			return numberPicked;
		}

		public void setNumberPicked(String numberPicked) {
			this.numberPicked = numberPicked;
		}

		public String getBetName() {
			return betName;
		}

		public void setBetName(String betName) {
			this.betName = betName;
		}

		public double getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public double getPanelPrice() {
			return panelPrice;
		}

		public void setPanelPrice(double panelPrice) {
			this.panelPrice = panelPrice;
		}

		public int getNoOfLines() {
			return noOfLines;
		}

		public void setNoOfLines(int noOfLines) {
			this.noOfLines = noOfLines;
		}

		public boolean isQp() {
			return isQp;
		}

		public void setQp(boolean isQp) {
			this.isQp = isQp;
		}
	}
}
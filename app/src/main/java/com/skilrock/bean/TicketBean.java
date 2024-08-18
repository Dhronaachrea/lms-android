package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class TicketBean implements Serializable {
	private static final long serialVersionUID = 11L;
	private String ticketNumber;
	private double purchaseAmt;
	private String gameName;
	private String purchaseTime;
	private String purchaseDate;
	private ArrayList<BetData> betDatas;
	private ArrayList<DrawData> drawDatas;

	public TicketBean(String ticketNumber, double purchaseAmt, String gameName,
			String purchaseTime, String purchaseDate) {
		super();
		this.ticketNumber = ticketNumber;
		this.purchaseAmt = purchaseAmt;
		this.gameName = gameName;
		this.purchaseTime = purchaseTime;
		this.purchaseDate = purchaseDate;
		this.betDatas = betDatas;
		this.drawDatas = drawDatas;
	}

	public TicketBean() {

	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public double getPurchaseAmt() {
		return purchaseAmt;
	}

	public String getGameName() {
		return gameName;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public ArrayList<BetData> getBetDatas() {
		return betDatas;
	}

	public ArrayList<DrawData> getDrawDatas() {
		return drawDatas;
	}

	public class BetData implements Serializable {
		private static final long serialVersionUID = 12L;
		private int numberPicked;
		private String pickedNumbers;
		private String betName;
		private double unitPrice;
		private double panelPrice;
		private int noOfLines;
		private boolean isQp;

		public BetData(int betAmtMul, int numberPicked, String pickedNumbers,
				String betName, double unitPrice, double panelPrice,
				int noOfLines, boolean isQp) {
			super();
			this.betAmtMul = betAmtMul;
			this.numberPicked = numberPicked;
			this.pickedNumbers = pickedNumbers;
			this.betName = betName;
			this.unitPrice = unitPrice;
			this.panelPrice = panelPrice;
			this.noOfLines = noOfLines;
			this.isQp = isQp;
		}

		private int betAmtMul;

		public int getBetAmtMul() {
			return betAmtMul;
		}

		public int getNumberPicked() {
			return numberPicked;
		}

		public String getPickedNumbers() {
			return pickedNumbers;
		}

		public String getBetName() {
			return betName;
		}

		public double getUnitPrice() {
			return unitPrice;
		}

		public double getPanelPrice() {
			return panelPrice;
		}

		public int getNoOfLines() {
			return noOfLines;
		}

		public boolean isQp() {
			return isQp;
		}

	}

	public class DrawData implements Serializable {
		private static final long serialVersionUID = 13L;
		private int drawId;
		private String drawTime;
		private String drawName;
		private String drawDate;
		private String drawStatus;

		public DrawData(String drawStatus, int drawId, String drawTime,
				String drawName, String drawDate) {
			super();
			this.drawStatus = drawStatus;
			this.drawId = drawId;
			this.drawTime = drawTime;
			this.drawName = drawName;
			this.drawDate = drawDate;
		}

		public String getDrawStatus() {
			return drawStatus;
		}

		public int getDrawId() {
			return drawId;
		}

		public String getDrawTime() {
			return drawTime;
		}

		public String getDrawName() {
			return drawName;
		}

		public String getDrawDate() {
			return drawDate;
		}

	}
}

package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FortuneBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int noOfDraws;
	private boolean advancePlay;
	private int noOfPanel;
	private int totalPurchaseAmt;
	private boolean isQp;
	private int totalBetAmtMul; // Quick Pick Count
	private ArrayList<FortunePanelData> panelData;

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public boolean getAdvancePlay() {
		return advancePlay;
	}

	public void setAdvancePlay(boolean advancePlay) {
		this.advancePlay = advancePlay;
	}

	public int getNoOfPanel() {
		return noOfPanel;
	}

	public void setNoOfPanel(int noOfPanel) {
		this.noOfPanel = noOfPanel;
	}

	public int getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(int totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public boolean isQp() {
		return isQp;
	}

	public void setQp(boolean isQp) {
		this.isQp = isQp;
	}

	public int getTotalBetAmtMul() {
		return totalBetAmtMul;
	}

	public void setTotalBetAmtMul(int totalBetAmtMul) {
		this.totalBetAmtMul = totalBetAmtMul;
	}

	public ArrayList<FortunePanelData> getPanelData() {
		return panelData;
	}

	public void setPanelData(ArrayList<FortunePanelData> panelData) {
		this.panelData = panelData;
	}

	public class FortunePanelData {
		private int betAmtMul;
		private String pickedNumbers; // Sun Sign

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
	}
}

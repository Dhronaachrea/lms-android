package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class KenoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int noOfDraws;
	private boolean AdvancePlay;
	private int noOfPanel;
	private double totalPurchaseAmt;
	private ArrayList<PanelData> panelData;

	public KenoBean(int noOfDraws, boolean AdvancePlay, int noOfPanel,
			double totalPurchaseAmt, ArrayList<PanelData> panelData) {
		this.noOfDraws = noOfDraws;
		this.AdvancePlay = AdvancePlay;
		this.noOfPanel = noOfPanel;
		this.totalPurchaseAmt = totalPurchaseAmt;
		this.panelData = panelData;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public boolean getAdvancePlay() {
		return AdvancePlay;
	}

	public void setAdvancePlay(boolean AdvancePlay) {
		this.AdvancePlay = AdvancePlay;
	}

	public int getNoOfPanel() {
		return noOfPanel;
	}

	public void setNoOfPanel(int noOfPanel) {
		this.noOfPanel = noOfPanel;
	}

	public double getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public void setTotalPurchaseAmt(int totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public ArrayList<PanelData> getPanelData() {
		return panelData;
	}

	public void setPanelData(ArrayList<PanelData> panelData) {
		this.panelData = panelData;
	}
}

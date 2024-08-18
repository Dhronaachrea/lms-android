package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class DrawGames implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String title;
	public static ArrayList<Game> game;

	public class Game implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String gameId;
		public String title;
		public Banner banner;
		public String winningnumber;
		public ArrayList<Draw> draws;
		public ArrayList<Bet> bets;

		public String getGameId() {
			return gameId;
		}

		public void setGameId(String gameId) {
			this.gameId = gameId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Banner getBanner() {
			return banner;
		}

		public void setBanner(Banner banner) {
			this.banner = banner;
		}

		public String getWinningnumber() {
			return winningnumber;
		}

		public void setWinningnumber(String winningnumber) {
			this.winningnumber = winningnumber;
		}

		public ArrayList<Draw> getDraws() {
			return draws;
		}

		public void setDraws(ArrayList<Draw> draws) {
			this.draws = draws;
		}

		public ArrayList<Bet> getBets() {
			return bets;
		}

		public void setBets(ArrayList<Bet> bets) {
			this.bets = bets;
		}

	}

	public class Banner implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Image image;
		public String action;

		public Image getImage() {
			return image;
		}

		public void setImage(Image image) {
			this.image = image;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

	}

	// public class Draws {
	//
	// ArrayList<Draw> draw;
	//
	// }

	public class Draw implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String title;
		public String drawtime;
		public String freezetime;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDrawtime() {
			return drawtime;
		}

		public void setDrawtime(String drawtime) {
			this.drawtime = drawtime;
		}

		public String getFreezetime() {
			return freezetime;
		}

		public void setFreezetime(String freezetime) {
			this.freezetime = freezetime;
		}

	}

	// public class Bets {
	//
	// ArrayList<Bet> bet;
	//
	// }

	public class Bet implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String title;
		public String minnumber;
		public String maxnumber;
		public String unitpricer;
		public String prefunitprice;
		public String maxbetamount;
		public String lastplayednumber;
		public String favoritenumber;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getMinnumber() {
			return minnumber;
		}

		public void setMinnumber(String minnumber) {
			this.minnumber = minnumber;
		}

		public String getMaxnumber() {
			return maxnumber;
		}

		public void setMaxnumber(String maxnumber) {
			this.maxnumber = maxnumber;
		}

		public String getUnitpricer() {
			return unitpricer;
		}

		public void setUnitpricer(String unitpricer) {
			this.unitpricer = unitpricer;
		}

		public String getPrefunitprice() {
			return prefunitprice;
		}

		public void setPrefunitprice(String prefunitprice) {
			this.prefunitprice = prefunitprice;
		}

		public String getMaxbetamount() {
			return maxbetamount;
		}

		public void setMaxbetamount(String maxbetamount) {
			this.maxbetamount = maxbetamount;
		}

		public String getLastplayednumber() {
			return lastplayednumber;
		}

		public void setLastplayednumber(String lastplayednumber) {
			this.lastplayednumber = lastplayednumber;
		}

		public String getFavoritenumber() {
			return favoritenumber;
		}

		public void setFavoritenumber(String favoritenumber) {
			this.favoritenumber = favoritenumber;
		}

	}

	public class Image implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String update;
		public String url;

		public String getUpdate() {
			return update;
		}

		public void setUpdate(String update) {
			this.update = update;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}

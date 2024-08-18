package com.skilrock.bean;

import java.io.Serializable;

public class GameInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private String gameTitle;
	private String[] gameNames;
	private int gamePos;

	public int getGamePos() {
		return gamePos;
	}

	public GameInfo(String gameTitle, String[] subGames, int gamePos) {
		super();
		this.gameTitle = gameTitle;
		this.gameNames = subGames;
		this.gamePos = gamePos;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public String[] getGameNames() {
		return gameNames;
	}

 }

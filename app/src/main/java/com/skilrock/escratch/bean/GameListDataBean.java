package com.skilrock.escratch.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abhishekd on 5/23/2015.
 */
public class GameListDataBean implements Serializable {

    private int errorCode;
    private String errorMsg;
    private ArrayList<Games> games;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ArrayList<Games> getGames() {
        return games;
    }

    public static class Games implements Serializable {

        private String gameCategory;
        private GameImageLocations gameImageLocations;
        private String gameDescription;
        private String gameName;
        private String isFlash;
        private String isHTML5;
        private String isImageGeneration;
        private String isKeyboard;
        private String gameNumber;
        private int windowHeight;
        private String currencyCode;
        private int windowWidth;
        private double gamePrice;



        public String getGameNumber() {
            return gameNumber;
        }

        public String getGameCategory() {
            return gameCategory;
        }

        public GameImageLocations getGameImageLocations() {
            return gameImageLocations;
        }

        public String getGameDescription() {
            return gameDescription;
        }

        public String getGameName() {
            return gameName;
        }

        public String getIsFlash() {
            return isFlash;
        }

        public String getIsHTML5() {
            return isHTML5;
        }

        public String getIsImageGeneration() {
            return isImageGeneration;
        }

        public String getIsKeyboard() {
            return isKeyboard;
        }

        public int getWindowHeight() {
            return windowHeight;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public int getWindowWidth() {
            return windowWidth;
        }

        public double getGamePrice() {
            return gamePrice;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public void setGamePrice(double gamePrice) {
            this.gamePrice = gamePrice;
        }

        public void setGameNumber(String gameNumber) {
            this.gameNumber = gameNumber;
        }
    }

    public class GameImageLocations implements Serializable {
        private String imagePath;

        public String getImagePath() {
            return imagePath;
        }
    }

}

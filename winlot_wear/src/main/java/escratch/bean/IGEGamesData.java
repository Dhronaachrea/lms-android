package escratch.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class IGEGamesData implements Serializable {
    private double width, height;
    private int noOfPrizeSpriteStrips, noOfSymblSpriteStrips, noSpriteStrips;
    private String scratchMode, spriteImg, spriteMping;
    private PopupInfo popInfo;
    private ScrtchBtnInfo scrtchBtnInfo;
    private ArrayList<BackImgScrtchInfo> backImgScrtchInfos;
    private ArrayList<SpriteInfo> spriteInfos;

    public IGEGamesData(double width, double height, int noOfPrizeSpriteStrips, int noOfSymblSpriteStrips, int noSpriteStrips, String scratchMode, String spriteImg, String spriteMping, PopupInfo popInfo, ArrayList<BackImgScrtchInfo> backImgScrtchInfos, ArrayList<SpriteInfo> spriteInfos, ScrtchBtnInfo scrtchBtnInfo) {
        this.width = width;
        this.height = height;
        this.noOfPrizeSpriteStrips = noOfPrizeSpriteStrips;
        this.noOfSymblSpriteStrips = noOfSymblSpriteStrips;
        this.noSpriteStrips = noSpriteStrips;
        this.scratchMode = scratchMode;
        this.spriteImg = spriteImg;
        this.spriteMping = spriteMping;
        this.popInfo = popInfo;
        this.backImgScrtchInfos = backImgScrtchInfos;
        this.spriteInfos = spriteInfos;
        this.scrtchBtnInfo = scrtchBtnInfo;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getNoOfPrizeSpriteStrips() {
        return noOfPrizeSpriteStrips;
    }

    public int getNoOfSymblSpriteStrips() {
        return noOfSymblSpriteStrips;
    }

    public int getNoSpriteStrips() {
        return noSpriteStrips;
    }

    public String getScratchMode() {
        return scratchMode;
    }

    public String getSpriteImg() {
        return spriteImg;
    }

    public String getSpriteMping() {
        return spriteMping;
    }

    public PopupInfo getPopInfo() {
        return popInfo;
    }

    public ArrayList<BackImgScrtchInfo> getBackImgScrtchInfos() {
        return backImgScrtchInfos;
    }

    public ArrayList<SpriteInfo> getSpriteInfos() {
        return spriteInfos;
    }

    public ScrtchBtnInfo getScrtchBtnInfo() {
        return scrtchBtnInfo;
    }

    public static class PopupInfo implements Serializable {
        private String popUpImg;
        private PopUpPlcingInfo popUpPlcingInfo;
        private PopUpPrzInfo popUpPrzInfo;

        public PopupInfo(String popUpImg, PopUpPlcingInfo popUpPlcingInfo, PopUpPrzInfo popUpPrzInfo) {
            this.popUpImg = popUpImg;
            this.popUpPlcingInfo = popUpPlcingInfo;
            this.popUpPrzInfo = popUpPrzInfo;
        }

        public String getPopUpImg() {
            return popUpImg;
        }

        public PopUpPlcingInfo getPopUpPlcingInfo() {
            return popUpPlcingInfo;
        }

        public PopUpPrzInfo getPopUpPrzInfo() {
            return popUpPrzInfo;
        }
    }

    public static class PopUpPlcingInfo implements Serializable {
        private double x, y, w, h;

        public PopUpPlcingInfo(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getW() {
            return w;
        }

        public double getH() {
            return h;
        }

    }

    public static class PopUpPrzInfo implements Serializable {
        private String fontface;
        private String fontsize;
        private String fontstyle;
        private double h, w, xp, yp;

        public PopUpPrzInfo(String fontface, String fontsize, String fontstyle, double h, double w, double xp, double yp) {
            this.fontface = fontface;
            this.fontsize = fontsize;
            this.fontstyle = fontstyle;
            this.h = h;
            this.w = w;
            this.xp = xp;
            this.yp = yp;
        }

        public String getFontface() {
            return fontface;
        }

        public String getFontsize() {
            return fontsize;
        }

        public String getFontstyle() {
            return fontstyle;
        }

        public double getH() {
            return h;
        }

        public double getW() {
            return w;
        }

        public double getXp() {
            return xp;
        }

        public double getYp() {
            return yp;
        }
    }

    public static class SpriteInfo implements Serializable {
        private int noOfsymbols, w, h;
        private String type;

        public SpriteInfo(int noOfsymbols, int w, int h, String type) {
            this.noOfsymbols = noOfsymbols;
            this.w = w;
            this.h = h;
            this.type = type;
        }

        public int getNoOfsymbols() {
            return noOfsymbols;
        }

        public int getW() {
            return w;
        }

        public int getH() {
            return h;
        }

        public String getType() {
            return type;
        }
    }

    public static class BackImgScrtchInfo implements Serializable {
        private double xb, yb, xt, yt, width, height, wt, ht;
        private String type;

        public BackImgScrtchInfo(double xb, double yb, double xt, double yt, double width, double height, String type, double wt, double ht) {
            this.xb = xb;
            this.yb = yb;
            this.xt = xt;
            this.yt = yt;
            this.width = width;
            this.height = height;
            this.wt = wt;
            this.ht = ht;
            this.type = type;
        }

        public double getXb() {
            return xb;
        }

        public double getYb() {
            return yb;
        }

        public double getXt() {
            return xt;
        }

        public double getYt() {
            return yt;
        }

        public double getHeight() {
            return height;
        }

        public double getWidth() {
            return width;
        }

        public String getType() {
            return type;
        }

        public double getWt() {
            return wt;
        }

        public double getHt() {
            return ht;
        }
    }

    public static class ScrtchBtnInfo {

        double h, w, x, y;

        public ScrtchBtnInfo(double x, double y, double w, double h) {
            this.h = h;
            this.w = w;
            this.x = x;
            this.y = y;
        }

        public double getH() {
            return h;
        }

        public double getW() {
            return w;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}

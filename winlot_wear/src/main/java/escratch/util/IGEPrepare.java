package escratch.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import escratch.bean.IGEGamesData;

//import com.skilrock.escratch.IGECommunication;

/**
 * Created by Abhishek Dubey on 5/15/2015.
 */
public class IGEPrepare {
    public static double heightRatio, widthRatio;
    double deviceHeight, deviceWidth, actionBarHeight;
    private Context mContext;

    public IGEPrepare(Context context) {
        this.mContext = context;
        getDisplayDetails();
    }

    public IGEGamesData getIGEGamesData(JSONObject jsonObject) {
        int width, height, noOfPrizeSpriteStrips, noOfSymblSpriteStrips, noSpriteStrips;
        String scratchMode, spriteImg, spriteMping;
        IGEGamesData.PopupInfo popInfo;
        IGEGamesData.ScrtchBtnInfo scrtchBtnInfo;
        ArrayList<IGEGamesData.BackImgScrtchInfo> backImgScrtchInfos = new ArrayList<>();
        ArrayList<IGEGamesData.SpriteInfo> spriteInfos = new ArrayList<>();
        try {
            width = Integer.parseInt(jsonObject.getString("width"));
            height = Integer.parseInt(jsonObject.getString("height"));
            noOfPrizeSpriteStrips = Integer.parseInt(jsonObject.getString("noOfPrizeSpriteStrips"));
            noOfSymblSpriteStrips = Integer.parseInt(jsonObject.getString("noOfSymblSpriteStrips"));
            noSpriteStrips = Integer.parseInt(jsonObject.getString("noSpriteStrips"));
            scratchMode = jsonObject.getString("scratchMode");
            spriteImg = jsonObject.getString("spriteImg");
            spriteMping = jsonObject.getString("spriteMping");


            widthRatio = deviceWidth / width;
            heightRatio = deviceHeight / height;
            JSONObject jsonPopInfo = jsonObject.getJSONObject("popInfo");
            String popUpImg = jsonPopInfo.getString("popUpImg");
            JSONObject jsonPopUpPrzInfo = jsonPopInfo.getJSONObject("popUpPrzInfo");
            JSONObject jsonPopUpPlcingInfo = jsonPopInfo.getJSONObject("popUpPlcingInfo");
            IGEGamesData.PopUpPlcingInfo popUpPlcingInfo = new IGEGamesData.PopUpPlcingInfo(jsonPopUpPlcingInfo.getDouble("x") * widthRatio, jsonPopUpPlcingInfo.getDouble("y") * heightRatio, jsonPopUpPlcingInfo.getDouble("w") * widthRatio, jsonPopUpPlcingInfo.getDouble("h") * heightRatio);
            IGEGamesData.PopUpPrzInfo popUpPrzInfo =
                    new IGEGamesData.PopUpPrzInfo(jsonPopUpPrzInfo.getString("font_face"), Double.valueOf(jsonPopUpPrzInfo.getInt("font_size") * heightRatio).intValue() + "", jsonPopUpPrzInfo.getString("font_style"), Integer.parseInt(jsonPopUpPrzInfo.getString("h")) * heightRatio, Integer.parseInt(jsonPopUpPrzInfo.getString("w")) * widthRatio, Integer.parseInt(jsonPopUpPrzInfo.getString("xp")) * widthRatio, Integer.parseInt(jsonPopUpPrzInfo.getString("yp")) * heightRatio);
            popInfo = new IGEGamesData.PopupInfo(popUpImg, popUpPlcingInfo, popUpPrzInfo);

            if (spriteMping.equalsIgnoreCase("one-to-one")) {
                JSONArray jsonArraySpriteInfo = jsonObject.getJSONArray("spriteInfo");
                JSONArray jsonArrayBackImgScrtchInfo = jsonObject.getJSONArray("backImgScrtchInfos");

                for (int i = 0; i < jsonArrayBackImgScrtchInfo.length(); i++) {
                    JSONObject object1 = jsonArrayBackImgScrtchInfo.getJSONObject(i);
                    JSONObject object = jsonArraySpriteInfo.getJSONObject(i);
                    //Getting data for Sprite Information
                    int noOfsymbols = Integer.parseInt(object.getString("noOfsymbols"));
                    int w = Integer.parseInt(object.getString("w"));
                    int h = Integer.parseInt(object.getString("h"));
                    String typeSprite = object.getString("type");
                    //Getting data for Back Image Scratch Information
                    int xb = Integer.parseInt(object1.getString("xb"));
                    int yb = Integer.parseInt(object1.getString("yb"));
                    int xt = Integer.parseInt(object1.getString("xt"));
                    int yt = Integer.parseInt(object1.getString("yt"));
                    int wt = Integer.parseInt(object1.getString("xbWidth"));
                    int ht = Integer.parseInt(object1.getString("ybHeight"));
                    String type = object1.getString("type");
                    if (type.equalsIgnoreCase(typeSprite)) {
                        IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = new IGEGamesData.BackImgScrtchInfo(Math.round(xb * widthRatio), Math.round(yb * heightRatio), Math.round(xt * widthRatio), Math.round(yt * heightRatio), Math.round(w * widthRatio), Math.round(h * heightRatio), type, Math.round(wt * widthRatio), Math.round(ht * heightRatio));
                        backImgScrtchInfos.add(backImgScrtchInfo);
                        IGEGamesData.SpriteInfo spriteInfo = new IGEGamesData.SpriteInfo(noOfsymbols, w, h, typeSprite);
                        spriteInfos.add(spriteInfo);
                    } else {
//                        throw new TypeNotMatchException("Back Image Scratch info and correspond sprite info does not match in one-to-one mapping at position " + i + ".");
                    }
                }
            } else {//In case of Random Animation
                JSONArray jsonArraySpriteInfo = jsonObject.getJSONArray("spriteInfo");
                int scratchWidth = 0, scratchHeight = 0, prizeWidth = 0, prizeHeight = 0;
                for (int i = 0; i < jsonArraySpriteInfo.length(); i++) {
                    JSONObject object = jsonArraySpriteInfo.getJSONObject(i);
                    int noOfsymbols = Integer.parseInt(object.getString("noOfsymbols"));
                    int w = Integer.parseInt(object.getString("w"));
                    int h = Integer.parseInt(object.getString("h"));
                    String typeSprite = object.getString("type");
                    if (typeSprite.equalsIgnoreCase("S")) {
                        scratchHeight = h;
                        scratchWidth = w;
                    } else {
                        prizeWidth = w;
                        prizeHeight = h;
                    }
                    IGEGamesData.SpriteInfo spriteInfo = new IGEGamesData.SpriteInfo(noOfsymbols, w, h, typeSprite);
                    spriteInfos.add(spriteInfo);
                }
                JSONArray jsonArrayBackImgScrtchInfo = jsonObject.getJSONArray("backImgScrtchInfos");
                for (int i = 0; i < jsonArrayBackImgScrtchInfo.length(); i++) {
                    JSONObject object1 = jsonArrayBackImgScrtchInfo.getJSONObject(i);
                    int xb = Integer.parseInt(object1.getString("xb"));
                    int yb = Integer.parseInt(object1.getString("yb"));
                    int xt = Integer.parseInt(object1.getString("xt"));
                    int yt = Integer.parseInt(object1.getString("yt"));
                    int wt = Integer.parseInt(object1.getString("xbWidth"));
                    int ht = Integer.parseInt(object1.getString("ybHeight"));
                    String type = object1.getString("type");
                    if (type.equalsIgnoreCase("S")) {
                        IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = new IGEGamesData.BackImgScrtchInfo(Math.round(xb * widthRatio), Math.round(yb * heightRatio), Math.round(xt * widthRatio), Math.round(yt * heightRatio), Math.round(scratchWidth * widthRatio), Math.round(scratchHeight * heightRatio), type, wt, ht);
                        backImgScrtchInfos.add(backImgScrtchInfo);
                    } else {
                        IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = new IGEGamesData.BackImgScrtchInfo(Math.round(xb * widthRatio), Math.round(yb * heightRatio), Math.round(xt * widthRatio), Math.round(yt * heightRatio), Math.round(prizeWidth * widthRatio), Math.round(prizeHeight * heightRatio), type, wt, ht);
                        backImgScrtchInfos.add(backImgScrtchInfo);
                    }
                }
            }
            JSONObject scrtchBtnInfoJSON = jsonObject.getJSONObject("scrtchBtnInfo");
            int y = Integer.parseInt(scrtchBtnInfoJSON.getString("y"));
            int x = Integer.parseInt(scrtchBtnInfoJSON.getString("x"));
            int w = Integer.parseInt(scrtchBtnInfoJSON.getString("w"));
            int h = Integer.parseInt(scrtchBtnInfoJSON.getString("h"));
            scrtchBtnInfo = new IGEGamesData.ScrtchBtnInfo(Math.round(x * widthRatio), Math.round(y * heightRatio), Math.round(w * widthRatio), Math.round(h * heightRatio));
            return new IGEGamesData(width, height, noOfPrizeSpriteStrips, noOfSymblSpriteStrips, noSpriteStrips, scratchMode, spriteImg, spriteMping, popInfo, backImgScrtchInfos, spriteInfos, scrtchBtnInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        TypedValue tv = new TypedValue();
        actionBarHeight = 0;
        deviceHeight = displaymetrics.heightPixels - result - actionBarHeight;
        Log.i("deviceHeight", deviceHeight + "");
        deviceWidth = displaymetrics.widthPixels;
    }

    public Bitmap getResultBitmap(Bitmap bitmap, IGEGamesData.PopUpPrzInfo przInfo, IGEGamesData.PopUpPlcingInfo popUpInfo, double przAmt) {
        Canvas myCanvas = new Canvas(bitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, (int) przInfo.getW(), (int) przInfo.getH());
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(convertPixelsToDp(Float.parseFloat(przInfo.getFontsize()), mContext));
        paint.setTextAlign(Paint.Align.CENTER);
        Bitmap bmp = Bitmap.createBitmap((int) przInfo.getW(), (int) przInfo.getH(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        int width = rect.width();
        int numOfChars = paint.breakText(("$"+ "" + przAmt), true, width, null);
        int start = (("$"+ "" + przAmt).length() - numOfChars) / 2;
        canvas.drawText(("$" + "" + przAmt), start, start + numOfChars, rect.exactCenterX(), rect.exactCenterY(), paint);

        myCanvas.drawBitmap(bmp, (float) przInfo.getXp(), (float) przInfo.getYp(), null);

//        myCanvas.drawText(IGECommunication.CURRENCY_SYMBOL + " " + przAmt, (float) przInfo.getXp(), (float) ((popUpInfo.getH() / 3) - przInfo.getYp()), paint);

        return bitmap;

    }


    public float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

}

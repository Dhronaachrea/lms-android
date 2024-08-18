package escratch.util;

import android.graphics.Bitmap;


import java.util.ArrayList;
import java.util.Random;

import escratch.bean.IGEGamesData;


/**
 * Created by Abhishek Dubey on 5/12/2015.
 * <p/>
 * <p/>
 * This class have constructor having parameter Bitmap(Sprite Image), int(No of animation row available in sprite), ArrayList<ScratchBean>  Array
 * <p/>
 * PrepareSprite Class have two methods
 * getSpriteList();
 * getFirstImages();
 * <p/>
 * getSpriteList() method return type ArrayList<Bitmap[]>
 * getFirstImages() method return type  ArrayList<Bitmap>
 */
public class IGEPrepareSprite {
    private int noOfIcons, noOfPrizeSpriteStrips;
    private ArrayList<IGEGamesData.SpriteInfo> scratchBeans;
    private Bitmap mBitmap;
    private boolean ramdom;

    public IGEPrepareSprite(Bitmap bitmap, int noOfSprites, int noOfPrizeSpriteStrips, boolean random, ArrayList<IGEGamesData.SpriteInfo> scratchBeans) {
        this.mBitmap = bitmap;
        this.noOfIcons = noOfSprites;
        this.ramdom = random;
        this.scratchBeans = scratchBeans;
        this.noOfPrizeSpriteStrips = noOfPrizeSpriteStrips;
    }

    public static ArrayList<Bitmap> getWinningSprite(Bitmap bmp, int noOfStrips, int width, int height) {
        ArrayList<Bitmap> result = new ArrayList<>();
        for (int i = 0; i < noOfStrips; i++) {
            Bitmap bitmap = Bitmap.createBitmap(bmp, 0, (height * i), width, height);
            result.add(bitmap);
        }
        return result;
    }

//    public static int randInt(int min, int max) {
//
//        // NOTE: Usually this should be a field rather than a method
//        // variable so that it is not re-seeded every call.
//        Random rand = new Random();
//
//        // nextInt is normally exclusive of the top value,
//        // so add 1 to make it inclusive
//        int randomNum = rand.nextInt((max - min) + 1) + min;
//
//        return randomNum;
//    }

    public Bitmap[] getBitmapArray(int noOfSymbol, int posY, int width, int height) {
        try {
            ArrayList<Bitmap> result = new ArrayList<>();
            int w = width;
            int h = height;
            int y = posY;
            for (int i = 0; i < noOfSymbol; i++) {
                int x = w * i;
                Bitmap bitmap = Bitmap.createBitmap(mBitmap, x, y, w, h);
                result.add(Bitmap.createScaledBitmap(bitmap, 224, 217, true));
            }
            result.toArray();
            Bitmap[] bmp = new Bitmap[result.size()];
            return result.toArray(bmp);
        }catch (OutOfMemoryError e){
            return null;
        }
    }

    public ArrayList<Bitmap[]> getSpriteList() {
        ArrayList<Bitmap[]> result = new ArrayList<>();
        int posY = 0;
        for (int i = 0; i < scratchBeans.size(); i++) {
            IGEGamesData.SpriteInfo bean = scratchBeans.get(i);
            int noOfSymbol = bean.getNoOfsymbols();
            int w = Double.valueOf(Math.floor(bean.getW())).intValue();
            int h = Double.valueOf(Math.floor(bean.getH())).intValue();
            result.add(getBitmapArray(noOfSymbol, posY, w, h));
            posY += h;
        }
//        if (ramdom) {
//            return getImageArrayFromRandom(result, noOfPrizeSpriteStrips);
//        } else {
            return result;
//        }
    }

    public ArrayList<Bitmap> getFirstImages() {
        try{
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        int posY = 0;
        for (int i = 0; i < scratchBeans.size(); i++) {
            IGEGamesData.SpriteInfo bean = scratchBeans.get(i);
            int width = Double.valueOf(Math.floor(bean.getW())).intValue();
            int height = Double.valueOf(Math.floor(bean.getH())).intValue();
            bitmapArrayList.add(Bitmap.createScaledBitmap(Bitmap.createBitmap(mBitmap, 0, posY, width, height), 224, 217, true));
            posY += Double.valueOf(Math.floor(bean.getH())).intValue();
        }
//        if (ramdom) {
//            return getFirstImageFromRandom(bitmapArrayList, noOfPrizeSpriteStrips);
//        } else {
            return bitmapArrayList;
//        }
        }catch (OutOfMemoryError e){
            return null;
        }
    }

//    private ArrayList<Bitmap[]> getImageArrayFromRandom(ArrayList<Bitmap[]> bitmaps, int noOfPrzStrps) {
//        ArrayList<Bitmap[]> result = new ArrayList<>();
//        for (int i = 0; i < (noOfIcons - 1); i++) {
//            result.add(bitmaps.get(randInt(0, bitmaps.size() - noOfPrzStrps - 1)));
//        }
//        result.add(bitmaps.get(randInt(bitmaps.size() - noOfPrzStrps, bitmaps.size() - 1)));
//        return result;
//    }
//
//    private ArrayList<Bitmap> getFirstImageFromRandom(ArrayList<Bitmap> bitmaps, int noOfPrzStrps) {
//        ArrayList<Bitmap> result = new ArrayList<>();
//        for (int i = 0; i < (noOfIcons - 1); i++) {
//            result.add(bitmaps.get(randInt(0, bitmaps.size() - noOfPrzStrps - 1)));
//        }
//        result.add(bitmaps.get(randInt(bitmaps.size() - noOfPrzStrps, bitmaps.size() - 1)));
//        return result;
//    }
}

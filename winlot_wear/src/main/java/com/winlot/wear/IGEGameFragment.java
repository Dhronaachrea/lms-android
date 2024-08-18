package com.winlot.wear;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;
import com.skilrock.lms.ui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import escratch.bean.IGEGamesData;
import escratch.util.IGEAnimDrawable;
import escratch.util.IGEPrepare;
import escratch.util.IGEPrepareSprite;


public class IGEGameFragment extends Fragment {
    private static final int DURATION = 60;
    private int deviceHeight, deviceWidth, actionBarHeight;
    private boolean isComplete;
    private static String gameMode;
    private double przAmt;


    private ImageView backIV;
    private View scratchImage[];

    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout parentLayout;


    private ArrayList<Bitmap> firstImages;
    private ArrayList<Bitmap> popUpImages;
    private ArrayList<Bitmap[]> animationBitmaps;
    private Bitmap backB;

    private ArrayList<IGEGamesData.BackImgScrtchInfo> backImgScrtchInfos;
    private ArrayList<IGEAnimDrawable> myAnimDrawables;
    private IGEAnimDrawable.IAnimationFinishListener iAnimationFinishListener;
    private IGEPrepare igePrepare;
    private ProgressBar progressBar;
    private IGEGamesData igeGamesData;
    private static boolean enableBackPress;
    private Bitmap myBitMap;

    private static int[] backImages;
    private static int[] backImages1 = new int[]{R.drawable.objact1, R.drawable.objact2, R.drawable.objact3, R.drawable.objact4_720, R.drawable.objact5};
    private static int[] backImages2 = new int[]{R.drawable.objact5, R.drawable.objact2, R.drawable.objact5, R.drawable.objact5, R.drawable.objact4_720};

    private int pos;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ige_activity_wear, null);
        bindViewIds(view);
        backIV = (ImageView) view.findViewById(R.id.back);
        progressBar = (ProgressBar) view.findViewById(R.id.p_bar);
        igePrepare = new IGEPrepare(getActivity());
        getDisplayDetails();
        pos = getArguments().getInt("pos");
        getGameAssets(pos);
        Random random = new Random();

        switch (random.nextInt((2 - 0) + 1) + 0) {
            case 1:
                backImages = backImages1;
                break;
            default:
                backImages = backImages2;
                break;
        }

        return view;
    }

    private void getGameAssets(int pos) {
        try {
            JSONObject jsonObject = new JSONObject(GAME_402);
            igeGamesData = igePrepare.getIGEGamesData(jsonObject);
            backImgScrtchInfos = igeGamesData.getBackImgScrtchInfos();
            new FetchImageFromURLTask().execute(igeGamesData.getPopInfo().getPopUpImg(), "front", pos + "");
        } catch (JSONException e) {

        }
    }

    private void bindViewIds(View view) {
        parentLayout = (RelativeLayout) view.findViewById(R.id.parent_lay);
    }


    private View.OnClickListener getCommonClickListener(final int pos) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IGEGamesData.BackImgScrtchInfo info = backImgScrtchInfos.get(pos);
                doScratch(pos);

            }
        };
        return onClickListener;
    }


    private void doScratch(int pos) {
        if (((IGEAnimDrawable) scratchImage[pos].getTag()) == null) {
            prepareAnim();
            IGEAnimDrawable[] animDrawables = myAnimDrawables.toArray(new IGEAnimDrawable[myAnimDrawables.size()]);
            ((ImageView) scratchImage[pos]).setImageDrawable(animDrawables[pos]);
            scratchImage[pos].setTag(animDrawables[pos]);
            animDrawables[pos].setAnimationFinishListener(iAnimationFinishListener);
            ((Animatable) ((ImageView) scratchImage[pos]).getDrawable()).start();
        }
    }

    private void prepareAnim() {
        myAnimDrawables = new ArrayList<>();
        for (int i = 0; i < animationBitmaps.size(); i++) {
            IGEAnimDrawable animDrawable = new IGEAnimDrawable();
            Bitmap[] bmpData = animationBitmaps.get(i);
            for (int j = 0; j < bmpData.length; j++) {
                animDrawable.addFrame(new BitmapDrawable(getResources(), bmpData[j]), DURATION);
                animDrawable.setOneShot(true);
            }
            myAnimDrawables.add(animDrawable);
        }
        iAnimationFinishListener = new IGEAnimDrawable.IAnimationFinishListener() {
            @Override
            public void onAnimationFinished() {
                if (isAllFinished(scratchImage)) {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), "Completed !", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pos != 4) {
                                InstantGame.pager.setCurrentItem(pos + 1);
                            } else {
                                if (Arrays.equals(backImages, backImages2)) {
                                    Toast.makeText(getActivity(), "You Won", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                                }
                                getActivity().finish();
                            }
                        }
                    }, 500);
                }

            }
        };
    }


//    private void showWinningAnimation() {
//        ParticleSystem ps = new ParticleSystem(mActivity, 100, R.drawable.star1, 800);
//        ps.setScaleRange(0.7f, 1.3f);
//        ps.setSpeedRange(0.1f, 0.25f);
//        ps.setRotationSpeedRange(90, 180);
//        ps.setFadeOut(200, new AccelerateInterpolator());
//        ps.oneShot(scratchImage[scratchImage.length - 1], 70);
//
//        ParticleSystem ps2 = new ParticleSystem(mActivity, 100, R.drawable.star2, 800);
//        ps2.setScaleRange(0.7f, 1.3f);
//        ps2.setSpeedRange(0.1f, 0.25f);
//        ps.setRotationSpeedRange(90, 180);
//        ps2.setFadeOut(200, new AccelerateInterpolator());
//        ps2.oneShot(scratchImage[scratchImage.length - 1], 70);
//    }


    private boolean isAllFinished(View imageView[]) {
        boolean isOkay = false;
        for (int i = 0; i < imageView.length; i++) {
            if ((((IGEAnimDrawable) imageView[i].getTag()) != null) && ((IGEAnimDrawable) imageView[i].getTag()).isFinished()) {
                isOkay = true;
            } else {
                isOkay = false;
                break;
            }
        }
        return isOkay;
    }


    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            if (firstImages != null)
                for (int i = 0; i < firstImages.size(); i++)
                    firstImages.get(i).recycle();
            if (popUpImages != null)
                for (int i = 0; i < popUpImages.size(); i++)
                    popUpImages.get(i).recycle();
            if (animationBitmaps != null)
                for (int i = 0; i < animationBitmaps.size(); i++) {

                    for (int j = 0; j < animationBitmaps.get(i).length; j++) {
                        animationBitmaps.get(i)[j].recycle();
                    }
                }
            if (backB != null)
                backB.recycle();

            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*private View.OnClickListener doScratchManual() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < scratchImage.length; i++) {
                    scratchImageManual[i].setVisibility(View.VISIBLE);
                    scratchImage[i].setVisibility(View.GONE);
                }
                for (int i = 0; i < scratchImageManual.length; i++) {
                    scratchImageManual[i].startAnimation(animFadeOut);
                }
                showResult();
            }
        };

        return clickListener;
    }*/

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        TypedValue tv = new TypedValue();
        actionBarHeight = 0;
        deviceHeight = displaymetrics.heightPixels - result - actionBarHeight;
        Log.i("deviceHeight", deviceHeight + "");
        deviceWidth = displaymetrics.widthPixels;
    }


    private class FetchImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

        private String imgDesc = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.imgDesc = params[1];
            int selectedImageID = 0;
            try {
                if (imgDesc.equalsIgnoreCase("front")) {
                    selectedImageID = R.drawable.merge_image;
                    return BitmapFactory.decodeResource(getResources(), R.drawable.merge_image);
                } else if (imgDesc.equalsIgnoreCase("back")) {
                    selectedImageID = backImages[pos];
                    return BitmapFactory.decodeResource(getResources(), selectedImageID);
                } else {
                    return null;
                }
            } catch (final Exception e) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inSampleSize = 16;
                o.inDither = false;                     //Disable Dithering mode
                o.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
                myBitMap = BitmapFactory.decodeResource(getResources(), selectedImageID, o);
                return myBitMap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPreExecute();
            if (bitmap != null) {
                try {
                    if (imgDesc.equalsIgnoreCase("front")) {
                        IGEPrepareSprite ps = new IGEPrepareSprite(bitmap, backImgScrtchInfos.size(), igeGamesData.getNoOfPrizeSpriteStrips(), true, igeGamesData.getSpriteInfos());
                        firstImages = ps.getFirstImages();
                        animationBitmaps = ps.getSpriteList();

                        new FetchImageFromURLTask().execute("", "back");
                    } else {//In ca successfully download case of Back Image
                        backB = Bitmap.createScaledBitmap(bitmap, 212, 185, true);
                        scratchImage = new ImageView[1];
                        for (int i = 0; i < scratchImage.length; i++) {
                            IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = backImgScrtchInfos.get(i);
                            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//58, 46
                            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                            scratchImage[i] = new ImageView(getActivity());
                            ((ImageView) scratchImage[i]).setImageBitmap(firstImages.get(i));
                            ((ImageView) scratchImage[i]).setScaleType(ImageView.ScaleType.FIT_XY);
                            parentLayout.addView(scratchImage[i], i + 1);
                            if (i == (scratchImage.length - 1))
                                backIV.setImageBitmap(backB);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                                scratchImage[i].setX(25);
//                                scratchImage[i].setY(30);
//                            } else {
//                                layoutParams.setMargins(53, 60, 0, 0);
//                            }
                            scratchImage[i].setLayoutParams(layoutParams);
                            scratchImage[i].setOnClickListener(getCommonClickListener(i));
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error !", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static String GAME_402 = "{\"popInfo\":{\"popUpPrzInfo\":{\"font_style\":\"BOLD\",\"w\":264,\"yp\":324,\"font_size\":55,\"font_face\":\"Comic Sans MS\",\"xp\":191,\"h\":74},\"popUpPlcingInfo\":{\"w\":658,\"h\":1704,\"y\":212,\"x\":23},\"popUpImg\":\"http://182.156.76.142:8280/IGEContent/content/gamePlayContent/402/720x1134/Assets/winPopup.png\"},\"scratchMode\":\"Automated\",\"spriteInfo\":[{\"w\":262,\"type\":\"S\",\"h\":252,\"noOfsymbols\":20}],\"width\":\"720\",\"noSpriteStrips\":1,\"noOfPrizeSpriteStrips\":0,\"backImgScrtchInfos\":[{\"ybHeight\":252,\"yt\":164,\"yb\":167,\"xb\":228,\"type\":\"S\",\"xbWidth\":262,\"xt\":227},{\"ybHeight\":252,\"yt\":335,\"yb\":335,\"xb\":36,\"type\":\"S\",\"xbWidth\":262,\"xt\":36},{\"ybHeight\":252,\"yt\":335,\"yb\":335,\"xb\":417,\"type\":\"S\",\"xbWidth\":262,\"xt\":417},{\"ybHeight\":252,\"yt\":630,\"yb\":630,\"xb\":35,\"type\":\"S\",\"xbWidth\":262,\"xt\":35},{\"ybHeight\":252,\"yt\":630,\"yb\":630,\"xb\":417,\"type\":\"S\",\"xbWidth\":262,\"xt\":417},{\"ybHeight\":252,\"yt\":763,\"yb\":763,\"xb\":229,\"type\":\"S\",\"xbWidth\":262,\"xt\":229},{\"ybHeight\":252,\"yt\":478,\"yb\":478,\"xb\":229,\"type\":\"P\",\"xbWidth\":262,\"xt\":229}],\"height\":\"1134\",\"noOfSymblSpriteStrips\":1,\"spriteImg\":\"http://182.156.76.142:8280/IGEContent/content/gamePlayContent/402/720x1134/Assets/randomAnimated.png\",\"spriteMping\":\"random\",\"errorCode\":0,\"scrtchBtnInfo\":{\"w\":228,\"h\":106,\"y\":1023,\"x\":255}}";

}

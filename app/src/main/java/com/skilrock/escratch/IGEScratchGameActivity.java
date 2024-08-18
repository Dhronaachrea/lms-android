package com.skilrock.escratch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEGamesData;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.escratch.customui.LoadingDialog;
import com.skilrock.escratch.customui.WScratchView;
import com.skilrock.escratch.util.IGEAnimDrawable;
import com.skilrock.escratch.util.IGEPrepare;
import com.skilrock.escratch.util.IGEPrepareSprite;
import com.skilrock.escratch.util.ImageLoader;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.IGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class IGEScratchGameActivity extends Activity implements WebServicesListener, View.OnClickListener {
    private static final int DURATION = 60;
    private int deviceHeight, deviceWidth, actionBarHeight;
    private boolean isComplete;
    private static String gameMode;
    private double przAmt;
    private String ticketNo;
    private boolean isCompleted = false;

    private LinearLayout buttonLayout, winPopLayout, winningLayout;
    private ImageView backIV, winPopupImageView;
    private View scratchImage[];
    private TextView playForCash, playAgain, mainMenu, isMyTicketMessage;
    private View mProgressView;
    private View mGameListView;
    private TextView mLoadingMsg, ticketNumber;
    private ImageView scratchImageManual[];
    private RelativeLayout.LayoutParams layoutParams;
    private RelativeLayout parentLayout;
    private ImageView drawerImage, headerImage;
    private LoadingDialog gameStaringDialog;
    private CustomTextView headerText, subHeaderText;
    private Spinner spinner;

    private ArrayList<Bitmap> firstImages;
    private ArrayList<Bitmap> popUpImages;
    private ArrayList<Bitmap[]> animationBitmaps;
    private Bitmap backB;

    private ArrayList<IGEGamesData.BackImgScrtchInfo> backImgScrtchInfos;
    private ArrayList<IGEAnimDrawable> myAnimDrawables;
    private IGEAnimDrawable.IAnimationFinishListener iAnimationFinishListener;
    private IGEPrepare igePrepare;
    private GameListDataBean.Games games;
    private Animation animFadeOut;
    private IGEGamesData igeGamesData;
    private ImageLoader imageLoader;
    private String gameName = "";
    private static boolean enableBackPress;
    private IGEUnfinishGameData.UnfinishedGameList unfinishedGameList;
    private boolean isMyTickets = false;
    //    private Tracker tracker;
    private Analytics analytics;
    public int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ige_activity_scratch);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.INSTANT_GAME_PLAY);
        bindViewIds();
        enableBackPress = false;
        if (getIntent().getStringExtra("mode").equals("UNFINISH")) {
            gameMode = getIntent().getStringExtra("mode");
            unfinishedGameList = (IGEUnfinishGameData.UnfinishedGameList) getIntent().getSerializableExtra("games");
            subHeaderText.setText(unfinishedGameList.getGameMaster().getGameName());
            ticketNo = String.valueOf(unfinishedGameList.getTicketNbr());
        } else if (getIntent().hasExtra("games")) {
            games = (GameListDataBean.Games) getIntent().getSerializableExtra("games");
            gameMode = getIntent().getStringExtra("mode");
            gameName = games.getGameName();
            subHeaderText.setText(games.getGameName().toUpperCase(Locale.ENGLISH));
        }
        if (getIntent().hasExtra("isMyTickets")) {
            isMyTickets = getIntent().getBooleanExtra("isMyTickets", false);
        }
        headerText.setVisibility(View.VISIBLE);
        drawerImage.setVisibility(View.GONE);
        subHeaderText.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        headerText.setText(getString(R.string.ige_name));
        headerText.setTypeface(null, Typeface.BOLD);

        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        winPopLayout.setVisibility(View.GONE);
        backIV = (ImageView) findViewById(R.id.back);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        igePrepare = new IGEPrepare(this);
        igePrepare.setButtonStyle(playForCash);

        getDisplayDetails();
        if (GlobalVariables.connectivityExists(getApplicationContext())) {
            showProgress(true, "Loading Assets...");
            enableBackPress = false;

            new IGETask(IGEScratchGameActivity.this, "GAME_ASSETS" + gameName, IGEService.prepareGameAssetsURL(IGEScratchGameActivity.this, unfinishedGameList, games), null, "Loading Assets...").execute();
            //           new FetchGameData().execute();
        } else {
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
        } else if (onStartCount == 1) {
            onStartCount++;
        }
    }

    private void bindViewIds() {
        spinner = (Spinner) findViewById(R.id.spinner);
        imageLoader = new ImageLoader(IGEScratchGameActivity.this);
        mProgressView = findViewById(R.id.game_load_progress);
        mLoadingMsg = (TextView) findViewById(R.id.loadingMsg);
        mGameListView = findViewById(R.id.parent_lay);
        playForCash = (TextView) findViewById(R.id.playForCash);

        parentLayout = (RelativeLayout) findViewById(R.id.parent_lay);
        ticketNumber = (TextView) findViewById(R.id.ticketNumber);
        winPopLayout = (LinearLayout) findViewById(R.id.winningPop);
        buttonLayout = (LinearLayout) findViewById(R.id.btn_lay);
        winningLayout = (LinearLayout) findViewById(R.id.winning);
        playAgain = (TextView) findViewById(R.id.playAgain);
        isMyTicketMessage = (TextView) findViewById(R.id.isMyTicketMessage);
        mainMenu = (TextView) findViewById(R.id.mainMenu);
        headerText = (CustomTextView) findViewById(R.id.header_text);
        drawerImage = (ImageView) findViewById(R.id.drawer_image);
        headerImage = (ImageView) findViewById(R.id.header_image);
        subHeaderText = (CustomTextView) findViewById(R.id.header_sub_text);
        playForCash.setOnClickListener(this);
        playAgain.setOnClickListener(this);
        mainMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playForCash:
                onClickBuy("Buy");
                finish();
                break;
            case R.id.playAgain:
                finish();
                break;
            case R.id.mainMenu:
                finish();
                break;
        }
    }

    private View.OnClickListener getCommonClickListener(final int pos) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IGEGamesData.BackImgScrtchInfo info = backImgScrtchInfos.get(pos);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (v.getX() >= info.getXt() && v.getY() >= info.getYt() && v.getX() <= (info.getXt() + info.getWt()) && v.getX() <= (info.getXt() + info.getWt()))
                        doScratch(pos);
                } else
                    doScratch(pos);

            }
        };
        return onClickListener;
    }


    private void doScratch(int pos) {
        try {
            if (((IGEAnimDrawable) scratchImage[pos].getTag()) == null) {
                prepareAnim();
                IGEAnimDrawable[] animDrawables = myAnimDrawables.toArray(new IGEAnimDrawable[myAnimDrawables.size()]);
                ((ImageView) scratchImage[pos]).setImageDrawable(animDrawables[pos]);
                scratchImage[pos].setTag(animDrawables[pos]);
                animDrawables[pos].setAnimationFinishListener(iAnimationFinishListener);
                ((Animatable) ((ImageView) scratchImage[pos]).getDrawable()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
        }
    }

    private void prepareAnim() {
        myAnimDrawables = new ArrayList<>();
        try {
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

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                try {
                                    if (!isCompleted)
                                        showResult();
                                } catch (RuntimeException e) {
                                }

                            }
                        }, 1000);
//                    showResult();
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
        }
    }

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


    @Override
    public void onBackPressed() {
        if (enableBackPress) {
            if (gameStaringDialog != null && gameStaringDialog.isShowing())
                gameStaringDialog.dismiss();
            IGEScratchGameActivity.this.finish();
        } else
            return;
    }


    /**
     * Shows the progress UI and hides the game list page.
     */
    public void showProgress(final boolean show, String msg) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mGameListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mGameListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mGameListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingMsg.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingMsg.setText(msg);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoadingMsg.setVisibility(show ? View.VISIBLE : View.GONE);
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingMsg.setText(msg);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingMsg.setVisibility(show ? View.VISIBLE : View.GONE);
            mGameListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean connectivityExists() {
        ConnectivityManager con_manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showProgress(false);
//    }


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
    protected void onDestroy() {
        try {
            for (int i = 0; i < firstImages.size(); i++) {
                if (firstImages.get(i) != null && !firstImages.get(i).isRecycled()) {
                    firstImages.get(i).recycle();
                }
            }

            for (int i = 0; i < popUpImages.size(); i++) {
                if (popUpImages.get(i) != null && !popUpImages.get(i).isRecycled()) {
                    popUpImages.get(i).recycle();
                }
            }
            for (int i = 0; i < animationBitmaps.size(); i++) {

                for (int j = 0; j < animationBitmaps.get(i).length; j++) {
                    animationBitmaps.get(i)[j].recycle();
                }
            }
            if (backB != null && !backB.isRecycled()) {
                backB.recycle();
                backB = null;
            }
            unbindDrawables(findViewById(R.id.parent_lay));
            unbindDrawables(findViewById(R.id.winningPop));
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        imageLoader.clearCache();
        super.onDestroy();
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

    private WScratchView.OnScratchCallback commonCallback() {
        WScratchView.OnScratchCallback onScratchCallback = new WScratchView.OnScratchCallback() {
            @Override
            public void onScratch(float percentage) {
                isAllScratched();
            }
        };
        return onScratchCallback;
    }

    private void isAllScratched() {
        for (int i = 0; i < scratchImage.length; i++) {
            Utils.logPrint(scratchImage[i] + " Scratch :" + ((WScratchView) scratchImage[i]).getScratchedRatio() + "");
            if (((WScratchView) scratchImage[i]).getScratchedRatio() >= 85) {
                isComplete = true;
            } else {
                isComplete = false;
                break;
            }
        }
        if (isComplete) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    try {
                        if (!isCompleted)
                            showResult();
                    } catch (RuntimeException e) {
                    }

                }
            }, 100);
//            Utils.Toast(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult() {
        setDisableBackView();
        isCompleted = true;
        layoutParams = new RelativeLayout.LayoutParams((int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getW(), (int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getH() / 3);//58, 46
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        winPopLayout.setBackgroundColor(getResources().getColor(R.color.alpha_win_popup_bg));
        winPopupImageView = new ImageView(IGEScratchGameActivity.this);
        winPopupImageView.setLayoutParams(layoutParams);
        if (gameMode.equalsIgnoreCase("try")) {
            if (przAmt > 0) {
                showWinningAnimation();
                winPopupImageView.setImageBitmap(igePrepare.getResultBitmap(popUpImages.get(1), igeGamesData.getPopInfo().getPopUpPrzInfo(), igeGamesData.getPopInfo().getPopUpPlcingInfo(), przAmt));
            } else {
                winPopupImageView.setImageBitmap(igePrepare.getResultBitmap(popUpImages.get(2), igeGamesData.getPopInfo().getPopUpPrzInfo(), igeGamesData.getPopInfo().getPopUpPlcingInfo(), 0));
            }
        } else if (przAmt > 0) {
            showWinningAnimation();
            playAgain.setText("Play Again !!");
            winPopupImageView.setImageBitmap(igePrepare.getResultBitmap(popUpImages.get(0), igeGamesData.getPopInfo().getPopUpPrzInfo(), igeGamesData.getPopInfo().getPopUpPlcingInfo(), przAmt));

            new IGETask(IGEScratchGameActivity.this, "GAME_FINISH" + gameName, IGEService.gameFinishURL(IGEScratchGameActivity.this, gameMode, unfinishedGameList, games, ticketNo), null, "Finishing Game...").execute();
        } else {
            playAgain.setText("Try Again !!");
            winPopupImageView.setImageBitmap(popUpImages.get(2));
            new IGETask(IGEScratchGameActivity.this, "GAME_FINISH" + gameName, IGEService.gameFinishURL(IGEScratchGameActivity.this, gameMode, unfinishedGameList, games, ticketNo), null, "Finishing Game...").execute();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            winPopupImageView.setX((float) igeGamesData.getPopInfo().getPopUpPlcingInfo().getX());
            winPopupImageView.setY((float) igeGamesData.getPopInfo().getPopUpPlcingInfo().getY());
        } else {
            layoutParams.setMargins((int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getX(), (int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getY(), 0, 0);
        }
        winningLayout.removeAllViews();
        winningLayout.addView(winPopupImageView);
        visibilityOff(igeGamesData.getScratchMode().equalsIgnoreCase("Mannual"));


        winPopLayout.setVisibility(View.VISIBLE);
        winningLayout.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);
        if (gameMode.equalsIgnoreCase("try")) {
            playForCash.setVisibility(View.VISIBLE);
            playAgain.setVisibility(View.GONE);
            mainMenu.setVisibility(View.GONE);
//            winningLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
//            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 8f));
        }
//        else if (gameMode.equalsIgnoreCase("UNFINISH")) {
//            playForCash.setVisibility(View.VISIBLE);
//            playAgain.setVisibility(View.GONE);
//            mainMenu.setVisibility(View.GONE);
//        }
        else {
//            winningLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
//            buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
            playForCash.setVisibility(View.GONE);
            playAgain.setVisibility(View.VISIBLE);
            mainMenu.setVisibility(View.GONE);
        }
        if (isMyTickets) {
            playAgain.setVisibility(View.GONE);
            isMyTicketMessage.setVisibility(View.VISIBLE);
        } else {
            isMyTicketMessage.setVisibility(View.GONE);

        }

        Animation bottomUp = AnimationUtils.loadAnimation(IGEScratchGameActivity.this, R.anim.bottom_up);
        Animation bottomDown = AnimationUtils.loadAnimation(IGEScratchGameActivity.this, R.anim.bottom_down);
        buttonLayout.setAnimation(bottomUp);
        winningLayout.setAnimation(bottomDown);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            buttonLayout.animate();
            winningLayout.animate();
        }
        bottomDown.start();
        bottomUp.start();

    }

    private void showWinningAnimation() {
        mLoadingMsg.setText("");
        mLoadingMsg.setVisibility(View.VISIBLE);
        ParticleSystem ps = new ParticleSystem(IGEScratchGameActivity.this, 100, R.drawable.star1, 800);
        ps.setScaleRange(0.7f, 1.3f);
        ps.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps.setFadeOut(200, new AccelerateInterpolator());
        ps.oneShot(mLoadingMsg, 70);

        ParticleSystem ps2 = new ParticleSystem(IGEScratchGameActivity.this, 100, R.drawable.star2, 800);
        ps2.setScaleRange(0.7f, 1.3f);
        ps2.setSpeedRange(0.1f, 0.25f);
        ps.setRotationSpeedRange(90, 180);
        ps2.setFadeOut(200, new AccelerateInterpolator());
        ps2.oneShot(mLoadingMsg, 70);
    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        TypedValue tv = new TypedValue();
        actionBarHeight = (int) getResources().getDimension(R.dimen.header_height);
        deviceHeight = displaymetrics.heightPixels - result - actionBarHeight;
        Utils.logPrint("deviceHeight :" + deviceHeight + "");
        deviceWidth = displaymetrics.widthPixels;
    }

    //    public void setButtonStyle(Context context, TextView playForCash) {
//        SpannableString styledString = new SpannableString("PLAY FOR CASH!!");
//        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "ROBOTO-LIGHT.TTF");
//        Typeface typefaceBold = Typeface.createFromAsset(context.getAssets(), "ROBOTO-REGULAR.TTF");
//        styledString.setSpan(new CustomTypefaceSpan("", typefaceBold), 0, 8, 0);
//        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 9, 15, 0);
//        playForCash.setText(styledString);
//    }
    private void setDisableBackView() {
        if (parentLayout != null) {
            parentLayout.setClickable(false);
            parentLayout.setFocusable(false);
            parentLayout.setOnLongClickListener(null);
            parentLayout.setOnClickListener(null);
        }
    }


    public void visibilityOff(boolean manual) {
        for (int i = 0; i < scratchImage.length; i++) {
            scratchImage[i].setVisibility(View.GONE);
            if (manual) {
                scratchImageManual[i].setVisibility(View.GONE);
            }

        }

    }

    private void onClickBuy(String mode) {
        if (!UserInfo.isLogin(this)) {

            startActivityForResult(new Intent(this,
                    LoginActivity.class), 100);
            overridePendingTransition(GlobalVariables.startAmin,
                    GlobalVariables.endAmin);
        } else {
            GameListDataBean.Games games = new GameListDataBean.Games();
            if (gameMode.equalsIgnoreCase("UNFINISH")) {
                games.setGameName(unfinishedGameList.getGameMaster().getGameName());
                games.setGameNumber(unfinishedGameList.getGameMaster().getGameNum() + "");
                games.setGamePrice(0);
            } else {
                games.setGameName(this.games.getGameName());
                games.setGameNumber(this.games.getGameNumber());
                games.setGamePrice(0);
            }
            String tempBal = VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.USER_BAL);
            if (tempBal.contains(",")) {
                tempBal = tempBal.replace(",", "");
            } else {
                tempBal = tempBal;
            }
            double balance = Double.parseDouble(tempBal);
            double purchaseAmount = new Double(games.getGamePrice());
            if (balance < purchaseAmount) {
                Utils.Toast(this, "Available balace is low");
            } else {
                Intent intent = new Intent(this, IGEScratchGameActivity.class);
                intent.putExtra("games", games);
                intent.putExtra("mode", mode);
                startActivity(intent);
                finish();
            }
        }
    }

    private class FetchImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

        private String imgDesc = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true, "Preparing Game...");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.imgDesc = params[1];
            String path = params[0];
            return imageLoader.getImageInBitmap(path);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPreExecute();
            if (bitmap != null) {
                try {
                    if (imgDesc.equalsIgnoreCase("win_sprite")) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getW(), (int) igeGamesData.getPopInfo().getPopUpPlcingInfo().getH(), true);
                        popUpImages = IGEPrepareSprite.getWinningSprite(bitmap, 3, bitmap.getWidth(), bitmap.getHeight() / 3);
                        new FetchImageFromURLTask().execute(igeGamesData.getSpriteImg(), "front");
                    } else if (Config.isStatic && imgDesc.equalsIgnoreCase("bckImgePth")) {
                        bitmapLoaded(bitmap);
                    } else {
                        IGEPrepareSprite ps;
                        if (igeGamesData.getScratchMode().equalsIgnoreCase("Mannual")) {
                            gameStaringDialog = new LoadingDialog(IGEScratchGameActivity.this);
                            gameStaringDialog.setMessage("Starting game...");
                            if (igeGamesData.getSpriteMping().equalsIgnoreCase("one-to-one")) {
                                ps = new IGEPrepareSprite(bitmap, backImgScrtchInfos.size(), igeGamesData.getNoOfPrizeSpriteStrips(), false, igeGamesData.getSpriteInfos());
                            } else {
                                ps = new IGEPrepareSprite(bitmap, backImgScrtchInfos.size(), igeGamesData.getNoOfPrizeSpriteStrips(), true, igeGamesData.getSpriteInfos());
                            }
                            firstImages = ps.getFirstImages();
                        } else {
                            if (igeGamesData.getSpriteMping().equalsIgnoreCase("one-to-one")) {
                                ps = new IGEPrepareSprite(bitmap, backImgScrtchInfos.size(), igeGamesData.getNoOfPrizeSpriteStrips(), false, igeGamesData.getSpriteInfos());
                            } else {
                                ps = new IGEPrepareSprite(bitmap, backImgScrtchInfos.size(), igeGamesData.getNoOfPrizeSpriteStrips(), true, igeGamesData.getSpriteInfos());
                            }
                            firstImages = ps.getFirstImages();
                            animationBitmaps = ps.getSpriteList();
                        }
                        if (gameMode.equalsIgnoreCase("UNFINISH")) {
                            enableBackPress = false;
                            new IGETask(IGEScratchGameActivity.this, "BACK_TXN" + gameName, IGEService.prepareUnfinish(IGEScratchGameActivity.this, unfinishedGameList), null, "Initializing game...").execute();
                        } else {
                            enableBackPress = false;
                            new IGETask(IGEScratchGameActivity.this, "BACK_TXN" + gameName, IGEService.prepareTryBuyURL(IGEScratchGameActivity.this, gameMode, games), null, "Initializing game...").execute();
                        }
                    }
                } catch (Exception e) {
                    analytics.sendAll(Fields.Category.IGE_GAME_PLAY, Fields.Action.IMG_LOAD, Fields.Label.FAILURE);
                    e.printStackTrace();
                    View.OnClickListener okClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IGEScratchGameActivity.this.finish();
                        }
                    };
                    try {
                        new DownloadDialogBox(IGEScratchGameActivity.this, "Problem loading game, Please try again !!", "Oops...", false, true, okClickListener, null).show();
                    } catch (WindowManager.BadTokenException e1) {

                    }


                }
            } else {
                analytics.sendAll(Fields.Category.IGE_GAME_PLAY, Fields.Action.IMG_LOAD, Fields.Label.FAILURE);
                Utils.consolePrint("View Null+++++++++++++");
                View.OnClickListener okClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IGEScratchGameActivity.this.finish();
                    }
                };
                try {
                    new DownloadDialogBox(IGEScratchGameActivity.this, "Problem loading game, Please try again !!", "Oops...", false, true, okClickListener, null).show();
                } catch (WindowManager.BadTokenException e1) {

                }

            }
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            bitmapLoaded(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            analytics.sendAll(Fields.Category.IGE_GAME_PLAY, Fields.Action.IMG_LOAD, gameMode.toUpperCase(Locale.ENGLISH) + " " + Fields.Label.FAILURE);

            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            try {
                new DownloadDialogBox(IGEScratchGameActivity.this, "Problem loading game, Please try again !!", "Oops...", false, true, okClickListener, null).show();
            } catch (WindowManager.BadTokenException e1) {

            }
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {

        }
    };

//    private void someMethod() {
//        Picasso.with(this).load("url").into(target);
//    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        JSONObject jsonObject = null;
        if (resultData != null) {
            try {
                jsonObject = new JSONObject(resultData.toString());
                if (jsonObject.getInt("errorCode") == 0) {
                    dialog.dismiss();
                    switch (methodType) {
                        case "GAME_ASSETS":
                            enableBackPress = true;
                            igeGamesData = igePrepare.getIGEGamesData(jsonObject);
                            backImgScrtchInfos = igeGamesData.getBackImgScrtchInfos();
                            new FetchImageFromURLTask().execute(igeGamesData.getPopInfo().getPopUpImg(), "win_sprite");
                            break;
                        case "BACK_TXN":
                            enableBackPress = true;
                            przAmt = jsonObject.getDouble("przAmnt");
                            if (gameMode.equalsIgnoreCase("Buy")) {
                                ticketNo = jsonObject.getString("tktNum");
                                VariableStorage.UserPref.setStringPreferences(IGEScratchGameActivity.this, VariableStorage.UserPref.USER_BAL, AmountFormat.getCorrectAmountFormat(AmountFormat.getAmountFormatForMobile(Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_BAL))) - games.getGamePrice())) + "");
                            }
                            Utils.consolePrint(jsonObject.getString("bckImgePth"));
                            if (Config.isStatic) {
                                new FetchImageFromURLTask().execute(jsonObject.getString("bckImgePth"), "bckImgePth");
                            } else {
                                Picasso.with(this).load(jsonObject.getString("bckImgePth")).into(target);
                            }
                            break;
                        case "GAME_FINISH":
                            enableBackPress = true;
                            VariableStorage.UserPref.setStringPreferences(IGEScratchGameActivity.this, VariableStorage.UserPref.USER_BAL, AmountFormat.getCorrectAmountFormat(AmountFormat.getAmountFormatForMobile(Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_BAL))) + przAmt)) + "");
//                            Utils.Toast(IGEScratchGameActivity.this, "Game Completed !!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else if (jsonObject.getInt("errorCode") == 118) {
                    enableBackPress = true;
                    UserInfo.setLogout(IGEScratchGameActivity.this);
                    Utils.Toast(IGEScratchGameActivity.this, jsonObject.getString("errorMsg"));
                    startActivity(new Intent(IGEScratchGameActivity.this, LoginActivity.class));
                    overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                    dialog.dismiss();
                } else if (jsonObject.getInt("errorCode") == 113) {
                    enableBackPress = true;
                    UserInfo.setLogout(IGEScratchGameActivity.this);
                    Utils.Toast(IGEScratchGameActivity.this, jsonObject.getString("errorMsg"));
                    startActivityForResult(new Intent(IGEScratchGameActivity.this, LoginActivity.class), 100);
                    overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                    dialog.dismiss();
                    finish();
                } else {
                    enableBackPress = true;
                    View.OnClickListener okClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            IGEScratchGameActivity.this.finish();
                        }
                    };
                    dialog.dismiss();
                    new DownloadDialogBox(IGEScratchGameActivity.this, jsonObject.getString("errorMsg"), "Instant Win", false, true, okClickListener, null).show();
                }
            } catch (JSONException e) {
                enableBackPress = true;
                View.OnClickListener okClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IGEScratchGameActivity.this.finish();
                    }
                };
                dialog.dismiss();
                new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
            } catch (Exception e1) {
                enableBackPress = true;
                View.OnClickListener okClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IGEScratchGameActivity.this.finish();
                    }
                };
                dialog.dismiss();
                try {
                    new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
                } catch (WindowManager.BadTokenException e) {

                }
            }
        } else {
            enableBackPress = true;
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            dialog.dismiss();
            try {
                new DownloadDialogBox(IGEScratchGameActivity.this, "Some internal error !!", "Oops...", false, true, okClickListener, null).show();
            } catch (WindowManager.BadTokenException e1) {

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                if (gameMode.equalsIgnoreCase("UNFINISH")) {
                    onClickBuy("buy");
                    new IGETask(IGEScratchGameActivity.this, "BACK_TXN" + gameName, IGEService.prepareUnfinish(IGEScratchGameActivity.this, unfinishedGameList), null, "Initializing game...").execute();
                } else {
                    gameMode = "Buy";
                    new IGETask(IGEScratchGameActivity.this, "BACK_TXN" + gameName, IGEService.prepareTryBuyURL(IGEScratchGameActivity.this, gameMode, games), null, "Initializing game...").execute();
                }
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
//        if (fetchImageFromURLTask != null) {
//            if (fetchImageFromURLTask.getStatus() == AsyncTask.Status.PENDING || fetchImageFromURLTask.getStatus() == AsyncTask.Status.RUNNING) {
//                fetchImageFromURLTask.cancel(true);
//            }
//        }

        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(IGEScratchGameActivity.this);
    }


    private void bitmapLoaded(Bitmap bitmap) {

        try {//In ca successfully download case of Back Image
            backB = Bitmap.createScaledBitmap(bitmap, new Double(Math.round(bitmap.getWidth() * IGEPrepare.widthRatio)).intValue(), new Double(Math.round(bitmap.getHeight() * IGEPrepare.heightRatio)).intValue(), true);
            if (gameMode.equalsIgnoreCase("UNFINISH") || gameMode.equalsIgnoreCase("Buy")) {
                ticketNumber.setText("Ticket No : " + ticketNo);
                ticketNumber.setVisibility(View.VISIBLE);
            } else {
                ticketNumber.setText("Sample Game");
                ticketNumber.setVisibility(View.VISIBLE);
            }
            if (igeGamesData.getScratchMode().equalsIgnoreCase("Mannual")) {
                showProgress(false, "");
                if (!gameStaringDialog.isShowing())
                    gameStaringDialog.show();
                scratchImage = new WScratchView[backImgScrtchInfos.size()];
                scratchImageManual = new ImageView[backImgScrtchInfos.size()];
                for (int i = 0; i < scratchImage.length; i++) {
                    IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = backImgScrtchInfos.get(i);
                    layoutParams = new RelativeLayout.LayoutParams((int) backImgScrtchInfo.getWidth(), (int) backImgScrtchInfo.getHeight());
                    scratchImage[i] = new WScratchView(getApplicationContext());
                    scratchImageManual[i] = new ImageView(IGEScratchGameActivity.this);
                    ((WScratchView) scratchImage[i]).setScratchDrawable(new BitmapDrawable(getResources(), firstImages.get(i)));
                    scratchImageManual[i].setImageBitmap(firstImages.get(i));
                    if (i == (scratchImage.length - 1))
                        backIV.setImageBitmap(backB);
                    parentLayout.addView(scratchImage[i], i + 1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        scratchImage[i].setX((float) backImgScrtchInfo.getXb());
                        scratchImageManual[i].setX((float) backImgScrtchInfo.getXb());
                        scratchImage[i].setY((float) backImgScrtchInfo.getYb());
                        scratchImageManual[i].setY((float) backImgScrtchInfo.getYb());
                    } else {
                        layoutParams.setMargins((int) backImgScrtchInfo.getXb(), (int) backImgScrtchInfo.getYb(), 0, 0);
                    }
                    scratchImageManual[i].setLayoutParams(layoutParams);
                    scratchImageManual[i].setVisibility(View.GONE);
                    scratchImage[i].setLayoutParams(layoutParams);
                    ((WScratchView) scratchImage[i]).setOnScratchCallback(commonCallback());


                }
                layoutParams = new RelativeLayout.LayoutParams((int) igeGamesData.getScrtchBtnInfo().getW(), (int) igeGamesData.getScrtchBtnInfo().getH());//58, 46
                Button scratchAllBtn = new Button(getApplicationContext());

                scratchAllBtn.setBackgroundColor(Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    scratchAllBtn.setX((float) igeGamesData.getScrtchBtnInfo().getX());
                    scratchAllBtn.setY((float) igeGamesData.getScrtchBtnInfo().getY());
                } else {
                    layoutParams.setMargins((int) igeGamesData.getScrtchBtnInfo().getX(), (int) igeGamesData.getScrtchBtnInfo().getY(), 0, 0);
                }
                scratchAllBtn.setLayoutParams(layoutParams);
                parentLayout.addView(scratchAllBtn);

                scratchAllBtn.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        for (int i = 0; i < scratchImage.length; i++) {
                            scratchImageManual[i].setVisibility(View.VISIBLE);
                            scratchImage[i].setVisibility(View.GONE);
                            scratchImageManual[i].startAnimation(animFadeOut);
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if (!isCompleted)
                                    showResult();
                            }
                        }, 1000);
//                                    showResult();
                        return false;
                    }
                });
                analytics.sendAll(Fields.Category.IGE_GAME_PLAY_MANUAL, Fields.Action.IMG_LOAD, Fields.Label.SUCCESS);


            } else {
                scratchImage = new ImageView[backImgScrtchInfos.size()];
                for (int i = 0; i < scratchImage.length; i++) {
                    IGEGamesData.BackImgScrtchInfo backImgScrtchInfo = backImgScrtchInfos.get(i);
                    layoutParams = new RelativeLayout.LayoutParams((int) backImgScrtchInfo.getWidth(), (int) backImgScrtchInfo.getHeight());//58, 46
                    scratchImage[i] = new ImageView(getApplicationContext());
                    ((ImageView) scratchImage[i]).setImageBitmap(firstImages.get(i));
                    ((ImageView) scratchImage[i]).setScaleType(ImageView.ScaleType.FIT_XY);
                    parentLayout.addView(scratchImage[i], i + 1);
                    if (i == (scratchImage.length - 1))
                        backIV.setImageBitmap(backB);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        scratchImage[i].setX((float) backImgScrtchInfo.getXb());
                        scratchImage[i].setY((float) backImgScrtchInfo.getYb());
                    } else {
                        layoutParams.setMargins((int) backImgScrtchInfo.getXb(), (int) backImgScrtchInfo.getYb(), 0, 0);
                    }
                    scratchImage[i].setLayoutParams(layoutParams);
                    scratchImage[i].setOnClickListener(getCommonClickListener(i));
                }

                layoutParams = new RelativeLayout.LayoutParams((int) igeGamesData.getScrtchBtnInfo().getW(), (int) igeGamesData.getScrtchBtnInfo().getH());//58, 46
                Button scratchAllBtn = new Button(getApplicationContext());

                scratchAllBtn.setBackgroundColor(Color.TRANSPARENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    scratchAllBtn.setX((float) igeGamesData.getScrtchBtnInfo().getX());
                    scratchAllBtn.setY((float) igeGamesData.getScrtchBtnInfo().getY());
                } else {
                    layoutParams.setMargins((int) igeGamesData.getScrtchBtnInfo().getX(), (int) igeGamesData.getScrtchBtnInfo().getY(), 0, 0);
                }
                scratchAllBtn.setLayoutParams(layoutParams);
                parentLayout.addView(scratchAllBtn);
                scratchAllBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        for (int i = 0; i < scratchImage.length; i++)
                            doScratch(i);
                        return false;
                    }
                });
                try {
                    showProgress(false, "");
                } catch (IllegalArgumentException e) {
                }
                analytics.sendAll(Fields.Category.IGE_GAME_PLAY_AUTOMATIC, Fields.Action.IMG_LOAD, Fields.Label.SUCCESS);
            }
            if (gameStaringDialog != null)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (gameStaringDialog.isShowing())
                                gameStaringDialog.dismiss();

                        } catch (IllegalArgumentException e) {
                        }

                    }
                }, 2000);
        } catch (Exception e) {
            analytics.sendAll(Fields.Category.IGE_GAME_PLAY, Fields.Action.IMG_LOAD, gameMode.toUpperCase(Locale.ENGLISH) + " " + Fields.Label.FAILURE);

            e.printStackTrace();
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEScratchGameActivity.this.finish();
                }
            };
            try {
                new DownloadDialogBox(IGEScratchGameActivity.this, "Problem loading game, Please try again !!", "Oops...", false, true, okClickListener, null).show();
            } catch (WindowManager.BadTokenException e1) {

            }
        }
    }
}

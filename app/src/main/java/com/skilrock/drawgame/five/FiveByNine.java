package com.skilrock.drawgame.five;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.skilrock.adapters.GamePageGridAdapter;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.CustomTextViewTop.TextStyles;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DebouncedOnItemClickListener;
import com.skilrock.drawgame.DGPlayActivity;
import com.skilrock.drawgame.ResultScreen;
import com.skilrock.drawgame.StatisticScreen;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.CounterClass;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.TimeCalculator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FiveByNine extends Fragment implements WebServicesListener {
    private View mainView;
    // private JazzyViewPager myPager;
    private CustomTextView nextDraw;
    private GridView itemGrid;
    private int height;
    private LinearLayout playNowLay;
    private int width;
    private double heightForGridChild;
    private int widthForGridChild;
    private double gridWeight = 0.6;// 10%5
    private int actionBarHeight, footerHeight;
    private int tabBarHeight;
    private int deviceDpi;
    private int images[] = new int[]{R.drawable.stats, R.drawable.result};
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS"};
    private ArrayList<IconWithTitle> mainMenuData;
    private ImageView bannerView;
    private RelativeLayout dataLay;
    private CustomTextView CustomTextView;
    protected String frgamentName;
    private String gameName, winningNumbers, currentDrawDateTime,
            drawFreezeTime, gameBannerURL, drawResultDate;
    private CounterClass counterClass;
    private int winningNoSize;
    private CustomTextViewDown play;
    private CustomTextViewTop now;
    private JSONObject jsonResult;
    private LinearLayout statView, resultView, resultBox;
    private CustomTextView playNow;
    private RelativeLayout lastDrawLay;
    private Analytics analytics;
    private Activity activity;
    private View focusPre, focusNext;
    private String gameID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.FIVE_GAME);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        gameName = getArguments().getString("gameName");
        winningNumbers = getArguments().getString("lastDrawResult");
        drawResultDate = getArguments().getString("lastDrawTime");
        currentDrawDateTime = getArguments().getString("currentDrawDateTime");
        drawFreezeTime = getArguments().getString("currentDrawFreezeTime");
        gameBannerURL = getArguments().getString("bannerUrl");
        gameID = getArguments().getString("gameId");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainMenuData = new ArrayList<IconWithTitle>();
        for (int i = 0; i < titlesMenu.length; i++) {
            IconWithTitle iconWithTitle = new IconWithTitle(titlesMenu[i],
                    images[i]);
            mainMenuData.add(iconWithTitle);
        }
        mainView = inflater.inflate(R.layout.five_fragment, null);
        bindViewIds(mainView);
        switch (gameID) {
            case Config.fiveGameName:
                bannerView.setImageResource(R.drawable.sub_banner_five);
                break;
            case Config.fiveGameNameMachine:
                bannerView.setImageResource(R.drawable.sub_banner_five_machine);
                break;
        }
//        bannerView.setImageResource(R.drawable.sub_banner_five);
        nextDraw.setTypeface(Config.timerFont);
        SpannableString string = new SpannableString("03:30 :52");
        string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
        string.setSpan(new RelativeSizeSpan(1f), 6, 9, 0);
        nextDraw.setText(string);
        playNow.setOnClickListener(listener);
        dataLay.setBackgroundColor(getResources().getColor(R.color.draw_theme_color));
        getDisplayDetails();
        setWinningNumbersData(winningNumbers);
//        setCounterDownTimer();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        // imageLoader.displayImage(gameBannerURL, bannerView, options,
        // imageListener);
        itemGrid.setAdapter(new GamePageGridAdapter(activity, R.layout.menu_row, (int) heightForGridChild / 2, (int) widthForGridChild / 2, mainMenuData, getResources().getColor(R.color.five_theme_color)));


        statView.setOnClickListener(listener);
        resultView.setOnClickListener(listener);
        itemGrid.setOnItemClickListener(new DebouncedOnItemClickListener(1000) {
            @Override
            public void onDebouncedItemClick(AdapterView<?> parent, View view,
                                             int position, long id) {
                switch (position) {
                    case 0:
                        if (GlobalVariables.connectivityExists(activity)) {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.FIVE_GAME_STATS);

                            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                            String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(FiveByNine.this, "GET_STATS", url, json).execute();
                        } else {

                            GlobalVariables.showDataAlert(activity);
                        }
                        break;
                    case 1:
                        if (GlobalVariables.connectivityExists(activity)) {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.FIVE_GAME_RESULT);

                            String urlRes = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                            String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(FiveByNine.this, "GET_RESULTS", urlRes, jsonRes).execute();
                        } else {

                            GlobalVariables.showDataAlert(activity);
                        }
                        break;
                    case 2:
                        Utils.Toast(activity, "Coming Soon!");
                        break;
                    case 3:
                        goToGameScreen();
                        break;

                    default:
                        break;
                }
            }
        });
        return mainView;
    }

    private void goToGameScreen() {
        frgamentName = getResources().getString(R.string.dg_header);
        Intent intent = new Intent(activity, DGPlayActivity.class);
        intent.putExtra("selectedGame", gameName);
        intent.putExtra("isDraws", getArguments().getBoolean("isDraws"));
        startActivity(intent);
        activity.overridePendingTransition(
                GlobalVariables.startAmin, GlobalVariables.endAmin);
    }

    private void setCounterDownTimer() {
        try {
            long remainingSeconds = (TimeCalculator
                    .timestampToMilliseconds(currentDrawDateTime) - (Integer
                    .parseInt(drawFreezeTime) * 1000))
                    - (TimeCalculator
                    .timestampToMilliseconds(GlobalVariables.GamesData.currentTime) + (System
                    .currentTimeMillis() - MainScreen.loginTimeMilliseconds));
            nextDraw.setVisibility(View.VISIBLE);
            if (remainingSeconds > 1000) {
                counterClass = new CounterClass(nextDraw, remainingSeconds,
                        1000);

                counterClass.start();
            } else {
                SpannableString string = new SpannableString("00:00:00");
                string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
                string.setSpan(new RelativeSizeSpan(1f), 6, 9, 0);
                nextDraw.setText(string);
                nextDraw.setTextColor(Color.RED);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.one:

                    break;
                default:
                    break;
            }

        }
    };
    private String[] winNumber;

    private void bindViewIds(View view) {
        ((ImageView) view.findViewById(R.id.imageView1)).setColorFilter(
                Color.WHITE, Mode.SRC_IN);
        lastDrawLay = (RelativeLayout) view.findViewById(R.id.last_draw);
        resultBox = (LinearLayout) view.findViewById(R.id.result_box);
        statView = (LinearLayout) view.findViewById(R.id.stats_view);
        resultView = (LinearLayout) view.findViewById(R.id.result_view);
        playNow = (CustomTextView) view.findViewById(R.id.buy_now);
        play = (CustomTextViewDown) view.findViewById(R.id.play);
        now = (CustomTextViewTop) view.findViewById(R.id.now);
        play.setTextStyle(CustomTextViewDown.TextStyles.BOLD);
        now.setTextStyle(TextStyles.LIGHT);
        playNowLay = (LinearLayout) view.findViewById(R.id.aa);
        nextDraw = (CustomTextView) view.findViewById(R.id.next_draw);
        itemGrid = (GridView) view.findViewById(R.id.item_grid);
        bannerView = (ImageView) view.findViewById(R.id.banner);
        dataLay = (RelativeLayout) view.findViewById(R.id.data_lay);
        CustomTextView = (CustomTextView) view.findViewById(R.id.text);

        if (!winningNumbers.trim().equals("") && !drawResultDate.trim().equals(""))
            ((CustomTextView) mainView.findViewById(R.id.draw_date)).setText(GlobalVariables.formatDateFromCal(activity, drawResultDate, VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DATE_FORMAT) + " HH:mm:ss"));
//        else if (drawResultDate.equals(""))
//            lastDrawLay.setVisibility(View.GONE);
//        else if (winningNumbers.equals(""))
//            resultBox.setVisibility(View.GONE);
        else
            dataLay.setVisibility(View.GONE);

        if (gameID.equalsIgnoreCase(Config.fiveGameNameMachine))
            playNow.setEnabled(false);
    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int var = 0;
        if (GlobalVariables.onTablet(activity)) {
            var = 70;
        } else {
            var = 50;
        }
        actionBarHeight = (int) (var * getResources()
                .getDisplayMetrics().density);
        footerHeight = /* GlobalVariables.getPx(40, activity) */0;
        tabBarHeight = (int) getResources().getDimension(R.dimen.tabBarHeight);
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        deviceDpi = displaymetrics.densityDpi;
        height = displaymetrics.heightPixels - result - actionBarHeight
                - footerHeight - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Utils.logPrint("data-" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    private void setDispMetrices(GradientDrawable drawable, int hw) {
        drawable.setSize(hw, hw);
        // drawable.setSize(GlobalVariables.getPx(hw, activity),
        // GlobalVariables.getPx(hw, activity));
    }

    private void setWinningNumbersData(String winningNumbers) {
        if (winningNumbers.equals(""))
            return;
        Utils.consolePrint("======" + winningNumbers);
        try {
            String[] data = winningNumbers.split(",");
            if (data.length > 4) {
                LinearLayout winNoLay = (LinearLayout) mainView
                        .findViewById(R.id.win_num_lay);
                winNumber = data;
                int wi = winNoLay.getMeasuredWidth();
                DisplayMetrics displaymetrics = new DisplayMetrics();

                activity.getWindowManager().getDefaultDisplay()
                        .getMetrics(displaymetrics);
                deviceDpi = displaymetrics.densityDpi;
                widthForGridChild = width = displaymetrics.widthPixels;
                int margin = (int) (2 * getResources()
                        .getDisplayMetrics().density) * 14;
                int txtSize = (widthForGridChild - margin) / 7;
                resultBox.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                lastDrawLay.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int ballHeight = (int) (height * 0.166 - lastDrawLay.getMeasuredHeight());
                if (ballHeight < txtSize)
                    txtSize = ballHeight - GlobalVariables.getPx(2, activity);
                winningNoSize = txtSize;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(winningNoSize, winningNoSize);
                layoutParams.setMargins(GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()));
                for (int i = 0; i < winNoLay.getChildCount(); i++) {
                    CustomTextView textViewWin = (CustomTextView) winNoLay
                            .getChildAt(i);
                    textViewWin.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            winningNoSize / 2);
                    //bonus ball
                    if (winNumber.length <= i) {
                        textViewWin.setVisibility(View.GONE);
                        break;
                    }
                    //bonus ball end
                    textViewWin.setLayoutParams(layoutParams);

                    textViewWin.setText(winNumber[i]);
                    GradientDrawable drawableWin = (GradientDrawable) textViewWin
                            .getBackground().mutate();

                    setDispMetrices(drawableWin, winningNoSize);
//                    textViewWin.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//                            winningNoSize / 2);



                   /* if (GlobalVariables.onTablet(activity)) {
                        winningNoSize = 75;
                        setDispMetrices(drawableWin, winningNoSize);
                    } else {

                        if (deviceDpi == DisplayMetrics.DENSITY_LOW) {
                            winningNoSize = 30;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                        if (deviceDpi == DisplayMetrics.DENSITY_MEDIUM) {
                            winningNoSize = 40;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                        if (deviceDpi == DisplayMetrics.DENSITY_HIGH) {
                            winningNoSize = 45;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                        if (deviceDpi == DisplayMetrics.DENSITY_XHIGH) {
                            winningNoSize = 50;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                        if (deviceDpi == DisplayMetrics.DENSITY_XXHIGH) {
                            winningNoSize = 55;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                        if (deviceDpi == DisplayMetrics.DENSITY_XXXHIGH) {
                            winningNoSize = 75;
                            setDispMetrices(drawableWin, winningNoSize);
                        }
                    }*/
                }
            }
        } catch (Exception e) {
            Utils.consolePrint(e + "");

        }
    }

//    private ImageLoader imageLoader = ImageLoader.getInstance();
//    private ImageLoadingListener imageListener = new ImageDisplayListener();


    View.OnFocusChangeListener a = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    };

//    private static class ImageDisplayListener extends
//            SimpleImageLoadingListener {
//
//        static final List<String> displayedImages = Collections
//                .synchronizedList(new LinkedList<String>());
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view,
//                                      Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//
//    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "GET_STATS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.STATS, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(activity,
                                    StatisticScreen.class);
                            statsIntent.putExtra("gameId", gameID);
                            statsIntent.putExtra("jsonData", resultData.toString());
                            startActivity(statsIntent);
                            activity.overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                } else {
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(activity);
                }

                break;
            case "GET_RESULTS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.RESULT, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(activity,
                                    ResultScreen.class);
                            statsIntent.putExtra("gameId", gameID);
                            statsIntent.putExtra("jsonData", jsonResult.getJSONArray("gameData").toString());
                            startActivity(statsIntent);
                            activity.overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else {
                            analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                } else {
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(activity);
                }

                break;
        }
    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(1000) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.stats_view:
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.FIVE_GAME_STATS);

                    String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                    String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(FiveByNine.this, "GET_STATS", url, json).execute();
                    break;
                case R.id.result_view:
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.FIVE_GAME_RESULT);

                    String urlRes = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                    String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(FiveByNine.this, "GET_RESULTS", urlRes, jsonRes).execute();
                    break;
                case R.id.buy_now:
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.BUY);
                    if (gameID.equalsIgnoreCase(Config.fiveGameName))
                        goToGameScreen();
                    else
                        Utils.Toast(activity, "Currently sale is not Available");
                    break;
            }
        }
    };
}

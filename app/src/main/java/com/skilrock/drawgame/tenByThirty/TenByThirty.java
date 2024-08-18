package com.skilrock.drawgame.tenByThirty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.drawgame.DGPlayActivity;
import com.skilrock.drawgame.ResultScreen;
import com.skilrock.drawgame.StatisticScreen;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.CounterClass;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.TimeCalculator;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.support.v4.app.Fragment;

/**
 * Created by stpl on 8/8/2016.
 */
public class TenByThirty extends Fragment implements WebServicesListener {
    private ArrayList<IconWithTitle> mainMenuData;
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS"};
    private int images[] = new int[]{R.drawable.stats, R.drawable.result};
    private View mainView;
    private RelativeLayout lastDrawLay;
    private LinearLayout resultBox;
    private LinearLayout statView;
    private LinearLayout resultView;
    private CustomTextView playNow;
    private CustomTextViewDown play;
    private CustomTextViewTop now;
    private LinearLayout playNowLay;
    private CustomTextView nextDraw;
    private GridView itemGrid;
    private ImageView bannerView;
    private RelativeLayout dataLay;
    private String gameName, winningNumbers, currentDrawDateTime,
            drawFreezeTime, gameBannerURL;
    private JSONObject jsonResult;
    private String frgamentName;
    private int actionBarHeight, footerHeight, tabBarHeight, deviceDpi, height, width, widthForGridChild;
    private double heightForGridChild;
    private double gridWeight = 0.6;
    private int winningNoSize;
    private CounterClass counterClass;
    private boolean isData, isDataResult;
    private Activity activity;
    private String gameID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameName = getArguments().getString("gameName");
        //new comment
        winningNumbers = getArguments().getString("lastDrawResult");
        currentDrawDateTime = getArguments().getString("currentDrawDateTime");
        gameID = getArguments().getString("gameId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainMenuData = new ArrayList<IconWithTitle>();
        gameID = getArguments().getString("gameId");
        for (int i = 0; i < titlesMenu.length; i++) {
            IconWithTitle iconWithTitle = new IconWithTitle(titlesMenu[i],
                    images[i]);
            mainMenuData.add(iconWithTitle);
        }
        mainView = inflater.inflate(R.layout.ten_fragment, null);
        bindViewIds(mainView);
        switch (gameID) {
            case Config.tenByNinety:
                bannerView.setImageResource(R.drawable.sub_banner_ten_by_ninty);
                break;
            case Config.tenByThirty:
                bannerView.setImageResource(R.drawable.fast_img_back);
                break;
        }

        playNow.setOnClickListener(listener);
//        playNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToGameScreen();
//            }
//        });
        dataLay.setBackgroundColor(getResources().getColor(R.color.draw_theme_color));
        getDisplayDetails();
        setWinningNumbersData(winningNumbers);
        setCounterDownTimer();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        statView.setOnClickListener(listener);
        resultView.setOnClickListener(listener);

//        statView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
//                String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
//                new DGETask(TenByNinety.this, "GET_STATS", url, json).execute();
//            }
//        });
//        resultView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
//                String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
//                new DGETask(TenByNinety.this, "GET_RESULTS", urlRes, jsonRes).execute();
//            }
//        });


        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (true) {

                } else {
                    switch (position) {
                        case 0:
                            if (GlobalVariables.connectivityExists(getActivity())) {
                                String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                                String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                                new DGETask(TenByThirty.this, "GET_STATS", url, json).execute();
                            } else {

                                GlobalVariables.showDataAlert(getActivity());
                            }
                            break;
                        case 1:
                            if (GlobalVariables.connectivityExists(getActivity())) {
                                String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                                String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                                new DGETask(TenByThirty.this, "GET_RESULTS", urlRes, jsonRes).execute();
                            } else {

                                GlobalVariables.showDataAlert(getActivity());
                            }
                            break;
                        case 2:
                            Utils.Toast(getActivity(), "Coming Soon!");
                            break;
                        case 3:
                            goToGameScreen();
                            break;

                        default:
                            break;
                    }
                }
            }

        });

        return mainView;
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

    private void setWinningNumbersData(String winningNumbers) {
        System.out.println("======" + winningNumbers);
        try {
            String[] data = winningNumbers.split(",");
            if (data.length > 4) {
                LinearLayout winNoLay = (LinearLayout) mainView
                        .findViewById(R.id.win_num_lay);
                LinearLayout winNoLay1 = (LinearLayout) mainView
                        .findViewById(R.id.win_num_lay1);
                winNumber = data;
                int wi = winNoLay.getMeasuredWidth();
                DisplayMetrics displaymetrics = new DisplayMetrics();

                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(displaymetrics);
                deviceDpi = displaymetrics.densityDpi;
                widthForGridChild = width = displaymetrics.widthPixels;
                int margin = (int) (2 * getResources()
                        .getDisplayMetrics().density) * 14;
                int txtSize = (widthForGridChild - margin) / 7;
                resultBox.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                lastDrawLay.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int ballHeight = (int) (height * 0.166 - lastDrawLay.getMeasuredHeight()) / 2 - GlobalVariables.getPx(2, getActivity());
                if (ballHeight < txtSize)
                    txtSize = ballHeight - GlobalVariables.getPx(2, getActivity());
                winningNoSize = txtSize;
                for (int i = 0; i < winNoLay.getChildCount(); i++) {
                    CustomTextView textViewWin = (CustomTextView) winNoLay
                            .getChildAt(i);
                    textViewWin.setText(winNumber[i]);
                    GradientDrawable drawableWin = (GradientDrawable) textViewWin
                            .getBackground().mutate();

                    setDispMetrices(drawableWin, winningNoSize);


                    textViewWin.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            winningNoSize / 2);
                }
                for (int i = 0; i < winNoLay1.getChildCount(); i++) {
                    CustomTextView textViewWin = (CustomTextView) winNoLay1
                            .getChildAt(i);
                    textViewWin.setText(winNumber[5 + i]);
                    GradientDrawable drawableWin = (GradientDrawable) textViewWin
                            .getBackground().mutate();

                    setDispMetrices(drawableWin, winningNoSize);


                    textViewWin.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            winningNoSize / 2);
                }
            } else {
                LinearLayout winNoLay = (LinearLayout) mainView.findViewById(R.id.win_num_lay);
                LinearLayout winNoLay1 = (LinearLayout) mainView.findViewById(R.id.win_num_lay1);
                winNoLay1.setVisibility(View.GONE);
                CustomTextView messageView = new CustomTextView(activity);
                messageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                messageView.setText("No Winning Available");
                messageView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                messageView.setTextColor(getResources().getColor(R.color.white));
                winNoLay.removeAllViews();
                winNoLay.addView(messageView);
            }
        } catch (Exception e) {
            if (winningNumbers == null) {
                LinearLayout winNoLay = (LinearLayout) mainView.findViewById(R.id.win_num_lay);
                LinearLayout winNoLay1 = (LinearLayout) mainView.findViewById(R.id.win_num_lay1);
                winNoLay1.setVisibility(View.GONE);
                CustomTextView messageView = new CustomTextView(activity);
                messageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                messageView.setText("No Winning Available");
                messageView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                messageView.setTextColor(getResources().getColor(R.color.white));
                winNoLay.removeAllViews();
                winNoLay.addView(messageView);
            }
            System.out.println(e);
        }
    }

    private void setDispMetrices(GradientDrawable drawable, int hw) {
        drawable.setSize(hw, hw);
    }

//    private ImageLoader imageLoader = ImageLoader.getInstance();
//    private ImageLoadingListener imageListener = new ImageDisplayListener();
//
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
//    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int var = 0;
        if (GlobalVariables.onTablet(getActivity())) {
            var = 70;
        } else {
            var = 50;
        }
        actionBarHeight = (int) (var * getResources()
                .getDisplayMetrics().density);
        footerHeight = /* GlobalVariables.getPx(40, getActivity()) */0;
        tabBarHeight = (int) getResources().getDimension(R.dimen.tabBarHeight);
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        deviceDpi = displaymetrics.densityDpi;
        height = displaymetrics.heightPixels - result - actionBarHeight
                - footerHeight - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Log.i("data", height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    private void goToGameScreen() {
        frgamentName = getResources().getString(R.string.dg_header);
        Intent intent = new Intent(getActivity(), DGPlayActivity.class);
        intent.putExtra("selectedGame", gameName);
        intent.putExtra("isDraws", getArguments().getBoolean("isDraws"));
        startActivity(intent);
        getActivity().overridePendingTransition(
                GlobalVariables.startAmin, GlobalVariables.endAmin);
    }

    private String[] winNumber;

    private void bindViewIds(View view) {
        ((ImageView) view.findViewById(R.id.imageView1)).setColorFilter(
                Color.WHITE, PorterDuff.Mode.SRC_IN);
        lastDrawLay = (RelativeLayout) view.findViewById(R.id.last_draw);
        resultBox = (LinearLayout) view.findViewById(R.id.result_box);
        statView = (LinearLayout) view.findViewById(R.id.stats_view);
        resultView = (LinearLayout) view.findViewById(R.id.result_view);
        playNow = (CustomTextView) view.findViewById(R.id.buy_now);
        play = (CustomTextViewDown) view.findViewById(R.id.play);
        now = (CustomTextViewTop) view.findViewById(R.id.now);
        play.setTextStyle(CustomTextViewDown.TextStyles.BOLD);
        now.setTextStyle(CustomTextViewTop.TextStyles.LIGHT);
        playNowLay = (LinearLayout) view.findViewById(R.id.aa);
        nextDraw = (CustomTextView) view.findViewById(R.id.next_draw);
        itemGrid = (GridView) view.findViewById(R.id.item_grid);
        bannerView = (ImageView) view.findViewById(R.id.banner);
        dataLay = (RelativeLayout) view.findViewById(R.id.data_lay);

        try {
            if (!getArguments().getString("lastDrawTime").trim().equalsIgnoreCase(""))
                ((CustomTextView) mainView.findViewById(R.id.draw_date)).setText(GlobalVariables.formatDateFromCal(getActivity(), getArguments().getString("lastDrawTime"), VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DATE_FORMAT) + " HH:mm:ss"));
            else
                ((CustomTextView) mainView.findViewById(R.id.draw_date)).setText("");
        } catch (Exception e) {
            ((CustomTextView) mainView.findViewById(R.id.draw_date)).setText("");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "GET_STATS":
                if (resultData != null) {
                    try {

                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            for (int i = 0; i < jsonResult.getJSONArray("dgeStats").length(); i++) {
                                JSONObject jsonObject = jsonResult.getJSONArray("dgeStats").getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.tenByThirty)) {
                                    isData = true;
                                    break;
                                } else {
                                    isData = false;
                                }
                            }
                            if (!isData) {
                                dialog.dismiss();
                                Utils.Toast(getActivity(), "No Stats Found");
                            } else {
                                Intent statsIntent = new Intent(getActivity(),
                                        StatisticScreen.class);
                                statsIntent.putExtra("gameId", Config.tenByThirty);
                                statsIntent.putExtra("jsonData", resultData.toString());
                                startActivity(statsIntent);
                                getActivity().overridePendingTransition(
                                        GlobalVariables.startAmin, GlobalVariables.endAmin);
                                dialog.dismiss();
                            }
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            dialog.dismiss();
                            Utils.Toast(getActivity(), jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }

                break;
            case "GET_RESULTS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            for (int i = 0; i < jsonResult.getJSONArray("gameData").length(); i++) {
                                JSONObject jsonObject = jsonResult.getJSONArray("gameData").getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.tenByThirty)) {
                                    isDataResult = true;
                                    break;
                                } else {
                                    isDataResult = false;
                                }
                            }
                            if (!isDataResult) {
                                dialog.dismiss();
                                Utils.Toast(getActivity(), "No Results Found");
                            } else {
                                Intent statsIntent = new Intent(getActivity(),
                                        ResultScreen.class);
                                statsIntent.putExtra("gameId", Config.tenByThirty);
                                statsIntent.putExtra("jsonData", jsonResult.getJSONArray("gameData").toString());
                                startActivity(statsIntent);
                                getActivity().overridePendingTransition(
                                        GlobalVariables.startAmin, GlobalVariables.endAmin);
                                dialog.dismiss();
                            }
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            dialog.dismiss();
                            Utils.Toast(getActivity(), jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }

                break;
        }
    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.stats_view:
                    String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                    String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(TenByThirty.this, "GET_STATS", url, json).execute();
                    break;
                case R.id.result_view:
                    String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                    String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(TenByThirty.this, "GET_RESULTS", urlRes, jsonRes).execute();
                    break;
                case R.id.buy_now:
                    goToGameScreen();
                    break;
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();
    }
}


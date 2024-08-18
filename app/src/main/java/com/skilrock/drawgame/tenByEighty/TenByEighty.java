package com.skilrock.drawgame.tenByEighty;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by stpl on 11/16/2016.
 */
public class TenByEighty extends Fragment implements WebServicesListener {
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
        mainView = inflater.inflate(R.layout.ten_by_eighty_fragment, null);
        bindViewIds(mainView);
        switch (gameID) {
            case Config.tenByEighty:
                bannerView.setImageResource(R.drawable.sub_banner_ten_by_20);
                break;
        }

        playNow.setOnClickListener(listener);
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
                                new DGETask(TenByEighty.this, "GET_STATS", url, json).execute();
                            } else {

                                GlobalVariables.showDataAlert(getActivity());
                            }
                            break;
                        case 1:
                            if (GlobalVariables.connectivityExists(getActivity())) {
                                String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                                String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                                new DGETask(TenByEighty.this, "GET_RESULTS", urlRes, jsonRes).execute();
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
                    textViewWin.setText(winNumber[10 + i]);
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
                                if (jsonObject.getString("gameCode").equals(Config.tenByEighty)) {
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
                                statsIntent.putExtra("gameId", Config.tenByEighty);
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
                                if (jsonObject.getString("gameCode").equals(Config.tenByEighty)) {
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
                                statsIntent.putExtra("gameId", Config.tenByEighty);
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
                    new DGETask(TenByEighty.this, "GET_STATS", url, json).execute();
                    break;
                case R.id.result_view:
                    String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                    String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(TenByEighty.this, "GET_RESULTS", urlRes, jsonRes).execute();
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


    public Object fakeJson() {
        JSONObject jsonObject = null;
        try {
            String fake = "{\"gameData\":[{\"resultData\":[{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"4.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 2\"},{\"amount\":\"500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 3\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 4\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":5,\"match\":\"Match 5\"}],\"drawTime\":\"13:28:00\",\"drawName\":\"null\",\"winningNo\":\"69,17,12,44,15\"},{\"matchInfo\":[{\"amount\":\"4.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 2\"},{\"amount\":\"500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 3\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 4\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":5,\"match\":\"Match 5\"}],\"drawTime\":\"13:12:00\",\"drawName\":\"null\",\"winningNo\":\"2,21,45,49,65\"}],\"resultDate\":\"17-11-2016\"}],\"lastDrawId\":88081,\"gameCode\":\"KenoTwo\"},{\"resultData\":[{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"4.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 2\"},{\"amount\":\"500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 3\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 4\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":5,\"match\":\"Match 5\"}],\"drawTime\":\"13:36:00\",\"drawName\":\"null\",\"winningNo\":\"5,58,23,9,18,5,58,23,9,18,5,58,23,9,18,5,58,23,9,18\"},{\"matchInfo\":[{\"amount\":\"4.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 2\"},{\"amount\":\"500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 3\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 4\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":5,\"match\":\"Match 5\"}],\"drawTime\":\"13:20:00\",\"drawName\":\"null\",\"winningNo\":\"81,7,16,28,88,81,7,16,28,88,81,7,16,28,88,81,7,16,28,88\"},{\"matchInfo\":[{\"amount\":\"4.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 2\"},{\"amount\":\"500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 3\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 4\"},{\"amount\":\"1000.00\",\"noOfWinners\":\"0\",\"prizeRank\":5,\"match\":\"Match 5\"}],\"drawTime\":\"13:04:00\",\"drawName\":\"null\",\"winningNo\":\"65,15,25,4,5,65,15,25,4,5,65,15,25,4,5,65,15,25,4,5\"}],\"resultDate\":\"17-11-2016\"}],\"lastDrawId\":88081,\"gameCode\":\"KenoSix\"},{\"resultData\":[{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"13:32:00\",\"drawName\":\"null\",\"winningNo\":\"One(1)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"13:07:00\",\"drawName\":\"null\",\"winningNo\":\"Zero(0)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"12:42:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"12:17:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"11:52:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"11:27:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"11:02:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"10:37:00\",\"drawName\":\"null\",\"winningNo\":\"Eight(8)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"10:12:00\",\"drawName\":\"null\",\"winningNo\":\"Six(6)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"09:47:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"09:22:00\",\"drawName\":\"null\",\"winningNo\":\"One(1)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"08:57:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"08:32:00\",\"drawName\":\"null\",\"winningNo\":\"Five(5)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"08:07:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"07:42:00\",\"drawName\":\"null\",\"winningNo\":\"Zero(0)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"07:17:00\",\"drawName\":\"null\",\"winningNo\":\"Eight(8)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"06:52:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"06:27:00\",\"drawName\":\"null\",\"winningNo\":\"Four(4)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"06:02:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"05:37:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"05:12:00\",\"drawName\":\"null\",\"winningNo\":\"Zero(0)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"04:47:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"04:22:00\",\"drawName\":\"null\",\"winningNo\":\"Six(6)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"03:57:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"03:32:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"03:07:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"02:42:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"02:17:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"01:52:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"01:27:00\",\"drawName\":\"null\",\"winningNo\":\"Five(5)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"01:02:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"00:37:00\",\"drawName\":\"null\",\"winningNo\":\"Eight(8)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"00:22:00\",\"drawName\":\"null\",\"winningNo\":\"Eight(8)\"}],\"resultDate\":\"17-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"23:57:00\",\"drawName\":\"null\",\"winningNo\":\"Four(4)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"23:32:00\",\"drawName\":\"null\",\"winningNo\":\"Five(5)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"23:07:00\",\"drawName\":\"null\",\"winningNo\":\"Three(3)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"22:42:00\",\"drawName\":\"null\",\"winningNo\":\"Four(4)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"22:17:00\",\"drawName\":\"null\",\"winningNo\":\"Three(3)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"21:52:00\",\"drawName\":\"null\",\"winningNo\":\"Zero(0)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"21:27:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"21:02:00\",\"drawName\":\"null\",\"winningNo\":\"Two(2)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"20:37:00\",\"drawName\":\"null\",\"winningNo\":\"Three(3)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"20:12:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"19:47:00\",\"drawName\":\"null\",\"winningNo\":\"Three(3)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"6\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"19:22:00\",\"drawName\":\"null\",\"winningNo\":\"Nine(9)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"18:57:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"18:32:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"18:07:00\",\"drawName\":\"null\",\"winningNo\":\"Six(6)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"17:42:00\",\"drawName\":\"null\",\"winningNo\":\"Six(6)\"},{\"matchInfo\":[{\"amount\":\"2.50\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 1\"}],\"drawTime\":\"17:17:00\",\"drawName\":\"null\",\"winningNo\":\"Seven(7)\"}],\"resultDate\":\"16-11-2016\"}],\"lastDrawId\":146009,\"gameCode\":\"Zerotonine\"},{\"resultData\":[{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 6\"}],\"drawTime\":\"19:45:00\",\"winningNo\":\"8,15,16,20,21,27\",\"bonusNo\":\"1\",\"drawName\":\"null\"}],\"resultDate\":\"18-02-2015\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"5000.00\",\"noOfWinners\":\"2\",\"prizeRank\":1,\"match\":\"Match 6\"}],\"drawTime\":\"19:45:00\",\"winningNo\":\"9,15,24,29,32,33\",\"bonusNo\":\"36\",\"drawName\":\"null\"}],\"resultDate\":\"14-02-2015\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 6\"}],\"drawTime\":\"19:45:00\",\"winningNo\":\"4,7,13,19,20,31\",\"bonusNo\":\"32\",\"drawName\":\"null\"}],\"resultDate\":\"07-02-2015\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"5000.00\",\"noOfWinners\":\"2\",\"prizeRank\":1,\"match\":\"Match 6\"}],\"drawTime\":\"19:45:00\",\"winningNo\":\"5,8,12,22,26,29\",\"bonusNo\":\"23\",\"drawName\":\"null\"}],\"resultDate\":\"31-01-2015\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 6\"}],\"drawTime\":\"19:45:00\",\"winningNo\":\"17,21,23,28,31,32\",\"bonusNo\":\"2\",\"drawName\":\"null\"}],\"resultDate\":\"10-12-2014\"}],\"lastDrawId\":33,\"gameCode\":\"ZimLottoBonusTwoFree\"},{\"resultData\":[{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Thu1\",\"winningNo\":\"12,5,13,9,20,16,3,18,4,7\"}],\"resultDate\":\"17-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Wed2\",\"winningNo\":\"20,8,4,10,18,5,13,19,11,16\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Wed1\",\"winningNo\":\"8,13,18,7,17,15,5,1,20,3\"}],\"resultDate\":\"16-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Tue2\",\"winningNo\":\"11,5,8,9,13,15,10,19,2,3\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Tue1\",\"winningNo\":\"11,8,18,17,3,1,12,4,14,6\"}],\"resultDate\":\"15-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Mon2\",\"winningNo\":\"14,2,11,20,6,15,9,1,8,19\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Mon1\",\"winningNo\":\"1,19,13,11,9,8,18,2,7,16\"}],\"resultDate\":\"14-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sun2\",\"winningNo\":\"7,5,12,16,9,14,19,13,6,20\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sun1\",\"winningNo\":\"12,8,1,9,16,3,2,10,5,4\"}],\"resultDate\":\"13-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sat2\",\"winningNo\":\"5,8,12,1,19,10,13,7,2,18\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sat1\",\"winningNo\":\"11,9,15,8,17,2,4,6,1,20\"}],\"resultDate\":\"12-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Fri2\",\"winningNo\":\"14,3,11,1,15,17,10,19,5,6\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Fri1\",\"winningNo\":\"14,16,1,19,13,20,2,9,7,8\"}],\"resultDate\":\"11-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Thu2\",\"winningNo\":\"2,1,11,4,14,8,15,20,7,17\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Thu1\",\"winningNo\":\"20,16,11,10,19,12,15,18,5,17\"}],\"resultDate\":\"10-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Wed2\",\"winningNo\":\"9,1,6,15,16,17,19,4,11,7\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Wed1\",\"winningNo\":\"2,11,4,17,3,20,6,18,8,19\"}],\"resultDate\":\"09-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"3\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Tue2\",\"winningNo\":\"2,15,1,4,18,9,3,11,6,10\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"2\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Tue1\",\"winningNo\":\"2,7,15,1,12,18,14,19,8,4\"}],\"resultDate\":\"08-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Mon2\",\"winningNo\":\"20,19,10,4,15,6,17,5,3,18\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Mon1\",\"winningNo\":\"14,1,13,8,4,2,3,10,17,20\"}],\"resultDate\":\"07-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sun2\",\"winningNo\":\"6,10,8,12,13,11,4,7,15,18\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sun1\",\"winningNo\":\"9,11,13,10,4,7,14,3,8,19\"}],\"resultDate\":\"06-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sat2\",\"winningNo\":\"12,19,20,8,1,7,2,5,11,13\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sat1\",\"winningNo\":\"6,18,16,14,17,4,5,1,20,7\"}],\"resultDate\":\"05-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"1\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Fri2\",\"winningNo\":\"5,7,18,19,12,13,11,14,16,6\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Friday1\",\"winningNo\":\"19,7,3,14,11,17,10,18,2,6\"}],\"resultDate\":\"04-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"833333.33\",\"noOfWinners\":\"3\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"1\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Thrusday2\",\"winningNo\":\"1,2,3,4,5,6,7,8,9,10\"},{\"matchInfo\":[{\"amount\":\"357142.86\",\"noOfWinners\":\"7\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"1\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Thrusday1\",\"winningNo\":\"1,2,3,4,5,6,7,8,9,10\"}],\"resultDate\":\"03-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"2\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Wednesday2\",\"winningNo\":\"4,5,9,14,13,6,11,12,3,2\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"8\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Wednesday1\",\"winningNo\":\"3,2,18,1,4,6,15,11,14,8\"}],\"resultDate\":\"02-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Tuesday2\",\"winningNo\":\"8,5,13,15,14,17,4,9,19,18\"}],\"resultDate\":\"01-11-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Monday2\",\"winningNo\":\"14,4,2,3,11,5,10,15,12,8\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Monday1\",\"winningNo\":\"20,9,12,17,19,10,6,11,5,18\"}],\"resultDate\":\"31-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sunday2\",\"winningNo\":\"6,14,7,18,12,10,3,2,5,20\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sunday1\",\"winningNo\":\"17,3,13,20,18,15,1,7,14,6\"}],\"resultDate\":\"30-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Saturday2\",\"winningNo\":\"7,12,17,10,5,1,6,14,2,9\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Saturday1\",\"winningNo\":\"15,8,3,2,17,12,1,16,19,5\"}],\"resultDate\":\"29-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Friday2\",\"winningNo\":\"20,19,12,15,5,14,7,6,10,13\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Friday1\",\"winningNo\":\"13,16,2,15,8,5,6,12,14,17\"}],\"resultDate\":\"28-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Thrusday2\",\"winningNo\":\"14,10,6,17,19,1,2,8,4,3\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Thrusday1\",\"winningNo\":\"7,10,12,3,13,11,18,16,6,17\"}],\"resultDate\":\"27-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Wednesday2\",\"winningNo\":\"5,17,14,8,4,7,6,15,18,19\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Wednesday1\",\"winningNo\":\"11,6,14,3,20,10,17,1,13,2\"}],\"resultDate\":\"26-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Tuesday2\",\"winningNo\":\"7,6,12,11,16,13,15,9,20,5\"}],\"resultDate\":\"25-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Monday2\",\"winningNo\":\"17,2,20,7,10,5,14,12,4,8\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Monday1\",\"winningNo\":\"3,5,4,13,14,19,6,11,10,1\"}],\"resultDate\":\"24-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Sunday2\",\"winningNo\":\"20,18,14,8,17,16,11,2,19,3\"},{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"12:00:00\",\"drawName\":\"Sunday1\",\"winningNo\":\"9,11,10,5,1,17,14,6,19,2\"}],\"resultDate\":\"23-10-2016\"},{\"resultInfo\":[{\"matchInfo\":[{\"amount\":\"2500000.00\",\"noOfWinners\":\"0\",\"prizeRank\":1,\"match\":\"Match 10\"},{\"amount\":\"10000.00\",\"noOfWinners\":\"0\",\"prizeRank\":2,\"match\":\"Match 9\"},{\"amount\":\"2500.00\",\"noOfWinners\":\"0\",\"prizeRank\":3,\"match\":\"Match 8\"},{\"amount\":\"20.00\",\"noOfWinners\":\"0\",\"prizeRank\":4,\"match\":\"Match 7\"}],\"drawTime\":\"18:00:00\",\"drawName\":\"Saturday2\",\"winningNo\":\"7,14,2,18,5,17,16,11,13,12\"}],\"resultDate\":\"22-10-2016\"}],\"lastDrawId\":70,\"gameCode\":\"TenByTwenty\"}],\"responseCode\":0,\"responseMsg\":\"SUCCESS\"}";
            jsonObject = new JSONObject(fake);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
}



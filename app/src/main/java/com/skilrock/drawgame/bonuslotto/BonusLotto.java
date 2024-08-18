package com.skilrock.drawgame.bonuslotto;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

import com.google.android.gms.analytics.Tracker;
import com.skilrock.adapters.GamePageGridAdapter;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.CustomTextViewTop.TextStyles;
import com.skilrock.drawgame.DGPlayActivity;
import com.skilrock.drawgame.ResultScreen;
import com.skilrock.drawgame.StatisticScreen;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.CounterClass;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.TimeCalculator;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BonusLotto extends Fragment implements WebServicesListener {
    private Analytics analytics;
    private View mainView;
    // private JazzyViewPager myPager;
    private GridView itemGrid;
    private int height;
    private int width;
    private double heightForGridChild;
    private int widthForGridChild;
    private double gridWeight = 0.6;// 10%5
    private int actionBarHeight, footerHeight;
    private LinearLayout playNowLay;
    private int tabBarHeight;
    private int deviceDpi;
    private int images[] = new int[]{R.drawable.stats, R.drawable.result};
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS"};
    private ArrayList<IconWithTitle> mainMenuData;
    private ImageView bannerView;
    private RelativeLayout dataLay;
    private String[] winNumber;
    // private RelativeLayout playNow;
    private CustomTextViewTop now;
    private CustomTextViewDown play;
    private CustomTextView nextDraw;
    private String gameName, winningNumbers, currentDrawDateTime,
            drawFreezeTime, gameBannerURL;
    private CounterClass counterClass;
    private JSONObject jsonResult;
    private LinearLayout statView, resultView;
    private CustomTextView playNow;
    private LinearLayout resultBox;
    private RelativeLayout lastDrawLay;
    private Tracker tracker;
    private Activity activity;
    private String gameID;
    private String drawResultDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.BONUS_GAME);
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
    public void onResume() {
        super.onResume();
        gameID = getArguments().getString("gameId");
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
        mainView = inflater.inflate(R.layout.lotto_bonus_fragment, null);
        bindViewIds(mainView);
        bannerView.setImageResource(R.drawable.sub_banner_bonus);
        nextDraw.setTypeface(Config.timerFont);
        SpannableString string = new SpannableString("03:30 :52");
        string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
        string.setSpan(new RelativeSizeSpan(1f), 6, 9, 0);
        nextDraw.setText(string);
        playNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.CLICK, Fields.Label.BUY);
                Intent intent = new Intent(getActivity(), DGPlayActivity.class);
                intent.putExtra("selectedGame", gameName);
                intent.putExtra("isDraws", getArguments().getBoolean("isDraws"));
                startActivity(intent);
                getActivity().overridePendingTransition(
                        GlobalVariables.startAmin, GlobalVariables.endAmin);
            }
        });
        dataLay.setBackgroundColor(getResources().getColor(
                R.color.elec_theme_color));
        getDisplayDetails();
        setWinningNumbersData(winningNumbers);
        // setViewSizes();
//        setCounterDownTimer();
        // imageLoader.displayImage(gameBannerURL, bannerView, options,
        // imageListener);
        itemGrid.setAdapter(new GamePageGridAdapter(getActivity(),
                R.layout.menu_row, (int) heightForGridChild / 2,
                (int) widthForGridChild / 2, mainMenuData, getResources()
                .getColor(R.color.bonus_theme_color)));
        itemGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.CLICK, Fields.Label.BONUS_GAME_STATS);
                        if (GlobalVariables.connectivityExists(getActivity())) {
                            String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                            String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(BonusLotto.this, getResources().getString(R.string.get_stats), url, json).execute();
                        } else {
                            GlobalVariables.showDataAlert(getActivity());
                        }
                        break;
                    case 1:
                        analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.CLICK, Fields.Label.BONUS_GAME_RESULT);
                        if (GlobalVariables.connectivityExists(getActivity())) {
                            String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                            String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(BonusLotto.this, getResources().getString(R.string.get_results), urlRes, jsonRes).execute();
                        } else {

                            GlobalVariables.showDataAlert(getActivity());
                        }
                        break;
                    case 2:
                        Utils.Toast(getActivity(),
                                getResources().getString(R.string.coming_soon));
                        break;
                    case 3:
                        Intent intent1 = new Intent(getActivity(), DGPlayActivity.class);
                        intent1.putExtra("selectedGame", gameName);
                        startActivity(intent1);
                        getActivity().overridePendingTransition(
                                GlobalVariables.startAmin, GlobalVariables.endAmin);
                        break;

                    default:
                        break;
                }
            }
        });
        statView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.CLICK, Fields.Label.BONUS_GAME_STATS);

                String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                new DGETask(BonusLotto.this, getResources().getString(R.string.get_stats), url, json).execute();
            }
        });
        resultView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.CLICK, Fields.Label.BONUS_GAME_RESULT);

                String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                new DGETask(BonusLotto.this, getResources().getString(R.string.get_results), urlRes, jsonRes).execute();
            }
        });
        return mainView;
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
    private int winningNoSize;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void bindViewIds(View view) {
        ((ImageView) view.findViewById(R.id.imageView1)).setColorFilter(
                Color.WHITE, Mode.SRC_IN);
        lastDrawLay = (RelativeLayout) view.findViewById(R.id.last_draw);
        resultBox = (LinearLayout) view.findViewById(R.id.result_box);
        play = (CustomTextViewDown) view.findViewById(R.id.play);
        now = (CustomTextViewTop) view.findViewById(R.id.now);
        play.setTextStyle(CustomTextViewDown.TextStyles.BOLD);
        now.setTextStyle(TextStyles.LIGHT);
        statView = (LinearLayout) view.findViewById(R.id.stats_view);
        resultView = (LinearLayout) view.findViewById(R.id.result_view);
        playNow = (CustomTextView) view.findViewById(R.id.buy_now);
        playNowLay = (LinearLayout) view.findViewById(R.id.aa);
        nextDraw = (CustomTextView) view.findViewById(R.id.next_draw);
        itemGrid = (GridView) mainView.findViewById(R.id.item_grid);
        bannerView = (ImageView) mainView.findViewById(R.id.banner);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);

        if (!winningNumbers.trim().equals("") && !drawResultDate.trim().equals(""))
            ((CustomTextView) mainView.findViewById(R.id.draw_date))
                    .setText(GlobalVariables.formatDateFromCal(activity, drawResultDate, VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DATE_FORMAT) + " HH:mm:ss"));
        else
            dataLay.setVisibility(View.GONE);

//        ((CustomTextView) mainView.findViewById(R.id.draw_date))
//                .setText(GlobalVariables.formatDateFromCal(getActivity(), getArguments().getString("lastDrawTime"), "dd-MM-yyyy"));

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
        footerHeight = /*
                         * (int) (40 *
						 * getResources().getDisplayMetrics().density)
						 */0;
        tabBarHeight = (int) getResources().getDimension(R.dimen.tabBarHeight);
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        deviceDpi = displaymetrics.densityDpi;
        height = displaymetrics.heightPixels - result - actionBarHeight
                - footerHeight - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Utils.logPrint("data:-" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    private void setDispMetrices(GradientDrawable drawable, int hw) {
        drawable.setSize(hw,
                hw);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setWinningNumbersData(String winningNumbers) {
        Utils.consolePrint("======" + winningNumbers);
        try {
            String[] data = winningNumbers.split(",");
            if (data.length > 4) {
                LinearLayout winNoLay = (LinearLayout) mainView
                        .findViewById(R.id.win_num_lay);
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
                int ballHeight = (int) (height * 0.166 - lastDrawLay.getMeasuredHeight());
                if (ballHeight < txtSize)
                    txtSize = ballHeight - GlobalVariables.getPx(2, getActivity());
                winningNoSize = txtSize;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(winningNoSize, winningNoSize);
                layoutParams.setMargins(GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()), GlobalVariables.getPx(2, getActivity()));
                for (int i = 0; i < winNoLay.getChildCount(); i++) {
                    CustomTextView textViewWin = (CustomTextView) winNoLay
                            .getChildAt(i);
                    textViewWin.setText(winNumber[i]);
                    textViewWin.setLayoutParams(layoutParams);
                    textViewWin.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            winningNoSize / 2);
                }
            }
        } catch (Exception e) {
            Utils.consolePrint(e + "");
        }
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

//    private ImageLoader imageLoader = ImageLoader.getInstance();
//    //    private DisplayImageOptions options = new DisplayImageOptions.Builder()
////            .showImageOnFail(R.drawable.main_banner_bonus)
////            .showStubImage(R.drawable.main_banner_bonus)
////            .showImageForEmptyUri(R.drawable.main_banner_bonus).cacheInMemory()
////            .cacheOnDisc().build();
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

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "GET_STATS":
                if (resultData != null) {
                    try {

                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.STATS, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(getActivity(),
                                    StatisticScreen.class);
                            statsIntent.putExtra("gameId", gameID);
                            statsIntent.putExtra("jsonData", resultData.toString());
                            startActivity(statsIntent);
                            getActivity().overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            analytics.sendAll(Fields.Category.BONUS_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }

                break;
            case "GET_RESULTS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.RESULT, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(getActivity(),
                                    ResultScreen.class);
                            statsIntent.putExtra("gameId", gameID);
                            statsIntent.putExtra("jsonData", jsonResult.getJSONArray("gameData").toString());
                            startActivity(statsIntent);
                            getActivity().overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(getActivity(), jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }

                break;
        }
    }
}

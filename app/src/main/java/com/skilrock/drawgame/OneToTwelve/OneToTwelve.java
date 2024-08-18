package com.skilrock.drawgame.OneToTwelve;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

//import com.nostra13.universalimageloader.core.ImageLoader;
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
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.drawgame.DGPlayActivity;
import com.skilrock.drawgame.ResultScreen;
import com.skilrock.drawgame.StatisticScreen;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.CounterClass;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.TimeCalculator;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class OneToTwelve extends Fragment implements WebServicesListener {
    private Analytics analytics;

    private View mainView;
    // private JazzyViewPager myPager;
    private CustomTextView fastNum;
    private CustomTextView fastText;
    private GridView itemGrid;
    private int height;
    private int width;
    private double heightForGridChild, winNoLayheight;
    private int widthForGridChild;
    private double gridWeight = 0.6;//
    private int actionBarHeight, footerHeight;
    private int tabBarHeight;
    private LinearLayout playNowLay;
    private int deviceDpi;
    private int images[] = new int[]{R.drawable.stats, R.drawable.result};
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS"};
    private ArrayList<IconWithTitle> mainMenuData;
    private ImageView bannerView;
    private RelativeLayout dataLay;
    private CustomTextViewDown play;
    private CustomTextViewTop now;
    private CustomTextView nextDraw;
    private String gameName, winningNumbers, currentDrawDateTime,
            drawFreezeTime, gameBannerURL;
    private CounterClass counterClass;
    private JSONObject jsonData;
    private JSONObject jsonResult;
    private LinearLayout statView, resultView;
    private CustomTextView playNow;
    private LinearLayout resultBox;
    private RelativeLayout lastDrawLay;
    private Activity activity;
    private GlobalPref globalPref;
    private CustomTextView no_result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.FAST_GAME);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        globalPref = GlobalPref.getInstance(OneToTwelve.this.getActivity());

        gameName = getArguments().getString("gameName");
        winningNumbers = getArguments().getString("lastDrawResult");
        currentDrawDateTime = getArguments().getString("currentDrawDateTime");
        drawFreezeTime = getArguments().getString("currentDrawFreezeTime");
        gameBannerURL = getArguments().getString("bannerUrl");
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
        mainView = inflater.inflate(R.layout.one_to_twelve_fragment, null);
        bindViewIds(mainView);

        if (globalPref.getCountry().equalsIgnoreCase("ZIM"))
            bannerView.setImageResource(R.drawable.sub_banner_one_two_12);
        else
            bannerView.setImageResource(R.drawable.sub_banner_fast);

        nextDraw.setTypeface(Config.timerFont);
        SpannableString string = new SpannableString("03:30 :52");
        string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
        string.setSpan(new RelativeSizeSpan(1f), 6, 9, 0);
        nextDraw.setText(string);
        setCounterDownTimer();

        playNow.setOnClickListener(listener);
//        playNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DGPlayActivity.class);
//                intent.putExtra("selectedGame", gameName);
//                intent.putExtra("isDraws", getArguments().getBoolean("isDraws"));
//                startActivity(intent);
//                getActivity().overridePendingTransition(
//                        GlobalVariables.startAmin, GlobalVariables.endAmin);
//            }
//        });

        dataLay.setBackgroundColor(getResources().getColor(
                R.color.scratch_theme_color));
        getDisplayDetails();
        // setViewSizes();
        //last draw  result
        if (!winningNumbers.equalsIgnoreCase("")) {
            no_result.setVisibility(View.GONE);
            resultBox.setVisibility(View.VISIBLE);
            String fastResText = winningNumbers.contains("(") ? winningNumbers.substring(winningNumbers.indexOf("(") + 1, winningNumbers.indexOf(")")) : winningNumbers;
            String fastResNum = winningNumbers.split("\\(")[0];
            fastNum.setText(fastResText.length() == 1 ? 0 + "" + fastResText : fastResText);
            fastText.setText(fastResNum);
        } else {
            no_result.setVisibility(View.VISIBLE);
            resultBox.setVisibility(View.GONE);
        }
        // imageLoader.displayImage(gameBannerURL, bannerView, options,
        // imageListener);
        itemGrid.setAdapter(new GamePageGridAdapter(getActivity(), R.layout.menu_row, (int) heightForGridChild / 2, (int) widthForGridChild / 2, mainMenuData, getResources().getColor(R.color.fast_theme_color)));
        itemGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.FAST_GAME_STATS);
                        if (GlobalVariables.connectivityExists(getActivity())) {
                            String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                            String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(OneToTwelve.this, "GET_STATS", url, json).execute();
                        } else {

                            GlobalVariables.showDataAlert(getActivity());
                        }
                        break;
                    case 1:
                        analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.FAST_GAME_RESULT);
                        if (GlobalVariables.connectivityExists(getActivity())) {
                            String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                            String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                            new DGETask(OneToTwelve.this, "GET_RESULTS", urlRes, jsonRes).execute();
                        } else {

                            GlobalVariables.showDataAlert(getActivity());
                        }
                        break;
                    case 2:
                        Utils.Toast(getActivity(),
                                "Coming Soon!");
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

        statView.setOnClickListener(listener);
        resultView.setOnClickListener(listener);


//        statView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.FAST_GAME_STATS);
//
//                String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
//                String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
//                new DGETask(OneToTwelve.this, "GET_STATS", url, json).execute();
//            }
//        });
//        resultView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.FAST_GAME_RESULT);
//
//                String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
//                String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
//                new DGETask(OneToTwelve.this, "GET_RESULTS", urlRes, jsonRes).execute();
//            }
//        });
        return mainView;
    }

    View.OnClickListener commonClickListener = new View.OnClickListener() {

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

    private void bindViewIds(View view) {
        ((ImageView) view.findViewById(R.id.imageView1)).setColorFilter(
                Color.WHITE, PorterDuff.Mode.SRC_IN);
        lastDrawLay = (RelativeLayout) view.findViewById(R.id.linearLayout1);
        resultBox = (LinearLayout) view.findViewById(R.id.result_box);
        play = (CustomTextViewDown) view.findViewById(R.id.play);
        now = (CustomTextViewTop) view.findViewById(R.id.now);
        play.setTextStyle(CustomTextViewDown.TextStyles.BOLD);
        now.setTextStyle(CustomTextViewTop.TextStyles.LIGHT);
        statView = (LinearLayout) view.findViewById(R.id.stats_view);
        resultView = (LinearLayout) view.findViewById(R.id.result_view);
        playNow = (CustomTextView) view.findViewById(R.id.buy_now);
        playNowLay = (LinearLayout) view.findViewById(R.id.aa);
        nextDraw = (CustomTextView) view.findViewById(R.id.next_draw);
        itemGrid = (GridView) mainView.findViewById(R.id.item_grid);
        bannerView = (ImageView) mainView.findViewById(R.id.banner);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);
        fastNum = (CustomTextView) mainView
                .findViewById(R.id.fast_res_num_text);
        fastText = (CustomTextView) mainView.findViewById(R.id.fast_res_text);
        no_result = (CustomTextView) mainView.findViewById(R.id.no_result);
        ((CustomTextView) mainView.findViewById(R.id.draw_date))
                .setText(GlobalVariables.formatDateFromCal(getActivity(), getArguments().getString("lastDrawTime"), VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DATE_FORMAT) + " HH:mm:ss"));

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
        // if (GlobalVariables.onTablet(getActivity())) {
        // } else {
        // switch (deviceDpi) {
        // case DisplayMetrics.DENSITY_LOW:
        // fastNum.setTextSize(30);
        // fastText.setTextSize(25);
        // break;
        // case DisplayMetrics.DENSITY_MEDIUM:
        // fastNum.setTextSize(40);
        // fastText.setTextSize(35);
        // break;
        // case DisplayMetrics.DENSITY_HIGH:
        // fastNum.setTextSize(50);
        // fastText.setTextSize(45);
        // break;
        // case DisplayMetrics.DENSITY_XHIGH:
        // fastNum.setTextSize(55);
        // fastText.setTextSize(50);
        // break;
        // case DisplayMetrics.DENSITY_XXHIGH:
        // fastNum.setTextSize(60);
        // fastText.setTextSize(55);
        // break;
        // case DisplayMetrics.DENSITY_XXXHIGH:
        // fastNum.setTextSize(75);
        // fastText.setTextSize(70);
        // break;
        // default:
        // break;
        // }
        // }
        height = displaymetrics.heightPixels - result - actionBarHeight
                - tabBarHeight - footerHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        winNoLayheight = (height * 0.166);
        resultBox.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        lastDrawLay.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int ballHeight = (int) (height * 0.166 - lastDrawLay.getMeasuredHeight()) - GlobalVariables.getPx((int) getResources().getDimension(R.dimen.fast_frag_bal_padding), getActivity());
        GradientDrawable drawableWin = (GradientDrawable) fastNum
                .getBackground().mutate();
        drawableWin.setSize(ballHeight, ballHeight);

        fastNum.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (float) ((float) ballHeight / 2));
        fastText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                (float) ((float) winNoLayheight / 3));
        Log.i("data", ballHeight + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    // private void setViewSizes() {
    // LinearLayout winNoLay = (LinearLayout) mainView
    // .findViewById(R.id.win_num_lay);
    // for (int i = 0; i < winNoLay.getChildCount(); i++) {
    // CustomTextView textViewWin = (CustomTextView) winNoLay.getChildAt(i);
    // GradientDrawable drawableWin = (GradientDrawable) textViewWin
    // .getBackground().mutate();
    // drawableMac.setColor(Color.parseColor(""));
    // if (onTablet()) {
    // setDispMetrices(drawableMac, 60);
    // setDispMetrices(drawableWin, 60);
    // } else {
    //
    // if (deviceDpi == DisplayMetrics.DENSITY_LOW) {
    // setDispMetrices(drawableMac, 20);
    // setDispMetrices(drawableWin, 20);
    // }
    // if (deviceDpi == DisplayMetrics.DENSITY_MEDIUM) {
    // setDispMetrices(drawableMac, 25);
    // setDispMetrices(drawableWin, 25);
    // }
    // if (deviceDpi == DisplayMetrics.DENSITY_HIGH) {
    // setDispMetrices(drawableMac, 32);
    // setDispMetrices(drawableWin, 32);
    // }
    // if (deviceDpi == DisplayMetrics.DENSITY_XHIGH) {
    // setDispMetrices(drawableMac, 45);
    // setDispMetrices(drawableWin, 45);
    // }
    // if (deviceDpi == DisplayMetrics.DENSITY_XXHIGH) {
    // setDispMetrices(drawableMac, 55);
    // setDispMetrices(drawableWin, 55);
    // }
    // if (deviceDpi == DisplayMetrics.DENSITY_XXXHIGH) {
    // setDispMetrices(drawableMac, 65);
    // setDispMetrices(drawableWin, 65);
    // }
    // }
    // }
    // }
    //
    // private void setDispMetrices(GradientDrawable drawable, int hw) {
    // drawable.setSize(GlobalVariables.getPx(hw, getActivity()),
    // GlobalVariables.getPx(hw, getActivity()));
    // }
    //
    // private boolean onTablet() {
    // int intScreenSize =
    // getActivity().getResources().getConfiguration().screenLayout
    // & Configuration.SCREENLAYOUT_SIZE_MASK;
    // return (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) // LARGE
    // || (intScreenSize == Configuration.SCREENLAYOUT_SIZE_LARGE + 1); //
    // Configuration.SCREENLAYOUT_SIZE_XLARGE
    // }
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
//    private ImageLoadingListener imageListener = new ImageDisplayListener();

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "GET_STATS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.STATS, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(getActivity(),
                                    StatisticScreen.class);
                            statsIntent.putExtra("gameId", Config.oneToTwelve);
                            statsIntent.putExtra("jsonData", resultData.toString());
                            startActivity(statsIntent);
                            getActivity().overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else {
                            analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.STATS, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(getActivity(), jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.STATS, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }
                break;
            case "GET_RESULTS":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.RESULT, Fields.Label.SUCCESS);

                            Intent statsIntent = new Intent(getActivity(), ResultScreen.class);
                            statsIntent.putExtra("gameId", Config.oneToTwelve);
                            statsIntent.putExtra("jsonData", jsonResult.getJSONArray("gameData").toString());
                            startActivity(statsIntent);
                            getActivity().overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else {
                            analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(getActivity(), jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                        e.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(getActivity());
                    }
                } else {
                    analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.RESULT, Fields.Label.FAILURE);

                    dialog.dismiss();
                    GlobalVariables.showServerErr(getActivity());
                }

                break;
        }
    }

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

    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.stats_view:
                    String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawGameStatistics";
                    String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(OneToTwelve.this, "GET_STATS", url, json).execute();
                    break;
                case R.id.result_view:
                    String urlRes = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/getDrawResults";
                    String jsonRes = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.DGE_MER_CODE) + "\", \"gameCode\": \"-1\"}";
                    new DGETask(OneToTwelve.this, "GET_RESULTS", urlRes, jsonRes).execute();
                    break;
                case R.id.buy_now:
                    Intent intent = new Intent(getActivity(), DGPlayActivity.class);
                    intent.putExtra("selectedGame", gameName);
                    intent.putExtra("isDraws", getArguments().getBoolean("isDraws"));
                    startActivity(intent);
                    getActivity().overridePendingTransition(
                            GlobalVariables.startAmin, GlobalVariables.endAmin);
                    break;
            }
        }
    };


}

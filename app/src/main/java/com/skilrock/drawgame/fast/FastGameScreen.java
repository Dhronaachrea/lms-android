package com.skilrock.drawgame.fast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.skilrock.adapters.DrawAdapater;
import com.skilrock.adapters.FastGameAdapter;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.DrawData;
import com.skilrock.config.Config;
import com.skilrock.config.DismissListener;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.BetDialog;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextView.TextStyles;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawDialog;
import com.skilrock.customui.ExpandableGridView;
import com.skilrock.drawgame.DialogKeyListener;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.TicketDescActivity;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FastGameScreen extends Fragment implements DismissListener, WebServicesListener {
    private static Activity activity;
    private CustomTextView buyNow/* , okay , selectNos */;
    private CustomTextViewDown scrollTextView;
    private ScrollView scrollView;
    private ExpandableGridView gridView;
    private boolean isVisible, isFav = true, isLast = true;
    private LinearLayout buyOptions, lastLay, lastLaySub;
    private LayoutParams params, buyLP;
    private int key;
    private CustomCheckedTextView favNos, lastPckd, quickPick, selectNos;
    private int[] textSize = new int[2];
    private LinearLayout firstSelectedNosLay, secondSelectedNosLay;
    private RelativeLayout nolSn;
    private boolean isDrawSecond;
    // private String[] numArr = new String[] { "22", "32", "12", "36", "22",
    // "32", "12", "36" };
    private String[] numArr = new String[]{"22", "32", "12", "36", "19",
            "28", "45", "62", "88", "55", "63", "44", "25"};
    private int totalBallWidth, ballWidth, ballHeight;
    private int selectedNosParentHeight, selectedNosParentDefaultHeight = 120,
            noOfBallsInSingleLine = 5;
    private LinearLayout qpNoLines, restNoLines;
    private LinkedHashMap<String, DrawData> drawData;
    private LinkedHashMap<String, BetTypeBean> betTypeData;
    private CustomTextView changeBetType;
    private ImageView advanceDrawMenu;
    private CustomTextView drawName, restDraws, betName;
    private CustomTextViewTop drawTime;
    private DrawDialog darwDialog;
    private BetDialog betDialog;
    private boolean showOne;
    private int actionBarHeight;
    private int height;
    private double widthForGridChild;
    private double heightForGridChild;
    private int width;
    private LayoutParams firstParentParms, lastParentParms;
    private int noOfLay;
    private int check;
    public static CustomTextView selectedNos, qpNos, noOfLines, finalAmt, unitPrice;
    private ImageView dec, inc;
    private ImageView qpDec, qpInc;
    public static BetTypeBean selectedBetBean;
    public static double unitPriceVal;
    public static int numberSelected;
    private JSONObject gameObject;
    private RelativeLayout betLayout;
    private ImageView edit;
    private ArrayList<DrawData> drawDatas;
    private boolean isSingleSelected;
    public static ArrayList<DrawData> finalDrawDatas;
    public static long noOfLinesVal;
    public static double totalAmt;
    private DownloadDialogBox dBox;
    private JSONObject jsonResult;
    private Context context;
    private JSONObject ticketSaleDta;
    private String[] noPckdArr;
    private SpannableString spannableString;
    private Analytics analytics;

    //onlongpress
    private boolean isIncremented = false;
    private boolean isDecremented = false;
    private Handler onLongPressCounterHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        context = getActivity();
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        onLongPressCounterHandler = new Handler();
        analytics.setScreenName(Fields.Screen.FAST_GAME_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        spannableString = new SpannableString(getResources().getString(R.string.play_now_text));
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, 0);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 4, spannableString.length(), 0);
        FastGameAdapter.numbersSelected = new int[10];
        if (GlobalVariables.onTablet(activity)) {
            textSize[0] = 80;
            textSize[1] = 26;
            selectedNosParentDefaultHeight = 150;
        } else {
            switch (GlobalVariables.getDensity(activity)) {
                case DisplayMetrics.DENSITY_LOW://120
                    textSize[0] = 12;
                    textSize[1] = 10;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM://160
                    textSize[0] = 22;
                    textSize[1] = 14;
                    break;
                case DisplayMetrics.DENSITY_HIGH://240
                    textSize[0] = 32;
                    textSize[1] = 15;
                    break;
                case DisplayMetrics.DENSITY_XHIGH://320
                    textSize[0] = 37;
                    textSize[1] = 17;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH://480
                    textSize[0] = 40;
                    textSize[1] = 20;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH://640
                    textSize[0] = 42;
                    textSize[1] = 20;
                    break;
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fast_game_screen, null);
        bindViewIds(view);
        updateTheme();
        edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openGrid(gridView, true);
            }
        });
        getDisplayDetails();
        parseJson();
        drawDatas = new ArrayList<DrawData>(drawData.values());
        darwDialog = new DrawDialog(activity, drawDatas, this, false);
        if (drawData.size() > 0) {
            DrawData preDrawData = (DrawData) drawData.entrySet().iterator().next()
                    .getValue();

            if (!preDrawData.getDrawName().equalsIgnoreCase("N/A")) {
                drawName.setText(preDrawData.getDrawName());
                drawTime.setText(preDrawData.getDrawDateTime());
                drawTime.setVisibility(View.VISIBLE);
            } else {
                drawTime.setVisibility(View.GONE);
                drawName.setText(preDrawData.getDrawDateTime());
            }
//            drawName.setText(preDrawData.getDrawName());
//            if (preDrawData.getDrawDateTime().equals("")) {
//                drawTime.setVisibility(View.GONE);
//            } else {
//                drawTime.setVisibility(View.VISIBLE);
//            }
            drawTime.setText(preDrawData.getDrawDateTime());
        } else {
            drawName.setText(activity.getString(R.string.no_draws_text));
            drawTime.setVisibility(View.GONE);
        }
        selectedBetBean = (BetTypeBean) betTypeData.entrySet().iterator()
                .next().getValue();
        betName.setText(selectedBetBean.getBetDisplayName());
        if (betTypeData.size() <= 1) {
            betLayout.setVisibility(View.GONE);
        } else {
            betLayout.setVisibility(View.VISIBLE);
        }
        updateBetValiadtion(selectedBetBean);
        restDraws.setTextStyle(TextStyles.BOLD);
        restDraws.setVisibility(View.INVISIBLE);
        betDialog = new BetDialog(activity, new ArrayList<BetTypeBean>(
                betTypeData.values()), betName);
        betDialog.setCancelable(false);
        betDialog.setOnKeyListener(new DialogKeyListener(betDialog));

        qpInc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getMinNo(),
                        selectedBetBean.getMaxNo(), 1, R.id.qp_inc, qpNos,
                        false);
            }
        });
        qpDec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getMinNo(),
                        selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                        false);
            }
        });
        inc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.inc, unitPrice,
                        true);
            }
        });
        dec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                        true);
            }
        });
        advanceDrawMenu.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showOne = false;
                darwDialog = new DrawDialog(activity, drawDatas,
                        FastGameScreen.this, false);
                darwDialog.setCancelable(false);
                darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));

                darwDialog.show();
                return false;
            }
        });

        betLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (betTypeData.size() <= 1) {
                } else {
                    betDialog.show();
                }
//                betDialog.show();
            }
        });
        betDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                betName.setText(BetDialog.selectedBet);
                selectedBetBean = betTypeData.get(betName.getText());
                updateBetValiadtion(selectedBetBean);
            }
        });


        qpInc.setOnTouchListener(incDecTouchListener);
        qpDec.setOnTouchListener(incDecTouchListener);
        qpInc.setOnLongClickListener(incDecLongClickListener);
        qpDec.setOnLongClickListener(incDecLongClickListener);

        lastLay.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, selectedNosParentHeight));
        lastLaySub.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, selectedNosParentHeight));
        lastLaySub.setGravity(Gravity.LEFT);
        lastLaySub.setOrientation(LinearLayout.VERTICAL);
        favNos.setOnClickListener(commonListener);
        lastPckd.setOnClickListener(commonListener);
        quickPick.setOnClickListener(commonListener);
        selectNos.setOnClickListener(commonListener);
        gridView.setExpanded(true);
        buyNow.setOnClickListener(new DebouncedOnClickListener(500) {
            @Override
            public void onDebouncedClick(View v) {
                {
                    if (buyNow.getText().toString().equalsIgnoreCase("ok")) {
                        for (int i = 0; i < FastGameAdapter.numbersSelected.length; i++) {
                            if (FastGameAdapter.numbersSelected[i] > 0) {
                                isSingleSelected = true;
                                break;
                            } else {
                                isSingleSelected = false;
                            }
                        }
                        if (isSingleSelected) {
                            selectedBetBean.setCurrentNos(getPickedNo());
                            openGrid(gridView, false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateBallLay(getPickedNo());
                                }
                            }, 700);
                            numberSelected = getNoSelected(R.id.select_nos);
                            gameAmtCalculation(selectedBetBean);
                        } else {
                            Utils.Toast(activity, getResources().getString(R.string.plz_sel_at_lest_five) + " " + selectedBetBean.getMinNo() + " Number(s)");
                        }
                    } else {
                        if (drawName.getText().toString().equals(activity.getString(R.string.no_draws_text))) {
                            Utils.Toast(activity, activity.getString(R.string.no_draws_text));
                            Intent intent = new Intent(activity,
                                    MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            activity.overridePendingTransition(
                                    GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                        } else {
                            if (!UserInfo.isLogin(activity)) {
                                startActivityForResult(new Intent(activity,
                                        LoginActivity.class), 100);
                                activity.overridePendingTransition(GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                            } else {
                                purchaseNow();
                            }
                        }
                    }
                }
            }
        });
        manageTabs();
        return view;
    }

    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };


    private LinearLayout unitLay;

    private void bindViewIds(View view) {
        edit = (ImageView) view.findViewById(R.id.edit);
        betLayout = (RelativeLayout) view.findViewById(R.id.bet_lay);
        unitLay = (LinearLayout) view.findViewById(R.id.unit_lay);
        qpDec = (ImageView) view.findViewById(R.id.qp_dec);
        qpInc = (ImageView) view.findViewById(R.id.qp_inc);
        dec = (ImageView) view.findViewById(R.id.dec);
        inc = (ImageView) view.findViewById(R.id.inc);
        unitPrice = (CustomTextView) view.findViewById(R.id.unit_price);
        finalAmt = (CustomTextView) view.findViewById(R.id.final_amt);
        selectedNos = (CustomTextView) view.findViewById(R.id.selected_nos);
        qpNos = (CustomTextView) view.findViewById(R.id.qp_nos);
        noOfLines = (CustomTextView) view.findViewById(R.id.no_of_lines);
        nolSn = (RelativeLayout) view.findViewById(R.id.nol_sn);
        qpNoLines = (LinearLayout) view.findViewById(R.id.qp_no_lines);
        restNoLines = (LinearLayout) view.findViewById(R.id.rest_no_lines);
        firstSelectedNosLay = (LinearLayout) view
                .findViewById(R.id.first_selected_nos);
        secondSelectedNosLay = (LinearLayout) view
                .findViewById(R.id.second_selected_nos);
        lastLay = (LinearLayout) view.findViewById(R.id.last_lay);
        lastLaySub = (LinearLayout) view.findViewById(R.id.last_lay_sub);
        buyNow = (CustomTextView) view.findViewById(R.id.buy_now);
        buyOptions = (LinearLayout) view.findViewById(R.id.four_opns);
        // selectNos = (CustomTextView) findViewById(R.id.select_nos);
        scrollTextView = (CustomTextViewDown) view.findViewById(R.id.scroll);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        gridView = (ExpandableGridView) view.findViewById(R.id.no_grid);
        // okay = (CustomTextView) findViewById(R.id.okay);
        favNos = (CustomCheckedTextView) view.findViewById(R.id.fav_nos);
        lastPckd = (CustomCheckedTextView) view.findViewById(R.id.last_picked);
        quickPick = (CustomCheckedTextView) view.findViewById(R.id.quick_pick);
        selectNos = (CustomCheckedTextView) view.findViewById(R.id.select_nos);
        advanceDrawMenu = (ImageView) view.findViewById(R.id.advance_menu);
        changeBetType = (CustomTextView) view.findViewById(R.id.change_bet_type);
        drawName = (CustomTextView) view.findViewById(R.id.draw_name);
        drawTime = (CustomTextViewTop) view.findViewById(R.id.draw_time);
        restDraws = (CustomTextView) view.findViewById(R.id.rest_draws);
        betName = (CustomTextView) view.findViewById(R.id.bet_name);
        restNoLines.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < DataSource.numbers.length; i++)
            DataSource.numbers[i] = 0;
        activity.overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    private final void focusOnView() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, unitLay.getBottom());
            }
        });
    }

    private OnClickListener commonListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            updateUI(view);
        }
    };

    private void updateUI(View view) {
        switch (view.getId()) {
            case R.id.fav_nos:
                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.FAV_NO);
                setEditVisible(View.GONE);
                // Utils.Toast(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000);
                favNos.setChecked(true);
                lastPckd.setChecked(false);
                quickPick.setChecked(false);
                selectNos.setChecked(false);
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.GONE);
                buyNow.setText(spannableString);
                numberSelected = getNoSelected(R.id.fav_nos);
                unitPriceVal = selectedBetBean.getUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, unitPriceVal);
                gameAmtCalculation(selectedBetBean);
                if (selectedBetBean.getFavNos().length == 1) {
                    if (selectedBetBean.getFavNos()[0].equals("")) {
                        isFav = false;
                        manageTabs();
                    } else {
                        isFav = true;
                        updateBallLay(selectedBetBean.getFavNos());
                    }
                } else {
                    isFav = true;
                    updateBallLay(selectedBetBean.getFavNos());
                }
                break;
            case R.id.last_picked:
                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.LAST_PICK);
                setEditVisible(View.GONE);
                // Utils.Toast(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000);
                favNos.setChecked(false);
                lastPckd.setChecked(true);
                quickPick.setChecked(false);
                selectNos.setChecked(false);
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.GONE);
                buyNow.setText(spannableString);
                numberSelected = getNoSelected(R.id.last_picked);
                unitPriceVal = selectedBetBean.getUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, unitPriceVal);
                gameAmtCalculation(selectedBetBean);
                if (selectedBetBean.getLastPicked().length == 1) {
                    if (selectedBetBean.getLastPicked()[0].equals("")) {
                        isLast = false;
                        manageTabs();
                    } else {
                        isLast = true;
                        updateBallLay(selectedBetBean.getLastPicked());
                    }
                } else {
                    isLast = true;
                    updateBallLay(selectedBetBean.getLastPicked());
                }
                break;
            case R.id.quick_pick:
                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.QUICK_PICK);
                setEditVisible(View.GONE);
                // Utils.Toast(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000);
                favNos.setChecked(false);
                lastPckd.setChecked(false);
                quickPick.setChecked(true);
                selectNos.setChecked(false);
                lastLay.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.VISIBLE);
                numberSelected = selectedBetBean.getMinNo();
                unitPriceVal = selectedBetBean.getUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, unitPriceVal);
                gameAmtCalculation(selectedBetBean);
                buyNow.setText(spannableString);
                break;
            case R.id.select_nos:
                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.CLICK, Fields.Label.PICK_NEW);
                if (selectNos.isChecked()) {

                } else {
                    setEditVisible(View.VISIBLE);
                    // Utils.Toast(activity,
                    // ((CustomCheckedTextView) view).getText().toString(),
                    // 1000);
                    favNos.setChecked(false);
                    lastPckd.setChecked(false);
                    quickPick.setChecked(false);
                    selectNos.setChecked(true);
                    lastLay.setVisibility(View.VISIBLE);
                    openGrid(gridView, true);
                    qpNoLines.setVisibility(View.GONE);
                    restNoLines.setVisibility(View.GONE);
                    numberSelected = getNoSelected(R.id.select_nos);
                    unitPriceVal = selectedBetBean.getUnitPrice();
                    colorButtons(false, true, numberSelected);
                    colorButtons(true, false, unitPriceVal);
                    gameAmtCalculation(selectedBetBean);
                    updateBallLay(selectedBetBean.getCurrentNos());
                    for (int i = 0; i < DataSource.numbers.length; i++)
                        DataSource.numbers[i] = 0;
                    String string[] = selectedBetBean.getCurrentNos();
                    // DataSource.Keno.numbersSelected = string.length;
                    // for (int i = 0; i < string.length; i++) {
                    // DataSource.numbers[Integer.parseInt(string[i]) - 1] = 1;
                    // }
                    unitLay.post(new Runnable() {
                        @Override
                        public void run() {

                            int unitLayheight = (int) unitLay.getMeasuredHeight();

                            gridView.setAdapter(new FastGameAdapter(activity,
                                    (int) widthForGridChild / 3, (int) (heightForGridChild - unitLayheight) / 4,
                                    textSize, selectedBetBean));


                        }
                    });
                }
                break;
        }
        buyLP = (LayoutParams) selectNos.getLayoutParams();
        for (int i = 0; i < buyOptions.getChildCount(); i++) {
            CustomCheckedTextView checkedTextView = (CustomCheckedTextView) buyOptions
                    .getChildAt(i);
            if (checkedTextView.isChecked()) {
                buyLP.topMargin = 1;
                checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.jublee_mon_direct),
                        Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null,
                        getResources().getDrawable(R.drawable.strip_down1));
                checkedTextView.setTextColor(getResources().getColor(
                        R.color.jublee_mon_direct));
            } else {
                checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.four_options_text),
                        Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null, null);
                checkedTextView.setTextColor(getResources().getColor(
                        R.color.four_options_text));
            }
        }

    }

    private void manageTabs() {
        if (isFav && isLast) {
            key = 4;
        } else {
            if (isFav) {
                key = 3;
                buyOptions.getChildAt(1).setVisibility(View.GONE);
            } else if (isLast) {
                key = 3;
                buyOptions.getChildAt(0).setVisibility(View.GONE);
            } else {
                key = 2;
                buyOptions.getChildAt(0).setVisibility(View.GONE);
                buyOptions.getChildAt(1).setVisibility(View.GONE);
            }
        }
        switch (key) {
            case 4:
                params = new LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    params.weight = 0.25f;
                    buyOptions.getChildAt(i).setLayoutParams(params);
                    buyOptions.getChildAt(i).setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                params = new LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    if (i == 2) {
                        params.weight = 0.34f;
                    } else {
                        params.weight = 0.33f;
                    }
                    buyOptions.getChildAt(i).setLayoutParams(params);
                }
                if (isFav) {
                    buyOptions.getChildAt(0).setVisibility(View.VISIBLE);
                    buyOptions.getChildAt(2).setVisibility(View.VISIBLE);
                    buyOptions.getChildAt(3).setVisibility(View.VISIBLE);
                }
                if (isLast) {
                    buyOptions.getChildAt(1).setVisibility(View.VISIBLE);
                    buyOptions.getChildAt(2).setVisibility(View.VISIBLE);
                    buyOptions.getChildAt(3).setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                params = new LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    params.weight = 0.5f;
                    buyOptions.getChildAt(i).setLayoutParams(params);
                }
                break;

            default:
                break;
        }
        for (int i = 0; i < buyOptions.getChildCount(); i++) {
            if (buyOptions.getChildAt(i).getVisibility() == View.VISIBLE) {
                updateUI(buyOptions.getChildAt(i));
                break;
            }
        }
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
            var = (int) getResources().getDimension(R.dimen.action_bar_height);
        } else {
            var = (int) getResources().getDimension(R.dimen.action_bar_height);
        }
        actionBarHeight = var;
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        height = displaymetrics.heightPixels - result - actionBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        if (GlobalVariables.onTablet(activity)) {
            selectedNosParentHeight = (int) (selectedNosParentDefaultHeight * 1.5);
        } else {
            switch (GlobalVariables.getDensity(activity)) {
                case DisplayMetrics.DENSITY_LOW:
                    selectedNosParentHeight = (int) (selectedNosParentDefaultHeight * 0.75);
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    selectedNosParentHeight = selectedNosParentDefaultHeight;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    selectedNosParentHeight = (int) (selectedNosParentDefaultHeight * 1.5);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    selectedNosParentHeight = selectedNosParentDefaultHeight * 2;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    selectedNosParentHeight = selectedNosParentDefaultHeight * 3;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    selectedNosParentHeight = selectedNosParentDefaultHeight * 4;
                    break;
            }
        }
        if (numArr.length > noOfBallsInSingleLine) {
            totalBallWidth = width
                    - (int) (2 * getResources().getDisplayMetrics().density)
                    - (int) ((noOfBallsInSingleLine) * getResources()
                    .getDisplayMetrics().density);
        } else {
            totalBallWidth = width
                    - (int) (2 * getResources().getDisplayMetrics().density)
                    - (int) ((numArr.length - 1) * getResources()
                    .getDisplayMetrics().density);
        }
        int var1 = 0;
        if (GlobalVariables.onTablet(activity)) {
            var1 = (int) getResources().getDimension(R.dimen.game_footer_height);
        } else {
            var1 = (int) getResources().getDimension(R.dimen.game_footer_height);
        }
        heightForGridChild = (height - var1);
        Utils.logPrint("data-" + getResources().getDisplayMetrics().density + " " + height
                + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    private void updateBallLay(String[] numArr) {
        if (numArr.length > 0) {
            lastLay.setVisibility(View.VISIBLE);

            if (numArr.length <= noOfBallsInSingleLine) {
                secondSelectedNosLay.setVisibility(View.GONE);
                isDrawSecond = false;
                noOfLay = 1;
            } else {
                secondSelectedNosLay.setVisibility(View.VISIBLE);
                isDrawSecond = true;
                noOfLay = 2;
            }
            if (isDrawSecond) {
                check = noOfBallsInSingleLine;
            } else {
                check = numArr.length;
            }
            firstParentParms = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
            firstSelectedNosLay.setLayoutParams(firstParentParms);
            if (totalBallWidth / check < (selectedNosParentHeight - getPx(2))
                    / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(1));
                ballHeight = (int) (totalBallWidth / check - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3)))
                        / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3)))
                        / noOfLay;
            }
            firstSelectedNosLay.removeAllViews();
            for (int i = 0; i < check; i++) {
                LayoutParams firstParms = new LayoutParams(
                        ballHeight, ballWidth);
                View view = ((LayoutInflater) activity.getSystemService(
                        activity.LAYOUT_INFLATER_SERVICE)).inflate(
                        R.layout.fast_game_nos_lay, null);
                LinearLayout sublay = (LinearLayout) view
                        .findViewById(R.id.sub_lay);
                CustomTextView fastNos = (CustomTextView) view
                        .findViewById(R.id.fast_number);
                CustomTextView nosSelected = (CustomTextView) view
                        .findViewById(R.id.no_selected);
                fastNos.setTextColor(getResources().getColor(
                        R.color.fast_nos_bg_color));
                nosSelected.setTextColor(getResources().getColor(
                        R.color.fast_nos_text_color));
                fastNos.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        (float) (ballHeight / 2.9));
                nosSelected.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        (float) (ballHeight / 3.2));
                if (isDrawSecond) {
                    if (i != noOfBallsInSingleLine - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                } else {
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                }
                fastNos.setGravity(Gravity.CENTER);
                nosSelected.setGravity(Gravity.CENTER);
                // if (numArr[i].equals("")) {
                // fastNos.setBackgroundDrawable(getResources().getDrawable(
                // R.drawable.impossible));
                // } else {
                // fastNos.setBackgroundDrawable(getResources().getDrawable(
                // R.drawable.impossible));
                // }
                sublay.setLayoutParams(firstParms);
                fastNos.setText(numArr[i].split("#")[0]);
                nosSelected.setText(numArr[i].split("#")[1]);
                firstSelectedNosLay.addView(view);
            }
            if (isDrawSecond) {
                lastParentParms = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        ((int) (selectedNosParentHeight - (getPx(2))))
                                / noOfLay);
                secondSelectedNosLay.setLayoutParams(lastParentParms);
                if (totalBallWidth / check < (selectedNosParentHeight - getPx(2))
                        / noOfLay) {
                    ballWidth = (int) (totalBallWidth / check - getPx(1));
                    ballHeight = (int) (totalBallWidth / check - getPx(1));
                } else {
                    ballWidth = ((int) (selectedNosParentHeight - getPx(3)))
                            / noOfLay;
                    ballHeight = ((int) (selectedNosParentHeight - getPx(3)))
                            / noOfLay;
                }
                secondSelectedNosLay.removeAllViews();
                for (int i = noOfBallsInSingleLine; i < numArr.length; i++) {
                    LayoutParams firstParms = new LayoutParams(
                            ballHeight, ballWidth);
                    View view = ((LayoutInflater) activity
                            .getSystemService(activity.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.fast_game_nos_lay, null);
                    LinearLayout sublay = (LinearLayout) view
                            .findViewById(R.id.sub_lay);
                    CustomTextView fastNos = (CustomTextView) view
                            .findViewById(R.id.fast_number);
                    CustomTextView nosSelected = (CustomTextView) view
                            .findViewById(R.id.no_selected);
                    fastNos.setTextColor(getResources().getColor(
                            R.color.fast_nos_bg_color));
                    nosSelected.setTextColor(getResources().getColor(
                            R.color.fast_nos_text_color));
                    fastNos.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            ballHeight / 3);
                    nosSelected.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            ballHeight / 3);
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                    fastNos.setGravity(Gravity.CENTER);
                    nosSelected.setGravity(Gravity.CENTER);
                    fastNos.setText(numArr[i].split("#")[0]);
                    nosSelected.setText(numArr[i].split("#")[1]);
                    // CustomTextView.setBackgroundDrawable(getResources()
                    // .getDrawable(R.drawable.impossible));
                    sublay.setLayoutParams(firstParms);
                    secondSelectedNosLay.addView(view);
                }
            }
        } else {
            lastLay.setVisibility(View.GONE);
        }
    }

    private double getPx(double d) {
        return (d * getResources().getDisplayMetrics().density);

    }

    private void parseJson() {
        try {
            loop:
            for (int i = 0; i < GlobalVariables.GamesData.gamesDisplayName.length; i++) {
                switch (GlobalVariables.GamesData.gamenameMap
                        .get(getArguments().getString("gameName"))) {
                    case Config.fiveGameName:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.fiveGameName));
                        break loop;
                    case Config.bonusGameName:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.bonusGameName));
                        break loop;
                    case Config.fastGameName:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.fastGameName));
                        break loop;

                }
            }
            // JSONArray crtDraw = gameObject.getJSONArray("currentDraw");
            // JSONObject crtDrawObj = crtDraw.getJSONObject(0);
            // DrawData data = new DrawData(crtDrawObj.getString("drawId"),
            // crtDrawObj.getString("drawName"),
            // crtDrawObj.getString("drawFreezeTime"),
            // crtDrawObj.getString("drawDateTime"), 0);
            // data.setSelected(true);
            drawData = new LinkedHashMap<String, DrawData>();
            finalDrawDatas = new ArrayList<>();
            // drawData.put(data.getDrawName(), data);
            JSONArray draws = gameObject.getJSONArray("draws");
            if (draws.length() <= 1) {
                advanceDrawMenu.setEnabled(false);
                advanceDrawMenu.setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
            } else {
                advanceDrawMenu.setEnabled(true);
                advanceDrawMenu.setColorFilter(
                        getResources().getColor(R.color.five_color_three),
                        Mode.SRC_IN);
            }
            for (int i = 0; i < draws.length(); i++) {
                JSONObject advDrawObj = draws.getJSONObject(i);
                String time = advDrawObj.getString("drawDateTime");
                String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                DrawData data1 = new DrawData(advDrawObj.getString("drawId"), "N/A", drawTime, i + 1);
                if (i == 0) {
                    data1.setSelected(true);
                    finalDrawDatas.add(data1);
                } else {
                    data1.setSelected(false);
                }
                drawData.put(data1.getDrawName() + i, data1);

            }
            JSONArray betTypeArr = gameObject.getJSONArray("bets");
            betTypeData = new LinkedHashMap<String, BetTypeBean>();

            LotteryPreferences preferences = new LotteryPreferences(activity);

            for (int i = 0; i < betTypeArr.length(); i++) {
                JSONObject betObject = betTypeArr.getJSONObject(i);
/*
                String[] favArr = betObject.getString("favNo").split(",");
                String[] lastPicked = betObject.getString("lastPicked").split(",");
*/
                String[] favArr = {""};
//                String[] favArr = new String[]{"0#4","5#2","3#1","2#2","9#1"};
//                String[] lastPicked = new String[]{"1#1","8#2","4#1","6#3","0#1","2#1","7#1"};

/*                BetTypeBean bean = new BetTypeBean(
                        betObject.getDouble("unitPrice"),
                        betObject.getInt("maxBetAmtMul"),
                        betObject.getString("betDisplayName"),
                        betObject.getInt("betCode"), betObject.getInt("minNo"),
                        betObject.getInt("maxNo"), new String[]{}, favArr,
                        lastPicked);*/
                BetTypeBean bean = new BetTypeBean(
                        betObject.getDouble("unitPrice"),
                        betObject.getInt("maxBetAmtMul"),
                        betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"),
                        betObject.getInt("betCode"), 1,
                        100, new String[]{}, favArr,
                        preferences.getZERO_TO_NINE().split(","));
                bean.setBetName(betObject.optString("betDispName"));
                betTypeData.put(bean.getBetDisplayName(), bean);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateBetValiadtion(final BetTypeBean typeBean) {
        // DataSource.Keno.numbersSelected = 0;
        if (selectedBetBean.getFavNos().length == 1) {
            if (selectedBetBean.getFavNos()[0].equals("")) {
                isFav = false;
            } else {
                isFav = true;
            }
        } else {
            isFav = true;
        }
        if (selectedBetBean.getLastPicked().length == 1) {
            if (selectedBetBean.getLastPicked()[0].equals("")) {
                isLast = false;
            } else {
                isLast = true;
            }
        } else {
            isLast = true;
        }
        manageTabs();
        for (int i = 0; i < DataSource.numbers.length; i++)
            DataSource.numbers[i] = 0;
        unitLay.post(new Runnable() {
            @Override
            public void run() {

                int unitLayheight = (int) unitLay.getMeasuredHeight();
                gridView.setAdapter(new FastGameAdapter(activity,
                        (int) widthForGridChild / 3, (int) heightForGridChild / 4,
                        textSize, typeBean));


            }
        });
        // Utils.Toast(activity,
        // typeBean.getBetCode() + " " + typeBean.getBetDisplayName(),
        // 1000);
    }

    public static String[] getPickedNo() {
        String[] result = null;
        String value = "";
        int valuefast;
        for (int i = 0; i < FastGameAdapter.numbersSelected.length; i++) {
            if (FastGameAdapter.numbersSelected[i] >= 1) {
                if (i <= 8) {
                    valuefast = i + 1;
                    value += "0" + valuefast;
                } else {
                    value += 0 + "0";
                }
                int selectedVal = FastGameAdapter.numbersSelected[i];
                String selValText = selectedVal + "";
                if (selectedVal <= 9) {
                    selValText = "0" + selectedVal;
                }
                value += "#" + selValText;
                value += ",";
            }
        }
        result = value.split(",");
        return result;
    }

    public static void gameAmtCalculation(BetTypeBean bean) {
        noOfLinesVal = 0;
        totalAmt = 0;
        if (numberSelected == 0) {
            totalAmt = 0f;
        } else {
            noOfLinesVal = numberSelected;
        }
        totalAmt = unitPriceVal * noOfLinesVal;
        totalAmt = totalAmt * finalDrawDatas.size();
        noOfLines.setText(noOfLinesVal + "");
        unitPrice.setText(unitPriceVal + "");
        qpNos.setText(numberSelected + "");
        selectedNos.setText(numberSelected + "");
//        totalAmt = AmountFormat.roundDrawTktAmt(totalAmt);
        finalAmt.setText(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(totalAmt) + "");
    }

    private long getNoOfLines(int betCode, int selectedNos) {
        switch (betCode) {
            case 1:
                return selectedNos;
            case 2:
                return selectedNos;
            case 3:
                return selectedNos;
            case 4:
                return selectedNos;
            case 5:
                return selectedNos;
            case 6:
                return combination(selectedNos, 2);
            case 7:
                return combination(selectedNos, 3);
            case 8:
                return 0;
            case 9:
                return 0;
            case 11:
                return selectedNos;
            case 15:
                return selectedNos;
            default:
                return 0;
        }
    }

    private long combination(int n, int r) {
        return fact(n) / (fact(r) * fact(n - r));
    }

    private long fact(long n) {
        if (n == 0)
            return 1;
        else
            return n * fact(n - 1);
    }

    private void incDecHandler(double min, double max, double numberGap,
                               int clickedId, CustomTextView CustomTextView, boolean isUnitPrice) {
        switch (clickedId) {
            case R.id.inc:
                increment(max, numberGap, CustomTextView, true);
                break;
            case R.id.dec:
                decrement(min, numberGap, CustomTextView, true);
                break;
            case R.id.qp_inc:
                increment(max, numberGap, CustomTextView, false);
                break;
            case R.id.qp_dec:
                decrement(min, numberGap, CustomTextView, false);
                break;
        }
    }

    public void increment(double max, double numberGap,
                          CustomTextView CustomTextView, boolean isUnitPrice) {
        double value = Double.parseDouble(CustomTextView.getText().toString());
        if (value < max) {
            value += numberGap;
            DecimalFormat format = new DecimalFormat("0.##");
            value = Double.parseDouble(format.format(value));
            if (isUnitPrice) {
                unitPriceVal = value;
            } else {
                numberSelected = (int) value;
            }
            gameAmtCalculation(selectedBetBean);
            if (isUnitPrice) {
                colorButtons(isUnitPrice, false, value);
            } else {
                colorButtons(isUnitPrice, true, value);
            }
        }
    }

    public void decrement(double min, double numberGap,
                          CustomTextView CustomTextView, boolean isUnitPrice) {
        double value1 = Double.parseDouble(CustomTextView.getText().toString());
        if (value1 > min) {
            value1 -= numberGap;
            DecimalFormat format = new DecimalFormat("0.##");
            value1 = Double.parseDouble(format.format(value1));
            if (isUnitPrice) {
                unitPriceVal = value1;
            } else {
                numberSelected = (int) value1;
            }
            gameAmtCalculation(selectedBetBean);
            if (isUnitPrice) {
                colorButtons(isUnitPrice, false, value1);
            } else {
                colorButtons(isUnitPrice, true, value1);
            }
        }
    }

    private void colorButtons(boolean isUnitPrice, boolean isQP, double value) {
        if (isUnitPrice) {
            if (value >= selectedBetBean.getMaxBetAmtMul()
                    && value == selectedBetBean.getUnitPrice()) {
                inc.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
                dec.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
            } else {
                if (value >= selectedBetBean.getMaxBetAmtMul()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);

                } else if (value < selectedBetBean.getMaxBetAmtMul()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                }

                if (value == selectedBetBean.getUnitPrice()) {
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            Mode.SRC_IN);
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                }
            }
        }
        if (isQP) {
            if (value >= selectedBetBean.getMaxNo()
                    && value == selectedBetBean.getMinNo()) {
                qpInc.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
                qpDec.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
            } else {
                if (value >= selectedBetBean.getMaxNo()) {
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            Mode.SRC_IN);
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                } else if (value < selectedBetBean.getMaxNo()) {
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                }
                if (value == selectedBetBean.getMinNo()) {
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            Mode.SRC_IN);
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                }
            }
        }
    }

    private void openGrid(final View view, boolean toOpen) {
        if (!toOpen) {
            buyNow.setText(spannableString);
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    activity, R.anim.btncloseanim);
            setEditVisible(View.VISIBLE);
            btn_toogleanim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // view.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)/*if gt or eq then icecream*/ {
                        scrollToTop(scrollView);

                    } else {
                        scrollView.smoothScrollTo(0, 0);//lt 4.0
                    }
                    ;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            view.setVisibility(View.GONE);
                        }
                    }, 300);
                    // scrollToTop(unitLay);
                }
            });
            view.startAnimation(btn_toogleanim);
            view.setVisibility(View.INVISIBLE);
        } else {
            setEditVisible(View.GONE);
            view.setVisibility(View.VISIBLE);
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    activity, R.anim.btnopenanim);
            view.startAnimation(btn_toogleanim);
            focusOnView();
            buyNow.setText(activity.getResources().getString(R.string.ok_draw));
        }
    }

    public void scrollToTop(View mScrollView) {
        int x = 0;
        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(mScrollView,
                "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(mScrollView,
                "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(1000L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    private void setEditVisible(int visibility) {
        edit.setVisibility(visibility);
    }

    @Override
    public void onDismissCustomDiloag(ArrayList<DrawData> newObj, boolean isDrawFreeze) {
        drawDatas = new ArrayList<>();
        for (int i = 0; i < newObj.size(); i++) {
            DrawData org = newObj.get(i);
            DrawData newData = new DrawData(org.getDrawId(), org.getDrawName(),
                    org.getDrawFreezeTime(), org.getDrawDateTime(),
                    org.getPosition());
            newData.setSelected(org.isSelected());
            drawDatas.add(newData);
        }
    }

    @Override
    public void onCancelCustomDiloag() {
        ArrayList<DrawData> list = DrawAdapater.drawCopy;
        finalDrawDatas = new ArrayList<DrawData>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                DrawData data = list.get(i);
                finalDrawDatas.add(data);
                if (!showOne) {
                    showOne = !showOne;
                    if (!data.getDrawName().equalsIgnoreCase("N/A")) {
                        drawName.setText(data.getDrawName());
                        drawTime.setText(data.getDrawDateTime());
                        drawTime.setVisibility(View.VISIBLE);
                    } else {
                        drawTime.setVisibility(View.GONE);
                        drawName.setText(data.getDrawDateTime());
                    }
//                    drawName.setText(data.getDrawName());
//                    drawTime.setText(data.getDrawDateTime());
                }
            }
        }
        gameAmtCalculation(selectedBetBean);
        if (DrawDialog.count > 1) {
            restDraws.setVisibility(View.VISIBLE);
            restDraws.setText("(" + (DrawDialog.count - 1) + "+" + ")");
        } else {
            restDraws.setVisibility(View.INVISIBLE);
        }
    }

    public static int getNoSelected(int id) {
        int result = 0;
        switch (id) {
            case R.id.fav_nos:
                String arr[] = selectedBetBean.getFavNos();
                for (int i = 0; i < arr.length; i++) {
                    int nos = Integer.parseInt(arr[i].split("#")[1]);
                    result += nos;
                }
                break;
            case R.id.last_picked:
                String arr1[] = selectedBetBean.getLastPicked();
                for (int i = 0; i < arr1.length; i++) {
                    int nos = Integer.parseInt(arr1[i].split("#")[1]);
                    result += nos;
                }
                break;
            case R.id.select_nos:
                String arr2[] = selectedBetBean.getCurrentNos();
                if (arr2.length > 0 && !(arr2[0].equals(""))) {
                    for (int i = 0; i < arr2.length; i++) {
                        int nos = Integer.parseInt(arr2[i].split("#")[1]);
                        result += nos;
                    }
                } else {
                    result = 0;
                }
                break;

        }
        return result;
    }

    private void updateTheme() {
        changeBetType.getCompoundDrawables()[2].setColorFilter(getResources()
                .getColor(R.color.jublee_mon_direct), Mode.SRC_IN);
        advanceDrawMenu.setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
        edit.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
        qpDec.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
        qpInc.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
        dec.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
        inc.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), Mode.SRC_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                purchaseNow();
            }
        }
    }

    private void purchaseNow() {
        OnClickListener okay = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(activity)) {
                    dBox.dismiss();
                    analytics.sendAction(Fields.Category.FAST_PURCHASE, Fields.Action.CLICK);
                    buyNow();
                } else {
                    GlobalVariables.showDataAlert(activity);
                }
            }
        };
        OnClickListener cancel = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dBox.dismiss();
            }
        };
        dBox = new DownloadDialogBox(activity,
                getResources().getString(R.string.purchase_conf), getResources().getString(R.string.confirm),
                true, true, okay, cancel);
        if (totalAmt > Double.parseDouble(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL))) {
            Utils.Toast(
                    activity,
                    getResources().getString(R.string.insufficient_balance)
            );
        } else {
            dBox.show();
        }
    }

    private void buyNow() {
        ticketSaleDta = new JSONObject();
        try {
            ticketSaleDta.put("gameCode", Config.fastGameName);
            if (finalDrawDatas.get(0) == drawDatas.get(0) && finalDrawDatas.size() > 1) {
                ticketSaleDta.put("isAdvancePlay", true);
            } else if (finalDrawDatas.get(0) != drawDatas.get(0)) {
                ticketSaleDta.put("isAdvancePlay", true);
            } else {
                ticketSaleDta.put("isAdvancePlay", false);
            }
            ticketSaleDta.put("merchantCode", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE));
            ticketSaleDta.put("noOfDraws", finalDrawDatas.size());
            ticketSaleDta.put("noOfPanel", 1);
            ticketSaleDta.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
            ticketSaleDta.put("userId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.PLAYER_ID));
            JSONArray drawArr = new JSONArray();
            for (int i = 0; i < finalDrawDatas.size(); i++) {
                JSONObject drawObj = new JSONObject();
                drawObj.put("drawId", finalDrawDatas.get(i).getDrawId());
                drawArr.put(drawObj);
            }
            JSONArray panelArr = new JSONArray();
            if (quickPick.isChecked()) {
                JSONObject panelObj = new JSONObject();
                panelObj.put("isQP", true);
                panelObj.put("pickedNumbers", "QP");
                panelObj.put("noPicked", Integer.parseInt(qpNos.getText().toString()));
                panelObj.put("betAmtMul", 1);
                panelObj.put("playType", selectedBetBean.getBetName());
                panelArr.put(panelObj);
            } else {
                if (selectNos.isChecked()) {
                    noPckdArr = selectedBetBean.getCurrentNos();
                    for (int l = 0; l < noPckdArr.length; l++) {
                        String pickedNo = noPckdArr[l].split("#")[0];
                        String betAmtMltpl = noPckdArr[l].split("#")[1];
                        JSONObject panelObj = new JSONObject();
                        panelObj.put("noPicked", 1);
                        panelObj.put("isQP", false);
                        panelObj.put("pickedNumbers", pickedNo);
                        panelObj.put("betAmtMul", Integer.parseInt(betAmtMltpl));
                        panelObj.put("playType", selectedBetBean.getBetName());
                        panelArr.put(panelObj);
                    }
                }
                if (favNos.isChecked()) {
                    noPckdArr = selectedBetBean.getFavNos();
                    for (int l = 0; l < noPckdArr.length; l++) {
                        String pickedNo = noPckdArr[l].split("#")[0];
                        String betAmtMltpl = noPckdArr[l].split("#")[1];
                        JSONObject panelObj = new JSONObject();
                        panelObj.put("noPicked", 1);
                        panelObj.put("isQP", false);
                        panelObj.put("pickedNumbers", pickedNo);
                        panelObj.put("betAmtMul", Integer.parseInt(betAmtMltpl));
                        panelObj.put("playType", selectedBetBean.getBetName());
                        panelArr.put(panelObj);
                    }
                }
                if (lastPckd.isChecked()) {
                    noPckdArr = selectedBetBean.getLastPicked();
                    for (int l = 0; l < noPckdArr.length; l++) {
                        String pickedNo = noPckdArr[l].split("#")[0];
                        String betAmtMltpl = noPckdArr[l].split("#")[1];
                        JSONObject panelObj = new JSONObject();
                        panelObj.put("noPicked", 1);
                        panelObj.put("isQP", false);
                        panelObj.put("pickedNumbers", pickedNo);
                        panelObj.put("betAmtMul", Integer.parseInt(betAmtMltpl));
                        panelObj.put("playType", selectedBetBean.getBetName());
                        panelArr.put(panelObj);
                    }
                }
            }
            ticketSaleDta.put("drawData", drawArr);
            ticketSaleDta.put("panelData", panelArr);
            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpPlayMgmt/purchaseTicket";
            new DGETask(FastGameScreen.this, "BUY", url, ticketSaleDta.toString()).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "BUY":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.SUCCESS);

                            Intent intent = new Intent(activity,
                                    TicketDescActivity.class);
                            intent.putExtra("data", resultData.toString());
                            intent.putExtra("isPurchase", true);
                            startActivity(intent);
                            activity.finish();
                            activity.overridePendingTransition(
                                    GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 110) {
                            analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            Intent intent = new Intent(activity,
                                    MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            activity.overridePendingTransition(
                                    GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 3009) {
                            analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                            UserInfo.setLogout(activity);
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            startActivityForResult(new Intent(activity,
                                    LoginActivity.class), 100);
                            activity.overridePendingTransition(GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 3017) {
                            analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            JSONArray array = jsonResult.getJSONArray("games");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.fastGameName)) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("draws");
                                    drawDatas = new ArrayList<>();
                                    finalDrawDatas = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject advDrawObj = jsonArray.getJSONObject(j);
                                        String time = advDrawObj.getString("drawDateTime");
                                        String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                                        DrawData data1 = new DrawData(advDrawObj.getString("drawId"), "N/A", drawTime, i);
                                        if (j == 0) {
                                            data1.setSelected(true);
                                            finalDrawDatas.add(data1);
                                        } else {
                                            data1.setSelected(false);
                                        }
                                        drawDatas.add(data1);
                                    }
                                    restDraws.setVisibility(View.INVISIBLE);
                                    if (drawDatas.size() > 0) {
                                        DrawData preDrawData = drawDatas.get(0);
                                        if (!preDrawData.getDrawName().equalsIgnoreCase("N/A")) {
                                            drawName.setText(preDrawData.getDrawName());
                                            drawTime.setText(preDrawData.getDrawDateTime());
                                        } else {
                                            drawTime.setVisibility(View.GONE);
                                            drawName.setText(preDrawData.getDrawDateTime());
                                        }
                                        DrawDialog.count = 1;
                                        showOne = false;
                                        darwDialog = new DrawDialog(activity, drawDatas,
                                                FastGameScreen.this, false);
                                        darwDialog.setCancelable(false);
                                        darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                                        darwDialog.show();
                                    } else {
                                        drawName.setText(activity.getString(R.string.no_draws_text));
                                        drawTime.setVisibility(View.GONE);
                                    }

                                    break;
                                }
                            }
                            MainScreen.parseRefreshedData(array);
                            dialog.dismiss();

                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FAST_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                    if (dialog != null)
                        dialog.dismiss();
                    Utils.Toast(activity, "Data not available in offline mode");
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(activity);
                }
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);
    }


    View.OnTouchListener incDecTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            if (id == R.id.qp_inc) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL) && isIncremented) {
                    isIncremented = false;
                }
            } else if (id == R.id.qp_dec) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL) && isDecremented) {
                    isDecremented = false;
                }
            }
            return false;
        }
    };

    View.OnLongClickListener incDecLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.qp_dec:
                    isDecremented = true;
                    onLongPressCounterHandler
                            .postDelayed(new CounterValueIncDec(), 100);
                    break;
                case R.id.qp_inc:
                    isIncremented = true;
                    onLongPressCounterHandler
                            .postDelayed(new CounterValueIncDec(), 100);
                    break;

                default:
                    return true;

            }
            return false;
        }
    };


    class CounterValueIncDec implements Runnable {
        @Override
        public void run() {
            if (isIncremented) {
                incDecHandler(selectedBetBean.getMinNo(),
                        selectedBetBean.getMaxNo(), 1, R.id.qp_inc, qpNos,
                        false);
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            } else if (isDecremented) {
                incDecHandler(selectedBetBean.getMinNo(),
                        selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                        false);
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            }
        }

    }
}
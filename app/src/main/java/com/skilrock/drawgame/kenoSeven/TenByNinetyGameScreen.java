package com.skilrock.drawgame.kenoSeven;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.skilrock.adapters.ButtonAdapter;
import com.skilrock.adapters.DialogListAdapterTenByNinty;
import com.skilrock.adapters.DrawAdapater;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.DrawData;
import com.skilrock.bean.PanelData;
import com.skilrock.config.Config;
import com.skilrock.config.DismissListener;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.BetDialog;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DebouncedOnTouchListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawDialog;
import com.skilrock.customui.ExpandableGridView;
import com.skilrock.drawgame.DialogKeyListener;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.TicketDescActivity;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.skilrock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by stpl on 12/23/2015.
 */
public class TenByNinetyGameScreen extends Fragment implements DismissListener, WebServicesListener {
    private HashMap<String, Integer> betPosition;
    private Analytics analytics;
    public static ArrayList<DrawData> finalDrawDatas;
    private RelativeLayout playInfoLay;
    private CustomTextView buyNow/* , okay , selectNos */;
    private CustomTextViewDown scrollTextView;
    private ScrollView scrollView;
    private ExpandableGridView gridView;
    private boolean isVisible, isFav = true, isLast = true;
    private LinearLayout buyOptions, lastLay, lastLaySub;
    private LinearLayout.LayoutParams params, buyLP;
    private int key;
    private CustomCheckedTextView favNos, lastPckd, quickPick, selectNos;
    private int textSize;
    private LinearLayout firstSelectedNosLay, secondSelectedNosLay;
    private RelativeLayout nolSn;
    private boolean isDrawSecond;
    private int totalBallWidth, ballWidth, ballHeight;
    private int selectedNosParentHeight, selectedNosParentDefaultHeight = 100,
            noOfBallsInSingleLine = 10, margin = 30;
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
    private LinearLayout.LayoutParams firstParentParms, lastParentParms;
    private int noOfLay;
    private int check;
    public static TextView selectedNos, qpNos, noOfLines, finalAmt, unitPrice;
    private ImageView dec, inc;
    private ImageView qpDec, qpInc;
    private BetTypeBean selectedBetBean;
    public static double unitPriceVal;
    public static int numberSelected;
    private JSONObject gameObject;
    private RelativeLayout betLayout;
    private ImageView edit;
    private ArrayList<DrawData> drawDatas;
    public static double totalAmt;
    public static long noOfLinesVal;
    private DownloadDialogBox dBox;
    private JSONObject ticketSaleDta;
    private Context context;
    private StringBuffer noPckd;
    private String[] noPckdArr;
    private JSONObject jsonResult;
    private LinearLayout egrid;
    private int unitLayheight;
    private LinearLayout buy_lay;
    private double min;
    private double max;
    private SpannableString spannableString;
    private TextView amount;
    private Activity activity;

    //panel Calculation
    private ArrayList<PanelData> panelDatas;
    private ButtonAdapter buttonAdapter;
    private int noofpanel = 0;
    private int panelPosition = 0;
    public static double totalPurchaseAmt = 0;
    public static int lastDrawDataSize;


    //onlongpress
    private boolean isIncremented = false;
    private boolean isDecremented = false;
    private Handler onLongPressCounterHandler;

    private GlobalPref globalPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        context = getActivity();
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        analytics.setScreenName(Fields.Screen.FIVE_GAME_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        onLongPressCounterHandler = new Handler();

        betPosition = new HashMap<>();
        if (globalPref.getCountry().equalsIgnoreCase("lagos")) {
            spannableString = new SpannableString(getResources().getString(R.string.proceed));
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
//            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
        } else {
            spannableString = new SpannableString(getResources().getString(R.string.play_now_text));
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, 0);
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 4, spannableString.length(), 0);
        }
        DataSource.numbers = new int[90];
        if (GlobalVariables.onTablet(activity)) {
            textSize = 40;
            selectedNosParentDefaultHeight = 150;
            margin = 45;
            noOfBallsInSingleLine = 8;
        } else {
            switch (GlobalVariables.getDensity(activity)) {
                case DisplayMetrics.DENSITY_LOW:
                    textSize = 10;
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    textSize = 15;
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    textSize = 20;
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    textSize = 23;
                    noOfBallsInSingleLine = 10;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    textSize = 26;
                    noOfBallsInSingleLine = 10;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    textSize = 30;
                    noOfBallsInSingleLine = 10;
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ten_game_screen, null);
        bindViewIds(view);
        updateTheme();

        edit.setOnClickListener(listener);
        getDisplayDetails();
        parseJson();

        drawDatas = new ArrayList<DrawData>(drawData.values());
        darwDialog = new DrawDialog(activity, drawDatas, this, false);
        if (drawData.size() > 0) {
            DrawData preDrawData = (DrawData) drawData.entrySet().iterator().next()
                    .getValue();
            if (preDrawData.getDrawName().equalsIgnoreCase("N/A") || preDrawData.getDrawName().equalsIgnoreCase("") || preDrawData.getDrawName().equalsIgnoreCase("null"))
                drawName.setVisibility(View.GONE);
            drawName.setText(preDrawData.getDrawName());
            drawTime.setText(preDrawData.getDrawDateTime());
        } else {
            drawName.setText(activity.getString(R.string.no_draws_text));
            drawTime.setVisibility(View.GONE);
        }
        selectedBetBean = (BetTypeBean) betTypeData.entrySet().iterator().next().getValue();
        betName.setText(selectedBetBean.getBetDisplayName());
        if (betTypeData.size() <= 1) {
            betLayout.setVisibility(View.GONE);
        } else {
            betLayout.setVisibility(View.VISIBLE);
        }

        buttonAdapter = new ButtonAdapter(this,
                (int) widthForGridChild / 9, ((int) heightForGridChild - unitLayheight) / 10,
                textSize, selectedBetBean, 4);
        gridView.setAdapter(buttonAdapter);
        updateBetValiadtion(selectedBetBean);
        restDraws.setTextStyle(CustomTextView.TextStyles.BOLD);
        restDraws.setVisibility(View.INVISIBLE);
        betDialog = new BetDialog(activity, new ArrayList<BetTypeBean>(
                betTypeData.values()), betName);

        betDialog.setCancelable(false);

        betDialog.setOnKeyListener(new DialogKeyListener(betDialog));


        qpInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                incDecHandler(numberSelected + 1,
                        selectedBetBean.getMaxNo(), 1, R.id.qp_inc, qpNos,
                        false);
            }
        });
        qpDec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (numberSelected == selectedBetBean.getMinNo()) {
                    incDecHandler(numberSelected,
                            selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                            false);
                } else {
                    incDecHandler(numberSelected - 1,
                            selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                            false);
                }

            }
        });
        inc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.inc, unitPrice,
                        true);
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                        true);
            }
        });

        inc.setOnTouchListener(incDecTouchListener);
        dec.setOnTouchListener(incDecTouchListener);
        inc.setOnLongClickListener(incDecLongClickListener);
        dec.setOnLongClickListener(incDecLongClickListener);

//        advanceDrawMenu.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                showOne = false;
//                darwDialog = new DrawDialog(activity, drawDatas,
//                        TenByNinetyGameScreen.this);
//                darwDialog.setCancelable(false);
//                darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
//                darwDialog.show();
//                return false;
//            }
//        });

        betLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (betTypeData.size() <= 1) {
                } else {
                    betDialog.show();
                }
            }
        });
        betDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                betName.setText(BetDialog.selectedBet);
                selectedBetBean = betTypeData.get(betName.getText());
            }
        });

        lastLay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, selectedNosParentHeight));
        lastLaySub.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, selectedNosParentHeight));
        lastLaySub.setGravity(Gravity.LEFT);
        lastLaySub.setOrientation(LinearLayout.VERTICAL);
//        favNos.setOnClickListener(commonListener);
//        lastPckd.setOnClickListener(commonListener);
//        quickPick.setOnClickListener(commonListener);
//        selectNos.setOnClickListener(commonListener);
//        buyNow.setOnClickListener(new DebouncedOnClickListener(500) {
//            @Override
//            public void onDebouncedClick(View v) {
//                {
//                    if (buyNow.getText().toString().equalsIgnoreCase("OK")) {
//                        if (DataSource.Keno.numbersSelected >= selectedBetBean.getMinNo()) {
//                            selectedBetBean.setCurrentNos(getPickedNo());
//                            openGrid(gridView, false);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    {
//                                        updateBallLay(getPickedNo());
//                                    }
//                                }
//                            }, 700);
//                            numberSelected = selectedBetBean.getCurrentNos().length;
//                            gameAmtCalculation(selectedBetBean);
//                        } else {
//                            Toast.makeText(
//                                    activity,
//                                    activity.getResources().getString(R.string.plz_sel_at_lest_five)
//                                            + selectedBetBean.getMinNo()
//                                            + activity.getResources().getString(R.string.numb_five), Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        if (drawName.getText().toString().equals(activity.getString(R.string.no_draws_text))) {
//                            Toast.makeText(activity, activity.getString(R.string.no_draws_text), Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(activity,
//                                    MainScreen.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            activity.overridePendingTransition(
//                                    GlobalVariables.startAmin,
//                                    GlobalVariables.endAmin);
//                        } else {
//                            if (!UserInfo.isLogin(activity)) {
//                                startActivityForResult(new Intent(activity,
//                                        LoginActivity.class), 100);
//                                activity.overridePendingTransition(GlobalVariables.startAmin,
//                                        GlobalVariables.endAmin);
//                            } else {
//                                if (totalAmt < 0.50) {
//                                    Toast.makeText(activity, "Minimum purchase of " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + " 0.50 are allowed.", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (panelDatas == null)
//                                    panelDatas = new ArrayList<>();
//                                panelDatas.add(collectData());
//                                purchaseNow();
//                            }
//                        }
//                    }
//                }
//            }
//        });
        gridView.setExpanded(true);
        lastLaySub.setGravity(Gravity.LEFT);
        lastLaySub.setOrientation(LinearLayout.VERTICAL);
        favNos.setOnClickListener(listener);
        lastPckd.setOnClickListener(listener);
        quickPick.setOnClickListener(listener);
        selectNos.setOnClickListener(listener);
        gridView.setExpanded(true);
        buyNow.setOnClickListener(listener);
        advanceDrawMenu.setOnTouchListener(touchListener);

        manageTabs();
        return view;
    }

    private LinearLayout unitLay;

    private void bindViewIds(View view) {
        playInfoLay = (RelativeLayout) view.findViewById(R.id.play_info);
        edit = (ImageView) view.findViewById(R.id.edit);
        betLayout = (RelativeLayout) view.findViewById(R.id.bet_lay);
        unitLay = (LinearLayout) view.findViewById(R.id.unit_lay);
        qpDec = (ImageView) view.findViewById(R.id.qp_dec);
        qpInc = (ImageView) view.findViewById(R.id.qp_inc);
        dec = (ImageView) view.findViewById(R.id.dec);
        inc = (ImageView) view.findViewById(R.id.inc);
        unitPrice = (TextView) view.findViewById(R.id.unit_price);
        finalAmt = (TextView) view.findViewById(R.id.final_amt);
        selectedNos = (TextView) view.findViewById(R.id.selected_nos);
        qpNos = (TextView) view.findViewById(R.id.qp_nos);
        noOfLines = (TextView) view.findViewById(R.id.no_of_lines);
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
        changeBetType = (CustomTextView) view
                .findViewById(R.id.change_bet_type);
        drawName = (CustomTextView) view.findViewById(R.id.draw_name);
        drawTime = (CustomTextViewTop) view.findViewById(R.id.draw_time);
        restDraws = (CustomTextView) view.findViewById(R.id.rest_draws);
        betName = (CustomTextView) view.findViewById(R.id.bet_name);


        //new view initialization
        egrid = (LinearLayout) view.findViewById(R.id.egrid);
        buy_lay = (LinearLayout) view.findViewById(R.id.buy_lay);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (int i = 0; i < DataSource.numbers.length; i++)
            DataSource.numbers[i] = 0;
        activity.overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
        selectedBetBean = null;
        unitPriceVal = 0;
        numberSelected = 0;
        finalDrawDatas = null;
        noOfLinesVal = 0;
        totalAmt = 0;
        System.out.println("here");
        totalPurchaseAmt = 0;
    }

    private final void focusOnView() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                Animation slide = null;
                slide = new TranslateAnimation(0, 0, 0, -unitLay.getTop());
                slide.setDuration(300);
                slide.setFillAfter(true);
                slide.setFillEnabled(true);
                egrid.startAnimation(slide);
                slide.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        egrid.clearAnimation();
                        scrollView.scrollTo(0, unitLay.getTop());
                        setEditVisible(View.INVISIBLE);
                        viewEnable(true);
                    }
                });
            }
        });
    }

    private View.OnClickListener commonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            updateUI(view);
        }
    };

    private void updatePanelData(PanelData panelData, boolean editCheck) {
        selectedBetBean = betTypeData.get(panelData.getPlayType());
        updateBetValiadtion(selectedBetBean);
        BetDialog.selectedPos = betPosition.get(panelData.getPlayType());
        BetDialog.selectedBet = panelData.getPlayType();
        betName.setText(panelData.getPlayType());
        if (panelData.isQp()) {
            quickPick.setChecked(true);
            favNos.setChecked(false);
            lastPckd.setChecked(false);
            selectNos.setChecked(false);
            setEditVisible(View.INVISIBLE);
            DataSource.Keno.numbersSelected = numberSelected = Integer.parseInt(panelData.getNoPicked());
            betName.setText(panelData.getPlayType());
            unitPriceVal = Double.parseDouble(AmountFormat.getAmountFormatForSingleDecimal(panelData.getBetAmtMul() * selectedBetBean.getUnitPrice()));
            unitPrice.setText(unitPriceVal + "");
            gameAmtCalculation(selectedBetBean);
            if (selectedBetBean.getMaxNo() == selectedBetBean.getMinNo()) {
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.VISIBLE);
            } else {
                qpNoLines.setVisibility(View.VISIBLE);
                restNoLines.setVisibility(View.GONE);
            }
            updateBallLay(selectedBetBean.getMaxNo(), Double.parseDouble(panelData.getNoPicked()));
            colorButtons(true, true, unitPriceVal);
            colorButtons(false, true, numberSelected);
            changeColor();
        } else {
            quickPick.setChecked(false);
            favNos.setChecked(false);
            lastPckd.setChecked(false);
            selectNos.setChecked(true);
            if (!editCheck)
                setEditVisible(View.VISIBLE);
            else
                openGrid(gridView, true);
            qpNoLines.setVisibility(View.GONE);
            String[] numbers = getPickedNo(panelData.getPickedNumbers());
            for (int i = 0; i < numbers.length; i++) {
                DataSource.numbers[Integer.parseInt(numbers[i]) - 1] = 1;
            }
            buttonAdapter.notifyDataSetChanged();
            selectedBetBean.setCurrentNos(getPickedNo(panelData.getPickedNumbers()));
            if (panelData.getNoPicked().equals("") || panelData.getNoPicked() == null) {
                Utils.Toast(activity, "there is no update for no picked");
            } else {
                DataSource.Keno.numbersSelected = numberSelected = Integer.parseInt(panelData.getNoPicked());
            }
            updateBallLay(getPickedNo(panelData.getPickedNumbers()));
            unitPriceVal = Double.parseDouble(AmountFormat.getAmountFormatForSingleDecimal(panelData.getBetAmtMul() * selectedBetBean.getUnitPrice()));
            unitPrice.setText(unitPriceVal + "");
            gameAmtCalculation(selectedBetBean);
            colorButtons(true, true, unitPriceVal);
            colorButtons(false, true, numberSelected);
            changeColor();
        }
    }

    private void updateUI(View view) {
        switch (view.getId()) {
            case R.id.fav_nos:
                analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.FAV_NO);
                setEditVisible(View.INVISIBLE);
                playInfoLay.setVisibility(View.GONE);
                // Toast.makeText(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000).show();
                favNos.setChecked(true);
                lastPckd.setChecked(false);
                quickPick.setChecked(false);
                selectNos.setChecked(false);
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.VISIBLE);
                buyNow.setText(spannableString);
                numberSelected = selectedBetBean.getFavNos().length;
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
                analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.LAST_PICK);
                playInfoLay.setVisibility(View.GONE);
                setEditVisible(View.INVISIBLE);
                // Toast.makeText(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000).show();
                favNos.setChecked(false);
                lastPckd.setChecked(true);
                quickPick.setChecked(false);
                selectNos.setChecked(false);
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.VISIBLE);
                buyNow.setText(spannableString);
                numberSelected = selectedBetBean.getLastPicked().length;
                unitPriceVal = selectedBetBean.getUnitPrice();
                colorButtons(false, true, numberSelected);
                unitPriceVal = selectedBetBean.getUnitPrice();
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
                if (quickPick.isChecked()) {
                } else {
                    analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.QUICK_PICK);
                    playInfoLay.setVisibility(View.GONE);
                    setEditVisible(View.INVISIBLE);
                    // Toast.makeText(activity,
                    // ((CustomCheckedTextView) view).getText().toString(),
                    // 1000).show();
                    favNos.setChecked(false);
                    lastPckd.setChecked(false);
                    quickPick.setChecked(true);
                    selectNos.setChecked(false);
                    lastLay.setVisibility(View.GONE);
                    gridView.setVisibility(View.GONE);
                    if (selectedBetBean.getMaxNo() == selectedBetBean.getMinNo()) {
                        qpNoLines.setVisibility(View.GONE);
                        restNoLines.setVisibility(View.VISIBLE);
                    } else {
                        qpNoLines.setVisibility(View.VISIBLE);
                        restNoLines.setVisibility(View.GONE);
                    }
                    numberSelected = selectedBetBean.getMinNo();
                    unitPriceVal = selectedBetBean.getUnitPrice();
                    colorButtons(false, true, numberSelected);
                    colorButtons(true, false, unitPriceVal);

                    //new code related to balls
                    max = selectedBetBean.getMaxNo();
                    min = selectedBetBean.getMinNo();
                    updateBallLay(max, min);
                    gameAmtCalculation(selectedBetBean);
                    buyNow.setText(spannableString);
                }
                break;
            case R.id.select_nos:
                analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.PICK_NEW);

                if (selectNos.isChecked()) {

                } else {
                    playInfoLay.setVisibility(View.GONE);
                    //setEditVisible(View.VISIBLE);
                    // Toast.makeText(activity,
                    // ((CustomCheckedTextView) view).getText().toString(),
                    // 1000).show();
                    favNos.setChecked(false);
                    lastPckd.setChecked(false);
                    quickPick.setChecked(false);
                    selectNos.setChecked(true);
                    openGrid(gridView, true);
                    if (DataSource.Keno.numbersSelected == 0) {
                        lastLay.setVisibility(View.GONE);
                    } else {
                        lastLay.setVisibility(View.VISIBLE);
                    }
                    qpNoLines.setVisibility(View.GONE);
                    restNoLines.setVisibility(View.VISIBLE);

                    if (selectedBetBean.getCurrentNos().length == 0) {
                        numberSelected = selectedBetBean.getCurrentNos().length;
                    } else {
                        numberSelected = selectedBetBean.getCurrentNos()[0].equals("") ? 0 : selectedBetBean.getCurrentNos().length;
                    }
                    unitPriceVal = selectedBetBean.getUnitPrice();

                    //old zim code
//                    numberSelected = selectedBetBean.getCurrentNos().length;
//                    unitPriceVal = selectedBetBean.getUnitPrice();
                    colorButtons(false, true, numberSelected);
                    colorButtons(true, false, unitPriceVal);
                    gameAmtCalculation(selectedBetBean);
                    updateBallLay(selectedBetBean.getCurrentNos());
                    for (int i = 0; i < DataSource.numbers.length; i++)
                        DataSource.numbers[i] = 0;

                    String string[] = selectedBetBean.getCurrentNos();
                    DataSource.Keno.numbersSelected = string.length;
                    if (string.length > 0 && !string[0].equals("")) {
                        for (int i = 0; i < string.length; i++) {
                            DataSource.numbers[Integer.parseInt(string[i]) - 1] = 1;
                        }
                    } else {
                        DataSource.Keno.numbersSelected = 0;
                    }
                    unitLay.post(new Runnable() {
                        @Override
                        public void run() {

                            unitLayheight = (int) unitLay.getMeasuredHeight();

                            buttonAdapter.updateBean(selectedBetBean);
                            buttonAdapter.notifyDataSetChanged();
                        }
                    });


//                    gridView.setAdapter(new ButtonAdapter(activity,
//                            (int) widthForGridChild / 9, (int) heightForGridChild / 10,
//                            textSize, selectedBetBean));
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    (int) (heightForGridChild - gridView.getHeight()));
                            playInfoLay.setLayoutParams(params);
                        }
                    }, 50);
                    break;
                }
        }
        buyLP = (LinearLayout.LayoutParams) selectNos.getLayoutParams();
        changeColor();

    }


    private void changeColor() {
        for (int i = 0; i < buyOptions.getChildCount(); i++) {
            CustomCheckedTextView checkedTextView = (CustomCheckedTextView) buyOptions
                    .getChildAt(i);
            if (checkedTextView.isChecked()) {
                buyLP.topMargin = 1;
                //checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.jublee_mon_direct_select),
                        PorterDuff.Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null,
                        getResources().getDrawable(R.drawable.strip_down1));
                checkedTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                checkedTextView.setTextColor(getResources().getColor(
                        R.color.jublee_mon_direct_select));
            } else {
                //checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.four_options_text),
                        PorterDuff.Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null, null);
                checkedTextView.setGravity(Gravity.CENTER_HORIZONTAL);
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
                params = new LinearLayout.LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    params.weight = 0.25f;
                    params.gravity = Gravity.CENTER;
                    //  buyOptions.getChildAt(i).setLayoutParams(params);
                    buyOptions.getChildAt(i).setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                params = new LinearLayout.LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    if (i == 2) {
                        params.weight = 0.34f;
                    } else {
                        params.weight = 0.33f;
                    }
                    params.gravity = Gravity.CENTER;
                    //  buyOptions.getChildAt(i).setLayoutParams(params);
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
//                params = new LayoutParams(buyOptions.getLayoutParams());
//                for (int i = 0; i < buyOptions.getChildCount(); i++) {
//                    params.weight = 0.5f;
//                    params.gravity = Gravity.CENTER;
//                    buyOptions.getChildAt(i).setLayoutParams(params);
//                }
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
        int var1 = 0;
        if (GlobalVariables.onTablet(activity)) {
            var1 = (int) getResources().getDimension(R.dimen.game_footer_height);
        } else {
            var1 = (int) getResources().getDimension(R.dimen.game_footer_height);
        }
        heightForGridChild = (height - var1);
        Log.i("data", getResources().getDisplayMetrics().density + " " + height
                + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }


    //new code related to balls

    private void updateBallLay(double max, double min) {
        //oldcode
        double actualmin = selectedBetBean.getMinNo();
        double actualmax = selectedBetBean.getMaxNo();

        if (actualmax > 0) {
            totalBallWidth = width - (int) (2 * getResources().getDisplayMetrics().density) - (int) ((actualmax - 1) * getResources()
                    .getDisplayMetrics().density);
        }
        lastLay.setVisibility(View.VISIBLE);

        if (actualmax <= noOfBallsInSingleLine) {
            secondSelectedNosLay.setVisibility(View.GONE);
            isDrawSecond = false;
            noOfLay = 1;
        } else {
            secondSelectedNosLay.setVisibility(View.VISIBLE);
            isDrawSecond = true;
            noOfLay = 2;
        }
        if (isDrawSecond) {
            check = (int) Math.round((double) actualmax / 2);
        } else {
            check = (int) actualmax;
        }


        firstParentParms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
        firstSelectedNosLay.setLayoutParams(firstParentParms);


        if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin / noOfLay)) / noOfLay) {
            ballWidth = (int) (totalBallWidth / check - getPx(1));
            ballHeight = (int) (totalBallWidth / check - getPx(1));
        } else {
            ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
            ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
        }

        firstSelectedNosLay.removeAllViews();


        for (int i = 0; i < check; i++) {
            LinearLayout.LayoutParams firstParms = new LinearLayout.LayoutParams(
                    ballHeight, ballWidth);
            CustomTextView CustomTextView = new CustomTextView(
                    activity);
            CustomTextView.setTextColor(Color.WHITE);
            CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ballHeight / 2);
            CustomTextView.setText("");
            if (isDrawSecond) {
                if (i != noOfBallsInSingleLine - 1) {
                    firstParms.setMargins(0, 0, 1, 0);
                }
            } else {
                if (i != actualmax - 1) {
                    firstParms.setMargins(0, 0, 1, 0);
                }
            }
            if (min > i) {
                CustomTextView.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.impossible));
                CustomTextView.setGravity(Gravity.CENTER);
                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText("QP");
                CustomTextView.setTag("0");
                firstSelectedNosLay.addView(CustomTextView);
            } else {
                CustomTextView.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.impossible));
                CustomTextView.setGravity(Gravity.CENTER);
                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText("");
                CustomTextView.setTag("1");
                firstSelectedNosLay.addView(CustomTextView);
            }
        }
        if (isDrawSecond) {
            lastParentParms = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ((int) (selectedNosParentHeight - (getPx(2))))
                            / noOfLay);
            secondSelectedNosLay.setLayoutParams(lastParentParms);
            if (totalBallWidth / check < (selectedNosParentHeight
                    - getPx(2) - getPx(margin / noOfLay))
                    / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(1));
                ballHeight = (int) (totalBallWidth / check - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
            }
            secondSelectedNosLay.removeAllViews();
            for (int i = check; i < actualmax; i++) {
                LinearLayout.LayoutParams firstParms = new LinearLayout.LayoutParams(
                        ballHeight, ballWidth);
                CustomTextView CustomTextView = new CustomTextView(
                        activity);
                CustomTextView.setTextColor(Color.WHITE);
                CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        ballHeight / 2);
                if (i != actualmax - 1) {
                    firstParms.setMargins(0, 0, 1, 0);
                }

                if (min > i) {
                    CustomTextView.setGravity(Gravity.CENTER);
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));
                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText("QP");
                    secondSelectedNosLay.addView(CustomTextView);
                } else {
                    CustomTextView.setGravity(Gravity.CENTER);
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));
                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText("");
                    secondSelectedNosLay.addView(CustomTextView);
                }
            }//for loop

        } else {
            //  lastLay.setVisibility(View.VISIBLE);
        }


        lastLay.setVisibility(View.VISIBLE);
    }
    //


    private void updateBallLay(String[] numArr) {
        if (numArr.length >= 1) {
            totalBallWidth = width
                    - (int) (2 * getResources().getDisplayMetrics().density)
                    - (int) ((numArr.length - 1) * getResources()
                    .getDisplayMetrics().density);
            // }
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
                check = (int) Math.round((double) numArr.length / 2);
            } else {
                check = numArr.length;
            }
            firstParentParms = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
            firstSelectedNosLay.setLayoutParams(firstParentParms);

            if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin
                    / noOfLay))
                    / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(1));
                ballHeight = (int) (totalBallWidth / check - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
            }
            firstSelectedNosLay.removeAllViews();
            for (int i = 0; i < check; i++) {
                LinearLayout.LayoutParams firstParms = new LinearLayout.LayoutParams(
                        ballHeight, ballWidth);
                CustomTextView CustomTextView = new CustomTextView(
                        activity);
                CustomTextView.setTextColor(Color.WHITE);
                CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        ballHeight / 2);
                if (isDrawSecond) {
                    if (i != noOfBallsInSingleLine - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                } else {
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                }
                CustomTextView.setGravity(Gravity.CENTER);
                if (numArr[i].equals("")) {
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));
                } else {
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));
                }


                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText(numArr[i]);
                firstSelectedNosLay.addView(CustomTextView);
            }
            if (isDrawSecond) {
                lastParentParms = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        ((int) (selectedNosParentHeight - (getPx(2))))
                                / noOfLay);
                secondSelectedNosLay.setLayoutParams(lastParentParms);
                if (totalBallWidth / check < (selectedNosParentHeight
                        - getPx(2) - getPx(margin / noOfLay))
                        / noOfLay) {
                    ballWidth = (int) (totalBallWidth / check - getPx(1));
                    ballHeight = (int) (totalBallWidth / check - getPx(1));
                } else {
                    ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                            / noOfLay)))
                            / noOfLay;
                    ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                            / noOfLay)))
                            / noOfLay;
                }
                secondSelectedNosLay.removeAllViews();
                for (int i = /* noOfBallsInSingleLine */check; i < numArr.length; i++) {
                    LinearLayout.LayoutParams firstParms = new LinearLayout.LayoutParams(
                            ballHeight, ballWidth);
                    CustomTextView CustomTextView = new CustomTextView(
                            activity);
                    CustomTextView.setTextColor(Color.WHITE);
                    CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            ballHeight / 2);
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                    CustomTextView.setGravity(Gravity.CENTER);
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));

                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText(numArr[i]);
                    secondSelectedNosLay.addView(CustomTextView);
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
                    case Config.tenByNinety:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.tenByNinety));
                        break loop;
                }
            }
            drawData = new LinkedHashMap<String, DrawData>();
            finalDrawDatas = new ArrayList<>();
            // drawData.put(data.getDrawName(), data);
            JSONArray draws = gameObject.getJSONArray("draws");
            if (draws.length() <= 1) {
                advanceDrawMenu.setEnabled(false);
                advanceDrawMenu.setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        PorterDuff.Mode.SRC_IN);
            } else {
                advanceDrawMenu.setEnabled(true);
                advanceDrawMenu.setColorFilter(
                        getResources().getColor(R.color.five_color_three_ad),
                        PorterDuff.Mode.SRC_IN);
            }
            for (int i = 0; i < draws.length(); i++) {
                JSONObject advDrawObj = draws.getJSONObject(i);
                String time = advDrawObj.getString("drawDateTime");
                String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                DrawData data1 = new DrawData(advDrawObj.getString("drawId"),
                        advDrawObj.getString("drawName"),
                        drawTime, i);
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
            for (int i = 0; i < betTypeArr.length(); i++) {
                JSONObject betObject = betTypeArr.getJSONObject(i);

                String[] favArr = {""};
                LotteryPreferences lotteryPreferences = new LotteryPreferences(activity);
                switch (globalPref.getCountry().toUpperCase(Locale.US)) {
                    case "LAGOS":
                        lagosBetTypeUpdate(betObject, favArr, lotteryPreferences);
                        break;
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void lagosBetTypeUpdate(JSONObject betObject, String[] favArr, LotteryPreferences lotteryPreferences) {
        BetTypeBean bean = null;
        try {
            switch (betObject.getInt("betCode")) {
                //Perm1
                case 1:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 10, 10, new String[]{}, favArr, lotteryPreferences.getDIRECT_10().split(","));//direct 10
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 0);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static String[] getPickedNo() {
        String[] result = null;
        String value = "";
        for (int i = 0; i < DataSource.numbers.length; i++) {
            if (DataSource.numbers[i] == 1) {
                value += (i + 1);
                value += ",";
            }
        }
        result = value.split(",");
        return result;
    }

    public static String getPickedNo(String str[]) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < str.length; i++) {
            if (i != str.length - 1)
                result.append(str[i]).append(",");
            else
                result.append(str[i]);
        }
        return result.toString();
    }

    public static String[] getPickedNo(String str) {
        String[] result = null;

        result = str.split(",");
        return result;
    }

    private static int lagosBetUpdate(BetTypeBean bean, int minNo) {
        if (bean.getBetCode() == 1)
            minNo = 10;
        return minNo;
    }

    public void gameAmtCalculation(BetTypeBean bean) {
        int minNo = 0;
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "LAGOS":
                minNo = lagosBetUpdate(bean, minNo);
                break;
        }
        noOfLinesVal = 0;
        totalAmt = 0;
        if (numberSelected < minNo) {
            totalAmt = 0f;
            numberSelected = 0;
        } else {
            noOfLinesVal = getNoOfLines(bean.getBetCode(), numberSelected, globalPref);
        }
        totalAmt = unitPriceVal * noOfLinesVal;
        totalAmt = totalAmt * finalDrawDatas.size();
        noOfLines.setText(noOfLinesVal + "");
        unitPrice.setText(unitPriceVal + "");
        qpNos.setText(numberSelected + "");
        selectedNos.setText(numberSelected + "");
        finalAmt.setText(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(totalAmt) + "");
    }


    static long getNoOfLines(int betCode, int selectedNos, GlobalPref globalPref) {
        long lineNo = 0;
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "LAGOS":
                lineNo = lagosGetNoOfLines(betCode, selectedNos);
                break;
            default:
                lineNo = 0;
                break;
        }
        return lineNo;
    }


    private static long lagosGetNoOfLines(int betCode, int selectedNos) {
        int minNo = 0;
        minNo = getMinNoForLagos(betCode);
        if (selectedNos < minNo) {
            return 0;
        } else {
            switch (betCode) {
                case 1:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    private static int getMinNoForLagos(int betCode) {
        int minNo = 0;
        switch (betCode) {
            case 1:
                minNo = 1;
                break;
            case 2:
                minNo = 2;
                break;
            case 3:
                minNo = 3;
                break;
            case 4:
                minNo = 4;
                break;
            case 5:
                minNo = 5;
                break;
            case 6:
                minNo = 3;
                break;
            case 7:
                minNo = 4;
                break;
            case 8:
                minNo = 1;
                break;
            case 9:
                minNo = 1;
                break;
            case 10:
                minNo = 10;
                break;
            case 11:
                minNo = 2;
                break;
            case 15:
                minNo = 3;
                break;
        }
        return minNo;
    }

    static long combination(int n, int r) {
        return fact(n) / (fact(r) * fact(n - r));
    }

    static long fact(long n) {
        if (n == 0)
            return 1;
        else
            return n * fact(n - 1);
    }

    private void incDecHandler(double min, double max, double numberGap,
                               int clickedId, TextView CustomTextView, boolean isUnitPrice) {
        switch (clickedId) {
            case R.id.inc:
                increment(max, numberGap, CustomTextView, true);
                break;
            case R.id.dec:
                decrement(min, numberGap, CustomTextView, true);
                break;
            case R.id.qp_inc:
                increment(max, numberGap, CustomTextView, false);
                updateBallLay(max, min);
                break;
            case R.id.qp_dec:
                decrement(min, numberGap, CustomTextView, false);
                updateBallLay(max, min);
                break;
        }
    }

    public synchronized void increment(double max, double numberGap,
                                       TextView CustomTextView, boolean isUnitPrice) {
        double value = Double.parseDouble(CustomTextView.getText().toString());
        DecimalFormat format = new DecimalFormat("0.##");
        numberGap = Double.parseDouble(format.format(numberGap));
        if (value < max) {
            value += numberGap;
            value = Double.parseDouble(format.format(value));
            if (isUnitPrice) {
                unitPriceVal = value;
            } else {
                numberSelected = (int) value;
                if (numberSelected == 0) {
                    Utils.Toast(activity, "number is" + numberSelected);
                }
            }
            gameAmtCalculation(selectedBetBean);
        }
        if (isUnitPrice) {
            colorButtons(isUnitPrice, false, value);
        } else {
            colorButtons(isUnitPrice, true, value);

        }
    }

    public synchronized void decrement(double min, double numberGap,
                                       TextView CustomTextView, boolean isUnitPrice) {
        double value1 = Double.parseDouble(CustomTextView.getText().toString());
        DecimalFormat format = new DecimalFormat("0.##");
        numberGap = Double.parseDouble(format.format(numberGap));
        if (value1 > min) {
            value1 -= numberGap;
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
                        PorterDuff.Mode.SRC_IN);
                dec.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        PorterDuff.Mode.SRC_IN);
            } else {
                if (value >= selectedBetBean.getMaxBetAmtMul()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            PorterDuff.Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);

                } else if (value < selectedBetBean.getMaxBetAmtMul()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                }

                if (value == selectedBetBean.getUnitPrice()) {
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            PorterDuff.Mode.SRC_IN);
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                }
            }
        }
        if (isQP) {
            if (value >= selectedBetBean.getMaxNo()
                    && value == selectedBetBean.getMinNo()) {
                qpInc.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        PorterDuff.Mode.SRC_IN);
                qpDec.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        PorterDuff.Mode.SRC_IN);
            } else {
                if (value >= selectedBetBean.getMaxNo()) {
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            PorterDuff.Mode.SRC_IN);
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                } else if (value < selectedBetBean.getMaxNo()) {
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                }
                if (value == selectedBetBean.getMinNo()) {
                    qpDec.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            PorterDuff.Mode.SRC_IN);
                    qpInc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }

    private void viewEnable(Boolean enable) {
        edit.setEnabled(enable);
        betLayout.setEnabled(enable);
        favNos.setEnabled(enable);
        lastPckd.setEnabled(enable);
        quickPick.setEnabled(enable);
        selectNos.setEnabled(enable);
        buyNow.setEnabled(enable);
        advanceDrawMenu.setEnabled(enable);
    }

    private void openGrid(final View view, boolean toOpen) {
        if (!toOpen) {
            viewEnable(false);
            buyNow.setText(spannableString);
            buttonAdapter.setButtonClickable(false);
            gridView.setFocusable(false);
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    activity, R.anim.btncloseanim);
//            setEditVisible(View.VISIBLE);//old code
            btn_toogleanim.setAnimationListener(new Animation.AnimationListener() {
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
                    //scrollView.smoothScrollTo(0,0);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            view.setVisibility(View.GONE);
                            setEditVisible(View.VISIBLE);//new code
                            viewEnable(true);
                        }
                    }, 300);
                    // scrollToTop(unitLay);
                }
            });
            view.startAnimation(btn_toogleanim);
            //view.setVisibility(View.INVISIBLE);
        } else {
            viewEnable(false);
            gridView.setFocusable(true);
            buttonAdapter.setButtonClickable(true);
            buyNow.setText(activity.getResources().getString(R.string.ok_draw));
//            setEditVisible(View.INVISIBLE);//old code
            int r = view.getVisibility();
            if (r == 0) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        focusOnView();
                    }
                }, 100);
            } else {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Animation slide = null;
                        slide = new TranslateAnimation(0, 0, buy_lay.getTop(), scrollView.getTop());
                        slide.setDuration(300);
                        slide.setFillAfter(true);
                        slide.setFillEnabled(true);
                        view.setVisibility(View.VISIBLE);
                        view.startAnimation(slide);
                        slide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                view.clearAnimation();
                                view.scrollTo(0, scrollView.getTop());
                            }
                        });
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusOnView();
                    }
                }, 150);
            }
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
        animators.setDuration(1500L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

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
        if (isDrawFreeze) {
            editPanel(panelPosition, true);
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
                    if (data.getDrawName().equalsIgnoreCase("N/A") || data.getDrawName().equalsIgnoreCase("") || data.getDrawName().equalsIgnoreCase("null"))
                        drawName.setVisibility(View.GONE);
                    drawName.setText(data.getDrawName());
                    drawTime.setText(data.getDrawDateTime());
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

    private void updateTheme() {
        changeBetType.getCompoundDrawables()[2].setColorFilter(getResources()
                .getColor(R.color.jublee_mon_direct), PorterDuff.Mode.SRC_IN);
        advanceDrawMenu.setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
        edit.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
        qpDec.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
        qpInc.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
        dec.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
        inc.getBackground().setColorFilter(
                getResources().getColor(R.color.five_color_three), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (globalPref.getCountry().equalsIgnoreCase("lagos"))
                onCancel();
            else
                panelDatas = null;
            return;
        }
        if (requestCode == 100) {
            if (totalAmt < 0.50) {
                Utils.Toast(activity, "Minimum purchase of " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + " 0.50 are allowed.");
                return;
            } else {
                if (panelDatas == null)
                    panelDatas = new ArrayList<>();
                panelDatas.add(collectData());
                purchaseNow();
            }
        } else if (requestCode == 102) {// for after session out and for ghana too
            if (Double.parseDouble(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL).equals("") ? "0.0" : VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL))
                    >= totalPurchaseAmt) {
                buyNow();
            } else {
                Utils.Toast(activity, getString(R.string.insufficient_balance));
                if ((globalPref.getCountry().equalsIgnoreCase("GHANA") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                    editPanel(panelDatas.size() - 1, false);
                else if (globalPref.getCountry().equalsIgnoreCase("ZIM") && panelDatas.size() > 0)
                    panelDatas = null;
            }
        }
    }

    private void purchaseNow() {
        View.OnClickListener okay = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(activity)) {
                    dBox.dismiss();
                    analytics.sendAction(Fields.Category.FIVE_PURCHASE, Fields.Action.CLICK);
                    buyNow();
                } else {
                    GlobalVariables.showDataAlert(activity);
                }
            }
        };
        View.OnClickListener cancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelDatas = null;
                dBox.dismiss();
            }
        };
        dBox = new DownloadDialogBox(activity,
                activity.getResources().getString(R.string.purchase_conf), activity.getResources().getString(R.string.conf_five),
                true, true, okay, cancel);
        if (totalAmt > Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL)))) {
            Utils.Toast(
                    activity,
                    activity.getResources().getString(R.string.ins_bal_five)
            );
        } else {
            dBox.show();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (betDialog != null) {
            if (betDialog.isShowing()) {
                betDialog.cancel();
            }
        }
        if (darwDialog != null) {

            if (darwDialog.isShowing()) {
                darwDialog.cancel();
            }
        }
    }


    private void buyNow() {
        ticketSaleDta = new JSONObject();
        try {
            ticketSaleDta.put("gameCode", Config.tenByNinety);
            if (finalDrawDatas.get(0) == drawDatas.get(0) && finalDrawDatas.size() > 1) {
                ticketSaleDta.put("isAdvancePlay", true);
            } else if (finalDrawDatas.get(0) != drawDatas.get(0)) {
                ticketSaleDta.put("isAdvancePlay", true);
            } else {
                ticketSaleDta.put("isAdvancePlay", false);
            }
            ticketSaleDta.put("merchantCode", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE));
            ticketSaleDta.put("noOfDraws", finalDrawDatas.size());
            ticketSaleDta.put("noOfPanel", panelDatas.size());
            ticketSaleDta.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
            ticketSaleDta.put("userId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.PLAYER_ID));
            JSONArray drawArr = new JSONArray();
            for (int i = 0; i < finalDrawDatas.size(); i++) {
                JSONObject drawObj = new JSONObject();
                drawObj.put("drawId", finalDrawDatas.get(i).getDrawId());
                drawArr.put(drawObj);
            }
            JSONArray panelArr = new JSONArray();
            for (int i = 0; i < panelDatas.size(); i++) {
                JSONObject panelObj = new JSONObject();
                panelObj.put("betAmtMul", panelDatas.get(i).getBetAmtMul());
                panelObj.put("isQP", panelDatas.get(i).isQp());
                panelObj.put("pickedNumbers", panelDatas.get(i).getPickedNumbers());
                //old code
                panelObj.put("noPicked", panelDatas.get(i).getNoPicked());
                panelObj.put("playType", panelDatas.get(i).getPlayTypeName());
                panelArr.put(panelObj);
            }
            ticketSaleDta.put("panelData", panelArr);
            ticketSaleDta.put("drawData", drawArr);
            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpPlayMgmt/purchaseTicket";

//            try {
//                String fakeJson = "{\"responseCode\":0,\"isPromo\":false,\"saleTransId\":\"543\",\"availableBal\":1608.82,\"ticketData\":{\"purchaseAmt\":550,\"purchaseTime\":\"07-01-2016 16:03:04\",\"ticketNumber\":\"250001150072185\",\"gameName\":\"10\\/90\",\"gameCode\":\"KenoSeven\",\"drawData\":[{\"drawId\":\"311\",\"drawName\":\"Fortune Thursday\",\"drawTime\":\"19:00:00\",\"drawDate\":\"07-01-2016\"},{\"drawId\":\"295\",\"drawName\":\"Friday Bonanza\",\"drawTime\":\"19:00:00\",\"drawDate\":\"08-01-2016\"},{\"drawId\":\"2644\",\"drawName\":\"National Weekly\",\"drawTime\":\"19:00:00\",\"drawDate\":\"09-01-2016\"},{\"drawId\":\"386\",\"drawName\":\"Monday Special\",\"drawTime\":\"19:00:00\",\"drawDate\":\"11-01-2016\"},{\"drawId\":\"308\",\"drawName\":\"Lucky Tuesday\",\"drawTime\":\"19:00:00\",\"drawDate\":\"12-01-2016\"}],\"panelData\":[{\"unitPrice\":10,\"betAmtMul\":1,\"noOfLines\":1,\"isQP\":false,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"1,11,24,36,53,57,62,81,82,90\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":1,\"noOfLines\":1,\"isQP\":false,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"11,16,17,19,25,30,45,48,51,52\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":1,\"noOfLines\":1,\"isQP\":true,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"10,12,13,22,32,35,42,64,79,83\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":1,\"noOfLines\":1,\"isQP\":false,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"17,21,24,26,35,36,39,47,52,59\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":7,\"noOfLines\":1,\"isQP\":false,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"01,11,24,36,53,57,62,81,82,90\",\"panelPrice\":350}]},\"saleStatus\":\"DONE\"}";
//                Object resultData = fakeJson.toString();
//                onResult("buy",resultData, dia);
//            } catch (Exception ex) {
//            }
            new DGETask(TenByNinetyGameScreen.this, "BUY", url, ticketSaleDta.toString()).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PanelData collectData() {
        PanelData panelData = new PanelData();
        panelData.setBetAmtMul((int) Math.round(unitPriceVal / selectedBetBean.getUnitPrice()));
        totalPurchaseAmt += totalAmt;
        panelData.setPanelamount(Double.parseDouble(AmountFormat.getAmountFormatForSingleDecimal(totalAmt)));
        panelData.setSetSinglePanelAmt(Double.parseDouble(AmountFormat.getAmountFormatForSingleDecimal(totalAmt)) / finalDrawDatas.size());
        if (favNos.isChecked()) {
            noPckd = new StringBuffer();
            noPckdArr = selectedBetBean.getFavNos();
            for (int j = 0; j < noPckdArr.length; j++) {
                if (j == noPckdArr.length - 1)
                    noPckd.append(noPckdArr[j] + "");
                else
                    noPckd.append(noPckdArr[j] + ",");
            }
            panelData.setQp(false);
            panelData.setPickedNumbers(noPckd.toString() + "");
            panelData.setNoPicked(selectedBetBean.getFavNos().length + "");
        }
        if (lastPckd.isChecked()) {
            noPckd = new StringBuffer();
            noPckdArr = selectedBetBean.getLastPicked();
            for (int k = 0; k < noPckdArr.length; k++) {
                if (k == noPckdArr.length - 1)
                    noPckd.append(noPckdArr[k] + "");
                else
                    noPckd.append(noPckdArr[k] + ",");
            }
            panelData.setQp(false);
            panelData.setPickedNumbers(noPckd.toString() + "");
            Log.d("numberpicked", noPckd.toString());
            panelData.setNoPicked(selectedBetBean.getLastPicked().length + "");
            Log.d("numberpicked", selectedBetBean.getLastPicked().length + "");
        }
        if (quickPick.isChecked()) {
            panelData.setQp(true);
            panelData.setPickedNumbers("QP");
            panelData.setNoPicked(qpNos.getText().toString());
        }
        if (selectNos.isChecked()) {
            noPckd = new StringBuffer();
            noPckdArr = selectedBetBean.getCurrentNos();
            for (int l = 0; l < noPckdArr.length; l++) {
                if (l == noPckdArr.length - 1)
                    noPckd.append(noPckdArr[l]).append("");
                else
                    noPckd.append(noPckdArr[l]).append(",");
                //selectedBetBean.getCurrentNos()[0].equals("") ? 0 : selectedBetBean.getCurrentNos().length;
            }
            panelData.setNoPicked(selectedBetBean.getCurrentNos().length + "");
            panelData.setQp(false);
            panelData.setPickedNumbers(noPckd.toString());
        }
        panelData.setPlayType(selectedBetBean.getBetDisplayName());
        panelData.setPlayTypeName(selectedBetBean.getBetName());
        return panelData;
    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "BUY":
                if (resultData != null) {
                    try {
//                        try {
//                            String fakeJson = "{\"responseCode\":0,\"isPromo\":false,\"saleTransId\":\"543\",\"availableBal\":1608.82,\"ticketData\":{\"purchaseAmt\":550,\"purchaseTime\":\"07-01-2016 16:03:04\",\"ticketNumber\":\"250001150072185\",\"gameName\":\"10/90\",\"gameCode\":\"KenoSeven\",\"drawData\":[{\"drawId\":\"311\",\"drawName\":\"Fortune Thursday\",\"drawTime\":\"19:00:00\",\"drawDate\":\"07-01-2016\"},{\"drawId\":\"295\",\"drawName\":\"Friday Bonanza\",\"drawTime\":\"19:00:00\",\"drawDate\":\"08-01-2016\"},{\"drawId\":\"2644\",\"drawName\":\"National Weekly\",\"drawTime\":\"19:00:00\",\"drawDate\":\"09-01-2016\"},{\"drawId\":\"386\",\"drawName\":\"Monday Special\",\"drawTime\":\"19:00:00\",\"drawDate\":\"11-01-2016\"},{\"drawId\":\"308\",\"drawName\":\"Lucky Tuesday\",\"drawTime\":\"19:00:00\",\"drawDate\":\"12-01-2016\"}],\"panelData\":[{\"unitPrice\":10,\"betAmtMul\":5,\"noOfLines\":5,\"isQP\":false,\"numberPicked\":50,\"betName\":\"Direct10\",\"pickedNumbers\":\"12,13,24,44,53,57,62,81,82,90\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":1,\"noOfLines\":1,\"isQP\":false,\"numberPicked\":10,\"betName\":\"Direct10\",\"pickedNumbers\":\"11,16,17,19,25,30,45,48,51,52\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":2,\"noOfLines\":2,\"isQP\":true,\"numberPicked\":22,\"betName\":\"Direct10\",\"pickedNumbers\":\"10,12,13,22,32,35,42,64,79,83\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":3,\"noOfLines\":3,\"isQP\":false,\"numberPicked\":30,\"betName\":\"Direct10\",\"pickedNumbers\":\"17,21,24,26,35,36,39,47,52,59\",\"panelPrice\":50},{\"unitPrice\":10,\"betAmtMul\":4,\"noOfLines\":4,\"isQP\":false,\"numberPicked\":40,\"betName\":\"Direct10\",\"pickedNumbers\":\"01,11,24,36,53,57,62,81,82,90\",\"panelPrice\":350}]},\"saleStatus\":\"DONE\"}";
//                            resultData = fakeJson.toString();
//                        } catch (Exception ex) {
//                        }
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
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
                            UserInfo.setLogout(activity);
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            startActivityForResult(new Intent(activity, LoginActivity.class), 102);
                            activity.overridePendingTransition(GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 3017) {
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            JSONArray array = jsonResult.getJSONArray("games");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.tenByNinety)) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("draws");
                                    drawDatas = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject advDrawObj = jsonArray.getJSONObject(j);
                                        String time = advDrawObj.getString("drawDateTime");
                                        String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                                        DrawData data1 = new DrawData(advDrawObj.getString("drawId"), advDrawObj.getString("drawName"), drawTime, i);
                                        if (j == 0) {
                                            data1.setSelected(true);
                                            finalDrawDatas.add(data1);
                                        } else {
                                            data1.setSelected(false);
                                        }
                                        drawDatas.add(data1);
                                    }
                                    DrawDialog.count = 1;
                                    restDraws.setVisibility(View.INVISIBLE);
                                    showOne = false;
                                    darwDialog = new DrawDialog(activity, drawDatas, TenByNinetyGameScreen.this, true);
                                    darwDialog.setCancelable(false);
                                    darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                                    darwDialog.show();
                                    MainScreen.parseRefreshedData(array);
                                    dialog.dismiss();
                                    break;
                                }
                            }
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            if (globalPref.getCountry().equalsIgnoreCase("Lagos") && panelDatas.size() > 0)
                                editPanel(panelPosition, false);
                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                        dialog.dismiss();
                        if (globalPref.getCountry().equalsIgnoreCase("lagos") && panelDatas.size() > 0)
                            editPanel(panelPosition, false);
                        GlobalVariables.showServerErr(activity);
                    }
                } else {
                    dialog.dismiss();
                    if (globalPref.getCountry().equalsIgnoreCase("lagos") && panelDatas.size() > 0)
                        editPanel(panelPosition, false);
                    GlobalVariables.showServerErr(activity);
                }
                break;
        }
    }


    public static void hide_keyboard(Activity activity) {
        unitPrice.setCursorVisible(false);
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {

        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.edit:
                    hide_keyboard(activity);
                    openGrid(gridView, true);
                    break;
                case R.id.buy_now:
                    if (buyNow.getText().toString().equalsIgnoreCase("OK")) {
                        hide_keyboard(activity);
                        if (DataSource.Keno.numbersSelected >= selectedBetBean
                                .getMinNo()) {
                            selectedBetBean.setCurrentNos(getPickedNo());
                            openGrid(gridView, false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    updateBallLay(getPickedNo());
                                }
                            }, 700);
                            numberSelected = selectedBetBean.getCurrentNos().length;
                            gameAmtCalculation(selectedBetBean);
                        } else {
                            Utils.Toast(
                                    activity,
                                    activity.getResources().getString(R.string.plz_sel_at_lest_five)
                                            + selectedBetBean.getMinNo()
                                            + activity.getResources().getString(R.string.numb_five));
                        }
                    } else if (buyNow.getText().toString().equalsIgnoreCase(activity.getResources().getString(R.string.proceed))) {
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
                            if (totalAmt < 0.5) {
//                                ToastMessage.getInstance(activity, getString(R.string.dialog_notice), Toast.LENGTH_LONG);
                                Utils.Toast(activity, "Minimum purchase of "
                                        + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
                                        + " 0.50 are allowed.");
                            } else {
                                if (panelDatas == null) {
                                    panelDatas = new ArrayList<>();
                                }
//                                updatePanelDraw(panelDatas);
                                panelDatas.add(panelPosition, collectData());
                                ++noofpanel;
//                                updateBetValiadtion(selectedBetBean);
                                showAlertDialog();
                            }
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
                                if (panelDatas == null)
                                    panelDatas = new ArrayList<>();
                                panelDatas.add(collectData());
                                purchaseNow();
                            }
                        }
                    }
                    break;
                case R.id.bet_lay:
                    hide_keyboard(activity);
                    betDialog.show();
                    break;
                case R.id.advance_menu:
                    hide_keyboard(activity);
                    showOne = false;
                    darwDialog = new DrawDialog(activity, drawDatas, TenByNinetyGameScreen.this, false);
                    darwDialog.setCancelable(false);
                    darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                    darwDialog.show();
                    break;
                case R.id.fav_nos:
                case R.id.last_picked:
                case R.id.quick_pick:
                case R.id.select_nos:
                    hide_keyboard(activity);
                    updateUI(v);
                    break;
            }
        }
    };

    private void gameAmtCalculationUpdate(PanelData panelData) {
        for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
            betType.getValue().setCurrentNos(new String[]{});
        }
        for (int i = 0; i < panelDatas.size() - 1; i++) {
            updatePanelData(panelDatas.get(i), true);
        }
    }

    private void updatePanelDraw(ArrayList<PanelData> panelDatas) {
        for (int i = 0; i < panelDatas.size(); i++) {
            gameAmtCalculationUpdate(panelDatas.get(i));
        }
//        if (panelDatas.size() > 1) {
//            panelDatas.get(panelDatas.size() - 1).setDrawSize(finalDrawDatas.size());
//            lastDrawDataSize = panelDatas.get(panelDatas.size() - 1).getDrawSize();
//            for (int i = 0; i < (panelDatas.size() - 1); i++) {
//                panelDatas.get(i).setPanelamount(panelDatas.get(i).getPanelamount() / lastDrawDataSize);
//            }
//        } else {
//            panelDatas.get(0).setDrawSize(drawDatas.size());
//        }
//        lastDrawDataSize = finalDrawDatas.size();
    }

    public void editPanel(int position, boolean editCheck) {
        PanelData panelData = panelDatas.remove(position);
        for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
            betType.getValue().setCurrentNos(new String[]{});
        }
        updatePanelData(panelData, editCheck);
        --noofpanel;
        panelPosition = position;
        totalPurchaseAmt -= panelData.getPanelamount();
        if (commentDialog.isShowing())
            commentDialog.dismiss();
    }

    public void setNoofPanel() {
        --noofpanel;
        if (noofpanel < 5) {
            add_panel_lay.setVisibility(View.VISIBLE);
        }
        if (noofpanel == 0) {
            commentDialog.dismiss();
            noofpanel = 0;
            panelPosition = 0;
            for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
                betType.getValue().setCurrentNos(new String[]{});
            }
            selectedBetBean = (BetTypeBean) betTypeData.entrySet().iterator()
                    .next().getValue();
            betName.setText(selectedBetBean.getBetDisplayName());
            BetDialog.selectedPos = 0;
            updateBetValiadtion(selectedBetBean);
            panelPosition = panelDatas.size();
        }
    }

    private void updateBetValiadtion(BetTypeBean typeBean) {
        DataSource.Keno.numbersSelected = 0;
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

        buttonAdapter.updateBean(typeBean);
        buttonAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);
    }


    private void showConfirmation() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        // alertDialog
        // .setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Would you like to play another game ?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//						 showDialog(dialog_preview);
                        showAlertDialog();
                    }
                });
        alertDialog.show();

    }

    // panel Dialog Box Open method
    DialogListAdapterTenByNinty adapter;
    Dialog commentDialog;
    LinearLayout add_panel_lay;

    public void showAlertDialog() {
        commentDialog = new Dialog(activity,
                R.style.PanelDialog);
        commentDialog.setCancelable(false);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.ticket_preview_dialog);
        if (!finalDrawDatas.get(0).getDrawName().equals("") || finalDrawDatas.get(0).getDrawName() != null || finalDrawDatas.size() >= 2) {
            if (finalDrawDatas.size() >= 2)
                ((TextView) commentDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName() + " (" + (finalDrawDatas.size() - 1) + "+)");
            else
                ((TextView) commentDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName());
        } else {
            if (finalDrawDatas.size() >= 2)
                ((TextView) commentDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName() + " (" + (finalDrawDatas.size() - 1) + "+)");
            else
                ((TextView) commentDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName());
        }
        TextView totalAmtText = (TextView) commentDialog
                .findViewById(R.id.ttlamt_text);
        TextView totalAmt = (TextView) commentDialog.findViewById(R.id.ttlamt);
        ListView dialogList = (ListView) commentDialog
                .findViewById(R.id.dialogTextContent);
        add_panel_lay = (LinearLayout) commentDialog.findViewById(R.id.add_panel_lay);


        //new Layout

        LinearLayout totalPanel = (LinearLayout) commentDialog.findViewById(R.id.panel_data_totalAmt);
        TextView panelText = (TextView) commentDialog.findViewById(R.id.no_of_panel_text);
        TextView panelEditText = (TextView) commentDialog.findViewById(R.id.no_of_panel_edit_text);

        LinearLayout drawData = (LinearLayout) commentDialog.findViewById(R.id.panel_data);
        TextView drawDataText = (TextView) commentDialog.findViewById(R.id.no_of_panel_text2);
        TextView drawDataEditText = (TextView) commentDialog.findViewById(R.id.no_of_panel_edit_text2);

        totalPanel.setVisibility(View.VISIBLE);
        drawData.setVisibility(View.VISIBLE);


        totalAmtText.setText("Total Amount" + "("
                + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + ")");

        double totalAmtPanel = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            totalAmtPanel = totalAmtPanel + (panelDatas.get(i).getSetSinglePanelAmt() * finalDrawDatas.size());
        }

        totalAmt.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(totalAmtPanel)));
        drawDataText.setText("Total Draws");
        drawDataEditText.setText("");
        drawDataEditText.setText(finalDrawDatas.size() + "");


        double totalAmtPanelWD = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            totalAmtPanelWD = totalAmtPanelWD + (panelDatas.get(i).getSetSinglePanelAmt());
        }
        panelText.setText("Total Panel Amount");
        panelEditText.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(totalAmtPanelWD)));

        adapter = new DialogListAdapterTenByNinty(this, R.layout.ticket_preview_row, panelDatas, totalAmt, commentDialog, panelEditText);
        dialogList.setAdapter(adapter);
        Button done = (Button) commentDialog.findViewById(R.id.dialogDone);
        Button add = (Button) commentDialog.findViewById(R.id.add_more);
        Button dialogCancel = (Button) commentDialog
                .findViewById(R.id.dialogCancel);

        if (noofpanel == 5) {
            add_panel_lay.setVisibility(View.GONE);
        } else {
            add_panel_lay.setVisibility(View.VISIBLE);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!UserInfo.isLogin(activity)) {
                    commentDialog.dismiss();
                    startActivityForResult(new Intent(activity,
                            LoginActivity.class), 102);
                    activity.overridePendingTransition(GlobalVariables.startAmin,
                            GlobalVariables.endAmin);
                } else {
                    if (Double.parseDouble(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL).equals("") ? "0.0" : VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL))
                            >= totalPurchaseAmt) {
                        panelPosition = panelDatas.size() - 1;
                        if (panelDatas.size() > 0) {
                            commentDialog.cancel();
                            buyNow();
                        } else {
                            Utils.Toast(activity,
                                    getString(R.string.dialog_notice_buy)
                            );
                        }
                    } else {
                        Utils.Toast(activity,
                                getString(R.string.insufficient_balance)
                        );
                    }
                }
            }
        });


        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
                commentDialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
                    betType.getValue().setCurrentNos(new String[]{});
                }
                selectedBetBean = betTypeData.entrySet().iterator()
                        .next().getValue();
                betName.setText(selectedBetBean.getBetDisplayName());
                BetDialog.selectedPos = 0;
                updateBetValiadtion(selectedBetBean);
                panelPosition = panelDatas.size();
                commentDialog.dismiss();
            }
        });
        commentDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.widget_progress_dialog_bg);

        commentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        commentDialog.show();
    }


    private void onCancel() {
        panelPosition = panelDatas.size() - 1;
        if (panelPosition == -1) {
            noofpanel = 0;
            panelPosition = 0;
            for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
                betType.getValue().setCurrentNos(new String[]{});
            }
            selectedBetBean = betTypeData.entrySet().iterator()
                    .next().getValue();
            betName.setText(selectedBetBean.getBetDisplayName());
            BetDialog.selectedPos = 0;
            updateBetValiadtion(selectedBetBean);
            panelPosition = panelDatas.size();
            commentDialog.dismiss();
            return;
        }
        if (panelDatas.size() > 0) {
//                    updatePanelData(panelDatas.get(panelDatas.size() - 1));
//                    totalPurchaseAmt -= panelDatas.get(panelDatas.size() - 1).getPanelamount();
//                    panelDatas.remove(panelDatas.size() - 1);
            editPanel(panelPosition, false);
        } else {
            updateBetValiadtion(selectedBetBean);
        }
    }

    DebouncedOnTouchListener touchListener = new DebouncedOnTouchListener(500) {
        @Override
        public void onDebouncedClick(View v, MotionEvent event) {
            if (v.getId() == R.id.advance_menu) {
                hide_keyboard(activity);
                showOne = false;
                darwDialog = new DrawDialog(activity, drawDatas, TenByNinetyGameScreen.this, false);
                darwDialog.setCancelable(false);
                darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                darwDialog.show();
            }
        }
    };


    //for long press handling
    View.OnTouchListener incDecTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int id = v.getId();
            if (id == R.id.inc) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event
                        .getAction() == MotionEvent.ACTION_CANCEL) && isIncremented) {
                    isIncremented = false;
                }
            } else if (id == R.id.dec) {
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
                case R.id.dec:
                    isDecremented = true;
                    onLongPressCounterHandler
                            .postDelayed(new CounterValueIncDec(), 100);
                    break;
                case R.id.inc:
                    isIncremented = true;
                    onLongPressCounterHandler
                            .postDelayed(new CounterValueIncDec(), 100);
                    break;
            }
            return false;
        }
    };

    class CounterValueIncDec implements Runnable {
        @Override
        public void run() {
            if (isIncremented) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.inc, unitPrice,
                        true);
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            } else if (isDecremented) {
                incDecHandler(selectedBetBean.getUnitPrice(),
                        selectedBetBean.getMaxBetAmtMul(),
                        selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                        true);
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            }
        }

    }


}
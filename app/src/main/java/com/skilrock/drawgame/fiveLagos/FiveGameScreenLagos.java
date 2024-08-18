package com.skilrock.drawgame.fiveLagos;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.skilrock.adapters.ButtonAdapter;
import com.skilrock.adapters.DialogListAdapter;
import com.skilrock.adapters.DrawAdapater;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.DrawData;
import com.skilrock.bean.GamePlay;
import com.skilrock.bean.PanelData;
import com.skilrock.config.Config;
import com.skilrock.config.DismissListener;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.BetDialog;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomEditText;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextView.TextStyles;
import com.skilrock.customui.CustomTextViewDown;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DebouncedOnTouchListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawDialog;
import com.skilrock.customui.ExpandableGridView;
import com.skilrock.drawgame.DGPlayActivity;
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
import com.skilrock.utils.DecimalDigitsInputFilter;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.skilrock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FiveGameScreenLagos extends Fragment implements DismissListener, WebServicesListener {
    // private String[] numArr = new String[] { "22", "32", "12", "36", "22",
    // "32", "12", "36" };
//    private String[] numArr = new String[]{"22", "32", "12", "36", "19",
//            "28", "45", "62", "88", "55", "63", "44", "25"};
    private HashMap<String, Integer> betPosition;
    private Analytics analytics;
    public ArrayList<DrawData> finalDrawDatas;
    private RelativeLayout playInfoLay;
    private CustomTextView buyNow/* , okay , selectNos */;
    private CustomTextViewDown scrollTextView;
    private ScrollView scrollView;
    private ExpandableGridView gridView;
    private boolean isVisible, isFav = true, isLast = true;
    private LinearLayout buyOptions, lastLay, lastLaySub;
    private LayoutParams params, buyLP;
    private int key;
    private CustomCheckedTextView favNos, lastPckd, quickPick, selectNos;
    private int textSize;
    private LinearLayout firstSelectedNosLay, secondSelectedNosLay;
    //    private PanelBean panelBean;
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
    private LayoutParams firstParentParms, lastParentParms;
    private int noOfLay;
    private int check;
    public TextView selectedNos, qpNos, noOfLines, finalAmt;
    public EditText unitPrice;
    private ImageView dec, inc;
    private ImageView qpDec, qpInc;
    private BetTypeBean selectedBetBean;
    public double unitPriceVal;
    public int numberSelected;
    private JSONObject gameObject;
    private RelativeLayout betLayout;
    private ImageView edit;
    private ArrayList<DrawData> drawDatas;
    public double totalAmt = 0;
    public long noOfLinesVal;
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
    public double totalPurchaseAmt = 0;
    public int length = 0;
    private boolean isIncremented = false;
    private boolean isDecremented = false;
    private Handler onLongPressCounterHandler;


    //lagos update
    private RelativeLayout ulBlLay;
    private ImageView qpDecUl, qpIncUl, qpDecBl, qpIncBl;
    private CustomTextView qpNosUl, qpNosBl;
    public int numberSelectedUL;
    public int numberSelectedBL;
    public ArrayList<GamePlay> lagosBet = new ArrayList<>();

    public int bankerUlmin;
    public int bankerUlmax;
    public int bankerBlmin;
    public int bankerBlmax;
    private boolean isLagos = false;
    private boolean isNext = false;
    private boolean isQuick = false;
    private boolean isFirst = false;
    private String ulText = "UL";
    private String blText = "BL";
    private GlobalPref globalPref;

    private Dialog panelDialog;
    private boolean isForceDismiss = true;
    private double MinBetAmountMultipleForDc = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        Utils.consolePrint("five game screen on Create");
        activity.getWindow().setSoftInputMode(InputMethodManager.HIDE_NOT_ALWAYS);
        onLongPressCounterHandler = new Handler();
//        context = getActivity();
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        analytics.setScreenName(Fields.Screen.FIVE_GAME_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        betPosition = new HashMap<>();
        isLagos = false;
        if (globalPref.getCountry().equalsIgnoreCase("zim")) {
            spannableString = new SpannableString(getResources().getString(R.string.play_now_text));
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, 0);
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 4, spannableString.length(), 0);
        } else if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            spannableString = new SpannableString(getResources().getString(R.string.proceed));
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        } else if (globalPref.getCountry().equalsIgnoreCase("Lagos")) {
            spannableString = new SpannableString(getResources().getString(R.string.play_now_text));
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
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
//        ((DGPlayActivity) activity).enableTouchEvent(true);
    }

    public boolean hide_keyboard(Activity activity, BetTypeBean selectedBetBean) {
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            buyNow.setFocusable(true);
            ((CustomEditText) unitPrice).setIsKeyBoardShow(false);
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (isCheckUnitPrice(selectedBetBean, unitPrice)) {
                unitPrice.setCursorVisible(false);
                gameAmtCalculation(selectedBetBean);
                return true;
            } else {
                return false;
            }
        } else
            return true;
    }

    //for lagos
    private void colorButtonsBanker(String betName, int number) {
        ImageView incbanker;
        ImageView decbanker;
        int minbanker;
        int maxbanker;

        if (betName.equalsIgnoreCase("below")) {
            incbanker = qpIncBl;
            decbanker = qpDecBl;
            minbanker = bankerBlmin;
            maxbanker = bankerBlmax;
        } else {
            incbanker = qpIncUl;
            decbanker = qpDecUl;
            minbanker = bankerUlmin;
            maxbanker = bankerUlmax;
        }

        if (number >= maxbanker && number == minbanker) {
            incbanker.getBackground().setColorFilter(
                    getResources().getColor(R.color.disabled_color),
                    Mode.SRC_IN);
            decbanker.getBackground().setColorFilter(
                    getResources().getColor(R.color.disabled_color),
                    Mode.SRC_IN);
        } else {
            if (number >= maxbanker) {
                incbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
                decbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.five_color_three),
                        Mode.SRC_IN);
            } else if (number < maxbanker) {
                incbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.five_color_three),
                        Mode.SRC_IN);
                decbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.five_color_three),
                        Mode.SRC_IN);
            }
            if (number == minbanker) {
                decbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
                incbanker.getBackground().setColorFilter(
                        getResources().getColor(R.color.five_color_three),
                        Mode.SRC_IN);
            }
        }
    }

    public void hide_keyboard(Activity activity) {
        unitPrice.setCursorVisible(false);
        buyNow.setFocusable(true);
        setUnitPrice();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.consolePrint("five game screen on Create View");
        View view = inflater.inflate(R.layout.five_game_screen, null);
        bindViewIds(view);
        updateTheme();


//        edit.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (Double.parseDouble(unitPrice.getText().toString()) > 0) {
//                    } else {
//                        unitPriceVal = selectedBetBean.getUnitPrice();
//                        unitPrice.setText(unitPriceVal + "");
//                    }
//                } catch (Exception e) {
//                    unitPriceVal = selectedBetBean.getUnitPrice();
//                    unitPrice.setText(unitPriceVal + "");
//                }
//                if (hide_keyboard(activity, selectedBetBean)) {
//                    colorButtons(true, false, unitPriceVal);
//                } else
//                    return;
//                hide_keyboard(activity);
//                openGrid(gridView, true);
//            }
//        });

        getDisplayDetails();
        // updateBallLay(numArr);
        parseJson();
//        new MThread(this);
        if (globalPref.getCountry().equalsIgnoreCase("Lagos")) {
            bankerDataForLagos();
        }


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
                textSize, selectedBetBean, 3);
        gridView.setAdapter(buttonAdapter);

        updateBetValiadtion(selectedBetBean);
        restDraws.setTextStyle(TextStyles.BOLD);
        restDraws.setVisibility(View.INVISIBLE);
        betDialog = new BetDialog(activity, new ArrayList<BetTypeBean>(
                betTypeData.values()), betName);

        betDialog.setCancelable(false);

        betDialog.setOnKeyListener(new DialogKeyListener(betDialog));


        //for lagos


        qpDecBl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bankerDataForLagos();
                if (numberSelectedBL == bankerBlmin)
                    incDecHandlerForLagos(bankerBlmin, bankerBlmax, numberSelectedBL, qpNosBl, "below");
                else
                    incDecHandlerForLagos(bankerBlmin, bankerBlmax - 1, numberSelectedBL - 1, qpNosBl, "below");
            }
        });
        qpIncBl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bankerDataForLagos();
                if (numberSelectedBL == bankerBlmax)
                    incDecHandlerForLagos(bankerBlmin, bankerBlmax, numberSelectedBL, qpNosBl, "below");
                else
                    incDecHandlerForLagos(bankerBlmin, bankerBlmax + 1, numberSelectedBL + 1, qpNosBl, "below");
            }
        });


        qpDecUl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bankerDataForLagos();
                if (numberSelectedUL == bankerUlmin)
                    incDecHandlerForLagos(bankerUlmin, bankerUlmax, numberSelectedUL, qpNosUl, "upper");
                else
                    incDecHandlerForLagos(bankerUlmin, bankerUlmax - 1, numberSelectedUL - 1, qpNosUl, "upper");
            }
        });
        qpIncUl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bankerDataForLagos();
                if (numberSelectedUL == bankerUlmax)
                    incDecHandlerForLagos(bankerUlmin, bankerUlmax, numberSelectedUL, qpNosUl, "upper");
                else
                    incDecHandlerForLagos(bankerUlmin, bankerUlmax, numberSelectedUL + 1, qpNosUl, "upper");
            }
        });


        qpInc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    if (Double.parseDouble(unitPrice.getText().toString()) > 0) {
//                    } else {
//                        unitPriceVal = selectedBetBean.getUnitPrice();
//                        unitPrice.setText(unitPriceVal + "");
//                    }
//                } catch (Exception e) {
//                    unitPriceVal = selectedBetBean.getUnitPrice();
//                    unitPrice.setText(unitPriceVal + "");
//                }
//                if (hide_keyboard(activity, selectedBetBean)) {
//                    colorButtons(true, false, unitPriceVal);
//                } else
//                    return;
                hide_keyboard(activity);
                incDecHandler(numberSelected + 1,
                        selectedBetBean.getMaxNo(), 1, R.id.qp_inc, qpNos,
                        false);
                if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
                    selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, Integer.parseInt(qpNos.getText().toString().trim())));
                    colorButtons(true, false, unitPriceVal);
                }
            }
        });
        qpDec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                try {
//                    if (Double.parseDouble(unitPrice.getText().toString()) > 0) {
//                    } else {
//                        unitPriceVal = selectedBetBean.getUnitPrice();
//                        unitPrice.setText(unitPriceVal + "");
//                    }
//                } catch (Exception e) {
//                    unitPriceVal = selectedBetBean.getUnitPrice();
//                    unitPrice.setText(unitPriceVal + "");
//                }
//                if (hide_keyboard(activity, selectedBetBean)) {
//                    colorButtons(true, false, unitPriceVal);
//                } else
//                    return;
                hide_keyboard(activity);
                if (numberSelected == selectedBetBean.getMinNo()) {
                    incDecHandler(numberSelected,
                            selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                            false);
                } else {
                    incDecHandler(numberSelected - 1,
                            selectedBetBean.getMaxNo(), 1, R.id.qp_dec, qpNos,
                            false);
                }
                if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
                    double unitPrice = setUnitPriceVal(selectedBetBean, Integer.parseInt(qpNos.getText().toString().trim()));
                    selectedBetBean.setLowerUnitPrice(unitPrice);
                    if (unitPriceVal < unitPrice) {
                        unitPriceVal = unitPrice;
                        gameAmtCalculation(selectedBetBean);
                    } else
                        colorButtons(true, false, unitPriceVal);
                }
            }
        });
        inc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Double.parseDouble(unitPrice.getText().toString()) > 0) {
                        if (hide_keyboard(activity, selectedBetBean)) {
                            colorButtons(true, false, unitPriceVal);
                        } else return;
                        // old code
                        hide_keyboard(activity);
                        incDecHandler(selectedBetBean.getUnitPrice(),
                                selectedBetBean.getMaxBetAmtMulUnitPrice(),
                                selectedBetBean.getUnitPrice(), R.id.inc, unitPrice,
                                true);
                    } else {
                        unitPriceVal = selectedBetBean.getLowerUnitPrice();
                        unitPrice.setText(unitPriceVal + "");
                        if (hide_keyboard(activity, selectedBetBean)) {
                            colorButtons(true, false, unitPriceVal);
                        } else return;
                    }
                } catch (Exception e) {
//                    Utils.Toast(activity, "Invalid play amount", Toast.LENGTH_SHORT);
                    unitPriceVal = selectedBetBean.getLowerUnitPrice();
                    unitPrice.setText(unitPriceVal + "");
                    if (hide_keyboard(activity, selectedBetBean)) {
                        colorButtons(true, false, unitPriceVal);
                    } else return;
                }
            }
        });
        dec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Double.parseDouble(unitPrice.getText().toString()) > 0) {
                    } else {
                        unitPriceVal = selectedBetBean.getLowerUnitPrice();
                        unitPrice.setText(unitPriceVal + "");
                    }
                    if (hide_keyboard(activity, selectedBetBean)) {
                        colorButtons(true, false, unitPriceVal);
                    } else return;
                    hide_keyboard(activity);
                    incDecHandler(selectedBetBean.getLowerUnitPrice(),
                            selectedBetBean.getMaxBetAmtMul(),
                            selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                            true);
                } catch (Exception e) {
//                    Utils.Toast(activity, "Invalid play amount", Toast.LENGTH_SHORT);
                    unitPriceVal = selectedBetBean.getLowerUnitPrice();
                    unitPrice.setText(unitPriceVal + "");
                    if (hide_keyboard(activity, selectedBetBean)) {
                        colorButtons(true, false, unitPriceVal);
                    } else return;
                    incDecHandler(selectedBetBean.getLowerUnitPrice(),
                            selectedBetBean.getMaxBetAmtMul(),
                            selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                            true);
                }
            }
        });

        inc.setOnTouchListener(incDecTouchListener);
        dec.setOnTouchListener(incDecTouchListener);
        inc.setOnLongClickListener(incDecLongClickListener);
        dec.setOnLongClickListener(incDecLongClickListener);
        advanceDrawMenu.setOnTouchListener(touchListener);
        edit.setOnClickListener(listener);


        betDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                betName.setText(BetDialog.selectedBet);
                selectedBetBean = betTypeData.get(betName.getText());
                // for lagos
                if (globalPref.getCountry().equalsIgnoreCase("Lagos") && (selectedBetBean.getBetCode() == 8 || selectedBetBean.getBetCode() == 26)) {
                    isLagos = true;
                } else {
                    isLagos = false;
                }
                updateBetValiadtion(selectedBetBean);
                unitPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter((selectedBetBean.getMaxBetAmtMul() + "").length(),
                        AmountFormat.getDecimalCount(selectedBetBean.getUnitPrice())), inputFilter});
            }
        });

        lastLay.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, selectedNosParentHeight));
        lastLaySub.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, selectedNosParentHeight));
        lastLaySub.setGravity(Gravity.LEFT);
        lastLaySub.setOrientation(LinearLayout.VERTICAL);

        edit.setOnClickListener(listener);
        betLayout.setOnClickListener(listener);
        favNos.setOnClickListener(listener);
        lastPckd.setOnClickListener(listener);
        quickPick.setOnClickListener(listener);
        selectNos.setOnClickListener(listener);
        buyNow.setOnClickListener(listener);
        gridView.setExpanded(true);

        // for Edittext new Change Requirement
        unitPrice.setOnFocusChangeListener(changeListener);
        unitPrice.setOnClickListener(focusClickListener);
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            unitPrice.setFocusable(true);
            unitPrice.setFocusableInTouchMode(true);
            unitPrice.setCursorVisible(false);
        } else
            unitPrice.setEnabled(false);
        unitPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter((selectedBetBean.getMaxBetAmtMul() + "").length(),
                AmountFormat.getDecimalCount(selectedBetBean.getUnitPrice())), inputFilter});
        unitPrice.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    try {
                    hide_keyboard(activity);
                       /* if ((Double.parseDouble(v.getText().toString())) < 0.5) {
                            Utils.Toast(activity, "Minimum amount should be " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + " 0.5");
                        } else*/
//                        if (((int) (Double.parseDouble(v.getText().toString()) * 10) % ((int) (selectedBetBean.getUnitPrice() * 10))) > 0) {
//                            Utils.Toast(activity, "Please enter play amount in multiple of " + selectedBetBean.getUnitPrice());
//                            unitPriceVal = selectedBetBean.getLowerUnitPrice();
//                            colorButtons(true, false, unitPriceVal);
//                            gameAmtCalculation(selectedBetBean);
//                        } else if (Double.parseDouble(v.getText().toString()) <= 0) {
//                            Utils.Toast(activity, "Please enter play amount greater than " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
//                                    + selectedBetBean.getLowerUnitPrice());
//                            unitPriceVal = selectedBetBean.getLowerUnitPrice();
//                            colorButtons(true, false, unitPriceVal);
//                            gameAmtCalculation(selectedBetBean);
//                        } else if (Double.parseDouble(v.getText().toString()) > selectedBetBean.getMaxBetAmtMulUnitPrice()) {
//                            Utils.Toast(activity, "Please enter play amount less than or equal to " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
//                                    + selectedBetBean.getMaxBetAmtMulUnitPrice());
//                            unitPriceVal = selectedBetBean.getLowerUnitPrice();
//                            colorButtons(true, false, unitPriceVal);
//                            gameAmtCalculation(selectedBetBean);
//                        } else {
//                            unitPriceVal = Double.parseDouble(v.getText().toString());
//                            colorButtons(true, false, unitPriceVal);
//                            gameAmtCalculation(selectedBetBean);
//                            return false;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utils.Toast(activity, "Invalid play amount");
//                    }
//                    return true;
                    return !isCheckUnitPrice(selectedBetBean, (EditText) v);
                }
                return false;
            }
        });
        manageTabs();


        return view;
    }


    private void bankerDataForLagos() {
        bankerUlmin = lagosBet.get(0).getMinValue();
        bankerUlmax = lagosBet.get(0).getMaxValue();
        bankerBlmin = lagosBet.get(1).getMinValue();
        bankerBlmax = lagosBet.get(1).getMaxValue();
    }


    private void updateBallLayPick() {
        String totalStrin[];
        if ((selectedBetBean.getUlNos().length == 0) || (selectedBetBean.getBlNos().length == 0)) {
            updateBallLay(new String[0]);
        } else {
            List<String> both = new ArrayList<String>(selectedBetBean.getUlNos().length + selectedBetBean.getBlNos().length + 2);
            Collections.addAll(both, selectedBetBean.getUlNos());
            Collections.addAll(both, ulText);
            Collections.addAll(both, selectedBetBean.getBlNos());
            Collections.addAll(both, blText);
            both.toArray(new String[both.size()]);
            totalStrin = both.toArray(new String[both.size()]);
            updateBallLay(totalStrin);
        }
    }

    private void incDecHandlerForLagos(int min, int max, int number, CustomTextView tv, String
            betName) {
        if (betName.equalsIgnoreCase("upper")) {
            tv.setText(number + "");
            numberSelectedUL = number;
        } else if (betName.equalsIgnoreCase("below")) {
            tv.setText(number + "");
            numberSelectedBL = number;
        }
        updateBallLayForLagosBanker(numberSelectedUL, numberSelectedBL);
        colorButtonsBanker(betName, number);
        gameAmtCalculation(selectedBetBean);
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
        unitPrice = (CustomEditText) view.findViewById(R.id.unit_price);
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

        //new for lagos
        ulBlLay = (RelativeLayout) view.findViewById(R.id.ul_bl_lay);

        qpDecUl = (ImageView) view.findViewById(R.id.qp_dec_ul);
        qpNosUl = (CustomTextView) view.findViewById(R.id.qp_nos_ul);
        qpIncUl = (ImageView) view.findViewById(R.id.qp_inc_ul);

        qpDecBl = (ImageView) view.findViewById(R.id.qp_dec_bl);
        qpNosBl = (CustomTextView) view.findViewById(R.id.qp_nos_bl);
        qpIncBl = (ImageView) view.findViewById(R.id.qp_inc_bl);

        //for ball Layout bl and ul
//        ulTextDivider = (CustomTextView) view.findViewById(R.id.ul_text_divider);
//        ulText = (CustomTextView) view.findViewById(R.id.ul_text);
//        blText = (CustomTextView) view.findViewById(R.id.bl_text);
//        lastLayBanker = (LinearLayout) view.findViewById(R.id.last_lay_banker);
//        lastLaySubBanker = (LinearLayout) view.findViewById(R.id.last_lay_sub_banker);

        //extra layout
//        lastLayBankerUl = (LinearLayout) view.findViewById(R.id.last_lay_banker_ul);
//        lastLaySubBankerUl = (LinearLayout) view.findViewById(R.id.last_lay_sub_banker_ul);
//        firstSelectedNosUl = (LinearLayout) view.findViewById(R.id.first_selected_nos_ul);
//        secondSelectedNosBankerUl = (LinearLayout) view.findViewById(R.id.second_selected_nos_banker_ul);
        /*
        for ghana Keyboard hide problem By mehul
         */
        ((CustomEditText) unitPrice).setFragment(this);
        ((CustomEditText) unitPrice).setIsKeyBoardShow(false);
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
        System.out.println("we are here in OnDestroy");
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
                slide.setAnimationListener(new AnimationListener() {
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


//    private OnClickListener commonListener = new OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            isNext = false;
//            hide_keyboard(activity);
//            updateUI(view);
//        }
//    };

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
            restNoLines.setVisibility(View.VISIBLE);
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
                // Utils.Toast(activity,
                // ((CustomCheckedTextView) view).getText().toString(),
                // 1000).show();
                favNos.setChecked(true);
                lastPckd.setChecked(false);
                quickPick.setChecked(false);
                selectNos.setChecked(false);
                isQuick = false;
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.VISIBLE);
                buyNow.setText(spannableString);
                numberSelected = selectedBetBean.getFavNos().length;
                selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, selectedBetBean.getMinNo()));
                unitPriceVal = selectedBetBean.getLowerUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, selectedBetBean.getUnitPrice());
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

//                //for lagos
//                if (isLagos) {
//                    ulBlLay.setVisibility(View.GONE);
//                }
                ulBlLay.setVisibility(View.GONE);

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
                isQuick = true;
                lastLay.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                qpNoLines.setVisibility(View.GONE);
                restNoLines.setVisibility(View.VISIBLE);
                buyNow.setText(spannableString);
                numberSelected = selectedBetBean.getLastPicked().length;
                if (numberSelected < selectedBetBean.getMinNo())
                    selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, selectedBetBean.getMinNo()));
                else
                    selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, numberSelected));
                unitPriceVal = selectedBetBean.getLowerUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, unitPriceVal);

//                for lagos
                if (isLagos) {
                    if (selectedBetBean.getLastPicked()[0].equals("")) {
                    } else {
                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(selectedBetBean.getLastPicked()));
                        numberSelectedUL = arrayList.indexOf(ulText);
                        numberSelectedBL = arrayList.indexOf(blText) - (numberSelectedUL + 1);
                        numberSelected = numberSelectedUL + numberSelectedBL;
                    }
                }
                //
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
                ulBlLay.setVisibility(View.GONE);

//                //for lagos
//                if ((isLagos)) {
//                    ulBlLay.setVisibility(View.GONE);
//                }
                break;
            case R.id.quick_pick:
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

                isQuick = true;
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
                Utils.consolePrint("number selected in QP" + numberSelected);
                selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, selectedBetBean.getMinNo()));
                unitPriceVal = selectedBetBean.getLowerUnitPrice();
                colorButtons(false, true, numberSelected);
                colorButtons(true, false, unitPriceVal);

                //new code related to balls
                max = selectedBetBean.getMaxNo();
                min = selectedBetBean.getMinNo();
                updateBallLay(max, min);

                //for lagos
                if (isLagos) {
                    qpNoLines.setVisibility(View.GONE);
                    restNoLines.setVisibility(View.GONE);
                    lastLay.setVisibility(View.GONE);
                    ulBlLay.setVisibility(View.VISIBLE);

                    numberSelectedUL = bankerUlmin;
                    numberSelectedBL = bankerBlmin;

                    qpNosUl.setText(numberSelectedUL + "");
                    qpNosBl.setText(numberSelectedBL + "");

                    colorButtonsBanker("upper", numberSelectedUL);
                    colorButtonsBanker("below", numberSelectedBL);

                    updateBallLayForLagosBanker(numberSelectedUL, numberSelectedBL);

                } else {
                    ulBlLay.setVisibility(View.GONE);
                }
                gameAmtCalculation(selectedBetBean);
                buyNow.setText(spannableString);
                break;
            case R.id.select_nos:
                analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.CLICK, Fields.Label.PICK_NEW);

                if (isLagos) {
//                    selectedBetBean.setCurrentNos(new String[]{});
                    if (selectedBetBean.getLastPicked().length > 0 && !isFirst) {
                        isFirst = true;
                        selectedBetBean.setBlNos(new String[]{});
                        selectedBetBean.setUlNos(new String[]{});
                    } else if (selectedBetBean.getBlNos().toString().equalsIgnoreCase(selectedBetBean.getCurrentNos().toString())) {
                        selectedBetBean.setCurrentNos(selectedBetBean.getUlNos());
                        selectedBetBean.setMinAndMaxNo(1, 4);
                    }
                }
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
                    isQuick = false;
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
                    selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, selectedBetBean.getMinNo()));
                    unitPriceVal = selectedBetBean.getLowerUnitPrice();

                    //old zim code
//                    numberSelected = selectedBetBean.getCurrentNos().length;
//                    unitPriceVal = selectedBetBean.getUnitPrice();
                    colorButtons(false, true, numberSelected);
                    colorButtons(true, false, unitPriceVal);
                    gameAmtCalculation(selectedBetBean);
                    if (isLagos) {
                        updateBallLayPick();
                    } else {
                        updateBallLay(selectedBetBean.getCurrentNos());
                    }
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
                            buttonAdapter.updateBean(selectedBetBean);
                            buttonAdapter.notifyDataSetChanged();
                        }
                    });


                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LayoutParams params = new LayoutParams(
                                    LayoutParams.MATCH_PARENT,
                                    (int) (heightForGridChild - gridView.getHeight()));
                            playInfoLay.setLayoutParams(params);
                        }
                    }, 50);
                    ulBlLay.setVisibility(View.GONE);
                    // for lagos
//                    if (isLagos) {
//                        ulBlLay.setVisibility(View.GONE);
//                    }
                }
                break;
        }
        buyLP = (LayoutParams) selectNos.getLayoutParams();
        changeColor();

    }

    private void updateBallLayForLagosBanker(int numberSelectedUL, int numberSelectedBL) {

        int ulmax = bankerUlmax;
        int blmax = bankerBlmax;

        int total = ulmax + blmax + 2;

        String ulStrin[] = new String[ulmax + 1];
        String blStrin[] = new String[blmax + 1];
        String totalStrin[];


        for (int i = 0; i < (ulmax + 1); i++) {
            if (numberSelectedUL > i) {
                ulStrin[i] = "QP";
            } else if ((ulmax) == i) {
                ulStrin[i] = ulText;
            } else {
                ulStrin[i] = "";
            }
        }

        for (int i = 0; i < (blmax + 1); i++) {
            if (numberSelectedBL > i) {
                blStrin[i] = "QP";
            } else if ((blmax) == i) {
                blStrin[i] = blText;
            } else {
                blStrin[i] = "";
            }
        }

        List<String> both = new ArrayList<String>(ulStrin.length + blStrin.length);
        Collections.addAll(both, ulStrin);
        Collections.addAll(both, blStrin);
        both.toArray(new String[both.size()]);

        totalStrin = both.toArray(new String[both.size()]);
        updateBallLay(totalStrin);
    }

    private String[] splitLastPicked(String[] lastPicked) {
        //ul,bl
        ArrayList<String> lastData = new ArrayList<>();
        String lastPickedArray[];

        for (int i = 0; i < lastPicked.length; i++) {
            if (lastPicked[i].equalsIgnoreCase(ulText) || lastPicked[i].contentEquals(blText)) {

            } else {
                lastData.add(lastPicked[i]);
            }
        }
        lastPickedArray = lastData.toArray(new String[lastData.size()]);
        return lastPickedArray;
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
                        Mode.SRC_IN);
                Drawable drawable1 = getResources().getDrawable(R.drawable.strip_down1);
                drawable1.setColorFilter(getResources().getColor(R.color.jublee_mon_direct_select), Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null, drawable1);
                checkedTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                checkedTextView.setTextColor(getResources().getColor(
                        R.color.jublee_mon_direct_select));
            } else {
                //checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.four_options_text),
                        Mode.SRC_IN);
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
                params = new LayoutParams(buyOptions.getLayoutParams());
                for (int i = 0; i < buyOptions.getChildCount(); i++) {
                    params.weight = 0.25f;
                    params.gravity = Gravity.CENTER;
                    //  buyOptions.getChildAt(i).setLayoutParams(params);
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
//        changeColor();
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
        Utils.logPrint("data-" + getResources().getDisplayMetrics().density + " " + height
                + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }


    //new code related to balls

    private void updateBallLay(double max, double min) {

        //for lagos
        if (isLagos) {
            noOfBallsInSingleLine = 12;
        }
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


        firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT,
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
            LayoutParams firstParms = new LayoutParams(
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
            lastParentParms = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
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
                LayoutParams firstParms = new LayoutParams(
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
                    //CustomTextView.setTag("0");
                    secondSelectedNosLay.addView(CustomTextView);

                } else {


                    CustomTextView.setGravity(Gravity.CENTER);
                    CustomTextView.setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.impossible));
                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText("");
                    // CustomTextView.setTag("1");
                    secondSelectedNosLay.addView(CustomTextView);
                }
                // lastLay.setVisibility(View.VISIBLE);
            }//for loop

        } else {
            //  lastLay.setVisibility(View.VISIBLE);
        }


        lastLay.setVisibility(View.VISIBLE);
    }
    //


    private void updateBallLay(String[] numArr) {

        //for lagos
        int ul = 0;
        int bl = 0;
        //

        if (numArr.length >= 1) {

            //for lagos
//        splitLastPicked
            if (isLagos) {
                noOfBallsInSingleLine = 12;
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(numArr));
                ul = arrayList.indexOf(ulText);
                bl = arrayList.indexOf(blText);
                numArr = splitLastPicked(numArr);
            }
            //


            // if (numArr.length > noOfBallsInSingleLine) {
            // totalBallWidth = width
            // - (int) (2 * getResources().getDisplayMetrics().density)
            // - (int) ((noOfBallsInSingleLine) * getResources()
            // .getDisplayMetrics().density);
            // } else {
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
            firstParentParms = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
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
                LayoutParams firstParms = new LayoutParams(
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


                //for lagos
                if (isLagos) {
                    if (i > (ul - 1)) {
                        CustomTextView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.impossiblebl));
                    } else {
                        CustomTextView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.impossible));
                    }
                } else {
                    //


                    if (numArr[i].equals("")) {
                        CustomTextView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.impossible));
                    } else {
                        CustomTextView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.impossible));
                    }


                }


                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText(numArr[i]);
                firstSelectedNosLay.addView(CustomTextView);
            }
            if (isDrawSecond) {
                lastParentParms = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
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
                    LayoutParams firstParms = new LayoutParams(
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
                    //for lagos
                    if (isLagos) {
                        if (i > (ul - 1)) {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossiblebl));
                        } else {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossible));
                        }
                    } else {
                        //

                        CustomTextView.setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.impossible));

                    }
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
                    case Config.fiveGameNameLagos:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.fiveGameNameLagos));
                        if (!gameObject.optString("MinBetAmountMultipleForDc").equalsIgnoreCase(""))
                            MinBetAmountMultipleForDc = Math.max(gameObject.getDouble("MinBetAmountMultipleForDc"), 1);
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
            Utils.consolePrint("final DrawData" + finalDrawDatas.size() + "");
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
                        getResources().getColor(R.color.five_color_three_ad),
                        Mode.SRC_IN);
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
//                String[] lastPicked = {"6","12","14","43", "12"};
//
                LotteryPreferences lotteryPreferences = new LotteryPreferences(activity);
//                String[] favArr = betObject.getString("favNo").split(",");
//                String[] lastPicked = betObject.getString("lastPicked").split(",");


                switch (globalPref.getCountry()) {
                    case "GHANA":
                        ghanaBetTypeUpdate(betObject, favArr, lotteryPreferences);
                        break;
                    case "ZIM":
                        zimBetTypeUpdate(betObject, favArr, lotteryPreferences);
                        break;
                    case "LAGOS":
                        lagosBetTypeUpdate(betObject, favArr, lotteryPreferences);
                        break;
                }


                //old code Zim
//                BetTypeBean bean = null;
//                switch (betObject.getString("betDispName")) {
//                    case "Perm1":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 10, 10, new String[]{}, favArr, lotteryPreferences.getPERM_1().split(","));
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                    case "Perm2":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 5, 20, new String[]{}, favArr, lotteryPreferences.getPERM_2().split(","));
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                    case "Perm3":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 5, 20, new String[]{}, favArr, lotteryPreferences.getPERM_3().split(","));
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                    case "Direct6":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 12, 24, new String[]{}, favArr, lotteryPreferences.getDIRECT_5().split(","));
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                    case "Perm6":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 7, 15, new String[]{}, favArr, lotteryPreferences.getPERM_6().split(","));
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                    case "zeroToNine":
//                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.getString("betDispName"), betObject.getInt("betCode"), 7, 15, new String[]{}, new String[]{"8"}, new String[]{"8"});
//                        betTypeData.put(bean.getBetDisplayName(), bean);
//                        break;
//                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Utils.consolePrint("in Exception parse json");
            e.printStackTrace();
        }
    }


    private void ghanaBetTypeUpdate(JSONObject betObject, String[] favArr, LotteryPreferences
            lotteryPreferences) {
        BetTypeBean bean = null;
        try {
            switch (betObject.getInt("betCode")) {
                //direct 1
                case 1:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 1, new String[]{}, favArr, lotteryPreferences.getDIRECT_1().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 0);
                    break;
                //direct 2
                case 2:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 2, 2, new String[]{}, favArr, lotteryPreferences.getDIRECT_2().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 1);
                    break;
                //direct 3
                case 3:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 3, new String[]{}, favArr, lotteryPreferences.getDIRECT_3().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 2);
                    break;
                //direct 4
                case 4:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 4, new String[]{}, favArr, lotteryPreferences.getDIRECT_4().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 3);
                    break;
                //direct 5
                case 5:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 5, new String[]{}, favArr, lotteryPreferences.getDIRECT_5().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 4);
                    break;
                //Perm2
                case 6:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 20, new String[]{}, favArr, lotteryPreferences.getPERM_2().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 5);
                    break;
                //Perm3
                case 7:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 20, new String[]{}, favArr, lotteryPreferences.getPERM_3().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 6);
                    break;
                //Banker1AgainstAll
                case 9:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 1, new String[]{}, favArr, lotteryPreferences.getBANKER_1_AGAINST_ALL().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    betPosition.put(bean.getBetDisplayName(), 7);
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void lagosBetTypeUpdate(JSONObject betObject, String[] favArr, LotteryPreferences
            lotteryPreferences) {
        BetTypeBean bean = null;
        String[] lastPicked;
        try {
            switch (betObject.getInt("betCode")) {
                //Perm1
                case 1:
                case 19:
                    lastPicked = betObject.getInt("betCode") == 1 ? lotteryPreferences.getDIRECT_1_LAGOS().split(",") : lotteryPreferences.getMN_DIRECT1_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 1, new String[]{}, favArr, lastPicked);//direct 1//19
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 2:
                case 20:
                    lastPicked = betObject.getInt("betCode") == 2 ? lotteryPreferences.getDIRECT_2_LAGOS().split(",") : lotteryPreferences.getMN_DIRECT2_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 2, 2, new String[]{}, favArr, lastPicked);//direct 2//20
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 3:
                case 21:
                    lastPicked = betObject.getInt("betCode") == 3 ? lotteryPreferences.getDIRECT_3_LAGOS().split(",") : lotteryPreferences.getMN_DIRECT3_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 3, new String[]{}, favArr, lastPicked);//direct 3//21
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 4:
                case 22:
                    lastPicked = betObject.getInt("betCode") == 4 ? lotteryPreferences.getDIRECT_4_LAGOS().split(",") : lotteryPreferences.getMN_DIRECT4_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 4, new String[]{}, favArr, lastPicked);//direct 4//22
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 5:
                case 23:
                    lastPicked = betObject.getInt("betCode") == 5 ? lotteryPreferences.getDIRECT_5_LAGOS().split(",") : lotteryPreferences.getMN_DIRECT5_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 5, new String[]{}, favArr, lastPicked);//direct 5//23
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 6:
                case 24:
                    lastPicked = betObject.getInt("betCode") == 6 ? lotteryPreferences.getPERM_2_LAGOS().split(",") : lotteryPreferences.getMN_PERM2_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 20, new String[]{}, favArr, lastPicked);// perm2 //24
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 7:
                case 25:
                    lastPicked = betObject.getInt("betCode") == 7 ? lotteryPreferences.getPERM_3_LAGOS().split(",") : lotteryPreferences.getMN_PERM3_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 20, new String[]{}, favArr, lastPicked);// perm3//25
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 8:
                case 26:
                    lastPicked = betObject.getInt("betCode") == 8 ? lotteryPreferences.getBANKER_LAGOS().split(",") : lotteryPreferences.getMN_BANKER_LAGOS().split(",");
//                    String[] lastPicked = {"39", "40", "46", "UL", "20", "21", "31", "1", "3", "20", "21", "11", "BL"};
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 4, new String[]{}, favArr, lastPicked);//BANKER//26
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    lagosBet.add(new GamePlay(1, 4, betObject.getInt("maxBetAmtMul"), betObject.getInt("betCode"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName")));
                    lagosBet.add(new GamePlay(1, 20, betObject.getInt("maxBetAmtMul"), betObject.getInt("betCode"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName")));

                    break;
                case 9:
                case 27:
                    lastPicked = betObject.getInt("betCode") == 9 ? lotteryPreferences.getBANKER_1_AGAINST_ALL_LAGOS().split(",") : lotteryPreferences.getMN_BANKER1AGAINSTALL_LAGOS().split(",");
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 1, new String[]{}, favArr, lastPicked);//BANKER_1_AGAINST_ALL//27
                    bean.setBetName(betObject.optString("betDispName"));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                //DC-Perm3
                case 16:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 20, new String[]{}, favArr, lotteryPreferences.getDC_Perm3_LAGOS().split(","));//Dc-perm3
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 12:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 3, new String[]{}, favArr, lotteryPreferences.getDC_DIRECT3_LAGOS().split(","));//DC-Direct3
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 10:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 10, 10, new String[]{}, favArr, lotteryPreferences.getPERM_1_LAGOS().split(","));//PERM1
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 11:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 2, 2, new String[]{}, favArr, lotteryPreferences.getDC_Direct2_ALL_LAGOS().split(","));//DC_Direct2
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 15:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 20, new String[]{}, favArr, lotteryPreferences.getDC_Perm2_ALL_LAGOS().split(","));//DC_Perm2
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void zimBetTypeUpdate(JSONObject betObject, String[] favArr, LotteryPreferences
            lotteryPreferences) {
        BetTypeBean bean = null;
        try {
            switch (betObject.getInt("betCode")) {
                //Perm1
                case 1:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 10, 10, new String[]{}, favArr, lotteryPreferences.getPERM_1().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                //Perm2
                case 2:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 20, new String[]{}, favArr, lotteryPreferences.getPERM_2().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                //Perm3
                case 3:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 20, new String[]{}, favArr, lotteryPreferences.getPERM_3().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 4:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 12, 24, new String[]{}, favArr, lotteryPreferences.getDIRECT_5().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 5:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 7, 15, new String[]{}, favArr, lotteryPreferences.getPERM_6().split(","));
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
                case 6:
                    bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 7, 15, new String[]{}, new String[]{"8"}, new String[]{"8"});
                    bean.setMaxBetAmtAndUnitPrice(setMaxUnitPriceVal(bean), setUnitPriceVal(bean, bean.getMinNo()));
                    bean.setBetName(betObject.optString("betDispName"));
                    betTypeData.put(bean.getBetDisplayName(), bean);
                    break;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void updateBetValiadtion(BetTypeBean typeBean) {
        DataSource.Keno.numbersSelected = 0;
        switch (selectedBetBean.getBetCode()) {
            case 1:
                analytics.sendAll(Fields.Category.FIVE_BET_TYPE, Fields.Action.OPEN, Fields.Label.FIVE_PERM_1);
                break;
            case 2:
                analytics.sendAll(Fields.Category.FIVE_BET_TYPE, Fields.Action.OPEN, Fields.Label.FIVE_PERM_2);
                break;
            case 3:
                analytics.sendAll(Fields.Category.FIVE_BET_TYPE, Fields.Action.OPEN, Fields.Label.FIVE_PERM_3);
                break;

        }


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
        // FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        // LayoutParams.MATCH_PARENT, (int) heightForGridChild);
        // gridView.setLayoutParams(params);

        buttonAdapter.updateBean(typeBean);
        buttonAdapter.notifyDataSetChanged();
//        gridView.setAdapter(new ButtonAdapter(activity,
//                (int) widthForGridChild / 9, (int) heightForGridChild / 10,
//                textSize, typeBean, 1));
        // Toast.makeText(activity,
        // typeBean.getBetCode() + " " + typeBean.getBetDisplayName(),
        // 1000).show();
    }

    public String[] getPickedNo() {
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

    public String getPickedNo(String str[]) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < str.length; i++) {
            if (i != str.length - 1)
                result.append(str[i]).append(",");
            else
                result.append(str[i]);
        }
        return result.toString();
    }

    public String[] getPickedNo(String str) {
        String[] result = null;

        result = str.split(",");
        return result;
    }


    private int lagosBetUpdate(BetTypeBean bean, int minNo) {
        if (bean.getBetCode() == 6 || bean.getBetCode() == 24)
            minNo = 3;
        else if (bean.getBetCode() == 7 || bean.getBetCode() == 25 || bean.getBetCode() == 16)
            minNo = 4;
        else if (bean.getBetCode() == 1 || bean.getBetCode() == 9 || bean.getBetCode() == 19 || bean.getBetCode() == 27 || bean.getBetCode() == 12)
            minNo = 1;
        else if (bean.getBetCode() == 2 || bean.getBetCode() == 20)
            minNo = 2;
        else if (bean.getBetCode() == 3 || bean.getBetCode() == 21)
            minNo = 3;
        else if (bean.getBetCode() == 4 || bean.getBetCode() == 22)
            minNo = 4;
        else if (bean.getBetCode() == 5 || bean.getBetCode() == 23)
            minNo = 5;
        else if (bean.getBetCode() == 8)
            minNo = bean.getMinNo();
        else if (bean.getBetCode() == 11)
            minNo = 2;
        else if (bean.getBetCode() == 15)
            minNo = 3;
        return minNo;
    }

    private int ghanaBetUpdate(BetTypeBean bean, int minNo) {
        if (bean.getBetCode() == 6)
            minNo = 3;
        else if (bean.getBetCode() == 7)
            minNo = 4;
        else if (bean.getBetCode() == 1 || bean.getBetCode() == 9 || bean.getBetCode() == 12)
            minNo = 1;
        else if (bean.getBetCode() == 2)
            minNo = 2;
        else if (bean.getBetCode() == 3)
            minNo = 3;
        else if (bean.getBetCode() == 4)
            minNo = 4;
        else if (bean.getBetCode() == 5)
            minNo = 5;
        return minNo;
    }

    private int zimBetUpdate(BetTypeBean bean, int minNo) {
        if (bean.getBetCode() == 1)
            minNo = 10;
        else if (bean.getBetCode() == 2 || bean.getBetCode() == 3)
            minNo = 5;
        else
            minNo = 0;
        return minNo;
    }

    public void gameAmtCalculation(BetTypeBean bean) {
        int minNo = 0;
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "GHANA":
                minNo = ghanaBetUpdate(bean, minNo);
                break;
            case "ZIM":
                minNo = zimBetUpdate(bean, minNo);
                break;
            case "LAGOS":
                minNo = lagosBetUpdate(bean, minNo);
                break;
        }

        //old Code Zim
//        if (bean.getBetCode() == 1)
//            minNo = 10;
//        else if (bean.getBetCode() == 2 || bean.getBetCode() == 3)
//            minNo = 5;

        noOfLinesVal = 0;
        totalAmt = 0;
        if (numberSelected < minNo) {
            Utils.consolePrint("number seleted " + numberSelected);
            totalAmt = 0;
            numberSelected = 0;
            if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
                double unitPrice = setUnitPriceVal(selectedBetBean, minNo);
                if (unitPriceVal < unitPrice)
                    unitPriceVal = unitPrice;
                selectedBetBean.setLowerUnitPrice(unitPrice);
            }
        } else {
            Utils.consolePrint("number seleted here ");
            noOfLinesVal = getNoOfLines(bean.getBetCode(), numberSelected);
            if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
                if (numberSelected == minNo) {
                    double unitPrice = setUnitPriceVal(selectedBetBean, minNo);
                    if (unitPriceVal < unitPrice)
                        unitPriceVal = unitPrice;
                    selectedBetBean.setLowerUnitPrice(unitPrice);
                } else
                    selectedBetBean.setLowerUnitPrice(setUnitPriceVal(selectedBetBean, numberSelected));
            }
        }
        colorButtons(true, false, unitPriceVal);

        if (isLagos) {
            if (isQuick) {
                noOfLinesVal = numberSelectedBL * numberSelectedUL;
            } else {
                if (!isNext) {
                    bean.setUlNos(bean.getCurrentNos());
                    String values = null;

                    //new code
                    ArrayList<String> noList = new ArrayList<>();
                    for (int i = 0; i < bean.getBlNos().length; i++) {
                        noList.add(bean.getBlNos()[i]);
                    }

                    for (int i = 0; i < bean.getUlNos().length; i++) {
                        if (noList.contains(bean.getUlNos()[i])) {
                            int matchPos = noList.indexOf(bean.getUlNos()[i]);
                            noList.remove(matchPos);
                        }
                    }

                    String[] blData = noList.toArray(new String[noList.size()]);
                    bean.setBlNos(blData);
                } else
                    bean.setBlNos(bean.getCurrentNos());
                noOfLinesVal = bean.getBlNos().length * bean.getUlNos().length;
                numberSelected = bean.getBlNos().length + bean.getUlNos().length;

            }
        }
        JSONObject multiple = new JSONObject();//arushi
        Utils.consolePrint("unit Price" + unitPriceVal);
        Utils.consolePrint("no of Lines" + noOfLinesVal);
        totalAmt = unitPriceVal * noOfLinesVal;
        Utils.consolePrint("total Amt" + totalAmt);
        totalAmt = totalAmt * finalDrawDatas.size();
        if ((selectedBetBean.getBetCode() == 11 || selectedBetBean.getBetCode() == 15 || selectedBetBean.getBetCode() == 12 || selectedBetBean.getBetCode() == 16) && (globalPref.getCountry().equalsIgnoreCase("LAGOS"))) {
            totalAmt = totalAmt * MinBetAmountMultipleForDc;
        } else {
            totalAmt = totalAmt * 1;
        }
        noOfLines.setText(noOfLinesVal + "");
        unitPrice.setText(unitPriceVal + "");
        qpNos.setText(numberSelected + "");
        selectedNos.setText(numberSelected + "");
        switch (globalPref.getCountry()) {
            case "ZIM":
//                totalAmt = AmountFormat.roundDrawTktAmt(totalAmt);
                break;
        }
        finalAmt.setText(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(totalAmt) + "");
//        if (globalPref.getCountry().equalsIgnoreCase("ghana"))
//            if (finalAmt.getText().length() > 7)
//                finalAmt.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.text_marquee));
    }


    private long getNoOfLines(int betCode, int selectedNos) {
        long lineNo = 0;
        switch (globalPref.getCountry()) {
            case "GHANA":
                lineNo = ghanaGetNoOfLines(betCode, selectedNos);
                break;

            case "ZIM":
                lineNo = zimGetNoOfLines(betCode, selectedNos);
                break;

            case "LAGOS":
                lineNo = lagosGetNoOfLines(betCode, selectedNos);
                break;

            default:
                lineNo = 0;
                break;

        }
        return lineNo;
    }


    //    static long getNoOfLines(int betCode, int selectedNos) {
//        int minNo = 0;
//        if (betCode == 1)
//            minNo = 10;
//        else if (betCode == 2 || betCode == 3)
//            minNo = 5;
//        if (selectedNos < minNo) {
//            return 0;
//        } else {
//            switch (betCode) {
//
//                case 1:
//                    return selectedNos;
//                case 2:
//                    return combination(selectedNos, 2);
//                case 3:
//                    return combination(selectedNos, 3);
//                case 4:
//                    return selectedNos;
//                case 5:
//                    return selectedNos;
//                case 6:
//                    return combination(selectedNos, 2);
//                case 7:
//                    return combination(selectedNos, 3);
//                case 8:
//                    return 0;
//                case 9:
//                    return 0;
//                case 11:
//                    return selectedNos;
//                case 15:
//                    return selectedNos;
//                default:
//                    return 0;
//            }
//        }
//    }

    private long zimGetNoOfLines(int betCode, int selectedNos) {
        int minNo = 0;
        if (betCode == 1)
            minNo = 10;
        else if (betCode == 2 || betCode == 3)
            minNo = 5;
        if (selectedNos < minNo) {
            return 0;
        } else {
            switch (betCode) {
                case 1:
                    return selectedNos;
                case 2:
                    return combination(selectedNos, 2);
                case 3:
                    return combination(selectedNos, 3);
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
    }

    private long lagosGetNoOfLines(int betCode, int selectedNos) {
        int minNo = 0;
        minNo = getMinNoForLagos(betCode);
        if (selectedNos < minNo) {
            return 0;
        } else {
            switch (betCode) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 12://for dcdirect3
                    return 1;
                case 6:
                case 24:
                    return combination(selectedNos, 2);
                case 7:
                case 25:
                case 16:
                    return combination(selectedNos, 3);
                case 8:
                case 26:
                    return 0;
                case 9:
                case 27:
                    return 89;
                case 10:
                    return 10;
                case 11:
                    return 1;
                case 15:
                    return combination(selectedNos, 2);
                default:
                    return 0;
            }
        }
    }

    private long ghanaGetNoOfLines(int betCode, int selectedNos) {
        int minNo = 0;
        //old code for ghana
//        if (betCode == 6|| betCode == 7)
//            minNo = 5;
//        if (betCode == 1|| betCode == 9)
//            minNo = 1;
        minNo = getMinNoForGhana(betCode);

        Utils.consolePrint("selected nos " + selectedNos);
        if (selectedNos < minNo) {
            Utils.consolePrint("here after selected Nos");
            return 0;
        } else {
            switch (betCode) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    return 1;
                case 6:
                    return combination(selectedNos, 2);
                case 7:
                    return combination(selectedNos, 3);
                case 9:
                    return 89;
                default:
                    return 0;
            }
        }
    }

    private int getMinNoForLagos(int betCode) {
        int minNo = 0;
        switch (betCode) {
            case 1:
            case 19:
            case 12:
                minNo = 1;
                break;
            case 2:
            case 20:
                minNo = 2;
                break;
            case 3:
            case 21:
                minNo = 3;
                break;
            case 4:
            case 22:
                minNo = 4;
                break;
            case 5:
            case 23:
                minNo = 5;
                break;
            case 6:
            case 24:
                minNo = 3;
                break;
            case 7:
            case 25:
            case 16:
                minNo = 4;
                break;
            case 8:
            case 26:
                minNo = 1;
                break;
            case 9:
            case 27:
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

    private int getMinNoForGhana(int betCode) {
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
            case 9:
                minNo = 1;
                break;
        }
        Utils.consolePrint("min no " + minNo);
        return minNo;
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
            if (value >= selectedBetBean.getMaxBetAmtMulUnitPrice()
                    && value == selectedBetBean.getLowerUnitPrice()) {
                inc.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
                dec.getBackground().setColorFilter(
                        getResources().getColor(R.color.disabled_color),
                        Mode.SRC_IN);
            } else {
                if (value >= selectedBetBean.getMaxBetAmtMulUnitPrice()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.disabled_color),
                            Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);

                } else if (value < selectedBetBean.getMaxBetAmtMulUnitPrice()) {
                    inc.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                    dec.getBackground().setColorFilter(
                            getResources().getColor(R.color.five_color_three),
                            Mode.SRC_IN);
                }

                if (value == selectedBetBean.getLowerUnitPrice()) {
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
            viewEnable(false);
            buyNow.setText(spannableString);
            buttonAdapter.setButtonClickable(false);
            gridView.setFocusable(false);
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    activity, R.anim.btncloseanim);
//            setEditVisible(View.VISIBLE);//old code
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
            // lagos
            if (isLagos)
                buyNow.setText(activity.getResources().getString(R.string.next_grid));
            else
                buyNow.setText(activity.getResources().getString(R.string.ok_draw));
//            setEditVisible(View.INVISIBLE);
            //view.setVisibility(View.VISIBLE);
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
                        slide.setAnimationListener(new AnimationListener() {
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

    private void slideGrid(final View view, boolean isBack) {
        if (isBack) {
            buyNow.setText(getString(R.string.ok_draw));
            Animation animLeftOut = AnimationUtils.loadAnimation(
                    activity, R.anim.anim_slide_out_left);

            animLeftOut.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation animRightIn = AnimationUtils.loadAnimation(
                            activity, R.anim.anim_slide_in_left);
                    view.startAnimation(animRightIn);
                }
            });
            view.startAnimation(animLeftOut);
        } else {
            buyNow.setText(getString(R.string.next_grid));
            Animation animLeftOut = AnimationUtils.loadAnimation(
                    activity, R.anim.anim_slide_out_right);

            animLeftOut.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation animRightIn = AnimationUtils.loadAnimation(
                            activity, R.anim.anim_slide_in_right);
                    view.startAnimation(animRightIn);
                }
            });
            view.startAnimation(animLeftOut);
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
        if (resultCode != Activity.RESULT_OK) {
            if (globalPref.getCountry().equalsIgnoreCase("Ghana"))
                onCancel(true);
            else
                panelDatas = null;
            return;
        }
        if (requestCode == 100) {
            if (totalAmt < selectedBetBean.getMinValue("ghana")) {
                Utils.Toast(activity, "Minimum purchase of " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + " " + AmountFormat.getAmountFormatForMobile(selectedBetBean.getMinValue("ghana")) + " is allowed.");
                return;
            } else {
                if (panelDatas == null)
                    panelDatas = new ArrayList<>();
                panelDatas.clear();
                panelDatas.add(collectData());
                purchaseNow();
            }
        } else if (requestCode == 102) {// for after session out and for ghana too

            if (Double.parseDouble(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL).equals("") ? "0.0" : VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL))
                    >= totalPurchaseAmt) {
                buyNow();
            } else {
                Utils.Toast(activity, getString(R.string.insufficient_balance));
                if (globalPref.getCountry().equalsIgnoreCase("GHANA") && panelDatas.size() > 0)
                    editPanel(panelDatas.size() - 1, false);
                else if ((globalPref.getCountry().equalsIgnoreCase("ZIM") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                    panelDatas = null;
            }
        }
//        if (requestCode == 101) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (panelDatas == null) {
//                    panelDatas = new ArrayList<>();
//                }
//                panelDatas.add(panelPosition, collectData());
//                ++noofpanel;
//                updateBetValiadtion(selectedBetBean);
//                showAlertDialog();
//            }
//        }
    }

    private void purchaseNow() {
        OnClickListener okay = new OnClickListener() {
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
        OnClickListener cancel = new OnClickListener() {
            @Override
            public void onClick(View v) {
                panelDatas = null;
                dBox.dismiss();
            }
        };
        if (dBox != null) {
            dBox.dismiss();
        }
        dBox = new DownloadDialogBox(activity,
                activity.getResources().getString(R.string.purchase_conf), activity.getResources().getString(R.string.conf_five),
                true, true, okay, cancel);
        if (totalAmt > Double.parseDouble(AmountFormat.getCorrectAmountFormat(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL)))) {
            Utils.Toast(
                    activity,
                    activity.getResources().getString(R.string.ins_bal_five)
            );
            panelDatas = null;
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

//    private void buyNow() {
//        ticketSaleDta = new JSONObject();
//        try {
//            ticketSaleDta.put("gameCode", Config.fiveGameName);
//            if (finalDrawDatas.get(0) == drawDatas.get(0) && finalDrawDatas.size() > 1) {
//                ticketSaleDta.put("isAdvancePlay", true);
//            } else if (finalDrawDatas.get(0) != drawDatas.get(0)) {
//                ticketSaleDta.put("isAdvancePlay", true);
//            } else {
//                ticketSaleDta.put("isAdvancePlay", false);
//            }
//            ticketSaleDta.put("merchantCode", VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_MER_CODE));
//            ticketSaleDta.put("noOfDraws", finalDrawDatas.size());
//            ticketSaleDta.put("noOfPanel", 1);
//            ticketSaleDta.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
//            ticketSaleDta.put("userId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.PLAYER_ID));
//            JSONArray drawArr = new JSONArray();
//            for (int i = 0; i < finalDrawDatas.size(); i++) {
//                JSONObject drawObj = new JSONObject();
//                drawObj.put("drawId", finalDrawDatas.get(i).getDrawId());
//                drawArr.put(drawObj);
//            }
//            JSONArray panelArr = new JSONArray();
////            PanelData panelData = new PanelData();
//            for (int i = 0; i < 1; i++) {
//                JSONObject panelObj = new JSONObject();
////                panelData.setBetAmtMul((int) Math.round(unitPriceVal / selectedBetBean.getUnitPrice()));
//                panelObj.put("betAmtMul", (int) Math.round(unitPriceVal / selectedBetBean
//                        .getUnitPrice()));
//                if (favNos.isChecked()) {
//                    noPckd = new StringBuffer();
//                    noPckdArr = selectedBetBean.getFavNos();
//                    for (int j = 0; j < noPckdArr.length; j++) {
//                        if (j == noPckdArr.length - 1)
//                            noPckd.append(noPckdArr[j] + "");
//                        else
//                            noPckd.append(noPckdArr[j] + ",");
//                    }
////                    panelData.setQp(false);
//                    panelObj.put("isQP", false);
////                    panelData.setPickedNumbers(noPckd.toString() + "");
//                    panelObj.put("pickedNumbers", noPckd.toString() + "");
//                    panelObj.put("noPicked", selectedBetBean.getFavNos().length);
//                }
//                if (lastPckd.isChecked()) {
//                    noPckd = new StringBuffer();
//                    noPckdArr = selectedBetBean.getLastPicked();
//                    for (int k = 0; k < noPckdArr.length; k++) {
//                        if (k == noPckdArr.length - 1)
//                            noPckd.append(noPckdArr[k] + "");
//                        else
//                            noPckd.append(noPckdArr[k] + ",");
//                    }
//                    panelObj.put("isQP", false);
//                    panelObj.put("pickedNumbers", noPckd.toString() + "");
//                    panelObj.put("noPicked", selectedBetBean.getLastPicked().length);
//                }
//                if (quickPick.isChecked()) {
//                    panelObj.put("isQP", true);
//                    panelObj.put("pickedNumbers", "QP");
//                    panelObj.put("noPicked", qpNos.getText().toString());
//                }
//                if (selectNos.isChecked()) {
//                    noPckd = new StringBuffer();
//                    noPckdArr = selectedBetBean.getCurrentNos();
//                    for (int l = 0; l < noPckdArr.length; l++) {
//                        if (l == noPckdArr.length - 1)
//                            noPckd.append(noPckdArr[l] + "");
//                        else
//                            noPckd.append(noPckdArr[l] + ",");
//                        //selectedBetBean.getCurrentNos()[0].equals("") ? 0 : selectedBetBean.getCurrentNos().length;
//                        panelObj.put("noPicked", selectedBetBean.getCurrentNos().length);
//                    }
//                    panelObj.put("isQP", false);
//                    panelObj.put("pickedNumbers", noPckd.toString() + "");
//                }
//                panelObj.put("playType", selectedBetBean.getBetDisplayName());
//                panelArr.put(panelObj);
//            }
//
//            ticketSaleDta.put("drawData", drawArr);
//            ticketSaleDta.put("panelData", panelArr);
//            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpPlayMgmt/purchaseTicket";
//            new DGETask(FiveGameScreenLagos.this, "BUY", url, ticketSaleDta.toString()).execute();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    private void buyNow() {
        ticketSaleDta = new JSONObject();
        try {
            ticketSaleDta.put("gameCode", Config.fiveGameNameLagos);
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
//            PanelData panelData = new PanelData();
            for (int i = 0; i < panelDatas.size(); i++) {
                JSONObject panelObj = new JSONObject();
                panelObj.put("betAmtMul", panelDatas.get(i).getBetAmtMul());
                panelObj.put("isQP", panelDatas.get(i).isQp());
                panelObj.put("pickedNumbers", panelDatas.get(i).getPickedNumbers());

                //for lagos
//                if ((globalPref.getCountry().equalsIgnoreCase("lagos")) && (selectedBetBean.getBetCode() == 8) && (panelDatas.get(i).isQp())) {
//                    panelObj.put("noPicked", numberSelectedUL + "," + numberSelectedBL + "");
//                } else {
//                    panelObj.put("noPicked", panelDatas.get(i).getPickedNumbers());
//                }

                //old code
                panelObj.put("noPicked", panelDatas.get(i).getNoPicked());
                panelObj.put("playType", panelDatas.get(i).getPlayTypeName());
                panelArr.put(panelObj);
            }

            ticketSaleDta.put("drawData", drawArr);
            ticketSaleDta.put("panelData", panelArr);
            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpPlayMgmt/purchaseTicket";
            new DGETask(FiveGameScreenLagos.this, "BUY", url, ticketSaleDta.toString()).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PanelData collectData() {
        DecimalFormat formatter = new DecimalFormat("00");
        PanelData panelData = new PanelData();

        if ((selectedBetBean.getBetCode() == 11 || selectedBetBean.getBetCode() == 15 || selectedBetBean.getBetCode() == 12 || selectedBetBean.getBetCode() == 16) && (globalPref.getCountry().equalsIgnoreCase("LAGOS")))
            panelData.setBetAmtMul((int) Math.round((unitPriceVal * MinBetAmountMultipleForDc) / selectedBetBean.getUnitPrice()));
        else
            panelData.setBetAmtMul((int) Math.round((unitPriceVal) / selectedBetBean.getUnitPrice()));

        totalPurchaseAmt += totalAmt;
        panelData.setPanelamount(Double.parseDouble(AmountFormat.getAmountFormatForSingleDecimal(totalAmt)));
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
            Utils.logPrint("numberpicked-" + noPckd.toString());
            if (isLagos) {
                int ul, bl;
                noTwoDecimal(selectedBetBean.getLastPicked(), selectedBetBean, "Last");
                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(selectedBetBean.getLastPicked()));
                ul = arrayList.indexOf(ulText);
                bl = arrayList.indexOf(blText);
                panelData.setNoPicked(formatter.format(ul) + "," + formatter.format(bl - (ul + 1)));
            } else {
                panelData.setNoPicked(selectedBetBean.getLastPicked().length + "");
            }
            Utils.logPrint("numberpicked-" + selectedBetBean.getLastPicked().length + "");
        }
        if (quickPick.isChecked()) {
            panelData.setQp(true);
            panelData.setPickedNumbers("QP");
            if (isLagos) {
                panelData.setNoPicked(formatter.format(Integer.parseInt(qpNosUl.getText().toString())) + "," + formatter.format(Integer.parseInt(qpNosBl.getText().toString())));
            } else {
                panelData.setNoPicked(qpNos.getText().toString());
            }
        }
        if (selectNos.isChecked()) {
            noPckd = new StringBuffer();
            // for lagos
            if (isLagos) {
                noTwoDecimal(selectedBetBean.getUlNos(), selectedBetBean, "UL");
                noTwoDecimal(selectedBetBean.getBlNos(), selectedBetBean, "BL");
//                noPckdArr = selectedBetBean.getCurrentNos();
                int length = selectedBetBean.getUlNos().length + selectedBetBean.getBlNos().length;
                for (int l = 0; l < length; l++) {
                    if (l == selectedBetBean.getUlNos().length - 1)
                        noPckd.append(selectedBetBean.getUlNos()[l]).append(",").append("UL,");
                    else if (l < selectedBetBean.getUlNos().length - 1)
                        noPckd.append(selectedBetBean.getUlNos()[l]).append(",");
                    else if (l == length - 1)
                        noPckd.append(selectedBetBean.getBlNos()[l - selectedBetBean.getUlNos().length]).append(",BL");
                    else
                        noPckd.append(selectedBetBean.getBlNos()[l - selectedBetBean.getUlNos().length]).append(",");
                }
                panelData.setNoPicked(formatter.format(selectedBetBean.getUlNos().length) + "," + formatter.format(selectedBetBean.getBlNos().length));
                panelData.setQp(false);
                panelData.setPickedNumbers(noPckd.toString());
            } else {
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
        }
        panelData.setPlayType(selectedBetBean.getBetDisplayName());
        panelData.setPlayTypeName(selectedBetBean.getBetName());
        return panelData;
    }

    private void noTwoDecimal(String[] lastPicked, BetTypeBean selectedBetBean, String last) {

        String noPick[] = new String[lastPicked.length];
        DecimalFormat formatter = new DecimalFormat("00");

        if (last.equalsIgnoreCase("Last")) {
            for (int i = 0; i < lastPicked.length; i++) {
                if (lastPicked[i].equalsIgnoreCase(ulText)) {
                    noPick[i] = lastPicked[i];
                } else if (lastPicked[i].equalsIgnoreCase(blText)) {
                    noPick[i] = lastPicked[i];
                } else {
                    noPick[i] = formatter.format(Integer.parseInt(lastPicked[i]));
                }
            }
            selectedBetBean.setLastPicked(noPick);
        } else if (last.equalsIgnoreCase("UL")) {
            for (int i = 0; i < lastPicked.length; i++) {
                if (lastPicked[i].equalsIgnoreCase(ulText)) {
                    noPick[i] = lastPicked[i];
                } else if (lastPicked[i].equalsIgnoreCase(blText)) {
                    noPick[i] = lastPicked[i];
                } else {
                    noPick[i] = formatter.format(Integer.parseInt(lastPicked[i]));
                }
            }
            selectedBetBean.setUlNos(noPick);
        } else if (last.equalsIgnoreCase("BL")) {
            for (int i = 0; i < lastPicked.length; i++) {
                if (lastPicked[i].equalsIgnoreCase(ulText)) {
                    noPick[i] = lastPicked[i];
                } else if (lastPicked[i].equalsIgnoreCase(blText)) {
                    noPick[i] = lastPicked[i];
                } else {
                    noPick[i] = formatter.format(Integer.parseInt(lastPicked[i]));
                }
            }
            selectedBetBean.setBlNos(noPick);
        }
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "BUY":
                if (resultData != null) {
                    try {
//                        try {
//                            String fakeJson = "{\"responseCode\":0,\"saleTransId\":\"197\",\"ticketData\":{\"gameCode\":\"KenoFour\",\"ticketNumber\":\"100000123224852\",\"panelData\":[{\"betAmtMul\":5,\"pickedNumbers\":\"04,55,31,UL,51,86,88,3,22,98,BL\",\"numberPicked\":\"3,6\",\"betName\":\"Banker\",\"unitPrice\":0.1,\"panelPrice\":0.5,\"noOfLines\":1,\"isQP\":true}],\"drawData\":[{\"drawId\":\"1089\",\"drawTime\":\"19:00:00\",\"drawDate\":\"18-11-2015\",\"drawName\":\"Midweek\"}],\"purchaseAmt\":1.7,\"gameName\":\"5/90\",\"purchaseTime\":\"18-11-2015 12:11:47\"},\"saleStatus\":\"DONE\",\"availableBal\":18266.4,\"isPromo\":false}";
//                            resultData = new JSONObject(fakeJson);
//                        } catch (Exception ex) {
//                        }
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.SUCCESS);
                            Intent intent = new Intent(activity,
                                    TicketDescActivity.class);
                            intent.putExtra("data", resultData.toString());
                            intent.putExtra("isPurchase", true);
                            startActivity(intent);
                            activity.overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                            activity.finish();
                        } else if (jsonResult.getInt("responseCode") == 110) {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

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
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            UserInfo.setLogout(activity);
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            startActivityForResult(new Intent(activity, LoginActivity.class), 102);
                            activity.overridePendingTransition(GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 3017) {
                            analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);

                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            JSONArray array = jsonResult.getJSONArray("games");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.fiveGameNameLagos)) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("draws");
                                    drawDatas = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject advDrawObj = jsonArray.getJSONObject(j);
                                        String time = advDrawObj.getString("drawDateTime");
                                        if (!gameObject.optString("MinBetAmountMultipleForDc").equalsIgnoreCase(""))
                                            MinBetAmountMultipleForDc = jsonObject.getDouble("MinBetAmountMultipleForDc");
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
                                    darwDialog = new DrawDialog(activity, drawDatas, FiveGameScreenLagos.this, false);
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
                            if (globalPref.getCountry().equalsIgnoreCase("GHANA") && panelDatas.size() > 0)
                                editPanel(panelPosition, false);
                            else if ((globalPref.getCountry().equalsIgnoreCase("ZIM") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                                panelDatas = null;
                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                        dialog.dismiss();
                        if (globalPref.getCountry().equalsIgnoreCase("GHANA") && panelDatas.size() > 0)
                            editPanel(panelPosition, false);
                        else if ((globalPref.getCountry().equalsIgnoreCase("ZIM") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                            panelDatas = null;
                        GlobalVariables.showServerErr(activity);
                    }
                } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                    if (dialog != null)
                        dialog.dismiss();
                    Utils.Toast(activity, "Data not available in offline mode");
                } else {
                    analytics.sendAll(Fields.Category.FIVE_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                    dialog.dismiss();
                    if (globalPref.getCountry().equalsIgnoreCase("GHANA") && panelDatas.size() > 0)
                        editPanel(panelPosition, false);
                    else if ((globalPref.getCountry().equalsIgnoreCase("ZIM") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                        panelDatas = null;
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

    public void showAlertDialog() {
        if (panelDialog != null) {
            isForceDismiss = false;
            panelDialog.dismiss();
        }

//        panelDialog = new PanelDialog(this, finalDrawDatas, totalPurchaseAmt, noofpanel, panelDatas);
//        panelDialog.show();
        panelDialog = new Dialog(activity, R.style.DialogZoomAnim);
        panelDialog.setCancelable(false);
        panelDialog.setOnKeyListener(new DialogKeyListener(panelDialog));
        panelDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        panelDialog.setContentView(R.layout.ticket_preview_dialog);
        if (!finalDrawDatas.get(0).getDrawName().equals("") || finalDrawDatas.get(0).getDrawName() != null) {
            ((TextView) panelDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName());
        } else {
            ((TextView) panelDialog.findViewById(R.id.dialogDrawText)).setText(finalDrawDatas.get(0).getDrawName());
        }
        TextView totalAmtText = (TextView) panelDialog
                .findViewById(R.id.ttlamt_text);
        TextView totalAmt = (TextView) panelDialog.findViewById(R.id.ttlamt);
        ListView dialogList = (ListView) panelDialog
                .findViewById(R.id.dialogTextContent);
        LinearLayout add_panel_lay = (LinearLayout) panelDialog.findViewById(R.id.add_panel_lay);

        totalAmtText.setText("Total Amount" + "("
                + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + ")");
        totalAmt.setText(String.valueOf(AmountFormat
                .getCurrentAmountFormatForMobile(totalPurchaseAmt)));

        DialogListAdapter adapter = new DialogListAdapter(this,
                R.layout.ticket_preview_row, panelDatas,
                totalAmt, add_panel_lay);
        dialogList.setAdapter(adapter);
        Button done = (Button) panelDialog.findViewById(R.id.dialogDone);
        Button add = (Button) panelDialog.findViewById(R.id.add_more);
        Button dialogCancel = (Button) panelDialog
                .findViewById(R.id.dialogCancel);

        //new for lagos
        LinearLayout drawData = (LinearLayout) panelDialog.findViewById(R.id.panel_data);
        LinearLayout totalPanel = (LinearLayout) panelDialog.findViewById(R.id.panel_data_totalAmt);
        drawData.setVisibility(View.GONE);
        totalPanel.setVisibility(View.GONE);

        if (noofpanel == 5) {
            add_panel_lay.setVisibility(View.GONE);
        } else {
            add_panel_lay.setVisibility(View.VISIBLE);
        }

        done.setOnClickListener(listener);
        dialogCancel.setOnClickListener(listener);

        add.setOnClickListener(listener);
        panelDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.widget_progress_dialog_bg);

        panelDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.logPrint("Panel Data On Dismiss Listener");
                onCancel(isForceDismiss);
            }
        });

//        if (screenHeight != 0 && screenWidth != 0) {
//            panelDialog.getWindow().setLayout(screenWidth - 50,
//                    LayoutParams.WRAP_CONTENT);
//        } else {
//            panelDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
//                    LayoutParams.WRAP_CONTENT);
//        }

        panelDialog.show();
    }


    private void onCancel(boolean isForceDismiss) {
        if (!isForceDismiss)
            return;
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

    public void editPanel(int position, boolean editCheck) {
        PanelData panelData = panelDatas.remove(position);
        for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
            betType.getValue().setCurrentNos(new String[]{});
        }
        updatePanelData(panelData, editCheck);
        --noofpanel;
        panelPosition = position;
        totalPurchaseAmt -= panelData.getPanelamount();
        if (panelDialog.isShowing()) {
            isForceDismiss = false;
            panelDialog.dismiss();
        }
    }

    public void setNoofPanel(LinearLayout add_panel_lay) {
        --noofpanel;
        if (noofpanel < 5) {
            add_panel_lay.setVisibility(View.VISIBLE);
        }
        if (noofpanel == 0) {
            isForceDismiss = false;
            panelDialog.dismiss();
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

    public void setNextGrid() {
        isNext = false;
        DataSource.Keno.numbersSelected = selectedBetBean.getUlNos().length;
        slideGrid(gridView, false);
        selectedBetBean.setMinAndMaxNo(1, 4);
        Arrays.fill(DataSource.numbers, 0);
        for (int i = 0; i < selectedBetBean.getUlNos().length; i++) {
            DataSource.numbers[Integer.parseInt(selectedBetBean.getUlNos()[i]) - 1] = 1;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonAdapter.notifyDataSetChanged();
            }
        }, 550);

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

    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {

        @Override
        public void onDebouncedClick(View v) {
//            viewEnable(false);
            switch (v.getId()) {
                case R.id.edit:
                    hide_keyboard(activity);
                    openGrid(gridView, true);
                    break;
                case R.id.buy_now:
                    if (buyNow.getText().toString().equalsIgnoreCase("Next")) {
                        if (DataSource.Keno.numbersSelected >= selectedBetBean.getMinNo()) {
                            isNext = true;
                            ((DGPlayActivity) activity).isNext(isNext, FiveGameScreenLagos.this);
//                            selectedBetBean.setCurrentNos(getPickedNo());
                            selectedBetBean.setUlNos(getPickedNo());
                            DataSource.Keno.numbersSelected = 0;
                            slideGrid(gridView, true);
                            for (int i = 0; i < DataSource.numbers.length; i++) {
                                if (DataSource.numbers[i] == 1) {
                                    DataSource.numbers[i] = -1;
                                } else
                                    DataSource.numbers[i] = 0;
                            }
                            selectedBetBean.setMinAndMaxNo(1, 20);
                            // changes for edit screen
                            if (selectedBetBean.getBlNos().length != 0) {
                                selectedBetBean.setCurrentNos(selectedBetBean.getBlNos());
                                for (int i = 0; i < selectedBetBean.getBlNos().length; i++) {
                                    DataSource.numbers[Integer.parseInt(selectedBetBean.getBlNos()[i]) - 1] = 1;
                                }
                                DataSource.Keno.numbersSelected = selectedBetBean.getBlNos().length;
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buttonAdapter.notifyDataSetChanged();
                                }
                            }, 550);
                        } else {
                            Utils.Toast(
                                    activity,
                                    activity.getResources().getString(R.string.plz_sel_at_lest_five)
                                            + selectedBetBean.getMinNo()
                                            + activity.getResources().getString(R.string.numb_five));
                        }

                    } else if (buyNow.getText().toString().equalsIgnoreCase("OK")) {
                        hide_keyboard(activity);
                        if (DataSource.Keno.numbersSelected >= selectedBetBean
                                .getMinNo()) {
                            selectedBetBean.setCurrentNos(getPickedNo());
                            ((DGPlayActivity) activity).isNext(false, FiveGameScreenLagos.this);
                            openGrid(gridView, false);
                            if (isLagos) {
                                isNext = false;
                                selectedBetBean.setBlNos(getPickedNo());
                                selectedBetBean.setMinAndMaxNo(1, 4);
                                Arrays.fill(DataSource.numbers, 0);
                                DataSource.Keno.numbersSelected = selectedBetBean.getUlNos().length;
                                for (int i = 0; i < selectedBetBean.getUlNos().length; i++) {
                                    DataSource.numbers[Integer.parseInt(selectedBetBean.getUlNos()[i]) - 1] = 1;
                                }
                                selectedBetBean.setCurrentNos(selectedBetBean.getUlNos());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        buttonAdapter.notifyDataSetChanged();
                                    }
                                }, 900);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isLagos) {
                                        updateBallLayPick();
                                    } else {
                                        updateBallLay(getPickedNo());
                                    }
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
                        if (!isCheckUnitPrice(selectedBetBean, unitPrice))
                            return;
                        else
                            hide_keyboard(activity);
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
                            if (totalAmt < selectedBetBean.getMinValue("ghana")) {
//                                ToastMessage.getInstance(activity, getString(R.string.dialog_notice), Toast.LENGTH_LONG);
                                Utils.Toast(activity, "Minimum purchase of "
                                        + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
                                        + " " + AmountFormat.getAmountFormatForMobile(selectedBetBean.getMinValue("ghana")) + " is allowed.");
                            } else {
                                if (panelDatas == null) {
                                    panelDatas = new ArrayList<>();
                                }
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

                                //for single panel new add
                                panelDatas.clear();

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
                    darwDialog = new DrawDialog(activity, drawDatas, FiveGameScreenLagos.this, false);
                    darwDialog.setCancelable(false);
                    darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                    darwDialog.show();
                    break;
                case R.id.fav_nos:
                case R.id.last_picked:
                case R.id.quick_pick:
                case R.id.select_nos:
                    isNext = false;
                    hide_keyboard(activity);
                    updateUI(v);
                    break;
                case R.id.dialogCancel:
//                    onCancel();
                    isForceDismiss = true;
                    panelDialog.dismiss();
                    break;
                case R.id.dialogDone:
                    if (!UserInfo.isLogin(activity)) {
                        isForceDismiss = false;
                        panelDialog.dismiss();
                        startActivityForResult(new Intent(activity,
                                LoginActivity.class), 102);
                        activity.overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    } else {
                        if (Double.parseDouble(VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL).equals("") ? "0.0" : VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_BAL))
                                >= totalPurchaseAmt) {
                            panelPosition = panelDatas.size() - 1;
                            if (panelDatas.size() > 0) {
                                isForceDismiss = false;
                                panelDialog.cancel();
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
                            if ((globalPref.getCountry().equalsIgnoreCase("ZIM") || globalPref.getCountry().equalsIgnoreCase("LAGOS")) && panelDatas.size() > 0)
                                panelDatas = null;
                        }
                    }
                    break;
                case R.id.add_more:
                    for (Map.Entry<String, BetTypeBean> betType : betTypeData.entrySet()) {
                        betType.getValue().setCurrentNos(new String[]{});
                    }
                    selectedBetBean = betTypeData.entrySet().iterator()
                            .next().getValue();
                    betName.setText(selectedBetBean.getBetDisplayName());
                    BetDialog.selectedPos = 0;
                    updateBetValiadtion(selectedBetBean);
                    panelPosition = panelDatas.size();
                    isForceDismiss = false;
                    panelDialog.dismiss();
                    break;
            }
        }
    };


    View.OnFocusChangeListener changeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.unit_price) {
                if (v.hasFocus()) {
                    ((CustomEditText) v).setIsKeyBoardShow(true);
                    ((CustomEditText) v).setCursorVisible(true);
                }
            }
        }
    };

    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                Utils.consolePrint(dest.toString() + source.toString());
//                if (!unitPrice.getText().toString().contains(".")
//                        && unitPrice.getText().toString().length() > (selectedBetBean.getMaxBetAmtMul() + "").length()) {
//                    unitPrice.setText(unitPrice.getText().toString().substring(0, (selectedBetBean.getMaxBetAmtMul() + "").length() - 1));
//                }
                if (!source.toString().equals("")) {
                    if ((dest + source.toString()).equalsIgnoreCase(unitPriceVal + ""))
                        return null;
                    if (dstart < dend)
                        return null;
                    if (!(dest + source.toString()).equals(""))
                        unitPriceVal = Double.parseDouble((dest + source.toString()).equals(".") ? ".0" : (dest + source.toString()));
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    };

    private boolean isCheckUnitPrice(BetTypeBean selectedBetBean, EditText unitPrice) {
        try {
            if (((int) (Double.parseDouble(unitPrice.getText().toString()) * 10) % ((int) (selectedBetBean.getUnitPrice() * 10))) > 0) {
                setUnitPriceLower("Please enter play amount in multiple of " + selectedBetBean.getUnitPrice(), selectedBetBean);
            } else if (Double.parseDouble(unitPrice.getText().toString()) <= 0 || Double.parseDouble(unitPrice.getText().toString()) < selectedBetBean.getLowerUnitPrice()) {
                setUnitPriceLower("Please enter play amount greater than " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
                        + selectedBetBean.getLowerUnitPrice(), selectedBetBean);
            } else if (Double.parseDouble(unitPrice.getText().toString()) > selectedBetBean.getMaxBetAmtMulUnitPrice()) {
                setUnitPriceLower("Please enter play amount less than or equal to " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE)
                        + selectedBetBean.getMaxBetAmtMulUnitPrice(), selectedBetBean);
            } else {
                unitPriceVal = Double.parseDouble(unitPrice.getText().toString());
                colorButtons(true, false, unitPriceVal);
                gameAmtCalculation(selectedBetBean);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setUnitPriceLower("Invalid play amount", selectedBetBean);
        }
        return false;
    }

    OnClickListener focusClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(activity.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("", "");
            clipboard.setPrimaryClip(clipData);
            ((EditText) v).setFocusableInTouchMode(true);
            ((EditText) v).setFocusable(true);
            ((EditText) v).setCursorVisible(true);
            ((CustomEditText) v).setIsKeyBoardShow(true);
        }
    };


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
            if (hide_keyboard(activity, selectedBetBean))
                if (isIncremented) {
                    incDecHandler(selectedBetBean.getUnitPrice(),
                            selectedBetBean.getMaxBetAmtMulUnitPrice(),
                            selectedBetBean.getUnitPrice(), R.id.inc, unitPrice,
                            true);
                    onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                            100);
                } else if (isDecremented) {
                    incDecHandler(selectedBetBean.getLowerUnitPrice(),
                            selectedBetBean.getMaxBetAmtMul(),
                            selectedBetBean.getUnitPrice(), R.id.dec, unitPrice,
                            true);
                    onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                            100);
                }
        }

    }

    DebouncedOnTouchListener touchListener = new DebouncedOnTouchListener(500) {
        @Override
        public void onDebouncedClick(View v, MotionEvent event) {
            if (v.getId() == R.id.advance_menu) {
                hide_keyboard(activity);
                showOne = false;
                darwDialog = new DrawDialog(activity, drawDatas, FiveGameScreenLagos.this, false);
                darwDialog.setCancelable(false);
                darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                darwDialog.show();
            }
        }
    };

    private double setUnitPriceVal(BetTypeBean bean, int minNo) {
        if (getPanelAmt(bean.getBetCode(), minNo, bean.getUnitPrice()) < bean.getMinValue("ghana") && globalPref.getCountry().equalsIgnoreCase("ghana")) {
            DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            format.applyPattern("0.##");
            double value = Double.parseDouble(format.format(bean.getUnitPrice()));
            while (true) {
                value += Double.parseDouble(format.format(bean.getUnitPrice()));
                value = Double.parseDouble(format.format(value));
                if (getPanelAmt(bean.getBetCode(), minNo, value) >= bean.getMinValue("ghana"))
                    break;
            }
            return Double.parseDouble(format.format(value));
        }
        return bean.getUnitPrice();
    }

    private double getPanelAmt(int betCode, int minNo, double unitPrice) {
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("0.##");
        long noOfLines = getNoOfLines(betCode, minNo);
        return Double.parseDouble(format.format(noOfLines * unitPrice));
    }

    private double setMaxUnitPriceVal(BetTypeBean selectedBetBean) {
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            format.applyPattern("0.##");
            double value = Double.parseDouble(format.format(selectedBetBean.getUnitPrice()));
            while (true) {
                value += Double.parseDouble(format.format(selectedBetBean.getUnitPrice()));
                value = Double.parseDouble(format.format(value));
                if (value > selectedBetBean.getMaxBetAmtMul())
                    break;
            }
            value -= Double.parseDouble(format.format(selectedBetBean.getUnitPrice()));
            return Double.parseDouble(format.format(value));
        }
        return selectedBetBean.getMaxBetAmtMul();
    }

    public void setUnitPrice() {
        hide_keyboard(activity, selectedBetBean);
        colorButtons(true, false, unitPriceVal);
    }

    public void gameAmtCalcuation(BetTypeBean bean) {
        setUnitPrice();
        gameAmtCalculation(bean);
    }

    private void setUnitPriceLower(String msg, BetTypeBean selectedBetBean) {
        Utils.Toast(activity, msg);
        unitPriceVal = selectedBetBean.getLowerUnitPrice();
        colorButtons(true, false, unitPriceVal);
        gameAmtCalculation(selectedBetBean);
    }

    public DebouncedOnClickListener getListener() {
        return listener;
    }

//    class MThread extends Thread {
//        Fragment fragment;
//
//        MThread(Fragment fragment) {
//            this.fragment = fragment;
//        }
//
//        @Override
//        public void run() {
//            super.run();
//            parseJson();
//        }
//    }
}
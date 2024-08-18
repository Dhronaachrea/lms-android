package com.skilrock.drawgame.bonusLotto_keno_SixThirtySix;

import android.app.Activity;
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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.skilrock.adapters.ButtonAdapter;
import com.skilrock.adapters.DrawAdapater;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.DrawData;
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
import java.util.LinkedHashMap;

/**
 * Created by stpl on 8/12/2016.
 */
public class BonusKenoGameScreen extends Fragment implements DismissListener, WebServicesListener {

    private Analytics analytics;
    private RelativeLayout playInfoLay, betLayout, nolSn;
    private ImageView edit;
    private LinearLayout unitLay;
    private ImageView qpDec, qpInc, dec, inc;
    public static CustomTextView unitPrice;
    public static TextView finalAmt;
    public static CustomTextView selectedNos;
    public static CustomTextView noOfLines, qpNos;
    private LinearLayout qpNoLines, restNoLines, firstSelectedNosLay, secondSelectedNosLay;
    private LinearLayout lastLay, lastLaySub, buyOptions;
    private CustomTextView buyNow;
    private CustomTextViewDown scrollTextView;
    private ScrollView scrollView;
    private ExpandableGridView gridView;
    private CustomCheckedTextView favNos, selectNos, quickPick, lastPckd;
    private ImageView advanceDrawMenu;
    private CustomTextView changeBetType;
    private CustomTextView restDraws, drawName;
    private CustomTextViewTop drawTime;
    private CustomTextView betName;
    private LinearLayout buy_lay, egrid;
    private CustomTextView selectNosText;
    private BetTypeBean selectedBetBean;
    private JSONObject gameObject;
    private LinkedHashMap<String, DrawData> drawData;
    public static ArrayList<DrawData> finalDrawDatas;
    private LinkedHashMap<String, BetTypeBean> betTypeData;
    private static Context context;
    private SpannableString spannableString;
    private int textSize;
    ;
    private int selectedNosParentHeight, selectedNosParentDefaultHeight = 100,
            noOfBallsInSingleLine = 10, margin = 30;
    public double unitPriceVal;
    public int numberSelected;
    public long noOfLinesVal;
    public double totalAmt;
    private ArrayList<DrawData> drawDatas;
    private DrawDialog darwDialog;
    private BetDialog betDialog;
    private int actionBarHeight;
    private int height;
    private double widthForGridChild;
    private double heightForGridChild;
    private int unitLayheight;
    private int width;
    private boolean isVisible, isFav = true, isLast = true;
    private LinearLayout.LayoutParams params, buyLP;
    private int key;
    private boolean showOne;
    private double min;
    private double max;
    private DownloadDialogBox dBox;
    private String[] numArr;
    private int totalBallWidth, ballWidth, ballHeight;
    private boolean isDrawSecond;
    private int noOfLay;
    private int check;
    private LinearLayout.LayoutParams firstParentParms, lastParentParms;
    private JSONObject ticketSaleDta;
    private StringBuffer noPckd;
    private String[] noPckdArr;
    private JSONObject jsonResult;
    private Activity activity;

    //for long press
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
        analytics.setScreenName(Fields.Screen.BONUS_KENO_GAME);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        DataSource.numbers = new int[36];
        spannableString = new SpannableString(getResources().getString(R.string.play_now_text));
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, 0);
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 4, spannableString.length(), 0);


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


    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_twelve_game_screen, null);
        bindViewIds(view);
        updateTheme();

        edit.setOnClickListener(listener);
        getDisplayDetails();
        parseJson();


        drawDatas = new ArrayList<DrawData>(drawData.values());
        darwDialog = new DrawDialog(activity, drawDatas, this, false);
        if (drawData.size() > 0) {
            DrawData preDrawData = (DrawData) drawData.entrySet().iterator().next().getValue();

            if (!preDrawData.getDrawName().equalsIgnoreCase("N/A")) {
                drawName.setText(preDrawData.getDrawName());
                drawTime.setText(preDrawData.getDrawDateTime());
            } else {
                drawTime.setVisibility(View.GONE);
                drawName.setText(preDrawData.getDrawDateTime());
            }
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
        restDraws.setTextStyle(CustomTextView.TextStyles.BOLD);
        restDraws.setVisibility(View.INVISIBLE);
        betDialog = new BetDialog(activity, new ArrayList<BetTypeBean>(betTypeData.values()), betName);
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


        advanceDrawMenu.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showOne = false;
                darwDialog = new DrawDialog(activity, drawDatas,
                        BonusKenoGameScreen.this, false);
                darwDialog.setCancelable(false);
                darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                darwDialog.show();
                return false;
            }
        });

        betLayout.setOnClickListener(listener);

//        betLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                betDialog.show();
//            }
//        });
        betDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                betName.setText(BetDialog.selectedBet);
                selectedBetBean = betTypeData.get(betName.getText());
                updateBetValiadtion(selectedBetBean);
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


        favNos.setOnClickListener(listener);
        lastPckd.setOnClickListener(listener);
        quickPick.setOnClickListener(listener);
        selectNos.setOnClickListener(listener);
        buyNow.setOnClickListener(listener);
        gridView.setExpanded(true);

        manageTabs();

        return view;
    }

    private void purchaseNow() {

        View.OnClickListener okay = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(activity)) {
                    analytics.sendAction(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.CLICK);
                    dBox.dismiss();
                    buyNow();
                } else {
                    GlobalVariables.showDataAlert(activity);
                }
            }
        };
        View.OnClickListener cancel = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
        } else {
            dBox.show();
        }

    }

    private void buyNow() {
        ticketSaleDta = new JSONObject();
        try {
            ticketSaleDta.put("gameCode", Config.bonusKeno);
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
            for (int i = 0; i < 1; i++) {
                JSONObject panelObj = new JSONObject();
                panelObj.put("betAmtMul", (int) Math.round(unitPriceVal / selectedBetBean
                        .getUnitPrice()));
                if (favNos.isChecked()) {
                    noPckd = new StringBuffer();
                    noPckdArr = selectedBetBean.getFavNos();
                    for (int j = 0; j < noPckdArr.length; j++) {
                        if (j == noPckdArr.length - 1)
                            noPckd.append(noPckdArr[j] + "");
                        else
                            noPckd.append(noPckdArr[j] + ",");
                    }
                    panelObj.put("isQP", false);
                    panelObj.put("pickedNumbers", noPckd.toString() + "");
                    panelObj.put("noPicked", selectedBetBean.getFavNos().length);
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
                    panelObj.put("isQP", false);
                    panelObj.put("pickedNumbers", noPckd.toString() + "");
                    panelObj.put("noPicked", selectedBetBean.getLastPicked().length);
                }
                if (quickPick.isChecked()) {
                    panelObj.put("isQP", true);
                    panelObj.put("pickedNumbers", "QP");
                    panelObj.put("noPicked", qpNos.getText().toString());
                }
                if (selectNos.isChecked()) {
                    noPckd = new StringBuffer();
                    noPckdArr = selectedBetBean.getCurrentNos();
                    for (int l = 0; l < noPckdArr.length; l++) {
                        if (l == noPckdArr.length - 1)
                            noPckd.append(noPckdArr[l] + "");
                        else
                            noPckd.append(noPckdArr[l] + ",");
                        panelObj.put("noPicked", selectedBetBean.getCurrentNos().length);
                    }
                    panelObj.put("isQP", false);
                    panelObj.put("pickedNumbers", noPckd.toString() + "");
                }
                panelObj.put("playType", selectedBetBean.getBetName());
                panelArr.put(panelObj);
            }

            ticketSaleDta.put("drawData", drawArr);
            ticketSaleDta.put("panelData", panelArr);
            String url = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpPlayMgmt/purchaseTicket";
            new DGETask(BonusKenoGameScreen.this, "BUY", url, ticketSaleDta.toString()).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void openGrid(final View view, boolean toOpen) {
        if (!toOpen) {
            viewEnable(false);
            buyNow.setText(spannableString);
            Animation btn_toogleanim = AnimationUtils.loadAnimation(
                    activity, R.anim.btncloseanim);
//            setEditVisible(View.VISIBLE); //old
            btn_toogleanim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)/*if gt or eq then icecream*/ {
                        scrollToTop(scrollView);
                    } else {
                        scrollView.smoothScrollTo(0, 0);//lt 4.0
                    }
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            view.setVisibility(View.GONE);
                            view.clearAnimation();
                            setEditVisible(View.VISIBLE);//new
                            viewEnable(true);
                        }
                    }, 300);
                    // scrollToTop(unitLay);
                }
            });
            view.startAnimation(btn_toogleanim);

        } else {
//            setEditVisible(View.GONE);//old
            viewEnable(false);
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
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    private final void focusOnView() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.clearAnimation();
                scrollView.smoothScrollTo(0, unitLay.getBottom());
                setEditVisible(View.INVISIBLE);//new code
                viewEnable(true);
            }
        });
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
                updateBallLay(max, min);
                break;
            case R.id.qp_dec:
                decrement(min, numberGap, CustomTextView, false);
                updateBallLay(max, min);
                break;
        }
    }

    public void increment(double max, double numberGap,
                          CustomTextView CustomTextView, boolean isUnitPrice) {
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
            }
            gameAmtCalculation(selectedBetBean);
        }
        if (isUnitPrice) {
            colorButtons(isUnitPrice, false, value);
        } else {
            colorButtons(isUnitPrice, true, value);

        }
    }

    public void decrement(double min, double numberGap,
                          CustomTextView CustomTextView, boolean isUnitPrice) {
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

//        if (typeBean.getBetCode() == 1) {
//            selectNosText.setVisibility(View.VISIBLE);
//            buyOptions.setVisibility(View.VISIBLE);
//            nolSn.setVisibility(View.VISIBLE);
//            if (isLast || quickPick.isChecked()) {
//                qpNoLines.setVisibility(View.GONE);
//                restNoLines.setVisibility(View.VISIBLE);
//            } else {
//                qpNoLines.setVisibility(View.VISIBLE);
//                restNoLines.setVisibility(View.GONE);
//            }
//            //exp
//            lastLay.setVisibility(View.VISIBLE);
//        } else if (typeBean.getBetCode() == 10) {
//            selectNosText.setVisibility(View.VISIBLE);
//            buyOptions.setVisibility(View.VISIBLE);
//            nolSn.setVisibility(View.VISIBLE);
//            if (isLast) {
//                qpNoLines.setVisibility(View.GONE);
//                restNoLines.setVisibility(View.VISIBLE);
//            } else {
//                qpNoLines.setVisibility(View.VISIBLE);
//                restNoLines.setVisibility(View.GONE);
//            }
//            //exp
//            lastLay.setVisibility(View.VISIBLE);
//
//        } else if (typeBean.getBetCode() == 11) {
//            selectNosText.setVisibility(View.VISIBLE);
//            buyOptions.setVisibility(View.VISIBLE);
//            nolSn.setVisibility(View.VISIBLE);
//            if (isLast || quickPick.isChecked()) {
//                qpNoLines.setVisibility(View.GONE);
//                restNoLines.setVisibility(View.VISIBLE);
//            } else {
//                qpNoLines.setVisibility(View.VISIBLE);
//                restNoLines.setVisibility(View.GONE);
//            }
//            //exp
//            lastLay.setVisibility(View.VISIBLE);
//        } else {
//            selectNosText.setVisibility(View.GONE);
//            buyOptions.setVisibility(View.GONE);
//            qpNoLines.setVisibility(View.GONE);
//            restNoLines.setVisibility(View.VISIBLE);
//            setEditVisible(View.GONE);
//            getBetCode(typeBean);
//            updateBallLay(numArr);
//        }

        selectNosText.setVisibility(View.VISIBLE);
        buyOptions.setVisibility(View.VISIBLE);
        nolSn.setVisibility(View.VISIBLE);
        if ((isLast || quickPick.isChecked()) && (typeBean.getMaxNo() == typeBean.getMinNo())) {
            qpNoLines.setVisibility(View.GONE);
            restNoLines.setVisibility(View.VISIBLE);
        } else {
            qpNoLines.setVisibility(View.VISIBLE);
            restNoLines.setVisibility(View.GONE);
        }
        manageTabs();
        for (int i = 0; i < DataSource.numbers.length; i++)
            DataSource.numbers[i] = 0;
    }

    private void getBetCode(BetTypeBean betCode) {
        switch (betCode.getBetCode()) {
            case 2:
                numArr = new String[]{"1", "2", "3", "4", "5",
                        "6", "7", "8", "9", "10"};
                break;
            case 3:
                numArr = new String[]{"11", "12", "13", "14", "15",
                        "16", "17", "18", "19", "20"};
                break;
            case 4:
                numArr = new String[]{"21", "22", "23", "24", "25",
                        "26", "27", "28", "29", "30"};
                break;
        }

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
                    } else {
                        drawTime.setVisibility(View.GONE);
                        drawName.setText(data.getDrawDateTime());
                    }
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
        Utils.logPrint("data:" + getResources().getDisplayMetrics().density + " " + height
                + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);

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
                    buyOptions.getChildAt(i).setLayoutParams(params);
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
                params = new LinearLayout.LayoutParams(buyOptions.getLayoutParams());
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

    private View.OnClickListener commonListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            updateUI(view);
        }
    };


    private void updateUI(View view) {
        switch (view.getId()) {
            case R.id.fav_nos:
                analytics.sendAll(Fields.Category.BONUS_KENO_GAME, Fields.Action.CLICK, Fields.Label.FAV_NO);
                setEditVisible(View.INVISIBLE);
                playInfoLay.setVisibility(View.GONE);
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
                analytics.sendAll(Fields.Category.BONUS_KENO_GAME, Fields.Action.CLICK, Fields.Label.LAST_PICK);
                playInfoLay.setVisibility(View.GONE);
                setEditVisible(View.INVISIBLE);
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
                restNoLines.setVisibility(View.VISIBLE);
                buyNow.setText(spannableString);
                numberSelected = selectedBetBean.getLastPicked().length;
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
                analytics.sendAll(Fields.Category.BONUS_KENO_GAME, Fields.Action.CLICK, Fields.Label.QUICK_PICK);
                playInfoLay.setVisibility(View.GONE);
                setEditVisible(View.INVISIBLE);
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
                restNoLines.setVisibility(View.GONE);
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

                gameAmtCalculation(selectedBetBean);

                //new code related to balls
                max = selectedBetBean.getMaxNo();
                min = selectedBetBean.getMinNo();
                updateBallLay(max, min);

                buyNow.setText(spannableString);
                break;
            case R.id.select_nos:
                analytics.sendAll(Fields.Category.BONUS_KENO_GAME, Fields.Action.CLICK, Fields.Label.PICK_NEW);
                if (selectNos.isChecked()) {

                } else {
                    playInfoLay.setVisibility(View.GONE);
                    favNos.setChecked(false);
                    lastPckd.setChecked(false);
                    quickPick.setChecked(false);
                    selectNos.setChecked(true);
                    lastLay.setVisibility(View.VISIBLE);
                    openGrid(gridView, true);
                    qpNoLines.setVisibility(View.GONE);
                    restNoLines.setVisibility(View.VISIBLE);
                    numberSelected = selectedBetBean.getCurrentNos().length;
                    unitPriceVal = selectedBetBean.getUnitPrice();
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
                            gridView.setAdapter(new ButtonAdapter(BonusKenoGameScreen.this,
                                    (int) widthForGridChild / 6, (int) widthForGridChild / 6,
                                    textSize, selectedBetBean, 6));
                            openGrid(gridView, true);

//                            gridView.setAdapter(new ButtonAdapter(activity,
//                                    (int) widthForGridChild / 9, (int) widthForGridChild / 9,
//                                    textSize, selectedBetBean));

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
        for (int i = 0; i < buyOptions.getChildCount(); i++) {
            CustomCheckedTextView checkedTextView = (CustomCheckedTextView) buyOptions
                    .getChildAt(i);
            if (checkedTextView.isChecked()) {
                buyLP.topMargin = 1;
                checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.jublee_mon_direct_select),
                        PorterDuff.Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null,
                        getResources().getDrawable(R.drawable.strip_down1));

                checkedTextView.setTextColor(getResources().getColor(
                        R.color.jublee_mon_direct_select));
            } else {
                checkedTextView.setLayoutParams(buyLP);
                Drawable drawable = checkedTextView.getCompoundDrawables()[1];
                drawable.setColorFilter(
                        getResources().getColor(R.color.four_options_text),
                        PorterDuff.Mode.SRC_IN);
                checkedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        drawable, null, null);
                checkedTextView.setTextColor(getResources().getColor(
                        R.color.four_options_text));
            }
        }

    }

    public void gameAmtCalculation(BetTypeBean bean) {
        int minNo = 0;
        //min numbers
        if (bean.getBetCode() == 1)
            minNo = 1;
        else if (bean.getBetCode() == 2)
            minNo = 2;
        else if (bean.getBetCode() == 3 || bean.getBetCode() == 7)
            minNo = 3;
        else if (bean.getBetCode() == 4 || bean.getBetCode() == 8)
            minNo = 4;
        else if (bean.getBetCode() == 5 || bean.getBetCode() == 9)
            minNo = 5;
        else if (bean.getBetCode() == 6)
            minNo = 6;

        noOfLinesVal = 0;
        totalAmt = 0;
        if (numberSelected < minNo) {
            totalAmt = 0f;
            numberSelected = 0;
        } else {
            noOfLinesVal = getNoOfLines(bean, numberSelected);
        }
        totalAmt = unitPriceVal * noOfLinesVal;
        totalAmt = totalAmt * finalDrawDatas.size();
        noOfLines.setText(noOfLinesVal + "");
        unitPrice.setText(unitPriceVal + "");
        qpNos.setText(numberSelected + "");
        selectedNos.setText(numberSelected + "");
        finalAmt.setText(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(totalAmt) + "");
    }

    long getNoOfLines(BetTypeBean bean, int selectedNos) {
        int minNo = getMinNo(bean);

        if (selectedNos < minNo) {
            return 0;
        } else {
            switch (bean.getBetCode()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return 1;
                case 7:
                    return combination(selectedNos, 2);
                case 8:
                    return combination(selectedNos, 3);
                case 9:
                    return combination(selectedNos, 4);
                default:
                    return 0;
            }
        }
    }

    private int getMinNo(BetTypeBean bean) {
        int minNo = 0;
        if (bean.getBetCode() == 1)
            minNo = 1;
        else if (bean.getBetCode() == 2)
            minNo = 2;
        else if (bean.getBetCode() == 3 || bean.getBetCode() == 7)
            minNo = 3;
        else if (bean.getBetCode() == 4 || bean.getBetCode() == 8)
            minNo = 4;
        else if (bean.getBetCode() == 5 || bean.getBetCode() == 9)
            minNo = 5;
        else if (bean.getBetCode() == 6)
            minNo = 6;
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

    private void updateBallLay(String[] numArr) {
        if (numArr.length >= 1) {
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

    private void updateBallLay(double max, double min) {
        double actualmin = selectedBetBean.getMinNo();
        double actualmax = selectedBetBean.getMaxNo();
        if (actualmax > 0) {
            totalBallWidth = width
                    - (int) (2 * getResources().getDisplayMetrics().density)
                    - (int) ((actualmax - 1) * getResources()
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


//    colorButtons(false, true, numberSelected);
//    colorButtons(true, false, unitPriceVal);

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
//    colorButtons(false, true, numberSelected);
//    colorButtons(true, false, unitPriceVal);
            if ((value == selectedBetBean.getMaxNo()) && (value == selectedBetBean.getMinNo())) {
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


    private void setEditVisible(int visibility) {
        edit.setVisibility(visibility);
    }


    private void parseJson() {
        try {
            loop:
            for (int i = 0; i < GlobalVariables.GamesData.gamesDisplayName.length; i++) {
                switch (GlobalVariables.GamesData.gamenameMap
                        .get(getArguments().getString("gameName"))) {
                    case Config.bonusKeno:
                        gameObject = new JSONObject(
                                GlobalVariables.GamesData.gameDataMap
                                        .get(Config.bonusKeno));
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
                        getResources().getColor(R.color.five_color_three),
                        PorterDuff.Mode.SRC_IN);
            }
            for (int i = 0; i < draws.length(); i++) {
                JSONObject advDrawObj = draws.getJSONObject(i);
                String time = advDrawObj.getString("drawDateTime");
                String drawName = advDrawObj.has("drawName") && !advDrawObj.getString("drawName").equals("null") ? advDrawObj.getString("drawName") : "N/A";
                String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                DrawData data1 = new DrawData(advDrawObj.getString("drawId"), drawName, drawTime, i);
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
                BetTypeBean bean = null;

                switch (betObject.getInt("betCode")) {
                    case 1://Direct1
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 1, 1, new String[]{}, favArr, lotteryPreferences.getDIRECT_1_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 2://Direct2
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 2, 2, new String[]{}, favArr, lotteryPreferences.getDIRECT_2_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 3://Direct3
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 3, new String[]{}, favArr, lotteryPreferences.getDIRECT_3_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 4://Direct4
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 4, new String[]{}, favArr, lotteryPreferences.getDIRECT_4_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 5://Direct5
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 5, new String[]{}, favArr, lotteryPreferences.getDIRECT_5_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 6://Direct6
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 6, 6, new String[]{}, favArr, lotteryPreferences.getDIRECT_6_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 7://perm2
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 3, 10, new String[]{}, favArr, lotteryPreferences.getPERM_2_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 8://perm3
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 4, 10, new String[]{}, favArr, lotteryPreferences.getPERM_3_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                    case 9://perm4
                        bean = new BetTypeBean(betObject.getDouble("unitPrice"), betObject.getInt("maxBetAmtMul"), betObject.optString("betName").equalsIgnoreCase("") ? betObject.optString("betDispName") : betObject.optString("betName"), betObject.getInt("betCode"), 5, 10, new String[]{}, favArr, lotteryPreferences.getPERM_4_BONUS_KENO().split(","));
                        bean.setBetName(betObject.optString("betDispName"));
                        betTypeData.put(bean.getBetDisplayName(), bean);
                        break;
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
    }

    private void bindViewIds(View view) {
        playInfoLay = (RelativeLayout) view.findViewById(R.id.play_info);
        edit = (ImageView) view.findViewById(R.id.edit);
        betLayout = (RelativeLayout) view.findViewById(R.id.bet_lay);
        unitLay = (LinearLayout) view.findViewById(R.id.unit_lay);
        qpDec = (ImageView) view.findViewById(R.id.qp_dec);
        qpInc = (ImageView) view.findViewById(R.id.qp_inc);
        dec = (ImageView) view.findViewById(R.id.dec);
        inc = (ImageView) view.findViewById(R.id.inc);
        unitPrice = (CustomTextView) view.findViewById(R.id.unit_price);
        finalAmt = (TextView) view.findViewById(R.id.final_amt);
        selectedNos = (CustomTextView) view.findViewById(R.id.selected_nos);
        qpNos = (CustomTextView) view.findViewById(R.id.qp_nos);
        noOfLines = (CustomTextView) view.findViewById(R.id.no_of_lines);

        qpNoLines = (LinearLayout) view.findViewById(R.id.qp_no_lines);
        restNoLines = (LinearLayout) view.findViewById(R.id.rest_no_lines);
        firstSelectedNosLay = (LinearLayout) view
                .findViewById(R.id.first_selected_nos);
        secondSelectedNosLay = (LinearLayout) view
                .findViewById(R.id.second_selected_nos);

        lastLaySub = (LinearLayout) view.findViewById(R.id.last_lay_sub);
        buyNow = (CustomTextView) view.findViewById(R.id.buy_now);

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


        //new variable
        selectNosText = (CustomTextView) view.findViewById(R.id.selectNosText);
        buyOptions = (LinearLayout) view.findViewById(R.id.four_opns);
        nolSn = (RelativeLayout) view.findViewById(R.id.nol_sn);
        lastLay = (LinearLayout) view.findViewById(R.id.last_lay);

        selectNosText.setVisibility(View.GONE);
        buyOptions.setVisibility(View.GONE);
        nolSn.setVisibility(View.VISIBLE);

    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "BUY":
                if (resultData != null) {
                    try {
                        jsonResult = new JSONObject(resultData.toString());
                        if (jsonResult.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.SUCCESS);
                            Intent intent = new Intent(activity, TicketDescActivity.class);
                            intent.putExtra("data", resultData.toString());
                            intent.putExtra("isPurchase", true);
                            startActivity(intent);
                            activity.finish();
                            activity.overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 110) {
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
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
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            UserInfo.setLogout(activity);
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            startActivityForResult(new Intent(activity,
                                    LoginActivity.class), 100);
                            activity.overridePendingTransition(GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();
                        } else if (jsonResult.getInt("responseCode") == 3017) {
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                            JSONArray array = jsonResult.getJSONArray("games");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                if (jsonObject.getString("gameCode").equals(Config.bonusKeno)) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("draws");
                                    drawDatas = new ArrayList<>();
                                    finalDrawDatas = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject advDrawObj = jsonArray.getJSONObject(j);
                                        String time = advDrawObj.getString("drawDateTime");
                                        String drawName = advDrawObj.has("drawName") && !advDrawObj.getString("drawName").equals("null") ? advDrawObj.getString("drawName") : "N/A";
                                        String drawTime = time.split(" ")[0] + " " + time.split(" ")[1].split(":")[0] + ":" + time.split(" ")[1].split(":")[1];
                                        DrawData data1 = new DrawData(advDrawObj.getString("drawId"), drawName, drawTime, i);
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
                                                BonusKenoGameScreen.this, false);
                                        darwDialog.setCancelable(false);
                                        darwDialog.setOnKeyListener(new DialogKeyListener(darwDialog));
                                        darwDialog.show();
                                    } else {
                                        drawName.setText(activity.getString(R.string.no_draws_text));
                                        drawTime.setVisibility(View.GONE);
                                    }
                                    MainScreen.parseRefreshedData(array);

                                    dialog.dismiss();
                                    break;
                                }
                            }
                        } else if (jsonResult.getInt("responseCode") == 501 || jsonResult.getInt("responseCode") == 2001 || jsonResult.getInt("responseCode") == 20001) {
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, getString(R.string.sql_exception));
                        } else {
                            analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(activity, jsonResult.getString("responseMsg"));
                        }
                    } catch (JSONException e) {
                        analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                    if (dialog != null)
                        dialog.dismiss();
                    Utils.Toast(activity, "Data not available in offline mode");
                } else {
                    analytics.sendAll(Fields.Category.BONUS_KENO_GAME_PURCHASE, Fields.Action.RESULT, Fields.Label.FAILURE);
                    dialog.dismiss();
                    GlobalVariables.showServerErr(activity);
                }
                break;

        }
    }

    public void hide_keyboard(Activity activity) {
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

    DebouncedOnClickListener listener = new DebouncedOnClickListener(500) {

        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.edit:
                    hide_keyboard(activity);
                    openGrid(gridView, true);
                    break;
                case R.id.buy_now:
                    if (buyNow.getText().toString().equalsIgnoreCase("OK")) {
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
                    break;
                case R.id.bet_lay:
                    hide_keyboard(activity);
                    betDialog.show();
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


    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);
    }

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

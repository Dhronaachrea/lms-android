package com.skilrock.myaccount;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.bean.TicketDataBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.FastPanelPrepForResult;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.skilrock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TicketDescActivity extends DrawerBaseActivity {
    private Analytics analytics;

    private LinearLayout ticketsParent;
    private View tcktDescChild;
    private JSONObject jsonObject;
    private TicketDataBean bean;
    private ArrayList<TicketDataBean> ticketBeans;
    private LinearLayout firstSelectedNosLay;

    private LinearLayout ticketPanels, ticketPanel, fiveByNineResult;
    private LinearLayout secondSelectedNosLay;
    private LinearLayout lastLay;
    private LinearLayout bonusPanelLay;
    private LinearLayout lastLaySub;
    private int selectedNosParentHeight, selectedNosParentDefaultHeight = 100, noOfBallsInSingleLine = 10;
    private int totalBallWidth, ballWidth, ballHeight;
    private String[] numArr;
    private boolean isDrawSecond, isFastGame;
    private int noOfLay;
    private int check;
    private LayoutParams firstParentParms, lastParentParms;
    private DisplayMetrics displaymetrics;
    private String gameDevName;
    private RelativeLayout betDetailsData;
    private LotteryPreferences mLotteryPreferences;
    private String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private DecimalFormat format = new DecimalFormat("0.##");
    private int width, bonusBallWidth;
    private boolean isDSix;
    private long duration = 900;
    private boolean isTwelveGame;
    private int count = 0;
    private Boolean anim = false;
    private CustomTextView payAmt, selectedNumberText;
    private boolean isFirst = true;
    private int lengOfChildView;

    //for lagos
    private String ulText = "UL";
    private String blText = "BL";
    private boolean isLagos = false;

    //Multiple
    //new
    private LinearLayout lastLayMul;
    private LinearLayout firstSelectedNosLayMul;
    private LinearLayout secondSelectedNosLayMul;
    private RelativeLayout betDetailsDataMul;
    private LinearLayout fivebynineResultMul;


    private String jsonObjectPrint;
    private GlobalPref globalPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tckt_desc_lay);
        globalPref = GlobalPref.getInstance(this);

        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.DRAW_GAME_TICKET);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);


        if (getIntent().hasExtra("betCode") && getIntent().getIntExtra("betCode", 0) == 1 || getIntent().getIntExtra("betCode", 0) == 2) {
            isDSix = true;
        } else {
            isDSix = false;
        }
        mLotteryPreferences = new LotteryPreferences(this);
        if (GlobalVariables.onTablet(getApplicationContext())) {
            selectedNosParentDefaultHeight = 120;
            noOfBallsInSingleLine = 10;
        } else {
            switch (GlobalVariables.getDensity(TicketDescActivity.this)) {
                case DisplayMetrics.DENSITY_LOW:
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    noOfBallsInSingleLine = 6;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    noOfBallsInSingleLine = 10;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    noOfBallsInSingleLine = 10;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    noOfBallsInSingleLine = 10;
                    break;
            }
        }
        sHeader();
        setDrawerItems();
        bindViewIds();
        getDisplayDetails();
        parseJson();


        if (globalPref.getCountry().equalsIgnoreCase("Lagos") && ((bean.getPanelData().get(0).getBetDispName().equalsIgnoreCase("banker")) || (bean.getPanelData().get(0).getBetDispName().equalsIgnoreCase("MN-Banker")))) {
            isLagos = true;
        } else {
            isLagos = false;
        }

        for (int i = 0; i < ticketBeans.size(); i++) {
            ticketsParent.addView(addChilds(i), i);
            duration = duration + (duration * i);
        }
        ticketsParent.setVisibility(View.VISIBLE);
//        Animation bottomDown = AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.bottom_down_ticket);
        ticketsParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    isFirst = !isFirst;
                    float height = ticketsParent.getHeight(); //height is ready
                    TranslateAnimation bottomDown = new TranslateAnimation(0.0f, 0.0f, -height, 0);
                    bottomDown.setDuration(duration);
                    ticketsParent.setAnimation(bottomDown);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                        ticketsParent.animate();
                    }
                    bottomDown.start();
                }
            }
        });
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToHomeScreen();
            }
        });
        headerText.setText(getResources().getString(R.string.my_tckts));
        headerSubText.setText("PURCHASED TICKET");


        if (appInstalledOrNot("com.skilrock.printtest")) {
            Intent intent = new Intent();
            if (gameDevName.equalsIgnoreCase(Config.thaiGameName)) {
                intent.putExtra("printType", "thai");
            }
            intent.putExtra("printer", jsonObjectPrint);
            intent.setComponent(new ComponentName("com.skilrock.printtest", "com.skilrock.printtest.MainActivity"));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        globalPref = GlobalPref.getInstance(this);
        manageHeader();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void bindViewIds() {
        ticketsParent = (LinearLayout) findViewById(R.id.tickets_parent);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    private void parseJson() {
        try {
            jsonObjectPrint = getIntent().getStringExtra("data");
            ticketBeans = new ArrayList<TicketDataBean>();
            jsonObject = new JSONObject(getIntent().getStringExtra("data"));

//            if (!gameDevName.equals(Config.thaiGameName))
            VariableStorage.UserPref.setStringPreferences(getApplicationContext(), VariableStorage.UserPref.USER_BAL, AmountFormat.getAmountFormatForTwoDecimal(jsonObject.getString("availableBal")));
            //  jsonObject = new JSONObject("{\"responseCode\":0,\"saleTransId\":\"207\",\"responseMsg\":\"\",\"ticketData\":{\"ticketNumber\":\"100150111970164\",\"gameCode\":\"KenoTwo\",\"purchaseAmt\":1,\"drawData\":[{\"drawId\":\"49483\",\"drawTime\":\"10:15:00\",\"drawName\":\"Mhanza\",\"drawDate\":\"2015-07-16\"}],\"panelData\":[{\"betAmtMul\":1,\"numberPicked\":10,\"pickedNumbers\":\"4,13,22,31,49,58,67,76,85,88,\",\"betName\":\"Perm1\",\"unitPrice\":0.1,\"panelPrice\":1,\"noOfLines\":10,\"isQP\":false}],\"gameName\":\"Lucky Numbers\",\"purchaseTime\":\"2015-07-16 10:10:50\"},\"availableBal\":1221715.7,\"isPromo\":false}");

            if (jsonObject != null) {
                bean = new Gson().fromJson(jsonObject.getJSONObject("ticketData").toString(),
                        TicketDataBean.class);
                ticketBeans.add(bean);
                if (jsonObject.getBoolean("isPromo")) {
                    JSONArray array = jsonObject.getJSONArray("promoTicketData");
                    for (int i = 0; i < array.length(); i++) {
                        TicketDataBean bean1 = new Gson().fromJson(array
                                        .getJSONObject(i).toString(),
                                TicketDataBean.class);
                        ticketBeans.add(bean1);
                    }
                }
                ticketsParent.removeAllViews();
            }
            gameDevName = jsonObject.getJSONObject("ticketData").getString("gameCode");

            //new code for lagos
            isFastGame = gameDevName.equalsIgnoreCase(Config.oneToTwelve) || gameDevName.equalsIgnoreCase(Config.fastGameName);

//            if (globalPref.getCountry().equalsIgnoreCase("lagos")) {
//                isFastGame = gameDevName.equals(Config.oneToTwelve);
//            } else {
//                isFastGame = gameDevName.equals(Config.fastGameName);
//            }

            //old
//            isFastGame = gameDevName.equalsIgnoreCase(Config.fastGameName);


            isTwelveGame = gameDevName.equals(Config.twelveGameName);
            if (isFastGame) {
                selectedNosParentDefaultHeight = 120;
            } else {
                selectedNosParentDefaultHeight = 62;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utils.Toast(TicketDescActivity.this, "Some internal error!");
            TicketDescActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.Toast(TicketDescActivity.this, "Some internal error!");
            TicketDescActivity.this.finish();
        }
    }

    private View addChilds(int pos) {

        try {
            bean = ticketBeans.get(pos);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tcktDescChild = inflater.inflate(R.layout.tckt_desc_child, null);

            // for Payable text for y
            CustomTextViewTop payableText = ((CustomTextViewTop) tcktDescChild.findViewById(R.id.payable_text));
//            ((CustomTextViewDown) tcktDescChild.findViewById(R.id.payable_text)).setDownSpacing(6);

            TicketDataBean beanFull = ticketBeans.get(pos);
            TicketDataBean.PanelData betTypeData = beanFull.getPanelData().get(0);
            numArr = betTypeData.getPickedNumbers().split(",");
            //MN-Banker1AgainstAll
            if (betTypeData.getPickedNumbers().contains(",")
                    || betTypeData.getBetDispName().equalsIgnoreCase("Direct1") || betTypeData.getBetDispName().equalsIgnoreCase("MN-Direct1")
                    || betTypeData.getBetDispName().equalsIgnoreCase("Banker1AgainstAll") || betTypeData.getBetDispName().equalsIgnoreCase("MN-Banker1AgainstAll"))
                setLastPickedtoPreferences(betTypeData.getBetDispName(), betTypeData.getPickedNumbers(), gameDevName);
            if (numArr.length > noOfBallsInSingleLine) {
                totalBallWidth = displaymetrics.widthPixels - (int) (2 * getResources().getDisplayMetrics().density) - (int) ((noOfBallsInSingleLine) * getResources().getDisplayMetrics().density) - GlobalVariables.getPx(14, getApplicationContext());
            } else {
                totalBallWidth = displaymetrics.widthPixels - (int) (2 * getResources().getDisplayMetrics().density) - (int) ((numArr.length - 1) * getResources().getDisplayMetrics().density) - GlobalVariables.getPx(14, getApplicationContext());
            }

            if (isFastGame) {
                analytics.sendAll(Fields.Category.FAST_GAME, Fields.Action.OPEN, Fields.Label.TICKET);
                setLastPickedtoPreferences(gameDevName, new FastPanelPrepForResult(beanFull.getPanelData()).getFastLottoPanel(), gameDevName);
            }
            ArrayList<TicketDataBean.DrawData> drawDatas = beanFull.getDrawData();
            CustomTextView ticketNo, gameName, ticketDate, betName, noOfDraws, totalAmt, free;
            RobotoTextView noOfLine, unitPrice, panelPrice;
            // LinearLayout drawParent;

            ticketPanels = (LinearLayout) tcktDescChild.findViewById(R.id.ticketPanels);
            ticketNo = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_no);
            ticketDate = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_date);
            totalAmt = (CustomTextView) tcktDescChild.findViewById(R.id.tot_amt);
            noOfDraws = (CustomTextView) tcktDescChild.findViewById(R.id.no_of_draws);
            gameName = (CustomTextView) tcktDescChild.findViewById(R.id.gameName);
            free = (CustomTextView) tcktDescChild.findViewById(R.id.free);
            payAmt = (CustomTextView) tcktDescChild.findViewById(R.id.pay_amt);
            gameName.setText(beanFull.getGameName().toUpperCase(Locale.ENGLISH));
            if (beanFull.getGameName().contains("Free") || beanFull.getGameName().contains("FREE")) {
                free.setVisibility(View.VISIBLE);
            } else {
                free.setVisibility(View.GONE);
            }

            ticketPanels.removeAllViews();
            //new code

//            if (beanFull.getPanelData().size() > 1 && !isFastGame) {
//                lengOfChildView = beanFull.getPanelData().size();
//            } else {
            lengOfChildView = drawDatas.size();
//            }

            for (int i = 0; i < lengOfChildView; i++) {
                TicketDataBean.DrawData drawData;
                //new code
                if (drawDatas.size() > 1) {
                    drawData = drawDatas.get(i);
                } else {
                    drawData = drawDatas.get(0);
                }

                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View drawChild = inflater1.inflate(R.layout.ticket_single_bet, null);
                CustomTextView drawName, drawDate/*, drawStatus*/;
                LinearLayout drawNameLayout;
                if (globalPref.getCountry().equalsIgnoreCase("Ghana"))
                    ((TextView) drawChild.findViewById(R.id.bet_amount_text)).setText("Play Amount");
                drawNameLayout = (LinearLayout) drawChild.findViewById(R.id.drawNameLayout);
                drawName = (CustomTextView) drawChild.findViewById(R.id.draw_name);
                drawDate = (CustomTextView) drawChild.findViewById(R.id.draw_date);
//            drawStatus = (CustomTextView) drawChild.findViewById(R.id.draw_status);
                betName = (CustomTextView) drawChild.findViewById(R.id.bet_name);

                ticketPanel = (LinearLayout) drawChild.findViewById(R.id.draw_parent);
                fiveByNineResult = (LinearLayout) drawChild.findViewById(R.id.fivebynineResult);


                //new code
                if (drawDatas.size() > 1) {
                    drawNameLayout.setVisibility(View.VISIBLE);
                } else {
                    if (i == 0) {
                        drawNameLayout.setVisibility(View.VISIBLE);
                    } else {
                        drawNameLayout.setVisibility(View.GONE);
                    }
                }


                if (!isFastGame) {
                    firstSelectedNosLay = (LinearLayout) drawChild.findViewById(R.id.first_selected_nos);
                    secondSelectedNosLay = (LinearLayout) drawChild.findViewById(R.id.second_selected_nos);
                    lastLay = (LinearLayout) drawChild.findViewById(R.id.last_lay);
                    selectedNumberText = (CustomTextView) drawChild.findViewById(R.id.selected_number_text);
                    bonusPanelLay = (LinearLayout) drawChild.findViewById(R.id.bonus_panel_lay);
                    if ((gameDevName.equals(Config.bonusGameName) || gameDevName.equals(Config.bonusGameNameTwo) || gameDevName.equals(Config.bonusFree)) && isDSix) {
                        lastLay.setVisibility(View.GONE);
                        drawName.setVisibility(View.GONE);
                        bonusPanelLay.setVisibility(View.VISIBLE);
                        if (!betTypeData.getBetDispName().equalsIgnoreCase("perm6")) {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.OPEN, Fields.Label.TICKET);
                            updateBonus(betTypeData.getPickedNumbers());
                        } else {
                            analytics.sendAll(Fields.Category.BONUS_GAME, Fields.Action.OPEN, Fields.Label.TICKET);
                            if (pos == 0)
                                updateBonusFreePerm6(beanFull.getPanelData());
                            else
                                updateBonusFreePerm6(beanFull.getPanelData());
                        }
                    } else if (gameDevName.equals(Config.twelveGameName)) {
                        analytics.sendAll(Fields.Category.TWELVE_GAME, Fields.Action.OPEN, Fields.Label.TICKET);
                        lastLay.setVisibility(View.VISIBLE);
                        bonusPanelLay.setVisibility(View.GONE);
                        lastLaySub = (LinearLayout) drawChild.findViewById(R.id.last_lay_sub);
                        updateBallLay(numArr, isFastGame);
                    } else {
                        analytics.sendAll(Fields.Category.FIVE_GAME, Fields.Action.OPEN, Fields.Label.TICKET);
                        lastLay.setVisibility(View.VISIBLE);
                        bonusPanelLay.setVisibility(View.GONE);
                        lastLaySub = (LinearLayout) drawChild.findViewById(R.id.last_lay_sub);

                        if (beanFull.getPanelData().size() > 1) {
                            betName.setVisibility(View.GONE);
                            selectedNumberText.setVisibility(View.GONE);
                            fiveByNineResult.setVisibility(View.GONE);
                            ticketPanel.setVisibility(View.VISIBLE);
                            updateMulBallLay(beanFull.getPanelData(), beanFull, pos, drawDatas);
                        } else {
                            betName.setVisibility(View.VISIBLE);
                            selectedNumberText.setVisibility(View.VISIBLE);
                            fiveByNineResult.setVisibility(View.VISIBLE);
                            ticketPanel.setVisibility(View.GONE);
                            updateBallLay(numArr, isFastGame);
                        }

                        //new code
//                        if (beanFull.getPanelData().size() > 1) {
//                            numArr = beanFull.getPanelData().get(i).getPickedNumbers().split(",");
//                            setLastPickedtoPreferences(beanFull.getPanelData().get(i).getBetName(),
//                                    beanFull.getPanelData().get(i).getPickedNumbers());
//                            updateBallLay(numArr, isFastGame);
//                        } else {
//                            updateBallLay(numArr, isFastGame);
//                        }
                    }
                    gameName = (CustomTextView) drawChild.findViewById(R.id.game_name);
                    betDetailsData = (RelativeLayout) drawChild.findViewById(R.id.bet_details_data);
                    if (isFastGame) {
                        betDetailsData.setVisibility(View.GONE);
                    } else {
                        betDetailsData.setVisibility(View.VISIBLE);
                    }
                    noOfLine = (RobotoTextView) drawChild.findViewById(R.id.no_of_lines);
                    unitPrice = (RobotoTextView) drawChild.findViewById(R.id.unit_price);
                    panelPrice = (RobotoTextView) drawChild.findViewById(R.id.panel_price);
                    //                noOfLine.setText(betTypeData.getNoOfLines() + "");


//                    unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getUnitPrice())) * betTypeData.getBetAmtMul()));

                    if (beanFull.getPanelData().size() > 1) {
//                        ghanaBetNameUpdataFiveGame(betName, beanFull, i);
//                        unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getPanelData().get(i).getUnitPrice() * beanFull.getPanelData().get(i).getBetAmtMul()))));
//                        noOfLine.setText(beanFull.getPanelData().get(i).getNoOfLines() + "");
//                        ghanaPanelPriceSet(pos, panelPrice, beanFull, i);
                    } else {
                        zimBetNameUpdataFiveGame(betName, betTypeData);
                        unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getUnitPrice())) * betTypeData.getBetAmtMul()));
                        noOfLine.setText(getNoOfLines(beanFull.getPanelData()) + "");
                        zimPanelPriceSet(pos, panelPrice, betTypeData, beanFull, drawDatas);
                    }

//                    if (pos != 0)
//                        panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getPanelPrice(beanFull.getPanelData()));
//                    else
//                        panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getPanelPrice() / drawDatas.size()))));


                    // changes in mode
                    if (panelPrice.getText().toString().length() > 12) {
                        panelPrice.startAnimation(AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.text_marquee));
                    }
                } else {
                    drawName.setVisibility(View.GONE);
                    ticketPanel.setVisibility(View.VISIBLE);
                    fiveByNineResult.setVisibility(View.GONE);
                    updateFastBallLay(beanFull.getPanelData(), drawDatas.size());
                }

                String[] date = drawData.getDrawDate().split("-");
                String yeardraw = "", monthdraw = "", time = "", datedraw = " ";

                if (date.length > 2) {
                    yeardraw = date[2];
                    monthdraw = date[1];
                    datedraw = date[0];
                    time = drawData.getDrawTime().substring(0, 5);
                }
//            drawDate.setText(datedraw + " " + MONTH[Integer.parseInt(monthdraw) - 1] + ", " + yeardraw + " " + time);
//            drawName.setText(drawData.getDrawName());

                if (drawData.getDrawName() == null || drawData.getDrawName().equals("") || drawData.getDrawName().trim().equalsIgnoreCase("null")) {
                /*drawName.setVisibility(View.GONE);
                drawDate.setVisibility(View.VISIBLE);*/

                    drawName.setVisibility(View.VISIBLE);
                    drawDate.setVisibility(View.GONE);
                    drawName.setText(drawData.getDrawDate() + " " + time);

                } else {
                    drawName.setVisibility(View.VISIBLE);
                    drawDate.setVisibility(View.VISIBLE);

                    if (gameDevName.equals(Config.thaiGameName))
                        drawDate.setVisibility(View.GONE);

                    drawName.setText(drawData.getDrawName());
                    drawDate.setText(datedraw + "-" + monthdraw + "-" + yeardraw + " " + time);

                }

                //old code
                if (isFastGame) {
                    if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                        if (betTypeData.isQP())
                            betName.setText(betTypeData.getBetName() + "(QP)");
                        else
                            betName.setText(betTypeData.getBetName());
                    } else
                        betName.setText(betTypeData.getBetName());
                }

                ticketPanels.addView(drawChild, i);
            }

            ticketNo.setText(beanFull.getTicketNumber());


            String[] dateTimeTKT = beanFull.getPurchaseTime().split(" ");
            String date = "", month = "", year = "";
            if (dateTimeTKT.length > 1) {
                year = dateTimeTKT[0].split("-")[2];
                month = dateTimeTKT[0].split("-")[1];
                date = dateTimeTKT[0].split("-")[0];
            }
            ticketDate.setText(date + "-" + month + "-" + year + " " + dateTimeTKT[1]);
            noOfDraws.setText(drawDatas.size() + "");

            payAmt.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getPurchaseAmt()))));

            if (payAmt.getText().toString().length() > 11) {
                payAmt.startAnimation(AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.text_marquee));
            }


            int perPanelSize = bean.getPanelData().size();
            Double totalCal = 0.00;
            for (int i = 0; i < perPanelSize; i++) {
                totalCal = totalCal + (bean.getPanelData().get(i).getPanelPrice() / drawDatas.size());
            }

            totalCal = totalCal * drawDatas.size();
            if (!gameDevName.equals(Config.thaiGameName))
                totalAmt.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(totalCal))));
            else
                totalAmt.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(totalCal / 2))));
            if (totalAmt.getText().toString().length() > 12) {
                totalAmt.startAnimation(AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.text_marquee));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.Toast(TicketDescActivity.this, "Some internal error!");
            TicketDescActivity.this.finish();
        }
        return tcktDescChild;

    }

    private void ghanaBetNameUpdataFiveGame(CustomTextView betName, TicketDataBean betTypeData, int i) {
        if (betTypeData.getPanelData().get(i).isQP()) {
            if (gameDevName.equals(Config.thaiGameName))
                betName.setText("Serial No:" + betTypeData.getPanelData().get(i).getBetName() + "(QP)");
            else
                betName.setText(betTypeData.getPanelData().get(i).getBetName() + "(QP)");
        } else {
            if (gameDevName.equals(Config.thaiGameName))
                betName.setText("Serial No:" + betTypeData.getPanelData().get(i).getBetName());
            else
                betName.setText(betTypeData.getPanelData().get(i).getBetName());
        }
    }

    private void zimBetNameUpdataFiveGame(CustomTextView betName, TicketDataBean.PanelData betTypeData) {
        if (betTypeData.isQP()) {
            betName.setText(betTypeData.getBetName() + "(QP)");
        } else {
            betName.setText(betTypeData.getBetName());
        }
    }

    private void zimPanelPriceSet(int pos, TextView panelPrice, TicketDataBean.PanelData betTypeData, TicketDataBean beanFull, ArrayList<TicketDataBean.DrawData> drawDatas) {
        if (pos != 0)
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getPanelPrice(beanFull.getPanelData()));
        else
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getPanelPrice() / drawDatas.size()))));

    }

    private void ghanaPanelPriceSet(int pos, TextView panelPrice, TicketDataBean beanFull, int i, ArrayList<TicketDataBean.DrawData> drawDatas) {
        if (pos != 0)
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(beanFull.getPanelData().get(i).getPanelPrice()));
        else {
            double panelprice = beanFull.getPanelData().get(i).getPanelPrice() / drawDatas.size();
//            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(beanFull.getPanelData().get(i).getPanelPrice()));
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(panelprice));
        }
    }

    private void updateBonus(String pickedNumbers) {
        String[] nos = pickedNumbers.split("Nxt");
        bonusBallWidth = width - GlobalVariables.getPx(28, getApplicationContext());
        bonusBallWidth = bonusBallWidth / 7 - GlobalVariables.getPx(5, getApplicationContext());
        bonusPanelLay.removeAllViews();
        for (int i = 0; i < nos.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.bonus_ball_lay, null);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.first_selected_nos);
            linearLayout.removeAllViews();
            String[] balls = nos[i].split(",");
            LayoutParams layoutParams = new LayoutParams(bonusBallWidth, bonusBallWidth);
            layoutParams.setMargins(1, 3, 1, 1);
            CustomTextView countTextView = new CustomTextView(getApplicationContext());
            countTextView.setGravity(Gravity.CENTER);
            countTextView.setLayoutParams(layoutParams);
            countTextView.setBackgroundColor(Color.TRANSPARENT);
            countTextView.setTextColor(getResources().getColor(R.color.txn_cal_month));
            countTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bonusBallWidth / 3);
            countTextView.setText("#" + (i + 1));
            linearLayout.addView(countTextView);

            for (int j = 0; j < balls.length; j++) {
                CustomTextView customTextView = new CustomTextView(getApplicationContext());
                customTextView.setGravity(Gravity.CENTER);
                customTextView.setLayoutParams(layoutParams);
                customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bonusBallWidth / 2);
                customTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                customTextView.setTextColor(getResources().getColor(R.color.white));
                customTextView.setText(balls[j]);
                linearLayout.addView(customTextView);
            }
            bonusPanelLay.addView(view, i);
        }
    }

    private void updateBonusFreePerm6(List<TicketDataBean.PanelData> pickedNumbers) {
        bonusPanelLay.removeAllViews();
        for (int n = 0; n < pickedNumbers.size(); n++) {
            String[] nos = pickedNumbers.get(n).getPickedNumbers().split(",");
            int frac = 8, add = 8;
            ArrayList<String> strList = new ArrayList<>();
            String temp = "";
            for (int k = 0; k < nos.length; k++) {
                if (k < frac) {
                    if (k == frac - 1) {
                        temp = temp.concat(nos[k]);
                    } else {
                        temp = temp.concat(nos[k] + ",");
                    }
                    if (nos.length <= frac && k == nos.length - 1) {
                        strList.add(temp);
                    }
                } else {
                    strList.add(temp);
                    temp = "";
                    temp = temp.concat(nos[k] + ",");
                    frac = frac + add;
                    if (nos.length < frac && k == nos.length - 1) {
                        strList.add(temp);
                    }
                }
            }

            for (int i = 0; i < strList.size(); i++) {
                bonusPanelLay.addView(getRowView(strList.get(i).split(",")));
            }
            if (n != pickedNumbers.size() - 1) {
                View viewLine = new View(TicketDescActivity.this);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 15, 0, 10);
                viewLine.setLayoutParams(params);
                viewLine.setBackgroundColor(getResources().getColor(R.color.grey));
                CustomTextView betName = new CustomTextView(TicketDescActivity.this);
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 15);
                betName.setGravity(Gravity.CENTER);
                betName.setTextAppearance(TicketDescActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                betName.setTextColor(getResources().getColor(R.color.txn_cal_month));
                betName.setLayoutParams(params);
                betName.setTextStyle(CustomTextView.TextStyles.BOLD);
                if (pickedNumbers.get(n).isQP()) {
                    betName.setText(pickedNumbers.get(n).getBetName() + "(QP)");
                } else {
                    betName.setText(pickedNumbers.get(n).getBetName());
                }
                bonusPanelLay.addView(viewLine);
                bonusPanelLay.addView(betName);
            }
        }
    }


//    private void updateBonusFreePerm6(String pickedNumbers) {
//        String[] nos = pickedNumbers.split(",");
//        int frac = 8, add=8;
//        ArrayList<String> strList = new ArrayList<>();
//        String temp = "";
//        for (int k = 0; k < nos.length; k++) {
//            if (k < frac) {
//                if (k == frac - 1) {
//                    temp = temp.concat(nos[k]);
//                } else {
//                    temp = temp.concat(nos[k] + ",");
//                }
//                if (nos.length <= frac && k == nos.length - 1) {
//                    strList.add(temp);
//                }
//            } else {
//                strList.add(temp);
//                temp = "";
//                temp = temp.concat(nos[k] + ",");
//                frac = frac +add;
//                if (nos.length < frac && k == nos.length - 1) {
//                    strList.add(temp);
//                }
//            }
//        }
//        bonusPanelLay.removeAllViews();
//        for (int i = 0; i < strList.size(); i++) {
//            bonusPanelLay.addView(getRowView(strList.get(i).split(",")), i);
//        }
//    }

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

    private void updateVisiblity(boolean visible) {
        if (visible) {


        } else {

        }

    }

    private void updateMulBallLay(List<TicketDataBean.PanelData> panelDatas, TicketDataBean beanFull, int pos, ArrayList<TicketDataBean.DrawData> drawDatas) {
        ticketPanel.removeAllViews();


        for (int n = 0; n < panelDatas.size(); n++) {
            //for new View
            RobotoTextView noOfLineMul, unitPriceMul, panelPriceMul;
            CustomTextView betNameMul;
            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View drawChildMul = inflater1.inflate(R.layout.multiple_panel, null);

            fivebynineResultMul = (LinearLayout) drawChildMul.findViewById(R.id.fivebynineResult_mul);
            firstSelectedNosLayMul = (LinearLayout) drawChildMul.findViewById(R.id.first_selected_nos_mul);
            secondSelectedNosLayMul = (LinearLayout) drawChildMul.findViewById(R.id.second_selected_nos_mul);
            lastLayMul = (LinearLayout) drawChildMul.findViewById(R.id.last_lay_mul);

            noOfLineMul = (RobotoTextView) drawChildMul.findViewById(R.id.no_of_lines);
            unitPriceMul = (RobotoTextView) drawChildMul.findViewById(R.id.unit_price);
            panelPriceMul = (RobotoTextView) drawChildMul.findViewById(R.id.panel_price);
            betNameMul = (CustomTextView) drawChildMul.findViewById(R.id.bet_name_mul);
            if (globalPref.getCountry().equalsIgnoreCase("Ghana"))
                ((TextView) drawChildMul.findViewById(R.id.bet_amount_text)).setText("Play Amount");

            String[] nos = panelDatas.get(n).getPickedNumbers().split(",");

            //new code for saving lastpick
//            if (globalPref.getCountry().equalsIgnoreCase("lagos"))
//                setLastPickedtoPreferences(betTypeData.getBetName(), panelDatas.get(n).getPickedNumbers(), gameDevName);
            setLastPickedtoPreferences(panelDatas.get(n).getBetDispName(), panelDatas.get(n).getPickedNumbers(), gameDevName);

            updateBallLayMul(nos, isFastGame);

//            if (beanFull.getPanelData().size() > 1) {
            ghanaBetNameUpdataFiveGame(betNameMul, beanFull, n);
            unitPriceMul.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getPanelData().get(n).getUnitPrice() * beanFull.getPanelData().get(n).getBetAmtMul()))));
            noOfLineMul.setText(beanFull.getPanelData().get(n).getNoOfLines() + "");
            ghanaPanelPriceSet(pos, panelPriceMul, beanFull, n, drawDatas);
//            } else {
//                zimBetNameUpdataFiveGame(betNameMul, betTypeData);
//                unitPriceMul.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getUnitPrice())) * betTypeData.getBetAmtMul()));
//                noOfLineMul.setText(getNoOfLines(beanFull.getPanelData()) + "");
//                zimPanelPriceSet(pos, panelPriceMul, betTypeData, beanFull, drawDatas);
//            }

            // changes in mode
            if (panelPriceMul.getText().toString().length() > 12) {
                panelPriceMul.startAnimation(AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.text_marquee));
            }
            ticketPanel.addView(drawChildMul, n);
        }
    }


    private void updateOldLuckeyNumbers(List<TicketDataBean.PanelData> pickedNumbers, String[] drawResArr) {
        bonusPanelLay.removeAllViews();
        for (int n = 0; n < pickedNumbers.size(); n++) {
            String[] nos = pickedNumbers.get(n).getPickedNumbers().split(",");
            int frac = 8, add = 8;
            ArrayList<String> strList = new ArrayList<>();
            String temp = "";
            for (int k = 0; k < nos.length; k++) {
                if (k < frac) {
                    if (k == frac - 1) {
                        temp = temp.concat(nos[k]);
                    } else {
                        temp = temp.concat(nos[k] + ",");
                    }
                    if (nos.length <= frac && k == nos.length - 1) {
                        strList.add(temp);
                    }
                } else {
                    strList.add(temp);
                    temp = "";
                    temp = temp.concat(nos[k] + ",");
                    frac = frac + add;
                    if (nos.length < frac && k == nos.length - 1) {
                        strList.add(temp);
                    }
                }
            }

            for (int i = 0; i < strList.size(); i++) {
                bonusPanelLay.addView(getRowView(strList.get(i).split(",")));
            }
            if (n != pickedNumbers.size() - 1) {
                View viewLine1 = new View(TicketDescActivity.this);
                View viewLine2 = new View(TicketDescActivity.this);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 15, 0, 10);
                viewLine1.setLayoutParams(params);
                viewLine1.setBackgroundColor(getResources().getColor(R.color.grey));
                viewLine2.setLayoutParams(params);
                viewLine2.setBackgroundColor(getResources().getColor(R.color.grey));
                CustomTextView betName = new CustomTextView(TicketDescActivity.this);
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 15);
                betName.setGravity(Gravity.CENTER);
                betName.setTextAppearance(TicketDescActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                betName.setTextColor(getResources().getColor(R.color.txn_cal_month));
                betName.setLayoutParams(params);
                betName.setTextStyle(CustomTextView.TextStyles.BOLD);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View drawChild = inflater.inflate(R.layout.tckt_old_row_child, null);
                CustomTextView noOfLine, unitPrice, panelPrice;
                noOfLine = (CustomTextView) drawChild.findViewById(R.id.no_of_lines);
                unitPrice = (CustomTextView) drawChild.findViewById(R.id.unit_price);
                panelPrice = (CustomTextView) drawChild.findViewById(R.id.panel_price);
                noOfLine.setText("" + pickedNumbers.get(n).getNoOfLines());
                unitPrice.setText(AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getUnitPrice() * pickedNumbers.get(n).getBetAmtMul()));
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getPanelPrice()));
                if (pickedNumbers.get(n + 1).isQP()) {
                    betName.setText(pickedNumbers.get(n + 1).getBetName() + "(QP)");
                } else {
                    betName.setText(pickedNumbers.get(n + 1).getBetName());
                }

                bonusPanelLay.addView(viewLine1);
                bonusPanelLay.addView(drawChild);
                bonusPanelLay.addView(viewLine2);
                bonusPanelLay.addView(betName);
            } else {
                View viewLine1 = new View(TicketDescActivity.this);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 15, 0, 10);
                viewLine1.setLayoutParams(params);
                viewLine1.setBackgroundColor(getResources().getColor(R.color.grey));
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View drawChild = inflater.inflate(R.layout.tckt_old_row_child, null);
                CustomTextView noOfLine, unitPrice, panelPrice;
                noOfLine = (CustomTextView) drawChild.findViewById(R.id.no_of_lines);
                unitPrice = (CustomTextView) drawChild.findViewById(R.id.unit_price);
                panelPrice = (CustomTextView) drawChild.findViewById(R.id.panel_price);
                noOfLine.setText("" + pickedNumbers.get(n).getNoOfLines());
                unitPrice.setText(AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getUnitPrice() * pickedNumbers.get(n).getBetAmtMul()));
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getPanelPrice()));
                bonusPanelLay.addView(viewLine1);
                bonusPanelLay.addView(drawChild);
            }
        }
    }

    private void updateBallLayMul(String[] numArr, boolean isFast) {

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

            lastLayMul.setVisibility(View.VISIBLE);
            if (numArr.length <= noOfBallsInSingleLine) {
                secondSelectedNosLayMul.setVisibility(View.GONE);
                isDrawSecond = false;
                noOfLay = 1;
            } else {
                secondSelectedNosLayMul.setVisibility(View.VISIBLE);
                isDrawSecond = true;
                noOfLay = 2;
            }
            if (isDrawSecond) {
                check = (int) Math.round((double) numArr.length / 2);
            } else {
                check = numArr.length;
            }
            firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
            firstSelectedNosLayMul.setLayoutParams(firstParentParms);
            int margin = 32;
            if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin
                    / noOfLay))
                    / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(2));
                ballHeight = (int) (totalBallWidth / check - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin
                        / noOfLay)))
                        / noOfLay;
            }
            firstSelectedNosLayMul.removeAllViews();
            for (int i = 0; i < check; i++) {
                LayoutParams firstParms = new LayoutParams(
                        ballHeight, ballWidth);
                if (isDrawSecond) {
                    if (i != noOfBallsInSingleLine - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                } else {
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                }
                if (!isFast) {
                    CustomTextView CustomTextView = new CustomTextView(getApplicationContext());
                    CustomTextView.setTextColor(getResources().getColor(R.color.white));
                    CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ballHeight / 2);

                    CustomTextView.setGravity(Gravity.CENTER);


                    //for lagos
                    if (isLagos) {
                        CustomTextView.setTextColor(getResources().getColor(R.color.loc_white));
                        if (i > (ul - 1)) {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossiblebl));
                        } else {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossible));
                        }
                    } else {
                        if (numArr[i].equals("")) {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        } else {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else if (gameDevName.equals(Config.thaiGameName)) {
//                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tkt_circle));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        }
                    }
                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText(numArr[i]);
                    firstSelectedNosLayMul.addView(CustomTextView);
                }
            }
            if (isDrawSecond) {
                lastParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
                secondSelectedNosLayMul.setLayoutParams(lastParentParms);
                if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin / noOfLay)) / noOfLay) {
                    ballWidth = (int) (totalBallWidth / check - getPx(1));
                    ballHeight = (int) (totalBallWidth / check - getPx(1));
                } else {
                    ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay)))
                            / noOfLay;
                    ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay)))
                            / noOfLay;
                }
                secondSelectedNosLayMul.removeAllViews();
                for (int i = check; i < numArr.length; i++) {
                    LayoutParams firstParms = new LayoutParams(ballHeight, ballWidth);
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                    if (!isFast) {
                        CustomTextView CustomTextView = new CustomTextView(getApplicationContext());
                        CustomTextView.setTextColor(getResources().getColor(R.color.white));
                        CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ballHeight / 2);
                        CustomTextView.setGravity(Gravity.CENTER);
                        //for lagos
                        if (isLagos) {
                            CustomTextView.setTextColor(getResources().getColor(R.color.loc_white));
                            if (i > (ul - 1)) {
                                CustomTextView.setBackgroundDrawable(getResources()
                                        .getDrawable(R.drawable.impossiblebl));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources()
                                        .getDrawable(R.drawable.impossible));
                            }
                        } else {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        }
                        CustomTextView.setLayoutParams(firstParms);
                        CustomTextView.setText(numArr[i]);
                        secondSelectedNosLayMul.addView(CustomTextView);
                    }
                }
            }
        } else {
            lastLayMul.setVisibility(View.GONE);
        }
    }


    private void updateBallLay(String[] numArr, boolean isFast) {

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
            firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
            firstSelectedNosLay.setLayoutParams(firstParentParms);
            int margin = 32;
            if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin
                    / noOfLay))
                    / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(2));
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
                if (isDrawSecond) {
                    if (i != noOfBallsInSingleLine - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                } else {
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                }
                if (!isFast) {
                    CustomTextView CustomTextView = new CustomTextView(getApplicationContext());
                    CustomTextView.setTextColor(getResources().getColor(R.color.white));
                    CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ballHeight / 2);

                    CustomTextView.setGravity(Gravity.CENTER);


                    //for lagos
                    if (isLagos) {
                        CustomTextView.setTextColor(getResources().getColor(R.color.loc_white));
                        if (i > (ul - 1)) {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossiblebl));
                        } else {
                            CustomTextView.setBackgroundDrawable(getResources()
                                    .getDrawable(R.drawable.impossible));
                        }
                    } else {

                        if (numArr[i].equals("")) {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        } else {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        }
                    }
                    CustomTextView.setLayoutParams(firstParms);
                    CustomTextView.setText(numArr[i]);
                    firstSelectedNosLay.addView(CustomTextView);
                }
            }
            if (isDrawSecond) {
                lastParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
                secondSelectedNosLay.setLayoutParams(lastParentParms);
                if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin / noOfLay)) / noOfLay) {
                    ballWidth = (int) (totalBallWidth / check - getPx(1));
                    ballHeight = (int) (totalBallWidth / check - getPx(1));
                } else {
                    ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay)))
                            / noOfLay;
                    ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay)))
                            / noOfLay;
                }
                secondSelectedNosLay.removeAllViews();
                for (int i = check; i < numArr.length; i++) {
                    LayoutParams firstParms = new LayoutParams(ballHeight, ballWidth);
                    if (i != numArr.length - 1) {
                        firstParms.setMargins(0, 0, 1, 0);
                    }
                    if (!isFast) {
                        CustomTextView CustomTextView = new CustomTextView(getApplicationContext());
                        CustomTextView.setTextColor(getResources().getColor(R.color.white));
                        CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ballHeight / 2);
                        CustomTextView.setGravity(Gravity.CENTER);
                        //for lagos
                        if (isLagos) {
                            CustomTextView.setTextColor(getResources().getColor(R.color.loc_white));
                            if (i > (ul - 1)) {
                                CustomTextView.setBackgroundDrawable(getResources()
                                        .getDrawable(R.drawable.impossiblebl));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources()
                                        .getDrawable(R.drawable.impossible));
                            }
                        } else {
                            if (gameDevName.equals(Config.fiveGameName)) {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            } else {
                                CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                            }
                        }
                        CustomTextView.setLayoutParams(firstParms);
                        CustomTextView.setText(numArr[i]);
                        secondSelectedNosLay.addView(CustomTextView);
                    }
                }
            }
        } else {
            lastLay.setVisibility(View.GONE);
        }
    }

    private void updateFastBallLay(List<TicketDataBean.PanelData> panelData, int noOfDraws) {
        if (panelData.size() > 0) {
            ticketPanel.setVisibility(View.VISIBLE);
            noOfLay = 1;
            check = 1;
            firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, ((int) (selectedNosParentHeight - (getPx(2)))) / noOfLay);
            int margin = 10;
            if (totalBallWidth / check < (selectedNosParentHeight - getPx(2) - getPx(margin / noOfLay)) / noOfLay) {
                ballWidth = (int) (totalBallWidth / check - getPx(1));
                ballHeight = (int) (totalBallWidth / check - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
            }
            ticketPanel.removeAllViews();
            for (int i = 0; i < panelData.size(); i++) {
                TicketDataBean.PanelData data = panelData.get(i);

                LayoutParams firstParms = new LayoutParams(ballHeight, ballWidth);
                if (i != numArr.length - 1) {
                    firstParms.setMargins(0, 0, 1, 0);
                }
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View drawChild = inflater.inflate(R.layout.ticket_fast_lotto_single_panel, null);
                RobotoTextView noOfLine, unitPrice, panelPrice;
                LinearLayout selectedNos;
                noOfLine = (RobotoTextView) drawChild.findViewById(R.id.no_of_lines);
                unitPrice = (RobotoTextView) drawChild.findViewById(R.id.unit_price);
                panelPrice = (RobotoTextView) drawChild.findViewById(R.id.panel_price);
                selectedNos = (LinearLayout) drawChild.findViewById(R.id.first_selected_nos);


                noOfLine.setText("" + data.getNoOfLines() * data.getBetAmtMul());
                unitPrice.setText("" + AmountFormat.getAmountFormatForMobile((data.getUnitPrice())));
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TicketDescActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(data.getPanelPrice() / noOfDraws))));
                if (panelPrice.getText().toString().length() > 10) {
                    panelPrice.startAnimation(AnimationUtils.loadAnimation(TicketDescActivity.this, R.anim.text_marquee));

                }

//                panelPrice.setText("" + data.getPanelPrice());
                String betAmtMul = String.valueOf(data.getBetAmtMul());
                String numb = data.getPickedNumbers();

                String fastResNum = numb.substring(numb.indexOf("(") + 1, numb.indexOf(")"));
                String nos = "" + (fastResNum.length() == 1 ? 0 + "" + fastResNum : fastResNum) + "#" + (betAmtMul.length() == 1 ? 0 + "" + betAmtMul : betAmtMul);

                LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater1.inflate(R.layout.fast_game_nos_lay, null);
                LinearLayout sublay = (LinearLayout) view.findViewById(R.id.sub_lay);
                CustomTextView fastNos = (CustomTextView) view.findViewById(R.id.fast_number);
                CustomTextView nosSelected = (CustomTextView) view.findViewById(R.id.no_selected);
                fastNos.setTextColor(getResources().getColor(R.color.fast_nos_bg_color));
                nosSelected.setTextColor(getResources().getColor(R.color.fast_nos_text_color));
                fastNos.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        (float) (ballHeight / 2.9));
                nosSelected.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        (float) (ballHeight / 3.2));
                fastNos.setGravity(Gravity.CENTER);
                nosSelected.setGravity(Gravity.CENTER);
                sublay.setLayoutParams(firstParms);
                if (nos.contains("#")) {
                    fastNos.setText(nos.split("#")[0]);
                    nosSelected.setText(nos.split("#")[1]);
                }
                selectedNos.addView(view);

                ticketPanel.addView(drawChild, i);

            }

        } else {
            ticketPanel.setVisibility(View.GONE);
        }
    }


    private void getDisplayDetails() {
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        if (GlobalVariables.onTablet(getApplicationContext())) {
            selectedNosParentHeight = (int) (selectedNosParentDefaultHeight * 1.5);
        } else {
            switch (GlobalVariables.getDensity(TicketDescActivity.this)) {
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
    }

    private double getPx(double d) {
        return (d * getResources().getDisplayMetrics().density);

    }


    public void setLastPickedtoPreferences(String betName, String pickedNos, String gameDevName) {
        if (gameDevName.equalsIgnoreCase(Config.tenByTwenty)) {
            if (betName.equalsIgnoreCase("first10")) {
                mLotteryPreferences.setFIRST_10_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("last10")) {
                mLotteryPreferences.setLAST_10_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("allodd")) {
                mLotteryPreferences.setALLODD_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("alleven")) {
                mLotteryPreferences.setALLEVEN_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("oddeven")) {
                mLotteryPreferences.setODDEVEN_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("evenodd")) {
                mLotteryPreferences.setEVENODD_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("jumpoddeven")) {
                mLotteryPreferences.setJUMP_ODD_EVEN_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("jumpevenodd")) {
                mLotteryPreferences.setJUMP_EVEN_ODD_TEN_BY_TWENTY(pickedNos);
            } else if (betName.equalsIgnoreCase("direct10")) {
                mLotteryPreferences.setDIRECT_10_TEN_BY_TWENTY(pickedNos);
            }
        } else if (gameDevName.equalsIgnoreCase(Config.bonusKeno)) {
            if (betName.equalsIgnoreCase("Direct1")) {
                mLotteryPreferences.setDIRECT_1_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct2")) {
                mLotteryPreferences.setDIRECT_2_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct3")) {
                mLotteryPreferences.setDIRECT_3_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct4")) {
                mLotteryPreferences.setDIRECT_4_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct5")) {
                mLotteryPreferences.setDIRECT_5_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct6")) {
                mLotteryPreferences.setDIRECT_6_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm2")) {
                mLotteryPreferences.setPERM_2_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm3")) {
                mLotteryPreferences.setPERM_3_BONUS_KENO(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm4")) {
                mLotteryPreferences.setPERM_4_BONUS_KENO(pickedNos);
            }
        } else if (gameDevName.equalsIgnoreCase(Config.tenByThirty)) {
            if (betName.equalsIgnoreCase("Direct10")) {
                mLotteryPreferences.setDIRECT_10_TEN_THIRTY(pickedNos);
            } else if (betName.equalsIgnoreCase("First10")) {
                mLotteryPreferences.setFIRST_10_TEN_THIRTY(pickedNos);
            } else if (betName.equalsIgnoreCase("Middle10")) {
                mLotteryPreferences.setMIDDLE_10_TEN_THIRTY(pickedNos);
            } else if (betName.equalsIgnoreCase("Last10")) {
                mLotteryPreferences.setLAST_10_TEN_THIRTY(pickedNos);
            }
        } else if (gameDevName.equalsIgnoreCase(Config.fiveGameNameLagos)) {
            setNameForKeno(betName, pickedNos);
        } else {
            if (betName.equalsIgnoreCase("Perm1")) {
                mLotteryPreferences.setPERM_1(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm2")) {
                mLotteryPreferences.setPERM_2(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm3")) {
                mLotteryPreferences.setPERM_3(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm4")) {
                mLotteryPreferences.setPERM_4(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm5")) {
                mLotteryPreferences.setPERM_5(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm6")) {
                mLotteryPreferences.setPERM_6(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm6")) {
                mLotteryPreferences.setPERM_6(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct1")) {
                mLotteryPreferences.setDIRECT_1(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct2")) {
                mLotteryPreferences.setDIRECT_2(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct3")) {
                mLotteryPreferences.setDIRECT_3(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct4")) {
                mLotteryPreferences.setDIRECT_4(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct5")) {
                mLotteryPreferences.setDIRECT_5(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct6")) {
                mLotteryPreferences.setDIRECT_6(pickedNos);
            } else if (betName.equalsIgnoreCase("Banker")) {
                mLotteryPreferences.setBANKER(pickedNos);
            } else if (betName.equalsIgnoreCase("Banker1AgainstAll")) {//Banker1AgainstAll
                mLotteryPreferences.setBANKER_1_AGAINST_ALL(pickedNos);
            } else if (betName.equalsIgnoreCase("zeroToNine")) {
                mLotteryPreferences.setZERO_TO_NINE(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct12")) {
                mLotteryPreferences.setDIRECT_12(pickedNos);
            } else if (betName.equalsIgnoreCase("First12")) {
                mLotteryPreferences.setFIREST_12(pickedNos);
            } else if (betName.equalsIgnoreCase("Last12")) {
                mLotteryPreferences.setLAST_12(pickedNos);
            } else if (betName.equalsIgnoreCase("AllOdd")) {
                mLotteryPreferences.setALLODD(pickedNos);
            } else if (betName.equalsIgnoreCase("AllEven")) {
                mLotteryPreferences.setALLEVEN(pickedNos);
            } else if (betName.equalsIgnoreCase("OddEven")) {
                mLotteryPreferences.setODDEVEN(pickedNos);
            } else if (betName.equalsIgnoreCase("EvenOdd")) {
                mLotteryPreferences.setEVENODD(pickedNos);
            } else if (betName.equalsIgnoreCase("JumpEvenOdd")) {
                mLotteryPreferences.setJUMPEVENODD(pickedNos);
            } else if (betName.equalsIgnoreCase("JumpOddEven")) {
                mLotteryPreferences.setJUMPODDEVEN(pickedNos);
            } else if (betName.equalsIgnoreCase("Perm12")) {
                mLotteryPreferences.setPERM_12(pickedNos);
            } else if (betName.equalsIgnoreCase("Match10")) {
                mLotteryPreferences.setMATCH_10(pickedNos);
            } else if (betName.equalsIgnoreCase("Direct10")) {
                mLotteryPreferences.setDIRECT_10(pickedNos);
            } else if (betName.equalsIgnoreCase("oneToTwelve")) {
                mLotteryPreferences.setONE_TO_TWELVE(pickedNos);
            } else if (betName.equalsIgnoreCase("DC-Direct2")) {
                mLotteryPreferences.setDC_Direct2(pickedNos);
            } else if (betName.equalsIgnoreCase("DC-Perm2")) {
                mLotteryPreferences.setDC_Perm2(pickedNos);
            } else if (betName.equalsIgnoreCase("DC-Direct3")) {
                mLotteryPreferences.setDC_DIRECT3(pickedNos);
            } else if (betName.equalsIgnoreCase("DC-Perm3")) {
                mLotteryPreferences.setDC_Perm3(pickedNos);
            }

            //for machine
            else if (betName.equalsIgnoreCase("MN-Perm2")) {
                mLotteryPreferences.setMN_PERM2(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Direct2")) {
                mLotteryPreferences.setMN_DIRECT2(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Direct3")) {
                mLotteryPreferences.setMN_DIRECT3(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Direct4")) {
                mLotteryPreferences.setMN_DIRECT4(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Direct5")) {
                mLotteryPreferences.setMN_DIRECT5(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Perm3")) {
                mLotteryPreferences.setMN_PERM3(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Banker")) {
                mLotteryPreferences.setMN_BANKER(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Banker1AgainstAll")) {
                mLotteryPreferences.setMN_BANKER1AGAINSTALL(pickedNos);
            } else if (betName.equalsIgnoreCase("MN-Direct1")) {
                mLotteryPreferences.setMN_DIRECT1(pickedNos);
            }
        }
    }


    public void setNameForKeno(String betName, String pickedNos) {
        if (betName.equalsIgnoreCase("Perm2")) {
            mLotteryPreferences.setPERM_2_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Perm3")) {
            mLotteryPreferences.setPERM_3_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Perm4")) {
//            mLotteryPreferences.setPERM_4_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Perm5")) {
//            mLotteryPreferences.setPERM_5_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Perm6")) {
//            mLotteryPreferences.setPERM_6_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Perm6")) {
//            mLotteryPreferences.setPERM_6_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct1")) {
            mLotteryPreferences.setDIRECT_1_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct2")) {
            mLotteryPreferences.setDIRECT_2_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct3")) {
            mLotteryPreferences.setDIRECT_3_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct4")) {
            mLotteryPreferences.setDIRECT_4_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct5")) {
            mLotteryPreferences.setDIRECT_5_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Direct6")) {
//            mLotteryPreferences.setDIRECT_6_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Banker")) {
            mLotteryPreferences.setBANKER_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("Banker1AgainstAll")) {
            mLotteryPreferences.setBANKER_1_AGAINST_ALL_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("DC-Direct2")) {
            mLotteryPreferences.setDC_Direct2_ALL_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("DC-Perm2")) {
            mLotteryPreferences.setDC_Perm2_ALL_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("DC-Direct3")) {
            mLotteryPreferences.setDC_DIRECT3_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("DC-Perm3")) {
            mLotteryPreferences.setDC_Perm3_LAGOS(pickedNos);
        }


        //for machine
        //for machine
        else if (betName.equalsIgnoreCase("MN-Perm2")) {
            mLotteryPreferences.setMN_PERM2_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Direct2")) {
            mLotteryPreferences.setMN_DIRECT2_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Direct3")) {
            mLotteryPreferences.setMN_DIRECT3_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Direct4")) {
            mLotteryPreferences.setMN_DIRECT4_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Direct5")) {
            mLotteryPreferences.setMN_DIRECT5_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Perm3")) {
            mLotteryPreferences.setMN_PERM3_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Banker")) {
            mLotteryPreferences.setMN_BANKER_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Banker1AgainstAll")) {
            mLotteryPreferences.setMN_BANKER1AGAINSTALL_LAGOS(pickedNos);
        } else if (betName.equalsIgnoreCase("MN-Direct1")) {
            mLotteryPreferences.setMN_DIRECT1_LAGOS(pickedNos);
        }
    }

    private String getPickedNumberOfBonusFree(List<TicketDataBean.PanelData> panelDatas) {
        String result = "";
        for (int i = 0; i < panelDatas.size(); i++) {
            if (i != panelDatas.size() - 1) {
                result =
                        result.concat(panelDatas.get(i).getPickedNumbers() + ",");
            } else {
                result = result.concat(panelDatas.get(i).getPickedNumbers());

            }

        }

        return result;

    }

    private int getNoOfLines(List<TicketDataBean.PanelData> panelDatas) {
        int result = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            result = result + panelDatas.get(i).getNoOfLines();
        }
        return result;
    }

    private String getPanelPrice(List<TicketDataBean.PanelData> panelDatas) {
        double result = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            result = result + panelDatas.get(i).getPanelPrice();
        }
        return AmountFormat.getCorrectAmountFormat(result + "");
    }


    private View getRowView(String[] numArr) {
        LinearLayout linearLayout = new LinearLayout(TicketDescActivity.this);
        if (numArr.length > 0) {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            isDrawSecond = false;
            noOfLay = 1;
            check = numArr.length;
            LayoutParams firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            firstParentParms.setMargins(0, 1, 0, 1);
            linearLayout.setLayoutParams(firstParentParms);
            int margin = 12;
            if (totalBallWidth / 8 < (selectedNosParentHeight - getPx(2) - getPx(margin / noOfLay)) / noOfLay) {
                ballWidth = (int) (totalBallWidth / 8 - getPx(2));
                ballHeight = (int) (totalBallWidth / 8 - getPx(1));
            } else {
                ballWidth = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
                ballHeight = ((int) (selectedNosParentHeight - getPx(3) - getPx(margin / noOfLay))) / noOfLay;
            }
            linearLayout.removeAllViews();
            for (int i = 0; i < check; i++) {
                LayoutParams firstParms = new LayoutParams(ballHeight, ballWidth);
                if (i != numArr.length - 1) {
                    firstParms.setMargins(0, 0, 1, 0);
                }
                CustomTextView CustomTextView = new CustomTextView(getApplicationContext());
                CustomTextView.setTextColor(getResources().getColor(R.color.white));
                CustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ballHeight / 2);
                CustomTextView.setGravity(Gravity.CENTER);
                if (numArr[i].equals("")) {
                    CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                } else {
                    CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                }
                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText(numArr[i]);
                linearLayout.addView(CustomTextView);
            }

        }
        return linearLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goToHomeScreen();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(getApplicationContext(),
                MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(
                GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }


}
package com.skilrock.myaccount;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.bean.TrackTicketBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TrackTicketActivity extends DrawerBaseActivity {
    private LinearLayout ticketsParent;
    private View tcktDescChild;
    private JSONObject jsonObject;
    private TrackTicketBean bean;
    private ArrayList<TrackTicketBean> ticketBeans;
    private LinearLayout firstSelectedNosLay;
    private LinearLayout ticketPanels, ticketPanel, fiveByNineResult;

    private LinearLayout secondSelectedNosLay;
    private LinearLayout lastLay;
    private LinearLayout lastLaySub;
    private int selectedNosParentHeight, selectedNosParentDefaultHeight = 100, noOfBallsInSingleLine = 10;
    private int totalBallWidth, ballWidth, ballHeight;
    private String[] numArr;
    private String[] drawResArr;
    private boolean isDrawSecond, isFastGame;
    private int noOfLay;
    private int check;
    private LayoutParams firstParentParms, lastParentParms;
    private DisplayMetrics displaymetrics;
    private String gameDevName;
    private RelativeLayout betDetailsData;
    private String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private DecimalFormat format = new DecimalFormat("0.##");
    private int width, bonusBallWidth;
    private boolean isDSix;
    private LinearLayout bonusPanelLay;
    private CustomTextView payAmt, selectedNumberText;
    ;
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
    private GlobalPref globalPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.TRACK_TICKET);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        setContentView(R.layout.tckt_desc_lay);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.TRACK_TICKET);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        if (getIntent().hasExtra("betCode") && getIntent().getIntExtra("betCode", 0) == 1) {
            isDSix = true;
        } else {
            isDSix = false;
        }
        if (GlobalVariables.onTablet(getApplicationContext())) {
            selectedNosParentDefaultHeight = 120;
            noOfBallsInSingleLine = 10;
        } else {
            switch (GlobalVariables.getDensity(TrackTicketActivity.this)) {
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

        //lagos
        if (globalPref.getCountry().equalsIgnoreCase("Lagos") && ((bean.getBetTypeData().get(0).getBetDispName().equalsIgnoreCase("banker")) || (bean.getBetTypeData().get(0).getBetDispName().equalsIgnoreCase("MN-Banker")))) {
            isLagos = true;
        } else {
            isLagos = false;
        }

        for (int i = 0; i < ticketBeans.size(); i++) {
            ticketsParent.addView(addChilds(i), i);
        }
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText(getResources().getString(R.string.track_tckts));
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }


    private void bindViewIds() {
        ticketsParent = (LinearLayout) findViewById(R.id.tickets_parent);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.INVISIBLE);
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
            ticketBeans = new ArrayList<TrackTicketBean>();
            jsonObject = new JSONObject(getIntent().getStringExtra("data"));
//            String data = "{\"mainData\":{\"commonSaleData\":{\"drawData\":[{\"drawId\":\"1129\",\"drawTime\":\"19:00:00\",\"drawName\":\"Mondaykjskdj Special\",\"drawResult\":\"90,89,88,87,86 \",\"winningAmt\":19116.9,\"drawStatus\":\"VERIFICATION PENDING\",\"drawDate\":\"2016-01-04\",\"ticketWinStatus\":\"SETTLEMENT PENDING\"}],\"purchaseAmt\":1155.1,\"gameType\":\"normal\",\"purchaseDate\":\"04-12-2015\",\"ticketNumber\":\"100001723380765\",\"gameName\":\"5/90\",\"gameDevName\":\"Keno\",\"purchaseTime\":\"09:35:29\"},\"betTypeData\":[{\"betAmtMul\":7,\"numberPicked\":\"3\",\"pickedNumbers\":\"88,89,90\",\"betName\":\"Direct3\",\"unitPrice\":0.1,\"panelPrice\":0.7000000000000001,\"noOfLines\":1,\"isQp\":false},{\"betAmtMul\":10,\"numberPicked\":\"3\",\"pickedNumbers\":\"71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90\",\"betName\":\"Perm3\",\"unitPrice\":0.1,\"panelPrice\":1140,\"noOfLines\":1140,\"isQp\":false},{\"betAmtMul\":4,\"numberPicked\":\"2\",\"pickedNumbers\":\"82,83,84,85,86,87,88,89,90\",\"betName\":\"Perm2\",\"unitPrice\":0.1,\"panelPrice\":14.4,\"noOfLines\":36,\"isQp\":false}]},\"errorMsg\":\"\",\"isPromo\":false,\"isSuccess\":true}";
//            jsonObject = new JSONObject(data.toString());
            //String fakejson="{\"mainData\":{\"commonSaleData\":{\"purchaseAmt\":1,\"drawData\":[{\"drawId\":\"55650\",\"drawTime\":\"11:30:00\",\"drawName\":\"Woza Mpelaviki19\",\"drawResult\":\"12,45,33,36\",\"winningAmt\":0,\"drawStatus\":\"VERIFICATION PENDING\",\"drawDate\":\"2015-10-09\",\"ticketWinStatus\":\"UNCLAIMED\"}],\"gameType\":\"normal\",\"purchaseDate\":\"09-10-2015\",\"ticketNumber\":\"100002812820193\",\"gameName\":\"LUCKY NUMBERS\",\"gameDevName\":\"KenoTwo\",\"purchaseTime\":\"11:16:36\"},\"betTypeData\":[{\"betAmtMul\":1,\"pickedNumbers\":\"33,36,41,55,56,62,69,70,81,83\",\"numberPicked\":\"1\",\"betName\":\"Perm1\",\"unitPrice\":0.1,\"panelPrice\":1,\"noOfLines\":10,\"isQp\":true}]},\"errorMsg\":\"\",\"isSuccess\":true,\"isPromo\":false}";
            //  VariableStorage.UserPref.setStringPreferences(getApplicationContext(), VariableStorage.UserPref.USER_BAL, jsonObject.getString("availableBal"));
            //   jsonObject = new JSONObject("{\"responseCode\":0,\"saleTransId\":\"207\",\"responseMsg\":\"\",\"ticketData\":{\"ticketNumber\":\"100150111970164\",\"gameCode\":\"KenoTwo\",\"purchaseAmt\":1,\"drawData\":[{\"drawId\":\"49483\",\"drawTime\":\"10:15:00\",\"drawName\":\"Mhanza\",\"drawDate\":\"2015-07-16\"}],\"panelData\":[{\"betAmtMul\":1,\"numberPicked\":10,\"pickedNumbers\":\"4,13,22,31,49,58,67,76,85,88,\",\"betName\":\"Perm1\",\"unitPrice\":0.1,\"panelPrice\":1,\"noOfLines\":10,\"isQP\":false}],\"gameName\":\"Lucky Numbers\",\"purchaseTime\":\"2015-07-16 10:10:50\"},\"availableBal\":1221715.7,\"isPromo\":false}");
            // String fakeJson="{\"mainData\":{\"commonSaleData\":{\"purchaseAmt\":1,\"drawData\":[{\"drawId\":\"55650\",\"drawTime\":\"11:30:00\",\"drawName\":\"Woza Mpelaviki19\",\"drawResult\":\"1,33,41,55,45,45,45\",\"winningAmt\":0,\"drawStatus\":\"VERIFICATION PENDING\",\"drawDate\":\"2015-10-09\",\"ticketWinStatus\":\"UNCLAIMED\"}],\"gameType\":\"normal\",\"purchaseDate\":\"09-10-2015\",\"ticketNumber\":\"100002812820193\",\"gameName\":\"LUCKY NUMBERS\",\"gameDevName\":\"KenoTwo\",\"purchaseTime\":\"11:16:36\"},\"betTypeData\":[{\"betAmtMul\":1,\"pickedNumbers\":\"33,36,41,55,56,62,69,70,81,83\",\"numberPicked\":\"1\",\"betName\":\"Perm1\",\"unitPrice\":0.1,\"panelPrice\":1,\"noOfLines\":10,\"isQp\":true}]},\"errorMsg\":\"\",\"isSuccess\":true,\"isPromo\":false}";
            if (jsonObject != null) {
                bean = new Gson().fromJson(jsonObject.getJSONObject("mainData").toString(),
                        TrackTicketBean.class);
                ticketBeans.add(bean);
                if (jsonObject.getBoolean("isPromo")) {
                    JSONArray array = jsonObject.getJSONArray("promoTicketData");
                    for (int i = 0; i < array.length(); i++) {
                        TrackTicketBean bean1 = new Gson().fromJson(array
                                        .getJSONObject(i).toString(),
                                TrackTicketBean.class);
                        ticketBeans.add(bean1);
                    }
                }
                ticketsParent.removeAllViews();
            }
            gameDevName = jsonObject.getJSONObject("mainData").getJSONObject("commonSaleData").getString("gameDevName");
            //new code for lagos
            isFastGame = gameDevName.equalsIgnoreCase(Config.oneToTwelve) || gameDevName.equalsIgnoreCase(Config.fastGameName);

//            if (globalPref.getCountry().equalsIgnoreCase("lagos")) {
//                isFastGame = gameDevName.equals(Config.oneToTwelve);
//            } else {
//                isFastGame = gameDevName.equals(Config.fastGameName);
//            }

            //old
//            isFastGame = gameDevName.equalsIgnoreCase(Config.fastGameName);
            if (isFastGame) {
                selectedNosParentDefaultHeight = 120;
            } else {
                selectedNosParentDefaultHeight = 62;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private View addChilds(int pos) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tcktDescChild = inflater.inflate(R.layout.tckt_desc_child, null);
        TrackTicketBean beanFull = ticketBeans.get(pos);
        TrackTicketBean.BetTypeData betTypeData = beanFull.getBetTypeData().get(0);

        CustomTextViewTop payableText = ((CustomTextViewTop) tcktDescChild.findViewById(R.id.payable_text));
//        ((CustomTextViewDown) tcktDescChild.findViewById(R.id.payable_text)).setDownSpacing(6);

        numArr = betTypeData.getPickedNumbers().split(",");


        if (numArr.length > noOfBallsInSingleLine) {
            totalBallWidth = displaymetrics.widthPixels - (int) (2 * getResources().getDisplayMetrics().density) - (int) ((noOfBallsInSingleLine) * getResources().getDisplayMetrics().density) - GlobalVariables.getPx(14, getApplicationContext());
        } else {
            totalBallWidth = displaymetrics.widthPixels - (int) (2 * getResources().getDisplayMetrics().density) - (int) ((numArr.length - 1) * getResources().getDisplayMetrics().density) - GlobalVariables.getPx(14, getApplicationContext());
        }
        headerSubText.setText(beanFull.getCommonSaleData().getGameName().toUpperCase(Locale.ENGLISH));

        ArrayList<TrackTicketBean.DrawData> drawDatas = beanFull.getCommonSaleData().getDrawData();
        CustomTextView ticketNo, gameName, ticketDate, betName, noOfDraws, totalAmt, free;
        RobotoTextView noOfLine, unitPrice, panelPrice;

        ticketPanels = (LinearLayout) tcktDescChild.findViewById(R.id.ticketPanels);
        ticketNo = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_no);
        ticketDate = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_date);
        totalAmt = (CustomTextView) tcktDescChild.findViewById(R.id.tot_amt);
        noOfDraws = (CustomTextView) tcktDescChild.findViewById(R.id.no_of_draws);
        gameName = (CustomTextView) tcktDescChild.findViewById(R.id.gameName);
        free = (CustomTextView) tcktDescChild.findViewById(R.id.free);
        payAmt = (CustomTextView) tcktDescChild.findViewById(R.id.pay_amt);


        gameName.setText(beanFull.getCommonSaleData().getGameName().toUpperCase(Locale.ENGLISH));
        if (beanFull.getCommonSaleData().getGameName().contains("Free") || beanFull.getCommonSaleData().getGameName().contains("FREE")) {
            free.setVisibility(View.VISIBLE);
        } else {
            free.setVisibility(View.GONE);
        }
        ticketPanels.removeAllViews();
        //new code

//        if (beanFull.getBetTypeData().size() > 1 && !isFastGame) {
//            lengOfChildView = beanFull.getBetTypeData().size();
//        } else {
        lengOfChildView = drawDatas.size();
//        }


        for (int i = 0; i < lengOfChildView; i++) {
            TrackTicketBean.DrawData drawData;
            //new code
//            if (drawDatas.size() > 1 && !(drawDatas.size() <= i)) {
            if (drawDatas.size() > 1) {
                drawData = drawDatas.get(i);
            } else {
                drawData = drawDatas.get(0);
            }
            //old
//            TrackTicketBean.DrawData drawData = drawDatas.get(i);

            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View drawChild = inflater1.inflate(R.layout.ticket_single_bet, null);
            CustomTextView drawName, drawDate/*, drawStatus*/;
            CustomTextView winAmnt, winAmntCurrency;
            LinearLayout winAmtLayout;
            ImageView isWinIcon;
            CustomTextView winText;
            LinearLayout panelDetailLay;
            LinearLayout drawNameLayout;

            drawNameLayout = (LinearLayout) drawChild.findViewById(R.id.drawNameLayout);
            if (globalPref.getCountry().equalsIgnoreCase("Ghana"))
                ((TextView) drawChild.findViewById(R.id.bet_amount_text)).setText("Play Amount");
            winAmnt = (CustomTextView) drawChild.findViewById(R.id.winAmnt);
            winText = (CustomTextView) drawChild.findViewById(R.id.winText);
            winAmntCurrency = (CustomTextView) drawChild.findViewById(R.id.win_Amnt_curr);
            winAmtLayout = (LinearLayout) drawChild.findViewById(R.id.win_amt_layout);


            isWinIcon = (ImageView) drawChild.findViewById(R.id.isWinIcon);
            drawName = (CustomTextView) drawChild.findViewById(R.id.draw_name);
            drawDate = (CustomTextView) drawChild.findViewById(R.id.draw_date);
//            drawStatus = (CustomTextView) drawChild.findViewById(R.id.draw_status);
            betName = (CustomTextView) drawChild.findViewById(R.id.bet_name);

            ticketPanel = (LinearLayout) drawChild.findViewById(R.id.draw_parent);
            fiveByNineResult = (LinearLayout) drawChild.findViewById(R.id.fivebynineResult);

            if (drawData.getWinningAmt() > 0) {
                DecimalFormat df = new DecimalFormat("0.00");
                df.setMaximumFractionDigits(2);
                winAmnt.setText(df.format(drawData.getWinningAmt()));
                winAmntCurrency.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE));
                winAmtLayout.setVisibility(View.VISIBLE);
                if (winAmnt.getText().length() >= 9)
                    winAmnt.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));
            } else {
                winAmtLayout.setVisibility(View.INVISIBLE);
            }

            if (drawData.getTicketWinStatus().equalsIgnoreCase("SETTLEMENT PENDING")) {
                isWinIcon.setImageResource(R.drawable.pending_icon);
                isWinIcon.setVisibility(View.VISIBLE);
            } else if (drawData.getTicketWinStatus().equalsIgnoreCase("CLAIMED")) {
                winText.setText(drawData.getTicketWinStatus());
                winText.setVisibility(View.VISIBLE);
                isWinIcon.setVisibility(View.GONE);
            } else if (drawData.getTicketWinStatus().equalsIgnoreCase("UNCLAIMED")) {
                winText.setText(drawData.getTicketWinStatus());
                winText.setVisibility(View.VISIBLE);
                isWinIcon.setVisibility(View.GONE);
            } else if (drawData.getTicketWinStatus().equalsIgnoreCase("WIN"))
                isWinIcon.setVisibility(View.VISIBLE);
            else
                isWinIcon.setVisibility(View.INVISIBLE);

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
                panelDetailLay = (LinearLayout) drawChild.findViewById(R.id.panel_detail_lay);
                selectedNumberText = (CustomTextView) drawChild.findViewById(R.id.selected_number_text);

                bonusPanelLay = (LinearLayout) drawChild.findViewById(R.id.bonus_panel_lay);
                if ((gameDevName.equals(Config.bonusGameName
                ) || gameDevName.equals(Config.bonusFree) || gameDevName.equals(Config.bonusGameNameTwo)) && isDSix) {
                    lastLay.setVisibility(View.GONE);
                    drawName.setVisibility(View.GONE);
                    bonusPanelLay.setVisibility(View.VISIBLE);
//                    updateBonus(betTypeData.getPickedNumbers());
                    if (!betTypeData.getBetDispName().equalsIgnoreCase("perm6")) {
                        drawResArr = drawData.getDrawResult().split(",");
                        updateBonus(betTypeData.getPickedNumbers(), drawResArr);
                    } else {
                        drawResArr = drawData.getDrawResult().split(",");
                        if (pos == 0)
                            updateBonusFreePerm6(beanFull.getBetTypeData(), drawResArr);
                        else
                            updateBonusFreePerm6(beanFull.getBetTypeData(), drawResArr);
                    }
                } else {
                    lastLay.setVisibility(View.VISIBLE);
                    bonusPanelLay.setVisibility(View.GONE);
                    lastLaySub = (LinearLayout) drawChild.findViewById(R.id.last_lay_sub);
                    drawResArr = drawData.getDrawResult().split(",");
                    boolean mulPanel = false;
                    if ((beanFull.getBetTypeData().size() > 1) && (mulPanel)) {
                        lastLay.setVisibility(View.GONE);
                        drawName.setVisibility(View.GONE);
                        panelDetailLay.setVisibility(View.GONE);
                        bonusPanelLay.setVisibility(View.VISIBLE);
                        updateOldLuckeyNumbers(beanFull.getBetTypeData(), drawResArr);
                    } else {

                        if (beanFull.getBetTypeData().size() > 1) {
                            betName.setVisibility(View.GONE);
                            selectedNumberText.setVisibility(View.GONE);
                            fiveByNineResult.setVisibility(View.GONE);
                            ticketPanel.setVisibility(View.VISIBLE);
                            updateMulBallLay(beanFull.getBetTypeData(), isFastGame, beanFull, pos, betTypeData, drawDatas);
                        } else {
                            updateBallLay(numArr, isFastGame, drawResArr);
                        }
//                         oldcode
//                        updateBallLay(numArr, isFastGame, drawResArr);
                    }
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

                if (beanFull.getBetTypeData().size() > 1) {
//                    noOfLine.setText(beanFull.getBetTypeData().get(i).getNoOfLines() + "");
//
//                    unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getBetTypeData().get(i).getUnitPrice() * beanFull.getBetTypeData().get(i).getBetAmtMul()))));
//
//                    setPanelPriceForPanelRef(pos, panelPrice, beanFull, i);
//
//                    setBetNameForPanelUpdataFiveGame(betName, beanFull, i);

                } else {
                    noOfLine.setText(getNoOfLines(beanFull.getBetTypeData()) + "");

                    unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getUnitPrice() * betTypeData.getBetAmtMul()))));

                    setPanelPriceForDrawRef(pos, panelPrice, betTypeData, beanFull, drawDatas);

                    setBetNameForDrawUpdataFiveGame(betName, betTypeData);

                    //old code
//                        if (betTypeData.getBetName().equalsIgnoreCase("perm6") && (beanFull.getCommonSaleData().getGameName().contains("Free") || beanFull.getCommonSaleData().getGameName().contains("FREE"))) {
//                            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getPanelPrice(beanFull.getBetTypeData()));
//                        } else {
//                            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getPanelPrice() / drawDatas.size()))));
//                        }

//                        if (betTypeData.isQp()) {
//                            betName.setText(betTypeData.getBetName() + "(QP)");
//                        } else {
//                            betName.setText(betTypeData.getBetName());
//                        }

                }

                //old code
                if (isFastGame)
                    betName.setText(betTypeData.getBetName());


//                    noOfLine.setText(betTypeData.getNoOfLines() + "");
//                    noOfLine.setText(getNoOfLines(beanFull.getBetTypeData()) + "");
//                    unitPrice.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getUnitPrice()*betTypeData.getBetAmtMul()))));

//                    if (betTypeData.getBetName().equalsIgnoreCase("perm6") && (beanFull.getCommonSaleData().getGameName().contains("Free") || beanFull.getCommonSaleData().getGameName().contains("FREE"))) {
//                        panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getPanelPrice(beanFull.getBetTypeData()));
//                    } else {
//                        panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(betTypeData.getPanelPrice() / drawDatas.size()))));
//                    }


                if (panelPrice.getText().toString().length() > 12) {
                    panelPrice.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));
                }

            } else {
                drawName.setVisibility(View.GONE);
                ticketPanel.setVisibility(View.VISIBLE);
                fiveByNineResult.setVisibility(View.GONE);
                updateFastBallLay(beanFull.getBetTypeData(), drawDatas.size());
            }

            if (isFastGame) {
                if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                    if (betTypeData.isQp())
                        betName.setText(betTypeData.getBetName() + "(QP)");
                    else
                        betName.setText(betTypeData.getBetName());
                } else
                    betName.setText(betTypeData.getBetName());
            }

            String[] date = drawData.getDrawDate().split("-");
            String yeardraw = "", monthdraw = "", datedraw = "", time = "";

            if (date.length > 2) {
                yeardraw = date[0];
                monthdraw = date[1];
                datedraw = date[2];
                time = drawData.getDrawTime().substring(0, 5);
            }


//            drawDate.setText(datedraw + " " + MONTH[Integer.parseInt(monthdraw) - 1] + ", " + yeardraw + " " + time);
//            drawName.setText(drawData.getDrawName());


            if (drawData.getDrawName() == null || drawData.getDrawName().equals("") || (drawData.getDrawName() != null && drawData.getDrawName().trim().equalsIgnoreCase("null"))) {
                drawName.setVisibility(View.VISIBLE);
                drawDate.setVisibility(View.GONE);
                drawName.setText(datedraw + "-" + monthdraw + "-" + yeardraw + " " + time);
                drawDate.setText(datedraw + "-" + monthdraw + "-" + yeardraw + " " + time);

            } else {
                drawName.setVisibility(View.VISIBLE);
                drawDate.setVisibility(View.VISIBLE);
                drawName.setText(drawData.getDrawName());
                drawDate.setText(datedraw + "-" + monthdraw + "-" + yeardraw + " " + time);

            }

//            if (betTypeData.isQp()) {
//                betName.setText(betTypeData.getBetName() + "(QP)");
//            } else {
//                betName.setText(betTypeData.getBetName());
//            }
//            drawStatus.setText("");
            ticketPanels.addView(drawChild, i);
        }

        ticketNo.setText(beanFull.getCommonSaleData().getTicketNumber());


        String[] dateTimeTKT = (beanFull.getCommonSaleData().getPurchaseDate() + " " + beanFull.getCommonSaleData().getPurchaseTime()).split(" ");
//        String[] dateTimeTKT = beanFull.getCommonSaleData().getPurchaseTime().split(" ");
        String date = "", month = "", time = "", year = "";

        if (dateTimeTKT.length > 1) {
            year = dateTimeTKT[0].split("-")[2];
            month = dateTimeTKT[0].split("-")[1];
            date = dateTimeTKT[0].split("-")[0];
            time = dateTimeTKT[1].substring(0, 5);
        }
        ticketDate.setText(date + "-" + month + "-" + year + " " + dateTimeTKT[1]);
//        ticketDate.setText(beanFull.getCommonSaleData().getPurchaseDate()+beanFull.getCommonSaleData().getPurchaseTime());
        //ticketDate.setText(date + " " + MONTH[Integer.parseInt(month) - 1] + " " + year + " " + time);
        noOfDraws.setText(drawDatas.size() + "");

        payAmt.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getCommonSaleData().getPurchaseAmt()))));
//        if (payAmt.getText().toString().length() > 12) {
//            payAmt.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));
//        }


        int perPanelSize = bean.getBetTypeData().size();
        Double totalCal = 0.00;
        for (int i = 0; i < perPanelSize; i++) {
            totalCal = totalCal + bean.getBetTypeData().get(i).getPanelPrice();
        }


        totalAmt.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(totalCal))));
        if (totalAmt.getText().toString().length() > 12) {
            totalAmt.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));

        }


        return tcktDescChild;
    }

//    private void updateMulBallLay(ArrayList<TrackTicketBean.BetTypeData> betTypeData, boolean isFastGame, TrackTicketBean beanFull, int pos, TrackTicketBean.BetTypeData betTypeData1, ArrayList<TrackTicketBean.DrawData> drawDatas) {
//
//    }

    private void setBetNameForPanelUpdataFiveGame(CustomTextView betName, TrackTicketBean beanFull, int i) {
        if (beanFull.getBetTypeData().get(i).isQp()) {
            betName.setText(beanFull.getBetTypeData().get(i).getBetName() + "(QP)");
        } else {
            betName.setText(beanFull.getBetTypeData().get(i).getBetName());
        }
    }

    private void setPanelPriceForPanelRef(int pos, TextView panelPrice, TrackTicketBean beanFull, int i, ArrayList<TrackTicketBean.DrawData> drawDatas) {
        if (pos != 0)
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(beanFull.getBetTypeData().get(i).getPanelPrice()));
        else {
            double panelprice = beanFull.getBetTypeData().get(i).getPanelPrice() / drawDatas.size();
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(panelprice));
        }
    }

    private void setBetNameForDrawUpdataFiveGame(CustomTextView betName, TrackTicketBean.BetTypeData betTypeData) {
        if (betTypeData.isQp()) {
            betName.setText(betTypeData.getBetName() + "(QP)");
        } else {
            betName.setText(betTypeData.getBetName());
        }
    }

    private void setPanelPriceForDrawRef(int pos, TextView panelPrice, TrackTicketBean.BetTypeData betTypeData, TrackTicketBean beanFull, ArrayList<TrackTicketBean.DrawData> drawDatas) {
        if (pos != 0)
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + getPanelPrice(beanFull.getBetTypeData()));
        else {
            double panelprice = betTypeData.getPanelPrice() / drawDatas.size();
            panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(panelprice));
        }
    }

    private int getNoOfLines(List<TrackTicketBean.BetTypeData> panelDatas) {
        int result = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            result = result + panelDatas.get(i).getNoOfLines();
        }
        return result;
    }

    private String getPanelPrice(List<TrackTicketBean.BetTypeData> panelDatas) {
        double result = 0;
        for (int i = 0; i < panelDatas.size(); i++) {
            result = result + panelDatas.get(i).getPanelPrice();
        }
        return AmountFormat.getCorrectAmountFormat(result + "");
    }


    private void updateBonus(String pickedNumbers, String[] drawResArr) {
        String[] nos = pickedNumbers.split("Nxt");
        bonusBallWidth = width - GlobalVariables.getPx(21, getApplicationContext());
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
                customTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, bonusBallWidth / 2);
                customTextView.setGravity(Gravity.CENTER);
                customTextView.setLayoutParams(layoutParams);

//                String winNo = new String();
//
//                for (int z = 0; z < drawResArr.length; z++) {
//                    if (balls[j].equalsIgnoreCase(drawResArr[z])) {
//                        winNo = balls[j];
//                        break;
//                    } else {
//
//                    }
//                }
//
//                if (winNo.equalsIgnoreCase(balls[j])) {
//                    customTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
//                    customTextView.setTextColor(getResources().getColor(R.color.white));
//                    customTextView.setText(balls[j]);
//                } else {
//                    customTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tkt_circle));
//                    customTextView.setTextColor(getResources().getColor(R.color.txn_cal_month));
//                    customTextView.setText(balls[j]);
//                }
//                linearLayout.addView(customTextView);

                //old code
                customTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
                customTextView.setTextColor(getResources().getColor(R.color.white));
                customTextView.setText(balls[j]);
                linearLayout.addView(customTextView);
            }
            bonusPanelLay.addView(view, i);
        }
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


    private void updateMulBallLay(ArrayList<TrackTicketBean.BetTypeData> pickedNumbers, boolean isFastGame, TrackTicketBean beanFull, int pos, TrackTicketBean.BetTypeData betTypeData1, ArrayList<TrackTicketBean.DrawData> drawDatas) {
        ticketPanel.removeAllViews();

        for (int n = 0; n < pickedNumbers.size(); n++) {
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

            String[] nos = pickedNumbers.get(n).getPickedNumbers().split(",");
            updateBallLayMul(nos, isFastGame);

            noOfLineMul.setText(beanFull.getBetTypeData().get(n).getNoOfLines() + "");

            unitPriceMul.setText(AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(beanFull.getBetTypeData().get(n).getUnitPrice() * beanFull.getBetTypeData().get(n).getBetAmtMul()))));

            setPanelPriceForPanelRef(pos, panelPriceMul, beanFull, n, drawDatas);

            setBetNameForPanelUpdataFiveGame(betNameMul, beanFull, n);

            // changes in mode
            if (panelPriceMul.getText().toString().length() > 12) {
                panelPriceMul.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));
            }
            ticketPanel.addView(drawChildMul, n);
        }
    }


    private void updateBallLay(String[] numArr, boolean isFast, String[] drawResArr) {

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

//                    String winNo = new String();
//
//                    for (int z = 0; z < drawResArr.length; z++) {
//                        if (numArr[i].equalsIgnoreCase(drawResArr[z])) {
//                            winNo = numArr[i];
//                            break;
//                        } else {
//
//                        }
//                    }
//
//                    if (winNo.equalsIgnoreCase(numArr[i])) {
//                        CustomTextView.setLayoutParams(firstParms);
//                        CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
//                        CustomTextView.setTextColor(getResources().getColor(R.color.white));
//                        CustomTextView.setText(numArr[i]);
//                        firstSelectedNosLay.addView(CustomTextView);
//                    } else {
//                        CustomTextView.setLayoutParams(firstParms);
//                        CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tkt_circle));
//                        CustomTextView.setText(numArr[i]);
//                        firstSelectedNosLay.addView(CustomTextView);
//                    }

                    //oldcode
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


//                        String winNo = new String();
//
//                        for (int z = 0; z < drawResArr.length; z++) {
//                            if (numArr[i].equalsIgnoreCase(drawResArr[z])) {
//                                winNo = numArr[i];
//                                break;
//                            } else {
//
//                            }
//                        }
//
//                        if (winNo.equalsIgnoreCase(numArr[i])) {
//                            CustomTextView.setLayoutParams(firstParms);
//                            CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
//                            CustomTextView.setText(numArr[i]);
//                            secondSelectedNosLay.addView(CustomTextView);
//                        } else {
//                            CustomTextView.setLayoutParams(firstParms);
//                            CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tkt_circle));
//                            CustomTextView.setText(numArr[i]);
//                            secondSelectedNosLay.addView(CustomTextView);
//                        }


                        //oldcode
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

    private void updateFastBallLay(List<TrackTicketBean.BetTypeData> panelData, int noOfDraws) {
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
                TrackTicketBean.BetTypeData data = panelData.get(i);

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
//                unitPrice.setText("" + data.getUnitPrice());
                unitPrice.setText("" + AmountFormat.getAmountFormatForMobile((data.getUnitPrice())));
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(Double.parseDouble(format.format(data.getPanelPrice() / noOfDraws))));
                if (panelPrice.getText().toString().length() > 10) {
                    panelPrice.startAnimation(AnimationUtils.loadAnimation(TrackTicketActivity.this, R.anim.text_marquee));
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

    private void updateBonusFreePerm6(List<TrackTicketBean.BetTypeData> betTypeDatas, String[] drawResArr) {
        bonusPanelLay.removeAllViews();
        for (int n = 0; n < betTypeDatas.size(); n++) {
            String[] nos = betTypeDatas.get(n).getPickedNumbers().split(",");
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
                bonusPanelLay.addView(getRowView(strList.get(i).split(","), drawResArr));
            }
            if (n != betTypeDatas.size() - 1) {
                View viewLine = new View(TrackTicketActivity.this);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 15, 0, 10);
                viewLine.setLayoutParams(params);
                viewLine.setBackgroundColor(getResources().getColor(R.color.grey));
                CustomTextView betName = new CustomTextView(TrackTicketActivity.this);
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 15);
                betName.setGravity(Gravity.CENTER);
                betName.setTextAppearance(TrackTicketActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
                betName.setTextColor(getResources().getColor(R.color.txn_cal_month));
                betName.setLayoutParams(params);
                betName.setTextStyle(CustomTextView.TextStyles.BOLD);


                if (betTypeDatas.get(n).isQp()) {
                    betName.setText(betTypeDatas.get(n).getBetName() + "(QP)");
                } else {
                    betName.setText(betTypeDatas.get(n).getBetName());
                }
                bonusPanelLay.addView(viewLine);
                bonusPanelLay.addView(betName);
            }


//            View viewLine = new View(TrackTicketActivity.this);
//            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            params.setMargins(1, 1, 1, 1);
//            viewLine.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//            viewLine.setBackgroundColor(getResources().getColor(R.color.grey));
//            bonusPanelLay.addView(viewLine);
        }
    }


    private void updateOldLuckeyNumbers(List<TrackTicketBean.BetTypeData> pickedNumbers, String[] drawResArr) {
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
                bonusPanelLay.addView(getRowView(strList.get(i).split(","), drawResArr));
            }
            if (n != pickedNumbers.size() - 1) {
                View viewLine1 = new View(TrackTicketActivity.this);
                View viewLine2 = new View(TrackTicketActivity.this);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(0, 15, 0, 10);
                viewLine1.setLayoutParams(params);
                viewLine1.setBackgroundColor(getResources().getColor(R.color.grey));
                viewLine2.setLayoutParams(params);
                viewLine2.setBackgroundColor(getResources().getColor(R.color.grey));
                CustomTextView betName = new CustomTextView(TrackTicketActivity.this);
                params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 15);
                betName.setGravity(Gravity.CENTER);
                betName.setTextAppearance(TrackTicketActivity.this, android.R.style.TextAppearance_DeviceDefault_Medium);
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
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getPanelPrice()));
                if (pickedNumbers.get(n + 1).isQp()) {
                    betName.setText(pickedNumbers.get(n + 1).getBetName() + "(QP)");
                } else {
                    betName.setText(pickedNumbers.get(n + 1).getBetName());
                }

                bonusPanelLay.addView(viewLine1);
                bonusPanelLay.addView(drawChild);
                bonusPanelLay.addView(viewLine2);
                bonusPanelLay.addView(betName);
            } else {
                View viewLine1 = new View(TrackTicketActivity.this);
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
                panelPrice.setText(VariableStorage.GlobalPref.getStringData(TrackTicketActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getAmountFormatForMobile(pickedNumbers.get(n).getPanelPrice()));
                bonusPanelLay.addView(viewLine1);
                bonusPanelLay.addView(drawChild);
            }
        }
    }


//    private void updateBonusFreePerm6(String pickedNumbers) {
//        String[] nos = pickedNumbers.split(",");
//        int frac = 8, add = 8;
//        ArrayList<String> strList = new ArrayList<>();
//        String temp = "";
//        for (int k = 0; k < nos.length; k++) {
//            if (k < frac) {
//                if (k == frac - 1) {
//                    temp = temp.concat(nos[k]);
//                } else {
//                    temp = temp.concat(nos[k] + ",");
//                }
//                if (nos.length < frac && k == nos.length - 1) {
//                    strList.add(temp);
//                }
//            } else {
//                strList.add(temp);
//                temp = "";
//                temp = temp.concat(nos[k] + ",");
//                frac = frac + add;
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

    private String getPickedNumberOfBonusFree(List<TrackTicketBean.BetTypeData> panelDatas) {
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


    private void getDisplayDetails() {
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        if (GlobalVariables.onTablet(getApplicationContext())) {
            selectedNosParentHeight = (int) (selectedNosParentDefaultHeight * 1.5);
        } else {
            switch (GlobalVariables.getDensity(TrackTicketActivity.this)) {
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

    private View getRowView(String[] numArr, String[] drawResArr) {
        LinearLayout linearLayout = new LinearLayout(TrackTicketActivity.this);
        if (numArr.length > 0) {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            isDrawSecond = false;
            noOfLay = 1;
            check = numArr.length;
            LayoutParams firstParentParms = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            firstParentParms.setMargins(0, 1, 0, 1);
            linearLayout.setLayoutParams(firstParentParms);
            int margin = 15;
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

//                String winNo = new String();
//
//                for (int z = 0; z < drawResArr.length; z++) {
//                    if (numArr[i].equalsIgnoreCase(drawResArr[z])) {
//                        winNo = numArr[i];
//                        break;
//                    } else {
//
//                    }
//                }
//
//                if (winNo.equalsIgnoreCase(numArr[i])) {
//                    CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.impossible));
//                    CustomTextView.setTextColor(getResources().getColor(R.color.white));
//                    CustomTextView.setLayoutParams(firstParms);
//                    CustomTextView.setText(numArr[i]);
//                } else {
//                    CustomTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tkt_circle));
//                    CustomTextView.setTextColor(getResources().getColor(R.color.txn_cal_month));
//                    CustomTextView.setLayoutParams(firstParms);
//                    CustomTextView.setText(numArr[i]);
//                }

//old code
                CustomTextView.setLayoutParams(firstParms);
                CustomTextView.setText(numArr[i]);
                linearLayout.addView(CustomTextView);
//                linearLayout.addView(CustomTextView);

            }

        }
        return linearLayout;
    }


    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}
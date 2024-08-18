package com.skilrock.sportslottery;//package com.skilrock.sportslottery;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.skilrock.bean.SportsTicketBean;
//import com.skilrock.bean.TicketDataBean;
//import com.skilrock.config.Config;
//import com.skilrock.customui.CustomTextView;
//import com.skilrock.customui.DrawerBaseActivity;
//import com.skilrock.lottery.R;
//import com.skilrock.utils.GlobalVariables;
//import com.skilrock.utils.LotteryPreferences;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class TicketDescActivity extends DrawerBaseActivity {
//    private LinearLayout ticketsParent;
//    private View tcktDescChild;
//    private JSONObject jsonObject;
//    private TicketDataBean bean;
//    private ArrayList<TicketDataBean> ticketBeans;
//    private LinearLayout ticketPanel;
//
//    private LinearLayout lastLay;
//    private LinearLayout lastLaySub;
//    private int totalBallWidth, ballWidth, ballHeight;
//    private String[] numArr;
//    private boolean isDrawSecond, isFastGame;
//    private int noOfLay;
//    private int check;
//    private LayoutParams firstParentParms, lastParentParms;
//    private DisplayMetrics displaymetrics;
//    private String gameDevName;
//    private RelativeLayout betDetailsData;
//    private LotteryPreferences mLotteryPreferences;
//    private String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
//
//
//
//
//    private CheckBox cbMinusTwo, cbMinusOne, cbDraw, cbPlusOne, cbPlusTwo;
//    private CheckBox cbHome, cbAway;
//    private TextView txtTime, txtVenue;
//    private TextView txtHomeShort, txtHomeFull, txtAwayShort, txtAwayFull;
//    private int adapterRes;
//    private View convertView;
//    private SportsTicketBean sportsTicketBean;
//    private ArrayList<HashMap<String, String>> eventDataList;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tckt_desc_lay);
//        mLotteryPreferences = new LotteryPreferences(this);
//        sHeader();
//        setDrawerItems();
//        bindViewIds();
//
//        sportsTicketBean = (SportsTicketBean) getIntent().getExtras()
//                .getSerializable("sportsTicketBean");
//        eventDataList = new ArrayList<HashMap<String, String>>();
//        for (int i = 0; i < ticketBeans.size(); i++) {
//            ticketsParent.addView(addChilds(i), i);
//        }
//        headerNavigation.setImageResource(R.drawable.back);
//        headerNavigation.setOnTouchListener(null);
//        headerNavigation.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        headerText.setText(getResources().getString(R.string.my_tckts));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        manageHeader();
//    }
//
//    private void bindViewIds() {
//        ticketsParent = (LinearLayout) findViewById(R.id.tickets_parent);
//    }
//
//    private void manageHeader() {
//        locateRetailerScratch.setVisibility(View.GONE);
//        headerNavigation.setVisibility(View.VISIBLE);
//        headerImage.setVisibility(View.GONE);
//        headerSpinner.setVisibility(View.INVISIBLE);
//        headerText.setVisibility(View.VISIBLE);
//        headerSubText.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(GlobalVariables.startAmin,
//                GlobalVariables.endAmin);
//    }
//
//
//    private View addChilds(int pos) {
//
//
//
//        ticketPanel.removeAllViews();
//
//        for (int i = 0; i < drawDatas.size(); i++) {
//            TicketDataBean.DrawData drawData = drawDatas.get(i);
//            LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View drawChild = inflater1.inflate(R.layout.ticket_single_bet, null);
//            CustomTextView drawName, drawDate, drawStatus;
//            drawName = (CustomTextView) drawChild.findViewById(R.id.draw_name);
//            drawDate = (CustomTextView) drawChild.findViewById(R.id.draw_date);
//            noOfLine = (CustomTextView) drawChild.findViewById(R.id.no_of_lines);
//            panelPrice = (CustomTextView) drawChild.findViewById(R.id.panel_price);
//
//            // drawParent = (LinearLayout) tcktDescChild.findViewById(R.id.draw_parent);
//
////            if (drawDatas.size() > 1) {
////                drawDivider.setVisibility(View.VISIBLE);
////            } else {
////                drawDivider.setVisibility(View.GONE);
////            }
//
//
//            String[] date = drawData.getDrawDate().split("-");
//            String dateTxt = "", month = "", time = "";
//
//            if (date.length > 2) {
//                dateTxt = date[2];
//                month = date[1];
//                time = drawData.getDrawTime().substring(0, 5);
//            }
//
//            drawDate.setText(dateTxt + " " + MONTH[Integer.parseInt(month)] + " " + time);
////            gameName.setText(beanFull.getGameName());
//            noOfLine.setText(betTypeData.getNoOfLines() + "");
//            unitPrice.setText(betTypeData.getUnitPrice() + "");
//            panelPrice.setText(betTypeData.getPanelPrice() + "");
//            drawName.setText(drawData.getDrawName());
//
//            ticketPanel.addView(drawChild, i);
//        }
//
//        ticketNo.setText(beanFull.getTicketNumber());
//
//
//        String[] dateTimeTKT = beanFull.getPurchaseTime().split(" ");
//        String date = "", month = "", time = "";
//
//        if (dateTimeTKT.length > 1) {
//            date = dateTimeTKT[0].split("-")[2];
//            month = dateTimeTKT[0].split("-")[1];
//            time = dateTimeTKT[1].substring(0, 5);
//        }
//
//        ticketDate.setText(date + " " + MONTH[Integer.parseInt(month)] + " " + time);
//        noOfDraws.setText(drawDatas.size() + "");
//        totalAmt.setText(beanFull.getPurchaseAmt() + "");
//
//        return tcktDescChild;
//    }
//
//    private View showTicket(int pos, String[] selectedDraw,
//                            HashMap<String, String> matchData) {
//        convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
//                .inflate(adapterRes, null);
//        txtHomeShort = (TextView) convertView.findViewById(R.id.txt_team_home_short_name);
//        txtHomeFull = (TextView) convertView.findViewById(R.id.txt_team_home_full_name);
//        txtAwayShort = (TextView) convertView.findViewById(R.id.txt_team_away_short_name);
//        txtAwayFull = (TextView) convertView.findViewById(R.id.txt_team_away_full_name);
//        txtTime = (TextView) convertView.findViewById(R.id.txt_time);
//        txtVenue = (TextView) convertView.findViewById(R.id.txt_venue);
//
//        txtHomeShort.setText(matchData.get("eventDisplayHomeShort"));
//        txtHomeFull.setText(matchData.get("eventDisplayHome"));
//        txtAwayShort.setText(matchData.get("eventDisplayAwayShort"));
//        txtAwayFull.setText(matchData.get("eventDisplayAway"));
//
//        txtVenue.setText(matchData.get("eventLeague") + ", "
//                + matchData.get("eventVenue"));
//        txtTime.setText(matchData.get("eventDate"));
//
//        if (adapterRes == R.layout.sports_row_item_five)
//            showTicketFive(convertView);
//        else
//            showTicketThree(convertView);
//
////        homeSelected = (TextView) convertView.findViewById(R.id.home_selected);
////        awaySelected = (TextView) convertView.findViewById(R.id.away_selected);
////        drawSelected = (TextView) convertView.findViewById(R.id.draw_selected);
//
//        // parent = (LinearLayout) convertView.findViewById(R.id.parent);
//
////        if (pos % 2 == 0) {
////            parent.setBackgroundColor(Color.parseColor("#0EB241"));
////        } else {
////            parent.setBackgroundColor(Color.parseColor("#13CC4C"));
////        }
////        setNormal(homeSelected);
////        setNormal(drawSelected);
////        setNormal(awaySelected);
//        // System.out.println("------" + selectedDraw.length);
//
//        // for (int i = 0; i < selectedDraw.length; i++) {
//        // System.out.println("==================" + i + "=========="
//        // + selectedDraw[i]);
//        // }
//
//        for (int i = 0; i < selectedDraw.length; i++) {
//            if (selectedDraw[i] == "H" || selectedDraw[i].contains("H")
//                    || selectedDraw[i].equalsIgnoreCase("H")) {
//                setSelected(cbHome);
//            }
//            if (selectedDraw[i] == "D" || selectedDraw[i].contains("D")
//                    || selectedDraw[i].equalsIgnoreCase("D")) {
//                setSelected(cbDraw);
//            }
//            if (selectedDraw[i] == "A" || selectedDraw[i].contains("A")
//                    || selectedDraw[i].equalsIgnoreCase("A")) {
//                setSelected(cbAway);
//            }
//
//            if (selectedDraw[i] == "-2" || selectedDraw[i].contains("-2")
//                    || selectedDraw[i].equalsIgnoreCase("-2")) {
//                setSelected(cbMinusTwo);
//            }
//            if (selectedDraw[i] == "-1" || selectedDraw[i].contains("-1")
//                    || selectedDraw[i].equalsIgnoreCase("-1")) {
//                setSelected(cbMinusOne);
//            }
//
//            if (selectedDraw[i] == "+1" || selectedDraw[i].contains("+1")
//                    || selectedDraw[i].equalsIgnoreCase("+1")) {
//                setSelected(cbPlusOne);
//            }
//            if (selectedDraw[i] == "+2" || selectedDraw[i].contains("+2")
//                    || selectedDraw[i].equalsIgnoreCase("+2")) {
//                setSelected(cbPlusTwo);
//            }
//        }
//
//        return convertView;
//    }
//
//    private void prepareTicket() {
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        tcktDescChild = inflater.inflate(R.layout.tckt_desc_child, null);
//
//
//        headerSubText.setText(sportsTicketBean.getTktData().getGameName());
//
//        CustomTextView ticketNo, ticketDate, noOfLine, panelPrice, noOfDraws, totalAmt;
//
//        ticketPanel = (LinearLayout) tcktDescChild.findViewById(R.id.ticketPanels);
//        ticketNo = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_no);
//        ticketDate = (CustomTextView) tcktDescChild.findViewById(R.id.tckt_date);
//        totalAmt = (CustomTextView) tcktDescChild.findViewById(R.id.tot_amt);
//        noOfDraws = (CustomTextView) tcktDescChild.findViewById(R.id.no_of_draws);
//
//        if (sportsTicketBean != null) {
//
//            if (sportsTicketBean.getResponseMsg().equalsIgnoreCase("success")) {
//
//                try {
//
//                    if (sportsTicketBean
//                            .getTktData().getEventType().split(",").length == 5) {
//                        adapterRes = R.layout.sports_row_item_five;
//                    } else if (sportsTicketBean
//                            .getTktData().getEventType().split(",").length == 3) {
//                        adapterRes = R.layout.sports_row_item_three;
//                    } else {
//                        Toast.makeText(this, "Wrong event type", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ArrayList<SportsTicketBean.EventData> eventDatas = sportsTicketBean
//                            .getTktData().getBoardData().get(0).getEventData();
//
//                    for (int i = 0; i < eventDatas.size(); i++) {
//
//                        SportsTicketBean.EventData tempData = eventDatas.get(i);
//                        HashMap<String, String> data = new HashMap<String, String>();
//                        data.put("eventDisplayHomeShort", tempData.getEventCodeHome());
//                        data.put("eventDisplayHome",
//                                tempData.getEventDisplayHome());
//                        data.put("eventDisplayAwayShort", tempData.getEventCodeAway());
//                        data.put("eventDisplayAway",
//                                tempData.getEventDisplayAway());
//                        data.put("eventLeague", tempData.getEventLeague());
//                        data.put("eventVenue", tempData.getEventVenue());
//                        data.put("eventDate", tempData.getEventDate());
//                        data.put("selection", tempData.getSelection());
//
//                        eventDataList.add(data);
//                    }
//                    for (int i = 0; i < eventDataList.size(); i++) {
//
//                        gamesChild.addView(showTicket(i, eventDataList.get(i).get("selection")
//                                        .split(","),
//                                eventDataList.get(i)));
//                        if (i < eventDataList.size() - 1)
//                            gamesChild.addView(addBar());
//                    }
//                    gameName.setText(sportsTicketBean.getTktData()
//                            .getGameTypeName());
//                    ticket_date.setText(sportsTicketBean.getTktData()
//                            .getPurchaseDate());
//                    ticket_time.setText(sportsTicketBean.getTktData()
//                            .getPurchaseTime());
//                    ticket_no_top.setText(sportsTicketBean.getTktData()
//                            .getTicketNo() + "");
//                    ticket_no_bottom.setText(sportsTicketBean.getTktData()
//                            .getTicketNo() + "");
//
//                    totAmt.setText(sportsTicketBean.getTktData().getTicketAmt());
//                    noOfDraws.setText(sportsTicketBean.getTktData().getBoardData().get(0).getNoOfLines());
//
//                    if (sportsTicketBean.getTktData().getBoardData() != null) {
//
//                        ArrayList<SportsTicketBean.BoardData> boardDatas = sportsTicketBean
//                                .getTktData().getBoardData();
//
//                        if (boardDatas.size() != 0) {
//                            SportsTicketBean.BoardData boardData = boardDatas.get(0);
//                            draw_name.setText(boardData.getDrawName());
//                            draw_date.setText(boardData.getDrawDate());
//                            draw_time.setText(boardData.getDrawTime());
//                            no_of_lines.setText(boardData.getNoOfLines() + "");
//                            unitPrice.setText(boardData.getUnitPrice() + "");
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                    e.printStackTrace();
//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                            TicketDescActivity.this);
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                    alertDialog.setCancelable(false);
//                    alertDialog.setMessage("Internal Error!, Try again later");
//                    alertDialog.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    TicketDescActivity.this.finish();
//                                }
//                            });
//                    alertDialog.show();
//                }
//
//            } else {
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                        TicketDescActivity.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("Internal Error!, Try again later");
//                alertDialog.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                TicketDescActivity.this.finish();
//                            }
//                        });
//                alertDialog.show();
//            }
//
//        } else {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                    TicketDescActivity.this);
//            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//            alertDialog.setCancelable(false);
//            alertDialog.setMessage(getString(R.string.net_error));
//            alertDialog.setPositiveButton("OK",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            TicketDescActivity.this.finish();
//                        }
//                    });
//            alertDialog.show();
//        }
//
//    }
//
//    private View addBar() {
//        View view = new View(this);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) GlobalVariables.pxToDp(2, this)));
//        view.setBackgroundColor(getResources().getColor(R.color.spl_bg_time));
//        return view;
//    }
//
//    private void setSelected(View view) {
//        ((CheckBox) view).setTextColor(getResources().getColor(R.color.spl_text_game_selected));
//        if (adapterRes == R.layout.sports_row_item_five)
//            view.setBackgroundResource(R.drawable.spl_check_box_selected_five);
//        else
//            view.setBackgroundResource(R.drawable.spl_check_box_selected_three);
//        view.setClickable(false);
//    }
//
//    private void showTicketFive(View view) {
//        cbMinusTwo = (CheckBox) view.findViewById(R.id.cb_minus_two);
//        cbMinusOne = (CheckBox) view.findViewById(R.id.cb_minus_one);
//        cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
//        cbPlusOne = (CheckBox) view.findViewById(R.id.cb_plus_one);
//        cbPlusTwo = (CheckBox) view.findViewById(R.id.cb_plus_two);
//    }
//
//    private void showTicketThree(View view) {
//        cbHome = (CheckBox) view.findViewById(R.id.cb_home);
//        cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
//        cbAway = (CheckBox) view.findViewById(R.id.cb_away);
//    }
//}

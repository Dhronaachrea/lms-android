package com.skilrock.sportslottery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.bean.SportsTicketBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SportsLotteryTicketActivity extends DrawerBaseActivity implements OnClickListener {
    // private JSONObject data;
    private TextView txtgameName;
    private TextView txtTicketDate;
    private TextView txtTicketNo;
    private TextView txtTotalAmt;
    private TextView txtNoOfLines;
    private TextView txtDrawName;
    private TextView txtWinningAmount;
    private TextView txtDrawDate;
    private TextView txtDrawUnitPrice;


    private ImageView imgWinStatus;
    private TextView txtWinStatus;
    private View divider;

    private boolean trackTicket;
    private LinearLayout ticketParent;
    private LinearLayout ticketPanel;
    private LinearLayout llRow;
    private View convertView;


    private int adapterRes;
    private CheckBox cbMinusTwo, cbMinusOne, cbDraw, cbPlusOne, cbPlusTwo;
    private CheckBox cbHome, cbAway;

    private TextView txtTime, txtVenue;
    private TextView txtHomeShort, txtHomeFull, txtAwayShort, txtAwayFull;
    private ArrayList<HashMap<String, String>> eventDataList;

    private SportsTicketBean sportsTicketBean;

    private String[] eventTypes;
    private Analytics analytics;
    private LinearLayout payabaleAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tckt_desc_lay);

        analytics = new Analytics();
        analytics.startAnalytics(this);

        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        sportsTicketBean = (SportsTicketBean) getIntent().getExtras()
                .getSerializable("sportsTicketBean");
        trackTicket = getIntent().getExtras().getBoolean("trackTicket");
        ticketParent = (LinearLayout) findViewById(R.id.tickets_parent);
        eventDataList = new ArrayList<HashMap<String, String>>();
        ticketParent.removeAllViews();
        sHeader();
        setDrawerItems();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (sportsTicketBean != null) {
            try {
                if (!trackTicket) {
                    headerText.setText(getResources().getString(R.string.my_tckts));
                    headerSubText.setText("PURCHASED TICKET");
                    analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER_TICKET);
                } else {
                    headerSubText.setText(sportsTicketBean.getTktData().getGameName());
                    headerText.setText(getResources().getString(R.string.track_tckts));
                    analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER_TRACK_TICKET);
                }
//                    DataSource.Login.currentBalance = Integer.parseInt(sportsTicketBean
//                            .getTktData().getBalance());
            } catch (Exception e) {

            }
        }
        prepareTicket();
//        String jsonObjectPrint = getIntent().getExtras().getString("printBean");
//        if (appInstalledOrNot("com.skilrock.printtest")) {
//            Intent intent = new Intent();
//            intent.putExtra("printType", "sports");
//            intent.putExtra("printer", jsonObjectPrint);
//            intent.setComponent(new ComponentName("com.skilrock.printtest", "com.skilrock.printtest.MainActivity"));
//            startActivity(intent);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();

    }

//    private boolean appInstalledOrNot(String uri) {
//        PackageManager pm = getPackageManager();
//        boolean app_installed;
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            app_installed = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            app_installed = false;
//        }
//        return app_installed;
//    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            SportsLotteryTicketActivity.this.finish();
        }
    }

    private View addBar() {
        View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) GlobalVariables.getPx(1f, this)));
        view.setBackgroundColor(getResources().getColor(R.color.grey));
        return view;
    }

    private View showTicket(int pos, String[] selectedDraw,
                            HashMap<String, String> matchData) {
        convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(adapterRes, null);
        txtHomeShort = (TextView) convertView.findViewById(R.id.txt_team_home_short_name);
        txtHomeFull = (TextView) convertView.findViewById(R.id.txt_team_home_full_name);
        txtAwayShort = (TextView) convertView.findViewById(R.id.txt_team_away_short_name);
        txtAwayFull = (TextView) convertView.findViewById(R.id.txt_team_away_full_name);
        txtTime = (TextView) convertView.findViewById(R.id.txt_time);
        txtVenue = (TextView) convertView.findViewById(R.id.txt_venue);

        txtHomeShort.setText(matchData.get("eventDisplayHomeShort"));
        txtHomeFull.setText(matchData.get("eventDisplayHome"));
        txtAwayShort.setText(matchData.get("eventDisplayAwayShort"));
        txtAwayFull.setText(matchData.get("eventDisplayAway"));

        txtVenue.setText(matchData.get("eventLeague") + ", "
                + matchData.get("eventVenue"));

        Date d = null;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            d = dateFormat.parse(matchData.get("eventDate"));
            dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US);
            String dateTime = dateFormat.format(d);
            String[] startTime = dateTime.split(" ");
            String[] date = startTime[0].split("-");
            String[] time = startTime[1].split(":");
            txtTime.setText(time[0] + ":" + time[1] + ", " + date[0] + " " + date[1].toUpperCase(Locale.US));
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        String[] startTime = matchData.get("eventDate").split(" ");
//        String[] date = startTime[0].split("-");
//        String[] time = startTime[1].split(":");
//        txtTime.setText(time[0] + ":" + time[1] + ", " + date[0] + " " + getMonth(Integer.parseInt(date[1])));
        //txtTime.setText(matchData.get("eventDate"));

        if (adapterRes == R.layout.sports_result_row_item_five)
            showTicketFive(convertView);
        else
            showTicketThree(convertView);

//        homeSelected = (TextView) convertView.findViewById(R.id.home_selected);
//        awaySelected = (TextView) convertView.findViewById(R.id.away_selected);
//        drawSelected = (TextView) convertView.findViewById(R.id.draw_selected);

        // parent = (LinearLayout) convertView.findViewById(R.id.parent);

//        if (pos % 2 == 0) {
//            parent.setBackgroundColor(Color.parseColor("#0EB241"));
//        } else {
//            parent.setBackgroundColor(Color.parseColor("#13CC4C"));
//        }
//        setNormal(homeSelected);
//        setNormal(drawSelected);
//        setNormal(awaySelected);
        // System.out.println("------" + selectedDraw.length);

        // for (int i = 0; i < selectedDraw.length; i++) {
        // System.out.println("==================" + i + "=========="
        // + selectedDraw[i]);
        // }

        for (int i = 0; i < selectedDraw.length; i++) {
            if (adapterRes == R.layout.sports_result_row_item_three) {
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[0].trim())) {
                    setSelected(cbHome);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[1].trim())) {
                    setSelected(cbDraw);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[2].trim())) {
                    setSelected(cbAway);
                }
            } else if (adapterRes == R.layout.sports_result_row_item_five) {

                if (selectedDraw[i].equalsIgnoreCase(eventTypes[0].trim())) {
                    setSelected(cbMinusTwo);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[1].trim())) {
                    setSelected(cbMinusOne);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[2].trim())) {
                    setSelected(cbDraw);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[3].trim())) {
                    setSelected(cbPlusOne);
                }
                if (selectedDraw[i].equalsIgnoreCase(eventTypes[4].trim())) {
                    setSelected(cbPlusTwo);
                }
            }
        }

        return convertView;
    }

    private void showTicketFive(View view) {
        cbMinusTwo = (CheckBox) view.findViewById(R.id.cb_minus_two);
        cbMinusOne = (CheckBox) view.findViewById(R.id.cb_minus_one);
        cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
        cbPlusOne = (CheckBox) view.findViewById(R.id.cb_plus_one);
        cbPlusTwo = (CheckBox) view.findViewById(R.id.cb_plus_two);
    }

    private void showTicketThree(View view) {
        cbHome = (CheckBox) view.findViewById(R.id.cb_home);
        cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
        cbAway = (CheckBox) view.findViewById(R.id.cb_away);
    }

   /* private void setNormal(View view) {
        ((CheckBox) view).setTextColor(getResources().getColor(R.color.spl_text_game_selected));
        ((CheckBox) view).setChecked(true);
        view.setClickable(false);
    }*/

    private void setSelected(View view) {
        ((CheckBox) view).setTextColor(getResources().getColor(R.color.spl_text_game_selected));
        if (adapterRes == R.layout.sports_result_row_item_five)
            view.setBackgroundResource(R.drawable.spl_check_box_selected_five);
        else
            view.setBackgroundResource(R.drawable.spl_check_box_selected_five);
        view.setClickable(false);
    }

    private void prepareTicket() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View tcktDescChild = inflater.inflate(R.layout.tckt_desc_child, null);
        tcktDescChild.findViewById(R.id.sec_divider).setVisibility(View.GONE);
        tcktDescChild.findViewById(R.id.divider).setVisibility(View.GONE);
        txtgameName = (TextView) tcktDescChild.findViewById(R.id.gameName);
        txtTicketNo = (TextView) tcktDescChild.findViewById(R.id.tckt_no);
        txtTicketDate = (TextView) tcktDescChild.findViewById(R.id.tckt_date);
        txtNoOfLines = (TextView) tcktDescChild.findViewById(R.id.no_of_draws);
        ((TextView) tcktDescChild.findViewById(R.id.txt_draw)).setText("Of Lines");
        txtTotalAmt = (TextView) tcktDescChild.findViewById(R.id.tot_amt);
        ticketPanel = (LinearLayout) tcktDescChild.findViewById(R.id.ticketPanels);
        View row = inflater.inflate(R.layout.sports_lottery_ticket_single_bet, null);
        txtDrawName = (TextView) row.findViewById(R.id.draw_name);
        txtDrawDate = (TextView) row.findViewById(R.id.draw_date);
        txtDrawUnitPrice = (TextView) row.findViewById(R.id.panel_price);
        txtWinningAmount = (TextView) row.findViewById(R.id.winAmnt);
        imgWinStatus = (ImageView) row.findViewById(R.id.isWinIcon);
        txtWinStatus = (TextView) row.findViewById(R.id.winText);
        llRow = (LinearLayout) row.findViewById(R.id.ll_row);
        payabaleAmt = (LinearLayout) tcktDescChild.findViewById(R.id.payabale_amt);
        payabaleAmt.setVisibility(View.GONE);

        if (sportsTicketBean != null) {

            if (sportsTicketBean.getResponseMsg().equalsIgnoreCase("success")) {

                try {

                    if (sportsTicketBean
                            .getTktData().getEventType().split(",").length == 5) {
                        adapterRes = R.layout.sports_result_row_item_five;
                        eventTypes = sportsTicketBean
                                .getTktData().getEventType().replace("[", "").replace("]", "").split(",");
                    } else if (sportsTicketBean
                            .getTktData().getEventType().split(",").length == 3) {
                        adapterRes = R.layout.sports_result_row_item_three;
                        eventTypes = sportsTicketBean
                                .getTktData().getEventType().replace("[", "").replace("]", "").split(",");
                    } else {
                        Utils.Toast(this, "Wrong event type");
                        return;
                    }

                    ArrayList<SportsTicketBean.EventData> eventDatas = sportsTicketBean
                            .getTktData().getBoardData().get(0).getEventData();

                    for (int i = 0; i < eventDatas.size(); i++) {

                        SportsTicketBean.EventData tempData = eventDatas.get(i);
                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("eventDisplayHomeShort", tempData.getEventCodeHome());
                        data.put("eventDisplayHome",
                                tempData.getEventDisplayHome());
                        data.put("eventDisplayAwayShort", tempData.getEventCodeAway());
                        data.put("eventDisplayAway",
                                tempData.getEventDisplayAway());
                        data.put("eventLeague", tempData.getEventLeague());
                        data.put("eventVenue", tempData.getEventVenue());
                        data.put("eventDate", tempData.getEventDate());
                        data.put("selection", tempData.getSelection());

                        eventDataList.add(data);
                    }
                    for (int i = 0; i < eventDataList.size(); i++) {
                        llRow.addView(showTicket(i, eventDataList.get(i).get("selection")
                                        .split(","),
                                eventDataList.get(i)));
                        if (i < eventDataList.size() - 1)
                            llRow.addView(addBar());
                    }
                    // txtTicketNo.setText(sportsTicketBean.getTktData().get);
//                    sportsTicketBean.getTktData()


                    String[] date = sportsTicketBean.getTktData().getPurchaseDate().split("-");
                    String[] time = sportsTicketBean.getTktData().getPurchaseTime().split(":");
                    txtTicketNo.setText(sportsTicketBean.getTktData().getTicketNo() + "");
                    txtgameName.setText(sportsTicketBean.getTktData().getGameTypeName());
                    analytics.sendAll(Fields.Category.SL_TICKET, Fields.Action.OPEN, "Game : " + sportsTicketBean.getTktData().getGameTypeName());
                    txtTicketDate.setText(sportsTicketBean.getTktData().getPurchaseDate() + " " + sportsTicketBean.getTktData().getPurchaseTime());
//                    txtTicketDate.setText(date[2] + " " + getMonth(Integer.parseInt(date[1])).substring(0, 3).toUpperCase() + " " + date[0] + "\t" + time[0] + ":" + time[1]);
                    txtTotalAmt.setText(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE) + sportsTicketBean.getTktData().getTicketAmt());
//                    if (txtTotalAmt.getText().toString().length() > 6) {
//                        txtTotalAmt.startAnimation(AnimationUtils.loadAnimation(this, R.anim.spl_text_marquee));
//                    }
                    ticketPanel.addView(row);
                    ticketParent.addView(tcktDescChild);
                    if (sportsTicketBean.getTktData().getBoardData() != null) {

                        ArrayList<SportsTicketBean.BoardData> boardDatas = sportsTicketBean
                                .getTktData().getBoardData();

                        if (boardDatas.size() != 0) {
                            SportsTicketBean.BoardData boardData = boardDatas.get(0);

                            SimpleDateFormat dateFormat;
                            try {
                                dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                Date d = dateFormat.parse(boardData.getDrawDate());
                                dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                String dateTime = dateFormat.format(d);
                                date = dateTime.split("-");
                                time = boardData.getDrawTime().split(":");
                                txtDrawName.setText(boardData.getDrawName());
                                txtDrawDate.setText(date[0] + " " + date[1].toUpperCase(Locale.US) + ", " + date[2] + " " + time[0] + ":" + time[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            txtNoOfLines.setText(boardData.getNoOfLines() + "");
                            txtDrawUnitPrice.setText(AmountFormat.getAmountFormatForMobileDecimal(Double.parseDouble(sportsTicketBean.getTktData().getTicketAmt()) / boardData.getNoOfLines()) + "");


                            if (boardData.getWinAmt() != 0) {
                                txtWinningAmount.setVisibility(View.VISIBLE);
                                DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
                                df.applyPattern("0.00");
                                df.setMaximumFractionDigits(2);
                                txtWinningAmount.setText(VariableStorage.GlobalPref.getStringData(
                                        this, VariableStorage.GlobalPref.CURRENCY_CODE) + "\n" + df.format(boardData.getWinAmt()));
//                                ((AmountTextView) txtWinningAmount).setCurrencySymbol(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE) + "\n");
//                                txtWinningAmount.setText(AmountFormat.getCurrentAmountFormatForMobile(boardData.getWinAmt()) + "");
//                                if (txtWinningAmount.getText().toString().length() > 6) {
//                                    txtWinningAmount.startAnimation(AnimationUtils.loadAnimation(this, R.anim.spl_text_marquee));
//                                }
                            } else {
                                txtWinningAmount.setVisibility(View.INVISIBLE);
                            }
                            if (boardData.getWinStatus() != null) {
                                if (boardData.getWinStatus().equalsIgnoreCase("WIN")) {
                                    imgWinStatus.setVisibility(View.VISIBLE);
                                } else if (boardData.getWinStatus().equalsIgnoreCase("NON WIN")) {
                                    imgWinStatus.setVisibility(View.INVISIBLE);
                                } else if (boardData.getWinStatus().equalsIgnoreCase("RA") || boardData.getWinStatus().equalsIgnoreCase("")) {
                                    imgWinStatus.setVisibility(View.INVISIBLE);
                                } else if (boardData.getWinStatus().equalsIgnoreCase("SETTLEMENT PENDING")) {
                                    imgWinStatus.setVisibility(View.VISIBLE);
                                    imgWinStatus.setImageResource(R.drawable.pending_icon);
                                } else if (boardData.getWinStatus().equalsIgnoreCase("CLAIMED") || boardData.getWinStatus().equalsIgnoreCase("UNCLAIMED")) {
                                    imgWinStatus.setVisibility(View.GONE);
                                    txtWinStatus.setText(boardData.getWinStatus());
                                    txtWinStatus.setVisibility(View.VISIBLE);
                                } else {
                                    imgWinStatus.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                imgWinStatus.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    e.printStackTrace();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            SportsLotteryTicketActivity.this);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Internal Error!, Try again later");
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SportsLotteryTicketActivity.this.finish();
                                }
                            });
                    alertDialog.show();
                }

            } else {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        SportsLotteryTicketActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Internal Error!, Try again later");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                SportsLotteryTicketActivity.this.finish();
                            }
                        });
                alertDialog.show();
            }

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    SportsLotteryTicketActivity.this);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.net_error));
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SportsLotteryTicketActivity.this.finish();
                        }
                    });
            alertDialog.show();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}

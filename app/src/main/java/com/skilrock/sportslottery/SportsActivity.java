package com.skilrock.sportslottery;//package com.skilrock.sportslottery;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.skilrock.bean.SportsBean;
//import com.skilrock.bean.SportsHelper;
//import com.skilrock.bean.SportsTicketBean;
//import com.skilrock.lms.communication.Communication;
//import com.skilrock.lottery.R;
//import com.skilrock.utils.DataSource;
//import com.skilrock.utils.GlobalVariables;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//
//public class SportsActivity extends Activity implements
//        OnLongClickListener, OnClickListener, OnTouchListener {
//    private Handler onLongPressCounterHandler;
//    private int sum;
//    private boolean isIncremented = false;
//    private boolean isDecremented = false;
//    private double maxPanelAmount;
//    public static TextView countText;
//    private TextView countInc;
//    private TextView countDec;
//    private ProgressDialog progressDialog;
//    private ListView menuList;
//    private SportsHelper adapter;
//    // private TextView username;
//    //
//    private ImageView drawerImageHeader;
//    private Spinner spinnerHeader;
//    private TextView nextDrawTime;
//    private Button purchase, clear;
//    private ArrayList<SportsBean.EventData> dummyData;
//    private ArrayList<SportsBean.EventData> adapterData;
//    private JSONObject drawData;
//    private SportsBean sportsBean;
//    private boolean pd;
//    private StringBuffer buffer;
//    private SportsBean.GameTypeData gameTypeData;
//    private TextView noOfLines, finalAmount, headerName;
//    private static int gameId = 0;
//
//    JSONObject drawInfos, finalJson;
//    JSONArray eventData, drawInfo;
//    private View buyViewSports, clearViewSports;
//
//    public static DecimalFormat decimalFormat = new DecimalFormat("######.##");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sports_lottery);
//        SportsHelper.totalLines = 0;
//        onLongPressCounterHandler = new Handler();
//        countText = (TextView) findViewById(R.id.count);
//        countInc = (TextView) findViewById(R.id.inc);
//        countDec = (TextView) findViewById(R.id.dec);
//        headerName = (TextView) findViewById(R.id.header_name_sports);
//        // headerHeadingName = (TextView) findViewById(R.id.sub_header_name);
//        buyViewSports = (View) findViewById(R.id.buyViewSports);
//        clearViewSports = (View) findViewById(R.id.clearViewSports);
//        spinnerHeader = (Spinner) findViewById(R.id.spinner);
//        drawerImageHeader = (ImageView) findViewById(R.id.drawer_image);
//        spinnerHeader.setVisibility(View.GONE);
//        drawerImageHeader.setVisibility(View.GONE);
//
//        countInc.setOnClickListener(this);
//        countDec.setOnClickListener(this);
//        countDec.setOnTouchListener(this);
//        countInc.setOnTouchListener(this);
//        countInc.setOnLongClickListener(this);
//        countDec.setOnLongClickListener(this);
//
//        noOfLines = (TextView) findViewById(R.id.no_of_lines);
//        finalAmount = (TextView) findViewById(R.id.final_amount);
//        // View header = findViewById(R.id.ll);
//        // header.findViewById(R.id.sub_header).setVisibility(View.GONE);
//        // header.findViewById(R.id.sub_header_name).setVisibility(View.VISIBLE);
//
//        gameTypeData = (SportsBean.GameTypeData) getIntent().getSerializableExtra(
//                "matchData");
//        finalAmount.setText("0.0" + " " + DataSource.Login.currenctSymbol);
//        gameId = getIntent().getIntExtra("gameId", 0);
//        maxPanelAmount = gameTypeData.getGameTypeMaxBetAmtMultiple();
//        countText.setText((int) gameTypeData.getGameTypeUnitPrice() + "");
//        // username = (TextView) findViewById(R.id.player_name);
//        // balance = (TextView) findViewById(R.id.player_balance);
//
//        nextDrawTime = (TextView) findViewById(R.id.next_draw_time);
//        purchase = (Button) findViewById(R.id.buy);
//        clear = (Button) findViewById(R.id.clear);
//
//        clear.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (clear.isEnabled()) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                            SportsActivity.this);
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                    alertDialog.setCancelable(false);
//                    alertDialog.setMessage("Are you sure to reset ?");
//                    alertDialog.setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    prepareData();
//                                    adapter.notifyDataSetChanged();
//                                    noOfLines.setText("0");
//                                    finalAmount.setText("0 "
//                                            + DataSource.Login.currenctSymbol);
//                                    countText.setText(0.0 + "");
//                                    SportsHelper.totalLines = 0;
//                                    purchase.setEnabled(false);
//                                    buyViewSports.setBackgroundColor(Color
//                                            .parseColor("#88000000"));
//                                }
//                            });
//                    alertDialog.setNegativeButton("No", null);
//                    alertDialog.show();
//                } else {
//                    return;
//                }
//            }
//        });
//        purchase.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawInfos = new JSONObject();
//                eventData = new JSONArray();
//                drawInfo = new JSONArray();
//                finalJson = new JSONObject();
//                buffer = new StringBuffer();
////                for (int i = 0; i < SportsHelper.data.size(); i++) {
////
////                    // System.out.println("\"eventId\" : "
////                    // + SportsHelper.data.get(i).getEventId());
////                    // System.out.println("\"H\" : "
////                    // + SportsHelper.data.get(i).isHomeSelected());
////                    // System.out.println("\"D\" : "
////                    // + SportsHelper.data.get(i).isDrawSelected());
////                    // System.out.println("\"A\" : "
////                    // + SportsHelper.data.get(i).isAwaySelected());
////
////                }
//
//                for (int i = 0; i < SportsHelper.data.size(); i++) {
//                    if (!SportsHelper.data.get(i).isHomeSelected()
//                            && !SportsHelper.data.get(i).isDrawSelected()
//                            && !SportsHelper.data.get(i).isAwaySelected()) {
//                        pd = false;
//                        break;
//                    } else {
//                        pd = true;
//                        JSONObject eventDatas = new JSONObject();
//                        try {
//                            eventDatas.put("eventId", SportsHelper.data.get(i)
//                                    .getEventId());
//                        } catch (Exception e) {
//                        }
//
//                        if (SportsHelper.data.get(i).isHomeSelected()) {
//
//                            try {
//                                if (eventDatas.has("eventSelected")) {
//                                    String temp = eventDatas
//                                            .getString("eventSelected");
//
//                                    eventDatas
//                                            .put("eventSelected", temp + ",H");
//                                } else {
//                                    eventDatas.put("eventSelected", "H");
//
//                                }
//                            } catch (JSONException e) {
//                            }
//
//                        }
//                        if (SportsHelper.data.get(i).isDrawSelected()) {
//
//                            try {
//
//                                if (eventDatas.has("eventSelected")) {
//                                    String temp = eventDatas
//                                            .getString("eventSelected");
//                                    eventDatas
//                                            .put("eventSelected", temp + ",D");
//                                } else {
//                                    eventDatas.put("eventSelected", "D");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (SportsHelper.data.get(i).isAwaySelected()) {
//                            try {
//                                if (eventDatas.has("eventSelected")) {
//                                    String temp = eventDatas
//                                            .getString("eventSelected");
//                                    eventDatas
//                                            .put("eventSelected", temp + ",A");
//                                } else {
//                                    eventDatas.put("eventSelected", "A");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        eventData.put(eventDatas);
//                    }
//                }
//                if (pd) {
//                    try {
//                        drawInfos.put("drawId", gameTypeData.getDrawData().get(0)
//                                .getDrawId());
//                        drawInfos.put("betAmtMul", Double.parseDouble(countText
//                                .getText().toString()));
//                        drawInfos.put("eventData", eventData);
//                        drawInfo.put(drawInfos);
//
//                        finalJson.put("gameId", SportsActivity.gameId);
//                        finalJson.put("gameTypeId", gameTypeData.gameTypeId);
//                        finalJson.put("noOfBoard", 1);
//                        finalJson.put("playerName", DataSource.Login.username);
//                        finalJson.put("merchantCode",
//                                DataSource.Login.merchantCode);
//                        // finalJson.put("merchantPwd",
//                        // DataSource.Login.merchantPwd);
//                        finalJson.put("sessionId", DataSource.Login.sessionID);
//                        finalJson.put("drawInfo", drawInfo);
//
//                        Log.i("finalJson", finalJson + "");
//
//                        // showMessageAlertDialog();
//
//                        if (Double.parseDouble(finalAmount.getText().toString()
//                                .replace(DataSource.Login.currenctSymbol, "")
//                                .trim()) != 0) {
//                            if (Double.parseDouble(finalAmount
//                                    .getText()
//                                    .toString()
//                                    .replace(DataSource.Login.currenctSymbol,
//                                            "").trim()) > 100000) {
//                                Toast.makeText(SportsActivity.this,
//                                        "Balance is not Sufficient ",
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                showDialog(1);
//
//                            }
//                        } else {
//                            Toast.makeText(SportsActivity.this,
//                                    "Play Amount should not Zero(0)",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Select atleast one from all!", Toast.LENGTH_LONG)
//                            .show();
//                }
//            }
//        });
//        menuList = (ListView) findViewById(R.id.main_menu);
//        try {
//            if (gameTypeData.getDrawData().getCurrentData().size() > 0) {
//                prepareData();
//
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                        SportsActivity.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("No Match Available !");
//                alertDialog.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                // startActivity(new
//                                // Intent(SportsActivity.this,
//                                // LoginActivity.class));
//                                SportsActivity.this.finish();
//
//                            }
//                        });
//                alertDialog.show();
//            }
//
//        } catch (Exception e) {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                    SportsActivity.this);
//            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//            alertDialog.setCancelable(false);
//            alertDialog.setMessage(R.string.net_error);
//            alertDialog.setPositiveButton("OK", null);
//            alertDialog.show();
//
//        }
//        // progressDialog = ProgressDialog.show(SportsActivity.this, "",
//        // "Please Wait...", false, false);
//        // new Thread(this).start();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // username.setText(DataSource.Login.username);
//        // balance.setText(DataSource.Login.currentBalance + " "
//        // + DataSource.Login.currenctSymbol);
//
//        headerName.setText(getIntent().getStringExtra("gameHeaderName"));
//        nextDrawTime.setText(gameTypeData.getDrawData().getDrawDateTime());
//        // if (!DataSource.Login.isSessionValid)
//        // SportsActivity.this.finish();
//        // headerHeadingName.setText(gameTypeData.getDrawData()
//        // .getDrawDisplayString());
//        // list[0] = "5/90 Ghana " + DataSource.Login.drawName;
//        // Check Internet Connectivity
//    }
//
//    public void run() {
//        // drawData = Communication.drawGameData(DataSource.Login.username);
//        // drawData = DataSource.sportsListJson;
//        Gson gson = new Gson();
//        sportsBean = gson.fromJson(DataSource.sportsListJson, SportsBean.class);
//        handler.sendEmptyMessage(0);
//    }
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            progressDialog.dismiss();
//            if (sportsBean != null) {
//                try {
//                    if (sportsBean.isSuccess) {
//                        ArrayList<SportsBean.GameData> gameData = sportsBean
//                                .getSportsLotteryData().getGameData();
//                        if (gameData.size() != 0) {
//                            DataSource.SportsLottery.gameId = gameData.get(0)
//                                    .getGameId();
//                            SportsBean.GameTypeData gameTypeData = gameData.get(0)
//                                    .getGameTypeData().get(0);
//                            DataSource.SportsLottery.gameTypeDisplayName = gameTypeData
//                                    .getGameTypeDisplayName();
//                            DataSource.SportsLottery.gameTypeId = gameTypeData
//                                    .getGameTypeId();
//                            SportsBean.DrawData drawData = gameTypeData.getDrawData();
//                            DataSource.SportsLottery.drawDisplayString = drawData
//                                    .getDrawDisplayString();
//                            DataSource.SportsLottery.drawId = drawData
//                                    .getDrawId();
//                            DataSource.SportsLottery.drawDateTime = drawData
//                                    .getDrawDateTime();
//                            ArrayList<SportsBean.EventData> eventData = drawData
//                                    .getCurrentData().get(0).getEventData();
//                            for (int i = 0; i < eventData.size(); i++) {
//                                SportsBean.EventData modal = new SportsBean.EventData();
//                                modal.setHomeSelected(false);
//                                modal.setDrawSelected(false);
//                                modal.setAwaySelected(false);
//                                modal.setEventDisplayHome("");
//                                modal.setEventDisplayAway("");
//                                modal.setEventLeague("");
//                                modal.setEventVenue("");
//                                modal.setEventDate("");
//                                modal.setEventId(10);
//                                dummyData.add(modal);
//                            }
//                            // adapter = new SportsHelper(SportsActivity.this,
//                            // R.layout.sports_row, dummyData);
//                            menuList.setAdapter(adapter);
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    "No games!", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        if (sportsBean.getErrorCode() == 118) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                    SportsActivity.this);
//                            alertDialog
//                                    .setIcon(android.R.drawable.ic_dialog_alert);
//                            alertDialog.setCancelable(false);
//                            alertDialog.setMessage("Session Out Login Again");
//                            alertDialog.setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(
//                                                DialogInterface dialog,
//                                                int which) {
//                                            // startActivity(new
//                                            // Intent(SportsActivity.this,
//                                            // LoginActivity.class));
//                                            SportsActivity.this.finish();
//                                            DataSource.Login.isSessionValid = false;
//                                        }
//                                    });
//                            alertDialog.show();
//                        } else
//                            Toast.makeText(SportsActivity.this,
//                                    sportsBean.getErrorMsg(), Toast.LENGTH_LONG)
//                                    .show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                        SportsActivity.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("No Draw Available !");
//                alertDialog.setPositiveButton("OK", null);
//                alertDialog.show();
//            }
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.inc) {
//            double value = Double.parseDouble(countText.getText().toString());
//            // sum = 0;
//            // for (int i = 0; i < DataSource.Fortune.fortunePanels.length; i++)
//            // {
//            // sum += DataSource.Fortune.fortunePanels[i];
//            // }
//            if (value >= maxPanelAmount) {
//                value = 0;
//                value += gameTypeData.getGameTypeUnitPrice();
//                countText.setText(String.valueOf(value));
//                updateFinalAmt(value * SportsHelper.totalLines);
//                // Toast.makeText(getApplicationContext(),
//                // "your max panel is seleted!", 1000).show();
//            } else {
//                if (value < maxPanelAmount && value > -1) {
//                    value += gameTypeData.getGameTypeUnitPrice();
//                    countText.setText(String.valueOf(value));
//                    updateFinalAmt(value * SportsHelper.totalLines);
//                }
//            }
//        } else if (id == R.id.dec) {
//            double value1 = Double.parseDouble(countText.getText().toString());
//            if (value1 > 1) {
//                value1 -= gameTypeData.getGameTypeUnitPrice();
//                countText.setText(String.valueOf(value1));
//                updateFinalAmt(value1 * SportsHelper.totalLines);
//            } else {
//                value1 = maxPanelAmount + gameTypeData.getGameTypeUnitPrice();
//                value1 -= gameTypeData.getGameTypeUnitPrice();
//                countText.setText(String.valueOf(value1));
//                updateFinalAmt(value1 * SportsHelper.totalLines);
//            }
//        }
//    }
//
//    public void decrement() {
//        double value1 = Double.parseDouble(countText.getText().toString());
//        if (value1 > 1) {
//            value1 -= gameTypeData.getGameTypeUnitPrice();
//            countText.setText(String.valueOf(value1));
//            updateFinalAmt(value1 * SportsHelper.totalLines);
//        } else {
//            value1 = maxPanelAmount + gameTypeData.getGameTypeUnitPrice();
//            value1 -= gameTypeData.getGameTypeUnitPrice();
//            countText.setText(String.valueOf(value1));
//            updateFinalAmt(value1 * SportsHelper.totalLines);
//        }
//    }
//
//    // Value Increment method
//    public void increment() {
//
//        double value = Double.parseDouble(countText.getText().toString());
//        // sum = 0;
//        // for (int i = 0; i < DataSource.Fortune.fortunePanels.length; i++) {
//        // sum += DataSource.Fortune.fortunePanels[i];
//        // }
//        if (value >= maxPanelAmount) {
//            value = 0;
//            value += gameTypeData.getGameTypeUnitPrice();
//            countText.setText(String.valueOf(value));
//            updateFinalAmt(value * SportsHelper.totalLines);
//            // Toast.makeText(getApplicationContext(),
//            // "your max panel is seleted!", 1000).show();
//        } else {
//            if (value < maxPanelAmount && value > -1) {
//                value += gameTypeData.getGameTypeUnitPrice();
//                countText.setText(String.valueOf(value));
//                updateFinalAmt(value * SportsHelper.totalLines);
//            }
//        }
//
//    }
//
//    @Override
//    public boolean onLongClick(View v) {
//        int id = v.getId();
//        if (id == R.id.inc) {
//            isIncremented = true;
//            onLongPressCounterHandler
//                    .postDelayed(new CounterValueIncDec(), 100);
//        } else if (id == R.id.dec) {
//            isDecremented = true;
//            onLongPressCounterHandler
//                    .postDelayed(new CounterValueIncDec(), 100);
//        }
//        return false;
//    }
//
//    // Thread to update Counter Value
//    class CounterValueIncDec implements Runnable {
//        @Override
//        public void run() {
//            if (isIncremented) {
//                increment();
//                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
//                        100);
//            } else if (isDecremented) {
//                decrement();
//                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
//                        100);
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//        int id = v.getId();
//        if (id == R.id.inc) {
//            if ((event.getAction() == MotionEvent.ACTION_UP || event
//                    .getAction() == MotionEvent.ACTION_CANCEL) && isIncremented) {
//                isIncremented = false;
//            }
//        } else if (id == R.id.dec) {
//            if ((event.getAction() == MotionEvent.ACTION_UP || event
//                    .getAction() == MotionEvent.ACTION_CANCEL) && isDecremented) {
//                isDecremented = false;
//            }
//        }
//
//        return false;
//    }
//
//    private void updateFinalAmt(double amt) {
//
//        finalAmount.setText(decimalFormat.format(amt) + " "
//                + DataSource.Login.currenctSymbol);
//    }
//
//    private void prepareData() {
//        dummyData = gameTypeData.getDrawData().getCurrentData().get(0)
//                .getEventData();
//        adapterData = new ArrayList<SportsBean.EventData>();
//        for (int j = 0; j < dummyData.size(); j++) {
//            SportsBean.EventData eventData = new SportsBean.EventData();
//            eventData.setAwaySelected(false);
//            eventData.setHomeSelected(false);
//            eventData.setDrawSelected(false);
//            eventData.setAwayId(j);
//            eventData.setHomeId(j);
//            eventData.setDrawId(j);
//            eventData.setEventDate(dummyData.get(j).getEventDate());
//            eventData.setEventDisplayAway(dummyData.get(j)
//                    .getEventDisplayAway());
//            eventData.setEventDisplayHome(dummyData.get(j)
//                    .getEventDisplayHome());
//            eventData.setEventId(dummyData.get(j).getEventId());
//            eventData.setEventLeague(dummyData.get(j).getEventLeague());
//            eventData.setEventVenue(dummyData.get(j).getEventVenue());
//            adapterData.add(eventData);
//        }
//        adapter = new SportsHelper(SportsActivity.this, R.layout.sports_row,
//                adapterData, noOfLines, finalAmount, purchase, buyViewSports,
//                clear, clearViewSports);
//        menuList.setAdapter(adapter);
//        clear.setEnabled(false);
//        clearViewSports.setBackgroundColor(Color.parseColor("#88000000"));
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id, Bundle bundle) {
//        AlertDialog dialogDetails = null;
//        LayoutInflater inflater = null;
//        View dialogView = null;
//        AlertDialog.Builder dialogbuilder = null;
//
//        switch (id) {
//            case 1:
//                inflater = LayoutInflater.from(this);
//
//                dialogView = inflater.inflate(
//                        R.layout.sports_lottery_dialog_layout_preview, null);
//
//                dialogbuilder = new AlertDialog.Builder(this);
//                dialogbuilder.setTitle("Purchase Confirmation");
//
//                dialogbuilder.setView(dialogView);
//                // dialogDetails.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                dialogDetails = dialogbuilder.create();
//
//                break;
//        }
//        return dialogDetails;
//    }
//
//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
//        final AlertDialog alertDialog = (AlertDialog) dialog;
//        // final Bundle bundle1 = bundle;
//        Button ok = null;
//        Button cancel = null;
//        TextView numbersOfLines = null;
//        TextView betAmountm = null;
//
//        TextView totalAmount = null;
//        switch (id) {
//
//            case 1:
//                ok = (Button) alertDialog.findViewById(R.id.btn_ok);
//                cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
//                numbersOfLines = (TextView) alertDialog
//                        .findViewById(R.id.no_of_lines);
//                betAmountm = (TextView) alertDialog
//                        .findViewById(R.id.betamount_mul);
//                totalAmount = (TextView) alertDialog
//                        .findViewById(R.id.total_amount);
//
//                if (noOfLines != null && finalAmount != null && countText != null) {
//
//                    numbersOfLines.setText(noOfLines.getText().toString());
//                    betAmountm.setText(countText.getText().toString());
//
//                    totalAmount.setText(finalAmount.getText().toString());
//
//                }
//
//                ok.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (finalJson != null) {
//                            alertDialog.dismiss();
//
//                            executeTask();
//
//                        } else {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                    SportsActivity.this);
//                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                            alertDialog.setCancelable(false);
//                            alertDialog.setMessage("Internal Error !, Try Later");
//                            alertDialog.setPositiveButton("OK", null);
//                            alertDialog.show();
//
//                        }
//
//                        // Intent intent = new Intent(SportsActivity.this,
//                        // SportsTicketActivity.class);
//                        // intent.putExtra("trackTicket", false);
//                        //
//                        // startActivity(intent);
//                        // SportsActivity.this.finish();
//
//                    }
//
//                });
//
//                cancel.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                break;
//
//        }
//    }
//
//    private void executeTask() {
//        if (finalJson != null)
//            progressDialog = ProgressDialog.show(SportsActivity.this, "",
//                    "Please Wait...", false, false);
//        progressDialog.show();
//        new PurchaseTask().execute(finalJson);
//    }
//
//    // public void showMessageAlertDialog() {
//    //
//    // final Dialog commentDialog = new Dialog(SportsActivity.this);
//    // commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//    // commentDialog
//    // .setContentView(R.layout.dialog_layout_sports_lottery_preview);
//    //
//    // Button ok = null;
//    // Button cancel = null;
//    // TextView numbersOfLines = null;
//    // TextView betAmountm = null;
//    //
//    // TextView totalAmount = null;
//    //
//    // ok = (Button)
//    // commentDialog.findViewById(R.id.btn_ok);
//    // cancel = (Button)
//    // commentDialog.findViewById(R.id.btn_cancel);
//    // numbersOfLines = (TextView) commentDialog
//    // .findViewById(R.id.no_of_lines);
//    // betAmountm = (TextView)
//    // commentDialog.findViewById(R.id.betamount_mul);
//    // totalAmount = (TextView)
//    // commentDialog.findViewById(R.id.total_amount);
//    //
//    // if (noOfLines != null && finalAmount != null && countText != null) {
//    //
//    // numbersOfLines.setText(noOfLines.getText().toString());
//    // betAmountm.setText(countText.getText().toString());
//    //
//    // totalAmount.setText(finalAmount.getText().toString());
//    //
//    // }
//    //
//    // ok.setOnClickListener(new OnClickListener() {
//    // @Override
//    // public void onClick(View v) {
//    //
//    // Intent intent = new Intent(SportsActivity.this,
//    // SportsTicketActivity.class);
//    // intent.putExtra("trackTicket", false);
//    //
//    // startActivity(intent);
//    // SportsActivity.this.finish();
//    //
//    // }
//    // });
//    //
//    // cancel.setOnClickListener(new View.OnClickListener() {
//    // @Override
//    // public void onClick(View v) {
//    // commentDialog.dismiss();
//    // }
//    // });
//    //
//    // commentDialog.getWindow().setBackgroundDrawableResource(
//    // R.drawable.widget_progress_dialog_bg);
//    // // commentDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
//    // // LayoutParams.WRAP_CONTENT);
//    //
//    // commentDialog.setOnCancelListener(new OnCancelListener() {
//    //
//    // @Override
//    // public void onCancel(DialogInterface dialog) {
//    //
//    // }
//    // });
//    // commentDialog.show();
//    // }
//
//    private class PurchaseTask extends AsyncTask<JSONObject, Void, JSONObject> {
//
//        @Override
//        protected void onPreExecute() {
//
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected JSONObject doInBackground(JSONObject... params) {
//
//            if (params[0] != null) {
//
//                try {
//
//                    JSONObject response = Communication
//                            .sportsLotteyBuy(params[0]);
//                    System.out.println("Purchase " + response);
//                    return response;
//
//                } catch (Exception e) {
//                    return null;
//                }
//            } else {
//                return null;
//
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject result) {
//            super.onPostExecute(result);
//
//            SportsTicketBean sportsTicketBean = null;
//            if (result != null) {
//                if (result.has("isSuccess")) {
//
//                    Gson gson = new Gson();
//                    sportsTicketBean = gson.fromJson(result.toString(),
//                            SportsTicketBean.class);
//
//                    if (result.has("isSuccess")) {
//                        Intent intent = new Intent(SportsActivity.this,
//                                SportsTicketActivity.class);
//                        overridePendingTransition(GlobalVariables.startAmin,
//                                GlobalVariables.endAmin);
//                        intent.putExtra("trackTicket", false);
//                        intent.putExtra("sportsTicketBean", sportsTicketBean);
//                        startActivity(intent);
//                        progressDialog.dismiss();
//                        SportsActivity.this.finish();
//
//                    } else {
//                        try {
//                            if (drawData.getInt("errorCode") == 118) {
//                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                        SportsActivity.this);
//                                alertDialog
//                                        .setIcon(android.R.drawable.ic_dialog_alert);
//                                alertDialog.setCancelable(false);
//                                alertDialog
//                                        .setMessage("Session Out Login Again");
//                                alertDialog.setPositiveButton("OK",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(
//                                                    DialogInterface dialog,
//                                                    int which) {
//                                                // startActivity(new
//                                                // Intent(MainMenuActivity.this,
//                                                // LoginActivity.class));
//                                                SportsActivity.this.finish();
//                                                DataSource.Login.isSessionValid = false;
//                                            }
//                                        });
//                                alertDialog.show();
//                            } else {
//
//                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                        SportsActivity.this);
//                                alertDialog
//                                        .setIcon(android.R.drawable.ic_dialog_alert);
//                                alertDialog.setCancelable(false);
//                                alertDialog.setMessage(drawData
//                                        .getString("errorMsg"));
//                                alertDialog.setPositiveButton("OK", null);
//                                alertDialog.show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            // AlertDialog.Builder alertDialog = new
//                            // AlertDialog.Builder(
//                            // SportsActivity.this);
//                            // alertDialog
//                            // .setIcon(android.R.drawable.ic_dialog_alert);
//                            // alertDialog.setCancelable(false);
//                            // alertDialog
//                            // .setMessage("Internal Error !, Try Later");
//                            // alertDialog.setPositiveButton("OK", null);
//                            // alertDialog.show();
//                        }
//                    }
//                } else {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                            SportsActivity.this);
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                    alertDialog.setCancelable(false);
//                    alertDialog.setMessage(R.string.net_error);
//                    alertDialog.setPositiveButton("OK", null);
//                    alertDialog.show();
//                }
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                        SportsActivity.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage(R.string.net_error);
//                alertDialog.setPositiveButton("OK", null);
//                alertDialog.show();
//            }
//
//        }
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(GlobalVariables.startAmin,
//                GlobalVariables.endAmin);
//    }
//}

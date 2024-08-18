package com.skilrock.sportslottery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.bean.SportsBean;
import com.skilrock.bean.SportsTicketBean;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.SimpleDialogBox;
import com.skilrock.lms.communication.SLETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.viewpager.SliderLayout;
import com.viewpager.indicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SportsLotteryActivity extends DrawerBaseActivity implements WebServicesListener, View.OnLongClickListener, View.OnTouchListener, View.OnClickListener {

    private static int gameId;
    private double tktMaxAmount;
    private double unitPrice;
    private SliderLayout pager;
    private TitlePageIndicator indicator;
    private Button btnProceed;
    private TextView txtLineNo;
    public static TextView txtBetAmount;
    private TextView txtTicketAmount;
    private TextView txtInfo;
    private ImageView imgBetDec;
    private ImageView imgBetInc;
    private RelativeLayout rlContainer;
    private FrameLayout flContainer;
    private LinearLayout rlDrawNotAvail;
    private TextView txtNextDrawDate;
    private TextView txtNextDrawTime;
    private boolean pd;
    private StringBuffer buffer;
    private JSONObject drawInfos, finalJson;
    private JSONArray eventData, drawInfo;
    private SportsBean.GameTypeData gameTypeData;
    private SportsBean bean;

    private double maxPanelAmount;
    private ArrayList<SportsBean.EventData> dummyData;
    private ArrayList<SportsBean.EventData> adapterData;
    private SportsLotteryAdapter adapter;

    private boolean isIncremented = false;
    private boolean isDecremented = false;

    private Handler onLongPressCounterHandler;
    private JSONObject drawData;

    public static DecimalFormat decimalFormat = new DecimalFormat("######.##");

    private ProgressDialog progressDialog;
    private boolean isDrawAvailable;

    private String[] eventTypes;
    private int position;
    private int gameTypeId = -1;
    private int adapterRes;
    private DownloadDialogBox downloadDialogBox;
    private String gameType;
    private boolean isEnable;
    private Map<String, Integer> gameTypesMap;
    private ArrayList<String> gameTypes;

    public static final String GAME_TYPE_ID = "game_type_id";
    private Analytics analytics;
    private boolean isFreezed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_lottery_activity);

        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER_PURCHASE);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        sHeader();
        setDrawerItems();
        bindIds();
        addListeners();
        bean = (SportsBean) getIntent().getSerializableExtra(
                "bean");
        pager.setPageMargin(30);
        pager.setPresetTransformer(SliderLayout.Transformer.Tablet);
        init();

        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long arg3) {
                view.setBackgroundColor(Color.TRANSPARENT);
                position = pos;
                gameType = gameTypes.get(pos);
                init(gameTypesMap.get(gameType));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void init() {
        gameId = bean.getSleData().getGameData().get(0).getGameId();
        tktMaxAmount = bean.getSleData().getGameData().get(0).getTktMaxAmt();
        gameTypesMap = getGameType();
        gameTypes = new ArrayList(gameTypesMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_row,
                R.id.spinner_text, gameTypes);
        headerSpinner.setAdapter(adapter);

        if (getIntent().hasExtra(GAME_TYPE_ID)) {
            //init(getIntent().getIntExtra(GAME_TYPE_ID, -1));
            for (int i = 0; i < gameTypesMap.size(); i++) {
                if (getIntent().getIntExtra(GAME_TYPE_ID, -1) == bean.getSleData().getGameData().get(0).getGameTypeData().get(i).getGameTypeId()) {
                    //gameTypeData = bean.getSleData().getGameData().get(0).getGameTypeData().get(i);
                    headerSpinner.setSelection(i);
                    break;
                }
            }
            return;
        }
        if (isFreezed) {
            if (bean.getSleData().getGameData().get(0).getGameTypeData().get(position).getDrawData().size() != 0)
                headerSpinner.setSelection(position);
            else
                setInitialGame();
            isFreezed = false;
        } else
            setInitialGame();
    }

    private void setInitialGame() {
        if (gameTypesMap.size() != 0) {
            for (int i = 0; i < gameTypesMap.size(); i++) {
                if (bean.getSleData().getGameData().get(0).getGameTypeData().get(i).getDrawData().size() != 0) {
                    headerSpinner.setSelection(i);
                    //init(i);
                    //gameType = gameTypes.get(i);
                    break;
                } else if (i == gameTypesMap.size() - 1) {
                    headerSpinner.setSelection(0);
                    //init(0);
                    //gameType = gameTypes.get(0);
                }
            }
        } else {
            isGameAvailable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setGameName(getIntent().getStringExtra("gameHeaderName"));
        manageHeader();
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle bundle) {
        AlertDialog dialogDetails = null;
        LayoutInflater inflater = null;
        View dialogView = null;
        AlertDialog.Builder dialogbuilder = null;

        switch (id) {
            case 1:
                inflater = LayoutInflater.from(this);

                dialogView = inflater.inflate(
                        R.layout.sports_lottery_dialog_layout_preview, null);

                dialogbuilder = new AlertDialog.Builder(this);
                dialogbuilder.setTitle(getResources().getString(R.string.purchase_conf_sl));

                dialogbuilder.setView(dialogView);
                // dialogDetails.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogDetails = dialogbuilder.create();

                break;
        }
        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
        final AlertDialog alertDialog = (AlertDialog) dialog;
        // final Bundle bundle1 = bundle;
        Button ok = null;
        Button cancel = null;
        TextView numbersOfLines = null;
        TextView betAmountm = null;

        TextView totalAmount = null;
        switch (id) {

            case 1:
                ok = (Button) alertDialog.findViewById(R.id.btn_ok);
                cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
                numbersOfLines = (TextView) alertDialog
                        .findViewById(R.id.no_of_lines);
                betAmountm = (TextView) alertDialog
                        .findViewById(R.id.betamount_mul);
                totalAmount = (TextView) alertDialog
                        .findViewById(R.id.total_amount);

                if (txtLineNo != null && txtTicketAmount != null && txtBetAmount != null) {

                    numbersOfLines.setText(txtLineNo.getText().toString());
                    betAmountm.setText(txtBetAmount.getText().toString());

                    totalAmount.setText(txtTicketAmount.getText().toString());

                }

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (finalJson != null) {
                            alertDialog.dismiss();

                            executeTask();

                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    SportsLotteryActivity.this);
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage(getResources().getString(R.string.int_error_sl));
                            alertDialog.setPositiveButton("OK", null);
                            alertDialog.show();

                        }

                        // Intent intent = new Intent(SportsLotteryActivity.this,
                        // SportsTicketActivity.class);
                        // intent.putExtra("trackTicket", false);
                        //
                        // startActivity(intent);
                        // SportsLotteryActivity.this.finish();

                    }

                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                break;

        }
    }

    private Map<String, Integer> getGameType() {
        Map<String, Integer> gameTypes = new LinkedHashMap<>();
        List<SportsBean.GameTypeData> data = bean.getSleData().getGameData().get(0).getGameTypeData();
        for (int i = 0; i < data.size(); i++) {
            gameTypes.put(data.get(i).getGameTypeDisplayName(), data.get(i).getGameTypeId());
        }
        return gameTypes;
    }

    protected void bindIds() {
        //super.bindIds();
        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        pager = (SliderLayout) findViewById(R.id.pager);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        txtLineNo = (TextView) findViewById(R.id.txt_line_no);
        txtBetAmount = (TextView) findViewById(R.id.txt_bet_amount);
        txtTicketAmount = (TextView) findViewById(R.id.txt_ticket_amount);
        imgBetDec = (ImageView) findViewById(R.id.img_bet_dec);
        imgBetInc = (ImageView) findViewById(R.id.img_bet_inc);
        rlDrawNotAvail = (LinearLayout) findViewById(R.id.rl_draw_not_avail);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        flContainer = (FrameLayout) findViewById(R.id.content_frame);
        txtNextDrawDate = (TextView) findViewById(R.id.txt_date);
        txtNextDrawTime = (TextView) findViewById(R.id.txt_time);
        txtInfo = (TextView) findViewById(R.id.txt_info);
    }

    protected void addListeners() {
        //super.addListeners();
        imgBetDec.setOnClickListener(this);
        imgBetInc.setOnClickListener(this);
        btnProceed.setOnClickListener(listener);

        imgBetDec.setOnLongClickListener(this);
        imgBetInc.setOnLongClickListener(this);
        imgBetDec.setOnTouchListener(this);
        imgBetInc.setOnTouchListener(this);

        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
//        headerSpinner.setVisibility(View.VISIBLE);
        headerSpinner.setVisibility(gameTypes.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
    }

    private void init(int gameTypeId) {
        for (int i = 0; i < gameTypesMap.size(); i++) {
            if (gameTypeId == bean.getSleData().getGameData().get(0).getGameTypeData().get(i).getGameTypeId()) {
                gameTypeData = bean.getSleData().getGameData().get(0).getGameTypeData().get(i);
                break;
            }
        }
        headerText.setText(gameTypeData.getGameTypeDisplayName());
        if (gameTypeData.getDrawData().size() == 0) {
            isDrawAvailable(false);
            if (gameTypeData.getUpcomingDrawStartTime() != null && !gameTypeData.getUpcomingDrawStartTime().equals("")) {
                String[] date = gameTypeData.getUpcomingDrawStartTime().split(" ");
                txtNextDrawDate.setText(date[0]);
                txtNextDrawTime.setText(date[1].split(":")[0] + ":" + date[1].split(":")[1]);
                if (gameTypeData.isAreEventsMappedForUpcomingDraw())
                    //date = gameTypeData.getUpcomingDrawStartTime().split(":");
                    matchNotAvailDialog(gameTypeData.getUpcomingDrawStartTime(), true);
            }
            return;
        }

        onLongPressCounterHandler = new Handler();
        // btnProceed.setEnabled(false);
        // btnProceed.setTextColor(getResources().getColor(R.color.five_play_disable_now_txt_color));
        txtLineNo.setText("0");
        txtBetAmount.setText(gameTypeData.getGameTypeUnitPrice() + "");
        txtTicketAmount.setText(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE) + " 0.00");
        gameId = bean.getSleData().getGameData().get(0).getGameId();
        maxPanelAmount = gameTypeData.getGameTypeMaxBetAmtMultiple();
        unitPrice = gameTypeData.getGameTypeUnitPrice();
        pager.setAdapter(new PurchasePagerAdapter(getSupportFragmentManager(), gameTypeData, tktMaxAmount / unitPrice));
        indicator.setViewPager(pager);
    }

    private void matchNotAvailDialog(String time, boolean isMapped) {
        View.OnClickListener checkMatches = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariables.connectivityExists(SportsLotteryActivity.this)) {
                    String url = VariableStorage.GlobalPref.getStringData(SportsLotteryActivity.this, VariableStorage.GlobalPref.SLE_ROOT_URL) + "/rest/dataMgmt/fetchSLEMatchListData";
                    String json = "{" +
                            "\"merchantCode\":\"" + VariableStorage.GlobalPref.getStringData(SportsLotteryActivity.this, VariableStorage.GlobalPref.SLE_MER_CODE) + "\"," +
                            "\"listType\":\"drawWise\"" +
                            "}";
                    new SLETask(SportsLotteryActivity.this, "MATCH", null, url, json).execute();
                } else {
                    GlobalVariables.showDataAlert(SportsLotteryActivity.this);
                }
            }
        };

        new DownloadDialogBox(SportsLotteryActivity.this, "Next match is on " + time, "Match not available !", true, isMapped, checkMatches, null, "Match List", "Cancel").show();
    }

    public void isPurchaseEnable(boolean isEnable) {
//        btnProceed.setEnabled(isEnable);
//        btnProceed.setTextColor(color);
        this.isEnable = isEnable;
    }

    public void setTicketAmount(String amount) {
        txtTicketAmount.setText(amount);
    }

    public void setLineNo(String lineNo) {
        txtLineNo.setText(lineNo);
    }

    public void setEventTypes(String[] eventTypes) {
        this.eventTypes = eventTypes;
    }

    @Override
    public void onClick(View v) {
        // super.onClick(v);
        switch (v.getId()) {
            case R.id.img_bet_dec:
                double value1 = Double.parseDouble(txtBetAmount.getText().toString());
                if (value1 > gameTypeData.getGameTypeUnitPrice()) {
                    value1 -= gameTypeData.getGameTypeUnitPrice();
                    txtBetAmount.setText(String.valueOf(value1));
                    updateFinalAmt(value1 * Integer.parseInt(txtLineNo.getText().toString()));
                } else {
                    value1 = maxPanelAmount + gameTypeData.getGameTypeUnitPrice();
                    value1 -= gameTypeData.getGameTypeUnitPrice();
                    txtBetAmount.setText(String.valueOf(value1));
                    updateFinalAmt(value1 * Integer.parseInt(txtLineNo.getText().toString()));
                }
                break;
            case R.id.img_bet_inc:
                double value = Double.parseDouble(txtBetAmount.getText().toString());
                // sum = 0;
                // for (int i = 0; i < DataSource.Fortune.fortunePanels.length; i++)
                // {
                // sum += DataSource.Fortune.fortunePanels[i];
                // }
                if (value >= maxPanelAmount) {
                    value = 0;
                    value += gameTypeData.getGameTypeUnitPrice();
                    txtBetAmount.setText(String.valueOf(value));
                    updateFinalAmt(value * Integer.parseInt(txtLineNo.getText().toString()));
                    // Utils.Toast(getApplicationContext(),
                    // "your max panel is seleted!", 1000);
                } else {
                    if (value < maxPanelAmount && value > -1) {
                        value += gameTypeData.getGameTypeUnitPrice();
                        txtBetAmount.setText(String.valueOf(value));
                        updateFinalAmt(value * Integer.parseInt(txtLineNo.getText().toString()));
                    }
                }
                break;
//            case R.id.btn_proceed:
//                if (!isDrawAvailable)
//                    return;
//
//                if (!UserInfo.isLogin(this)) {
//                    startActivityForResult(new Intent(this,
//                            LoginActivity.class), 100);
//                    this.overridePendingTransition(GlobalVariables.startAmin,
//                            GlobalVariables.endAmin);
//                } else {
//                    if (!isEnable) {
//                        if (GlobalPref.getInstance(this).getCountry().equalsIgnoreCase("Ghana"))
//                            Utils.Toast(this, "Please select at least 1 option in each event");
//                        else
//                            Utils.Toast(this, "Please select at least 1 team in each event.");
//                        return;
//                    }
//
//                    double balance = Double.parseDouble(VariableStorage.UserPref.getStringData(SportsLotteryActivity.this, VariableStorage.UserPref.USER_BAL));
//                    double purchaseAmount = Double.parseDouble(txtTicketAmount.getText().toString().replace(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim());
//                    if (balance < purchaseAmount) {
//                        Utils.Toast(SportsLotteryActivity.this, "Available balance is low");
//                    } else {
//                        onPurchase();
//                    }
//                }
//                break;
        }
    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(1000) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.btn_proceed:
                    if (!isDrawAvailable)
                        return;
                    if (!UserInfo.isLogin(SportsLotteryActivity.this)) {
                        startActivityForResult(new Intent(SportsLotteryActivity.this,
                                LoginActivity.class), 100);
                        SportsLotteryActivity.this.overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    } else {
                        if (!isEnable) {
                            if (GlobalPref.getInstance(SportsLotteryActivity.this).getCountry().equalsIgnoreCase("Ghana"))
                                Utils.Toast(SportsLotteryActivity.this, "Please select at least 1 option in each event");
                            else
                                Utils.Toast(SportsLotteryActivity.this, "Please select at least 1 team in each event.");
                            return;
                        }

                        double balance = Double.parseDouble(VariableStorage.UserPref.getStringData(SportsLotteryActivity.this, VariableStorage.UserPref.USER_BAL));
                        double purchaseAmount = Double.parseDouble(txtTicketAmount.getText().toString().replace(VariableStorage.GlobalPref.getStringData(SportsLotteryActivity.this, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim());
                        if (balance < purchaseAmount) {
                            Utils.Toast(SportsLotteryActivity.this, "Available balance is low");
                        } else {
                            onPurchase();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                double balance = Double.parseDouble(VariableStorage.UserPref.getStringData(SportsLotteryActivity.this, VariableStorage.UserPref.USER_BAL));
                double purchaseAmount = Double.parseDouble(txtTicketAmount.getText().toString().replace(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim());
                if (balance < purchaseAmount) {
                    Utils.Toast(SportsLotteryActivity.this, "Available balance is low");
                } else {
                    onPurchase();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int id = v.getId();
        if (id == R.id.img_bet_inc) {
            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL) && isIncremented) {
                isIncremented = false;
            }
        } else if (id == R.id.img_bet_dec) {
            if ((event.getAction() == MotionEvent.ACTION_UP || event
                    .getAction() == MotionEvent.ACTION_CANCEL) && isDecremented) {
                isDecremented = false;
            }
        }

        return false;
    }

    private void executeTask() {
        if (finalJson != null) {
            //   new PurchaseTask().execute(finalJson);
            String url = VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.SLE_ROOT_URL);
            SLETask webTask = new SLETask(this, "PURCHASE", url + "/rest/playMgmt/purchaseTicket", finalJson.toString());
            webTask.execute();
        }
    }

    private void updateFinalAmt(double amt) {
        PurchasePagerAdapter adapter = (PurchasePagerAdapter) pager.getAdapter();
        SportsLotteryPurchaseFragment fragment = (SportsLotteryPurchaseFragment) adapter.getFragment(pager.getCurrentItem());
        fragment.setBetMultiple(amt / Integer.parseInt(txtLineNo.getText().toString()));
        fragment.setTicketAmount(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE) + " " + AmountFormat.getCurrentAmountFormatForMobile(amt));
    }

    public void updateBetMultipleAmt(double amt) {
        txtBetAmount.setText(amt + "");
    }

    public void isDrawAvailable(boolean isAvailable) {
        isDrawAvailable = isAvailable;
        if (isAvailable) {
            rlContainer.setVisibility(View.VISIBLE);
            rlDrawNotAvail.setVisibility(View.GONE);
        } else {
            rlContainer.setVisibility(View.GONE);
            rlDrawNotAvail.setVisibility(View.VISIBLE);
        }
    }

    public void isGameAvailable(boolean isAvailable) {
        if (isAvailable) {
            flContainer.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.GONE);
        } else {
            flContainer.setVisibility(View.GONE);
            txtInfo.setVisibility(View.VISIBLE);
        }
    }

    private void onPurchase() {
        if (GlobalVariables.connectivityExists(SportsLotteryActivity.this)) {
            PurchasePagerAdapter adapter = (PurchasePagerAdapter) pager.getAdapter();
            SportsLotteryPurchaseFragment fragment = (SportsLotteryPurchaseFragment) adapter.getFragment(pager.getCurrentItem());
            adapterData = fragment.getAdapterData();
            adapterRes = fragment.getAdapterRes();
            drawInfos = new JSONObject();
            eventData = new JSONArray();
            drawInfo = new JSONArray();
            finalJson = new JSONObject();
            buffer = new StringBuffer();

            for (int i = 0; i < adapterData.size(); i++) {
                if (adapterRes == R.layout.sports_row_item_five
                        && !adapterData.get(i).isMinusTwoSelected()
                        && !adapterData.get(i).isMinusOneSelected()
                        && !adapterData.get(i).isDrawSelected()
                        && !adapterData.get(i).isPlusOneSelected()
                        && !adapterData.get(i).isPlusTwoSelected()) {
                    pd = false;
                    break;
                } else if (adapterRes == R.layout.sports_row_item_three
                        && !adapterData.get(i).isHomeSelected()
                        && !adapterData.get(i).isDrawSelected()
                        && !adapterData.get(i).isAwaySelected()) {
                    pd = false;
                    break;
                } else {
                    pd = true;
                    JSONObject eventDatas = new JSONObject();
                    try {
                        eventDatas.put("eventId", adapterData.get(i)
                                .getEventId());
                    } catch (Exception e) {
                    }

                    if (adapterRes == R.layout.sports_row_item_five) {
                        if (adapterData.get(i).isMinusTwoSelected())
                            populateEventData(eventDatas, eventTypes[0].trim());
                        if (adapterData.get(i).isMinusOneSelected())
                            populateEventData(eventDatas, eventTypes[1].trim());
                        if (adapterData.get(i).isDrawSelected())
                            populateEventData(eventDatas, eventTypes[2].trim());
                        if (adapterData.get(i).isPlusOneSelected())
                            populateEventData(eventDatas, eventTypes[3].trim());
                        if (adapterData.get(i).isPlusTwoSelected())
                            populateEventData(eventDatas, eventTypes[4].trim());
                    } else if (adapterRes == R.layout.sports_row_item_three) {
                        if (adapterData.get(i).isHomeSelected())
                            populateEventData(eventDatas, eventTypes[0].trim());
                        if (adapterData.get(i).isDrawSelected())
                            populateEventData(eventDatas, eventTypes[1].trim());
                        if (adapterData.get(i).isAwaySelected())
                            populateEventData(eventDatas, eventTypes[2].trim());
                    }

                    eventData.put(eventDatas);
                }
            }
            if (pd) {
                try {
                    drawInfos.put("drawId", gameTypeData.getDrawData().get(pager.getCurrentItem())
                            .getDrawId()/*1111111*/);
                    drawInfos.put("betAmtMul", (int) (Double.parseDouble(txtBetAmount
                            .getText().toString()) / gameTypeData.getGameTypeUnitPrice()));
                    drawInfos.put("eventData", eventData);
                    drawInfo.put(drawInfos);
                    finalJson.put("gameId", SportsLotteryActivity.gameId);
                    finalJson.put("gameTypeId", gameTypeData.gameTypeId);
                    finalJson.put("noOfBoard", 1);
                    finalJson.put("playerName", VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.USER_NAME));
                    finalJson.put("merchantCode",
                            VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.SLE_MER_CODE));
                    // finalJson.put("merchantPwd",
                    // DataSource.Login.merchantPwd);
                    finalJson.put("sessionId", VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.SESSION_ID));
                    finalJson.put("drawInfo", drawInfo);

                    Utils.logPrint("finalJson:" + finalJson + "");

                    // showMessageAlertDialog();

                    if (Double.parseDouble(txtTicketAmount.getText().toString()
                            .replace(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE), "")
                            .trim()) != 0) {
                        if (Double.parseDouble(txtTicketAmount
                                .getText()
                                .toString()
                                .replace(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.CURRENCY_CODE),
                                        "").trim()) > Double.parseDouble(VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.USER_BAL))) {
                            Utils.Toast(SportsLotteryActivity.this,
                                    getResources().getString(R.string.bal_insuff_sl)
                            );
                        } else {
                            //showDialog(1);
                            String message = "Are you sure you want to purchase?";

                            dialog = new SimpleDialogBox(SportsLotteryActivity.this, message, getResources().getString(R.string.purch_conf_sl), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    analytics.sendAll(Fields.Category.SL_PURCHASE, Fields.Action.DROPDOWN, "Game : " + gameType);
                                    executeTask();
                                    dialog.dismiss();
                                }
                            });
                            dialog.showAlertDialog();

                        }
                    } else {
                        Utils.Toast(SportsLotteryActivity.this,
                                getResources().getString(R.string.play_amt_insuff_sl)
                        );
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.Toast(SportsLotteryActivity.this,
                        getResources().getString(R.string.sel_atlest_one_sl));
            }
        } else {
            GlobalVariables.showDataAlert(SportsLotteryActivity.this);
        }
    }

    SimpleDialogBox dialog;

    private void populateEventData(JSONObject eventDatas, String selectedEvent) {
        try {
            if (eventDatas.has("eventSelected")) {
                String temp = eventDatas
                        .getString("eventSelected");
                eventDatas
                        .put("eventSelected", temp + "," + selectedEvent);
            } else {
                eventDatas.put("eventSelected", selectedEvent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.img_bet_dec:
                isDecremented = true;
                onLongPressCounterHandler
                        .postDelayed(new CounterValueIncDec(), 100);
                break;
            case R.id.img_bet_inc:
                isIncremented = true;
                onLongPressCounterHandler
                        .postDelayed(new CounterValueIncDec(), 100);
                break;
        }
        return false;
    }

    //String data = "{\"responseCode\":10029,\"sleData\":{\"gameData\":[{\"maxEventCount\":1,\"gameDisplayName\":\"Soccer\",\"gameId\":1,\"tktMaxAmt\":5000,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":100,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer13\",\"gameTypeId\":1,\"drawData\":[],\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 13\",\"upcomingDrawStartTime\":\"\",\"areEventsMappedForUpcomingDraw\":false,\"gameTypeUnitPrice\":1,\"gameTypeMaxBetAmtMultiple\":600,\"eventSelectionType\":\"MULTIPLE\"},{\"gameTypeDevName\":\"soccer10\",\"gameTypeId\":2,\"drawData\":[{\"drawId\":70,\"drawDateTime\":\"01-11-2015 06:00:00\",\"drawNumber\":62,\"drawDisplayString\":\"SUNDAY-S10\",\"eventData\":[{\"eventLeague\":\"GERMANY BUNDESLIGA 2\",\"eventVenue\":\"Al ahly\",\"favTeam\":\"HOME\",\"eventDate\":\"31-10-2015 14:05:00\",\"eventDisplayHome\":\"RB Leipzig\",\"eventCodeAway\":\"ST.\",\"awayTeamOdds\":\"2\",\"eventId\":360,\"homeTeamOdds\":\"0\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"St. Pauli\",\"eventCodeHome\":\"RB \"},{\"eventLeague\":\"GERMANY BUNDESLIGA 2\",\"eventVenue\":\"Al ahly benghazi\",\"favTeam\":\"HOME\",\"eventDate\":\"31-10-2015 16:27:00\",\"eventDisplayHome\":\"Paderborn\",\"eventCodeAway\":\"UNI\",\"awayTeamOdds\":\"3\",\"eventId\":361,\"homeTeamOdds\":\"1\",\"drawOdds\":\"2\",\"eventDisplayAway\":\"Union Berlin\",\"eventCodeHome\":\"PADB\"},{\"eventLeague\":\"GERMANY BUNDESLIGA 2\",\"eventVenue\":\"Al Ahli Wad Medani\",\"favTeam\":\"HOME\",\"eventDate\":\"31-10-2015 16:27:00\",\"eventDisplayHome\":\"Karlsruher SC\",\"eventCodeAway\":\"FOR\",\"awayTeamOdds\":\"4\",\"eventId\":362,\"homeTeamOdds\":\"2\",\"drawOdds\":\"3\",\"eventDisplayAway\":\"Fortuna Dusseldorf\",\"eventCodeHome\":\"KAR\"},{\"eventLeague\":\"German Bundesliga\",\"eventVenue\":\"Aberdeen\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:28:00\",\"eventDisplayHome\":\"SC Paderborn 07\",\"eventCodeAway\":\"WDBM\",\"awayTeamOdds\":\"1\",\"eventId\":363,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Werder Bremen\",\"eventCodeHome\":\"SCPA\"},{\"eventLeague\":\"German Bundesliga\",\"eventVenue\":\"Al Hilal Omdurman\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:28:00\",\"eventDisplayHome\":\"SC Paderborn 07\",\"eventCodeAway\":\"BAL\",\"awayTeamOdds\":\"1\",\"eventId\":364,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Bayer Leverkusen\",\"eventCodeHome\":\"SCPA\"},{\"eventLeague\":\"South Africa\",\"eventVenue\":\"Al Ahly Shendi\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:29:00\",\"eventDisplayHome\":\"Ajax Cape Town\",\"eventCodeAway\":\"BDW\",\"awayTeamOdds\":\"1\",\"eventId\":365,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Bidvest Wits\",\"eventCodeHome\":\"ACT\"},{\"eventLeague\":\"South Africa\",\"eventVenue\":\"Al Ahly Shendi\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:29:00\",\"eventDisplayHome\":\"Bidvest Wits\",\"eventCodeAway\":\"BLA\",\"awayTeamOdds\":\"1\",\"eventId\":366,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Black Aces\",\"eventCodeHome\":\"BDW\"},{\"eventLeague\":\"South Africa\",\"eventVenue\":\"Al Hilal Omdurman\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:29:00\",\"eventDisplayHome\":\"Maritzburg Utd\",\"eventCodeAway\":\"KAC\",\"awayTeamOdds\":\"1\",\"eventId\":368,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Kaizer Chiefs\",\"eventCodeHome\":\"MBU\"},{\"eventLeague\":\"South Africa\",\"eventVenue\":\"Al ahly benghazi\",\"favTeam\":\"\",\"eventDate\":\"31-10-2015 16:30:00\",\"eventDisplayHome\":\"Maritzburg Utd\",\"eventCodeAway\":\"GDA\",\"awayTeamOdds\":\"1\",\"eventId\":369,\"homeTeamOdds\":\"1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Golden Arrows\",\"eventCodeHome\":\"MBU\"},{\"eventLeague\":\"CAF Champions League\",\"eventVenue\":\"Al ahly benghazi\",\"favTeam\":\"HOME\",\"eventDate\":\"31-10-2015 16:33:00\",\"eventDisplayHome\":\"MC El Eulma\",\"eventCodeAway\":\"AL-\",\"awayTeamOdds\":\"1\",\"eventId\":370,\"homeTeamOdds\":\"0.1\",\"drawOdds\":\"1\",\"eventDisplayAway\":\"Al-Merrikh\",\"eventCodeHome\":\"MC \"}],\"ftg\":\"31-10-2015 13:00:00\"}],\"eventType\":\"[H, D, A]\",\"gameTypeDisplayName\":\"Soccer 10\",\"upcomingDrawStartTime\":\"\",\"areEventsMappedForUpcomingDraw\":false,\"gameTypeUnitPrice\":1,\"gameTypeMaxBetAmtMultiple\":600,\"eventSelectionType\":\"MULTIPLE\"},{\"gameTypeDevName\":\"soccer6\",\"gameTypeId\":3,\"drawData\":[],\"eventType\":\"[H+, H, D, A, A+]\",\"gameTypeDisplayName\":\"Soccer 6\",\"upcomingDrawStartTime\":\"31-10-2015 13:30:00\",\"areEventsMappedForUpcomingDraw\":false,\"gameTypeUnitPrice\":0.5,\"gameTypeMaxBetAmtMultiple\":600,\"eventSelectionType\":\"MULTIPLE\"},{\"gameTypeDevName\":\"soccer4\",\"gameTypeId\":4,\"drawData\":[],\"eventType\":\"\",\"gameTypeDisplayName\":\"Soccer 4\",\"upcomingDrawStartTime\":\"31-10-2015 13:30:00\",\"areEventsMappedForUpcomingDraw\":false,\"gameTypeUnitPrice\":0.5,\"gameTypeMaxBetAmtMultiple\":600,\"eventSelectionType\":\"MULTIPLE\"}]}]},\"responseMsg\":\"SUCCESS\"}";
    DownloadDialogBox dialogBox = null;

    @Override
    public void onResult(String methodType, final Object resultData, final Dialog dialog) {
        SportsTicketBean sportsTicketBean = null;
        Intent intent;
        if (resultData != null) {
            Gson gson = new Gson();
            switch (methodType) {
                case "MATCH":
                    try {
                        SportsLotteryMatchBean sportsLotteryMatchBean;
                        JSONObject object = (JSONObject) resultData;
                        if (object.getInt("responseCode") == 0) {
                            intent = new Intent(this, SportsLotteryMatchActivity.class);
                            sportsLotteryMatchBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsLotteryMatchBean.class);
                            intent.putExtra(SportsLotteryMatchActivity.SPORTS_LOTTERY_MATCH_LIST, sportsLotteryMatchBean);
                            intent.putExtra(SportsLotteryMatchActivity.SPORTS_LOTTERY_MATCH_LIST_POSITION, position);
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            sportsLotteryMatchBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsLotteryMatchBean.class);
                            downloadDialogBox = new DownloadDialogBox(this, sportsLotteryMatchBean.getResponseMsg(), "", false, true, null, null);
                            downloadDialogBox.show();
                        }
                    } catch (Exception e) {
                        dialog.dismiss();
                        GlobalVariables.showServerErr(SportsLotteryActivity.this);
                    }
                    break;
                default:
                    try {
                        JSONObject object = new JSONObject(resultData.toString());
                        if (object.getInt("responseCode") == 10029) {
                            View.OnClickListener cancel = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                    dialogBox.dismiss();
                                }
                            };
                            View.OnClickListener updateDraw = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    bean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsBean.class);
                                    isFreezed = true;
                                    init();
                                    dialogBox.dismiss();
                                }
                            };
                            dialogBox = new DownloadDialogBox(SportsLotteryActivity.this, object.getString("responseMsg"), "Draw Freeze !", true, true, updateDraw, cancel, "Update Draw", "Cancel");
                            dialogBox.show();
                            dialog.dismiss();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }

                    sportsTicketBean = gson.fromJson(resultData.toString(),
                            SportsTicketBean.class);
                    if (sportsTicketBean != null) {
                        if (sportsTicketBean.getResponseCode() == 0) {
                            analytics.sendAll(Fields.Category.SL_PURCHASE, Fields.Action.GET, Fields.Label.SUCCESS);
                            VariableStorage.UserPref.setStringPreferences(SportsLotteryActivity.this, VariableStorage.UserPref.USER_BAL, sportsTicketBean.getTktData().getBalance());

                            intent = new Intent(SportsLotteryActivity.this,
                                    SportsLotteryTicketActivity.class);
                            overridePendingTransition(GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            intent.putExtra("trackTicket", false);
                            intent.putExtra("sportsTicketBean", sportsTicketBean);
                            intent.putExtra("printBean", resultData.toString());
                            startActivity(intent);
                            dialog.dismiss();
                            SportsLotteryActivity.this.finish();
                        } else {
                            try {
                                if (sportsTicketBean.getResponseCode() == 10012 || sportsTicketBean.getResponseCode() == 118) {
                                    View.OnClickListener okClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserInfo.setLogout(getApplicationContext());
                                            Intent intent = new Intent(SportsLotteryActivity.this,
                                                    LoginActivity.class);
                                            startActivityForResult(intent, 100);
                                            downloadDialogBox.dismiss();
                                        }
                                    };
                                    dialog.dismiss();
                                    downloadDialogBox = new DownloadDialogBox(this, sportsTicketBean.getResponseMsg(), "", false, true, okClickListener, null);
                                    downloadDialogBox.show();

                                } else if (sportsTicketBean.getResponseCode() == 10003) {
                                    dialog.dismiss();
                                    //String[] date = sportsTicketBean.getUpcomingDrawStartTime().split(":");
                                    matchNotAvailDialog(sportsTicketBean.getUpcomingDrawStartTime(), sportsTicketBean.isAreEventsMappedForUpcomingDraw());
                                } else {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                            SportsLotteryActivity.this);
                                    alertDialog
                                            .setIcon(android.R.drawable.ic_dialog_alert);
                                    alertDialog.setCancelable(false);
                                    alertDialog.setMessage(sportsTicketBean.getResponseMsg());
                                    alertDialog.setPositiveButton("Ok", null);
                                    dialog.dismiss();
                                    alertDialog.show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                dialog.dismiss();
                                GlobalVariables.showServerErr(SportsLotteryActivity.this);
                            }
                            analytics.sendAll(Fields.Category.SL_PURCHASE, Fields.Action.GET, Fields.Label.FAILURE);
                        }
                    } else {
                        dialog.dismiss();
                        GlobalVariables.showServerErr(SportsLotteryActivity.this);
                        analytics.sendAll(Fields.Category.SL_PURCHASE, Fields.Action.GET, Fields.Label.FAILURE);
                    }
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(SportsLotteryActivity.this);
        }
    }

    class CounterValueIncDec implements Runnable {
        @Override
        public void run() {
            if (isIncremented) {
                increment();
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            } else if (isDecremented) {
                decrement();
                onLongPressCounterHandler.postDelayed(new CounterValueIncDec(),
                        100);
            }
        }
    }

    public void decrement() {
        double value1 = Double.parseDouble(txtBetAmount.getText().toString());
        if (value1 > gameTypeData.getGameTypeUnitPrice()) {
            value1 -= gameTypeData.getGameTypeUnitPrice();
            txtBetAmount.setText(String.valueOf(value1));
            updateFinalAmt(value1 * Integer.parseInt(txtLineNo.getText().toString()));
        } else {
            value1 = maxPanelAmount + gameTypeData.getGameTypeUnitPrice();
            value1 -= gameTypeData.getGameTypeUnitPrice();
            txtBetAmount.setText(String.valueOf(value1));
            updateFinalAmt(value1 * Integer.parseInt(txtLineNo.getText().toString()));
        }
    }

    // Value Increment method
    public void increment() {

        double value = Double.parseDouble(txtBetAmount.getText().toString());
        // sum = 0;
        // for (int i = 0; i < DataSource.Fortune.fortunePanels.length; i++) {
        // sum += DataSource.Fortune.fortunePanels[i];
        // }
        if (value >= maxPanelAmount) {
            value = 0;
            value += gameTypeData.getGameTypeUnitPrice();
            txtBetAmount.setText(String.valueOf(value));
            updateFinalAmt(value * Integer.parseInt(txtLineNo.getText().toString()));
            // Utils.Toast(getApplicationContext(),
            // "your max panel is seleted!", 1000);
        } else {
            if (value < maxPanelAmount && value > -1) {
                value += gameTypeData.getGameTypeUnitPrice();
                txtBetAmount.setText(String.valueOf(value));
                updateFinalAmt(value * Integer.parseInt(txtLineNo.getText().toString()));
            }
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

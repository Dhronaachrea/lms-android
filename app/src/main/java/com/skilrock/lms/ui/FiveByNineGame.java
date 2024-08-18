package com.skilrock.lms.ui;//package com.skilrock.lottery;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.Toast;
//
//import com.skilrock.adapters.ButtonAdapter;
//import com.skilrock.bean.GamePlay;
//import com.skilrock.bean.KenoBean;
//import com.skilrock.bean.PanelData;
//import com.skilrock.customui.CustomTextView;
//import com.skilrock.lms.communication.Communication;
//import com.skilrock.utils.AmountFormat;
//import com.skilrock.utils.DataSource;
//
//public class FiveByNineGame extends Activity implements OnClickListener,
//        TextWatcher, Runnable {
//    private JSONObject data;
//
//    private CustomTextView username;
//    private CustomTextView balance;
//    private CustomTextView header;
//    private CustomTextView subHeader;
//    private GridView gridView;
//
//    private Button quickPick;
//    private Button ok;
//    private GamePlay gamePlay;
//    private EditText betAmount;
//    private EditText quickPickNumbers;
//    private CustomTextView noOfLines;
//    private CustomTextView totalAmount;
//    private String betString;
//    private String qpString;
//    private boolean isQuickPick;
//
//    final private static int DIALOG_QP = 0;
//    final private static int DIALOG_BET = 1;
//    final private static int PREVIEW = 2;
//    private String error;
//    protected int position;
//    protected ProgressDialog progressDialog;
//
//    private CustomTextView qpText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.zero_nine);
//        gamePlay = (GamePlay) getIntent().getExtras().getSerializable(
//                "gamePlay");
//        position = getIntent().getIntExtra("pos", 0);
//        gridView = (GridView) findViewById(R.id.grid_view);
//        quickPick = (Button) findViewById(R.id.quick_pick);
//        ok = (Button) findViewById(R.id.ok);
//        ok.setVisibility(Button.INVISIBLE);
//
//        DataSource.numbers = new int[90];
//
//        for (int i = 0; i < DataSource.numbers.length; i++)
//            DataSource.numbers[i] = 0;
//
//        String density = getDensity();
//        gridView.setAdapter(new ButtonAdapter(FiveByNineGame.this, gamePlay,
//                ok, density));
//
//        quickPick.setOnClickListener(this);
//        ok.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        username.setText(DataSource.Login.username);
//        balance.setText("$" + DataSource.Login.currentBalance);
//        header.setText(DataSource.gameName);
//        if (!DataSource.Login.isSessionValid)
//            FiveByNineGame.this.finish();
//        subHeader.setText(getPlayType(gamePlay.getBetName()));
//        // for(int i=0;i<DataSource.numbers.length;i++)
//        // DataSource.numbers[i] = 0;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.quick_pick) {
//            isQuickPick = true;
//            showDialog(DIALOG_QP);
//        } else if (id == R.id.ok) {
//            isQuickPick = false;
//            showDialog(DIALOG_BET);
//        }
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        AlertDialog dialogDetails = null;
//        LayoutInflater inflater = null;
//        View dialogView = null;
//        AlertDialog.Builder dialogbuilder = null;
//
//        switch (id) {
//            case DIALOG_QP:
//                inflater = LayoutInflater.from(this);
//                dialogView = inflater.inflate(R.layout.dialog_layout, null);
//
//                dialogbuilder = new AlertDialog.Builder(this);
//                dialogbuilder
//                        .setTitle(getPlayType(gamePlay.getBetName()) + " (QP)");
//
//                dialogbuilder.setView(dialogView);
//                dialogDetails = dialogbuilder.create();
//                break;
//            case DIALOG_BET:
//                inflater = LayoutInflater.from(this);
//                dialogView = inflater.inflate(R.layout.dialog_layout, null);
//
//                dialogbuilder = new AlertDialog.Builder(this);
//                dialogbuilder.setTitle(getPlayType(gamePlay.getBetName()));
//
//                dialogbuilder.setView(dialogView);
//                dialogDetails = dialogbuilder.create();
//                break;
//            case PREVIEW:
//                inflater = LayoutInflater.from(this);
//                dialogView = inflater.inflate(R.layout.preview, null);
//
//                dialogbuilder = new AlertDialog.Builder(this);
//                dialogbuilder.setCancelable(false);
//                dialogbuilder.setTitle("Ticket Preview");
//
//                dialogbuilder.setView(dialogView);
//                dialogDetails = dialogbuilder.create();
//                break;
//        }
//        return dialogDetails;
//    }
//
//    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        final AlertDialog alertDialog = (AlertDialog) dialog;
//        TableRow layout_one = null;
//        TableRow layout_two = null;
//        Button ok = null;
//        Button cancel = null;
//        CustomTextView numbersPicked = null;
//        CustomTextView drawSelected = null;
//        double unitPrice = -1;
//
//        switch (id) {
//            case PREVIEW:
//                TableLayout showpreview = null;
//                PanelData data = null;
//                CustomTextView totalamount = null;
//                CustomTextView totalamount1 = null;
//                int size;
//                totalamount1 = (CustomTextView) alertDialog
//                        .findViewById(R.id.txt_pre_tot);
//                totalamount1.setText("Total Amount ("
//                        + DataSource.Login.currenctSymbol + ")");
//                showpreview = (TableLayout) alertDialog
//                        .findViewById(R.id.panel_preview);
//                ok = (Button) alertDialog.findViewById(R.id.preview_ok);
//                cancel = (Button) alertDialog.findViewById(R.id.preview_cancel);
//                totalamount = (CustomTextView) alertDialog
//                        .findViewById(R.id.preview_totalamount);
//                totalamount.setText(String
//                        .valueOf(DataSource.Keno.totalPurchaseAmt));
//                size = DataSource.Keno.panelData.size();
//
//                final LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View panels[] = new View[size];
//                for (int i = 0; i < size; i++)
//                    panels[i] = inflator.inflate(R.layout.preview_row, null);
//
//                TableLayout previewData[] = new TableLayout[size];
//                for (int i = 0; i < size; i++)
//                    previewData[i] = (TableLayout) panels[i]
//                            .findViewById(R.id.preview_data);
//
//                CustomTextView bet_name[] = new CustomTextView[size];
//                CustomTextView nooflines[] = new CustomTextView[size];
//                CustomTextView panelamount[] = new CustomTextView[size];
//                CustomTextView pickednumbers[] = new CustomTextView[size];
//                CustomTextView numbers[] = new CustomTextView[size];
//
//                // System.out.println(size);
//
//                for (int i = 0; i < size; i++) {
//                    data = DataSource.Keno.panelData.get(i);
//                    bet_name[i] = (CustomTextView) previewData[i]
//                            .findViewById(R.id.bet_name);
//                    bet_name[i].setText(data.getPlayType());
//
//                    // nooflines[i]=
//                    // (CustomTextView)previewData[i].findViewById(R.id.no_of_lines);
//                    // nooflines[i].setText(String.valueOf(data.getNoOfLines()));
//
//                    panelamount[i] = (CustomTextView) previewData[i]
//                            .findViewById(R.id.panel_amount);
//                    panelamount[i].setText(String.valueOf(data.getPanelamount()));
//
//                    pickednumbers[i] = (CustomTextView) previewData[i]
//                            .findViewById(R.id.ispickednumbers);
//                    if (data.isQp())
//                        pickednumbers[i].setText("QP");
//                    else {
//                        pickednumbers[i].setText("Manual");
//                        numbers[i] = (CustomTextView) previewData[i]
//                                .findViewById(R.id.preview_pickednumbers);
//                        String number = data.getPickedNumbers().replaceAll(",",
//                                ", ");
//                        if (number.contains("UL")) {
//                            int index = number.indexOf("UL") - 2; // to leave last ,
//                            number = "UL"
//                                    + "\n"
//                                    + number.substring(0, index)
//                                    + "\n"
//                                    + "BL"
//                                    + "\n"
//                                    + number.substring(index + 6,
//                                    number.lastIndexOf(","));
//                        }
//                        numbers[i].setText(number);
//                        numbers[i].setVisibility(View.VISIBLE);
//                    }
//
//                    showpreview.addView(previewData[i]);
//                }
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        removeDialog(PREVIEW);
//                        alertDialog.dismiss();
//                        progressDialog = ProgressDialog.show(FiveByNineGame.this,
//                                "", resources.getString(R.string.please_wait), false, false);
//                        Thread thread = new Thread(FiveByNineGame.this);
//                        thread.start();
//                    }
//                });
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DataSource.Keno.totalPurchaseAmt = 0;
//                        DataSource.Keno.panelData = new ArrayList<PanelData>();
//                        removeDialog(PREVIEW);
//                        alertDialog.dismiss();
//                    }
//                });
//                break;
//            case DIALOG_QP:
//                ok = (Button) alertDialog.findViewById(R.id.btn_ok);
//                cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
//
//                layout_one = (TableRow) alertDialog
//                        .findViewById(R.id.quick_pick_ui_perm_one);
//                layout_two = (TableRow) alertDialog
//                        .findViewById(R.id.quick_pick_ui_perm_two);
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM3
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2) {
//                    layout_one.setVisibility(View.VISIBLE);
//                    layout_two.setVisibility(View.VISIBLE);
//                } else if (gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL) {
//                    layout_one.setVisibility(View.GONE);
//                    layout_two.setVisibility(View.VISIBLE);
//                } else {
//                    layout_one.setVisibility(View.GONE);
//                    layout_two.setVisibility(View.GONE);
//                }
//
//                numbersPicked = (CustomTextView) alertDialog
//                        .findViewById(R.id.numbers_picked_text);
//                drawSelected = (CustomTextView) alertDialog
//                        .findViewById(R.id.no_of_draws_text);
//                betAmount = (EditText) alertDialog
//                        .findViewById(R.id.bet_amount_text);
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM6) {
//                    betAmount.setFocusable(false);
//                    betAmount.setTextColor(Color.WHITE);
//                    betAmount.setFocusableInTouchMode(false);
//                    betAmount.setEnabled(false);
//                } else {
//                    betAmount.setFocusable(true);
//                    betAmount.setFocusableInTouchMode(true);
//                    betAmount.setTextColor(Color.BLACK);
//                    betAmount.setEnabled(true);
//                }
//                totalAmount = (CustomTextView) alertDialog
//                        .findViewById(R.id.total_amount);
//                qpText = (CustomTextView) alertDialog.findViewById(R.id.qp_text);
//                quickPickNumbers = (EditText) alertDialog
//                        .findViewById(R.id.qp_amount_text);
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1) {
//                    qpText.setText("No. for QP");
//                    quickPickNumbers.setFocusable(false);
//                    quickPickNumbers.setTextColor(Color.WHITE);
//                    quickPickNumbers.setFocusableInTouchMode(false);
//                    quickPickNumbers.setEnabled(false);
//                } else {
//                    qpText.setText("Enter No. for QP");
//                    quickPickNumbers.setFocusable(true);
//                    quickPickNumbers.setFocusableInTouchMode(true);
//                    quickPickNumbers.setTextColor(Color.BLACK);
//                    quickPickNumbers.setEnabled(true);
//                }
//                noOfLines = (CustomTextView) alertDialog.findViewById(R.id.no_of_lines);
//                quickPickNumbers
//                        .addTextChangedListener(com.skilrock.FiveByNineGame.GameActivity.this);
//                betAmount
//                        .addTextChangedListener(com.skilrock.FiveByNineGame.GameActivity.this);
//                unitPrice = DataSource.betUnitPriceDouble;
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM3
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3
//                        || gamePlay.getBetName() == DataSource.Ghana.BANKER
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2)
//                    numbersPicked.setText("QP");
//                else
//                    numbersPicked.setText(gamePlay.getMaxValue() + "");
//                drawSelected.setText("1");
//
//                // Setting Bet Amount and Total Amount
//                betAmount.setText(AmountFormat
//                        .getCurrentAmountFormatForMobile(unitPrice));
//
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM3
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3)
//                    totalAmount.setText("");
//                else if (gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL) {
//                    double amount = Double.parseDouble(betAmount.getText()
//                            .toString().trim()) * 89;
//                    totalAmount.setText(amount + "");
//                } else
//                    totalAmount.setText(unitPrice + "");
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1) {
//                    quickPickNumbers.setText("10");
//                } else {
//                    quickPickNumbers.setText("");
//                }
//
//                // Setting No Of Lines
//                if (gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL)
//                    noOfLines.setText("89");
//                else
//                    noOfLines.setText("");
//                betString = String.valueOf(unitPrice + "");
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!validationsOk(gamePlay.getBetName())) {
//                            Toast.makeText(FiveByNineGame.this, error,
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (betAmount.getText().toString().trim().length() > 0
//                                    && !betAmount.getText().toString().trim()
//                                    .equalsIgnoreCase("0")) {
//                                alertDialog.dismiss();
//                                String noPicked = null;
//                                if (gamePlay.getBetName() == DataSource.Ghana.PERM1
//                                        || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                                        || gamePlay.getBetName() == DataSource.Ghana.PERM3
//                                        || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3
//                                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2) {
//                                    noPicked = qpString;
//                                } else
//                                    noPicked = String.valueOf(gamePlay
//                                            .getMinValue());
//
//                                String playType = getPlayType(gamePlay.getBetName());
//                                double betAmtMul = Double.parseDouble(betAmount
//                                        .getText().toString().trim());
//                                // betAmtMul = betAmtMul * 1000
//                                // / DataSource.betUnitPriceDouble * 1000;
//                                String pickedNumbers = "QP";
//                                PanelData panelData = new PanelData(true, noPicked,
//                                        Double.parseDouble(totalAmount.getText()
//                                                .toString().trim()), playType,
//                                        AmountFormat.getBetAmountMultiple(
//                                                betAmtMul,
//                                                DataSource.betUnitPriceDouble),
//                                        pickedNumbers);
//
//                                if (DataSource.Keno.panelData == null)
//                                    DataSource.Keno.panelData = new ArrayList<PanelData>();
//                                DataSource.Keno.panelData.add(panelData);
//
//                                if (getIntent().getBooleanExtra("isD6", false)) {
//                                } else {
//                                    BetActivity.betBeans.get(position)
//                                            .setNoOfPannel(
//                                                    BetActivity.betBeans.get(
//                                                            position)
//                                                            .getNoOfPannel() + 1);
//                                }
//
//                                DataSource.Keno.totalPurchaseAmt += Double
//                                        .parseDouble(totalAmount.getText()
//                                                .toString().trim());
//
//                                qpString = "";
//                                quickPickNumbers = null;
//                                if (getIntent().getBooleanExtra("isD6", false)) {
//                                    showDialog(PREVIEW);
//                                } else {
//                                    FiveByNineGame.this.finish();
//                                }
//                            }
//                        }
//                    }
//                });
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qpString = "";
//                        quickPickNumbers = null;
//                        alertDialog.dismiss();
//                    }
//                });
//                break;
//            case DIALOG_BET:
//                ok = (Button) alertDialog.findViewById(R.id.btn_ok);
//                cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
//
//                layout_one = (TableRow) alertDialog
//                        .findViewById(R.id.quick_pick_ui_perm_one);
//                layout_two = (TableRow) alertDialog
//                        .findViewById(R.id.quick_pick_ui_perm_two);
//
//                layout_one.setVisibility(View.GONE);
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM1
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM3
//                        || gamePlay.getBetName() == DataSource.Ghana.PERM1
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3
//                        || gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2)
//                    layout_two.setVisibility(View.VISIBLE);
//                else
//                    layout_two.setVisibility(View.GONE);
//
//                numbersPicked = (CustomTextView) alertDialog
//                        .findViewById(R.id.numbers_picked_text);
//                drawSelected = (CustomTextView) alertDialog
//                        .findViewById(R.id.no_of_draws_text);
//                betAmount = (EditText) alertDialog
//                        .findViewById(R.id.bet_amount_text);
//                if (gamePlay.getBetName() == DataSource.Ghana.PERM6) {
//                    betAmount.setFocusable(false);
//                    betAmount.setTextColor(Color.WHITE);
//                    betAmount.setFocusableInTouchMode(false);
//                    betAmount.setEnabled(false);
//                } else {
//                    betAmount.setFocusable(true);
//                    betAmount.setTextColor(Color.BLACK);
//                    betAmount.setFocusableInTouchMode(true);
//                    betAmount.setEnabled(true);
//                }
//                totalAmount = (CustomTextView) alertDialog
//                        .findViewById(R.id.total_amount);
//                noOfLines = (CustomTextView) alertDialog.findViewById(R.id.no_of_lines);
//                betAmount
//                        .addTextChangedListener(com.skilrock.FiveByNineGame.GameActivity.this);
//
//                unitPrice = DataSource.betUnitPriceDouble;
//                numbersPicked.setText(String
//                        .valueOf(DataSource.Keno.numbersSelected));
//
//                drawSelected.setText("1");
//                betAmount.setText(String.valueOf(AmountFormat
//                        .getCurrentAmountFormatForMobile(unitPrice)));
//
//                if (gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL) {
//                    noOfLines.setText("89");
//                    totalAmount.setText(String.valueOf(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice * 89)));
//                } else if (gamePlay.getBetName() == DataSource.Ghana.PERM1) {
//                    long lines = combination(DataSource.Keno.numbersSelected, 1);
//                    noOfLines.setText(String.valueOf(lines));
//                    totalAmount.setText(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice * lines));
//                } else if (gamePlay.getBetName() == DataSource.Ghana.PERM2
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2) {
//                    long lines = combination(DataSource.Keno.numbersSelected, 2);
//                    noOfLines.setText(String.valueOf(lines));
//                    totalAmount.setText(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice * lines));
//                } else if (gamePlay.getBetName() == DataSource.Ghana.PERM3
//                        || gamePlay.getBetName() == DataSource.Ghana.DC_Perm3) {
//                    long lines = combination(DataSource.Keno.numbersSelected, 3);
//                    noOfLines.setText(String.valueOf(lines));
//                    totalAmount.setText(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice * lines));
//                } else if (gamePlay.getBetName() == DataSource.Ghana.PERM6) {
//                    long lines = combination(DataSource.Keno.numbersSelected, 6);
//                    noOfLines.setText(String.valueOf(lines));
//                    totalAmount.setText(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice * lines));
//                } else {
//                    noOfLines.setText("");
//                    totalAmount.setText(AmountFormat
//                            .getCurrentAmountFormatForMobile(unitPrice));
//                }
//
//                betString = String.valueOf(AmountFormat
//                        .getCurrentAmountFormatForMobile(unitPrice));
//
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!isOK(gamePlay.getBetName())) {
//                            Toast.makeText(FiveByNineGame.this, error,
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (betAmount.getText().toString().trim().length() > 0
//                                    && !betAmount.getText().toString().trim()
//                                    .equalsIgnoreCase("0")) {
//                                alertDialog.dismiss();
//                                String noPicked = String.valueOf(getNoPicked());
//                                String playType = getPlayType(gamePlay.getBetName());
//                                double betAmtMul = Double.parseDouble(betAmount
//                                        .getText().toString().trim());
//                                // betAmtMul = betAmtMul * 1000
//                                // / DataSource.betUnitPriceDouble * 1000;
//                                String pickedNumbers = getPickedNo();
//                                PanelData panelData = new PanelData(false,
//                                        noPicked, Double.parseDouble(totalAmount
//                                        .getText().toString().trim()),
//                                        playType,
//                                        AmountFormat.getBetAmountMultiple(
//                                                betAmtMul,
//                                                DataSource.betUnitPriceDouble),
//                                        pickedNumbers);
//                                if (DataSource.Keno.panelData == null)
//                                    DataSource.Keno.panelData = new ArrayList<PanelData>();
//                                DataSource.Keno.panelData.add(panelData);
//
//                                if (getIntent().getBooleanExtra("isD6", false)) {
//                                } else {
//                                    BetActivity.betBeans.get(position)
//                                            .setNoOfPannel(
//                                                    BetActivity.betBeans.get(
//                                                            position)
//                                                            .getNoOfPannel() + 1);
//                                }
//                                // DataSource.Zim.panelCount[gamePlay.getBetName()]
//                                // += 1;
//                                DataSource.Keno.totalPurchaseAmt += Double
//                                        .parseDouble(totalAmount.getText()
//                                                .toString().trim());
//                                qpString = "";
//                                quickPickNumbers = null;
//                                if (getIntent().getBooleanExtra("isD6", false)) {
//                                    showDialog(PREVIEW);
//                                } else {
//                                    FiveByNineGame.this.finish();
//                                }
//                            }
//                        }
//                    }
//                });
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        qpString = "";
//                        quickPickNumbers = null;
//                        alertDialog.dismiss();
//                    }
//                });
//                break;
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        try {
//            betString = betAmount.getText().toString().trim()
//                    .replaceAll("\\s", "");
//            if (gamePlay.getBetName() == DataSource.Ghana.PERM1) {
//                commonCheck(gamePlay.getMinValue(), gamePlay.getMaxValue(), 1);
//            } else if (gamePlay.getBetName() == DataSource.Ghana.PERM2) {
//                commonCheck(gamePlay.getMinValue(), gamePlay.getMaxValue(), 2);
//            } else if (gamePlay.getBetName() == DataSource.Ghana.PERM3) {
//                commonCheck(gamePlay.getMinValue(), gamePlay.getMaxValue(), 3);
//            } else if (gamePlay.getBetName() == DataSource.Ghana.PERM6) {
//                commonCheck(gamePlay.getMinValue(), gamePlay.getMaxValue(), 6);
//            } else if (gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL) {
//                // Scenario will be same for Manual or Quick Pick
//                if (!betString.equals("")) {
//                    if (noOfLines.getText().toString().trim().length() > 0)
//                        totalAmount.setText(AmountFormat
//                                .getCurrentAmountFormatForMobile(Double
//                                        .parseDouble(betString)
//                                        * Integer.parseInt(noOfLines.getText()
//                                        .toString().trim())));
//                } else
//                    totalAmount.setText("");
//            } else
//                totalAmount.setText(betString);
//        } catch (NumberFormatException exception) {
//            totalAmount.setText("");
//        }
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count,
//                                  int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    void commonCheck(int min, int max, int r) {
//
//        if (isQuickPick) {
//            int n = 0;
//            long combination = 0;
//
//            qpString = quickPickNumbers.getText().toString().trim()
//                    .replaceAll("\\s", "");
//            if (qpString.length() > 0) {
//                if (Double.parseDouble(qpString) >= min
//                        && Integer.parseInt(qpString) <= max) {
//                    n = Integer.parseInt(qpString);
//                    combination = combination(n, r);
//                    noOfLines.setText(String.valueOf(combination));
//                    if (betString.length() > 0) {
//                        if (!betString.substring(betString.length() - 1)
//                                .equals(".")) {
//                            totalAmount.setText(AmountFormat
//                                    .getCurrentAmountFormatForMobile(Double
//                                            .parseDouble(betString)
//                                            * Integer.parseInt(noOfLines
//                                            .getText().toString()
//                                            .trim())));
//                        } else {
//                            totalAmount.setText("");
//                        }
//                    } else
//                        totalAmount.setText("");
//                } else {
//                    totalAmount.setText("");
//                    noOfLines.setText("");
//                }
//            } else {
//                noOfLines.setText("");
//                totalAmount.setText("");
//            }
//        } else // Manual Pick
//        {
//            if (!betString.equals("")) {
//                if (noOfLines.getText().toString().trim().length() > 0) {
//                    if (betString.length() > 0) {
//                        if (!betString.substring(betString.length() - 1)
//                                .equals(".")) {
//                            double amount = Double.parseDouble(noOfLines
//                                    .getText().toString().trim())
//                                    * Double.parseDouble(betString.trim());
//                            totalAmount.setText(AmountFormat
//                                    .getCurrentAmountFormatForMobile(amount));
//                        } else {
//                            totalAmount.setText("");
//                        }
//                    } else
//                        totalAmount.setText("");
//                }
//            } else
//                totalAmount.setText("");
//        }
//
//    }
//
//    private int getNoPicked() {
//        int value = 0;
//        for (int i = 0; i < DataSource.numbers.length; i++) {
//            if (DataSource.numbers[i] == 1)
//                value += 1;
//        }
//        return value;
//    }
//
//    private String getPlayType(int playType) {
//        switch (playType) {
//            case DataSource.Ghana.DIRECT1:
//                return "Direct1";
//            case DataSource.Ghana.DIRECT2:
//                return "Direct2";
//            // case DataSource.Ghana.DC_Direct2:
//            // return "DC-Direct2";
//            case DataSource.Ghana.DIRECT3:
//                return "Direct3";
//            case DataSource.Ghana.DC_Direct3:
//                return "DC-Direct3";
//            case DataSource.Ghana.DIRECT4:
//                return "Direct4";
//            case DataSource.Ghana.DIRECT5:
//                return "Direct5";
//            case DataSource.Ghana.PERM2:
//                return "Perm2";
//            case DataSource.Ghana.DC_Perm2:
//                return "DC-Perm2";
//            case DataSource.Ghana.PERM3:
//                return "Perm3";
//            case DataSource.Ghana.PERM1:
//                return "Perm1";
//            case DataSource.Ghana.PERM6:
//                return "Perm6";
//            case DataSource.Ghana.DC_Perm3:
//                return "DC-Perm3";
//            case DataSource.Ghana.BANKER:
//                return "Banker";
//
//            default:
//                return "Banker1AgainstAll";
//        }
//    }
//
//    private String getPickedNo() {
//        String value = "";
//        for (int i = 0; i < DataSource.numbers.length; i++) {
//            if (DataSource.numbers[i] == 1) {
//                value += (i + 1);
//                value += ",";
//            }
//        }
//        value = value.substring(0, value.length() - 1);
//        return value;
//    }
//
//    private long combination(int n, int r) {
//        // n!/(r!*(n-r)!);
//        return fact(n) / (fact(r) * fact(n - r));
//    }
//
//    private long fact(long n) {
//        if (n == 0)
//            return 1;
//        else
//            return n * fact(n - 1);
//    }
//
//    private String getDensity() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        switch (metrics.densityDpi) {
//            case DisplayMetrics.DENSITY_LOW:
//                return "ldpi";
//            case DisplayMetrics.DENSITY_MEDIUM:
//                return "mdpi";
//            case DisplayMetrics.DENSITY_HIGH:
//                return "hdpi";
//            case DisplayMetrics.DENSITY_XHIGH:
//                return "xhdpi";
//            case DisplayMetrics.DENSITY_XXHIGH:
//                return "xxhdpi";
//            case DisplayMetrics.DENSITY_XXXHIGH:
//                return "xxxhdpi";
//            default:
//                return null;
//        }
//    }
//
//    private boolean validationsOk(int betName) {
//        boolean check = false;
//        double ticketAmount = 0;
//        double availabeBalace;
//        if (totalAmount.getText().toString().trim().length() != 0)
//            ticketAmount = DataSource.Keno.totalPurchaseAmt
//                    + Double.parseDouble(totalAmount.getText().toString()
//                    .trim());
//        availabeBalace = Double.parseDouble(DataSource.Login.currentBalance);
//        // if (betAmount.getText().toString().trim().length() == 1) {
//        // if (String.valueOf(DataSource.betUnitPriceDouble).length() == 2) {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 1.0;
//        // } else {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 10;
//        // }
//        // newUnitPriceQP = DataSource.betUnitPriceDouble * 10;
//        // }
//        // if (betAmount.getText().toString().trim().length() == 3) {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 10;
//        // newUnitPriceQP = DataSource.betUnitPriceDouble * 10;
//        // }
//        // if (betAmount.getText().toString().trim().length() == 4) {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 100;
//        // newUnitPriceQP = DataSource.betUnitPriceDouble * 100;
//        // }
//        try {
//            switch (betName) {
//                case DataSource.Ghana.PERM1:
//                case DataSource.Ghana.PERM2:
//                case DataSource.Ghana.PERM3:
//                case DataSource.Ghana.PERM6:
//                case DataSource.Ghana.DC_Perm3:
//                case DataSource.Ghana.DC_Perm2:
//                    if (betAmount.getText().toString().trim().length() == 0
//                            || quickPickNumbers.getText().toString().trim()
//                            .length() == 0) {
//                        check = false;
//                        error = "All Fields Are Not Fullfilled";
//                    } else if (Double.parseDouble(betAmount.getText().toString()
//                            .trim()) == 0) {
//                        check = false;
//                        error = "Invalid Amount";
//                    } else if (betName == DataSource.Ghana.PERM1
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (betName == DataSource.Ghana.PERM2
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (betName == DataSource.Ghana.PERM3
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (betName == DataSource.Ghana.PERM6
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (betName == DataSource.Ghana.DC_Perm3
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (betName == DataSource.Ghana.DC_Perm2
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if ((Integer.parseInt(quickPickNumbers.getText()
//                            .toString().trim()) < gamePlay.getMinValue())
//                            || (Integer.parseInt(quickPickNumbers.getText()
//                            .toString().trim()) > gamePlay.getMaxValue())) {
//                        check = false;
//                        if (betName == DataSource.Ghana.PERM1) {
//                            error = "Quick Pick must be equal to "
//                                    + gamePlay.getMaxValue() + " numbers ";
//                        } else {
//                            error = "Quick Pick must be between " + ""
//                                    + gamePlay.getMinValue() + " to " + ""
//                                    + gamePlay.getMaxValue() + " numbers ";
//                        }
//                    } else if (!(Double.parseDouble(new BigDecimal(betAmount
//                            .getText().toString().trim()).remainder(new BigDecimal(
//                            DataSource.betUnitPriceDouble + "")) + "") == 0)) {
//                        check = false;
//                        error = "Bet Amount must be multiple of  " + " "
//                                + DataSource.betUnitPriceDouble;
//                    } else if (ticketAmount > availabeBalace) {
//                        check = false;
//                        error = "Balace is not Sufficient for this betType";
//                    } else if (betName == DataSource.Ghana.PERM1
//                            && totalAmount.getText().toString().trim().equals("")) {
//                        check = false;
//                        error = "Invalid amount";
//                    } else if (betName == DataSource.Ghana.PERM2
//                            && totalAmount.getText().toString().trim().equals("")) {
//                        check = false;
//                        error = "Invalid amount";
//                    } else if (betName == DataSource.Ghana.PERM3
//                            && totalAmount.getText().toString().trim().equals("")) {
//                        check = false;
//                        error = "Invalid amount";
//                    } else if (betName == DataSource.Ghana.PERM6
//                            && totalAmount.getText().toString().trim().equals("")) {
//                        check = false;
//                        error = "Invalid amount";
//                    } else
//                        check = true;
//                    break;
//                default:
//                    if (betAmount.getText().toString().trim().length() == 0) {
//                        check = false;
//                        error = "Enter some amount in Bet Amount";
//                    } else if (Double.parseDouble(betAmount.getText().toString()
//                            .trim()) == 0) {
//                        check = false;
//                        error = "Invalid Amount";
//                    } else if ((gamePlay.getBetName() == DataSource.Ghana.DIRECT1
//                            || gamePlay.getBetName() == DataSource.Ghana.DIRECT2 || gamePlay
//                            .getBetName() == DataSource.Ghana.DC_Direct2)
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if ((gamePlay.getBetName() == DataSource.Ghana.DIRECT4 || gamePlay
//                            .getBetName() == DataSource.Ghana.DIRECT5)
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (((gamePlay.getBetName() == DataSource.Ghana.DIRECT3) || (gamePlay
//                            .getBetName() == DataSource.Ghana.DC_Direct3))
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if ((gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL)
//                            && Double.parseDouble(betAmount.getText().toString()
//                            .trim()) > DataSource.maxBetAmtMul) {
//                        check = false;
//                        error = "Bet Amount must be less than or equal to "
//                                + DataSource.maxBetAmtMul;
//                    } else if (!(Double.parseDouble(new BigDecimal(betAmount
//                            .getText().toString().trim()).remainder(new BigDecimal(
//                            DataSource.betUnitPriceDouble + "")) + "") == 0)) {
//                        check = false;
//                        error = "Bet Amount must be multiple of  " + " "
//                                + DataSource.betUnitPriceDouble;
//                    } else if (ticketAmount > availabeBalace) {
//                        check = false;
//                        error = "Balance is not Sufficient for this Bet Type";
//                    } else
//                        check = true;
//                    break;
//            }
//        } catch (NumberFormatException exception) {
//            check = false;
//            error = "Invalid Amount";
//        }
//        return check;
//    }
//
//    private boolean isOK(int betName) {
//        boolean check = false;
//        double ticketAmount = 0;
//        double availabeBalace;
//        if (totalAmount.getText().toString().trim().length() != 0) {
//            ticketAmount = DataSource.Keno.totalPurchaseAmt
//                    + Double.parseDouble(totalAmount.getText().toString()
//                    .trim());
//        }
//        availabeBalace = Double.parseDouble(DataSource.Login.currentBalance);
//        // if (betAmount.getText().toString().trim().length() == 1) {
//        // if (String.valueOf(DataSource.betUnitPriceDouble).length() == 2) {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 1.0;
//        // } else {
//        // newBetAmtQP = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 10;
//        // }
//        // newUnitPrice = DataSource.betUnitPriceDouble * 10;
//        // }
//        // if (betAmount.getText().toString().trim().length() == 3) {
//        // newBetAmt = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 10;
//        // newUnitPrice = DataSource.betUnitPriceDouble * 10;
//        // }
//        // if (betAmount.getText().toString().trim().length() == 4) {
//        // newBetAmt = Double.parseDouble(betAmount.getText().toString()
//        // .trim()) * 100;
//        // newUnitPrice = DataSource.betUnitPriceDouble * 100;
//        // }
//        try {
//            if (betAmount.getText().toString().trim().length() == 0) {
//                check = false;
//                error = "Enter some amount";
//            } else if (Double
//                    .parseDouble(betAmount.getText().toString().trim()) == 0) {
//                check = false;
//                error = "Invalid Amount";
//            } else if ((gamePlay.getBetName() == DataSource.Ghana.PERM1
//                    || gamePlay.getBetName() == DataSource.Ghana.PERM2
//                    || gamePlay.getBetName() == DataSource.Ghana.PERM6
//                    || gamePlay.getBetName() == DataSource.Ghana.DC_Perm2
//                    || gamePlay.getBetName() == DataSource.Ghana.DIRECT1
//                    || gamePlay.getBetName() == DataSource.Ghana.DIRECT2 || gamePlay
//                    .getBetName() == DataSource.Ghana.DC_Direct2)
//                    && Double
//                    .parseDouble(betAmount.getText().toString().trim()) > DataSource.maxBetAmtMul) {
//                check = false;
//                error = "Bet Amount must be less than or equal to "
//                        + DataSource.maxBetAmtMul;
//            } else if ((gamePlay.getBetName() == DataSource.Ghana.PERM3
//                    || gamePlay.getBetName() == DataSource.Ghana.DIRECT4 || gamePlay
//                    .getBetName() == DataSource.Ghana.DIRECT5)
//                    && Double
//                    .parseDouble(betAmount.getText().toString().trim()) > DataSource.maxBetAmtMul) {
//                check = false;
//                error = "Bet Amount must be less than or equal to "
//                        + DataSource.maxBetAmtMul;
//            } else if ((gamePlay.getBetName() == DataSource.Ghana.DIRECT3)
//                    && Double
//                    .parseDouble(betAmount.getText().toString().trim()) > DataSource.maxBetAmtMul) {
//                check = false;
//                error = "Bet Amount must be less than or equal to "
//                        + DataSource.maxBetAmtMul;
//            } else if ((gamePlay.getBetName() == DataSource.Ghana.BANKER_1_AGAINST_ALL)
//                    && Double
//                    .parseDouble(betAmount.getText().toString().trim()) > DataSource.maxBetAmtMul) {
//                check = false;
//                error = "Bet Amount must be less than or equal to "
//                        + DataSource.maxBetAmtMul;
//            } else if (!(Double.parseDouble(new BigDecimal(betAmount.getText()
//                    .toString().trim()).remainder(new BigDecimal(
//                    DataSource.betUnitPriceDouble + "")) + "") == 0)) {
//                check = false;
//                error = "Bet Amount must be multiple of  " + " "
//                        + DataSource.betUnitPriceDouble;
//            } else if (ticketAmount > availabeBalace) {
//                check = false;
//                error = "Balance is not Sufficient for this betType";
//            } else if (betName == DataSource.Ghana.PERM1
//                    && totalAmount.getText().toString().trim().equals("")) {
//                check = false;
//                error = "Invalid amount";
//            } else if (betName == DataSource.Ghana.PERM2
//                    && totalAmount.getText().toString().trim().equals("")) {
//                check = false;
//                error = "Invalid amount";
//            } else if (betName == DataSource.Ghana.PERM3
//                    && totalAmount.getText().toString().trim().equals("")) {
//                check = false;
//                error = "Invalid amount";
//            } else if (betName == DataSource.Ghana.PERM6
//                    && totalAmount.getText().toString().trim().equals("")) {
//                check = false;
//                error = "Invalid amount";
//            } else
//                check = true;
//        } catch (NumberFormatException exception) {
//            check = false;
//            error = "Invalid Amount";
//        }
//        return check;
//
//    }
//
//    @Override
//    public void run() {
//        DataSource.Keno.kenoBean = new KenoBean(1, false,
//                DataSource.Keno.panelData.size(),
//                DataSource.Keno.totalPurchaseAmt, DataSource.Keno.panelData);
//        data = Communication.kenoBuy(DataSource.Keno.kenoBean,
//                DataSource.Login.username, DataSource.gameDevName);
//        // Log.i("resp", data.toString());
//        DataSource.resetVariables();
//        handler.sendEmptyMessage(0);
//    }
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            progressDialog.dismiss();
//            if (data != null) {
//                try {
//                    if (data.getString("isSuccess").equalsIgnoreCase("true")) {
//                        Intent intent = new Intent(getApplicationContext(),
//                                TicketActivity.class);
//                        intent.putExtra("ticketData", data.toString());
//                        intent.putExtra("trackTicket", false);
//                        startActivity(intent);
//                        DataSource.Login.isPurSuccess = true;
//                        finish();
//                    } else {
//                        if (data.getInt("errorCode") == 118) {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                    FiveByNineGame.this);
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
//                                            // Intent(GameActivity.this,
//                                            // LoginActivity.class));
//                                            FiveByNineGame.this.finish();
//                                            DataSource.Login.isSessionValid = false;
//                                        }
//                                    });
//                            alertDialog.show();
//
//                        } else {
//                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                    FiveByNineGame.this);
//                            alertDialog
//                                    .setIcon(android.R.drawable.ic_dialog_alert);
//                            alertDialog.setCancelable(false);
//                            alertDialog.setMessage(data.getString("errorMsg"));
//                            alertDialog.setPositiveButton("OK",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(
//                                                DialogInterface dialog,
//                                                int which) {
//                                            FiveByNineGame.this.finish();
//                                            DataSource.Login.isPurSuccess = true;
//                                        }
//                                    });
//                            alertDialog.show();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                        FiveByNineGame.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                alertDialog.setCancelable(false);
//                alertDialog.setMessage("Some Error Occured, Please try later");
//                alertDialog.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                FiveByNineGame.this.finish();
//                                DataSource.Login.isPurSuccess = true;
//                            }
//                        });
//                alertDialog.show();
//            }
//        }
//    };
//
//    protected void onDestroy() {
//        try {
//            Apsalar.unregisterApsalarReceiver();
//        } catch (Exception e) {
//            ;
//        }
//        super.onDestroy();
//    }
//}
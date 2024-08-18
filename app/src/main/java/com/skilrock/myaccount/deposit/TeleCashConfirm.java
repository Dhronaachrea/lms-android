package com.skilrock.myaccount.deposit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.weidget.LotteryEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class TeleCashConfirm extends HeaderBaseActivity implements
        View.OnClickListener, View.OnFocusChangeListener, WebServicesListener {

    private LinearLayout teleCashSecond;
    private CustomTextView txnAmt;
    private CustomTextView feeAmountedittext;
    private CustomTextView totalAmountedittext;
    private EditText otpSecond;
    private CustomTextView txtSubmitSecond;
    private Context context;
    private String txnAmount, feeAmount, totalAmount, mobileNo;

    private final String pathtelecash = "/rest/telecash/performDepositTxn";
    private String code;
    private CustomTextView mobileSecondNo, mobileSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_cash_confirm);
        context = TeleCashConfirm.this;
        code = VariableStorage.GlobalPref.getStringData(TeleCashConfirm.this, VariableStorage.GlobalPref.CURRENCY_CODE);
        IntentValue();
        bindsViewById();

        txtSubmitSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!otpSecond.getText().toString().equalsIgnoreCase("") || otpSecond.getText().toString().equalsIgnoreCase(null)) {
                    try {
                        JSONObject data = new JSONObject();
                        data.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                        data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                        data.put("mobileNbr", mobileNo);
                        data.put("txnAmount", txnAmount);
                        data.put("feeAmount", feeAmount);
                        data.put("otp", otpSecond.getText().toString());
                        new PMSWebTask((Activity) context, pathtelecash, "N/A", data, "telecashresp", null, "Loading...").execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Utils.Toast(context, context.getResources().getString(R.string.otp_null));
                }

            }
        });

    }

    private void IntentValue() {
        Intent intent = getIntent();
        txnAmount = intent.getStringExtra("txnAmount");
        feeAmount = intent.getStringExtra("feeAmount");
        totalAmount = intent.getStringExtra("totalAmount");
        mobileNo = intent.getStringExtra("mobileNo");
    }

    private void bindsViewById() {
        teleCashSecond = (LinearLayout) findViewById(R.id.tele_cash_second);
        txnAmt = (CustomTextView) findViewById(R.id.txn_amt);
        feeAmountedittext = (CustomTextView) findViewById(R.id.fee_Amount);
        totalAmountedittext = (CustomTextView) findViewById(R.id.total_amount);
        otpSecond = (LotteryEditText) findViewById(R.id.otp_second);
        txtSubmitSecond = (CustomTextView) findViewById(R.id.txt_submit_second);
        mobileSecond = (CustomTextView) findViewById(R.id.mobile_second);
        mobileSecondNo = (CustomTextView) findViewById(R.id.mobile_second_no);
        ((CustomTextView) findViewById(R.id.header_name))
                .setText("TeleCash");

        txnAmt.setText(code + "" + txnAmount);
        feeAmountedittext.setText(code + "" + feeAmount);
        totalAmountedittext.setText(code + "" + totalAmount);
        mobileSecondNo.setText(mobileNo);


        (findViewById(R.id.done)).setVisibility(View.INVISIBLE);
        (findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {

        if (resultData != null) {
            try {
                final JSONObject json = new JSONObject((String) resultData);

                if (methodType.equalsIgnoreCase("telecashresp")) {
                    if (json.getInt("responseCode") == 0) {
                        dialog.dismiss();

                        View.OnClickListener okClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                DrawerBaseActivity.selectedItemId = -1;
//                                Intent intent = new Intent(TeleCashConfirm.this,
//                                        MainScreen.class);
//
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(GlobalVariables.startAmin,
//                                        GlobalVariables.endAmin);
                                finish();
                            }
                        };

                        new DownloadDialogBox(TeleCashConfirm.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();


                    } else if (json.getInt("responseCode") == 118) {
                        dialog.dismiss();
                        View.OnClickListener okClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawerBaseActivity.selectedItemId = -1;
                                UserInfo.setLogout(TeleCashConfirm.this);
                                Intent intent = new Intent(TeleCashConfirm.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                            }
                        };

                        new DownloadDialogBox(TeleCashConfirm.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();


                    } else {
                        dialog.dismiss();
                        new DownloadDialogBox(TeleCashConfirm.this, json.getString("responseMsg"), "", false, true, null, null).show();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                GlobalVariables.showServerErr(TeleCashConfirm.this);
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(TeleCashConfirm.this);
        }
    }


}

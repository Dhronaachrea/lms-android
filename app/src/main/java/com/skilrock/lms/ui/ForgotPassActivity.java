package com.skilrock.lms.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONObject;

public class ForgotPassActivity extends HeaderBaseActivity implements
        OnClickListener, WebServicesListener {
    private EditText phonenumber;
    private Button forgotPassOK;
    // private CustomTextView header;
    private String invalidMsg;
    protected Resources resources;
    private int mobilelen;
    private Context context;
    private JSONObject loginData;
    private GlobalPref globalPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        context = this;
        resources = getResources();
        setContentView(R.layout.activity_forgot_password);
        setHeaderText(R.string.forgot_header);
        bindIds();
        forgotPassOK.setOnClickListener(this);
        phonenumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) ForgotPassActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phonenumber.getWindowToken(), 0);
                    forgotPassOKCall();
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // header.setText("Retrive Password");
        // if(!DataSource.Login.isSessionValid)
        // ForgotPassActivity.this.finish();
    }


    @Override
    public void onClick(View arg0) {
        forgotPassOKCall();
    }

    private void forgotPassOKCall() {
        if (!checkPhoneNumber())
            Utils.Toast(ForgotPassActivity.this, invalidMsg
            );
        else {
            if (GlobalVariables.connectivityExists(context)) {
                String path = "/com/skilrock/pms/mobile/loginMgmt/Action/forgotPlayerPassword.action?";
                String params = "mobileNo=" + phonenumber.getText().toString().trim();
                new PMSWebTask(ForgotPassActivity.this, path + params, "", null, "FP", null, "").execute();
            } else {
                GlobalVariables.showDataAlert(context);
            }
        }
    }

    private boolean checkPhoneNumber() {
        if (phonenumber.getText().toString().trim().equals("")) {
            invalidMsg = resources.getString(R.string.enter_mob);
            return false;
        } else {
            mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(ForgotPassActivity.this, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));
            if (phonenumber.getText().toString().trim().length() != mobilelen) {
                invalidMsg = getString(R.string.mobile_no_validate) + mobilelen + ".";
                return false;
            } else
                return true;
        }

    }

    private void bindIds() {
        phonenumber = (EditText) findViewById(R.id.forgotpass_number);
        forgotPassOK = (Button) findViewById(R.id.forgotpassOk);
        phonenumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.MOBILE_NO_LENGTH)))});
        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            findViewById(R.id.mobile_code).setVisibility(View.VISIBLE);
        }
        // header = (CustomTextView) findViewById(R.id.header_name);
        ((CustomTextView) findViewById(R.id.header_name))
                .setText(R.string.forgot_header);
        (findViewById(R.id.close)).setVisibility(View.INVISIBLE);
        ((ImageView) (findViewById(R.id.done))).setImageResource(R.drawable.close);
        (findViewById(R.id.done))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "FP":
                if (resultData != null) {
                    try {
                        loginData = new JSONObject(resultData.toString());
                        if (loginData.getString("isSuccess").equals("true")) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ForgotPassActivity.this.finish();
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(ForgotPassActivity.this,
                                    resources.getString(R.string.pass_sent), "",
                                    false, true, okClickListener, okClickListener)
                                    .show();
                        } else {
                            if (loginData.getString("errorCode").equalsIgnoreCase("118")) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(
                                                GlobalVariables.startAmin,
                                                GlobalVariables.endAmin);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(ForgotPassActivity.this,
                                        loginData.getString("errorMsg"), "", false, true, okClickListener,
                                        null).show();
                            } else {
                                dialog.dismiss();
                                Utils.Toast(context, loginData.getString("errorMsg"));
                            }
                        }
                    } catch (Exception e) {
                        dialog.dismiss();
                        GlobalVariables.showServerErr(context);
                    }
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(context);
                }
                break;
        }
    }
}

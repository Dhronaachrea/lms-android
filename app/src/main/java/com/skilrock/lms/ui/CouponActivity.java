package com.skilrock.lms.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class CouponActivity extends HeaderBaseActivity implements
        OnClickListener, WebServicesListener {
    private EditText phonenumber;
    private Button forgotPassOK;
    // private CustomTextView header;
    private String invalidMsg;
    protected Resources resources;
    private int mobilelen;
    private Context context;
    private JSONObject loginData;
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        resources = getResources();
        setContentView(R.layout.coupon_activity);
        ((CustomTextView) findViewById(R.id.header_name)).setText(R.string.apply_promo);


//        setHeaderText(R.string.apply_promo);
        bindIds();
        forgotPassOK.setOnClickListener(this);
        phonenumber.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) CouponActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phonenumber.getWindowToken(), 0);
                    applyCouponCall();
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
    }


    @Override
    public void onClick(View arg0) {
        applyCouponCall();

    }

    private void applyCouponCall() {

        if (phonenumber.getText().toString().trim() != null && phonenumber.getText().toString().trim().length() > 1) {
            if (GlobalVariables.connectivityExists(context)) {
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                    jsonObject.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                    jsonObject.put("promoCode", phonenumber.getText().toString().trim());
                    new PMSWebTask(CouponActivity.this, "/rest/playerMgmt/applyPromoCode", "N/A", jsonObject, "APPLY_COUPON", null, "Checking Code...").execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                GlobalVariables.showDataAlert(context);
            }
        } else
            Utils.Toast(CouponActivity.this, "Please enter promo code");
    }


    private void bindIds() {
        phonenumber = (EditText) findViewById(R.id.forgotpass_number);
        forgotPassOK = (Button) findViewById(R.id.forgotpassOk);
        // header = (CustomTextView) findViewById(R.id.header_name);
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
            case "APPLY_COUPON":
                if (resultData != null) {
                    try {
                        loginData = new JSONObject(resultData.toString());
                        if (loginData.getInt("responseCode") == 0) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {

                                        VariableStorage.UserPref.setStringPreferences(CouponActivity.this, VariableStorage.UserPref.USER_BAL, AmountFormat.getCorrectAmountFormat(loginData.getString("availableBal")));
                                        CouponActivity.this.finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(CouponActivity.this,
                                    loginData.getString("responseMsg"), "",
                                    false, true, okClickListener, okClickListener)
                                    .show();
                        } else if (loginData.getInt("responseCode") == 1029) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(CouponActivity.this,
                                    loginData.getString("responseMsg"), "", false, true, null,
                                    null).show();
                        } else if (loginData.getInt("responseCode") == 1028) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(CouponActivity.this,
                                    loginData.getString("responseMsg"), "", false, true, okClickListener,
                                    null).show();
                        } else {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            };
                            dialog.dismiss();
                            if (loginData.has("errorMsg"))
                                new DownloadDialogBox(CouponActivity.this, loginData.getString("errorMsg"), "", false, true, okClickListener, null).show();
                            else if (loginData.has("responseMsg"))
                                new DownloadDialogBox(CouponActivity.this, loginData.getString("responseMsg"), "", false, true, okClickListener, null).show();
                            else
                                new DownloadDialogBox(CouponActivity.this, "Something Wrong, Try Later !", "", false, true, okClickListener, null).show();
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

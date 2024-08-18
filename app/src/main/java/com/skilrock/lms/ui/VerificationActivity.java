package com.skilrock.lms.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.GlobalVariables;
import com.weidget.LotteryEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationActivity extends HeaderBaseActivity implements OnClickListener, WebServicesListener {
    private Button done;
    private LotteryEditText verification;
    private ImageView headerImage;
    private CustomTextView resendVerification;
    private String userName;

    private String methodType = "";
    private final String VERIFICATION = "VERIFICATION";
    private final String CODE_CHECK = "CODE_CHECK";
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        setContentView(R.layout.activity_verification);
        setHeaderText(R.string.verify_header);
        done = (Button) findViewById(R.id.verification_done);
        verification = (LotteryEditText) findViewById(R.id.verification_code);
        resendVerification = (CustomTextView) findViewById(R.id.resend_verification);
        headerImage = (ImageView) findViewById(R.id.header_image);
        headerImage.setVisibility(View.INVISIBLE);
        done.setOnClickListener(this);
        resendVerification.setOnClickListener(this);
        userName = getIntent().getStringExtra("username");
        methodType = VERIFICATION;

        new PMSWebTask(VerificationActivity.this, getVerificationPath(), "", null, methodType, null, "Please wait...").execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        verification.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verification_done:
                String code = verification.getText().toString().trim();
                if (code.length() == 6) {
                    methodType = CODE_CHECK;

                    new PMSWebTask(VerificationActivity.this, getCheckCodePath(), "", null, methodType, null, "Please wait...").execute();
                } else if (code.length() == 0) {
                    Utils.Toast(getApplicationContext(),
                            resources.getString(R.string.enter_code)
                    );
                } else {
                    Utils.Toast(getApplicationContext(),
                            resources.getString(R.string.invalid_code)
                    );
                }
                break;
            case R.id.resend_verification:

                methodType = VERIFICATION;
                new PMSWebTask(VerificationActivity.this, getVerificationPath(), "", null, methodType, null, "Please wait...").execute();
                break;
        }
    }

    private void updateDeviceId(String bindingId) {
        SharedPreferences prefs = getSharedPreferences(
                LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("bindingId", bindingId);
        prefsEditor.commit();
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        Utils.logPrint("data:" + resultData.toString());
        Utils.consolePrint(resultData.toString());
        if (resultData != null) switch (methodType) {
            case "VERIFICATION":
                verification.setText("");
                dialog.dismiss();
                new DownloadDialogBox(VerificationActivity.this,
                        resources.getString(R.string.sms_sent), "", false,
                        true, null, null).show();

                break;
            case "CODE_CHECK":
                try {
                    JSONObject json = new JSONObject(resultData.toString());
                    verification.setText("");
                    if (json.getBoolean("isSuccess")) {
                        updateDeviceId(json.getString("bindingId"));
                        OnClickListener clickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(VerificationActivity.this, LoginActivity.class));
                                VerificationActivity.this.finish();
                            }
                        };
                        dialog.dismiss();
                        new DownloadDialogBox(VerificationActivity.this,
                                resources.getString(R.string.device_reg), "", false,
                                true, clickListener, null).show();
                    } else {
                        dialog.dismiss();
                        Utils.Toast(getApplicationContext(), json.getString("errorMsg"));
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(VerificationActivity.this);
                }
                break;
        }
        else
            GlobalVariables.showServerErr(VerificationActivity.this);
    }


    private String getCheckCodePath() {
        String path = "/com/skilrock/pms/mobile/playerInfo/Action/deviceBindingUpdate.action?";
        String params = "userName=" + userName + "&deviceName=Android&deviceType=Phone&verificationCode=" + verification.getText().toString().trim();
        return path + params;
    }

    private String getVerificationPath() {
        String path = "/com/skilrock/pms/mobile/playerInfo/Action/deviceBindingVerificationCode.action?";
        String params = "userName=" + userName + "&deviceName=Android&deviceType=Phone";
        return path + params;
    }
}

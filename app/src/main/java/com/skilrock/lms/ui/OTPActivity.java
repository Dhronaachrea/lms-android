package com.skilrock.lms.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.customui.SkilrockProgressDialog;
import com.skilrock.lms.communication.Communication;
import com.skilrock.utils.Utils;

public class OTPActivity extends HeaderBaseActivity implements OnClickListener {

    private Button sendButton, regenerateButton;
    private EditText otpEditText;
    private static final String SENDOTP = "SEND";

    private static final String REGENERATE = "REGENERATE";
    private SkilrockProgressDialog progressDialog;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        resources = getResources();
        setHeaderText(R.string.otp_header);
        sendButton = (Button) findViewById(R.id.sendOTP);
        regenerateButton = (Button) findViewById(R.id.regenerateOTP);
        otpEditText = (EditText) findViewById(R.id.otp);
        sendButton.setOnClickListener(this);
        regenerateButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (otpEditText != null)
            otpEditText.setText("");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendOTP:

                if (otpEditText.getText().toString() != null) {

                    try {
                        if (otpEditText.getText().toString().length() > 5) {
                            new SentOTPTask().execute(otpEditText.getText()
                                    .toString(), SENDOTP);
                        }
                        // if (otpEditText.getText().toString().length() == 0
                        // || otpEditText.getText().toString().length() == 1) {
                        // Utils.Toast(getApplicationContext(),
                        // "Please Enter OTP", Toast.LENGTH_SHORT);
                        // }

                        else {
                            Utils.Toast(getApplicationContext(),
                                    resources.getString(R.string.enter_otp));
                        }
                    } catch (NullPointerException e) {
                        Utils.Toast(getApplicationContext(),
                                resources.getString(R.string.enter_otp)
                        );
                    } catch (Exception e) {
                        Utils.Toast(getApplicationContext(),
                                resources.getString(R.string.enter_otp)
                        );
                    }
                } else {
                    Utils.Toast(getApplicationContext(),
                            resources.getString(R.string.enter_otp)
                    );
                }

                break;
            case R.id.regenerateOTP:
                getMobileNumberAlertDialog(resources.getString(R.string.regen_otp));
                break;

            default:
                break;
        }
    }

    private class SentOTPTask extends AsyncTask<String, Void, String> {
        boolean taskFlag = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new SkilrockProgressDialog(OTPActivity.this, "",
                    resources.getString(R.string.please_wait), false, false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String data = params[0];
            String value = params[1];
            String returnVal = null;
            if (value.equals(SENDOTP) || value.equalsIgnoreCase(SENDOTP)) {

                JSONObject json = Communication.sentOTPVerificationCode(data);

                if (json != null) {

                    try {
                        if (json.has("isSuccess")) {
                            if (json.getBoolean("isSuccess")) {
                                taskFlag = true;
                                returnVal = json.getString("message");
                            } else {
                                if (json.has("errorCode")) {
                                    if (json.getInt("errorCode") == 201) {
                                        taskFlag = false;
                                        returnVal = resources
                                                .getString(R.string.inv_vc);

                                    } else if (json.getInt("errorCode") == 202) {
                                        taskFlag = false;
                                        returnVal = resources
                                                .getString(R.string.vc_exp);

                                    } else {
                                        if (json.has("errorMsg")) {
                                            taskFlag = true;
                                            returnVal = json
                                                    .getString("errorMsg");
                                        } else {
                                            taskFlag = true;
                                            returnVal = resources
                                                    .getString(R.string.pls_try);

                                        }
                                    }
                                } else {
                                    taskFlag = true;
                                    returnVal = resources
                                            .getString(R.string.pls_try);

                                }
                            }
                        } else {
                            taskFlag = true;
                            returnVal = resources.getString(R.string.pls_try);

                        }

                    } catch (JSONException e) {
                        returnVal = null;
                    }
                }
            } else if (value.equals(REGENERATE)
                    || value.equalsIgnoreCase(REGENERATE)) {
                JSONObject json = Communication.regenerateOTP(data);

                if (json != null) {

                    try {
                        // taskFlag = false;
                        // returnVal = json.getString("message");

                        if (json.has("isSuccess")) {
                            if (json.getBoolean("isSuccess")) {
                                taskFlag = false;
                                returnVal = json.getString("message");
                            } else {
                                if (json.has("errorCode")) {
                                    if (json.has("errorMsg")) {
                                        taskFlag = true;
                                        returnVal = json.getString("errorMsg");
                                    } else {
                                        taskFlag = true;
                                        returnVal = resources
                                                .getString(R.string.pls_try);

                                    }

                                } else {
                                    taskFlag = true;
                                    returnVal = resources
                                            .getString(R.string.pls_try);

                                }
                            }
                        } else {
                            taskFlag = true;
                            returnVal = resources.getString(R.string.pls_try);

                        }

                    } catch (JSONException e) {
                        returnVal = null;
                    }

                }

            }
            return returnVal;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result != null) {

                showMessageAlertDialog("OTP", result, taskFlag);
            } else {
                showMessageAlertDialog("OTP", getString(R.string.net_error),
                        taskFlag);
            }

            super.onPostExecute(result);

        }
    }

    private boolean dialogFlag = false;

    public void showMessageAlertDialog(String header, String messages,
                                       boolean flag) {
        dialogFlag = flag;
        final Dialog commentDialog = new Dialog(OTPActivity.this);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.widget_dialog_box);
        CustomTextView mDialogText = (CustomTextView) commentDialog
                .findViewById(R.id.dialogTextContent);

        CustomTextView mDialogHeader = (CustomTextView) commentDialog
                .findViewById(R.id.dialogHeaderText);

        try {

            mDialogText.setText(messages);
            mDialogHeader.setText(header);

        } catch (Exception e) {
        }

        Button done = (Button) commentDialog.findViewById(R.id.dialogDone);

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogFlag) {
                    commentDialog.dismiss();
                    OTPActivity.this.finish();
                } else {
                    commentDialog.dismiss();

                } // startActivity(new Intent(OTPActivity.this,
                // LoginActivity.class));
            }
        });

        commentDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.widget_progress_dialog_bg);
        commentDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        commentDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        commentDialog.show();
    }

    private EditText mDialogTextMobile;

    public void getMobileNumberAlertDialog(String header) {

        final Dialog commentDialog = new Dialog(OTPActivity.this);
        commentDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.widget_mobile_number_dialog_box);
        mDialogTextMobile = (EditText) commentDialog
                .findViewById(R.id.dialogTextContent);

        CustomTextView mDialogHeader = (CustomTextView) commentDialog
                .findViewById(R.id.dialogHeaderText);

        try {
            mDialogHeader.setText(header);
        } catch (Exception e) {
        }

        Button done = (Button) commentDialog.findViewById(R.id.dialogDone);

        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (mDialogTextMobile.getText().toString().length() > 8
                            && mDialogTextMobile.getText().toString().length() < 11) {
                        if (mDialogTextMobile.getText().toString().length() > 0) {

                            commentDialog.dismiss();
                            new SentOTPTask().execute(mDialogTextMobile
                                            .getText().toString().toString(),
                                    REGENERATE);

                        }
                    } else {
                        Utils.Toast(OTPActivity.this,
                                resources.getString(R.string.enter_mob)
                        );
                    }

                } catch (NullPointerException e) {
                    Utils.Toast(OTPActivity.this,
                            resources.getString(R.string.enter_mob)
                    );
                }
            }
        });

        commentDialog.getWindow().setBackgroundDrawableResource(
                R.drawable.widget_progress_dialog_bg);
        commentDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        commentDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        commentDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OTPActivity.this.finish();
    }

}

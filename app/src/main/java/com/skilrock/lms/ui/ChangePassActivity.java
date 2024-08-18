package com.skilrock.lms.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class ChangePassActivity extends Activity implements OnClickListener,
        WebServicesListener {
    private Button OK;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText retypePassword;
    private JSONObject changePassData;
    private Context context;
    private DownloadDialogBox dBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = this;
        bindId();
        oldPassword = (EditText) findViewById(R.id.oldpassword);
        newPassword = (EditText) findViewById(R.id.newpassword);
        retypePassword = (EditText) findViewById(R.id.retypepassword);
        oldPassword.setTypeface(Typeface.SANS_SERIF);
        newPassword.setTypeface(Typeface.SANS_SERIF);
        retypePassword.setTypeface(Typeface.SANS_SERIF);
        OK = (Button) findViewById(R.id.changepassOk);
        OK.setOnClickListener(this);
        retypePassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) ChangePassActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(retypePassword.getWindowToken(), 0);
                    changePassCall();
                }
                return false;
            }
        });

    }

    private void bindId() {
        ((CustomTextView) findViewById(R.id.header_name))
                .setText(R.string.change_header);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.changepassOk) {
            changePassCall();
        }
    }

    private void changePassCall() {

        if (newPassword.getText().toString().trim().length() < 8
                || newPassword.getText().toString().trim().length() > 16)
            Utils.Toast(ChangePassActivity.this,
                    "Password Must Be Between 8 To 16 Characters"
            );
        else if (!newPassword.getText().toString().trim()
                .equals(retypePassword.getText().toString().trim()))
            Utils.Toast(ChangePassActivity.this,
                    "New Password and Confirm Password Mismatch"
            );
        else if (oldPassword.getText().toString().trim()
                .equals(retypePassword.getText().toString().trim()))
            Utils.Toast(ChangePassActivity.this,
                    "New Passwords Must Be Different From Old Password"
            );
        else {
            if (GlobalVariables.connectivityExists(context)) {
                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("oldPassword", oldPassword.getText()
                            .toString().trim());
                    data.put("newPassword", newPassword.getText().toString().trim());
                    data.put("userName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String path = "/com/skilrock/pms/mobile/loginMgmt/Action/changePlayerPassword.action?";
                String param = "changePasswordData=" + URLEncoder.encode(data.toString());
                new PMSWebTask(ChangePassActivity.this, path + param, "", null, "CHANGE", null, "").execute();
            } else {
                GlobalVariables.showDataAlert(context);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "CHANGE":
                if (resultData != null) {
                    try {
                        changePassData = new JSONObject(resultData.toString());
                        if (changePassData.getBoolean("isSuccess")) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    dBox.dismiss();
                                    ChangePassActivity.this.finish();
//                                    UserInfo.setLogout(ChangePassActivity.this);
//                                    Intent intent = new Intent(ChangePassActivity.this,
//                                            MainScreen.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
//                                    overridePendingTransition(
//                                            GlobalVariables.startAmin,
//                                            GlobalVariables.endAmin);

                                    // new Code for Logout the User

//                                    if (GlobalVariables.connectivityExists(context)) {
//                                        dBox.dismiss();
//                                        String path = "/com/skilrock/pms/mobile/loginMgmt/Action/playerLogout.action?";
//                                        String params = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
//                                        new PMSWebTask(MainScreen.mainScreen, path + params, "", null, "LOGOUT_CHANGE_PASS", null, "").execute();
//                                    } else {
//                                        GlobalVariables.showDataAlert(context);
//                                    }
                                }
                            };
                            dialog.dismiss();
                            dBox = new DownloadDialogBox(context,
                                    "Password changed Successfully.", "INFORMATION", false, true, okClickListener,
                                    null);
                            dBox.show();
                        } else {
                            if (changePassData.getInt("errorCode") == 118) {
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
                                new DownloadDialogBox(context,
                                        changePassData.getString("errorMsg"), "", false, true, okClickListener,
                                        null).show();
                            } else {
                                dialog.dismiss();
                                Utils.Toast(ChangePassActivity.this,
                                        changePassData.getString("errorMsg")
                                );
                            }

                        }
                    } catch (JSONException e) {
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

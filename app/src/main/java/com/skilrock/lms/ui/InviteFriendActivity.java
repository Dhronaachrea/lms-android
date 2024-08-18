package com.skilrock.lms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoButton;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InviteFriendActivity extends DrawerBaseActivity implements WebServicesListener {
    private EditText phoneNumber;
    private RobotoButton invite, fromContact;
    private HashMap<String, String> data;
    private String invalidMsg;
    private ChooseContact contactDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
        context = this;

        sHeader();
        setDrawerItems();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText(getResources().getString(R.string.invite_friend));

        phoneNumber = (EditText) findViewById(R.id.invite_number);
        invite = (RobotoButton) findViewById(R.id.invite_done);
        fromContact = (RobotoButton) findViewById(R.id.open_contact);
        fromContact.setOnClickListener(debouncedOnClickListener);
        invite.setOnClickListener(debouncedOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        phoneNumber.setText("");
        manageHeader();
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
    }

    private DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(1000) {
        @Override
        public void onDebouncedClick(View v) {
            boolean connectivity = GlobalVariables.connectivityExists(InviteFriendActivity.this);
            int id = v.getId();
            if (id == R.id.invite_done) {
                if (!Config.isStatic && !connectivity) {
                    GlobalVariables.showDataAlert(InviteFriendActivity.this);
                    return;
                }
                if (Config.isStatic && GlobalVariables.loadDummyData) {
                    Utils.Toast(InviteFriendActivity.this,
                            "This feature is not available in offline mode");
                    return;
                }
                if (phoneNumber.getText().toString().trim().equals("")) {
                    Utils.Toast(InviteFriendActivity.this,
                            "Please enter mobile number");
                } else {
                    if (chkValidation(phoneNumber.getText().toString().trim())) {
                        String path = "/com/skilrock/pms/mobile/playerInfo/Action/invitePlayerRequest.action?";
                        String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME) + "&mobileNum=" + phoneNumber.getText().toString().trim();
                        new PMSWebTask((Activity) context, path + params, "GET", new JSONObject(), "NUMBER", null, "Sending invitation...").execute();
                    }
                }
            } else if (id == R.id.open_contact) {
                if (Config.isStatic && GlobalVariables.loadDummyData) {
                    Utils.Toast(InviteFriendActivity.this,
                            "This feature is not available in offline mode");
                    return;
                }
                contactDialog = new ChooseContact(InviteFriendActivity.this);
                contactDialog.show();
            }
        }
    };

    private boolean chkValidation(String numbers) {
        if (numbers == null && numbers.equalsIgnoreCase("")) {
            return false;
        } else {
            if (chkNumberLength(numbers)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean chkNumberLength(String numbers) {
        String[] num = numbers.split(",");
        for (int i = 0; i < num.length; i++) {
            String mobNo = num[i].contains("-") ? num[i].replaceAll("-", "") : num[i];
            if ((mobNo.length() < 6 || mobNo.length() > 15)) {
                Utils.Toast(context, "mobile number length should be between 6 to 15 digits");
                return false;
            }
        }
        if (num.length == 0) {
            Utils.Toast(context, "mobile number length should be between 6 to 15 digits");
            return false;
        } else {
            return true;
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            phoneNumber.setText("");
            if (data != null) {
                if (data.get("isSuccess").equalsIgnoreCase("true")) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                            InviteFriendActivity.this);
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//                    alertDialog.setCancelable(false);
//                    alertDialog.setMessage("Sending invitation is in process...");
//                    alertDialog.setPositiveButton("OK", null);
//                    alertDialog.show();

                    new DownloadDialogBox(InviteFriendActivity.this, "Your request for invitation has been submitted successfully", "INFORMATION", false, true, null, null).show();
                } else if (data.get("isSuccess").equals("false")) {
                    if (data.get("errorCode").equalsIgnoreCase("118")) {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                InviteFriendActivity.this);
//                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                        alertDialog.setCancelable(false);
//                        alertDialog.setMessage("Session Out Login Again");

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InviteFriendActivity.this.finish();
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

//                        alertDialog.show();
                        new DownloadDialogBox(InviteFriendActivity.this, "Session Out Login Again", "INFORMATION", false, true, clickListener, null).show();
                    } else {
                        // password.setText("");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                context);
                        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage(data.get("errorMsg"));
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
//                                        contactDialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            } else

            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        InviteFriendActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(R.string.net_error);
                alertDialog.setPositiveButton("OK", null);
                alertDialog.show();
            }
        }
    };

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // phoneNumber.setText("");
            if (data != null) {
                if (data.get("isSuccess").equalsIgnoreCase("true")) {
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                            InviteFriendActivity.this);
//                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
//                    alertDialog.setCancelable(false);
//                    alertDialog.setMessage("Invitation sent to your friend(s)");
//                    alertDialog.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface arg0,
//                                                    int arg1) {
//                                    contactDialog.dismiss();
//                                }
//                            });
//                    alertDialog.show();

                    View.OnClickListener clickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactDialog.dismiss();
                        }
                    };
                    new DownloadDialogBox(InviteFriendActivity.this, "Your request for invitation has been submitted successfully", "INFORMATION", false, true, clickListener, null).show();
                } else if (data.get("isSuccess").equals("false")) {
                    if (data.get("errorCode").equalsIgnoreCase("118")) {
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                InviteFriendActivity.this);
//                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
//                        alertDialog.setCancelable(false);
//                        alertDialog.setMessage("Session Out Login Again");

                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InviteFriendActivity.this.finish();
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

//                        alertDialog.show();
                        new DownloadDialogBox(InviteFriendActivity.this, "Session Out Login Again", "INFORMATION", false, true, clickListener, null).show();
                    }
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            context);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_info);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(data.get("errorMsg"));
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    contactDialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        context);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(R.string.net_error);
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                contactDialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    };


    public static HashMap<String, String> inviteFriend(JSONObject json) {
        HashMap<String, String> data = null;
        try {
            if (json != null) {
                data = new HashMap<String, String>();
                if (json.getBoolean("isSuccess"))
                    data.put("isSuccess", "true");
                else {
                    data.put("isSuccess", "false");
                    data.put("errorMsg", json.getString("errorMsg"));
                    data.put("errorCode", json.getString("errorCode"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return data;
    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (resultData != null) {
            if (methodType.equals("CONTACT")) {
                try {
                    data = inviteFriend(new JSONObject((String) resultData));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler1.sendEmptyMessage(0);
            } else if (methodType.equals("NUMBER")) {
                try {
                    data = inviteFriend(new JSONObject((String) resultData));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        } else if (resultData == null && Config.isStatic && GlobalVariables.loadDummyData) {
            Utils.Toast(context, "Data not available in offline mode");
        } else {
            GlobalVariables.showServerErr(InviteFriendActivity.this);
        }
    }

    @Override
    public void finish() {
        super.finish();
        selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }
}
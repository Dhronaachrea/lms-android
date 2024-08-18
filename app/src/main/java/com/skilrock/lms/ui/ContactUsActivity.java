package com.skilrock.lms.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUsActivity extends DrawerBaseActivity implements WebServicesListener {
    private RobotoTextView name;
    private RobotoTextView website;
    private RobotoTextView contact;
    private RobotoTextView email;
    private ProgressDialog progressDialog;
    private JSONObject drawData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_activity);
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
        headerText.setText(getResources().getString(R.string.contact_us_header));

        name = (RobotoTextView) findViewById(R.id.user_name_new);
        website = (RobotoTextView) findViewById(R.id.website_name_new);
        contact = (RobotoTextView) findViewById(R.id.contact_name_new);
        email = (RobotoTextView) findViewById(R.id.email_name_new);
        name.setVisibility(RobotoTextView.INVISIBLE);
        website.setVisibility(RobotoTextView.INVISIBLE);
        contact.setVisibility(RobotoTextView.INVISIBLE);
        email.setVisibility(RobotoTextView.INVISIBLE);

        String path;

        if (GlobalPref.getInstance(this).getCountry().equalsIgnoreCase("lagos")) {
            path = "/com/skilrock/pms/mobile/playerInfo/Action/contactBackOffice.action";
            String params = "userName=" + VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.USER_NAME);
        } else {
            path = "/com/skilrock/pms/mobile/playerInfo/Action/contactBackOffice.action";
        }
        new PMSWebTask(this, path, "GET", new JSONObject(), "CONTACT_US", null, "Loading...").execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (drawData != null) {
                try {
                    if (drawData.getBoolean("isSuccess")) {
                        name.setText(drawData.getString("contactName"));

                        String websiteName = drawData.getString("websiteName").replace("\n", "&")
                                .replaceAll("\\s+", "").replace("&", "\n");

                        if (websiteName.contains("<br/>")) {
                            website.setText(websiteName.replace("<br/>", "\n"));

                        } else {
                            website.setText(websiteName);

                        }
                        contact.setText(drawData.getString("mobileNum"));
                        email.setText(drawData.getString("emailId"));

                        name.setVisibility(RobotoTextView.VISIBLE);
                        website.setVisibility(RobotoTextView.VISIBLE);
                        contact.setVisibility(RobotoTextView.VISIBLE);
                        email.setVisibility(RobotoTextView.VISIBLE);
                    } else {
                        if (drawData.getInt("errorCode") == 118) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    ContactUsActivity.this);
                            alertDialog
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Session Out Login Again");
                            alertDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            finish();
                                            // ContactUsActivity.this.finish();
                                            //DataSource.Login.isSessionValid = false;
                                        }
                                    });
                            alertDialog.show();
                        } else
                            Utils.Toast(ContactUsActivity.this,
                                    drawData.getString("errorMsg")
                            );
                    }

                } catch (Exception e) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            ContactUsActivity.this);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Network Error, please try later");
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    ContactUsActivity.this.finish();
                                }
                            });
                    alertDialog.show();
                }

            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        ContactUsActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Network Error, please try later");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ContactUsActivity.this.finish();
                            }
                        });
                alertDialog.show();
            }
        }
    };

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (dialog != null)
            dialog.dismiss();
        if (resultData != null) {
            if (methodType.equals("CONTACT_US")) try {
                drawData = new JSONObject((String) resultData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        } else if (Config.isStatic && GlobalVariables.loadDummyData) {
            this.finish();
            Utils.Toast(this, "Data not available in offline mode");
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
package com.skilrock.lms.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.skilrock.adapters.FAQExpandableListAdapter;
import com.skilrock.bean.FAQBean;
import com.skilrock.config.Config;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FAQActivity extends DrawerBaseActivity implements WebServicesListener {

    private JSONObject jsonObject;
    private ProgressDialog progressDialog;
    private FAQBean faqBean;
    // private FAQAdapter adapter;
    private ListView listView;
    private ExpandableListView expandableListView;
    FAQExpandableListAdapter expandableListAdapter;
    private ArrayList<HashMap<String, String>> qaData;
    private static LinkedHashMap<String, ArrayList<HashMap<String, String>>> faqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main_menu);
        setContentView(R.layout.faq_activity);
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
        headerText.setText(getResources().getString(R.string.faq_header));
        // listView = (ListView) findViewById(R.id.main_menu);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        faqData = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        String path = "/com/skilrock/pms/mobile/playerInfo/Action/getFAQData.action";
        new PMSWebTask(this, path, "GET", new JSONObject(), "FAQ", null, "Loading...").execute();

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

            try {

                if (jsonObject != null) {
                    if (jsonObject.has("isSuccess")) {
                        if (jsonObject.getBoolean("isSuccess")) {

                            Gson gson = new Gson();

                            faqBean = gson.fromJson(jsonObject.toString(),
                                    FAQBean.class);

                            qaData = new ArrayList<HashMap<String, String>>();

                            for (int i = 0; i < faqBean.getFaqData().size(); i++) {

                                FAQBean.FAQData value = faqBean.getFaqData().get(i);

                                ArrayList<HashMap<String, String>> listD = new ArrayList<HashMap<String, String>>();
                                for (int j = 0; j < value.getFaq().size(); j++) {
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    // if (j == 0) {
                                    // map.put("head", value.getHead());
                                    // }

                                    FAQBean.FAQ name = value.getFaq().get(j);
                                    HashMap<String, String> valueData = new HashMap<String, String>();
                                    valueData.put("Q", name.getQ());
                                    valueData.put("A", name.getA());

                                    listD.add(valueData);

                                    map.put("Q", name.getQ());
                                    map.put("A", name.getA());
                                    qaData.add(map);
                                }
                                faqData.put(value.getHead(), listD);
                            }
                            expandableListAdapter = new FAQExpandableListAdapter(
                                    FAQActivity.this, new ArrayList<String>(
                                    faqData.keySet()), faqData);
                            expandableListView
                                    .setAdapter(expandableListAdapter);

                            //
                            // adapter = new FAQAdapter(FAQActivity.this,
                            // R.layout.faq_row, qaData);
                            // listView.setAdapter(adapter);
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    FAQActivity.this);
                            alertDialog
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setCancelable(false);
                            if (jsonObject.has("errorMessage")) {
                                alertDialog.setMessage(jsonObject.get(
                                        "errorMessage").toString());
                            } else {
                                alertDialog.setMessage("Connection error");
                            }

                            alertDialog.setPositiveButton("OK",
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            FAQActivity.this.finish();
                                        }
                                    });
                            alertDialog.show();
                        }
                    } else {

                        if (jsonObject.getString("errorCode").equalsIgnoreCase(
                                "118")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    FAQActivity.this);
                            alertDialog
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Session Out Login Again");
                            alertDialog.setPositiveButton("OK",
                                    new OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            //DataSource.Login.isSessionValid = false;
                                            FAQActivity.this.finish();
                                        }
                                    });
                            alertDialog.show();
                        } else {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    FAQActivity.this);
                            alertDialog
                                    .setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setCancelable(false);
                            if (jsonObject.has("errorMessage")) {
                                alertDialog.setMessage(jsonObject.get(
                                        "errorMessage").toString());
                            } else {
                                alertDialog.setMessage("Connection error");
                            }

                            alertDialog.setPositiveButton("OK",
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            FAQActivity.this.finish();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            FAQActivity.this);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(R.string.net_error);
                    alertDialog.setPositiveButton("OK", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    });
                    alertDialog.show();

                }
            } catch (Exception e) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        FAQActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(R.string.net_error);
                alertDialog.setPositiveButton("OK", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
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
            if (methodType.equals("FAQ")) try {
                jsonObject = new JSONObject((String) resultData);
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
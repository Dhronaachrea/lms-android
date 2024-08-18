package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.skilrock.bean.BetTypeBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.bean.DrawData;
import com.skilrock.bean.ProfileDetail;
import com.skilrock.config.Config;
import com.skilrock.config.Config.MyAcctOptions;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.deposit.InitialDepositScreen;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyAccountActivity extends DrawerBaseActivity implements WebServicesListener {
    private JSONObject jsonObject;
    private LinkedHashMap<String, DrawData> drawData;
    private LinkedHashMap<String, BetTypeBean> betTypeData;
    private ProfileDetail.PersonalInfo personalInfo;
    private ProfileDetail.WalletInfo walletInfo;
    private String playerName;
    private Intent intent;
    public static final String PERSONAL_INFO_KEY = "personal_info";
    public static final String WALLET_INFO_KEY = "wallet_info";
    public static final String PLAYER_NAME = "player_name";
    private int position;
    private GlobalPref globalPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        setContentView(R.layout.draw_games_play);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.APP_TOUR);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        sHeader();
        setDrawerItems();
        bindViewIds();
        position = getIntent().getIntExtra("fragPos", 0);
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
            }
        });
        intent = getIntent();
        playerName = intent.getStringExtra(ProfileFragment.PLAYER_NAME);
        personalInfo = (ProfileDetail.PersonalInfo) intent.getSerializableExtra(ProfileFragment.PERSONAL_INFO_KEY);
        walletInfo = (ProfileDetail.WalletInfo) intent.getSerializableExtra(ProfileFragment.WALLET_INFO_KEY);

        headerText.setText(getResources().getString(R.string.account_header));
        ArrayList<String> accntOptionsList = new ArrayList<String>();
        for (String str : Config.myAccntOptions) {
            accntOptionsList.add(str);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_row, R.id.spinner_text, accntOptionsList);

        headerSpinner.setAdapter(adapter);

        if (!globalPref.getCountry().equalsIgnoreCase("Lagos")) {
            accntOptionsList.remove(accntOptionsList.size() - 1);
            adapter.notifyDataSetChanged();
        }
        switch (position) {
            case 0:
                replaceFragment(MyAcctOptions.DEPOSIT);
                break;
            case 1:
                replaceFragment(MyAcctOptions.WITHDRAWAL);
                break;
            case 2:
                headerSpinner.setSelection(2);
                replaceFragment(MyAcctOptions.MY_TICKETS);
                break;
            case 3:
                replaceFragment(MyAcctOptions.MY_TRANSACTION);
                break;
            case 4:
                replaceFragment(MyAcctOptions.WITHDRAWAL_REPORT);
                break;
            case 5:
                replaceFragment(MyAcctOptions.MY_BANK_DEPOSIT);
                break;
        }
        headerSpinner.post(new Runnable() {
            @Override
            public void run() {
                headerSpinner
                        .setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int pos, long arg3) {
                                if (drawerLayout.isDrawerOpen(drawerView))
                                    drawerLayout.closeDrawer(drawerView);
                                view.setBackgroundColor(Color.TRANSPARENT);
                                replaceFragment(MyAcctOptions.values()[pos]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedItemId = R.id.my_account;
        manageHeader();
    }


    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };

    private void bindViewIds() {
        // View view = findViewById(R.id.header);
        // headerNavigation = (ImageView) view.findViewById(R.id.drawer_image);
        // headerImage = (ImageView) view.findViewById(R.id.header_image);
        // // headerSpinner = (Spinner) view.findViewById(R.id.spinner);
        // headerText = (CustomTextView) view.findViewById(R.id.header_text);
        // headerSubText = (CustomTextView)
        // view.findViewById(R.id.header_sub_text);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    class TapGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return super.onSingleTapConfirmed(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    // public void animHeaderVisibility(int visibility) {
    // headerAnim.setVisibility(visibility);
    // }

    private void replaceFragment(MyAcctOptions acctOptions) {
        headerSubText.setText(acctOptions.toString());
        // headerSpinner.setSelection(Arrays.asList(Config.myAccntOptions)
        // .indexOf(gameName));
        if (GlobalVariables.connectivityExists(MyAccountActivity.this)) {
            switch (acctOptions) {
//                case MY_PROFILE:
//                    ProfileFragment profile = new ProfileFragment();
//                    Bundle profileBundle = new Bundle();
//                    profileBundle.putString(PLAYER_NAME, intent.getStringExtra(ProfileFragment.PLAYER_NAME));
//                    profileBundle.putSerializable(PERSONAL_INFO_KEY, personalInfo);
//                    profileBundle.putSerializable(WALLET_INFO_KEY, walletInfo);
//                    profile.setArguments(profileBundle);
//                    replaceFragment(profile);
//                    break;
                case DEPOSIT:
                    analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.DEPOSIT);
                    String depositLimitPath = "/com/skilrock/pms/mobile/accMgmt/Action/fetchPaymentModeLimits.action?";
                    String params = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                    new PMSWebTask(this, depositLimitPath + params, "N/A", null, "DEPOSIT_LIMIT", null, "Loading...").execute();

                    break;
                case WITHDRAWAL:
                    analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.WITHDRAWAL);
                    replaceFragment(new WithdrawalScreen());

                    break;
                case MY_TICKETS:
                    analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.MY_TICKETS);

                    String path = "/rest/playerMgmt/fetchSaleTransactions";
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.SESSION_ID));
                        jsonObject.put("playerId", VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.PLAYER_ID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        jsonObject = null;
                    }
                    if (jsonObject != null)
                        new PMSWebTask(this, path, "N/A", jsonObject, "TICKET", null, "Loading...").execute();
                    else
                        Utils.Toast(this, "Error on Fatching");

                    break;
                case MY_TRANSACTION:
                    analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.MY_TRANSACTION);

                    replaceFragment(new MyTransactionFragment());
                    break;
                case WITHDRAWAL_REPORT:
                    analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.WITHDRAWAL_REPORT);

                    //replaceFragment(new WithdrawalReportScreen());
                    String withdrawalpath = "/com/skilrock/pms/mobile/home/reportsMgmt/Action/fetchWithdrawlRequestReport.action?";
                    String withdrawalparams = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                    new PMSWebTask(this, withdrawalpath + withdrawalparams, "N/A", null, "WITHDRAWAL_REPORT", null, "Loading...").execute();
                    break;

                case MY_BANK_DEPOSIT:
                    //replaceFragment(new WithdrawalReportScreen());
                    try {
                        JSONObject data = new JSONObject();
                        String depositeReportpath = "/com/skilrock/pms/mobile/home/reportsMgmt/Action/playerBankDepositNotificationReport.action?";
                        String usernamedeosite = VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                        data.put("userName", usernamedeosite);
                        new PMSWebTask(this, depositeReportpath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "MY_BANK_DEPOSIT", null, "Loading...").execute();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    break;
            }
        } else {
            GlobalVariables.showDataAlert(MyAccountActivity.this);
        }
    }


    void replaceFragment(Fragment fragment) {
        FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragments_frame, fragment);
        //ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commitAllowingStateLoss();
//        String backStateName = fragment.getClass().getName();
//        FragmentManager manager = getSupportFragmentManager();
//        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);
//
//        if (!fragmentPopped) { //fragment not in back stack, create it.
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            if (fragment instanceof ProfileFragment)
//                ft.addToBackStack(backStateName);
//            ft.commit();
//        }
    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (resultData != null) {
            switch (methodType) {
                case "MY_BANK_DEPOSIT":
                    try {
                        JSONObject json = new JSONObject((String) resultData);
                        if ((json).getBoolean("isSuccess")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("BANK_DEPOSIT_REPORT", resultData.toString());
                            Fragment fragment = new MyBankDepositeReport();
                            fragment.setArguments(bundle);
                            dialog.dismiss();
                            replaceFragment(fragment);
                        } else {
                            if (json.getInt("responseCode") == 118) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
                            }

                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        GlobalVariables.showServerErr(this);
                        e.printStackTrace();
                    }
                    break;
                case "TICKET":
                    try {
                        JSONObject json = new JSONObject((String) resultData);
                        if ((json).getString("responseMsg").equalsIgnoreCase("success")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("TICKET", resultData.toString());
                            Fragment fragment = new MyTicketsScreen();
                            fragment.setArguments(bundle);
                            dialog.dismiss();
                            replaceFragment(fragment);
                        } else {
                            if (json.getInt("responseCode") == 118) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
                            }

                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    break;
                case "WITHDRAWAL_REPORT":
                    try {
//                        resultData = "{\"withdrawlRequestData\":[{\"withdrawalCharges\":0.25,\"payableAmount\":4.75,\"transactionTime\":\"22-01-2016 16:37:17\",\"expiryDate\":\"NA\",\"verificationCode\":\"0\",\"withdrawalAmount\":5,\"transactionStatus\":\"APPROVED\",\"Withdrawlchannel\":\"Paga Non Reg\"},{\"withdrawalCharges\":1,\"payableAmount\":19,\"transactionTime\":\"22-01-2016 15:46:54\",\"expiryDate\":\"29-01-2016 15:46:54\",\"verificationCode\":\"0\",\"withdrawalAmount\":20,\"transactionStatus\":\"APPROVED\",\"Withdrawlchannel\":\"Paga Non Reg\"},{\"withdrawalCharges\":1,\"payableAmount\":19,\"transactionTime\":\"22-01-2016 15:00:56\",\"expiryDate\":\"29-01-2016 15:00:56\",\"verificationCode\":\"0\",\"withdrawalAmount\":20,\"transactionStatus\":\"APPROVED\",\"Withdrawlchannel\":\"Paga Non Reg\"},{\"withdrawalCharges\":0.25,\"payableAmount\":4.75,\"transactionTime\":\"14-01-2016 14:42:40\",\"withdrawalAmount\":5,\"transactionStatus\":\"WITHDRAWL_FAILED\",\"Withdrawlchannel\":\"Paga Reg\"},{\"withdrawalCharges\":0.5,\"payableAmount\":9.5,\"transactionTime\":\"14-01-2016 14:39:26\",\"withdrawalAmount\":10,\"transactionStatus\":\"WITHDRAWL_FAILED\",\"Withdrawlchannel\":\"Paga Reg\"},{\"withdrawalCharges\":0.5,\"payableAmount\":9.5,\"transactionTime\":\"14-01-2016 14:35:15\",\"withdrawalAmount\":10,\"paidDate\":\"14-01-2016 14:35:18\",\"transactionStatus\":\"DONE\",\"Withdrawlchannel\":\"Interswitch\"}],\"isSuccess\":true}";
                        JSONObject json = new JSONObject((String) resultData);
                        if ((json).getBoolean("isSuccess")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("WITHDRAWALREPORT", resultData.toString());
                            Fragment fragment = new WithdrawalReportScreen();
                            fragment.setArguments(bundle);
                            dialog.dismiss();
                            replaceFragment(fragment);
                        } else {
                            if (json.getInt("responseCode") == 118) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
                            }

                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    break;
                case "DEPOSIT_LIMIT":
                    try {
                        JSONObject json = new JSONObject((String) resultData);
                        DepositLimitBean bean = new Gson().fromJson(json.toString(), DepositLimitBean.class);
                        if (bean.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("depositLimit", bean);
                            Fragment fragment = new InitialDepositScreen();
                            fragment.setArguments(bundle);
                            dialog.dismiss();
                            replaceFragment(fragment);

                        } else {
                            if (bean.getErrorCode() == 118) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(this, bean.getErrorMsg(), "", false, true, okClickListener, null).show();
                            }

                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        GlobalVariables.showServerErr(this);
                    }
                    break;

            }
        } else {
            dialog.dismiss();
        }

    }

    public Spinner getSpinner() {
        return headerSpinner;
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}

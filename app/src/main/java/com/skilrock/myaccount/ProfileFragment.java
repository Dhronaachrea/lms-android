package com.skilrock.myaccount;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.bean.ProfileDetail;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AmountTextView;
import com.skilrock.customui.CircleImageView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoButton;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WeaverWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.CropSquareTransformation;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stpl on 7/6/2015.
 */
public class ProfileFragment extends DrawerBaseActivity implements WebServicesListener {

    private CircleImageView imgProfile;
    private RobotoTextView txtFirstName;
    private RobotoTextView mobileNo;
    private AmountTextView txtAvailBalance;
    private AmountTextView txtBonusBalance;
    private AmountTextView txtDepositBalance;
    private AmountTextView txtWinningBalance;
    private AmountTextView txtRedeemableBalance;
    private RobotoButton btnDeposit;
    private RobotoButton btnMyTickets;
    private RobotoButton btnMyTransactions;
    private RelativeLayout rlProfile;
    private HorizontalScrollView depositScroll;
    private HorizontalScrollView bonusScroll;
    private HorizontalScrollView winningScroll;
    private HorizontalScrollView redeemScroll;


    private ProfileDetail.PersonalInfo personalInfo;
    private ProfileDetail.WalletInfo walletInfo;
    private String playerName;
    private Context context;
    private boolean isFirst = true;


    public static final String PERSONAL_INFO_KEY = "personal_info";
    public static final String WALLET_INFO_KEY = "wallet_info";
    public static final String PLAYER_NAME = "player_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.PROFILE_FRAGMENT);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        isFirst = false;
        sHeader();
        setDrawerItems();
        manageHeader();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText("MY ACCOUNT");
        headerSubText.setText("MY PROFILE");
        context = this;
        playerName = getIntent().getStringExtra(ProfileFragment.PLAYER_NAME);
        personalInfo = (ProfileDetail.PersonalInfo) getIntent().getSerializableExtra(ProfileFragment.PERSONAL_INFO_KEY);
        walletInfo = (ProfileDetail.WalletInfo) getIntent().getSerializableExtra(ProfileFragment.WALLET_INFO_KEY);
        bindIds();
        setListeners();
        populateData();
    }

    @Override
    public void onResume() {
        super.onResume();

        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        manager.setText("");
        if (!Config.isWearer) {
            isFirst = false;
        }
        if (!isFirst) {
            isFirst = true;
            txtAvailBalance.setText(VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.USER_BAL));
            setTextAmount(txtBonusBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.BONUS_BAL));
            setTextAmount(txtDepositBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.DEPOSIT_BAL));
            setTextAmount(txtWinningBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.WINNING_BAL));
            setTextAmount(txtRedeemableBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.WITHDRAWAL_BAL));
        } else {
            String path = "/Weaver/service/rest/fetchHeaderInfo";
            JSONObject object = new JSONObject();
            try {
                object.put("playerId", VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.PLAYER_ID));
                object.put("domainName", Config.domain_name);
                object.put("playerToken", VariableStorage.UserPref.getStringData(this, VariableStorage.UserPref.SESSION_ID));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            new WeaverWebTask(ProfileFragment.this, path, "POST", object, "BALANCE", null, "Please wait...").execute();
        }

    }

    private void bindIds() {
        imgProfile = (CircleImageView) findViewById(R.id.img_profile);
        txtFirstName = (RobotoTextView) findViewById(R.id.txt_first_name);
        mobileNo = (RobotoTextView) findViewById(R.id.mobileNo);
        txtAvailBalance = (AmountTextView) findViewById(R.id.txt_avail_balance);
        txtBonusBalance = (AmountTextView) findViewById(R.id.txt_bonus_balance);
        txtDepositBalance = (AmountTextView) findViewById(R.id.txt_deposit_balance);
        txtWinningBalance = (AmountTextView) findViewById(R.id.txt_winning_balance);
        txtRedeemableBalance = (AmountTextView) findViewById(R.id.txt_redeemable_balance);
        btnDeposit = (RobotoButton) findViewById(R.id.btn_deposit);
        btnMyTickets = (RobotoButton) findViewById(R.id.btn_my_tickets);
        btnMyTransactions = (RobotoButton) findViewById(R.id.btn_my_transactions);
        rlProfile = (RelativeLayout) findViewById(R.id.rl_profile);
        //deposit edited by Mehul
        depositScroll = (HorizontalScrollView) findViewById(R.id.deposit_scroll);
        bonusScroll = (HorizontalScrollView) findViewById(R.id.bonus_scroll);
        winningScroll = (HorizontalScrollView) findViewById(R.id.winning_scroll);
        redeemScroll = (HorizontalScrollView) findViewById(R.id.redeem_scroll);
        depositScroll.setOnTouchListener(touchListener);
        bonusScroll.setOnTouchListener(touchListener);
        winningScroll.setOnTouchListener(touchListener);
        redeemScroll.setOnTouchListener(touchListener);

        txtAvailBalance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        txtBonusBalance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        txtDepositBalance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        txtWinningBalance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        txtRedeemableBalance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));


    }

//    private void setListeners() {
//        btnDeposit.setOnTouchListener(this);
//        btnMyTickets.setOnTouchListener(this);
//        btnMyTransactions.setOnTouchListener(this);
//        rlProfile.setOnTouchListener(this);
//    }

    private void setListeners() {
        btnDeposit.setOnClickListener(listener);
        btnMyTickets.setOnClickListener(listener);
        btnMyTransactions.setOnClickListener(listener);
        rlProfile.setOnClickListener(listener);
    }

    private void populateData() {
        if (!personalInfo.getProfilePhoto().equals("")) {
            if (VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_PIC_URL) != "")
                Picasso.with(context).load(VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_PIC_URL)).transform(new CropSquareTransformation()).placeholder(R.drawable.no_img).into(imgProfile);
            else
                imgProfile.setImageResource(R.drawable.no_img);

        } else {
            imgProfile.setImageResource(R.drawable.no_img);
        }
        setText(txtFirstName, VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
        setText(mobileNo, personalInfo.getMobileNum());
        if (walletInfo != null) {
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_BAL, walletInfo.getAvailableBal());
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.BONUS_BAL, walletInfo.getBonusBal());
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.DEPOSIT_BAL, walletInfo.getDepositBal());
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.WINNING_BAL, walletInfo.getWinningBal());
            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.WITHDRAWAL_BAL, walletInfo.getWithdrawlBal());
        }
//        setText(txtAvailBalance, walletInfo.getAvailableBal());
//        setTextAmount(txtBonusBalance, walletInfo.getBonusBal());
//        setTextAmount(txtDepositBalance, walletInfo.getDepositBal());
//        setTextAmount(txtWinningBalance, walletInfo.getWinningBal());
//        setTextAmount(txtRedeemableBalance, walletInfo.getWithdrawlBal());


    }


    //    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (GlobalVariables.connectivityExists(context)) {
//            switch (v.getId()) {
//
//
//                case R.id.btn_deposit:
//
//                    String depositLimitPath = "/com/skilrock/pms/mobile/accMgmt/Action/fetchPaymentModeLimits.action?";
//                    String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME);
//                    new PMSWebTask(this, depositLimitPath + params, "N/A", null, "DEPOSIT_LIMIT", null, "Loading...").execute();
//                    break;
//                case R.id.btn_my_tickets:
//                    String path = "/rest/playerMgmt/fetchSaleTransactions";
//                    JSONObject jsonObject = null;
//
//                    try {
//                        jsonObject = new JSONObject();
//                        jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
//                        jsonObject.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        jsonObject = null;
//                    }
//                    if (jsonObject != null)
//                        new PMSWebTask(this, path, "N/A", jsonObject, "TICKET", null, "Loading...").execute();
//                    else
//                        Toast.makeText(context, "Error on Fatching", Toast.LENGTH_SHORT).show();
//
//                    break;
//
//                case R.id.btn_my_transactions:
//                    ((MyAccountActivity) context).getSpinner().setSelection(4);
//                    ((MyAccountActivity) context).replaceFragment(new MyTransactionFragment());
//                    break;
//                case R.id.rl_profile:
//                    Intent editProfile = new Intent(context, ProfileEditActivity.class);
//                    editProfile.putExtra(ProfileFragment.PERSONAL_INFO_KEY, personalInfo);
//                    editProfile.putExtra(ProfileFragment.PLAYER_NAME, playerName);
//                    startActivityForResult(editProfile, 0);
//                    break;
//            }
//        } else {
//            GlobalVariables.showDataAlert(context);
//        }
//
//
//        return false;
//    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {
        @Override
        public void onDebouncedClick(View v) {
            if (GlobalVariables.connectivityExists(context)) {
                switch (v.getId()) {


                    case R.id.btn_deposit:
                        analytics.sendAction(Fields.Category.BUTTON_DEPOSIT, Fields.Action.CLICK);
                        String depositLimitPath = "/com/skilrock/pms/mobile/accMgmt/Action/fetchPaymentModeLimits.action?";
                        String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME);
                        new PMSWebTask(ProfileFragment.this, depositLimitPath + params, "N/A", null, "DEPOSIT_LIMIT", null, "Loading...").execute();
                        break;
                    case R.id.btn_my_tickets:
                        analytics.sendAction(Fields.Category.BUTTON_TICKET, Fields.Action.CLICK);
                        String path = "/rest/playerMgmt/fetchSaleTransactions";
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject();
                            jsonObject.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                            jsonObject.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            jsonObject = null;
                        }
                        if (jsonObject != null)
                            new PMSWebTask(ProfileFragment.this, path, "N/A", jsonObject, "TICKET", null, "Loading...").execute();
                        else
                            Utils.Toast(context, "Error on Fatching");

                        break;

                    case R.id.btn_my_transactions:
                        analytics.sendAction(Fields.Category.BUTTON_TRANSACTION, Fields.Action.CLICK);
                        Intent intent = new Intent(context, MyAccountActivity.class);
                        intent.putExtra("fragPos", 3);
                        startActivity(intent);
                        break;
                    case R.id.rl_profile:
                        analytics.sendAction(Fields.Category.BUTTON_PROFILE, Fields.Action.CLICK);
                        Intent editProfile = new Intent(context, ProfileEditActivity.class);
                        editProfile.putExtra(ProfileFragment.PERSONAL_INFO_KEY, personalInfo);
                        editProfile.putExtra(ProfileFragment.PLAYER_NAME, playerName);
                        startActivityForResult(editProfile, 0);
                        break;
                }
            } else {
                GlobalVariables.showDataAlert(context);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            personalInfo = (ProfileDetail.PersonalInfo) data.getSerializableExtra(ProfileFragment.PERSONAL_INFO_KEY);
            walletInfo = (ProfileDetail.WalletInfo) data.getSerializableExtra(ProfileFragment.WALLET_INFO_KEY);
            populateData();
        }

    }

    private void setText(TextView view, String text) {
        view.setText(text);
    }

    private void setTextAmount(AmountTextView view, String text) {
        view.setText(text);
        if (view.getText().toString().length() > 7) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.text_marquee));

        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        DrawerBaseActivity.selectedItemId = -1;
//    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (resultData != null) {
            try {
                switch (methodType) {
                    case "TICKET":
                        try {
                            JSONObject json = new JSONObject((String) resultData);
                            if ((json).getString("responseMsg").equalsIgnoreCase("success")) {
                                Intent intent = new Intent(context, MyAccountActivity.class);
                                intent.putExtra("fragPos", 2);
                                startActivity(intent);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("TICKET", resultData.toString());
//                                Fragment fragment = new MyTicketsScreen();
//                                fragment.setArguments(bundle);
//                                ((MyAccountActivity) context).replaceFragment(fragment);
                                dialog.dismiss();
                            } else {
                                if (json.getInt("responseCode") == 118) {
                                    View.OnClickListener okClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserInfo.setLogout(context);
                                            Intent intent = new Intent(context,
                                                    MainScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(context, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
                                } else if (json.getInt("responseCode") == 501) {
                                    dialog.dismiss();
                                    Utils.Toast(context, getString(R.string.sql_exception));
                                } else {
                                    dialog.dismiss();
                                    new DownloadDialogBox(context, json.getString("responseMsg"), "", false, true, null, null).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            GlobalVariables.showServerErr(context);

                        }
                        break;

                    case "DEPOSIT_LIMIT":
                        try {
                            JSONObject json = new JSONObject((String) resultData);
                            DepositLimitBean bean = new Gson().fromJson(json.toString(), DepositLimitBean.class);
                            if (bean.isSuccess()) {
                                Intent intent = new Intent(context, MyAccountActivity.class);
                                intent.putExtra("fragPos", 0);
                                startActivity(intent);

//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("depositLimit", bean);
//                                Fragment fragment = new InitialDepositScreen();
//                                fragment.setArguments(bundle);
//                                ((MyAccountActivity) context).replaceFragment(fragment);
                                dialog.dismiss();
                            } else if (bean.getErrorCode() == 501) {
                                dialog.dismiss();
                                Utils.Toast(context, getString(R.string.sql_exception));
                            } else {
                                if (bean.getErrorCode() == 118) {
                                    View.OnClickListener okClickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserInfo.setLogout(context);
                                            Intent intent = new Intent(context, MainScreen.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(context, bean.getErrorMsg(), "", false, true, okClickListener, null).show();
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            GlobalVariables.showServerErr(this);
                        }
                        break;
                    case "BALANCE":
                        try {
                            JSONObject balanceData = new JSONObject(resultData.toString());
                            if (balanceData.getInt("errorCode") == 0) {
                                if (balanceData.has("cashbal"))
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_BAL, AmountFormat.getAmountFormatForTwoDecimal(balanceData.getString("cashbal")));
                                else {// after api upgrade
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.BONUS_BAL, walletInfo.getBonusBal());
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.DEPOSIT_BAL, walletInfo.getDepositBal());
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.WINNING_BAL, walletInfo.getWinningBal());
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.WITHDRAWAL_BAL, walletInfo.getWithdrawlBal());
                                }
                                dialog.dismiss();
                            } else if (balanceData.getInt("errorCode") == 203) {
                                View.OnClickListener okClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserInfo.setLogout(context);
                                        Intent intent = new Intent(context, MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(context, balanceData.getString("respMsg"), "", false, true, okClickListener, null).show();
                            } else {
                                Utils.Toast(this, balanceData.getString("respMsg"));
                                dialog.dismiss();
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            Utils.Toast(this, "Server error");
                        }
                        txtAvailBalance.setText(VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.USER_BAL));
                        setTextAmount(txtBonusBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.BONUS_BAL));
                        setTextAmount(txtDepositBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.DEPOSIT_BAL));
                        setTextAmount(txtWinningBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.WINNING_BAL));
                        setTextAmount(txtRedeemableBalance, VariableStorage.UserPref.getStringDataAmt(context, VariableStorage.UserPref.WITHDRAWAL_BAL));
                        break;
                }
            } catch (NullPointerException e) {
                return;
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(context);

        }

    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
        selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };
}

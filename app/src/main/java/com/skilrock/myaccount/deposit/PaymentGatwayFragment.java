package com.skilrock.myaccount.deposit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skilrock.adapters.PGAdapter;
import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AnimatedExpandableListView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnGroupClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.MobileConfirmDialog;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.PaymentGatewayWeb;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;


public class PaymentGatwayFragment extends Fragment implements WebServicesListener {

    public static String mobNo;
    private AnimatedExpandableListView paymentGateList;
    private View mainView;
    private ArrayList<CategoryInfoBean> mPowerBeans;
    private String paymentResponse;
    private final String[] pgModes = new String[]{"Vpayments", "VISA/MasterCard", "PayNow"};
    private final String[] pgWalletProvider = new String[]{"Mpower Account", "PayPoint"};

    //private final String[] pgWalletProvider = new String[]{"VISA/MasterCard", "MTN Mobile Money", "Mpower Account"};
    private int[] pgIcons;
    private String depositAmount;
    private final String vPayPath = "/com/skilrock/pms/api/accMgmt/action/vpaymentsPayRequest.action?";
    private final String visaMasterPayPath = "/com/skilrock/pms/api/accMgmt/action/vpaymentsCardPayRequest.action?";
    private final String paymentPath = "/com/skilrock/pms/api/accMgmt/action/mpowerPayRequest.action?";
    private CustomTextView amount, edit, status, depositAmnt, subMessage, balance, message, done;
    private RelativeLayout successPage;
    private ImageView imageView;
    //lagos
    private final String[] depositeProvider = new String[]{"Deposit through Paga Account", "Deposit through Verve, MasterCard or VISA"};


    private static final int REQUEST_CODE = 0;
    private double compAmt;
    ArrayList<DepositLimitBean.PgRange> pgRng;
    private DepositLimitBean bean;
    private int vpaymentpos;
    private int vpaymentcardpos;
    private int pagaPos;
    private int interswitchPos;
    private String symbol;
    private Analytics analytics;
    private Activity activity;
    private MobileConfirmDialog mobFrag = null;
    //lagos Data
    private final String lagosPagaPath = "/com/skilrock/pms/api/accMgmt/action/pagaPayRequest.action?";
    private final String lagosInterSwitchPath = "/com/skilrock/pms/interSwitch/Action/initiateRequest.action?";

    //for payNow
    private int payNowpos;
    //new implemention for PayNow
    private final String payNowPath = "/com/skilrock/pms/api/accMgmt/action/payNowPayRequest.action?";
    private GlobalPref globalPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        analytics.setScreenName(Fields.Screen.PAYMENT_GATEWAY);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        symbol = VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_payment_gatway, null);
        depositAmount = activity.getIntent().getStringExtra("amount");

        bean = (DepositLimitBean) activity.getIntent().getSerializableExtra("bean");
        pgRng = new ArrayList<DepositLimitBean.PgRange>();
        int count = 0;
        for (int i = 0; i < bean.getPgRanges().size(); i++) {
            if (bean.getPgRanges().get(i).getPgCode().equalsIgnoreCase("pms") && GlobalPref.getInstance(activity).getCountry().equalsIgnoreCase("ghana")) {
            } else {
                pgRng.add(count, bean.getPgRanges().get(i));
                count++;
            }
        }
        bindViewIds(mainView);
        setView();


        paymentGateList.setOnGroupClickListener(new DebouncedOnGroupClickListener(500) {

            @Override
            public void onDebouncedGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                try {
                    JSONObject data = new JSONObject();
                    data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
                    data.put("depositAmt", depositAmount);
                    //for zim changes
                    data.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
                    if (GlobalVariables.connectivityExists(activity)) {
//                        if (groupPosition == 0) {
//                            new PMSWebTask(activity, vPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VPAYMENT", null, "Loading...").execute();
//                        } else {
//                            new PMSWebTask(activity, visaMasterPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VISA_MASTERCARD", null, "Loading...").execute();
//                        }
                        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
                            case "GHANA":
                                ghanaExecute(groupPosition, data);
                                break;
                            case "ZIM":
                                zimExecute(groupPosition, data);
                                break;
                            case "LAGOS":
                                lagosExecute(groupPosition);
                                break;
                            default:
                                break;
                        }

//                        if((groupPosition==0) && ((pgRng.get(vpaymentpos).getMin() <= compAmt) && (pgRng.get(vpaymentpos).getMax() >= compAmt))){
//                            analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.CLICK, Fields.Label.V_PAYMENT);
//                            new PMSWebTask(PaymentGatwayFragment.this, vPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VPAYMENT", null, "Loading...").execute();
//                        }else{
//                            if(groupPosition==0){
//                                Utils.Toast(getActivity(),"amount should in between "+symbol+pgRng.get(vpaymentpos).getMin()+" to "+symbol+pgRng.get(vpaymentpos).getMax() , Toast.LENGTH_SHORT);
//                            }
//                        }
//
//                        if((groupPosition==1) && ((pgRng.get(vpaymentcardpos).getMin() <= compAmt) && (pgRng.get(vpaymentcardpos).getMax() >= compAmt))){
//                            analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.CLICK, Fields.Label.VISA_MASTER_CARD);
//                            new PMSWebTask(PaymentGatwayFragment.this, visaMasterPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VISA_MASTERCARD", null, "Loading...").execute();
//                        }else{
//                            if(groupPosition==1){
//                                Utils.Toast(getActivity(),"amount should in between "+symbol+pgRng.get(vpaymentcardpos).getMin()+" to "+symbol+pgRng.get(vpaymentcardpos).getMax(), Toast.LENGTH_SHORT);
//                            }
//                        }


                    } else {
                        GlobalVariables.showDataAlert(activity);
                    }

                } catch (JSONException e) {
                    try {
                        new DownloadDialogBox(activity, activity.getResources().getString(R.string.some_internal_error), activity.getResources().getString(R.string.oops), false, true, null, null).show();
                    } catch (WindowManager.BadTokenException e1) {
                        GlobalVariables.showServerErr(getActivity());
                    }

                }
            }
        });


        return mainView;
    }

    private void zimExecute(int groupPosition, JSONObject data) {
        if ((groupPosition == 0) && ((pgRng.get(vpaymentpos).getMin() <= compAmt) && (pgRng.get(vpaymentpos).getMax() >= compAmt))) {
            analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.CLICK, Fields.Label.V_PAYMENT);
            new PMSWebTask(PaymentGatwayFragment.this, vPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VPAYMENT", null, "Loading...").execute();
        } else {
            if (groupPosition == 0) {
                Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(vpaymentpos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(vpaymentpos).getMax());
            }
        }

        if ((groupPosition == 1) && ((pgRng.get(vpaymentcardpos).getMin() <= compAmt) && (pgRng.get(vpaymentcardpos).getMax() >= compAmt))) {
            analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.CLICK, Fields.Label.VISA_MASTER_CARD);
            new PMSWebTask(PaymentGatwayFragment.this, visaMasterPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VISA_MASTERCARD", null, "Loading...").execute();
        } else {
            if (groupPosition == 1) {
                Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(vpaymentcardpos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(vpaymentcardpos).getMax());
            }
        }

        //new Implmetion of PayNow
        if ((groupPosition == 2) && ((pgRng.get(payNowpos).getMin() <= compAmt) && (pgRng.get(payNowpos).getMax() >= compAmt))) {
            new PMSWebTask(PaymentGatwayFragment.this, payNowPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "PAY_NOW", null, "Loading...").execute();
        } else {
            if (groupPosition == 2) {
                Utils.Toast(activity, activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(payNowpos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(payNowpos).getMax());
            }
        }
    }

    private void lagosExecute(final int groupPosition) {
        try {
            Intent intent = new Intent(activity, PaymentGatewayWeb.class);
            JSONObject data = new JSONObject();
            if ((groupPosition == 0)) {
                if ((pgRng.get(pagaPos).getMin() <= compAmt) && (pgRng.get(pagaPos).getMax() >= compAmt)) {
                    data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
                    data.put("depositAmount", depositAmount);
                    data.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
                    intent.putExtra("gatewayName", "Paga Money Transfer");
                    intent.putExtra("redirectUrl", Config.getInstance().getBaseURL() + lagosPagaPath + "depositRequest=" + URLEncoder.encode(data.toString()));
                    activity.startActivityForResult(intent, REQUEST_CODE);
//                new PMSWebTask(PaymentGatwayFragment.this, lagosPagaPath + "depositRequest=" + URLEncoder.encode(data.toString()), "N/A", null, "LAGOSPAGE", null, "Loading...").execute();
                } else {
                    Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(pagaPos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(pagaPos).getMax());
                }
            } else if ((groupPosition == 1)) {
                if ((pgRng.get(interswitchPos).getMin() <= compAmt) && (pgRng.get(interswitchPos).getMax() >= compAmt)) {
                    data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
                    data.put("amount", depositAmount);
                    data.put("sessionId", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.SESSION_ID));
                    intent.putExtra("gatewayName", "Interswitch Account Deposit");
                    intent.putExtra("redirectUrl", Config.getInstance().getBaseURL() + lagosInterSwitchPath + "requestData=" + URLEncoder.encode(data.toString()));
                    activity.startActivityForResult(intent, REQUEST_CODE);
//                new PMSWebTask(PaymentGatwayFragment.this, lagosInterSwitchPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "LAGOSMASTER", null, "Loading...").execute();
                } else {
                    Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(interswitchPos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(interswitchPos).getMax());
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void ghanaExecute(final int groupPosition, JSONObject data) {
        int isLots = -1;
        int isMPower = -1;

        for (int i = 0; i < pgRng.size(); i++) {
            if (pgRng.get(i).getPgCode().equalsIgnoreCase("mpower"))
                isMPower = i;
            else if (pgRng.get(i).getPgCode().equalsIgnoreCase("lots"))
                isLots = i;
        }

        if ((groupPosition == 0)) {
            if (isMPower > -1) {
                if (Double.parseDouble(depositAmount) < pgRng.get(isMPower).getMin() || Double.parseDouble(depositAmount) > pgRng.get(isMPower).getMax()) {
                    Utils.Toast(activity, getString(R.string.amtvalidation,
                                    symbol + pgRng.get(isMPower).getMin())
                                    + symbol + pgRng.get(isMPower).getMax()
                    );
                } else {
                    new PMSWebTask(PaymentGatwayFragment.this, paymentPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "M_POWER", null, "Loading...").execute();
                }
            } else {
                Utils.Toast(activity, "Internal system error");
            }
//        } else if ((groupPosition == 1)) {
//            if (isMPower > -1) {
//                if (Double.parseDouble(depositAmount) < pgRng.get(isMPower).getMin() || Double.parseDouble(depositAmount) > pgRng.get(isMPower).getMax()) {
//                    Utils.Toast(activity, getString(R.string.amtvalidation, symbol + pgRng.get(isMPower).getMin()
//                                    + symbol + pgRng.get(isMPower).getMax()),
//                            Toast.LENGTH_SHORT);
//                } else {
//                    new PMSWebTask(PaymentGatwayFragment.this, paymentPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "MASTER", null, "Loading...").execute();
//                }
//            } else {
//                Utils.Toast(activity, "Internal system error", Toast.LENGTH_SHORT);
//            }
//        } else if ((groupPosition == 2)) {
//            if (isMPower > -1) {
//                if (Double.parseDouble(depositAmount) < pgRng.get(isMPower).getMin() || Double.parseDouble(depositAmount) > pgRng.get(isMPower).getMax()) {
//                    Utils.Toast(activity, getString(R.string.amtvalidation, symbol + pgRng.get(isMPower).getMin()
//                                    + symbol + pgRng.get(isMPower).getMax()),
//                            Toast.LENGTH_SHORT);
//                } else {
//                    new PMSWebTask(PaymentGatwayFragment.this, paymentPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VISA", null, "Loading...").execute();
//                }
//            } else {
//                Utils.Toast(activity, "Internal system error", Toast.LENGTH_SHORT);
//            }
//        } else if ((groupPosition == 3)) {
//            if (isMPower > -1) {
//                if (Double.parseDouble(depositAmount) < pgRng.get(isMPower).getMin() || Double.parseDouble(depositAmount) > pgRng.get(isMPower).getMax()) {
//                    Utils.Toast(activity, getString(R.string.amtvalidation, symbol + pgRng.get(isMPower).getMin())
//                                    + symbol + pgRng.get(isMPower).getMax(),
//                            Toast.LENGTH_SHORT);
//                } else {
//                    new PMSWebTask(PaymentGatwayFragment.this, paymentPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "M_POWER", null, "Loading...").execute();
//                }
//            } else {
//                Utils.Toast(activity, "Internal system error", Toast.LENGTH_SHORT);
//            }
        } else if ((groupPosition == 1)) {

            View.OnClickListener cancel = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mobFrag.dismiss();
                }
            };
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mobFrag.dismiss();
                    lotsRequest(groupPosition);
//                    mobFrag = null;
                }
            };
            mobFrag = new MobileConfirmDialog(activity, null, "MOBILE CONFIRMATION", true, true, okClickListener, cancel);
            if (isLots > -1) {
                if (Double.parseDouble(depositAmount) < pgRng.get(isLots).getMin() || Double.parseDouble(depositAmount) > pgRng.get(isLots).getMax()) {
                    Utils.Toast(activity, getString(R.string.amtvalidation, symbol + pgRng.get(isLots).getMin()
                            ) + symbol + pgRng.get(isLots).getMax()
                    );
                } else {
                    mobFrag.show();
                }
            } else {
                Utils.Toast(activity, "Internal system error");
            }

        }
    }

    private void lotsRequest(int position) {
        try {
            int playid = Integer.parseInt(VariableStorage.UserPref.getStringData(PaymentGatwayFragment.this.getActivity(), VariableStorage.UserPref.PLAYER_ID));
            double depositAt = Double.parseDouble(depositAmount.toString());
            JSONObject data = new JSONObject();
            data.put("playerId", playid);
            data.put("mobileNumber", mobNo);
            data.put("depositAmt", depositAt);
            String path = "/rest/lotsPayment/lotsDepositTxn";//rest/lotsPayment/lotsDepositTxn
            int mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));
            if ((mobNo.length() == mobilelen)) {
                mobFrag.dismiss();
                new PMSWebTask(PaymentGatwayFragment.this, path, "N/A", data, "LOTS_REQ", null, "Requesting deposit...").execute();
//                Utils.Toast(activity, activity.getResources().getString(R.string.mobile_no_validate) + mobilelen, Toast.LENGTH_SHORT);
//                new PMSWebTask(PaymentGatwayFragment.this, path, "", data, "LOTS_REQ", null, "Requesting withdraw...").execute();
            } else {
//                if (((mobNo.length() == (mobilelen + 1)) && mobNo.startsWith("0")))
//                    Utils.Toast(activity, activity.getResources().getString(R.string.mobile_validate_zero), Toast.LENGTH_SHORT);
//                else
                Utils.Toast(activity, activity.getResources().getString(R.string.mobile_no_validate) + mobilelen);
//                new PMSWebTask(PaymentGatwayFragment.this, path, "N/A", data, "LOTS_REQ", null, "Requesting withdraw...").execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void bindViewIds(View view) {
        paymentGateList = (AnimatedExpandableListView) view.findViewById(R.id.list_view_mob_mony);
    }

    private void setView() {
        paymentGateList.setVisibility(View.VISIBLE);
        mPowerBeans = new ArrayList<>();
        CategoryInfoBean bean;
        int PgLength = 0;
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "ZIM":
                PgLength = pgModes.length;
                break;
            case "GHANA":
                PgLength = pgWalletProvider.length;
                break;
            case "LAGOS":
                PgLength = depositeProvider.length;
                break;
            default:
                break;
        }

        for (int i = 0; i < PgLength; i++) {
            switch (globalPref.getCountry().toUpperCase(Locale.US)) {
                case "ZIM":
                    pgIcons = new int[]{R.drawable.vpay, R.drawable.master_visa_card, R.drawable.paynow};
                    bean = new CategoryInfoBean(pgIcons[i], pgModes[i], "");
                    mPowerBeans.add(bean);
                    break;
                case "LAGOS":
                    pgIcons = new int[]{R.drawable.paga_reg, R.drawable.master_visa_card};
                    bean = new CategoryInfoBean(pgIcons[i], depositeProvider[i], "");
                    mPowerBeans.add(bean);
                    break;
                case "GHANA":
                    pgIcons = new int[]{R.drawable.mpower, R.drawable.card1};
                    bean = new CategoryInfoBean(pgIcons[i], pgWalletProvider[i], "");
                    mPowerBeans.add(bean);
                    break;
                default:
                    break;
            }
//            CategoryInfoBean bean = new CategoryInfoBean(
//                    pgIcons[i], pgModes[i], "");
//            mPowerBeans.add(bean);
        }

        paymentGateList.setAdapter(new PGAdapter(getActivity(), mPowerBeans, pgRng));

        compAmt = Double.valueOf(depositAmount).doubleValue();

        for (int i = 0; i < pgRng.size(); i++) {
            if (pgRng.get(i).getPgCode().equalsIgnoreCase("vpayment")) {
                vpaymentpos = i;
            } else if (pgRng.get(i).getPgCode().equalsIgnoreCase("vpaymentcard")) {
                vpaymentcardpos = i;
            } else if (pgRng.get(i).getPgCode().equalsIgnoreCase("paga")) {
                pagaPos = i;
            } else if (pgRng.get(i).getPgCode().equalsIgnoreCase("interswitch")) {
                interswitchPos = i;
            } else if (pgRng.get(i).getPgCode().equalsIgnoreCase("paynow")) {
                //Pay Now intergation
                payNowpos = i;
            }
        }


        // Comment because of duplicate of Listener
//        paymentGateList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//                if ((groupPosition == 0) && ((pgRng.get(vpaymentpos).getMin() <= compAmt) && (pgRng.get(vpaymentpos).getMax() >= compAmt))) {
//
//                    if (paymentGateList.isGroupExpanded(groupPosition)) {
//                        paymentGateList.removeAllViewsInLayout();
//                        paymentGateList.collapseGroupWithAnimation(groupPosition);
//                    } else {
//                        paymentGateList.removeAllViewsInLayout();
//                        paymentGateList.expandGroupWithAnimation(groupPosition);
//                    }
//                } else {
//                    if (groupPosition == 0) {
//                        Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(vpaymentpos).getMin() + "  " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(vpaymentpos).getMax(), Toast.LENGTH_SHORT);
//                    }
//                }
//
//
//                if ((groupPosition == 1) && ((pgRng.get(vpaymentcardpos).getMin() <= compAmt) && (pgRng.get(vpaymentcardpos).getMax() >= compAmt))) {
//
//                    if (paymentGateList.isGroupExpanded(groupPosition)) {
//                        paymentGateList.removeAllViewsInLayout();
//                        paymentGateList.collapseGroupWithAnimation(groupPosition);
//                    } else {
//                        paymentGateList.removeAllViewsInLayout();
//                        paymentGateList.expandGroupWithAnimation(groupPosition);
//                    }
//                } else {
//                    if (groupPosition == 1) {
//                        Utils.Toast(activity, activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(vpaymentcardpos).getMin() + " " + activity.getResources().getString(R.string.to) + " " + symbol + pgRng.get(vpaymentcardpos).getMin(), Toast.LENGTH_SHORT);
//                    }
//                }
//
//
//                return true;
//            }
//        });


    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (resultData != null) {
            try {
                final JSONObject json = new JSONObject((String) resultData);
                //lotsInt
                if (methodType.equalsIgnoreCase("LOTS_REQ")) {
                    if (json.getInt("responseCode") == 0) {
                        dialog.dismiss();
                        View.OnClickListener okClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.finish();
                                try {
                                    VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, json.getString("balance"));
                                    VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, json.getString("depositBal"));
                                    VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, json.getString("winningBal"));
                                    VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, json.getString("withdrawlBal"));
                                    VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, json.getString("bonusBal"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    new DownloadDialogBox(activity, "Some internal error", "Information", false, true, null, null).show();
                                }
                            }
                        };

                        new DownloadDialogBox(getActivity(), json.getString("responseMsg"), "", false, true, okClickListener, null).show();

                    } else if (json.getInt("responseCode") == 118) {
                        dialog.dismiss();
                        View.OnClickListener okClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawerBaseActivity.selectedItemId = -1;
                                UserInfo.setLogout(activity);
                                Intent intent = new Intent(activity,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        };

                        new DownloadDialogBox(activity, json.getString("responseMsg"), "", false, true, okClickListener, null).show();


                    } else {
                        dialog.dismiss();
                        new DownloadDialogBox(activity, json.getString("responseMsg"), "", false, true, null, null).show();
                    }

                } else {

                    if (GlobalVariables.connectivityExists(activity)) {

                        Intent intent = new Intent(activity, PaymentGatewayWeb.class);
                        if (json.getBoolean("isSuccess")) {
                            switch (methodType) {
                                case "VPAYMENT":
                                    analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.GET, Fields.Label.V_PAYMENT + " " + Fields.Label.SUCCESS);
                                    String redirectUrl = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl);
                                    intent.putExtra("gatewayName", "VPAYMENTS");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;
                                case "VISA_MASTERCARD":
                                    analytics.sendAll(Fields.Category.PAYMENT_GATEWAY, Fields.Action.GET, Fields.Label.VISA_MASTER_CARD + " " + Fields.Label.SUCCESS);
                                    String redirectUrl1 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl1);
                                    intent.putExtra("gatewayName", "VISA/MASTERCARD");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;
                                case "VISA":
                                    String redirectUrl2 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl2);
                                    intent.putExtra("gatewayName", "VISA");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;

                                //for payNow in zim
                                case "PAY_NOW":
                                    String redirectUrlPay = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrlPay);
                                    intent.putExtra("gatewayName", "PAYNOW");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;

                                case "MASTER":
                                    String redirectUrl4 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl4);
                                    intent.putExtra("gatewayName", "MASTER CARD");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;

                                case "MTN_MOBILE":
                                    String redirectUrl3 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl3);
                                    intent.putExtra("gatewayName", "MTN MOBILE MONEY");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;
                                case "M_POWER":
                                    String redirectUrl5 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl5);
                                    intent.putExtra("gatewayName", "MPOWER ACCOUNT");
                                    activity.startActivityForResult(intent, REQUEST_CODE);
                                    break;
                            }
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();

                            if (json.getLong("errorCode") == 2002) {
                                new DownloadDialogBox(getActivity(), json.getString("errorMsg"), "", false, true, null, null).show();
                            } else if (json.getLong("errorCode") == 2001) {
                                new DownloadDialogBox(getActivity(), activity.getResources().getString(R.string.vpayment_con_err), "", false, true, null, null).show();
                            } else if (json.getLong("errorCode") == 163) {
                                new DownloadDialogBox(getActivity(), activity.getResources().getString(R.string.req_pay_not_inti), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 2003) {
                                new DownloadDialogBox(getActivity(), activity.getResources().getString(R.string.vpay_req_id_not_avl), "", false, true, null, null).show();
                            } else if (json.getLong("errorCode") == 2004) {
                                new DownloadDialogBox(getActivity(), activity.getResources().getString(R.string.req_id_not_avl), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 118) {
                                View.OnClickListener okClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        UserInfo.setLogout(activity);
                                        Intent intent = new Intent(activity,
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                };

                                new DownloadDialogBox(getActivity(), /*txnBean.getErrorMsg()*/activity.getResources().getString(R.string.time_out), "", false, true, okClickListener, null).show();
                            } else {
                                new DownloadDialogBox(activity, json.getString("errorMsg"), "", false, true, null, null).show();

                            }

                        }
                    } else {
                        GlobalVariables.showDataAlert(activity);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                GlobalVariables.showServerErr(activity);
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(activity);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);
    }
}

package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skilrock.adapters.ExpandableListAdapter;
import com.skilrock.adapters.WithdrawPGExpAdapter;
import com.skilrock.bean.BankDetailsWithdraw;
import com.skilrock.bean.WithdrawalOptionsBean;
import com.skilrock.bean.WithdrawalPGBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DecimalDigitsInputFilter;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WithdrawalScreen extends Fragment implements WebServicesListener {
    private LinearLayout tabParent;
    private String[] tabTitles;
    private int[] tabTitlesId;
    protected int width;
    private float weight;
    private CustomTextView currencySymbol;
    private ExpandableListAdapter listAdapter;
    private ListView paymentGateList;
    //    private AnimatedExpandableListView paymentGateList;
    private int lastExpandedPosition = -1;
    private EditText amountView;
    private double amount = 5.00;
    private String amtstr = "";
    private DownloadDialogBox dBox = null;
    private ImageView edit;

    private int previousId = -1;// used for preventing reopening the selected

    private int[] pgIds = new int[]{1, 2, 3, 4};
    //    private ArrayList<WithdrawalPGBean> pgBeanList;
    private ArrayList<String> withdrawlModeNameList = new ArrayList<String>();
    private ArrayList<String> withdrawlModeKeyList = new ArrayList<String>();

    private Activity activity;
    private Analytics analytics;
    private static boolean isGhana, isLagos, isZim;
    private ArrayList<String> providerListCodeMobile = new ArrayList<String>();
    private ArrayList<String> providerListNameMobile = new ArrayList<String>();
    private ArrayList<String> providerListCodeBank = new ArrayList<String>();
    private ArrayList<String> providerListNameBank = new ArrayList<String>();
    private DownloadDialogBox downloadDialog;
    WithdrawPGExpAdapter withdrawPGExpAdapter;

    //lagos Data
    private HashMap<String, ArrayList<String>> lagosWithdrawal = new HashMap<>();
    private ArrayList<String> modeDataLagos = new ArrayList<>();
    private ArrayList<String> modeKeyLagos = new ArrayList<>();
    private ArrayList<String> interswitchProviderBankName = new ArrayList<>();
    private ArrayList<String> interswitchProviderBankKey = new ArrayList<>();
    private ArrayList<String> accountType = new ArrayList<>();
    private HashMap<String, BankDetailsWithdraw> playerRegData = new HashMap<>();

    private GlobalPref globalPref;


    @Override
    public void onResume() {
        super.onResume();
        String languageToLoad = "en_US"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        if (amountView.getText().toString().replace(",", "").equalsIgnoreCase("") || amountView.getText().toString().replace(",", "") == null) {
            amountView.setText(Double.toString(amount).replace(",", ""));
//            GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
        } else {
            GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
        }

        //old
//        GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        countryTypeData();
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        analytics.setScreenName(Fields.Screen.WITHDRAWAL_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //defaultLocale();
        width = displaymetrics.widthPixels;
        tabTitles = new String[]{"Withdrawal Options"};
        tabTitlesId = new int[]{1};
        View view = inflater.inflate(R.layout.withdrawal_screen, null);
        bindViewIds(view);
        edit.setOnClickListener(applyEditClick());
        amountView.setEnabled(false);
        try {
            JSONObject data = new JSONObject();
            data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
            String path = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlModeAction.action?requestData=" + URLEncoder.encode(data.toString());
            new PMSWebTask(WithdrawalScreen.this, path, "", null, "WITHDRAWAL", null, "Loading withdraw modes...").execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Utils.Toast(activity, "No Withdrawal Options Available");
        }
        checkPlayerBankRegistration();
        getBankListFromWithdrawBank();
        //amtstr=Double.toString(amount).replace(",","");
        amountView.setText(Double.toString(amount).replace(",", ""));
        return view;
    }

    private void checkPlayerBankRegistration() {
        JSONObject json = null;
        JSONObject data = null;
        try {
            data = new JSONObject();
            data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
            data.put("channelType", "BANK_WITHDRAWAL");
            String path = "/com/skilrock/pms/mobile/accMgmt/Action/checkFetchPlayerRegisteredBank.action?requestData=" + URLEncoder.encode(data.toString());
            new PMSWebTask(WithdrawalScreen.this, path, "", null, "CHECKPLAYERBANKDATA", null, "Loading withdraw modes...").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBankListFromWithdrawBank() {
        try {
            String requestType = "Withdrawal";
            String userName = VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME);
            JSONObject data = null;
            String path = "/com/skilrock/pms/mobile/accMgmt/Action/getBankDetails.action?";
            String params = "userName=" + userName + "&" + "requestType="
                    + requestType;
            new PMSWebTask(WithdrawalScreen.this, path + params, "", null, "getBankListWithdraw", null, "Loading withdraw modes...").execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerBankAcc() {
        JSONObject json = null;
        JSONObject data = null;
        String path = "com/skilrock/pms/mobile/accMgmt/Action/registerPlayerBank.action?";
        try {
            data = new JSONObject();
            data.put("playerName", VariableStorage.UserPref.getStringData(activity, VariableStorage.UserPref.USER_NAME));
//            data.put("bankId", bankId);
//            data.put("branchName", branchName);
//            data.put("accName", accName);
//            data.put("accNbr", accNbr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void countryTypeData() {
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "GHANA":
                amount = 5.00;
                isZim = false;
                isGhana = true;
                isLagos = false;
                break;
            case "ZIM":
                amount = 5.00;
                isZim = true;
                isGhana = false;
                isLagos = false;
                break;
            case "LAGOS":
                amount = 500.00;
                isZim = false;
                isGhana = false;
                isLagos = true;
                break;
            default:
                break;
        }
    }

    private void defaultLocale() {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getResources().updateConfiguration(config, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void bindViewIds(View view) {
        paymentGateList = (ListView) view.findViewById(R.id.list_view);
        amountView = (EditText) view.findViewById(R.id.amount);
        amountView.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
        amountView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setSelectAmountTag(v);
                    return true;
                }
                return false;
            }
        });
//        paymentGateList = (AnimatedExpandableListView) view.findViewById(R.id.exp_list);
//        paymentGateList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        tabParent = (LinearLayout) view.findViewById(R.id.custom_tabs);
        currencySymbol = (CustomTextView) view.findViewById(R.id.currencySymbol);
        edit = (ImageView) view.findViewById(R.id.edit);
        tabParent.setVisibility(View.VISIBLE);
        tabParent.removeAllViews();
        currencySymbol.setText(VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE));
        for (int i = 0; i < tabTitles.length; i++) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//            params.weight = weight;
            View tabView = activity.getLayoutInflater().inflate(R.layout.tab_view, null);
            CustomCheckedTextView textView = (CustomCheckedTextView) tabView
                    .findViewById(R.id.tab_views);
            textView.setText(tabTitles[i]);
            textView.setId(tabTitlesId[i]);
            textView.setLayoutParams(params);
//            textView.setOnClickListener(getClickListener(textView));
            Drawable drawable = getResources().getDrawable(
                    R.drawable.strip_down_d);
            drawable.setColorFilter(Color.parseColor("#E7F6F9"), Mode.SRC_IN);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                    drawable);
            tabParent.addView(tabView);
        }
        CustomCheckedTextView firstChild = (CustomCheckedTextView) tabParent
                .getChildAt(0);
        Drawable drawable = getResources().getDrawable(R.drawable.strip_down1);
        if (tabTitles.length > 1)
            drawable.setColorFilter(getResources().getColor(R.color.txn_cal_month), Mode.SRC_IN);//txn_cal_month
        else
            drawable.setColorFilter(getResources().getColor(R.color.txn_cal_year), Mode.SRC_IN);//txn_cal_year

        firstChild.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                drawable);
    }

//    private OnClickListener getClickListener(final CustomCheckedTextView clicked) {
//        OnClickListener clickListener = new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i < tabParent.getChildCount(); i++) {
//                    CustomCheckedTextView view = (CustomCheckedTextView) tabParent
//                            .getChildAt(i);
//                    if (view.getId() == v.getId()) {
//                        CustomCheckedTextView textView = (CustomCheckedTextView) tabParent
//                                .getChildAt(i);
//                        textView.setCompoundDrawablesWithIntrinsicBounds(
//                                null,
//                                null,
//                                null,
//                                getResources().getDrawable(
//                                        R.drawable.strip_down));
//                        int scrollX = (view.getLeft() - (width / 2))
//                                + (view.getWidth() / 2);
//                        // scrollView.smoothScrollTo(scrollX, 0);
//                    } else {
//                        CustomCheckedTextView textView = (CustomCheckedTextView) tabParent
//                                .getChildAt(i);
//                        Drawable drawable = getResources().getDrawable(
//                                R.drawable.strip_down_d);
//                        drawable.setColorFilter(Color.parseColor("#E7F6F9"),
//                                Mode.SRC_IN);
//                        textView.setCompoundDrawablesWithIntrinsicBounds(null,
//                                null, null, drawable);
//                    }
//                }
//                setView(clicked.getId());
//
//            }
//        };
//        return clickListener;
//    }


    private OnClickListener applyEditClick() {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountView.isEnabled()) {
                    try {
                        if (Double.parseDouble(amountView.getText().toString().replace(",", "")) > 0) {
                            GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                            amount = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                            amountView.setEnabled(false);
                            withdrawPGExpAdapter.setRsCheck(false);
                            amountView.setCursorVisible(false);
                            amountView.setBackgroundColor(Color.TRANSPARENT);
                            int dimen = Float.valueOf(activity.getResources().getDimension(R.dimen._8sdp)).intValue();
                            amountView.setPadding(0, dimen, dimen, dimen);
                            amountView.setText(AmountFormat.getAmountFormatForMobileDecimal(amount).replace(",", ""));
                            edit.setBackgroundColor(getResources().getColor(R.color.trans));
                            edit.setBackgroundResource(R.drawable.edit_nos);
                            edit.setPadding(0, 0, 0, 0);
                        } else {
                            Utils.Toast(activity, "Invalid Amount");
                        }
                    } catch (NumberFormatException e) {
                        Utils.Toast(activity, "Invalid Amount");
                    }

                } else {
                    String a = amountView.getText().toString().replace(",", "");
                    amountView.setText(a);
                    withdrawPGExpAdapter.setRsCheck(true);
                    amountView.setEnabled(true);
                    int dimen = Float.valueOf(activity.getResources().getDimension(R.dimen._8sdp)).intValue();
                    amountView.setPadding(dimen, dimen, dimen, dimen);
                    amountView.requestFocus();
                    amountView.setCursorVisible(true);
                    edit.setBackgroundResource(R.drawable.done_icon);
                    edit.setPadding(dimen, dimen, dimen, dimen);
                    //amount = Double.parseDouble(amountView.getText().toString());


                    String amt = amountView.getText().toString();
                    if (amt.replace(",", "").length() >= 8) {
                    } else {
                        amt = amt.replace(",", "");
                        try {
                            GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                            amount = Double.parseDouble(amt);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }


                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        amountView.setBackgroundDrawable(getResources().getDrawable(R.drawable.withdraw_amnt_bg));
                    } else {
                        amountView.setBackground(getResources().getDrawable(R.drawable.withdraw_amnt_bg));
                    }

                }
            }
        };
        return clickListener;
    }

    private void setView(int id) {

        switch (id) {
            case 1:
                paymentGateList.setVisibility(View.VISIBLE);
                ArrayList<WithdrawalPGBean> pgBeanList = new ArrayList<>();
                for (int i = 0; i < withdrawlModeNameList.size(); i++) {
                    WithdrawalPGBean bean = new WithdrawalPGBean("", withdrawlModeNameList.get(i), withdrawlModeKeyList.get(i));
                    pgBeanList.add(bean);
                }
                paymentGateList.setAdapter(new WithdrawPGExpAdapter(this, R.layout.expandable_group_lay, pgBeanList, providerListCodeMobile, providerListNameMobile, providerListCodeBank, providerListNameBank, WithdrawalScreen.this, true, true, lagosWithdrawal));

                paymentGateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        try {
                            analytics.sendAction(Fields.Category.WITHDRAWAL, Fields.Action.CLICK + " " + withdrawlModeNameList.get(position));

                            if (Double.parseDouble(amountView.getText().toString()) > 0) {
                                amount = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                                OnClickListener cancel = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dBox.dismiss();
                                        dBox = null;
                                    }
                                };
                                OnClickListener okay = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dBox.dismiss();
                                        withdrawalRequest(position);
                                        dBox = null;
                                    }
                                };
                                if (dBox == null) {
                                    dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(position) + " ?", "Confirmation", true, true, okay, cancel);
                                    dBox.show();
                                }
//                                dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(position) + " ?", "Confirmation", true, true, okay, cancel);
//                                dBox.show();
                            } else {
                                Utils.Toast(activity, "Invalid Amount");
                            }
                        } catch (NumberFormatException e) {
                            Utils.Toast(activity, "Invalid Amount");
                        }

                    }
                });
                break;

            case 2:
                paymentGateList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                paymentGateList.setVisibility(View.VISIBLE);
                ArrayList<WithdrawalPGBean> pgBeanList1 = new ArrayList<WithdrawalPGBean>();
                for (int i = 0; i < withdrawlModeNameList.size(); i++) {
                    WithdrawalPGBean bean = new WithdrawalPGBean("", withdrawlModeNameList.get(i), withdrawlModeKeyList.get(i));
                    pgBeanList1.add(bean);
                }
                paymentGateList.setAdapter(new WithdrawPGExpAdapter(this, R.layout.expandable_group_lay, pgBeanList1, providerListCodeMobile, providerListNameMobile, providerListCodeBank, providerListNameBank, WithdrawalScreen.this, true, false, lagosWithdrawal));
//                paymentGateList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//                    @Override
//                    public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
//
//                        if (isZim) {
//                            try {
//                                analytics.sendAction(Fields.Category.WITHDRAWAL, Fields.Action.CLICK + " " + withdrawlModeNameList.get(groupPosition));
//                                if (Double.parseDouble(amountView.getText().toString()) > 0) {
//                                    amount = Double.parseDouble(amountView.getText().toString().replace(",", ""));
//                                    OnClickListener cancel = new OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dBox.dismiss();
//                                            dBox = null;
//                                        }
//                                    };
//                                    OnClickListener okay = new OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dBox.dismiss();
//                                            withdrawalRequest(groupPosition);
//                                            dBox = null;
//                                        }
//                                    };
//                                    if (dBox == null) {
//                                        dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(groupPosition) + " ?", "Confirmation", true, true, okay, cancel);
//                                        dBox.show();
//                                    }
////                                dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(position) + " ?", "Confirmation", true, true, okay, cancel);
////                                dBox.show();
//                                } else {
//                                    Utils.Toast(activity, "Invalid Amount", Toast.LENGTH_SHORT);
//                                }
//                            } catch (NumberFormatException e) {
//                                Utils.Toast(activity, "Invalid Amount", Toast.LENGTH_SHORT);
//                            }
//
//                        } else if (isGhana) {
//                            paymentGateList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//                            if (paymentGateList.isGroupExpanded(groupPosition)) {
//                                paymentGateList.removeAllViewsInLayout();
//                                GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
//                                paymentGateList.collapseGroupWithAnimation(groupPosition);
//                            } else {
//                                paymentGateList.removeAllViewsInLayout();
//                                GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
//                                paymentGateList.expandGroupWithAnimation(groupPosition);
//                            }
//                        } else {
//
//                        }
//                        return true;
//                    }
//                });
//
//                paymentGateList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//                    @Override
//                    public void onGroupExpand(int groupPosition) {
//                        hideKeyboard();
//                        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
//                            paymentGateList.collapseGroupWithAnimation(lastExpandedPosition);
//                        }
//                        lastExpandedPosition = groupPosition;
//                    }
//                });
//                paymentGateList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//                    @Override
//                    public void onGroupCollapse(int groupPosition) {
//                        hideKeyboard();
//                    }
//                });
                break;

            case 3:
                paymentGateList.setVisibility(View.VISIBLE);
                paymentGateList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                ArrayList<WithdrawalPGBean> pgBeanList2 = new ArrayList<>();
                for (int i = 0; i < withdrawlModeNameList.size(); i++) {
                    WithdrawalPGBean bean = new WithdrawalPGBean("", withdrawlModeNameList.get(i), withdrawlModeKeyList.get(i));
                    pgBeanList2.add(bean);
                }
                GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                withdrawPGExpAdapter = new WithdrawPGExpAdapter(this, R.layout.expandable_group_lay, pgBeanList2, providerListCodeMobile, providerListNameMobile, providerListCodeBank, providerListNameBank, WithdrawalScreen.this, isGhana, isLagos, lagosWithdrawal, playerRegData);
                paymentGateList.setAdapter(withdrawPGExpAdapter);
                paymentGateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        if (isZim) {
                            try {
                                analytics.sendAction(Fields.Category.WITHDRAWAL, Fields.Action.CLICK + " " + withdrawlModeNameList.get(position));
                                if (Double.parseDouble(amountView.getText().toString()) > 0) {
                                    amount = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                                    OnClickListener cancel = new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dBox.dismiss();
                                            dBox = null;
                                        }
                                    };
                                    OnClickListener okay = new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dBox.dismiss();
                                            withdrawalRequest(position);
                                            dBox = null;
                                        }
                                    };
                                    if (dBox == null) {
                                        dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(position) + " ?", "CONFIRMATION", true, true, okay, cancel);
                                        dBox.show();
                                    }
//                                dBox = new DownloadDialogBox(activity, "Are you sure to withdraw " + VariableStorage.GlobalPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.GlobalPref.CURRENCY_CODE) + String.valueOf(Double.valueOf(amount)) + " through " + withdrawlModeNameList.get(position) + " ?", "Confirmation", true, true, okay, cancel);
//                                dBox.show();
                                } else {
                                    Utils.Toast(activity, "Invalid Amount");
                                }
                            } catch (NumberFormatException e) {
                                Utils.Toast(activity, "Invalid Amount");
                            }
                        } else if (isGhana) {
                            parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                        }
                    }
                });


                paymentGateList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                        if (scrollState != 0) {

                            InputMethodManager inputMethodManger = (InputMethodManager) activity.getSystemService(Activity
                                    .INPUT_METHOD_SERVICE);
                            inputMethodManger.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });

                break;

        }
    }

    private void withdrawalRequest(int position) {
        try {
            JSONObject data = new JSONObject();
            data.put("playerName", VariableStorage.UserPref.getStringData(WithdrawalScreen.this.activity, VariableStorage.UserPref.USER_NAME));
            data.put("withdrawlAmount", amount);
            data.put("withdrawlChannel", withdrawlModeKeyList.get(position));
            String path = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?withdrawlData=" + URLEncoder.encode(data.toString());
            new PMSWebTask(WithdrawalScreen.this, path, "", null, "WITHDRAWAL_REQ", null, "Requesting withdraw...").execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (resultData != null) {
            switch (methodType) {
                case "Modified_withdraw":
                    try {
                        dialog.dismiss();
                        final JSONObject jsonObject = new JSONObject(String.valueOf(Html.fromHtml(resultData.toString())));
                        if (jsonObject.getBoolean("isSuccess")) {
                            Gson gson = new Gson();
                            BankDetailsWithdraw playerRegDataBean = gson.fromJson(resultData.toString(), BankDetailsWithdraw.class);
                            playerRegDataBean.setIsReg(true);
//                            playerRegData.get("bankDetailRegWithdrawName").
                            playerRegData.put("bankDetailRegWithdrawName", playerRegDataBean);
                            //Your Account is successfully registered
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    setView(3);
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, "Your Account is successfully registered", "", false, true, okClickListener, null);
                            downloadDialog.show();
                        } else if (jsonObject.getInt("errorCode") == 118) {
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    DrawerBaseActivity.selectedItemId = -1;
                                    UserInfo.setLogout(activity);
                                    Intent intent = new Intent(activity,
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    activity.overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                                }
                            };

                            new DownloadDialogBox(activity, jsonObject.getString("errorMsg"), "", false, true, okClickListener, null).show();
                        } else {
                            GlobalVariables.showServerErr(activity);
                        }
                    } catch (Exception ex) {
                        dialog.dismiss();
                        ex.printStackTrace();
                    }
                    break;

                case "getBankListWithdraw":
                    try {
                        dialog.dismiss();
                        final JSONObject jsonObject = new JSONObject(String.valueOf(Html.fromHtml(resultData.toString())));
                        if (jsonObject.getBoolean("isSuccess")) {
                            ArrayList<String> bankListWithdrawName = new ArrayList<String>();
                            ArrayList<String> bankIdListWithdrawName = new ArrayList<String>();
                            Gson gson = new Gson();
                            BankDetailsWithdraw bankWithdraw = gson.fromJson(resultData.toString(), BankDetailsWithdraw.class);
                            for (int i = 0; i < bankWithdraw.getBankDetails().length; i++) {
                                bankListWithdrawName.add(bankWithdraw.getBankDetails()[i].getBankName());
                                bankIdListWithdrawName.add(bankWithdraw.getBankDetails()[i].getBankId());
                            }
                            lagosWithdrawal.put("bankDetailWithdrawName", bankListWithdrawName);
                            lagosWithdrawal.put("bankIdDetailWithdrawName", bankIdListWithdrawName);
                        } else {
                            GlobalVariables.showServerErr(activity);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dialog.dismiss();
                    }
                    break;

                case "CHECKPLAYERBANKDATA":
                    try {
                        dialog.dismiss();
                        final JSONObject jsonObject = new JSONObject(String.valueOf(Html.fromHtml(resultData.toString())));
                        if (jsonObject.getBoolean("isSuccess")) {
                            Gson gson = new Gson();
                            BankDetailsWithdraw playerRegDataBean = gson.fromJson(resultData.toString(), BankDetailsWithdraw.class);
                            playerRegData.put("bankDetailRegWithdrawName", playerRegDataBean);
                        } else if (jsonObject.getInt("errorCode") == 118) {
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    DrawerBaseActivity.selectedItemId = -1;
                                    UserInfo.setLogout(activity);
                                    Intent intent = new Intent(activity,
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    activity.overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                                }
                            };
                            new DownloadDialogBox(activity, jsonObject.getString("errorMsg"), "", false, true, okClickListener, null).show();
                        } else {
                            GlobalVariables.showServerErr(activity);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dialog.dismiss();
                    }
                    break;
                case "lotsWithdrawIc":
                    try {
                        final JSONObject jsonObject = new JSONObject(String.valueOf(Html.fromHtml(resultData.toString())));
                        if (jsonObject.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    String currentBalance = null;
                                    try {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        currentBalance = jsonObject.getString("currentBalance");
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, jsonObject.getString("bonusBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, jsonObject.getString("depositBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, jsonObject.getString("winningBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, jsonObject.getString("withdrawlBal"));
                                        ((DrawerBaseActivity) activity).refresh();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, jsonObject.getString("message"), "", false, true, okClickListener, null);
                            downloadDialog.show();
//                            String currentBalance = jsonObject.getString("currentBalance");
//                            String message = jsonObject.getString("message");
//                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
//                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;
                case "lotsWithdraw":
                    try {
                        final JSONObject jsonObject = new JSONObject(String.valueOf(Html.fromHtml(resultData.toString())));
                        if (jsonObject.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    String currentBalance = null;
                                    try {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        currentBalance = jsonObject.getString("currentBalance");
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, jsonObject.getString("bonusBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, jsonObject.getString("depositBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, jsonObject.getString("winningBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, jsonObject.getString("withdrawlBal"));
                                        ((DrawerBaseActivity) activity).refresh();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, jsonObject.getString("message"), "", false, true, okClickListener, null);
                            downloadDialog.show();
//                            String currentBalance = jsonObject.getString("currentBalance");
//                            String message = jsonObject.getString("message");
//                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
//                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;

                case "mPowerAccount":
                    try {
                        final JSONObject jsonObject = new JSONObject(resultData.toString());
                        if (jsonObject.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    String currentBalance = null;
                                    try {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        currentBalance = jsonObject.getString("currentBalance");
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, jsonObject.getString("bonusBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, jsonObject.getString("depositBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, jsonObject.getString("winningBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, jsonObject.getString("withdrawlBal"));
                                        ((DrawerBaseActivity) activity).refresh();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, jsonObject.getString("message"), "", false, true, okClickListener, null);
                            downloadDialog.show();
//                            String currentBalance = jsonObject.getString("currentBalance");
//                            String message = jsonObject.getString("message");
//                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
//                            dialog.dismiss();
                        } else {
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                            dialog.dismiss();
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }

                    break;
                case "personalBank":
                    try {
                        final JSONObject jsonObject = new JSONObject(resultData.toString());
                        if (jsonObject.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    String currentBalance = null;
                                    try {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        currentBalance = jsonObject.getString("currentBalance");
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, jsonObject.getString("bonusBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, jsonObject.getString("depositBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, jsonObject.getString("winningBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, jsonObject.getString("withdrawlBal"));
                                        ((DrawerBaseActivity) activity).refresh();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, jsonObject.getString("message"), "", false, true, okClickListener, null);
                            downloadDialog.show();
//                            String currentBalance = jsonObject.getString("currentBalance");
//                            String message = jsonObject.getString("message");
//                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
//                            dialog.dismiss();
                        } else {
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                            dialog.dismiss();
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;
                case "mtnMobile":
                    try {
                        final JSONObject jsonObject = new JSONObject(resultData.toString());
                        if (jsonObject.getBoolean("isSuccess")) {
                            dialog.dismiss();
                            final OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    downloadDialog.dismiss();
                                    String currentBalance = null;
                                    try {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        currentBalance = jsonObject.getString("currentBalance");
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.BONUS_BAL, jsonObject.getString("bonusBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.DEPOSIT_BAL, jsonObject.getString("depositBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WINNING_BAL, jsonObject.getString("winningBal"));
                                        VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.WITHDRAWAL_BAL, jsonObject.getString("withdrawlBal"));
                                        ((DrawerBaseActivity) activity).refresh();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            downloadDialog = new DownloadDialogBox(activity, jsonObject.getString("message"), "", false, true, okClickListener, null);
                            downloadDialog.show();
//                            String currentBalance = jsonObject.getString("currentBalance");
//                            String message = jsonObject.getString("message");
//                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
//                            dialog.dismiss();
                        } else {
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                            dialog.dismiss();
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;

                case "WITHDRAWAL":
                    try {
                        //for ghana
//                        resultData="{\"modeList\":[{\"modeData\":[{\"modeKey\":\"MPOWER\",\"modeName\":\"Mpower Account\"}],\"modeKey\":\"MPOWER\",\"modeName\":\"Mpower Account\"},{\"modeData\":[{\"modeKey\":\"MPOWER_BANK\",\"modeName\":\"Personal Bank Account\",\"providersList\":[{\"providerCode\":\"ABG\",\"providerName\":\"Access Bank Ghana\"},{\"providerCode\":\"ADB\",\"providerName\":\"Agricultural Development Bank\"},{\"providerCode\":\"BOA\",\"providerName\":\"Bank of Africa (Ghana)\"},{\"providerCode\":\"BOB\",\"providerName\":\"Bank of Baroda\"},{\"providerCode\":\"BBG\",\"providerName\":\"Barclays Bank\"},{\"providerCode\":\"CBL\",\"providerName\":\"CAL Bank\"},{\"providerCode\":\"EBG\",\"providerName\":\"Ecobank Ghana\"},{\"providerCode\":\"EBL\",\"providerName\":\"Energy Bank Limited\"},{\"providerCode\":\"FBG\",\"providerName\":\"Fidelity Bank Ghana Limited\"},{\"providerCode\":\"FAB\",\"providerName\":\"First Allied\"},{\"providerCode\":\"FMG\",\"providerName\":\"First Atlantic Merchant Bank Ghana\"},{\"providerCode\":\"FCP\",\"providerName\":\"First Capital Plus Bank\"},{\"providerCode\":\"GCB\",\"providerName\":\"Ghana Commercial Bank\"},{\"providerCode\":\"GTB\",\"providerName\":\"Guaranty Trust Bank (Ghana)\"},{\"providerCode\":\"HFC\",\"providerName\":\"HFC Bank\"},{\"providerCode\":\"ICB\",\"providerName\":\"International Commercial Bank\"},{\"providerCode\":\"MBG\",\"providerName\":\"Merchant Bank Ghana Limited\"},{\"providerCode\":\"NIB\",\"providerName\":\"National Investment Bank\"},{\"providerCode\":\"PBL\",\"providerName\":\"Prudential Bank Limited\"},{\"providerCode\":\"SSB\",\"providerName\":\"SG-SSB Bank\"},{\"providerCode\":\"SBG\",\"providerName\":\"Stanbic Bank\"},{\"providerCode\":\"SCB\",\"providerName\":\"Standard Chartered Bank\"},{\"providerCode\":\"TRB\",\"providerName\":\"The Royal Bank\"},{\"providerCode\":\"UBL\",\"providerName\":\"UniBank Limited\"},{\"providerCode\":\"UBA\",\"providerName\":\"United Bank for Africa\"},{\"providerCode\":\"UTB\",\"providerName\":\"UT Bank\"},{\"providerCode\":\"ZBG\",\"providerName\":\"Zenith Bank Ghana\"}]},{\"modeKey\":\"MPOWER_MOBILE\",\"modeName\":\"MTN Mobile Money Account\",\"providersList\":[{\"providerCode\":\"MTN\",\"providerName\":\"MTN Mobile Money\"}]}],\"modeKey\":\"\",\"modeName\":\"Personal Bank Account\"}],\"isSuccess\":true}";

                        //for zim
                        //resultData="{\"modeList\":[{\"modeName\":\"Tele Cash\",\"modeData\":[],\"modeKey\":\"TELE_CASH\"},{\"modeName\":\"Eco Cash\",\"modeData\":[],\"modeKey\":\"ECO_CASH\"},{\"modeName\":\"Africa Outlets\",\"modeData\":[],\"modeKey\":\"WINLOT\"}],\"isSuccess\":true}";

                        //for Lagos
//                        resultData = "{\"modeList\":[{\"modeData\":[],\"modeKey\":\"PAGA_REG\",\"modeName\":\"Paga Reg\"},{\"modeData\":[],\"modeKey\":\"PAGA_NONREG\",\"modeName\":\"Paga Non Reg\"},{\"modeData\":[],\"modeKey\":\"WINLOT\",\"modeName\":\"Winlot\"},{\"modeData\":[],\"modeKey\":\"BANK_WITHDRAWAL\",\"modeName\":\"Bank Withdrawal\"},{\"modeData\":[{\"modeKey\":\"INTER_SWITCH\",\"modeName\":\"Interswitch\",\"providersList\":[{\"providerCode\":\"044\",\"providerName\":\"Access Bank\"},{\"providerCode\":\"014\",\"providerName\":\"Afri Bank\"},{\"providerCode\":\"023\",\"providerName\":\"Citi Bank\"},{\"providerCode\":\"063\",\"providerName\":\"Diamond Bank\"},{\"providerCode\":\"050\",\"providerName\":\"Ecobank\"},{\"providerCode\":\"040\",\"providerName\":\"Equitorial Trust Bank\"},{\"providerCode\":\"214\",\"providerName\":\"FCMB\"},{\"providerCode\":\"070\",\"providerName\":\"Fidelity Bank\"},{\"providerCode\":\"070\",\"providerName\":\"Fidelity Bank( Old FSB)\"},{\"providerCode\":\"085\",\"providerName\":\"FinBank (First Inland Bank)\"},{\"providerCode\":\"011\",\"providerName\":\"First Bank of Nigeria\"},{\"providerCode\":\"058\",\"providerName\":\"Guaranty Trust Bank\"},{\"providerCode\":\"069\",\"providerName\":\"Intercontinental Bank\"},{\"providerCode\":\"056\",\"providerName\":\"Oceanic Bank\"},{\"providerCode\":\"082\",\"providerName\":\"Platinum Habib Bank\"},{\"providerCode\":\"076\",\"providerName\":\"Skye Bank( Old Prudent Bank)\"},{\"providerCode\":\"084\",\"providerName\":\"Spring Bank\"},{\"providerCode\":\"039\",\"providerName\":\"Stanbic IBTC Bank (former IBTC Chartered Bank)\"},{\"providerCode\":\"068\",\"providerName\":\"Standard chartered\"},{\"providerCode\":\"232\",\"providerName\":\"Sterling Bank\"},{\"providerCode\":\"032\",\"providerName\":\"Union Bank\"},{\"providerCode\":\"033\",\"providerName\":\"United Bank for Africa\"},{\"providerCode\":\"215\",\"providerName\":\"Unity Bank\"},{\"providerCode\":\"035\",\"providerName\":\"Wema Bank\"},{\"providerCode\":\"035\",\"providerName\":\"Wema Bank(former National Bank)\"},{\"providerCode\":\"057\",\"providerName\":\"Zenith International Bank\"}]}],\"modeKey\":\"INTER_SWITCH\",\"modeName\":\"Interswitch\"}],\"isSuccess\":true}";


                        Gson gson = new Gson();
                        WithdrawalOptionsBean withdrawalOptionsBean = gson.fromJson(resultData.toString(), WithdrawalOptionsBean.class);
                        ArrayList<WithdrawalOptionsBean.ModeList> modeLists = withdrawalOptionsBean.getModeList();

                        if (withdrawalOptionsBean.isSuccess()) {
                            for (int i = 0; i < modeLists.size(); i++) {
                                ArrayList<WithdrawalOptionsBean.ModeData> modeDatas = modeLists.get(i).getModeData();
                                if (isLagos) {
                                    modeDataLagos.add(modeLists.get(i).getModeName());
                                    modeKeyLagos.add(modeLists.get(i).getModeKey());

                                }
                                if (modeDatas.size() > 0) {
                                    for (int j = 0; j < modeDatas.size(); j++) {
                                        withdrawlModeNameList.add(modeDatas.get(j).getModeName());
                                        withdrawlModeKeyList.add(modeDatas.get(j).getModeKey());

                                        if (modeDatas.size() > 0) {
                                            if (modeDatas.get(j).getProvidersList() != null) {
                                                for (int p = 0; p < modeDatas.get(j).getProvidersList().size(); p++) {
                                                    switch (modeDatas.get(j).getModeKey()) {
                                                        case "MPOWER_BANK":
                                                            providerListCodeBank.add(modeDatas.get(j).getProvidersList().get(p).getProviderCode());
                                                            providerListNameBank.add(modeDatas.get(j).getProvidersList().get(p).getProviderName());
                                                            break;
                                                        case "MPOWER_MOBILE":
                                                            providerListCodeMobile.add(modeDatas.get(j).getProvidersList().get(p).getProviderCode());
                                                            providerListNameMobile.add(modeDatas.get(j).getProvidersList().get(p).getProviderName());
                                                            break;
                                                        case "INTER_SWITCH":
                                                            interswitchProviderBankName.add(modeDatas.get(j).getProvidersList().get(p).getProviderName());
                                                            interswitchProviderBankKey.add(modeDatas.get(j).getProvidersList().get(p).getProviderCode());
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            } else {

                                            }
                                        }
                                    }
                                } else {
                                    withdrawlModeNameList.add(modeLists.get(i).getModeName());
                                    withdrawlModeKeyList.add(modeLists.get(i).getModeKey());
                                }
                            }
                            if (isLagos) {
                                accountType.add("Saving Account");
                                accountType.add("Current Account");

                                lagosWithdrawal.put("modeName", modeDataLagos);
                                lagosWithdrawal.put("modeKey", modeKeyLagos);
                                lagosWithdrawal.put("interswitchProviderBankName", interswitchProviderBankName);
                                lagosWithdrawal.put("interswitchProviderBankKey", interswitchProviderBankKey);
                                lagosWithdrawal.put("accountType", accountType);
                            }

                            setView(3);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            Utils.Toast(activity, withdrawalOptionsBean.getErrorMsg());
                        }

//                        if(withdrawalOptionsBean.isSuccess()){
//                            analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.SUCCESS);
//                            for(int i=0;i<withdrawalOptionsBean.getModeList().size();i++){
////                                withdrawlModeNameList.add(withdrawalOptionsBean.);
//                            }
//
//                        }

//                        JSONObject jsonObject = new JSONObject(resultData.toString());
//                        if (jsonObject.getBoolean("isSuccess")) {
//                            analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.SUCCESS);
//                            JSONArray withdrawlModeList = jsonObject.getJSONArray("modeList");
//                            for (int i = 0; i < withdrawlModeList.length(); i++) {
//
//                                withdrawlModeNameList.add(withdrawlModeList.getJSONObject(i).getString("modeName"));
//                                withdrawlModeKeyList.add(withdrawlModeList.getJSONObject(i).getString("modeKey"));
//
//
//
//                                //for zim
//                                //for ghana
//                                //modeKeyList.put(withdrawlModeList.getJSONObject(i).getString("modeKey"),true);
//                            }
//                            setView(2);
//                            dialog.dismiss();
//                        } else {
//                            dialog.dismiss();
//                            Utils.Toast(activity, jsonObject.getString("errorMsg"), Toast.LENGTH_SHORT);
//                        }
                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;
                case "WITHDRAWAL_REQ":
                    try {
                        JSONObject jsonObject = new JSONObject(resultData.toString());
                        if (jsonObject.getBoolean("isSuccess")) {
                            analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.SUCCESS);
                            String currentBalance = jsonObject.getString("currentBalance");
                            String message = jsonObject.getString("message");
                            VariableStorage.UserPref.setStringPreferences(activity, VariableStorage.UserPref.USER_BAL, String.valueOf(currentBalance));
                            ((DrawerBaseActivity) activity).refresh();
                            dialog.dismiss();
                            new DownloadDialogBox(activity, "Transaction Id : " + jsonObject.getString("transactioId")
                                    + "\n" + "Withdrawal Amount : " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + "" + jsonObject.getString("withdrawlAmount")
                                    + "\n" + "Withdrawal Charge : " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + "" + jsonObject.getString("withdrawlCharges")
                                    + "\n" + "Current Balance : " + VariableStorage.GlobalPref.getStringData(activity, VariableStorage.GlobalPref.CURRENCY_CODE) + "" + jsonObject.getString("currentBalance")
                                    + "\n\n" + jsonObject.getString("message"), "WITHDRAWAL", false, true, null, null).show();
                        } else {
                            dialog.dismiss();
                            Utils.Toast(activity, jsonObject.getString("errorMsg"));
                        }
                    } catch (JSONException e1) {
                        analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
                        e1.printStackTrace();
                        dialog.dismiss();
                        GlobalVariables.showServerErr(activity);
                    }
                    break;
            }

        } else {
            analytics.sendAll(Fields.Category.WITHDRAWAL, Fields.Action.GET, Fields.Label.FAILURE);
            dialog.dismiss();
            GlobalVariables.showServerErr(activity);
        }

    }

    public void setSelectAmountTag(final View v) {
        try {
            if (Double.parseDouble(amountView.getText().toString().replace(",", "")) > 0) {
                GlobalVariables.amountWithDrawal = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                amount = Double.parseDouble(amountView.getText().toString().replace(",", ""));
                amountView.setEnabled(false);
                withdrawPGExpAdapter.setRsCheck(false);
                amountView.setCursorVisible(false);
                amountView.setBackgroundColor(Color.TRANSPARENT);
                int dimen = Float.valueOf(activity.getResources().getDimension(R.dimen._8sdp)).intValue();
                amountView.setPadding(0, dimen, dimen, dimen);
                amountView.setText(AmountFormat.getAmountFormatForMobileDecimal(amount).replace(",", ""));
                edit.setBackgroundColor(getResources().getColor(R.color.trans));
                edit.setBackgroundResource(R.drawable.edit_nos);
                edit.setPadding(0, 0, 0, 0);
            } else {
                Utils.Toast(activity, "Invalid Amount");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        amountView.requestFocus();
                        amountView.setCursorVisible(true);
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(edit.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    }
                }, 700);
            }
        } catch (NumberFormatException e) {
            Utils.Toast(activity, "Invalid Amount");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    amountView.requestFocus();
                    amountView.setCursorVisible(true);
                    InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(edit.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
            }, 700);
        }
    }


}

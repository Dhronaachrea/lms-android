package com.skilrock.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.bean.BankDetailsWithdraw;
import com.skilrock.bean.WithdrawalPGBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.BetterSpinner;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.WithdrawalScreen;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stpl on 10/20/2015.
 */
public class WithdrawPGExpAdapter extends BaseAdapter {

    private WithdrawalScreen withdrawalScreen;
    private final Context context;
    private final int resource;
    private final LayoutInflater inflater;
    private final ArrayList<WithdrawalPGBean> pgBean;
    private boolean isGhana, isLagos;
    private double amount;
    private GroupHolder groupHolder;
    //    private Holder holder;
    private ArrayList<String> providerListCodeMobile;
    private ArrayList<String> providerListNameMobile;
    private ArrayList<String> providerListCodeBank;
    private ArrayList<String> providerListNameBank;
    private String bankListName = new String();
    private String mtnMobileMoneyName;
    private HashMap<String, ArrayList<String>> lagosWithdrawal;
    private final String mpowerPath = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";
    private final String persBankPath = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";
    private final String mtnPath = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";

    //lagos
    String lagospath = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";
    String lagospathIc = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";


    private boolean rsCheck;
    private String walletProvider = "-Select-";
    private Fragment fragment;
    private String bankCode = "";
    private String walletProv = "";

    private final HashMap<String, BankDetailsWithdraw> playerRegData;


    public WithdrawPGExpAdapter(Fragment fragment, int resource, ArrayList<WithdrawalPGBean> pgBean, ArrayList<String> providerListCodeMobile, ArrayList<String> providerListNameMobile, ArrayList<String> providerListCodeBank, ArrayList<String> providerListNameBank, WithdrawalScreen withdrawalScreen, boolean isGhana, boolean isLagos, HashMap<String, ArrayList<String>> lagosWithdrawal, HashMap<String, BankDetailsWithdraw> playerRegData) {
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.pgBean = pgBean;
        this.resource = resource;
        this.withdrawalScreen = withdrawalScreen;
        this.providerListCodeBank = providerListCodeBank;
        this.providerListCodeMobile = providerListCodeMobile;
        this.providerListNameBank = providerListNameBank;
        this.providerListNameMobile = providerListNameMobile;
        inflater = LayoutInflater.from(context);
        this.lagosWithdrawal = lagosWithdrawal;
        this.isGhana = isGhana;
        this.isLagos = isLagos;
        this.rsCheck = false;
        this.playerRegData = playerRegData;
    }

    public WithdrawPGExpAdapter(Fragment fragment, int resource, ArrayList<WithdrawalPGBean> pgBean, ArrayList<String> providerListCodeMobile, ArrayList<String> providerListNameMobile, ArrayList<String> providerListCodeBank, ArrayList<String> providerListNameBank, WithdrawalScreen withdrawalScreen, boolean isGhana, boolean isLagos, HashMap<String, ArrayList<String>> lagosWithdrawal) {
        this.context = fragment.getActivity();
        this.fragment = fragment;
        this.pgBean = pgBean;
        this.resource = resource;
        this.withdrawalScreen = withdrawalScreen;
        this.providerListCodeBank = providerListCodeBank;
        this.providerListCodeMobile = providerListCodeMobile;
        this.providerListNameBank = providerListNameBank;
        this.providerListNameMobile = providerListNameMobile;
        inflater = LayoutInflater.from(context);
        this.lagosWithdrawal = lagosWithdrawal;
        this.isGhana = isGhana;
        this.isLagos = isLagos;
        this.rsCheck = false;
        this.playerRegData = null;
    }


    @Override
    public int getCount() {
        return pgBean.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        if (position == 1) return 1;
        if (position == 2) return 2;
        if (position == 3) return 3;
        if (position == 4) return 4;

        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return pgBean.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            // linear layout
            if (isGhana) {
                if (getItemViewType(position) == 0) {
                    groupHolder = new MPower();
                    MPower mHolder = (MPower) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.mpower_account, null));
                    mHolder.mPowerUsername = (EditText) groupHolder.expand.findViewById(R.id.mpower_username);
                    mHolder.txtSubmitMPower = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mpower);
                    // set focus on edittext and spinner
                    mHolder.mPowerUsername.setOnFocusChangeListener(onFocusChangeListener);
                    convertView.setTag(mHolder);
                } else if (getItemViewType(position) == 1) {
                    groupHolder = new PersonalBankAcc();
                    PersonalBankAcc pHolder = (PersonalBankAcc) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.personal_bank_acc, null));
                    pHolder.getPersonalAccNo = (EditText) groupHolder.expand.findViewById(R.id.personal_acc_no);
                    pHolder.bankList = (BetterSpinner) groupHolder.expand.findViewById(R.id.bank_list);
                    pHolder.personalAccName = (EditText) groupHolder.expand.findViewById(R.id.personal_acc_name);
                    pHolder.personalBranchName = (EditText) groupHolder.expand.findViewById(R.id.personal_branch_name);
                    pHolder.txtSubmitPer = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_per);
                    // set focus on edittext and spinner
                    pHolder.getPersonalAccNo.setOnFocusChangeListener(onFocusChangeListener);
                    pHolder.bankList.setOnFocusChangeListener(onFocusChangeListener);
                    pHolder.personalBranchName.setOnFocusChangeListener(onFocusChangeListener);
                    pHolder.personalAccName.setOnFocusChangeListener(onFocusChangeListener);
                    pHolder.bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            bankCode = providerListCodeBank.get(position);
                        }
                    });
                    convertView.setTag(pHolder);
                } else if (getItemViewType(position) == 2) {
                    groupHolder = new MTNMobileMoney();
                    MTNMobileMoney mTNHolder = (MTNMobileMoney) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.mtn_mobile_money, null));
                    mTNHolder.walletProvider = (BetterSpinner) groupHolder.expand.findViewById(R.id.wallet_provider);
                    ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, providerListNameMobile);
                    mTNHolder.walletProvider.setAdapter(mobileAdapter);
                    mTNHolder.mtnMobileNo = (EditText) groupHolder.expand.findViewById(R.id.mtn_mobile_number);
                    mTNHolder.mtnRecipient = (EditText) groupHolder.expand.findViewById(R.id.mtn_name_of_recep);
                    mTNHolder.txtSubmitMTN = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
                    // set focus on edittext and spinner
                    mTNHolder.walletProvider.setOnFocusChangeListener(onFocusChangeListener);
                    mTNHolder.mtnMobileNo.setOnFocusChangeListener(onFocusChangeListener);
                    mTNHolder.mtnRecipient.setOnFocusChangeListener(onFocusChangeListener);
                    mTNHolder.walletProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            walletProv = providerListCodeMobile.get(position);
                        }
                    });
                    convertView.setTag(mTNHolder);
                }
            } else if (isLagos) {
                if (getItemViewType(position) == 0) {
                    groupHolder = new LagosWithdrawPagaReg();
                    LagosWithdrawPagaReg prHolder = (LagosWithdrawPagaReg) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.lagos_withdraw_pgw, null));
                    prHolder.pagaRegProvider = (BetterSpinner) groupHolder.expand.findViewById(R.id.wallet_provider);
                    prHolder.txtSubmitLagos = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
                    convertView.setTag(prHolder);
                } else if (getItemViewType(position) == 1) {
                    groupHolder = new LagosWithdrawPagaNonReg();
                    LagosWithdrawPagaNonReg pnrHolder = (LagosWithdrawPagaNonReg) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.lagos_withdraw_pgw, null));
                    pnrHolder.pagaNonRegProvider = (BetterSpinner) groupHolder.expand.findViewById(R.id.wallet_provider);
                    pnrHolder.txtSubmitLagos = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
                    convertView.setTag(pnrHolder);
                } else if (getItemViewType(position) == 2) {
                    groupHolder = new LagosWithdrawWinlot();
                    LagosWithdrawWinlot winlotHolder = (LagosWithdrawWinlot) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.lagos_withdraw_pgw, null));
                    winlotHolder.winlotProvider = (BetterSpinner) groupHolder.expand.findViewById(R.id.wallet_provider);
                    winlotHolder.txtSubmitLagos = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
                    convertView.setTag(winlotHolder);
                } else if (getItemViewType(position) == 3) {
                    groupHolder = new LagosWithdrawBankWithdraw();
                    LagosWithdrawBankWithdraw withdrawHolder = (LagosWithdrawBankWithdraw) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.lagos_withdraw_bank, null));//lagos_withdraw_bank

                    bankWithDrawList(withdrawHolder);
                    convertView.setTag(withdrawHolder);
                } else if (getItemViewType(position) == 4) {
                    groupHolder = new LagosWithdrawInterSwitch();
                    LagosWithdrawInterSwitch withdrawIsHolder = (LagosWithdrawInterSwitch) groupHolder;
                    groupHolder.expand = (LinearLayout) convertView.findViewById(R.id.expand);
                    groupHolder.expand.addView(inflater.inflate(R.layout.lagos_withdraw_ic, null));
                    withdrawIsHolder.accNo = (EditText) groupHolder.expand.findViewById(R.id.acc_number);
                    withdrawIsHolder.bankType = (BetterSpinner) groupHolder.expand.findViewById(R.id.bank_type);
                    withdrawIsHolder.accType = (BetterSpinner) groupHolder.expand.findViewById(R.id.acc_type);
                    withdrawIsHolder.txtSubmitLagos = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
                    convertView.setTag(withdrawIsHolder);
                }
            } else {
                groupHolder = new GroupHolder();
            }
            groupHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            groupHolder.txtName = (CustomTextView) convertView.findViewById(R.id.name);
            groupHolder.txtDesc = (CustomTextView) convertView.findViewById(R.id.desc);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (isGhana) {
            if (getItemViewType(position) == 0) {
                final MPower mHolder = (MPower) groupHolder;
                mHolder.txtSubmitMPower.setOnClickListener(new DebouncedOnClickListener(500) {
                    @Override
                    public void onDebouncedClick(View v) {
                        if (getLength(mHolder.mPowerUsername) > 0) {
                            String username = mHolder.mPowerUsername.getText().toString();
                            if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    // data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                    data.put("withdrawlAmount", AmountFormat.round(GlobalVariables.amountWithDrawal, 2));
                                    data.put("accountName", username);
                                    data.put("withdrawlChannel", pgBean.get(0).getPgKey());
                                    new PMSWebTask(withdrawalScreen, mpowerPath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "mPowerAccount", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            } else {
                                if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) <= 0)
                                    Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                                else if (rsCheck)
                                    Utils.Toast(context, context.getString(R.string.rs_check_on));
                            }
                        } else {
                            Utils.Toast(context, "Please enter username");
                        }
                    }
                });
            } else if (getItemViewType(position) == 1) {
                final PersonalBankAcc pHolder = (PersonalBankAcc) groupHolder;
                ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(context, R.layout.txn_spinner_row, R.id.spinner_text, providerListNameBank);
                pHolder.bankList.setText("-Select-");
                pHolder.bankList.setAdapter(bankAdapter);
                pHolder.txtSubmitPer.setOnClickListener(new DebouncedOnClickListener(500) {
                    @Override
                    public void onDebouncedClick(View v) {
                        if (getLength(pHolder.getPersonalAccNo) > 0
                                && getLength(pHolder.personalAccName) > 0
                                && getLength(pHolder.personalBranchName) > 0
                                && !pHolder.bankList.getText().toString().trim().equalsIgnoreCase("-select-")) {
                            if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                                try {
//                                Utils.Toast(context, bankList.getText().toString(), Toast.LENGTH_SHORT);
                                    String accNo = pHolder.getPersonalAccNo.getText().toString();
//                                    String bankName = pHolder.bankList.getText().toString();
                                    String accountName = pHolder.personalAccName.getText().toString();
                                    String branch = pHolder.personalBranchName.getText().toString();
                                    try {
                                        JSONObject data = new JSONObject();
                                        data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                        // data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                        data.put("withdrawlAmount", AmountFormat.round(GlobalVariables.amountWithDrawal, 2));
                                        data.put("accountName", accountName);
                                        data.put("accountNumber", accNo);
                                        data.put("bankCode", bankCode);
                                        data.put("bankBranch", branch);
                                        data.put("withdrawlChannel", pgBean.get(1).getPgKey());
                                        new PMSWebTask(withdrawalScreen, persBankPath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "personalBank", null, "Loading...").execute();
                                    } catch (Exception e) {
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) <= 0)
                                    Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                                else if (rsCheck)
                                    Utils.Toast(context, context.getString(R.string.rs_check_on));
                            }
                        } else {
                            if ((getLength(pHolder.getPersonalAccNo) == 0 && getLength(pHolder.personalAccName) == 0)
                                    || (getLength(pHolder.getPersonalAccNo) == 0 && getLength(pHolder.personalBranchName) == 0)
                                    || (getLength(pHolder.getPersonalAccNo) == 0 && pHolder.bankList.getText().toString().trim().equalsIgnoreCase("-select-"))
                                    || (getLength(pHolder.personalAccName) == 0 && pHolder.bankList.getText().toString().trim().equalsIgnoreCase("-select-"))
                                    || (getLength(pHolder.personalAccName) == 0 && getLength(pHolder.personalBranchName) == 0)
                                    || (getLength(pHolder.personalBranchName) == 0 && pHolder.bankList.getText().toString().trim().equalsIgnoreCase("-select-"))) {
                                Utils.Toast(context, "Please fill empty field");
                            } else if (getLength(pHolder.getPersonalAccNo) == 0) {
                                Utils.Toast(context, "Please enter account number");
                            } else if (getLength(pHolder.personalAccName) == 0) {
                                Utils.Toast(context, "Please enter account name");
                            } else if (getLength(pHolder.personalBranchName) == 0) {
                                Utils.Toast(context, "Please enter branch name");
                            } else if (pHolder.bankList.getText().toString().trim().equalsIgnoreCase("-select-")) {
                                Utils.Toast(context, "Please select bank");
                            }

                        }
                    }
                });
            } else if (getItemViewType(position) == 2) {
                final MTNMobileMoney mTNHolder = (MTNMobileMoney) groupHolder;
                mTNHolder.walletProvider.setText(walletProvider);
                mTNHolder.walletProvider.dismissDropDown();
                mTNHolder.txtSubmitMTN.setOnClickListener(new DebouncedOnClickListener(500) {
                    @Override
                    public void onDebouncedClick(View v) {
                        if (getLength(mTNHolder.mtnMobileNo) > 0
                                && !mTNHolder.walletProvider.getText().toString().trim().equalsIgnoreCase("-Select-")
                                && getLength(mTNHolder.mtnRecipient) > 0) {
                            if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                                try {
//                                Utils.Toast(context, walletProvider.getText().toString(), Toast.LENGTH_SHORT);
                                    String mobileno = mTNHolder.mtnMobileNo.getText().toString();
//                                    String walletProv = mTNHolder.walletProvider.getText().toString();
                                    String fundrecei = mTNHolder.mtnRecipient.getText().toString();

                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    //  data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                    data.put("withdrawlAmount", AmountFormat.round(GlobalVariables.amountWithDrawal, 2));
                                    data.put("mobileNumber", mobileno);
                                    data.put("walletProvider", walletProv);
                                    data.put("recipient_name", fundrecei);
                                    data.put("withdrawlChannel", pgBean.get(2).getPgKey());
                                    new PMSWebTask(withdrawalScreen, mtnPath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "mtnMobile", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            } else {
                                if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) <= 0)
                                    Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                                else if (rsCheck)
                                    Utils.Toast(context, context.getString(R.string.rs_check_on));
                            }
                        } else {
                            if ((getLength(mTNHolder.mtnMobileNo) == 0 && mTNHolder.walletProvider.getText().toString().trim().equalsIgnoreCase("-Select-"))
                                    || (getLength(mTNHolder.mtnRecipient) == 0 && getLength(mTNHolder.mtnMobileNo) == 0)
                                    || (mTNHolder.walletProvider.getText().toString().trim().equalsIgnoreCase("-Select-") && getLength(mTNHolder.mtnRecipient) == 0)) {
                                Utils.Toast(context, "Please fill empty field");
                            } else if (getLength(mTNHolder.mtnMobileNo) == 0) {
                                Utils.Toast(context, "Please enter mobile number");
                            } else if (getLength(mTNHolder.mtnRecipient) == 0) {
                                Utils.Toast(context, "Please enter name of recipient");
                            } else if (mTNHolder.walletProvider.getText().toString().trim().equalsIgnoreCase("-Select-")) {
                                Utils.Toast(context, "Please select wallet provider");
                            }
                        }
                    }
                });
            }
        } else if (isLagos) {
            if (getItemViewType(position) == 0) {
                ArrayList<String> list = new ArrayList<>();
                list.add(lagosWithdrawal.get("modeName").get(0));
                final LagosWithdrawPagaReg prHolder = (LagosWithdrawPagaReg) groupHolder;
                ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, list);
                prHolder.pagaRegProvider.setText(list.get(0));
                prHolder.pagaRegProvider.setAdapter(mobileAdapter);
                prHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                            if (prHolder.pagaRegProvider.getText().toString().equalsIgnoreCase("-select-")) {
                                Utils.Toast(context, "please select Withdrawal Option");
                            } else {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
                                    data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(0));
//                                    new PMSWebTask(withdrawalScreen, lagospath, "withdrawlData", data, "lotsWithdraw", null, "Loading...").execute();
                                    new PMSWebTask(withdrawalScreen, lagospath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdraw", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                        }
                    }
                });
            } else if (getItemViewType(position) == 1) {
                ArrayList<String> list = new ArrayList<>();
                list.add(lagosWithdrawal.get("modeName").get(1));
                final LagosWithdrawPagaNonReg pnrHolder = (LagosWithdrawPagaNonReg) groupHolder;
                ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, list);
                pnrHolder.pagaNonRegProvider.setText(list.get(0));
                pnrHolder.pagaNonRegProvider.setAdapter(mobileAdapter);
                pnrHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                            if (pnrHolder.pagaNonRegProvider.getText().toString().equalsIgnoreCase("-select-")) {
                                Utils.Toast(context, "please select Withdrawal Option");
                            } else {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
                                    data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(1));
//                                    new PMSWebTask(withdrawalScreen, lagospath, "withdrawlData", data, "lotsWithdraw", null, "Loading...").execute();
                                    new PMSWebTask(withdrawalScreen, lagospath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdraw", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            }
                        } else {
//                            if (GlobalVariables.amountWithDrawal > 0)
                            Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                        }
                    }
                });
            } else if (getItemViewType(position) == 2) {
                ArrayList<String> list = new ArrayList<>();
                list.add(lagosWithdrawal.get("modeName").get(2));
                final LagosWithdrawWinlot winlotHolder = (LagosWithdrawWinlot) groupHolder;
                ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, list);
                winlotHolder.winlotProvider.setText(list.get(0));
                winlotHolder.winlotProvider.setAdapter(mobileAdapter);
                winlotHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                            if (winlotHolder.winlotProvider.getText().toString().equalsIgnoreCase("-select-")) {
                                Utils.Toast(context, "please select Withdrawal Option");
                            } else {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
                                    data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(2));
//                                    new PMSWebTask(withdrawalScreen, lagospath, "withdrawlData", data, "lotsWithdraw", null, "Loading...").execute();
                                    new PMSWebTask(withdrawalScreen, lagospath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdraw", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                        }
                    }
                });
            } else if (getItemViewType(position) == 3) {
                updateTheme(groupHolder);
                //txtSubmitLagos;
//                txtModify;


//                list.add(lagosWithdrawal.get("modeName").get(3));
//                final LagosWithdrawBankWithdraw withdrawHolder = (LagosWithdrawBankWithdraw) groupHolder;
//                ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, list);
//                withdrawHolder.bankWithdrawProvider.setText("-Select-");
//                withdrawHolder.bankWithdrawProvider.setAdapter(mobileAdapter);
//                withdrawHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
//                            if (withdrawHolder.bankWithdrawProvider.getText().toString().equalsIgnoreCase("-select-")) {
//                                Utils.Toast(context, "please select Withdrawal Option");
//                            } else {
//                                try {
////                                    JSONObject data = new JSONObject();
////                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
////                                    data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
////                                    data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(3));
////                                    new PMSWebTask(withdrawalScreen, lagospath + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdraw", null, "Loading...").execute();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//                            Utils.Toast(context, context.getString(R.string.withdrawal_amt));
//                        }
//                    }
//                });
            } else if (getItemViewType(position) == 4) {
                final LagosWithdrawInterSwitch withdrawIsHolder = (LagosWithdrawInterSwitch) groupHolder;
                ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, lagosWithdrawal.get("interswitchProviderBankName"));
                withdrawIsHolder.bankType.setText("-Select Bank Type-");
                withdrawIsHolder.bankType.setAdapter(bankAdapter);

                ArrayAdapter<String> accAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, lagosWithdrawal.get("accountType"));
                withdrawIsHolder.accType.setText("-Select Account Type-");
                withdrawIsHolder.accType.setAdapter(accAdapter);

                withdrawIsHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                            int accountType = 0;
                            int bankIdPosition = 0;
                            if (isValidate(withdrawIsHolder)) {
                                try {
                                    if (withdrawIsHolder.accType.getText().toString().equalsIgnoreCase(lagosWithdrawal.get("accountType").get(0))) {
                                        accountType = 10;
                                    } else if (withdrawIsHolder.accType.getText().toString().equalsIgnoreCase(lagosWithdrawal.get("accountType").get(1))) {
                                        accountType = 20;
                                    }

                                    for (int i = 0; i < lagosWithdrawal.get("interswitchProviderBankName").size(); i++) {
                                        if (withdrawIsHolder.bankType.getText().toString().equalsIgnoreCase(lagosWithdrawal.get("interswitchProviderBankName").get(i))) {
                                            bankIdPosition = i;
                                        }
                                    }
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
                                    data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(4));
                                    data.put("accountNo", withdrawIsHolder.accNo.getText().toString());
                                    data.put("bankId", lagosWithdrawal.get("interswitchProviderBankKey").get(bankIdPosition));
                                    data.put("accountType", accountType);
//                                new PMSWebTask(withdrawalScreen, lagospathIc, "withdrawlData", data, "lotsWithdrawIc", null, "Loading...").execute();
                                    new PMSWebTask(withdrawalScreen, lagospathIc + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdrawIc", null, "Loading...").execute();
                                } catch (Exception e) {

                                }
                            }
                        } else {
                            Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                        }
                    }
                });
            }
        }

        groupHolder.txtName.setText(pgBean.get(position).

                        getBankName()

        );
        groupHolder.txtDesc.setVisibility(View.GONE);

        setImages(position, groupHolder);

        return convertView;
    }

    private void updateTheme(GroupHolder groupHolder) {
        final LagosWithdrawBankWithdraw withdrawHolder = (LagosWithdrawBankWithdraw) groupHolder;
        BankDetailsWithdraw bankDetails = playerRegData.get("bankDetailRegWithdrawName");
        if (bankDetails.getIsReg()) {
            withdrawHolder.accName.setText(bankDetails.getAccName() + "");
            withdrawHolder.accNumber.setText(bankDetails.getAccNbr() + "");
            withdrawHolder.branhName.setText(bankDetails.getBranchName() + "");
            withdrawHolder.bankWithdrawProvider.setText(bankDetails.getBankName() + "");
        } else {
            withdrawHolder.accNameModify.setText("");
            withdrawHolder.accNumberModify.setText("");
            withdrawHolder.branhNameModify.setText("");
        }
//        playerRegData
        ArrayAdapter<String> mobileAdapter = new ArrayAdapter<>(context, R.layout.txn_spinner_row, R.id.spinner_text, lagosWithdrawal.get("bankDetailWithdrawName"));
        withdrawHolder.bankWithdrawProviderModify.setText("-Select Bank Type-");
        withdrawHolder.bankWithdrawProviderModify.setAdapter(mobileAdapter);

        //visiblity chk
        withdrawHolder.txtModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawHolder.modify.setVisibility(View.VISIBLE);
                withdrawHolder.unModify.setVisibility(View.GONE);
            }
        });
        withdrawHolder.txtSubmitBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawHolder.modify.setVisibility(View.GONE);
                withdrawHolder.unModify.setVisibility(View.VISIBLE);
            }
        });

        //modify data to server
        withdrawHolder.txtSubmitLagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                    String path = "/com/skilrock/pms/mobile/accMgmt/Action/playerWithdrawlAction.action?";
                    try {
                        JSONObject data = new JSONObject();
                        data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                        data.put("withdrawlAmount", GlobalVariables.amountWithDrawal);
                        data.put("withdrawlChannel", lagosWithdrawal.get("modeKey").get(3));
                        new PMSWebTask(withdrawalScreen, path + "withdrawlData=" + URLEncoder.encode(data.toString()), "N/A", null, "lotsWithdraw", null, "Loading...").execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.Toast(context, context.getString(R.string.withdrawal_amt));
                }
            }
        });

        //send to server
        withdrawHolder.txtSubmitLagosModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bankIdPosition = 0;
                for (int i = 0; i < lagosWithdrawal.get("bankDetailWithdrawName").size(); i++) {
                    if (withdrawHolder.bankWithdrawProviderModify.getText().toString().equalsIgnoreCase(lagosWithdrawal.get("bankDetailWithdrawName").get(i))) {
                        bankIdPosition = i;
                    }
                }
                if (AmountFormat.round(GlobalVariables.amountWithDrawal, 2) > 0 && !rsCheck) {
                    String path = "/com/skilrock/pms/mobile/accMgmt/Action/registerPlayerBank.action?";
                    try {

                        if (bankValidation(withdrawHolder)) {
                            JSONObject data;
                            data = new JSONObject();
                            data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                            data.put("bankId", lagosWithdrawal.get("bankIdDetailWithdrawName").get(bankIdPosition).toString());
                            data.put("branchName", withdrawHolder.branhNameModify.getText().toString());
                            data.put("accName", withdrawHolder.accNameModify.getText().toString());
                            data.put("accNbr", withdrawHolder.accNumberModify.getText().toString());
                            new PMSWebTask(withdrawalScreen, path + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "Modified_withdraw", null, "Loading...").execute();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean bankValidation(LagosWithdrawBankWithdraw withdrawHolder) {
        if (withdrawHolder.branhNameModify.getText().toString().equalsIgnoreCase("") || withdrawHolder.accNameModify.getText().toString().equalsIgnoreCase("") || withdrawHolder.accNumberModify.getText().toString().equalsIgnoreCase("") || withdrawHolder.bankWithdrawProviderModify.getText().toString().contains("Select")) {
            Utils.Toast(context, "Please fill all details");
            return false;
        } else if (withdrawHolder.branhNameModify.getText().toString().contains("'") || withdrawHolder.accNameModify.getText().toString().contains("'") || withdrawHolder.accNumberModify.getText().toString().contains("'")) {
            Utils.Toast(context, "Please enter valid characters");
            return false;
        } else {
            return true;
        }
    }

    private void bankWithDrawList(LagosWithdrawBankWithdraw withdrawHolder) {
        BankDetailsWithdraw bankDetails = playerRegData.get("bankDetailRegWithdrawName");

        withdrawHolder.unModify = (LinearLayout) groupHolder.expand.findViewById(R.id.un_modify);
        withdrawHolder.bankWithdrawProvider = (EditText) groupHolder.expand.findViewById(R.id.bank_name);
        withdrawHolder.txtSubmitLagos = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn);
        withdrawHolder.txtModify = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_modify);

        withdrawHolder.branhName = (EditText) groupHolder.expand.findViewById(R.id.branch_name);
        withdrawHolder.accName = (EditText) groupHolder.expand.findViewById(R.id.acc_name);
        withdrawHolder.accNumber = (EditText) groupHolder.expand.findViewById(R.id.acc_number);

        withdrawHolder.modify = (LinearLayout) groupHolder.expand.findViewById(R.id.modify);
        withdrawHolder.bankWithdrawProviderModify = (BetterSpinner) groupHolder.expand.findViewById(R.id.bank_name_modify);
        withdrawHolder.txtSubmitLagosModify = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_mtn_modify);

        withdrawHolder.branhNameModify = (EditText) groupHolder.expand.findViewById(R.id.branh_name_modify);
        withdrawHolder.accNameModify = (EditText) groupHolder.expand.findViewById(R.id.acc_name_modify);
        withdrawHolder.accNumberModify = (EditText) groupHolder.expand.findViewById(R.id.acc_number_modify);
        withdrawHolder.txtSubmitBack = (CustomTextView) groupHolder.expand.findViewById(R.id.txt_submit_back);

        if (bankDetails.getIsReg() == true) {
            withdrawHolder.unModify.setVisibility(View.VISIBLE);
            withdrawHolder.modify.setVisibility(View.GONE);
            withdrawHolder.txtSubmitBack.setVisibility(View.VISIBLE);
        } else {
            withdrawHolder.unModify.setVisibility(View.GONE);
            withdrawHolder.modify.setVisibility(View.VISIBLE);
            withdrawHolder.txtSubmitBack.setVisibility(View.GONE);
        }
    }

    private boolean isValidate(LagosWithdrawInterSwitch interSwitch) {
        if ((interSwitch.accNo.getText().toString().length() == 0) || (interSwitch.bankType.getText().toString().equalsIgnoreCase("-select bank type-")) ||
                (interSwitch.accType.getText().toString().equalsIgnoreCase("-select account type-"))) {
            Utils.Toast(context, "please fill all details correctly");
            return false;
        } else if (!(getLength(interSwitch.accNo) == 10)) {
            Utils.Toast(context, "Enter 10 digit account number");
            return false;
        }
        return true;
    }

    private void setImages(int groupPosition, GroupHolder groupHolder) {
        switch (pgBean.get(groupPosition).getPgKey()) {
            case "TELE_CASH":
                groupHolder.imgIcon.setImageResource(R.drawable.tele_cash);
                break;
            case "ECO_CASH":
                groupHolder.imgIcon.setImageResource(R.drawable.eco_cash);
                break;
            case "WINLOT":
                groupHolder.imgIcon.setImageResource(R.drawable.shop);
                break;
            case "MPOWER":
                groupHolder.imgIcon.setImageResource(R.drawable.mpower);
                break;
            case "MPOWER_BANK":
                groupHolder.imgIcon.setImageResource(R.drawable.personal_bank);
                break;
            case "MPOWER_MOBILE":
                groupHolder.imgIcon.setImageResource(R.drawable.mobile_banking);
                break;

            case "PAGA_REG":
                groupHolder.imgIcon.setImageResource(R.drawable.paga_reg);
                break;
            case "PAGA_NONREG":
                groupHolder.imgIcon.setImageResource(R.drawable.paga_reg);
                break;
//            case "WINLOT":
//                groupHolder.imgIcon.setImageResource(R.drawable.winlot);
//                break;
            case "BANK_WITHDRAWAL":
                groupHolder.imgIcon.setImageResource(R.drawable.bank);
                break;
            case "INTER_SWITCH":
                groupHolder.imgIcon.setImageResource(R.drawable.interswithch);
                break;
            default:
                groupHolder.imgIcon.setImageResource(R.drawable.card1);
                break;
        }
    }


    private class GroupHolder {
        ImageView imgIcon;
        CustomTextView txtName;
        CustomTextView txtDesc;
        LinearLayout expand;
    }

    private class MPower extends GroupHolder {
        EditText mPowerUsername;
        CustomTextView txtSubmitMPower;

    }

    private class PersonalBankAcc extends GroupHolder {
        EditText getPersonalAccNo;
        BetterSpinner bankList;
        EditText personalAccName;
        EditText personalBranchName;
        CustomTextView txtSubmitPer;
    }

    private class MTNMobileMoney extends GroupHolder {
        EditText mtnMobileNo;
        BetterSpinner walletProvider;
        EditText mtnRecipient;
        CustomTextView txtSubmitMTN;
    }

    private class LagosWithdrawPagaReg extends GroupHolder {
        BetterSpinner pagaRegProvider;
        CustomTextView txtSubmitLagos;
    }

    private class LagosWithdrawPagaNonReg extends GroupHolder {
        BetterSpinner pagaNonRegProvider;
        CustomTextView txtSubmitLagos;
    }

    private class LagosWithdrawWinlot extends GroupHolder {
        BetterSpinner winlotProvider;
        CustomTextView txtSubmitLagos;
    }

    private class LagosWithdrawBankWithdraw extends GroupHolder {
        LinearLayout unModify;
        EditText bankWithdrawProvider;
        CustomTextView txtSubmitLagos;
        CustomTextView txtModify;
        EditText branhName;
        EditText accName;
        EditText accNumber;

        LinearLayout modify;
        BetterSpinner bankWithdrawProviderModify;
        CustomTextView txtSubmitLagosModify;
        CustomTextView txtSubmitBack;
        EditText accNumberModify;
        EditText branhNameModify;
        EditText accNameModify;
    }

    private class LagosWithdrawInterSwitch extends GroupHolder {
        EditText accNo;
        BetterSpinner accType;
        BetterSpinner bankType;
        CustomTextView txtSubmitLagos;
    }


    private int getLength(TextView view) {
        return view.getText().toString().length();
    }

    public void setRsCheck(boolean rsCheck) {
        this.rsCheck = rsCheck;
        notifyDataSetChanged();
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.hasFocus()) {
//                Utils.Toast(context, "this is focus changes listener", Toast.LENGTH_SHORT);
                switch (v.getId()) {
                    case R.id.bank_list:
                    case R.id.wallet_provider:
                        break;
                    default:
                        ((EditText) v).setCursorVisible(true);
                }
                if (rsCheck)
                    ((WithdrawalScreen) fragment).setSelectAmountTag(v);
            } else {
                switch (v.getId()) {
                    case R.id.bank_list:
                    case R.id.wallet_provider:
                        break;
                    default:
                        ((EditText) v).setCursorVisible(false);
                }
            }
        }
    };
}

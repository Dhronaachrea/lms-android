package com.skilrock.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.bean.BankDetailsBean;
import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.skilrock.customui.BetterSpinner;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.deposit.DepositScreen;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OutletsAdapter extends AnimatedExpandableListAdapter {

    private Context context;
    private Holder holder;
    private GroupHolder groupHolder;
    private List<CategoryInfoBean> dataBean;
    private LayoutInflater inflater;
    private boolean isMobileWallet;
    private String depositAmount;
    private boolean isOutlet;
    private final String path = "/rest/ecoNetPayment/ecoNetDepositRequest";
    private final String pathtelecash = "/rest/telecash/getFeeAmount";
    public static String mobtelcash;

    ArrayList<DepositLimitBean.PgRange> pgRng;
    private int ecocashpos, telecashpos;
    private Double compAmt;
    final private static int DIALOG_DOB = 0;

    private GlobalPref globalPref;


    public OutletsAdapter(Context context, List<CategoryInfoBean> listDataHeader, boolean isMobileWallet, String depositAmount, ArrayList<DepositLimitBean.PgRange> pgRng, boolean isOutlet) {
        this.context = context;
        this.isOutlet = isOutlet;
        this.dataBean = listDataHeader;
        this.isMobileWallet = isMobileWallet;
        this.depositAmount = depositAmount;
        this.pgRng = pgRng;
        inflater = LayoutInflater.from(context);
        globalPref = GlobalPref.getInstance(context);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return "";
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataBean.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.dataBean.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getRealChildType(int groupPosition, int childPosition) {
        if (groupPosition == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public int getRealChildTypeCount() {
        return 2;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        CategoryInfoBean bean = this.dataBean.get(groupPosition);

        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = inflater.inflate(R.layout.expandable_group_lay, null);
            groupHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            groupHolder.txtName = (CustomTextView) convertView.findViewById(R.id.name);
            groupHolder.txtDesc = (CustomTextView) convertView.findViewById(R.id.desc);
            convertView.setTag(groupHolder);

        } else {
            groupHolder = (GroupHolder) convertView.getTag();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(groupHolder.imgIcon.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(groupHolder.txtName.getWindowToken(), 0);
        }
        groupHolder.txtDesc.setVisibility(View.GONE);
        groupHolder.imgIcon.setImageResource(bean.getIcon());
        groupHolder.txtName.setText(bean.getMode());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        int childType = getRealChildType(groupPosition, childPosition);
        int grppos = groupPosition;


        if ((convertView == null) && (globalPref.getCountry().equalsIgnoreCase("ZIM"))) {
            if (groupPosition == 0 && !isOutlet) {
                holder = new EchoCashHolder();
                EchoCashHolder echoCashHolder = (EchoCashHolder) holder;
                convertView = inflater.inflate(R.layout.eco_cash_activity, null);
                echoCashHolder.txtSubmit = (CustomTextView) convertView.findViewById(R.id.txt_submit);
                echoCashHolder.edMobile = (EditText) convertView.findViewById(R.id.ed_mobile_number);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(echoCashHolder.edMobile, InputMethodManager.SHOW_IMPLICIT);
                convertView.setTag(holder);


            } else if (groupPosition == 1 && !isOutlet) {
                holder = new TeleCashHolder();
                TeleCashHolder teleCashHolder = (TeleCashHolder) holder;
                convertView = inflater.inflate(R.layout.tele_cash, null);
                teleCashHolder.edMobileNumber = (EditText) convertView.findViewById(R.id.ed_mobile_number);
                teleCashHolder.teleCashFirst = (LinearLayout) convertView.findViewById(R.id.tele_cash_first);
                teleCashHolder.txtSubmit = (CustomTextView) convertView.findViewById(R.id.txt_submit);
                teleCashHolder.teleCashFirst.setVisibility(View.VISIBLE);

                teleCashHolder.teleCashSecond = (LinearLayout) convertView.findViewById(R.id.tele_cash_second);
                teleCashHolder.txnAmt = (EditText) convertView.findViewById(R.id.txn_amt);
                teleCashHolder.feeAmount = (EditText) convertView.findViewById(R.id.feeAmount);
                teleCashHolder.totalAmount = (EditText) convertView.findViewById(R.id.total_amount);
                teleCashHolder.otpSecond = (EditText) convertView.findViewById(R.id.otp_second);
                teleCashHolder.txtSubmitSecond = (CustomTextView) convertView.findViewById(R.id.txt_submit_second);
                teleCashHolder.teleCashSecond.setVisibility(View.GONE);

//                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(teleCashHolder.edMobile, InputMethodManager.SHOW_IMPLICIT);
                convertView.setTag(holder);
            } else if (groupPosition == 2 && !isOutlet) {
                holder = new OneWalletHolder();
                OneWalletHolder oneWalletHolder = (OneWalletHolder) holder;
                convertView = inflater.inflate(R.layout.one_wallet, null);
                oneWalletHolder.txtDesc = (CustomTextView) convertView.findViewById(R.id.txt_desc);
            } else {
                holder = new ChildHolder();
                ChildHolder childHolder = (ChildHolder) holder;
                convertView = inflater.inflate(R.layout.info_child, null);
                childHolder.txtDesc = (CustomTextView) convertView.findViewById(R.id.txt_desc);
                childHolder.txtDesc.setText(dataBean.get(childPosition).getDesc());
                convertView.setTag(holder);
            }
        } else if (convertView == null && globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
            if (groupPosition == 0) {
                holder = new OnlineTransfer();
                OnlineTransfer onltrsf = (OnlineTransfer) holder;
                convertView = inflater.inflate(R.layout.online_lagos_deposite, null);
                onltrsf.notifyFirst = (LinearLayout) convertView.findViewById(R.id.notify_first);
                onltrsf.bankInfo = (CustomTextView) convertView.findViewById(R.id.bank_info);
                onltrsf.header = (CustomTextView) convertView.findViewById(R.id.header);
                onltrsf.notifyFirstSubmit = (CustomTextView) convertView.findViewById(R.id.txt_submit_notify_first);

                onltrsf.notifySecond = (LinearLayout) convertView.findViewById(R.id.notify_second);
                onltrsf.paymentOption = (BetterSpinner) convertView.findViewById(R.id.payment_option);
                onltrsf.selectBank = (BetterSpinner) convertView.findViewById(R.id.select_bank);
                onltrsf.enterBranch = (EditText) convertView.findViewById(R.id.enter_branch);
                onltrsf.date = (EditText) convertView.findViewById(R.id.date);
                onltrsf.tellerName = (EditText) convertView.findViewById(R.id.teller_name);
                onltrsf.notifySecondSubmit = (CustomTextView) convertView.findViewById(R.id.txt_submit_notify_second);
                onltrsf.notifyFirst.setVisibility(View.VISIBLE);
                onltrsf.notifySecond.setVisibility(View.GONE);
//                onltrsf.tellerName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH)))});
                convertView.setTag(holder);
            } else if (groupPosition == 1) {
                holder = new WinlotOffice();
                WinlotOffice winlotOffice = (WinlotOffice) holder;
                convertView = inflater.inflate(R.layout.info_child, null);
                winlotOffice.txtDesc = (CustomTextView) convertView.findViewById(R.id.txt_desc);
                convertView.setTag(holder);
            } else if (groupPosition == 2) {
                holder = new PagaOutlet();
                PagaOutlet pagaOutlet = (PagaOutlet) holder;
                convertView = inflater.inflate(R.layout.info_child, null);
                pagaOutlet.txtDesc = (CustomTextView) convertView.findViewById(R.id.txt_desc);
                convertView.setTag(holder);
            }
        } else
            holder = (Holder) convertView.getTag();

        if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
            if (grppos == 0 && !isOutlet) {
                final EchoCashHolder echoCashHolder = (EchoCashHolder) holder;
                echoCashHolder.txtSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String mob1 = echoCashHolder.edMobile.getText().toString();
                            int mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));
                            if (mob1.length() == mobilelen) {
                                JSONObject data = new JSONObject();
                                data.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                                data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                data.put("mobileNumber", mob1);
                                data.put("depositAmt", depositAmount);
                                new PMSWebTask((Activity) context, path, "N/A", data, "ecocash", null, "Loading...").execute();
                            } else {
                                Utils.Toast(context, context.getResources().getString(R.string.mobile_no_validate) + " " + mobilelen);
                            }
                        } catch (Exception e) {

                        }
                    }
                });

            } else if (grppos == 1 && !isOutlet) {
                final TeleCashHolder teleCashHolder = (TeleCashHolder) holder;
                teleCashHolder.txtSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            mobtelcash = teleCashHolder.edMobileNumber.getText().toString();
                            int mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));
                            if (mobtelcash.length() == mobilelen) {
                                JSONObject data = new JSONObject();
                                data.put("playerId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.PLAYER_ID));
                                data.put("sessionId", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                data.put("txnAmount", Double.parseDouble(depositAmount));
                                data.put("mobileNbr", mobtelcash);
                                DepositScreen.telechashMobileNo = mobtelcash;

                                new PMSWebTask((Activity) context, pathtelecash, "N/A", data, "telecash", null, "Loading...").execute();
                            } else {
                                Utils.Toast(context, context.getResources().getString(R.string.mobile_no_validate) + " " + mobilelen);
                            }

                        } catch (Exception e) {
                        }
                    }
                });

            } else if (grppos == 2 && !isOutlet) {
                OneWalletHolder oneWalletHolder = (OneWalletHolder) holder;
                oneWalletHolder.txtDesc.setText(((CategoryInfoBean) getGroup(groupPosition)).getDesc());
            }
        } else if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {

            if (grppos == 0) {
                final OnlineTransfer onlTrsfr = (OnlineTransfer) holder;
                onlTrsfr.date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setDate(onlTrsfr);
                    }
                });
                if (DepositScreen.bankDetails.get("providerList") == null) {
                    onlTrsfr.notifyFirst.setVisibility(View.GONE);
                    onlTrsfr.notifySecond.setVisibility(View.GONE);
                } else {
                    BankDetailsBean bean = DepositScreen.bankDetails.get("providerList").get(0);
                    onlTrsfr.header.setText(bean.getBankName());
                    onlTrsfr.bankInfo.setText(bean.getBankInfo());

                    ArrayList<String> paymentList = new ArrayList<>();
                    paymentList.add("CASH");
                    paymentList.add("ONLINE");
                    ArrayAdapter<String> paymentAdapter = new ArrayAdapter<String>(context, R.layout.txn_spinner_row, R.id.spinner_text, paymentList);
                    onlTrsfr.paymentOption.setText("-select payment option-");
                    onlTrsfr.paymentOption.setAdapter(paymentAdapter);


                    ArrayList<String> bankList = new ArrayList<>();
                    bankList.add(bean.getBankName());
                    ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(context, R.layout.txn_spinner_row, R.id.spinner_text, bankList);
                    onlTrsfr.selectBank.setText("-select bank-");
                    onlTrsfr.selectBank.setAdapter(bankAdapter);

                    onlTrsfr.notifyFirstSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onlTrsfr.notifySecond.setVisibility(View.VISIBLE);
                            onlTrsfr.notifyFirst.setVisibility(View.GONE);
                        }
                    });

                    onlTrsfr.notifySecondSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ///com/skilrock/pms/mobile/accMgmt/Action/notifyDeposit.action?
                            String notfyPath = "/com/skilrock/pms/mobile/accMgmt/Action/notifyDeposit.action?";
                            BankDetailsBean bean = DepositScreen.bankDetails.get("providerList").get(0);
                            String paymentOptionName = onlTrsfr.paymentOption.getText().toString();
                            String bankName = onlTrsfr.selectBank.getText().toString();
                            String bankId = bean.getBankId();
                            String amount = Double.parseDouble(depositAmount) + "";
                            String tellerNo = onlTrsfr.tellerName.getText().toString();
                            String depositDate = onlTrsfr.date.getText().toString();
                            String branchName = onlTrsfr.enterBranch.getText().toString();

                            if (isValidate(onlTrsfr)) {
                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    data.put("bankId", bankId);
                                    data.put("amount", amount);
                                    data.put("depositDate", depositDate);
                                    data.put("tellerNo", tellerNo);
                                    data.put("paymentType", paymentOptionName);
//                                    data.put("branchName", "");//optional
                                    data.put("branchName", branchName.replaceAll("'", ""));
                                    data.put("narration", "NA");
                                    String path = notfyPath + "requestData=" + URLEncoder.encode(data.toString());
                                    new PMSWebTask((Activity) context, path, "", null, "notfyLotsDeposite", null, "Loading...").execute();
                                } catch (Exception e) {
                                }
                            }

                        }
                    });
                }
            } else if (grppos == 1) {
                WinlotOffice winlotOffice = (WinlotOffice) holder;
                winlotOffice.txtDesc.setText(((CategoryInfoBean) getGroup(groupPosition)).getDesc());
            } else if (grppos == 2) {
                PagaOutlet pagaOutlet = (PagaOutlet) holder;
                pagaOutlet.txtDesc.setText(((CategoryInfoBean) getGroup(groupPosition)).getDesc());
            }
        } else {
            ChildHolder childHolder = (ChildHolder) holder;
            childHolder.txtDesc.setText(((CategoryInfoBean) getGroup(groupPosition)).getDesc());
        }


        return convertView;

    }

    private boolean isValidate(OnlineTransfer onlTrsfr) {
        int count = 0;
        int mobilelen = 0;

        if ((onlTrsfr.selectBank.getText().toString().contains("select"))
                && (onlTrsfr.paymentOption.getText().toString().contains("select")) &&
                (onlTrsfr.tellerName.getText().toString().length() == 0)
                && (onlTrsfr.enterBranch.getText().toString().length() == 0)
                && (onlTrsfr.date.getText().toString().length() == 0)) {
            Utils.Toast(context, "Please fill all details correctly");
            return false;
        } else {
            if (!dropDownCheck(onlTrsfr.selectBank)) {
                Utils.Toast(context,
                        "Please select bank"
                );
                return false;
            } else if (!dropDownCheck(onlTrsfr.paymentOption)) {
                Utils.Toast(context,
                        "Please select payment option"
                );
                return false;
            } /*else if (onlTrsfr.enterBranch.getText().toString().length() == 0) {
                Utils.Toast(context, "Branch name can not be empty", Toast.LENGTH_SHORT);
                return false;
            }*/ else if (!dateValidation(onlTrsfr)) {
                Utils.Toast(context, "Date can not be empty");
                return false;
            } else if (!mobileValidate(onlTrsfr)) {
                Utils.Toast(context,
                        "Teller Number Should Not Be Blank"
                );
                return false;
            } else {
                return true;
            }
        }
    }


    private boolean dateValidation(OnlineTransfer onlTrsfr) {
        String dateStr = onlTrsfr.date.getText().toString();
        if (dateStr == null || dateStr.equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }

//        Calendar calendar = Calendar.getInstance();
//        int dayR = calendar.get(Calendar.DAY_OF_MONTH);
//        int monthR = calendar.get(Calendar.MONTH);
//        int yearR = calendar.get(Calendar.YEAR);
//
//        int day, month, year;
//        Calendar currentCal = Calendar.getInstance();
//        int break1 = dateStr.indexOf('-');
//        int break2 = dateStr.indexOf("-", break1 + 1);
//        day = Integer.parseInt(dateStr.substring(0, break1));
//        month = Integer.parseInt(dateStr.substring(break1 + 1, break2)) - 1;
//        year = Integer.parseInt(dateStr.substring(break2 + 1));
//
//        int diffYear = yearR - year;
//        int diffMonth = monthR - month;
//        int diffDay = dayR - day;
//
//        if ((diffYear >= 0)) {
//            if ((diffMonth >= 0)) {
//                if ((diffDay >= 0)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
    }

    private boolean dropDownCheck(BetterSpinner spinner) {
        if (spinner.getText().toString().contains("select"))
            return false;
        return true;
    }

    private boolean mobileValidate(OnlineTransfer onlTrsfr) {
//        int mobilelen = Integer.parseInt(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.MOBILE_NO_LENGTH));
        if (onlTrsfr.tellerName == null || onlTrsfr.tellerName.getText().toString().equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }
    }

    private void setDate(final OnlineTransfer onltransfr) {
        //new code
        DatePickerDialog dateDlg;
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);//calender year starts from 1900 so you must add 1900 to the value recevie.i.e., 1990+112 = 2012
        int cmonth = c.get(Calendar.MONTH);//this is april so you will receive  3 instead of 4.
        int cday = c.get(Calendar.DAY_OF_MONTH);
        dateDlg = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);
                        System.out.println(dtDob);
                        CharSequence strDate = DateFormat.format(
                                "dd-MM-yyyy", dtDob);
                        Utils.Toast(
                                context,
                                context.getResources().getString(R.string.date_picked)
                                        + strDate);
                        onltransfr.date.setText(strDate);
                    }
                }, cyear, cmonth, cday);
        dateDlg.show();
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }


    private class EchoCashHolder implements Holder {
        CustomTextView txtSubmit;
        EditText edMobile;
    }


    private class OneWalletHolder implements Holder {
        CustomTextView txtDesc;
    }

    private class TeleCashHolder implements Holder {
        LinearLayout teleCashFirst;
        EditText edMobileNumber;
        CustomTextView txtSubmit;
        LinearLayout teleCashSecond;
        EditText txnAmt;
        EditText feeAmount;
        EditText totalAmount;
        EditText otpSecond;
        CustomTextView txtSubmitSecond;
    }

    private class ChildHolder implements Holder {
        CustomTextView txtDesc;
    }

    private class GroupHolder {
        ImageView imgIcon;
        CustomTextView txtName;
        CustomTextView txtDesc;
    }

    //lagos
    private class WinlotOffice implements Holder {
        CustomTextView txtDesc;
    }

    private class PagaOutlet implements Holder {
        CustomTextView txtDesc;
    }

    private class OnlineTransfer implements Holder {
        LinearLayout notifyFirst;
        CustomTextView header;
        CustomTextView bankInfo;
        CustomTextView notifyFirstSubmit;
        LinearLayout notifySecond;
        BetterSpinner paymentOption;
        BetterSpinner selectBank;
        EditText enterBranch;
        EditText date;
        EditText tellerName;
        CustomTextView notifySecondSubmit;

    }

    private interface Holder {

    }


}

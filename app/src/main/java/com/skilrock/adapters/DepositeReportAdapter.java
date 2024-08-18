package com.skilrock.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skilrock.bean.TxnBean;
import com.skilrock.bean.WithDrawRepBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AmountTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stpl on 1/5/2016.
 */
public class DepositeReportAdapter extends ArrayAdapter<WithDrawRepBean> {
    private Context context;
    private int resource;
    private ArrayList<WithDrawRepBean.DepositData> depBeans;
    private WithDrawRepBean Wbean;
    private String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public DepositeReportAdapter(Context context, int resource, WithDrawRepBean bean) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.Wbean = bean;
    }

    @Override
    public int getCount() {
        return Wbean.getDepositData().size();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        String date = "", month = "", time = "", year = "";
        if (convertView == null) {
            holder = new Holder();
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.month = (TextView) convertView.findViewById(R.id.month);

            holder.bankName = (RobotoTextView) convertView.findViewById(R.id.bank_name);
            holder.requestId = (CustomTextView) convertView.findViewById(R.id.request_id);
            holder.tellerNumber = (CustomTextView) convertView.findViewById(R.id.teller_number);
            holder.txnstatus = (CustomTextView) convertView.findViewById(R.id.status);

            holder.paymentOptions = (CustomTextView) convertView.findViewById(R.id.payment_options);
            holder.depositAmount = (AmountTextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        WithDrawRepBean.DepositData bean = Wbean.getDepositData().get(position);

        if (bean.getRequestDate() == null || bean.getRequestDate() == "" || bean.getRequestDate() == "N/A") {
            holder.date.setVisibility(View.INVISIBLE);
            holder.month.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
        } else {
            holder.date.setVisibility(View.VISIBLE);
            holder.month.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            String[] dateTime = bean.getRequestDate().split(" ");
            SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
            Date dt;
            String formattedDate = "";
            try {
                formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                dt = (Date) ((DateFormat) formatter).parse(dateTime[0]);
                formattedDate = formatter.format(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (dateTime.length > 1) {
                date = formattedDate.split("/")[0];
                month = formattedDate.split("/")[1];
                time = dateTime[1].split(":")[0] + ":" + dateTime[1].split(":")[1];
            }
            holder.date.setText(date);
            holder.month.setText(MONTH[Integer.parseInt(month) - 1]);
            holder.time.setText(time);
        }

        if (bean.getBankName() == null) {
            holder.bankName.setVisibility(View.GONE);
        } else {
            holder.bankName.setText(bean.getBankName().toString());
        }

        if (bean.getRequestId() == null) {
            holder.requestId.setVisibility(View.GONE);
        } else {
            holder.requestId.setVisibility(View.VISIBLE);
            holder.requestId.setText("Request Id : " + bean.getRequestId());
        }

        if (bean.getTellerNbr() == null) {
            holder.tellerNumber.setVisibility(View.GONE);
        } else {
            holder.tellerNumber.setVisibility(View.VISIBLE);
            holder.tellerNumber.setText("Teller Number : " + bean.getTellerNbr());
        }


//        if (bean.getWithdrawlRequestData().get(position).getWithdrawlchannel() == null) {
//            holder.txnType.setVisibility(View.GONE);
//        } else {
//            holder.txnType.setVisibility(View.VISIBLE);
//            holder.txnType.setText(bean.getWithdrawlRequestData().get(position).getWithdrawlchannel());
//        }

        if (bean.getStatus() == null) {
            holder.txnstatus.setVisibility(View.GONE);
        } else {
            holder.txnstatus.setVisibility(View.VISIBLE);
            if (bean.getStatus().equalsIgnoreCase("APPROVED") || bean.getStatus().equalsIgnoreCase("DONE")) {
                holder.txnstatus.setText(bean.getStatus());
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg));
            } else if (bean.getStatus().equalsIgnoreCase("PENDING")) {
                holder.txnstatus.setText(bean.getStatus());
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_light_red));
            } else if (bean.getStatus().equalsIgnoreCase("CANCELLED_BY_BO")) {
                holder.txnstatus.setText("CANCELLED");
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            } else if (bean.getStatus().equalsIgnoreCase("TIME_EXPIRED")) {
                holder.txnstatus.setText("EXPIRED");
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            } else if (bean.getStatus().equalsIgnoreCase("WITHDRAWL_FAILED")) {
                holder.txnstatus.setText("FAILED");
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            } else {
                holder.txnstatus.setText(bean.getStatus());
                holder.txnstatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            }
        }


        if (bean.getPaymentOption() == null) {
            holder.paymentOptions.setVisibility(View.GONE);
        } else {
            holder.paymentOptions.setVisibility(View.VISIBLE);
            holder.paymentOptions.setText(bean.getPaymentOption());
        }

        if (bean.getDepositAmount() == null) {
            holder.depositAmount.setVisibility(View.GONE);
        } else {
            holder.depositAmount.setVisibility(View.VISIBLE);
            holder.depositAmount.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
            holder.depositAmount.setCurrencySymbolColor(context.getResources().getColor(R.color.txn_status_appr_color));
            holder.depositAmount.setTextColor(context.getResources().getColor(R.color.txn_status_appr_color));
            holder.depositAmount.setText(bean.getDepositAmount());
        }
        return convertView;
    }


    class Holder {
        private TextView date, time, month;
        private CustomTextView requestId, tellerNumber, txnstatus;
        private RobotoTextView bankName;
        private CustomTextView paymentOptions;
        private AmountTextView depositAmount;
    }
}
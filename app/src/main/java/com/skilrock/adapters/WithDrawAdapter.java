package com.skilrock.adapters;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stpl on 8/13/2015.
 */
public class WithDrawAdapter extends ArrayAdapter<TxnBean.TransactionData> {
    private Context context;
    private int resource;
    private ArrayList<TxnBean.TransactionData> txnBeans;
    private WithDrawRepBean bean;
    private String[] MONTH = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public WithDrawAdapter(Context context, int resource, WithDrawRepBean bean) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.bean = bean;
    }

    @Override
    public int getCount() {
        return bean.getWithdrawlRequestData().size();
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
            holder.balance = (AmountTextView) convertView.findViewById(R.id.amount);
            holder.closingBalance = (CustomTextView) convertView.findViewById(R.id.closing_bal);
            holder.clzBalText = (CustomTextView) convertView.findViewById(R.id.clzBalText);
            holder.expDate = (CustomTextView) convertView.findViewById(R.id.exp_date);
            holder.verCode = (CustomTextView) convertView.findViewById(R.id.ver_code);
            holder.txnStatus = (CustomTextView) convertView.findViewById(R.id.status);
            holder.txnType = (CustomTextView) convertView.findViewById(R.id.txn_type);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        if (bean.getWithdrawlRequestData().get(position).getTransactionTime() == null) {
            holder.date.setVisibility(View.INVISIBLE);
            holder.month.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);

        } else {
            holder.date.setVisibility(View.VISIBLE);
            holder.month.setVisibility(View.VISIBLE);
            holder.time.setVisibility(View.VISIBLE);
            String[] dateTime = bean.getWithdrawlRequestData().get(position).getTransactionTime().split(" ");


            SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
            Date dt;
            String formattedDate = "";
            try {
                dt = (Date) ((DateFormat) formatter).parse(dateTime[0]);
                formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
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

        holder.balance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        if (bean.getWithdrawlRequestData().get(position).getPayableAmount() == 0) {
            holder.balance.setVisibility(View.INVISIBLE);
        } else {
            holder.balance.setVisibility(View.VISIBLE);
            String payableamt = AmountFormat.getAmountFormatForMobile(bean.getWithdrawlRequestData().get(position).getPayableAmount());
            holder.balance.setCurrencySymbolColor(context.getResources().getColor(R.color.txn_status_appr_color));
            holder.balance.setTextColor(context.getResources().getColor(R.color.txn_status_appr_color));

            holder.balance.setText(payableamt);
        }
        if (bean.getWithdrawlRequestData().get(position).getWithdrawlchannel() == null) {
            holder.txnType.setVisibility(View.GONE);
        } else {
            holder.txnType.setVisibility(View.VISIBLE);
            holder.txnType.setText(bean.getWithdrawlRequestData().get(position).getWithdrawlchannel());
        }
        if (bean.getWithdrawlRequestData().get(position).getTransactionStatus() == null) {
            holder.txnStatus.setVisibility(View.GONE);
        } else {
            holder.txnStatus.setVisibility(View.VISIBLE);
            if (bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("APPROVED") || bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("DONE")) {
                holder.txnStatus.setText(bean.getWithdrawlRequestData().get(position).getTransactionStatus());
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg));
            } else if (bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("PENDING")) {
                holder.txnStatus.setText(bean.getWithdrawlRequestData().get(position).getTransactionStatus());
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_light_red));
            } else if (bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("CANCELLED_BY_BO")) {
                holder.txnStatus.setText("CANCELLED");
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));

            } else if (bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("TIME_EXPIRED")) {
                holder.txnStatus.setText("EXPIRED");
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            } else if (bean.getWithdrawlRequestData().get(position).getTransactionStatus().equalsIgnoreCase("WITHDRAWL_FAILED")) {
                holder.txnStatus.setText("WITHDRAWAL FAILED");
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            } else {
                holder.txnStatus.setText(bean.getWithdrawlRequestData().get(position).getTransactionStatus());
                holder.txnStatus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.txn_status_bg_red));
            }
        }
        if (bean.getWithdrawlRequestData().get(position).getExpiryDate() == null) {
            holder.expDate.setVisibility(View.GONE);
            if (bean.getWithdrawlRequestData().get(position).getPaidDate() != null) {
                holder.expDate.setVisibility(View.VISIBLE);
                String[] expdateTime = bean.getWithdrawlRequestData().get(position).getPaidDate().split(" ");
                SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
                Date dt;
                String formattedDate = "";
                try {
                    dt = (Date) ((DateFormat) formatter).parse(expdateTime[0]);
                    formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    formattedDate = formatter.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (expdateTime.length > 1) {
                    date = formattedDate.split("/")[0];
                    month = formattedDate.split("/")[1];
                    year = formattedDate.split("/")[2];
                    time = expdateTime[1].split(":")[0] + ":" + expdateTime[1].split(":")[1];
                }
                String expdate = date + " " + MONTH[Integer.parseInt(month) - 1] + " " + year + " " + time;
                holder.expDate.setText("Paid Date" + " " + expdate);
            } else if (bean.getWithdrawlRequestData().get(position).getCancelDate() != null) {
                holder.expDate.setVisibility(View.VISIBLE);
                String[] expdateTime = bean.getWithdrawlRequestData().get(position).getCancelDate().split(" ");
                SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
                Date dt;
                String formattedDate = "";
                try {
                    dt = (Date) ((DateFormat) formatter).parse(expdateTime[0]);
                    formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    formattedDate = formatter.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (expdateTime.length > 1) {
                    date = formattedDate.split("/")[0];
                    month = formattedDate.split("/")[1];
                    year = formattedDate.split("/")[2];
                    time = expdateTime[1].split(":")[0] + ":" + expdateTime[1].split(":")[1];
                }
                String expdate = date + " " + MONTH[Integer.parseInt(month) - 1] + " " + year + " " + time;
                holder.expDate.setText("Cancel Date" + " " + expdate);
            } else if (bean.getWithdrawlRequestData().get(position).getExpiredDate() != null) {
                holder.expDate.setVisibility(View.VISIBLE);
                String[] expdateTime = bean.getWithdrawlRequestData().get(position).getExpiredDate().split(" ");
                SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
                Date dt;
                String formattedDate = "";
                try {
                    dt = (Date) ((DateFormat) formatter).parse(expdateTime[0]);
                    formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    formattedDate = formatter.format(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (expdateTime.length > 1) {
                    date = formattedDate.split("/")[0];
                    month = formattedDate.split("/")[1];
                    year = formattedDate.split("/")[2];
                    time = expdateTime[1].split(":")[0] + ":" + expdateTime[1].split(":")[1];
                }
                String expdate = date + " " + MONTH[Integer.parseInt(month) - 1] + " " + year + " " + time;
                holder.expDate.setText("Expired Date" + " " + expdate);
            }
        } else {
            holder.expDate.setVisibility(View.VISIBLE);
            String[] expdateTime = bean.getWithdrawlRequestData().get(position).getExpiryDate().split(" ");
            SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
            Date dt;
            String formattedDate = "";
            try {
                dt = (Date) ((DateFormat) formatter).parse(expdateTime[0]);
                formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                formattedDate = formatter.format(dt);
            } catch (Exception e1) {
                e1.printStackTrace();
                holder.expDate.setText("Expiry Date" + " " + bean.getWithdrawlRequestData().get(position).getExpiryDate());
            }
            if (expdateTime.length > 1) {
                date = formattedDate.split("/")[0];
                month = formattedDate.split("/")[1];
                year = formattedDate.split("/")[2];
                time = expdateTime[1].split(":")[0] + ":" + expdateTime[1].split(":")[1];
                String expdate = date + " " + MONTH[Integer.parseInt(month) - 1] + " " + year + " " + time;
                holder.expDate.setText("Expiry Date" + " " + expdate);
            }
        }
        if (bean.getWithdrawlRequestData().get(position).getVerificationCode() == null || bean.getWithdrawlRequestData().get(position).getVerificationCode().equalsIgnoreCase("0")) {
            holder.verCode.setVisibility(View.GONE);
        } else {
            holder.verCode.setVisibility(View.VISIBLE);
            holder.verCode.setText("Verification Code" + " " + bean.getWithdrawlRequestData().get(position).getVerificationCode());
        }
        return convertView;
    }

    class Holder {
        private TextView date, time, month;
        private AmountTextView balance;
        private CustomTextView closingBalance, expDate, verCode, txnStatus, txnType, clzBalText;
    }
}

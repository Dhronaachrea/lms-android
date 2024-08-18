package com.skilrock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skilrock.bean.TxnBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AmountTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.CustomTypefaceSpan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TxnAdapter extends ArrayAdapter<TxnBean.TransactionData> {
    private Context context;
    private int resource;
    private ArrayList<TxnBean.TransactionData> txnBeans;
    private boolean isClosing = false;

    public TxnAdapter(Context context, int resource,
                      ArrayList<TxnBean.TransactionData> resultBeans, boolean isClosing) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.txnBeans = resultBeans;
        this.isClosing = isClosing;
    }

    @Override
    public int getCount() {
        return txnBeans.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
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
        TxnBean.TransactionData bean = txnBeans.get(position);
        String[] dateTime = bean.getTransactionTime().split(" ");
        String date = "", month = "", time = "";

        SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
        Date dt;
        String formattedDate = "";
        try {
            dt = formatter.parse(dateTime[0]);
            formatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
            formattedDate = formatter.format(dt);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (dateTime.length > 1) {
            String dateValue[] = formattedDate.split("/");
            date = dateValue[0];
            month = dateValue[1];
            time = dateTime[1].split(":")[0] + ":" + dateTime[1].split(":")[1];
        }
        holder.date.setText(date);
        holder.time.setText(time);
        holder.month.setText(month.toUpperCase(Locale.ENGLISH));
        holder.balance.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        holder.balance.setText("");

        if (bean.getTransactionMode().equalsIgnoreCase("DEBIT")) {
            holder.balance.setText("-" + AmountFormat.getAmountFormatForTwoDecimal(bean.getTransactionAmount()));
            holder.balance.setCurrencySymbolColor(context.getResources().getColor(R.color.txn_status_pending_color));
            holder.balance.setTextColor(context.getResources().getColor(R.color.txn_status_pending_color));
        } else {
            holder.balance.setText(bean.getTransactionAmount());
            holder.balance.setCurrencySymbolColor(context.getResources().getColor(R.color.txn_color_green));
            holder.balance.setTextColor(context.getResources().getColor(R.color.txn_color_green));
        }
        if (!isClosing) {
            setStyledClzBal(holder.clzBalText);
            setStyledClzBal(holder.closingBalance, VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + "" + AmountFormat.getAmountFormatTxn(Double.parseDouble(bean.getPlayerBalance().replace(",", ""))));
            holder.clzBalText.setVisibility(View.VISIBLE);
            holder.closingBalance.setVisibility(View.VISIBLE);
        } else {
            holder.clzBalText.setVisibility(View.INVISIBLE);
            holder.closingBalance.setVisibility(View.INVISIBLE);

        }
        holder.expDate.setVisibility(View.VISIBLE);

        holder.expDate.setText(bean.getTransactionType());
        holder.verCode.setVisibility(View.GONE);
        if (bean.getTxnStatus() == null) {
            holder.txnStatus.setVisibility(View.INVISIBLE);
        } else
            holder.txnStatus.setText(bean.getTxnStatus());
        holder.txnType.setText(bean.getTransCategory());
        return convertView;
    }


    class Holder {
        private TextView date, time, month;
        private AmountTextView balance;
        private CustomTextView closingBalance, expDate, verCode, txnStatus, txnType, clzBalText;
    }

    public void setStyledClzBal(TextView clzBal) {
        SpannableString styledString = new SpannableString("Closing Bal");
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "ROBOTO-THIN.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, styledString.length(), 0);
        clzBal.setText(styledString);
    }

    public void setStyledClzBal(TextView clzBal, String bal) {
        SpannableString styledString = new SpannableString(bal);
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "ROBOTO-BOLD.TTF");
        styledString.setSpan(new CustomTypefaceSpan("", typefaceThin), 0, styledString.length(), 0);
        clzBal.setText(styledString);
    }
}

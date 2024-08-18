package com.skilrock.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.skilrock.bean.PurchaseTicketBean;
import com.skilrock.bean.TicketBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AmountTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TicketAdapter extends ArrayAdapter<TicketBean> {
    private Context context;
    private int resource;
    private ArrayList<PurchaseTicketBean> tcktBeans;
    private int DG = R.drawable.draw_game;
    private int SG = R.drawable.m_scratch;
    private int IGE = R.drawable.e_scratch;
    private int SL = R.drawable.sports_lottery;
    private int WIN = R.drawable.money_icon_ticket;
    private int LOSE = R.drawable.img_sad;
    private int RA = R.drawable.img_clock;
    private int SETTLEMENT_PENDING = R.drawable.pending_icon;

    public TicketAdapter(Context context, int resource,
                         ArrayList<PurchaseTicketBean> resultBeans) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.tcktBeans = resultBeans;
    }

    @Override
    public int getCount() {
        return tcktBeans.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, null);
            holder.isWinning = (ImageView) convertView.findViewById(R.id.isWinning);
            holder.txnId = (CustomTextView) convertView.findViewById(R.id.txnId);
            holder.gameName = (CustomTextView) convertView.findViewById(R.id.gameName);
            holder.txnDateTime = (CustomTextView) convertView.findViewById(R.id.txnDateTime);
            holder.ticketAmount = (AmountTextView) convertView.findViewById(R.id.ticketAmount);
            holder.gameDisplayName = (CustomTextView) convertView.findViewById(R.id.gameDisplayName);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        PurchaseTicketBean bean = tcktBeans.get(position);
        holder.txnId.setText(bean.getTxnId());
        setWinning(bean, holder.gameName);
        String[] startTime = bean.getTxnTime().split(" ");

        SimpleDateFormat formatter = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
        Date dt;
        String formattedDate = "";
        try {
            dt = formatter.parse(startTime[0]);
            formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            formattedDate = formatter.format(dt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String[] date = formattedDate.split("-");
        String[] time = startTime[1].split(":");
        holder.txnDateTime.setText(date[0] + " " + date[1].toUpperCase(Locale.US) + " " + time[0] + ":" + time[1] + "");
        //\ holder.txnDateTime.setText(bean.getTxnTime());
        holder.gameDisplayName.setText(bean.getGameDispName());
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        holder.ticketAmount.setCurrencySymbol(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE));
        holder.ticketAmount.setText(df.format(bean.getTxnAmount()));
        if (bean.getTxnPwt().equalsIgnoreCase("WIN")) {
            holder.isWinning.setImageResource(WIN);
            holder.isWinning.setVisibility(View.VISIBLE);
        }/* else if (bean.getTxnPwt().equalsIgnoreCase("NON_WIN")) { // remove after discussion bug id - 20319
            holder.isWinning.setImageResource(LOSE);
            holder.isWinning.setVisibility(View.VISIBLE);
        }*/ else if (bean.getTxnPwt().equalsIgnoreCase("SETTLEMENT PENDING")) {
            holder.isWinning.setImageResource(SETTLEMENT_PENDING);
            holder.isWinning.setVisibility(View.VISIBLE);
        } else if (bean.getTxnPwt().equalsIgnoreCase("RA")) {
            holder.isWinning.setImageResource(RA);
            holder.isWinning.setVisibility(View.VISIBLE);
        } else {
            holder.isWinning.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class Holder {
        private CustomTextView txnId, gameName, txnDateTime, gameDisplayName;
        private AmountTextView ticketAmount;
        private ImageView isWinning;
    }


    private void setWinning(PurchaseTicketBean bean, CustomTextView textView) {

        PurchaseTicketBean ticketBean = bean;
        switch (ticketBean.getServiceCode()) {
            case "SLE":
                textView.setCompoundDrawablesWithIntrinsicBounds(0, SL, 0, 0);
                if (VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SLE_SER_NAME).contains(" "))
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SLE_SER_NAME).replace(" ", "\n"));
                else
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SLE_SER_NAME));
                break;
            case "DGE":
                textView.setCompoundDrawablesWithIntrinsicBounds(0, DG, 0, 0);
                if (VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_SER_NAME).contains(" "))
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_SER_NAME).replace(" ", "\n"));
                else
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_SER_NAME));
                break;
            case "IGE":
                textView.setCompoundDrawablesWithIntrinsicBounds(0, IGE, 0, 0);
                if (VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SER_NAME).contains(" "))
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SER_NAME).replace(" ", "\n"));
                else
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.IGE_SER_NAME));
                break;
            case "SC":
                textView.setCompoundDrawablesWithIntrinsicBounds(0, SG, 0, 0);
                if (VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SC_SER_NAME).contains(" "))
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SC_SER_NAME).replace(" ", "\n"));
                else
                    textView.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.SC_SER_NAME));
                break;
        }
    }

}

package com.skilrock.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.bean.PanelData;
import com.skilrock.drawgame.five.FiveGameScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogListAdapter extends ArrayAdapter<PanelData> {

    ArrayList<PanelData> panelDatas;
    private int resource;
    private Context context;
    private Fragment fragment;
    private Holder holder;
    private TextView totalAmount;
    private final String KEY = "CLICKED";
    private Map<String, Long> lastClickMap;
    private final long minimumInterval = 500;
    private LinearLayout add_panel_lay;

    public DialogListAdapter(Fragment fragment, int resource, ArrayList<PanelData> objects, TextView totalAmount, LinearLayout add_panel_lay) {
        super(fragment.getActivity(), resource, objects);
        this.context = fragment.getActivity();
        this.panelDatas = objects;
        this.resource = resource;
        this.totalAmount = totalAmount;
        this.fragment = fragment;
        this.add_panel_lay = add_panel_lay;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder = null;
        if (view == null) {
            holder = new Holder();
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
            holder.playType = (TextView) view.findViewById(R.id.playType);
            holder.amountPlayed = (TextView) view.findViewById(R.id.playTypeAmount);
            holder.selectedNumbers = (TextView) view.findViewById(R.id.selectedNumbers);
            holder.pickedType = (TextView) view.findViewById(R.id.pickedType);
            holder.panelCamcel = (Button) view.findViewById(R.id.panelCamcel);
            holder.panelEdit = (Button) view.findViewById(R.id.panelEdit);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.panelCamcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!getTimeLimit())
                    return;
                ((FiveGameScreen) fragment).totalPurchaseAmt = AmountFormat.round(((FiveGameScreen) fragment).totalPurchaseAmt - panelDatas.get(position).getPanelamount(), 2);
                panelDatas.remove(position);
                DialogListAdapter.this.notifyDataSetChanged();
                totalAmount.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(((FiveGameScreen) fragment).totalPurchaseAmt)));
                ((FiveGameScreen) fragment).setNoofPanel(add_panel_lay);
            }
        });
        holder.panelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!getTimeLimit())
                    return;
                ((FiveGameScreen) fragment).editPanel(position, true);
            }
        });
        PanelData modal = panelDatas.get(position);
        holder.playType.setText(modal.getPlayType());
        if (modal.isQp())
            holder.pickedType.setText("QP");
        else
            holder.pickedType.setText("Manual");

        holder.amountPlayed.setText(String.valueOf(AmountFormat.getAmountFormatForMobile(modal.getPanelamount())));
        String numbers = new String(modal.getPickedNumbers().replace(",", ", "));
        holder.selectedNumbers.setText(numbers.replace(",", ", "));
        return view;
    }

    @Override
    public int getCount() {
        return panelDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class Holder {
        TextView playType, amountPlayed, selectedNumbers, pickedType;
        Button panelCamcel, panelEdit;
    }

    private boolean getTimeLimit() {
        if (lastClickMap == null)
            lastClickMap = new HashMap<>();
        Long previousClickTimestamp = lastClickMap.get(KEY);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(KEY, currentTimestamp);
        if (previousClickTimestamp == null || (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval))
            return true;
        return false;
    }
}
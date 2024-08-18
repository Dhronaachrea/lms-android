package com.skilrock.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.skilrock.bean.PanelData;
import com.skilrock.drawgame.kenoSeven.TenByNinetyGameScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;

import java.util.ArrayList;

/**
 * Created by stpl on 1/6/2016.
 */
public class DialogListAdapterTenByNinty extends ArrayAdapter<PanelData> {

    ArrayList<PanelData> panelDatas;
    private int resource;
    private Context context;
    private Fragment fragment;
    private Holder holder;
    private TextView totalAmount;
    private Dialog dialog;
    private TextView panelEditText;

    public DialogListAdapterTenByNinty(Fragment fragment, int resource, ArrayList<PanelData> objects, TextView totalAmount, Dialog showDialog, TextView panelEditText) {
        super(fragment.getActivity(), resource, objects);
        this.context = fragment.getActivity();
        this.panelDatas = objects;
        this.resource = resource;
        this.totalAmount = totalAmount;
        this.fragment = fragment;
        this.panelEditText = panelEditText;
        this.dialog = showDialog;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder = null;
        if (view == null) {
            holder = new Holder();
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
            holder.playType = (TextView) view.findViewById(R.id.playType);
            holder.amountPlayed = (TextView) view.findViewById(R.id.playTypeAmount);
            holder.playTypeAmountText = (TextView) view.findViewById(R.id.playTypeAmountText);
            holder.selectedNumbers = (TextView) view.findViewById(R.id.selectedNumbers);
            holder.pickedType = (TextView) view.findViewById(R.id.pickedType);
            holder.panelCamcel = (Button) view.findViewById(R.id.panelCamcel);
            holder.panelEdit = (Button) view.findViewById(R.id.panelEdit);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.playTypeAmountText.setText("Panel Amount :");
        holder.panelCamcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                TenByNinetyGameScreen.totalPurchaseAmt = AmountFormat.round(TenByNinetyGameScreen.totalPurchaseAmt - panelDatas.get(position).getPanelamount(), 2);
                TenByNinetyGameScreen.totalPurchaseAmt = AmountFormat.round(TenByNinetyGameScreen.totalPurchaseAmt - panelDatas.get(position).getSetSinglePanelAmt() * TenByNinetyGameScreen.finalDrawDatas.size(), 2);

                panelDatas.remove(position);
                double totalPanelAmt = 0;
                double totalPanel = 0;
                for (int i = 0; i < panelDatas.size(); i++) {
                    totalPanelAmt = totalPanelAmt + (panelDatas.get(i).getSetSinglePanelAmt() * (TenByNinetyGameScreen.finalDrawDatas.size()));
                    totalPanel = totalPanel + (panelDatas.get(i).getSetSinglePanelAmt());
                }

//                totalAmount.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(TenByNinetyGameScreen.totalPurchaseAmt)));
                //new for lagos
                totalAmount.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(totalPanelAmt)));
                panelEditText.setText(String.valueOf(AmountFormat.getCurrentAmountFormatForMobile(totalPanel)));

                DialogListAdapterTenByNinty.this.notifyDataSetChanged();
                ((TenByNinetyGameScreen) fragment).setNoofPanel();
            }
        });
        holder.panelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ((TenByNinetyGameScreen) fragment).editPanel(position, true);
            }
        });
        PanelData modal = panelDatas.get(position);
        holder.playType.setText(modal.getPlayType());
        if (modal.isQp())
            holder.pickedType.setText("QP");
        else
            holder.pickedType.setText("Manual");

        holder.amountPlayed.setText(String.valueOf(AmountFormat.getAmountFormatForMobile(modal.getSetSinglePanelAmt())));
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
        TextView playType, amountPlayed, selectedNumbers, pickedType, playTypeAmountText;
        Button panelCamcel, panelEdit;
    }
}
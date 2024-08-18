package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.skilrock.bean.MyRetailerBean;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

/**
 * Created by stpl on 9/19/2016.
 */
public class RetailerTicketAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<MyRetailerBean.TicketData> tcktBeans;
    private LayoutInflater layoutInflater;
    private Holder holder = null;
    private int DG = R.drawable.draw_game;
    private int SG = R.drawable.m_scratch;
    private int IGE = R.drawable.e_scratch;
    private int SL = R.drawable.sports_lottery;


    public RetailerTicketAdapter(Context context, int resource,
                                 ArrayList<MyRetailerBean.TicketData> resultBeans) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.tcktBeans = resultBeans;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return tcktBeans.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        setImagesForGames(holder, position);
        return convertView;
    }

    private void setImagesForGames(Holder holder, int position) {
        MyRetailerBean.TicketData bean = tcktBeans.get(position);
        holder.ticketNo.setText(bean.getTicketNumber());
        holder.gameType.setText(bean.getServiceCode());

        holder.game_img_text.setText(bean.getServiceCode());
        switch (bean.getServiceCode()) {
            case "SLE":
                holder.game_img_text.setCompoundDrawablesWithIntrinsicBounds(0, SL, 0, 0);
                break;
            case "DGE":
                holder.game_img_text.setCompoundDrawablesWithIntrinsicBounds(0, DG, 0, 0);
                break;
            case "IGE":
                holder.game_img_text.setCompoundDrawablesWithIntrinsicBounds(0, IGE, 0, 0);
                break;
            case "SC":
                holder.game_img_text.setCompoundDrawablesWithIntrinsicBounds(0, SG, 0, 0);
                break;
        }
    }

    private class Holder {
        private CustomTextView ticketNo, gameType, game_img_text;

        public Holder(View convertView) {
            ticketNo = (CustomTextView) convertView.findViewById(R.id.ticketNo);
            gameType = (CustomTextView) convertView.findViewById(R.id.game_name);
            game_img_text = (CustomTextView) convertView.findViewById(R.id.game_img_text);
        }
    }
}


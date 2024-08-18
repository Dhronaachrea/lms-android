package com.skilrock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;

public class FiveDrawResAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private String[] match;
    private String[] winner;
    private String[] amount;

    public FiveDrawResAdapter(Context context, int resource, String[] match,
                              String[] winner, String[] amount) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.amount = amount;
        this.match = match;
        this.winner = winner;
    }

    @Override
    public int getCount() {
        return amount.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, null);
            holder.mainLayout = (LinearLayout) convertView
                    .findViewById(R.id.draw_res_main_lay);
            holder.match = (CustomTextView) convertView.findViewById(R.id.match);
            holder.winner = (CustomTextView) convertView.findViewById(R.id.winner);
            holder.amount = (CustomTextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position % 2 == 0) {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#E6E6E6"));
        } else {
            holder.mainLayout.setBackgroundColor(Color.WHITE);
        }
        holder.match.setText(match[position]);
        holder.winner.setText(winner[position]);
        holder.amount.setText(amount[position]);
        if (GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("lagos"))
            holder.winner.setVisibility(View.GONE);
        return convertView;
    }

    class Holder {
        private LinearLayout mainLayout;
        private CustomTextView match, winner, amount;
    }
}

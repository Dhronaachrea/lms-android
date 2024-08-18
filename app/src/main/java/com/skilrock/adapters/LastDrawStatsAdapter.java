package com.skilrock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.bean.CommonStatsBean;
import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

public class LastDrawStatsAdapter extends ArrayAdapter<Object> {
    private Context context;
    private int resource;
    private Object resultBeans;
    private String game;

    public LastDrawStatsAdapter(Context context, int resource,
                                Object resultBeans, String game) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.game = game;
        this.resultBeans = resultBeans;
    }

    @Override
    public int getCount() {
        return ((CommonStatsBean) resultBeans).getSubDatas().size();
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
            holder.subLayout = (LinearLayout) convertView
                    .findViewById(R.id.draw_res_sub_lay);
            holder.ball = (CustomTextView) convertView.findViewById(R.id.match);
            holder.frequency = (CustomTextView) convertView.findViewById(R.id.winner);
            holder.lastSeen = (CustomTextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position % 2 == 0) {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#E6E6E6"));
            holder.subLayout.setBackgroundColor(Color.parseColor("#E6E6E6"));
        } else {
            holder.mainLayout.setBackgroundColor(Color.WHITE);
            holder.subLayout.setBackgroundColor(Color.WHITE);
        }
        if (game.equals(Config.fiveGameName) || game.equals(Config.fiveGameNameMachine)) {
            ((GradientDrawable) holder.ball.getBackground().mutate())
                    .setColor(context.getResources().getColor(
                            R.color.five_last_no_bg_color));
            CommonStatsBean.SubData data = ((CommonStatsBean) resultBeans)
                    .getSubDatas().get(position);
            holder.ball.setText(data.getBall() + "");
            holder.frequency.setText(data.getFrequency() + "");
            holder.lastSeen.setText(data.getLastSeenDisplay());
        } else if (game.equals(Config.bonusGameName)) {
            ((GradientDrawable) holder.ball.getBackground().mutate())
                    .setColor(context.getResources().getColor(
                            R.color.bonus_last_no_bg_color));
            CommonStatsBean.SubData data = ((CommonStatsBean) resultBeans)
                    .getSubDatas().get(position);
            holder.ball.setText(data.getBall() + "");
            holder.frequency.setText(data.getFrequency() + "");
            holder.lastSeen.setText(data.getLastSeenDisplay());
        } else {
            ((GradientDrawable) holder.ball.getBackground().mutate())
                    .setColor(context.getResources().getColor(
                            R.color.fast_last_no_bg_color));
            CommonStatsBean.SubData data = ((CommonStatsBean) resultBeans)
                    .getSubDatas().get(position);
            holder.ball.setText(data.getBall() + "");
            holder.frequency.setText(data.getFrequency() + "");
            holder.lastSeen.setText(data.getLastSeenDisplay());
        }
        return convertView;
    }

    class Holder {
        private LinearLayout mainLayout, subLayout;
        private CustomTextView ball, frequency, lastSeen;
    }
}

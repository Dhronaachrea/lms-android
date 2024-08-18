package com.skilrock.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.bean.FastLottoResultBean;
import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;

public class FastDrawResAdapter extends ArrayAdapter<Object> {
    private LayoutInflater layoutInflater;
    private Context context;
    private int resource;
    private Object resultBeans;
    private String currentHr = "-1";
    private ArrayList<String> strArrayList;
    Holder holder = null;

    public FastDrawResAdapter(Context context, int resource, Object resultBeans) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.resultBeans = resultBeans;
        this.layoutInflater = LayoutInflater.from(context);

        strArrayList = new ArrayList<String>();

        for (int i = 0; i < ((FastLottoResultBean) resultBeans).getWinner().length; i++) {
            FastLottoResultBean bean = (FastLottoResultBean) resultBeans;
            String[] hours = bean.getHour();

            if (currentHr.equals(hours[i])) {
                strArrayList.add("true");
            } else {
                currentHr = bean.getHour()[i];
                strArrayList.add("false");
            }
        }
    }

    @Override
    public int getCount() {
        return ((FastLottoResultBean) resultBeans).getWinner().length;
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
        FastLottoResultBean bean = (FastLottoResultBean) resultBeans;
        if (position % 2 == 0) {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#E6E6E6"));
        } else {
            holder.mainLayout.setBackgroundColor(Color.WHITE);
        }
        String[] strings = bean.getBallInfo()[position].split(" ");
        holder.winner.setText(bean.getWinner()[position]);
        String time = bean.getTime()[position];
        String newTime = time.split(":")[0] + ":" + time.split(":")[1];
        holder.time.setText(newTime);
        if (false) {
            holder.no.setText(strings[0].length() == 1 ? 0 + "" + strings[0] : strings[0]);
            holder.noText.setText(strings[1]);
        } else {
            String numberPrint = strings[0].length() == 1 ? "<b>" + 0 + "" + strings[0] + "</b><br/>" + strings[1] : "<b>" + strings[0] + "</b><br/>" + strings[1];
            holder.no.setText(Html.fromHtml(numberPrint));
        }

        if (strArrayList.get(position).equalsIgnoreCase("true")) {
            holder.hour.setVisibility(View.INVISIBLE);
        } else {
            holder.hour.setText(bean.getHour()[position]);
            holder.hour.setVisibility(View.VISIBLE);
        }
        if (GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("lagos")) {
            holder.winner.setVisibility(View.GONE);
        } else {
            holder.winner.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class Holder {
        private LinearLayout mainLayout;
        private CustomTextView winner, no, noText, hour, time;

        public Holder(View convertView) {
            mainLayout = (LinearLayout) convertView.findViewById(R.id.draw_res_main_lay);
            winner = (CustomTextView) convertView.findViewById(R.id.winner);

            hour = (CustomTextView) convertView.findViewById(R.id.hour);
            time = (CustomTextView) convertView.findViewById(R.id.time);

            no = (CustomTextView) convertView.findViewById(R.id.fast_res_num_text);
            noText = (CustomTextView) convertView.findViewById(R.id.fast_res_text);
        }
    }
}
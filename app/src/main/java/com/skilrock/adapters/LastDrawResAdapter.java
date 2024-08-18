package com.skilrock.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.bean.BonusLottoResultBean;
import com.skilrock.bean.FastLottoResultBean;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

public class LastDrawResAdapter extends ArrayAdapter<Object> {
    private Context context;
    private int resource;
    private Object resultBeans;
    private int game;

    public LastDrawResAdapter(Context context, int resource,
                              Object resultBeans, int game) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.game = game;
        this.resultBeans = resultBeans;
    }

    @Override
    public int getCount() {
        if (game == 0) {
            return 0/*
                     * ((FiveByNineResultBean) resultBeans).getFastSubDatas()
					 * .get(0).getWinner().length
					 */;
        } else if (game == 1) {
            return ((BonusLottoResultBean) resultBeans).getWinner().length;
        } else {
            return ((FastLottoResultBean) resultBeans).getWinner().length;
        }
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
        if (game == 0) {
            // FiveByNineResultBean.FastSubData data = ((FiveByNineResultBean)
            // resultBeans)
            // .getFastSubDatas().get(position);
            // holder.match.setText(data.getBallInfo()[position]);
            // holder.winner.setText(((FiveByNineResultBean) resultBeans)
            // .getWinner()[position]);
            // holder.amount.setText(((FiveByNineResultBean) resultBeans)
            // .getAmount()[position]);
        } else if (game == 1) {
            holder.match.setText(((BonusLottoResultBean) resultBeans)
                    .getMatch()[position]);
            holder.winner.setText(((BonusLottoResultBean) resultBeans)
                    .getWinner()[position]);
            holder.amount.setText(((BonusLottoResultBean) resultBeans)
                    .getAmount()[position]);
        } else {
//			holder.match
//					.setText(((FastLottoResultBean) resultBeans).getBallInfo()[position]);
//			holder.winner.setText(((FastLottoResultBean) resultBeans)
//					.getWinner()[position]);
//			holder.amount.setText(((FastLottoResultBean) resultBeans)
//					.getAmount()[position]);
        }
        return convertView;
    }

    class Holder {
        private LinearLayout mainLayout;
        private CustomTextView match, winner, amount;
    }
}

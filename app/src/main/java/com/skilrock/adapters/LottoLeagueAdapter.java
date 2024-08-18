package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.bean.LottoLeagueBean;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 3/3/2016.
 */
public class LottoLeagueAdapter extends ArrayAdapter<Object> {

    private final Context context;
    private final int resource;
    private final Object leagueBean;
    private static LayoutInflater inflater = null;

    public LottoLeagueAdapter(Context context, int resource, Object leagueBean) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.leagueBean = leagueBean;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ((LottoLeagueBean) leagueBean).getResponseData().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        LottoLeagueBean.ResponseData responseData = ((LottoLeagueBean) leagueBean).getResponseData()[position];
        if (convertView == null) {
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position % 2 == 0) {
            holder.rowLayout.setBackgroundColor(context.getResources().getColor(R.color.lotto_league_row_a));
        } else {
            holder.rowLayout.setBackgroundColor(context.getResources().getColor(R.color.lotto_league_row_b));
        }
        holder.nameLeague.setText(responseData.getPseudoName());
        holder.rankLeague.setText(responseData.getRank());
        holder.pointsLeague.setText(responseData.getPoints());
        return convertView;
    }

    class Holder {
        private LinearLayout rowLayout;
        private CustomTextView rankLeague, nameLeague, pointsLeague;

        Holder(View view) {
            rowLayout = (LinearLayout) view.findViewById(R.id.rowLayout);
            rankLeague = (CustomTextView) view.findViewById(R.id.rankLeague);
            nameLeague = (CustomTextView) view.findViewById(R.id.nameLeague);
            pointsLeague = (CustomTextView) view.findViewById(R.id.pointsLeague);
        }
    }
}

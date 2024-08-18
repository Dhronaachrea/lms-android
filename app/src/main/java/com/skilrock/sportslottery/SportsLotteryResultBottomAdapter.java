package com.skilrock.sportslottery;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.skilrock.config.VariableStorage;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.sportslottery.SportsLotteryCheckResultBean.WinningData;
import com.skilrock.utils.AmountFormat;

import java.util.List;

/**
 * Created by stpl on 7/17/2015.
 */
public class SportsLotteryResultBottomAdapter extends BaseAdapter {

    private List<WinningData> winningDatas;
    private LayoutInflater inflater;
    private Context context;
    private String currencyCode;
    private Typeface regular, light;

    public SportsLotteryResultBottomAdapter(Context context, List<WinningData> winningDatas) {
        this.winningDatas = winningDatas;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        currencyCode = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE);
        regular = Typeface.createFromAsset(context.getAssets(), "ROBOTO-REGULAR.TTF");
        light = Typeface.createFromAsset(context.getAssets(), "ROBOTO-LIGHT.TTF");
    }

    @Override
    public int getCount() {
        return winningDatas.size() + 1;
    }

    @Override
    public WinningData getItem(int position) {
        return winningDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.sports_result_row_item_bottom, parent, false);
            holder = new ViewHolder();
            holder.background = (LinearLayout) view.findViewById(R.id.ll_background);
            holder.txtMatch = (RobotoTextView) view.findViewById(R.id.txt_match);
            holder.txtWinning = (RobotoTextView) view.findViewById(R.id.txt_winning);
            holder.txtAmount = (RobotoTextView) view.findViewById(R.id.txt_amount);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (position % 2 == 0)
            holder.background.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.spl_result_bg_white));
        else
            holder.background.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.spl_result_bg_gray));

        if (position == 0) {
            holder.txtMatch.setText("Match");
            holder.txtWinning.setText("Winner");
            holder.txtAmount.setText("Amount");
            holder.txtMatch.setTypeface(regular);
            holder.txtWinning.setTypeface(regular);
            holder.txtAmount.setTypeface(regular);
        } else {
            holder.txtMatch.setTypeface(light);
            holder.txtWinning.setTypeface(light);
            holder.txtAmount.setTypeface(light);

            holder.txtMatch.setText(winningDatas.get(position - 1).getNoOfMatches() + "");
            holder.txtWinning.setText(winningDatas.get(position - 1).getNoOfWinners() + "");
            holder.txtAmount.setText(currencyCode + AmountFormat.getCurrentAmountFormatForMobile(winningDatas.get(position - 1).getPrizeAmount()));
        }
        return view;
    }

    private class ViewHolder {
        LinearLayout background;
        RobotoTextView txtMatch;
        RobotoTextView txtWinning;
        RobotoTextView txtAmount;
    }
}

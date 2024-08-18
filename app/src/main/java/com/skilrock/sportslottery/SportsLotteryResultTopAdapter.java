package com.skilrock.sportslottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.sportslottery.SportsLotteryCheckResultBean.EventData;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by stpl on 7/17/2015.
 */
public class SportsLotteryResultTopAdapter extends BaseAdapter {

    private List<EventData> eventDatas;
    private LayoutInflater inflater;
    private Context context;
    private int layoutRes;
    private String[] months;

    public SportsLotteryResultTopAdapter(Context context, int layoutRes, List<EventData> eventDatas, String[] eventType) {
        this.eventDatas = eventDatas;
        this.layoutRes = layoutRes;
        this.context = context;
        this.months = new DateFormatSymbols().getMonths();
        this.inflater = LayoutInflater.from(context);

        for (int i = 0; i < eventDatas.size(); i++) {
            String[] selected = eventDatas.get(i).getWinningOption().split(",");
            for (int j = 0; j < selected.length; j++) {
                if (eventType.length == 5) {
                    if (selected[j].equalsIgnoreCase(eventType[0].trim())) {
                        eventDatas.get(i).setIsMinusTwo(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[1].trim())) {
                        eventDatas.get(i).setIsMinusOne(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[2].trim())) {
                        eventDatas.get(i).setIsDraw(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[3].trim())) {
                        eventDatas.get(i).setIsPlusOne(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[4].trim())) {
                        eventDatas.get(i).setIsPlusTwo(true);
                    }
                    if (selected[j].equalsIgnoreCase("C")) {
                        eventDatas.get(i).setIsCancled(true);
                    }
                } else if (eventType.length == 3) {
                    if (selected[j].equalsIgnoreCase(eventType[0].trim())) {
                        eventDatas.get(i).setIsHome(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[1].trim())) {
                        eventDatas.get(i).setIsDraw(true);
                    }
                    if (selected[j].equalsIgnoreCase(eventType[2].trim())) {
                        eventDatas.get(i).setIsAway(true);
                    }
                    if (selected[j].equalsIgnoreCase("C")) {
                        eventDatas.get(i).setIsCancled(true);
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return eventDatas.size();
    }

    @Override
    public EventData getItem(int position) {
        return eventDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(layoutRes, parent, false);
            holder = new ViewHolder();
            holder.rlParent = (RelativeLayout) view.findViewById(R.id.rl_parent);
            holder.txtMatchCancelled = (RobotoTextView) view.findViewById(R.id.txt_match_cancled);
            holder.txtTime = (RobotoTextView) view.findViewById(R.id.txt_time);
            holder.txtVenue = (RobotoTextView) view.findViewById(R.id.txt_venue);
            holder.txtHomeShort = (RobotoTextView) view.findViewById(R.id.txt_team_home_short_name);
            holder.txtHome = (RobotoTextView) view.findViewById(R.id.txt_team_home_full_name);
            holder.txtAwayShort = (RobotoTextView) view.findViewById(R.id.txt_team_away_short_name);
            holder.txtAway = (RobotoTextView) view.findViewById(R.id.txt_team_away_full_name);

            if (layoutRes == R.layout.sports_result_row_item_three)
                bindIdsThree(holder, view);
            else
                bindIdsFive(holder, view);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        EventData eventData = eventDatas.get(position);

        Date d = null;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            d = dateFormat.parse(eventData.getStartTime());
            dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US);
            String dateTime = dateFormat.format(d);
            String[] startTime = dateTime.split(" ");
            String[] date = startTime[0].split("-");
            String[] time = startTime[1].split(":");
            holder.txtTime.setText(time[0] + ":" + time[1] + ", " + date[0] + " " + date[1].toUpperCase(Locale.US));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        String[] startTime = eventData.getStartTime().split(" ");
//        String[] date = startTime[0].split("-");
//        String[] time = startTime[1].split(":");
//        holder.txtTime.setText(time[0] + ":" + time[1] + ", " + date[0] + " " + getMonth(Integer.parseInt(date[1])));
        holder.txtVenue.setText(eventData.getEventLeague() + ", " + eventData.getEventVenue());
//        holder.txtHomeShort.setText(eventData.getEventCodeHome());
        holder.txtHome.setText(eventData.getEventDisplayHome()/* + " (" + eventData.getEventCodeHome() +")"*/);
//        holder.txtAwayShort.setText(eventData.getEventCodeAway());
        holder.txtAway.setText(eventData.getEventDisplayAway()/* + " (" + eventData.getEventCodeAway() +")" */);
        if (eventData.isCancled()) {
            holder.rlParent.setBackgroundColor(context.getResources().getColor(R.color.spl_result_bg_match_cancelled));
            holder.txtMatchCancelled.setVisibility(View.VISIBLE);
        } else {
            holder.rlParent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            holder.txtMatchCancelled.setVisibility(View.GONE);
        }

        if (layoutRes == R.layout.sports_result_row_item_three)
            createViewThree(holder, eventData);
        else
            createViewFive(holder, eventData);

        return view;
    }

    private void bindIdsFive(ViewHolder holder, View view) {
        holder.cbMinusTwo = (CheckBox) view.findViewById(R.id.cb_minus_two);
        holder.cbMinusOne = (CheckBox) view.findViewById(R.id.cb_minus_one);
        holder.cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
        holder.cbPlusOne = (CheckBox) view.findViewById(R.id.cb_plus_one);
        holder.cbPlusTwo = (CheckBox) view.findViewById(R.id.cb_plus_two);
        holder.cbMinusTwo.setClickable(false);
        holder.cbMinusOne.setClickable(false);
        holder.cbDraw.setClickable(false);
        holder.cbPlusOne.setClickable(false);
        holder.cbPlusTwo.setClickable(false);
    }

    private void bindIdsThree(ViewHolder holder, View view) {
        holder.cbHome = (CheckBox) view.findViewById(R.id.cb_home);
        holder.cbDraw = (CheckBox) view.findViewById(R.id.cb_draw);
        holder.cbAway = (CheckBox) view.findViewById(R.id.cb_away);
        holder.cbHome.setClickable(false);
        holder.cbDraw.setClickable(false);
        holder.cbAway.setClickable(false);
    }

    private void createViewThree(ViewHolder holder, EventData eventData) {
        setSelected(eventData.isHome(), holder.cbHome);
        setSelected(eventData.isDraw(), holder.cbDraw);
        setSelected(eventData.isAway(), holder.cbAway);
    }

    private void createViewFive(ViewHolder holder, EventData eventData) {
        setSelected(eventData.isMinusTwo(), holder.cbMinusTwo);
        setSelected(eventData.isMinusOne(), holder.cbMinusOne);
        setSelected(eventData.isDraw(), holder.cbDraw);
        setSelected(eventData.isPlusOne(), holder.cbPlusOne);
        setSelected(eventData.isPlusTwo(), holder.cbPlusTwo);
    }

    private void setSelected(boolean isChecked, CheckBox view) {
        //view.setTextColor(getResources().getColor(R.color.spl_text_game_selected));
        if (isChecked) {
            if (layoutRes == R.layout.sports_result_row_item_five) {
                view.setBackgroundResource(R.drawable.spl_result_check_box_selected_three);
                view.setTextColor(context.getResources().getColor(R.color.spl_result_text_game_selected));
            } else {
                view.setBackgroundResource(R.drawable.spl_result_check_box_selected_three);
                view.setTextColor(context.getResources().getColor(R.color.spl_result_text_game_selected));
            }
        } else {
            if (layoutRes == R.layout.sports_result_row_item_five) {
                view.setBackgroundResource(R.drawable.spl_result_check_box_normal_three);
                view.setTextColor(context.getResources().getColor(R.color.spl_result_text_game_normal));
            } else {
                view.setBackgroundResource(R.drawable.spl_result_check_box_normal_three);
                view.setTextColor(context.getResources().getColor(R.color.spl_result_text_game_normal));
            }
        }
    }

    private class ViewHolder {
        RelativeLayout rlParent;
        RobotoTextView txtMatchCancelled;
        RobotoTextView txtTime, txtVenue;
        RobotoTextView txtHomeShort, txtAwayShort;
        RobotoTextView txtHome, txtAway;
        CheckBox cbMinusTwo, cbMinusOne, cbDraw, cbPlusOne, cbPlusTwo;
        CheckBox cbHome, cbAway;
    }
}

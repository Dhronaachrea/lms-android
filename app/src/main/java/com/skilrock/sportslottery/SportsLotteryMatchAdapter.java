package com.skilrock.sportslottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.sportslottery.SportsLotteryMatchBean.EventData;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by stpl on 7/16/2015.
 */
public class SportsLotteryMatchAdapter extends BaseAdapter {

    private List<EventData> eventDatas;
    private LayoutInflater inflater;

    public SportsLotteryMatchAdapter(Context context, List<EventData> eventDatas) {
        this.eventDatas = eventDatas;
        this.inflater = LayoutInflater.from(context);
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
            view = inflater.inflate(R.layout.sports_match_row_item, parent, false);
            holder = new ViewHolder();
            holder.txtTime = (RobotoTextView) view.findViewById(R.id.txt_time);
            holder.txtVenue = (RobotoTextView) view.findViewById(R.id.txt_venue);
            holder.txtHomeOdds = (RobotoTextView) view.findViewById(R.id.txt_home_odds);
            holder.txtHomeTeam = (RobotoTextView) view.findViewById(R.id.txt_team_home_full_name);
            holder.txtDrawOdds = (RobotoTextView) view.findViewById(R.id.txt_draw_odds);
            holder.txtAwayOdds = (RobotoTextView) view.findViewById(R.id.txt_away_odds);
            holder.txtAwayTeam = (RobotoTextView) view.findViewById(R.id.txt_team_away_full_name);
            holder.imgFavHome = (ImageView) view.findViewById(R.id.img_fav_home);
            holder.imgFavAway = (ImageView) view.findViewById(R.id.img_fav_away);
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

        holder.txtVenue.setText(eventData.getEventLeague() + ", " + eventData.getEventVenue());
        holder.txtHomeOdds.setText(eventData.getHomeTeamOdds());
        holder.txtHomeTeam.setText(eventData.getEventDisplayHome());
        holder.txtDrawOdds.setText(eventData.getDrawOdds());
        holder.txtAwayOdds.setText(eventData.getAwayTeamOdds());
        holder.txtAwayTeam.setText(eventData.getEventDisplayAway());
        if (eventData.getFavTeam().equalsIgnoreCase("HOME")) {
            holder.imgFavHome.setVisibility(View.VISIBLE);
            holder.imgFavAway.setVisibility(View.GONE);
        } else if (eventData.getFavTeam().equalsIgnoreCase("AWAY")) {
            holder.imgFavHome.setVisibility(View.GONE);
            holder.imgFavAway.setVisibility(View.VISIBLE);
        } else {
            holder.imgFavHome.setVisibility(View.GONE);
            holder.imgFavAway.setVisibility(View.GONE);
        }
        return view;
    }

    private class ViewHolder {
        private RobotoTextView txtTime;
        private RobotoTextView txtVenue;
        private RobotoTextView txtHomeOdds;
        private RobotoTextView txtHomeTeam;
        private RobotoTextView txtDrawOdds;
        private ImageView imgFavHome, imgFavAway;
        private RobotoTextView txtAwayOdds;
        private RobotoTextView txtAwayTeam;
    }
}

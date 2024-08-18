package com.skilrock.sportslottery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.bean.SportsBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stpl on 6/17/2015.
 */
public class SportsLotteryAdapter extends ArrayAdapter<SportsBean.EventData> implements View.OnClickListener {

    private final double maxLine;
    private SportsLotteryPurchaseFragment fragment;
    private boolean isSingleSelection = true;
    private Holder[] holders;
    private final Context context;
    private final int resource;
    private int[] noOfLineArr;
    private boolean[] checkDefaultLine;
    private Holder holder;
    public static int totalLines;
    private ArrayList<SportsBean.EventData> data;

    public SportsLotteryAdapter(Context context, int resources,
                                ArrayList<SportsBean.EventData> data, SportsLotteryPurchaseFragment fragment, double maxLine, boolean isSingleSelection) {
        super(context, resources, data);
        this.context = context;
        this.data = data;
        this.fragment = fragment;
        this.maxLine = maxLine;
        this.resource = resources;
        this.isSingleSelection = isSingleSelection;
        this.noOfLineArr = new int[data.size()];
        this.checkDefaultLine = new boolean[data.size()];
        this.holders = new Holder[data.size()];
        Arrays.fill(this.noOfLineArr, 1);
        performCalculation(null, 0, null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, parent, false);

            holder.txtHome = (TextView) convertView.findViewById(R.id.txt_team_home_full_name);
            holder.txtHomeOdds = (TextView) convertView.findViewById(R.id.txt_home_odds);
            holder.txtDrawOdds = (TextView) convertView.findViewById(R.id.txt_draw_odds);
            holder.txtAwayOdds = (TextView) convertView.findViewById(R.id.txt_away_odds);
            holder.imgFavHome = (ImageView) convertView.findViewById(R.id.img_fav_home);
            holder.imgFavAway = (ImageView) convertView.findViewById(R.id.img_fav_away);
            holder.txtAway = (TextView) convertView.findViewById(R.id.txt_team_away_full_name);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
            holder.txtVenue = (TextView) convertView.findViewById(R.id.txt_venue);
            if (resource == R.layout.sports_row_item_five)
                bindIdsFive(holder, convertView);
            else
                bindIdsThree(holder, convertView);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        SportsBean.EventData modal = data.get(position);

        if (resource == R.layout.sports_row_item_five)
            createViewFive(holder, modal);
        else
            createViewThree(holder, modal);

        holders[position] = holder;
        if (modal.getFavTeam().equalsIgnoreCase("HOME")) {
            holder.imgFavHome.setVisibility(View.VISIBLE);
            holder.imgFavAway.setVisibility(View.GONE);
        } else if (modal.getFavTeam().equalsIgnoreCase("AWAY")) {
            holder.imgFavHome.setVisibility(View.GONE);
            holder.imgFavAway.setVisibility(View.VISIBLE);
        } else {
            holder.imgFavHome.setVisibility(View.GONE);
            holder.imgFavAway.setVisibility(View.GONE);
        }
        holder.txtHome.setText(modal.getEventDisplayHome().trim());
        holder.txtHomeOdds.setText(modal.getHomeTeamOdds());
        holder.txtDrawOdds.setText(modal.getDrawOdds());
        holder.txtAway.setText(modal.getEventDisplayAway().trim());
        holder.txtAwayOdds.setText(modal.getAwayTeamOdds());
        holder.txtVenue.setText(modal.getEventLeague() + ", " + modal.getEventVenue());

        Date d = null;
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
            d = dateFormat.parse(modal.getEventDate());
            dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US);
            String dateTime = dateFormat.format(d);
            String[] startTime = dateTime.split(" ");
            String[] date = startTime[0].split("-");
            String[] time = startTime[1].split(":");
            holder.txtTime.setText(time[0] + ":" + time[1] + ", " + date[0] + " " + date[1].toUpperCase(Locale.US));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private void bindIdsFive(Holder holder, View convertView) {

        holder.cbMinusTwo = (CheckBox) convertView.findViewById(R.id.cb_minus_two);
        holder.cbMinusOne = (CheckBox) convertView.findViewById(R.id.cb_minus_one);
        holder.cbDraw = (CheckBox) convertView.findViewById(R.id.cb_draw);
        holder.cbPlusOne = (CheckBox) convertView.findViewById(R.id.cb_plus_one);
        holder.cbPlusTwo = (CheckBox) convertView.findViewById(R.id.cb_plus_two);
    }

    private void bindIdsThree(Holder holder, View convertView) {

        holder.cbHome = (CheckBox) convertView.findViewById(R.id.cb_home);
        holder.cbDraw = (CheckBox) convertView.findViewById(R.id.cb_draw);
        holder.cbAway = (CheckBox) convertView.findViewById(R.id.cb_away);
    }

    private void createViewThree(Holder holder, SportsBean.EventData modal) {
        holder.cbHome.setOnClickListener(this);
        holder.cbDraw.setOnClickListener(this);
        holder.cbAway.setOnClickListener(this);

        doColoring(modal.isHomeSelected(), holder.cbHome);
        doColoring(modal.isDrawSelected(), holder.cbDraw);
        doColoring(modal.isAwaySelected(), holder.cbAway);

        holder.cbHome.setChecked(modal.isHomeSelected());
        holder.cbDraw.setChecked(modal.isDrawSelected());
        holder.cbAway.setChecked(modal.isAwaySelected());

        holder.cbHome.setTag(modal);
        holder.cbDraw.setTag(modal);
        holder.cbAway.setTag(modal);
    }

    private void createViewFive(Holder holder, SportsBean.EventData modal) {
        holder.cbMinusTwo.setOnClickListener(this);
        holder.cbMinusOne.setOnClickListener(this);
        holder.cbDraw.setOnClickListener(this);
        holder.cbPlusOne.setOnClickListener(this);
        holder.cbPlusTwo.setOnClickListener(this);

        doColoring(modal.isMinusTwoSelected(), holder.cbMinusTwo);
        doColoring(modal.isMinusOneSelected(), holder.cbMinusOne);
        doColoring(modal.isDrawSelected(), holder.cbDraw);
        doColoring(modal.isPlusOneSelected(), holder.cbPlusOne);
        doColoring(modal.isPlusTwoSelected(), holder.cbPlusTwo);

        holder.cbMinusTwo.setChecked(modal.isMinusTwoSelected());
        holder.cbMinusOne.setChecked(modal.isMinusOneSelected());
        holder.cbDraw.setChecked(modal.isDrawSelected());
        holder.cbPlusOne.setChecked(modal.isPlusOneSelected());
        holder.cbPlusTwo.setChecked(modal.isPlusTwoSelected());

        holder.cbMinusTwo.setTag(modal);
        holder.cbMinusOne.setTag(modal);
        holder.cbDraw.setTag(modal);
        holder.cbPlusOne.setTag(modal);
        holder.cbPlusTwo.setTag(modal);
    }

    @Override
    public void onClick(View v) {
        CheckBox checkBox = (CheckBox) v;
        SportsBean.EventData modal = (SportsBean.EventData) (checkBox).getTag();
        if (resource == R.layout.sports_row_item_five)
            onClickFive(checkBox, modal);
        else
            onClickThree(checkBox, modal);
        doColoring(checkBox.isChecked(), checkBox);
    }

    private void onClickThree(CheckBox checkBox, SportsBean.EventData modal) {
        if (isSingleSelection) {
            if (checkBox.isChecked() && noOfLineArr[modal.getDrawId()] != 0) {
                Holder holder = holders[modal.getDrawId()];
                holder.cbHome.setChecked(false);
                doColoring(holder.cbHome.isChecked(), holder.cbHome);
                performCalculation(holder.cbHome, modal.getHomeId(), modal);
                holder.cbDraw.setChecked(false);
                doColoring(holder.cbDraw.isChecked(), holder.cbDraw);
                performCalculation(holder.cbDraw, modal.getDrawId(), modal);
                holder.cbAway.setChecked(false);
                doColoring(holder.cbAway.isChecked(), holder.cbAway);
                performCalculation(holder.cbAway, modal.getAwayId(), modal);
                checkBox.setChecked(true);
                doColoring(checkBox.isChecked(), checkBox);

                modal.setHomeSelected(false);
                modal.setDrawSelected(false);
                modal.setAwaySelected(false);

                switch (checkBox.getId()) {
                    case R.id.cb_home:
                        modal.setHomeSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getMinusTwoId(), modal);
                        break;
                    case R.id.cb_draw:
                        modal.setDrawSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getDrawId(), modal);
                        break;
                    case R.id.cb_away:
                        modal.setAwaySelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getPlusTwoId(), modal);
                        break;
                }
                return;
            }
        }
        switch (checkBox.getId()) {
            case R.id.cb_home:
                modal.setHomeSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getMinusTwoId(), modal);
                break;
            case R.id.cb_draw:
                modal.setDrawSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getDrawId(), modal);
                break;
            case R.id.cb_away:
                modal.setAwaySelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getPlusTwoId(), modal);
                break;
        }
    }

    private void onClickFive(CheckBox checkBox, SportsBean.EventData modal) {
        if (isSingleSelection) {
            if (checkBox.isChecked() && noOfLineArr[modal.getDrawId()] != 0) {
                Holder holder = holders[modal.getDrawId()];
                holder.cbMinusTwo.setChecked(false);
                doColoring(holder.cbMinusTwo.isChecked(), holder.cbMinusTwo);
                performCalculation(holder.cbMinusTwo, modal.getMinusTwoId(), modal);
                holder.cbMinusOne.setChecked(false);
                doColoring(holder.cbMinusOne.isChecked(), holder.cbMinusOne);
                performCalculation(holder.cbMinusOne, modal.getMinusOneId(), modal);
                holder.cbDraw.setChecked(false);
                doColoring(holder.cbDraw.isChecked(), holder.cbDraw);
                performCalculation(holder.cbDraw, modal.getDrawId(), modal);
                holder.cbPlusOne.setChecked(false);
                doColoring(holder.cbPlusOne.isChecked(), holder.cbPlusOne);
                performCalculation(holder.cbPlusOne, modal.getPlusOneId(), modal);
                holder.cbPlusTwo.setChecked(false);
                doColoring(holder.cbPlusTwo.isChecked(), holder.cbPlusTwo);
                performCalculation(holder.cbPlusTwo, modal.getPlusTwoId(), modal);
                checkBox.setChecked(true);
                doColoring(checkBox.isChecked(), checkBox);

                modal.setMinusTwoSelected(false);
                modal.setMinusOneSelected(false);
                modal.setDrawSelected(false);
                modal.setPlusOneSelected(false);
                modal.setPlusTwoSelected(false);
                switch (checkBox.getId()) {
                    case R.id.cb_minus_two:
                        modal.setMinusTwoSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getMinusTwoId(), modal);
                        break;
                    case R.id.cb_minus_one:
                        modal.setMinusOneSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getMinusOneId(), modal);
                        break;
                    case R.id.cb_draw:
                        modal.setDrawSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getDrawId(), modal);
                        break;
                    case R.id.cb_plus_one:
                        modal.setPlusOneSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getPlusOneId(), modal);
                        break;
                    case R.id.cb_plus_two:
                        modal.setPlusTwoSelected(checkBox.isChecked());
                        performCalculation(checkBox, modal.getPlusTwoId(), modal);
                        break;
                }
                return;
            }
        }
        switch (checkBox.getId()) {
            case R.id.cb_minus_two:
                modal.setMinusTwoSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getMinusTwoId(), modal);
                break;
            case R.id.cb_minus_one:
                modal.setMinusOneSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getMinusOneId(), modal);
                break;
            case R.id.cb_draw:
                modal.setDrawSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getDrawId(), modal);
                break;
            case R.id.cb_plus_one:
                modal.setPlusOneSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getPlusOneId(), modal);
                break;
            case R.id.cb_plus_two:
                modal.setPlusTwoSelected(checkBox.isChecked());
                performCalculation(checkBox, modal.getPlusTwoId(), modal);
                break;
        }
    }

    private class Holder {
        //        public LinearLayout parent;
//        TextView homeTeam, awayTeam, league, venue, eventTime;
//        CheckBox home, draw, away;
        TextView txtTime, txtVenue;
        TextView txtHomeOdds, txtDrawOdds, txtAwayOdds;
        ImageView imgFavHome, imgFavAway;
        TextView txtHome, txtAway;
        CheckBox cbMinusTwo, cbMinusOne, cbDraw, cbPlusOne, cbPlusTwo;
        CheckBox cbHome, cbAway;

    }

    private void doColoring(boolean isSelected, CheckBox view) {
        int resNormal, resSelected;
        if (resource == R.layout.sports_row_item_five) {
            resNormal = R.drawable.spl_check_box_normal_five;
            resSelected = R.drawable.spl_check_box_selected_five;
        } else {
            resNormal = R.drawable.spl_check_box_normal_three;
            resSelected = R.drawable.spl_check_box_selected_three;
        }

        if (isSelected) {
            view.setBackgroundResource(resSelected);
            view.setTextColor(context.getResources().getColor(R.color.spl_text_game_selected));
        } else {
            view.setBackgroundResource(resNormal);
            view.setTextColor(context.getResources().getColor(R.color.spl_text_game_normal));
        }
    }

    private void performCalculation(CheckBox checkBox, int pos, SportsBean.EventData modal) {
        int noOfLine = 1;
        double finalAmt;
        if (checkBox != null && checkBox.isChecked()) {
            if (checkDefaultLine[pos]) {
                noOfLineArr[pos]++;
            } else
                checkDefaultLine[pos] = true;
        } else {
            noOfLineArr[pos]--;
            if (noOfLineArr[pos] == 0) {
                noOfLineArr[pos]++;
                checkDefaultLine[pos] = false;
            }
        }
        boolean isProceedEnable = false;
        for (int i = 0; i < noOfLineArr.length; i++) {
            noOfLine *= noOfLineArr[i];
        }
        for (int i = 0; i < checkDefaultLine.length; i++) {
            isProceedEnable = checkDefaultLine[i];
            if (!isProceedEnable)
                break;
        }
        if (noOfLine <= maxLine) {
            if (noOfLine > 0) {
                if (isProceedEnable) {
                    fragment.isPurchaseEnable(true);
                } else {
                    fragment.isPurchaseEnable(false);
                }
                // buyViewSports.setBackgroundColor(Color.TRANSPARENT);
            } else {
                fragment.isPurchaseEnable(false);
            }
            fragment.setLineNo("" + noOfLine);
            finalAmt = noOfLine
                    * Double.parseDouble(SportsLotteryActivity.txtBetAmount.getText()
                    .toString());
            totalLines = noOfLine;
            fragment.setTicketAmount(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE) + " " + AmountFormat.getCurrentAmountFormatForMobile(finalAmt));
        } else {
            new DownloadDialogBox(context, "You have reached max limit", "Limit Exceed !!", false, true, null, null).show();
            doColoring(false, checkBox);
            checkBox.setChecked(false);

            switch (checkBox.getId()) {
                case R.id.cb_minus_two:
                    modal.setMinusTwoSelected(checkBox.isChecked());
                    break;
                case R.id.cb_minus_one:
                    modal.setMinusOneSelected(checkBox.isChecked());
                    break;
                case R.id.cb_home:
                    modal.setHomeSelected(checkBox.isChecked());
                    break;
                case R.id.cb_draw:
                    modal.setDrawSelected(checkBox.isChecked());
                    break;
                case R.id.cb_away:
                    modal.setAwaySelected(checkBox.isChecked());
                    break;
                case R.id.cb_plus_one:
                    modal.setPlusOneSelected(checkBox.isChecked());
                    break;
                case R.id.cb_plus_two:
                    modal.setPlusTwoSelected(checkBox.isChecked());
                    break;
            }
            performCalculation(checkBox, pos, modal);
        }
    }
}
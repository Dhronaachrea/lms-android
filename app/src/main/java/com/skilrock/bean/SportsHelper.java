package com.skilrock.bean;//package com.skilrock.bean;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.skilrock.lottery.R;
//import com.skilrock.utils.DataSource;
//
//import java.util.ArrayList;
//
//public class SportsHelper extends ArrayAdapter<SportsBean.EventData> {
//    private Context context;
//    private int resource;
//    public static ArrayList<SportsBean.EventData> data;
//    public static int totalLines;
//    private Holder holder;
//    private String dTextColor = "#765700", sTextColor = "#FFFFFF";
//    private int noOfLineArr[];
//
//    private TextView noOfLines;
//    private TextView finalAmount;
//    private Button purchase;
//    private View buyViewSports;
//    private Button clear;
//    private View clearViewSports;
//    private boolean checkClear = false;
//
//    public SportsHelper(Context context, int resources,
//                        ArrayList<SportsBean.EventData> data, TextView noOfLines,
//                        TextView finalAmount, Button purchase, View buyViewSports,
//                        Button clear, View clearViewSports) {
//        super(context, resources, data);
//        this.context = context;
//        SportsHelper.data = data;
//        this.noOfLines = noOfLines;
//        this.finalAmount = finalAmount;
//        this.resource = resources;
//        noOfLineArr = new int[SportsHelper.data.size()];
//        this.purchase = purchase;
//        this.buyViewSports = buyViewSports;
//        this.clear = clear;
//        this.clearViewSports = clearViewSports;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        holder = null;
//        if (convertView == null) {
//            holder = new Holder();
//            convertView = ((LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                    .inflate(resource, null);
//            holder.homeTeam = (TextView) convertView
//                    .findViewById(R.id.home_team);
//            holder.awayTeam = (TextView) convertView
//                    .findViewById(R.id.away_team);
//            holder.home = (CheckBox) convertView.findViewById(R.id.home);
//            holder.draw = (CheckBox) convertView.findViewById(R.id.draw);
//            holder.away = (CheckBox) convertView.findViewById(R.id.away);
//            // holder.home.setId(position);
//            // holder.draw.setId(position);
//            // holder.away.setId(position);
//            holder.league = (TextView) convertView
//                    .findViewById(R.id.league_name);
//            holder.venue = (TextView) convertView.findViewById(R.id.venue);
//            holder.eventTime = (TextView) convertView
//                    .findViewById(R.id.event_time);
//            holder.parent = (LinearLayout) convertView
//                    .findViewById(R.id.parent);
//            convertView.setTag(holder);
//        } else {
//            holder = (Holder) convertView.getTag();
//        }
//        if (position % 2 == 0) {
//            holder.parent.setBackgroundColor(Color.parseColor("#0E435C"));
//        } else {
//            holder.parent.setBackgroundColor(Color.parseColor("#0E435C"));
//        }
//        holder.home.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view;
//                SportsBean.EventData modal = (SportsBean.EventData) (checkBox).getTag();
//                modal.setHomeSelected(checkBox.isChecked());
//                doColoring(checkBox.isChecked(), checkBox);
//                performCalculation(checkBox.isChecked(), modal.getHomeId());
//            }
//        });
//        holder.draw.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view;
//                SportsBean.EventData modal = (SportsBean.EventData) (checkBox).getTag();
//                modal.setDrawSelected(checkBox.isChecked());
//                doColoring(checkBox.isChecked(), checkBox);
//                performCalculation(checkBox.isChecked(), modal.getDrawId());
//            }
//        });
//        holder.away.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                CheckBox checkBox = (CheckBox) view;
//                SportsBean.EventData modal = (SportsBean.EventData) (checkBox).getTag();
//                modal.setAwaySelected(checkBox.isChecked());
//                doColoring(checkBox.isChecked(), checkBox);
//                performCalculation(checkBox.isChecked(), modal.getAwayId());
//            }
//        });
//        SportsBean.EventData modal = data.get(position);
//        holder.homeTeam.setText(modal.getEventDisplayHome());
//        holder.awayTeam.setText(modal.getEventDisplayAway());
//        holder.league.setText(modal.getEventLeague());
//        holder.venue.setText(modal.getEventVenue());
//        holder.eventTime.setText(modal.getEventDate());
//        holder.home.setChecked(modal.isHomeSelected());
//        holder.draw.setChecked(modal.isDrawSelected());
//        holder.away.setChecked(modal.isAwaySelected());
//        holder.home.setTag(modal);
//        holder.draw.setTag(modal);
//        holder.away.setTag(modal);
//        doColoring(modal.isHomeSelected(), holder.home);
//        doColoring(modal.isDrawSelected(), holder.draw);
//        doColoring(modal.isAwaySelected(), holder.away);
//        return convertView;
//    }
//
//    class Holder {
//        public LinearLayout parent;
//        TextView homeTeam, awayTeam, league, venue, eventTime;
//        CheckBox home, draw, away;
//    }
//
//    private void doColoring(boolean isSelected, CheckBox view) {
//        if (isSelected) {
//            view.setBackgroundResource(R.drawable.sports_selected);
//            view.setTextColor(Color.parseColor(sTextColor));
//        } else {
//            view.setBackgroundResource(R.drawable.sports_normal);
//            view.setTextColor(Color.parseColor(dTextColor));
//        }
//    }
//
//    private void performCalculation(boolean isSelected, int pos) {
//        int noOfLine = 1;
//        double finalAmt;
//        if (isSelected) {
//            noOfLineArr[pos]++;
//        } else {
//            noOfLineArr[pos]--;
//        }
//
//        for (int i = 0; i < noOfLineArr.length; i++) {
//            noOfLine *= noOfLineArr[i];
//
//        }
//        if (noOfLine > 0) {
//            purchase.setEnabled(true);
//            buyViewSports.setBackgroundColor(Color.TRANSPARENT);
//        } else {
//            purchase.setEnabled(false);
//            buyViewSports.setBackgroundColor(Color.parseColor("#88000000"));
//        }
//        noOfLines.setText("" + noOfLine);
//        finalAmt = noOfLine
//                * Double.parseDouble(SportsActivity.countText.getText()
//                .toString());
//        totalLines = noOfLine;
//        finalAmount.setText(SportsActivity.decimalFormat.format(finalAmt) + " "
//                + DataSource.Login.currenctSymbol);
//
//        for (int i = 0; i < data.size(); i++) {
//
//            if (data.get(i).isAwaySelected() || data.get(i).isHomeSelected()
//                    || data.get(i).isDrawSelected()) {
//                checkClear = true;
//                break;
//
//            } else {
//                checkClear = false;
//            }
//
//        }
//        if (checkClear) {
//            clear.setEnabled(true);
//            clearViewSports.setBackgroundColor(Color.TRANSPARENT);
//        } else {
//            clear.setEnabled(false);
//            clearViewSports.setBackgroundColor(Color.parseColor("#88000000"));
//        }
//
//    }
//}

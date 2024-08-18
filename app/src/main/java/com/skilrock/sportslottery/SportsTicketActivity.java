package com.skilrock.sportslottery;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skilrock.bean.SportsTicketBean;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

public class SportsTicketActivity extends Activity implements OnClickListener {
    // private JSONObject data;
    private TextView gameName;
    private TextView ticket_date;
    private TextView ticket_time;
    private TextView ticket_no_top;
    private TextView ticket_no_bottom;
    private TextView no_of_lines;
    private TextView unitPrice;

    private TextView draw_name;
    private TextView draw_date;
    private TextView draw_time;
    private TextView totAmt;

    private TextView noOfDraws;

    private Button back;
    private boolean trackTicket;
    private ScrollView scrollView;
    private ProgressDialog progressDialog;
    // private TicketBean bean;
    private LinearLayout gamesChild;
    private View convertView;

    TextView home, away, homeSelected, awaySelected, drawSelected, venueLeague,
            dateTime;
    LinearLayout parent;
    // private String[][] selectedDraw;

    private ArrayList<HashMap<String, String>> eventDataList;

    private SportsTicketBean sportsTicketBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // selectEDDRAW = NEW STRING[][] { { "H" }, { "H", "D", "A" },
        // { "H", "A" }, { "H", "D" }, { "A" } };
        setContentView(R.layout.sports_activity_ticket);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        sportsTicketBean = (SportsTicketBean) getIntent().getExtras()
                .getSerializable("sportsTicketBean");
        // Same class will be used for Sale and Track Ticket
        trackTicket = getIntent().getExtras().getBoolean("trackTicket");
        scrollView = (ScrollView) findViewById(R.id.list_view);
        progressDialog = ProgressDialog.show(SportsTicketActivity.this, "",
                "Please Wait...", false, false);
        progressDialog.show();

        gameName = (TextView) findViewById(R.id.game_name);
        ticket_date = (TextView) findViewById(R.id.current_date);
        ticket_time = (TextView) findViewById(R.id.current_time);
        ticket_no_top = (TextView) findViewById(R.id.ticket_no_top);
        ticket_no_bottom = (TextView) findViewById(R.id.ticket_no_bottom);
        draw_name = (TextView) findViewById(R.id.draw_name);
        draw_date = (TextView) findViewById(R.id.draw_date);
        draw_time = (TextView) findViewById(R.id.draw_time);
        totAmt = (TextView) findViewById(R.id.total_amount);
        no_of_lines = (TextView) findViewById(R.id.no_of_lines_ticket);
        unitPrice = (TextView) findViewById(R.id.unitPriceTicket);

        noOfDraws = (TextView) findViewById(R.id.no_of_draws);
        gamesChild = (LinearLayout) findViewById(R.id.games_child);
        scrollView.setVisibility(View.INVISIBLE);
        eventDataList = new ArrayList<HashMap<String, String>>();
        if (sportsTicketBean != null) {
            try {
                if (!trackTicket){}
//                    DataSource.Login.currentBalance = Integer.parseInt(sportsTicketBean
//                            .getTktData().getBalance());
            } catch (Exception e) {

            }
        }
        try {
            if (eventDataList.size() > 0) {
                eventDataList.clear();
            }
        } catch (Exception e) {
        }
        gamesChild.removeAllViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();

        prepareTicket();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            SportsTicketActivity.this.finish();
        }
    }

    View showTicket(int pos, String[] selectedDraw,
                    HashMap<String, String> matchData) {
        convertView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.sports_ticket_row, null);
        home = (TextView) convertView.findViewById(R.id.home_team);
        away = (TextView) convertView.findViewById(R.id.away_team);
        homeSelected = (TextView) convertView.findViewById(R.id.home_selected);
        awaySelected = (TextView) convertView.findViewById(R.id.away_selected);
        drawSelected = (TextView) convertView.findViewById(R.id.draw_selected);
        venueLeague = (TextView) convertView.findViewById(R.id.venue_league);
        dateTime = (TextView) convertView.findViewById(R.id.date_time);
        parent = (LinearLayout) convertView.findViewById(R.id.parent);
        home.setText(matchData.get("eventDisplayHome"));
        away.setText(matchData.get("eventDisplayAway"));
        homeSelected.setText("Home");
        awaySelected.setText("Away");
        drawSelected.setText("Draw");
        venueLeague.setText(matchData.get("eventLeague") + ", "
                + matchData.get("eventVenue"));
        dateTime.setText(matchData.get("eventDate"));

        if (pos % 2 == 0) {
            parent.setBackgroundColor(Color.parseColor("#0EB241"));
        } else {
            parent.setBackgroundColor(Color.parseColor("#13CC4C"));
        }
        setNormal(homeSelected);
        setNormal(drawSelected);
        setNormal(awaySelected);
        // System.out.println("------" + selectedDraw.length);

        // for (int i = 0; i < selectedDraw.length; i++) {
        // System.out.println("==================" + i + "=========="
        // + selectedDraw[i]);
        // }

        for (int i = 0; i < selectedDraw.length; i++) {
            if (selectedDraw[i] == "H" || selectedDraw[i].contains("H")
                    || selectedDraw[i].equalsIgnoreCase("H")) {
                setSelected(homeSelected);
            }
            if (selectedDraw[i] == "D" || selectedDraw[i].contains("D")
                    || selectedDraw[i].equalsIgnoreCase("D")) {
                setSelected(drawSelected);
            }
            if (selectedDraw[i] == "A" || selectedDraw[i].contains("A")
                    || selectedDraw[i].equalsIgnoreCase("A")) {
                setSelected(awaySelected);
            }
        }

        return convertView;
    }

    private void setNormal(View view) {
        ((TextView) view).setTextColor(Color.parseColor("#079533"));
        ((TextView) view).setBackgroundDrawable(getResources().getDrawable(
                android.R.color.transparent));
    }

    private void setSelected(View view) {
        ((TextView) view).setTextColor(Color.WHITE);
        ((TextView) view).setBackgroundDrawable(getResources().getDrawable(
                R.drawable.buttons_selected));
    }

    private void prepareTicket() {

        if (sportsTicketBean != null) {

            if (true) {

                try {

                    ArrayList<SportsTicketBean.EventData> eventDatas = sportsTicketBean
                            .getTktData().getBoardData().get(0).getEventData();

                    for (int i = 0; i < eventDatas.size(); i++) {

                        SportsTicketBean.EventData tempData = eventDatas.get(i);
                        HashMap<String, String> data = new HashMap<String, String>();

                        data.put("eventDisplayHome",
                                tempData.getEventDisplayHome());
                        data.put("eventDisplayAway",
                                tempData.getEventDisplayAway());
                        data.put("eventLeague", tempData.getEventLeague());
                        data.put("eventVenue", tempData.getEventVenue());
                        data.put("eventDate", tempData.getEventDate());
                        data.put("selection", tempData.getSelection());

                        eventDataList.add(data);
                    }
                    for (int i = 0; i < eventDataList.size(); i++) {

                        gamesChild.addView(
                                showTicket(i,
                                        eventDataList.get(i).get("selection")
                                                .split(","),
                                        eventDataList.get(i)), i);
                    }
                    gameName.setText(sportsTicketBean.getTktData()
                            .getGameTypeName());
                    ticket_date.setText(sportsTicketBean.getTktData()
                            .getPurchaseDate());
                    ticket_time.setText(sportsTicketBean.getTktData()
                            .getPurchaseTime());
                    ticket_no_top.setText(sportsTicketBean.getTktData()
                            .getTicketNo() + "");
                    ticket_no_bottom.setText(sportsTicketBean.getTktData()
                            .getTicketNo() + "");

                    totAmt.setText(sportsTicketBean.getTktData().getTicketAmt());
                    noOfDraws.setText("1");

                    if (sportsTicketBean.getTktData().getBoardData() != null) {

                        ArrayList<SportsTicketBean.BoardData> boardDatas = sportsTicketBean
                                .getTktData().getBoardData();

                        if (boardDatas.size() != 0) {
                            SportsTicketBean.BoardData boardData = boardDatas.get(0);
                            draw_name.setText(boardData.getDrawName());
                            draw_date.setText(boardData.getDrawDate());
                            draw_time.setText(boardData.getDrawTime());
                            no_of_lines.setText(boardData.getNoOfLines() + "");
                            unitPrice.setText(boardData.getUnitPrice() + "");
                        }
                    }
                    progressDialog.dismiss();
                    scrollView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    progressDialog.dismiss();

                    e.printStackTrace();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            SportsTicketActivity.this);
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("Internal Error!, Try again later");
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SportsTicketActivity.this.finish();
                                }
                            });
                    alertDialog.show();
                }

            } else {
                progressDialog.dismiss();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        SportsTicketActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Internal Error!, Try again later");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                SportsTicketActivity.this.finish();
                            }
                        });
                alertDialog.show();
            }

        } else {
            progressDialog.dismiss();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    SportsTicketActivity.this);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.net_error));
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SportsTicketActivity.this.finish();
                        }
                    });
            alertDialog.show();
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }
}

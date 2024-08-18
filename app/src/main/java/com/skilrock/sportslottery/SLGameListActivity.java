package com.skilrock.sportslottery;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.skilrock.adapters.MenuHelper;
import com.skilrock.bean.SportsBean;
import com.skilrock.bean.SportsModal;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.Utils;

public class SLGameListActivity extends Activity implements Runnable,
        OnItemClickListener {
    private ProgressDialog progressDialog;
    private ListView menuList;
    private MenuHelper adapter;


    private ArrayList<SportsModal> list = new ArrayList<SportsModal>();
    ArrayList<String> menuListData = new ArrayList<String>();
    // private JSONObject drawData;
    private SportsBean sportsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);


        menuList = (ListView) findViewById(R.id.main_menu);

        progressDialog = ProgressDialog.show(SLGameListActivity.this, "",
                "Please Wait...", false, false);
        new Thread(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!DataSource.Login.isSessionValid)
            SLGameListActivity.this.finish();
    }

    public void run() {
        // drawData = Communication.drawGameData(DataSource.Login.username);
        // drawData = DataSource.sportsListJson;
        Gson gson = new Gson();
        sportsBean = gson.fromJson(DataSource.sportsListJson, SportsBean.class);
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            if (sportsBean != null) {
                try {
                    if (sportsBean.responseMsg.equalsIgnoreCase("success"))
                        if (sportsBean.responseCode == 0) {
                            ArrayList<SportsBean.GameData> gameData = sportsBean
                                    .getSleData().getGameData();
                            if (gameData.size() != 0) {

                                for (int i = 0; i < gameData.size(); i++) {

                                    menuListData
                                            .add(gameData.get(i).gameDisplayName);
                                }

                                adapter = new MenuHelper(SLGameListActivity.this,
                                        menuListData
                                                .toArray(new String[menuListData
                                                        .size()]));
                                menuList.setAdapter(adapter);

                                if (menuListData.size() > 1) {

                                    menuList.setOnItemClickListener(SLGameListActivity.this);
                                } else {
                                    Intent mIntent = new Intent(
                                            SLGameListActivity.this,
                                            SLGameTypeActivity.class);
                                    mIntent.putExtra("gameName",
                                            menuListData.get(0));
                                    mIntent.putExtra("gameData", sportsBean
                                            .getSleData().getGameData()
                                            .get(0));
                                    startActivity(mIntent);
                                    SLGameListActivity.this.finish();
                                }
                                // DataSource.SportsLottery.gameId = gameData.get(0)
                                // .getGameId();
                                // GameTypeData gameTypeData = gameData.get(0)
                                // .getGameTypeData().get(0);
                                // DataSource.SportsLottery.gameTypeDisplayName =
                                // gameTypeData
                                // .getGameTypeDisplayName();
                                // DataSource.SportsLottery.gameTypeId =
                                // gameTypeData
                                // .getGameTypeId();
                                // DrawData drawData = gameTypeData.getDrawData();
                                // DataSource.SportsLottery.drawDisplayString =
                                // drawData
                                // .getDrawDisplayString();
                                // DataSource.SportsLottery.drawId = drawData
                                // .getDrawId();
                                // DataSource.SportsLottery.drawDateTime = drawData
                                // .getDrawDateTime();
                                // ArrayList<EventData> eventData = drawData
                                // .getCurrentData().get(0).getEventData();
                                // ((TextView) findViewById(R.id.game_header))
                                // .setText(DataSource.SportsLottery.gameTypeDisplayName);
                                // ((TextView) findViewById(R.id.game_string))
                                // .setText(DataSource.SportsLottery.drawDisplayString);
                                // ((TextView) findViewById(R.id.date_time))
                                // .setText(DataSource.SportsLottery.drawDateTime);
                                // for (int i = 0; i < eventData.size(); i++) {
                                // SportsModal modal = new SportsModal();
                                // modal.setEventId(eventData.get(i).getEventId());
                                // modal.setEventName(eventData.get(i)
                                // .getEventDisplayString());
                                // modal.setHomeSelected(false);
                                // modal.setDrawSelected(false);
                                // modal.setAwaySelected(false);
                                // list.add(modal);
                                // }
                                // adapter = new
                                // SportsHelper(SLGameListActivity.this,
                                // R.layout.sports_row, list);
                                // menuList.setAdapter(adapter);
                            } else {
                                Utils.Toast(getApplicationContext(),
                                        "No games!");
                            }
                        } else {
                            if (sportsBean.getResponseCode() == 118) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        SLGameListActivity.this);
                                alertDialog
                                        .setIcon(android.R.drawable.ic_dialog_alert);
                                alertDialog.setCancelable(false);
                                alertDialog.setMessage("Session Out Login Again");
                                alertDialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // startActivity(new
                                                // Intent(SLGameListActivity.this,
                                                // LoginActivity.class));
                                                SLGameListActivity.this.finish();
                                                DataSource.Login.isSessionValid = false;
                                            }
                                        });
                                alertDialog.show();
                            } else
                                Utils.Toast(SLGameListActivity.this,
                                        sportsBean.getResponseMsg());
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        SLGameListActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setCancelable(false);
                alertDialog.setMessage(R.string.net_error);
                alertDialog.setPositiveButton("OK", null);
                alertDialog.show();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent mIntent = new Intent(SLGameListActivity.this,
                SLGameTypeActivity.class);
        mIntent.putExtra("gameName", menuListData.get(arg2));
        // mIntent.putExtra("position", arg2);
        mIntent.putExtra("gameData", sportsBean.getSleData()
                .getGameData().get(arg2));
        startActivity(mIntent);

    }
}

package com.skilrock.sportslottery;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.skilrock.adapters.MenuHelper;
import com.skilrock.bean.SportsBean;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.DataSource;

public class SLGameTypeActivity extends Activity {

    private ListView menuList;
    private MenuHelper adapter;

    private List<SportsBean.GameTypeData> gameTypeDatas;
    private SportsBean.GameData gameData;
    private ArrayList<String> gameTypeListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuList = (ListView) findViewById(R.id.main_menu);
        gameTypeListData = new ArrayList<String>();
        try {

            gameData = (SportsBean.GameData) getIntent().getExtras().getSerializable(
                    "gameData");
            // gameData = datalist.get(getIntent().getIntExtra("position", 0));

            gameTypeDatas = gameData.getGameTypeData();

            for (int i = 0; i < gameTypeDatas.size(); i++) {

                gameTypeListData.add(gameTypeDatas.get(i).gameTypeDisplayName);

            }

            adapter = new MenuHelper(
                    SLGameTypeActivity.this,
                    gameTypeListData.toArray(new String[gameTypeListData.size()]));

            menuList.setAdapter(adapter);
            menuList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    // Intent intent = new Intent(getApplicationContext(),
                    // SLGameTypeOptionActivity.class);
                    // intent.putExtra("matchData", gameTypeDatas.get(arg2));
                    // intent.putExtra("gameId", gameData.getGameId());
                    // intent.putExtra("gameTypeName",gameTypeListData.get(arg2));
                    // startActivity(intent);

                    Intent intent = new Intent(getApplicationContext(),
                            SportsLotteryActivity.class);
                    intent.putExtra("gameId", gameData.getGameId());
                    intent.putExtra("gameHeaderName",
                            gameTypeListData.get(arg2));
                    intent.putExtra("matchData", gameTypeDatas.get(arg2));
                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    SLGameTypeActivity.this);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Invalid Error ! Try again later");
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SLGameTypeActivity.this.finish();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!DataSource.Login.isSessionValid)
            SLGameTypeActivity.this.finish();

    }
}

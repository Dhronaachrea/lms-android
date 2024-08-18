package com.skilrock.sportslottery;

import android.app.Activity;
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

public class SLGameTypeOptionActivity extends Activity {
    private ListView menuList;
    private MenuHelper adapter;

    private SportsBean.GameTypeData gameTypeData;
    private int gameId = 0;
    private String gameHeaderName;
    private String[] list = new String[]{"Play Game", "Mtach List"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        gameTypeData = (SportsBean.GameTypeData) getIntent().getSerializableExtra(
                "matchData");
        gameId = getIntent().getIntExtra("gameId", 0);

        gameHeaderName = getIntent().getStringExtra("gameTypeName");

        menuList = (ListView) findViewById(R.id.main_menu);
        adapter = new MenuHelper(SLGameTypeOptionActivity.this, list);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == 0) {

                    Intent intent = new Intent(getApplicationContext(),
                            SportsLotteryActivity.class);

                    intent.putExtra("gameId", gameId);
                    intent.putExtra("gameHeaderName", getIntent()
                            .getStringExtra("gameTypeName"));
                    intent.putExtra("matchData", gameTypeData);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!DataSource.Login.isSessionValid)
            SLGameTypeOptionActivity.this.finish();

    }
}

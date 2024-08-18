package com.skilrock.myaccount;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.skilrock.adapters.LottoLeagueAdapter;
import com.skilrock.bean.LottoLeagueBean;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

import org.json.JSONObject;

/**
 * Created by stpl on 3/3/2016.
 */
public class LottoLeague extends DrawerBaseActivity implements WebServicesListener {
    private Activity context;
    private String fakeJson;
    private LinearLayout mainLeague;
    private ListView lottoLeagueList;
    private CustomTextView rank, name, points;
    private String lottoLeaguePath;
    private LottoLeagueBean lottoBean;
    private RobotoTextView legueInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_league);
        context = LottoLeague.this;
        PMSCall();
        sHeader();
        setDrawerItems();
        manageTheme();
        bindsId();
    }

    private void manageTheme() {
        headerNavigation.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerImage.setImageResource(R.drawable.back);
        headerText.setVisibility(View.VISIBLE);
        headerImage.setOnTouchListener(null);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerBaseActivity.selectedItemId = -1;
                finish();
            }
        });
        headerText.setText(getResources().getString(R.string.lotto_league_header));
    }

    private void PMSCall() {
        if (true) {
            lottoLeaguePath = "/rest/pmsDataMgmt/getLeaguePlayerData";
            new PMSWebTask(LottoLeague.this, lottoLeaguePath, "", null, "LottoLeague", null, "Loading").execute();
        } else {
            //dummy
            String resultData = "{\"responseCode\":\"0\",\"responseMsg\":\"SUCCESS\",\"responseData\":[{\"playerId\":1001501,\"rank\":1,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":2,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":3,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":4,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":5,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":2,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":6,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001501,\"rank\":7,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":2,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":8,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":9,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":10,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":11,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":12,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":2,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":13,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":14,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":15,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":16,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":17,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":18,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":19,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":20,\"points\":1,\"pseudoName\":\"nikal\"},{\"playerId\":1001501,\"rank\":21,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":22,\"points\":1,\"pseudoName\":\"nikal\"}]}";
            try {
                JSONObject jsonData = new JSONObject(resultData.toString());
                lottoBean = new Gson().fromJson(jsonData.toString(), LottoLeagueBean.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void bindsId() {
        mainLeague = (LinearLayout) findViewById(R.id.main_league);
        lottoLeagueList = (ListView) findViewById(R.id.lotto_league_list);
        rank = (CustomTextView) findViewById(R.id.rank);
        name = (CustomTextView) findViewById(R.id.name);
        points = (CustomTextView) findViewById(R.id.points);
        legueInfo = (RobotoTextView) findViewById(R.id.lotto_legue_txt_info);

        rank.setText(context.getResources().getString(R.string.lotto_rank));
        name.setText(context.getResources().getString(R.string.lotto_name));
        points.setText(context.getResources().getString(R.string.lotto_points));

    }

    @Override
    protected void onResume() {
        super.onResume();
        rank.setText(context.getResources().getString(R.string.lotto_rank));
        name.setText(context.getResources().getString(R.string.lotto_name));
        points.setText(context.getResources().getString(R.string.lotto_points));
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        dialog.dismiss();
        JSONObject jsonData;
//        String fakeJson = "{\"responseCode\":\"0\",\"responseMsg\":\"SUCCESS\",\"responseData\":[{\"playerId\":1001501,\"rank\":1,\"points\":2,\"pseudoName\":\"niks\"},{\"playerId\":1001502,\"rank\":2,\"points\":1,\"pseudoName\":\"nikal\"}]}";
        if (resultData != null) {
            if (methodType.equalsIgnoreCase("LottoLeague")) {
                try {
                    jsonData = new JSONObject(resultData.toString());
                    if (jsonData.optString("responseCode").equalsIgnoreCase("0")) {
                        lottoBean = new Gson().fromJson(jsonData.toString(), LottoLeagueBean.class);
                        setViewForleague();
                    } else {
                        GlobalVariables.showServerErr(context);
                    }
                } catch (Exception ex) {
                    GlobalVariables.showServerErr(context);
                    ex.printStackTrace();
                }
            }
        } else {
            GlobalVariables.showServerErr(context);
        }
    }

    private void setViewForleague() {
        try {
            if (lottoBean.getResponseData() == null || lottoBean.getResponseData().length == 0) {
                legueInfo.setVisibility(View.VISIBLE);
                mainLeague.setVisibility(View.GONE);
            } else {
                legueInfo.setVisibility(View.GONE);
                mainLeague.setVisibility(View.VISIBLE);
                try {
                    LottoLeagueAdapter leagueAdapter = new LottoLeagueAdapter(context, R.layout.activity_league_row, lottoBean);
                    lottoLeagueList.setAdapter(leagueAdapter);
                } catch (Exception ex) {
                    GlobalVariables.showServerErr(context);
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            legueInfo.setVisibility(View.VISIBLE);
            mainLeague.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }
}

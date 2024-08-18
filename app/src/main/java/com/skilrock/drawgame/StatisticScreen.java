package com.skilrock.drawgame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.skilrock.adapters.DrawGameFragAdapter;
import com.skilrock.bean.CommonStatsBean;
import com.skilrock.bean.CommonStatsBean.SubData;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.viewpager.SliderLayout;
import com.viewpager.indicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class StatisticScreen extends DrawerBaseActivity {
    private Analytics analytics;
    private ArrayList<CommonStatsBean> bonusStatsBeans;
    private ArrayList<CommonStatsBean> fiveStatsBeans, fiveStatsBeansLagos;
    private ArrayList<CommonStatsBean> lottoStatsBeans, oneToTwelveStatsBeans, bonusKenoStatsBeans;
    private ArrayList<CommonStatsBean> twelveStatsBeans, tenStatsBeans, tenByThirtyStatsBean, tenByTwentyStatsBean, tenByEightyStatsBean;
    private SliderLayout myPager;
    private String[] resultTitles;
    // Header Components
    private String gameId;
    private ArrayList<Fragment> fragments;
    private JSONArray jsonArray;
    private JSONObject statsJson;
    private JSONArray statsArray;
    private JSONObject mainJSONObj;
    private String[] titlesArr;
    private String[] spinnerTitle;
    private GlobalPref globalPref;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        analytics = new Analytics();
        globalPref = GlobalPref.getInstance(StatisticScreen.this);
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.DRAW_GAME_STATS);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        sHeader();

        setDrawerItems();
        gameId = getIntent().getStringExtra("gameId");
        try {
            mainJSONObj = new JSONObject(getIntent().getStringExtra("jsonData"));
            jsonArray = mainJSONObj.getJSONArray("dgeStats");
            JSONArray array = new JSONArray();

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject gameData = jsonArray.getJSONObject(j);
                if (!gameData.getString("gameCode").equals(Config.bonusFree) && !gameData.getString("gameCode").equalsIgnoreCase("ZimLottoBonusTwo")) {
                    switch (gameData.optString("gameCode")) {
                        case Config.bonusKeno:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.fiveGameNameMachine:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.fiveGameName:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.fiveGameNameLagos:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.bonusGameName:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.bonusGameNameTwo:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.fastGameName:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.twelveGameName:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.tenByThirty:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.tenByTwenty:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.oneToTwelve:
//                            if (globalPref.getCountry().equalsIgnoreCase("LAGOS"))
                            array.put(jsonArray.get(j));
                            break;
                        case Config.tenByNinety:
                            array.put(jsonArray.get(j));
                            break;
                        case Config.tenByEighty:
                            array.put(jsonArray.get(j));
                            break;
                        default:
                            break;
                    }
                }
            }
            spinnerTitle = new String[array.length()];
            titlesArr = new String[array.length()];

            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String gameCode = jsonObject.getString("gameCode");
                titlesArr[i] = gameCode;
                spinnerTitle[i] = GlobalVariables.GamesData.gameCodeMap.get(gameCode) == null ? "" : GlobalVariables.GamesData.gameCodeMap.get(gameCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bindViewIds();
        headerSubText.setText("STATISTICS");
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerSpinner.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.VISIBLE);
        if (MainScreen.gameFragments.size() > 1)
            headerText.setText(VariableStorage.GlobalPref.getStringData(getApplication(), VariableStorage.GlobalPref.DGE_SER_NAME));
        else
            headerText.setText(VariableStorage.GlobalPref.getStringData(getApplication(), VariableStorage.GlobalPref.DGE_SER_NAME));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_row,
                R.id.spinner_text, spinnerTitle);
        headerSpinner.setAdapter(adapter);
        headerSpinner.setSelection(Arrays.asList(titlesArr).indexOf(gameId));
        headerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                if (drawerLayout.isDrawerOpen(drawerView))
                    drawerLayout.closeDrawer(drawerView);
                setGameResult(titlesArr[pos], Fields.Action.DROPDOWN);
                ((CustomTextView) ((LinearLayout) ((LinearLayout) parent.getChildAt(0))
                        .getChildAt(0)).getChildAt(0)).setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        if (spinnerTitle.length <= 1) {
            headerSpinner.setVisibility(View.GONE);
        } else
            headerSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        }
    };

    private void bindViewIds() {
        myPager = (SliderLayout) findViewById(R.id.jazzy_pager);
        setGameResult(gameId, Fields.Action.OPEN);
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setTypeface(Config.globalTextFont);
        indicator.setViewPager(myPager);
        indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
        myPager.setPresetTransformer(SliderLayout.Transformer.Tablet);
        myPager.setOffscreenPageLimit(3);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        //  headerSpinner.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    private void setGameResult(String gameId, String action) {
        switch (gameId) {
            case Config.fiveGameName:

                getFiveJunkData(Config.fiveGameName);
                fragments = new ArrayList<>();
                resultTitles = new String[fiveStatsBeans.size()];
                for (int i = 0; i < fiveStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), fiveStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = fiveStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.fiveGameNameMachine:
                analytics.sendAll(Fields.Category.FIVE_GAME, action, Fields.Label.FIVE_GAME_STATS);

                getFiveJunkData(Config.fiveGameNameMachine);
                fragments = new ArrayList<>();
                resultTitles = new String[fiveStatsBeans.size()];
                for (int i = 0; i < fiveStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), fiveStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = fiveStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.tenByEighty:
                getTenByEightyJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[tenByEightyStatsBean.size()];
                for (int i = 0; i < tenByEightyStatsBean.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), tenByEightyStatsBean.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = tenByEightyStatsBean.get(i).getStatsType();
                }
                break;
            case Config.fiveGameNameLagos:
                analytics.sendAll(Fields.Category.FIVE_GAME, action, Fields.Label.FIVE_GAME_STATS);

                getFiveJunkDataLagos();
                fragments = new ArrayList<>();
                resultTitles = new String[fiveStatsBeansLagos.size()];
                for (int i = 0; i < fiveStatsBeansLagos.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), fiveStatsBeansLagos.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = fiveStatsBeansLagos.get(i).getStatsType();
                }
                break;
            case Config.bonusGameName:
                analytics.sendAll(Fields.Category.BONUS_GAME, action, Fields.Label.BONUS_GAME_STATS);

                getBonusJunkData(Config.bonusGameName);
                fragments = new ArrayList<>();
                resultTitles = new String[bonusStatsBeans.size()];
                for (int i = 0; i < bonusStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), bonusStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = bonusStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.bonusGameNameTwo:
                analytics.sendAll(Fields.Category.BONUS_GAME, action, Fields.Label.BONUS_GAME_STATS);

                getBonusJunkData(Config.bonusGameNameTwo);
                fragments = new ArrayList<>();
                resultTitles = new String[bonusStatsBeans.size()];
                for (int i = 0; i < bonusStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), bonusStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = bonusStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.fastGameName:
                analytics.sendAll(Fields.Category.FAST_GAME, action, Fields.Label.FAST_GAME_STATS);

                getFastJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[lottoStatsBeans.size()];
                for (int i = 0; i < lottoStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), lottoStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = lottoStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.oneToTwelve:
//                if (!globalPref.getCountry().equalsIgnoreCase("LAGOS"))
//                    break;
                analytics.sendAll(Fields.Category.FAST_GAME, action, Fields.Label.FAST_GAME_STATS);
                getOneToTwelveJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[oneToTwelveStatsBeans.size()];
                for (int i = 0; i < oneToTwelveStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), oneToTwelveStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = oneToTwelveStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.twelveGameName:
                analytics.sendAll(Fields.Category.TWELVE_GAME, action, Fields.Label.TWELVE_GAME_STATS);
                getTwelveJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[twelveStatsBeans.size()];
                for (int i = 0; i < twelveStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), twelveStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = twelveStatsBeans.get(i).getStatsType();
                }
                break;
            case Config.tenByThirty:
                getTenByThirtyJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[tenByThirtyStatsBean.size()];
                for (int i = 0; i < tenByThirtyStatsBean.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), tenByThirtyStatsBean.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = tenByThirtyStatsBean.get(i).getStatsType();
                }
                break;
            case Config.tenByTwenty:
                getTenByTwentyJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[tenByTwentyStatsBean.size()];
                for (int i = 0; i < tenByTwentyStatsBean.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), tenByTwentyStatsBean.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = tenByTwentyStatsBean.get(i).getStatsType();
                }
                break;
            case Config.tenByNinety:
                getTenJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[tenStatsBeans.size()];
                for (int i = 0; i < tenStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), tenStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = tenStatsBeans.get(i).getStatsType();
                }
                break;
            //new game 6/36
            case Config.bonusKeno:
                getBonusKenoJunkData();
                fragments = new ArrayList<>();
                resultTitles = new String[bonusKenoStatsBeans.size()];
                for (int i = 0; i < bonusKenoStatsBeans.size(); i++) {
                    DGStatsFragment dgStatsFragment = new DGStatsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DGStatsFragment.Keys.RESULT_BEAN.name(), bonusKenoStatsBeans.get(i));
                    bundle.putString(DGStatsFragment.Keys.GAME_ID.name(), gameId);
                    dgStatsFragment.setArguments(bundle);
                    fragments.add(dgStatsFragment);
                    resultTitles[i] = bonusKenoStatsBeans.get(i).getStatsType();
                }
                break;


            default:
                break;
        }
        myPager.setAdapter(new DrawGameFragAdapter(this,
                getSupportFragmentManager(), fragments, resultTitles));
    }

    private void getBonusKenoJunkData() {
        bonusKenoStatsBeans = new ArrayList<CommonStatsBean>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.bonusKeno)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }

            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        bonusKenoStatsBeans.add(resultBean);
        bonusKenoStatsBeans.add(resultBean1);
        bonusKenoStatsBeans.add(resultBean2);
    }

    private void getOneToTwelveJunkData() {
        oneToTwelveStatsBeans = new ArrayList<CommonStatsBean>();
        ArrayList<SubData> datas = new ArrayList<SubData>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.oneToTwelve)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }

            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                String ballS = jsonObject.getString("ball");
                int ball = Integer.parseInt(ballS.substring(ballS.indexOf("(") + 1, ballS.indexOf(")")));
                subData = bean.new SubData(ball, jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        oneToTwelveStatsBeans.add(resultBean);
        oneToTwelveStatsBeans.add(resultBean1);
        oneToTwelveStatsBeans.add(resultBean2);
    }

    private void getFastJunkData() {
        lottoStatsBeans = new ArrayList<CommonStatsBean>();
        ArrayList<SubData> datas = new ArrayList<SubData>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.fastGameName)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }

            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                String ballS = jsonObject.getString("ball");
                int ball = Integer.parseInt(ballS.substring(ballS.indexOf("(") + 1, ballS.indexOf(")")));
                subData = bean.new SubData(ball, jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        lottoStatsBeans.add(resultBean);
        lottoStatsBeans.add(resultBean1);
        lottoStatsBeans.add(resultBean2);
    }

    private void getBonusJunkData(String gameCode) {
        bonusStatsBeans = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<SubData>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(gameCode)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");
            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }
            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        bonusStatsBeans.add(resultBean);
        bonusStatsBeans.add(resultBean1);
        bonusStatsBeans.add(resultBean2);
    }

    private void getFiveJunkDataLagos() {
        fiveStatsBeansLagos = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.fiveGameNameLagos)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");
            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }
            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        fiveStatsBeansLagos.add(resultBean);
        fiveStatsBeansLagos.add(resultBean1);
        fiveStatsBeansLagos.add(resultBean2);
    }

    private void getTenByEightyJunkData() {
        tenByEightyStatsBean = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.tenByEighty)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        tenByEightyStatsBean.add(resultBean);
        tenByEightyStatsBean.add(resultBean1);
        tenByEightyStatsBean.add(resultBean2);
    }

    private void getFiveJunkData(String gameCode) {
        fiveStatsBeans = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equalsIgnoreCase(gameCode)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");
            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }
            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        fiveStatsBeans.add(resultBean);
        fiveStatsBeans.add(resultBean1);
        fiveStatsBeans.add(resultBean2);
    }


    private void getTenByTwentyJunkData() {
        tenByTwentyStatsBean = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.tenByTwenty)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        tenByTwentyStatsBean.add(resultBean);
        tenByTwentyStatsBean.add(resultBean1);
        tenByTwentyStatsBean.add(resultBean2);
    }

    private void getTenByThirtyJunkData() {
        tenByThirtyStatsBean = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.tenByThirty)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        tenByThirtyStatsBean.add(resultBean);
        tenByThirtyStatsBean.add(resultBean1);
        tenByThirtyStatsBean.add(resultBean2);
    }

    private void getTwelveJunkData() {
        twelveStatsBeans = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.twelveGameName)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }
            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        twelveStatsBeans.add(resultBean);
        twelveStatsBeans.add(resultBean1);
        twelveStatsBeans.add(resultBean2);
    }

    private void getTenJunkData() {
        tenStatsBeans = new ArrayList<>();
        ArrayList<SubData> datas = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("gameCode").equals(Config.tenByNinety)) {
                    statsJson = jsonArray.getJSONObject(i);
                }
            }

            if (statsJson == null) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                return;
            }
            statsArray = statsJson.getJSONArray("statsData");

            if (statsArray.length() == 0) {
                myPager.setVisibility(View.GONE);
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            } else {
                myPager.setVisibility(View.VISIBLE);
                findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                findViewById(R.id.txt_info).setVisibility(View.GONE);
            }

            for (int i = 0; i < statsArray.length(); i++) {
                CommonStatsBean bean = new CommonStatsBean();
                SubData subData = null;
                JSONObject jsonObject = statsArray.getJSONObject(i);
                subData = bean.new SubData(jsonObject.getInt("ball"), jsonObject.getInt("frequency"), jsonObject.getLong("lastSeen"), jsonObject.getString("lastSeenDisplay"));
                datas.add(subData);
            }
        } catch (JSONException e) {
        }
        CommonStatsBean resultBean = new CommonStatsBean("Overview", getDataOnFreq((ArrayList<SubData>) datas.clone(), "BALL"));
        CommonStatsBean resultBean1 = new CommonStatsBean("Most Frequent",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "FREQUENCY"));
        CommonStatsBean resultBean2 = new CommonStatsBean("Most Overdue",
                getDataOnFreq((ArrayList<SubData>) datas.clone(), "LAST_SEEN"));
        tenStatsBeans.add(resultBean);
        tenStatsBeans.add(resultBean1);
        tenStatsBeans.add(resultBean2);
    }


    private ArrayList<SubData> getDataOnFreq(
            ArrayList<SubData> datas, String sortType) {
        ArrayList<SubData> result = datas;

        Comparator<SubData> comparator = null;

        if ("FREQUENCY".equals(sortType)) {
            comparator = new Comparator<SubData>() {
                @Override
                public int compare(SubData o1,
                                   SubData o2) {
                    return o2.getFrequency() - o1.getFrequency();
                }
            };
        } else if ("LAST_SEEN".equals(sortType)) {
            comparator = new Comparator<SubData>() {
                @Override
                public int compare(SubData o1,
                                   SubData o2) {
                    return (int) (o2.getLastSeenSec() - (o1.getLastSeenSec()));
                }
            };

        } else if ("BALL".equals(sortType)) {
            comparator = new Comparator<SubData>() {
                @Override
                public int compare(SubData o1,
                                   SubData o2) {
                    return o1.getBall() - (o2.getBall());
                }
            };

        }
        Collections.sort(result, comparator);

        return result;

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }


    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

}

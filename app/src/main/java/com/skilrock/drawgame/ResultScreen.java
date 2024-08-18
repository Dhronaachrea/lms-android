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
import com.skilrock.bean.BonusLottoResultBean;
import com.skilrock.bean.FastLottoResultBean;
import com.skilrock.bean.FiveByNineResultBean;
import com.skilrock.bean.TwelveByTwentyFourResulBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.drawgame.OneToTwelve.OneToTwelveResFragement;
import com.skilrock.drawgame.TenByTwenty.TenByTwentyGameScreen;
import com.skilrock.drawgame.TenByTwenty.TenByTwentyResultFragment;
import com.skilrock.drawgame.bonusLotto_keno_SixThirtySix.BonusKenoResult;
import com.skilrock.drawgame.bonuslotto.BonusLottoResFragment;
import com.skilrock.drawgame.fast.FastLottoResFragment;
import com.skilrock.drawgame.five.FiveByNineResFragment;
import com.skilrock.drawgame.fiveLagos.FiveByNineResFragmentLagos;
import com.skilrock.drawgame.kenoSeven.TenByNinetyResFragment;
import com.skilrock.drawgame.tenByEighty.TenByEightyResFragment;
import com.skilrock.drawgame.tenByThirty.TenByThirtyResFragment;
import com.skilrock.drawgame.twelve.TwelveResultFragment;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.viewpager.SliderLayout;
import com.viewpager.indicator.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ResultScreen extends DrawerBaseActivity {
    private Analytics analytics;
    private ArrayList<BonusLottoResultBean> bonusResultBeans;
    private ArrayList<FiveByNineResultBean> fiveResultBeans, fiveLagosResultBeans, bonusKenoResultBean;
    private ArrayList<TwelveByTwentyFourResulBean> twelveResultBeans, tenByThirthResultBean, tenByTwentyResultBean, tenByEightyResultBean;
    private ArrayList<TwelveByTwentyFourResulBean> tenResultBeans;
    private ArrayList<FastLottoResultBean> fastLottoResultBeans;
    private ArrayList<FastLottoResultBean> oneToTwelveResultBeans;

    private SliderLayout myPager;
    private String[] resultTitles;
    private String gameId;
    private ArrayList<Fragment> fragments;
    private CustomTextView buyNow, myTckt, verifyTckt, locateRetailer;
    private String[] winningNo;
    private String[] machineNo;
    //    private int[] machineNo;
    private String[] match;
    private String[] winner;
    private String[] amount;
    private boolean isAdd;
    private JSONArray resultJSONArr;
    private JSONObject fiveJSON, fiveMachineJSON, fastJSON, bonusJSON, bonusJSONTwo,
            twelveJSON, oneTwoTwelveJSON, fiveLagosJSON, tenJSON, bonusKenoJSON, tenByTwentyJson, tenByEightyJson;
    private String drawDate;
    private String drawName;
    private String drawTime;
    private FastLottoResultBean fastLottoResultBean, oneToTwelveResulBean;
    private String[] ballInfo;
    private String[] fastDrawTime;
    private String[] fastHour;
    private String[] titlesArr;
    private String[] spinnerTitle;
    private JSONObject tenByThirtyJSON;
    private GlobalPref globalPref;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        globalPref = GlobalPref.getInstance(ResultScreen.this);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.DRAW_GAME_RESULT);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        sHeader();
        setDrawerItems();
        try {
            gameId = getIntent().getStringExtra("gameId");
            resultJSONArr = new JSONArray(getIntent().getStringExtra("jsonData"));
            JSONArray array = new JSONArray();
            for (int j = 0; j < resultJSONArr.length(); j++) {
                JSONObject gameData = resultJSONArr.getJSONObject(j);
                if (!gameData.getString("gameCode").equals(Config.bonusFree) && !gameData.getString("gameCode").equalsIgnoreCase("ZimLottoBonusTwo")) {
                    switch (gameData.optString("gameCode")) {
                        case Config.bonusKeno:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.fiveGameNameMachine:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.fiveGameName:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.fiveGameNameLagos:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.bonusGameName:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.bonusGameNameTwo:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.fastGameName:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.twelveGameName:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.tenByThirty:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.tenByTwenty:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.oneToTwelve:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.tenByNinety:
                            array.put(resultJSONArr.get(j));
                            break;
                        case Config.tenByEighty:
                            array.put(resultJSONArr.get(j));
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
            for (int i = 0; i < resultJSONArr.length(); i++) {
                JSONObject jsonObject = resultJSONArr.getJSONObject(i);
                switch (jsonObject.getString("gameCode")) {
                    case Config.fiveGameName:
                        fiveJSON = jsonObject;
                        break;
                    case Config.fiveGameNameMachine:
                        fiveMachineJSON = jsonObject;
                        break;
                    case Config.fiveGameNameLagos:
                        fiveLagosJSON = jsonObject;
                        break;
                    case Config.bonusGameName:
                        bonusJSON = jsonObject;
                        break;
                    case Config.bonusGameNameTwo:
                        bonusJSONTwo = jsonObject;
                        break;
                    case Config.fastGameName:
                        fastJSON = jsonObject;
                        break;
                    case Config.twelveGameName:
                        twelveJSON = jsonObject;
                        break;
                    case Config.tenByThirty:
                        tenByThirtyJSON = jsonObject;
                        break;
                    case Config.tenByNinety:
                        tenJSON = jsonObject;
                        break;
                    case Config.bonusKeno:
                        bonusKenoJSON = jsonObject;
                        break;
                    case Config.oneToTwelve:
//                        if (globalPref.getCountry().equalsIgnoreCase("LAGOS"))
                        oneTwoTwelveJSON = jsonObject;
                        break;
                    case Config.tenByEighty:
                        tenByEightyJson = jsonObject;
                        break;
                    //new Game tenBytwenty
                    case Config.tenByTwenty:
                        tenByTwentyJson = jsonObject;
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bindViewIds();
        setClickListenerOnViews();
        headerSubText.setText("RESULTS");
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, R.id.spinner_text, spinnerTitle);
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
        buyNow = (CustomTextView) findViewById(R.id.buy_now);
        myTckt = (CustomTextView) findViewById(R.id.my_tckt);
        verifyTckt = (CustomTextView) findViewById(R.id.verify_tckt);
        locateRetailer = (CustomTextView) findViewById(R.id.locate_ret_text);
        myPager = (SliderLayout) findViewById(R.id.jazzy_pager);
        setGameResult(gameId, Fields.Action.OPEN);
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setTypeface(Config.globalTextFont);
        if (myPager.getAdapter() != null) {
            indicator.setViewPager(myPager);
            indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);
            myPager.setPresetTransformer(SliderLayout.Transformer.Tablet);
        }
    }

    private void setClickListenerOnViews() {
        buyNow.setOnClickListener(commonClickListener);
        myTckt.setOnClickListener(commonClickListener);
        verifyTckt.setOnClickListener(commonClickListener);
        locateRetailer.setOnClickListener(commonClickListener);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        // headerSpinner.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    private void setGameResult(String gameId, String action) {
        switch (gameId) {
            case Config.fiveGameName:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.FIVE_GAME_RESULT);
                getFiveJunkData(Config.fiveGameName);
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[fiveResultBeans.size()];
                for (int i = 0; i < fiveResultBeans.size(); i++) {
                    fragments.add(new FiveByNineResFragment(fiveResultBeans.get(i)));
                    resultTitles[i] = fiveResultBeans.get(i).getDrawName();
                }
                break;

            case Config.bonusKeno:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.FIVE_GAME_RESULT);
                getBonusKenoJunkData(Config.bonusKeno);
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[bonusKenoResultBean.size()];
                for (int i = 0; i < bonusKenoResultBean.size(); i++) {
                    fragments.add(new BonusKenoResult(bonusKenoResultBean.get(i)));
                    resultTitles[i] = bonusKenoResultBean.get(i).getDrawName();
                }
                break;

            case Config.fiveGameNameMachine:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.FIVE_GAME_RESULT);
                getFiveJunkData(Config.fiveGameNameMachine);
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[fiveResultBeans.size()];
                for (int i = 0; i < fiveResultBeans.size(); i++) {
                    fragments.add(new FiveByNineResFragment(fiveResultBeans.get(i)));
                    resultTitles[i] = fiveResultBeans.get(i).getDrawName();
                }
                break;
            case Config.fiveGameNameLagos:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.FIVE_GAME_RESULT);
                getFiveJunkDataForLagos();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[fiveLagosResultBeans.size()];
                for (int i = 0; i < fiveLagosResultBeans.size(); i++) {
                    fragments.add(new FiveByNineResFragmentLagos(fiveLagosResultBeans.get(i)));
                    resultTitles[i] = fiveLagosResultBeans.get(i).getDrawName();
                }
                break;
            case Config.bonusGameName:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.BONUS_GAME_RESULT);
                getBonusJunkData(Config.bonusGameName);
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[bonusResultBeans.size()];
                for (int i = 0; i < bonusResultBeans.size(); i++) {
                    fragments.add(new BonusLottoResFragment(bonusResultBeans.get(i)));
                    resultTitles[i] = bonusResultBeans.get(i).getDrawDate();
                }
                break;
            case Config.bonusGameNameTwo:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.BONUS_GAME_RESULT);
                getBonusJunkData(Config.bonusGameNameTwo);
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[bonusResultBeans.size()];
                for (int i = 0; i < bonusResultBeans.size(); i++) {
                    fragments.add(new BonusLottoResFragment(bonusResultBeans.get(i)));
                    resultTitles[i] = bonusResultBeans.get(i).getDrawDate();
                }
                break;
            case Config.fastGameName:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.FAST_GAME_RESULT);
                getFastJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[fastLottoResultBeans.size()];
                for (int i = 0; i < fastLottoResultBeans.size(); i++) {
                    fragments.add(new FastLottoResFragment(fastLottoResultBeans.get(i)));
                    resultTitles[i] = fastLottoResultBeans.get(i).getDrawName();
                }
                break;
            case Config.tenByThirty:
                getTenByThirtyJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[tenByThirthResultBean.size()];
                for (int i = 0; i < tenByThirthResultBean.size(); i++) {
                    fragments.add(new TenByThirtyResFragment(tenByThirthResultBean.get(i)));
                    resultTitles[i] = tenByThirthResultBean.get(i).getDrawName();
                }
                break;
            //new game tenBytwenty
            case Config.tenByTwenty:
                getTenByTwentyJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[tenByTwentyResultBean.size()];
                for (int i = 0; i < tenByTwentyResultBean.size(); i++) {
                    fragments.add(new TenByTwentyResultFragment(tenByTwentyResultBean.get(i)));
                    resultTitles[i] = tenByTwentyResultBean.get(i).getDrawName();
                }
                break;
            //end of game
            case Config.twelveGameName:
                analytics.sendAll(Fields.Category.UX, action, Fields.Label.TWELVE_GAME_RESULT);
                getTwelveJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[twelveResultBeans.size()];
                for (int i = 0; i < twelveResultBeans.size(); i++) {
                    fragments.add(new TwelveResultFragment(twelveResultBeans.get(i)));
                    resultTitles[i] = twelveResultBeans.get(i).getDrawName();
                }
                break;
            case Config.tenByNinety:
                getTenJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[tenResultBeans.size()];
                for (int i = 0; i < tenResultBeans.size(); i++) {
                    fragments.add(new TenByNinetyResFragment(tenResultBeans.get(i)));
                    resultTitles[i] = tenResultBeans.get(i).getDrawName();
                }
                break;
            case Config.tenByEighty:
                getTenByEightyJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[tenByEightyResultBean.size()];
                for (int i = 0; i < tenByEightyResultBean.size(); i++) {
                    fragments.add(new TenByEightyResFragment(tenByEightyResultBean.get(i)));
                    resultTitles[i] = tenByEightyResultBean.get(i).getDrawName();
                }
                break;
            case Config.oneToTwelve:
//                if (!globalPref.getCountry().equalsIgnoreCase("LAGOS"))
//                    break;
//               gameId
//                if (!returnValue) {
//                    myPager.setVisibility(View.VISIBLE);
//                    findViewById(R.id.indicator).setVisibility(View.VISIBLE);
//                    findViewById(R.id.txt_info).setVisibility(View.GONE);
//                    return;
//                }
                if (oneTwoTwelveJSON == null) {
                    findViewById(R.id.indicator).setVisibility(View.GONE);
                    findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    return;
                } else {
                    findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                    findViewById(R.id.txt_info).setVisibility(View.GONE);
                }

                getOneTweleveJunkData();
                fragments = new ArrayList<Fragment>();
                resultTitles = new String[oneToTwelveResultBeans.size()];
                for (int i = 0; i < oneToTwelveResultBeans.size(); i++) {
                    fragments.add(new OneToTwelveResFragement(oneToTwelveResultBeans.get(i)));
                    resultTitles[i] = oneToTwelveResultBeans.get(i).getDrawName();
                }
                break;
            default:
                break;
        }
        myPager.setAdapter(new DrawGameFragAdapter(getApplicationContext(),
                getSupportFragmentManager(), fragments, resultTitles));
        myPager.setPageMargin(30);
    }

    private void getTenByTwentyJunkData() {
        tenByTwentyResultBean = new ArrayList<TwelveByTwentyFourResulBean>();
        TwelveByTwentyFourResulBean resultBean = null;
        try {
            JSONArray resultArray = tenByTwentyJson.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    resultBean = new TwelveByTwentyFourResulBean(drawName, drawDate, drawTime, winningNo,
                            new String[0], match, winner, amount);
                    tenByTwentyResultBean.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getBonusKenoJunkData(String gameCode) {
        bonusKenoResultBean = new ArrayList<FiveByNineResultBean>();
        FiveByNineResultBean resultBean = null;
        try {
            JSONArray resultArray = null;
            if (bonusKenoJSON != null)
                resultArray = bonusKenoJSON.getJSONArray("resultData");

            if (resultArray != null && resultArray.length() > 0) {
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject drawObj = resultArray.getJSONObject(i);
                    JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                    winningNo = new String[drawArray.length()];
                    for (int j = 0; j < drawArray.length(); j++) {
                        JSONObject drawSubObj = drawArray.getJSONObject(j);
                        drawDate = drawObj.getString("resultDate");
                        drawTime = drawSubObj.getString("drawTime");
                        drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                        winningNo = drawSubObj.getString("winningNo").split(",");

                        if (drawSubObj.has("machineNo")) {
                            if (drawSubObj.getString("machineNo").length() != 0) {
                                machineNo = drawSubObj.getString("machineNo").split(",");
                            } else {
                                machineNo = new String[0];
                            }
                        } else {
                            machineNo = new String[0];
                        }

                        JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                        match = new String[matchInfo.length()];
                        winner = new String[matchInfo.length()];
                        amount = new String[matchInfo.length()];
                        if (matchInfo.length() == 0) {
                            myPager.setVisibility(View.GONE);
                            findViewById(R.id.indicator).setVisibility(View.GONE);
                            findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                        } else {
                            myPager.setVisibility(View.VISIBLE);
                            findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                            findViewById(R.id.txt_info).setVisibility(View.GONE);
                        }

                        for (int k = 0; k < matchInfo.length(); k++) {
                            JSONObject jsonObject = matchInfo.getJSONObject(k);
                            match[k] = jsonObject.getString("match");
                            winner[k] = jsonObject.getString("noOfWinners");
                            amount[k] = jsonObject.getString("amount");
                        }
                        resultBean = new FiveByNineResultBean(drawName, drawDate, drawTime, winningNo,
                                machineNo, match, winner, amount);
                        bonusKenoResultBean.add(resultBean);
                    }
                }
            } else {
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getBonusJunkData(String gameCode) {
        bonusResultBeans = new ArrayList<>();
        try {
            JSONArray resultArray = null;

            if (gameCode.equalsIgnoreCase(Config.bonusGameName)) {
                if (bonusJSON != null)
                    resultArray = bonusJSON.getJSONArray("resultData");
            } else if (gameCode.equalsIgnoreCase(Config.bonusGameNameTwo)) {
                if (bonusJSONTwo != null)
                    resultArray = bonusJSONTwo.getJSONArray("resultData");
            }

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    GlobalVariables.formatDateFromCal(getApplicationContext(), drawDate, "dd-MM-yyyy");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(VariableStorage.GlobalPref.getStringData(getApplicationContext(), VariableStorage.GlobalPref.DATE_FORMAT), Locale.US);
                    try {
                        Date date = simpleDateFormat.parse(drawDate);
                        drawDate = simpleDateFormat.format(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (!drawSubObj.getString("drawName").equals("")) {
                        drawName = drawSubObj.getString("drawName");
                    } else {
                        drawName = "N/A";
                    }
                    drawTime = drawSubObj.getString("drawTime");
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    BonusLottoResultBean
                            resultBean = new BonusLottoResultBean(drawName, drawDate, drawTime, winningNo, drawSubObj.getString("bonusNo"),
                            new String[0], match, winner, amount);
                    bonusResultBeans.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFastJunkData() {
        fastLottoResultBeans = new ArrayList<>();
        try {
            JSONArray resultArray = fastJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                ballInfo = new String[drawArray.length()];
                fastDrawTime = new String[drawArray.length()];
                fastHour = new String[drawArray.length()];
                winner = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    String winningNo = drawSubObj.getString("winningNo");
                    String fastResText = winningNo.contains("(") ? winningNo.substring(winningNo.indexOf("(") + 1, winningNo.indexOf(")")) : winningNo; /*winningNo.substring(winningNo.indexOf("(") + 1, winningNo.indexOf(")"));*/
                    String fastResNum = winningNo.split("\\(")[0];
                    fastResText = fastResText.length() == 1 ? 0 + "" + fastResText : fastResText;
                    ballInfo[j] = fastResText + " " + fastResNum;
                    fastDrawTime[j] = drawSubObj.getString("drawTime");
                    fastHour[j] = fastDrawTime[j].split(":")[0];
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    JSONObject jsonObject = matchInfo.getJSONObject(0);
                    winner[j] = jsonObject.getString("noOfWinners");
                }
                drawName = drawObj.getString("resultDate");
                fastLottoResultBean = new FastLottoResultBean(drawName, ballInfo, winner, fastHour,
                        fastDrawTime);
                fastLottoResultBeans.add(fastLottoResultBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFiveJunkDataForLagos() {
        fiveLagosResultBeans = new ArrayList<FiveByNineResultBean>();
        FiveByNineResultBean resultBean = null;
        try {
            JSONArray resultArray = fiveLagosJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");

                    if (drawSubObj.has("machineNo")) {
                        if (drawSubObj.getString("machineNo").length() != 0) {
                            machineNo = drawSubObj.getString("machineNo").split(",");
                        } else {
                            machineNo = new String[0];
                        }
                    } else {
                        machineNo = new String[0];
                    }

                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }

                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    resultBean = new FiveByNineResultBean(drawName, drawDate, drawTime, winningNo,
                            machineNo, match, winner, amount);
                    fiveLagosResultBeans.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFiveJunkData(String gameCode) {
        fiveResultBeans = new ArrayList<FiveByNineResultBean>();
        FiveByNineResultBean resultBean = null;
        try {
            JSONArray resultArray = null;
            if (gameCode.equals(Config.fiveGameName)) {
                if (fiveJSON != null)
                    resultArray = fiveJSON.getJSONArray("resultData");
            } else {
                if (fiveMachineJSON != null)
                    resultArray = fiveMachineJSON.getJSONArray("resultData");
            }

            if (resultArray != null && resultArray.length() > 0) {
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject drawObj = resultArray.getJSONObject(i);
                    JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                    winningNo = new String[drawArray.length()];
                    for (int j = 0; j < drawArray.length(); j++) {
                        JSONObject drawSubObj = drawArray.getJSONObject(j);
                        drawDate = drawObj.getString("resultDate");
                        drawTime = drawSubObj.getString("drawTime");
                        drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                        winningNo = drawSubObj.getString("winningNo").split(",");

                        if (drawSubObj.has("machineNo")) {
                            if (drawSubObj.getString("machineNo").length() != 0) {
                                machineNo = drawSubObj.getString("machineNo").split(",");
                            } else {
                                machineNo = new String[0];
                            }
                        } else {
                            machineNo = new String[0];
                        }

                        JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                        match = new String[matchInfo.length()];
                        winner = new String[matchInfo.length()];
                        amount = new String[matchInfo.length()];
                        if (matchInfo.length() == 0) {
                            myPager.setVisibility(View.GONE);
                            findViewById(R.id.indicator).setVisibility(View.GONE);
                            findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                        } else {
                            myPager.setVisibility(View.VISIBLE);
                            findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                            findViewById(R.id.txt_info).setVisibility(View.GONE);
                        }

                        for (int k = 0; k < matchInfo.length(); k++) {
                            JSONObject jsonObject = matchInfo.getJSONObject(k);
                            match[k] = jsonObject.getString("match");
                            winner[k] = jsonObject.getString("noOfWinners");
                            amount[k] = jsonObject.getString("amount");
                        }
                        resultBean = new FiveByNineResultBean(drawName, drawDate, drawTime, winningNo,
                                machineNo, match, winner, amount);
                        fiveResultBeans.add(resultBean);
                    }
                }
            } else {
                findViewById(R.id.indicator).setVisibility(View.GONE);
                findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOneTweleveJunkData() {
        oneToTwelveResultBeans = new ArrayList<>();
        try {
            JSONArray resultArray = oneTwoTwelveJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                ballInfo = new String[drawArray.length()];
                fastDrawTime = new String[drawArray.length()];
                fastHour = new String[drawArray.length()];
                winner = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    String winningNo = drawSubObj.getString("winningNo");
                    String fastResText = winningNo.contains("(") ? winningNo.substring(winningNo.indexOf("(") + 1, winningNo.indexOf(")")) : winningNo;
//                    String fastResText = winningNo.substring(winningNo.indexOf("(") + 1, winningNo.indexOf(")"));
                    String fastResNum = winningNo.split("\\(")[0];
                    fastResText = fastResText.length() == 1 ? 0 + "" + fastResText : fastResText;
                    ballInfo[j] = fastResText + " " + fastResNum;
                    fastDrawTime[j] = drawSubObj.getString("drawTime");
                    fastHour[j] = fastDrawTime[j].split(":")[0];
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    JSONObject jsonObject = matchInfo.getJSONObject(0);
                    winner[j] = jsonObject.getString("noOfWinners");
                }
                drawName = drawObj.getString("resultDate");
                oneToTwelveResulBean = new FastLottoResultBean(drawName, ballInfo, winner, fastHour,
                        fastDrawTime);
                oneToTwelveResultBeans.add(oneToTwelveResulBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getTenByThirtyJunkData() {
        tenByThirthResultBean = new ArrayList<TwelveByTwentyFourResulBean>();
        TwelveByTwentyFourResulBean resultBean = null;
        try {
            JSONArray resultArray = twelveJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    resultBean = new TwelveByTwentyFourResulBean(drawName, drawDate, drawTime, winningNo,
                            new String[0], match, winner, amount);
                    tenByThirthResultBean.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTwelveJunkData() {
        twelveResultBeans = new ArrayList<TwelveByTwentyFourResulBean>();
        TwelveByTwentyFourResulBean resultBean = null;
        try {
            JSONArray resultArray = twelveJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    resultBean = new TwelveByTwentyFourResulBean(drawName, drawDate, drawTime, winningNo,
                            new String[0], match, winner, amount);
                    twelveResultBeans.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTenByEightyJunkData() {
        tenByEightyResultBean = new ArrayList<TwelveByTwentyFourResulBean>();
        TwelveByTwentyFourResulBean resultBean = null;
        try {
            JSONArray resultArray = tenByEightyJson.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }
                    resultBean = new TwelveByTwentyFourResulBean(drawName, drawDate, drawTime, winningNo,
                            new String[0], match, winner, amount);
                    tenByEightyResultBean.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTenJunkData() {
        tenResultBeans = new ArrayList<TwelveByTwentyFourResulBean>();
        TwelveByTwentyFourResulBean resultBean = null;
        try {
            JSONArray resultArray = tenJSON.getJSONArray("resultData");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject drawObj = resultArray.getJSONObject(i);
                JSONArray drawArray = drawObj.getJSONArray("resultInfo");
                winningNo = new String[drawArray.length()];
                for (int j = 0; j < drawArray.length(); j++) {
                    JSONObject drawSubObj = drawArray.getJSONObject(j);
                    drawDate = drawObj.getString("resultDate");
                    drawTime = drawSubObj.getString("drawTime");
                    drawName = getFormatedDrawName(drawSubObj.getString("drawName"), drawDate, drawTime);
                    winningNo = drawSubObj.getString("winningNo").split(",");
                    JSONArray matchInfo = drawSubObj.getJSONArray("matchInfo");
                    match = new String[matchInfo.length()];
                    winner = new String[matchInfo.length()];
                    amount = new String[matchInfo.length()];
                    if (matchInfo.length() == 0) {
                        myPager.setVisibility(View.GONE);
                        findViewById(R.id.indicator).setVisibility(View.GONE);
                        findViewById(R.id.txt_info).setVisibility(View.VISIBLE);
                    } else {
                        myPager.setVisibility(View.VISIBLE);
                        findViewById(R.id.indicator).setVisibility(View.VISIBLE);
                        findViewById(R.id.txt_info).setVisibility(View.GONE);
                    }
                    for (int k = 0; k < matchInfo.length(); k++) {
                        JSONObject jsonObject = matchInfo.getJSONObject(k);
                        match[k] = jsonObject.getString("match");
                        winner[k] = jsonObject.getString("noOfWinners");
                        amount[k] = jsonObject.getString("amount");
                    }//gfdg
                    resultBean = new TwelveByTwentyFourResulBean(drawName, drawDate, drawTime, winningNo,
                            new String[0], match, winner, amount);
                    tenResultBeans.add(resultBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Function for Five By Nine Result Header title name
    public String getFormatedDrawName(String drawName, String drawDate, String drawTime) {
        String result = "";
        try {
            if (drawName.equalsIgnoreCase("null") || drawName.equalsIgnoreCase("")) {
                String date = drawDate;
                String time = drawTime;
                date = date.split("-")[0] + " " + MONTHS[Integer.parseInt(date.split("-")[1]) - 1];
                time = time.substring(0, 5);
                result = result.concat(date + " " + time);
            } else {
                result = drawName;
            }
            return result;
        } catch (Exception e1) {
            e1.printStackTrace();
            return "null";
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    private String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

}

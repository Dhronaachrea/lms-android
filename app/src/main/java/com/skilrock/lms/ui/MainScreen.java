package com.skilrock.lms.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.adapters.MenuAdapter;
import com.skilrock.banner.BannerView;
import com.skilrock.banner.OnBannerClickListener;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.bean.ScratchGameBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.DebouncedOnItemClickListener;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.drawgame.DGActivity;
import com.skilrock.drawgame.OneToTwelve.OneToTwelve;
import com.skilrock.drawgame.TenByTwenty.TenByTwenty;
import com.skilrock.drawgame.bonusLotto_keno_SixThirtySix.BonusKeno;
import com.skilrock.drawgame.bonuslotto.BonusLotto;
import com.skilrock.drawgame.fast.FastLotto;
import com.skilrock.drawgame.five.FiveByNine;
import com.skilrock.drawgame.fiveLagos.FiveByNineLagos;
import com.skilrock.drawgame.kenoSeven.TenByNinety;
import com.skilrock.drawgame.tenByEighty.TenByEighty;
import com.skilrock.drawgame.tenByThirty.TenByThirty;
import com.skilrock.drawgame.twelve.TwelveByTwentyFour;
import com.skilrock.escratch.IGEGameListActivity;
import com.skilrock.lms.communication.DGETask;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.preference.GlobalPref;
import com.skilrock.scratch.ScratchCards;
import com.skilrock.sportslottery.SLActivity;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainScreen extends DrawerBaseActivity implements
        OnBannerClickListener, WebServicesListener {
    public static MainScreen mainScreen;
    public static long loginTimeMilliseconds;
    public static long junkTicket;
    // public static MainListAdapter adapter;
    private GridView mainGrid;
    private int height;
    private int width;
    private double heightForGridChild;
    private int widthForGridChild;
    private double gridWeight = 0.65;// 10 % 6.5
    private int actionBarHeight, footerHeight;
    // private LinearLayout menuParent;
    private ArrayList<IconWithTitle> mainMenuData;
    String[] titlesMenu = new String[]{"Draw Games", "Scratch Cards",
            "Electronic Scratch"};
    //    private int sideIcon[] = new int[]{R.drawable.drawer_draw,
//            R.drawable.drawer_scratch, R.drawable.drawer_escratch};
    String[] sideTitles = new String[]{"Draw Games", "Sports Lottery",
            "Electronic Scratch", "Scratch Cards", "Instant Draw"};
    private MenuAdapter menuAdapter;
    private LinearLayout bannerView;
    //    private int bannerRes[] = new int[]{R.drawable.main_banner_five,
//            R.drawable.main_banner_bonus, R.drawable.main_banner_fast};
    public static ArrayList<Fragment> gameFragments;
    private Context context;
    private ArrayList<HashMap<String, String>> banners;
    private ArrayList<HashMap<String, String>> serviceInfos;
    private int[] mainGridIcon;
    private JSONObject jsonData;
    private JSONObject loginData;
    private boolean isExit;
    private View header;
    private Dialog dialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Analytics analytics;
    private GlobalPref globalPref;

    // protected boolean isDrawerClosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalPref = GlobalPref.getInstance(this);
        context = MainScreen.this;
        mainScreen = MainScreen.this;
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.MAIN_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        banners = new Gson().fromJson(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_BANNERS_DETAILS), type);
        serviceInfos = new Gson().fromJson(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_SERVICE_DETAILS), type);
        VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.SC_SER_NAME, "Scratch Cards");

        if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("serviceCode", Config.SC);
            hashMap.put("serviceName", "Scratch Cards");
            serviceInfos.add(hashMap);
        }

        sHeader();
        setDrawerItems();
        mainMenuData = new ArrayList<>();
        mainGridIcon = new int[serviceInfos.size()];
        for (int i = 0; i < serviceInfos.size(); i++) {
            Map<String, String> serivceInfo = serviceInfos.get(i);
            switch (serivceInfo.get("serviceCode")) {
                case Config.DG:
                    mainGridIcon[i] = R.drawable.draw;
                    break;
                case Config.IGE:
                    mainGridIcon[i] = R.drawable.escratch;
                    break;
                case Config.SC:
                    mainGridIcon[i] = R.drawable.scratch;
                    break;
                case Config.SL:
                    mainGridIcon[i] = R.drawable.sports;
                    break;
            }
            IconWithTitle iconWithTitle = new IconWithTitle(serivceInfo.get("serviceCode")
                    , serivceInfo.get("serviceName"),
                    mainGridIcon[i]);
            mainMenuData.add(iconWithTitle);
        }
        getDisplayDetails();

        bindViewIds();
        headerImage.setImageResource(R.drawable.full_logo);
        switch (serviceInfos.size()) {
            case 1:
                mainGrid.setNumColumns(1);
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild,
                        (int) widthForGridChild, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;
            case 2:
                mainGrid.setNumColumns(1);
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild / 2,
                        (int) widthForGridChild, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;
            case 3:
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild / 2,
                        (int) widthForGridChild / 2, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;
            case 4:
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild / 2,
                        (int) widthForGridChild / 2, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;
            case 5:
                mainGrid.setNumColumns(3);
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild / 2,
                        (int) widthForGridChild / 3, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;
            case 6:
                mainGrid.setNumColumns(3);
                menuAdapter = new MenuAdapter(getApplicationContext(),
                        R.layout.main_menu_row, (int) heightForGridChild / 2,
                        (int) widthForGridChild / 3, mainMenuData, mainGrid,
                        serviceInfos.size());
                break;

            default:
                break;
        }
        mainGrid.setAdapter(menuAdapter);
        mainGrid.setOnItemClickListener(new DebouncedOnItemClickListener(1000) {
            @Override
            public void onDebouncedItemClick(AdapterView<?> parent, View view,
                                             int position, long id) {
                setGamesData(position);
            }
        });
        if (banners != null) {
            if (banners.size() > 0) {
                View view = new View(getApplicationContext());
                view.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                BannerView bannerView = new BannerView(getApplicationContext());
                MainScreen.this.bannerView.addView(bannerView
                        .onCreateViewPanel(getApplicationContext(),
                                view, banners, MainScreen.this));
            } else {
                bannerView.setBackgroundResource(R.drawable.no_banner);
            }
        }
        if (getIntent().getBooleanExtra("isSLE", false)) {
            Intent intent = new Intent(this, SLActivity.class);
            intent.putExtra("gameTypeId", getIntent().getStringExtra("gameTypeId"));
            startActivity(intent);
        }
        preferences = getSharedPreferences("name", 0);
        if (!preferences.getBoolean("show", false)) {
            startActivity(new Intent(context, AppTourActivity.class));
            editor = getSharedPreferences("name", 0).edit();
            editor.putBoolean("show", true);
            editor.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    private void bindViewIds() {
        mainGrid = (GridView) findViewById(R.id.main_grid);
        header = findViewById(R.id.header);
        bannerView = (LinearLayout) findViewById(R.id.bannerView);
    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        int var;
        var = (int) getResources().getDimension(R.dimen.main_header_height);
        actionBarHeight = var;
        float v = getResources().getDisplayMetrics().density;
        GlobalVariables.topHeight = result + actionBarHeight;
        footerHeight = 0/*
                         * (int) (40 *
						 * getResources().getDisplayMetrics().density)
						 */;
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels - result - actionBarHeight
                - footerHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
    }

    private void setGamesData(int pos) {
        switch (mainMenuData.get(pos).getItemCode()) {
            case Config.DG:
                if (GlobalVariables.connectivityExists(context)) {
                    analytics.sendAction(Fields.Category.DRAW_GAME, Fields.Action.CLICK);
                    String url = VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_ROOT_URL) + "/services/tpDataMgmt/fetchGameData";
                    String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.DGE_MER_CODE) + "\"}";
                    new DGETask(MainScreen.this, "DGE", url, json).execute();
                } else {
                    GlobalVariables.showDataAlert(context);
                }
                break;
            case Config.SC:
                if (GlobalVariables.connectivityExists(context)) {
                    analytics.sendAction(Fields.Category.SCRATCH_CARD, Fields.Action.CLICK);
                    String path = "/com/skilrock/pms/mobile/scratchGames/action/fetchScratchGameList.action?";
                    String params = "userName=" + VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME);
                    new PMSWebTask(MainScreen.this, path + params, "", null, "SD", ScratchGameBean.class, "").execute();
                } else {
                    GlobalVariables.showDataAlert(context);
                }
                break;
            case Config.IGE:
                if (GlobalVariables.connectivityExists(context)) {
                    analytics.sendAction(Fields.Category.INSTANT_GAME, Fields.Action.CLICK);
                    startActivity(new Intent(getApplicationContext(), IGEGameListActivity.class));
                } else {
                    GlobalVariables.showDataAlert(context);
                }
                break;
            case Config.SL:
                analytics.sendAction(Fields.Category.SPORTS_LOTTERY, Fields.Action.CLICK);
                Intent intent = new Intent(this, SLActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.VISIBLE);
        headerSpinner.setVisibility(View.INVISIBLE);
        headerText.setVisibility(View.GONE);
        headerSubText.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onPause() {
        // if (mBanner != null) {
        // mBanner.onPauseBanner();
        // }
        super.onPause();
    }

    private void parseJson(JSONArray jsonArr) {
        try {
            JSONArray array = new JSONArray();
            GlobalVariables.GamesData.gamesData = array;
            for (int j = 0; j < jsonArr.length(); j++) {
                JSONObject gameData = jsonArr.getJSONObject(j);
                if (gameData.getInt("gameType") == 0) {
                    switch (gameData.optString("gameCode")) {
                        case Config.bonusKeno:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fiveGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fiveGameNameMachine:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fiveGameNameLagos:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.bonusGameName:
                            array.put(jsonArr.get(j));
                            break;
//                        case Config.bonusGameNameTwo:
//                            array.put(jsonArr.get(j));
//                            break;
                        case Config.fastGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.twelveGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByThirty:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByTwenty:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.oneToTwelve:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByNinety:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByEighty:
                            array.put(jsonArr.get(j));
                            break;
                        default:
                            break;
                    }
                }
            }
            GlobalVariables.GamesData.gamesDisplayName = new String[array
                    .length()];


            gameFragments = new ArrayList<>();
            GlobalVariables.GamesData.gamenameMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gameCodeMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gameDataMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gamesDevsArr = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject gameData = array.getJSONObject(i);
                GlobalVariables.GamesData.gamesDisplayName[i] = gameData
                        .getString("gameDispName");
                GlobalVariables.GamesData.gamenameMap.put(
                        GlobalVariables.GamesData.gamesDisplayName[i],
                        gameData.getString("gameCode"));
                GlobalVariables.GamesData.gameCodeMap.put(
                        gameData.getString("gameCode"),
                        GlobalVariables.GamesData.gamesDisplayName[i]);
                GlobalVariables.GamesData.gameDataMap.put(
                        gameData.getString("gameCode"), gameData + "");
                GlobalVariables.GamesData.gamesDevsArr[i] = gameData
                        .getString("gameCode");

                switch (gameData.getString("gameCode")) {
                    case Config.fiveGameName:
                        FiveByNine fiveByNine = new FiveByNine();
                        Bundle fiveBundle = new Bundle();
                        fiveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundle.putString("gameId", Config.fiveGameName);
                        fiveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray = gameData.getJSONArray("draws");
                        if (drawArray.length() > 0) {
                            fiveBundle.putBoolean("isDraws", true);
                        } else {
                            fiveBundle.putBoolean("isDraws", true);
                        }
                        fiveByNine.setArguments(fiveBundle);
                        gameFragments.add(fiveByNine);
                        break;

                    //new game add
                    case Config.bonusKeno:
                        BonusKeno bonusKeno = new BonusKeno();
                        Bundle bonusKenoBundle = new Bundle();
                        bonusKenoBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        bonusKenoBundle.putString("gameId", Config.bonusKeno);
                        bonusKenoBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        bonusKenoBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray bonusKenodrawArray = gameData.getJSONArray("draws");
                        if (bonusKenodrawArray.length() > 0) {
                            bonusKenoBundle.putBoolean("isDraws", true);
                        } else {
                            bonusKenoBundle.putBoolean("isDraws", true);
                        }
                        bonusKeno.setArguments(bonusKenoBundle);
                        gameFragments.add(bonusKeno);
                        break;

                    case Config.fiveGameNameMachine:
                        fiveByNine = new FiveByNine();
                        fiveBundle = new Bundle();
                        fiveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundle.putString("gameId", Config.fiveGameNameMachine);
                        fiveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        drawArray = gameData.getJSONArray("draws");
                        if (drawArray.length() > 0) {
                            fiveBundle.putBoolean("isDraws", true);
                        } else {
                            fiveBundle.putBoolean("isDraws", true);
                        }
                        fiveByNine.setArguments(fiveBundle);
                        gameFragments.add(fiveByNine);
                        break;

                    case Config.fiveGameNameLagos:
                        FiveByNineLagos fiveByNineLagos = new FiveByNineLagos();
                        Bundle fiveBundleLagos = new Bundle();
                        fiveBundleLagos.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundleLagos.putString("gameId", Config.fiveGameNameLagos);
                        fiveBundleLagos.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundleLagos.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayLagos = gameData.getJSONArray("draws");
                        if (drawArrayLagos.length() > 0) {
                            fiveBundleLagos.putBoolean("isDraws", true);
                        } else {
                            fiveBundleLagos.putBoolean("isDraws", true);
                        }
                        fiveByNineLagos.setArguments(fiveBundleLagos);
                        gameFragments.add(fiveByNineLagos);
                        break;


                    case Config.bonusGameName:
                        BonusLotto bonus = new BonusLotto();
                        Bundle bonusBundle = new Bundle();
                        bonusBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        bonusBundle.putString("gameId", Config.bonusGameName);
                        bonusBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        bonusBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray0 = gameData.getJSONArray("draws");
                        if (drawArray0.length() > 0) {
                            bonusBundle.putBoolean("isDraws", true);
                        } else {
                            bonusBundle.putBoolean("isDraws", true);
                        }
                        bonus.setArguments(bonusBundle);
                        gameFragments.add(bonus);
                        break;

//                    case Config.bonusGameNameTwo:
//                        BonusLotto bonusTwo = new BonusLotto();
//                        Bundle bonusBundleTwo = new Bundle();
//                        bonusBundleTwo.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
//                        bonusBundleTwo.putString("lastDrawResult", gameData.optString("lastDrawResult"));
//                        bonusBundleTwo.putString("gameId", Config.bonusGameNameTwo);
//                        bonusBundleTwo.putString("lastDrawTime", gameData.optString("lastDrawTime"));
//                        JSONArray drawArrayTS = gameData.getJSONArray("draws");
//                        if (drawArrayTS.length() > 0) {
//                            bonusBundleTwo.putBoolean("isDraws", true);
//                        } else {
//                            bonusBundleTwo.putBoolean("isDraws", true);
//                        }
//                        bonusTwo.setArguments(bonusBundleTwo);
//                        gameFragments.add(bonusTwo);
//                        break;

                    case Config.fastGameName:
                        FastLotto fast = new FastLotto();
                        Bundle fastBundle = new Bundle();
                        fastBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fastBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fastBundle.putString("gameId", Config.fastGameName);
                        fastBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray1 = gameData.getJSONArray("draws");
                        if (drawArray1.length() > 0) {
                            fastBundle.putBoolean("isDraws", true);
                        } else {
                            fastBundle.putBoolean("isDraws", true);
                        }
                        fast.setArguments(fastBundle);
                        gameFragments.add(fast);
                        break;

                    case Config.twelveGameName:
                        TwelveByTwentyFour twelve = new TwelveByTwentyFour();
                        Bundle twelveBundle = new Bundle();
                        twelveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        twelveBundle.putString("gameId", Config.twelveGameName);
                        if (gameData.has("lastDrawResult"))
                            twelveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            twelveBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            twelveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            twelveBundle.putString("lastDrawTime", "");
                        JSONArray drawArray2 = gameData.getJSONArray("draws");
                        if (drawArray2.length() > 0) {
                            twelveBundle.putBoolean("isDraws", true);
                        } else {
                            twelveBundle.putBoolean("isDraws", true);
                        }
                        twelve.setArguments(twelveBundle);
                        gameFragments.add(twelve);
                        break;

                    case Config.tenByThirty:
                        TenByThirty tenByThirty = new TenByThirty();
                        Bundle tenByThirtyBundle = new Bundle();
                        tenByThirtyBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenByThirtyBundle.putString("gameId", Config.tenByThirty);
                        if (gameData.has("lastDrawResult"))
                            tenByThirtyBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            tenByThirtyBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            tenByThirtyBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            tenByThirtyBundle.putString("lastDrawTime", "");
                        JSONArray drawArraytenByThirty = gameData.getJSONArray("draws");
                        if (drawArraytenByThirty.length() > 0) {
                            tenByThirtyBundle.putBoolean("isDraws", true);
                        } else {
                            tenByThirtyBundle.putBoolean("isDraws", true);
                        }
                        tenByThirty.setArguments(tenByThirtyBundle);
                        gameFragments.add(tenByThirty);
                        break;

                    //new game development
                    case Config.tenByTwenty:
                        TenByTwenty tenByTwenty = new TenByTwenty();
                        Bundle tenByTwentyBundle = new Bundle();
                        tenByTwentyBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenByTwentyBundle.putString("gameId", Config.tenByTwenty);
                        if (gameData.has("lastDrawResult"))
                            tenByTwentyBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            tenByTwentyBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            tenByTwentyBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            tenByTwentyBundle.putString("lastDrawTime", "");
                        JSONArray drawArraytenByTwenty = gameData.getJSONArray("draws");
                        if (drawArraytenByTwenty.length() > 0) {
                            tenByTwentyBundle.putBoolean("isDraws", true);
                        } else {
                            tenByTwentyBundle.putBoolean("isDraws", true);
                        }
                        tenByTwenty.setArguments(tenByTwentyBundle);
                        gameFragments.add(tenByTwenty);
                        break;
                    //end of game

                    case Config.oneToTwelve:
                        OneToTwelve ontToTwe = new OneToTwelve();
                        Bundle oneBundle = new Bundle();
                        oneBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        oneBundle.putString("gameId", Config.oneToTwelve);
                        oneBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        oneBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayOne = gameData.getJSONArray("draws");
                        if (drawArrayOne.length() > 0) {
                            oneBundle.putBoolean("isDraws", true);
                        } else {
                            oneBundle.putBoolean("isDraws", true);
                        }
                        ontToTwe.setArguments(oneBundle);
                        gameFragments.add(ontToTwe);
                        break;

                    case Config.tenByNinety:
                        TenByNinety tenByNinety = new TenByNinety();
                        Bundle tenBundle = new Bundle();
                        tenBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenBundle.putString("gameId", Config.tenByNinety);
                        tenBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        tenBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayForTen = gameData.getJSONArray("draws");
                        if (drawArrayForTen.length() > 0) {
                            tenBundle.putBoolean("isDraws", true);
                        } else {
                            tenBundle.putBoolean("isDraws", true);
                        }
                        tenByNinety.setArguments(tenBundle);
                        gameFragments.add(tenByNinety);
                        break;
                    case Config.tenByEighty:
                        TenByEighty tenByEighty = new TenByEighty();
                        Bundle tenByEightyBundle = new Bundle();
                        tenByEightyBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenByEightyBundle.putString("gameId", Config.tenByEighty);

                        if (gameData.has("lastDrawResult"))
                            tenByEightyBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            tenByEightyBundle.putString("lastDrawResult", "");

                        if (gameData.has("lastDrawTime"))
                            tenByEightyBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            tenByEightyBundle.putString("lastDrawTime", "");

                        JSONArray drawArraytenByEighty = gameData.getJSONArray("draws");
                        if (drawArraytenByEighty.length() > 0) {
                            tenByEightyBundle.putBoolean("isDraws", true);
                        } else {
                            tenByEightyBundle.putBoolean("isDraws", true);
                        }
                        tenByEighty.setArguments(tenByEightyBundle);
                        gameFragments.add(tenByEighty);
                        break;

                    default:
                        array.remove(i);
                        break;
//                    case Config.thaiGameName:
//                        ThaiBySix thaiBySix = new ThaiBySix();
//                        Bundle thaiBundle = new Bundle();
//                        thaiBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
//                        thaiBundle.putString("lastDrawResult", gameData.getString("lastDrawResult"));
//                        thaiBundle.putString("lastDrawTime", gameData.getString("lastDrawTime"));
//                        JSONArray drawArrayForThai = gameData.getJSONArray("draws");
//                        if (drawArrayForThai.length() > 0) {
//                            thaiBundle.putBoolean("isDraws", true);
//                        } else {
//                            thaiBundle.putBoolean("isDraws", true);
//                        }
//                        thaiBySix.setArguments(thaiBundle);
//                        gameFragments.add(thaiBySix);
//                        break;
                }
            }
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
            Intent intent = new Intent(getApplicationContext(),
                    DGActivity.class);
//            intent.putExtra(DGActivity.GAME_FRAMGENT, gameFragments);
            startActivity(intent);
            overridePendingTransition(GlobalVariables.startAmin,
                    GlobalVariables.endAmin);
            dialog.dismiss();
//                }
//            }, 5000);
        } catch (JSONException e) {
            dialog.dismiss();
            e.printStackTrace();
        }
    }


    @Override
    public void onBannerClickListener(HashMap<String, String> sliderImagesData) {
        switch (sliderImagesData.get("actiontype")) {
            case "0":
                //Toast.makeText(context, "Internal", Toast.LENGTH_SHORT).show();
                break;
            case "1":
                //Toast.makeText(context, "External", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        this.dialog = dialog;
        if (methodType == null) {
            if (dialog != null)
                dialog.dismiss();
            return;
        }
        switch (methodType) {
            case "SD":
                if (resultData != null) {
                    ScratchGameBean scratchGameData = (ScratchGameBean) resultData;
                    if (scratchGameData.isSuccess()) {
                        analytics.sendAll(Fields.Category.SCRATCH_CARD, Fields.Action.CLICK, Fields.Label.SUCCESS);
                        ArrayList<ScratchGameBean.ScratchGameData> scratchGameBean = scratchGameData.getScratchGameData();
                        Intent intent1 = new Intent(getApplicationContext(),
                                ScratchCards.class);
                        intent1.putExtra("data", scratchGameBean);
                        startActivity(intent1);
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        dialog.dismiss();
                    } else {
                        analytics.sendAll(Fields.Category.SCRATCH_CARD, Fields.Action.CLICK, Fields.Label.FAILURE);
                        dialog.dismiss();
                        Utils.Toast(context, scratchGameData.getErrorMsg());
                    }
                } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                    if (dialog != null)
                        dialog.dismiss();
                    Utils.Toast(context, "Data not available in offline mode");
                } else {
                    analytics.sendAll(Fields.Category.SCRATCH_CARD, Fields.Action.CLICK, Fields.Label.FAILURE);
                    dialog.dismiss();
                    GlobalVariables.showServerErr(MainScreen.this);
                }
                break;
            case "DGE":
                jsonData = (JSONObject) resultData;
                try {
                    if (jsonData != null) {
                        if (jsonData.getInt("responseCode") == 0) {
                            analytics.sendAll(Fields.Category.DRAW_GAME, Fields.Action.CLICK, Fields.Label.SUCCESS);
                            parseJson(jsonData.getJSONArray("games"));
                        } else {
                            analytics.sendAll(Fields.Category.DRAW_GAME, Fields.Action.CLICK, Fields.Label.FAILURE);
                            dialog.dismiss();
                            Utils.Toast(context, jsonData.getString("responseMsg"));
                        }
                    } else if (Config.isStatic && GlobalVariables.loadDummyData) {
                        dialog.dismiss();
                        Utils.Toast(context, "Data not available in offline mode");
                    } else {
                        analytics.sendAll(Fields.Category.DRAW_GAME, Fields.Action.CLICK, Fields.Label.FAILURE);
                        dialog.dismiss();
                        GlobalVariables.showServerErr(MainScreen.this);
                    }
                } catch (Exception e) {
                    analytics.sendAll(Fields.Category.DRAW_GAME, Fields.Action.CLICK, Fields.Label.FAILURE);
                    e.printStackTrace();
                    dialog.dismiss();
                    GlobalVariables.showServerErr(MainScreen.this);
                }
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isDrawerClosed) {
                if (isExit) {
                    VariableStorage.GlobalPref.setStringPreferences(context,
                            VariableStorage.GlobalPref.EXIT_APP, "true");
                    finish();
                    return super.onKeyDown(keyCode, event);
                } else {
                    isExit = true;
                    Utils.Toast(context, "Press back again to exit!"
                    );
                    return false;
                }
            } else {
                isExit = false;
                drawerLayout.closeDrawer(drawerView);
                isDrawerClosed = false;
                return false;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    public static void parseRefreshedData(JSONArray jsonArr) {
        try {
            JSONArray array = new JSONArray();
            GlobalVariables.GamesData.gamesData = array;
            for (int j = 0; j < jsonArr.length(); j++) {
                JSONObject gameData = jsonArr.getJSONObject(j);
                if (gameData.getInt("gameType") == 0) {
                    switch (gameData.optString("gameCode")) {
                        case Config.bonusKeno:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fiveGameNameMachine:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fiveGameNameLagos:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.bonusGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.bonusGameNameTwo:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.fastGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.twelveGameName:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByThirty:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByTwenty:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.oneToTwelve:
                            array.put(jsonArr.get(j));
                            break;
                        case Config.tenByNinety:
                            array.put(jsonArr.get(j));
                            break;
                        default:
                            break;
                    }
                }
            }
            GlobalVariables.GamesData.gamesDisplayName = new String[array
                    .length()];


            gameFragments = new ArrayList<>();
            GlobalVariables.GamesData.gamenameMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gameCodeMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gameDataMap = new LinkedHashMap<>();
            GlobalVariables.GamesData.gamesDevsArr = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                JSONObject gameData = array.getJSONObject(i);
                GlobalVariables.GamesData.gamesDisplayName[i] = gameData
                        .getString("gameDispName");
                GlobalVariables.GamesData.gamenameMap.put(
                        GlobalVariables.GamesData.gamesDisplayName[i],
                        gameData.getString("gameCode"));
                GlobalVariables.GamesData.gameCodeMap.put(
                        gameData.getString("gameCode"),
                        GlobalVariables.GamesData.gamesDisplayName[i]);
                GlobalVariables.GamesData.gameDataMap.put(
                        gameData.getString("gameCode"), gameData + "");
                GlobalVariables.GamesData.gamesDevsArr[i] = gameData
                        .getString("gameCode");
                switch (gameData.getString("gameCode")) {
                    case Config.fiveGameName:
                        FiveByNine fiveByNine = new FiveByNine();
                        Bundle fiveBundle = new Bundle();
                        fiveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundle.putString("gameId", Config.fiveGameName);
                        fiveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray = gameData.getJSONArray("draws");
                        if (drawArray.length() > 0) {
                            fiveBundle.putBoolean("isDraws", true);
                        } else {
                            fiveBundle.putBoolean("isDraws", true);
                        }
                        fiveByNine.setArguments(fiveBundle);
                        gameFragments.add(fiveByNine);
                        break;

                    //new game add
                    case Config.bonusKeno:
                        BonusKeno bonusKeno = new BonusKeno();
                        Bundle bonusKenoBundle = new Bundle();
                        bonusKenoBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        bonusKenoBundle.putString("gameId", Config.bonusKeno);
                        bonusKenoBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        bonusKenoBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray bonusKenodrawArray = gameData.getJSONArray("draws");
                        if (bonusKenodrawArray.length() > 0) {
                            bonusKenoBundle.putBoolean("isDraws", true);
                        } else {
                            bonusKenoBundle.putBoolean("isDraws", true);
                        }
                        bonusKeno.setArguments(bonusKenoBundle);
                        gameFragments.add(bonusKeno);
                        break;

                    case Config.fiveGameNameMachine:
                        fiveByNine = new FiveByNine();
                        fiveBundle = new Bundle();
                        fiveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundle.putString("gameId", Config.fiveGameNameMachine);
                        fiveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        drawArray = gameData.getJSONArray("draws");
                        if (drawArray.length() > 0) {
                            fiveBundle.putBoolean("isDraws", true);
                        } else {
                            fiveBundle.putBoolean("isDraws", true);
                        }
                        fiveByNine.setArguments(fiveBundle);
                        gameFragments.add(fiveByNine);
                        break;

                    case Config.fiveGameNameLagos:
                        FiveByNineLagos fiveByNineLagos = new FiveByNineLagos();
                        Bundle fiveBundleLagos = new Bundle();
                        fiveBundleLagos.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fiveBundleLagos.putString("gameId", Config.fiveGameNameLagos);
                        fiveBundleLagos.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fiveBundleLagos.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayLagos = gameData.getJSONArray("draws");
                        if (drawArrayLagos.length() > 0) {
                            fiveBundleLagos.putBoolean("isDraws", true);
                        } else {
                            fiveBundleLagos.putBoolean("isDraws", true);
                        }
                        fiveByNineLagos.setArguments(fiveBundleLagos);
                        gameFragments.add(fiveByNineLagos);
                        break;


                    case Config.bonusGameName:
                        BonusLotto bonus = new BonusLotto();
                        Bundle bonusBundle = new Bundle();
                        bonusBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        bonusBundle.putString("gameId", Config.bonusGameName);
                        bonusBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        bonusBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray0 = gameData.getJSONArray("draws");
                        if (drawArray0.length() > 0) {
                            bonusBundle.putBoolean("isDraws", true);
                        } else {
                            bonusBundle.putBoolean("isDraws", true);
                        }
                        bonus.setArguments(bonusBundle);
                        gameFragments.add(bonus);
                        break;

                    case Config.bonusGameNameTwo:
                        BonusLotto bonusTwo = new BonusLotto();
                        Bundle bonusBundleTwo = new Bundle();
                        bonusBundleTwo.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        bonusBundleTwo.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        bonusBundleTwo.putString("gameId", Config.bonusGameNameTwo);
                        bonusBundleTwo.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayTS = gameData.getJSONArray("draws");
                        if (drawArrayTS.length() > 0) {
                            bonusBundleTwo.putBoolean("isDraws", true);
                        } else {
                            bonusBundleTwo.putBoolean("isDraws", true);
                        }
                        bonusTwo.setArguments(bonusBundleTwo);
                        gameFragments.add(bonusTwo);
                        break;

                    case Config.fastGameName:
                        FastLotto fast = new FastLotto();
                        Bundle fastBundle = new Bundle();
                        fastBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        fastBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        fastBundle.putString("gameId", Config.fastGameName);
                        fastBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArray1 = gameData.getJSONArray("draws");
                        if (drawArray1.length() > 0) {
                            fastBundle.putBoolean("isDraws", true);
                        } else {
                            fastBundle.putBoolean("isDraws", true);
                        }
                        fast.setArguments(fastBundle);
                        gameFragments.add(fast);
                        break;

                    case Config.twelveGameName:
                        TwelveByTwentyFour twelve = new TwelveByTwentyFour();
                        Bundle twelveBundle = new Bundle();
                        twelveBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        twelveBundle.putString("gameId", Config.twelveGameName);
                        if (gameData.has("lastDrawResult"))
                            twelveBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            twelveBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            twelveBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            twelveBundle.putString("lastDrawTime", "");
                        JSONArray drawArray2 = gameData.getJSONArray("draws");
                        if (drawArray2.length() > 0) {
                            twelveBundle.putBoolean("isDraws", true);
                        } else {
                            twelveBundle.putBoolean("isDraws", true);
                        }
                        twelve.setArguments(twelveBundle);
                        gameFragments.add(twelve);
                        break;

                    case Config.tenByThirty:
                        TenByThirty tenByThirty = new TenByThirty();
                        Bundle tenByThirtyBundle = new Bundle();
                        tenByThirtyBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenByThirtyBundle.putString("gameId", Config.tenByThirty);
                        if (gameData.has("lastDrawResult"))
                            tenByThirtyBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            tenByThirtyBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            tenByThirtyBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            tenByThirtyBundle.putString("lastDrawTime", "");
                        JSONArray drawArraytenByThirty = gameData.getJSONArray("draws");
                        if (drawArraytenByThirty.length() > 0) {
                            tenByThirtyBundle.putBoolean("isDraws", true);
                        } else {
                            tenByThirtyBundle.putBoolean("isDraws", true);
                        }
                        tenByThirty.setArguments(tenByThirtyBundle);
                        gameFragments.add(tenByThirty);
                        break;

                    //new game development
                    case Config.tenByTwenty:
                        TenByTwenty tenByTwenty = new TenByTwenty();
                        Bundle tenByTwentyBundle = new Bundle();
                        tenByTwentyBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenByTwentyBundle.putString("gameId", Config.tenByTwenty);
                        if (gameData.has("lastDrawResult"))
                            tenByTwentyBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        else
                            tenByTwentyBundle.putString("lastDrawResult", "");
                        if (gameData.has("lastDrawTime"))
                            tenByTwentyBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        else
                            tenByTwentyBundle.putString("lastDrawTime", "");
                        JSONArray drawArraytenByTwenty = gameData.getJSONArray("draws");
                        if (drawArraytenByTwenty.length() > 0) {
                            tenByTwentyBundle.putBoolean("isDraws", true);
                        } else {
                            tenByTwentyBundle.putBoolean("isDraws", true);
                        }
                        tenByTwenty.setArguments(tenByTwentyBundle);
                        gameFragments.add(tenByTwenty);
                        break;
                    //end of game

                    case Config.oneToTwelve:
                        OneToTwelve ontToTwe = new OneToTwelve();
                        Bundle oneBundle = new Bundle();
                        oneBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        oneBundle.putString("gameId", Config.oneToTwelve);
                        oneBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        oneBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayOne = gameData.getJSONArray("draws");
                        if (drawArrayOne.length() > 0) {
                            oneBundle.putBoolean("isDraws", true);
                        } else {
                            oneBundle.putBoolean("isDraws", true);
                        }
                        ontToTwe.setArguments(oneBundle);
                        gameFragments.add(ontToTwe);
                        break;

                    case Config.tenByNinety:
                        TenByNinety tenByNinety = new TenByNinety();
                        Bundle tenBundle = new Bundle();
                        tenBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
                        tenBundle.putString("gameId", Config.tenByNinety);
                        tenBundle.putString("lastDrawResult", gameData.optString("lastDrawResult"));
                        tenBundle.putString("lastDrawTime", gameData.optString("lastDrawTime"));
                        JSONArray drawArrayForTen = gameData.getJSONArray("draws");
                        if (drawArrayForTen.length() > 0) {
                            tenBundle.putBoolean("isDraws", true);
                        } else {
                            tenBundle.putBoolean("isDraws", true);
                        }
                        tenByNinety.setArguments(tenBundle);
                        gameFragments.add(tenByNinety);
                        break;
                    default:
                        break;
//                    case Config.thaiGameName:
//                        ThaiBySix thaiBySix = new ThaiBySix();
//                        Bundle thaiBundle = new Bundle();
//                        thaiBundle.putString("gameName", GlobalVariables.GamesData.gamesDisplayName[i]);
//                        thaiBundle.putString("lastDrawResult", gameData.getString("lastDrawResult"));
//                        thaiBundle.putString("lastDrawTime", gameData.getString("lastDrawTime"));
//                        JSONArray drawArrayForThai = gameData.getJSONArray("draws");
//                        if (drawArrayForThai.length() > 0) {
//                            thaiBundle.putBoolean("isDraws", true);
//                        } else {
//                            thaiBundle.putBoolean("isDraws", true);
//                        }
//                        thaiBySix.setArguments(thaiBundle);
//                        gameFragments.add(thaiBySix);
//                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

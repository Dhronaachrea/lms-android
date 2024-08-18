package com.skilrock.drawgame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.drawgame.OneToTwelve.OneToTwelveGameScreen;
import com.skilrock.drawgame.TenByTwenty.TenByTwentyGameScreen;
import com.skilrock.drawgame.bonusLotto_keno_SixThirtySix.BonusKeno;
import com.skilrock.drawgame.bonusLotto_keno_SixThirtySix.BonusKenoGameScreen;
import com.skilrock.drawgame.bonuslotto.BonusGameScreen;
import com.skilrock.drawgame.fast.FastGameScreen;
import com.skilrock.drawgame.five.FiveGameScreen;
import com.skilrock.drawgame.fiveLagos.FiveGameScreenLagos;
import com.skilrock.drawgame.kenoSeven.TenByNinetyGameScreen;
import com.skilrock.drawgame.tenByEighty.TenByEightyGameScreen;
import com.skilrock.drawgame.tenByThirty.TenByThirty;
import com.skilrock.drawgame.tenByThirty.TenByThirtyGameScreen;
import com.skilrock.drawgame.twelve.TwelveGameScreen;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import java.util.Arrays;

public class DGPlayActivity extends DrawerBaseActivity {
    private Analytics analytics;
    //    private JSONObject jsonObject;
//    private LinkedHashMap<String, DrawData> drawData;
//	private LinkedHashMap<String, BetTypeBean> betTypeData;
    private FiveGameScreen fiveGameScreen;
    private FiveGameScreenLagos fiveGameScreenLagos;

    private boolean isNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_games_play);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.APP_TOUR);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        sHeader();
        setDrawerItems();
        bindViewIds();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText(VariableStorage.GlobalPref.getStringData(getApplication(), VariableStorage.GlobalPref.DGE_SER_NAME));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, R.id.spinner_text, GlobalVariables.GamesData.gamesDisplayName);
        headerSpinner.setAdapter(adapter);
        replaceFragment(getIntent().getStringExtra("selectedGame"));
        headerSpinner.post(new Runnable() {
            @Override
            public void run() {
                headerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long arg3) {
                        if (drawerLayout.isDrawerOpen(drawerView))
                            drawerLayout.closeDrawer(drawerView);
                        view.setBackgroundColor(Color.TRANSPARENT);
                        replaceFragment(GlobalVariables.GamesData.gamesDisplayName[pos]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        if (GlobalVariables.GamesData.gamesDisplayName.length <= 1 || GlobalPref.getInstance(this).getCountry().equalsIgnoreCase("ghana")) {
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
        View view = findViewById(R.id.header);
        headerNavigation = (ImageView) view.findViewById(R.id.drawer_image);
        headerImage = (ImageView) view.findViewById(R.id.header_image);
        // headerSpinner = (Spinner) view.findViewById(R.id.spinner);
        headerText = (CustomTextView) view.findViewById(R.id.header_text);
        headerSubText = (CustomTextView)
                view.findViewById(R.id.header_sub_text);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
//        headerSpinner.setVisibility(View.VISIBLE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }


    class TapGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return super.onSingleTapConfirmed(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
    }

    // public void animHeaderVisibility(int visibility) {
    // headerAnim.setVisibility(visibility);
    // }

    private void replaceFragment(String gameName) {
        headerSubText.setText(gameName);
        headerSpinner.setSelection(Arrays.asList(GlobalVariables.GamesData.gamesDevsArr).indexOf(GlobalVariables.GamesData.gamenameMap.get(gameName)));
        switch (GlobalVariables.GamesData.gamenameMap.get(gameName)) {
            case Config.bonusKeno:
                BonusKenoGameScreen screenBonusKeno = new BonusKenoGameScreen();
                Bundle bonusKenoBundle = new Bundle();
                bonusKenoBundle.putString("gameName", gameName);
                bonusKenoBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                screenBonusKeno.setArguments(bonusKenoBundle);
                replaceFragment(screenBonusKeno);
                break;
            case Config.tenByEighty:
                TenByEightyGameScreen tenByEightyGameScreen = new TenByEightyGameScreen();
                Bundle tenByEightyBundle = new Bundle();
                tenByEightyBundle.putString("gameName", gameName);
                tenByEightyBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                tenByEightyGameScreen.setArguments(tenByEightyBundle);
                replaceFragment(tenByEightyGameScreen);
                break;
            case Config.fiveGameName:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.FIVE_GAME_SCREEN);
                FiveGameScreen screen = new FiveGameScreen();
                Bundle fiveBundle = new Bundle();
                fiveBundle.putString("gameName", gameName);
                fiveBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                screen.setArguments(fiveBundle);
                replaceFragment(screen);
                break;
            case Config.fiveGameNameLagos:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.FIVE_GAME_SCREEN);
                FiveGameScreenLagos screenLagos = new FiveGameScreenLagos();
                Bundle fiveBundleLagos = new Bundle();
                fiveBundleLagos.putString("gameName", gameName);
                fiveBundleLagos.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                screenLagos.setArguments(fiveBundleLagos);
                replaceFragment(screenLagos);
                break;
            case Config.bonusGameName:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.BONUS_GAME_SCREEN);
                BonusGameScreen bonusGameScreen = new BonusGameScreen();
                Bundle bonusBundle = new Bundle();
                bonusBundle.putString("gameName", gameName);
                bonusBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                bonusBundle.putString("gameId", Config.bonusGameName);
                bonusGameScreen.setArguments(bonusBundle);
                replaceFragment(bonusGameScreen);
                break;
            case Config.bonusGameNameTwo:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.BONUS_GAME_SCREEN);
                BonusGameScreen bonusGameScreenTwo = new BonusGameScreen();
                Bundle bonusBundleTwo = new Bundle();
                bonusBundleTwo.putString("gameName", gameName);
                bonusBundleTwo.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                bonusBundleTwo.putString("gameId", Config.bonusGameNameTwo);
                bonusGameScreenTwo.setArguments(bonusBundleTwo);
                replaceFragment(bonusGameScreenTwo);
                break;

            case Config.fastGameName:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.FAST_GAME_SCREEN);

                FastGameScreen fastsGameScreen = new FastGameScreen();
                Bundle fastBundle = new Bundle();
                fastBundle.putString("gameName", gameName);
                fastBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                fastsGameScreen.setArguments(fastBundle);
                replaceFragment(fastsGameScreen);
                break;
            case Config.twelveGameName:
                analytics.sendAll(Fields.Category.UX, Fields.Action.DROPDOWN, Fields.Label.TWELVE_GAME_SCREEN);

                TwelveGameScreen twescreen = new TwelveGameScreen();
                Bundle tweBundle = new Bundle();
                tweBundle.putString("gameName", gameName);
                tweBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                twescreen.setArguments(tweBundle);
                replaceFragment(twescreen);
                break;
            case Config.tenByNinety:
                TenByNinetyGameScreen tenScreen = new TenByNinetyGameScreen();
                Bundle tenBundle = new Bundle();
                tenBundle.putString("gameName", gameName);
                tenBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                tenScreen.setArguments(tenBundle);
                replaceFragment(tenScreen);
                break;
            case Config.tenByThirty:
                TenByThirtyGameScreen tenByThirtyGameScreen = new TenByThirtyGameScreen();
                Bundle TenByThirtyBundle = new Bundle();
                TenByThirtyBundle.putString("gameName", gameName);
                TenByThirtyBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                tenByThirtyGameScreen.setArguments(TenByThirtyBundle);
                replaceFragment(tenByThirtyGameScreen);
                break;
            //new game tenByTwenty
            case Config.tenByTwenty:
                TenByTwentyGameScreen tenByTwentyGameScreen = new TenByTwentyGameScreen();
                Bundle tenByTwentyBundle = new Bundle();
                tenByTwentyBundle.putString("gameName", gameName);
                tenByTwentyBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                tenByTwentyGameScreen.setArguments(tenByTwentyBundle);
                replaceFragment(tenByTwentyGameScreen);
                break;
            //end of game
            case Config.oneToTwelve:
                OneToTwelveGameScreen oneTwelveScreen = new OneToTwelveGameScreen();
                Bundle oneTweBundle = new Bundle();
                oneTweBundle.putString("gameName", gameName);
                oneTweBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
                oneTwelveScreen.setArguments(oneTweBundle);
                replaceFragment(oneTwelveScreen);
                break;
            case Config.thaiGameName:
//                ActivityThaiLottery thaiLottoGameScreen = new ActivityThaiLottery();
//                Bundle thaiBundle = new Bundle();
//                thaiBundle.putString("gameName", gameName);
//                thaiBundle.putBoolean("isDraws", getIntent().getBooleanExtra("isDraws", false));
//                thaiLottoGameScreen.setArguments(thaiBundle);
//                replaceFragment(thaiLottoGameScreen);
                break;
            default:
                Utils.Toast(this, "Currently sale is not available!!!");
                break;
        }
    }

    void replaceFragment(Fragment fragment) {
        FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragments_frame, fragment);
        ft.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    public void isNext(boolean isNext, Fragment fiveGameScreen) {
        this.isNext = isNext;
        if (fiveGameScreen instanceof FiveGameScreen)
            this.fiveGameScreen = (FiveGameScreen) fiveGameScreen;
        else
            this.fiveGameScreenLagos = (FiveGameScreenLagos) fiveGameScreen;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isNext) {
                if (fiveGameScreen != null) {
                    isNext = false;
                    fiveGameScreen.setNextGrid();
                } else {
                    isNext = false;
                    fiveGameScreenLagos.setNextGrid();
                }
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected boolean enableTouch = true;

    public void enableTouchEvent(boolean flag) {
        enableTouch = flag;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (enableTouch)
            return super.dispatchTouchEvent(ev);
        return true;
    }
}
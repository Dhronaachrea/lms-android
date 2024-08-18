package com.skilrock.sportslottery;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.viewpager.SliderLayout;
import com.viewpager.indicator.TitlePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stpl on 7/16/2015.
 */
public class SportsLotteryMatchActivity extends DrawerBaseActivity {

    private LinearLayout llDraw;
    private TextView txtInfo;
    private SliderLayout pager;
    private GestureDetector gestureDetector;
    private FragmentManager fragmentManager;
    private SportsLotteryMatchBean bean;
    private List<SportsLotteryMatchBean.GameTypeData> gameTypeData;
    private List<SportsLotteryMatchBean.DrawData> drawData;
    private List<String> gameTypeNames;
    private MatchPagerAdapter pagerAdapter;
    private TitlePageIndicator indicator;
    private String headerText;
    public static String SPORTS_LOTTERY_MATCH_LIST_POSITION = "sports_lottery_match_list_position";
    public static String SPORTS_LOTTERY_MATCH_LIST = "sports_lottery_match_list";
    private Analytics analytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_match_activity);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER_MATCH_LIST);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);


        sHeader();
        setDrawerItems();

        llDraw = (LinearLayout) findViewById(R.id.ll_draw);
        txtInfo = (TextView) findViewById(R.id.txt_info);
        pager = (SliderLayout) findViewById(R.id.pager);
        pager.setPageMargin(30);

        fragmentManager = getSupportFragmentManager();
        bean = (SportsLotteryMatchBean) getIntent().getSerializableExtra(SPORTS_LOTTERY_MATCH_LIST);
        if (bean == null) return;
        headerText = bean.getMatchListData().getGameData().get(0).getGameDisplayName();
        gameTypeData = bean.getMatchListData().getGameData().get(0).getGameTypeData();
        gameTypeNames = getGameTypeNames();
        gestureDetector = new GestureDetector(this, new TapGesture());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_row,
                R.id.spinner_text, gameTypeNames);
        headerSpinner.setAdapter(adapter);
        if (getIntent().hasExtra(SPORTS_LOTTERY_MATCH_LIST_POSITION)) {
            int position = getIntent().getIntExtra(SPORTS_LOTTERY_MATCH_LIST_POSITION, 0);
            headerSpinner.setSelection(position);
            drawData = gameTypeData.get(position).getDrawData();
        } else
            setInitialGame();
        checkDraw();
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pager.setPresetTransformer(SliderLayout.Transformer.Tablet);
        pagerAdapter = new MatchPagerAdapter(fragmentManager, drawData);
        pager.setAdapter(pagerAdapter);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                gestureDetector.onTouchEvent(arg1);
                return false;
            }
        });
        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setTypeface(Config.globalTextFont);
        indicator.setViewPager(pager);
        indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
        super.headerText.setText(headerText);
        headerSubText.setText("MATCH LIST");

        headerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long arg3) {
                if (drawerLayout.isDrawerOpen(drawerView))
                    drawerLayout.closeDrawer(drawerView);
                view.setBackgroundColor(Color.TRANSPARENT);
                //pager.setCurrentItem(pos, true);
                drawData = gameTypeData.get(pos).getDrawData();
                checkDraw();
                pagerAdapter = new MatchPagerAdapter(fragmentManager, drawData);
                pager.setAdapter(pagerAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    private void setInitialGame() {
        for (int i = 0; i < gameTypeNames.size(); i++) {
            if (gameTypeData.get(i).getDrawData().size() != 0) {
                headerSpinner.setSelection(i);
                drawData = gameTypeData.get(i).getDrawData();
                break;
            } else if (i == gameTypeNames.size() - 1) {
                headerSpinner.setSelection(0);
                drawData = gameTypeData.get(i).getDrawData();
            }
        }
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(gameTypeNames.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        super.headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    private List<String> getGameTypeNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < gameTypeData.size(); i++) {
            names.add(gameTypeData.get(i).getGameTypeDisplayName());
        }
        return names;
    }

    class TapGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return super.onSingleTapConfirmed(e);
        }
    }

    private void checkDraw() {
        if (drawData == null || drawData.size() == 0) {
            drawData = new ArrayList<>();
            llDraw.setVisibility(View.GONE);
            txtInfo.setVisibility(View.VISIBLE);
        } else {
            llDraw.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

}

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
 * Created by stpl on 7/17/2015.
 */
public class SportsLotteryResultActivity extends DrawerBaseActivity {

    //    private ListView top;
//    private ListView bottom;
//    private SportsLotteryResultTopAdapter topAdapter;
//    private SportsLotteryResultBottomAdapter bottomAdapter;
    private LinearLayout llDraw;
    private TextView txtInfo;
    private SliderLayout pager;
    private GestureDetector gestureDetector;
    private FragmentManager fragmentManager;
    private ResultPagerAdapter pagerAdapter;
    private TitlePageIndicator indicator;

    private SportsLotteryCheckResultBean bean;
    private List<SportsLotteryCheckResultBean.GameTypeData> eventDatas;
    private int layoutRes;
    public static String SPORTS_LOTTERY_RESULT = "sports_lottery_result_bean";

    private List<String> gameTypeNames;
    private String headerText;
    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sports_lottery_result_activity);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER_CHECK_RESULT);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        fragmentManager = getSupportFragmentManager();
        sHeader();
        setDrawerItems();
//        top = (ListView) findViewById(R.id.lst_sports_result_top);
//        bottom = (ListView) findViewById(R.id.lst_sports_result_bottom);
        llDraw = (LinearLayout) findViewById(R.id.ll_draw);
        txtInfo = (TextView) findViewById(R.id.txt_info);
        pager = (SliderLayout) findViewById(R.id.pager);

        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        gestureDetector = new GestureDetector(this, new TapGesture());
        bean = (SportsLotteryCheckResultBean) getIntent().getSerializableExtra(SPORTS_LOTTERY_RESULT);
        eventDatas = bean.getDrawResultData().getGameData().get(0).getGameTypeData();
        headerText = bean.getDrawResultData().getGameData().get(0).getGameDisplayName();
        gameTypeNames = getGameTypeNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_row,
                R.id.spinner_text, gameTypeNames);
        headerSpinner.setAdapter(adapter);
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkDraw(0);
        if (eventDatas != null && eventDatas.size() != 0) {
            pagerAdapter = new ResultPagerAdapter(fragmentManager, eventDatas.get(0)/*, pager*/);
            pager.setOffscreenPageLimit(eventDatas.get(0).getDrawData().size());
            pager.setAdapter(pagerAdapter);
            pager.setPresetTransformer(SliderLayout.Transformer.Tablet);
            pager.setPageMargin(1);
            indicator.setTypeface(Config.globalTextFont);
            indicator.setViewPager(pager);
            indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);
        }
//        pager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                gestureDetector.onTouchEvent(arg1);
//                return false;
//            }
//        });


        super.headerText.setText(headerText);
        headerSubText.setText("RESULTS");
        headerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long arg3) {
                if (drawerLayout.isDrawerOpen(drawerView))
                    drawerLayout.closeDrawer(drawerView);
                view.setBackgroundColor(Color.TRANSPARENT);
                //pager.setCurrentItem(pos, true);
                checkDraw(pos);
                if (eventDatas != null && eventDatas.size() != 0) {
                    pagerAdapter = new ResultPagerAdapter(fragmentManager, eventDatas.get(pos));
                    pager.setOffscreenPageLimit(eventDatas.get(pos).getDrawData().size());
                    pager.setAdapter(pagerAdapter);
                }
                //   init(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //  init(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        manageHeader();
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(gameTypeNames.size() > 1 ? View.VISIBLE : View.INVISIBLE);
        if (gameTypeNames.size() <= 1)
            headerSpinner.setVisibility(View.GONE);
        super.headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.VISIBLE);
    }

    private List<String> getGameTypeNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < bean.getDrawResultData().getGameData().get(0).getGameTypeData().size(); i++) {
            names.add(bean.getDrawResultData().getGameData().get(0).getGameTypeData().get(i).getGameTypeDisplayName());
        }
        return names;
    }

    class TapGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }

    private void checkDraw(int position) {
        if (eventDatas == null
                || eventDatas.size() == 0
                || eventDatas.get(position).getDrawData() == null
                || eventDatas.get(position).getDrawData().size() == 0) {
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

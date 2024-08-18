package com.skilrock.sportslottery;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.skilrock.bean.SportsBean;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.viewpager.SliderLayout;
import com.viewpager.indicator.TitlePageIndicator;

public class SLActivity extends DrawerBaseActivity {
    private GestureDetector gestureDetector;
    private SliderLayout myPager;
    private SportsBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_description);

        sHeader();
        setDrawerItems();
        bindViewIds();
        bean = (SportsBean) getIntent().getSerializableExtra(
                "bean");

        myPager.setAdapter(new SLPagerFragAdapter(getApplicationContext(),
                getSupportFragmentManager(), bean, getIntent().hasExtra("gameTypeId") ? getIntent().getStringExtra("gameTypeId") : "-1"));

        myPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                gestureDetector.onTouchEvent(arg1);
                return false;
            }
        });
        myPager.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });
        myPager.setPageMargin(30);
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setVisibility(View.GONE);
//        indicator.setTypeface(Config.globalTextFont);
//        indicator.setViewPager(myPager);
//        indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
//        indicator.setOnPageChangeListener(new OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int arg0) {
//                headerSpinner.setSelection(arg0);
//
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
        //indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
        myPager.setPresetTransformer(SliderLayout.Transformer.Tablet);
        gestureDetector = new GestureDetector(this, new TapGesture());
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerText.setText(VariableStorage.GlobalPref.getStringData(this, VariableStorage.GlobalPref.SLE_SER_NAME));

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
        // headerNavigation = (ImageView) view.findViewById(R.id.drawer_image);
        headerImage = (ImageView) view.findViewById(R.id.header_image);
        // headerSpinner = (Spinner) view.findViewById(R.id.spinner);
        headerText = (CustomTextView) view.findViewById(R.id.header_text);
        headerSubText = (CustomTextView) view
                .findViewById(R.id.header_sub_text);
        myPager = (SliderLayout) findViewById(R.id.jazzy_pager);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
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
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VariableStorage.GlobalPref.setBooleanPreferences(this, VariableStorage.GlobalPref.SLE_NOTIFICATION, false);
    }
}

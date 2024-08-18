package com.winlot.wear;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class InstantGame extends FragmentActivity {
    private DisplayMetrics displaymetrics;
    public static int height;
    public static int width;
    private ArrayList<Fragment> fragments;
    private CirclePageIndicator circlePageIndicator;
    public static ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDisplayDetails();
        setContentView(R.layout.instant_view);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.grid_view_pager);
        fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("pos", i);
            IGEGameFragment igeGameFragment = new IGEGameFragment();
            igeGameFragment.setArguments(bundle);
            fragments.add(igeGameFragment);

        }
        pager.setOffscreenPageLimit(5);
        MyPagerAdapter pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);
        circlePageIndicator.setViewPager(pager);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


    }

    private void getDisplayDetails() {
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
    }
}

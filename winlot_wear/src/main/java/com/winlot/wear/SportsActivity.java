package com.winlot.wear;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skilrock.lms.ui.R;

import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class SportsActivity extends FragmentActivity {
    public static ViewPager viewPager;
    private String[] home = new String[]{"BlackBurn\nRobers", "Liverpool", "Hull City", "Crystal Palace"};
    private String[] away = new String[]{"Manchester\nUnited", "Chelsea", "Leeds United", "Derby County"};
    private ArrayList<SportsFragment> fragments;
    private CirclePageIndicator circlePageIndicator;
    public static ImageView done;
    private SLEBean sleBean;
    private boolean isAllSelected;
    private int nonSPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        sleBean = SLEBean.getInstance();
        viewPager = (ViewPager) findViewById(R.id.grid_view_pager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        done = (ImageView) findViewById(R.id.done);
        fragments = new ArrayList<>();
        for (int i = 0; i < home.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putString("home", home[i]);
            bundle.putString("away", away[i]);
            bundle.putInt("pos", i);
            SportsFragment sportsFragment = new SportsFragment();
            sportsFragment.setArguments(bundle);
            fragments.add(sportsFragment);
        }
        viewPager.setOffscreenPageLimit(4);
        MyPagerAdapter pageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        circlePageIndicator.setViewPager(viewPager);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (sleBean.getCount() >= 4) {
//                    done.setVisibility(View.VISIBLE);
//                } else {
//                    done.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Ticket purchased", Toast.LENGTH_SHORT).show();
                sleBean.clear();
                finish();
            }
        });
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

    @Override
    public void finish() {
        sleBean.clear();
        super.finish();
    }
}

package com.skilrock.sportslottery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skilrock.bean.SportsBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stpl on 10/1/2015.
 */
public class PurchasePagerAdapter extends FragmentStatePagerAdapter {

    private SportsBean.GameTypeData gameTypeData;
    private double maxLine;
    private Map<Integer, Fragment> map;


    public PurchasePagerAdapter(FragmentManager fm, SportsBean.GameTypeData gameTypeData, double maxLine/*, JazzyViewPager pager*/) {
        super(fm);
        this.gameTypeData = gameTypeData;
        this.maxLine = maxLine;
        map = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = SportsLotteryPurchaseFragment.getInstance(gameTypeData, maxLine, position);
        map.put(position, fragment);
        return fragment;
    }



    @Override
    public int getCount() {
        return gameTypeData.drawData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] time = gameTypeData.getDrawData().get(position).getDrawDateTime().split(" ");
        return time[0];
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        mJazzy.setObjectForPosition(obj, position);
//        return obj;
//    }

    public Fragment getFragment(int position) {
        return map.get(position);
    }
}

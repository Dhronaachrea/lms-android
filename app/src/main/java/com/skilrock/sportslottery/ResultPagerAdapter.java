package com.skilrock.sportslottery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skilrock.lms.ui.R;

import java.util.HashMap;
import java.util.Map;

public class ResultPagerAdapter extends FragmentStatePagerAdapter {


    private SportsLotteryCheckResultBean.GameTypeData gameTypeData;
    private int layoutRes;
    private String[] eventType;
    private Map<Integer, Fragment> fragments;

    public ResultPagerAdapter(FragmentManager fm, SportsLotteryCheckResultBean.GameTypeData gameTypeData/*, JazzyViewPager pager*/) {
        super(fm);
        fragments = new HashMap<>();
        this.gameTypeData = gameTypeData;
        eventType = gameTypeData.getEventType().replace("[", "").replace("]", "").split(",");
        if (eventType.length == 5)
            layoutRes = R.layout.sports_result_row_item_five;
        else if (eventType.length == 3)
            layoutRes = R.layout.sports_result_row_item_three;
        else {

        }
    }

    @Override
    public Fragment getItem(int position) {
        if(!fragments.containsKey(position))
            fragments.put(position, new SportLotteryResultFragment(gameTypeData.getDrawData().get(position), layoutRes, eventType));
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
      //  String date = GlobalVariables.formatDateFromCal(mJazzy.getContext(), gameTypeData.getgetDrawDateTime().split(" ")[0], "yyyy MMM dd");
//        return drawDatas.get(position).getDrawDisplayString();
        String[] header = gameTypeData.getDrawData().get(position).getDrawDateTime().split(" ");
        return header[0];/* + ":" + header[1];*/

        //return gameTypeData.getGameTypeDisplayName();
    }

    @Override
    public int getCount() {
        return gameTypeData.getDrawData().size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        mJazzy.setObjectForPosition(obj, position);
//        return obj;
//    }
}

package com.skilrock.sportslottery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MatchPagerAdapter extends FragmentStatePagerAdapter {


    private List<SportsLotteryMatchBean.DrawData> drawDatas;

    public MatchPagerAdapter(FragmentManager fm, List<SportsLotteryMatchBean.DrawData> eventDatas/*, JazzyViewPager pager*/) {
        super(fm);
        this.drawDatas = eventDatas;
    }



    @Override
    public Fragment getItem(int position) {
        return new SportlotteryMatchFragment(drawDatas.get(position).getEventData());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
//        return drawDatas.get(position).getDrawDisplayString();
        String[] header = drawDatas.get(position).getDrawDateTime().split(" ");
        return header[0];/* + ":" + header[1];*/
    }

    @Override
    public int getCount() {
        return drawDatas.size();
    }

    public void setData(List<SportsLotteryMatchBean.DrawData> eventDatas) {
        this.drawDatas = eventDatas;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        mJazzy.setObjectForPosition(obj, position);
//        return obj;
//    }
}

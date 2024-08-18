package com.skilrock.sportslottery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skilrock.bean.SportsBean;

public class SLPagerFragAdapter extends FragmentStatePagerAdapter {
//    private JazzyViewPager mJazzy;
    protected Context context;
    private SLFragment fragment;
    private Bundle bundle;

    public SLPagerFragAdapter(Context context, FragmentManager fm, /*JazzyViewPager mJazzy,*/ SportsBean bean, String gameTypeId) {
        super(fm);
        this.context = context;
//        this.mJazzy = mJazzy;
        this.bundle = new Bundle();
        this.bundle.putSerializable("bean", bean);
        try {
            this.bundle.putInt(SportsLotteryActivity.GAME_TYPE_ID, Integer.parseInt(gameTypeId));
        } catch (Exception e) {
            this.bundle.putInt(SportsLotteryActivity.GAME_TYPE_ID, -1);
        }
        fragment = new SLFragment();
        fragment.setArguments(bundle);
    }

    @Override
    public Fragment getItem(int pos) {
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        mJazzy.setObjectForPosition(obj, position);
//        return obj;
//    }

    // @Override
    // public Object instantiateItem(View container, int position) {
    // LayoutInflater inflater = (LayoutInflater) container.getContext()
    // .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //
    // View page = inflater.inflate(R.layout.common_frag_game, null);
    // GridView gridView = (GridView) page.findViewById(R.id.item_grid);
    // gridView.setOnItemClickListener(new OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view,
    // int position, long id) {
    // Toast.makeText(context, "Grid ciclced " + position, 1000)
    // .show();
    //
    // }
    // });
    // page.setOnClickListener(new OnClickListener() {
    // public void onClick(View v) {
    // Toast.makeText(context, "Grid ciclced", 1000).show();
    // }
    // });
    //
    // ((ViewPager) container).addView(page, 0);
    // return page;
    // }

}

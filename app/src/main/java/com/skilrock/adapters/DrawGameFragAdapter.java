package com.skilrock.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class DrawGameFragAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments;
    private String[] titles;
    protected Context context;

    public DrawGameFragAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
//        this.mJazzy = mJazzy;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragments.size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        if (mJazzy != null)
//            mJazzy.setObjectForPosition(obj, position);
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

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titles[position];
    }

}

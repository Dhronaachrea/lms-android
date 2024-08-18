package com.skilrock.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by stpl on 9/22/2015.
 */
public class DepositeGateWayAdapter extends FragmentStatePagerAdapter {

    private final String[] tabTitles;
    Context context;
    FragmentManager fragmentManager;
    ArrayList<Fragment> fragments;
    ViewPager viewPager;


    public DepositeGateWayAdapter(Context context, FragmentManager fragmentManager, ArrayList<Fragment> fragments, String[] tabTitles, ViewPager viewPager) {
        super(fragmentManager);
        this.context=context;
        this.tabTitles=tabTitles;
        this.fragmentManager=fragmentManager;
        this.fragments=fragments;
        this.viewPager=viewPager;

    }


    @Override
    public Fragment getItem(int position) {


       return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Object obj = super.instantiateItem(container, position);
//        viewPager.setObjectForPosition(obj, position);
//        return obj;
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return tabTitles[position];
    }


}

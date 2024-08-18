package com.skilrock.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


public class ResultFragAdapter extends FragmentStatePagerAdapter {
	private ArrayList<Fragment> fragments;
	private String[] titles;

	public ResultFragAdapter(FragmentManager fm, ArrayList<Fragment> fragments,
			String[] titles/*, JazzyViewPager mJazzy*/) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		Object obj = super.instantiateItem(container, position);
//		mJazzy.setObjectForPosition(obj, position);
//		return obj;
//	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}


}

package com.skilrock.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.tabbar.IconPagerAdapter;

public class GamesOptionPagerAdapter extends PagerAdapter implements
		IconPagerAdapter {
	private Context context;
	private int[] images;
	private String[] titles;

	public GamesOptionPagerAdapter(Context context, int[] images,
			String[] titles) {
		super();
		this.context = context;
		this.images = images;
		this.titles = titles;
	}

	@Override
	public int getIconResId(int index) {
		// TODO Auto-generated method stub
		return images[index];
	}

	@Override
	public String getTitle(int index) {
		// TODO Auto-generated method stub
		return titles[index];
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		((ViewPager) container).removeView((View) object);
	}

}

package com.tabbar;

public interface IconPagerAdapter {
	/**
	 * Get icon representing the page at {@code index} in the adapter.
	 */
	int getIconResId(int index);

	String getTitle(int index);

	// From PagerAdapter
	int getCount();
}
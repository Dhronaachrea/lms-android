package com.tabbar;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class IconPageIndicator extends HorizontalScrollView implements
		PageIndicator {
	private final IcsLinearLayout mIconsLayout;

	private ViewPager mViewPager;
	private OnPageChangeListener mListener;
	private Runnable mIconSelector;
	private int mSelectedIndex;

	public IconPageIndicator(Context context) {
		this(context, null);
	}

	public IconPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);

		mIconsLayout = new IcsLinearLayout(context,
				R.attr.vpiIconPageIndicatorStyle);
		mIconsLayout.setOrientation(LinearLayout.VERTICAL);
		addView(mIconsLayout, new LayoutParams(WRAP_CONTENT, FILL_PARENT,
				Gravity.CENTER));
	}

	private void animateToIcon(final int position) {
		final View iconView = mIconsLayout.getChildAt(position);
		if (mIconSelector != null) {
			removeCallbacks(mIconSelector);
		}
		mIconSelector = new Runnable() {
			public void run() {
				final int scrollPos = iconView.getLeft()
						- (getWidth() - iconView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mIconSelector = null;
			}
		};
		post(mIconSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mIconSelector != null) {
			// Re-post the selector we saved
			post(mIconSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mIconSelector != null) {
			removeCallbacks(mIconSelector);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mIconsLayout.removeAllViews();
		IconPagerAdapter iconAdapter = (IconPagerAdapter) mViewPager
				.getAdapter();
		int count = iconAdapter.getCount();
		for (int i = 0; i < count; i++) {
			CustomTextView view = new CustomTextView(getContext(), null,
					R.attr.vpiIconPageIndicatorStyle);
			view.setCompoundDrawables(null,
					getResources().getDrawable(iconAdapter.getIconResId(i)),
					null, null);
			view.setTextColor(Color.RED);
			view.setText(iconAdapter.getTitle(i));
			mIconsLayout.addView(view);
		}
		if (mSelectedIndex > count) {
			mSelectedIndex = count - 1;
		}
		setCurrentItem(mSelectedIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedIndex = item;
		mViewPager.setCurrentItem(item);

		int tabCount = mIconsLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			View child = mIconsLayout.getChildAt(i);
			boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToIcon(item);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}
}
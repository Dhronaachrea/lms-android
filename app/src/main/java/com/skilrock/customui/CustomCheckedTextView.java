package com.skilrock.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.skilrock.config.Config;

public class CustomCheckedTextView extends CheckedTextView {

	public CustomCheckedTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomCheckedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomCheckedTextView(Context context) {
		super(context);
		init();
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
	}

	private void init() {

		setTypeface(Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-BOLD.TTF"));
	}
}
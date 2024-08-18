package com.skilrock.customui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.skilrock.config.Config;

public class CustomTextViewTop extends TextView {
	private Context context;

	public enum TextStyles {
		THIN, LIGHT, MEDIUM, BOLD
	};

	public CustomTextViewTop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public CustomTextViewTop(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public CustomTextViewTop(Context context) {
		super(context);
		this.context = context;
		init();
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
	}

	private void init() {
		setTypeface(Config.globalTextFont);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int yOffset = getHeight() - getBaseline();
		canvas.translate(0, -yOffset);
		super.onDraw(canvas);
	}

	public void setTextStyle(TextStyles textStyles) {
		switch (textStyles) {
		case THIN:
			setTypeface(Typeface.createFromAsset(context.getAssets(),
					"ROBOTO-THIN.TTF"));
			break;

		case LIGHT:
			setTypeface(Typeface.createFromAsset(context.getAssets(),
					"ROBOTO-LIGHT.TTF"));
			break;

		case MEDIUM:
			setTypeface(Typeface.createFromAsset(context.getAssets(),
					"ROBOTO-MEDIUM.TTF"));
			break;

		case BOLD:
			setTypeface(Typeface.createFromAsset(context.getAssets(),
					"ROBOTO-BOLD.TTF"));
			break;
		}
	}
}
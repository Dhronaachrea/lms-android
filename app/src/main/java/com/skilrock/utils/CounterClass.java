package com.skilrock.utils;

import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.skilrock.customui.CustomTextView;

public class CounterClass extends CountDownTimer {

	CustomTextView counterTextView;

	public CounterClass(CustomTextView counterTextView, long millisInFuture,
			long countDownInterval) {
		super(millisInFuture, countDownInterval);
		this.counterTextView = counterTextView;
	}

	@Override
	public void onFinish() {

		SpannableString string = new SpannableString("00:00:00");
		string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
		string.setSpan(new RelativeSizeSpan(1f), 6, 8, 0);
		counterTextView.setText(string);
		counterTextView.setTextColor(Color.RED);

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onTick(long millisUntilFinished) {

		long millis = millisUntilFinished;
		String hms = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

		SpannableString string = new SpannableString(hms);
		string.setSpan(new RelativeSizeSpan(1.5f), 0, 5, 0);
		string.setSpan(new RelativeSizeSpan(1f), 6, 8, 0);

		counterTextView.setText(string);

	}
}
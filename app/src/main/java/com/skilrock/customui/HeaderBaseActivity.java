package com.skilrock.customui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.skilrock.lms.ui.R;

public class HeaderBaseActivity extends Activity {
	private CustomTextView header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pre_header);
	}

	protected void setHeaderText(int headerId) {
		if (header == null) {
			header = (CustomTextView) findViewById(R.id.header_text);
		}
		if (header != null) {
			header.setText(headerId);
		}
	}

}

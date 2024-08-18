package com.skilrock.lms.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.skilrock.config.UserInfo;
import com.skilrock.customui.CustomTextView;

public class FakeLogin extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		findViewById(R.id.forgotpassOk).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((CustomTextView) findViewById(R.id.forgotpass_number))
								.getText().toString().equalsIgnoreCase("")) {
							//UserInfo.setLogout(getApplicationContext());
							com.skilrock.utils.DataSource.Login.username = "GUEST";
							finish();
						} else {
							UserInfo.setLogin(getApplicationContext());
							com.skilrock.utils.DataSource.Login.username = ((CustomTextView) findViewById(R.id.forgotpass_number))
									.getText().toString();
							finish();
						}
					}
				});
	}
}

package com.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.lms.ui.Splash;

import java.util.List;

public class GCMDialogActivity extends Activity {
    private String title, details, isClick;
    private CustomTextView ok, cancel;
    private Analytics analytics;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.GCMDIALOG);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        // WindowManager.LayoutParams params = getWindow().getAttributes();
        // params.x = -20;
        // params.height = 100;
        // params.width = 550;
        // params.y = -10;
        // this.getWindow().setAttributes(params);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(false);
        }
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        setContentView(R.layout.gcm_dialog);
        title = getIntent().getStringExtra("title");
        details = getIntent().getStringExtra("message");
        isClick = getIntent().getStringExtra("isClick");
        ((CustomTextView) findViewById(R.id.title)).setText(title);
        ((CustomTextView) findViewById(R.id.details)).setText(details);
        ok = (CustomTextView) findViewById(R.id.deposit);
        cancel = (CustomTextView) findViewById(R.id.cancel);
        if (isClick.equalsIgnoreCase("true")) {
            ok.setText("Buy Now");
        } else {
            cancel.setVisibility(View.GONE);
        }
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ok.setOnClickListener(new OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      finish();
                                      analytics.sendAction(Fields.Category.GCMDIALOG, Fields.Action.CLICK);

                                      if (isClick.equalsIgnoreCase("true")) {
                                          Intent intent = new Intent(getApplicationContext(), Splash.class);
                                          intent.putExtra("isSLE", true);
                                          intent.putExtra("gameTypeId",getIntent().getStringExtra("gameTypeId"));
                                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                          startActivity(intent);
                                      }
                                      // Communication.notificationCount = -1;
                                      // Communication.setBadge(getApplicationContext(), 0);
                                      // Communication.sendToSony(getApplicationContext(),
                                      // false, ++Communication.notificationCount);
                                  }
                              }

        );
    }

}

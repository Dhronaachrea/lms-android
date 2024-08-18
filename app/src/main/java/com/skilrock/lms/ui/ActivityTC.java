package com.skilrock.lms.ui;

import android.app.Dialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.utils.Utils;

public class ActivityTC extends HeaderBaseActivity implements View.OnClickListener, WebServicesListener {

    private WebView web;
    private TextView header;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc);
        findIds();
        header.setText("Terms & Conditions");
        String url = "/com/skilrock/pms/mobile/playerInfo/Action/termsAndConditions.action?deviceType=android";

        new PMSWebTask(this, url, "", null, "TERM", null, "").execute();
//        web.loadUrl(Config.baseURL + url);
        web.getSettings().setDomStorageEnabled(true);

        Utils.logPrint(url);
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Utils.logPrint("ssl error" + error.toString());
                handler.proceed();
            }
        });
        close.setOnClickListener(this);
    }

    private void findIds() {
        web = (WebView) findViewById(R.id.web_tc);
        header = (TextView) findViewById(R.id.header_name);
        findViewById(R.id.close).setVisibility(View.INVISIBLE);
        close = (ImageView) findViewById(R.id.done);
        close.setImageResource(R.drawable.close);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                finish();
                break;
        }
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (methodType.equalsIgnoreCase("TERM")) {
            web.loadData(resultData.toString(), "text/html", "utf-8");
        }
        dialog.dismiss();
    }
}

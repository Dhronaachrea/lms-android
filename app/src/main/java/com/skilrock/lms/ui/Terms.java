package com.skilrock.lms.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.customui.SkilrockProgressDialog;
import com.skilrock.lms.communication.Communication;

public class Terms extends HeaderBaseActivity {
    // private CustomTextView username;
    // private CustomTextView balance;
    private CustomTextView header;
    private WebView faqWVWebView;
    private SkilrockProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.terms_activity);
        setHeaderText(R.string.terms_header);
        // WindowManager.LayoutParams params = getWindow().getAttributes();
        // params.x = -20;
        // params.height = 100;
        // params.width = 550;
        // params.y = -10;
        // this.getWindow().setAttributes(params);
        // dialog = new ProgressDialog(Terms.this);
        // dialog.setCancelable(false);
        // dialog.setMessage("Wait...");

        dialog = new SkilrockProgressDialog(Terms.this, "", getResources()
                .getString(R.string.please_wait), false, false);
        dialog.show();
        faqWVWebView = (WebView) findViewById(R.id.web_view);
        // username = (CustomTextView) findViewById(R.id.player_name);
        // balance = (CustomTextView) findViewById(R.id.player_balance);
        header = (CustomTextView) findViewById(R.id.header_name);
        faqWVWebView.loadUrl(Communication.getTermsAndCondition());

        faqWVWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        // username.setText(DataSource.Login.username);
        // balance.setText("$" + DataSource.Login.currentBalance);
        header.setText(getResources().getString(R.string.terms_header));
        // if (!DataSource.Login.isSessionValid)
        // FAQActivity.this.finish();
    }

}

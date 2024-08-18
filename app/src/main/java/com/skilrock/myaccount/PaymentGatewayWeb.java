package com.skilrock.myaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Spinner;

import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;

public class PaymentGatewayWeb extends Activity {
    private WebView pagaWebView;
    private String amount;
    private ImageView drawerImage, headerImage;
    private Spinner spinner;
    private CustomTextView headerText, subHeaderText;
    private ProgressDialog loading;

    private boolean paymentComplete = false;
    private String paymentResponse = null;
    private String redirectUrl;
    public int onStartCount = 0;
    private GlobalPref globalPref;


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway_web);
        globalPref = GlobalPref.getInstance(this);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }

        if (globalPref.getCountry().equalsIgnoreCase("ghana")) {
            findViewById(R.id.title).setVisibility(View.VISIBLE);
        }


        // counter = 0;
        amount = getIntent().getExtras().getString("amount");
        pagaWebView = (WebView) findViewById(R.id.webview_paga);
        drawerImage = (ImageView) findViewById(R.id.drawer_image);
        headerImage = (ImageView) findViewById(R.id.header_image);
        headerText = (CustomTextView) findViewById(R.id.header_text);
        subHeaderText = (CustomTextView) findViewById(R.id.header_sub_text);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        drawerImage.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        subHeaderText.setVisibility(View.VISIBLE);
        headerText.setText(getString(R.string.deposit_my_profile));
        headerText.setTypeface(null, Typeface.BOLD);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentGatewayWeb.this.finish();
            }
        });
        subHeaderText.setText(getIntent().getStringExtra("gatewayName"));
        loading = ProgressDialog.show(PaymentGatewayWeb.this, "", "Please Wait...", false, false);
        redirectUrl = getIntent().getStringExtra("redirectUrl");
        WebSettings webSettings = pagaWebView.getSettings();
        pagaWebView.getSettings().setJavaScriptEnabled(true);
        pagaWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        pagaWebView.getSettings().setSupportZoom(true);
        pagaWebView.getSettings().setBuiltInZoomControls(true);

        pagaWebView.setWebViewClient(new MyClient());
        pagaWebView.setWebChromeClient(new MyChromeClient());
        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(tp);
        }
        pagaWebView.loadUrl(redirectUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }
    }

    @SuppressLint("JavascriptInterface")
    class MyClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("mpowerPayConfirmation.action")) { // NON-NLS
                // DO SOMETHING
                view.setOnClickListener(null);
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });

            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            try {
                loading.show();
                Utils.logPrint("onPageStarted");
            } catch (Exception e) {
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            // super.onReceivedSslError(view, handler, error);
            Utils.logPrint("onReceivedSslError");
            handler.proceed();
            // shouldOverrideUrlLoading(view, URL);
        }

        // @Override
        // public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // view.loadUrl(url);
        // Log.i("shouldOverrideUrlLoading", "shouldOverrideUrlLoading");
        // return super.shouldOverrideUrlLoading(view, url);
        // }

        // @Override
        // public void onReceivedError(WebView view, int errorCode,
        // String description, String failingUrl) {
        // Toast.makeText(getApplicationContext(), description, 1000).show();
        // super.onReceivedError(view, errorCode, description, failingUrl);
        // }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                loading.dismiss();
                if (globalPref.getCountry().equalsIgnoreCase("lagos")) {
                    if (url.contains("com/skilrock/pms/interSwitch/Action/payConfirmationAndroid.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);
                        pagaWebView.setVisibility(View.INVISIBLE);
                    }
                    if (url.contains("payRequestCancelAndroid.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);

                    }
                    if (url.contains("pagaPayConfirmation.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);
                        pagaWebView.setVisibility(View.INVISIBLE);
                    }

                } else {
                    if (url.contains("PayConfirmation.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);
                        pagaWebView.setVisibility(View.INVISIBLE);
                    }
                    if (url.contains("PayRequestCancel.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);
                        pagaWebView.setVisibility(View.INVISIBLE);
                    }
                    if (url.contains("payNowPayConfirmationAndroid.action")) {
                        paymentComplete = true;
                        pagaWebView.addJavascriptInterface(new JIFace(), "HtmlViewer");
                        String ht = "javascript:window.HtmlViewer.print(document.getElementsByTagName('body')[0].innerHTML);";
                        pagaWebView.loadUrl(ht);
                        pagaWebView.setVisibility(View.INVISIBLE);
                    }
                }

            } catch (Exception e) {

            }


        }
    }

    class MyChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            setProgress(newProgress * 100);
        }

        private JsResult mResult;

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            mResult = result;

            try {
                mResult.confirm();
//                AlertDialog dialog = new AlertDialog.Builder(PaymentGatewayWeb.this)
//                        .setTitle("Deposit")
//                        .setMessage("Do you want to cancel transaction ?")
//                        .setOnCancelListener(new CancelListener())
//                        .setNegativeButton("Yes", new CancelListener())
//                        .setPositiveButton("No", new PositiveListener())
//                        .create();
//                dialog.getWindow().setType(
//                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                dialog.show();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();

            }
            return true;
        }

        private class CancelListener implements
                DialogInterface.OnCancelListener,
                DialogInterface.OnClickListener {

            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mResult.cancel();
            }

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mResult.cancel();
            }
        }

        private class PositiveListener implements
                DialogInterface.OnClickListener {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mResult.confirm();
            }
        }
    }

    class JIFace {
        @JavascriptInterface
        public void print(String data) {
            paymentResponse = data;
            if (paymentComplete) {
                Utils.consolePrint("data  " + data);
                Intent intent = getIntent();
                intent.putExtra("paymentResponse", paymentResponse);
                setResult(RESULT_OK, intent);
                PaymentGatewayWeb.this.finish();
            }
        }

        public void checkSession(String string) {
            PaymentGatewayWeb.this.finish();
        }
    }

}

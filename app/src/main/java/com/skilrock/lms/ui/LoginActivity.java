package com.skilrock.lms.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.HeaderBaseActivity;
import com.skilrock.customui.InputDialogBox;
import com.skilrock.customui.SkilrockProgressDialog;
import com.skilrock.lms.communication.Communication;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WeaverWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.MyPreferences;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends HeaderBaseActivity implements
        WebServicesListener {
    MyPreferences myPreferences;
    private String regId = "";
    private Button login;
    private Button register;
    private EditText usernameET;
    private EditText password;
    // private CustomTextView balance;
    // private CustomTextView header;
    private CheckBox checkBox;
    private SkilrockProgressDialog progressDialog;
    private CustomTextView forgotpassword;
    private ProgressDialog pDialog;
    private SharedPreferences preferences;
    public boolean downloadComplete;
    private LinearLayout bannerView;
    // private BannerView mBanner;
    private boolean isShowGameList = true;
    private String bindingId;
    private double version;
    private Resources resources;
    // private CustomTextView registerView;
    private LinearLayout oTPButton;

    public static final String PREFS_NAME = "com.skilrock.lms.settings";
    private String userName;
    private ProgressDialog dialog;
    private Context context;
    private GoogleCloudMessaging gcmObj;
    private JSONObject loginData;
    private Analytics analytics;
    private DownloadDialogBox dbBox;
    private boolean isOkClick = false;

    //for pseudo Name
    private InputDialogBox inputDialgoBox = null;
    //new implemention for without google playservices
    private DownloadDialogBox dBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.LOGIN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        context = LoginActivity.this;
//        Config.currencySymbol = "$";
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        // WindowManager.LayoutParams params = getWindow().getAttributes();

        // params.x = -20;
        // params.height = displaymetrics.heightPixels
        // - GlobalVariables.getPx(20, getApplicationContext());
        // params.width = displaymetrics.widthPixels
        // - GlobalVariables.getPx(20, getApplicationContext());
        // params.y = -10;

        // this.getWindow().setAttributes(params);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            this.setFinishOnTouchOutside(true);

        // setHeaderText(R.string.login_header);
        boolean b = DataSource.Login.isSessionValid;
        resources = getResources();
        bindIds();
        // updateUIThemes();
        forgotpassword.setOnClickListener(listener);
        login.setOnClickListener(listener);
        register.setOnClickListener(listener);
        try {
            if (Communication.OTPRequest.equals("No")
                    || Communication.OTPRequest.equalsIgnoreCase("No")) {
                oTPButton.setVisibility(LinearLayout.GONE);
            } else {
                oTPButton.setVisibility(LinearLayout.VISIBLE);
                oTPButton.setOnClickListener(listener);
            }
        } catch (Exception e) {
            oTPButton.setVisibility(LinearLayout.GONE);

        }
        // delete these two lines when app is ready
        oTPButton.setVisibility(LinearLayout.VISIBLE);
        oTPButton.setOnClickListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataSource.Login.username = "";
        // Getting stored check and user_name
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        boolean checked = settings.getBoolean("checked", false);
        String user_name = settings.getString("user_name", "");
        bindingId = settings.getString("bindingId", "NA");
       /* checkBox.setChecked(checked);
        if (checked) {
            password.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(password, 0);
                }
            }, 200);
        } else {
            usernameET.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(usernameET, 0);
                }
            }, 200);
        }*/
        // balance.setText("Welcome Player");
        // header.setText("Login");
        usernameET.setText(user_name);
        password.setText("");
    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(1000) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.login:
                    callLogin();
                    break;

                case R.id.register:
                    Intent register = new Intent(LoginActivity.this,
                            RegisterActivity.class);
                    startActivity(register);
                    break;
                case R.id.forgotpassword:
                    Intent forgotpassword = new Intent(LoginActivity.this,
                            ForgotPassActivity.class);
                    startActivity(forgotpassword);
                    break;
                case R.id.oTPButtonLogin:
                    Intent mainActivity = new Intent(LoginActivity.this,
                            MainScreen.class);
                    startActivity(mainActivity);
                    break;
            }
        }
    };

    private void callLogin() {
        if (usernameET.getText().toString().trim().length() > 0
                && password.getText().toString().trim().length() > 0) {
            if (GlobalVariables.connectivityExists(context)) {
                if (usernameET.getText().toString().trim()
                        .equalsIgnoreCase("rajnikanth")
                        && password.getText().toString().trim()
                        .equalsIgnoreCase("12345678") && Config.isDebug) {
                    DownloadDialogBox dialogBox = new DownloadDialogBox(LoginActivity.this,
                            resources.getString(R.string.ver)
                                    + Communication.getAvailableVersion(this)
                                    + "\n" + Config.getInstance().getBaseURL() + "\nPackage Name : " + BuildConfig.APPLICATION_ID
                                    + "\nApp Release Date: " + Config.dateRelease, "", true,
                            true, null, null);
                    dialogBox.setIsEdittextVisible(true);
                    dialogBox.show();
                } else {
                    SharedPreferences prefs = getSharedPreferences(
                            PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = prefs
                            .edit();
                    prefsEditor.putString("user_name", usernameET
                            .getText().toString().trim());
                           /* if (checkBox.isChecked()) {
                                        .getText().toString().trim());
                            } else {
                                prefsEditor.putBoolean("checked", false);
                                prefsEditor.putString("user_name", "");
                            }*/
                    analytics.sendAction(Fields.Category.LOGIN, Fields.Action.CLICK);
                    prefsEditor.commit();
                    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
                    if (status == ConnectionResult.SUCCESS)
                        registerInBackground(true);
                    else {
                        updateGoogleplay();
                    }
                }
            } else {
                GlobalVariables.showDataAlert(LoginActivity.this);
            }
        } else
            Utils.Toast(LoginActivity.this,
                    resources.getString(R.string.un_pwd_validation)
            );
    }

    private void registerInBackground(boolean isOkClick) {
        new AsyncTask<Boolean, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected String doInBackground(Boolean... params) {
                String msg = "";
                try {
                    if (params[0]) {
                        if (gcmObj == null) {
                            gcmObj = GoogleCloudMessaging
                                    .getInstance(context);
                        }
                        regId = gcmObj.register(new GlobalVariables().getProjectId(LoginActivity.this));
                    } else {
                        regId = "";
                    }
                    VariableStorage.UserPref.setStringPreferences(LoginActivity.this, VariableStorage.UserPref.GCM_ID, regId);
                    msg = "Registration ID :" + regId;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                dialog.dismiss();
                if (GlobalVariables.connectivityExists(context)) {
                    if (!Config.isWearer) {
                        String path = "/com/skilrock/pms/mobile/loginMgmt/Action/playerLogin.action?";
                        String params = "userName=" + usernameET.getText().toString().trim() + "&password=" + password.getText().toString().trim()
                                + "&bindingId=" + bindingId + "&deviceName=" + DataSource.deviceName
                                + "&deviceType=" + DataSource.deviceType + "&currentVersion=" + getVersion()
                                + "&lang=" + DataSource.selectedLang + "&gcmId=" + regId
                                + "&deviceRes=" + getDeviceResolution(context) + "&deviceOSVer=" + getDeviceOSVersion()
                                + "&deviceModel=" + getDeviceManufacturerName();
                        new PMSWebTask(LoginActivity.this, path + params, "", null, "LOGIN", null, "").execute();
                    } else {
                        String path = "/Weaver/service/rest/playerLogin";
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("userName", usernameET.getText().toString().trim());
                            jsonObject.put("password", Utils.getMD5(Utils.getMD5(password.getText().toString().trim()) + Config.loginToken));
                            jsonObject.put("loginToken", Config.loginToken);
                            jsonObject.put("domainName", Config.domain_name);
                            jsonObject.put("requestIp", GlobalVariables.getIPAddress(true));
                            jsonObject.put("deviceType", Config.device_type_wearer);
                            jsonObject.put("appType", Config.appType);
                            jsonObject.put("userAgent", new WebView(LoginActivity.this).getSettings().getUserAgentString());
                            jsonObject.put("deviceId", regId.equals("") ? VariableStorage.UserPref.getStringData(LoginActivity.this, VariableStorage.UserPref.GCM_ID) : regId);
                            jsonObject.put("currAppVer", "2.1.7");
                            new WeaverWebTask(LoginActivity.this, path, "N/A", jsonObject, "LOGIN", null, "").execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    GlobalVariables.showDataAlert(context);
                }
            }
        }.execute(isOkClick, null, null);
    }

    public double getVersion() {
        double currentVersion = 3.5;
        try {
            String version = getPackageManager().getPackageInfo(
                    getApplicationInfo().packageName, 0).versionName;
            currentVersion = Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentVersion;
    }

    public String getDeviceManufacturerName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.replaceAll("\\s+", "");

        } else {
            return manufacturer.replaceAll("\\s+", "")
                    + model.replaceAll("\\s+", "");
        }
    }

    public String getDeviceResolution(Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return width + "x" + height;
    }

    public String getDeviceOSVersion() {
        String currentapiVersion = Build.VERSION.RELEASE;

        return currentapiVersion;
    }

    private void bindIds() {
        usernameET = (EditText) findViewById(R.id.username);
        ((CustomTextView) findViewById(R.id.header_name))
                .setText(R.string.login_header);
        (findViewById(R.id.close)).setVisibility(View.INVISIBLE);
        ((ImageView) (findViewById(R.id.done))).setImageResource(R.drawable.close);
        (findViewById(R.id.done))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });
        password = (EditText) findViewById(R.id.password);
        password.setTypeface(Typeface.SANS_SERIF);
        // checkBox = (CheckBox) findViewById(R.id.checkBox1);
        forgotpassword = (CustomTextView) findViewById(R.id.forgotpassword);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        bannerView = (LinearLayout) findViewById(R.id.bannerView);
        // mBanner = new BannerView(getApplicationContext());
        oTPButton = (LinearLayout) findViewById(R.id.oTPButtonLogin);

        password.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER || keyEvent.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    callLogin();
                    InputMethodManager imm = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                }
                return false;
            }
        });


    }

    private void updateDeviceId(String bindingId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("bindingId", bindingId);
        prefsEditor.commit();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
    }

    @Override
    public void
    onResult(String methodType, Object resultData, Dialog dialog) {
        switch (methodType) {
            case "LOGIN":
                if (resultData != null) {
                    if (Config.isWearer) {// WEARER LOGIN
                        dialog.dismiss();
                        Utils.consolePrint(resultData.toString());
                        try {
                            loginData = new JSONObject(resultData.toString());
                            if (loginData.getInt("errorCode") == 0) {
                                JSONObject playerLoginInfo = loginData.getJSONObject("playerLoginInfo");
                                analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.SUCCESS);
                                VariableStorage.GlobalPref.setStringPreferences(context, VariableStorage.GlobalPref.LOGIN_DATA, loginData.toString());
                                VariableStorage.UserPref.setStringPreferences(getApplicationContext(),
                                        VariableStorage.UserPref.USER_PIC_URL, playerLoginInfo.getString("commonContentPath") + "/" + playerLoginInfo.getString("avatarPath"));
                                VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_NAME, playerLoginInfo.getString("userName"));
                                VariableStorage.UserPref.setStringPreferences(context,
                                        VariableStorage.UserPref.USER_BAL, playerLoginInfo.getJSONObject("walletBean").getDouble("cashBalance") + "");
                                VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.SESSION_ID, loginData.getString("playerToken"));
                                VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.PLAYER_ID, playerLoginInfo.getString("playerId"));
//                                if (!loginData.getString("bindingId").toString()
//                                        .equalsIgnoreCase("NOT_REQUIRED"))
//                                    updateDeviceId(loginData.getString("bindingId").toString());
                                DataSource.resetVariables();
                                UserInfo.setLogin(context);
                                setResult(RESULT_OK);


                                if (loginData.optString("isPseudoNameReq").equalsIgnoreCase("YES")) {
                                    pseudoNameReqCall();
//                                Intent intent = new Intent(LoginActivity.this,
//                                        PseudoNameActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(
//                                        GlobalVariables.startAmin,
//                                        GlobalVariables.endAmin);
                                } else {
                                    finish();
                                }

                                dialog.dismiss();
                            } else if (loginData.getString("errorCode").equalsIgnoreCase("130")) {
                                OnClickListener okClickListener = new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(LoginActivity.this,
                                                VerificationActivity.class);
                                        intent.putExtra("username", usernameET.getText()
                                                .toString().trim());
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                };
                                dialog.dismiss();
                                new DownloadDialogBox(LoginActivity.this,
                                        resources.getString(R.string.reset_device)
                                        , "", true, true, okClickListener,
                                        null).show();

                            } else {
                                dialog.dismiss();
                                password.setText("");
                                Utils.Toast(LoginActivity.this,
                                        loginData.get("respMsg").toString()
                                );
                            }
                            analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.FAILURE);
                        } catch (Exception e) {
                            dialog.dismiss();
                            GlobalVariables.showServerErr(LoginActivity.this);
                            analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.FAILURE);
                        }
                    } else { // PMS login
                        try {
                            loginData = new JSONObject(resultData.toString());
                            if (loginData.getString("isSuccess").equals("true")) {
                                if (loginData.optString("isPseudoNameReq").equalsIgnoreCase("YES")) {
                                    pseudoNameReqCall();
                                } else {
                                    analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.SUCCESS);
                                    VariableStorage.UserPref.setStringPreferences(getApplicationContext(), VariableStorage.UserPref.USER_PIC_URL, loginData.getString("profilePhoto"));
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_NAME, loginData.getString("userName"));
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_BAL, AmountFormat.getAmountFormatForTwoDecimal(loginData.getString("currentBalance")));
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.SESSION_ID, loginData.getString("sessionId"));
                                    VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.PLAYER_ID, loginData.getString("playerId"));
                                    if (!loginData.getString("bindingId").toString()
                                            .equalsIgnoreCase("NOT_REQUIRED"))
                                        updateDeviceId(loginData.getString("bindingId").toString());
                                    DataSource.resetVariables();
                                    UserInfo.setLogin(context);
                                    setResult(RESULT_OK);
                                    dialog.dismiss();
                                    finish();
                                }
                            } else if (loginData.getString("isSuccess").equals("false")) {
                                if (loginData.getString("errorCode").equalsIgnoreCase("130")) {
                                    OnClickListener okClickListener = new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(LoginActivity.this,
                                                    VerificationActivity.class);
                                            intent.putExtra("username", usernameET.getText()
                                                    .toString().trim());
                                            startActivity(intent);
                                            LoginActivity.this.finish();
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(LoginActivity.this,
                                            resources.getString(R.string.reset_device)
                                            , "", true, true, okClickListener,
                                            null).show();

                                } else {
                                    dialog.dismiss();
                                    password.setText("");
                                    Utils.Toast(LoginActivity.this,
                                            loginData.get("errorMsg").toString()
                                    );
                                }
                                analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.FAILURE);
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            GlobalVariables.showServerErr(LoginActivity.this);
                            analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.FAILURE);
                        }
                    }
                } else {
                    dialog.dismiss();
                    GlobalVariables.showServerErr(LoginActivity.this);
                    analytics.sendAll(Fields.Category.LOGIN, Fields.Action.GET, Fields.Label.FAILURE);
                }
                break;

            case "PseudoName":
                try {
                    dialog.dismiss();
                    if (resultData != null) {
                        JSONObject pseudoData = new JSONObject(resultData.toString());
                        if (pseudoData.optBoolean("isSuccess")) {
                            if (inputDialgoBox != null)
                                inputDialgoBox.dismiss();
                            DownloadDialogBox dbox = null;
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((Activity) context).overridePendingTransition(GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                    finish();
                                }
                            };
                            UserInfo.setLogin(context);
                            dbox = new DownloadDialogBox(context, context.getResources().getString(R.string.successfully_register), "", false, true, okClickListener, null);
                            dbox.show();
                        } else if (!pseudoData.optString("errorCode").equalsIgnoreCase("")) {
                            UserInfo.setLogout(context);
                            Utils.Toast(context, pseudoData.optString("errorMsg"));
                        } else {
                            DownloadDialogBox dbox = null;
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((Activity) context).overridePendingTransition(GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                    finish();
                                }
                            };
                            UserInfo.setLogout(context);
                            dbox = new DownloadDialogBox(context, context.getResources().getString(R.string.unsuccessfully_register), "", false, true, okClickListener, null);
                            dbox.show();
                        }
                    } else {
                        GlobalVariables.showServerErr(LoginActivity.this);
                    }
                } catch (Exception ex) {
                    GlobalVariables.showServerErr(context);
                    ex.printStackTrace();
                }
                break;

            case "LOGOUT_FROM_LOGIN":
                try {
                    loginData = new JSONObject(resultData.toString());
                    dialog.dismiss();
                    if (inputDialgoBox != null)
                        inputDialgoBox.dismiss();
                    if (loginData.optString("isSuccess").equals("true")) {
                        UserInfo.setLogout(getApplicationContext());
                        finish();
                    } else if (loginData.getString("isSuccess").equals("false")) {
                        analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Not Success");
                        DrawerBaseActivity.selectedItemId = -1;
                        if (loginData.getString("errorCode").equalsIgnoreCase("118")) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(getApplicationContext());
                                    Intent intent = new Intent(getApplicationContext(),
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(
                                            GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                    finish();
                                }
                            };
                            new DownloadDialogBox(LoginActivity.this,
                                    loginData.getString("errorMsg"), "", false, true, okClickListener,
                                    null).show();
                        } else if (loginData.getString("errorCode").equalsIgnoreCase("501")) {
                            Utils.Toast(context, getString(R.string.sql_exception));
                        } else {
                            Utils.Toast(context, loginData.getString("errorMsg"));
                        }
                    }
                } catch (Exception e) {
                    DrawerBaseActivity.selectedItemId = -1;
                    GlobalVariables.showServerErr(getApplicationContext());
                }
                break;


        }
    }

    public void updateGoogleplay() {

        OnClickListener okClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                callMarketPlace();
                dbBox.dismiss();
                finish();
            }
        };

        OnClickListener calcelClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dbBox.dismiss();
                registerInBackground(false);
            }
        };

        dbBox = new DownloadDialogBox(this, "This application wants to update your \"Google Play Services\" app, otherwise google play services like push notification,maps etc will not work.", "Update Google Play Services", true, true, okClickListener, calcelClickListener, "OK", "NOT NOW");
        dbBox.show();
    }

    public void callMarketPlace() {
        try {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.google.android.gms")), 1);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.gms")), 1);
        }
    }

    private void pseudoNameReqCall() {
        inputDialgoBox = new InputDialogBox(LoginActivity.this, context.getResources().getString(R.string.pseudoName), context.getResources().getString(R.string.pseudoName), false, false, null, null, null);
        inputDialgoBox.show();
        this.setVisible(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}

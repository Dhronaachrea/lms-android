package com.skilrock.customui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.skilrock.bean.ProfileDetail;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView.TextStyles;
import com.skilrock.lms.communication.DrawerBaseListener;
import com.skilrock.lms.communication.GPSTracker;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WeaverWebTask;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.AppTourActivity;
import com.skilrock.lms.ui.ChangePassActivity;
import com.skilrock.lms.ui.ContactUsActivity;
import com.skilrock.lms.ui.CouponActivity;
import com.skilrock.lms.ui.FAQActivity;
import com.skilrock.lms.ui.InviteFriendActivity;
import com.skilrock.lms.ui.LoginActivity;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.locateretailer.LocateRetailer;
import com.skilrock.myaccount.LottoLeague;
import com.skilrock.myaccount.ProfileFragment;
import com.skilrock.preference.GlobalPref;
import com.skilrock.retailerScan.RetailerTicketScreen;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.CropSquareTransformation;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

public class DrawerBaseActivity extends ActionBarActivity implements DrawerBaseListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    public RelativeLayout drawerView;
    public ActionBar actionBar;
    public boolean isDrawerClosed;
    public static int selectedItemId = -1;

    public CircleImageView userPic;
    public CustomTextView changePwd, userName, userBalance, login, myAccount, inbox, promoCode,
            userSettings, appSettings, inviteFriend, locateRetailer, contactUs, faq, appTour;
    public CustomTextView logout;
    public CustomTextView changePwdLine, loginLine, myAccountLine, inboxLine,
            userSettingsLine, appSettingsLine, inviteFriendLine, faqLine, contactUsLine, appTourLine,
            locateRetailerLine;
    private CustomTextView promoCodeLine;
    public ImageView headerNavigation;
    public ImageView headerImage;
    public Spinner headerSpinner;
    public CustomTextView headerText;
    public CustomTextView headerSubText;
    public LinearLayout headerAnim;

    public ImageView filter, infoIcon;
    public EditText searchBox;
    protected DownloadDialogBox dBox;
    public ImageView locateRetailerScratch;
    private JSONObject loginData;
    private Context context;
    protected LinearLayout headerlay;
    public int onStartCount = 0;
    private AbsListView.LayoutParams params;
    private int newWidth;
    public Analytics analytics;
    //new for lotto league
    private CustomTextView lottoleagueLine, lottoleague;
    //retailer scan activity
    private TextView retailer_scan, retailer_scan_line;

    private GlobalPref globalPref;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        globalPref = GlobalPref.getInstance(this);
        super.setContentView(R.layout.app_base_activity);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.DRAWER_BASE_ACTIVITY);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        newWidth = (4 * width) / 5;
        context = this;
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }
        // sHeader();
        // setDrawerItems();
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

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        managerPrePostLoginViews(UserInfo.isLogin(getApplicationContext()));
        if (UserInfo.isLogin(getApplicationContext())) {
            String imgUrl = VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_PIC_URL);
            if (imgUrl.equals("")) {
                userPic.setImageResource(R.drawable.no_img);
            } else {
                Picasso.with(getApplicationContext()).load(imgUrl).transform(new CropSquareTransformation()).placeholder(R.drawable.no_img).into(userPic);
            }
            userName.setText(VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME));
            userBalance.setText(VariableStorage.GlobalPref.getStringData(getApplicationContext(), VariableStorage.GlobalPref.CURRENCY_CODE) + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_BAL));
        } else {
            userPic.setImageResource(R.drawable.no_img);
            com.skilrock.utils.DataSource.Login.username = "GUEST";
            userName.setText(com.skilrock.utils.DataSource.Login.username);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isDrawerClosed) {
            drawerLayout.closeDrawer(drawerView);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void sHeader() {
        headerlay = (LinearLayout) findViewById(R.id.header_lay);
        headerAnim = (LinearLayout) findViewById(R.id.anim_header);
        headerNavigation = (ImageView) findViewById(R.id.drawer_image);
        headerNavigation.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        headerImage = (ImageView) findViewById(R.id.header_image);
        headerSpinner = (Spinner) findViewById(R.id.spinner);
        headerSpinner.getBackground().setColorFilter(getResources().getColor(R.color.spinner_bg_color), Mode.SRC_IN);
        headerText = (CustomTextView) findViewById(R.id.header_text);
        headerText.setTextStyle(TextStyles.BOLD);
        headerSubText = (CustomTextView) findViewById(R.id.header_sub_text);
        locateRetailerScratch = (ImageView) findViewById(R.id.filter_ret);
    }

    public void setLRHeader() {
        headerImage = (ImageView) findViewById(R.id.header_image);
        headerText = (CustomTextView) findViewById(R.id.header_text);
        headerText.setTextStyle(TextStyles.BOLD);
        headerSubText = (CustomTextView) findViewById(R.id.header_sub_text);
        headerNavigation = (ImageView) findViewById(R.id.drawer_image);
        filter = (ImageView) findViewById(R.id.filter_ret);
        infoIcon = (ImageView) findViewById(R.id.info);
        searchBox = (EditText) findViewById(R.id.search_box);
    }

    public void setDrawerItems() {
        changePwd = (CustomTextView) findViewById(R.id.change_pwd);
        userName = (CustomTextView) findViewById(R.id.user_name);
        userPic = (CircleImageView) findViewById(R.id.user_pic);
        userBalance = (CustomTextView) findViewById(R.id.user_bal);
        login = (CustomTextView) findViewById(R.id.login);
        promoCode = (CustomTextView) findViewById(R.id.promoCode);
        myAccount = (CustomTextView) findViewById(R.id.my_account);
        inbox = (CustomTextView) findViewById(R.id.inbox);
        userSettings = (CustomTextView) findViewById(R.id.user_settings);
        appSettings = (CustomTextView) findViewById(R.id.app_settings);
        inviteFriend = (CustomTextView) findViewById(R.id.invite_a_friend);
        locateRetailer = (CustomTextView) findViewById(R.id.locate_ret);
        faq = (CustomTextView) findViewById(R.id.faq);
        contactUs = (CustomTextView) findViewById(R.id.contact_us);
        appTour = (CustomTextView) findViewById(R.id.app_tour);
        logout = (CustomTextView) findViewById(R.id.logout);

        //new Implementation for league
        lottoleague = (CustomTextView) findViewById(R.id.lottoleague);
        lottoleagueLine = (CustomTextView) findViewById(R.id.lottoleague_line);

        //implementaion of retailer screen
        retailer_scan = (CustomTextView) findViewById(R.id.retailer_scan);
        retailer_scan_line = (CustomTextView) findViewById(R.id.retailer_scan_line);

        userName.setOnClickListener(commonClickListener);
        changePwd.setOnClickListener(commonClickListener);
        userPic.setOnClickListener(commonClickListener);
        login.setOnClickListener(commonClickListener);
        userBalance.setOnClickListener(commonClickListener);
        myAccount.setOnClickListener(commonClickListener);
        inbox.setOnClickListener(commonClickListener);
        userSettings.setOnClickListener(commonClickListener);
        appSettings.setOnClickListener(commonClickListener);
        inviteFriend.setOnClickListener(commonClickListener);
        locateRetailer.setOnClickListener(commonClickListener);
        logout.setOnClickListener(commonClickListener);
        faq.setOnClickListener(commonClickListener);
        contactUs.setOnClickListener(commonClickListener);
        appTour.setOnClickListener(commonClickListener);
        promoCode.setOnClickListener(commonClickListener);
        lottoleague.setOnClickListener(commonClickListener);
        //retailer scan click
        retailer_scan.setOnClickListener(commonClickListener);


        changePwdLine = (CustomTextView) findViewById(R.id.change_pwd_line);
        loginLine = (CustomTextView) findViewById(R.id.login_line);
        faqLine = (CustomTextView) findViewById(R.id.faq_line);
        contactUsLine = (CustomTextView) findViewById(R.id.contact_us_line);
        appTourLine = (CustomTextView) findViewById(R.id.app_tour_line);
        myAccountLine = (CustomTextView) findViewById(R.id.my_account_line);
        inboxLine = (CustomTextView) findViewById(R.id.inbox_line);
        userSettingsLine = (CustomTextView) findViewById(R.id.user_settings_line);
        appSettingsLine = (CustomTextView) findViewById(R.id.app_settings_line);
        inviteFriendLine = (CustomTextView) findViewById(R.id.invite_a_friend_line);
        locateRetailerLine = (CustomTextView) findViewById(R.id.locate_ret_line);
        promoCodeLine = (CustomTextView) findViewById(R.id.promoCode_line);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.drawer_list);

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(240, ViewGroup.LayoutParams.MATCH_PARENT);
//        drawerView.setLayoutParams(layoutParams);


        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) drawerView.getLayoutParams();
        params.width = newWidth;
        drawerView.setLayoutParams(params);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_navigation_drawer, R.string.hello_world,
                R.string.hello_world) {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                ViewPropertyAnimator.animate(headerNavigation).translationX(-slideOffset * 8).setInterpolator(new DecelerateInterpolator(2)).start();
//            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
                isDrawerClosed = true;
                if (DrawerBaseActivity.this instanceof MainScreen)
                    ViewPropertyAnimator.animate(headerNavigation).translationX(-headerNavigation.getMeasuredWidth() / 3).setInterpolator(new DecelerateInterpolator(2)).start();
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
                isDrawerClosed = false;
                if (DrawerBaseActivity.this instanceof MainScreen)
                    ViewPropertyAnimator.animate(headerNavigation).translationX(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        // actionBar.hide();
        // drawerLayout.closeDrawer(drawerView);
        headerNavigation.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Utils.Toast(getApplicationContext(), "Tou", 1000).show();
                try {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {

                }
                if (isDrawerClosed) {
                    isDrawerClosed = false;
                    drawerLayout.closeDrawer(drawerView);
                } else {
                    drawerLayout.openDrawer(drawerView);
                    isDrawerClosed = true;
                }
                return false;
            }
        });
        headerImage.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Utils.Toast(getApplicationContext(), "Tou", 1000).show();
                try {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {

                }
                if (isDrawerClosed) {
                    isDrawerClosed = false;
                    drawerLayout.closeDrawer(drawerView);
                } else {
                    drawerLayout.openDrawer(drawerView);
                    isDrawerClosed = true;
                }
                return false;
            }
        });
        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
                .inflate(R.layout.app_base_activity, null);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        actionBar.setIcon(new ColorDrawable(getResources().getColor(
                android.R.color.transparent)));
        actionBar.setBackgroundDrawable(getResources().getDrawable(
                R.color.header_color));
        actionBar.hide();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    ;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    DebouncedOnClickListener commonClickListener = new DebouncedOnClickListener(500) {

        @Override
        public void onDebouncedClick(View v) {
            int clickedId = v.getId() == R.id.user_pic ? R.id.my_account : v.getId();
            if (clickedId != selectedItemId) {
                selectedItemId = clickedId;
                switch (clickedId) {
                    case R.id.retailer_scan:
                        startActivity(new Intent(DrawerBaseActivity.this, RetailerTicketScreen.class));
                        break;
                    case R.id.lottoleague:
                        startActivity(new Intent(getApplicationContext(),
                                LottoLeague.class));
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;
                    case R.id.user_name:
//                        Utils.Toast(DrawerBaseActivity.this, "Inbox",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.user_pic:
                        if (UserInfo.isLogin(getApplicationContext())) {
                            if (GlobalVariables.connectivityExists(context)) {
                                if (!Config.isWearer) {
                                    String path = "/com/skilrock/pms/mobile/playerInfo/Action/myAccountInfo.action?";
                                    String params = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                                    new PMSWebTask(DrawerBaseActivity.this, path + params, "", null, "MY_ACCOUNT", ProfileDetail.class, "").execute();
                                } else {
                                    Intent intent = new Intent(DrawerBaseActivity.this, ProfileFragment.class);
                                    try {
                                        JSONObject login_data = new JSONObject(VariableStorage.GlobalPref.getStringData(DrawerBaseActivity.this,
                                                VariableStorage.GlobalPref.LOGIN_DATA));
                                        JSONObject playerLoginInfo = login_data.getJSONObject("playerLoginInfo");
                                        JSONObject wallet = playerLoginInfo.getJSONObject("walletBean");
                                        JSONObject profiledata = new JSONObject();
                                        profiledata.put("isSuccess", true);
                                        JSONObject personalInfo = new JSONObject();
                                        personalInfo.put("firstName", playerLoginInfo.getString("firstName"));
                                        personalInfo.put("lastName", playerLoginInfo.getString("lastName"));
                                        personalInfo.put("mobileNum", playerLoginInfo.getString("mobileNo"));
                                        personalInfo.put("emailId", playerLoginInfo.getString("emailId"));
                                        personalInfo.put("address", "");
                                        personalInfo.put("state", playerLoginInfo.getString("state"));
                                        personalInfo.put("city", playerLoginInfo.getString("city"));
                                        personalInfo.put("dob", playerLoginInfo.getString("dob").replace("/", "-"));
                                        personalInfo.put("gender", playerLoginInfo.getString("gender").equalsIgnoreCase("m") ? "male" : "female");
                                        personalInfo.put("profilePhoto", playerLoginInfo.getString("commonContentPath") + "/" + playerLoginInfo.getString("avatarPath"));
                                        personalInfo.put("stateCode", "");
                                        personalInfo.put("cityCode", "");
                                        profiledata.put("personalInfo", personalInfo);
                                        JSONObject walletInfo = new JSONObject();
                                        walletInfo.put("availableBal", Double.parseDouble(wallet.getString("cashBalance")) + "");
                                        walletInfo.put("winningBal", "0.0");
                                        walletInfo.put("depositBal", "0.0");
                                        walletInfo.put("bonusBal", Double.parseDouble(wallet.getString("bonusBalance")) + "");
                                        walletInfo.put("withdrawlBal", Double.parseDouble(wallet.getString("withdrawableBal")) + "");
                                        profiledata.put("walletInfo", walletInfo);
                                        Gson gson = new Gson();
                                        ProfileDetail profileDetail = gson.fromJson(profiledata.toString(), ProfileDetail.class);
                                        if (profileDetail.isSuccess()) {
                                            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_PIC_URL, profileDetail.getPersonalInfo().getProfilePhoto());
                                            intent.putExtra(ProfileFragment.PERSONAL_INFO_KEY, profileDetail.getPersonalInfo());
                                            intent.putExtra(ProfileFragment.WALLET_INFO_KEY, profileDetail.getWalletInfo());
                                            intent.putExtra(ProfileFragment.PLAYER_NAME, VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                            startActivity(intent);
                                            overridePendingTransition(GlobalVariables.startAmin,
                                                    GlobalVariables.endAmin);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                drawerLayout.closeDrawer(drawerView);
                            } else {
                                selectedItemId = -1;
                                GlobalVariables.showDataAlert(context);
                            }
                        }
                        break;
                    case R.id.user_bal:
//                        Utils.Toast(DrawerBaseActivity.this, "Bal",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.login:
                        analytics.sendAll(Fields.Category.LOGIN, Fields.Action.CLICK, Fields.Label.LOGIN);
                        startActivity(new Intent(getApplicationContext(),
                                LoginActivity.class));
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        drawerLayout.closeDrawer(drawerView);
                        break;
                    case R.id.promoCode:
                        startActivity(new Intent(getApplicationContext(),
                                CouponActivity.class));
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;
                    case R.id.my_account:
                        analytics.sendAll(Fields.Category.ACCOUNT, Fields.Action.CLICK, Fields.Label.MY_ACCOUNT_ACTIVITY);
                        if (UserInfo.isLogin(getApplicationContext())) {
                            if (GlobalVariables.connectivityExists(context)) {
                                if (!Config.isWearer) {
                                    String path = "/com/skilrock/pms/mobile/playerInfo/Action/myAccountInfo.action?";
                                    String params = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                                    new PMSWebTask(DrawerBaseActivity.this, path + params, "", null, "MY_ACCOUNT", ProfileDetail.class, "").execute();
                                } else {
                                    Intent intent = new Intent(DrawerBaseActivity.this, ProfileFragment.class);
                                    try {
                                        JSONObject login_data = new JSONObject(VariableStorage.GlobalPref.getStringData(DrawerBaseActivity.this,
                                                VariableStorage.GlobalPref.LOGIN_DATA));
                                        JSONObject playerLoginInfo = login_data.getJSONObject("playerLoginInfo");
                                        JSONObject wallet = playerLoginInfo.getJSONObject("walletBean");
                                        JSONObject profiledata = new JSONObject();
                                        profiledata.put("isSuccess", true);
                                        JSONObject personalInfo = new JSONObject();
                                        personalInfo.put("firstName", playerLoginInfo.getString("firstName"));
                                        personalInfo.put("lastName", playerLoginInfo.getString("lastName"));
                                        personalInfo.put("mobileNum", playerLoginInfo.getString("mobileNo"));
                                        personalInfo.put("emailId", playerLoginInfo.getString("emailId"));
                                        personalInfo.put("address", "");
                                        personalInfo.put("state", playerLoginInfo.getString("state"));
                                        personalInfo.put("city", playerLoginInfo.getString("city"));
                                        personalInfo.put("dob", playerLoginInfo.getString("dob").replace("/", "-"));
                                        personalInfo.put("gender", playerLoginInfo.getString("gender").equalsIgnoreCase("m") ? "male" : "female");
                                        personalInfo.put("profilePhoto", playerLoginInfo.getString("commonContentPath") + "/" + playerLoginInfo.getString("avatarPath"));
                                        personalInfo.put("stateCode", "");
                                        personalInfo.put("cityCode", "");
                                        profiledata.put("personalInfo", personalInfo);
                                        JSONObject walletInfo = new JSONObject();
                                        walletInfo.put("availableBal", AmountFormat.getAmountFormatForTwoDecimal(wallet.getString("cashBalance")));
                                        walletInfo.put("winningBal", AmountFormat.getAmountFormatForTwoDecimal("0.0"));
                                        walletInfo.put("depositBal", AmountFormat.getAmountFormatForTwoDecimal("0.0"));
                                        walletInfo.put("bonusBal", AmountFormat.getAmountFormatForTwoDecimal(wallet.getString("bonusBalance")));
                                        walletInfo.put("withdrawlBal", AmountFormat.getAmountFormatForTwoDecimal(wallet.getString("withdrawableBal")));
                                        profiledata.put("walletInfo", walletInfo);
                                        Gson gson = new Gson();
                                        ProfileDetail profileDetail = gson.fromJson(profiledata.toString(), ProfileDetail.class);
                                        if (profileDetail.isSuccess()) {
                                            VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_PIC_URL, profileDetail.getPersonalInfo().getProfilePhoto());
                                            intent.putExtra(ProfileFragment.PERSONAL_INFO_KEY, profileDetail.getPersonalInfo());
                                            intent.putExtra(ProfileFragment.WALLET_INFO_KEY, profileDetail.getWalletInfo());
                                            intent.putExtra(ProfileFragment.PLAYER_NAME, VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                            startActivity(intent);
                                            overridePendingTransition(GlobalVariables.startAmin,
                                                    GlobalVariables.endAmin);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                drawerLayout.closeDrawer(drawerView);
                            } else {
                                selectedItemId = -1;
                                GlobalVariables.showDataAlert(context);
                            }
                        }
                        drawerLayout.closeDrawer(drawerView);
                        break;
                    case R.id.change_pwd:
                        analytics.sendAll(Fields.Category.CHANGE_PASS, Fields.Action.CLICK, Fields.Label.CHANGE_PASSWORD_ACTIVITY);

                        startActivity(new Intent(getApplicationContext(),
                                ChangePassActivity.class));
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        drawerLayout.closeDrawer(drawerView);
                        break;
                    case R.id.inbox:
                        Utils.Toast(DrawerBaseActivity.this, getResources().getString(R.string.inbox)
                        );
                        break;
                    case R.id.user_settings:
                        Utils.Toast(DrawerBaseActivity.this, getResources().getString(R.string.user_settings)
                        );
                        break;
                    case R.id.app_settings:
                        Utils.Toast(DrawerBaseActivity.this, getResources().getString(R.string.app_settings)
                        );
                        break;
                    case R.id.invite_a_friend:
//                        Utils.Toast(getApplicationContext(), getResources().getString(R.string.invite_friend),
//                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DrawerBaseActivity.this, InviteFriendActivity.class));
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;

                    case R.id.faq:
                        if (!GlobalVariables.connectivityExists(DrawerBaseActivity.this)) {
                            GlobalVariables.showDataAlert(DrawerBaseActivity.this);
                            return;
                        }
                        startActivity(new Intent(DrawerBaseActivity.this, FAQActivity.class));
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;

                    case R.id.contact_us:
                        if (!GlobalVariables.connectivityExists(DrawerBaseActivity.this)) {
                            GlobalVariables.showDataAlert(DrawerBaseActivity.this);
                            return;
                        }
                        startActivity(new Intent(DrawerBaseActivity.this, ContactUsActivity.class));
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;

                    case R.id.app_tour:
                        analytics.sendAll(Fields.Category.APP_TOUR, Fields.Action.CLICK, Fields.Label.APP_TOUR_ACTIVITY);

                        startActivity(new Intent(context, AppTourActivity.class));
                        selectedItemId = -1;
                        drawerLayout.closeDrawer(drawerView);
                        if (isDrawerMultipleCalling(activity))
                            finish();
                        break;
                    case R.id.locate_ret:
                        analytics.sendAll(Fields.Category.LOCATE_RETAILER, Fields.Action.CLICK, Fields.Label.LOCATE_RETAILER_ACTIVITY);
                        if (GlobalVariables.connectivityExists(context)) {
                            String retailerPath = "/com/skilrock/pms/mobile/lmsMgmt/action/fetchNearByRetailerInfo.action?requestData=";
                            GPSTracker location = new GPSTracker(DrawerBaseActivity.this);
                            if (location.canGetLocation()) {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("isGps", "ON");
                                    object.put("playerName", "");
                                    object.put("lng", location.getLongitude() + "");
                                    object.put("lat", location.getLatitude() + "");
                                    object.put("isCitySearch", false);
                                    PMSWebTask PMSWebTask = new PMSWebTask(DrawerBaseActivity.this, retailerPath + URLEncoder.encode(object.toString()), "GET", null, "RETAILER", null, "Loading...");
                                    PMSWebTask.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (UserInfo.isLogin(context)) {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("isGps", "OFF");
                                    object.put("playerName", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                                    object.put("lng", "0");
                                    object.put("lat", "0");
                                    object.put("isCitySearch", false);
                                    PMSWebTask PMSWebTask = new PMSWebTask(DrawerBaseActivity.this, retailerPath + URLEncoder.encode(object.toString()), "GET", null, "RETAILER", null, "Loading...");
                                    PMSWebTask.execute();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Intent intent = new Intent(getApplicationContext(),
                                        LocateRetailer.class);
                                intent.putExtra("RETAILER", "");
                                intent.putExtra("isScratchRet", false);
                                intent.putExtra(LocateRetailer.LOAD_LIST, true);
                                startActivity(intent);
                            }
                            drawerLayout.closeDrawer(drawerView);
                        } else {
                            selectedItemId = -1;
                            GlobalVariables.showDataAlert(context);
                        }
                        // overridePendingTransition(GlobalVariables.startAmin,
                        // GlobalVariables.endAmin);
                        // Utils.Toast(getApplicationContext(), "Logout",
                        // Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        OnClickListener okay = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (GlobalVariables.connectivityExists(context)) {
                                    dBox.dismiss();
                                    if (!Config.isWearer) {
                                        String path = "/com/skilrock/pms/mobile/loginMgmt/Action/playerLogout.action?";
                                        String params = "userName=" + VariableStorage.UserPref.getStringData(getApplicationContext(), VariableStorage.UserPref.USER_NAME);
                                        new PMSWebTask(DrawerBaseActivity.this, path + params, "", null, "LOGOUT", null, "").execute();
                                    } else {
                                        String path = "/Weaver/service/rest/playerLogout";
                                        JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("playerToken", VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.SESSION_ID));
                                            jsonObject.put("domainName", Config.domain_name);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        new WeaverWebTask(DrawerBaseActivity.this, path, "", jsonObject, "LOGOUT", null, "").execute();
                                    }
                                } else {
                                    selectedItemId = -1;
                                    GlobalVariables.showDataAlert(context);
                                }
                            }
                        };
                        OnClickListener cancel = new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                //UserInfo.setLogout(getApplicationContext());
                                dBox.dismiss();
                                selectedItemId = -1;
                            }
                        };
                        dBox = new DownloadDialogBox(DrawerBaseActivity.this,
                                getResources().getString(R.string.logout_conf), "LOGOUT",
                                true, true, okay, cancel);
                        dBox.show();
                        drawerLayout.closeDrawer(drawerView);
                        break;
                }
            } else {
                // Utils.Toast(getApplicationContext(),
                // "close for " + clickedId, Toast.LENGTH_SHORT).show();
            }
        }
    };

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // super.onActivityResult(requestCode, resultCode, data);
    // if (requestCode == 100 && resultCode == RESULT_OK) {
    //
    // }
    // }

    protected void manageDrawer() {

    }

    private void managerPrePostLoginViews(boolean isLogin) {
        if (isLogin) {
            userPic.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            userBalance.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
            myAccount.setVisibility(View.VISIBLE);
            inbox.setVisibility(View.GONE);
            userSettings.setVisibility(View.GONE);
            appSettings.setVisibility(View.GONE);
            inviteFriend.setVisibility(View.GONE);
            locateRetailer.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            changePwd.setVisibility(View.VISIBLE);
            promoCode.setVisibility(View.VISIBLE);
            loginLine.setVisibility(View.GONE);
            myAccountLine.setVisibility(View.VISIBLE);
            inboxLine.setVisibility(View.GONE);
            userSettingsLine.setVisibility(View.GONE);
            appSettingsLine.setVisibility(View.GONE);
            inviteFriendLine.setVisibility(View.GONE);
            locateRetailerLine.setVisibility(View.VISIBLE);
            changePwdLine.setVisibility(View.VISIBLE);
            appTourLine.setVisibility(View.VISIBLE);
            promoCodeLine.setVisibility(View.VISIBLE);

            retailer_scan.setVisibility(View.GONE);
            retailer_scan_line.setVisibility(View.GONE);

            switch (globalPref.getCountry().toUpperCase(Locale.US)) {
                case "ZIM":
                    promoCode.setVisibility(View.VISIBLE);
                    promoCodeLine.setVisibility(View.VISIBLE);
                    locateRetailer.setVisibility(View.VISIBLE);
                    locateRetailerLine.setVisibility(View.VISIBLE);
                    faq.setVisibility(View.GONE);
                    faqLine.setVisibility(View.GONE);
                    contactUs.setVisibility(View.GONE);
                    contactUsLine.setVisibility(View.GONE);
                    inviteFriend.setVisibility(View.VISIBLE);
                    inviteFriendLine.setVisibility(View.VISIBLE);
                    break;
                case "GHANA":
                    promoCode.setVisibility(View.GONE);
                    promoCodeLine.setVisibility(View.GONE);
                    locateRetailer.setVisibility(View.GONE);
                    locateRetailerLine.setVisibility(View.GONE);
                    faq.setVisibility(View.VISIBLE);
                    faqLine.setVisibility(View.VISIBLE);
                    contactUs.setVisibility(View.VISIBLE);
                    contactUsLine.setVisibility(View.VISIBLE);
                    inviteFriend.setVisibility(View.VISIBLE);
                    inviteFriendLine.setVisibility(View.VISIBLE);
                    break;
                case "LAGOS":
                    promoCode.setVisibility(View.GONE);
                    promoCodeLine.setVisibility(View.GONE);
                    locateRetailer.setVisibility(View.GONE);
                    locateRetailerLine.setVisibility(View.GONE);
                    faq.setVisibility(View.VISIBLE);
                    faqLine.setVisibility(View.VISIBLE);
                    contactUs.setVisibility(View.VISIBLE);
                    contactUsLine.setVisibility(View.VISIBLE);
                    inviteFriend.setVisibility(View.VISIBLE);
                    inviteFriendLine.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            userPic.setVisibility(View.VISIBLE);
            changePwd.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            userBalance.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            myAccount.setVisibility(View.GONE);
            inbox.setVisibility(View.GONE);
            userSettings.setVisibility(View.GONE);
            promoCode.setVisibility(View.GONE);
            appSettings.setVisibility(View.GONE);
            locateRetailer.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            loginLine.setVisibility(View.VISIBLE);
            myAccountLine.setVisibility(View.GONE);
            inboxLine.setVisibility(View.GONE);
            userSettingsLine.setVisibility(View.GONE);
            appSettingsLine.setVisibility(View.GONE);
            inviteFriend.setVisibility(View.GONE);
            inviteFriendLine.setVisibility(View.GONE);
            locateRetailerLine.setVisibility(View.VISIBLE);
            appTourLine.setVisibility(View.GONE);
            promoCodeLine.setVisibility(View.GONE);
            changePwdLine.setVisibility(View.GONE);
            retailer_scan.setVisibility(View.GONE);
            retailer_scan_line.setVisibility(View.GONE);

            switch (globalPref.getCountry().toUpperCase(Locale.US)) {
                case "ZIM":
                    locateRetailer.setVisibility(View.VISIBLE);
                    locateRetailerLine.setVisibility(View.VISIBLE);
                    faq.setVisibility(View.GONE);
                    faqLine.setVisibility(View.GONE);
                    contactUs.setVisibility(View.GONE);
                    contactUsLine.setVisibility(View.GONE);
                    break;
                case "GHANA":
                    locateRetailer.setVisibility(View.GONE);
                    locateRetailerLine.setVisibility(View.GONE);
                    faq.setVisibility(View.VISIBLE);
                    faqLine.setVisibility(View.VISIBLE);
                    contactUs.setVisibility(View.VISIBLE);
                    contactUsLine.setVisibility(View.VISIBLE);
                    break;
                case "LAGOS":
                    locateRetailer.setVisibility(View.GONE);
                    locateRetailerLine.setVisibility(View.GONE);
                    faq.setVisibility(View.VISIBLE);
                    faqLine.setVisibility(View.VISIBLE);
                    contactUs.setVisibility(View.VISIBLE);
                    contactUsLine.setVisibility(View.VISIBLE);
                    break;
            }
        }

        if (VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.PLAYER_LEAGUE_ACTIVE).equalsIgnoreCase("Yes")) {
            lottoleague.setVisibility(View.VISIBLE);
            lottoleagueLine.setVisibility(View.VISIBLE);
        } else {
            lottoleague.setVisibility(View.GONE);
            lottoleagueLine.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// Pass the event to
        // ActionBarDrawerToggle,
        // if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListen(String methodType, Object resultData, final Dialog dialog) {
        if (resultData != null && !resultData.toString().equalsIgnoreCase("")) {
            switch (methodType) {
                case "LOGOUT":
                    try {
                        loginData = new JSONObject(resultData.toString());
                        if (!Config.isWearer) {
                            if (loginData.getString("isSuccess").equals("true")) {
                                analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Success");
                                UserInfo.setLogout(DrawerBaseActivity.this);
                                Intent intent = new Intent(DrawerBaseActivity.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                                dialog.dismiss();

                            } else if (loginData.getString("isSuccess").equals("false")) {
                                analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Not Success");
                                selectedItemId = -1;
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
                                            dialog.dismiss();
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(DrawerBaseActivity.this,
                                            loginData.getString("errorMsg"), "", false, true, okClickListener,
                                            null).show();
                                } else if (loginData.getString("errorCode").equalsIgnoreCase("501")) {
                                    dialog.dismiss();
                                    Utils.Toast(context, getString(R.string.sql_exception));
                                } else {
                                    dialog.dismiss();
                                    Utils.Toast(context, loginData.getString("errorMsg"));
                                }
                            }
                        } else { // for weaver
                            if (loginData.getInt("errorCode") == 0) {
                                analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Success");
                                UserInfo.setLogout(DrawerBaseActivity.this);
                                Intent intent = new Intent(DrawerBaseActivity.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                                dialog.dismiss();

                            } else {
                                analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Not Success");
                                selectedItemId = -1;
                                if (loginData.getInt("errorCode") == 203) {
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
                                            dialog.dismiss();
                                        }
                                    };
                                    dialog.dismiss();
                                    new DownloadDialogBox(DrawerBaseActivity.this,
                                            loginData.getString("respMsg"), "", false, true, okClickListener,
                                            null).show();
                                } else {
                                    dialog.dismiss();
                                    Utils.Toast(context, loginData.getString("respMsg"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedItemId = -1;
                        dialog.dismiss();
                        GlobalVariables.showServerErr(context);
                    }
                    break;
                case "LOGOUT_CHANGE_PASS":
                    try {
                        loginData = new JSONObject(resultData.toString());
                        if (loginData.getString("isSuccess").equals("true")) {
                            analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Success");
                            UserInfo.setLogout(DrawerBaseActivity.this);
                            Intent intent = new Intent(DrawerBaseActivity.this,
                                    MainScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(
                                    GlobalVariables.startAmin,
                                    GlobalVariables.endAmin);
                            dialog.dismiss();

                        } else if (loginData.getString("isSuccess").equals("false")) {
                            analytics.sendAll(Fields.Category.LOGOUT, Fields.Action.CLICK, Fields.Label.LOGOUT + "Not Success");
                            selectedItemId = -1;
                            if (loginData.getString("errorCode").equalsIgnoreCase("118")) {
                                UserInfo.setLogout(getApplicationContext());
                                Intent intent = new Intent(getApplicationContext(),
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                                dialog.dismiss();

                                dialog.dismiss();
                            } else if (loginData.getString("errorCode").equalsIgnoreCase("501")) {
                                dialog.dismiss();
                                Utils.Toast(context, getString(R.string.sql_exception));
                            } else {
                                dialog.dismiss();
                                Utils.Toast(context, loginData.getString("errorMsg"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        selectedItemId = -1;
                        dialog.dismiss();
                        GlobalVariables.showServerErr(context);
                    }
                    break;
                case "MY_ACCOUNT":
                    Intent intent = new Intent(this, ProfileFragment.class);
                    ProfileDetail profileDetail = (ProfileDetail) resultData;
                    if (profileDetail.isSuccess()) {
                        VariableStorage.UserPref.setStringPreferences(context, VariableStorage.UserPref.USER_PIC_URL, profileDetail.getPersonalInfo().getProfilePhoto());
                        intent.putExtra(ProfileFragment.PERSONAL_INFO_KEY, profileDetail.getPersonalInfo());
                        intent.putExtra(ProfileFragment.WALLET_INFO_KEY, profileDetail.getWalletInfo());
                        intent.putExtra(ProfileFragment.PLAYER_NAME, VariableStorage.UserPref.getStringData(context, VariableStorage.UserPref.USER_NAME));
                        startActivity(intent);
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                        dialog.dismiss();
                        if (isDrawerMultipleCalling(activity))
                            finish();
                    } else if (profileDetail.getErrorCode().equalsIgnoreCase(501 + "") || profileDetail.getErrorCode().equalsIgnoreCase(2001 + "") || profileDetail.getErrorCode().equalsIgnoreCase(20001 + "")) {
                        dialog.dismiss();
                        Utils.Toast(context, getString(R.string.sql_exception));
                    } else {
                        analytics.sendAll(Fields.Category.ACCOUNT, Fields.Action.CLICK, Fields.Label.MY_ACCOUNT_ACTIVITY + "ERROR");
                        selectedItemId = -1;
                        if (profileDetail.getErrorCode().equalsIgnoreCase("118")) {
                            OnClickListener okClickListener = new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(context);
                                    Intent intent = new Intent(context,
                                            MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(
                                            GlobalVariables.startAmin,
                                            GlobalVariables.endAmin);
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(DrawerBaseActivity.this,
                                    profileDetail.getErrorMsg(), "", false, true, okClickListener,
                                    null).show();
                        } else {
                            dialog.dismiss();
                            Utils.Toast(context, profileDetail.getErrorMsg());
                        }
                    }
                    break;
                case "RETAILER":
                    try {
                        JSONObject object = new JSONObject((String) resultData);
                        if (object.getBoolean("isSuccess")) {
                            intent = new Intent(context,
                                    LocateRetailer.class);
                            intent.putExtra("RETAILER", (String) resultData);
                            intent.putExtra("isScratchRet", false);
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            analytics.sendAll(Fields.Category.LOCATE_RETAILER, Fields.Action.CLICK, Fields.Label.LOCATE_RETAILER_ACTIVITY + " " + object.getString("errorMsg"));
                            selectedItemId = -1;
                            Utils.Toast(context, object.getString("errorMsg"));
                            intent = new Intent(context,
                                    LocateRetailer.class);
                            intent.putExtra("RETAILER", "");
                            intent.putExtra("isScratchRet", false);
                            intent.putExtra(LocateRetailer.LOAD_LIST, true);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                        if (isDrawerMultipleCalling(activity))
                            finish();
                    } catch (Exception e) {
                        analytics.sendAll(Fields.Category.LOCATE_RETAILER, Fields.Action.CLICK, Fields.Label.LOCATE_RETAILER_ACTIVITY + "ERROR");

                        dialog.dismiss();
                        selectedItemId = -1;
                        intent = new Intent(context,
                                LocateRetailer.class);
                        intent.putExtra("RETAILER", "");
                        intent.putExtra("isScratchRet", false);
                        intent.putExtra(LocateRetailer.LOAD_LIST, true);
                        startActivity(intent);
                        //GlobalVariables.showServerErr(context);
                    }
                    break;

            }
        } else if (Config.isStatic && GlobalVariables.loadDummyData) {
            selectedItemId = -1;
            if (dialog != null)
                dialog.dismiss();
            Utils.Toast(context, "Data not available in offline mode");
        } else {
            selectedItemId = -1;
            dialog.dismiss();
            GlobalVariables.showServerErr(context);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }

    /*
    Function name isDrawerMultipleCalling
    @Activity - that is Represent Current Activity.
      Discription
      Function returns the @boolean value that represent that Drawer calling
      from another activity that is not calling from Drawer option.
     */
    private boolean isDrawerMultipleCalling(Activity activity) {
        return activity != null && (activity instanceof LottoLeague || activity instanceof ProfileFragment || activity instanceof InviteFriendActivity || activity instanceof FAQActivity
                || activity instanceof ContactUsActivity || activity instanceof AppTourActivity || activity instanceof LocateRetailer);
    }
}
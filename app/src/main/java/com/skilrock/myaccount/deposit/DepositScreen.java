package com.skilrock.myaccount.deposit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.skilrock.adapters.DepositeGateWayAdapter;
import com.skilrock.adapters.OutletsAdapter;
import com.skilrock.adapters.PGAdapter;
import com.skilrock.bean.BankDetailsBean;
import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AnimatedExpandableListView;
import com.skilrock.customui.CustomCheckedTextView;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.customui.SlidingTabLayoutForDeposite;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;
import com.skilrock.lms.communication.PMSWebTask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.myaccount.PaymentGatewayWeb;
import com.skilrock.myaccount.ProfileFragment;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.DataSource;
import com.skilrock.utils.GlobalVariables;
import com.tabbar.TitlePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DepositScreen extends FragmentActivity implements WebServicesListener {
    private LinearLayout tabParent;
    private String[] tabTitles;
    private int[] tabTitlesId;
    protected int width;
    private float weight;
    private AnimatedExpandableListView outletList;
    private AnimatedExpandableListView paymentGateList;
    private int lastExpandedPosition = -1;
    private static final int REQUEST_CODE = 0;
    private int previousId = -1;
    private String depositAmount;
    private ImageView drawerImage, headerImage, imageView;
    private Spinner spinner;
    private TextView headerText, subHeaderText;
    private RelativeLayout successPage;
    private FrameLayout contentFrame;
    private CustomTextView amount, edit, status, depositAmnt, subMessage, balance, message, done, transid;
    private LinearLayout transLay;
    private ArrayList<CategoryInfoBean> mPowerBeans;
    private String paymentResponse;
    private Context context;
    public int onStartCount = 0;
    private int tabItem = 0;
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private TitlePageIndicator indicator;
    private DepositLimitBean bean;
    ArrayList<DepositLimitBean.PgRange> pgRng;
    private DownloadDialogBox downloadDialogBox;
    private SlidingTabLayoutForDeposite tabs;
    public static String telechashMobileNo;
    private DownloadDialogBox downloadDialog;
    private Analytics analytics;
    private boolean isGhana, isLagos, isZim;
    private GlobalPref globalPref;

    //lagos new data
    public static HashMap<String, ArrayList<BankDetailsBean>> bankDetails = new HashMap<>();
    public static ArrayList<BankDetailsBean> bankDetailListData = new ArrayList<BankDetailsBean>();


    //private SlidingTabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(this);
        countryTypeData();
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.DEPOSITE_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        context = DepositScreen.this;
        bankDetails.clear();
        bean = (DepositLimitBean) getIntent().getSerializableExtra("bean");
        pgRng = new ArrayList<DepositLimitBean.PgRange>();
        for (int i = 0; i < bean.getPgRanges().size(); i++) {
            pgRng.add(i, bean.getPgRanges().get(i));
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        //TabTitles for Zim
//        tabTitles = new String[]{"Payment\nGateway", "Mobile\nMoney", "Africa\nOutlets"};
//        TabTitles for Ghana
        setTabTitles();
//        tabTitles = new String[]{"Payment\nGateway"};

        tabTitlesId = new int[]{3, 2, 1};
        setContentView(R.layout.deposit_screen);
        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }


        bindViewIds();

        setFragments();
        //fragment add in case of Zim
//        fragments = new ArrayList<Fragment>();
//        fragments.add(new PaymentGatwayFragment());
//        fragments.add(new MobileMoneyFragment());
//        fragments.add(new AfricaOutletsFragment());

        //fragment add in case of Ghana
//        fragments = new ArrayList<Fragment>();
//        fragments.add(new PaymentGatwayFragment());

        viewPager.setOffscreenPageLimit(tabTitles.length);
        viewPager.setAdapter(new DepositeGateWayAdapter(context, getSupportFragmentManager(), fragments, tabTitles, viewPager));
        viewPager.setPageMargin(30);

        tabs.setDistributeEvenly(true);


        if (tabTitles.length == 1) {
            tabs.setClickable(false);
        }


        tabs.setCustomTabColorizer(new SlidingTabLayoutForDeposite.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                if (tabTitles.length > 1)
                    return getResources().getColor(R.color.deposite_strip_color);
                else
                    return getResources().getColor(R.color.single_deposit_strip_color);
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideKeyboard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //tabs.setCustomTabView(R.layout.custom_text_view, 0);
        tabs.setViewPager(viewPager);


//        indicator.setTypeface(Config.globalTextFont);
//        indicator.setViewPager(viewPager);
//        indicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Underline);

        // viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);


        headerText.setText(getString(R.string.my_acc_drawer_lay_main));
        subHeaderText.setText(getString(R.string.my_acc_deposit));




       /* DepositLimitBean ob=new DepositLimitBean();
        ArrayList<DepositLimitBean.PgRange> arry;
        arry= new ArrayList<DepositLimitBean.PgRange>();
        arry=ob.getPgRanges();
        int a=arry.size();*/
        //DepositLimitBean i=Intent.getIntent(bean);
        //DepositLimitBean bean= (DepositLimitBean) getIntent().getSerializableExtra("bean");
        //DepositLimitBean bean=new DepositLimitBean();


        depositAmount = getIntent().getStringExtra("amount");
        if (depositAmount.contains(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE))) {
            depositAmount = depositAmount.replace(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE), "").trim();
        }
        amount.setText(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE) + depositAmount);


        outletList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (outletList.isGroupExpanded(groupPosition)) {

                    outletList.removeAllViewsInLayout();
                    outletList.collapseGroupWithAnimation(groupPosition);
                } else {
                    outletList.removeAllViewsInLayout();
                    outletList.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositScreen.this.finish();
            }
        });
        headerImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositScreen.this.finish();
            }
        });
        outletList.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                hideKeyboard();
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    outletList.collapseGroupWithAnimation(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        outletList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                hideKeyboard();
            }
        });

        paymentGateList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                try {
                    JSONObject data = new JSONObject();
                    data.put("playerName", VariableStorage.UserPref.getStringData(DepositScreen.this, VariableStorage.UserPref.USER_NAME));
                    data.put("depositAmt", depositAmount);

                    if (GlobalVariables.connectivityExists(context)) {
                        //if(depositAmount)
                        if (groupPosition == 0) {
                            new PMSWebTask(DepositScreen.this, vPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VPAYMENT", null, "Loading...").execute();
                        } else {
                            new PMSWebTask(DepositScreen.this, visaMasterPayPath + "requestData=" + URLEncoder.encode(data.toString()), "N/A", null, "VISA_MASTERCARD", null, "Loading...").execute();
                        }
                    } else {
                        GlobalVariables.showDataAlert(context);
                    }


                } catch (JSONException e) {
                    try {
                        new DownloadDialogBox(context, context.getResources().getString(R.string.some_internal_error), context.getResources().getString(R.string.oops), false, true, null, null).show();
                    } catch (WindowManager.BadTokenException e1) {
                        GlobalVariables.showServerErr(context);
                    }


                }

                return true;
            }
        });
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                DrawerBaseActivity.selectedItemId = -1;
                finish();
//                Intent intent = new Intent(DepositScreen.this,
//                        ProfileFragment.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                overridePendingTransition(
//                        GlobalVariables.startAmin,
//                        GlobalVariables.endAmin);
            }
        });

    }

    private void setFragments() {
        if (isZim) {
            fragments = new ArrayList<Fragment>();
            fragments.add(new PaymentGatwayFragment());
            fragments.add(new MobileMoneyFragment());
            fragments.add(new AfricaOutletsFragment());
        } else if (isGhana) {
            fragments = new ArrayList<Fragment>();
            fragments.add(new PaymentGatwayFragment());
        } else if (isLagos) {
            fragments = new ArrayList<Fragment>();
            fragments.add(new PaymentGatwayFragment());
            fragments.add(new MobileMoneyFragment());

            //lagos bank list data
            String path = "/com/skilrock/pms/mobile/accMgmt/Action/getBankDetails.action?";
            String params = "userName=" + VariableStorage.UserPref.getStringData(DepositScreen.this, VariableStorage.UserPref.USER_NAME) + "&" + "requestType=" + "Deposit";
            new PMSWebTask(DepositScreen.this, path + params, "", null, "BankDetail", null, "").execute();

        }
    }

    private void setTabTitles() {
        if (isGhana) {
            tabTitles = new String[]{"Payment\nOptions"};
        } else if (isZim) {
            tabTitles = new String[]{"Payment\nGateway", "Mobile\nMoney", "Africa\nOutlets"};
        } else if (isLagos) {
            tabTitles = new String[]{"Payment\nGateway", "Mobile\nMoney"};
        }
    }

    private void countryTypeData() {
        switch (globalPref.getCountry().toUpperCase(Locale.US)) {
            case "GHANA":
                isZim = false;
                isGhana = true;
                isLagos = false;
                break;
            case "ZIM":
                isZim = true;
                isGhana = false;
                isLagos = false;
                break;
            case "LAGOS":
                isZim = false;
                isGhana = false;
                isLagos = true;
                break;
            default:
                break;
        }
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

    private void bindViewIds() {
        paymentGateList = (AnimatedExpandableListView) findViewById(R.id.list_view);
        tabParent = (LinearLayout) findViewById(R.id.custom_tabs);
        outletList = (AnimatedExpandableListView) findViewById(R.id.exp_list);
        edit = (CustomTextView) findViewById(R.id.edit);
        imageView = (ImageView) findViewById(R.id.imageView);
        drawerImage = (ImageView) findViewById(R.id.drawer_image);
        headerImage = (ImageView) findViewById(R.id.header_image);
        status = (CustomTextView) findViewById(R.id.status);
        contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        done = (CustomTextView) findViewById(R.id.done);
        subMessage = (CustomTextView) findViewById(R.id.subMessage);
        successPage = (RelativeLayout) findViewById(R.id.successPage);
        balance = (CustomTextView) findViewById(R.id.balance);
        depositAmnt = (CustomTextView) findViewById(R.id.depositAmnt);
        message = (CustomTextView) findViewById(R.id.message);
        headerText = (TextView) findViewById(R.id.header_text);
        subHeaderText = (TextView) findViewById(R.id.header_sub_text);
        spinner = (Spinner) findViewById(R.id.spinner);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        tabs = (SlidingTabLayoutForDeposite) findViewById(R.id.tabs);
        transid = (CustomTextView) findViewById(R.id.transId);
        transLay = (LinearLayout) findViewById(R.id.translay);

        viewPager.setVisibility(View.VISIBLE);
        amount = (CustomTextView) findViewById(R.id.amount);
        headerText.setVisibility(View.VISIBLE);
        subHeaderText.setVisibility(View.VISIBLE);
        headerText.setTypeface(null, Typeface.BOLD);

        spinner.setVisibility(View.GONE);
        drawerImage.setVisibility(View.GONE);
        tabParent.removeAllViews();
        switch (tabTitles.length) {
            case 1:
                tabTitles[0] = tabTitles[0].replace("\n", " ");
                // singlePGHeader.setVisibility(View.VISIBLE);
                // singlePGHeader.setText(tabTitles[0]);
                // scrollView.setVisibility(View.GONE);
                break;
            case 2:
                // singlePGHeader.setVisibility(View.GONE);
                // scrollView.setVisibility(View.VISIBLE);
                weight = 0.5f;
                break;
            case 3:
                // singlePGHeader.setVisibility(View.GONE);
                // scrollView.setVisibility(View.VISIBLE);
                weight = 0.33f;
                break;
            case 4:
                // singlePGHeader.setVisibility(View.GONE);
                // scrollView.setVisibility(View.VISIBLE);
                weight = 0.25f;
                break;
            case 5:
                // singlePGHeader.setVisibility(View.GONE);
                // scrollView.setVisibility(View.VISIBLE);
                weight = 0.2f;
                break;

            default:
                // singlePGHeader.setVisibility(View.GONE);
                // scrollView.setVisibility(View.VISIBLE);
                weight = 0.3f;
                break;
        }
        for (int i = 0; i < tabTitles.length; i++) {
            View tabView = getLayoutInflater().inflate(
                    R.layout.tab_view, null);
            CustomCheckedTextView textView = (CustomCheckedTextView) tabView
                    .findViewById(R.id.tab_views);
            textView.setText(tabTitles[i]);
            textView.setId(tabTitlesId[i]);
            if (i == 0) {
                textView.setMinLines(2);
            }
            textView.setOnClickListener(getClickListener(textView));
            Drawable drawable = getResources().getDrawable(R.drawable.strip_down_d);
            drawable.setColorFilter(getResources().getColor(R.color.txn_cal_month), Mode.SRC_IN);

            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                    drawable);

            LayoutParams params = new LayoutParams(0,
                    LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.weight = weight;
            textView.setLayoutParams(params);
            textView.setCompoundDrawablePadding(5);
            tabParent.addView(tabView);
        }
        CustomCheckedTextView firstChild = (CustomCheckedTextView) tabParent
                .getChildAt(0);
        Drawable drawable = getResources().getDrawable(R.drawable.strip_down1);
        drawable.setColorFilter(getResources().getColor(R.color.txn_cal_year), Mode.SRC_IN);
        firstChild.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
                drawable);
        firstChild.setCompoundDrawablePadding(5);
        if (tabTitles.length == 1) {
            setView(firstChild.getId());
        } else {
            setView(tabTitlesId[0]);
        }

    }

    private OnClickListener getClickListener(final CustomCheckedTextView clicked) {
        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tabParent.getChildCount(); i++) {
                    hideKeyboard();
                    CustomCheckedTextView view = (CustomCheckedTextView) tabParent
                            .getChildAt(i);
                    if (view.getId() == v.getId()) {
                        CustomCheckedTextView textView = (CustomCheckedTextView) tabParent
                                .getChildAt(i);
                        Drawable drawable = getResources().getDrawable(
                                R.drawable.strip_down1);
                        drawable.setColorFilter(getResources().getColor(R.color.txn_cal_year),
                                Mode.SRC_IN);
                        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, null, drawable);
                        int scrollX = (view.getLeft() - (width / 2))
                                + (view.getWidth() / 2);
                        // scrollView.smoothScrollTo(scrollX, 0);
                    } else {
                        CustomCheckedTextView textView = (CustomCheckedTextView) tabParent
                                .getChildAt(i);
                        Drawable drawable = getResources().getDrawable(
                                R.drawable.strip_down_d);
                        drawable.setColorFilter(getResources().getColor(R.color.txn_cal_month),
                                Mode.SRC_IN);
                        textView.setCompoundDrawablesWithIntrinsicBounds(null,
                                null, null, drawable);
                    }
                }
                setView(clicked.getId());

            }
        };
        return clickListener;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setView(int id) {
        if (previousId != id) {
            previousId = id;
            switch (id) {
                case 1:
                    tabItem = 0;
                    paymentGateList.setVisibility(View.GONE);
                    outletList.setVisibility(View.VISIBLE);

                    mPowerBeans = new ArrayList<>();
                    for (int i = 0; i < bankModes.length; i++) {
                        CategoryInfoBean bean = new CategoryInfoBean(outletsIcons[i], bankModes[i], outletsDesc[i]);
                        mPowerBeans.add(bean);
                    }
                    outletList.setAdapter(new OutletsAdapter(context, mPowerBeans, false, depositAmount, pgRng, true));
                    break;
                case 2:
                    tabItem = 1;
                    paymentGateList.setVisibility(View.GONE);
                    outletList.setVisibility(View.VISIBLE);

                    mPowerBeans = new ArrayList<>();
                    for (int i = 0; i < mwModes.length; i++) {
                        CategoryInfoBean bean = new CategoryInfoBean(
                                mwIcons[i], mwModes[i], mwDesc[i]);
                        mPowerBeans.add(bean);
                    }
                    outletList.setAdapter(new OutletsAdapter(context, mPowerBeans, true, depositAmount, pgRng, false));
                    outletList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    break;
                case 3:
                    tabItem = 2;
                    paymentGateList.setVisibility(View.VISIBLE);
                    outletList.setVisibility(View.GONE);

                    mPowerBeans = new ArrayList<>();
                    for (int i = 0; i < pgModes.length; i++) {
                        CategoryInfoBean bean = new CategoryInfoBean(
                                pgIcons[i], pgModes[i], "");
                        mPowerBeans.add(bean);
                    }
                    paymentGateList.setAdapter(new PGAdapter(context,
                            mPowerBeans, pgRng));
            }
        }
    }

    @Override
    public void onResult(String methodType, final Object resultData, Dialog dialog) {
        if (resultData != null) {
            try {
                final JSONObject json = new JSONObject((String) resultData);
                if (methodType.equalsIgnoreCase("BankDetail")) {
                    if (json.getBoolean("isSuccess")) {
                        dialog.dismiss();
                        JSONArray resultArray = json.getJSONArray("bankDetails");
                        if (resultArray.length() > 0) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject tempData = resultArray.getJSONObject(i);
                                BankDetailsBean data;
                                if (tempData.has("actNo")) {
                                    data = new BankDetailsBean(
                                            tempData.getString("bankName"),
                                            tempData.getString("bankInfo"),
                                            tempData.getString("bankId"),
                                            tempData.getString("actNo"));
                                    bankDetailListData.add(data);
                                } else {
                                    data = new BankDetailsBean(
                                            tempData.getString("bankName"),
                                            tempData.getString("bankInfo"),
                                            tempData.getString("bankId"),
                                            " ");
                                    bankDetailListData.add(data);
                                }
                                bankDetails.put("providerList", bankDetailListData);
                            }
                        }
                    } else if (json.getInt("responseCode") == 118) {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Telecash " + Fields.Label.FAILURE);
                        dialog.dismiss();
                        OnClickListener okClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawerBaseActivity.selectedItemId = -1;
                                UserInfo.setLogout(DepositScreen.this);
                                Intent intent = new Intent(DepositScreen.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                            }
                        };

                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();


                    } else {
                        dialog.dismiss();
                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, null, null).show();
                    }

                } else if (methodType.equalsIgnoreCase("notfyLotsDeposite")) {
                    if (json.getBoolean("isSuccess")) {
                        dialog.dismiss();
                        OnClickListener okClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadDialog.dismiss();
                            }
                        };
                        downloadDialog = new DownloadDialogBox(DepositScreen.this, "Deposit Request Having Reference No. " + json.getString("referenceNo") + " is Successfully Submitted.", "REFERENCE NUMBER", false, true, okClickListener, null);
                        downloadDialog.show();
//                        if (json.getInt("responseCode") == 0) {
//                            dialog.dismiss();
//                            OnClickListener okClickListener = new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    downloadDialog.dismiss();
//                                }
//                            };
//
//                            downloadDialog = new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null);
//                            downloadDialog.show();
//
//                        } else if (json.getInt("responseCode") == 118) {
//                            dialog.dismiss();
//                            OnClickListener okClickListener = new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    DrawerBaseActivity.selectedItemId = -1;
//                                    UserInfo.setLogout(DepositScreen.this);
//                                    Intent intent = new Intent(DepositScreen.this,
//                                            MainScreen.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    overridePendingTransition(
//                                            GlobalVariables.startAmin,
//                                            GlobalVariables.endAmin);
//                                }
//                            };
//
//                            new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
//                        } else {
//                            dialog.dismiss();
//                            new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, null, null).show();
//                        }
                    } else {
                        dialog.dismiss();
                        new DownloadDialogBox(DepositScreen.this, json.getString("errorMsg"), "", false, true, null, null).show();
                    }
                } else if (methodType.equalsIgnoreCase("telecash")) {
                    if (json.getInt("responseCode") == 0) {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Telecash " + Fields.Label.SUCCESS);
                        dialog.dismiss();
                        Intent intent = new Intent(DepositScreen.this,
                                TeleCashConfirm.class);
                        try {
                            intent.putExtra("txnAmount", json.getString("txnAmount"));
                            intent.putExtra("feeAmount", json.getString("feeAmount"));
                            intent.putExtra("totalAmount", json.getString("totalAmount"));
                            intent.putExtra("mobileNo", telechashMobileNo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                        overridePendingTransition(GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    } else if (json.getInt("responseCode") == 118) {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Telecash " + Fields.Label.FAILURE);
                        dialog.dismiss();
                        OnClickListener okClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawerBaseActivity.selectedItemId = -1;
                                UserInfo.setLogout(DepositScreen.this);
                                Intent intent = new Intent(DepositScreen.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                            }
                        };
                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();
                    } else {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Telecash " + Fields.Label.FAILURE);
                        dialog.dismiss();
                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, null, null).show();
                    }

                } else if (methodType.equalsIgnoreCase("ecocash")) {
                    if (json.getInt("responseCode") == 0) {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Eco Cash " + Fields.Label.SUCCESS);
                        dialog.dismiss();
                        OnClickListener okClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                DrawerBaseActivity.selectedItemId = -1;
//                                Intent intent = new Intent(DepositScreen.this,
//                                        MainScreen.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                overridePendingTransition(GlobalVariables.startAmin,
//                                        GlobalVariables.endAmin);
                                downloadDialog.dismiss();
                            }
                        };

                        downloadDialog = new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null);
                        downloadDialog.show();

                    } else if (json.getInt("responseCode") == 118) {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Eco Cash " + Fields.Label.FAILURE);
                        dialog.dismiss();
                        OnClickListener okClickListener = new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawerBaseActivity.selectedItemId = -1;
                                UserInfo.setLogout(DepositScreen.this);
                                Intent intent = new Intent(DepositScreen.this,
                                        MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(
                                        GlobalVariables.startAmin,
                                        GlobalVariables.endAmin);
                            }
                        };

                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, okClickListener, null).show();


                    } else {
                        analytics.sendAll(Fields.Category.DEPOSIT, Fields.Action.GET, "Eco Cash " + Fields.Label.FAILURE);
                        dialog.dismiss();
                        new DownloadDialogBox(DepositScreen.this, json.getString("responseMsg"), "", false, true, null, null).show();
                    }

                } else {

                    if (GlobalVariables.connectivityExists(context)) {

                        Intent intent = new Intent(DepositScreen.this, PaymentGatewayWeb.class);
                        if (json.getBoolean("isSuccess")) {
                            switch (methodType) {
                                case "VPAYMENT":
                                    String redirectUrl = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl);
                                    intent.putExtra("gatewayName", "VPAYMENTS");
                                    startActivityForResult(intent, REQUEST_CODE);
                                    break;
                                case "VISA_MASTERCARD":
                                    String redirectUrl1 = json.getString("redirectUrl");
                                    intent.putExtra("redirectUrl", redirectUrl1);
                                    intent.putExtra("gatewayName", "VISA/MASTERCARD");
                                    startActivityForResult(intent, REQUEST_CODE);
                                    break;
                            }
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
//                    2005   =  "Proper Request Data Not Provided";
//                    118     =  "Time Out. Login Again"
//                    2006   =  "Deposit Amount should be greater than or equal to "
//                    2007   =  "Deposit Amount should be Less than or equal to ";
//                    2002   =  "Vpayment specific error at time of first communication"
//                    2001=" Vpayments Connection Error!"
//                    502     =  "Some Internal Error Occured !";
//                    501= "SQL Exception Occured !"
//                    163= "Deposit Payment request Not Intiated Successfully";
//                    0 (status="SUCCESS" & is Success= true)         =  "Amount Deposited Succesfully"
//                    No error Code(status="CANCELLED"  & is Success= true)=  "Deposit Transaction Cancelled Successfully"
//                    2003  = " Vpayments Deposit Request Id  Not Available!"
//                    2004 = "Deposit Request Id Not Available!";
//                    103="Invalid UserName or Password"
//                    512="Some Internal Error"

                            if (json.getLong("errorCode") == 2002) {
                                new DownloadDialogBox(context, context.getResources().getString(R.string.vpayment_specific_err), "", false, true, null, null).show();
                            } else if (json.getLong("errorCode") == 2001) {
                                new DownloadDialogBox(context, context.getResources().getString(R.string.vpayment_con_err), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 163) {
                                new DownloadDialogBox(context, context.getResources().getString(R.string.req_pay_not_inti), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 2003) {
                                new DownloadDialogBox(context, context.getResources().getString(R.string.vpay_req_id_not_avl), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 2004) {
                                new DownloadDialogBox(context, context.getResources().getString(R.string.req_id_not_avl), "", false, true, null, null).show();

                            } else if (json.getLong("errorCode") == 118) {
                                OnClickListener okClickListener = new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DrawerBaseActivity.selectedItemId = -1;
                                        UserInfo.setLogout(DepositScreen.this);
                                        Intent intent = new Intent(DepositScreen.this,
                                                MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(
                                                GlobalVariables.startAmin,
                                                GlobalVariables.endAmin);
                                    }
                                };

                                new DownloadDialogBox(context, /*txnBean.getErrorMsg()*/context.getResources().getString(R.string.time_out), "", false, true, okClickListener, null).show();
                            } else {
                                new DownloadDialogBox(DepositScreen.this, json.getString("errorMsg"), "", false, true, null, null).show();

                            }

                        }
                    } else {
                        GlobalVariables.showDataAlert(context);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
                GlobalVariables.showServerErr(DepositScreen.this);
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(DepositScreen.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentResponse != null) {
            try {
                JSONObject json = new JSONObject(paymentResponse);
                Utils.consolePrint(json.toString());

                String statusText = json.optString("status");
                double amountText = json.optDouble("amount");
                String mMessageText = json.optString("message");
                double aBalance = json.optDouble("balance");
                successPage.setVisibility(View.VISIBLE);
                contentFrame.setVisibility(View.GONE);
                if (statusText.toUpperCase(Locale.ENGLISH).contains("SUCCESS")) {//SUCCESS
                    imageView.setBackgroundResource(R.drawable.success);
                    status.setText(statusText);
                    if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                        if (json.optString("transId").equalsIgnoreCase("")) {
                            transLay.setVisibility(View.GONE);
                        } else {
                            transid.setText(json.optString("transId"));
                            transLay.setVisibility(View.VISIBLE);
                        }
                    }
                    balance.setText(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getCorrectAmountFormat(String.valueOf(aBalance)));
                    if (mMessageText.equalsIgnoreCase("")) {
                        subMessage.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
                    } else {
                        subMessage.setVisibility(View.VISIBLE);
                        message.setText(mMessageText);
                    }
                    subMessage.setVisibility(View.VISIBLE);
                    if (json.optString("amount").equalsIgnoreCase("")) {
                        findViewById(R.id.depositAmntText).setVisibility(View.GONE);
                        depositAmnt.setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.depositAmntText).setVisibility(View.VISIBLE);
                        depositAmnt.setText(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getCorrectAmountFormat(String.valueOf(amountText)));
                    }
                } else {
                    imageView.setBackgroundResource(R.drawable.faiure_icon);
                    status.setText(statusText);
                    if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                        if (json.optString("transId").equalsIgnoreCase("")) {
                            transLay.setVisibility(View.GONE);
                        } else {
                            transid.setText(json.optString("transId"));
                            transLay.setVisibility(View.VISIBLE);
                        }
                    }
                    subMessage.setVisibility(View.GONE);
                    balance.setText(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getCorrectAmountFormat(String.valueOf(aBalance)));
                    if (mMessageText.equalsIgnoreCase("")) {
                        subMessage.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
                    } else {
                        subMessage.setVisibility(View.VISIBLE);
                        message.setText(mMessageText);
                    }

                    if (json.optString("amount").equalsIgnoreCase("")) {
                        findViewById(R.id.depositAmntText).setVisibility(View.GONE);
                        depositAmnt.setVisibility(View.GONE);
                    } else {
                        findViewById(R.id.depositAmntText).setVisibility(View.VISIBLE);
                        depositAmnt.setText(VariableStorage.GlobalPref.getStringData(DepositScreen.this, VariableStorage.GlobalPref.CURRENCY_CODE) + AmountFormat.getCorrectAmountFormat(String.valueOf(amountText)));
                    }
                    status.setTextColor(Color.RED);
                    depositAmnt.setTextColor(Color.RED);
                    message.setTextColor(Color.RED);
                    balance.setTextColor(Color.RED);
                }
                if (aBalance > 0 && amountText > 0) {
                    VariableStorage.UserPref.setStringPreferences(DepositScreen.this, VariableStorage.UserPref.USER_BAL, AmountFormat.getCorrectAmountFormat(String.valueOf(aBalance)));
                    if (!globalPref.getCountry().equalsIgnoreCase("ZIM")) {
                        VariableStorage.UserPref.setStringPreferences(DepositScreen.this, VariableStorage.UserPref.DEPOSIT_BAL, json.getString("depositBal"));
                        VariableStorage.UserPref.setStringPreferences(DepositScreen.this, VariableStorage.UserPref.WINNING_BAL, json.getString("winningBal"));
                        VariableStorage.UserPref.setStringPreferences(DepositScreen.this, VariableStorage.UserPref.WITHDRAWAL_BAL, json.getString("withdrawlBal"));
                        VariableStorage.UserPref.setStringPreferences(DepositScreen.this, VariableStorage.UserPref.BONUS_BAL, json.getString("bonusBal"));
                    }
                }
//                OnClickListener okClickListener = new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UserInfo.setLogout(getApplicationContext());
//                        DepositScreen.this.finish();
//                    }
//                };
//                new DownloadDialogBox(this, "Status :" + statusText + "\n" + "Amount :" + amountText + "\n" + mMessageText, "", false, true, okClickListener, null).show();
            } catch (Exception e) {
//                e.printStackTrace();
                OnClickListener okClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerBaseActivity.selectedItemId = -1;
                        Intent intent = new Intent(DepositScreen.this,
                                MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(
                                GlobalVariables.startAmin,
                                GlobalVariables.endAmin);
                    }
                };
                new DownloadDialogBox(DepositScreen.this, context.getResources().getString(R.string.deposit_error), "Deposit", false, true, okClickListener, null).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Utils.consolePrint("Data: " + requestCode + resultCode + intent);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            paymentResponse = intent.getExtras().getString("paymentResponse");
        }
//        paymentResponse = "{\"status\":\"SUCCESS\",\"message\":\"Successfully Deposit.\",\"amount\":\"10.00\",\"balance\":\"11917.92\",\"bonusBal\":\"0.10\",\"depositBal\":\"11917.82\",\"withdrawlBal\":\"0.00\",\"winningBal\":\"0.00\"}";
    }


    private static final String AFRICA_CASH_MSG = "For a direct deposit, you may approach any Africa Lotto retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private static final String TELE_CASH_MSG = "For a direct deposit, you may approach any TeleCash retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private static final String ONE_WALLET_MSG = "For a direct deposit, you may approach any One Wallet retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private final String[] bankModes = new String[]{"AfricaLotto Outlets"};
    private final String[] pgModes = new String[]{"Vpayments", "VISA/MasterCard"};
    private final String vPayPath = "/com/skilrock/pms/api/accMgmt/action/vpaymentsPayRequest.action?";
    private final String visaMasterPayPath = "/com/skilrock/pms/api/accMgmt/action/vpaymentsCardPayRequest.action?";
    private final String[] outletsDesc = new String[]{AFRICA_CASH_MSG};
    private final int[] outletsIcons = new int[]{R.drawable.shop};
    private final int[] mwIcons = new int[]{R.drawable.eco_cash, R.drawable.tele_cash, R.drawable.one_wallet};
    private final String[] mwDesc = new String[]{"", TELE_CASH_MSG, ONE_WALLET_MSG};
    private final String[] mwModes = new String[]{"EcoCash", "TeleCash", "OneWallet"};
    private final String[] pgCode = new String[]{"vpayment", "vpaymentcard", "econet", "telecash"};

    private final int[] pgIcons = new int[]{R.drawable.vpay, R.drawable.master_visa_card};


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(this);
    }
}

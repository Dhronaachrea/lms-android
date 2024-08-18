package com.skilrock.myaccount.deposit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.skilrock.adapters.OutletsAdapter;
import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.AnimatedExpandableListView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import java.util.ArrayList;


public class MobileMoneyFragment extends Fragment {


    private View mainView;
    private final String[] mwModes = new String[]{"EcoCash", "TeleCash", "OneWallet"};
    private final int[] mwIcons = new int[]{R.drawable.eco_cash, R.drawable.tele_cash, R.drawable.one_wallet};
    private final String[] mwDesc = new String[]{"", TELE_CASH_MSG, ONE_WALLET_MSG};
    private static final String AFRICA_CASH_MSG = "For a direct deposit, you may approach any Africa Lotto retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private static final String TELE_CASH_MSG = "For a direct deposit, you may approach any TeleCash retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private static final String ONE_WALLET_MSG = "For a direct deposit, you may approach any One Wallet retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";

    private AnimatedExpandableListView outletList;
    private ArrayList<CategoryInfoBean> mPowerBeans;
    private String depositAmount;
    private int lastExpandedPosition = -1;
    ArrayList<DepositLimitBean.PgRange> pgRng;
    private DepositLimitBean bean;
    private int ecocashpos;
    private int telecashpos;
    private Double compAmt;
    private String symbol;
    private Analytics analytics;
    private Activity activity;
    private GlobalPref globalPref;


    //lagos
    private final int[] lagosIcon = new int[]{R.drawable.deposit_online, R.drawable.winlot, R.drawable.paga_reg};
    private final String[] lagosDesc = new String[]{"", WINLOT_OFFICE, PAGA_OUTLET};
    private final String[] lagosModes = new String[]{"Cash Deposit/Online Transfer", "Deposit at Winlot Office", "Deposit at Paga Outlet"};
    public static final String WINLOT_OFFICE = "Please deposit cash at WinLot office - 27, oba akran avenue-Ikeja-Lagos.";
    public static final String PAGA_OUTLET = "Please deposit cash at any Paga Agent";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.MOBILE_MONEY);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        symbol = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.CURRENCY_CODE);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_mobile_money, container, false);
        depositAmount = getActivity().getIntent().getStringExtra("amount");
        bean = (DepositLimitBean) getActivity().getIntent().getSerializableExtra("bean");
        pgRng = new ArrayList<DepositLimitBean.PgRange>();
        for (int i = 0; i < bean.getPgRanges().size(); i++) {
            pgRng.add(i, bean.getPgRanges().get(i));
        }
        bindViewIds(mainView);
        setView();
        return mainView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void bindViewIds(View view) {
        outletList = (AnimatedExpandableListView) view.findViewById(R.id.list_view_mob_mony);
        outletList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    private void setView() {
        outletList.setVisibility(View.VISIBLE);
        mPowerBeans = new ArrayList<>();
        if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
            for (int i = 0; i < lagosModes.length; i++) {
                CategoryInfoBean bean = new CategoryInfoBean(
                        lagosIcon[i], lagosModes[i], lagosDesc[i]);
                mPowerBeans.add(bean);
            }
            outletList.setAdapter(new OutletsAdapter(getActivity(), mPowerBeans, true, depositAmount, pgRng, false));
        } else if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
            for (int i = 0; i < mwModes.length; i++) {
                CategoryInfoBean bean = new CategoryInfoBean(
                        mwIcons[i], mwModes[i], mwDesc[i]);
                mPowerBeans.add(bean);
            }
            outletList.setAdapter(new OutletsAdapter(getActivity(), mPowerBeans, true, depositAmount, pgRng, false));
        }
//        outletList.setAdapter(new OutletsAdapter(getActivity(), mPowerBeans, true, depositAmount, pgRng, false));
        //outletList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);


        compAmt = Double.valueOf(depositAmount).doubleValue();

        for (int i = 0; i < pgRng.size(); i++) {
            if (pgRng.get(i).getPgCode().equalsIgnoreCase("econet")) {
                ecocashpos = i;
            }
            if (pgRng.get(i).getPgCode().equalsIgnoreCase("telecash")) {
                telecashpos = i;
            }
        }


        outletList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
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


        outletList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            private static final long MIN_DELAY_MS = 500;
            private long mLastClickTime;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                long lastClickTime = mLastClickTime;
                long now = System.currentTimeMillis();
                mLastClickTime = now;

                if (now - lastClickTime < MIN_DELAY_MS) {

                } else {
                    if (globalPref.getCountry().equalsIgnoreCase("LAGOS")) {
                        if (outletList.isGroupExpanded(groupPosition)) {
                            outletList.removeAllViewsInLayout();
                            outletList.collapseGroupWithAnimation(groupPosition);
                        } else {
                            outletList.removeAllViewsInLayout();
                            outletList.expandGroupWithAnimation(groupPosition);
                        }
                    } else if (globalPref.getCountry().equalsIgnoreCase("ZIM")) {
                        if ((groupPosition == 0) && ((pgRng.get(ecocashpos).getMin() <= compAmt) && (pgRng.get(ecocashpos).getMax() >= compAmt))) {

                            if (outletList.isGroupExpanded(groupPosition)) {
                                outletList.removeAllViewsInLayout();
                                outletList.collapseGroupWithAnimation(groupPosition);
                            } else {
                                outletList.removeAllViewsInLayout();
                                outletList.expandGroupWithAnimation(groupPosition);
                            }

                        } else {
                            if (groupPosition == 0) {
                                Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + symbol + pgRng.get(ecocashpos).getMin() + " " + activity.getResources().getString(R.string.to) + "  " + symbol + pgRng.get(ecocashpos).getMax());
                            }
                        }
                        if ((groupPosition == 1)) {
                            Toast.makeText(getActivity(), "currently not available", Toast.LENGTH_SHORT).show();
                        } else if ((groupPosition == 1) && ((pgRng.get(telecashpos).getMin() <= compAmt) && (pgRng.get(telecashpos).getMax() >= compAmt))) {
                            if (outletList.isGroupExpanded(groupPosition)) {
                                outletList.removeAllViewsInLayout();
                                outletList.collapseGroupWithAnimation(groupPosition);
                            } else {
                                outletList.removeAllViewsInLayout();
                                outletList.expandGroupWithAnimation(groupPosition);
                            }

                        } else {
                            if (groupPosition == 1) {
                                Utils.Toast(getActivity(), activity.getResources().getString(R.string.amt_in_bt) + " " + symbol + pgRng.get(telecashpos).getMin() + " " + activity.getResources().getString(R.string.to) + "  " + symbol + pgRng.get(telecashpos).getMax());
                            }
                        }

                        if ((groupPosition == 2)) {
                            if (outletList.isGroupExpanded(groupPosition)) {
                                outletList.removeAllViewsInLayout();
                                outletList.collapseGroupWithAnimation(groupPosition);
                            } else {
                                outletList.removeAllViewsInLayout();
                                outletList.expandGroupWithAnimation(groupPosition);
                            }

                        }

                    }
                }

                return true;


            }
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }
}

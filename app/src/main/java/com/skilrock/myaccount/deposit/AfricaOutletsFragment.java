package com.skilrock.myaccount.deposit;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.android.gms.analytics.HitBuilders;
import com.skilrock.adapters.OutletsAdapter;
import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.customui.AnimatedExpandableListView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;


public class AfricaOutletsFragment extends Fragment {


    private View mainView;
    private String depositAmount;
    private AnimatedExpandableListView outletList;
    private ArrayList<CategoryInfoBean> mPowerBeans;
    private final String[] bankModes = new String[]{"AfricaLotto Outlets"};
    private final int[] outletsIcons = new int[]{R.drawable.shop};
    private static final String AFRICA_CASH_MSG = "For a direct deposit, you may approach any Africa Lotto retailer or office close to you. Simply present your registration details used when you registered  on the mobile app, if you have not yet registered the clerk can assist you to do so. Present the amount of Money you would like to deposit in your Africa Lotto Account, and the clerk will deposit this amount for you, instantly \"juicing\" up your account. It will reflect immediately, you can now login through your mobile app, view your updated balance and play any Africa Lotto game on offer!";
    private final String[] outletsDesc = new String[]{AFRICA_CASH_MSG};
    ArrayList<DepositLimitBean.PgRange> pgRng;
    private DepositLimitBean bean;
    private Activity activity;
    private Analytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.AFRICA_OUTLET_FRAGMENT);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView= inflater.inflate(R.layout.fragment_africa_outlets, container, false);
        depositAmount = getActivity().getIntent().getStringExtra("amount");
        bean= (DepositLimitBean) getActivity().getIntent().getSerializableExtra("bean");
        pgRng=new ArrayList<DepositLimitBean.PgRange>();
        for(int i=0;i<bean.getPgRanges().size();i++)
        {
            pgRng.add(i,bean.getPgRanges().get(i));
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

    private void setView() {

        outletList.setVisibility(View.VISIBLE);

        mPowerBeans = new ArrayList<>();
        for (int i = 0; i < bankModes.length; i++) {
            CategoryInfoBean bean = new CategoryInfoBean(outletsIcons[i], bankModes[i], outletsDesc[i]);
            mPowerBeans.add(bean);
        }
        outletList.setAdapter(new OutletsAdapter(getActivity(), mPowerBeans, false, depositAmount,pgRng, true));
        outletList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

    }



    private void bindViewIds(View view) {
        outletList= (AnimatedExpandableListView) view.findViewById(R.id.list_view_mob_mony);
    }
}

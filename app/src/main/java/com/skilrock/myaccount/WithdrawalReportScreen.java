package com.skilrock.myaccount;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.skilrock.adapters.WithDrawAdapter;
import com.skilrock.bean.WithDrawRepBean;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 8/13/2015.
 */
public class WithdrawalReportScreen extends Fragment {

    private ListView itemList;

    private WithDrawAdapter adapter;
    private RobotoTextView txt_info;
    private Analytics analytics;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(activity);
        analytics.setScreenName(Fields.Screen.WITHDRAWAL_REPORT_SCREEN);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdrawal_report, null);
        bindViewIds(view);
        parseJson(getArguments().getString("WITHDRAWALREPORT"));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void parseJson(String json) {

        Gson gson = new Gson();
        WithDrawRepBean bean = gson.fromJson(json, WithDrawRepBean.class);

        if (bean.getWithdrawlRequestData().size() == 0) {
            txt_info.setVisibility(View.VISIBLE);
            txt_info.setText(getString(R.string.withdrawal_rep_data));
        } else {
            txt_info.setVisibility(View.GONE);
            adapter = new WithDrawAdapter(activity, R.layout.my_withdrawal_row, bean);
        }
        itemList.setAdapter(adapter);
    }

    private void bindViewIds(View view) {
        itemList = (ListView) view.findViewById(R.id.tickets_list);

        txt_info = (RobotoTextView) view.findViewById(R.id.txt_info);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(activity);

    }
}

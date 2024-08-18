package com.skilrock.myaccount;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.google.gson.Gson;
import com.skilrock.adapters.DepositeReportAdapter;
import com.skilrock.bean.WithDrawRepBean;
import com.skilrock.customui.RobotoTextView;
import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 1/5/2016.
 */
public class MyBankDepositeReport extends Fragment {
    private ListView itemList;
    private DepositeReportAdapter adapter;
    private RobotoTextView txt_info;
    private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideKeyboard();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        hideKeyboard();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.withdrawal_report, null);
        bindViewIds(view);
        parseJson(getArguments().getString("BANK_DEPOSIT_REPORT"));
        return view;
    }

    private void hideKeyboard() {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void parseJson(String json) {

        Gson gson = new Gson();
        WithDrawRepBean bean = gson.fromJson(json, WithDrawRepBean.class);

        if (bean.getDepositData().size() == 0) {
            txt_info.setVisibility(View.VISIBLE);
            txt_info.setText(getString(R.string.bank_deposit_rep_data));
        } else {
            txt_info.setVisibility(View.GONE);
            adapter = new DepositeReportAdapter(activity, R.layout.deposite_report_row, bean);
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
    }
}

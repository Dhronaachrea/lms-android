package com.skilrock.sportslottery;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.skilrock.bean.SportsBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DebouncedOnClickListener;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.lms.communication.SLETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class SLFragment extends Fragment implements WebServicesListener {

    private LinearLayout matchView, resultView;
    private CustomTextView buyNow;
    private SportsBean bean;
    private Bundle bundle;
    private Analytics analytics;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.SPORTS_LOTTERY_SOCCER);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spl_fragment, container, false);
        bindViewIds(view);
        bundle = getArguments();
        bean = (SportsBean) bundle.getSerializable("bean");
        if (bundle.getInt(SportsLotteryActivity.GAME_TYPE_ID) != -1)
            buyNow();

        if (!VariableStorage.GlobalPref.getBooleanData(getActivity(), VariableStorage.GlobalPref.SLE_IS_ENABLED)) {
            buyNow.setAlpha(.5f);
//            buyNow.setEnabled(false);
        }

        return view;
    }

    private void bindViewIds(View view) {
        matchView = (LinearLayout) view.findViewById(R.id.match_view);
        resultView = (LinearLayout) view.findViewById(R.id.result_view);
        buyNow = (CustomTextView) view.findViewById(R.id.buy_now);

        matchView.setOnClickListener(listener);
        resultView.setOnClickListener(listener);
        buyNow.setOnClickListener(listener);
    }

    DebouncedOnClickListener listener = new DebouncedOnClickListener(700) {
        @Override
        public void onDebouncedClick(View v) {
            switch (v.getId()) {
                case R.id.buy_now:
                    analytics.sendAction(Fields.Category.SL_FRAGMENT, Fields.Action.CLICK);
                    buyNow();
//                    else
//                        Utils.Toast(getActivity(), "Currently sale is not Available");

//                Intent intent = new Intent(getActivity(), SportsLotteryActivity.class);
//                SportsLotteryCheckResultBean sportsLotteryResultBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsLotteryCheckResultBean.class);
//                 intent.putExtra(SportsLotteryResultActivity.SPORTS_LOTTERY_RESULT, sportsLotteryResultBean);
//                startActivity(intent);
                    break;

                case R.id.match_view:
                    analytics.sendAction(Fields.Category.SL_FRAGMENT, Fields.Action.CLICK);

                    if (GlobalVariables.connectivityExists(getActivity())) {
                        analytics.sendAction(Fields.Category.SL_MATCH_LIST, Fields.Action.CLICK);
                        String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_ROOT_URL) + "/rest/dataMgmt/fetchSLEMatchListData";
                        String json = "{" +
                                "\"merchantCode\":\"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_MER_CODE) + "\"," +
                                "\"listType\":\"drawWise\"" +
                                "}";
                        new SLETask(SLFragment.this, "MATCH", null, url, json).execute();
                    } else {
                        GlobalVariables.showDataAlert(getActivity());
                    }
                    break;

                case R.id.result_view:
                    analytics.sendAction(Fields.Category.SL_FRAGMENT, Fields.Action.CLICK);

                    analytics.sendAction(Fields.Category.SL_CHECK_RESULT, Fields.Action.CLICK);
                    if (GlobalVariables.connectivityExists(getActivity())) {
                        String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_ROOT_URL) + "/rest/dataMgmt/fetchSLEResultData";
                        String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_MER_CODE) + "\"}";
                        new SLETask(SLFragment.this, "RESULT", null, url, json).execute();
                    } else {
                        GlobalVariables.showDataAlert(getActivity());
                    }
                    break;
            }
        }
    };

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {
        if (resultData != null) {
            try {
                JSONObject object = (JSONObject) resultData;
                if (object.getInt("responseCode") == 0) {
                    switch (methodType) {
                        case "SL":
                            analytics.sendAll(Fields.Category.SL_FRAGMENT, Fields.Action.GET, Fields.Label.SUCCESS);
                            Intent intent = new Intent(getActivity(), SportsLotteryActivity.class);
//                            String result = "{\"responseCode\":0,\"sleData\":{\"gameData\":[{\"maxEventCount\":1,\"gameDisplayName\":\"Soccer\",\"tktMaxAmt\":10000,\"gameId\":1,\"minEventCount\":1,\"gameDevname\":\"SOCCER\",\"tktThresholdAmt\":100,\"gameTypeData\":[{\"gameTypeDevName\":\"soccer12\",\"gameTypeId\":1,\"drawData\":[],\"eventType\":\"\",\"gameTypeDisplayName\":\"Soccer Cash 12\",\"areEventsMappedForUpcomingDraw\":false,\"upcomingDrawStartTime\":\"\",\"eventSelectionType\":\"MULTIPLE\",\"gameTypeMaxBetAmtMultiple\":100,\"gameTypeUnitPrice\":1}]}]},\"responseMsg\":\"SUCCESS\",\"sessVar\":false}";
//                            resultData = new JSONObject(result);
                            SportsBean sportsBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsBean.class);
                            if (bundle.getInt(SportsLotteryActivity.GAME_TYPE_ID) != -1 && VariableStorage.GlobalPref.getBooleanData(getActivity(), VariableStorage.GlobalPref.SLE_NOTIFICATION))
                                intent.putExtra(SportsLotteryActivity.GAME_TYPE_ID, bundle.getInt(SportsLotteryActivity.GAME_TYPE_ID));
                            intent.putExtra("bean", sportsBean);
                            startActivity(intent);
                            break;
                        case "MATCH":
                            analytics.sendAll(Fields.Category.SL_FRAGMENT, Fields.Action.GET, Fields.Label.SUCCESS);

                            intent = new Intent(getActivity(), SportsLotteryMatchActivity.class);
                            SportsLotteryMatchBean sportsLotteryMatchBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsLotteryMatchBean.class);
                            intent.putExtra(SportsLotteryMatchActivity.SPORTS_LOTTERY_MATCH_LIST, sportsLotteryMatchBean);
                            startActivity(intent);
                            break;

                        case "RESULT":
                            analytics.sendAll(Fields.Category.SL_FRAGMENT, Fields.Action.GET, Fields.Label.SUCCESS);

                            intent = new Intent(getActivity(), SportsLotteryResultActivity.class);
                            SportsLotteryCheckResultBean sportsLotteryResultBean = new Gson().fromJson(((JSONObject) resultData).toString(), SportsLotteryCheckResultBean.class);
                            intent.putExtra(SportsLotteryResultActivity.SPORTS_LOTTERY_RESULT, sportsLotteryResultBean);
                            startActivity(intent);
                            break;
                    }
                    dialog.dismiss();
                } else if (object.getInt("responseCode") == 20001) {
                    dialog.dismiss();
                    Utils.Toast(activity, getString(R.string.sql_exception));
                } else {
                    analytics.sendAll(Fields.Category.SL_FRAGMENT, Fields.Action.GET, Fields.Label.FAILURE);

                    dialog.dismiss();
                    new DownloadDialogBox(getActivity(), object.getString("responseMsg"), "", false, true, null, null).show();
                }
            } catch (JSONException e) {
                dialog.dismiss();
                GlobalVariables.showServerErr(getActivity());
            }
        } else {
            dialog.dismiss();
            GlobalVariables.showServerErr(getActivity());
        }
    }

    private void buyNow() {
        if (!VariableStorage.GlobalPref.getBooleanData(getActivity(), VariableStorage.GlobalPref.SLE_IS_ENABLED)) {
            Utils.Toast(SLFragment.this.getActivity(), "currently sale is not available");
            return;
        } else if (GlobalVariables.connectivityExists(getActivity())) {
            analytics.sendAll(Fields.Category.SL_SOCCER, Fields.Action.CLICK, Fields.Label.BUY);
            String url = VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_ROOT_URL) + "/rest/dataMgmt/fetchSLEDrawData";
            String json = "{\"merchantCode\": \"" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.SLE_MER_CODE) + "\"}";
            new SLETask(this, "SL", null, url, json).execute();
        } else {
            GlobalVariables.showDataAlert(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }
}

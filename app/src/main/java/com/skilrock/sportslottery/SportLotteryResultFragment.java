package com.skilrock.sportslottery;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 7/10/2015.
 */
public class SportLotteryResultFragment extends Fragment {

    private ListView top;
    private ListView bottom;
    private SportsLotteryCheckResultBean.DrawData drawData;
    private SportsLotteryResultTopAdapter topAdapter;
    private SportsLotteryResultBottomAdapter bottomAdapter;

    private int layoutRes;
    private String[] eventType;
    private Activity activity;

    public SportLotteryResultFragment(SportsLotteryCheckResultBean.DrawData drawData, int layoutRes, String[] eventType) {
        this.drawData = drawData;
        this.layoutRes = layoutRes;
        this.eventType = eventType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.sports_result_fragment, null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        top = (ListView) view.findViewById(R.id.lst_sports_result_top);
        bottom = (ListView) view.findViewById(R.id.lst_sports_result_bottom);
        topAdapter = new SportsLotteryResultTopAdapter(getActivity(), layoutRes, drawData.getEventData(), eventType);
        bottomAdapter = new SportsLotteryResultBottomAdapter(getActivity(), drawData.getWinningData());
        top.setAdapter(topAdapter);
        bottom.setAdapter(bottomAdapter);

    }
}

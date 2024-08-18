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
import com.skilrock.sportslottery.SportsLotteryMatchBean.EventData;

import java.util.List;

/**
 * Created by stpl on 7/16/2015.
 */
public class SportlotteryMatchFragment extends Fragment{

    private ListView matchList;
    private List<EventData> eventDatas;
    private SportsLotteryMatchAdapter adapter;
    private Activity activity;

    public SportlotteryMatchFragment(List<EventData> eventDatas) {
        this.eventDatas = eventDatas;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.sports_match_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        matchList = (ListView) view.findViewById(R.id.lst_sports_match);
        adapter = new SportsLotteryMatchAdapter(getActivity(), eventDatas);
        matchList.setAdapter(adapter);
    }
}

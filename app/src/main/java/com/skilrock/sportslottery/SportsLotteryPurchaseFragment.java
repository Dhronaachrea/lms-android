package com.skilrock.sportslottery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skilrock.bean.SportsBean;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.Utils;

import java.util.ArrayList;

/**
 * Created by stpl on 10/1/2015.
 */
public class SportsLotteryPurchaseFragment extends Fragment {

    private Context context;

    private ListView lstEvent;
    private SportsLotteryActivity activity;

    private SportsBean.GameTypeData gameTypeData;
    private ArrayList<SportsBean.EventData> dummyData;
    private int adapterRes;
    private String[] eventTypes;
    private ArrayList<SportsBean.EventData> adapterData;
    private SportsLotteryAdapter adapter;

    private double maxLine;
    private int position;

    private double betMulAmount;
    private String lineNo;
    private String amount;
    private boolean isEnable;

    public static String MAX_LINE = "max_line";
    public static String GAME_TYPE_DATA = "game_type_data";
    public static String POSITION = "position";
    private boolean isViewCreated = false;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        this.activity = (SportsLotteryActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sports_lottery_purchase_fragment, container, false);
        lstEvent = (ListView) view.findViewById(R.id.lst_event);
        Bundle arguments = getArguments();
        this.maxLine = arguments.getDouble(MAX_LINE);
        this.gameTypeData = (SportsBean.GameTypeData) arguments.getSerializable(GAME_TYPE_DATA);
        this.position = arguments.getInt(POSITION);
        this.betMulAmount = gameTypeData.getGameTypeUnitPrice();
        prepareData(position);
        isViewCreated = true;
        return view;
    }


    public static Fragment getInstance(SportsBean.GameTypeData gameTypeData, double maxLine, int position) {
        Fragment fragment = new SportsLotteryPurchaseFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(MAX_LINE, maxLine);
        bundle.putSerializable(GAME_TYPE_DATA, gameTypeData);
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isViewCreated) {
            updateBetMultiple(betMulAmount);
            setTicketAmount(amount);
            setLineNo(lineNo);
            isPurchaseEnable(isEnable);
        }
    }

    private void prepareData(int position) {
        if (gameTypeData.getDrawData().size() > 0) {
            activity.isDrawAvailable(true);
            dummyData = (ArrayList<SportsBean.EventData>) gameTypeData.getDrawData()
                    .get(position).getEventData();
            if (gameTypeData.getEventType().split(",").length == 5) {
                adapterRes = R.layout.sports_row_item_five;
                eventTypes = gameTypeData.getEventType().replace("[", "").replace("]", "").split(",");
            } else if (gameTypeData.getEventType().split(",").length == 3) {
                adapterRes = R.layout.sports_row_item_three;
                eventTypes = gameTypeData.getEventType().replace("[", "").replace("]", "").split(",");
            } else {
                Utils.Toast(getActivity(), "Wrong event type");
                return;
            }
            setEventTypes();
            adapterData = new ArrayList<SportsBean.EventData>();
            for (int j = 0; j < dummyData.size(); j++) {
                SportsBean.EventData eventData = new SportsBean.EventData();
                eventData.setMinusTwoSelected(false);
                eventData.setMinusOneSelected(false);
                eventData.setHomeSelected(false);
                eventData.setDrawSelected(false);
                eventData.setAwaySelected(false);
                eventData.setPlusOneSelected(false);
                eventData.setPlusTwoSelected(false);
                eventData.setMinusTwoId(j);
                eventData.setMinusOneId(j);
                eventData.setHomeId(j);
                eventData.setDrawId(j);
                eventData.setAwayId(j);
                eventData.setPlusOneId(j);
                eventData.setPlusTwoId(j);
                eventData.setEventDate(dummyData.get(j).getEventDate());
                eventData.setAwayTeamOdds(dummyData.get(j).getAwayTeamOdds());
                eventData.setEventDisplayAway(dummyData.get(j)
                        .getEventDisplayAway());
                eventData.setDrawOdds(dummyData.get(j).getDrawOdds());
                eventData.setHomeTeamOdds(dummyData.get(j).getHomeTeamOdds());
                eventData.setFavTeam(dummyData.get(j).getFavTeam());
                eventData.setEventDisplayHome(dummyData.get(j)
                        .getEventDisplayHome());
                eventData.setEventId(dummyData.get(j).getEventId());
                eventData.setEventLeague(dummyData.get(j).getEventLeague());
                eventData.setEventVenue(dummyData.get(j).getEventVenue());
                adapterData.add(eventData);
            }
            boolean isSingleSelection;
            if (gameTypeData.getEventSelectionType().equalsIgnoreCase("single"))
                isSingleSelection = true;
            else
                isSingleSelection = false;

            adapter = new SportsLotteryAdapter(context, adapterRes,
                    adapterData, this, maxLine, isSingleSelection);
            lstEvent.setAdapter(adapter);
        } else {
            activity.isDrawAvailable(false);

        }
    }


    public void isPurchaseEnable(boolean isEnable) {

        this.isEnable = isEnable;
        if (getUserVisibleHint()) {
            activity.isPurchaseEnable(isEnable);
        }
    }

    public void setTicketAmount(String amount) {

        this.amount = amount;
        if (getUserVisibleHint()) {
            activity.setTicketAmount(amount);
        }
    }

    public void setLineNo(String lineNo) {

        this.lineNo = lineNo;
        if (getUserVisibleHint()) {
            activity.setLineNo(lineNo);
        }
    }

    public void setEventTypes() {
        activity.setEventTypes(eventTypes);
    }

    public ArrayList<SportsBean.EventData> getAdapterData() {
        return adapterData;
    }

    public int getAdapterRes() {
        return adapterRes;
    }

    public void setBetMultiple(double amt) {
        this.betMulAmount = amt;
    }

    private void updateBetMultiple(double betMulAmount) {
        activity.updateBetMultipleAmt(betMulAmount);
    }
}

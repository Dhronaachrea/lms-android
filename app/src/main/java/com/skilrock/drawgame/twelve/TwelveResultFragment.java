package com.skilrock.drawgame.twelve;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.adapters.FiveDrawResAdapter;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.bean.TwelveByTwentyFourResulBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class TwelveResultFragment extends Fragment {

    private Analytics analytics;

    private View mainView;
    private final TwelveByTwentyFourResulBean resultBean;
    private ArrayList<IconWithTitle> mainMenuData;
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS", "RAFFEL\nCHECKER", "NEW\nDATA"};
    private int actionBarHeight;
    private int tabBarHeight;
    private int images[] = new int[]{R.drawable.draw, R.drawable.sports,
            R.drawable.escratch, R.drawable.scratch};
    private CustomTextView drawName, drawTime;
    private ListView itemList;
    private double gridWeight = 0.5;// 10%5
    private int height;
    private int width;
    private double heightForGridChild;
    private int widthForGridChild;
    private RelativeLayout dataLay;
    private String[] winNumber;
    private TextView amount, winner;
    private Activity activity;
    private GlobalPref globalPref;

    public TwelveResultFragment(TwelveByTwentyFourResulBean resultBean) {
        this.resultBean = resultBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.TWELVE_GAME);
        analytics.sendAll(Fields.Category.UX, Fields.Action.OPEN, Fields.Label.TWELVE_GAME_RESULT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainMenuData = new ArrayList<>();
        for (int i = 0; i < titlesMenu.length; i++) {
            IconWithTitle iconWithTitle = new IconWithTitle(titlesMenu[i],
                    images[i]);
            mainMenuData.add(iconWithTitle);
        }
        mainView = inflater.inflate(R.layout.activity_twelve_result_fragment, null);
        bindViewIds(mainView);
        getDisplayDetails();
        String time = resultBean.getDrawTime();
        String newTime = time.split(":")[0] + ":" + time.split(":")[1];
        drawName.setText(resultBean.getDrawDate() + " " + newTime);
        drawTime.setText(resultBean.getDrawDate());
        itemList.setAdapter(new FiveDrawResAdapter(getActivity(),
                R.layout.last_draw_res_list_row, resultBean.getMatch(),
                resultBean.getWinner(), resultBean.getAmount()));
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // setResultsData(position);
            }
        });
        return mainView;

    }

    private void getDisplayDetails() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        actionBarHeight = (int) (50 * getResources().getDisplayMetrics().density);
        tabBarHeight = (int) getResources().getDimension(R.dimen.tabBarHeight);
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        height = displaymetrics.heightPixels - result - actionBarHeight
                - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Utils.logPrint("data:" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }


    private void bindViewIds(View view) {
        itemList = (ListView) mainView.findViewById(R.id.item_grid);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);
        drawName = (CustomTextView) mainView.findViewById(R.id.draw_name);
        drawTime = (CustomTextView) mainView.findViewById(R.id.draw_time);
        LinearLayout winNoLay = (LinearLayout) mainView
                .findViewById(R.id.win_num_lay);
        LinearLayout winNoLay1 = (LinearLayout) mainView
                .findViewById(R.id.win_num_lay1);
        LinearLayout macNoLay = (LinearLayout) mainView
                .findViewById(R.id.mac_num_lay);
        winNumber = resultBean.getWinningNo();
        //  macNumber = new int[5];
        for (int i = 0; i < winNoLay.getChildCount(); i++) {
            CustomTextView textViewWin = (CustomTextView) winNoLay.getChildAt(i);
            //CustomTextView textViewMac = (CustomTextView) macNoLay.getChildAt(i);
            textViewWin.setText(winNumber[i] + "");
            //textViewMac.setText(macNumber[i] + "");
        }
        for (int i = 0; i < winNoLay1.getChildCount(); i++) {
            CustomTextView textViewWin = (CustomTextView) winNoLay1.getChildAt(i);
            //CustomTextView textViewMac = (CustomTextView) macNoLay.getChildAt(i);
            textViewWin.setText(winNumber[6 + i] + "");
            //textViewMac.setText(macNumber[i] + "");
        }
        amount = (TextView) view.findViewById(R.id.amount);
        amount.setText("Amount" + " " + "(" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.CURRENCY_CODE) + ")");
        winner = (TextView) view.findViewById(R.id.winner);
        if (globalPref.getCountry().equalsIgnoreCase("lagos"))
            winner.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }
}

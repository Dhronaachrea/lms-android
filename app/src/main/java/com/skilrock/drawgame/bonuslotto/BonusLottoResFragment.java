package com.skilrock.drawgame.bonuslotto;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.adapters.LastDrawResAdapter;
import com.skilrock.bean.BonusLottoResultBean;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.utils.Utils;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class BonusLottoResFragment extends Fragment {
    private Analytics analytics;
    private View mainView;
    private String[] winNumber;
    // private JazzyViewPager myPager;
    private ListView itemList;
    private CustomTextView drawName;
    private int height;
    private int width;
    private double heightForGridChild;
    private int widthForGridChild;
    private double gridWeight = 0.5;// 10%5
    private int actionBarHeight;
    private int tabBarHeight;
    private int images[] = new int[]{R.drawable.draw, R.drawable.sports,
            R.drawable.escratch, R.drawable.scratch};
    private String[] titlesMenu = new String[]{"STATISTICS\n ",
            "CHECK\nRESULTS", "RAFFEL\nCHECKER", "NEW\nDATA"};
    private ArrayList<IconWithTitle> mainMenuData;
    private RelativeLayout dataLay;

    private BonusLottoResultBean resultBean;
    private TextView amount;
    private Activity activity;

    public BonusLottoResFragment(BonusLottoResultBean resultBean) {
        this.resultBean = resultBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.BONUS_GAME);
        analytics.sendAll(Fields.Category.UX, Fields.Action.OPEN, Fields.Label.BONUS_GAME_RESULT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainMenuData = new ArrayList<IconWithTitle>();
        for (int i = 0; i < titlesMenu.length; i++) {
            IconWithTitle iconWithTitle = new IconWithTitle(titlesMenu[i],
                    images[i]);
            mainMenuData.add(iconWithTitle);
        }
        mainView = inflater.inflate(R.layout.bonus_lotto_res_frag, null);
        bindViewIds(mainView);
        getDisplayDetails();
        String time = resultBean.getDrawTime();
        String newTime = time.split(":")[0] + ":" + time.split(":")[1];
        drawName.setText(resultBean.getDrawDate() + " " + newTime);
        itemList.setAdapter(new LastDrawResAdapter(getActivity(),
                R.layout.last_draw_res_list_row, resultBean, 1));
        itemList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // setResultsData(position);
            }
        });
        return mainView;
    }

    OnClickListener commonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.one:
                    break;
                default:
                    break;
            }

        }
    };

    private void bindViewIds(View view) {
        itemList = (ListView) mainView.findViewById(R.id.item_grid);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);
        drawName = (CustomTextView) mainView.findViewById(R.id.draw_name);
        LinearLayout winNoLay = (LinearLayout) mainView
                .findViewById(R.id.win_num_lay);
        winNumber = resultBean.getWinningNo();
        for (int i = 0; i < winNoLay.getChildCount(); i++) {
            CustomTextView textViewWin = (CustomTextView) winNoLay.getChildAt(i);

            try {
                if ((winNumber.length - 1) < i) {
                    textViewWin.setVisibility(View.GONE);
                } else if (i == winNoLay.getChildCount() - 1) {
                    textViewWin.setText(resultBean.getBonusNo());
                } else {
                    textViewWin.setText(winNumber[i] + "");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                textViewWin.setVisibility(View.GONE);
            }
        }

        amount = (TextView) view.findViewById(R.id.amount);
        amount.setText("Amount" + " " + "(" + VariableStorage.GlobalPref.getStringData(

                getActivity(), VariableStorage

                        .GlobalPref.CURRENCY_CODE) + ")");
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
        Utils.logPrint("data:-" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}

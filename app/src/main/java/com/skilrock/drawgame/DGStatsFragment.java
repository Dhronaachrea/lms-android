package com.skilrock.drawgame;

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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.skilrock.adapters.LastDrawStatsAdapter;
import com.skilrock.bean.CommonStatsBean;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.customui.CustomTextView;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class DGStatsFragment extends Fragment {
    private View mainView;
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
    private CommonStatsBean resultBean;
    private RelativeLayout dummyShow;
    private String gameId;
    private Activity activity;

//    public DGStatsFragment(CommonStatsBean resultBean, String gameId) {
//        this.resultBean = resultBean;
//        this.gameId = gameId;
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resultBean = (CommonStatsBean) getArguments().getSerializable(Keys.RESULT_BEAN.name());
        gameId = getArguments().getString(Keys.GAME_ID.name());

        mainMenuData = new ArrayList<IconWithTitle>();
        for (int i = 0; i < titlesMenu.length; i++) {
            IconWithTitle iconWithTitle = new IconWithTitle(titlesMenu[i],
                    images[i]);
            mainMenuData.add(iconWithTitle);
        }
        mainView = inflater.inflate(R.layout.common_stats_frag, null);
        bindViewIds(mainView);
        getDisplayDetails();
        // drawName.setText(resultBean.getStatsType());
        itemList.setAdapter(new LastDrawStatsAdapter(activity,
                R.layout.last_draw_stats_list_row, resultBean, gameId));
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
        dummyShow = (RelativeLayout) mainView.findViewById(R.id.show);
        // drawName = (CustomTextView) mainView.findViewById(R.id.draw_name);
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
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        height = displaymetrics.heightPixels - result - actionBarHeight
                - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Utils.logPrint("data-" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    public enum Keys {
        RESULT_BEAN, GAME_ID
    }

}

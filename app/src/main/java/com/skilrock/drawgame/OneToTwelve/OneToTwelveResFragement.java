package com.skilrock.drawgame.OneToTwelve;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skilrock.adapters.FastDrawResAdapter;
import com.skilrock.bean.FastLottoResultBean;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.GlobalVariables;

import java.util.ArrayList;


public class OneToTwelveResFragement extends Fragment {
    private Analytics analytics;
    private View mainView;
    // private JazzyViewPager myPager;
    private ListView itemList;
    private CustomTextView drawName;
    private CustomTextView drawTime;
    private CustomTextView fastNum;
    private CustomTextView fastText;
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
    private FastLottoResultBean resultBean;
    private Activity activity;
    private TextView winner;
    private GlobalPref globalPref;

    public OneToTwelveResFragement(FastLottoResultBean resultBean) {
        this.resultBean = resultBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = GlobalPref.getInstance(activity);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.FAST_GAME);
        analytics.sendAll(Fields.Category.UX, Fields.Action.OPEN, Fields.Label.FAST_GAME_RESULT);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
        mainView = inflater.inflate(R.layout.fragment_one_to_twelve_res_fragement, null);
        bindViewIds(mainView);
        getDisplayDetails();
        drawName.setText(resultBean.getDrawName());
        itemList.setAdapter(new FastDrawResAdapter(getActivity(),
                R.layout.fast_res, resultBean));
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // setResultsData(position);
            }
        });
        return mainView;
    }

    View.OnClickListener commonClickListener = new View.OnClickListener() {

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
    private int deviceDpi;

    private void bindViewIds(View view) {
        itemList = (ListView) mainView.findViewById(R.id.item_grid);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);
        drawName = (CustomTextView) mainView.findViewById(R.id.draw_name);
        drawTime = (CustomTextView) mainView.findViewById(R.id.draw_time);
        fastNum = (CustomTextView) mainView.findViewById(R.id.fast_res_num_text);
        fastText = (CustomTextView) mainView.findViewById(R.id.fast_res_text);
        winner = (TextView) view.findViewById(R.id.winner);
        if (globalPref.getCountry().equalsIgnoreCase("lagos"))
            winner.setVisibility(View.GONE);
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
        deviceDpi = displaymetrics.densityDpi;
        if (GlobalVariables.onTablet(getActivity())) {
            fastNum.setTextSize(55);
            fastText.setTextSize(38);
        } else {
            switch (deviceDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    fastNum.setTextSize(28);
                    fastText.setTextSize(24);
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    fastNum.setTextSize(32);
                    fastText.setTextSize(28);
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    fastNum.setTextSize(40);
                    fastText.setTextSize(32);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    fastNum.setTextSize(50);
                    fastText.setTextSize(36);
                    break;

                default:
                    break;
            }
        }

        height = displaymetrics.heightPixels - result - actionBarHeight
                - tabBarHeight;
        widthForGridChild = width = displaymetrics.widthPixels;
        heightForGridChild = (height * gridWeight);
        Log.i("data", height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }
}

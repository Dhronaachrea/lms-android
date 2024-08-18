package com.skilrock.drawgame.bonusLotto_keno_SixThirtySix;

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
import com.skilrock.bean.FiveByNineResultBean;
import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import java.util.ArrayList;

/**
 * Created by stpl on 8/12/2016.
 */
public class BonusKenoResult extends Fragment {
    private Analytics analytics;
    private View mainView;
    // private JazzyViewPager myPager;
    private ListView itemList;
    private String[] winNumber;

    //private String[] macNumber;
    private CustomTextView drawName, drawTime;
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
    private FiveByNineResultBean resultBean;
    private TextView amount, winner;
    private Activity activity;

    //for Lagos
    private String[] winNumberMechine;


    public BonusKenoResult(FiveByNineResultBean resultBean) {
        this.resultBean = resultBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new Analytics();
        analytics.startAnalytics(getActivity());
        analytics.setScreenName(Fields.Screen.FIVE_GAME);
        analytics.sendAll(Fields.Category.UX, Fields.Action.OPEN, Fields.Label.FIVE_GAME_RESULT);
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
        mainView = inflater.inflate(R.layout.bonus_keno_res_frag, null);
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

    private void bindViewIds(View view) {
        itemList = (ListView) mainView.findViewById(R.id.item_grid);
        dataLay = (RelativeLayout) mainView.findViewById(R.id.data_lay);
        drawName = (CustomTextView) mainView.findViewById(R.id.draw_name);
        drawTime = (CustomTextView) mainView.findViewById(R.id.draw_time);
        winner = (TextView) mainView.findViewById(R.id.winner);
        if (GlobalPref.getInstance(activity).getCountry().equalsIgnoreCase("lagos"))
            winner.setVisibility(View.GONE);


        LinearLayout winNoLay = (LinearLayout) mainView
                .findViewById(R.id.win_num_lay);
        LinearLayout macNoLay = (LinearLayout) mainView
                .findViewById(R.id.mac_num_lay);
        winNumber = resultBean.getWinningNo();
        //  macNumber = new int[5];
        if (winNumber.length > 1) {
            for (int i = 0; i < winNoLay.getChildCount(); i++) {
                CustomTextView textViewWin = (CustomTextView) winNoLay.getChildAt(i);
                //CustomTextView textViewMac = (CustomTextView) macNoLay.getChildAt(i);
                textViewWin.setText(winNumber[i] + "");
                //textViewMac.setText(macNumber[i] + "");
            }
        } else {
            winNoLay.setVisibility(View.GONE);
        }
        //for Lagos
        CustomTextView mechineText;
        LinearLayout mechWinNumLay;
        mechineText = (CustomTextView) mainView.findViewById(R.id.mechine_text);
        mechWinNumLay = (LinearLayout) mainView.findViewById(R.id.mech_win_num_lay);

        if (resultBean.getMachineNo().length == 0) {
            mechineText.setVisibility(View.GONE);
            mechWinNumLay.setVisibility(View.GONE);
        } else {
            mechineText.setVisibility(View.VISIBLE);
            mechWinNumLay.setVisibility(View.VISIBLE);
            winNumberMechine = resultBean.getMachineNo();
            for (int i = 0; i < mechWinNumLay.getChildCount(); i++) {
                CustomTextView textViewWin = (CustomTextView) mechWinNumLay.getChildAt(i);
                //CustomTextView textViewMac = (CustomTextView) macNoLay.getChildAt(i);
                textViewWin.setText(winNumberMechine[i] + "");
                //textViewMac.setText(macNumber[i] + "");
            }
        }


        amount = (TextView) view.findViewById(R.id.amount);
        amount.setText("Amount" + " " + "(" + VariableStorage.GlobalPref.getStringData(getActivity(), VariableStorage.GlobalPref.CURRENCY_CODE) + ")");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
        Utils.logPrint("data-" + height + " " + width + " " + heightForGridChild + " "
                + widthForGridChild);
    }

    @Override
    public void onStop() {
        super.onStop();
        analytics.endAnalytics(getActivity());
    }

    // private void setResultsData(int pos) {
    // gameInfos = new ArrayList<GameInfo>();
    // GameInfo gameInfo = null;
    // switch (pos) {
    // case 0:
    // gameInfo = new GameInfo("Draw Games", getResources().getColor(
    // R.color.draw_theme_color),
    // GlobalVariables.GamesData.subGamesDG,
    // GlobalVariables.GamesData.bannersDG);
    // break;
    // case 1:
    // gameInfo = new GameInfo("Sports Lottery", getResources().getColor(
    // R.color.sports_theme_color),
    // GlobalVariables.GamesData.subGamesSL,
    // GlobalVariables.GamesData.bannersSL);
    // break;
    // case 2:
    // gameInfo = new GameInfo("Electronic Scratch", getResources()
    // .getColor(R.color.elec_theme_color),
    // GlobalVariables.GamesData.subGamesES,
    // GlobalVariables.GamesData.bannersES);
    // break;
    // case 3:
    // gameInfo = new GameInfo("Scratch Cards", getResources().getColor(
    // R.color.scratch_theme_color),
    // GlobalVariables.GamesData.subGamesSC,
    // GlobalVariables.GamesData.bannersSC);
    // break;
    //
    // default:
    // break;
    // }
    // gameInfos.add(gameInfo);
    // GlobalVariables.replaceFragment(getSupportFragmentManager(),
    // new GameDescription(), "GameDescription");
    // }
}

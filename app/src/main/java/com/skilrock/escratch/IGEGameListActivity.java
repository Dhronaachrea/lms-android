package com.skilrock.escratch;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.skilrock.config.Config;
import com.skilrock.config.UserInfo;
import com.skilrock.customui.DownloadDialogBox;
import com.skilrock.customui.DrawerBaseActivity;
import com.skilrock.escratch.adapter.ViewPagerAdapter;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.escratch.customui.IGEPurchaseDialog;
import com.skilrock.escratch.customui.SlidingTabLayout;
import com.skilrock.escratch.util.IGEArrangGameList;
import com.skilrock.lms.communication.IGETask;
import com.skilrock.lms.communication.WebServicesListener;
import com.skilrock.lms.tracker.Analytics;
import com.skilrock.lms.tracker.Fields;
import com.skilrock.lms.ui.MainScreen;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.skilrock.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * @author Abhishek Dubey
 */
public class IGEGameListActivity extends DrawerBaseActivity implements WebServicesListener {
    // Declaring View and Variables
    private static LinkedHashMap<String, ArrayList<GameListDataBean.Games>> allGameList;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private Spinner spinner;
    private IGEPurchaseDialog custPurchaseFrag;
    private LotteryPreferences lotteryPreferences;
    public static int actionBarHeight, deviceHeight, deviceWidth;
    public static IGEUnfinishGameData gameListDataBean;
    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ige_activity_gamelist);
        analytics = new Analytics();
        analytics.startAnalytics(this);
        analytics.setScreenName(Fields.Screen.INSTANT_GAME);
        analytics.sendAction(Fields.Category.UX, Fields.Action.OPEN);
        sHeader();
        setDrawerItems();
        bindViewIds();
        lotteryPreferences = new LotteryPreferences(IGEGameListActivity.this);
        manageHeader();
        getDisplayDetails(IGEGameListActivity.this);
        headerText.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
        headerText.setText(getString(R.string.ige_name));
        headerText.setTypeface(null, Typeface.BOLD);
        headerNavigation.setImageResource(R.drawable.back);
        headerNavigation.setOnTouchListener(null);
        headerNavigation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        if (!getIntent().hasExtra("UNFINISH_GAME_LIST"))
//            new IGETask(IGEGameListActivity.this, "UNFINISH_GAME_LIST", IGEService.prepareUnfinishGameListURL(IGEGameListActivity.this), null, "Loading unfinished games...").execute();
//        else
            analytics.sendAction(Fields.Category.IGE_NEW_GAME, Fields.Action.OPEN);
        new IGETask(IGEGameListActivity.this, "GAME_LIST", IGEService.prepareGameListURL(IGEGameListActivity.this), null, "Loading games...").execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("UNFINISH_GAME_LIST")) {
            headerSubText.setText("Unfinished Games");
            headerSubText.setVisibility(View.VISIBLE);
            analytics.sendAction(Fields.Category.IGE_UNFINISHED_GAME, Fields.Action.OPEN);
            new IGETask(IGEGameListActivity.this, "UNFINISH_GAME_LIST", IGEService.prepareUnfinishGameListURL(IGEGameListActivity.this), null, "Loading unfinished games...").execute();
        } else {
            headerSubText.setVisibility(View.GONE);
        }
    }

    private void bindViewIds() {
        spinner = (Spinner) findViewById(R.id.spinner);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        LinearLayout linearLayout = (LinearLayout) headerlay.getChildAt(2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
    }

    private void manageHeader() {
        locateRetailerScratch.setVisibility(View.GONE);
        headerNavigation.setVisibility(View.VISIBLE);
        headerImage.setVisibility(View.GONE);
        headerSpinner.setVisibility(View.GONE);
        headerText.setVisibility(View.VISIBLE);
        headerSubText.setVisibility(View.GONE);
    }


    public void showCofGamePlayDialog(GameListDataBean.Games games) {
        custPurchaseFrag = new IGEPurchaseDialog(IGEGameListActivity.this, games);
        custPurchaseFrag.show();
    }

    @Override
    public void onResult(String methodType, Object resultData, Dialog dialog) {

        if (resultData != null) {
            switch (methodType) {
                case "GAME_LIST":
                    try {
                        JSONObject jsonObject = new JSONObject(resultData.toString());
                        Gson gson = new Gson();
                        GameListDataBean gameListDataBean = gson.fromJson(jsonObject.toString(), GameListDataBean.class);
                        if (gameListDataBean.getErrorCode() == 0) {
                            allGameList = new IGEArrangGameList(gameListDataBean.getGames()).getArrangGameList();
                            if (allGameList.size() > 0) {
                                ArrayList<String> title = new ArrayList<>(allGameList.keySet());
                                title.add(0, "Home");
                                String titles[] = title.toArray(new String[title.size()]);
                                // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                                adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, titles.length, allGameList, tabs);
                                pager.setAdapter(adapter);
                                // Setting Custom Color for the Scroll bar indicator of the Tab View
                                tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                    @Override
                                    public int getIndicatorColor(int position) {
                                        return getResources().getColor(R.color.txn_cal_month);
                                    }
                                });
                                // Setting the ViewPager For the SlidingTabsLayout
                                tabs.setViewPager(pager);
                            } else {
                                View.OnClickListener okClickListener = new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        IGEGameListActivity.this.finish();
                                    }
                                };
                                new DownloadDialogBox(IGEGameListActivity.this, "Sorry, No Game Available !", "", false, true, okClickListener, null).show();
                            }
                            dialog.dismiss();

                        } else if (gameListDataBean.getErrorCode() == 118) {
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(IGEGameListActivity.this);
                                    Intent intent = new Intent(IGEGameListActivity.this, MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(IGEGameListActivity.this, /*txnBean.getErrorMsg()*/"Time Out. Login Again !", "", false, true, okClickListener, null).show();
                        } else {
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IGEGameListActivity.this.finish();
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(IGEGameListActivity.this, gameListDataBean.getErrorMsg(), "", false, true, okClickListener, null).show();
                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    break;
                case "UNFINISH_GAME_LIST":
                    try {
                        JSONObject jsonObject = new JSONObject(resultData.toString());
                        Gson gson = new Gson();
                        gameListDataBean = gson.fromJson(resultData.toString(), IGEUnfinishGameData.class);


                        if (gameListDataBean.getErrorCode() == 0) {
                            if (jsonObject.getBoolean("isUnfinishGames") && jsonObject.getJSONArray("unfinishedGameList").length() != 0) {
                                // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                                if (gameListDataBean.getUnfinishedGameList().size() != 0) {
                                    adapter = new ViewPagerAdapter(getSupportFragmentManager(), (ArrayList<IGEUnfinishGameData.UnfinishedGameList>) gameListDataBean.getUnfinishedGameList(), tabs);
                                    pager.setAdapter(adapter);
                                    // Setting Custom Color for the Scroll bar indicator of the Tab View
                                    tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                        @Override
                                        public int getIndicatorColor(int position) {
                                            return getResources().getColor(R.color.txn_cal_month);
                                        }
                                    });

                                    // Setting the ViewPager For the SlidingTabsLayout
                                    tabs.setViewPager(pager);
                                } else
                                    finish();

                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                                Utils.Toast(this, "You do not have any unfinished game(s)");
                                finish();
                            }
                        } else if (gameListDataBean.getErrorCode() == 118) {
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UserInfo.setLogout(IGEGameListActivity.this);
                                    Intent intent = new Intent(IGEGameListActivity.this, MainScreen.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(GlobalVariables.startAmin, GlobalVariables.endAmin);
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(IGEGameListActivity.this, /*txnBean.getErrorMsg()*/" Session Time Out. Login Again !", " Oops...", false, true, okClickListener, null).show();
                        } else {
                            View.OnClickListener okClickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    IGEGameListActivity.this.finish();
                                }
                            };
                            dialog.dismiss();
                            new DownloadDialogBox(IGEGameListActivity.this, " " + gameListDataBean.getErrorMsg(), " Oops...", false, true, okClickListener, null).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        View.OnClickListener okClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                IGEGameListActivity.this.finish();
                            }
                        };
                        dialog.dismiss();
                        new DownloadDialogBox(IGEGameListActivity.this, " Some Internal Error !", " Oops...", false, true, okClickListener, null).show();
                    }
                    break;
                default:
                    dialog.dismiss();
                    Utils.Toast(IGEGameListActivity.this, "Some Internal Error !");
            }
        } else if (Config.isStatic && GlobalVariables.loadDummyData) {
            if (dialog != null)
                dialog.dismiss();
            Utils.Toast(this, "Data not available in offline mode");
        } else {
            View.OnClickListener okClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IGEGameListActivity.this.finish();
                }
            };
            dialog.dismiss();
            new DownloadDialogBox(IGEGameListActivity.this, getResources().getString(R.string.net_error), "", false, true, okClickListener, null).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (custPurchaseFrag != null && custPurchaseFrag.isShowing()) {
            custPurchaseFrag.login();
        }
    }

    @Override
    public void finish() {
        super.finish();
        DrawerBaseActivity.selectedItemId = -1;
        overridePendingTransition(GlobalVariables.startAmin,
                GlobalVariables.endAmin);
    }

    private void getDisplayDetails(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        actionBarHeight = (int) mContext.getResources().getDimension(R.dimen.main_header_height);
        deviceHeight = displaymetrics.heightPixels - result - actionBarHeight;
        deviceWidth = displaymetrics.widthPixels;
        Utils.logPrint("deviceHeight :" + deviceHeight + "");
        Utils.logPrint("deviceWidth :" + deviceWidth + "");

        lotteryPreferences.setWIDTH(displaymetrics.widthPixels);
        lotteryPreferences.setHEIGHT(displaymetrics.heightPixels);

    }

    @Override
    protected void onStop() {
        super.onStop();
        analytics.endAnalytics(IGEGameListActivity.this);
    }
}
package com.skilrock.escratch.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.skilrock.escratch.GameListCatFragment;
import com.skilrock.escratch.GameListHomeFragment;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.escratch.customui.SlidingTabLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[]; // This will Store the titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private LinkedHashMap<String, ArrayList<GameListDataBean.Games>> gameMap;
    private ArrayList<IGEUnfinishGameData.UnfinishedGameList> unfinishedGameLists;
    private SlidingTabLayout tabs;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, LinkedHashMap<String, ArrayList<GameListDataBean.Games>> map, SlidingTabLayout tabs) {
        super(fm);

        this.titles = mTitles;
        this.gameMap = map;
        if (gameMap.size() == 1)
            this.NumbOfTabs = 1;
        else
            this.NumbOfTabs = mNumbOfTabsumb;
        this.tabs = tabs;
    }

    public ViewPagerAdapter(FragmentManager fm, ArrayList<IGEUnfinishGameData.UnfinishedGameList> unfinishedGameLists, SlidingTabLayout tabs) {
        super(fm);

        this.titles = new String[]{"UNFINISHED"};
        this.unfinishedGameLists = unfinishedGameLists;
        this.NumbOfTabs = 1;
        this.tabs = tabs;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (gameMap != null) {
            if (gameMap.size() == 1) {
                tabs.setVisibility(View.GONE);
                GameListCatFragment gameListCatFragment = new GameListCatFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("gameList", gameMap.get(new String(new StringBuilder(titles[1]))));
                gameListCatFragment.setArguments(bundle);
                return gameListCatFragment;
            } else {
                if (position == 0) // if the position is 0 we are returning the First tab
                {
                    GameListHomeFragment gameListHomeFragment = new GameListHomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gameList", gameMap);
                    bundle.putCharSequenceArray("titles", titles);
                    gameListHomeFragment.setArguments(bundle);

//            gameListHomeFragment.setArguments();
                    return gameListHomeFragment;
                } else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
                {
                    GameListCatFragment gameListCatFragment = new GameListCatFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gameList", gameMap.get(new String(new StringBuilder(titles[position]))));
                    gameListCatFragment.setArguments(bundle);
                    return gameListCatFragment;
                }
            }
        } else {
            tabs.setVisibility(View.GONE);
            GameListCatFragment gameListCatFragment = new GameListCatFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("unFinishGame", unfinishedGameLists);
            gameListCatFragment.setArguments(bundle);
            return gameListCatFragment;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip


    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
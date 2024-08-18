package com.skilrock.escratch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.skilrock.config.Config;
import com.skilrock.escratch.adapter.GameListAdapter;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class GameListCatFragment extends Fragment {

    private ArrayList<GameListDataBean.Games> gamesArrayList;
    private ArrayList<IGEUnfinishGameData.UnfinishedGameList> gameLists;
    private GameListAdapter adapter;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey("gameList"))
            gamesArrayList = (ArrayList<GameListDataBean.Games>) getArguments().getSerializable("gameList");
        else
            gameLists = (ArrayList<IGEUnfinishGameData.UnfinishedGameList>) getArguments().getSerializable("unFinishGame");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ige_fragment_category, container, false);

        GridView listView = (GridView) view.findViewById(R.id.simple_game_list);
        if (gamesArrayList != null) {
            adapter = new GameListAdapter(mActivity, R.layout.ige_single_item, gamesArrayList);
            listView.setOnItemClickListener(getCommonOnItemClickListener());
        } else {
            adapter = new GameListAdapter(mActivity, R.layout.ige_single_item, gameLists);
            listView.setOnItemClickListener(getUnfinishedItemClickListener());
        }
        listView.setAdapter(adapter);


        return view;
    }


    private AdapterView.OnItemClickListener getCommonOnItemClickListener() {
        AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((IGEGameListActivity) mActivity).showCofGamePlayDialog(gamesArrayList.get(position));
            }
        };
        return onClickListener;
    }

    private AdapterView.OnItemClickListener getUnfinishedItemClickListener() {
        AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Config.isStatic || !GlobalVariables.loadDummyData) {
                    Intent intent = new Intent(mActivity, IGEScratchGameActivity.class);
                    intent.putExtra("mode", "UNFINISH");
                    intent.putExtra("games", gameLists.get(position));
                    mActivity.startActivity(intent);
                } else {
                    Utils.Toast(mActivity, "Data not available in offline mode");
                }
//              getActivity().finish();
            }
        };
        return onClickListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
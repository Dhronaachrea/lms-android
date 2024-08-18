package com.skilrock.escratch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.customui.AmountTextView;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class GameListHomeFragment extends Fragment {

    private static LinkedHashMap<String, ArrayList<GameListDataBean.Games>> gamesData;
    private static CharSequence[] titles;
    private int width;
    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity= activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            gamesData = (LinkedHashMap<String, ArrayList<GameListDataBean.Games>>) getArguments().getSerializable("gameList");
            titles = getArguments().getCharSequenceArray("titles");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ige_fragment_home, container, false);
        LinearLayout parentView = (LinearLayout) v.findViewById(R.id.viewParent);
        for (int i = 0; i < (titles.length - 1); i++) {
            parentView.addView(GetRowView(gamesData.get(titles[i + 1])), i);
        }
        return v;
    }

    private View GetRowView(ArrayList<GameListDataBean.Games> gameses) {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ige_gamelist_row, null);
        TextView gameCategory = (TextView) rowView.findViewById(R.id.gameCategoryName);
        gameCategory.setText(gameses.get(0).getGameCategory());
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) rowView.findViewById(R.id.hrzScroll);
        horizontalScrollView.requestDisallowInterceptTouchEvent(false);
        LinearLayout sigleElement = (LinearLayout) rowView.findViewById(R.id.hrzScrollParent);
        for (int i = 0; i < gameses.size(); i++) {
            GameListDataBean.Games games = gameses.get(i);
            LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inf.inflate(R.layout.ige_single_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView gameName = (TextView) view.findViewById(R.id.gameName);
            AmountTextView gamePrice = (AmountTextView) view.findViewById(R.id.gamePrice);
            gameName.setText(games.getGameName());
            gamePrice.setCurrencySymbol(Config.CURRENCY_SYMBOL);
            gamePrice.setText(AmountFormat.getAmountFormatForSingleDecimal(games.getGamePrice()) + "");
            int width = (new LotteryPreferences(mActivity).getWIDTH()) - GlobalVariables.getPx((int) mActivity.getResources().getDimension(R.dimen._34sdp), mActivity);
            Picasso.with(mActivity).load(games.getGameImageLocations().getImagePath()).resize(width / 3, width / 3).placeholder(R.drawable.placeholder).into(imageView);
            view.setOnClickListener(getCommonClickListener(games));
            sigleElement.addView(view, i);
        }
        return rowView;
    }

    private View.OnClickListener getCommonClickListener(final GameListDataBean.Games games) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IGEGameListActivity) mActivity).showCofGamePlayDialog(games);

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

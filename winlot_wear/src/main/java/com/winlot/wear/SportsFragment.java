package com.winlot.wear;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.skilrock.lms.ui.R;

/**
 * Created by stpl on 9/24/2015.
 */
public class SportsFragment extends Fragment {
    private RobotoTextView homeTV, awayTV;
    private RadioGroup radioGroup;
    private int posotion;
    private SLEBean sleBean;
    private boolean isAllSelected;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sports_fragment, null);
        sleBean = SLEBean.getInstance();
        bindViewIds(view);
        Bundle bundle = getArguments();
        homeTV.setText(bundle.getString("home"));
        awayTV.setText(bundle.getString("away"));
        posotion = bundle.getInt("pos");
//        if (sleBean.getSelected(posotion) != null)
//            ((SquareCheckBox) radioGroup.getChildAt(sleBean.getSelected(posotion))).setChecked(true);
        radioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        sleBean.setSelected(posotion, i);
                        for (int j = 0; j < sleBean.getSelected().length; j++) {
                            if (sleBean.getSelected()[j] == 0) {
                                isAllSelected = false;
                                break;
                            } else {
                                isAllSelected = true;
                            }
                        }
                        if (isAllSelected) {
                            SportsActivity.done.setVisibility(View.VISIBLE);
                        } else {
                            SportsActivity.done.setVisibility(View.GONE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (posotion == 3) {
                                    SportsActivity.viewPager.setCurrentItem(0);
                                } else {
                                    SportsActivity.viewPager.setCurrentItem(posotion + 1);
                                }
                            }
                        }, 500);
                    }
                });
        return view;
    }


    private void bindViewIds(View view) {
        homeTV = (RobotoTextView) view.findViewById(R.id.home);
        awayTV = (RobotoTextView) view.findViewById(R.id.away);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
    }
}

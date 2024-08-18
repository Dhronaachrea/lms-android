package com.winlot.wear;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.Gravity;

import com.skilrock.lms.ui.R;

public class ScratchView extends Activity {
    private String titles[] = new String[]{"Big Game Hunt", "Bumper Harvest", "Go O Bhora", "New Play Play Play"};
    private String descs[] = new String[]{"Match 3 same symbol in a game to win prize", "match 3 crops to win\n", "Find your no. greater than dealer's no. & win the prize", "Match 3 same symbol in a game to win prize"};
    private int[] drawables = new int[]{R.drawable.s_one, R.drawable.s_two, R.drawable.s_three, R.drawable.s_four};
    private int[] drawablesBG = new int[]{R.drawable.l_one, R.drawable.l_two, R.drawable.l_three, R.drawable.l_four};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_view);
        final GridViewPager pager = (GridViewPager) findViewById(R.id.grid_view_pager);
        pager.setBackgroundResource(drawablesBG[0]);
        pager.setOnPageChangeListener(new GridViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, int i1, float v, float v1, int i2, int i3) {

            }

            @Override
            public void onPageSelected(int i, int i1) {
                pager.setBackgroundResource(drawablesBG[i1]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        MyPagerAdapter pageAdapter = new MyPagerAdapter(getFragmentManager());
        pager.setAdapter(pageAdapter);
    }


    public class MyPagerAdapter extends FragmentGridPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.app.Fragment getFragment(int i, int i1) {
            CardFragment fragment = CardFragment.create(titles[i1], descs[i1], drawables[i1]);
            fragment.setCardGravity(Gravity.BOTTOM);
            fragment.setExpansionEnabled(true);
            return fragment;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return drawables.length;
        }


    }
}

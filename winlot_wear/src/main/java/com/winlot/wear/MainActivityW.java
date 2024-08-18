package com.winlot.wear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import com.skilrock.lms.ui.R;

public class MainActivityW extends Activity implements WearableListView.ClickListener {
    private WearableListView listView;
    private String[] title = new String[]{"Instant Games", "Sports Lottery", "Physical Scratch", "Locate Retailers"};
    public static int[] icons = new int[]{R.drawable.e_scratch, R.drawable.sports_lottery, R.drawable.m_scratch, R.drawable.locate_retailer};
    public static int[] iconsBig = new int[]{R.drawable.e_scratch_b, R.drawable.sports_lottery_b, R.drawable.m_scratch_b, R.drawable.locate_retailer};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_w);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                listView = (WearableListView) findViewById(R.id.wearable_list);
                listView.setAdapter(new Adapter(MainActivityW.this, title, iconsBig));
                listView.setClickListener(MainActivityW.this);
            }
        });

    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        int pos = (int) viewHolder.itemView.getTag();
        switch (pos) {
            case 0:
                startActivity(new Intent(getApplicationContext(), InstantGame.class));
                break;
            case 1:
                startActivity(new Intent(getApplicationContext(), SportsActivity.class));
                break;
            case 2:
                startActivity(new Intent(getApplicationContext(), ScratchView.class));
                break;
            case 3:
                startActivity(new Intent(getApplicationContext(), RetailerActivity.class));
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
}

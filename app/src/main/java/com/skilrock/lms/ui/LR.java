package com.skilrock.lms.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.skilrock.adapters.ExpandableListAdapter;
import com.skilrock.bean.MPowerBean;
import com.skilrock.customui.AnimatedExpandableListView;

import java.util.ArrayList;

public class LR extends ActionBarActivity {
	private ArrayList<MPowerBean> mPowerBeans;
	private int lastExpandedPosition = -1;
	private AnimatedExpandableListView view;
	private String[] bankModes = new String[] { "Account", "Bank",
			"Mobile Wallet" };
	private String[] bankModesDesc = new String[] {
			"Withdrawal through your account",
			"Use yor bank details for withdrawal",
			"Use mobile wallert in this option " };
	private int[] icons = new int[] { R.drawable.draw,
                                      R.drawable.escratch,
			                          R.drawable.sports };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lr);
		view = ((AnimatedExpandableListView) findViewById(R.id.exp_list));
		view.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (view.isGroupExpanded(groupPosition)) {
					// view.collapseGroupWithAnimation(groupPosition);
				} else {
					// view.collapseGroupWithAnimation(lastExpandedPosition);
					view.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}
		});

		view.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				if (lastExpandedPosition != -1
						&& groupPosition != lastExpandedPosition) {
					view.collapseGroupWithAnimation(lastExpandedPosition);
				}
				lastExpandedPosition = groupPosition;
			}
		});
		mPowerBeans = new ArrayList<MPowerBean>();
		for (int i = 0; i < bankModes.length; i++) {
			MPowerBean bean = new MPowerBean(icons[i], bankModes[i],
					bankModesDesc[i]);
			mPowerBeans.add(bean);
		}

		view.setAdapter(new ExpandableListAdapter(getApplicationContext(),
				mPowerBeans));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lr, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

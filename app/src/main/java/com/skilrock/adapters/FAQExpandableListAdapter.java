package com.skilrock.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skilrock.lms.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class FAQExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<String> expandableListTitle;
	private LinkedHashMap<String, ArrayList<HashMap<String, String>>> expandableListDetail;

	public FAQExpandableListAdapter(
			Context context,
			List<String> expandableListTitle,
			LinkedHashMap<String, ArrayList<HashMap<String, String>>> expandableListDetail) {
		this.context = context;
		this.expandableListTitle = expandableListTitle;
		this.expandableListDetail = expandableListDetail;
	}

	@Override
	public Object getChild(int listPosition, int expandedListPosition) {
		return this.expandableListDetail.get(
				this.expandableListTitle.get(listPosition)).get(
				expandedListPosition);
	}

	@Override
	public long getChildId(int listPosition, int expandedListPosition) {
		return expandedListPosition;
	}

	@Override
	public View getChildView(int listPosition, final int expandedListPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		@SuppressWarnings("unchecked")
		final String expandedListTextHeader = ((HashMap<String, String>) getChild(
				listPosition, expandedListPosition)).get("Q");
		@SuppressWarnings("unchecked")
		final String expandedListText = ((HashMap<String, String>) getChild(
				listPosition, expandedListPosition)).get("A");

		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.faq_list_item, null);
		}
		LinearLayout questionHeader = (LinearLayout) convertView
				.findViewById(R.id.questionHeader);

		final ImageView imgv = (ImageView) convertView
				.findViewById(R.id.indicator);

		TextView expandedListTextViewHeader = (TextView) convertView
				.findViewById(R.id.expandListItemHeader);

		final TextView expandedListTextView = (TextView) convertView
				.findViewById(R.id.expandedListItem);

		expandedListTextView.setVisibility(View.GONE);

		expandedListTextViewHeader.setText(expandedListTextHeader);
		imgv.setBackgroundResource(R.drawable.ic_action_next_item);

		expandedListTextView.setText(expandedListText);

		// Adding Question header onclick listener
		questionHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (expandedListTextView.getVisibility() == View.VISIBLE) {
					imgv.setBackgroundResource(R.drawable.ic_action_next_item);
					expandedListTextView.setVisibility(View.GONE);

				} else {
					expandedListTextView.setVisibility(View.VISIBLE);
					imgv.setBackgroundResource(R.drawable.ic_action_expand);

				}

			}

		});

		return convertView;

	}

	@Override
	public int getChildrenCount(int listPosition) {
		return this.expandableListDetail.get(
				this.expandableListTitle.get(listPosition)).size();
	}

	@Override
	public Object getGroup(int listPosition) {
		return this.expandableListTitle.get(listPosition);
	}

	@Override
	public int getGroupCount() {
		return this.expandableListTitle.size();
	}

	@Override
	public long getGroupId(int listPosition) {
		return listPosition;
	}

	@Override
	public View getGroupView(int listPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String listTitle = (String) getGroup(listPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.faq_list_group, null);
		}
		TextView listTitleTextView = (TextView) convertView
				.findViewById(R.id.listTitle);
		listTitleTextView.setTypeface(null, Typeface.BOLD);
		listTitleTextView.setText(listTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int listPosition, int expandedListPosition) {
		return true;
	}
}
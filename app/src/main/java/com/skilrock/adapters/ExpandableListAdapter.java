package com.skilrock.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skilrock.bean.MPowerBean;
import com.skilrock.customui.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

public class ExpandableListAdapter extends AnimatedExpandableListAdapter {

	private Context _context;
	private List<MPowerBean> _listDataHeader; // header titles

	// child data in format of header title, child title

	public ExpandableListAdapter(Context context,
			List<MPowerBean> listDataHeader) {
		this._context = context;
		this._listDataHeader = listDataHeader;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return "";
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// @Override
	// public View getChildView(int groupPosition, final int childPosition,
	// boolean isLastChild, View convertView, ViewGroup parent) {}

	// @Override
	// public int getChildrenCount(int groupPosition) {
	// return 1;
	// }

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MPowerBean bean = (MPowerBean) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.expandable_group_lay,
					null);
		}
		ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
		CustomTextView name = (CustomTextView) convertView
				.findViewById(R.id.name);
		CustomTextView desc = (CustomTextView) convertView
				.findViewById(R.id.desc);
		imageView.setImageResource(bean.getIcon());
		name.setText(bean.getMode());
		desc.setText(bean.getDesc());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// final String childText = (String) getChild(groupPosition,
		// childPosition);

		// if (convertView == null) {
		View view = null;
		switch (groupPosition) {
		case 0:
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.mpower_accnt, null);
			// view.findViewById(R.id.lblListItem).setOnClickListener(
			// new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Toast.makeText(_context, "hai One", 1000).show();
			//
			// }
			// });
			break;
		case 1:
			LayoutInflater infalInflater1 = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater1.inflate(R.layout.mpower_bank_wd, null);
			// view.findViewById(R.id.lblListItem).setOnClickListener(
			// new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Toast.makeText(_context, "hai One", 1000).show();
			//
			// }
			// });
			break;
		case 2:
			LayoutInflater infalInflater2 = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater2.inflate(R.layout.mpower_wallet_wd, null);
			// ((Button) view.findViewById(R.id.aa)).setText("Bye LAst");
			break;
		default:
			break;
		}
		view.setPadding(GlobalVariables.getPx(10, _context), 0,
				GlobalVariables.getPx(10, _context),
				GlobalVariables.getPx(10, _context));
		// }

		// TextView txtListChild = (TextView) convertView
		// .findViewById(R.id.lblListItem);
		//
		// txtListChild.setText(childText);
		return view;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

}

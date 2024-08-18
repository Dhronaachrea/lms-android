package com.skilrock.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.DrawerBean;
import com.skilrock.lms.ui.R;

public class DrawerItemAdapater extends ArrayAdapter<DrawerBean> {
	private Context context;
	private Holder holder;
	private ArrayList<DrawerBean> objects;

	public DrawerItemAdapater(Context context, int resource,
			ArrayList<DrawerBean> objects) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		holder = null;
		if (view == null) {
			holder = new Holder();
			view = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.drawer_row, null);
			holder.name = (CustomTextView) view.findViewById(R.id.name);
			holder.line = (CustomTextView) view.findViewById(R.id.login_line);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		DrawerBean modal = objects.get(pos);
		holder.name.setText(modal.getName());
		holder.name.setCompoundDrawables(
				context.getResources().getDrawable(modal.getIcon()), null,
				context.getResources().getDrawable(R.drawable.arrow), null);
		if (pos == getCount() - 1) {
			holder.line.setVisibility(View.GONE);
		} else {
			holder.line.setVisibility(View.VISIBLE);
		}
		return view;
	}

	class Holder {
		private CustomTextView name, line;
		// private CustomTextView login, myAccount, inbox, userSettings, appSettings,
		// inviteFriend, locateRetailer, logout;
	}
}
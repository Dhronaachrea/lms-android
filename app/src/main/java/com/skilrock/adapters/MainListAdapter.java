package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.GlobalVariables;

public class MainListAdapter extends ArrayAdapter<String> {
	private Context context;
	private int resource;
	private String[] objects;

	public MainListAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = (View) ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(resource, null);
			holder.drawerListText = (CustomTextView) convertView
					.findViewById(R.id.drawerListText);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (position == GlobalVariables.selectedPosition) {
			holder.drawerListText.setBackgroundColor(context.getResources()
					.getColor(R.color.blue));
		} else {
			holder.drawerListText.setBackgroundColor(context.getResources()
					.getColor(R.color.white));
		}
		holder.drawerListText.setText(objects[position]);
		return convertView;
	}

	class Holder {
		private CustomTextView drawerListText;
	}
}

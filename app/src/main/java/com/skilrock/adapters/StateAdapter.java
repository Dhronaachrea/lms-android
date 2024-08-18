package com.skilrock.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skilrock.bean.StateModal.StateList;
import com.skilrock.lms.ui.R;

public class StateAdapter extends ArrayAdapter<StateList> {
	private Context context;
	private int resource;
	private List<StateList> objects;

	public StateAdapter(Context context, int resource, List<StateList> objects) {
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
			holder.drawerListText = (TextView) convertView
					.findViewById(R.id.drawerListText);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		// if (position == GlobalVariables.selectedPosition) {
		// holder.drawerListText.setBackgroundColor(context.getResources()
		// .getColor(R.color.blue));
		// } else {
		// holder.drawerListText.setBackgroundColor(context.getResources()
		// .getColor(R.color.white));
		// }
		holder.drawerListText.setText(objects.get(position).getStateName());
		return convertView;
	}

	class Holder {
		private TextView drawerListText;
	}

}

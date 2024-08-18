package com.skilrock.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skilrock.lms.ui.R;

public class MenuHelper extends BaseAdapter {
	private Activity activity;
	private String[] list;
 

	private static LayoutInflater inflater = null;

	public MenuHelper(Activity a, String[] list) {
		this.activity = a;
		this.list = list;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView name = (TextView) vi.findViewById(R.id.item_name);
		// TextView count = (TextView)vi.findViewById(R.id.item_count);

		name.setText(list[position]);
		// if(DataSource.Keno.showCount)
		// {
		// count.setVisibility(View.VISIBLE);
		// count.setText(list[position]);
		// }
		return vi;
	}
}

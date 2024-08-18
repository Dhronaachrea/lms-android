package com.skilrock.lms.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;

import com.skilrock.bean.ContactBean;
import com.skilrock.customui.RobotoTextView;

import java.util.ArrayList;


public class ContactAdapater extends ArrayAdapter<ContactBean> {
	private Context context;
	private Holder holder;
	public static ArrayList<ContactBean> modals;
	public static ArrayList<ContactBean> copyOfModals;

	public ContactAdapater(Context context, int resource,
						   ArrayList<ContactBean> objects) {
		super(context, resource, objects);
		this.context = context;
		modals = objects;
		copyOfModals = modals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return modals.size();
	}

	@Override
	public View getView(int pos, View view, ViewGroup arg2) {
		holder = null;
		if (view == null) {
			holder = new Holder();
			view = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.contact_selection_row, null);
			holder.name = (RobotoTextView) view.findViewById(R.id.con_name);
			holder.number = (RobotoTextView) view.findViewById(R.id.con_number);
			holder.box = (CheckBox) view.findViewById(R.id.check_box);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CheckBox checkBox = (CheckBox) arg0;
				ContactBean ContactBean = (com.skilrock.bean.ContactBean) checkBox.getTag();
				ContactBean.setSelected(checkBox.isChecked());
			}
		});
		ContactBean modal = modals.get(pos);
		holder.name.setText(modal.getName());
		holder.number.setText(modal.getNumber());
		holder.box.setChecked(modal.isSelected());
		holder.box.setTag(modal);
		return view;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				modals = new ArrayList<ContactBean>(results.count);
				modals = (ArrayList<ContactBean>) results.values;
				ContactAdapater.this.notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				ArrayList<ContactBean> beans = getFilterResult(constraint);
				FilterResults results = new FilterResults();
				results.values = beans;
				results.count = beans.size();
				return results;
			}

		};
	}

	private ArrayList<ContactBean> getFilterResult(CharSequence constraint) {
		ArrayList<ContactBean> beans = new ArrayList<ContactBean>();
		for (int i = 0; i < copyOfModals.size(); i++) {
			if (copyOfModals.get(i).getName().toLowerCase()
					.contains(constraint.toString().toLowerCase())) {
				ContactBean bean = copyOfModals.get(i);
				beans.add(bean);
			}
		}
		return beans;
	}

	class Holder {
		RobotoTextView name, number;
		CheckBox box;
	}
}
package com.skilrock.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.skilrock.bean.BetTypeBean;
import com.skilrock.customui.BetDialog;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;
//bet adapter file
public class BetAdapater extends ArrayAdapter<BetTypeBean> {
	private Context context;
	private Holder holder;
	private ArrayList<BetTypeBean> objects;
	private BetDialog betDialog;
	public static ArrayList<BetTypeBean> copyBet;

	public BetAdapater(Context context, int resource,
			ArrayList<BetTypeBean> objects, BetDialog betDialog) {
		super(context, resource, objects);
		this.context = context;
		this.objects = objects;
		this.betDialog = betDialog;
		copyBet = objects;
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
					.inflate(R.layout.bet_row, null);
			holder.name = (CustomTextView) view.findViewById(R.id.bet_name);
			holder.box = (CheckBox) view.findViewById(R.id.check_box);
			holder.parentView = (LinearLayout) view
					.findViewById(R.id.parent_view);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
		holder.box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// CheckBox checkBox = (CheckBox) arg0;
				// DrawData contactBean = (DrawData) checkBox.getTag();
				// contactBean.setSelected(checkBox.isChecked());
			}
		});
		if (pos == BetDialog.selectedPos) {
			holder.parentView.setBackgroundColor(context.getResources()
					.getColor(R.color.bet_selected_color));
			holder.name.setTextColor(context.getResources().getColor(
					R.color.dialog_text_color));
		} else {
			holder.parentView.setBackgroundColor(context.getResources()
					.getColor(R.color.dialog_bg_color));
			holder.name.setTextColor(context.getResources().getColor(
					R.color.dialog_text_color));
		}
		BetTypeBean modal = objects.get(pos);
		holder.name.setText(modal.getBetDisplayName());
		// holder.box.setChecked(modal.isSelected());
		// holder.box.setTag(modal);
		return view;
	}

	class Holder {
		CustomTextView name;
		CheckBox box;
		LinearLayout parentView;
	}

}
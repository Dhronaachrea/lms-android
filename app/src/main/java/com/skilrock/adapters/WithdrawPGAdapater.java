package com.skilrock.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.skilrock.bean.WithdrawalPGBean;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

public class WithdrawPGAdapater extends ArrayAdapter<WithdrawalPGBean> {
	private Context context;
	private ArrayList<WithdrawalPGBean> objects;

	// public static ArrayList<DrawData> drawCopy;

	public WithdrawPGAdapater(Context context, int resource,
							  ArrayList<WithdrawalPGBean> objects) {
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
	public View getView(int pos, View view, final ViewGroup arg2) {
		if (view == null) {
			view = ((LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.expandable_group_lay, null);

		}
		WithdrawalPGBean bean = objects.get(pos);
		ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		CustomTextView name = (CustomTextView) view.findViewById(R.id.name);
		CustomTextView desc = (CustomTextView) view.findViewById(R.id.desc);
		desc.setVisibility(View.GONE);
		name.setText(bean.getBankName());
		switch (bean.getPgKey()) {
			case "TELE_CASH":
				imageView.setImageResource(R.drawable.tele_cash);
				break;
			case "ECO_CASH":
				imageView.setImageResource(R.drawable.eco_cash);
				break;
			case "WINLOT":
				imageView.setImageResource(R.drawable.shop);
				break;
			default:
				imageView.setImageResource(R.drawable.card1);
				break;
		}
		return view;
	}

}
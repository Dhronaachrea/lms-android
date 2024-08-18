package com.skilrock.adapters;//package com.skilrock.adapters;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.skilrock.bean.DrawData;
//import com.skilrock.bean.RetailerFilterBean;
//import com.skilrock.customui.CustomTextView;
//import com.skilrock.customui.CustomTextViewTop;
//import com.skilrock.customui.RetailerFilterDialog;
//import com.skilrock.lottery.R;
//
//public class RetailerFilterAdapater extends ArrayAdapter<DrawData> {
//	public static ArrayList<DrawData> drawCopy;
//	private Context context;
//	private Holder holder;
//	private ArrayList<RetailerFilterBean> objects;
//
//	// public static ArrayList<DrawData> drawCopy;
//
//	public RetailerFilterAdapater(Context context, int resource,
//			ArrayList<RetailerFilterBean> objects,
//			RetailerFilterDialog drawDialog) {
//		super(context, resource, objects);
//		this.context = context;
//		this.objects = objects;
//		drawCopy = objects;
//
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return objects.size();
//	}
//
//	@Override
//	public View getView(int pos, View view, final ViewGroup arg2) {
//		holder = null;
//		if (view == null) {
//			holder = new Holder();
//			view = ((LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//					.inflate(R.layout.draw_row, null);
//			holder.name = (CustomTextView) view.findViewById(R.id.con_name);
//			holder.number = (com.skilrock.customui.CustomTextViewTop) view
//					.findViewById(R.id.con_number);
//			holder.box = (CheckBox) view.findViewById(R.id.check_box);
//			view.setTag(holder);
//		} else {
//			holder = (Holder) view.getTag();
//		}
//		holder.box.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				CheckBox checkBox = (CheckBox) arg0;
//				DrawData contactBean = (DrawData) checkBox.getTag();
//				contactBean.setSelected(checkBox.isChecked());
//				LinearLayout layout = (LinearLayout) ((LinearLayout) ((FrameLayout) ((LinearLayout) arg2
//						.getChildAt(contactBean.getPosition())).getChildAt(0))
//						.getChildAt(0)).getChildAt(1);
//				setSelectedColor(checkBox.isChecked(),
//						(CustomTextView) layout.getChildAt(0));
//				setSelectedColor(checkBox.isChecked(),
//						(CustomTextViewTop) layout.getChildAt(1));
//			}
//		});
//
//		DrawData modal = objects.get(pos);
//		/* Sukesh */
//		// tempOjects.set(pos, modal);
//		/* Sukesh */
//
//		holder.name.setText(modal.getDrawName());
//		holder.number.setText(modal.getDrawDateTime());
//		holder.box.setChecked(modal.isSelected());
//		holder.box.setTag(modal);
//		setSelectedColor(modal.isSelected(), holder.name);
//		setSelectedColor(modal.isSelected(), holder.number);
//		return view;
//	}
//
//	class Holder {
//		CustomTextView name;
//		CustomTextViewTop number;
//		CheckBox box;
//	}
//
//	private void setSelectedColor(boolean isSelected, TextView textView) {
//		if (isSelected) {
//			textView.setTextColor(context.getResources().getColor(
//					R.color.five_color_three));
//			textView.setTextColor(context.getResources().getColor(
//					R.color.five_color_three));
//		} else {
//			textView.setTextColor(context.getResources().getColor(
//					R.color.five_color_one));
//			textView.setTextColor(context.getResources().getColor(
//					R.color.five_color_one));
//
//		}
//	}
// }
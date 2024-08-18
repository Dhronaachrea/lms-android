package com.skilrock.adapters;//package com.skilrock.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//
//import com.skilrock.lottery.R;
//
//public class ServicesAdapter extends BaseAdapter {
//	private int[] data;
//	private Context context;
//
//	public ServicesAdapter(Context context, int[] data) {
//		this.data = data;
//		this.context = context;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return data.length;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View vi = convertView;
//		if (convertView == null)
//			vi = ((LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//					.inflate(R.layout.services_row, null);
//		else
//			vi = (View) convertView;
//
//		final ImageView imageView = (ImageView) vi.findViewById(R.id.image);
//		imageView.setImageResource(data[position]);
//		return vi;
//	}
//
// }
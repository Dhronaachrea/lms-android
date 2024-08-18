package com.skilrock.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skilrock.bean.CategoryInfoBean;
import com.skilrock.bean.DepositLimitBean;
import com.skilrock.customui.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;
import java.util.List;

public class PGAdapter extends AnimatedExpandableListAdapter {


    private Context _context;
    private List<CategoryInfoBean> dataBean;
    ArrayList<DepositLimitBean.PgRange> pgRng;
    private String amount;
    private ImageView imageView;
    private CustomTextView name, desc;

    public PGAdapter(Context context, List<CategoryInfoBean> listDataHeader, ArrayList<DepositLimitBean.PgRange> pgRng/*, String amount*/) {
        this._context = context;
        this.pgRng = pgRng;
        this.dataBean = listDataHeader;
        this.amount = amount;
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
        return this.dataBean.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.dataBean.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        CategoryInfoBean bean = (CategoryInfoBean) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_group_lay,
                    null);
        }
        imageView = (ImageView) convertView.findViewById(R.id.icon);
        name = (CustomTextView) convertView
                .findViewById(R.id.name);
        desc = (CustomTextView) convertView
                .findViewById(R.id.desc);

        imageView.setImageResource(bean.getIcon());
        desc.setVisibility(View.GONE);
        surchargeAmtShow(groupPosition);
        name.setText(bean.getMode());
        return convertView;
    }

    private void surchargeAmtShow(int groupPosition) {
        double compAmt = 0.00;
        if (pgRng.size() > groupPosition) {
            if ((pgRng.get(groupPosition).getSurchargeAmt() > compAmt) && (groupPosition != 4)) {
                desc.setVisibility(View.VISIBLE);
                desc.setText("(+" + pgRng.get(groupPosition).getSurchargeAmt() + "" + _context.getResources().getString(R.string.surch_amt));
            } else {
                desc.setVisibility(View.GONE);
            }
        } else if (pgRng.size() == 1) {
            if ((pgRng.get(0).getSurchargeAmt() > 0) && (groupPosition != 4)) {
                desc.setVisibility(View.VISIBLE);
                desc.setText("(+" + pgRng.get(0).getSurchargeAmt() + "" + _context.getResources().getString(R.string.surch_amt));
            } else {
                desc.setVisibility(View.GONE);
            }
        } else {
            desc.setVisibility(View.GONE);
        }

        if (groupPosition == 4) {
            desc.setVisibility(View.GONE);
        }
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
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
//        LayoutInflater infalInflater = (LayoutInflater) this._context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = infalInflater.inflate(R.layout.pg_deposit, null);
        return view;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }


}

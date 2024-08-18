package com.skilrock.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.skilrock.bean.IconWithTitle;
import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<IconWithTitle> {
    private Context context;
    private int layout;
    private int height;
    private int width;
    private ArrayList<IconWithTitle> data;
    private GridView gridView;
    private int casePos;
    private AbsListView.LayoutParams params;

    public MenuAdapter(Context context, int resource, int height, int width, ArrayList<IconWithTitle> data, GridView gridView, int casePos) {
        super(context, resource, data);
        this.context = context;
        this.casePos = casePos;
        this.gridView = gridView;
        layout = resource;
        this.data = data;
        this.height = height;
        Utils.logPrint("height :" + height + " ");
        this.width = width;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(layout, parent, false);
            if (data.size() == 3 && position == 2) {
                width = width * 2;
            }
            params = new AbsListView.LayoutParams(width, height);
            holder.mainLayout = (RelativeLayout) convertView
                    .findViewById(R.id.grid_child_main);
            holder.mainLayout.setLayoutParams(params);
            holder.mainLayout.setGravity(Gravity.CENTER);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.menu_image);
            holder.newService = (ImageView) convertView.findViewById(R.id.new_service);
            holder.name = (CustomTextView) convertView.findViewById(R.id.menu_name);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        IconWithTitle iconWithTitle = data.get(position);
        holder.image.setImageResource(iconWithTitle.getImageID());
        // holder.image.setColorFilter(Color.parseColor("#044D60"));
        holder.name.setText(iconWithTitle.getTitle());
        if ((iconWithTitle.getItemCode().equals(Config.DG))) {
            holder.newService.setVisibility(View.GONE);
        }
        if ((iconWithTitle.getItemCode().equals(Config.SL))) {
            if (GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("ghana"))
                holder.newService.setVisibility(View.GONE);
        }
        if ((iconWithTitle.getItemCode().equals(Config.IGE)))
            holder.newService.setVisibility(View.GONE);
        return convertView;
    }

    private class Holder {
        CustomTextView name;
        ImageView image, newService;
        RelativeLayout mainLayout;
    }

    private void setCommonPadding(LinearLayout layout, int left, int top,
                                  int right, int bottom) {
        layout.setPadding(left, top, right, bottom);
    }
}
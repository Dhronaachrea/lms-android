package com.skilrock.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.bean.IconWithTitle;
import com.skilrock.customui.CustomTextView;
import com.skilrock.utils.Utils;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;

public class GamePageGridAdapter extends ArrayAdapter<IconWithTitle> {
    private Context context;
    private int layout;
    private int height;
    private int width;
    private ArrayList<IconWithTitle> data;
    private int colorCode;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(layout, null);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                    width, height);
            holder.mainLayout = (LinearLayout) convertView
                    .findViewById(R.id.grid_child_main);
            holder.mainLayout.setGravity(Gravity.CENTER);
            holder.mainLayout.setLayoutParams(params);
            holder.mainLayout.setGravity(Gravity.CENTER);
            switch (position) {
                case 0:
                    setCommonPadding(holder.mainLayout, 0, 0, 1, 1);
                    break;
                case 1:
                    setCommonPadding(holder.mainLayout, 0, 0, 0, 1);
                    break;
                case 2:
                    setCommonPadding(holder.mainLayout, 0, 0, 1, 0);
                    break;
                case 3:
                    setCommonPadding(holder.mainLayout, 0, 0, 0, 0);
                    break;
                default:
                    break;
            }
            holder.image = (ImageView) convertView
                    .findViewById(R.id.menu_image);
            holder.name = (CustomTextView) convertView.findViewById(R.id.menu_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        IconWithTitle iconWithTitle = data.get(position);
        holder.image.setImageResource(iconWithTitle.getImageID());
        holder.image.setColorFilter(colorCode);
        holder.name.setText(iconWithTitle.getTitle());
        return convertView;
    }

    public GamePageGridAdapter(Context context, int resource, int height,
                               int width, ArrayList<IconWithTitle> data, int colorCode) {
        super(context, resource, data);
        this.context = context;
        this.colorCode = colorCode;
        layout = resource;
        this.data = data;
        this.height = height;
        Utils.logPrint("height-" + height + " ");
        this.width = width;

    }

    private class Holder {
        CustomTextView name;
        ImageView image;
        LinearLayout mainLayout;
    }

    private void setCommonPadding(LinearLayout layout, int left, int top,
                                  int right, int bottom) {
        layout.setPadding(left, top, right, bottom);
    }
}
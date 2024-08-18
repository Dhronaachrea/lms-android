package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.skilrock.bean.DrawData;
import com.skilrock.config.Config;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextViewTop;
import com.skilrock.customui.DrawDialog;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;

import java.util.ArrayList;

public class DrawAdapater extends ArrayAdapter<DrawData> {
    public static ArrayList<DrawData> drawCopy;
    private Context context;
    private Holder holder;
    private DrawDialog dialog;
    private ArrayList<DrawData> objects;
    private boolean isGhana = false;

    // public static ArrayList<DrawData> drawCopy;

    public DrawAdapater(Context context, int resource,
                        ArrayList<DrawData> objects, DrawDialog drawDialog) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.dialog = drawDialog;
        drawCopy = objects;
        if (GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("ghana"))
            isGhana = true;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public View getView(int pos, View view, final ViewGroup arg2) {
        holder = null;
        if (view == null) {
            holder = new Holder();
            view = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.draw_row, null);
            holder.name = (CustomTextView) view.findViewById(R.id.con_name);
            holder.number = (com.skilrock.customui.CustomTextViewTop) view
                    .findViewById(R.id.con_number);
            holder.box = (CheckBox) view.findViewById(R.id.check_box);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.box.setOnClickListener(new DrawCheckClickListener(view, pos));
        /*holder.box.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CheckBox checkBox = (CheckBox) arg0;
                DrawData contactBean = (DrawData) checkBox.getTag();
                contactBean.setSelected(checkBox.isChecked());
                LinearLayout layout = (LinearLayout) ((LinearLayout) ((FrameLayout) ((LinearLayout) arg2
                        .getChildAt(contactBean.getPosition())).getChildAt(0))
                        .getChildAt(0)).getChildAt(1);
                setSelectedColor(checkBox.isChecked(),
                        (CustomTextView) layout.getChildAt(0));
                setSelectedColor(checkBox.isChecked(),
                        (CustomTextViewTop) layout.getChildAt(1));
            }
        });*/

        DrawData modal = objects.get(pos);
        /* Sukesh */
        // tempOjects.set(pos, modal);
        /* Sukesh */
        if (!(modal.getDrawName().equalsIgnoreCase("N/A") || modal.getDrawName().equalsIgnoreCase("") || modal.getDrawName().equalsIgnoreCase("null"))) {
            holder.name.setText(modal.getDrawName());
            holder.number.setVisibility(View.VISIBLE);
        } else {
            holder.name.setText(modal.getDrawDateTime());
            holder.number.setVisibility(View.GONE);
        }
//        if (modal.getDrawDateTime().equals("")) {
//            holder.number.setVisibility(View.GONE);
//        } else {
//            holder.number.setVisibility(View.VISIBLE);
//        }
        holder.number.setText(modal.getDrawDateTime());
        holder.box.setChecked(modal.isSelected());
        holder.box.setTag(modal);
        setSelectedColor(modal.isSelected(), holder.name);
        setSelectedColor(modal.isSelected(), holder.number);
        return view;
    }

    class Holder {
        CustomTextView name;
        CustomTextViewTop number;
        CheckBox box;
    }

    private void setSelectedColor(boolean isSelected, TextView textView) {
//        if (isSelected) {
//            textView.setTextColor(context.getResources().getColor(
//                    R.color.five_color_three));
//            textView.setTextColor(context.getResources().getColor(
//                    R.color.five_color_three));
//        } else {
        textView.setTextColor(context.getResources().getColor(
                R.color.dialog_text_color));
//        textView.setTextColor(context.getResources().getColor(
//                R.color.five_color_one));

//        }
    }

    class DrawCheckClickListener implements OnClickListener {
        int position;
        View mView;

        public DrawCheckClickListener(View view, int position) {
            this.position = position;
            this.mView = view;
        }

        @Override
        public void onClick(View v) {
            CustomTextView name = (CustomTextView) mView.findViewById(R.id.con_name);
            com.skilrock.customui.CustomTextViewTop number = (com.skilrock.customui.CustomTextViewTop) mView.findViewById(R.id.con_number);
            CheckBox checkBox = (CheckBox) mView.findViewById(R.id.check_box);
            DrawData contactBean = objects.get(position);
            if (isGhana)
                for (int i = 0; i < objects.size(); i++)
                    objects.get(i).setSelected(false);
            if (isGhana)
                checkBox.setChecked(true);
            contactBean.setSelected(checkBox.isChecked());
            setSelectedColor(checkBox.isChecked(), name);
            setSelectedColor(checkBox.isChecked(), number);
            if (isGhana) {
                notifyDataSetChanged();
                dialog.cancel();
            }
        }
    }
}
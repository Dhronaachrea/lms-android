package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.skilrock.bean.ScratchGameBean;
import com.skilrock.config.Config;
import com.skilrock.config.VariableStorage;
import com.skilrock.customui.CustomTextView;
import com.skilrock.customui.CustomTextView.TextStyles;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ScratchGameAdapter extends ArrayAdapter<ScratchGameBean.ScratchGameData> {
    private Context context;
    private int resource;
    private List<ScratchGameBean.ScratchGameData> objects;

    public ScratchGameAdapter(Context context, int resource,
                              List<ScratchGameBean.ScratchGameData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, null);
            holder.name = (CustomTextView) convertView.findViewById(R.id.title);
            holder.price = (CustomTextView) convertView
                    .findViewById(R.id.duration);
            holder.desc = (CustomTextView) convertView
                    .findViewById(R.id.artist);
            holder.listImg = (ImageView) convertView
                    .findViewById(R.id.list_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTextStyle(TextStyles.BOLD);
        holder.price.setTextStyle(TextStyles.BOLD);
        Picasso.with(context).load(Config.getInstance().getBaseURL() + objects.get(position).getGameImagePath()).into(holder.listImg);
        holder.name.setText(objects.get(position).getGameName());
        try {
            holder.price.setText(VariableStorage.GlobalPref.getStringData(context, VariableStorage.GlobalPref.CURRENCY_CODE)
                    + AmountFormat.getAmountFormatForMobile(objects.get(position).getTicketPrice()) + "");
        } catch (Exception e) {
            holder.price.setText(0.0 + "");
        }
        holder.desc.setText(objects.get(position).getGameDescription());
        return convertView;
    }

    class Holder {
        private CustomTextView name, price, desc;
        private ImageView listImg;
    }

}

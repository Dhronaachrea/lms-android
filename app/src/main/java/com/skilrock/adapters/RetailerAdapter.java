package com.skilrock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.bean.RetailerListModal.RetailerList;
import com.skilrock.customui.CustomTextView;
import com.skilrock.lms.ui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RetailerAdapter extends ArrayAdapter<RetailerList> {
    private Context context;
    private int resource;
    private List<RetailerList> objects;
    private ArrayList<int[]> servicesImage;
    private int[] typeImages;
    private ArrayList<ArrayList<String>> arrayLists;

    public RetailerAdapter(Context context, int resource,
                           List<RetailerList> objects) {
        super(context, resource, objects);
        arrayLists = new ArrayList<ArrayList<String>>();
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        servicesImage = new ArrayList<>();
        typeImages = new int[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            String arr = objects.get(i).getType();
            switch (arr.trim().toUpperCase(Locale.ENGLISH)) {
                case "BO":
                    typeImages[i] = R.drawable.back_office;
                    break;
                case "AGENT":
                    typeImages[i] = R.drawable.agent;
                    break;
                case "RETAILER":
                    typeImages[i] = R.drawable.retailer;
                    break;
            }
        }
        for (int i = 0; i < objects.size(); i++) {
//            String[] arr = objects.get(i).getService();
            int[] intA = new int[objects.get(i).getService().size()];
            for (int j = 0; j < intA.length; j++) {
                switch (objects.get(i).getService().get(j).trim().toUpperCase(Locale.ENGLISH)) {
                    case "TICKET CANCEL":
                        intA[j] = R.drawable.cancel;
                        break;
                    case "MOBILE RECHARGE":
                        intA[j] = R.drawable.mobile_money;
                        break;
                    case "WINNING CLAIM LOW PRIZE UPTO $100":
                        intA[j] = R.drawable.dg_win_claim;
                        break;
                    case "BUY SPORTS LOTTERY":
                        intA[j] = R.drawable.sports_lot;
                        break;
                    case "DEPOSIT":
                        intA[j] = R.drawable.mobile_cash;
                        break;
                    case "BUY SCRATCH CARDS":
                        intA[j] = R.drawable.scratch_sale;
                        break;
                    case "WINNING CLAIM HIGH PRIZE":
                        intA[j] = R.drawable.high_win;
                        break;
                    case "BUY DRAW GAMES":
                        intA[j] = R.drawable.dg_sale;
                        break;
                }
            }
            servicesImage.add(intA);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = (View) ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(resource, null);
            holder.name = (CustomTextView) convertView
                    .findViewById(R.id.ret_name);
            holder.add = (CustomTextView) convertView
                    .findViewById(R.id.ret_add);
            holder.contact = (CustomTextView) convertView
                    .findViewById(R.id.ret_contact);
            holder.servicesView = (LinearLayout) convertView
                    .findViewById(R.id.service_view);
            holder.retType = (ImageView) convertView.findViewById(R.id.ret_type);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RetailerList list = objects.get(position);
        holder.servicesView.removeAllViews();
//        Map map = servicesImage.get(position);
//        Iterator entries = map.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry imageVal = (Map.Entry) entries.next();
//            int key = (Integer) imageVal.getKey(); // image path
//            Boolean value = (Boolean) imageVal.getValue();
//            View vi = ((LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
//                    .inflate(R.layout.services_row, null);
//            ImageView imageView = (ImageView) vi.findViewById(R.id.image);
//            Drawable drawable = resources.getDrawable(key);
//            drawable.setColorFilter(resources.getColor(R.color.disabled_color),
//                    PorterDuff.Mode.SRC_IN);
//            imageView.setImageDrawable(drawable);
//            if (value) {
//                Drawable drawable1 = resources.getDrawable(key);
//                drawable.setColorFilter(Color.GREEN,
//                        PorterDuff.Mode.SRC_IN);
//                imageView.setImageDrawable(drawable1);
//            }
//            holder.servicesView.addView(vi);
//        }

        for (int i = 0; i < servicesImage.get(position).length; i++) {
            View vi = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.services_row, null);
            ImageView imageView = (ImageView) vi.findViewById(R.id.image);
            imageView.setImageResource(servicesImage.get(position)[i]);
            holder.servicesView.addView(vi, i);
        }
        holder.retType.setImageResource(typeImages[position]);
        holder.name.setText(list.getFirstName() + " " + list.getLastName());
        holder.add.setText(list.getAddr_1() + ", " + list.getAddr_2());
        if (list.getPhoneNbr().length() > 1 && list.getMobileNbr().length() > 1) {
            holder.contact.setText(list.getEmail_id() + "\n" + "Phone : "
                    + list.getPhoneNbr() + "\n" + "Mobile : "
                    + list.getMobileNbr());
        } else if (list.getPhoneNbr().length() > 1
                && !(list.getMobileNbr().length() > 1)) {
            holder.contact.setText(list.getEmail_id() + "\n" + "Phone : "
                    + list.getPhoneNbr());
        } else if (!(list.getPhoneNbr().length() > 1)
                && (list.getMobileNbr().length() > 1)) {
            holder.contact.setText(list.getEmail_id() + "\n" + "Mobile : "
                    + list.getMobileNbr());
        } else if (!(list.getPhoneNbr().length() > 1)
                && !(list.getMobileNbr().length() > 1)) {
            holder.contact.setText(list.getEmail_id());
        } else {
            holder.contact.setText(list.getEmail_id());

        }
        return convertView;
    }

    class Holder {
        private CustomTextView name, add, contact;
        private LinearLayout servicesView;
        private ImageView retType;
    }

}

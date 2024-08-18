package com.skilrock.customui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.skilrock.bean.DrawData;
import com.skilrock.bean.RetailerFilterBean;
import com.skilrock.config.FilterDismissListener;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.Utils;

public class RetailerFilterDialog extends AlertDialog {
    private Context context;
    private ImageView close, done;
    public static int count;
    private LinearLayout serviceList, typeList;
    private StringBuffer bufferTypes;
    private String numbers;
    private HashMap<String, String> data;
    private RetailerFilterBean filterBeans;
    private View view;
    private ArrayList<DrawData> tempOjects;
    private FilterDismissListener dismissListener;
    private Activity activity;
    protected StringBuffer bufferServices;

    public RetailerFilterDialog(Context context,
                                RetailerFilterBean filterBeans, Activity activity) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.filterBeans = filterBeans;
        tempOjects = new ArrayList<DrawData>();
        dismissListener = (FilterDismissListener) activity;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void show() {
        super.show();
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.ret_filter_dialog, null);
        serviceList = (LinearLayout) view.findViewById(R.id.service_list);
        typeList = (LinearLayout) view.findViewById(R.id.type_list);
        View headerView = view.findViewById(R.id.header_id);
        done = (ImageView) headerView.findViewById(R.id.done);
        close = (ImageView) headerView.findViewById(R.id.close);
        serviceList.removeAllViews();
        for (int i = 0; i < filterBeans.getServices().size(); i++) {
            View filterRow = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.ret_filter_dilaog_row, null);
            CustomCheckedTextView textView = (CustomCheckedTextView) filterRow
                    .findViewById(R.id.filter_item);
            CustomTextView lineTV = (CustomTextView) filterRow.findViewById(R.id.line);
            if (i == filterBeans.getServices().size() - 1)
                lineTV.setVisibility(View.GONE);
            textView.setChecked(filterBeans.getServicesChecked()[i]);
            textView.setText(filterBeans.getServices().get(i));
            textView.setOnClickListener(getClickListener(textView));
            findServiceResource(textView, i);
            //textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(filterBeans.getServicesRes()[i]), null, null, null);
            serviceList.addView(filterRow, i);
        }
        typeList.removeAllViews();
        for (int i = 0; i < filterBeans.getTypes().size(); i++) {
            View filterRow = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.ret_filter_dilaog_row, null);
            CustomTextView lineTV = (CustomTextView) filterRow.findViewById(R.id.line);
            if (i == filterBeans.getTypes().size() - 1)
                lineTV.setVisibility(View.GONE);
            CustomCheckedTextView textView = (CustomCheckedTextView) filterRow
                    .findViewById(R.id.filter_item);
            textView.setChecked(filterBeans.getTypesChecked()[i]);
            textView.setText(filterBeans.getTypes().get(i));
            textView.setOnClickListener(getClickListener(textView));
            findTypeResource(textView, i);
            //textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(filterBeans.getTypesRes()[i]), null, null, null);
            typeList.addView(filterRow, i);
        }
        this.setContentView(view);
        ((CustomTextView) view.findViewById(R.id.header_name))
                .setText(context.getResources().getString(R.string.fil_by));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissListener.onCancelFilterDiloag(
                        filterBeans.getTypesChecked(),
                        filterBeans.getServicesChecked());
                cancel();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                bufferTypes = new StringBuffer();
                bufferServices = new StringBuffer();
                for (int i = 0; i < serviceList.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) serviceList.getChildAt(i);
                    CustomCheckedTextView view = ((CustomCheckedTextView) linearLayout
                            .getChildAt(0));
                    if (view.isChecked()) {
                        count++;
                        bufferServices.append(view.getText() + ",");
                    }
                }
                for (int i = 0; i < typeList.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) typeList.getChildAt(i);
                    CustomCheckedTextView view = ((CustomCheckedTextView) linearLayout
                            .getChildAt(0));
                    if (view.isChecked()) {
                        count++;
                        bufferTypes.append(view.getText() + ",");
                    }
                }
                if (count > 0) {
                    String[] typesArr;
                    String[] servicesArr;
                    if (bufferTypes.toString().equals("")) {
                        typesArr = new String[]{};
                    } else {
                        typesArr = bufferTypes.toString().split(",");
                    }
                    if (bufferServices.toString().equals("")) {
                        servicesArr = new String[]{};
                    } else {
                        servicesArr = bufferServices.toString().split(",");
                    }
                    dismissListener
                            .onDismissFilterDiloag(typesArr, servicesArr);
                    cancel();
                } else {
                    Utils.Toast(context, context.getResources().getString(R.string.sel_atle_one));
                }
            }
        });
    }

    private void findTypeResource(CustomCheckedTextView textView, int i) {
        switch (filterBeans.getTypes().get(i).trim().toUpperCase(Locale.ENGLISH)) {
            case "RETAILER":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.retailer, 0, 0, 0);
                break;
            case "BO":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back_office, 0, 0, 0);
                break;
            case "AGENT":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.agent, 0, 0, 0);//d
                break;
        }


    }

    private void findServiceResource(CustomCheckedTextView textView, int i) {
        switch (filterBeans.getServices().get(i).trim().toUpperCase(Locale.ENGLISH)) {
            case "TICKET CANCEL":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cancel, 0, 0, 0);//d
                break;
            case "MOBILE RECHARGE":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile_money, 0, 0, 0);
                break;
            case "WINNING CLAIM LOW PRIZE UPTO $100":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dg_win_claim, 0, 0, 0);//d
                break;
            case "BUY SPORTS LOTTERY":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sports_lot, 0, 0, 0);//d
                break;
            case "DEPOSIT":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mobile_cash, 0, 0, 0);
                break;
            case "BUY SCRATCH CARDS":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scratch_sale, 0, 0, 0);//d
                break;
            case "WINNING CLAIM HIGH PRIZE":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.high_win, 0, 0, 0);//d
                break;
            case "BUY DRAW GAMES":
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dg_sale, 0, 0, 0);
                break;
        }

    }

    private View.OnClickListener getClickListener(
            final CustomCheckedTextView textView) {
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (textView.isChecked()) {
                    textView.setChecked(false);
                } else {
                    textView.setChecked(true);
                }
            }
        };
        return clickListener;
    }
}
package com.skilrock.customui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.skilrock.adapters.DrawAdapater;
import com.skilrock.bean.DrawData;
import com.skilrock.config.Config;
import com.skilrock.config.DismissListener;
import com.skilrock.lms.ui.R;
import com.skilrock.preference.GlobalPref;
import com.skilrock.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawDialog extends AlertDialog {
    private Context context;
    private ImageView close, done;
    public static int count;
    private ListView contactList;
    //    private StringBuffer buffer;
    private String numbers;
    private HashMap<String, String> data;
    private DrawAdapater drawAdapater;
    private ArrayList<DrawData> drawDatas;
    private View view;
    private ArrayList<DrawData> tempOjects;
    private DismissListener dismissListener;
    public boolean isDismiss = false;
    private boolean isDrawFreeze;

    public DrawDialog(Context context, ArrayList<DrawData> drawDatas, Fragment fragment, boolean isDrawFreeze) {
        super(context, R.style.DialogZoomAnim);
        this.context = context;
        this.drawDatas = drawDatas;
        tempOjects = new ArrayList<DrawData>();
        dismissListener = (DismissListener) fragment;
        for (int i = 0; i < drawDatas.size(); i++) {
            DrawData org = drawDatas.get(i);
            DrawData newData = new DrawData(org.getDrawId(), org.getDrawName(),
                    org.getDrawFreezeTime(), org.getDrawDateTime(),
                    org.getPosition());
            newData.setSelected(org.isSelected());
            tempOjects.add(newData);
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.isDrawFreeze = isDrawFreeze;
    }

    @Override
    public void show() {
        super.show();
        isDismiss = true;
        this.setCancelable(false);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.draw_dialog, null);
        contactList = (ListView) view.findViewById(R.id.contact_list);
        View headerView = view.findViewById(R.id.header_id);
        done = (ImageView) headerView.findViewById(R.id.done);
        close = (ImageView) headerView.findViewById(R.id.close);
        drawAdapater = new DrawAdapater(context, R.layout.draw_row, drawDatas, this);
        contactList.setAdapter(drawAdapater);
        this.setContentView(view);
        ((CustomTextView) view.findViewById(R.id.header_name))
                .setText(context.getResources().getString(R.string.sel_draw));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
//                buffer = new StringBuffer();
                ArrayList<DrawData> list = DrawAdapater.drawCopy;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        count++;
                    }
                }
                if (count > 0) {
                    isDismiss = false;
                    dismissListener.onDismissCustomDiloag(tempOjects, isDrawFreeze);
                    drawAdapater.notifyDataSetChanged();
                    dismiss();
                } else {
                    if (GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("lagos") || GlobalPref.getInstance(context).getCountry().equalsIgnoreCase("zim")) {
                        dismiss();
                    } else {
                        isDismiss = true;
                        Utils.Toast(context, context.getResources().getString(R.string.sel_atlst_one));
                    }

                }
            }
        });
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                drawSelected();
                drawAdapater.notifyDataSetChanged();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isDismiss) {
                    dismissListener.onDismissCustomDiloag(tempOjects, isDrawFreeze);
                    drawAdapater.notifyDataSetChanged();
                    if (isShowing())
                        dismiss();
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSelected();
            }
        });
    }

    private void drawSelected() {
        count = 0;
//                buffer = new StringBuffer();
        ArrayList<DrawData> list = DrawAdapater.drawCopy;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                count++;
            }
        }
        if (count > 0) {
            isDismiss = false;
            dismissListener.onCancelCustomDiloag();
            cancel();
        } else {
            isDismiss = true;
            Utils.Toast(context, context.getResources().getString(R.string.sel_atlst_one));
        }
    }
    //    @Override
//    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (this.isShowing()) {
//
//                this.cancel();
//            }
//            return true;
//        }
//        return false;
//    }
}